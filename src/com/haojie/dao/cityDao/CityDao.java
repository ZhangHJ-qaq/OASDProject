package com.haojie.dao.cityDao;

import com.haojie.bean.City;

import java.util.List;


/**
 * The data access object designed for the table geocities in database
 */
public interface CityDao {
    /**
     * This will return a list of cities whose ISO can match the parameter you pass into.
     * @param iso ISO of a country or region
     * @return A list of cities
     */
    public abstract List<City> getCities(String iso);

    /**
     * To check whether the city belongs to the country
     * @param cityCode The citycode that can represent the city
     * @param iso The ISO that represents the country
     * @return If the city belongs to the country, returns true. Otherwise, it returns false.
     */
    public abstract boolean countryCityMatch(int cityCode,String iso);
}
