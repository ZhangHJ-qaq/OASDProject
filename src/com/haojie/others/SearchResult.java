package com.haojie.others;

import com.haojie.bean.Comment;
import com.haojie.bean.Image;
import com.haojie.bean.User;

import java.util.List;

/**
 * Here we encapsulate the result of search.
 */
public class SearchResult {
    private List<Image> imageList;
    private List<User> userList;
    private List<Comment> commentList;
    private int respondedPage;
    private int maxPage;

    public SearchResult() {

    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public int getRespondedPage() {
        return respondedPage;
    }

    public void setRespondedPage(int respondedPage) {
        this.respondedPage = respondedPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }


    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
