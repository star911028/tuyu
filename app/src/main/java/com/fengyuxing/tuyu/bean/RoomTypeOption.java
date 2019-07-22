package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

/**
 * 社区发布动态获取预加载数据，如变化幅度等
 *
 * @author LGJ
 */
public class RoomTypeOption implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;


    private String name;//
    private String classifyId;//
    private String classifyName;
    private boolean checked;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
