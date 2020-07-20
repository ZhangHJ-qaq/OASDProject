package com.haojie.dao.imageDao;

import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.genericDao.GenericDao;
import com.haojie.others.ActionResult;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    @Override
    public List<Image> getFavor(User user) {
        String sql = "select travelimage.ImageID,Title,Description,Longitude,Latitude,CityCode,Country_RegionCodeISO,travelimage.UID,PATH,Content,DateReleased from travelimagefavor inner join travelimage  on travelimagefavor.ImageID = travelimage.ImageID    where travelimagefavor.UID=?";
        try {
            List<Image> imageList = this.queryForList(connection, sql, user.getUid());
            return imageList;
        } catch (SQLException sqlException) {
            return null;

        }
    }

    @Override
    public Image getImage(int imageID) {
        String sql = "select\n" +
                "travelimage.ImageID,count(travelimagefavor.ImageID) as favorCount,Title,Description,\n" +
                "       travelimage.Latitude,travelimage.Longitude,\n" +
                "       CityCode,AsciiName,travelimage.Country_RegionCodeISO,geocountries_regions.Country_RegionName,PATH,Content,DateReleased,UserName\n" +
                "from\n" +
                "(travelimagefavor right join travelimage on travelimage.ImageID=travelimagefavor.ImageID)\n" +
                "left join geocities on CityCode=geocities.GeoNameID\n" +
                "left join geocountries_regions on travelimage.Country_RegionCodeISO=geocountries_regions.ISO\n" +
                "left join traveluser on travelimage.UID=traveluser.UID\n" +
                "where travelimage.ImageID=?\n" +
                "group by ImageID";
        try {
            Image image = this.queryForOne(connection, sql, imageID);
            return image;
        } catch (SQLException sqlException) {
            return null;
        }


    }

    @Override
    public boolean imageExists(int imageID) {
        return this.getImage(imageID) != null;
    }

    @Override
    public ActionResult insertImage(User user, Image image) {
        if (user == null) {
            return new ActionResult(false, "没有登陆或登录已过期");
        }
        String sql = "insert into travelimage (Title, Description, Latitude, Longitude, CityCode, Country_RegionCodeISO, UID, PATH, Content,\n" +
                "                         DateReleased)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
        try {
            int rowsAffected = this.update(connection, sql,
                    image.getTitle(), image.getDescription(), image.getLatitude(), image.getLongitude(), image.getCityCode()
                    , image.getCountry_RegionCodeISO(), image.getUid(), image.getPath(), image.getContent(), new Timestamp(System.currentTimeMillis()));
            if (rowsAffected > 0) {
                return new ActionResult(true, "成功");
            } else {
                return new ActionResult(false, "失败");
            }

        } catch (Exception e) {
            return new ActionResult(false, "失败");
        }

    }

    @Override
    public boolean imageExists(String fileName) {
        String sql = "select * from travelimage where PATH regexp ?";
        try {
            List<Image> imageList = this.queryForList(connection, sql, fileName);
            return imageList.size() > 0;

        } catch (Exception e) {
            return false;
        }
    }


}
