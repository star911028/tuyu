package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

public class GiftList implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;



    private String giftpic;//":"http://www.otctu.com/img/user/2_1557385420195.png",
    private String giftCount;//":33,




    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGiftpic() {
        return giftpic;
    }

    public void setGiftpic(String giftpic) {
        this.giftpic = giftpic;
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(String giftCount) {
        this.giftCount = giftCount;
    }
}
