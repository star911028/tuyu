package com.fengyuxing.tuyu.bean;


public class FollowModel extends MyBaseModel {

    private String portraitPath;//":"http://www.tuerapp.com/img/user/1_1557384225187.png",
    private String expRank;//":0,
    private String gender;//":"男",
    private String description;//":"大家好，希望大家关注我了！",
    private String userId;//":1,
    private String age;//":19,
    private String username;//":"谁的胖子"

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getExpRank() {
        return expRank;
    }

    public void setExpRank(String expRank) {
        this.expRank = expRank;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
