package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.haojie.bean.User;
import com.haojie.others.ActionResult;
import com.haojie.service.UserService;
import org.apache.commons.dbutils.DbUtils;


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
}
