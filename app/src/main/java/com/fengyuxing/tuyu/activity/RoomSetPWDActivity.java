package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.util.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomSetPWDActivity extends BaseActivity implements View.OnClickListener {
    //房间基本信息 设置房间密码
    private static final String TAG = "RoomSetPWDActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.room_name_et)
    EditText roomNameEt;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.pass_ll)
    LinearLayout pass_ll;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.main_rb)
    CheckBox mainRb;
    @BindView(R.id.textnum_tv)
    TextView textnumTv;
    @BindView(R.id.clear_tv)
    TextView clearTv;
    private String PWD = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_password;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(RoomSetPWDActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
//        saveTv.setEnabled(false);
        getIntent().getStringExtra("pwd");
        if (getIntent().getStringExtra("pwd") != null && getIntent().getStringExtra("pwd").length() == 4) {
            PWD = getIntent().getStringExtra("pwd");
            roomNameEt.setText(PWD);
            textnumTv.setText(roomNameEt.getText().length() + "/4");
            clearTv.setVisibility(View.VISIBLE);
        } else {
            clearTv.setVisibility(View.GONE);
        }
        if (mainRb.isChecked()) {
            pass_ll.setVisibility(View.VISIBLE);
        } else {
            pass_ll.setVisibility(View.GONE);
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
        roomNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (roomNameEt.getText().length() > 4) {
                    SPUtils.showToast(mContext, "已超过上限4个数字");
                } else {
                    textnumTv.setText(roomNameEt.getText().length() + "/4");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                if (roomNameEt.getText().toString().trim().length() == 4) {
//                    saveTv.setTextColor(getResources().getColor(R.color.mine_safe_bg));
//                    saveTv.setEnabled(true);
//                } else {
//                    saveTv.setTextColor(getResources().getColor(R.color.editnotext_col));
//                    saveTv.setEnabled(false);
//                }
            }
        });
        mainRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainRb.isChecked()) {
                    pass_ll.setVisibility(View.VISIBLE);
                } else {
                    pass_ll.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @OnClick({R.id.back_iv, R.id.save_tv, R.id.clear_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.save_tv://保存
                if (mainRb.isChecked()) {
                    Intent intent = new Intent(mContext, RoomBaseinfoActivity.class);
                    intent.putExtra("pwd", roomNameEt.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                } else {
                    Intent intent = new Intent(mContext, RoomBaseinfoActivity.class);
                    intent.putExtra("pwd", "");
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case R.id.clear_tv:
                roomNameEt.setText("");
                break;
        }
    }

}
