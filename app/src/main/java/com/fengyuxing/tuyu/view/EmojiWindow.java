package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.fengyuxing.tuyu.adapter.EmojiRecyAdapter;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


//表情弹窗

public class EmojiWindow extends PopupWindow {
    private LinearLayout pop_layout;
    private View conentView;
    private Context context;
    private RecyclerView recycler_gift;
    private EmojiRecyAdapter emojirecyAdapter;
    private LinearLayoutManager layoutManager;
    private List<MikeArray> datas=new ArrayList<>();
    private List<MikeArray> picdata = new ArrayList<>();
    private String  MyMikeNumber="";
    private String  Mikenumber="";
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

    public EmojiWindow(final Activity context, OnClickListener l,final List<MikeArray> mikeArray) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.emoji_window, null);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        recycler_gift = (RecyclerView) conentView.findViewById(R.id.recycler_gift);


        //房间表情列表
        for (int i = 0; i < 18; i++) {
            MikeArray emo = new MikeArray();
            datas.add(emo);
        }
        emojirecyAdapter = new EmojiRecyAdapter(context, datas);
        recycler_gift.setLayoutManager(new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false));//设置横向滚动
        recycler_gift.setAdapter(emojirecyAdapter);

        for(int i=0;i<mikeArray.size();i++){
            if(mikeArray.get(i).getMikerId()!=null){
                if(mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())){
                    MyMikeNumber=mikeArray.get(i).getMikeNumber();//自己的座位不是索引
                }
            }

        }
        emojirecyAdapter.setOnItemClickListener(new EmojiRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//给麦位发送表情
                    Log.e("recyAdapterOnClick", "tag=" + tag+" MyMikeNumber="+MyMikeNumber);//tag 表情索引
                    MyApplication.getInstance().setMikeNumber(MyMikeNumber);
                    MyApplication.getInstance().setEmojiindex(tag+"");
                    EventBus.getDefault().post(new TabCheckEvent("表情"));
                    dismiss();
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
