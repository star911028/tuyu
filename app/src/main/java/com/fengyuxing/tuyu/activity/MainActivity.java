package com.fengyuxing.tuyu.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.MyReceiver;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.fragment.MainFragment;
import com.fengyuxing.tuyu.fragment.MineFragment;
import com.fengyuxing.tuyu.fragment.NewsFragment;
import com.fengyuxing.tuyu.http.UpdateAppHttpUtil;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.view.MyRadioButton;
import com.fengyuxing.tuyu.view.NeedPWDWindow;
import com.fengyuxing.tuyu.view.UpdateWindow;
import com.fengyuxing.tuyu.yunxin.DemoCache;
import com.fengyuxing.tuyu.yunxin.UserPreferences;
import com.fengyuxing.tuyu.yunxin.helper.SystemMessageUnreadManager;
import com.fengyuxing.tuyu.yunxin.reminder.ReminderItem;
import com.fengyuxing.tuyu.yunxin.reminder.ReminderManager;
import com.fengyuxing.tuyu.yunxin.reminder.ReminderSettings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.fengyuxing.tuyu.activity.MainRoomActivity.returnActivityB;
import static com.fengyuxing.tuyu.activity.MainRoomActivity.returnActivitySmall;


public class MainActivity extends BaseActivity implements View.OnClickListener, ReminderManager.UnreadNumChangedCallback, MyReceiver.Message {


