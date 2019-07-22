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

import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MikeArray;

import java.util.List;


//直播间 管理员点击空麦位  点击麦上用户 弹窗

public class RoomAdminSetWindow extends PopupWindow {
    private final LinearLayout pop_layout, mai_nopeo_ll, mai_haspeo_ll;
    private TextView mai_title_tv, mai_up_tv, mai_uppeo_tv, mai_pai_tv, mai_boss_tv, mai_zc_tv, mai_lock_tv, mai_setadmin_tv, mai_black_tv, mai_close_talk_tv;
    private TextView cancel_tv;
    private View conentView;
    private Context context;

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

    public RoomAdminSetWindow(final Activity context, OnClickListener l, String type, int index, List<MikeArray> mikeArray) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.roomadmin_set_window, null);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        mai_nopeo_ll = (LinearLayout) conentView.findViewById(R.id.mai_nopeo_ll);
        mai_haspeo_ll = (LinearLayout) conentView.findViewById(R.id.mai_haspeo_ll);
        cancel_tv = (TextView) conentView.findViewById(R.id.cancel_tv);
        mai_title_tv = (TextView) conentView.findViewById(R.id.mai_title_tv);
        mai_up_tv = (TextView) conentView.findViewById(R.id.mai_up_tv);
        mai_uppeo_tv = (TextView) conentView.findViewById(R.id.mai_uppeo_tv);
        mai_pai_tv = (TextView) conentView.findViewById(R.id.mai_pai_tv);
        mai_boss_tv = (TextView) conentView.findViewById(R.id.mai_boss_tv);
        mai_zc_tv = (TextView) conentView.findViewById(R.id.mai_zc_tv);
        mai_lock_tv = (TextView) conentView.findViewById(R.id.mai_lock_tv);

        mai_setadmin_tv = (TextView) conentView.findViewById(R.id.mai_setadmin_tv);
        mai_close_talk_tv = (TextView) conentView.findViewById(R.id.mai_close_talk_tv);
        mai_black_tv = (TextView) conentView.findViewById(R.id.mai_black_tv);
        if (index >= 0) {
            mai_title_tv.setText(index + 1 + "号麦");//显示标题
            if (type.equals("1")) {//麦上无人的操作显示
                mai_nopeo_ll.setVisibility(View.VISIBLE);
                mai_haspeo_ll.setVisibility(View.GONE);
            } else if (type.equals("2")) {//麦上有人的操作显示
                mai_nopeo_ll.setVisibility(View.GONE);
                mai_haspeo_ll.setVisibility(View.VISIBLE);
            }

            if (mikeArray.get(index).getIsReceptionist().equals("true")) {
                mai_zc_tv.setText("取消设置主持位");
            } else {
                mai_zc_tv.setText("设置主持位");
            }
            if (mikeArray.get(index).getIsBoss().equals("true")) {
                mai_boss_tv.setText("取消设置老板位");
            } else {
                mai_boss_tv.setText("设置老板位");
            }
            if (mikeArray.get(index).getNeedLine().equals("true")) {
                mai_pai_tv.setText("取消需排麦上麦");
            } else {
                mai_pai_tv.setText("需排麦上麦");
            }

            ///0正常  1闭麦 2锁麦
            if (mikeArray.get(index).getStatus().equals("0")) {
                mai_lock_tv.setText("锁麦");
            } else if (mikeArray.get(index).getStatus().equals("2")) {
                mai_lock_tv.setText("取消锁麦");
            } else if (mikeArray.get(index).getStatus().equals("1")) {
                mai_lock_tv.setText("取消闭麦");
            }

            if (mikeArray.get(index).getIsManager() != null) {
                if (mikeArray.get(index).getIsManager().equals("true")) {
                    mai_setadmin_tv.setText("撤销管理员");
                } else {
                    mai_setadmin_tv.setText("设置管理员");
                }
            }
        } else {
            mai_title_tv.setText("管理");//显示标题
            Log.e("查看当前人员","getLookforManger="+MyApplication.getInstance().getLookforManger());
            if (MyApplication.getInstance().getLookforManger().equals("true")) {
                mai_setadmin_tv.setText("撤销管理员");
            } else {
                mai_setadmin_tv.setText("设置管理员");
            }
        }
//        if(mikeArray.get(index).getIsForbid()!=null){
//            if (mikeArray.get(index).getIsForbid().equals("true")) {
//                mai_close_talk_tv.setText("取消禁言");
//            } else {
//                mai_close_talk_tv.setText("禁言");
//            }
//        }
        cancel_tv.setTag(4);//取消
        cancel_tv.setOnClickListener(l);
        mai_up_tv.setTag(5);//上麦
        mai_up_tv.setOnClickListener(l);
        mai_uppeo_tv.setTag(6);//抱用户上麦
        mai_uppeo_tv.setOnClickListener(l);
        mai_pai_tv.setTag(7);//需要排麦上麦
        mai_pai_tv.setOnClickListener(l);
        mai_boss_tv.setTag(8);//设置老板位
        mai_boss_tv.setOnClickListener(l);
        mai_zc_tv.setTag(9);//设置主持位
        mai_zc_tv.setOnClickListener(l);
        mai_lock_tv.setTag(10);//锁麦
        mai_lock_tv.setOnClickListener(l);

        mai_setadmin_tv.setTag(11);//设为管理员
        mai_setadmin_tv.setOnClickListener(l);
        mai_close_talk_tv.setTag(12);//禁言
        mai_close_talk_tv.setOnClickListener(l);
        mai_black_tv.setTag(14);//从房间中拉黑
        mai_black_tv.setOnClickListener(l);


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
