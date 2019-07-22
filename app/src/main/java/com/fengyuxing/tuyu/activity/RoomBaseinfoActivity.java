package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.fengyuxing.tuyu.adapter.CommunityOptionAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.MainDTO;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.view.CustomGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomBaseinfoActivity extends BaseActivity implements View.OnClickListener {
    //房间基本信息
    private static final String TAG = "RoomBaseinfoActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.room_name_et)
    EditText roomNameEt;
    @BindView(R.id.custom_gv1)
    CustomGridView customGv1;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.pass_tv)
    TextView pass_tv;
    @BindView(R.id.content_et)
    EditText contentEt;
    @BindView(R.id.pwd_ll)
    LinearLayout pwdLl;
    private CommunityOptionAdapter mChangeTimeAdapter;
    private List<DataList> changeTimeList = new ArrayList<>();
    private String Checkid = "";
    private String Roomid = "";
    private MainDTO roominfo;
    private String Type = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_baseinfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(RoomBaseinfoActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明

        if (getIntent().getStringExtra("type") != null) {
            Type = getIntent().getStringExtra("type");
        }
        getFindClassify();//查询房间类型列表
    }

    public void UpdateINFO() {//查询房间类型列表后在更新数据
        roominfo = (MainDTO) getIntent().getSerializableExtra("roominfo");//接收房间信息对象
        if (roominfo != null) {
            roomNameEt.setText(roominfo.getRoomName());
            contentEt.setText(roominfo.getWelcomeWord());
            for (int i = 0; i < changeTimeList.size(); i++) {
                if (changeTimeList.get(i).getClassifyId().equals(roominfo.getClassifyId())) {
                    changeTimeList.get(i).setChecked(true);
                    Checkid = changeTimeList.get(i).getClassifyId();
                } else {
                    changeTimeList.get(i).setChecked(false);
                }
            }
            mChangeTimeAdapter.notifyDataSetChanged();
            if (roominfo.getPassword() != null) {
                pass_tv.setText(roominfo.getPassword());
            } else {
                pass_tv.setText("无");
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


    /****
     * 选择变化时间
     */
    private AdapterView.OnItemClickListener onChangeTimeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            for (int j = 0; j < changeTimeList.size(); j++) {
                if (j == i) {
                    changeTimeList.get(j).setChecked(true);
//                    changeTime = changeTimeList.get(j).getName();
                    Checkid = changeTimeList.get(j).getClassifyId();
                } else {
                    changeTimeList.get(j).setChecked(false);
                }
            }
            mChangeTimeAdapter.notifyDataSetChanged();
        }
    };

    @OnClick({R.id.back_iv, R.id.save_tv, R.id.pwd_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                if(Type.equals("room")){
                    finish();
                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                    intentyh.putExtra("roomid", "");//传递房间id
                    intentyh.putExtra("roomdata", (Serializable)null);//传递房间数据
                    startActivity(intentyh);
                }else {
                    finish();
                }

                break;
            case R.id.pwd_ll://房间密码
                Intent intent = new Intent(mContext, RoomSetPWDActivity.class);
                intent.putExtra("pwd", roominfo.getPassword());
                startActivityForResult(intent, 111);
                break;
            case R.id.save_tv://保存
                EditRoomInfo();
                break;
        }
    }


    private void getFindClassify() {//查询分类
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindClassify, map);
    }

    public void EditRoomInfo() {//修改直播间信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", roominfo.getRoomId());
        map.put("roomName", roomNameEt.getText().toString().trim());//房间名
        map.put("classifyId", Checkid);//分类ID
        map.put("welcomeWord", contentEt.getText().toString().trim());//	房间欢迎语
        if (pass_tv.getText().toString().equals("无")) {
            map.put("password", "");//房间密码
        } else {
            map.put("password", pass_tv.getText().toString());//房间密码
        }
        postRequest(RetrofitService.EditRoomInfo, map);
        Log.e("EditRoomInfo", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() +
                " roomId=" + roominfo.getRoomId() + "   roomName=" + roomNameEt.getText().toString().trim() + "  classifyId=" + Checkid + "  welcomeWord=" + contentEt.getText().toString().trim()
                + "  password=" + pass_tv.getText().toString());
    }

    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        Log.e("EditRoomInfo", "result=" + result);
        if (url.contains(RetrofitService.Head + RetrofitService.EditRoomInfo)) {
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
                Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                intentyh.putExtra("roomid", Roomid);//传递房间id
                intentyh.putExtra("roomdata", (Serializable)null);//传递房间数据
                startActivity(intentyh);
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        } else if (url.contains(RetrofitService.Head + RetrofitService.FindClassify)) {
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            if (mMainArrayModel.getCode().equals("1")) {
                if (mMainArrayModel.getData() != null) {
                    if (mMainArrayModel.getData().size() > 0) {
                        for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                            changeTimeList.add(mMainArrayModel.getData().get(i));
                        }
                        mChangeTimeAdapter = new CommunityOptionAdapter(mContext, changeTimeList);
                        customGv1.setSelector(new ColorDrawable(Color.TRANSPARENT));
                        customGv1.setAdapter(mChangeTimeAdapter);
                        customGv1.setOnItemClickListener(onChangeTimeItemClickListener);

                        UpdateINFO();
                    }
                }
            } else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data != null) {
                Log.e("onActivityResult", "111" + data.getStringExtra("pwd"));
                if (data.getStringExtra("pwd").length() > 0) {
                    pass_tv.setText(data.getStringExtra("pwd"));
                } else {
                    pass_tv.setText("无");
                }
            }
        }
    }
}
