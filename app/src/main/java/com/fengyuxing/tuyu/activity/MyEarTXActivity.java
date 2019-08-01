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
import retrofit2.Call;
import retrofit2.Response;

public class MyEarTXActivity extends BaseActivity implements View.OnClickListener {
    //我的收益--提现
    private static final String TAG = "MyEarTXActivity";
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
    @BindView(R.id.wx_ll)
    LinearLayout wxLl;
    @BindView(R.id.alcode_tv)
    TextView alcodeTv;
    @BindView(R.id.alname_tv)
    TextView alnameTv;
    @BindView(R.id.band_iv)
    TextView bandIv;
    @BindView(R.id.alinfo_ll)
    LinearLayout alinfoLl;
    private String WXopenid = "";
    private String Coin = "";
    private String AiCode = "";
    private String AiNmae = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eartx;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyEarTXActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        Coin = getIntent().getStringExtra("coin");
        AiCode = getIntent().getStringExtra("code");//支付宝账号
        AiNmae = getIntent().getStringExtra("name");//支付宝姓名
        tvYbnums.setText(Coin);
        if (AiCode != null && AiCode.length() > 0) {
            bandIv.setVisibility(View.GONE);
            alinfoLl.setVisibility(View.VISIBLE);
            alcodeTv.setText(AiCode);
            alnameTv.setText( AiNmae);
        } else {
            bandIv.setVisibility(View.VISIBLE);
            alinfoLl.setVisibility(View.GONE);
        }
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


    @OnClick({R.id.back_iv, R.id.dhzs_tv, R.id.wx_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.dhzs_tv://提现
                Log.e("提现", "length=" + etDiom.getText().toString().trim().length());
                if (etDiom.getText().toString().trim().length() == 0) {
                    Toast.makeText(MyEarTXActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (alcodeTv.getText().toString().length() <11) {
                    Toast.makeText(MyEarTXActivity.this, "请绑定支付宝账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                dhzsTv.setEnabled(false);
                DrawCash();//提现
                break;
            case R.id.wx_ll://绑定支付宝
//                authorization(SHARE_MEDIA.WEIXIN);
                Intent intent = new Intent(mContext, BandAlpayActivity.class);
                startActivityForResult(intent, 111);
                break;
        }
    }

    public void DrawCash() {//提现
//        userId	是	int	用户ID wxOpenId	否	string	微信 OpenId     alAccount	是	string	支付宝账号   alRealName	是	string	支付宝认证的真实姓名   cash	是	int	现金数
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("alAccount", alcodeTv.getText().toString());
        map.put("alRealName", AiNmae);
        map.put("cash", etDiom.getText().toString().trim());
        postRequest(RetrofitService.DrawCash, map);
        Log.e("DrawCash", "alAccount=" + alcodeTv.getText().toString() + "  alRealName=" + AiNmae + "  cash=" + etDiom.getText().toString().trim());
    }

    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.DrawCash)) {
            dhzsTv.setEnabled(true);
            Log.e("DrawCash", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                Toast.makeText(mContext, "提交成功,请等待审核", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "requestCode=" + requestCode + " data=" + data);
        if (requestCode == 111) {
            if (data != null) {
                Log.e("onActivityResult", "111" + data.getStringExtra("alcode"));
                alnameTv.setText(data.getStringExtra("alname"));
                alcodeTv.setText(data.getStringExtra("alcode"));
                AiNmae=data.getStringExtra("alname");
                AiCode=data.getStringExtra("alcode");
                bandIv.setVisibility(View.GONE);
                alinfoLl.setVisibility(View.VISIBLE);
            }
        }else {
            alnameTv.setText("");
            alcodeTv.setText("");
            bandIv.setVisibility(View.VISIBLE);
            alinfoLl.setVisibility(View.GONE);
        }
    }

}
