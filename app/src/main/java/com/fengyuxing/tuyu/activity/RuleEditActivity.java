package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.SPUtils;

import java.io.Serializable;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class RuleEditActivity extends BaseActivity implements View.OnClickListener {
    //玩法说明
    private static final String TAG = "RuleEditActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.main_et)
    EditText mainEt;
    @BindView(R.id.textnum_tv)
    TextView textnumTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    private String Roomid="";
    private String Description="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ruleedit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Roomid=   getIntent().getStringExtra("Roomid");
        Description =   getIntent().getStringExtra("Description");
        if(Description!=null&&Description.length()>0){
            mainEt.setText(Description);
            textnumTv.setText(mainEt.getText().length() + "/200");
            mainEt.setSelection(mainEt.getText().length());//将光标移至文字末尾
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

        mainEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (mainEt.getText().length() > 200) {
                    SPUtils.showToast(mContext, "已超过上限200个字");
                } else {
                    textnumTv.setText(mainEt.getText().length() + "/200");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (mainEt.getText().toString().trim().length() > 0) {
                    sureTv.setTextColor(getResources().getColor(R.color.mine_safe_bg));
                    sureTv.setEnabled(true);
                } else {
                    sureTv.setTextColor(getResources().getColor(R.color.editnotext_col));
                    sureTv.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);
        setStatusBarLightTheme(this, true);
    }


    @OnClick({R.id.back_iv, R.id.sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                intentyh.putExtra("roomid", Roomid);//传递房间id
                intentyh.putExtra("roomdata", (Serializable)null);//传递房间数据
                startActivity(intentyh);
                break;
            case R.id.sure_tv:
                EditRoom(mainEt.getText().toString().trim());
                break;
        }
    }

    private void EditRoom(String description) {//查询房间排行榜数据
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("description", description);
        postRequest(RetrofitService.EditRoomDescription, map);
    }


    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String
            result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.EditRoomDescription)) {//查询直播间背景
            Log.e("EditRoomDescription", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            if (mMainArrayModel.getCode().equals("1")) {
                Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
                Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                intentyh.putExtra("roomid", Roomid);//传递房间id
                intentyh.putExtra("roomdata", (Serializable)null);//传递房间数据
                startActivity(intentyh);
            }else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
