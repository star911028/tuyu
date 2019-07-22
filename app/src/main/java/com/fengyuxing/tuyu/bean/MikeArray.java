package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

public class MikeArray extends MyBaseModel implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;

    private String mikerId;//1,
    private String charm;//0,
    private String isBoss;//false,
    private String mikeNumber;//1,
    private String mikerName;//private String 谁的胖子private String ,
    private String mikeId;//5,
    private String isReceptionist;//true,
    private String mikerGender;//private String 男private String ,
    private String mikerPortraitPath;//private String http://www.tuerapp.com/img/user/1_1557384225187.pngprivate String ,
    private String status;//0正常  1闭麦 2锁麦
    private String needLine;//;//false,
    private String portraitPath;//
    private String giftId;//;//5,
    private String giftName;//;//"默默鼓掌",
    private String price;//;//100,
    private String imgPath;//;//"http://cdn.xdd-app.com/img/xdd/gift/guzhang.png"
    private String expRank;//14,
    private String diamond;//42,
    private String orderNumber;//1,
    private String gender;//"男",
    private String userId;//3,
    private String age;//18,
    private String username;//"Alexander"
    private String  isManager;//
    private String   isForbid;//
    private String   ishoster;
    private String     fileIdentifier;//
    private Boolean IsChecked=false;
    private String   count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Boolean getChecked() {
        return IsChecked;
    }

    public void setChecked(Boolean checked) {
        IsChecked = checked;
    }

    public String getFileIdentifier() {
        return fileIdentifier;
    }

    public void setFileIdentifier(String fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    public String getIshoster() {
        return ishoster;
    }

    public void setIshoster(String ishoster) {
        this.ishoster = ishoster;
    }

    public String getIsForbid() {
        return isForbid;
    }

    public void setIsForbid(String isForbid) {
        this.isForbid = isForbid;
    }

    public String getIsManager() {
        return isManager;
    }

    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }

    public String getExpRank() {
        return expRank;
    }

    public void setExpRank(String expRank) {
        this.expRank = expRank;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getNeedLine() {
        return needLine;
    }

    public void setNeedLine(String needLine) {
        this.needLine = needLine;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMikerId() {
        return mikerId;
    }

    public void setMikerId(String mikerId) {
        this.mikerId = mikerId;
    }

    public String getCharm() {
        return charm;
    }

    public void setCharm(String charm) {
        this.charm = charm;
    }

    public String getIsBoss() {
        return isBoss;
    }

    public void setIsBoss(String isBoss) {
        this.isBoss = isBoss;
    }

    public String getMikeNumber() {
        return mikeNumber;
    }

    public void setMikeNumber(String mikeNumber) {
        this.mikeNumber = mikeNumber;
    }

    public String getMikerName() {
        return mikerName;
    }

    public void setMikerName(String mikerName) {
        this.mikerName = mikerName;
    }

    public String getMikeId() {
        return mikeId;
    }

    public void setMikeId(String mikeId) {
        this.mikeId = mikeId;
    }

    public String getIsReceptionist() {
        return isReceptionist;
    }

    public void setIsReceptionist(String isReceptionist) {
        this.isReceptionist = isReceptionist;
    }

    public String getMikerGender() {
        return mikerGender;
    }

    public void setMikerGender(String mikerGender) {
        this.mikerGender = mikerGender;
    }

    public String getMikerPortraitPath() {
        return mikerPortraitPath;
    }

    public void setMikerPortraitPath(String mikerPortraitPath) {
        this.mikerPortraitPath = mikerPortraitPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
