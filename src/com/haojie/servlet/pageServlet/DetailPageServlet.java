package com.haojie.servlet.pageServlet;

import com.alibaba.fastjson.JSON;
import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.others.ActionResult;
import com.haojie.others.BrowseRecord;
import com.haojie.others.SingleBrowseRecord;
import com.haojie.service.ImageService;
import com.haojie.service.UserService;
import org.apache.commons.dbutils.DbUtils;

import javax.print.DocFlavor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

            addRecordToCookie(request, response, image, user);


            request.setAttribute("image", image);

            request.getRequestDispatcher("detailsjsp").forward(request, response);


        } catch (Exception ignored) {
        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    public void addRecordToCookie(HttpServletRequest request, HttpServletResponse response, Image image, User user) throws UnsupportedEncodingException {
        if (user != null && image != null) {
            Cookie[] cookies = request.getCookies();

            //遍历cookie
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(user.getUid() + "")) {
                    BrowseRecord browseRecord = JSON.parseObject(URLDecoder.decode(cookies[i].getValue()), BrowseRecord.class);
                    browseRecord.addRecord(new SingleBrowseRecord(image.getTitle(), image.getImageID()));

                    String jsonString = URLEncoder.encode(JSON.toJSONString(browseRecord));
                    jsonString = jsonString.replaceAll("\\+", "%20");

                    if (jsonString.getBytes(StandardCharsets.UTF_8).length > 4096) {//如果可能超长，则啥也不干
                        return;
                    }

                    cookies[i].setValue(jsonString);

                    cookies[i].setPath(cookies[i].getPath());
                    response.addCookie(cookies[i]);
                    return;
                }
            }

            Cookie browseRecordCookie = new Cookie(user.getUid() + "", "");
            BrowseRecord browseRecord = new BrowseRecord();
            browseRecord.addRecord(new SingleBrowseRecord(image.getTitle(), image.getImageID()));

            String jsonString = JSON.toJSONString(browseRecord);

            jsonString = URLEncoder.encode(jsonString);
            jsonString = jsonString.replaceAll("\\+", "%20");

            if (jsonString.getBytes(StandardCharsets.UTF_8).length > 4096) {//如果可能超长，则啥也不干
                return;
            }
            browseRecordCookie.setValue(jsonString);
            response.addCookie(browseRecordCookie);
        }

    }

}
