package com.haojie.others;

import com.haojie.bean.Comment;
import com.haojie.bean.Image;
import com.haojie.bean.User;

import java.util.List;

/**
 * 封装了搜索得到的结果的对象。imageList为图片列表，maxPage表示最大页数，respondedPage表示相应页数
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
