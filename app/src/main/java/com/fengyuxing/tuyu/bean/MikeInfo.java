package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

public class MikeInfo extends MyBaseModel implements Serializable {

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
    private String status;//0

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
