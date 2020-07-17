package com.haojie.bean;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
    private int uid;
    private String username;
    private String email;
    private String pass;
    private int state;
    private Timestamp dateJoined;
    private Timestamp dateLastModified;

    public User(int uid, String username, String email, String pass, int state, Timestamp dateJoined, Timestamp dateLastModified, String salt, String sessionID) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.state = state;
        this.dateJoined = dateJoined;
        this.dateLastModified = dateLastModified;
        this.salt = salt;
        this.sessionID = sessionID;
    }

    public User(String username, String email, String pass, int state, Timestamp dateJoined, Timestamp dateLastModified, String salt, String sessionID) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.state = state;
        this.dateJoined = dateJoined;
        this.dateLastModified = dateLastModified;
        this.salt = salt;
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    private String salt;
    private String sessionID;

    public User() {

    }

    public Timestamp getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Timestamp getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Timestamp dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
