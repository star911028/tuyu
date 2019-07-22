package com.fengyuxing.tuyu.bean;

import java.util.List;

public class MainGiftModel {

    private String code;//
    private List<MikeArray> data;//返回的商品精品信息列表
    private String errorMsg;//

    public List<MikeArray> getData() {
        return data;
    }

    public void setData(List<MikeArray> data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
