package com.fengyuxing.tuyu.util;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class TabCheckEventList {
    private List<String> attachArray;//

    public TabCheckEventList(List<String> attachArrays) {
        attachArray = attachArrays;
    }

    public List<String>  getArray(){
        return attachArray;
    }
}
