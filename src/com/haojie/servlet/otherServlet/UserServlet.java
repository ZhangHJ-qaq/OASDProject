package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.haojie.bean.Image;
import com.haojie.bean.ImageFavor;
import com.haojie.bean.User;
import com.haojie.dao.ImageFavorDao.ImageFavorDao;
import com.haojie.dao.ImageFavorDao.ImageFavorDaoImpl;
import com.haojie.exception.CountryCityMismatchException;
import com.haojie.exception.PhotoInfoIncompleteException;
import com.haojie.exception.TypeIncorrectException;
import com.haojie.others.ActionResult;
import com.haojie.others.UploadPhotoInfo;
import com.haojie.service.ImageService;
import com.haojie.service.UserService;
import com.haojie.utils.PicReduce;
import org.apache.commons.dbutils.DbUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


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
            if (request.getParameter("method").equals("delete")) {
                delete(request, response);
                return;
            }
            if (request.getParameter("method").equals("modify")) {
                modify(request, response);
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
    private void register(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            ActionResult registerResult = userService.register(
                    request.getParameter("username"), request.getParameter("email"), request.getParameter("password1"),
                    request.getParameter("password2"), request.getParameter("captcha")
            );
            DbUtils.close(connection);


            PrintWriter out = response.getWriter();
            out.println(JSON.toJSON(registerResult));
        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    /**
     * 处理用户的登录请求
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            ActionResult loginResult = userService.tryLogin(request.getParameter("username"), request.getParameter("password"), request.getParameter("captcha"));

            PrintWriter out = response.getWriter();
            Object o = JSON.toJSON(loginResult);
            out.println(o);
        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


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

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void upload(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            //检查用户是否登录
            UserService userService = new UserService(connection, request);
            User user = userService.tryAutoLogin();
            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录，或是登录已过期，请重新登录")));
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
                out.println(JSON.toJSON(new ActionResult(false, photoInfoIncompleteException.getMessage())));
                return;
            }

            //检测图片中国家与城市是否对应
            try {
                uploadPhotoInfo.checkCountryAndCity(connection);
            } catch (CountryCityMismatchException countryCityMismatchException) {
                out.println(JSON.toJSON(new ActionResult(false, countryCityMismatchException.getMessage())));
                return;
            }

            //检查图片类型
            try {
                uploadPhotoInfo.checkType();
            } catch (TypeIncorrectException typeIncorrectException) {
                out.println(JSON.toJSON(new ActionResult(false, typeIncorrectException.getMessage())));
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
                ActionResult insertResult = userService.insertImageToDB(user, image);

                String largeFilePlace = getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename();
                String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename();
                String smallFilePlace = getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename();

                uploadPhotoInfo.getPhoto().write(largeFilePlace);

                PicReduce.saveMinPhoto(largeFilePlace, mediumFilePlace, 800, 1);
                PicReduce.saveMinPhoto(largeFilePlace, smallFilePlace, 200, 1);

                if (!insertResult.isSuccess()) {
                    File largeFile = new File(largeFilePlace);
                    File mediumFile = new File(mediumFilePlace);
                    File smallFile = new File(smallFilePlace);
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


        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            ImageService imageService = new ImageService(connection);
            ImageFavorDao imageFavorDao = new ImageFavorDaoImpl(connection);
            //得到imageID
            int imageID = Integer.parseInt(request.getParameter("imageID"));

            Image image = imageService.getImage(imageID);

            User user = userService.tryAutoLogin();

            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录或登录已过期")));
            }

            connection.setAutoCommit(false);

            ActionResult deleteResult = userService.deleteImageFromDB(user, imageID);
            boolean deleteFavorResult = imageFavorDao.deleteAllImageFavor(imageID);


            if (!deleteResult.isSuccess()) {
                out.println(JSON.toJSON(deleteResult));
                return;
            }

            String filename = image.getPath();

            String largeFilePlace = getServletContext().getRealPath("/photos/large/") + filename;
            String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + filename;
            String smallFilePlace = getServletContext().getRealPath("/photos/small/") + filename;
            File largeFile = new File(largeFilePlace);
            File mediumFile = new File(mediumFilePlace);
            File smallFile = new File(smallFilePlace);

            largeFile.delete();
            mediumFile.delete();
            smallFile.delete();

            boolean deleteLargeResult =
                    !largeFile.exists();
            boolean deleteMediumResult = !largeFile.exists();
            boolean deleteSmallResult = !largeFile.exists();

            if (deleteLargeResult && deleteMediumResult && deleteSmallResult && deleteResult.isSuccess() && deleteFavorResult) {
                connection.commit();
                out.println(JSON.toJSON(new ActionResult(true, "删除成功")));
                connection.setAutoCommit(true);

            } else {
                connection.rollback();
                out.println(JSON.toJSON(new ActionResult(false, "删除失败")));
                connection.setAutoCommit(true);
            }

        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void modify(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            //检查用户是否登录
            UserService userService = new UserService(connection, request);
            ImageService imageService = new ImageService(connection);
            User user = userService.tryAutoLogin();
            int imageID = Integer.parseInt(request.getParameter("imageID"));


            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录，或是登录已过期，请重新登录")));
                return;
            }

            if (!userService.hasTheImage(user, imageID)) {
                out.println(JSON.toJSON(new ActionResult(false, "你还没有这张图片，不能修改！")));
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
                out.println(JSON.toJSON(new ActionResult(false, "你还没有这张图片，不能修改！")));
                return;
            }

            //检测图片中国家与城市是否对应
            try {
                uploadPhotoInfo.checkCountryAndCity(connection);
            } catch (CountryCityMismatchException countryCityMismatchException) {
                out.println(JSON.toJSON(new ActionResult(false, countryCityMismatchException.getMessage())));
                return;
            }

            boolean photoChanged = !(uploadPhotoInfo.getPhoto().getSubmittedFileName() == null);

            if (photoChanged) {
                //检查图片类型
                try {
                    uploadPhotoInfo.checkType();
                } catch (TypeIncorrectException typeIncorrectException) {
                    out.println(JSON.toJSON(new ActionResult(false, typeIncorrectException.getMessage())));
                    return;
                }

                //得到原先的图片信息
                Image originalImage = imageService.getImage(imageID);


                //得到原先的文件名
                String originalFilename = originalImage.getPath();


                //更新图片信息
                originalImage.setTitle(uploadPhotoInfo.getTitle());
                originalImage.setDescription(uploadPhotoInfo.getTitle());
                originalImage.setContent(uploadPhotoInfo.getContent());
                originalImage.setCountry_RegionCodeISO(uploadPhotoInfo.getCountry());
                originalImage.setCityCode(Integer.parseInt(uploadPhotoInfo.getCity()));
                originalImage.setPath(uploadPhotoInfo.generateFileName(connection));
                originalImage.setDateReleased(new Timestamp(System.currentTimeMillis()));

                Image newImage = originalImage;


                //开启事务
                connection.setAutoCommit(false);


                //这是修改数据库中字段的结果
                ActionResult modifyResult = userService.modifyImageInDB(user, imageID, newImage);

                //如果修改数据库不成功则退出
                if (!modifyResult.isSuccess()) {
                    out.println(JSON.toJSON(modifyResult));
                    connection.setAutoCommit(true);
                    return;
                }


                //得到源文件的路径和新文件的路径
                String largeFilePlace = getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename();
                String originalLargeFilePlace = getServletContext().getRealPath("/photos/large/") + originalFilename;

                String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename();
                String originalMediumFilePlace = getServletContext().getRealPath("/photos/medium/") + originalFilename;

                String smallFilePlace = getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename();
                String originalSmallFilePlace = getServletContext().getRealPath("/photos/small/") + originalFilename;

                try {
                    //尝试删除源文件，添加新文件
                    uploadPhotoInfo.getPhoto().write(largeFilePlace);
                    PicReduce.saveMinPhoto(largeFilePlace, mediumFilePlace, 800, 1);
                    PicReduce.saveMinPhoto(largeFilePlace, smallFilePlace, 200, 1);

                    File originalLargeFile = new File(originalLargeFilePlace);
                    File originalMediumFile = new File(originalMediumFilePlace);
                    File originalSmallFile = new File(originalSmallFilePlace);

                    originalLargeFile.delete();
                    originalMediumFile.delete();
                    originalSmallFile.delete();

                    //提交
                    connection.commit();
                    out.println(JSON.toJSON(new ActionResult(true, "修改成功")));
                    return;
                } catch (Exception e) {
                    //如果出错则回滚，删除新图片
                    new File(largeFilePlace).delete();
                    new File(smallFilePlace).delete();
                    new File(mediumFilePlace).delete();
                    connection.rollback();
                    out.println(JSON.toJSON(new ActionResult(false, "修改失败")));
                    return;
                } finally {
                    connection.setAutoCommit(true);
                }


            } else {//如果图片没有更改
                Image originalImage = imageService.getImage(imageID);
                originalImage.setTitle(uploadPhotoInfo.getTitle());
                originalImage.setDescription(uploadPhotoInfo.getTitle());
                originalImage.setContent(uploadPhotoInfo.getContent());
                originalImage.setCountry_RegionCodeISO(uploadPhotoInfo.getCountry());
                originalImage.setCityCode(Integer.parseInt(uploadPhotoInfo.getCity()));
                originalImage.setDateReleased(new Timestamp(System.currentTimeMillis()));

                Image newImage = originalImage;

                ActionResult modifyResult = userService.modifyImageInDB(user, imageID, newImage);

                out.println(JSON.toJSON(modifyResult));
            }


        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }


}
