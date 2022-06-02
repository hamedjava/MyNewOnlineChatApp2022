package com.example.newonlinechatapp2022;

public class MessagesModel {

    private String message;
    private String senderID;
    private long timeStamp;
    private String currentTime;

    public MessagesModel(String message, String senderID, long timeStamp, String currentTime) {
        this.message = message;
        this.senderID = senderID;
        this.timeStamp = timeStamp;
        this.currentTime = currentTime;
    }

    public MessagesModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
