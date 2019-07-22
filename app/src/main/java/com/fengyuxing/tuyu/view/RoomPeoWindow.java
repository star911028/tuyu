package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.fengyuxing.tuyu.adapter.RoomListRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;


//房间在线人数窗口

public class RoomPeoWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private RecyclerView recyclerview;
    private ImageView close_iv;
    private TextView numTv;
    private View conentView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private RoomListRecyAdapter adapter;
    private List<DataList> data = new ArrayList<>();
    private static String getOnlineType="";
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

    public RoomPeoWindow(final Activity context, OnClickListener l, String type, String roletype, List<DataList> datalist,Boolean IsShowUPMike,Boolean isManager,String onlineType) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_peo_window, null);
        recyclerview = (RecyclerView) conentView.findViewById(R.id.recyclerview);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        numTv = (TextView) conentView.findViewById(R.id.num_tv);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        close_iv.setTag(4);
        close_iv.setOnClickListener(l);
        if(IsShowUPMike){//显示抱用户上麦

        }else {//不显示抱用户上麦

        }
        if(isManager){//显示抱用户上麦

        }else {//不显示抱用户上麦

        }

        if (type.equals("1")) {
            numTv.setText("在线" + datalist.size());
        } else if (type.equals("2")) {
            numTv.setText("排麦人数" + datalist.size());
        }
        if(onlineType.equals("normal")){//从在线列表抱人上麦
            getOnlineType="normal";
        }else {
            getOnlineType="spapel";
        }
        Log.e("OnlineType","OnlineType="+getOnlineType+"  onlineType="+onlineType);
        data.clear();
        for (int i = 0; i < datalist.size(); i++) {
            data.add(datalist.get(i));
        }
        //   底部推荐列表
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new RoomListRecyAdapter(context, datalist);//datalist
//        adapter = new RoomListRecyAdapter(context, data);//datalist
//        DataList list = new DataList();
//        for (int i = 0; i < 20; i++) {
//            list.setPortraitPath("http://pic29.nipic.com/20130601/12122227_123051482000_2.jpg");
//            list.setRoomName("斯大区");
//            list.setOnlineCount("25");
//            list.setClassifyName("麦上");
//            data.add(list);
//        }
        adapter.setOnItemClickListener(mOnItemClickListener);
        recyclerview.setAdapter(adapter);
        Log.e("IsShowUPMike","IsShowUPMike="+IsShowUPMike);
        if(IsShowUPMike){//是否显示抱用户上麦
            if (roletype.equals("2")) {//   0  房主  1 管理员  2 普通用户
                adapter.changetIsMangner(false);
                adapter.notifyDataSetChanged();
            } else {
                adapter.changetIsMangner(true);
                adapter.notifyDataSetChanged();
            }
        }else {
            adapter.changetIsMangner(false);
            adapter.notifyDataSetChanged();
        }
        Log.e("roletype","roletype="+roletype);
        if (roletype.equals("2")) {//   0  房主  1 管理员  2 普通用户
            adapter.changetIsMangner(false);
            adapter.notifyDataSetChanged();
        } else {
            adapter.changetIsMangner(true);
            adapter.notifyDataSetChanged();
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

    private RoomListRecyAdapter.OnItemClickListener mOnItemClickListener = new RoomListRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {//麦位点击事件
                    //打开用户信息窗口
            dismiss();
//            EventBus.getDefault().post(new TabCheckEvent("look"+data.get(position).getUserId()));
            if(!data.get(position).getUserId().equals(MyApplication.getInstance().getUserId())){//不让点击自己的item
                EventBus.getDefault().post(new TabCheckEvent("look"+data.get(position).getUserId(),"look"+data.get(position).getIsMiker()));
            }
            Log.e("onItemClick", "getUserId=" + data.get(position).getUserId());
        }

        @Override
        public void onItemMaiUpClick(int position) {//抱她上麦
            Log.e("onItemMaiUpClick", "position=" + position);
            getMaiup(handler, data.get(position).getUserId());
        }

        @Override
        public void onItemRemoveClick(int position) {

        }
    };

    /**
     * 发起网络请求
     */
    public static void getMaiup(final Handler handler, final String linerId) {
        new Thread(new Runnable() {
            private FormBody formBody;

            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Log.e("OnlineType","OnlineType="+getOnlineType);
                    if(getOnlineType.equals("normal")){// 在线列表 点击抱人上麦 不穿type mikeID
                         formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                                .add("roomId",   MyApplication.getInstance().getMaiRoomid())  .add("linerId", linerId).build();
                    }else {// 麦位报人上麦打开在线列表 点击抱人上麦 穿 mikeID
                        formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                                .add("roomId",   MyApplication.getInstance().getMaiRoomid())  .add("linerId", linerId).add("mikeId", MyApplication.getInstance().getUpMikeid()).build();
                    }
                    Log.e("getMaiup", "userId=" + MyApplication.getInstance().getUserId() + " token=" + MyApplication.getInstance().getToken() + "  linerId=" + linerId + "  mikeId=" + MyApplication.getInstance().getUpMikeid() + "\n"
                            + "url=" + RetrofitService.Head + RetrofitService.PickUpForMike);
                    final Request request = new Request.Builder().url(NetConstant.API_PickUpForMike).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
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
            MainModel model = (MainModel) msg.obj;
            if (model.getCode().equals("1")) {
                dismiss();
                EventBus.getDefault().post(new TabCheckEvent("sucess"));
            } else {
                EventBus.getDefault().post(new TabCheckEvent("提示"+model.getErrorMsg()));
            }
        }
    };
}
