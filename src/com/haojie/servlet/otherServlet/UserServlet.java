package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.haojie.bean.User;
import com.haojie.config.Config;
import com.haojie.dao.userDao.UserDao;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.others.RegisterLoginResult;
import com.haojie.service.UserService;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.DbUtils;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

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
        if (request.getParameter("method").equals("register")) {
            try {
                register(request, response);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return;
        }
        if (request.getParameter("method").equals("login")) {
            try {
                login(request, response);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return;
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
        RegisterLoginResult registerResult = userService.register(
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
        RegisterLoginResult loginResult = userService.tryLogin(request.getParameter("username"), request.getParameter("password"), request.getParameter("captcha"));
        DbUtils.close(connection);

        PrintWriter out = response.getWriter();
        Object o = JSON.toJSON(loginResult);
        out.println(o);

    }
}
