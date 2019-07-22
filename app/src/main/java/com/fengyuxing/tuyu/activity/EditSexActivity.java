package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.view.EditSexWindow;

import butterknife.BindView;
import butterknife.OnClick;

public class EditSexActivity extends BaseActivity implements View.OnClickListener {
    //性别修改
    private static final String TAG = "EditSexActivity";
    @BindView(R.id.man_iv)
    ImageView manIv;
    @BindView(R.id.women_iv)
    ImageView womenIv;
    @BindView(R.id.canel_bt)
    TextView canelBt;
    @BindView(R.id.sure_bt)
    TextView sureBt;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.man_rb)
    RadioButton manRb;
    @BindView(R.id.women_rb)
    RadioButton womenRb;
    @BindView(R.id.sex_rg)
    RadioGroup sexRg;
    private String sex = "";
    private String normolsex = "";
    private EditSexWindow mEditSexWindow;
    private DataList userinfo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sexedit;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(EditSexActivity.this, getResources().getColor(R.color.mycollet_col), true);//isLightColor   透明或者不透明
        sex = getIntent().getStringExtra("sex");
        sureBt.setEnabled(false);//默认不可点击
        Log.e("","");
        userinfo = (DataList) getIntent().getSerializableExtra("userdata");//接收个人信息对象
        normolsex = sex;
        if (sex != null) {
            if (sex.equals("男")) {
                manRb.setChecked(true);
                womenRb.setChecked(false);
            } else if (sex.equals("女")) {
                manRb.setChecked(false);
                womenRb.setChecked(true);
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


    @OnClick({R.id.man_iv, R.id.women_iv, R.id.canel_bt, R.id.sure_bt, R.id.man_rb, R.id.women_rb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.man_iv:
                break;
            case R.id.women_iv:
                break;
            case R.id.canel_bt:
                finish();
                break;
            case R.id.sure_bt:
                Log.e("sure_bt", "点击了确认");
                mEditSexWindow = new EditSexWindow(EditSexActivity.this,
                        new View.OnClickListener() {
                            // 全部
                            @Override
                            public void onClick(View view) {
                                Integer point = (Integer) view.getTag();
                                switch (point) {
                                    case 4://取消
                                        mEditSexWindow.dismiss();
                                        break;
                                    case 5://确认修改
//                                        EditUserInfo();
                                        mEditSexWindow.dismiss();
                                        Intent intent=new Intent(mContext,EditInfoActivity.class);
                                        intent.putExtra("sex",normolsex);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        break;
                                }
                            }
                        });
                mEditSexWindow.setClippingEnabled(false);
                mEditSexWindow.showAtLocation(mainLv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
                break;
            case R.id.man_rb:
                if (manRb.isChecked()) {
                    normolsex = "男";
                    if (sex.equals(normolsex)) {
                        sureBt.setEnabled(false);
                        sureBt.setBackgroundResource(R.drawable.sex_sure2);
                    } else {
                        sureBt.setEnabled(true);
                        sureBt.setBackgroundResource(R.drawable.sex_sure);
                    }
                }
                break;
            case R.id.women_rb:
                if (womenRb.isChecked()) {
                    normolsex = "女";
                    if (sex.equals(normolsex)) {
                        sureBt.setEnabled(false);
                        sureBt.setBackgroundResource(R.drawable.sex_sure2);
                    } else {
                        sureBt.setEnabled(true);
                        sureBt.setBackgroundResource(R.drawable.sex_sure);
                    }
                }
                break;
        }
    }



//    public void EditUserInfo() {
//        //修改个人信息
//        OkHttpClient client = new OkHttpClient();
//        FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).add("username", userinfo.getUsername())
//                .add("gender", normolsex).add("birthDay", userinfo.getBirthDay())
//                .add("address", userinfo.getAddress()).add("description", userinfo.getDescription())
//                .add("portraitPathArray", userinfo.getPortraitPathArray()+"").build();
//        Log.e("EditUserInfo", "userId=" + MyApplication.getInstance().getUserId() + " username=" + userinfo.getUsername()+ "  token=" + MyApplication.getInstance().getToken() + " gender=" + normolsex + "  birthDay=" + userinfo.getBirthDay()
//                + "  address=" + userinfo.getAddress() + "  description=" + userinfo.getDescription() + "  portraitPathArray=" + userinfo.getPortraitPathArray()+"");
//        final Request request = new Request.Builder().url(NetConstant.API_EditUserInfo).post(formBody).build();
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
//                final String responseStr = response.body().string();
//                Log.e("EditUserInfo", "responseStr=" + responseStr);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MainModel mMainModel = new Gson().fromJson(responseStr,
//                                new TypeToken<MainModel>() {
//                                }.getType());
//                        if (mMainModel.getCode().equals("1")) {
//                            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(mContext,EditInfoActivity.class);
//                            intent.putExtra("sex",normolsex);
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }  else if (mMainModel.getCode().equals("0")) {
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



}
