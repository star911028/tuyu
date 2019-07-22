package com.fengyuxing.tuyu.activity;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.api.NimUIKit;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.GiftRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.GiftList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.SPUtils;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.view.NeedPWDWindow;
import com.fengyuxing.tuyu.view.UserSetingWindow;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class UserInfoctivity extends BaseActivity implements View.OnClickListener {
    //个人信息
    private static final String TAG = "UserInfoctivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.img_iv)
    CircleImageView imgIv;
    @BindView(R.id.share_tv)
    TextView share_tv;
    @BindView(R.id.voice_room_ll)
    LinearLayout voiceRoomLl;
    @BindView(R.id.room_name_tv)
    TextView roomNameTv;
    @BindView(R.id.roomnum_tv)
    TextView roomnumTv;
    @BindView(R.id.room_type_tv)
    TextView roomTypeTv;
    @BindView(R.id.room_status_tv)
    TextView roomStatusTv;
    @BindView(R.id.tuercode_tv)
    TextView tuercodeTv;
    @BindView(R.id.fansnum_tv)
    TextView fansnumTv;
    @BindView(R.id.des_tv)
    TextView desTv;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.nickname_tv)
    TextView nicknameTv;
    @BindView(R.id.add_tv)
    TextView addTv;
    @BindView(R.id.main_banner)
    BGABanner mainBanner;
    @BindView(R.id.room_ll)
    LinearLayout room_ll;
    @BindView(R.id.talk_ll)
    LinearLayout talkLl;
    @BindView(R.id.follow_ll)
    LinearLayout followLl;
    @BindView(R.id.bottom_ll)
    LinearLayout bottomLl;
    @BindView(R.id.main_cl)
    CoordinatorLayout mainCl;
    @BindView(R.id.follow_tv)
    TextView followTv;
    @BindView(R.id.sex_tv)
    TextView sexTv;
    @BindView(R.id.sex_ll)
    LinearLayout sexLl;
    @BindView(R.id.level_iv)
    ImageView levelIv;
    @BindView(R.id.nums_tv)
    TextView numsTv;
    private GiftRecyAdapter recyAdapter;
    private ArrayList<GiftList> listgift = new ArrayList<>();
    private DataList userinfo;
    private String InfoID = "";
    private UserSetingWindow mSettingWindow;
    private String imgtips[];
    private List<DataList> giftdata = new ArrayList<>();//
    private Boolean isDeFriend = false;//是否被拉黑
    private NeedPWDWindow mNeedPWDWindow;
    private Boolean Needpass = false;
    private Boolean toastnews = false;
    private String Type = "";
    private String tuercode = "";
    private List<String> imgpics;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_round);//左边返回键
        //Palette用来更漂亮地展示配色
        Palette.from(BitmapFactory.decodeResource(getResources(), R.drawable.loding_bg))
                .generate(new Palette.PaletteAsyncListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        int color = palette.getVibrantColor(getResources().getColor(R.color.white));//white  colorPrimary
                        collapsingToolbar.setContentScrimColor(color);
                        //因为我暂时没有找到比较好的透明状态栏来适配这一套效果布局。
                        //因此就直接替换掉StatusBar的颜色，这样其实也蛮好看的。
//                        getWindow().setStatusBarColor(color);
                    }
                });
        //CollapsingToolbarLayout
        if (getIntent().getStringExtra("type") != null) {
            Type = getIntent().getStringExtra("type");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Type.equals("room")) {
                    finish();//返回键
                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                    intentyh.putExtra("roomid", "");//传递房间id
                    intentyh.putExtra("roomdata", (Serializable) null);//传递房间数据
                    startActivity(intentyh);
                } else {
                    finish();
                }
            }
        });//.
        toolbar.setTitle("");
        if (getIntent().getStringExtra("userinfoid") != null) {
            InfoID = getIntent().getStringExtra("userinfoid");
//            getFindUserInfo(InfoID);
        }

        //收到的礼物列表
        rvRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        optMiz(rvRecyclerView, new GridLayoutManager(mContext, 5));//解决netscrollview 嵌套recyclerview引起的滑动卡顿问题。


        //banner 轮播图
        mainBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(mContext)
                        .load(model)
                        .into(itemView);

            }
        });
        mainBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override//banner图点击事件
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                if (imgpics.size() > 0) {
//                    Intent intent = new Intent(mContext, WebViewActivity.class);//
//                    intent.putExtra("webview_title", bannerdata.get(position).getTitle());//h5 跳转的标题
//                    intent.putExtra("webview_url", bannerdata.get(position).getH5TurnPath());//h5 跳转路径
//                    startActivity(intent);

                    Intent intent = new Intent(mContext, ShowBigPicActivity.class);
                    intent.putExtra("currentPosition", position);
                    intent.putExtra("imgdatas", (Serializable) imgpics);//传递房间数据
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("dataBean", mData);
//                    intent.putExtras(bundle);
//                    intent.putExtra("currentPosition", position);
                    startActivity(intent);
                }
            }
        });
        mainBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.e("setOnPageChangeListener","onPageScrolled="+i);
            }

            @Override
            public void onPageSelected(int i) {
                Log.e("setOnPageChangeListener","onPageSelected="+i);
                numsTv.setText(i + 1 + "/" + imgpics.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.e("setOnPageChangeListener","onPageScrollStateChanged="+i);
            }
        });
        FindMyGiftLog();
    }


    public void Followbg() {//未关注的状态
        followTv.setText("关注");
        Drawable drawable = getResources().getDrawable(R.mipmap.add_follow1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        followTv.setCompoundDrawables(drawable, null, null, null);
        followTv.setTextColor(getResources().getColor(R.color.white));
        followLl.setBackgroundResource(R.drawable.user_follow_bg1);
    }

    public void CanelFollowbg() {//已关注的状态
        followTv.setText("已关注");
        Drawable drawable = getResources().getDrawable(R.mipmap.following);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        followTv.setCompoundDrawables(drawable, null, null, null);
        followTv.setTextColor(getResources().getColor(R.color.follow_false));
        followLl.setBackgroundResource(R.drawable.user_follow_bg2);
    }

    public void FindMyGiftLog() {//查询我收到的礼物
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());//被查询的用户id
        map.put("infoUserId", InfoID);//被查询的用户id
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindMyGiftLog, map);
    }


    public void getFindUserInfo(String infoUserId) {//查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", infoUserId);
        postRequest(RetrofitService.FindUserInfo, map);
        Log.e("getFindUserInfo", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken() + "  infoUserId=" + infoUserId);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "InfoID=" + InfoID);
        if (InfoID.length() > 0) {
            getFindUserInfo(InfoID);//查询个人信息
        }
        int itemcheck = mainBanner.getCurrentItem();
        Log.e("onResume", "itemcheck=" + itemcheck);
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
        setTranslucentStatus(this);
        setStatusBarLightTheme(this, true);

    }


    @OnClick({R.id.right_icon, R.id.talk_ll, R.id.follow_ll, R.id.share_tv, R.id.room_ll, R.id.tuercode_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_icon://编辑资料
                if (InfoID.equals(MyApplication.getInstance().getUserId())) {//查看自己的个人主页
                    Intent intent = new Intent(mContext, EditInfoActivity.class);
                    intent.putExtra("userdata", (Serializable) userinfo);
                    startActivity(intent);
                } else {//查看别人
                    Seting(isDeFriend);
                }
                break;
            case R.id.share_tv://分享主页
                break;
            case R.id.talk_ll://私信
                if (isDeFriend) {
                    Toast.makeText(mContext, "已将对方拉黑,无法私聊", Toast.LENGTH_SHORT).show();
                } else {
                    NimUIKit.startP2PSession(mContext, userinfo.getAccid());//进入私聊页面
                }
                break;
            case R.id.follow_ll://关注
                if (followTv.getText().toString().equals("关注")) {
                    FollowUser();//关注
                } else if (followTv.getText().toString().equals("已关注")) {
                    CancelFollowUser();  //取消关注
                }
                break;
            case R.id.room_ll://语音房间
                toastnews = false;
                getFindRoomInfo();//进入直播间查询信息
                break;
            case R.id.tuercode_tv://点击兔语号
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                String wx = tuercode;
                if (wx.length() > 4) {
                    String code = wx.substring(4, wx.length());
                    cm.setText(wx);
                    Log.e("ClipboardManager", "wx=" + wx);
                    SPUtils.showToast(mContext, "已复制到剪切板，快去粘贴吧~");
                }
                break;

        }
    }

    public void getFindRoomInfo() {//进入直播间查询信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", userinfo.getRoomId());
        if (Needpass) {//有密码才传这个字段
            map.put("password", MyApplication.getInstance().getInputpass());
        }
        Log.e("getFindRoomInfo", "password=" + MyApplication.getInstance().getInputpass());
        postRequest(RetrofitService.FindRoomInfo, map);
    }


    private void Seting(final Boolean isDeFriend) {
        mSettingWindow = new UserSetingWindow(UserInfoctivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mSettingWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mSettingWindow.dismiss();
                                break;
                            case 5://拉黑
                                if (isDeFriend) {//已经被拉黑
                                    CancelDefriendUser();//取消拉黑
                                } else {
                                    DefriendUser();//拉黑
                                }
                                break;
                            case 6://举报
                                Intent intent = new Intent(mContext, WebViewActivity.class);//  http://ty.fengyugo.com/h5/h5/help.html
                                intent.putExtra("webview_title", "举报");
                                intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/report.html" + "?userId=" + MyApplication.getInstance().getUserId() + "&toUserId=" + InfoID);
                                startActivity(intent);
                                break;
                        }
                    }
                }, isDeFriend);
        mSettingWindow.setClippingEnabled(false);
        mSettingWindow.showAtLocation(mainCl, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    /**
     * 解决netscrollview 嵌套recyclerview引起的滑动卡顿问题。
     *
     * @param recyclerView
     * @param layoutManager
     */
    public void optMiz(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }


    private void FollowUser() {//关注用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("followId", InfoID);
        postRequest(RetrofitService.FollowUser, map);
    }

    private void CancelFollowUser() {//取消关注用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("followId", InfoID);
        postRequest(RetrofitService.CancelFollowUser, map);
    }


    private void DefriendUser() {//拉黑用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("blackerId", InfoID);
        postRequest(RetrofitService.DefriendUser, map);
    }

    private void CancelDefriendUser() {//取消拉黑用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("blackerId", InfoID);
        postRequest(RetrofitService.CancelDefriendUser, map);
    }


    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.contains(RetrofitService.FollowUser)) {
            Log.e("FollowUser", "result=" + result);
            try {
                JSONObject jo = new JSONObject(result);
                if (jo.has("code")) {
                    if (jo.getString("code").equals("1")) {
                        CanelFollowbg();//显示已关注
                        Log.e("FindUserInfo2", "显示关注");
                    } else {
                        if (jo.has("errorMsg")) {
                            String error = jo.getString("errorMsg");
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (url.contains(RetrofitService.CancelFollowUser)) {//取消关注
            try {
                JSONObject jo = new JSONObject(result);
                if (jo.has("code")) {
                    if (jo.getString("code").equals("1")) {
                        Followbg();//显示关注
                        Log.e("FindUserInfo2", "显示已关注");
                    } else {
                        if (jo.has("errorMsg")) {
                            String error = jo.getString("errorMsg");
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (url.contains(RetrofitService.FindMyGiftLog)) {//查询我收到的礼物
            Log.e("FindMyGiftLog", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            giftdata.clear();
            if (mMainArrayModel.getCode().equals("1")) {
                if (mMainArrayModel.getData() != null && mMainArrayModel.getData().size() > 0) {
                    for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                        giftdata.add(mMainArrayModel.getData().get(i));
                    }
                    recyAdapter = new GiftRecyAdapter(mContext, giftdata);
                    rvRecyclerView.setAdapter(recyAdapter);
                }
            } else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.contains(RetrofitService.DefriendUser)) {//拉黑用户
            Log.e("DefriendUser", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                Toast.makeText(mContext, "拉黑成功", Toast.LENGTH_SHORT).show();
                isDeFriend = true;
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.contains(RetrofitService.CancelDefriendUser)) {//取消拉黑用户
            Log.e("CancelDefriendUser", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                isDeFriend = false;
                Toast.makeText(mContext, "取消拉黑成功", Toast.LENGTH_SHORT).show();
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindRoomInfo)) {//进入直播间查询信息
            Log.e("FindRoomInfo", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
//                if(Needpass){
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", userinfo.getRoomId());//传递房间id
//                    intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
//                    intentyh.putExtra("roomdata", (Serializable) userinfo);//传递房间数据
//                    startActivity(intentyh);
//                }else {
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", userinfo.getRoomId());//传递房间id
//                    intentyh.putExtra("roomdata", (Serializable) userinfo);//传递房间数据
//                    startActivity(intentyh);
//                }
                if (Needpass) {
                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                        if (!MyApplication.getInstance().getMyRoomid().equals(mMainModel.getData().getRoomId())) {//不是同一个房间 先退出MainRoomActivity 在进入房间
                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                        }
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    } else {//当前没有在房间
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    }
                } else {
                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                        if (!MyApplication.getInstance().getMyRoomid().equals(mMainModel.getData().getRoomId())) {//不是同一个房间 先退出MainRoomActivity
                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                        }
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    } else {//当前没有在房间
                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                        startActivity(intentyh);
                    }
                }


            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else if (mMainModel.getCode().equals("2")) {//密码错误
                if (mMainModel.getErrorMsg().contains("拉黑")) {
                    Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Needpass = false;//重新打开密码输入框
                    if (toastnews) {
                        Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        NeedPWDwindow();
                    }
                }
            } else if (mMainModel.getCode().equals("3")) {//房间需要密码
                Needpass = true;//需要密码
                if (mMainModel.getErrorMsg() != null) {
                    NeedPWDwindow();
                }
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindUserInfo)) {
            Log.e("FindUserInfo", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    userinfo = mMainModel.getData();
                    tuercodeTv.setText("兔语号: " + userinfo.getTuId());
                    tuercode = userinfo.getTuId();
                    fansnumTv.setText("粉丝: " + userinfo.getFansCount());
                    addTv.setText(userinfo.getAddress());
                    nicknameTv.setText(userinfo.getUsername());
                    if (userinfo.getDescription() == null || userinfo.getDescription().length() < 1) {
                        desTv.setText("这家伙很懒,什么介绍都没留下");
                    } else {
                        desTv.setText(userinfo.getDescription());
                    }
                    if (InfoID != null) {
                        if (InfoID.equals(MyApplication.getInstance().getUserId())) {//查看自己的个人主页
                            rightIcon.setImageResource(R.mipmap.edit_data);
//                    share_tv.setVisibility(View.VISIBLE);
                            bottomLl.setVisibility(View.GONE);
                        } else {//查看他人的个人主页
                            rightIcon.setImageResource(R.mipmap.dot_white);
                            share_tv.setVisibility(View.GONE);
                            bottomLl.setVisibility(View.VISIBLE);
                        }
                    }
                    if (userinfo.getFollow() != null) {
                        Log.e("getFollow", "getFollow=" + userinfo.getFollow());
                        if (userinfo.getFollow()) {
                            CanelFollowbg();//取消关注的状态
                        } else {
                            Followbg();//关注的状态
                        }
                    }
                    //设置用户等级
                    int levelnum = Integer.valueOf(userinfo.getExpRank()).intValue();
                    Glide.with(mContext)
                            .load(RetrofitService.bgAraay[levelnum])
                            .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                            .into(levelIv);
                    if (userinfo.getGender() != null) {
                        sexTv.setText(userinfo.getAge());
                        if (userinfo.getGender().equals("男")) {
                            Drawable drawable = getResources().getDrawable(R.mipmap.man);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            sexTv.setCompoundDrawables(drawable, null, null, null);
                            sexLl.setBackgroundResource(R.drawable.room_sex);
                        } else {
                            Drawable drawable = getResources().getDrawable(R.mipmap.woman);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            sexTv.setCompoundDrawables(drawable, null, null, null);
                            sexLl.setBackgroundResource(R.drawable.room_sex2);
                        }
                    }

                    if (userinfo.getRoomId() != null && !userinfo.getRoomId().equals("")) {
                        voiceRoomLl.setVisibility(View.VISIBLE);
                        if (userinfo != null) {
                            roomnumTv.setText("ID：" + userinfo.getNumber());
                            roomNameTv.setText(userinfo.getRoomName());
                            roomTypeTv.setText(userinfo.getClassifyName());
                            Glide.with(mContext)
                                    .load(userinfo.getPortraitPath())
                                    .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                    .into(imgIv);
                        }
                    } else {
                        voiceRoomLl.setVisibility(View.GONE);
                    }
                    if (userinfo.getIsDeFriend() != null) {
                        if (userinfo.getIsDeFriend().equals("true")) {//已经被自己拉黑
                            isDeFriend = true;
                        } else {//没有被拉黑
                            isDeFriend = false;
                        }
                    }
                    List<String> imagdata = Arrays.asList(userinfo.getPortraitPathArray());//图片地址
                    List<String> imagdatatips = new ArrayList<>();//图片的描述 设置空 不显示
                    for (int i = 0; i < imagdata.size(); i++) {
                        imagdatatips.add("");
                    }
                    imgpics = Arrays.asList(userinfo.getPortraitPathArray());
                    mainBanner.setData(imagdata, imagdatatips);  //mainBanner.setData(Arrays.asList(imagdata.get(0), imagdata.get(1)), Arrays.asList("", ""));//前面是图片地址 后面是图片描述
                }
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

    //密码弹出框
    private void NeedPWDwindow() {//
        mNeedPWDWindow = new NeedPWDWindow(UserInfoctivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mNeedPWDWindow.dismiss();
                                break;
                            case 5://进入房间
                                mNeedPWDWindow.dismiss();
                                toastnews = true;//显示提示信息
                                getFindRoomInfo();
                                //TODO
                                break;
                        }
                    }
                });
        mNeedPWDWindow.setClippingEnabled(false);
        mNeedPWDWindow.showAtLocation(mainCl, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }
}

