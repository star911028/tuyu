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
import com.fengyuxing.tuyu.adapter.MyBlackerRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.view.BlackerLongClickWindow;
import com.fengyuxing.tuyu.view.CardLongClickWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MyBlackerActivity extends BaseActivity implements View.OnClickListener {
    //我的黑名单
    private static final String TAG = "MyBlackerActivity";
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
    private MyBlackerRecyAdapter recyAdapter3;
    private List<DataList> carddata = new ArrayList<>();
    private int page = 0;
    private CardLongClickWindow mCardLongClickWindow;
    private DataList userdata2;
    private BlackerLongClickWindow mBlackerLongClickWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_blacker;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyBlackerActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
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
        FindMyBlacker();
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


    private MyBlackerRecyAdapter.OnItemClickListener mOnItemClickListener = new MyBlackerRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
//            getFindUserInfo(carddata.get(position).getUserId());

            Intent intent = new Intent(mContext, UserInfoctivity.class);//
            intent.putExtra("userdata", (Serializable) userdata2);
            intent.putExtra("userinfoid", carddata.get(position).getUserId());
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(int position) {
            Log.e("onItemLongClick", "长按" + position);
            BlackerLongClickWindow(carddata.get(position).getUserId());
        }

    };

    private void BlackerLongClickWindow(final String blackerId) {//房间管理员
        mBlackerLongClickWindow = new BlackerLongClickWindow(MyBlackerActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mBlackerLongClickWindow.dismiss();
                                break;
                            case 5://取消拉黑
                                mBlackerLongClickWindow.dismiss();
                                CancelDefriendUser(blackerId);
                                break;
                        }
                    }
                });
        mBlackerLongClickWindow.setClippingEnabled(false);
        mBlackerLongClickWindow.showAtLocation(main_ll, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    public void FindMyBlacker() {//查询我的黑名单用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindMyBlacker, map);
    }

    public void getFindUserInfo(String otherid) {//查询他人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", otherid);
        postRequest(RetrofitService.FindUserInfo, map);
    }

    public void CancelDefriendUser(String blackerId) {//取消拉黑用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("blackerId", blackerId);
        postRequest(RetrofitService.CancelDefriendUser, map);
    }


    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindMyBlacker)) {
            Log.e("FindMyBlacker", "result=" + result);
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
                            recyAdapter3 = new MyBlackerRecyAdapter(mContext, carddata);
                            recycleView.setAdapter(recyAdapter3);
                            recyAdapter3.setOnItemClickListener(mOnItemClickListener);
                        } else {
                            recyAdapter3.notifyDataSetChanged();
                        }
                    } else {
                        refreshLayout.setVisibility(View.GONE);
                        emetyLl.setVisibility(View.VISIBLE);
                        if (recyAdapter3 == null) {
                            recyAdapter3 = new MyBlackerRecyAdapter(mContext, carddata);
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
        }
//        else if (url.contains(RetrofitService.Head + RetrofitService.FindUserInfo)) {
//            Log.e("MineFragment", "result=" + result);
//            RoomModel roomModel = new Gson().fromJson(result,
//                    new TypeToken<RoomModel>() {
//                    }.getType());
//            if (roomModel.getCode().equals("1")) {
//                if (roomModel.getData() != null) {
//                    userdata2 = roomModel.getData();
//                    Intent intent = new Intent(mContext, UserInfoctivity.class);//
//                    intent.putExtra("userdata", (Serializable) userdata2);
//                    intent.putExtra("userinfoid", "1");
//                    startActivity(intent);
//                }
//            } else if (roomModel.getCode().equals("0")) {
//                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
//                MyApplication.getInstance().setUserId("");
//                Intent intent = new Intent(mContext, LoginActivity.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(mContext, roomModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
//            }
//        }
        else if (url.contains(RetrofitService.Head + RetrofitService.CancelDefriendUser)) {
            Log.e("CancelDefriendUser", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                FindMyBlacker();//刷新数据
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
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
}
