package com.haojie.bean;

public class CommentFavor {
    private int commentFavorID;
    private int commentID;
    private int uid;

    public CommentFavor() {

    }

    public CommentFavor(int commentID, int uid) {
        this.commentID = commentID;
        this.uid = uid;
    }

    public int getCommentFavorID() {
        return commentFavorID;
    }

    public void setCommentFavorID(int commentFavorID) {
        this.commentFavorID = commentFavorID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
