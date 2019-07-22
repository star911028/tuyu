package com.fengyuxing.tuyu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    private Message message;

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收MainActivity传过来的数据
//        Toast.makeText(context, intent.getStringExtra("sessionId"), Toast.LENGTH_SHORT).show();

        //调用Message接口的方法
        message.getMsg(intent.getStringExtra("sessionId"),intent.getStringExtra("sessionName"));
    }

    public interface Message {
         void getMsg(String str1,String str2);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}