package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.RoomBGRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;

import java.util.ArrayList;
import java.util.List;


//房间心动值统计窗口

public class RoomValueWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private RecyclerView recyclerview;
    private ImageView close_iv;
    private TextView clear_tv;
    private CheckBox  main_cb;
    private View conentView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private RoomBGRecyAdapter adapter;
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

    public RoomValueWindow(final Activity context, OnClickListener l,String showcharm) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_value_window, null);
        recyclerview = (RecyclerView) conentView.findViewById(R.id.recyclerview);
        main_cb = (CheckBox) conentView.findViewById(R.id.main_cb);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        clear_tv= (TextView) conentView.findViewById(R.id.clear_tv);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        close_iv.setTag(4);
        close_iv.setOnClickListener(l);

        clear_tv.setTag(5);
        clear_tv.setOnClickListener(l);

        main_cb.setTag(6);//心动值显示状态
        main_cb.setOnClickListener(l);
        Log.e("RoomValueWindow","showcharm="+showcharm);
        if(showcharm.equals("true")){
            main_cb.setChecked(true);
        }else {
            main_cb.setChecked(false);
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
