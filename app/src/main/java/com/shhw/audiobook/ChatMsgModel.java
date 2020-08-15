package com.shhw.audiobook;

public class ChatMsgModel {
    String userName;
    String msg;
    long currentTimeMillis;

    public ChatMsgModel(String userName, String msg, long currentTimeMillis) {
        this.userName = userName;
        this.msg = msg;
        this.currentTimeMillis = currentTimeMillis;
    }

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public ChatMsgModel(String userName, String msg) {
        this.userName = userName;
        this.msg = msg;
    }

    public ChatMsgModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
