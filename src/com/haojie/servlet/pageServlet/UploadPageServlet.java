package com.haojie.servlet.pageServlet;

import com.haojie.bean.Country;
import com.haojie.bean.User;
import com.haojie.dao.countryDao.CountryDao;
import com.haojie.dao.countryDao.CountryDaoImpl;
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
import java.util.List;

@WebServlet("/upload")
public class UploadPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            User user = userService.tryAutoLogin();
            if (user == null) {
                request.getRequestDispatcher("login").forward(request, response);
                return;
            }
            request.setAttribute("user", user);

            CountryDao countryDao = new CountryDaoImpl(connection);
            List<Country> countryList = countryDao.getALL();
            request.setAttribute("countryList", countryList);

            request.getRequestDispatcher("uploadjsp").forward(request, response);
            DbUtils.close(connection);

        } catch (Exception ignored) {
        }

    }
}
