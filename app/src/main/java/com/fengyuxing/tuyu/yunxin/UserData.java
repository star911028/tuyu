package com.fengyuxing.tuyu.yunxin;

import java.io.Serializable;

public class UserData implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;

    private String expRank;//":282,
    private String portraitPath;//":1534,
    private String userId;//":"消息内容",
    private String username;//":282,


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getExpRank() {
        return expRank;
    }

    public void setExpRank(String expRank) {
        this.expRank = expRank;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
