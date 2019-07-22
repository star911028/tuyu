package com.netease.nim.uikit.business.session.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.netease.nim.uikit.common.ToastHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.util.CustomClickListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;
    private TextView title_tv;
    private LinearLayout userinfo_ll,back_ll;
    private ImageView back_iv,userinfo_iv;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private  AudioManager audio;
    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(P2PMessageActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        }

//         audio = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
//        audio.setMicrophoneMute(true);
        title_tv=(TextView)findViewById(R.id.title_tv);
        back_iv=(ImageView)findViewById(R.id.back_iv);
        userinfo_ll=(LinearLayout)findViewById(R.id.userinfo_ll);
        back_ll=(LinearLayout)findViewById(R.id.back_ll);
        userinfo_iv=(ImageView)findViewById(R.id.userinfo_iv);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        userinfo_ll.setOnClickListener(new CustomClickListener() {
//            @Override
//            protected void onSingleClick() {//进入个人主页
//                Log.d("CustomClickListener", "onSingleClick");
//                Intent intent = new Intent("com.nangch.broadcasereceiver.MYRECEIVER");
//                intent.putExtra("sessionId",sessionId);
//                intent.putExtra("sessionName",title_tv.getText().toString());
//                P2PMessageActivity.this.sendBroadcast(intent);      //发送广播
//                Log.e("BroadcastReceiver","发送广播"+"   sessionId="+sessionId+"  sessionName="+title_tv.getText().toString());
//            }
//            @Override
//            protected void onFastClick() {
//                Log.d("CustomClickListener", "onFastClick");
//            }
//        });
//        userinfo_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("onClick","isFastClick="+isFastClick());
//                if (isFastClick()) {
//                    // 进行点击事件后的逻辑操作
//                    //    快速点击
//                }else {
//                    Log.d("CustomClickListener", "onSingleClick");
//                    Intent intent = new Intent("com.nangch.broadcasereceiver.MYRECEIVER");
//                    intent.putExtra("sessionId",sessionId);
//                    intent.putExtra("sessionName",title_tv.getText().toString());
//                    P2PMessageActivity.this.sendBroadcast(intent);      //发送广播
//                    Log.e("BroadcastReceiver","发送广播"+"   sessionId="+sessionId+"  sessionName="+title_tv.getText().toString());
//                }
//            }
//        });
        userinfo_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userinfo_ll.setEnabled(false);
                Intent intent = new Intent("com.nangch.broadcasereceiver.MYRECEIVER");
                intent.putExtra("sessionId",sessionId);
                intent.putExtra("sessionName",title_tv.getText().toString());
                P2PMessageActivity.this.sendBroadcast(intent);      //发送广播
                Log.e("BroadcastReceiver","发送广播"+"   sessionId="+sessionId+"  sessionName="+title_tv.getText().toString());
            }
        });
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
    }


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
//        audio.setMicrophoneMute(false);//关掉云信麦克风使用
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        userinfo_ll.setEnabled(true);
//        audio.setMicrophoneMute(true);//打开云信麦克风使用
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;

    }

    @Override
    public void onPause() {
        super.onPause();
//        audio.setMicrophoneMute(false);//关掉云信麦克风使用
    }

    private void requestBuddyInfo() {
//        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        title_tv.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }


    /**
     * 命令消息接收观察者
     */
    private Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };


    /**
     * 用户信息变更观察者
     */
    private UserInfoObserver userInfoObserver = new UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (!accounts.contains(sessionId)) {
                return;
            }
            requestBuddyInfo();
        }
    };

    /**
     * 好友资料变更（eg:关系）
     */
    private ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    /**
     * 好友在线状态观察者
     */
    private OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            if (!accounts.contains(sessionId)) {
                return;
            }
            // 按照交互来展示
            displayOnlineState();
        }
    };

    private void registerObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (NimUIKit.enableOnlineState()) {
            NimUIKit.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
        }
    }


    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }
        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                ToastHelper.showToastLong(P2PMessageActivity.this, "对方正在输入...");
            } else {
                ToastHelper.showToast(P2PMessageActivity.this, "command: " + content);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }

    @Override
    protected boolean enableSensor() {
        return true;
    }



    //请求权限
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(P2PMessageActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(P2PMessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {
//            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(P2PMessageActivity.this, new String[]{
                    Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, BAIDU_READ_PHONE_STATE);
        } else {
//            startLocation();
            Log.e("onRequestPermissions", "获取到权限");
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
//                    startLocation();
                    Log.e("onRequestPermissions", "获取到权限");
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(P2PMessageActivity.this.getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
