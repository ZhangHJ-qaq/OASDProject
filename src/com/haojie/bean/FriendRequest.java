package com.haojie.bean;

public class FriendRequest {
    private int requestID;
    private int senderUID;
    private int receiverUID;
    private String senderUsername;//这是发请求的人的用户名。

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public FriendRequest() {

    }

    public FriendRequest(int senderUID, int receiverUID) {
        this.senderUID = senderUID;
        this.receiverUID = receiverUID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(int senderUID) {
        this.senderUID = senderUID;
    }

    public int getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(int receiverUID) {
        this.receiverUID = receiverUID;
    }


}
