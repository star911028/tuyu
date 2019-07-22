package com.fengyuxing.tuyu.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.datepicker.CustomDatePicker;
import com.fengyuxing.tuyu.datepicker.DateFormatUtils;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.view.AddPhotoWindow;
import com.fengyuxing.tuyu.view.AddTipsWindow;
import com.fengyuxing.tuyu.view.GetJsonDataUtil;
import com.fengyuxing.tuyu.view.LodingWindow;
import com.fengyuxing.tuyu.view.SetPhotoWindow;
import com.fengyuxing.tuyu.view.ShengBean;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditInfoActivity extends ImageBaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {
    //编辑个人资料
    private static final String TAG = "EditInfoActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.nickname_tv)
    TextView nicknameTv;
    @BindView(R.id.nickname_ll)
    LinearLayout nicknameLl;
    @BindView(R.id.sex_tv)
    TextView sexTv;
    @BindView(R.id.sex_ll)
    LinearLayout sexLl;
    @BindView(R.id.birth_tv)
    TextView birthTv;
    @BindView(R.id.birth_ll)
    LinearLayout birthLl;
    @BindView(R.id.add_tv)
    TextView addTv;
    @BindView(R.id.add_ll)
    LinearLayout addLl;
    @BindView(R.id.induce_tv)
    TextView induceTv;
    @BindView(R.id.induce_ll)
    LinearLayout induceLl;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;
    @BindView(R.id.headpic_tv)
    TextView headpicTv;
    private CustomDatePicker mDatePicker;
    private static final int PRC_PHOTO_PICKER = 1;
    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    private AddTipsWindow mAddTipsWindow;
    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";
    private DataList userinfo;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> modellist = new ArrayList<String>();
    private String[] newpic;
    private Boolean isAddpic = false;
    private AddPhotoWindow mAddPhotoWindow;
    private SetPhotoWindow mSetPhotoWindow;
    private List<ShengBean> options1Items = new ArrayList<ShengBean>();  //  省
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>(); //  市
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();  //  区
    private OptionsPickerView pvCustomOptions;
    private String Addres;
    private int Options1 = 0;
    private int Options2 = 0;
    private boolean changepics = false;
    private boolean changenick = false;
    private boolean changesex = false;
    private boolean changebirth = false;
    private boolean changeadd = false;
    private boolean changeqm = false;
    private boolean Changpics = false;
    private LodingWindow mLodingWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editinfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        initDatePicker();//初始化时间选择
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        mPhotosSnpl.setItemSpanCount(4);
        mPhotosSnpl.setMaxItemCount(8);
        mPhotosSnpl.setEditable(true);
        mPhotosSnpl.setPlusEnable(true);
        mPhotosSnpl.setSortable(true);
        mPhotosSnpl.setItemCornerRadius(10);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        userinfo = (DataList) getIntent().getSerializableExtra("userdata");//接收个人信息对象
        if (userinfo != null) {
            nicknameTv.setText(userinfo.getUsername());
            sexTv.setText(userinfo.getGender());
            birthTv.setText(userinfo.getBirthDay());
            addTv.setText(userinfo.getAddress());
            Addres = userinfo.getAddress();
            if (userinfo.getDescription() == null || userinfo.getDescription().length() < 1) {
                induceTv.setText("未填写");
            } else {
                induceTv.setText(userinfo.getDescription());
            }
            if (userinfo.getPortraitPathArray().length > 0) {
                String[] urls = userinfo.getPortraitPathArray();
                ArrayList<String> photos = new ArrayList<>(Arrays.asList(urls));
                mPhotosSnpl.setData(photos);
            }
        }
        if (MyApplication.getInstance().getAddindex1() == null || MyApplication.getInstance().getAddindex1().length() < 1) {
            MyApplication.getInstance().setAddindex1("16");
            MyApplication.getInstance().setAddindex2("0");
        }
        Log.e("Addindex", "init setAddindex1=" + MyApplication.getInstance().getAddindex1() + "     setAddindex2=" + MyApplication.getInstance().getAddindex2());
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

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
    }

    @Override
    protected void initEventListeners() {
        initData();

    }

    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);
        setStatusBarLightTheme(this, true);
    }


    @OnClick({R.id.back_iv, R.id.save_tv, R.id.nickname_ll, R.id.sex_ll, R.id.birth_ll, R.id.add_ll, R.id.induce_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.save_tv:
                saveTv.setEnabled(false);
                LodingWindow("资料上传中,请稍等.....");//弹出加载框
//                Toast.makeText(mContext, "资料上传中,请稍等.....", Toast.LENGTH_SHORT).show();
                Log.e("上传照片", "modellist 数量=" + modellist.size() + "mPhotosSnpl 数量=" + mPhotosSnpl.getData().size());
                if (Changpics) {//改变过图片顺序
                    Log.e("上传照片", "改变顺序后" + modellist.size());
                    if (modellist.size() != mPhotosSnpl.getData().size()) {
                        updatePic(mPhotosSnpl.getData());//上传当前控件内所有图片
                    } else {
                        updatePic(modellist);//上传当前控件内所有图片
                    }
                } else {
                    if (isAddpic) {
                        Log.e("上传照片", "有新照片");
                        updatePic(mPhotosSnpl.getData());//上传当前控件内所有图片
                    } else {
                        Log.e("上传照片", "没有新照片");
                        String result = "";
                        for (String str : userinfo.getPortraitPathArray()) {
                            Log.e("initView", "str=" + str);
                            result += str + ",";
                        }
                        Log.e("initView", "result1=" + result);
                        StringBuffer sb = new StringBuffer(result);
                        if (result.length() > 0) {
                            sb.deleteCharAt(result.length() - 1);//减1是因为java中String的索引是从0开始的，如果我们所指定的index以0开始的话，这里可以不用减1
                        }
                        Log.e("initView", "result2=" + sb.toString());
                        String newallpic = sb.toString();
                        EditUserInfo2(newallpic);//修改信息保存
                        Log.e("uploadImage", "newpic不添加保存=" + newallpic);
                    }
                }

                break;
            case R.id.nickname_ll://昵称
                Intent intent = new Intent(mContext, EditTextActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("text", nicknameTv.getText().toString());
                startActivityForResult(intent, 111);
                break;
            case R.id.sex_ll://性别
                if (userinfo.getIsEditGender() != null) {
                    if (userinfo.getIsEditGender().equals("true")) {//已经修改过性别
                        Toast.makeText(mContext, "你已经修改过性别", Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(mContext, EditSexActivity.class);
                        intent.putExtra("userdata", (Serializable) userinfo);
                        intent.putExtra("sex", sexTv.getText().toString());
                        startActivityForResult(intent, 333);
                    }
                }
                break;
            case R.id.birth_ll://生日
//                mDatePicker.show("2000-07-07");//ageTv.getText().toString()
                mDatePicker.show(birthTv.getText().toString());//ageTv.getText().toString()
                break;
            case R.id.add_ll://现居地
                Log.e("Addindex", "add_ll setAddindex1=" + MyApplication.getInstance().getAddindex1() + "     setAddindex2=" + MyApplication.getInstance().getAddindex2());
                parseData();  //              解析数据
                initCustomOptionPicker();//              展示省市区选择器
                break;
            case R.id.induce_ll://个性签名
                intent = new Intent(mContext, EditTextActivity.class);
                intent.putExtra("type", "2");
                if (induceTv.getText().toString().equals("未填写")) {
                    intent.putExtra("text", "");
                } else {
                    intent.putExtra("text", induceTv.getText().toString());
                }
                startActivityForResult(intent, 222);
                break;
        }
    }

    public void updatePic(ArrayList<String> datas) {//
        Log.e("上传照片", "控件里面的照片数量" + datas.size());
        for (int i = 0; i < datas.size(); i++) {
            try {
                String URL = "&userId=" + MyApplication.getInstance().getUserId() + "&token=" + MyApplication.getInstance().getToken();
                uploadImage(NetConstant.API_UploadUserImg + URL, datas.get(i), i, datas);//上传完成后开始保存数据
                Log.e("uploadImage", "getData=" + datas.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                // 正确转化方式
//                String result = "";
//                String[] strArrayTrue = (String[]) list.toArray(new String[0]);
//                for (String str : strArrayTrue) {
//                    System.out.println(str);
//                    Log.e("initView", "str=" + str);
//                    result += str + ",";
//                }
//                Log.e("initView", "result=" + result);
//                StringBuffer sb = new StringBuffer(result);
//                if (result.length() > 0) {
//                    sb.deleteCharAt(result.length() - 1);//减1是因为java中String的索引是从0开始的，如果我们所指定的index以0开始的话，这里可以不用减1
//                }
//                Log.e("initView", "result2=" + sb.toString());
//                String newallpic = sb.toString();
//                EditUserInfo2(newallpic);
//                Log.e("uploadImage", "newallpic保存=" + newallpic + "  list=" + list.size());
//            }
//        }, 4000);
    }

    public void Saveinfo() {
        // 正确转化方式
        String result = "";
        String[] strArrayTrue = (String[]) list.toArray(new String[0]);
        for (String str : strArrayTrue) {
            System.out.println(str);
            Log.e("initView", "str=" + str);
            result += str + ",";
        }
        Log.e("initView", "result=" + result);
        StringBuffer sb = new StringBuffer(result);
        if (result.length() > 0) {
            sb.deleteCharAt(result.length() - 1);//减1是因为java中String的索引是从0开始的，如果我们所指定的index以0开始的话，这里可以不用减1
        }
        Log.e("initView", "result2=" + sb.toString());
        String newallpic = sb.toString();
        EditUserInfo2(newallpic);
        Log.e("uploadImage", "newallpic保存=" + newallpic + "  list=" + list.size());
    }


    public void Saveinfo2(ArrayList<String> datas) {
        for (int i = 0; i < datas.size(); i++) {
            Log.e("Saveinfo2", "datas3=" + datas.get(i));
            if (!datas.get(i).startsWith("http")) {
                updatePic(datas);//上传当前控件内所有图片
                return;
            }
        }
        // 正确转化方式
        String result = "";
        String[] strArrayTrue = (String[]) datas.toArray(new String[0]);
        for (String str : strArrayTrue) {
            System.out.println(str);
            Log.e("initView", "str=" + str);
            result += str + ",";
        }
        Log.e("initView", "result=" + result);
        StringBuffer sb = new StringBuffer(result);
        if (result.length() > 0) {
            sb.deleteCharAt(result.length() - 1);//减1是因为java中String的索引是从0开始的，如果我们所指定的index以0开始的话，这里可以不用减1
        }
        Log.e("initView", "result2=" + sb.toString());
        String newallpic = sb.toString();
        EditUserInfo2(newallpic);
        Log.e("uploadImage", "newallpic保存=" + newallpic + "  datas=" + datas.size());
    }

//    public void updatePic2() {//上传图片 不保存
//        for (int i = 0; i < mPhotosSnpl.getData().size(); i++) {
//            try {
//                String URL = "&userId=" + MyApplication.getInstance().getUserId() + "&token=" + MyApplication.getInstance().getToken();
//                uploadImage(NetConstant.API_UploadUserImg + URL, mPhotosSnpl.getData().get(i),i);//上传完成后开始保存数据
//                Log.e("uploadImage", "getData=" + mPhotosSnpl.getData().get(i));
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("1980-05-01", false);
        final long endTimestamp = System.currentTimeMillis();

//        ageTv.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                birthTv.setText(DateFormatUtils.long2Str(timestamp, false));
                if (!userinfo.getBirthDay().equals(birthTv.getText().toString())) {
                    changebirth = true;
                }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }


    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        //点击加号
        firstRun();//判断是否第一次点击
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);//删除照片
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
//        if (mPhotosSnpl.getData().size() >= 2) {
//            mPhotosSnpl.removeItem(position);//删除照片
//            updatePic();//重新上传照片
//        } else {
//            Toast.makeText(mContext, "只有一张照片不能删除", Toast.LENGTH_SHORT).show();
//        }

        SetPhonto(position, models);//查看照片下拉框
//        //单张图片点击
//        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
//                .previewPhotos(models) // 当前预览的图片路径集合
//                .selectedPhotos(models) // 当前已选中的图片路径集合
//                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
//                .currentPosition(position) // 当前预览图片的索引
//                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
//                .build();
//        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
        Changpics = true;
        modellist.clear();
        for (int i = 0; i < models.size(); i++) {
            modellist.add(models.get(i));
            Log.e("models", "models=" + models.size() + "  " + models.get(i));
        }

        for (int i = 0; i < modellist.size(); i++) {
            Log.e("modellist", "modellist=" + modellist.size() + "  " + modellist.get(i));
        }

//        if (isAddpic) {//有新添加照片时候需要先上传在交换位置
//            Log.e("onNineExchanged", "排序发生变化" + isAddpic + "  " + list.size() + "  fromPosition=" + fromPosition + "  toPosition=" + toPosition + "  models=" + models.get(0));
//            //  models是更改排序后的list
//            for(int i=0;i<models.size();i++){
//                Log.e("models", "models1=" + models.get(i));
//            }
////            mPhotosSnpl.getData().size();
////            swap(mPhotosSnpl.getData(), fromPosition, toPosition);
////            swap(list,fromPosition,toPosition);
////            for(int i = 0;i<list.size();i++)
////                Log.e("onNineExchanged","排序"+list.get(i));
////            swarp(list, fromPosition, toPosition);
//        } else {
//            Log.e("onNineExchanged", "排序发生变化2" + isAddpic + "  " + userinfo.getPortraitPathArray().length + "  fromPosition=" + fromPosition + "  toPosition=" + toPosition+ "  models=" + models.get(fromPosition));
//            for(int i=0;i<models.size();i++){
//                Log.e("models", "models2=" + models.get(i));
//            }
////            String[] strs = { "yw", "sun", "xiao" };
//            swarp(userinfo.getPortraitPathArray(), fromPosition, toPosition);
//        }
//        changepics = true;
    }

    public static <T> void swarp(T[] t, int i, int j) {
        //交换前的数据位置打印
        for (int k = 0; k < t.length; k++) {
            System.out.println(t[k]);
        }
        T temp = t[i];
        t[i] = t[j];
        t[j] = temp;
        System.out.println("**************************");
        //交换后的数据位置打印
        for (int k = 0; k < t.length; k++) {
            System.out.println(t[k]);
        }
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(true ? takePhotoDir : null) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "requestCode=" + requestCode + " data=" + data);
        if (data == null) {
            if (requestCode == RC_CHOOSE_PHOTO || requestCode == RC_PHOTO_PREVIEW) {
                //没有添加新照片
                isAddpic = false;
            }
        } else {
            if (requestCode == RC_CHOOSE_PHOTO || requestCode == RC_PHOTO_PREVIEW) {
                //有添加新照片
                isAddpic = true;
                changepics = true;
            }
        }

        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            mPhotosSnpl.setItemCornerRadius(10);
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            Log.e("onActivityResult", " addMoreData= " + BGAPhotoPickerActivity.getSelectedPhotos(data) + "       getData.size=" + mPhotosSnpl.getData().size() + "  " + mPhotosSnpl.getData().get(0) + "  " + mPhotosSnpl.getData().get(1));
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setItemCornerRadius(10);
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        } else if (requestCode == 111) {
            if (data != null) {
                Log.e("onActivityResult", "111" + data.getStringExtra("text"));
                nicknameTv.setText(data.getStringExtra("text"));
                if (!data.getStringExtra("text").equals(userinfo.getUsername())) {
                    changenick = true;
                }
            }
        } else if (requestCode == 222) {
            if (data != null) {
                Log.e("onActivityResult", "222" + data.getStringExtra("text"));
                induceTv.setText(data.getStringExtra("text"));
                if (!data.getStringExtra("text").equals(userinfo.getDescription())) {
                    changenick = true;
                }
            }
        } else if (requestCode == 333) {
            if (data != null) {
                Log.e("onActivityResult", "222" + data.getStringExtra("sex"));
                sexTv.setText(data.getStringExtra("sex"));
            }
        }
    }


    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstAddPic", 0);
        Boolean first_add = sharedPreferences.getBoolean("Firstadd", true);
        if (first_add) {
            sharedPreferences.edit().putBoolean("Firstadd", false).commit();
            Log.e("firstRun", "第一次");
            //弹出提示框
            TipsWindow();
        } else {
            AddPhonto();//弹出框
            Log.e("firstRun", "不是第一次");
        }
    }


    private void TipsWindow() {//提示信息
        mAddTipsWindow = new AddTipsWindow(EditInfoActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
//                        mExitWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 5://确定
                                mAddTipsWindow.dismiss();
                                break;
                        }
                    }
                });
        mAddTipsWindow.showAtLocation(mainLv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    public void EditUserInfo(String allpics) {
        //修改个人信息
//        portraitPathArray	是	string[]	非新上传的用户头像路径数组
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).add("username", nicknameTv.getText().toString().trim())
                .add("gender", sexTv.getText().toString().trim()).add("birthDay", birthTv.getText().toString().trim())
                .add("address", Addres).add("description", induceTv.getText().toString().trim())
                .add("portraitPathArray", allpics).build();// allpics
        Log.e("EditUserInfo", "userId=" + MyApplication.getInstance().getUserId() + " username=" + nicknameTv.getText().toString().trim() + "  token=" + MyApplication.getInstance().getToken() + " gender=" + sexTv.getText().toString().trim() + "  birthDay=" + birthTv.getText().toString().trim()
                + "  address=" + addTv.getText().toString().trim() + "  description=" + induceTv.getText().toString().trim() + "  portraitPathArray=" + allpics);
        final Request request = new Request.Builder().url(NetConstant.API_EditUserInfo).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveTv.setEnabled(true);
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                saveTv.setEnabled(true);
                Log.e("EditUserInfo", "responseStr=" + responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        if (mMainModel.getCode().equals("1")) {
                            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
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

    public void EditUserInfo2(String allpics) {//修改个人信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("username", nicknameTv.getText().toString().trim());
        map.put("gender", sexTv.getText().toString().trim());
        map.put("birthDay", birthTv.getText().toString().trim());
        map.put("address", Addres);
        map.put("description", induceTv.getText().toString().trim());
        map.put("portraitPathArray", allpics);
        Log.e("EditUserInfo", "userId=" + MyApplication.getInstance().getUserId() + " username=" + nicknameTv.getText().toString().trim() + "  token=" + MyApplication.getInstance().getToken() + " gender=" + sexTv.getText().toString().trim() + "  birthDay=" + birthTv.getText().toString().trim()
                + "  address=" + addTv.getText().toString().trim() + "  description=" + induceTv.getText().toString().trim() + "  portraitPathArray=" + allpics);
        postRequest(RetrofitService.EditUserInfo, map);
    }

    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.EditUserInfo)) {
            Log.e("EditUserInfo", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                MyApplication.getInstance().setAddindex1(Options1 + "");
                MyApplication.getInstance().setAddindex2(Options2 + "");
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                if (mLodingWindow != null) {
                    mLodingWindow.dismiss();
                }
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
            saveTv.setEnabled(true);
        }


    }

//    public void EditUserInfo2(String allpics) {
//        //修改个人信息
////        portraitPathArray	是	string[]	非新上传的用户头像路径数组
//        OkHttpClient client = new OkHttpClient();
//        FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).add("username", nicknameTv.getText().toString().trim())
//                .add("gender", sexTv.getText().toString().trim()).add("birthDay", birthTv.getText().toString().trim())
//                .add("address", addTv.getText().toString().trim()).add("description", induceTv.getText().toString().trim())
//                .add("portraitPathArray", allpics).build();//allpics
//        Log.e("EditUserInfo2", "userId=" + MyApplication.getInstance().getUserId() + " username=" + nicknameTv.getText().toString().trim() + "  token=" + MyApplication.getInstance().getToken() + " gender=" + sexTv.getText().toString().trim() + "  birthDay=" + birthTv.getText().toString().trim()
//                + "  address=" + addTv.getText().toString().trim() + "  description=" + induceTv.getText().toString().trim() + "  portraitPathArray=" + allpics);
//        final Request request = new Request.Builder().url(NetConstant.API_EditUserInfo).post(formBody).build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        saveTv.setEnabled(true);
//                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseStr = response.body().string();
//                saveTv.setEnabled(true);
//                Log.e("EditUserInfo", "responseStr=" + responseStr);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MainModel mMainModel = new Gson().fromJson(responseStr,
//                                new TypeToken<MainModel>() {
//                                }.getType());
//                        if (mMainModel.getCode().equals("1")) {
//                            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
//                        } else if (mMainModel.getCode().equals("0")) {
//                            Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
//                            MyApplication.getInstance().setUserId("");
//                            Intent intent = new Intent(mContext, LoginActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//    }

    /**
     * 上传图片
     *
     * @param
     * @param imagePath 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    public String uploadImage(String URL, String imagePath, final int i, final ArrayList<String> datas) throws IOException, JSONException {
        Log.e("uploadImage_imagePath", "imagePath=  " + imagePath);
        if (!imagePath.startsWith("http")) {
            OkHttpClient okHttpClient = new OkHttpClient();
//            Log.e("uploadImage", imagePath + " \n" + URL);
            File file = new File(imagePath);
            RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("imgFile", imagePath, image)//上传文件在参数名 文件路径 本地路径
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("imagePath", "onFailure: " + e);
                    saveTv.setEnabled(true);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // {"code":1,"errorMsg":"","data":{"imgPath":"http://www.tuerapp.com/img/user/900985_1558001238812.jpg"}}
                    MainModel mMainModel = new Gson().fromJson(response.body().string(),
                            new TypeToken<MainModel>() {
                            }.getType());
                    if (mMainModel.getCode().equals("1")) {
                        Log.e("uploadImage", "上传成功");
                        list.add(mMainModel.getData().getImgPath());//重新添加照片数组
                        datas.set(i, mMainModel.getData().getImgPath());
                        Log.e("uploadImage", "list添加新图片=" + list.size() + "   imagePath=" + mMainModel.getData().getImgPath());
                        if (datas.size() - 1 == i) {
                            //上传完成 保存图片
                            Log.e("上传照片", "上传第" + i + 1 + "张新照片" + "   list size=" + list.size());
                            for (int k = 0; k < datas.size(); k++) {
                                Log.e("Saveinfo2", "datas1=" + datas.get(i));
                            }
                            Saveinfo2(datas);
                        }
                    } else {
                        Log.e("uploadImage", "上传失败");
                        Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        if (mLodingWindow != null) {
                            mLodingWindow.dismiss();
                        }
                    }
                }
            });

        } else {
            Log.e("uploadImage", "list添加旧图片=" + list.size() + " imagePath=" + imagePath);//list.size() =7
            if (datas.size() - 1 == i) {
                list.add(imagePath);
                //上传完成 保存图片
                Log.e("上传照片", "上传索引第" + i + "张旧照片" + "   list size=" + list.size() + "   datas size=" + datas.size());//list.size() =6
                for (int q = 0; q < datas.size(); q++) {
                    Log.e("Saveinfo2", "datas2=" + datas.get(i));
                }
                Saveinfo2(datas);
            }
        }
        return null;

    }

    private void AddPhonto() {//添加照片
        mAddPhotoWindow = new AddPhotoWindow(EditInfoActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mAddPhotoWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mAddPhotoWindow.dismiss();
                                break;
                            case 5://选择照片
                                choicePhotoWrapper();
                                break;
                        }
                    }
                });
        mAddPhotoWindow.showAtLocation(mainLv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void SetPhonto(final int poistion, final ArrayList<String> models) {//查看照片
        mSetPhotoWindow = new SetPhotoWindow(EditInfoActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mSetPhotoWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 2://查看
                                //单张图片点击
                                Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(mContext)
                                        .previewPhotos(models) // 当前预览的图片路径集合
                                        .selectedPhotos(models) // 当前已选中的图片路径集合
                                        .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                                        .currentPosition(poistion) // 当前预览图片的索引
                                        .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                                        .build();
                                startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
                                break;
                            case 3://删除
                                if (mPhotosSnpl.getData().size() >= 2) {
                                    mPhotosSnpl.removeItem(poistion);//删除照片
                                    isAddpic = true;
//                                    updatePic();//重新上传照片
                                } else {
                                    Toast.makeText(mContext, "只有一张照片不能删除", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 4://取消
                                mSetPhotoWindow.dismiss();
                                break;
                            case 5://选择照片
                                choicePhotoWrapper();
                                break;
                        }
                    }
                });
        mSetPhotoWindow.showAtLocation(mainLv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    /**
     * 解析数据并组装成自己想要的list
     */
    private void parseData() {
        String jsonStr = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
//     数据解析
        Gson gson = new Gson();
        Type type = new TypeToken<List<ShengBean>>() {
        }.getType();
        List<ShengBean> shengList = gson.fromJson(jsonStr, type);
//     把解析后的数据组装成想要的list
        options1Items = shengList;
//     遍历省
        for (int i = 0; i < shengList.size(); i++) {
//         存放城市
            ArrayList<String> cityList = new ArrayList<>();
//         存放区
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();
//         遍历市
            for (int c = 0; c < shengList.get(i).city.size(); c++) {
//        拿到城市名称
                String cityName = shengList.get(i).city.get(c).name;
                cityList.add(cityName);

                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                if (shengList.get(i).city.get(c).area == null || shengList.get(i).city.get(c).area.size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(shengList.get(i).city.get(c).area);
                }
                province_AreaList.add(city_AreaList);
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);
            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).name +
                        options2Items.get(options1).get(options2);// options3Items.get(options1).get(options2).get(options3)
                Options1 = options1;
                Options2 = options2;
                Log.e("Addindex", "setAddindex1 options=" + MyApplication.getInstance().getAddindex1() + "     setAddindex2=" + MyApplication.getInstance().getAddindex2() + "  options1=" + options1 + " options2=" + options2);
//                Toast.makeText(EditInfoActivity.this, tx, Toast.LENGTH_SHORT).show();
                addTv.setText(tx);//湖北省武汉市
                if (options2Items.get(options1).get(options2).contains("市")) {
                    Addres = options2Items.get(options1).get(options2).replace("市", "");
                    addTv.setText(Addres);//武汉
                } else {
                    Addres = options2Items.get(options1).get(options2);
                    addTv.setText(Addres);//武汉
                }
                if (!addTv.getText().toString().equals(userinfo.getAddress())) {
                    changeadd = true;
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                })
                .setTitleText("现居地")
                .setDividerColor(Color.WHITE)
                .setSelectOptions(Integer.parseInt(MyApplication.getInstance().getAddindex1()), Integer.parseInt(MyApplication.getInstance().getAddindex2())) //设置默认选中项
                .setTextColorCenter(R.color.sec_col) //设置选中项文字颜色
//                .setTextColorCenter(Color.BLUE) //设置选中项文字颜色
                .setContentTextSize(18)
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvCustomOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvCustomOptions.show();


    }


    private void LodingWindow(String text) {
        mLodingWindow = new LodingWindow(EditInfoActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
//                            case 4://取消
//                                mLodingWindow.dismiss();
//                                break;
                        }
                    }
                }, text);
        mLodingWindow.setClippingEnabled(false);
        mLodingWindow.showAtLocation(mainLv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }
}
