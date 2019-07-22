package com.fengyuxing.tuyu.bean;

public class RoomModel {

    private String code;//
    private DataList data;
    private String errorMsg;//

    public DataList getData() {
        return data;
    }

    public void setData(DataList data) {
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
