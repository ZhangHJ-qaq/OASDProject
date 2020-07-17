package com.haojie.service;

import com.haojie.bean.Image;
import com.haojie.dao.imageDao.ImageDao;
import com.haojie.dao.imageDao.ImageDaoImpl;
import com.haojie.others.SearchResult;
import jdk.nashorn.internal.ir.RuntimeNode;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ImageService {
    private final Connection connection;

    public ImageService(Connection connection) {
        this.connection = connection;

    }

    public List<Image> getMostPopularNImages(int n) {
        try {
            Connection connection = this.connection;
            ImageDaoImpl imageDao = new ImageDaoImpl(connection);
            List<Image> popularImageList = imageDao.getMostPopularNImages(n);
            return popularImageList;

        } catch (Exception exception) {
            return null;
        }


    }

    public List<Image> getMostFreshNImages(int n) {
        try {
            Connection connection = this.connection;
            ImageDaoImpl imageDao = new ImageDaoImpl(connection);
            List<Image> freshImageList = imageDao.getMostFreshNImages(n);
            return freshImageList;

        } catch (Exception exception) {
            return null;
        }
    }

    public SearchResult search(String howToSearch, String howToOrder, String input, int requestedPage, int pageSize) {
        try {
            ImageDaoImpl imageDao = new ImageDaoImpl(connection);
            List<Image> imageList = imageDao.search(howToSearch, howToOrder, input);

            SearchResult searchResult = new SearchResult();

            int maxPage = imageList.size() / pageSize + 1;
            searchResult.setMaxPage(maxPage);

            requestedPage = Math.min(requestedPage, maxPage);
            requestedPage = Math.max(requestedPage, 1);//净化requestedPage的输入

            searchResult.setRespondedPage(requestedPage);

            int start = pageSize * (requestedPage - 1);
            int end = Math.min(start + pageSize, imageList.size());


            ArrayList<Image> subImageList = new ArrayList<>();
            for (int i = start; i < end; i++) {
                subImageList.add(imageList.get(i));
            }

            searchResult.setImageList(subImageList);
            return searchResult;
        } catch (Exception e) {
            return null;
        }

    }


}
