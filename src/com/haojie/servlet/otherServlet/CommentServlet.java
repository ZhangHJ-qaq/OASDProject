package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.haojie.bean.Comment;
import com.haojie.bean.User;
import com.haojie.others.ActionResult;
import com.haojie.others.SearchResult;
import com.haojie.service.CommentService;
import com.haojie.service.UserService;
import com.haojie.utils.MyUtils;
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
import java.sql.Timestamp;

/**
 * The servlet for comments to handle ajax requests
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getParameter("method").equals("getComment")) {
                getComment(request, response);
                return;
            }

        } catch (Exception ignored) {

        }

    }

    private void getComment(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();


            UserService userService = new UserService(connection, request);


            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            int imageID = Integer.parseInt(request.getParameter("imageID"));
            int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            String howToOrder = request.getParameter("howToOrder");

            CommentService commentService = new CommentService(connection);

            SearchResult searchResult = commentService.getComments(imageID, me, requestedPage, pageSize, howToOrder, request);

            out.println(JSON.toJSON(searchResult));

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }
}
