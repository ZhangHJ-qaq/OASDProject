package com.haojie.servlet.pageServlet;

import com.haojie.bean.User;
import com.haojie.dao.userDao.UserDao;
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
import java.sql.SQLException;

@WebServlet("/search")
public class SearchPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            User user = userService.tryAutoLogin();
            request.setAttribute("user", user);
            request.getRequestDispatcher("searchjsp").forward(request, response);

        } catch (SQLException e) {

        }finally {
            DbUtils.closeQuietly(connection);
        }
    }
}
