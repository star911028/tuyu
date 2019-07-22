package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.constant.NetConstant;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RealNameauthenActivity extends BaseActivity implements View.OnClickListener {
    //实名认证
    private static final String TAG = "RealNameauthenActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.code_et)
    EditText codeEt;
    @BindView(R.id.sure_bt)
    Button sureBt;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_realnameanu;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(RealNameauthenActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明

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


    public void ValidateIdentity() {
        //校验身份证号  name		姓名  //idNumber

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                .add("name", nameEt.getText().toString().trim()).add("idNumber", codeEt.getText().toString().trim()).build();
        final Request request = new Request.Builder().url(NetConstant.API_ValidateIdentity).post(formBody).build();
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
                Log.e("ValidateIdentity", "responseStr=" + responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        if (mMainModel.getCode().equals("1")) {
                            Toast.makeText(mContext, "提交成功,请等待审核", Toast.LENGTH_SHORT).show();
                            finish();
//                            Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }  else if (mMainModel.getCode().equals("0")) {
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


    @OnClick({R.id.back_iv, R.id.sure_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.sure_bt://绑定
                if (codeEt.getText().toString().length() == 18 && nameEt.getText().toString().length() > 1) {
                    ValidateIdentity();
                } else {
                    Toast.makeText(mContext, "请输入正确的身份证信息", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
