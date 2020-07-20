package com.haojie.dao.countryDao;

import com.haojie.bean.Country;

import java.util.List;

public interface CountryDao {
    public abstract List<Country> getALL();

    public abstract boolean countryExist(String iso);
}
