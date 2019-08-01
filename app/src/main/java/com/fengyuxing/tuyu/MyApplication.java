package com.fengyuxing.tuyu;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zego.zegoaudioroom.ZegoAudioRoom;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.entity.ZegoExtPrepSet;
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.http.NetworkManager;
import com.fengyuxing.tuyu.util.SharePreferenceUtil;
import com.fengyuxing.tuyu.yunxin.CustomAttachParser;
import com.fengyuxing.tuyu.zego.AppSignKeyUtils;
import com.fengyuxing.tuyu.zego.PrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fengyuxing.tuyu.yunxin.NimSDKOptionConfig;

import static com.umeng.commonsdk.framework.UMModuleRegister.getAppContext;


public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance = null;
    private SharePreferenceUtil mSPUtil;
    private ArrayList<String> logSet;
    private ZegoAudioRoom mZegoAudioRoom;

    public interface ILogUpdateObserver {
        void onLogAdd(String logMessage);
    }

    @SuppressLint("HandlerLeak")
    private Handler logHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            String logMessage = (String) message.obj;
            if (logSet.size() >= 1000) {
                logSet.remove(logSet.size() - 1);
            }
            logSet.add(0, logMessage);
            synchronized (MyApplication.class) {
                for (ILogUpdateObserver observer : mLogObservers) {
                    observer.onLogAdd(logMessage);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        NetworkManager.initialize(this);
        HttpManager.initialize(this);
        mSPUtil = new SharePreferenceUtil(this, "tuyu");

        NIMClient.init(this, loginInfo(), options());//云信IM聊天  SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        if (NIMUtil.isMainProcess(this)) { // 注意：以下操作必须在主进程中进行
            initUiKit();  // 1、UI相关初始化操作
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser()); // 监听的注册，必须在主进程中。 相关Service调用
        }
        initUmeng(); //友盟初始化
        initSDK();//即构初始化
    }

    private void initUmeng() {
        //友盟初始化
//        UMShareAPI.get(this);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        UMConfigure.init(this, "5d2bebae4ca357bf25000024"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");   //   5d1424b03fc19596ee000de7  自己的友盟账号                5d2bebae4ca357bf25000024  公司友盟账号
        // 将默认Session间隔时长改为40秒。
        MobclickAgent.setSessionContinueMillis(1000*40);
        //微信
        PlatformConfig.setWeixin("wxace2f3323449db60", "0bc9503dab40f90881a7f3ddebd7bb54");
        //新浪微博(第三个参数为回调地址)  App Key：129650144
        PlatformConfig.setSinaWeibo("129650144", "2128b98e3beae065031ccbfc5c28d843", "http://sns.whalecloud.com/sina2/callback");//http://www.tuerapp.com  http://sns.whalecloud.com/sina2/callback
        //QQ
        PlatformConfig.setQQZone("1109455623", "y9HGSFr2cNFkgrrC");
    }

    //云信UI初始化
    private void initUiKit() {
        // 初始化
        NimUIKit.init(this);
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());
        // 3.设置会话中点击事件响应处理（一般需要）
