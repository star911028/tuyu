package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;

public class StartActivity extends BaseActivity implements View.OnClickListener {
    //启动页
    private static final String TAG = "StartActivity";
//    private  Handler handler;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
    //禁用返回键
    @Override
    public void onBackPressed() {

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initData() {
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
    }


    @Override
    protected void initEventListeners() {
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        initviewdata();
    }

    public void initviewdata(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.getInstance().getUserId() != null && MyApplication.getInstance().getUserId().length() > 0) {
                    Intent intent = new Intent(mContext, MainActivity.class);//主页
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);//登陆页面
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000); // 延时2秒
    }
}
