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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;


//查看房主信息

public class RoomLookInfoWindowRoomHost extends PopupWindow {
    private final LinearLayout pop_layout, admin_ll, bottom_ll;
    private ImageView close_iv, imggold_iv, level_iv;
    private View conentView;
    private Context context;
    private TextView type_tv, follow_tv, at_tv, title_tv, name_tv, letmeup_tv, closemai_tv, maidown_tv, talk_tv, sex_tv, level_tv, tuercode_tv, fans_tv, desc_tv;
    private retrofit2.Call<String> call;
    private retrofit2.Response<String> response;
    private String result;
    private View view;

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

    public RoomLookInfoWindowRoomHost(final Activity context, OnClickListener l, final String lookid, Boolean isManager) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_lookinfo_window_hoster, null);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        level_iv = (ImageView) conentView.findViewById(R.id.level_iv);
        imggold_iv = (ImageView) conentView.findViewById(R.id.imggold_iv);
        type_tv = (TextView) conentView.findViewById(R.id.type_tv);
        title_tv = (TextView) conentView.findViewById(R.id.title_tv);
        at_tv = (TextView) conentView.findViewById(R.id.at_tv);
        name_tv = (TextView) conentView.findViewById(R.id.name_tv);
        talk_tv = (TextView) conentView.findViewById(R.id.talk_tv);
        view = (View) conentView.findViewById(R.id.view);
        sex_tv = (TextView) conentView.findViewById(R.id.sex_tv);
        level_tv = (TextView) conentView.findViewById(R.id.level_tv);
        letmeup_tv = (TextView) conentView.findViewById(R.id.letmeup_tv);
        closemai_tv = (TextView) conentView.findViewById(R.id.closemai_tv);
        maidown_tv = (TextView) conentView.findViewById(R.id.maidown_tv);
        tuercode_tv = (TextView) conentView.findViewById(R.id.tuercode_tv);
        fans_tv = (TextView) conentView.findViewById(R.id.fans_tv);
        follow_tv = (TextView) conentView.findViewById(R.id.follow_tv);
        desc_tv = (TextView) conentView.findViewById(R.id.desc_tv);
        admin_ll = (LinearLayout) conentView.findViewById(R.id.admin_ll);
        bottom_ll = (LinearLayout) conentView.findViewById(R.id.bottom_ll);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);

        if(isManager){//管理员打开
            title_tv.setVisibility(View.VISIBLE);
        }else {//非管理员打开
            title_tv.setVisibility(View.INVISIBLE);
        }
        close_iv.setTag(4);//
        close_iv.setOnClickListener(l);
        talk_tv.setTag(9);//私聊
        talk_tv.setOnClickListener(l);
        imggold_iv.setTag(10);//
        imggold_iv.setOnClickListener(l);
        title_tv.setTag(11);//管理
        title_tv.setOnClickListener(l);
        at_tv.setTag(12);//@ta
        at_tv.setOnClickListener(l);
        Log.e("okHttp_synchronousGet", "lookid=" + lookid);
        okHttp_synchronousGet(handler, lookid);





        //关注 取消关注
        follow_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow_tv.getText().toString().equals("关注")) {
                    FollowUser(handler, lookid);
                } else if (follow_tv.getText().toString().equals("已关注")) {
                    CancelFollowUser(handler, lookid);
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
    public static void okHttp_synchronousGet(final Handler handler, final String infouserid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).add("infoUserId", infouserid).build();
                    Log.e("okHttp_synchronousGet", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + "  infoUserId=" + infouserid);
                    final Request request = new Request.Builder().url(NetConstant.API_FindUserInfo).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(22, mMainModel));
                        Log.e("okHttp_synchronousGet", "responseStr=" + responseStr);
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
     * 发起网络请求
     */
    public static void FollowUser(final Handler handler, final String infouserid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).add("followId", infouserid).build();
                    Log.e("FollowUser", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + "  infoUserId=" + infouserid);
                    final Request request = new Request.Builder().url(NetConstant.API_FollowUser).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(23, mMainModel));
                        Log.e("FollowUser", "responseStr=" + responseStr);
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
     * 发起网络请求
     */
    public static void CancelFollowUser(final Handler handler, final String infouserid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).add("followId", infouserid).build();
                    Log.e("CancelFollowUser", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + "  infoUserId=" + infouserid);
                    final Request request = new Request.Builder().url(NetConstant.API_CancelFollowUser).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(24, mMainModel));
                        Log.e("CancelFollowUser", "responseStr=" + responseStr);
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
                if (model.getData().getPortraitPathArray().length > 0) {
                    String imgurl = model.getData().getPortraitPathArray()[0];
//                    Glide.with(context)
//                            .load(imgurl)
//                            .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
//                            .into(imggold_iv);
//                    if (!imgurl.equals(imggold_iv.getTag())) {//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                        imggold_iv.setTag(null);//需要清空tag，否则报错
//                        Glide.with(context)
//                                .load(imgurl)
//                                .apply(new RequestOptions().placeholder(R.mipmap.boos_1).error(R.mipmap.boos_1).dontAnimate())
//                                .into(imggold_iv);
//                        imggold_iv.setTag(imggold_iv);
//                    }
//                    imggold_iv.setTag(10);//

                    Picasso.with(context)
                            .load(imgurl)//加载过程中的图片显示
                            .placeholder(R.mipmap.rabblt_icon)
//加载失败中的图片显示
//如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                            .error(R.mipmap.rabblt_icon)
                            .into(imggold_iv);
                }
                name_tv.setText(model.getData().getUsername());
                tuercode_tv.setText("兔语号:" + model.getData().getTuId());
                fans_tv.setText("粉丝:" + model.getData().getFansCount());
                desc_tv.setText(model.getData().getDescription());
                sex_tv.setText(model.getData().getAge());
                type_tv.setText(model.getData().getAddress());
                if (model.getData().getFollow()) {
                    follow_tv.setText("已关注");
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_following);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    follow_tv.setCompoundDrawables(null, drawable, null, null);
                } else {
                    follow_tv.setText("关注");
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_follow_eachother);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    follow_tv.setCompoundDrawables(null, drawable, null, null);
                }
                if (model.getData().getGender().equals("男")) {
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.man);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    sex_tv.setCompoundDrawables(drawable, null, null, null);
                    sex_tv.setBackgroundResource(R.drawable.room_sex);
                } else {
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.woman);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    sex_tv.setCompoundDrawables(drawable, null, null, null);
                    sex_tv.setBackgroundResource(R.drawable.room_sex2);
                }
                int levelnum = Integer.valueOf(model.getData().getExpRank()).intValue();//用户等级
                Glide.with(context)
                        .load(RetrofitService.bgAraay[levelnum])
                        .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                        .into(level_iv);
            } else if (msg.what == 23) {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    follow_tv.setText("已关注");
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_following);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    follow_tv.setCompoundDrawables(null, drawable, null, null);
                }
            } else if (msg.what == 24) {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    follow_tv.setText("关注");
                    Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_follow_eachother);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    follow_tv.setCompoundDrawables(null, drawable, null, null);
                }
            }
        }
    };


}
