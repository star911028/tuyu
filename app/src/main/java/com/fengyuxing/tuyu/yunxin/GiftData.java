package com.fengyuxing.tuyu.yunxin;

import java.io.Serializable;

public class GiftData implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;

    private String giftId;//":282,
    private String count;//":1534,
    private String   fileIdentifier;
    private String  giftName;

    public String getFileIdentifier() {
        return fileIdentifier;
    }

    public void setFileIdentifier(String fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
