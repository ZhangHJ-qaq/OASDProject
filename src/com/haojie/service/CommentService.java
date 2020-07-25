package com.haojie.service;

import com.haojie.bean.Comment;
import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.commentDao.CommentDao;
import com.haojie.dao.commentDao.CommentDaoImpl;
import com.haojie.dao.imageDao.ImageDao;
import com.haojie.dao.imageDao.ImageDaoImpl;
import com.haojie.others.ActionResult;
import com.haojie.others.SearchResult;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CommentService {

    private Connection connection;

    public CommentService(Connection connection) {
        this.connection = connection;
    }

    public ActionResult addComment(Comment newComment) {
        try {
            CommentDao commentDao = new CommentDaoImpl(connection);
            ImageDao imageDao = new ImageDaoImpl(connection);

            Image image = imageDao.getImage(newComment.getImageID());

            if (image == null) return new ActionResult(false, "图片不存在！");

            boolean success = commentDao.addComment(newComment);

            if (success) {
                return new ActionResult(true, "评论成功");
            } else {
                return new ActionResult(false, "评论失败");
            }

        } catch (Exception e) {
            return new ActionResult(false, "评论失败");
        }

    }

    public SearchResult getComments(int imageID, User user, int requestedPage, int pageSize, String howToOrder,HttpServletRequest request) {
        try {
            CommentDao commentDao = new CommentDaoImpl(connection);
            List<Comment> commentList = commentDao.getComments(imageID, howToOrder);

            SearchResult searchResult = getSearchResult(commentList, requestedPage, pageSize, user,request);

            return searchResult;

        } catch (Exception e) {
            return null;
        }

    }

    private SearchResult getSearchResult(List<Comment> originalCommentList, int requestedPage, int pageSize, User user, HttpServletRequest request) {
        ArrayList<Comment> subCommentList = new ArrayList<>();
        SearchResult searchResult = new SearchResult();


        int maxPage = (int) Math.ceil((double) originalCommentList.size() / pageSize);//得到最大页码数
        searchResult.setMaxPage(maxPage);

        requestedPage = Math.max(requestedPage, 1);
        requestedPage = Math.min(maxPage, requestedPage);

        searchResult.setRespondedPage(requestedPage);//净化用户输入的页码数，得到最终相应的页面


        int start = pageSize * (requestedPage - 1);
        int end = Math.min(start + pageSize, originalCommentList.size());//根据最终相应的页面，得到应该从原始的list中截取哪一段

        for (int i = start; i < end; i++) {
            subCommentList.add(originalCommentList.get(i));
        }

        UserService userService = new UserService(connection, request);


        //遍历评论的子列表 判断用户是否赞了这个评论
        for (int i = 0; i <= subCommentList.size() - 1; i++) {
            if (userService.hasFavoredTheComment(user.getUid(), subCommentList.get(i).getCommentID())) {
                subCommentList.get(i).setHaveFavoredByMe(true);
            } else {
                subCommentList.get(i).setHaveFavoredByMe(false);
            }
        }

        searchResult.setCommentList(subCommentList);

        return searchResult;

    }


}
