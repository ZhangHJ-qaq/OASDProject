package com.haojie.servlet.pageServlet;

import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.others.ActionResult;
import com.haojie.service.ImageService;
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

@WebServlet("/details")
public class DetailPageServlet extends HttpServlet {
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

            request.setAttribute("user", user);

            int imageID = Integer.parseInt(request.getParameter("imageID"));
            String action = request.getParameter("action") == null ? "" : request.getParameter("action");
            ActionResult actionResult = null;
            if (action.equals("favor")) {
                actionResult = userService.favorImage(user, imageID);
            } else if (action.equals("unfavor")) {
                actionResult = userService.unfavorImage(user, imageID);
            }

            request.setAttribute("actionResult", actionResult);

            boolean hasFavoredImage = userService.hasFavoredTheImage(user, imageID);
            request.setAttribute("hasFavoredImage", hasFavoredImage);

            ImageService imageService = new ImageService(connection);
            Image image = imageService.getImage(imageID);


            request.setAttribute("image", image);

            request.getRequestDispatcher("detailsjsp").forward(request, response);



            DbUtils.close(connection);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


    }
}
