package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.fengyuxing.tuyu.adapter.RoomListRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;

import java.util.ArrayList;
import java.util.List;


//房间设置窗口

public class RoomSetingWindow extends PopupWindow {
    private final LinearLayout pop_layout, baseinfo_ll,roombg_ll,roombgm_ll,value_ll,onlinenum_ll,admin_ll,closepin_ll;

    private View conentView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private RoomListRecyAdapter adapter;
    private List<DataList> data = new ArrayList<>();
    private TextView  showCharm_tv;
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

    public RoomSetingWindow(final Activity context, OnClickListener l,String showCharm) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_seting_window, null);
        baseinfo_ll = (LinearLayout) conentView.findViewById(R.id.baseinfo_ll);
        roombg_ll = (LinearLayout) conentView.findViewById(R.id.roombg_ll);
        roombgm_ll = (LinearLayout) conentView.findViewById(R.id.roombgm_ll);
        onlinenum_ll = (LinearLayout) conentView.findViewById(R.id.onlinenum_ll);
        admin_ll = (LinearLayout) conentView.findViewById(R.id.admin_ll);
        showCharm_tv = (TextView) conentView.findViewById(R.id.showCharm_tv);
        closepin_ll = (LinearLayout) conentView.findViewById(R.id.closepin_ll);
        value_ll = (LinearLayout) conentView.findViewById(R.id.value_ll);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        if(showCharm.equals("true")){
            showCharm_tv.setText("关闭公屏");
        }else {
            showCharm_tv.setText("打开公屏");
        }
        baseinfo_ll.setTag(4);//基本信息
        baseinfo_ll.setOnClickListener(l);

        roombg_ll.setTag(5);//背景音乐
        roombg_ll.setOnClickListener(l);

        roombgm_ll.setTag(6);//背景音乐
        roombgm_ll.setOnClickListener(l);

        onlinenum_ll.setTag(7);//在线人数
        onlinenum_ll.setOnClickListener(l);

        admin_ll.setTag(8);//管理员
        admin_ll.setOnClickListener(l);

        closepin_ll.setTag(9);//关闭公屏
        closepin_ll.setOnClickListener(l);

        value_ll.setTag(10);//心动值统计
        value_ll.setOnClickListener(l);



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
