package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.exception.CountryCityMismatchException;
import com.haojie.exception.PhotoInfoIncompleteException;
import com.haojie.exception.TypeIncorrectException;
import com.haojie.others.ActionResult;
import com.haojie.others.UploadPhotoInfo;
import com.haojie.service.ImageService;
import com.haojie.service.UserService;
import com.haojie.utils.PicReduce;
import org.apache.commons.dbutils.DbUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

@MultipartConfig()
@WebServlet("/UserServlet")
/**
 *
 * 处理用户各种请求的servlet
 */

public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getParameter("method").equals("register")) {

                register(request, response);

                return;
            }
            if (request.getParameter("method").equals("login")) {

                login(request, response);

                return;
            }
            if (request.getParameter("method").equals("unfavor")) {
                unfavor(request, response);
                return;

            }
            if (request.getParameter("method").equals("upload")) {
                upload(request, response);
                return;
            }
        } catch (Exception ignored) {

        }


    }

    /**
     * 处理用户的注册请求
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = dataSource.getConnection();
        UserService userService = new UserService(connection, request);
        ActionResult registerResult = userService.register(
                request.getParameter("username"), request.getParameter("email"), request.getParameter("password1"),
                request.getParameter("password2"), request.getParameter("captcha")
        );
        DbUtils.close(connection);


        PrintWriter out = response.getWriter();
        out.println(JSON.toJSON(registerResult));
    }

    /**
     * 处理用户的登录请求
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = dataSource.getConnection();
        UserService userService = new UserService(connection, request);
        ActionResult loginResult = userService.tryLogin(request.getParameter("username"), request.getParameter("password"), request.getParameter("captcha"));
        DbUtils.close(connection);

        PrintWriter out = response.getWriter();
        Object o = JSON.toJSON(loginResult);
        out.println(o);

    }

    /**
     * 处理用户的取消收藏请求
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    private void unfavor(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, httpServletRequest);
            User user = userService.tryAutoLogin();
            ActionResult actionResult = userService.unfavorImage(user, Integer.parseInt(httpServletRequest.getParameter("imageID")));

            PrintWriter out = httpServletResponse.getWriter();
            out.println(JSON.toJSON(actionResult));

        } catch (Exception ignored) {

        }

    }

    private void upload(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            Connection connection = dataSource.getConnection();

            //检查用户是否登录
            UserService userService = new UserService(connection, request);
            User user = userService.tryAutoLogin();
            if (user == null) {
                out.println(new ActionResult(false, "没有登录，或是登录已过期，请重新登录"));
                return;
            }


            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String content = request.getParameter("content");
            String country = request.getParameter("country");
            String city = request.getParameter("city");
            Part photo = request.getPart("photo");

            //得到上传图片信息的封装对象
            UploadPhotoInfo uploadPhotoInfo = new UploadPhotoInfo(title, description, city, country, content, photo);

            //清除xss
            uploadPhotoInfo.cleanXSS();

            //检测图片信息完整性
            try {
                uploadPhotoInfo.checkComplete();
            } catch (PhotoInfoIncompleteException photoInfoIncompleteException) {
                out.println(new ActionResult(false, photoInfoIncompleteException.getMessage()));
                return;
            }

            //检测图片中国家与城市是否对应
            try {
                uploadPhotoInfo.checkCountryAndCity(connection);
            } catch (CountryCityMismatchException countryCityMismatchException) {
                out.println(new ActionResult(false, countryCityMismatchException.getMessage()));
                return;
            }

            //检查图片类型
            try {
                uploadPhotoInfo.checkType();
            } catch (TypeIncorrectException typeIncorrectException) {
                out.println(new ActionResult(false, typeIncorrectException.getMessage()));
                return;
            }

            uploadPhotoInfo.generateFileName(connection);

            ImageService imageService = new ImageService(connection);

            Image image = new Image(
                    uploadPhotoInfo.getTitle(), uploadPhotoInfo.getDescription(), 0, 0, Integer.parseInt(uploadPhotoInfo.getCity()),
                    uploadPhotoInfo.getCountry(), user.getUid(), uploadPhotoInfo.getFilename(), uploadPhotoInfo.getContent(), new Timestamp(System.currentTimeMillis())

            );

            //开启事务
            connection.setAutoCommit(false);

            try {
                ActionResult insertResult = imageService.insertImage(user, image);

                String largeFilePlace = getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename();
                String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename();
                String smallFilePlace = getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename();

                uploadPhotoInfo.getPhoto().write(largeFilePlace);

                PicReduce.saveMinPhoto(largeFilePlace, mediumFilePlace, 800, 1);
                PicReduce.saveMinPhoto(largeFilePlace, smallFilePlace, 200, 1);

                if (!insertResult.isSuccess()) {
                    File largeFile = new File(largeFilePlace);
                    File mediumFile = new File(mediumFilePlace);
                    File smallFile = new File(getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename());
                    largeFile.delete();
                    mediumFile.delete();
                    smallFile.delete();
                    out.println(JSON.toJSON(new ActionResult(false, "上传失败")));
                    return;
                }
            } catch (Exception exception) {
                connection.rollback();
                File largeFile = new File(getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename());
                File mediumFile = new File(getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename());
                File smallFile = new File(getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename());
                largeFile.delete();
                mediumFile.delete();
                smallFile.delete();
                out.println(JSON.toJSON(new ActionResult(false, "上传失败")));
                return;
            }

            connection.commit();
            connection.setAutoCommit(true);
            out.println(JSON.toJSON(new ActionResult(true, "上传成功！")));

            DbUtils.close(connection);

        } catch (Exception e) {

        }


    }

}
