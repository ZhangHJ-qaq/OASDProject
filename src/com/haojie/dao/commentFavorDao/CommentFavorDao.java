package com.haojie.dao.commentFavorDao;

import com.haojie.bean.CommentFavor;

/**
 * The DAO layer for commentfavor
 */
public interface CommentFavorDao {

    /**
     * To check whether the user has favored the comment
     * @param uid The uid of user
     * @param commentID The commentID of a comment
     * @return If user has favored the comment, returns true. Otherwise, returns false.
     */
    public abstract boolean favorExists(int uid, int commentID);


    /**
     * To add a comment favor into database
     * @param commentFavor The object entity of  comment favor
     * @return If adding succeeds, returns true. Otherwise, returns false.
     */
    public abstract boolean addCommentFavor(CommentFavor commentFavor);


    public abstract CommentFavor getCommentFavor(int commentID);


    /**
     * Delete user's favor for the comment from the database
     * @param uid User's uid
     * @param commentID Comment's commentID
     * @return If successful, returns true. Otherwise, returns false.
     */
    public abstract boolean deleteCommentFavor(int uid, int commentID);

}
