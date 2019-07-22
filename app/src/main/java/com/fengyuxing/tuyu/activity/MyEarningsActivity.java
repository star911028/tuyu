package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MyEarningsActivity extends BaseActivity implements View.OnClickListener {
    //我的收益
    private static final String TAG = "MyEarningsActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.earnings_num_tv)
    TextView earningsNumTv;
    @BindView(R.id.earnings_list_tv)
    TextView earningsListTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.dhzs_tv)
    TextView dhzsTv;
    @BindView(R.id.cashout_tv)
    TextView cashoutTv;
    private String Coin = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_earnings;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyEarningsActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
    }

    @Override
    public void onResume() {
        super.onResume();
        getFindUserInfo();//刷新我的元宝
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


    @OnClick({R.id.back_iv, R.id.earnings_list_tv, R.id.dhzs_tv, R.id.cashout_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.earnings_list_tv://充值记录
                Intent intent = new Intent(mContext, MyEarDrawListActivity.class);
                startActivity(intent);
                break;
            case R.id.dhzs_tv://兑换钻石
                Log.e("Coin", "兑换钻石=" + Coin);
                intent = new Intent(mContext, MyEarDHActivity.class);
                intent.putExtra("coin", Coin);
                startActivity(intent);
                break;
            case R.id.cashout_tv://提现
                FindCanDrawCash();//查询是否可以提现
//                Log.e("Coin", "兑换钻石=" + Coin);
//                intent = new Intent(mContext, MyEarTXActivity.class);
//                intent.putExtra("coin", Coin);
//                startActivity(intent);
                break;
        }
    }


    public void getFindUserInfo() {//查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", MyApplication.getInstance().getUserId());
        postRequest(RetrofitService.FindUserInfo, map);
        Log.e("getFindUserInfo", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken());
    }


    public void FindCanDrawCash() {//查询是否可以提现
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindCanDrawCash, map);
    }


    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        RoomModel mMainModel = new Gson().fromJson(result,
                new TypeToken<RoomModel>() {
                }.getType());
        if (url.equals(RetrofitService.Head + RetrofitService.FindUserInfo)) {
            Log.e("MineFragment", "result=" + result);
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    earningsNumTv.setText(mMainModel.getData().getCoin());
                    Coin = mMainModel.getData().getCoin();
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindCanDrawCash)) {
            Log.e("FindCanDrawCash", "result=" + result);
            Log.e("Coin", "兑换钻石=" + Coin);
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {//之前绑定过   不为空返回用户名称 账号
                    Intent intent = new Intent(mContext, MyEarTXActivity.class);
                    intent.putExtra("coin", Coin);
                    intent.putExtra("name", mMainModel.getData().getAlRealName());//支付宝姓名
                    intent.putExtra("code", mMainModel.getData().getAlAccount());//支付宝账号
                    startActivity(intent);
                } else {//之前没绑定
                    Intent intent = new Intent(mContext, MyEarTXActivity.class);
                    intent.putExtra("coin", Coin);
                    startActivity(intent);
                }
            } else if (mMainModel.getCode().equals("4")) {//  4	未做实名认证、验证，需要跳转实名认证页面并提示错误信息
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, RealNameauthenActivity.class);
                startActivity(intent);
            } else if (mMainModel.getCode().equals("5")) {// 5	未做实名验证，需要跳转实名验证页面并提示错误信息
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MainSmyzActivity.class);
                startActivity(intent);
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