//        SessionHelper.init();
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

    private void initSDK() {//即构初始化
        initData();
        String userId = MyApplication.getInstance().getUserId();
        String userName = MyApplication.getInstance().getUserName();
        Log.e("initSDKv","userId="+userId+"  userName="+userName);
        ZegoAudioRoom.setUser(userId, userName);
        ZegoAudioRoom.setUseTestEnv(false);//     Android设置正式环境：g_ZegoLiveRoom.setTestEnv(false);
        ZegoExtPrepSet config = new ZegoExtPrepSet();
        config.encode = false;
        config.channel = 0;
        config.sampleRate = 0;
        config.samples = 1;
        ZegoAudioRoom.enableAudioPrep2(PrefUtils.isEnableAudioPrepare(), config);

        mZegoAudioRoom = new ZegoAudioRoom();
//         * 设置登录房间成功后，是否手动发布直播。
//         * true：手动发布直播，false：自动发布直播，默认为自动发布直播
        mZegoAudioRoom.setManualPublish(true);//
        long appId;
        byte[] signKey;
        int currentAppFlavor = PrefUtils.getCurrentAppFlavor();
        if (currentAppFlavor <= 1) {
            if (currentAppFlavor == -1 || currentAppFlavor == 0) {
                appId = AppSignKeyUtils.UDP_APP_ID;
                signKey = AppSignKeyUtils.requestSignKey(AppSignKeyUtils.UDP_APP_ID);
            } else {
                appId = AppSignKeyUtils.INTERNATIONAL_APP_ID;
                signKey = AppSignKeyUtils.requestSignKey(AppSignKeyUtils.INTERNATIONAL_APP_ID);
            }

        } else {
            appId = PrefUtils.getAppId();
            signKey = PrefUtils.getAppKey();
        }


//        ZegoAudioRoom.setBusinessType(PrefUtils.getBusinessType());//错误代码 login 256
        ZegoAudioRoom.setBusinessType(0);
        ZegoLiveRoom.setConfig("audio_device_detect_headset=true");
        mZegoAudioRoom.initWithAppId(appId, signKey, this);

        // 启用音频流量控制 需要在初始化之后设置
        mZegoAudioRoom.enableAudioTrafficControl(PrefUtils.getAudioTrafficControl());

//        mZegoAudioRoom.setLatencyMode(ZegoConstants.LatencyMode.Low3);

    }

    public ZegoAudioRoom getAudioRoomClient() {
        return mZegoAudioRoom;
    }

    public void reInitZegoSDK() {
        if (mZegoAudioRoom != null) {
            mZegoAudioRoom.unInit();
        }

        initSDK();
    }

    private void initData() {
        logSet = new ArrayList<>();
    }

    private String getUserIdjg() {
        String userId = PrefUtils.getUserId();
        if (TextUtils.isEmpty(userId)) {
            userId = System.currentTimeMillis() / 1000 + "";
            PrefUtils.setUserId(userId);
        }
        return userId;
    }

    private String getUserNamejg() {
        String userName = PrefUtils.getUserName();
        if (TextUtils.isEmpty(userName)) {
            userName = android.os.Build.MODEL + getUserId();
            PrefUtils.setUserName(userName);
        }
        return userName;
    }
    //判断应用是否在后台
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {//云信聊天配置
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = LoginActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.rabblt_icon;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        String sdkPath = getAppCacheDir(mInstance) + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return NimUIKit.getUserInfoProvider().getUserInfo(account);
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return UserInfoHelper.getUserDisplayName(account);
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
                return null;
            }
        };
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
//    private LoginInfo loginInfo() {
//        return null;
//    }

    private LoginInfo loginInfo() {
        String account = MyApplication.getInstance().getYXcode();
        String token = MyApplication.getInstance().getYXtoken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
//            storageRootPath = Environment.getExternalStorageDirectory() + "/" + DemoCache.getContext().getPackageName();
        }

        return storageRootPath;
    }

    private ArrayList<ILogUpdateObserver> mLogObservers = new ArrayList<>();

    public synchronized void registerLogUpdateObserver(ILogUpdateObserver observer) {
        if (observer != null && !mLogObservers.contains(observer)) {
            mLogObservers.add(observer);
        }
    }

    public synchronized void unregisterLogUpdateObserver(ILogUpdateObserver observer) {
        if (mLogObservers.contains(observer)) {
            mLogObservers.remove(observer);
        }
    }

    public void appendLog(String str) {
        Message msg = Message.obtain(logHandler, 0, str);
        msg.sendToTarget();
    }

    public ArrayList<String> getLogSet() {
        return logSet;
    }

    public void appendLog(String format, Object... args) {
        String str = String.format(format, args);
        appendLog(str);
    }

    /**
     * getInstance
     */
    public synchronized static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * SharePreference
     */
    public synchronized SharePreferenceUtil getSPUtil() {
        if (mSPUtil == null) {
            mSPUtil = new SharePreferenceUtil(this, "scsk");
        }
        return mSPUtil;
    }
    /**
     * 获取当前本地apk的版本
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = getAppContext().getPackageManager().
                    getPackageInfo(getAppContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static String getVersionname() {
        String versionName = "";
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = getAppContext().getPackageManager().
                    getPackageInfo(getAppContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public String getChangeRoomid() {
        return mSPUtil.getStringValue("ChangeRoomid");
    }

    public void setChangeRoomid(String ChangeRoomid) {
        mSPUtil.setStringValue("ChangeRoomid", ChangeRoomid);
    }

    // userId
    public String getUserId() {
        return mSPUtil.getStringValue("userId");
    }

    public void setUserId(String userId) {
        mSPUtil.setStringValue("userId", userId);
    }

    // token
    public String getToken() {
        return mSPUtil.getStringValue("token");
    }

    public void setToken(String token) {
        mSPUtil.setStringValue("token", token);
    }

    // Inputpass
    public String getInputpass() {
        return mSPUtil.getStringValue("Inputpass");
    }

    public void setInputpass(String Inputpass) {
        mSPUtil.setStringValue("Inputpass", Inputpass);
    }
    // newUser
//    public Boolean getNewUser() {
//        return mSPUtil.getBooleanValue("newUser",true);
//    }
//
//    public void setNewUser(Boolean newUser) {
//        mSPUtil.setBooleanValue("newUser", newUser);
//    }

    public String getNewUser() {
        return mSPUtil.getStringValue("newUser");
    }

    public void setNewUser(String newUser) {
        mSPUtil.setStringValue("newUser", newUser);
    }


    public String getRoomId() {
        return mSPUtil.getStringValue("RoomId");
    }

    public void setRoomId(String RoomId) {
        mSPUtil.setStringValue("RoomId", RoomId);
    }

    public String getRoomName() {
        return mSPUtil.getStringValue("RoomName");
    }

    public void setRoomName(String RoomName) {
        mSPUtil.setStringValue("RoomName", RoomName);
    }

    public String getRoomportraitPath() {
        return mSPUtil.getStringValue("RoomportraitPath");
    }

    public void setRoomportraitPath(String RoomportraitPath) {
        mSPUtil.setStringValue("RoomportraitPath", RoomportraitPath);
    }


    public String getRoomnumber() {
        return mSPUtil.getStringValue("Roomnumber");
    }

    public void setRoomnumber(String Roomnumber) {
        mSPUtil.setStringValue("Roomnumber", Roomnumber);
    }

    public String getRoomtype() {
        return mSPUtil.getStringValue("Roomtype");
    }

    public void setRoomtype(String Roomtype) {
        mSPUtil.setStringValue("Roomtype", Roomtype);
    }

    public Boolean getIsFirst() {
        return mSPUtil.getBooleanValue("IsFirst", true);
    }

    public void setIsFirst(Boolean IsFirst) {
        mSPUtil.setBooleanValue("IsFirst", true);
    }

    public String getRoombg() {
        return mSPUtil.getStringValue("Roombg");
    }

    public void setRoombg(String Roombg) {
        mSPUtil.setStringValue("Roombg", Roombg);
    }


    public String getUserName() {
        return mSPUtil.getStringValue("UserName");
    }

    public void setUserName(String UserName) {
        mSPUtil.setStringValue("UserName", UserName);
    }

    public String getUserDiamond() {
        return mSPUtil.getStringValue("UserDiamond");
    }

    public void setUserDiamond(String UserDiamond) {
        mSPUtil.setStringValue("UserDiamond", UserDiamond);
    }

    public String getUserImg() {
        return mSPUtil.getStringValue("UserImg");
    }

    public void setUserImg(String UserImg) {
        mSPUtil.setStringValue("UserImg", UserImg);
    }

    public String getUserRank() {
        return mSPUtil.getStringValue("UserRank");
    }

    public void setUserRank(String UserRank) {
        mSPUtil.setStringValue("UserRank", UserRank);
    }

    public int getUserLevel() {
        return mSPUtil.getIntValue("UserLevel");
    }

    public void setUserLevel(int UserLevel) {
        mSPUtil.setIntValue("UserLevel", UserLevel);
    }


    public String getUpMikeid() {
        return mSPUtil.getStringValue("UpMikeid");
    }

    public void setUpMikeid(String UpMikeid) {
        mSPUtil.setStringValue("UpMikeid", UpMikeid);
    }

    public String getMaiRoomid() {
        return mSPUtil.getStringValue("MaiRoomid");
    }

    public void setMaiRoomid(String MaiRoomid) {
        mSPUtil.setStringValue("MaiRoomid", MaiRoomid);
    }


    public String getYXcode() {
        return mSPUtil.getStringValue("YXcode");
    }

    public void setYXcode(String YXcode) {
        mSPUtil.setStringValue("YXcode", YXcode);
    }

    public String getYXtoken() {
        return mSPUtil.getStringValue("YXtoken");
    }

    public void setYXtoken(String YXtoken) {
        mSPUtil.setStringValue("YXtoken", YXtoken);
    }

    public int getRoomBlacknums() {
        return mSPUtil.getIntValue("RoomBlacknums");
    }

    public void setRoomBlacknums(int RoomBlacknums) {
        mSPUtil.setIntValue("RoomBlacknums", RoomBlacknums);
    }

    public int getRoomAdminnums() {
        return mSPUtil.getIntValue("RoomAdminnums");
    }

    public void setRoomAdminnums(int RoomAdminnums) {
        mSPUtil.setIntValue("RoomAdminnums", RoomAdminnums);
    }


    public String getYunXinRoomId() {
        return mSPUtil.getStringValue("YunXinRoomId");
    }

    public void setYunXinRoomId(String YunXinRoomId) {
        mSPUtil.setStringValue("YunXinRoomId", YunXinRoomId);
    }


    public String getMikeNumber() {
        return mSPUtil.getStringValue("MikeNumber");
    }

    public void setMikeNumber(String MikeNumber) {
        mSPUtil.setStringValue("MikeNumber", MikeNumber);
    }


    public String getEmojiindex() {
        return mSPUtil.getStringValue("Emojiindex");
    }

    public void setEmojiindex(String Emojiindex) {
        mSPUtil.setStringValue("Emojiindex", Emojiindex);
    }

    public String getExitRoomid() {
        return mSPUtil.getStringValue("ExitRoomid");
    }

    public void setExitRoomid(String ExitRoomid) {
        mSPUtil.setStringValue("ExitRoomid", ExitRoomid);
    }

    public String getExitRoomdata() {
        return mSPUtil.getStringValue("ExitRoomdata");
    }

    public void setExitRoomdata(String ExitRoomdata) {
        mSPUtil.setStringValue("ExitRoomdata", ExitRoomdata);
    }


    public String getAddindex1() {
        return mSPUtil.getStringValue("Addindex1");
    }

    public void setAddindex1(String Addindex1) {
        mSPUtil.setStringValue("Addindex1", Addindex1);
    }

    public String getAddindex2() {
        return mSPUtil.getStringValue("Addindex2");
    }

    public void setAddindex2(String Addindex2) {
        mSPUtil.setStringValue("Addindex2", Addindex2);
    }


    public String getCheckIndex() {
        return mSPUtil.getStringValue("CheckIndex");
    }

    public void setCheckIndex(String CheckIndex) {
        mSPUtil.setStringValue("CheckIndex", CheckIndex);
    }


    // token
    public String getMangerLink() {
        return mSPUtil.getStringValue("MangerLink");
    }

    public void setMangerLink(String MangerLink) {
        mSPUtil.setStringValue("MangerLink", MangerLink);
    }


    // 三方登录 昵称
    public String getThirdLoginname() {
        return mSPUtil.getStringValue("ThirdLoginname");
    }

    public void setThirdLoginname(String ThirdLoginname) {
        mSPUtil.setStringValue("ThirdLoginname", ThirdLoginname);
    }

    // 三方登录 性别
    public String getThirdLoginsex() {
        return mSPUtil.getStringValue("ThirdLoginsex");
    }

    public void setThirdLoginsex(String ThirdLoginsex) {
        mSPUtil.setStringValue("ThirdLoginsex", ThirdLoginsex);
    }


    // 三方登录 性别
    public String getThirdLoginpic() {
        return mSPUtil.getStringValue("ThirdLoginpic");
    }

    public void setThirdLoginpic(String ThirdLoginpic) {
        mSPUtil.setStringValue("ThirdLoginpic", ThirdLoginpic);
    }

    // 三方登录 地址
    public String getThirdLoginadd() {
        return mSPUtil.getStringValue("ThirdLoginadd");
    }

    public void setThirdLoginadd(String ThirdLoginadd) {
        mSPUtil.setStringValue("ThirdLoginadd", ThirdLoginadd);
    }

    // 三方登录 openid
    public String getThirdLoginopenid() {
        return mSPUtil.getStringValue("ThirdLoginopenid");
    }

    public void setThirdLoginopenid(String ThirdLoginopenid) {
        mSPUtil.setStringValue("ThirdLoginopenid", ThirdLoginopenid);
    }


    // 礼物 房主id
    public String getGiftHosterid() {
        return mSPUtil.getStringValue("GiftHosterid");
    }

    public void setGiftHosterid(String GiftHosterid) {
        mSPUtil.setStringValue("GiftHosterid", GiftHosterid);
    }

    public String getTuercode() {
        return mSPUtil.getStringValue("Tuercode");
    }

    public void setTuercode(String Tuercode) {
        mSPUtil.setStringValue("Tuercode", Tuercode);
    }


    public String getLookforManger() {
        return mSPUtil.getStringValue("LookforManger");
    }

    public void setLookforManger(String LookforManger) {
        mSPUtil.setStringValue("LookforManger", LookforManger);
    }



    //最小化离开房间时 保存自己当前房间号
    public String getMyRoomid() {
        return mSPUtil.getStringValue("MyRoomid");
    }

    public void setMyRoomid(String MyRoomid) {
        mSPUtil.setStringValue("MyRoomid", MyRoomid);
    }


    public String getRoomBack() {
        return mSPUtil.getStringValue("RoomBack");
    }

    public void setRoomBack(String RoomBack) {
        mSPUtil.setStringValue("RoomBack", RoomBack);
    }

    public String getIsRoomto() {
        return mSPUtil.getStringValue("IsRoomto");
    }

    public void setIsRoomto(String IsRoomto) {
        mSPUtil.setStringValue("IsRoomto", IsRoomto);
    }

    public String getIsInRoom() {
        return mSPUtil.getStringValue("IsInRoom");
    }

    public void setIsInRoom(String IsInRoom) {
        mSPUtil.setStringValue("IsInRoom", IsInRoom);
    }
}
