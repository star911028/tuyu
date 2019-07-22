package com.fengyuxing.tuyu.yunxin;

import java.io.Serializable;

public  class MainDataBean implements  Serializable {

    private static final long serialVersionUID = 2437848784778095851L;
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    private String type;//":282,
    private String roomId;//":1534,
    private String body;//":"消息内容",
    private UserData toUser;//接收消息人信息
    private UserData fromUser;//发消息人信息
    private GiftData gift;//礼物信息
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UserData getToUser() {
        return toUser;
    }

    public void setToUser(UserData toUser) {
        this.toUser = toUser;
    }

    public UserData getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserData fromUser) {
        this.fromUser = fromUser;
    }

    public GiftData getGift() {
        return gift;
    }

    public void setGift(GiftData gift) {
        this.gift = gift;
    }



}