    @BindView(R.id.room_news_ll)
    FrameLayout room_news_ll;
    @BindView(R.id.img_iv)
    ImageView img_iv;

    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.new_num_tv)
    TextView new_num_tv;
    @BindView(R.id.tab_a)
    MyRadioButton tabA;
    @BindView(R.id.tab_b)
    MyRadioButton tabB;
    @BindView(R.id.tab_c)
    MyRadioButton tabC;
    @BindView(R.id.activity_main_rgs)
    RadioGroup mViewgroup;
    @BindView(R.id.bgRelativeLayout)
    RelativeLayout bgRelativeLayout;
    @BindView(R.id.bg_iv)
    ImageView bgIv;
    private List<Fragment> mFragments = new ArrayList<>();
    private AbortableFuture<LoginInfo> loginRequest;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    MyReceiver myReceiver;
    private DataList userdata = new DataList();
    private String phoneNumber = "";
    private Boolean Needpass = false;
    private Boolean toastnews = false;
    private NeedPWDWindow mNeedPWDWindow;
    private String UrlRoomid = "";
    private String updateUrl = "";
    private UpdateWindow mUpdateWindow;
    private String VersionNumber = "";
    private String updateContent = "";
    private Boolean IsMustUpdate = false;
    int black, white, b4bdcc, ff828b99;
    private Boolean ISDownload = false;
    public static boolean HasMainActivity;//是否直接返回房间  在Mainactivity判断
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HasMainActivity=true;
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        }

        Log.e("onCreate", "isRunningForeground=" + isRunningForeground(MainActivity.this));
        setTranslucentStatus(MainActivity.this);
        setStatusBarLightTheme(MainActivity.this, true);

        if (MyApplication.getInstance().getUserId() != null && MyApplication.getInstance().getUserId().length() > 0) {
            getFindUserInfo();//用于获取云信 token
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);//手机号登录
            startActivity(intent);
            return;
        }
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //注册广播接收器
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.nangch.broadcasereceiver.MYRECEIVER");
        registerReceiver(myReceiver, intentFilter);
        //因为这里需要注入Message，所以不能在AndroidManifest文件中静态注册广播接收器
        myReceiver.setMessage(this);

        CheckVesion();//查询版本信息
        int width=  getScreenWidth(mContext);
        Log.e("getScreenWidth","width="+width);
        if(returnActivitySmall){

        }
    }
    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }


    public void CheckVesion() {
//        versionNumebr	是	string	当前版本号        os	是	string	操作系统 ios/android
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("versionNumebr", MyApplication.getVersionname());//版本号  版本号格式要求 X.X.X
        map.put("os", "android");
        postRequest(RetrofitService.FindVersion, map);
        Log.e("FindVersion", "versionNumebr=" + MyApplication.getVersionname() + " " + "getVersionCode=" + MyApplication.getVersionCode());

//        updateDiy2();
    }


    @Override
    public void getMsg(String str1, String str2) {
        //通过实现MyReceiver.Message接口可以在这里对MyReceiver中的数据进行处理
        Log.e("MyReceiver", "收到广播 " + str1 + " " + str2);//用户云心账号 昵称
        if (str1.length() == 11) {
            phoneNumber = str1;
            getFindUserInfoChat();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MainActivity", "onResume");
        if(returnActivityB){//跳转到直播间
            returnActivityB=false;
            Intent intentyh = new Intent(mContext, MainRoomActivity.class);
            intentyh.putExtra("roomid", MyApplication.getInstance().getExitRoomid());//传递房间id
            intentyh.putExtra("roomdata", (Serializable) null);//传递房间数据
            startActivity(intentyh);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.e("MainActivity", "onResume");
//        if(returnActivityB){//跳转到直播间
//            returnActivityB=false;
//            Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//            intentyh.putExtra("roomid", MyApplication.getInstance().getExitRoomid());//传递房间id
//            intentyh.putExtra("roomdata", (Serializable) null);//传递房间数据
//            startActivity(intentyh);
//        }
    }



    @Subscribe
    public void onEventMainThread(TabCheckEvent event) {//首页最小化图标
        Log.e("onEventMainThread", "TabCheckEvent.getMsg()= " + event.getMsg());
        if (event.getMsg() != null) {
            if (event.getMsg().equals("最小化")) {
                Log.e("onEventMainThread", "getExitRoomid= " + MyApplication.getInstance().getExitRoomid() + "  getExitRoomdata=" + MyApplication.getInstance().getExitRoomdata());
                if (MyApplication.getInstance().getExitRoomid() != null && MyApplication.getInstance().getExitRoomdata() != null) {
                    DataList mDataList = new Gson().fromJson(MyApplication.getInstance().getExitRoomdata(),
                            new TypeToken<DataList>() {
                            }.getType());
                    Log.e("最小化", "mDataList=" + mDataList.getHosterPortraitPath());
                    room_news_ll.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.room_small).into(bgIv);//设置GIF播放图
                    if (mDataList.getHosterPortraitPath() == null) {//别的房间最小化
                        Glide.with(mContext)//设置头像
                                .load(MyApplication.getInstance().getUserImg())
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(img_iv);
                    } else {//自己的房间最小化
                        Glide.with(mContext)//设置头像
                                .load(mDataList.getHosterPortraitPath())
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(img_iv);
                    }

                } else {
                    room_news_ll.setVisibility(View.GONE);
                }
            } else if (event.getMsg().equals("退出房间")) {
                room_news_ll.setVisibility(View.GONE);
            }
        }

    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        room_news_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataList mDataList = new Gson().fromJson(MyApplication.getInstance().getExitRoomdata(),
                        new TypeToken<DataList>() {
                        }.getType());
//                Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                intentyh.putExtra("roomid", MyApplication.getInstance().getExitRoomid());//传递房间id
//                intentyh.putExtra("roomdata", (Serializable) mDataList);//传递房间数据
//                startActivity(intentyh);
                Log.e("room_news_ll_onClick", "getMyRoomid=" + MyApplication.getInstance().getMyRoomid() + "   getExitRoomid=" + MyApplication.getInstance().getExitRoomid());
                if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                    if (!MyApplication.getInstance().getMyRoomid().equals(MyApplication.getInstance().getExitRoomid())) {//不是同一个房间 先退出MainRoomActivity
                        EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                    }
                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                    intentyh.putExtra("roomid", MyApplication.getInstance().getExitRoomid());//传递房间id
                    intentyh.putExtra("roomdata", (Serializable) mDataList);//传递房间数据
                    startActivity(intentyh);
                } else {//当前没有在房间
                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                    intentyh.putExtra("roomid", MyApplication.getInstance().getExitRoomid());//传递房间id
                    intentyh.putExtra("roomdata", (Serializable) mDataList);//传递房间数据
                    startActivity(intentyh);
                }
                room_news_ll.setVisibility(View.GONE);


            }
        });

    }

    public void getFindUserInfo() {//查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", MyApplication.getInstance().getUserId());
        postRequest(RetrofitService.FindUserInfo, map);
    }

    public void getFindUserInfoChat() {//聊天页面查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("phoneNumber", phoneNumber);
        postRequest(RetrofitService.FindUserInfo, map);
    }

    private void initData() {
        mFragments.add(new NewsFragment());
        mFragments.add(new MainFragment());
        mFragments.add(new MineFragment());
        ((MyRadioButton) mViewgroup.getChildAt(1)).setChecked(true);
//        replaceFragment(0);
    }

    @Override
    protected void initEventListeners() {
        mViewgroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        initData();
    }


    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View checkedRb = group.findViewById(checkedId);
            int index = group.indexOfChild(checkedRb);
            replaceFragment(index);
