package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.MainCardRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.bean.StringModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.view.CardLongClickWindow;
import com.fengyuxing.tuyu.view.NeedPWDWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {
    //我的收藏
    private static final String TAG = "MyCollectionActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.emety_ll)
    LinearLayout emetyLl;
    @BindView(R.id.main_ll)
    LinearLayout main_ll;
    private LinearLayoutManager layoutManager;
    private MainCardRecyAdapter recyAdapter3;
    private List<DataList> carddata = new ArrayList<>();
    private int page = 0;
    private CardLongClickWindow mCardLongClickWindow;
    private NeedPWDWindow mNeedPWDWindow;
    private String Roomid = "";
    private Boolean toastnews=false;
    private Boolean Needpass = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collet;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyCollectionActivity.this, getResources().getColor(R.color.mycollet_col), true);//isLightColor   透明或者不透明
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        //下拉刷新
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale));
    }

    @Override
    public void onResume() {
        super.onResume();
        FindCollectRoom();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initData() {
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
    }


    @Override
    protected void initEventListeners() {
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private MainCardRecyAdapter.OnItemClickListener mOnItemClickListener = new MainCardRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Roomid = carddata.get(position).getRoomId();
            toastnews=false;
            getFindRoomInfo();
//            Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//            intentyh.putExtra("roomid", carddata.get(position).getRoomId());//传递房间id
//            intentyh.putExtra("roomdata", (Serializable) carddata.get(position));//传递房间数据
//            startActivity(intentyh);
        }

        @Override
        public void onItemLongClick(int position) {
            Log.e("onItemLongClick", "长按" + position);
            CardLongClickWindow(carddata.get(position).getRoomId(), carddata.get(position));
        }

    };

    private void CardLongClickWindow(final String roomid, final DataList data) {//房间管理员
        mCardLongClickWindow = new CardLongClickWindow(MyCollectionActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mCardLongClickWindow.dismiss();
                                break;
                            case 5://取消收藏
                                mCardLongClickWindow.dismiss();
                                CancelCollectRoom(roomid);
                                break;
                            case 6://进入房间
                                mCardLongClickWindow.dismiss();
                                Roomid = data.getRoomId();
                                toastnews=false;
                                getFindRoomInfo();
//                                Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                                intentyh.putExtra("roomid", data.getRoomId());//传递房间id
//                                intentyh.putExtra("roomdata", (Serializable)data);//传递房间数据
//                                startActivity(intentyh);
                                break;
                        }
                    }
                });
        mCardLongClickWindow.setClippingEnabled(false);
//        mRoomValueWindow.setAnimationStyle(R.style.pop_animation);//动画效果
        mCardLongClickWindow.showAtLocation(main_ll, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    public void FindCollectRoom() {//查询收的藏直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindCollectRoom, map);
    }

    public void CancelCollectRoom(String roomId) {//取消收藏直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", roomId);
        postRequest(RetrofitService.CancelCollectRoom, map);
    }

    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindCollectRoom)) {
            Log.e("FindCollectRoom", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            if (mMainArrayModel.getCode().equals("1")) {
                if (mMainArrayModel.getData() != null) {
                    carddata.clear();
                    if (mMainArrayModel.getData().size() > 0) {
                        refreshLayout.setVisibility(View.VISIBLE);
                        emetyLl.setVisibility(View.GONE);
                        for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                            carddata.add(mMainArrayModel.getData().get(i));
                        }
                        if (recyAdapter3 == null) {
                            recyAdapter3 = new MainCardRecyAdapter(mContext, carddata);
                            recycleView.setAdapter(recyAdapter3);
                            recyAdapter3.setOnItemClickListener(mOnItemClickListener);
                        } else {
                            recyAdapter3.notifyDataSetChanged();
                        }
                    } else {
                        refreshLayout.setVisibility(View.GONE);
                        emetyLl.setVisibility(View.VISIBLE);
                        if (recyAdapter3 == null) {
                            recyAdapter3 = new MainCardRecyAdapter(mContext, carddata);
                            recycleView.setAdapter(recyAdapter3);
                            recyAdapter3.setOnItemClickListener(mOnItemClickListener);
                        } else {
                            recyAdapter3.notifyDataSetChanged();
                        }
                    }
                }
            } else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelCollectRoom)) {
            Log.e("CancelCollectRoom", "result=" + result);
            StringModel mStringModel = new Gson().fromJson(result,
                    new TypeToken<StringModel>() {
                    }.getType());
            if (mStringModel.getCode().equals("1")) {
                FindCollectRoom();
                Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT);
            } else if (mStringModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }


        } else if (url.equals(RetrofitService.Head + RetrofitService.FindRoomInfo)) {//进入直播间查询信息
            Log.e("FindRoomInfo", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
//                if (Needpass) {
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
//                    intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
//                    intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
//                    startActivity(intentyh);
//                } else {
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
//                    intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
//                    startActivity(intentyh);
//                }

                if (Needpass) {
                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                        if (!MyApplication.getInstance().getMyRoomid().equals(mMainModel.getData().getRoomId())) {//不是同一个房间 先退出MainRoomActivity 在进入房间
                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                        }
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    } else {//当前没有在房间
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    }
                } else {
                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                        if (!MyApplication.getInstance().getMyRoomid().equals(mMainModel.getData().getRoomId())) {//不是同一个房间 先退出MainRoomActivity
                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                        }
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    } else {//当前没有在房间
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    }
                }




            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else if (mMainModel.getCode().equals("2")) {//密码错误
                if(mMainModel.getErrorMsg().contains("拉黑")){
                    Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }else {
                    Needpass = false;//重新打开密码输入框
                    if(toastnews){
                        Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }else {
                        NeedPWDwindow();
                    }
                }
            } else if (mMainModel.getCode().equals("3")) {//房间需要密码
                Needpass = true;//需要密码
                if (mMainModel.getErrorMsg() != null) {
                    NeedPWDwindow();
                }
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

    }


    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }


    public void getFindRoomInfo() {//进入直播间查询信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        if (Needpass) {//有密码才传这个字段
            map.put("password", MyApplication.getInstance().getInputpass());
        }
        Log.e("getFindRoomInfo", "password=" + MyApplication.getInstance().getInputpass());
        postRequest(RetrofitService.FindRoomInfo, map);
    }

    //密码弹出框
    private void NeedPWDwindow() {//
        mNeedPWDWindow = new NeedPWDWindow(MyCollectionActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mNeedPWDWindow.dismiss();
                                break;
                            case 5://立即进入
                                mNeedPWDWindow.dismiss();
                                toastnews=true;
                                getFindRoomInfo();
                                //TODO
                                break;
                        }
                    }
                });
        mNeedPWDWindow.setClippingEnabled(false);
        mNeedPWDWindow.showAtLocation(main_ll, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }
}
