package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.util.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class EditTextActivity extends BaseActivity implements View.OnClickListener {
    //编辑个人资料--编辑昵称 个性签名
    private static final String TAG = "EditTextActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.edtext_et)
    EditText edtextEt;
    @BindView(R.id.textnum_tv)
    TextView textnumTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.name_ll)
    LinearLayout nameLl;
    @BindView(R.id.edtext_et2)
    EditText edtextEt2;
    @BindView(R.id.textnum_tv2)
    TextView textnumTv2;
    @BindView(R.id.induce_ll)
    LinearLayout induceLl;
    private String Type = "1";
    private String Text = "";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edittext;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (getIntent().getStringExtra("type") != null) {
            Type = getIntent().getStringExtra("type");
            Text = getIntent().getStringExtra("text");

            if (Type.equals("1")) {
                titleTv.setText("我的昵称");
                edtextEt.setText(Text);
                nameLl.setVisibility(View.VISIBLE);
                induceLl.setVisibility(View.GONE);
            } else {
                titleTv.setText("个性签名");
                edtextEt2.setText(Text);
                nameLl.setVisibility(View.GONE);
                induceLl.setVisibility(View.VISIBLE);
            }
        }
        if (edtextEt.getText().length() > 0) {
            textnumTv.setText(edtextEt.getText().length() + "/15");
        }
        if (edtextEt2.getText().length() > 0) {
            textnumTv2.setText(edtextEt2.getText().length() + "/40");
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
        edtextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (edtextEt.getText().length() >= 15) {
                    textnumTv.setText(edtextEt.getText().length() + "/15");
                    SPUtils.showToast(mContext, "已超过上限15个字");
                } else {
                    textnumTv.setText(edtextEt.getText().length() + "/15");
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (edtextEt.getText().toString().trim().length() > 0) {
                    saveTv.setTextColor(getResources().getColor(R.color.mine_safe_bg));
                } else {
                    saveTv.setTextColor(getResources().getColor(R.color.editnotext_col));
                }
            }
        });
        edtextEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (edtextEt2.getText().length() >= 40) {
                    SPUtils.showToast(mContext, "已超过上限40个字");
                    textnumTv2.setText(edtextEt2.getText().length() + "/40");
                } else {
                    textnumTv2.setText(edtextEt2.getText().length() + "/40");
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (edtextEt2.getText().toString().trim().length() > 0) {
                    saveTv.setTextColor(getResources().getColor(R.color.mine_safe_bg));
                } else {
                    saveTv.setTextColor(getResources().getColor(R.color.editnotext_col));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);
        setStatusBarLightTheme(this,true);
    }


    @OnClick({R.id.back_iv, R.id.save_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.save_tv:
                if(Type.equals("1")){
                    Intent intent=new Intent(mContext,EditInfoActivity.class);
                    intent.putExtra("text",edtextEt.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Intent intent=new Intent(mContext,EditInfoActivity.class);
                    intent.putExtra("text",edtextEt2.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

        }
    }
}
