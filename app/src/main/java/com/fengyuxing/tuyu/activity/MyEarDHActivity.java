package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MyEarDHActivity extends BaseActivity implements View.OnClickListener {
    //我的收益--兑换钻石
    private static final String TAG = "MyEarDHActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.tv_ybnums)
    TextView tvYbnums;
    @BindView(R.id.et_diom)
    EditText etDiom;
    @BindView(R.id.dhzs_tv)
    TextView dhzsTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    private  String Coin="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eardy;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyEarDHActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        Coin=  getIntent().getStringExtra("coin");
        Log.e("Coin","Coin="+Coin);
        tvYbnums.setText(Coin);
    }

    @Override
    public void onResume() {
        super.onResume();
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


    @OnClick({R.id.back_iv, R.id.dhzs_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.dhzs_tv://兑换钻石
                Log.e("兑换钻石","length="+etDiom.getText().toString().trim().length());
                if (etDiom.getText().toString().trim().length() > 0) {
                    DrawDiamond(etDiom.getText().toString().trim());
                } else {
                    Toast.makeText(MyEarDHActivity.this, "请输入兑换数量", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public void DrawDiamond(String diamond) {//兑换钻石
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("diamond", diamond);
        postRequest(RetrofitService.DrawDiamond, map);
    }

    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.DrawDiamond)) {
            Log.e("DrawDiamond", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                Toast.makeText(mContext, "兑换成功,1分钟左右到账", Toast.LENGTH_SHORT).show();
                finish();
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

}
