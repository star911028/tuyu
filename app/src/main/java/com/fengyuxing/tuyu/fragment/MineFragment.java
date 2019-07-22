package com.fengyuxing.tuyu.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.activity.MainRoomActivity;
import com.fengyuxing.tuyu.activity.MyContactsActivity;
import com.fengyuxing.tuyu.activity.MyEarningsActivity;
import com.fengyuxing.tuyu.activity.WebViewActivity;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.activity.MyCollectionActivity;
import com.fengyuxing.tuyu.activity.SettingActivity;
import com.fengyuxing.tuyu.activity.UserInfoctivity;
import com.fengyuxing.tuyu.activity.WebViewActivity2;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.util.CheckUtil;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.SPUtils;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.UILImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


//我的
public class MineFragment extends BaseFragment {
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.tuercode_ll)
    TextView tuercodeLl;
    @BindView(R.id.level_iv)
    ImageView level_iv;
    @BindView(R.id.fans_tv)
    TextView fans_tv;
    @BindView(R.id.follow_tv)
    TextView follow_tv;
    @BindView(R.id.friend_tv)
    TextView friend_tv;
    @BindView(R.id.my_room_tv)
    TextView my_room_tv;
    @BindView(R.id.img_iv)
    CircleImageView imgIv;
    @BindView(R.id.follow_ll)
    LinearLayout followLl;
    @BindView(R.id.fans_ll)
    LinearLayout fansLl;
    @BindView(R.id.friend_ll)
    LinearLayout friendLl;
    @BindView(R.id.zs_ll)
    LinearLayout zsLl;
    @BindView(R.id.get_ll)
    LinearLayout getLl;
    @BindView(R.id.room_ll)
    LinearLayout roomLl;
    @BindView(R.id.my_sc_ll)
    LinearLayout myScLl;
    @BindView(R.id.level_ll)
    LinearLayout levelLl;
    @BindView(R.id.help_ll)
    LinearLayout helpLl;
    @BindView(R.id.setting_ll)
    LinearLayout settingLl;
    @BindView(R.id.userinfo_ll)
    LinearLayout userinfo_ll;
    @BindView(R.id.levelall_ll)
    LinearLayout levelall_ll;
    @BindView(R.id.user_ll)
    LinearLayout user_ll;
    @BindView(R.id.right_iv)
    ImageView right_iv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.level_pro_tv)
    TextView level_pro_tv;
    @BindView(R.id.level_tv)
    TextView level_tv;

    Unbinder unbinder;
    //    @BindView(R.id.my_wallet_tv)
//    TextView myWalletTv;
//    @BindView(R.id.my_get_tv)
//    TextView myGetTv;
    @BindView(R.id.my_sc_tv)
    TextView myScTv;
    @BindView(R.id.my_level_tv)
    TextView myLevelTv;
    private UILImageLoader mUILImageLoader;
    private Context mContext;
    private View view;
    private HttpManager mHttpManager;
    private DataList userdata = new DataList();
    private DataList creatdata = new DataList();
    private String tuercode="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.mine_safe_bg), true);//isLightColor   透明或者不透明
        mContext = getContext();
        mUILImageLoader = new UILImageLoader(mContext);
        mHttpManager = HttpManager.getInstance();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, null);
            initView();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        unbinder = ButterKnife.bind(this, view);
        MyApplication.getInstance().setRoomId("");
        getFindUserInfo();//查询用户信息
