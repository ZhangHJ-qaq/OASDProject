package com.haojie.dao.commentDao;

import com.haojie.bean.Comment;

import java.util.List;

/**
 * The DAO layer for the comment
 */
public interface CommentDao {

    /**
     * To add a new comment into the database, table travelimageComment
     * @param newComment The object entity that encapsulate the information about that comment.
     * @return If success, returns true. Otherwise, returns false.
     */
    public abstract boolean addComment(Comment newComment);


    /**
     * Get all the comments that belong to a image.
     * @param imageID The imageID that represents the image.
     * @param howToOrder Two options are available. One is "time" and the other is "popularity".
     *                   In the first case, the comments will be ordered in a descendant chronicle order.
     *                   In the second case, the comments will be sorted according to their favor counts, in
     *                   a descendant order.
     * @return
     */
    public abstract List<Comment> getComments(int imageID, String howToOrder);


    /**
     * Get the object entity of a comment.
     * @param commentID The commentID that represents the comment.
     * @return The object entity of a comment
     */
    public abstract Comment getComment(int commentID);
}
