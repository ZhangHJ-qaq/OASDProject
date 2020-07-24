package com.haojie.bean;


public class FriendRecord {
    private int friendRecordID;
    private int UID1;
    private int UID2;

    public FriendRecord(){

    }

    public FriendRecord(int UID1, int UID2) {
        this.UID1 = UID1;
        this.UID2 = UID2;
    }

    public int getFriendRecordID() {
        return friendRecordID;
    }

    public void setFriendRecordID(int friendRecordID) {
        this.friendRecordID = friendRecordID;
    }

    public int getUID1() {
        return UID1;
    }

    public void setUID1(int UID1) {
        this.UID1 = UID1;
    }

    public int getUID2() {
        return UID2;
    }

    public void setUID2(int UID2) {
        this.UID2 = UID2;
    }
}
