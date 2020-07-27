package com.haojie.dao.countryDao;

import com.haojie.bean.Country;

import java.util.List;

/**
 * The DAO layer for country
 */
public interface CountryDao {

    /**
     * Get all countries from the database.
     * @return List of all countries in the database
     */
    public abstract List<Country> getALL();


    /**
     * To check whether a certain country exists in the database
     * @param iso The ISO that represents the country
     * @return If exists,returns true. Otherwise, returns false.
     */
    public abstract boolean countryExist(String iso);
}
