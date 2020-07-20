package com.haojie.dao.cityDao;

import com.haojie.bean.City;

import java.util.List;

public interface CityDao {
    public abstract List<City> getCities(String iso);

    public abstract boolean countryCityMatch(int cityCode,String iso);
}
