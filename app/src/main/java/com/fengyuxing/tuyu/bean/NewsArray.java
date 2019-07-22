package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

public class NewsArray extends MyBaseModel implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;

    private String body;//消息内容
    private String expRank;//用户等级
    private String userid;//id

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExpRank() {
        return expRank;
    }

    public void setExpRank(String expRank) {
        this.expRank = expRank;
    }
}
