package com.haojie.config;

/**
 * 此处存储的是数据库连接有关的常量
 */
public class Config {
    public static final String jdbcURL = "jdbc:mysql://localhost/travel?serverTimezone=GMT%2B8";
    public static final String username = "root";
    public static final String password = "000000";
    public static final String driverClassName = "com.mysql.cj.jdbc.Driver";
    public static final int maxPoolSize=50;
    public static final int minIdle=10;
}
