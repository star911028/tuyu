package com.fengyuxing.tuyu.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
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
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainDTO;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.SPUtils;
import com.fengyuxing.tuyu.view.ExitWindow;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //设置
    private static final String TAG = "SettingActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.safe_levlel_tv)
    TextView safeLevlelTv;
    @BindView(R.id.safe_ll)
    LinearLayout safeLl;
    @BindView(R.id.news_ll)
    LinearLayout newsLl;
    @BindView(R.id.hmd_ll)
    LinearLayout hmdLl;
    @BindView(R.id.size_tv)
    TextView sizeTv;
    @BindView(R.id.cache_ll)
    LinearLayout cacheLl;
    @BindView(R.id.about_ll)
    LinearLayout aboutLl;
    @BindView(R.id.exit_bt)
    TextView exitBt;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    private ExitWindow mExitWindow;
    private MainDTO userdata = new MainDTO();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(SettingActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        try {
            String size = getFormatSize(getFolderSize(this.getExternalCacheDir()));
            sizeTv.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FindSafeInfo();
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


    @OnClick({R.id.back_iv, R.id.safe_ll, R.id.news_ll, R.id.hmd_ll, R.id.cache_ll, R.id.about_ll, R.id.exit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.safe_ll://账号安全
                Intent intent = new Intent(mContext, SetsafeActivity.class);
                intent.putExtra("userdata", (Serializable) userdata);
                startActivity(intent);
                break;
            case R.id.news_ll://通知设置
                break;
            case R.id.hmd_ll://黑名单
                intent = new Intent(mContext, MyBlackerActivity.class);
                startActivity(intent);
                break;
            case R.id.cache_ll://清除缓存
                dialog();
                break;
            case R.id.about_ll://关于兔语
                intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit_bt://退出登录
//                logoutDialog();
                logOut();
                break;
        }
    }


//    protected void logoutDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("确认退出登录？");
//        builder.setTitle("提示");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                logOut();
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }

    private void logOut() {
        mExitWindow = new ExitWindow(SettingActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mExitWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mExitWindow.dismiss();
                                break;
                            case 5://退出登录
                                SPUtils.clear(SettingActivity.this);
                                MyApplication.getInstance().setUserId("");//重置
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                break;
                        }
                    }
                });
        mExitWindow.setClippingEnabled(false);
        mExitWindow.showAtLocation(mainLv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认清除缓存？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAllCache(SettingActivity.this);
                try {
                    String size = getFormatSize(getFolderSize(SettingActivity.this.getExternalCacheDir()));
                    sizeTv.setText(size);
                    Toast.makeText(SettingActivity.this, "清除缓存成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    public void FindSafeInfo() {
        //查询用户安全信息
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).build();
        final Request request = new Request.Builder().url(NetConstant.API_FindSafeInfo).post(formBody).build();
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
                Log.e("FindSafeInfo", "responseStr=" + responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        if (mMainModel.getCode().equals("1")) {
                            if (mMainModel.getData() != null) {
                                userdata = mMainModel.getData();
                                if (mMainModel.getData().getIdentityStatus().equals("0")) {
                                    safeLevlelTv.setText("低");
                                } else if (mMainModel.getData().getIdentityStatus().equals("1")) {
                                    safeLevlelTv.setText("中");
                                } else if (mMainModel.getData().getIdentityStatus().equals("2")) {
                                    safeLevlelTv.setText("高");
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

}
