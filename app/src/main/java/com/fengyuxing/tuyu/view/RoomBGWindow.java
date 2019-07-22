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
import com.fengyuxing.tuyu.adapter.RoomBGRecyAdapter;
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


//房间背景选择窗口

public class RoomBGWindow extends PopupWindow {
    private final LinearLayout pop_layout;
    private RecyclerView recyclerview;
    private ImageView close_iv;
    private View conentView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private RoomBGRecyAdapter adapter;
    private List<DataList> data = new ArrayList<>();
    private String Roomid="";
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

    public RoomBGWindow(final Activity context, OnClickListener l, List<DataList> datalist,String roomid) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_bg_window, null);
        recyclerview = (RecyclerView) conentView.findViewById(R.id.recyclerview);
        close_iv = (ImageView) conentView.findViewById(R.id.close_iv);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        close_iv.setTag(4);
        close_iv.setOnClickListener(l);
        Roomid=roomid;
        data = datalist;
        //   底部推荐列表
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new RoomBGRecyAdapter(context, datalist);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(mOnItemClickListener);


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

    private RoomBGRecyAdapter.OnItemClickListener mOnItemClickListener = new RoomBGRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
//            Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
            updatePosition(position);
            MyApplication.getInstance().setRoombg(data.get(position).getImgPath());
            getData(handler,Roomid,data.get(position).getImageId());
        }
    };

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    /**
     * 更新选中位置
     *
     * @param selectedPosition 选中位置
     */
    public void updatePosition(int selectedPosition) {
        if (adapter != null) {
            //设置单选位置，并且刷新列表
            adapter.setSelectedPosition(selectedPosition);
            adapter.notifyDataSetChanged();
        }
    }




    /**
     * 发起网络请求
     */
    public void getData(final Handler handler, final String roomId, final String imageId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("imageId", imageId).add("roomId", roomId).build();
                    Log.e("getData", "userId=" + MyApplication.getInstance().getUserId() + "   token=" + MyApplication.getInstance().getToken() + "   roomId=" + roomId + "  imageId=" + imageId);
                    final Request request = new Request.Builder().url(NetConstant.API_EditRoomBGI).post(formBody).build();
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
                if(model!=null){
                    if(model.getCode().equals("1")){
//                        dismiss();
                        EventBus.getDefault().post(new TabCheckEvent("sucess"));

                    }else {
                        EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                    }
                }
            }
        }
    };

}
