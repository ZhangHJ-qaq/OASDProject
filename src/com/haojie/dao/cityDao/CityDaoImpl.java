package com.haojie.dao.cityDao;

import com.haojie.bean.City;
import com.haojie.dao.genericDao.GenericDao;

import java.sql.Connection;
import java.util.List;

public class CityDaoImpl extends GenericDao<City> implements CityDao {
    Connection connection;

    public CityDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<City> getCities(String iso) {
        String sql = "select * from geocities where Country_RegionCodeISO=? order by AsciiName";
        try {
            List<City> cityList = this.queryForList(connection, sql, iso);
            return cityList;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public boolean countryCityMatch(int cityCode, String iso) {
        String sql = "select * from geocities where GeoNameID=? and Country_RegionCodeISO=?";
        try {
            List<City> cityList = this.queryForList(connection, sql, cityCode, iso);
            return cityList.size() != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
