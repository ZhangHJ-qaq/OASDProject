package com.haojie.dao.ImageFavorDao;

import com.haojie.bean.ImageFavor;
import com.haojie.bean.User;
import com.haojie.dao.genericDao.GenericDao;
import com.haojie.dao.imageDao.ImageDaoImpl;

import java.sql.Connection;
import java.util.List;

public class ImageFavorDaoImpl extends GenericDao<ImageFavor> implements ImageFavorDao {
    private Connection connection;

    public ImageFavorDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean hasLikedTheImage(User user, int imageID) {
        try {
            String sql = "select * from travelimagefavor where imageID=? and uid=?";
            int uid = user.getUid();
            List<ImageFavor> imageFavorList = this.queryForList(connection, sql, imageID, uid);
            return imageFavorList.size() > 0;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean likeImage(User user, int imageID) {
        try {
            int uid = user.getUid();
            String sql = "insert into travelimagefavor (UID, ImageID) VALUES (?,?)";
            int rowsAffected = this.update(this.connection, sql, uid, imageID);
            return rowsAffected == 1;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean unlikeImage(User user, int imageID) {
        try {
            int uid = user.getUid();
            String sql = "delete from travelimagefavor where ImageID=? and UID=?";
            int rowsAffected = this.update(this.connection, sql, imageID, uid);
            return rowsAffected >= 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAllImageFavor(int imageID) {
        try {
            String sql = "delete from travelimagefavor where imageID=?";
            this.update(this.connection, sql, imageID);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
