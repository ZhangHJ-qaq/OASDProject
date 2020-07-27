package com.haojie.dao.imageDao;

import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.others.ActionResult;

import java.util.List;

/**
 * The DAO layer for image
 */
public interface ImageDao {

    /**
     * Get the most popular n images
     *
     * @param n The num of images you want to get.
     * @return The image list that contains most popular n images
     */
    public abstract List<Image> getMostPopularNImages(int n);

    /**
     * Get the freshest n images
     *
     * @param n The number of images
     * @return The list of images that contains freshest n images
     */
    public abstract List<Image> getMostFreshNImages(int n);

    /**
     * To search the images in the database.
     *
     * @param howToSearch Two options available, "title" and "content"
     * @param howToOrder  Two options available, they're "time" and "popularity"
     *                    In the first case, the result is sorted in a descendant chronicle order.
     *                    In the second case, the result is sorted in a descendant popularity order.
     * @param input       The input from the user
     * @return The image list that have been gotten.
     */
    public abstract List<Image> search(String howToSearch, String howToOrder, String input);

    /**
     * Get all the photos a user has
     *
     * @param user The user entity object.
     * @return The image list he or she has.
     */
    public abstract List<Image> getMyPhotos(User user);


    /**
     * Get all the images a user have favored
     *
     * @param user The user entity object
     * @return The image list he or she has favored
     */
    public abstract List<Image> getFavor(User user);

    /**
     * Get the object entity of an image
     *
     * @param imageID The imageID that represents the image
     * @return The image entity object
     */
    public abstract Image getImage(int imageID);

    /**
     * To check whether the image exists in the database.
     *
     * @param imageID imageID
     * @return If exists, returns true. Otherwise, returns false
     */
    public abstract boolean imageExists(int imageID);

    /**
     * To check whether the image exists in the database.
     *
     * @param fileName The fileName
     * @return If exists, returns true. Otherwise, returns false
     */
    public abstract boolean imageExists(String fileName);

    /**
     * To check whether the image exists in the database.
     *
     * @param user The user entity
     * @param imageID The imageID
     * @return If exists, returns true. Otherwise, returns false
     */
    public abstract boolean imageExists(User user, int imageID);

    /**
     * To insert a new image into databse.
     * @param user The user entity object
     * @param image The image entity object
     * @return If successful ,returns true. Otherwise, false.
     */
    public abstract ActionResult insertImage(User user, Image image);

    /**
     * To delete an image from the database
     * @param imageID imageID
     * @return ActionResult Object
     */
    public abstract ActionResult deleteImage(int imageID);

    /**
     * To get the entity object of an image from the database
     * @param user User entity
     * @param imageID imageID
     * @return The image entity object
     */
    public abstract Image getImage(User user, int imageID);

    /**
     * To modify the image
     * @param user The user object
     * @param imageID The imageID
     * @param image The new image entity object
     * @return ActionResult Object
     */
    public abstract ActionResult modifyImage(User user, int imageID, Image image);


}


