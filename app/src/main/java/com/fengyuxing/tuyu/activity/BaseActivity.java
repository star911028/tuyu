package com.fengyuxing.tuyu.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.media.imagepicker.view.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.fengyuxing.tuyu.util.OSUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.receiver.ExitAppReceiver;
import com.fengyuxing.tuyu.util.HttpChannel;
import com.fengyuxing.tuyu.util.UILImageLoader;
import com.fengyuxing.tuyu.view.ConstantsString;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;

/**
 * Created by Administrator on 2017/10/16.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Context mContext;
    protected TextView titleCenter;
    protected ImageView titleLeft;
    protected HttpManager mHttpManager;
    protected UILImageLoader mUILImageLoader;
    //    public MemberDTO userInfo;
//    protected UpdateApkDialog updateApkDialog;
    private ExitAppReceiver exitReceiver = new ExitAppReceiver();
    //自定义退出应用Action,实际应用中应该放到整个应用的Constant类中.
    protected static final String EXIT_APP_ACTION = "com.micen.exit_app";
    //private NetWorkStateReceiver netWorkStateReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        MyStatusBarUtil.setStatusTransparent(this, false);
        //    getWindow().setFlags(0x80000000, 0x80000000);
        // 系统 6.0 以上 状态栏白底黑字的实现方法
        mContext = this;
//        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        MIUISetStatusBarLightMode(this.getWindow(), true);
//        FlymeSetStatusBarLightMode(this.getWindow(), true);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setStatusBar(mContext.getColor(R.color.white));
        mHttpManager = HttpManager.getInstance();
        if (mUILImageLoader == null) {
            mUILImageLoader = new UILImageLoader(mContext);
        }
        registerExitReceiver();
        // handle custom intent

        handleCustomIntent(getIntent());
        initView(savedInstanceState);
        initTitle();
        initEventListeners();
        initializeData();
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
    }


    private void registerExitReceiver() {

        IntentFilter exitFilter = new IntentFilter();
        exitFilter.addAction(EXIT_APP_ACTION);
        registerReceiver(exitReceiver, exitFilter);
    }

    private void unRegisterExitReceiver() {

        unregisterReceiver(exitReceiver);
    }


    protected void doout() {
        //    SPUtils.remove(mContext, SPConstant.USER_ID);
        //    SPUtils.remove(mContext, SPConstant.IS_LOGIN);

        //最后在要退出App的方法中添加以下发送广播代码即可.
        Intent intent = new Intent();
        intent.setAction(EXIT_APP_ACTION);
        sendBroadcast(intent);
    }


    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
       /* unregisterReceiver(netWorkStateReceiver);
        System.out.println("注销");*/
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unRegisterExitReceiver();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    private void initTitle() {
        if (findViewById(R.id.finish_header_tv_title) != null && findViewById(R.id.finish_header_iv_finish) != null) {
            titleCenter = (TextView) findViewById(R.id.finish_header_tv_title);
            titleLeft = (ImageView) findViewById(R.id.finish_header_iv_finish);
            if (titleCenter != null) {
                customTitleCenter(titleCenter);
            }
            if (titleLeft != null) {
                titleLeft.setOnClickListener(this);
            }
        }
    }

    protected void clickTitleLeft() {
        finish();
    }


    /**
     * init centerTitle
     *
     * @param titleCenter
     */
    protected void customTitleCenter(TextView titleCenter) {
        //默认"我的"
        titleCenter.setText("我的");
    }

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initEventListeners();

    protected void handleCustomIntent(Intent intent) {
        // Nothing to do
    }

    public void postRequest(String url, WeakHashMap weakHashMap) {
        weakHashMap.put("key", ConstantsString.key);
        weakHashMap.put("source", ConstantsString.source);
        Call<String> call = HttpChannel.getInstance().getRetrofitService().post(url, weakHashMap);
        call.enqueue(callback);

    }

    public void postFile(String url, WeakHashMap<String, RequestBody> map, File file, String type) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(type, file.getName(), requestFile);
        map.put("key", toRequestBody(ConstantsString.key));
        map.put("source", toRequestBody(ConstantsString.source));
        Call<String> call = HttpChannel.getInstance().getRetrofitService().postFile(url, map, body);
        call.enqueue(callback);
    }

    protected RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

    Callback<String> callback = new Callback<String>() {

        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.body() == null) {
                Log.i(TAG, "onResponse: body=null");
                return;
            }
            Log.i(TAG, "http返回：" + call.request().url() + " 结果： " + response.body() + "");
            if (!isDestroyed()) {
                onCalllBack(call, response, response.body(), call.request().url().toString());
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.i(TAG, "onFailure: url= " + call.request().url() + " exception: " + t.toString());
        }
    };


    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {

    }
