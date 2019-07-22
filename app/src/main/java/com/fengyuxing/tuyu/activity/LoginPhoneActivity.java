package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.StringModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.io.IOException;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginPhoneActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginPhoneActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.pass_tv)
    TextView passTv;
    @BindView(R.id.phone_et)
    EditText phoneEt;

    @BindView(R.id.code_et)
    EditText codeEt;
    @BindView(R.id.getcode_tv)
    TextView getcodeTv;
    @BindView(R.id.login_bt)
    Button loginBt;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.close_iv)
    ImageView closeIv;

    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    private MyCountDownTimer myCountDownTimer;
    private String sendphone = "";
    private String LoginType = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loginphone;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(LoginPhoneActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Bold.otf");//设置字体
        phoneEt.setTypeface(tf);
        codeEt.setTypeface(tf);
        LoginType = getIntent().getStringExtra("LoginType");
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
//        titleCenter.setText("登陆");
    }


    @Override
    protected void initEventListeners() {
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back_iv, R.id.pass_tv, R.id.close_iv, R.id.getcode_tv, R.id.login_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.pass_tv://跳过
                break;
            case R.id.close_iv://删除手机号
//                phoneEt.setText("");
                break;
            case R.id.getcode_tv://发送验证码
                if (phoneEt.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写您的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneEt.getText().toString().length() != 11) {//!CheckUtil.isMobileNO(etPhone.getText().toString()) ||
                    Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //注意先调接口请求验证码在倒计时
                SendCode();
                codeEt.requestFocus();//输入框获取焦点
                myCountDownTimer = new MyCountDownTimer(60000, 1000);
                myCountDownTimer.start();
                break;
            case R.id.login_bt://登录
                if (codeEt.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写您的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneEt.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写您的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneEt.getText().toString().length() != 11) {//!CheckUtil.isMobileNO(etPhone.getText().toString()) ||
                    Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                ValidatePhoneNumber();//验证手机号
                break;
        }
    }

    @OnClick(R.id.close_iv)
    public void onViewClicked() {
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


    public void ValidatePhoneNumber() {//聊天页面查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("phoneNumber", phoneEt.getText().toString().trim());
        map.put("validateCode", codeEt.getText().toString());
        map.put("os", "android");
        if (LoginType.equals("QQ")) {
            map.put("qqOpenId", MyApplication.getInstance().getThirdLoginopenid());
        } else if (LoginType.equals("微信")) {
            map.put("wxOpenId", MyApplication.getInstance().getThirdLoginopenid());
        }
        postRequest(RetrofitService.ValidatePhoneNumber, map);
    }

    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.ValidatePhoneNumber)) {
            Log.e("ValidatePhoneNumber","result="+result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    MyApplication.getInstance().setYXcode(sendphone);
                    if (mMainModel.getData().getNewUser() == null) {//老用户 跳到主页
                        MyApplication.getInstance().setUserId(mMainModel.getData().getUserId());
                        MyApplication.getInstance().setToken(mMainModel.getData().getToken());
                        Intent intentyh = new Intent(mContext, MainActivity.class);//  MainActivity
                        startActivity(intentyh);
                    } else {//新用户 跳到完善资料
                        MyApplication.getInstance().setUserId(mMainModel.getData().getUserId());
                        MyApplication.getInstance().setToken(mMainModel.getData().getToken());
                        MyApplication.getInstance().setNewUser(mMainModel.getData().getNewUser());
                        Intent intentyh = new Intent(mContext, AddinfoActivity.class);//AddinfoActivity
                        intentyh.putExtra("LoginType", LoginType);
                        startActivity(intentyh);
                    }
                }
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

    public void ValidatePhoneNumber2() {

//        phoneNumber	是	string	手机号
//        validateCode	是	string	验证码
//        os	是	string	操作系统 ios/android
//        registerWay
        //TODO 修改
//        phoneNumber	是	string	手机号
//        validateCode	是	string	验证码
//        wxOpenId	否	string	微信 OpenId
//        qqOpenId	否	string	QQ OpenId
//        os	是	string	操作系统 ios/android

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("phoneNumber", sendphone).add("validateCode", codeEt.getText().toString().trim())
                .add("os", "android").add("os", "android").add("os", "android").add("registerWay", LoginType).build();
        final Request request = new Request.Builder().url(NetConstant.API_ValidatePhoneNumber).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e("ValidatePhoneNumber", "responseStr=" + responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        if (mMainModel.getCode().equals("1")) {
                            if (mMainModel.getData() != null) {
//                                doLogin();//云信登陆接口
                                MyApplication.getInstance().setYXcode(sendphone);
                                if (mMainModel.getData().getNewUser() == null) {//老用户 跳到主页
                                    MyApplication.getInstance().setUserId(mMainModel.getData().getUserId());
                                    MyApplication.getInstance().setToken(mMainModel.getData().getToken());
                                    Intent intentyh = new Intent(mContext, MainActivity.class);//  MainActivity
                                    startActivity(intentyh);
                                } else {//新用户 跳到完善资料
                                    MyApplication.getInstance().setUserId(mMainModel.getData().getUserId());
                                    MyApplication.getInstance().setToken(mMainModel.getData().getToken());
                                    MyApplication.getInstance().setNewUser(mMainModel.getData().getNewUser());
                                    Intent intentyh = new Intent(mContext, AddinfoActivity.class);//AddinfoActivity
                                    intentyh.putExtra("LoginType", LoginType);
                                    startActivity(intentyh);
                                }
                            }
                        } else if (mMainModel.getCode().equals("0")) {
                            Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                            MyApplication.getInstance().setUserId("");
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void doLogin() {
        LoginInfo info = new LoginInfo("18571453917", "5969a14098e3f0bb912193ba327d92b2", RetrofitService.YXAppKey); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Log.e("doLogin", "onSuccess param=" + param);
                    }

                    @Override
                    public void onFailed(int code) {
                        Log.e("doLogin", "onFailed code=" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }


    public void SendCode() {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("phoneNumber", phoneEt.getText().toString().trim()).build();
        final Request request = new Request.Builder().url(NetConstant.API_SendCode).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e("SendCode", "responseStr=" + responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringModel mStringModel = new Gson().fromJson(responseStr,
                                new TypeToken<StringModel>() {
                                }.getType());
                        Log.e("SendCode", "responseStr=" + responseStr);
                        if (mStringModel.getCode().equals("1")) {
                            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                            sendphone = phoneEt.getText().toString().trim();
                        } else if (mStringModel.getCode().equals("0")) {
                            Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                            MyApplication.getInstance().setUserId("");
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