//        MyStatusBarUtil.setStatusTransparent(getActivity(), false);
    }

    private void initData() {
        getFindUserInfo();//查询用户信息
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    private void initListener() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.userinfo_ll,R.id.user_ll, R.id.levelall_ll, R.id.follow_ll, R.id.right_iv, R.id.name_tv, R.id.img_iv, R.id.fans_ll, R.id.tuercode_ll, R.id.friend_ll, R.id.zs_ll, R.id.get_ll, R.id.room_ll, R.id.my_sc_ll, R.id.level_ll, R.id.help_ll, R.id.setting_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.follow_ll://关注
                Intent intent = new Intent(getActivity(), MyContactsActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
            case R.id.user_ll://
                intent = new Intent(getActivity(), UserInfoctivity.class);//
                intent.putExtra("userinfoid", MyApplication.getInstance().getUserId());
                intent.putExtra("userdata", (Serializable) userdata);
                startActivity(intent);
                break;
            case R.id.name_tv://
                intent = new Intent(getActivity(), UserInfoctivity.class);//
                intent.putExtra("userinfoid", MyApplication.getInstance().getUserId());
                intent.putExtra("userdata", (Serializable) userdata);
                startActivity(intent);
                break;
            case R.id.img_iv://个人信息
                intent = new Intent(getActivity(), UserInfoctivity.class);//
                intent.putExtra("userinfoid", MyApplication.getInstance().getUserId());
                intent.putExtra("userdata", (Serializable) userdata);
                startActivity(intent);
                break;
            case R.id.right_iv://个人信息
                intent = new Intent(getActivity(), UserInfoctivity.class);//
                intent.putExtra("userinfoid", MyApplication.getInstance().getUserId());
                intent.putExtra("userdata", (Serializable) userdata);
                startActivity(intent);
                break;
            case R.id.fans_ll://粉丝
                intent = new Intent(getActivity(), MyContactsActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.friend_ll://朋友
                intent = new Intent(getActivity(), MyContactsActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.zs_ll://钱包
//                intent = new Intent(getActivity(), MyWalletActivity.class);//
//                startActivity(intent);
                    Boolean  ss=CheckUtil.isFastClick();
                   if(ss){//防止重复点击
                       intent = new Intent(getActivity(), WebViewActivity.class);//
                       intent.putExtra("webview_title", "我的钻石");//http://ty.fengyugo.com/h5/h5/recharge.html
                       intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/recharge.html" + "?id=" + MyApplication.getInstance().getUserId());
                       startActivity(intent);
                   }
                Log.e("isFastClick","isFastClick="+CheckUtil.isFastClick()+" ss="+ss);
                break;
            case R.id.get_ll://收益
                intent = new Intent(getActivity(), MyEarningsActivity.class);//
                startActivity(intent);
                break;
            case R.id.room_ll://我的房间
                if (my_room_tv.getText().equals("创建房间")) {
                    getOpenMyRoom();
                } else {//进入房间
//                    intent = new Intent(getActivity(), MainRoomActivity.class);//
//                    intent.putExtra("roomid", userdata.getMyRoomId());//传递房间id
//                    intent.putExtra("roomdata", (Serializable) userdata);//传递房间数据
//                    startActivity(intent);
                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                        if (!MyApplication.getInstance().getMyRoomid().equals(userdata.getMyRoomId())) {//不是同一个房间 先退出MainRoomActivity
                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                        }
                        intent = new Intent(getActivity(), MainRoomActivity.class);//
                        intent.putExtra("roomid", userdata.getMyRoomId());//传递房间id
                        intent.putExtra("roomdata", (Serializable) userdata);//传递房间数据
                        startActivity(intent);
                    } else {//当前没有在房间
                         intent = new Intent(getActivity(), MainRoomActivity.class);//
                        intent.putExtra("roomid", userdata.getMyRoomId());//传递房间id
                        intent.putExtra("roomdata", (Serializable) userdata);//传递房间数据
                        startActivity(intent);
                    }
                }
//                开启直播间
                break;
            case R.id.my_sc_ll://我的收藏
                intent = new Intent(getActivity(), MyCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.levelall_ll://头部 等级点击
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("webview_title", "等级称号");
                intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/grade.html");
                startActivity(intent);
                break;
            case R.id.level_ll://等级称号
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("webview_title", "等级称号");
                intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/grade.html");
                startActivity(intent);
                break;
            case R.id.help_ll://帮助
                intent = new Intent(getActivity(), WebViewActivity2.class);
                intent.putExtra("webview_title", "帮助");
                intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/help.html" + "?userId=" + MyApplication.getInstance().getUserId());
                startActivity(intent);
                break;
            case R.id.setting_ll://设置
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tuercode_ll://点击兔语号
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
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

    public void getFindUserInfo() {//查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", MyApplication.getInstance().getUserId());
        postRequest(RetrofitService.FindUserInfo, map);
        Log.e("getFindUserInfo", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken());
    }

    public void getOpenMyRoom() {//开启直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.OpenMyRoom, map);
    }


    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        RoomModel mMainModel = new Gson().fromJson(result,
                new TypeToken<RoomModel>() {
                }.getType());
        if (url.equals(RetrofitService.Head + RetrofitService.FindUserInfo)) {
            Log.e("MineFragment", "result=" + result);
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    userdata = mMainModel.getData();
                    if (mMainModel.getData().getPortraitPathArray().length > 0) {
                        MyApplication.getInstance().setUserImg(mMainModel.getData().getPortraitPathArray()[0]);
                        Glide.with(mContext)
                                .load(MyApplication.getInstance().getUserImg())
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIv);
                    }
                    int pro = Integer.parseInt(mMainModel.getData().getExp());
                    int max = Integer.parseInt(mMainModel.getData().getNeedExp());
                    progressBar.setProgress(pro);
                    progressBar.setMax(max);
                    level_pro_tv.setText(mMainModel.getData().getExp() + "/" + mMainModel.getData().getNeedExp());
                    level_tv.setText("LV." + mMainModel.getData().getExpRank());
                    int levelnum = Integer.valueOf(mMainModel.getData().getExpRank()).intValue();//用户等级
                    Glide.with(mContext)
                            .load(RetrofitService.bgAraay[levelnum])
                            .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                            .into(level_iv);
                    nameTv.setText(mMainModel.getData().getUsername());
                    MyApplication.getInstance().setUserName(mMainModel.getData().getUsername());
                    fans_tv.setText(mMainModel.getData().getFansCount());
                    follow_tv.setText(mMainModel.getData().getFollowCount());
                    friend_tv.setText(mMainModel.getData().getFriendCount());
                    tuercodeLl.setText("兔语号:" + mMainModel.getData().getTuId());
                    tuercode=mMainModel.getData().getTuId();
//                    myWalletTv.setText(mMainModel.getData().getDiamond());
//                    myGetTv.setText(mMainModel.getData().getEarnings());
                    if (mMainModel.getData().getMyRoomId() == null) {
                        my_room_tv.setText("创建房间");
                    } else {
                        my_room_tv.setText("进入房间");
                    }
                    MyApplication.getInstance().setRoomId(mMainModel.getData().getRoomId());
                    MyApplication.getInstance().setRoomName(mMainModel.getData().getRoomName());
                    MyApplication.getInstance().setRoomnumber(mMainModel.getData().getNumber());
                    MyApplication.getInstance().setRoomtype(mMainModel.getData().getClassifyName());
                    MyApplication.getInstance().setRoomportraitPath(mMainModel.getData().getPortraitPath());
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.OpenMyRoom)) {//开启直播间
            Log.e("MineFragment", "result=" + result);
            if (mMainModel.getCode().equals("1")) {
                creatdata = mMainModel.getData();
                Intent intent = new Intent(getActivity(), MainRoomActivity.class);//
                intent.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                intent.putExtra("roomdata", (Serializable) creatdata);//传递房间数据
                startActivity(intent);
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