//    public void UpdateVersionCode(final boolean isCheck) {
//        Log.e("UpdateVersionCode","getDeviceToken="+MyApplication.getInstance().getDeviceToken()+"  getUserId="+CommonUtils.getUserId(mContext));
//        mHttpManager.checkVesion(MyApplication.getInstance().getDeviceToken(),new HttpManager.HttpCallback<Result<Version>>() {
//            @Override
//            public void onSuccess(int id, Result<Version> result) {
//                if (result.isOK()) {
//                    version = result.getObject();
//                    if (version != null) {
//                        updateApkDialog = new UpdateApkDialog(BaseActivity.this, version);
//                        updateApkDialog.show();
//                        updateApkDialog.setCanceledOnTouchOutside(false);
//                        updateApkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                            @Override
//                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                                    // 操作
//                                    if(version.getIsforce()==0){
//                                        updateApkDialog.dismiss();
//                                        return false;
//                                    }
//                                }
//                                return true;
//                            }
//                        });
//                    }
//                    if (isCheck && StringUtils.isNotEmpty(result.getMessage())) {
//                        SPUtils.showToast(mContext, result.getMessage());
//                    }
//                } else {
//                    if (isCheck && StringUtils.isNotEmpty(result.getMessage())) {
//                        SPUtils.showToast(mContext, result.getMessage());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onError(int id, int errCode) {
//                L.e("e====", "版本更新" + errCode);
//            }
//        });
//
//    }


//    public void UpdateVersionCode(final boolean isCheck)  {//版本信息接口
////        参数说明： versionCode  版本号
////        clientSystem   系统类型 ： 1Android 2IOS
//        Log.e("UpdateVersionCode","versionCode="+CommonUtils.getVersionCode(mContext) + "");
//        OkHttpClient client = new OkHttpClient();
//        FormBody formBody = new FormBody.Builder().add("versionCode", CommonUtils.getVersionCode(mContext) + "").add("clientSystem", "1").build();
//        final Request request = new Request.Builder().url(NetConstant.API_GetVersion).post(formBody).build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                final String responseStr = response.body().string();
//                Log.e("UpdateVersionCode", "responseStr=" + responseStr);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        final VersionDTO mVersionDTO = new Gson().fromJson(responseStr,
//                                new TypeToken<VersionDTO>() {
//                                }.getType());
//                        if(mVersionDTO!=null){
//                            if (mVersionDTO.getMsg().equals("success")) {
//                                if(mVersionDTO.getData()!=null){
//                                    updateApkDialog = new UpdateApkDialog(BaseActivity.this, mVersionDTO.getData());
//                                    updateApkDialog.show();
//                                    updateApkDialog.setCanceledOnTouchOutside(false);
//                                    updateApkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                                        @Override
//                                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                                            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                                                // 操作
//                                                if(mVersionDTO.getData().getIsUppdate()==0){
//                                                    updateApkDialog.dismiss();
//                                                    return false;
//                                                }
//                                            }
//                                            return true;
//                                        }
//                                    });
//                                }
//                                if (isCheck && StringUtils.isNotEmpty(mVersionDTO.getMsg())) {
//                                    SPUtils.showToast(mContext, mVersionDTO.getMsg());
//                                }
//                            } else {
//                                Toast.makeText(mContext, mVersionDTO.getMsg(), Toast.LENGTH_SHORT).show();
//                            }
//                        }else {
//                            Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//    }


    /**
     * Initialize custom data
     */
    protected void initializeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_header_iv_finish: {
                Log.i("BaseActivity", "点击了顶部菜单左侧返回键");
                clickTitleLeft();
                break;
            }
            default:
                handleCustomClick(v);
                break;
        }
    }

    protected void handleCustomClick(View view) {
        switch (view.getId()) {
            case R.id.finish_header_iv_finish: {
                clickTitleLeft();
                break;
            }
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    protected boolean hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    /**********************状态栏*****************************/
    public static final int TYPE_MIUI = 0;
    public static final int TYPE_FLYME = 1;
    public static final int TYPE_M = 3;//6.0

    @IntDef({TYPE_MIUI,
            TYPE_FLYME,
            TYPE_M})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    public void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(colorId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTintManager,需要先将状态栏设置为透明
            setTranslucentStatus(activity);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
            systemBarTintManager.setStatusBarTintEnabled(true);//显示状态栏
            systemBarTintManager.setStatusBarTintColor(colorId);//设置状态栏颜色
        }
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    public void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }


    /**
     * 设置状态栏深色浅色切换
     */
    public boolean setStatusBarLightTheme(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarFontIconDark(activity, TYPE_M, dark);
            } else if (OSUtils.isMiui()) {
                setStatusBarFontIconDark(activity, TYPE_MIUI, dark);
            } else if (OSUtils.isFlyme()) {
                setStatusBarFontIconDark(activity, TYPE_FLYME, dark);
            } else {//其他情况
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    public boolean setStatusBarFontIconDark(Activity activity, int type, boolean dark) {
        switch (type) {
            case TYPE_MIUI:
                return setMiuiUI(activity, dark);
            case TYPE_FLYME:
                return setFlymeUI(activity, dark);
            case TYPE_M:
            default:
                return setCommonUI(activity, dark);
        }
    }

    //设置6.0 状态栏深色浅色切换
    public boolean setCommonUI(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                if (decorView.getSystemUiVisibility() != vis) {
                    decorView.setSystemUiVisibility(vis);
                }
                return true;
            }
        }
        return false;

    }

    //设置Flyme 状态栏深色浅色切换
    public boolean setFlymeUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //设置MIUI 状态栏深色浅色切换
    public boolean setMiuiUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            Class<?> clazz = activity.getWindow().getClass();
            @SuppressLint("PrivateApi") Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getDeclaredMethod("setExtraFlags", int.class, int.class);
            extraFlagField.setAccessible(true);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
