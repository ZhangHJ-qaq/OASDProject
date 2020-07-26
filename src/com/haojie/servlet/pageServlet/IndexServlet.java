package com.haojie.servlet.pageServlet;

import com.haojie.bean.Image;
import com.haojie.bean.User;
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
import java.util.List;

@WebServlet(urlPatterns = {"/index"})
public class IndexServlet extends HttpServlet {

    /**
     * 将dopost转到dogetff处理
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);

            User user = userService.tryAutoLogin();

            ImageService imageService = new ImageService(connection);
            List<Image> popularImageList = imageService.getMostPopularNImages(3);
            List<Image> freshImageList = imageService.getMostFreshNImages(3);

            request.setAttribute("user", user);
            request.setAttribute("popularImageList", popularImageList);
            request.setAttribute("freshImageList", freshImageList);

            request.getRequestDispatcher("indexjsp").forward(request, response);
        } catch (Exception e) {
            DbUtils.closeQuietly(connection);
        }


    }
}
