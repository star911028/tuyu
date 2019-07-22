package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

/**
 * Created by LGJ on 2018/6/26.
 */

public class Config implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;


    private String phone;
    private String qrcode;
    private String yaoqcode;
    private String spare1;//微信公众号
    private String spare2;//点赞积分
    private String spare3;//踩积分
    private String spare4;//评论积分
    private String spare5;

    public static String phone_default = "18774989918";
    public static String qrcode_default = "http://static.shangche.im/scsk/web/images/qrcode.png";// "qrcode":"http://static.shangche.im/scsk/web/images/qrcode.png",   http://www.yomeshare.com/qrcode.png
    public static String wx_default = "121408031";
    public static String my_yaoqcode = "7powhx";
    public static String score_zan_default = "1";
    public static String score_cai_default = "1";
    public static String score_comment_default = "2";

    public String getYaoqcode() {
        return yaoqcode;
    }

    public void setYaoqcode(String yaoqcode) {
        this.yaoqcode = yaoqcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSpare1() {
        return spare1;
    }

    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public void setSpare3(String spare3) {
        this.spare3 = spare3;
    }

    public String getSpare4() {
        return spare4;
    }

    public void setSpare4(String spare4) {
        this.spare4 = spare4;
    }

    public String getSpare5() {
        return spare5;
    }

    public void setSpare5(String spare5) {
        this.spare5 = spare5;
    }
}
