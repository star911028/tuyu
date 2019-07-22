package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;


//版本更新窗口

public class UpdateWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private final TextView content_tv,version_code_tv,sure_bt;
    private View conentView;
    private Context context;
    private Boolean IsmustUpdate=false;
    public void setDrawableAndTextColor(int id, TextView textview) {
        Drawable drawable = context.getResources().getDrawable(id);
        textview.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        textview.setTextColor(context.getResources().getColor(R.color.color_text_02));
    }

    public void setDrawableAndTextColor(int id, TextView textview, int color) {
        Drawable drawable = context.getResources().getDrawable(id);
        textview.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        textview.setTextColor(color);
    }

    public UpdateWindow(final Activity context, OnClickListener l,String type,String content,String versioncode) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.update_window, null);
        content_tv = (TextView) conentView.findViewById(R.id.content_tv);
        version_code_tv= (TextView) conentView.findViewById(R.id.version_code_tv);
        sure_bt= (TextView) conentView.findViewById(R.id.sure_bt);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        sure_bt.setTag(5);
        sure_bt.setOnClickListener(l);
        content_tv.setText(content);//更新内容
        version_code_tv.setText("发现新版本 V"+versioncode);//版本号
        if(type.equals("1")){//强制更新
            IsmustUpdate=true;
        }else {//非强制更新
            IsmustUpdate=false;
        }


        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        if(!IsmustUpdate){//控制点击键盘返回是否有效 true有效   false无效
            this.setFocusable(true);//
        }else {
            this.setFocusable(false);
        }
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x01000000);
        this.setBackgroundDrawable(dw);
        // 设置PopupWindow是否能响应外部点击事件


        conentView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("onTouch","getAction"+event.getAction());
                int height = conentView.findViewById(R.id.ipopwindowlayout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        if(!IsmustUpdate){
                            dismiss();
                        }
                    }
                }
                return true;
            }
        });

    }


    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }
}
