package com.fengyuxing.tuyu.yunxin;

import com.alibaba.fastjson.JSONObject;

import java.util.Random;

//第三步，继承这个基类，实现“骰子”的附件类型。注意，成员变量都要实现 Serializable。
public class CrapsAttachment extends CustomAttachment{
    public enum Craps {
        one(1, "1"),
        two(2, "2"),
        three(3, "3"),
        four(4,"4"),
        five(5,"5"),
        six(6,"6"),
        ;
        private int value;
        private String desc;
        Craps(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
        static Craps enumOfValue(int value) {
            for (Craps direction : values()){
                if (direction.getValue() == value) {
                    return direction;
                }
            }
            return one;
        }
        public int getValue() {
            return value;
        }
        public String getDesc() {
            return desc;
        }
    }
    private Craps value;
    public CrapsAttachment() {
//        super(CustomAttachmentType.Guess);
        random();
    }
    @Override
    protected void parseData(JSONObject data) {
        value = Craps.enumOfValue(data.getIntValue("value"));
    }
    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("value", value.getValue());
        return data;
    }
    private void random() {
        int value = new Random().nextInt(6) + 1;
        this.value = Craps.enumOfValue(value);
    }
    public Craps getValue() {
        return value;
    }
}