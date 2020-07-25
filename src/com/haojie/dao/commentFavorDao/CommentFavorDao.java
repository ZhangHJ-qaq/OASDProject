package com.haojie.dao.commentFavorDao;

import com.haojie.bean.CommentFavor;

public interface CommentFavorDao {

    public abstract boolean favorExists(int uid, int commentID);

    public abstract boolean addCommentFavor(CommentFavor commentFavor);

    public abstract CommentFavor getCommentFavor(int commentID);

    public abstract boolean deleteCommentFavor(int uid, int commentID);

}
