package com.fengyuxing.tuyu.yunxin;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

//定义一个自定义消息附件的基类，负责解析你的自定义消息的公用字段，比如类型等     注意: 实现 MsgAttachment 接口的成员都要实现 Serializable。
public  class CustomAttachment implements MsgAttachment {
    protected int type;
    private MainDataBean mainDataBean;
//    public CustomAttachment(int type) {
//        this.type = type;
//    }
    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }


    public void fromJson(String data) {
        if (data != null) {
            parseDataBg(data);
        }
    }

    public MainDataBean getMainDataBean() {
        return mainDataBean;
    }

    public void setMainDataBean(MainDataBean mainDataBean) {
        this.mainDataBean = mainDataBean;
    }

    @Override
    public String toJson(boolean send) {
        Log.e("toJson","toJson="+CustomAttachParser.packData( packData())+"  newdata="+JSON.toJSONString(mainDataBean));
        //toJson={"data":{"data":"{\"fromUser\":{\"expRank\":\"74\",\"portraitPath\":\"http://www.fengyugo.com:81/img/user/244349_1561952310533.jpg\",\"userId\":\"21\",\"username\":\"Joker185\"},\"type\":\"4\"}"}}
        // newdata={"fromUser":{"expRank":"74","portraitPath":"http://www.fengyugo.com:81/img/user/244349_1561952310533.jpg","userId":"21","username":"Joker185"},"type":"4"}
//        return CustomAttachParser.packData( packData());
        return JSON.toJSONString(mainDataBean);
    }
//    public int getType() {
//        return type;
//    }

    protected void parseData(JSONObject data) {//自己传的数据
        String object = data.getString("data");
        mainDataBean = JSON.parseObject(object,MainDataBean.class);
        Log.e("----",mainDataBean==null?"true":"false");
    }

    protected void parseDataBg(String  json) {//后台传的数据
        mainDataBean = JSON.parseObject(json,MainDataBean.class);
        Log.e("----",mainDataBean==null?"true":"false");
    }



    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("data", JSON.toJSONString(mainDataBean));
        return data;
    }
}