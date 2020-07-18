package com.haojie.dao.imageDao;

import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.genericDao.GenericDao;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ImageDaoImpl extends GenericDao<Image> implements ImageDao {
    private Connection connection;

    public ImageDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<Image> getMostPopularNImages(int n) {
        try {
            String sql = "select travelimagefavor.ImageID,Title,Description,Latitude,Longitude,CityCode,Country_RegionCodeISO,travelimage.UID,Content,PATH,DateReleased from travelimagefavor inner join travelimage on travelimage.ImageID=travelimagefavor.ImageID group by ImageID order by count(travelimagefavor.ImageID) desc limit ?";
            List<Image> popularImageList = this.queryForList(connection, sql, n);
            return popularImageList;
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public List<Image> getMostFreshNImages(int n) {
        try {
            String sql = "select UserName,ImageID, Title, Description, Latitude, Longitude, CityCode, Country_RegionCodeISO, travelimage.UID, PATH, Content, DateReleased from travelimage inner join traveluser t on travelimage.UID = t.UID order by DateReleased desc limit ?";
            List<Image> freshImageList = this.queryForList(connection, sql, n);
            return freshImageList;


        } catch (Exception exception) {
            return null;

        }

    }

    @Override
    public List<Image> search(String howToSearch, String howToOrder, String input) {
        String sql = null;
        if (howToOrder.equals("popularity")) {
            if (howToSearch.equals("title")) {
                sql = "select travelimagefavor.ImageID, count(travelimagefavor.ImageID) as favorCount, Title, Description, PATH\n" +
                        "from travelimagefavor\n" +
                        "         inner join travelimage t on travelimagefavor.ImageID = t.ImageID where Title regexp ?\n" +
                        "group by ImageID\n" +
                        "order by favorCount desc";

            } else if (howToSearch.equals("content")) {
                sql = "select travelimagefavor.ImageID, count(travelimagefavor.ImageID) as favorCount, Title, Description, PATH\n" +
                        "from travelimagefavor\n" +
                        "         inner join travelimage t on travelimagefavor.ImageID = t.ImageID where Content regexp ?\n" +
                        "group by ImageID\n" +
                        "order by favorCount desc";

            }

        } else if (howToOrder.equals("time")) {
            if (howToSearch.equals("title")) {
                sql = "select ImageID,Title,Description,PATH from travelimage where Title regexp ? order by DateReleased desc";

            } else if (howToSearch.equals("content")) {
                sql = "select ImageID,Title,Description,PATH from travelimage where Content regexp ? order by DateReleased desc";

            }
        }
        try {
            List<Image> imageList = this.queryForList(this.connection, sql, input);
            return imageList;
        } catch (SQLException sqlException) {
            return null;
        }

    }

    @Override
    public List<Image> getMyPhotos(User user) {
        String sql = "select * from travelimage where uid=?";
        try {
            List<Image> imageList = this.queryForList(this.connection, sql, user.getUid());
            return imageList;
        } catch (SQLException sqlException) {
            return null;
        }
    }
}
