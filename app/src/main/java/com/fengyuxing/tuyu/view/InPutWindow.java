package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;


//输入框

public class InPutWindow extends PopupWindow {
    private final LinearLayout pop_layout, send_ll;
    private final LottieAnimationView animation_view;
    private View conentView;
    private Context context;
    private TextView tv_submit;
    private EditText et_content;
    private ImageView send_iv;

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

    public InPutWindow(final Activity context, OnClickListener l, String text) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.input_window, null);
        animation_view = (LottieAnimationView) conentView.findViewById(R.id.animation_view);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        send_ll = (LinearLayout) conentView.findViewById(R.id.send_ll);
        send_iv = (ImageView) conentView.findViewById(R.id.send_iv);
        tv_submit = (TextView) conentView.findViewById(R.id.tv_submit);
        et_content = (EditText) conentView.findViewById(R.id.et_content);

        if (text != null && text.length() > 0) {
            et_content.setText("@" + text + " ");
            et_content.setSelection(et_content.getText().toString().length());
//            et_content.requestFocus();//
        } else {

//            et_content.requestFocus();//
        }
//        tv_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //发送消息
//                if (et_content.getText().toString().trim().length() == 0) {
//                    EventBus.getDefault().post(new TabCheckEvent("提示" + "请输入发送内容"));
//                    dismiss();
//                } else {
//                    EventBus.getDefault().post(new TabCheckEvent("发送消息" + et_content.getText().toString().trim()));
//                    dismiss();
//                }
//
//            }
//        });
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (et_content.getText().toString().trim().length() > 0) {
                    send_iv.setImageResource(R.mipmap.send_blue);
                } else {
                    send_iv.setImageResource(R.mipmap.send_gray);
                }
            }
        });
        send_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送消息
                if (et_content.getText().toString().trim().length() == 0) {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + "请输入发送内容"));
                    dismiss();
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("发送消息" + et_content.getText().toString().trim()));
                    dismiss();
                }

            }
        });
        TimerShowKeyboard(et_content);//弹出输入框延时1.5秒

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_content.requestFocus();//获取焦点 延时2秒
            }
        }, 500); // 延时2秒


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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                et_content.requestFocus();// //UI更改操作
            }
        }
    };

    //通过定时器强制打开虚拟键盘
    public static void TimerShowKeyboard(final EditText v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!imm.isActive(v)) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 300);
    }


}
