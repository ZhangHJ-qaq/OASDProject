package com.haojie.dao.ImageFavorDao;

import com.haojie.bean.User;

/**
 * The DAO layer for imagefavor
 */
public interface ImageFavorDao {

    /**
     * To check whether the user has favored the image
     * @param user 用户对象
     * @param imageID imageID
     * @return true表示收藏了 false表示还未收藏
     */
    public abstract boolean hasLikedTheImage(User user, int imageID);

    /**
     * To favor an image
     * @param user The user object
     * @param imageID imageID
     * @return If success, returns true. Otherwise, returns false.
     */
    public abstract boolean likeImage(User user, int imageID);

    /**
     * The unlike an image
     * @param user The user object
     * @param imageID imageID
     * @return If success, returns true, Otherwise, returns false.
     */
    public abstract boolean unlikeImage(User user,int imageID);

    /**
     * To delete all favor records related to an image.
     * @param imageID imageID
     * @return If success, returns true. Otherwise, returns false.
     */
    public abstract boolean deleteAllImageFavor(int imageID);


}
