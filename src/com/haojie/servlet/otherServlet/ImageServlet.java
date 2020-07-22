package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.userDao.UserDao;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.others.ActionResult;
import com.haojie.others.SearchResult;
import com.haojie.service.ImageService;
import com.haojie.service.UserService;
import org.apache.commons.dbutils.DbUtils;
import sun.security.mscapi.PRNG;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * 和图片有关的servlet 用来处理和转发各种和图片有关的Ajax请求
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("method").equals("pureSearch")) {
            pureSearch(request, response);
            return;
        }
        if (request.getParameter("method").equals("myphoto")) {
            myPhoto(request, response);
            return;
        }
        if (request.getParameter("method").equals("myfavor")) {
            myfavor(request, response);
            return;
        }
        if (request.getParameter("method").equals("getImageInfo")) {
            getImageInfo(request, response);
            return;
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 和图片搜索有关的ajax请求的处理
     *
     * @param request
     * @param response
     */
    private void pureSearch(HttpServletRequest request, HttpServletResponse response) {
        String howToSearch = request.getParameter("howToSearch");
        String howToOrder = request.getParameter("howToOrder");
        String input = request.getParameter("input");
        int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            ImageService imageService = new ImageService(connection);
            SearchResult searchResult = imageService.search(howToSearch, howToOrder, input, requestedPage, pageSize);

            Object s = JSON.toJSON(searchResult);

            PrintWriter out = response.getWriter();
            out.println(s);

        } catch (SQLException | IOException ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    /**
     * 和查看我的图片有关的ajax请求的处理
     *
     * @param request
     * @param response
     */
    public void myPhoto(HttpServletRequest request, HttpServletResponse response) {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            User user = userService.tryAutoLogin();
            if (user == null) return;

            int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            ImageService imageService = new ImageService(connection);

            SearchResult searchResult = imageService.getMyPhotos(user, requestedPage, pageSize);

            PrintWriter out = response.getWriter();
            out.println(JSON.toJSON(searchResult));


        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    /**
     * 处理我的收藏的ajax请求
     *
     * @param request
     * @param response
     */
    public void myfavor(HttpServletRequest request, HttpServletResponse response) {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);

            User user = userService.tryAutoLogin();
            if (user == null) return;

            int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            ImageService imageService = new ImageService(connection);

            SearchResult searchResult = imageService.getFavor(user, requestedPage, pageSize);

            PrintWriter out = response.getWriter();

            out.println(JSON.toJSON(searchResult));

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void getImageInfo(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            int imageID = Integer.parseInt(request.getParameter("imageID"));

            UserService userService = new UserService(connection, request);
            ImageService imageService = new ImageService(connection);

            User user = userService.tryAutoLogin();
            Image image = imageService.getImage(user, imageID);

            if(image==null){
                out.println(JSON.toJSON(new ActionResult(false,"你还没有这个图片，不能修改它。")));
                return;
            }
            out.println(JSON.toJSON(image));

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }
    }


}
