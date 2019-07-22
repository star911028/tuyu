package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.SplashAdapter;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.view.ScollLinearLayoutManager;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.iv_login_phone)
    ImageView ivLoginPhone;
    @BindView(R.id.main_rv)
    FrameLayout mainRv;

    @BindView(R.id.login_bt)
    Button login_bt;


    private String TAG2 = this.getClass().getSimpleName();
    @BindView(R.id.rule_tv)
    TextView ruleTv;
    @BindView(R.id.main_sv)
    ScrollView mainSv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        //全屏


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mRecyclerView.setAdapter(new SplashAdapter(LoginActivity.this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(LoginActivity.this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);
        if (MyApplication.getInstance().getUserId() != null && MyApplication.getInstance().getUserId().length() > 0) {
            Intent intent = new Intent(mContext, MainActivity.class);//主页方便测试
            startActivity(intent);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    @OnClick({R.id.rule_tv, R.id.iv_login_phone,R.id.login_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rule_tv://用户协议
                Intent intentyh = new Intent(mContext, WebViewActivity.class);
                intentyh.putExtra("webview_title", "用户协议");
                intentyh.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/agreement.html");
                startActivity(intentyh);
                break;
            case R.id.login_bt://手机号登录
                mRecyclerView.stopScroll();
                Log.e("Login", "getUserId=" + MyApplication.getInstance().getUserId() + "  getToken=" + MyApplication.getInstance().getToken());
                if (MyApplication.getInstance().getUserId() != null && MyApplication.getInstance().getUserId().length() > 0) {
                    Intent intent = new Intent(mContext, MainActivity.class);//主页
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, LoginPhoneActivity.class);//手机号登录
                    intent.putExtra("LoginType", "手机号");
                    startActivity(intent);
                }
                break;

            case R.id.iv_login_phone://手机号登录
                mRecyclerView.stopScroll();
                Log.e("Login", "getUserId=" + MyApplication.getInstance().getUserId() + "  getToken=" + MyApplication.getInstance().getToken());
                if (MyApplication.getInstance().getUserId() != null && MyApplication.getInstance().getUserId().length() > 0) {
                    Intent intent = new Intent(mContext, MainActivity.class);//主页
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, LoginPhoneActivity.class);//手机号登录
                    intent.putExtra("LoginType", "手机号");
                    startActivity(intent);
                }

//                Intent intent = new Intent(mContext, TestActivity.class);//手机号登录
//                startActivity(intent);
                break;
        }
    }
    //倒计时函数


    public void qqLogin(View view) {//qq登陆
        authorization(SHARE_MEDIA.QQ);
    }

    public void weiXinLogin(View view) {//微信登陆
        authorization(SHARE_MEDIA.WEIXIN);
    }

    public void sinaLogin(View view) {//微博登录
        authorization(SHARE_MEDIA.SINA);
    }

    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("authorization", "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String add = map.get("city");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                for (int k = 0; k < map.size(); k++) {
                    Log.e("authorization", " " + map.values() + " " + map.toString());
                }
//                Toast.makeText(getApplicationContext(), "name=" + name + ",gender=" + gender, Toast.LENGTH_SHORT).show();

                Log.e("authorization", "onComplete " + "授权完成" + "name=" + name + ",gender=" + gender + "  uid=" + uid + " iconurl=" + iconurl + " openid=" + openid + " map=" + map);
                MyApplication.getInstance().setThirdLoginopenid(openid);
                MyApplication.getInstance().setThirdLoginadd(add);
                MyApplication.getInstance().setThirdLoginname(name);
                MyApplication.getInstance().setThirdLoginpic(iconurl);
                if (gender.equals("男") || gender.equals("女")) {
                    MyApplication.getInstance().setThirdLoginsex(gender);
                } else {
                    MyApplication.getInstance().setThirdLoginsex("女");
                }

                //拿到信息去请求登录接口。。。
                if (share_media.equals(SHARE_MEDIA.QQ)) {//qq登录
                    QQLogin(openid);
                } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {//微信登录
                    WXLogin(openid);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("authorization", "onError " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.e("authorization", "onCancel " + "授权取消");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void QQLogin(String qqOpenId) {//QQ登录
        WeakHashMap map = new WeakHashMap();
        map.put("qqOpenId", qqOpenId);
        postRequest(RetrofitService.QQLogin, map);
    }

    private void WXLogin(String wxOpenId) {//wx登录
        WeakHashMap map = new WeakHashMap();
        map.put("wxOpenId", wxOpenId);
        postRequest(RetrofitService.WXLogin, map);
    }


    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.contains(RetrofitService.Head + RetrofitService.QQLogin)) {
            Log.e("QQLogin", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                if (mainModel.getData().getIsLogin() != null) {
                    if (mainModel.getData().getIsLogin().equals("true")) {
                        MyApplication.getInstance().setUserId(mainModel.getData().getUserId());
                        MyApplication.getInstance().setToken(mainModel.getData().getToken());
                        Intent intent = new Intent(mContext, MainActivity.class);//主页
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, LoginPhoneActivity.class);//手机号登录
                        intent.putExtra("LoginType", "QQ");
                        startActivity(intent);
                    }
                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        } else if (url.contains(RetrofitService.Head + RetrofitService.WXLogin)) {
            Log.e("WXLogin", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                if (mainModel.getData().getIsLogin() != null) {
                    if (mainModel.getData().getIsLogin().equals("true")) {
                        MyApplication.getInstance().setUserId(mainModel.getData().getUserId());
                        MyApplication.getInstance().setToken(mainModel.getData().getToken());
                        Intent intent = new Intent(mContext, MainActivity.class);//主页
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, LoginPhoneActivity.class);//手机号登录
                        intent.putExtra("LoginType", "微信");
                        startActivity(intent);
                    }
                }
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


    //友盟分享
//    public void qq(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
//        );
//    }
//
//    public void weiXin(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
//        );
//    }
//
//    public void weixinCircle(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
//        );
//    }
//
//    public void sina(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
//        );
//    }
//
//    public void Qzone(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
//        );
//    }

}
