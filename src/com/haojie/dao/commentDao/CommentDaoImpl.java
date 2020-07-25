package com.haojie.dao.commentDao;

import com.haojie.bean.Comment;
import com.haojie.dao.genericDao.GenericDao;

import java.sql.Connection;
import java.util.List;

public class CommentDaoImpl extends GenericDao<Comment> implements CommentDao {

    private Connection connection;

    public CommentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addComment(Comment newComment) {
        try {
            String sql = "insert into travelimagecomment (imageID, uid, text, dateReleased) VALUES (?,?,?,?)";
            int rowsAffected = this.update(connection, sql,
                    newComment.getImageID(), newComment.getUid(), newComment.getText(), newComment.getDateReleased());
            return rowsAffected > 0;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Comment> getComments(int imageID, String howToOrder) {
        try {
            String sql = "";
            if (howToOrder.equals("popularity")) {
                sql = "select travelimagecomment.commentID,\n" +
                        "       count(travemcommentfavor.commentID) as favorCount,\n" +
                        "       travelimagecomment.uid,\n" +
                        "       traveluser.UserName                 as commenterName,\n" +
                        "       text,\n" +
                        "       dateReleased\n" +
                        "from travemcommentfavor\n" +
                        "         right join travelimagecomment on travemcommentfavor.commentID = travelimagecomment.commentID\n" +
                        "         inner join traveluser on traveluser.UID = travelimagecomment.uid where travelimagecomment.imageID=?\n" +
                        "group by commentID\n" +
                        "order by favorCount desc";
            } else {
                sql = "select travelimagecomment.commentID,\n" +
                        "       count(travemcommentfavor.commentID) as favorCount,\n" +
                        "       travelimagecomment.uid,\n" +
                        "       traveluser.UserName                 as commenterName,\n" +
                        "       text,\n" +
                        "       dateReleased\n" +
                        "from travemcommentfavor\n" +
                        "         right join travelimagecomment on travemcommentfavor.commentID = travelimagecomment.commentID\n" +
                        "         inner join traveluser on traveluser.UID = travelimagecomment.uid where travelimagecomment.imageID=?\n" +
                        "group by commentID\n" +
                        "order by dateReleased desc";
            }

            return this.queryForList(connection, sql, imageID);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Comment getComment(int commentID) {
        try {
            String sql = "select * from travelimagecomment where commentID=?";
            return this.queryForOne(connection, sql, commentID);
        } catch (Exception e) {
            return null;
        }
    }
}
