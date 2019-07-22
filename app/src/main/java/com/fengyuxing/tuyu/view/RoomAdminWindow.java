package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.adapter.RoomListAdminRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;


//房间管理员窗口

public class RoomAdminWindow extends PopupWindow {
    private final LinearLayout pop_layout, lay_manneger, lay_black;
    private ImageView close_iv;
    private View conentView, view_recommend, view_follow;
    private Context context;
    private TextView tv_follow, tv_recommend;
    private RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private RoomListAdminRecyAdapter adapter;
    private List<DataList> data = new ArrayList<>();
    private int Poistion = -1;
    private String Roomid = "";
    public int orange, gray;
    public static String DeleteType = "";
    public String RemoveManid="";
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

    public RoomAdminWindow(final Activity context, OnClickListener l, final String roomid) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_admin_window, null);
        lay_manneger = (LinearLayout) conentView.findViewById(R.id.lay_manneger);
        lay_black = (LinearLayout) conentView.findViewById(R.id.lay_black);
        tv_follow = (TextView) conentView.findViewById(R.id.tv_follow);
        tv_recommend = (TextView) conentView.findViewById(R.id.tv_recommend);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        view_recommend = (View) conentView.findViewById(R.id.view_recommend);
        view_follow = (View) conentView.findViewById(R.id.view_follow);
        recyclerview = (RecyclerView) conentView.findViewById(R.id.recyclerview);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        close_iv.setTag(4);
        close_iv.setOnClickListener(l);

        Roomid = roomid;
        orange = ContextCompat.getColor(context, R.color.black);
        gray = ContextCompat.getColor(context, R.color.color_nosec);
        changePage(0);//设置默认状态
        getData(handler, roomid, "0");//更新数据
        lay_manneger.setOnClickListener(new View.OnClickListener() {
            @Override//管理员
            public void onClick(View v) {
                changePage(0);
                getData(handler, roomid, "0");//更新数据
            }
        });
        lay_black.setOnClickListener(new View.OnClickListener() {
            @Override//黑名单
            public void onClick(View v) {
                changePage(1);
                getData(handler, roomid, "1");  //更新数据
            }
        });

        //   底部推荐列表
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

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

    private void changePage(int i) {
        String sp1 = "管理员" + MyApplication.getInstance().getRoomAdminnums();
        String sp2 = "黑名单" + MyApplication.getInstance().getRoomBlacknums();
        Log.e("changePage2", "getRoomAdminnums=" + MyApplication.getInstance().getRoomAdminnums() + " getRoomBlacknums=" + MyApplication.getInstance().getRoomBlacknums());
        switch (i) {
            case 0:
//                String sp1="管理员" +MyApplication.getInstance().getRoomAdminnums();
//                String sp2="黑名单" +MyApplication.getInstance().getRoomBlacknums();
                Spannable span1 = new SpannableString(sp1);//TODO注意span的长度
                span1.setSpan(new RelativeSizeSpan(1.4f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_recommend.setText(span1);
                tv_recommend.setTextColor(orange);
                tv_recommend.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//加粗 抗锯齿
                view_recommend.setVisibility(View.VISIBLE);
                Spannable span2 = new SpannableString(sp2);
                span2.setSpan(new RelativeSizeSpan(1.0f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_follow.setText(span2);
                tv_follow.setTextColor(gray);
                view_follow.setVisibility(View.INVISIBLE);
                break;
            case 1:
//                String sp3="管理员" +MyApplication.getInstance().getRoomAdminnums();
//                String sp4="黑名单" +MyApplication.getInstance().getRoomBlacknums();
                Spannable span3 = new SpannableString(sp1);
                span3.setSpan(new RelativeSizeSpan(1.0f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_recommend.setText(span3);
                tv_recommend.setTextColor(gray);
                view_recommend.setVisibility(View.INVISIBLE);
                Spannable span4 = new SpannableString(sp2);
                span4.setSpan(new RelativeSizeSpan(1.4f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_follow.setText(span4);
                tv_follow.setTextColor(orange);
                tv_follow.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//加粗 抗锯齿
                view_follow.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 发起网络请求
     */
    public static void getData(final Handler handler, final String roomId, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("page", "0").add("roomId", roomId).build();
                    String Url = "";
                    if (type.equals("1")) {//黑名单
                        Url = NetConstant.API_FindBlacker;
                    } else {//管理员
                        Url = NetConstant.API_FindManager;
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
                data.clear();
                for (int i = 0; i < model.getData().getArray().size(); i++) {
                    data.add(model.getData().getArray().get(i));
                }
                adapter = new RoomListAdminRecyAdapter(context, model.getData().getArray());
                recyclerview.setAdapter(adapter);
                adapter.setOnItemClickListener(mOnItemClickListener);
            } else {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    getData(handler, Roomid, DeleteType);//更新数据
                    adapter.notifyDataSetChanged();
                    if (DeleteType.equals("0")) {//移除管理员
                        EventBus.getDefault().post(new TabCheckEvent("取消管理员" +RemoveManid));
                        MyApplication.getInstance().setRoomAdminnums(MyApplication.getInstance().getRoomAdminnums() - 1);
                        changePage(0);
                    } else {//移除黑名单
                        MyApplication.getInstance().setRoomBlacknums(MyApplication.getInstance().getRoomBlacknums() - 1);
                        changePage(1);
                    }
                }  else if(model.getCode().equals("0")){
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    EventBus.getDefault().post(new TabCheckEvent("提示" + R.string.login_token));
                }
                else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            }
        }
    };

    private RoomListAdminRecyAdapter.OnItemClickListener mOnItemClickListener = new RoomListAdminRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {//列表item点击事件

        }

        @Override
        public void onItemRemoveClick(int position) {//item移除
            if (view_recommend.getVisibility() == View.VISIBLE) {//管理员
                RemoveManid=data.get(position).getUserId();//移除的管理员id
                RemoveItem(handler, Roomid, "0", data.get(position).getUserId());//更新数据
            } else {//黑名单
                RemoveItem(handler, Roomid, "1", data.get(position).getUserId());//更新数据
            }
            Poistion = position;
        }
    };


    /**
     * 发起网络请求
     */
    public static void RemoveItem(final Handler handler, final String roomId, final String type, final String reuserid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String Url = "";
                    String name = "";
                    if (type.equals("1")) {//黑名单
                        Url = NetConstant.API_DeleteBlacker;
                        name = "blackerId";
                        DeleteType = "1";
                    } else {//管理员
                        Url = NetConstant.API_DeleteManager;
                        name = "managerId";
                        DeleteType = "0";
                    }
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add(name, reuserid).add("roomId", roomId).build();

                    final Request request = new Request.Builder().url(Url).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("getData", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(23, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
