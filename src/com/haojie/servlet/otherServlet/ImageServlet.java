package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.haojie.others.SearchResult;
import com.haojie.service.ImageService;
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

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("method").equals("pureSearch")) {
            pureSearch(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

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

            DbUtils.close(connection);
        } catch (SQLException | IOException ignored) {

        }


    }
}
