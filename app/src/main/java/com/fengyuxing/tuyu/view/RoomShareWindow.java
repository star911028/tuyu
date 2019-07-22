package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.fengyuxing.tuyu.adapter.RoomListRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



//房间分享窗口

public class RoomShareWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private final LinearLayout share_home_ll,share_friend_ll,navbar_ll,share_fans_ll,share_wx_ll,share_wxciec_ll,share_qq_ll,share_qqzone_ll,share_wb_ll,share_jb_ll,share_sc_ll;
    //    private ImageView  close_iv;
    private View conentView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private RoomListRecyAdapter adapter;
    private List<DataList> data = new ArrayList<>();
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

    public RoomShareWindow(final Activity context, OnClickListener l) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_share_window, null);
        share_home_ll= (LinearLayout) conentView.findViewById(R.id.share_home_ll);
        navbar_ll= (LinearLayout) conentView.findViewById(R.id.navbar_ll);
        share_friend_ll= (LinearLayout) conentView.findViewById(R.id.share_friend_ll);
        share_fans_ll= (LinearLayout) conentView.findViewById(R.id.share_fans_ll);
        share_wx_ll= (LinearLayout) conentView.findViewById(R.id.share_wx_ll);
        share_wxciec_ll= (LinearLayout) conentView.findViewById(R.id.share_wxciec_ll);
        share_qq_ll= (LinearLayout) conentView.findViewById(R.id.share_qq_ll);
        share_qqzone_ll= (LinearLayout) conentView.findViewById(R.id.share_qqzone_ll);
        share_wb_ll= (LinearLayout) conentView.findViewById(R.id.share_wb_ll);
        share_jb_ll= (LinearLayout) conentView.findViewById(R.id.share_jb_ll);
        share_sc_ll= (LinearLayout) conentView.findViewById(R.id.share_sc_ll);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);

        Boolean hasnavbar = checkDeviceHasNavigationBar(context);
        if (hasnavbar) {
            int hight = getNavigationBarHeight();//虚拟键的高度
            Log.e("hasnavbar", "有虚拟键" + " hight=" + hight);
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)navbar_ll.getLayoutParams();
            lp.height=hight;
            navbar_ll.setLayoutParams(lp);
        } else {
            Log.e("hasnavbar", "没有虚拟键");
        }
        share_home_ll.setTag(4);
        share_home_ll.setOnClickListener(l);
        share_friend_ll.setTag(5);
        share_friend_ll.setOnClickListener(l);
        share_fans_ll.setTag(6);
        share_fans_ll.setOnClickListener(l);
        share_wx_ll.setTag(7);
        share_wx_ll.setOnClickListener(l);
        share_wxciec_ll.setTag(8);
        share_wxciec_ll.setOnClickListener(l);
        share_qq_ll.setTag(9);
        share_qq_ll.setOnClickListener(l);
        share_qqzone_ll.setTag(10);
        share_qqzone_ll.setOnClickListener(l);
        share_wb_ll.setTag(11);
        share_wb_ll.setOnClickListener(l);
        share_jb_ll.setTag(12);
        share_jb_ll.setOnClickListener(l);
        share_sc_ll.setTag(13);
        share_sc_ll.setOnClickListener(l);



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
    private int getNavigationBarHeight() {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }
}
