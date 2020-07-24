package com.haojie.servlet.pageServlet;

import com.haojie.bean.User;
import com.haojie.service.UserService;
import org.apache.commons.dbutils.DbUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/myfriend")
public class MyFriendPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            UserService userService = new UserService(connection, request);

            User user = userService.tryAutoLogin();

            //如果用户没有登录
            if (user == null) {
                request.getRequestDispatcher("login").forward(request, response);
                return;
            }

            request.setAttribute("user", user);

            request.getRequestDispatcher("myfriendjsp").forward(request, response);


        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }
}
