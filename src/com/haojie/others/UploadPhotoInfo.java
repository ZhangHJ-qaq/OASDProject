package com.haojie.others;

import com.haojie.dao.cityDao.CityDao;
import com.haojie.dao.cityDao.CityDaoImpl;
import com.haojie.dao.countryDao.CountryDao;
import com.haojie.dao.countryDao.CountryDaoImpl;
import com.haojie.dao.imageDao.ImageDao;
import com.haojie.dao.imageDao.ImageDaoImpl;
import com.haojie.exception.*;
import com.haojie.utils.MD5Utils;
import com.haojie.utils.MyUtils;

import javax.servlet.http.Part;
import java.io.File;
import java.security.SecureRandom;
import java.sql.Connection;

/**
 * The object that encapsulates all the uploaded info about an image to be uploaded.
 */
public class UploadPhotoInfo {
    private String title;
    private String description;
    private String city;
    private String country;
    private String content;
    private Part photo;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UploadPhotoInfo(String title, String description, String city, String country, String content, Part photo) {
        this.title = title;
        this.description = description;
        this.city = city;
        this.country = country;
        this.content = content;
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Part getPhoto() {
        return photo;
    }

    public void setPhoto(Part photo) {
        this.photo = photo;
    }


    public void cleanXSS() {
        this.city = MyUtils.cleanXSS(this.city);
        this.country = MyUtils.cleanXSS(this.country);
        this.content = MyUtils.cleanXSS(this.content);
        this.description = MyUtils.cleanXSS(this.description);
        this.title = MyUtils.cleanXSS(this.title);
    }

    public void checkCountryAndCity(Connection connection) throws CountryCityMismatchException {

        CountryDao countryDao = new CountryDaoImpl(connection);
        CityDao cityDao = new CityDaoImpl(connection);
        if (!countryDao.countryExist(this.country)) {
            throw new CountryCityMismatchException("国家不存在！");
        }

        if (!cityDao.countryCityMatch(Integer.parseInt(this.city), this.country)) {
            throw new CountryCityMismatchException("国家和城市不对应！");
        }


    }

    public void checkComplete() throws PhotoInfoIncompleteException {

        if (this.title == null || this.title.equals("")) {
            throw new PhotoInfoIncompleteException("图片标题没有填写");
        }
        if (this.description == null || this.description.equals("")) {
            throw new PhotoInfoIncompleteException("图片描述没有填写");
        }
        if (this.content == null || this.content.equals("")) {
            throw new PhotoInfoIncompleteException("图片主题没有填写");
        }
        if (this.country == null || this.country.equals("")) {
            throw new PhotoInfoIncompleteException("图片国家没有填写");
        }
        if (this.city == null || this.city.equals("")) {
            throw new PhotoInfoIncompleteException("图片城市没有填写");
        }


    }

    public void checkType() throws TypeIncorrectException {
        String type = this.photo.getContentType();
        if (!type.equalsIgnoreCase("image/gif") &&
                !type.equalsIgnoreCase("image/png") &&
                !type.equalsIgnoreCase("image/jpeg")) {
            throw new TypeIncorrectException("看清楚！这个要图片！");
        }

    }

    public String generateFileName(Connection connection) {
        String extName = MyUtils.getExtName(this.photo.getSubmittedFileName());
        ImageDao imageDao = new ImageDaoImpl(connection);
        String filename = "";
        while (true) {
            filename = MD5Utils.MD5(new SecureRandom().nextLong() + "") + "." + extName;
            if (!imageDao.imageExists(filename)) break;
        }
        this.filename = filename;
        return filename;

    }

    public void checkFileExists() throws FileNotExistException {
        String name = this.photo.getSubmittedFileName();
        if (name == null) {
            throw new FileNotExistException("你没有上传文件！");
        }

    }

    public void checkFileSize() throws SizeToLargeException {
        long size = this.photo.getSize();

        if(size>10*1024*1024){
            throw new SizeToLargeException("文件大小超过了允许范围。本站最多只允许上传大小为10MB的图片。");
        }

    }


}
