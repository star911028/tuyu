package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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

public class BandAlpayActivity extends BaseActivity implements View.OnClickListener {
    //绑定支付宝
    private static final String TAG = "BandAlpayActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.et_alcode)
    EditText et_alcode;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_yzcode)
    EditText etYzcode;
    @BindView(R.id.getcode_tv)
    TextView getcodeTv;
    @BindView(R.id.band_tv)
    TextView bandTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bandalpay;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(BandAlpayActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
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
        etYzcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (etYzcode.getText().toString().trim().length() > 0 && et_alcode.getText().toString().trim().length() > 0 && etName.getText().toString().trim().length() > 0) {
                    bandTv.setBackgroundResource(R.drawable.sy_dhzs_bg);
                } else {
                    bandTv.setBackgroundResource(R.drawable.bandphone_nor);
                }
            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (etYzcode.getText().toString().trim().length() > 0 && et_alcode.getText().toString().trim().length() > 0 && etName.getText().toString().trim().length() > 0) {
                    bandTv.setBackgroundResource(R.drawable.sy_dhzs_bg);
                } else {
                    bandTv.setBackgroundResource(R.drawable.bandphone_nor);
                }
            }
        });
        et_alcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (etYzcode.getText().toString().trim().length() > 0 && et_alcode.getText().toString().trim().length() > 0 && etName.getText().toString().trim().length() > 0) {
                    bandTv.setBackgroundResource(R.drawable.sy_dhzs_bg);
                } else {
                    bandTv.setBackgroundResource(R.drawable.bandphone_nor);
                }
                if (et_alcode.getText().toString().length() > 10) {
                    getcodeTv.setTextColor(getResources().getColor(R.color.main_blue_col));
                } else {
                    getcodeTv.setTextColor(getResources().getColor(R.color.main_gray_col));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @OnClick({R.id.back_iv, R.id.getcode_tv, R.id.band_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.getcode_tv://获取验证码
                if (et_alcode.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写您的支付宝账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_alcode.getText().toString().length() < 11) {
                    Toast.makeText(mContext, "请输入正确的账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //注意先调接口请求验证码在倒计时
                SendValidateCode();
                etYzcode.requestFocus();//输入框获取焦点
                myCountDownTimer = new MyCountDownTimer(60000, 1000);
                myCountDownTimer.start();
                break;
            case R.id.band_tv://绑定支付宝
                if (et_alcode.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写您的支付宝账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etName.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写您的真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etYzcode.getText().toString().length() < 4) {//!CheckUtil.isMobileNO(etPhone.getText().toString()) ||
                    Toast.makeText(mContext, "请填写您的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                ValidatePhoneNumberTX();//验证提现
                break;
        }
    }

    public void SendValidateCode() {//发送提现验证码
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.SendValidateCode, map);
    }

    public void ValidatePhoneNumberTX() {//验证提现验证码
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("validateCode", etYzcode.getText().toString());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.ValidatePhoneNumberTX, map);
    }

    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        Log.e("onCalllBack", "result=" + result);//
        if (url.equals(RetrofitService.Head + RetrofitService.SendValidateCode)) {
            Log.e("SendValidateCode", "result=" + result);//
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.ValidatePhoneNumberTX)) {
            Log.e("ValidatePhoneNumberTX", "result=" + result);//
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                //返回页面传递参数
                Intent intent = new Intent(mContext, BandAlpayActivity.class);
                intent.putExtra("alname", etName.getText().toString());
                intent.putExtra("alcode", et_alcode.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
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


    //倒计时函数
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            getcodeTv.setClickable(false);
            getcodeTv.setText(l / 1000 + "秒后重发");
            getcodeTv.setTextColor(getResources().getColor(R.color.main_gray_col));

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            getcodeTv.setText("重新获取");
            getcodeTv.setTextColor(getResources().getColor(R.color.main_blue_col));
            //设置可点击
            getcodeTv.setClickable(true);
        }
    }
}
