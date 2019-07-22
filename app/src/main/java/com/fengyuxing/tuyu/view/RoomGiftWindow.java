package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;


//房间点击麦位用户礼物弹窗

public class RoomGiftWindow extends PopupWindow {
    private final LinearLayout pop_layout, info_ll;
    private View conentView;
    private Context context;
    private ImageView img_iv;
    private TextView setadmin_tv, follow_tv, pay_tv, give_tv, name_tv;
    private String showtext = "0";

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

    public RoomGiftWindow(final Activity context, OnClickListener l, String type, final int position, final List<MikeArray> mikeArray) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_gift_window, null);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        info_ll = (LinearLayout) conentView.findViewById(R.id.info_ll);
        img_iv = (ImageView) conentView.findViewById(R.id.img_iv);
        setadmin_tv = (TextView) conentView.findViewById(R.id.setadmin_tv);
        follow_tv = (TextView) conentView.findViewById(R.id.follow_tv);
        name_tv = (TextView) conentView.findViewById(R.id.name_tv);
        pay_tv = (TextView) conentView.findViewById(R.id.pay_tv);
        give_tv = (TextView) conentView.findViewById(R.id.give_tv);
        if (type.equals("1")) {//管理员
            setadmin_tv.setVisibility(View.VISIBLE);
        } else {//普通用户
            setadmin_tv.setVisibility(View.GONE);
        }
        name_tv.setText(mikeArray.get(position).getMikerName());
        Glide.with(context)
                .load(mikeArray.get(position).getMikerPortraitPath())
                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                .into(img_iv);
        setadmin_tv.setTag(4);//管理
        setadmin_tv.setOnClickListener(l);



        info_ll.setTag(6);//用户信息
        info_ll.setOnClickListener(l);


        follow_tv.setOnClickListener(new View.OnClickListener() {
            @Override//关注
            public void onClick(View v) {
                if (follow_tv.getText().equals("+关注")) {//取消关注
                    showtext = "1";
                    getData(handler, mikeArray.get(position).getMikerId(), "1");
                } else {//关注
                    showtext = "0";
                    getData(handler, mikeArray.get(position).getMikerId(), "0");

                }

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

    /**
     * 发起网络请求
     */
    public static void getData(final Handler handler, final String followId, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("followId", followId).build();
                    String Url = "";
                    if (type.equals("0")) {
                        Url = NetConstant.API_FollowUser;//关注
                    } else {
                        Url = NetConstant.API_CancelFollowUser;//取消关注
                    }
                    final Request request = new Request.Builder().url(Url).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("getData", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(22, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 接收解析后传过来的数据
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 22) {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    if (showtext.equals("0")) {
                        follow_tv.setText("+关注");
                    } else {
                        follow_tv.setText("取消关注");
                    }
                    dismiss();
                } else {
                    Toast.makeText(context, model.getErrorMsg(), Toast.LENGTH_SHORT);
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            }
        }
    };

}