//            if(index==2){
//                setTranslucentStatus(MainActivity.this);
//                setStatusBarLightTheme(MainActivity.this,false);
//            }else {
//                setStatusBarLightTheme(MainActivity.this, true);
//                setStatusBarColor(MainActivity.this, white);
//            }
        }
    };

    private void replaceFragment(int index) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        FragmentTransaction replace = fragmentTransaction.replace(R.id.fragment, mFragments.get(index));
        replace.commitAllowingStateLoss();//解决IllegalStateException: Can not perform this action after onSaveInstanceState
    }

    /**
     * 再按一次退出程序
     * 判断在一定的时间内连续点击两次才退出程序
     */

    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Boolean isshow = mUpdateWindow.isShowing();
        if (mUpdateWindow != null && mUpdateWindow.isShowing()) {
            Log.e("onKeyDown", "isShowing=" + mUpdateWindow.isShowing());
            return false;
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - touchTime) >= waitTime) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序",
                            Toast.LENGTH_SHORT).show();
                    touchTime = currentTime;
                } else {
                    doout();
                }
                return true;
            } else {
//                Log.e("onKeyDown", "isShowing=" + mUpdateWindow.isShowing());
            }
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindUserInfo)) {
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            Log.e("FindUserInfo", "result=" + result);
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    if (phoneNumber.length() > 0) {//跳转到个人主页
                        userdata = mMainModel.getData();
                        Intent intent = new Intent(mContext, UserInfoctivity.class);//
                        intent.putExtra("userinfoid", userdata.getUserId());
                        intent.putExtra("userdata", (Serializable) userdata);
                        startActivity(intent);
                    } else {
                        MyApplication.getInstance().setYXtoken(mMainModel.getData().getYunXinToken());//保存云信token
                        MyApplication.getInstance().setYXcode(mMainModel.getData().getAccid());
                        MyApplication.getInstance().setTuercode(mMainModel.getData().getTuId());
                        if (mMainModel.getData().getPortraitPathArray().length > 0) {
                            MyApplication.getInstance().setUserImg(mMainModel.getData().getPortraitPathArray()[0]);
                        }
                        MyApplication.getInstance().setUserName(mMainModel.getData().getUsername());
                        MyApplication.getInstance().reInitZegoSDK();//重新初始化即构
                        doLogin();//登陆云信
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
                if (mMainModel.getErrorMsg().contains("拉黑")) {
                    Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Needpass = false;//重新打开密码输入框
                    if (toastnews) {
                        Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {
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
        } else if (url.contains(RetrofitService.FindVersion)) {
            Log.e("FindVersion", "result=" + result);//    "downLoadUrl": "",   "canUpdate": true,//表示是否有新版本     "needUpdate": true,  //表示是否需要强制更新       "versionNumber": "1.0.0",     "content": "第一个版本"
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    if (mMainModel.getData().getCanUpdate() != null) {
                        if (mMainModel.getData().getCanUpdate().equals("true")) {//需要更新
                            try {
                                updateContent = URLDecoder.decode(mMainModel.getData().getContent(), "utf-8");//更新内容
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            VersionNumber = mMainModel.getData().getVersionNumber();//版本号
                            updateUrl = mMainModel.getData().getDownLoadUrl();//下载地址
                            if (mMainModel.getData().getNeedUpdate() != null) {
                                if (mMainModel.getData().getNeedUpdate().equals("true")) {//强制更新
                                    IsMustUpdate = true;
                                    UpdateWindow("1", mMainModel.getData().getContent(), mMainModel.getData().getVersionNumber());
                                } else {//非强制更新
                                    IsMustUpdate = false;
                                    UpdateWindow("2", mMainModel.getData().getContent(), mMainModel.getData().getVersionNumber());
                                }
                            } else {//非强制更新
                                IsMustUpdate = false;
                                UpdateWindow("2", mMainModel.getData().getContent(), mMainModel.getData().getVersionNumber());
                            }
                        } else {//不需要更新

                        }
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

    public void doLogin() {//云信登录
        Log.e("doLogin", "getYXcode=" + MyApplication.getInstance().getYXcode() + "  getYXtoken=" + MyApplication.getInstance().getYXtoken());
        LoginInfo info = new LoginInfo(MyApplication.getInstance().getYXcode(), MyApplication.getInstance().getYXtoken(), RetrofitService.YXAppKey); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Log.e("doLogin", "onSuccess param=" + " getToken=" + param.getToken() + "  getAccount=" + param.getAccount() + "  getAppKey=" + param.getAppKey());
//                        DemoCache.setAccount(MyApplication.getInstance().getYXcode());
//                        saveLoginInfo(MyApplication.getInstance().getYXcode(), MyApplication.getInstance().getYXtoken());
//                        // 初始化消息提醒配置
//                        initNotificationConfig();
                        //接收H5跳转信息   tuerapp://jp.app/openwith?type=0&id=26
                        Intent i_getvalue = getIntent();
                        String action = i_getvalue.getAction();
                        if (Intent.ACTION_VIEW.equals(action)) {
                            Uri uri = i_getvalue.getData();
                            if (uri != null) {
                                String type = uri.getQueryParameter("type");
                                String roomid = uri.getQueryParameter("id");
                                Log.e("接收H5跳转信息", "uri=" + uri + "  type=" + type + "   roomid=" + roomid);//
                                UrlRoomid = roomid;
                                if (type.equals("0")) {//首页

                                } else if (type.equals("1")) {//进入房间页面
                                    toastnews = false;
                                    getFindRoomInfo();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        Log.e("doLogin", "onFailed code1=" + code);
                        doLogin();//失败了再来
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Log.e("doLogin", "onException =" + exception);
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);

        registerMsgUnreadInfoObserver(true);//注册未读消息数量观察者
        registerSystemMessageObservers(true);// 注册/注销系统消息未读数变化
    }

    private void saveLoginInfo(final String account, final String token) {
        MyApplication.getInstance().setYXcode(account);
        MyApplication.getInstance().setYXtoken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }


    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }


    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);

    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
            Log.e("onUnreadNumChanged", "onEvent" + " unreadCount=" + unreadCount);
        }
    };


    //未读消息数量观察者实现
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        int unread = item.unread();//未读消息数
        if (unread > 0) {
            new_num_tv.setText(String.valueOf(ReminderSettings.unreadMessageShowRule(unread)));
        } else {
            new_num_tv.setText("0");
        }
        Log.e("onUnreadNumChanged", "unread=" + unread);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
        HasMainActivity=false;
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
        DropManager.getInstance().destroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        unregisterReceiver(myReceiver);     //注销广播接收器
    }

    public void getFindRoomInfo() {//进入直播间查询信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", UrlRoomid);
        if (Needpass) {//有密码才传这个字段
            map.put("password", MyApplication.getInstance().getInputpass());
        }
        Log.e("getFindRoomInfo", "password=" + MyApplication.getInstance().getInputpass());
        postRequest(RetrofitService.FindRoomInfo, map);
    }

    //密码弹出框
    private void NeedPWDwindow() {//
        mNeedPWDWindow = new NeedPWDWindow(MainActivity.this,
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
                                toastnews = true;
                                getFindRoomInfo();
                                //TODO
                                break;
                        }
                    }
                });
        mNeedPWDWindow.setClippingEnabled(false);
        mNeedPWDWindow.showAtLocation(bgRelativeLayout, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    //版本更新弹出窗
    private void UpdateWindow(String type, String content, String versioncode) {//
        mUpdateWindow = new UpdateWindow(MainActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 5://版本更新
                                if (ISDownload) {
                                    Toast.makeText(mContext, "正在下载最新版本,请稍后", Toast.LENGTH_SHORT).show();
                                } else {
                                    installProcess();//下载应用前判断权限
                                }
                                break;
                        }
                    }
                }, type, content, versioncode);
        mUpdateWindow.setClippingEnabled(false);
        mUpdateWindow.showAtLocation(bgRelativeLayout, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    public void onlyDownload() {
        Log.e("onlyDownload", "开始下载");
        Toast.makeText(mContext, "正在下载最新版本,请稍后", Toast.LENGTH_SHORT).show();
        UpdateAppBean updateAppBean = new UpdateAppBean();

        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(updateUrl);

        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = getCacheDir().getAbsolutePath();
        }
        Log.e("onlyDownload", "path=" + path);
        //设置apk 的保存路径
        updateAppBean.setTargetPath(path);
        //实现网络接口，只实现下载就可以
        updateAppBean.setHttpManager(new UpdateAppHttpUtil());

        UpdateAppManager.download(this, updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
                ISDownload = true;
//                HProgressDialogUtils.showHorizontalProgressDialog(MainActivity.this, "下载进度", false);
                Log.e("onlyDownload", "onStart() called");
            }

            @Override
            public void onProgress(float progress, long totalSize) {
//                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                Log.e("onlyDownload", "onProgress() called with: progress = [" + progress + "], totalSize = [" + totalSize + "]");
            }

            @Override
            public void setMax(long totalSize) {
                Log.e("onlyDownload", "setMax() called with: totalSize = [" + totalSize + "]");
            }

            @Override
            public boolean onFinish(File file) {
//                HProgressDialogUtils.cancel();
                ISDownload = false;
                Log.e("onlyDownload", "onFinish() called with: file = [" + file.getAbsolutePath() + "]");
                openAPK(file);//安装APP
                return true;
            }

            @Override
            public void onError(String msg) {
//                HProgressDialogUtils.cancel();
                ISDownload = false;
                Log.e("onlyDownload", "onError() called with: msg = [" + msg + "]");
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                Log.e("onlyDownload", "onInstallAppAndAppOnForeground() called with: file = [" + file + "]");
                return false;
            }
        });
    }

    /**
     * 安装apk
     *
     * @param //fileSavePath
     */
    private void openAPK(File file) {
        String filePath = file.getAbsolutePath();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
            // 生成文件的uri，，
            // 注意 下面参数com.ausee.fileprovider 为apk的包名加上.fileprovider，
            data = FileProvider.getUriForFile(MainActivity.this, "com.fengyuxing.tuyu.fileprovider", new File(filePath));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
        } else {
            data = Uri.fromFile(file);
        }

        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    //安装应用的流程
    private void installProcess() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                Toast.makeText(mContext, "安装应用需要打开未知来源权限，请去设置中开启权限", Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startInstallPermissionSettingActivity();
                }
                return;
            }
        }
        //有权限，开始下载应用程序
        onlyDownload();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, 10086);
    }


    private boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) &&
                currentPackageName.equals(getPackageName())) {
            return true;
        }
        return false;
    }

    //    请求权限
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {
//            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, BAIDU_READ_PHONE_STATE);
        } else {
//            startLocation();
            Log.e("onRequestPermissions", "获取到权限");
        }
    }

    //    Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if(grantResults.length>0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
//                    startLocation();
                        Log.e("onRequestPermissions", "获取到权限");
                    } else {
                        // 没有获取到权限，做特殊处理
                        Toast.makeText(mContext.getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
