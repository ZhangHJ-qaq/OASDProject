package com.haojie.dao.ImageFavorDao;

import com.haojie.bean.User;

/**
 * 为ImageFavor类设计的dao层
 */
public interface ImageFavorDao {

    /**
     * 检测用户是否已经收藏了这个图片
     * @param user 用户对象
     * @param imageID imageID
     * @return true表示收藏了 false表示还未收藏
     */
    public abstract boolean hasLikedTheImage(User user, int imageID);

    /**
     * 用户收藏图片的操作
     * @param user 用户对象
     * @param imageID imageID
     * @return true表示收藏成功 false表示收藏失败
     */
    public abstract boolean likeImage(User user, int imageID);

    /**
     * 用户取消图片的操作
     * @param user 用户对象
     * @param imageID imageID
     * @return true表示取消收藏成功 false表示取消收藏失败
     */
    public abstract boolean unlikeImage(User user,int imageID);


}
