package com.haojie.config;

/**
 * Here we have some constants related to the parameters you need to connect to the database!
 */
public class Config {
    public static final String jdbcURL = "jdbc:mysql://localhost/travel?serverTimezone=GMT%2B8";

    //You can change the username, password and driver class name according to your runtime environment
    public static final String username = "root";
    public static final String password = "000000";

    public static final String driverClassName = "com.mysql.jdbc.Driver";

}
