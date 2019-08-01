package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.datepicker.CustomDatePicker;
import com.fengyuxing.tuyu.datepicker.DateFormatUtils;
import com.fengyuxing.tuyu.util.PermissionUtils;
import com.fengyuxing.tuyu.util.RetrofitService;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.WeakHashMap;

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

public class AddinfoActivity extends ImageBaseActivity implements View.OnClickListener {
    //完善资料
    private static final String TAG = "AddinfoActivity";
    @BindView(R.id.head_img_pc)
    ImageView headImgPc;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.age_tv)
    TextView ageTv;
    @BindView(R.id.ok_bt)
    Button okBt;
    @BindView(R.id.main_sv)
    ScrollView mainSv;
    @BindView(R.id.main_rv)
    RelativeLayout mainRv;
    @BindView(R.id.age_ll)
    LinearLayout ageLl;
    @BindView(R.id.radioButton_man)
    RadioButton radioButtonMan;
    @BindView(R.id.radioButton_women)
    RadioButton radioButtonWomen;
    @BindView(R.id.rg)
    RadioGroup radioGroup;
    private CustomDatePicker mDatePicker, mTimerPicker;
    private String gender = "男";
    private File headfile;
    private String Imgadd = "";
    private String userbirth = "";
    private String Data = "";
    private String uppiccode = "";
    private String LoginType = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addinfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(AddinfoActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        uppdateBtnColor();
        initDatePicker();//初始化时间选择
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        LoginType = getIntent().getStringExtra("LoginType");
        if (!LoginType.equals("手机号")) {
            nameEt.setText(MyApplication.getInstance().getThirdLoginname());
            Glide.with(mContext)
                    .load(MyApplication.getInstance().getThirdLoginpic())
                    .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                    .into(headImgPc);
            if (MyApplication.getInstance().getThirdLoginsex().equals("男")) {
                radioButtonMan.setChecked(true);
                radioButtonWomen.setChecked(false);
            } else {
                radioButtonMan.setChecked(false);
                radioButtonWomen.setChecked(true);
            }
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
//        sHA1(this);
    }


    @Override
    protected void customTitleCenter(TextView titleCenter) {
        titleCenter.setText("登陆");
    }


    @Override
    protected void initEventListeners() {
        initData();
        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                File file = new File(fileUri.getEncodedPath());
                headfile = file;
                headImgPc.setImageBitmap(bitmap);
                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                photoPath = imagePath;
                Imgadd = imagePath;
                Log.e("onPictureSelected", "filePath==" + filePath);
                Log.e("onPictureSelected", "photoPath=" + photoPath);

                if (photoPath != null && !photoPath.equals("")) {
                    //上传图片
//                        uploadImage(NetConstant.HOST+"/api/userGiftCard/uploadImg?type=10&ispic=1&userId=" + MyApplication.getInstance().getUserid(), photoPath);
                }
            }
        });
        // 单选按钮组监听事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据ID判断选择的按钮
                if (checkedId == R.id.radioButton_man) {
                    //选中男
                    Log.e("radioGroup", "man " + checkedId);
                    gender = "男";
                } else {
                    //选中女
                    gender = "女";
                    Log.e("radioGroup", "women " + checkedId);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void uppdateBtnColor() {
//        String username = phoneEt.getText().toString().trim();
//        String pwd = codeEt.getText().toString().trim();
//        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(pwd)) {
//            loginTv.setBackground(mContext.getDrawable(R.drawable.order_pay_btn));
//            loginTv.setTextColor(getResources().getColor(R.color.white));
//            loginTv.setEnabled(true);
//        } else {
//            loginTv.setBackground(mContext.getDrawable(R.drawable.login_no_btn));
//            loginTv.setTextColor(getResources().getColor(R.color.login_no_color));
//            loginTv.setEnabled(false);
//        }
    }


    @OnClick({R.id.head_img_pc, R.id.ok_bt, R.id.age_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_img_pc://头像选择
                if (PermissionUtils.isCameraPermission(AddinfoActivity.this, 0x007)) {
                    selectPicture(1, 1, 512, 512);
                }
                break;
            case R.id.age_ll://选择年龄
                mDatePicker.show("2000-07-07");//ageTv.getText().toString()
                break;
            case R.id.ok_bt://确定
                if (nameEt.getText().toString().equals("")) {
                    Toast.makeText(this, "请填写您的昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ageTv.getText().toString().equals("")) {
                    Toast.makeText(this, "请填写您的年龄", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (LoginType.equals("手机号")) {
                    if(Imgadd.length()<2){
                        Toast.makeText(this, "请设置您的个人头像", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    InitUserInfo();//初始化用户信息   手机号用户
                } else {
                    if(MyApplication.getInstance().getThirdLoginpic().length()<2||MyApplication.getInstance().getThirdLoginpic()==null){
                        Toast.makeText(this, "请设置您的个人头像", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    InitUserInfoThird();//第三方登录初始化用户信息
                }
                break;

//
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void InitUserInfo() {
//        userId	是	int	用户ID
//        username	是	string	用户名
//        age	是	int	年龄
//        gender	是	string	性别
//        imgFile	是	file	头像文件
//        token	是	string	token
        try {//上传头像
            String URL = "&userId=" + MyApplication.getInstance().getUserId() + "&username=" + nameEt.getText().toString().trim() + "&birthDay=" + userbirth + "&token=" + MyApplication.getInstance().getToken() + "&gender=" + gender;
            Log.e("InitUserInfo", "URL=" + URL);
            uploadImage(NetConstant.API_InitUserInfo + URL, Imgadd);
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
     * @param imagePath 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    public String uploadImage(String url, String imagePath) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("imagePath", imagePath + " \n" + url);
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgFile", imagePath, image)//上传文件在参数名 文件路径 本地路径
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
//                Data=response.body().string();
                MainModel mMainModel = new Gson().fromJson(response.body().string(),
                        new TypeToken<MainModel>() {
                        }.getType());
                uppiccode = mMainModel.getCode();
                if (uppiccode.equals("1")) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
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

//        Log.d("uploadImage", jsonObject.optString("logoUrl"));
//        Updataimg = jsonObject2.get("logoUrl") + "";//新头像
//        MyApplication.getInstance().setPicbitmap("");//设置完成后清除
//        MyApplication.getInstance().setPhotoPath("");//设置完成后清除
//        if (!Updataimg.equals("")) {
//            UpdateUser(Updataimg);
//        }
        return Data;

    }


    //请求权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0x007:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        selectPicture(1, 1, 512, 512);
                    } else {
                        Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "数据异常,请重新登录再试", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("1980-05-01", false);
        final long endTimestamp = System.currentTimeMillis();

//        ageTv.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
//                ageTv.setText(DateFormatUtils.long2Str(timestamp, false));
                userbirth = DateFormatUtils.long2Str(timestamp, false);
                String birth = DateFormatUtils.long2Str(timestamp, false).substring(0, 4);
                String now = DateFormatUtils.long2Str(endTimestamp, false).substring(0, 4);
                int i = Integer.parseInt(now) - Integer.parseInt(birth);
                Log.e("initDatePicker", "birth=" + birth + "  now=" + now + "    age=" + i);
                ageTv.setText(i + "岁");
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        ageTv.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                ageTv.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    public static int getAge(Date birthDay) throws Exception {
        //获取当前系统时间
        Calendar cal = Calendar.getInstance();
        //如果出生日期大于当前时间，则抛出异常
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        //取出系统当前时间的年、月、日部分
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        //将日期设置为出生日期
        cal.setTime(birthDay);
        //取出出生日期的年、月、日部分
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) {
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        System.out.println("age:" + age);
        return age;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    private void InitUserInfoThird() {//第三方登录
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("username", MyApplication.getInstance().getThirdLoginname());
        map.put("gender", MyApplication.getInstance().getThirdLoginsex());
        map.put("birthDay", userbirth);
        map.put("portraitPath", MyApplication.getInstance().getThirdLoginpic());
        map.put("address", MyApplication.getInstance().getThirdLoginadd());
//        if (LoginType.equals("QQ")) {
//            map.put("qqOpenId", MyApplication.getInstance().getThirdLoginopenid());
//        } else if (LoginType.equals("微信")) {
//            map.put("wxOpenId", MyApplication.getInstance().getThirdLoginopenid());
//        }
        map.put("registerWay", LoginType);
        postRequest(RetrofitService.InitUserInfo, map);
    }


    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.InitUserInfo)) {
            Log.e("InitUserInfo", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
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
}
