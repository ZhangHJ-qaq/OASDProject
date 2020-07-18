package com.haojie.servlet.pageServlet;

import com.haojie.bean.User;
import com.haojie.dao.userDao.UserDaoImpl;
import org.apache.commons.dbutils.DbUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/myphoto")
public class MyPhotoPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserDaoImpl userDao = new UserDaoImpl(connection);
            User user = userDao.tryAutoLogin((String) request.getSession().getAttribute("username"), request.getSession().getId());
            if (user == null) {
                request.getRequestDispatcher("login").forward(request, response);
                return;
            }
            request.setAttribute("user", user);
            request.getRequestDispatcher("myphotojsp").forward(request, response);
            DbUtils.close(connection);

        } catch (Exception ignored) {

        }

    }
}
