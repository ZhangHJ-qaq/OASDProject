package com.haojie.bean;

import org.omg.PortableInterceptor.INACTIVE;

public class SysMessage {
    private int messageID;
    private int receiverUID;
    private String messageContent;

    public SysMessage(){

    }

    public SysMessage(int receiverUID, String messageContent) {
        this.receiverUID = receiverUID;
        this.messageContent = messageContent;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(int receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
