package com.haojie.service;

import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.imageDao.ImageDao;
import com.haojie.dao.imageDao.ImageDaoImpl;
import com.haojie.others.ActionResult;
import com.haojie.others.SearchResult;
import jdk.nashorn.internal.ir.RuntimeNode;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


/**
 * 有关于图片的业务层
 */
public class ImageService {
    private final Connection connection;

    /**
     * @param connection 数据库连接
     */
    public ImageService(Connection connection) {
        this.connection = connection;

    }

    /**
     * 根据n 得到最热的n张图片组成的图片列表 调用了ImageDao类中的同名方法
     *
     * @param n 图片数
     * @return 图片列表
     */
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

    /**
     * 根据n 得到最新上传的n张图片的列表 调用了ImageDao类中的同名方法
     *
     * @param n 图片数
     * @return 图片列表
     */
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

    /**
     * 搜索页的搜索处理函数 调用了ImageDao中同名函数，不同的是dao层中进行的是全表搜索，这里根据请求的页码，选出其中需要的部分，组成一个新的list
     * 得到respondedPage和maxPage 封装在一个SearchResult对象中
     *
     * @param howToSearch   title或content
     * @param howToOrder    time或popularity 都是倒叙
     * @param input         输入
     * @param requestedPage 用户请求的页面
     * @param pageSize      每一页中有多少个元素
     * @return SearchResult对象中
     */
    public SearchResult search(String howToSearch, String howToOrder, String input, int requestedPage, int pageSize) {
        try {
            ImageDaoImpl imageDao = new ImageDaoImpl(connection);
            List<Image> imageList = imageDao.search(howToSearch, howToOrder, input);

            SearchResult searchResult = this.getSearchResult(imageList, requestedPage, pageSize);

            return searchResult;

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 我的照片页的处理函数 调用了ImageDao类中的同名方法，在这里完成分页和封装
     *
     * @param user          用户对象
     * @param requestedPage 用户请求的页面
     * @param pageSize      一页中有多少条结果
     * @return SearchResult对象中
     */

    public SearchResult getMyPhotos(User user, int requestedPage, int pageSize) {
        try {

            ImageDaoImpl imageDao = new ImageDaoImpl(connection);

            List<Image> imageList = imageDao.getMyPhotos(user);

            SearchResult searchResult = this.getSearchResult(imageList, requestedPage, pageSize);
            return searchResult;

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 我的收藏页的处理函数 调用ImageDao类同名方法，在这里完成分页和封装
     *
     * @param user          用户对象
     * @param requestedPage 用户请求的页面
     * @param pageSize      每页上结果的条数
     * @return SearchResult对象
     */
    public SearchResult getFavor(User user, int requestedPage, int pageSize) {
        try {
            ImageDao imageDao = new ImageDaoImpl(connection);

            List<Image> imageList = imageDao.getFavor(user);

            SearchResult searchResult = this.getSearchResult(imageList, requestedPage, pageSize);
            return searchResult;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将ImageDao类中全表搜索得到的list传入到这里，再传入用户请求的页码和每页中条目的数量，裁剪后得到最终的SearchResult对象
     *
     * @param originalImageList 全表搜索得到的list
     * @param requestedPage     用户请求的页码数
     * @param pageSize          一页上有多少条目
     * @return SearchResult对象
     */
    private SearchResult getSearchResult(List<Image> originalImageList, int requestedPage, int pageSize) {
        ArrayList<Image> subImageList = new ArrayList<>();
        SearchResult searchResult = new SearchResult();


        int maxPage = (int) Math.ceil((double) originalImageList.size() / pageSize);//得到最大页码数
        searchResult.setMaxPage(maxPage);

        requestedPage = Math.max(requestedPage, 1);
        requestedPage = Math.min(maxPage, requestedPage);

        searchResult.setRespondedPage(requestedPage);//净化用户输入的页码数，得到最终相应的页面


        int start = pageSize * (requestedPage - 1);
        int end = Math.min(start + pageSize, originalImageList.size());//根据最终相应的页面，得到应该从原始的list中截取哪一段

        for (int i = start; i < end; i++) {
            subImageList.add(originalImageList.get(i));
        }

        searchResult.setImageList(subImageList);

        return searchResult;

    }

    /**
     * 根据ImageID得到图片对象
     *
     * @param imageID imageID
     * @return 图片对象
     */
    public Image getImage(int imageID) {
        ImageDao imageDao = new ImageDaoImpl(connection);
        Image image = imageDao.getImage(imageID);
        return image;
    }

    public ActionResult insertImage(User user, Image image) {
        ImageDao imageDao = new ImageDaoImpl(connection);
        if (user == null) return new ActionResult(false, "没有登陆或登录已经过期");
        return imageDao.insertImage(user, image);
    }


}
