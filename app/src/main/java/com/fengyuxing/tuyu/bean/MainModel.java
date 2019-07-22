package com.fengyuxing.tuyu.bean;

public class MainModel {

    private String code;//
    private MainDTO data;
    private String errorMsg;//

    public MainDTO getData() {
        return data;
    }

    public void setData(MainDTO data) {
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
