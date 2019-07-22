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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;

import java.util.Timer;
import java.util.TimerTask;

import lsp.com.lib.PasswordInputEdt;


//进入房间输入密码弹窗

public class NeedPWDWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private final TextView enter_tv;
    private View conentView;
    private Context context;
    private  ImageView close_iv;
    private PasswordInputEdt edt;
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

    public NeedPWDWindow(final Activity context, OnClickListener l) {
        this.context = context;
        MyApplication.getInstance().setInputpass("");//进入时先重置
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.needpwd_window, null);
        enter_tv = (TextView) conentView.findViewById(R.id.enter_tv);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        edt = (PasswordInputEdt) conentView.findViewById(R.id.edt);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        enter_tv.setEnabled(false);
        close_iv.setTag(4);
        close_iv.setOnClickListener(l);
        enter_tv.setTag(5);
        enter_tv.setOnClickListener(l);
        //输入框自动获取焦点并且弹出软键盘
        edt.setFocusable(true);
        edt.setFocusableInTouchMode(true);
        edt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);//这里的100是显示后多久弹出软键盘100=0.1/s


        edt.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                Log.e("NeedPWDWindow","text="+text);
                MyApplication.getInstance().setInputpass(text);
                enter_tv.setEnabled(true);
            }
        });

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x01000000);
        this.setBackgroundDrawable(dw);

        conentView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = conentView.findViewById(R.id.ipopwindowlayout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
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
