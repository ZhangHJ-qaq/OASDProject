package com.haojie.dao.commentDao;

import com.haojie.bean.Comment;

import java.util.List;

public interface CommentDao {


    public abstract boolean addComment(Comment newComment);

    public abstract List<Comment> getComments(int imageID, String howToOrder);

    public abstract Comment getComment(int commentID);
}
