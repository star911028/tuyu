package com.fengyuxing.tuyu.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;


import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";


    private int[] soundgif = {R.drawable.speak_boy_1, R.drawable.speak_boy_2, R.drawable.speak_boy_3, R.drawable.speak_boy_4, R.drawable.speak_boy_5, R.drawable.speak_boy_6, R.drawable.speak_boy_7, R.drawable.speak_boy_8, R.drawable.speak_boy_9
            , R.drawable.speak_boy_10, R.drawable.speak_boy_11, R.drawable.speak_boy_12, R.drawable.speak_boy_13, R.drawable.speak_boy_14, R.drawable.speak_boy_15};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initView(Bundle savedInstanceState) {
    }


    @Override
    protected void initEventListeners() {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
