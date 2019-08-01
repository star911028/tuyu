package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainDTO;

import butterknife.BindView;
import butterknife.OnClick;

public class SetsafeActivity extends BaseActivity implements View.OnClickListener {
    //设置--安全等级
    private static final String TAG = "SetsafeActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.main_type_tv)
    TextView mainTypeTv;
    @BindView(R.id.phone_type_tv)
    TextView phoneTypeTv;
    @BindView(R.id.phonebind_ll)
    LinearLayout phonebindLl;
    @BindView(R.id.smtype_tv)
    TextView smtypeTv;
    @BindView(R.id.smrz_ll)
    LinearLayout smrzLl;
    @BindView(R.id.yztype_tv)
    TextView yztypeTv;
    @BindView(R.id.smyz_ll)
    LinearLayout smyzLl;
    @BindView(R.id.jztype_tv)
    TextView jztypeTv;
    @BindView(R.id.jzsf_ll)
    LinearLayout jzsfLl;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    private MainDTO userinfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_safe;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(SetsafeActivity.this, getResources().getColor(R.color.mine_safe_bg), true);//isLightColor   透明或者不透明
        userinfo = (MainDTO) getIntent().getSerializableExtra("userdata");//接收个人信息对象
        if (userinfo != null) {
            phoneTypeTv.setText(userinfo.getPhoneNumber());
//            "identityStatus": 0,
//0表示实名认证和实名验证都未完成
//1表示完成了实名认证但未完成实名验证  此时实名认证显示绿色已完成 不可点击进入
//2表示完成了实名认证和实名验证  但实名验证在审核阶段  此时实名认证显示绿色已完成 不可点击进入
            //实名验证显示黄色待审核  不可点击进入
//3表示完成了实名认证和实名验证  此时实名认证显示绿色已完成 不可点击进入
            //实名验证显示绿色已完成  不可点击进入
            if(userinfo.getIdentityStatus()!=null){
                if (userinfo.getIdentityStatus().equals("0")) {
                    mainTypeTv.setText("低");
                    smtypeTv.setText("未完成");
                    yztypeTv.setText("未完成");
                    smrzLl.setEnabled(true);
                    smyzLl.setEnabled(true);
                } else if (userinfo.getIdentityStatus().equals("1")) {
                    mainTypeTv.setText("中");
                    smtypeTv.setText("已完成");
                    yztypeTv.setText("未完成");
                    smrzLl.setEnabled(false);
                    smyzLl.setEnabled(true);
                } else if (userinfo.getIdentityStatus().equals("2")) {
                    mainTypeTv.setText("高");
                    smtypeTv.setText("已完成");
                    yztypeTv.setText("待审核");
                    smrzLl.setEnabled(false);
                    smyzLl.setEnabled(false);
                }else {
                    mainTypeTv.setText("高");
                    smtypeTv.setText("已完成");
                    yztypeTv.setText("已完成");
                    smrzLl.setEnabled(false);
                    smyzLl.setEnabled(false);
                }
                if (userinfo.getIsProtect().equals("fale")) {
                    jztypeTv.setText("未开启");
                } else {
                    jztypeTv.setText("已开启");
                }
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


    @OnClick({R.id.back_iv, R.id.phonebind_ll, R.id.smrz_ll, R.id.smyz_ll, R.id.jzsf_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.phonebind_ll://手机绑定
                break;
            case R.id.smrz_ll://实名认证
                Intent intent = new Intent(mContext, RealNameauthenActivity.class);
                startActivity(intent);
                break;
            case R.id.smyz_ll://实名验证
                intent = new Intent(mContext, MainSmyzActivity.class);
                startActivity(intent);
                break;
            case R.id.jzsf_ll://家长保护
                break;
        }
    }
}
