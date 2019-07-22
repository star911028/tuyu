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
import com.fengyuxing.tuyu.adapter.RoomListLinkMaiRecyAdapter;
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


//房间管排麦窗口

public class RoomMikeLineWindow extends PopupWindow {
    private final LinearLayout pop_layout, lay_manneger, lay_black;
    private ImageView close_iv;
    private View conentView, view_recommend, view_follow;
    private Context context;
    private TextView tv_follow, mike_line_bt, clear_all_tv, link_num_tv, tv_recommend;
    private RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private RoomListLinkMaiRecyAdapter adapter;
    private List<DataList> data = new ArrayList<>();
    private int Poistion = -1;
    private String Roomid = "";
    public int orange, gray;
    public static String DeleteType = "";

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

    public RoomMikeLineWindow(final Activity context, OnClickListener l, final String roomid, Boolean hasboos, Boolean ismannger) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_mikeline_window, null);
        lay_manneger = (LinearLayout) conentView.findViewById(R.id.lay_manneger);
        lay_black = (LinearLayout) conentView.findViewById(R.id.lay_black);
        tv_follow = (TextView) conentView.findViewById(R.id.tv_follow);
        mike_line_bt = (TextView) conentView.findViewById(R.id.mike_line_bt);
        link_num_tv = (TextView) conentView.findViewById(R.id.link_num_tv);
        tv_recommend = (TextView) conentView.findViewById(R.id.tv_recommend);
        clear_all_tv = (TextView) conentView.findViewById(R.id.clear_all_tv);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        view_recommend = (View) conentView.findViewById(R.id.view_recommend);
        view_follow = (View) conentView.findViewById(R.id.view_follow);
        recyclerview = (RecyclerView) conentView.findViewById(R.id.recyclerview);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        close_iv.setTag(4);
        close_iv.setOnClickListener(l);
        if (hasboos) {//是否有老板位
            lay_black.setVisibility(View.VISIBLE);
            view_recommend.setVisibility(View.GONE);
        } else {
            lay_black.setVisibility(View.GONE);
            view_recommend.setVisibility(View.VISIBLE);
        }
        if (ismannger) {//是否是管理员  控制显示全部清空功能
            MyApplication.getInstance().setMangerLink("true");
            clear_all_tv.setVisibility(View.VISIBLE);
        } else {
            MyApplication.getInstance().setMangerLink("false");
            clear_all_tv.setVisibility(View.GONE);
        }

        Roomid = roomid;
        orange = ContextCompat.getColor(context, R.color.black);
        gray = ContextCompat.getColor(context, R.color.color_nosec);
        changePage(0);//设置默认状态
        getData(handler, roomid, "0");//更新数据
        lay_manneger.setOnClickListener(new OnClickListener() {
            @Override//管理员
            public void onClick(View v) {
                changePage(0);
                getData(handler, roomid, "0");//更新数据
            }
        });
        lay_black.setOnClickListener(new OnClickListener() {
            @Override//黑名单
            public void onClick(View v) {
                changePage(1);
                getData(handler, roomid, "1");  //更新数据
            }
        });

        mike_line_bt.setOnClickListener(new OnClickListener() {
            @Override//排队
            public void onClick(View v) {
                if (view_recommend.getVisibility() == View.VISIBLE) {
                    if (mike_line_bt.getText().equals("排麦")) {
                        LineForMike(handler, roomid, "0");
                    } else {
                        CancelLineForMike(handler);
                    }
                } else {
                    if (mike_line_bt.getText().equals("排麦")) {
                        LineForMike(handler, roomid, "1");
                    } else {
                        CancelLineForMike(handler);
                    }

                }

            }
        });
        clear_all_tv.setOnClickListener(new OnClickListener() {
            @Override//清空排麦列表
            public void onClick(View v) {
                if (view_recommend.getVisibility() == View.VISIBLE) {
                    ClearLiner(handler, roomid, "0");
                } else {
                    ClearLiner(handler, roomid, "1");
                }

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
        String sp1 = "普通位";
        String sp2 = "老板位";
        switch (i) {
            case 0:
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
     * 发起网络请求 查询直播间排麦人员
     */
    public static void getData(final Handler handler, final String roomId, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String maitype = "";
                    if (type.equals("1")) {//老板位
                        maitype = "boss";
                    } else {//普通位
                        maitype = "normal";
                    }
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("type", maitype).add("page", "0").add("roomId", roomId).build();
                    Log.e("findLiner", " userid=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + " type=" + maitype
                            + "roomId=" + roomId);
                    final Request request = new Request.Builder().url(NetConstant.API_findLiner).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("API_findLiner", "responseStr=" + responseStr);
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
                    if (MyApplication.getInstance().getUserId().equals(model.getData().getArray().get(i).getUserId())) {
                        mike_line_bt.setText("取消排麦"); //自己在排麦列表
                    }
                }
                adapter = new RoomListLinkMaiRecyAdapter(context, model.getData().getArray());
                recyclerview.setAdapter(adapter);
                adapter.setOnItemClickListener(mOnItemClickListener);
                link_num_tv.setText("当前排队人数:" + model.getData().getCount());
            } else if (msg.what == 23) {//排麦接口返回
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    //   {"code":1,"errorMsg":"","data":{"isBoss":false,"mikeNumber":2,"mikeId":6,"isReceptionist":false,"push":true,"status":0,"needLine":false}}
                    //需要刷新主页座位列表
                    EventBus.getDefault().post(new TabCheckEvent("sucess"));//刷新主页坐位列表
                    if (model.getData() != null) {
                        if (model.getData().getPush().equals("true")) {//已经可以上麦了
                            dismiss();
                        } else {
                            data.clear();
                            mike_line_bt.setText("取消排麦");
                            if (view_recommend.getVisibility() == View.VISIBLE) {
                                getData(handler, Roomid, "0");  //更新数据
                            } else {
                                getData(handler, Roomid, "1"); //更新数据
                            }
                        }
                    } else {
                        data.clear();
                        mike_line_bt.setText("取消排麦");
                        if (view_recommend.getVisibility() == View.VISIBLE) {
                            getData(handler, Roomid, "0");  //更新普通位数据
                        } else {
                            getData(handler, Roomid, "1");//更新老板位数据
                        }
                    }
                } else if (model.getCode().equals("0")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    EventBus.getDefault().post(new TabCheckEvent("提示" + R.string.login_token));
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            } else if (msg.what == 24) {
                //清空直播间排麦人员
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    if (view_recommend.getVisibility() == View.VISIBLE) {
                        getData(handler, Roomid, "0");  //更新数据
                    } else {
                        getData(handler, Roomid, "1");
                    }
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }

            } else if (msg.what == 25) {
                //直播间取消排麦
                data.clear();
                if (view_recommend.getVisibility() == View.VISIBLE) {
                    getData(handler, Roomid, "0");  //更新普通位数据
                    Log.e("直播间取消排麦", "更新普通位数据");
                } else {
                    getData(handler, Roomid, "1");//更新老板位数据
                    Log.e("直播间取消排麦", "更新老板位数据");
                }
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    mike_line_bt.setText("排麦");
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }

            } else if (msg.what == 26) {//抱用户上麦
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    dismiss();
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            }


        }
    };

    private RoomListLinkMaiRecyAdapter.OnItemClickListener mOnItemClickListener = new RoomListLinkMaiRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {//列表item点击事件

        }

        @Override
        public void onItemRemoveClick(int position) {//抱他上麦
            if (view_recommend.getVisibility() == View.VISIBLE) {
                PickUpForMike(handler, Roomid, "0", data.get(position).getUserId());//抱上普通位
                Log.e("抱他上麦", "Roomid="+Roomid+" linkid="+data.get(position).getUserId()+" 普通位");
            } else {
                PickUpForMike(handler, Roomid, "1", data.get(position).getUserId());//抱上老板位
                Log.e("抱他上麦", "Roomid="+Roomid+" linkid="+data.get(position).getUserId()+" 老板位");
            }
        }
    };


    /**
     * 直播间按麦位类型排麦
     */
    public static void LineForMike(final Handler handler, final String roomId, final String matype) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String maitype = "";
                    if (matype.equals("1")) {
                        maitype = "boss";
                    } else {
                        maitype = "normal";
                    }
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("type", maitype).add("roomId", roomId).build();
                    Log.e("LineForMike", " userid=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + " type=" + maitype
                            + "roomId=" + roomId);
                    final Request request = new Request.Builder().url(NetConstant.API_LineForMike).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("LineForMike", "responseStr=" + responseStr);
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


    /**
     * 清空直播间排麦人员
     */
    public static void ClearLiner(final Handler handler, final String roomId, final String matype) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String maitype = "";
                    if (matype.equals("1")) {
                        maitype = "boss";
                    } else {
                        maitype = "normal";
                    }
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("type", maitype).add("roomId", roomId).build();
                    Log.e("ClearLiner", " userid=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + " type=" + maitype
                            + "roomId=" + roomId);
                    final Request request = new Request.Builder().url(NetConstant.API_ClearLiner).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("LineForMike", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(24, mMainModel));
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
     * 直播间取消排麦
     */
    public static void CancelLineForMike(final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .build();
                    final Request request = new Request.Builder().url(NetConstant.API_CancelLineForMike).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("直播间取消排麦", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(25, mMainModel));
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
     * 抱用户上麦
     */
    public static void PickUpForMike(final Handler handler, final String roomId, final String matype, final String linerId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String maitype = "";
                    if (matype.equals("1")) {
                        maitype = "boss";
                    } else {
                        maitype = "normal";
                    }
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("type", maitype).add("roomId", roomId).add("linerId", linerId).build();
                    Log.e("PickUpForMike", " userid=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + " type=" + maitype
                            + "roomId=" + roomId + "linerId=" + linerId);
                    final Request request = new Request.Builder().url(NetConstant.API_PickUpForMike).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("LineForMike", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(26, mMainModel));
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
