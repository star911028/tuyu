package com.fengyuxing.tuyu.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.fengyuxing.tuyu.R;


//礼物动画弹窗

public class GiftWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private final LottieAnimationView animation_view;
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

    public GiftWindow(final Activity context, OnClickListener l, String giftid) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.gift_window, null);
        animation_view = (LottieAnimationView) conentView.findViewById(R.id.animation_view);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);

        if (giftid.equals("1")) {//播放礼物 玫瑰
            animation_view.setImageAssetsFolder("rose_images"); //java代码 设置路径
            animation_view.setAnimation("rose.json");
        } else if (giftid.equals("2")) {//播放礼物 情书
            animation_view.setImageAssetsFolder("letter_images"); //java代码 设置路径
            animation_view.setAnimation("letter.json");//设置动画文件
        } else if (giftid.equals("3")) {//播放礼物 666
            animation_view.setImageAssetsFolder("img"); //java代码 设置路径
            animation_view.setAnimation("animation_gift_666.json");
        } else if (giftid.equals("4")) {//播放礼物 么么哒
            animation_view.setImageAssetsFolder("kiss_images"); //java代码 设置路径
            animation_view.setAnimation("high_kiss.json");//设置动画文件
        } else if (giftid.equals("5")) {//播放礼物 丘比特
            animation_view.setImageAssetsFolder("cupid_images"); //java代码 设置路径
            animation_view.setAnimation("cupid.json");//设置动画文件
        } else if (giftid.equals("6")) {//播放礼物 520
            animation_view.setImageAssetsFolder("img"); //java代码 设置路径
            animation_view.setAnimation("520.json");//设置动画文件
        } else if (giftid.equals("7")) {//播放礼物 钻戒
            animation_view.setImageAssetsFolder("dia_images"); //java代码 设置路径
            animation_view.setAnimation("high_diamond.json");//设置动画文件
        } else if (giftid.equals("8")) {//播放礼物 一见倾心
            animation_view.setImageAssetsFolder("images_heart"); //java代码 设置路径
            animation_view.setAnimation("doubleheart.json");//设置动画文件
        } else if (giftid.equals("9")) {//播放礼物 火箭
            animation_view.setImageAssetsFolder("rocket"); //java代码 设置路径
            animation_view.setAnimation("rocket.json");
        } else if (giftid.equals("10")) {//播放礼物 城堡
            animation_view.setImageAssetsFolder("castle"); //java代码 设置路径
            animation_view.setAnimation("castle.json");//设置动画文件
        }

        animation_view.playAnimation();

        animation_view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override//动画监听
            public void onAnimationStart(Animator animation) {
                animation_view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation_view.setVisibility(View.GONE);
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

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
