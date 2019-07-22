package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
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
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.http.UpdateAppHttpUtil;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.view.UpdateWindow;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    //关于兔语
    private static final String TAG = "AboutActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.version_code_tv)
    TextView versionCodeTv;
    @BindView(R.id.check_update_tv)
    TextView checkUpdateTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.rule_tv)
    TextView ruleTv;
    @BindView(R.id.ys_tv)
    TextView ysTv;
    private String updateUrl = "";
    private UpdateWindow mUpdateWindow;
    private String VersionNumber = "";
    private String updateContent = "";
    private Boolean IsMustUpdate = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(AboutActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        versionCodeTv.setText("版本：" + MyApplication.getVersionname());
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


    @OnClick({R.id.back_iv, R.id.check_update_tv, R.id.rule_tv, R.id.ys_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.check_update_tv://检查更新
                CheckVesion();//查询版本信息
                break;
            case R.id.rule_tv://服务协议
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("webview_title", "隐私与协议");
                intent.putExtra("webview_url", " http://ty.fengyugo.com/h5/h5/agreement.html");
                startActivity(intent);
                break;
            case R.id.ys_tv://隐私
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("webview_title", "隐私与协议");
                intent.putExtra("webview_url", " http://ty.fengyugo.com/h5/h5/agreement.html");
                startActivity(intent);


                break;
        }
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
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.contains(RetrofitService.FindVersion)) {
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
                            Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_SHORT).show();
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

    //版本更新弹出窗
    private void UpdateWindow(String type, String content, String versioncode) {//
        mUpdateWindow = new UpdateWindow(AboutActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 5://版本更新
                                installProcess();//下载应用前判断权限
                                break;
                        }
                    }
                }, type, content, versioncode);
        mUpdateWindow.setClippingEnabled(false);
        mUpdateWindow.showAtLocation(mainLv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
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
                Log.e("onlyDownload", "onFinish() called with: file = [" + file.getAbsolutePath() + "]");
                openAPK(file);//安装APP
                return true;
            }

            @Override
            public void onError(String msg) {
//                HProgressDialogUtils.cancel();
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
            data = FileProvider.getUriForFile(AboutActivity.this, "com.fengyuxing.tuyu.fileprovider", new File(filePath));
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


}
