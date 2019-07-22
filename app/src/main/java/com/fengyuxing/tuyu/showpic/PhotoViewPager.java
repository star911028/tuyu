package com.fengyuxing.tuyu.showpic;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by qiaoyuang on 2017/4/26.
 * ViewPager适配器
 */
public class PhotoViewPager extends ViewPager {
    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //图片可以放大 拖动
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            Log.e("onInterceptTouchEvent","ev="+ev);
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}