package com.fengyuxing.tuyu.util;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class TabCheckEvent {
    private List<String> attachArray;//
    private String mMsg;
    private String mMsg1;
    private String mMsg2;

    public TabCheckEvent(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }

    public TabCheckEvent(String msg1, String msg2) {
        // TODO Auto-generated constructor stub
        mMsg1 = msg1;
        mMsg2 = msg2;
    }

    public TabCheckEvent(List<String> attachArrays) {
        // TODO Auto-generated constructor stub
        attachArray = attachArrays;
    }

    public String getMsg() {
        return mMsg;
    }

    public String getMsg1() {
        return mMsg1;
    }

    public String getMsg2() {
        return mMsg2;
    }

    public List<String> getArray() {
        return attachArray;
    }
}
