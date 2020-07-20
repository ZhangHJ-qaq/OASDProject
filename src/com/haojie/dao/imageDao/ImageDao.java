package com.haojie.dao.imageDao;

import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.others.ActionResult;

import java.util.List;

/**
 * 为Image类设计的Dao层
 */
public interface ImageDao {

    /**
     * 根据给出的n 得到最热门的n张图片
     * @param n 要得到的图片数量
     * @return 图片的列表
     */
    public abstract List<Image> getMostPopularNImages(int n);

    /**
     * 根据给出的n 得到最新上传的几张图片
     * @param n 图片数量
     * @return 图片列表
     */
    public abstract List<Image> getMostFreshNImages(int n);

    /**
     * 根据给出的搜索方法进行全表搜索
     * @param howToSearch 可选值为title或content 前者表示按标题搜索 后者表示按内容搜索
     * @param howToOrder 可选值为time或popularity 前者表示按时间排序 后者表示按热度排序 均为倒序
     * @param input 搜索的输入
     * @return 图片列表
     */
    public abstract List<Image> search(String howToSearch, String howToOrder, String input);

    /**
     * 得到用户所有的图片列表
     * @param user 用户对象
     * @return 对应的图片列表
     */
    public abstract List<Image> getMyPhotos(User user);


    /**
     * 得到用户所有的收藏了的图片的列表
     * @param user 用户对象
     * @return 用户收藏了的图片列表
     */
    public abstract List<Image> getFavor(User user);

    /**
     * 根据imageID，得到对应的图片对象
     * @param imageID imageID
     * @return 对应的图片对象
     */
    public abstract Image getImage(int imageID);

    /**
     * 根据imageID，得到图片是否在数据库中存在
     * @param imageID imageID
     * @return 如果存在为true，不存在为false
     */
    public abstract boolean imageExists(int imageID);

    public abstract boolean imageExists(String fileName);


    public abstract ActionResult insertImage(User user, Image image);


}


