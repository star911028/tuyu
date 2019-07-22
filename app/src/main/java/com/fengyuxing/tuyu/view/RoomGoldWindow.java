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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.fengyuxing.tuyu.R;

import cn.iwgang.countdownview.CountdownView;


//神豪弹出窗口

public class RoomGoldWindow extends PopupWindow {
    private final RelativeLayout has_gold_rv,no_gold_rv;
    private final FrameLayout gold_fl;
    private ImageView imggold_iv;
    private View conentView;
    private Context context;
    private TextView title_tv;
    private CountdownView countdown_v;

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

    public RoomGoldWindow(final Activity context, OnClickListener l, String imgpath, String goldname, String goldtime) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_gold_window, null);
        title_tv = (TextView) conentView.findViewById(R.id.title_tv);
        imggold_iv = (ImageView) conentView.findViewById(R.id.imggold_iv);
        countdown_v = (CountdownView) conentView.findViewById(R.id.countdown_v);
        has_gold_rv = (RelativeLayout) conentView.findViewById(R.id.has_gold_rv);
        no_gold_rv = (RelativeLayout) conentView.findViewById(R.id.no_gold_rv);
        gold_fl= (FrameLayout) conentView.findViewById(R.id.gold_fl);
        imggold_iv.setTag(4);
        imggold_iv.setOnClickListener(l);
        if (goldname != null) {
            has_gold_rv.setVisibility(View.VISIBLE);
            gold_fl.setVisibility(View.VISIBLE);
            no_gold_rv.setVisibility(View.GONE);
            title_tv.setText(goldname);
            long time = Long.valueOf(goldtime).longValue()*1000;
            Log.e("RoomGoldWindow","time="+time+"  goldtime="+goldtime);
            countdown_v.start(time); // Millisecond  开始倒计时 单位毫秒
//            if (!imgpath.equals(imggold_iv.getTag())) {//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                imggold_iv.setTag(null);//需要清空tag，否则报错
//                Glide.with(context)
//                        .load(imgpath)
//                        .apply(new RequestOptions().placeholder(R.mipmap.boos_1).error(R.mipmap.boos_1).dontAnimate())
//                        .into(imggold_iv);
//                imggold_iv.setTag(imgpath);
//            }
//            imggold_iv.setTag(4);//

            Picasso.with(context)
                    .load(imgpath)//加载过程中的图片显示
                    .placeholder(R.mipmap.rabblt_icon)
//加载失败中的图片显示
//如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                    .error(R.mipmap.rabblt_icon)
                    .into(imggold_iv);
        } else {
            has_gold_rv.setVisibility(View.GONE);
            no_gold_rv.setVisibility(View.VISIBLE);
            gold_fl.setVisibility(View.GONE);
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
