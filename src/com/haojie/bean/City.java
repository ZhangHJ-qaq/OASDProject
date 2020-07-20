package com.haojie.bean;

public class City {
    int geoNameID;
    String asciiName;
    String country_regionCodeISO;
    double latitude;
    double longitude;
    String featureCode;
    String admin1Code;
    String admin2Code;
    int population;
    int elevation;
    String timeZone;

    public int getGeoNameID() {
        return geoNameID;
    }

    public void setGeoNameID(int geoNameID) {
        this.geoNameID = geoNameID;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    public String getCountry_regionCodeISO() {
        return country_regionCodeISO;
    }

    public void setCountry_regionCodeISO(String country_regionCodeISO) {
        this.country_regionCodeISO = country_regionCodeISO;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getAdmin1Code() {
        return admin1Code;
    }

    public void setAdmin1Code(String admin1Code) {
        this.admin1Code = admin1Code;
    }

    public String getAdmin2Code() {
        return admin2Code;
    }

    public void setAdmin2Code(String admin2Code) {
        this.admin2Code = admin2Code;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
