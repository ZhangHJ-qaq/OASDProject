package com.haojie.dao.countryDao;

import com.haojie.bean.Country;
import com.haojie.dao.genericDao.GenericDao;

import java.sql.Connection;
import java.util.List;

public class CountryDaoImpl extends GenericDao<Country> implements CountryDao {
    Connection connection;

    public CountryDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Country> getALL() {
        String sql = "select * from geocountries_regions order by Country_RegionName";
        try {
            List<Country> countryList = this.queryForList(this.connection, sql);
            return countryList;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean countryExist(String iso) {
        String sql = "select * from geocountries_regions where ISO=?";
        try {
            List<Country> countryList = this.queryForList(connection, sql, iso);
            return countryList.size() != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
