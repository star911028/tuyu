package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
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
import com.wildma.idcardcamera.camera.IDCardCamera;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.view.SMYZWindow;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainSmyzActivity extends BaseActivity implements View.OnClickListener {
    //实名验证
    private static final String TAG = "MainSmyzActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.img_iv_1)
    ImageView imgIv1;
    @BindView(R.id.send_pic1_tv)
    TextView sendPic1Tv;
    @BindView(R.id.img_iv_2)
    ImageView imgIv2;
    @BindView(R.id.send_pic2_tv)
    TextView sendPic2Tv;
    @BindView(R.id.send_bt)
    TextView sendBt;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    private String img1path = "";
    private String img2path = "";
    private SMYZWindow mSMYZWindow;
    private String uppiccode = "";
    private String Data = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smyz;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MainSmyzActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
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


    @OnClick({R.id.back_iv, R.id.send_pic1_tv, R.id.send_pic2_tv, R.id.send_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.send_pic1_tv://上传正面照片
                IDCardCamera.create(MainSmyzActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                break;
            case R.id.send_pic2_tv://上传反面照片
                IDCardCamera.create(MainSmyzActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                break;
            case R.id.send_bt://上传照片 提交审核
                InitUserInfo();//上传照片
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    imgIv1.setImageBitmap(BitmapFactory.decodeFile(path));
                    img1path = path;
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    imgIv2.setImageBitmap(BitmapFactory.decodeFile(path));
                    img2path = path;
                }
            }
            if (img1path.length() > 0 && img2path.length() > 0) {
                sendBt.setBackgroundResource(R.drawable.btn_sure);
                sendBt.setEnabled(true);
            } else {
                sendBt.setBackgroundResource(R.drawable.btn_sure_nor);
                sendBt.setEnabled(false);
            }
        }
    }


    private void SMYZWindow() {//
        mSMYZWindow = new SMYZWindow(MainSmyzActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mSMYZWindow.dismiss();
                                finish();
                                break;
                        }
                    }
                });
        mSMYZWindow.setClippingEnabled(false);
        mSMYZWindow.showAtLocation(mainLv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    public void InitUserInfo() {
        try {//上传头像
            String URL = "&userId=" + MyApplication.getInstance().getUserId() + "&token=" + MyApplication.getInstance().getToken();
            Log.e("InitUserInfo", "URL=" + URL);
            uploadImage(NetConstant.API_UploadIdImage + URL, img1path, img2path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("uploadImage", "IOException=" + e);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("uploadImage", "JSONException=" + e);
        }
    }

    /**
     * 上传图片
     *
     * @param url
     * @param imagePath1 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    public String uploadImage(String url, String imagePath1, String imagePath2) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("imagePath", imagePath1 + " \n" + url);
        File file1 = new File(imagePath1);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file1);
        File file2 = new File(imagePath2);
        RequestBody image2 = RequestBody.create(MediaType.parse("image/png"), file2);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("frontImgFile", imagePath1, image)//上传文件在参数名 文件路径 本地路径
                .addFormDataPart("backImgFile", imagePath2, image2)//上传文件在参数名 文件路径 本地路径
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("imagePath", "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("imagePath", "onResponse: " + response.body().string());
                MainModel mMainModel = new Gson().fromJson(response.body().string(),
                        new TypeToken<MainModel>() {
                        }.getType());
                uppiccode = mMainModel.getCode();
                if (uppiccode.equals("1")) {
                    Looper.prepare();
                    SMYZWindow();
                    Looper.loop();
                } else if (uppiccode.equals("0")) {
                    Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                    MyApplication.getInstance().setUserId("");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return Data;

    }
}
