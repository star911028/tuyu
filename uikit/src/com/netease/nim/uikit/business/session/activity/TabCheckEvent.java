package com.netease.nim.uikit.business.session.activity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class TabCheckEvent {
    private List<String> attachArray;//
    private String mMsg;
    public TabCheckEvent(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }
    public TabCheckEvent(List<String> attachArrays) {
        // TODO Auto-generated constructor stub
        attachArray = attachArrays;
    }
    public String getMsg(){
        return mMsg;
    }

    public List<String>  getArray(){
        return attachArray;
    }
}
