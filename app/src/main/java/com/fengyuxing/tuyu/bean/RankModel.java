package com.fengyuxing.tuyu.bean;

public class RankModel {

    private String code;//
    private RankDTO data;
    private String errorMsg;//

    public RankDTO getData() {
        return data;
    }

    public void setData(RankDTO data) {
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
