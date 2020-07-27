package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.haojie.bean.City;
import com.haojie.bean.Country;
import com.haojie.bean.Image;
import com.haojie.bean.User;
import com.haojie.dao.cityDao.CityDao;
import com.haojie.dao.cityDao.CityDaoImpl;
import com.haojie.dao.countryDao.CountryDao;
import com.haojie.dao.countryDao.CountryDaoImpl;
import com.haojie.service.UserService;
import org.apache.commons.dbutils.DbUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

/**
 * The servlet of country and city to handle with ajax requests
 */
@WebServlet("/CountryCityServlet")
public class CountryCityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String method = request.getParameter("method");
            if (method.equals("erjiliandong")) {
                erjiliandong(request, response);
                return;
            }
            if (method.equals("getCountryOptions")) {
                getCountryOptions(request, response);
            }


        } catch (Exception ignored) {
            return;
        }

    }

    private void erjiliandong(HttpServletRequest request, HttpServletResponse response) {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String iso = request.getParameter("iso");
            CityDao cityDao = new CityDaoImpl(connection);
            List<City> cityList = cityDao.getCities(iso);

            PrintWriter out = response.getWriter();
            out.println(JSON.toJSON(cityList));

        } catch (Exception ignored) {
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private void getCountryOptions(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            CountryDao countryDao = new CountryDaoImpl(connection);
            List<Country> countryList = countryDao.getALL();
            out.println(JSON.toJSON(countryList));
        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }
    }


}
