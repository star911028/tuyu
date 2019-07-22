package com.fengyuxing.tuyu.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.MyReceiver;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.MainRoomSetAdapter;
import com.fengyuxing.tuyu.adapter.RoomNewsRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.GiftList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.MainDTO;
import com.fengyuxing.tuyu.bean.MainGiftModel;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.bean.NewsArray;
import com.fengyuxing.tuyu.bean.StringModel;
import com.fengyuxing.tuyu.task.BaseSyncTask;
import com.fengyuxing.tuyu.task.TinySyncExecutor;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.ShareUtils;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.TabCheckEventList;
import com.fengyuxing.tuyu.view.EmojiWindow;
import com.fengyuxing.tuyu.view.ExitRoomWindow;
import com.fengyuxing.tuyu.view.GiftWindow;
import com.fengyuxing.tuyu.view.InPutWindow;
import com.fengyuxing.tuyu.view.RoomAdminSetWindow;
import com.fengyuxing.tuyu.view.RoomAdminWindow;
import com.fengyuxing.tuyu.view.RoomBGWindow;
import com.fengyuxing.tuyu.view.RoomEggsWindow;
import com.fengyuxing.tuyu.view.RoomGiftWindow;
import com.fengyuxing.tuyu.view.RoomGiftWindowAll;
import com.fengyuxing.tuyu.view.RoomGoldWindow;
import com.fengyuxing.tuyu.view.RoomLookInfoWindow;
import com.fengyuxing.tuyu.view.RoomLookInfoWindowRoomHost;
import com.fengyuxing.tuyu.view.RoomMikeLineWindow;
import com.fengyuxing.tuyu.view.RoomPeoWindow;
import com.fengyuxing.tuyu.view.RoomSetingWindow;
import com.fengyuxing.tuyu.view.RoomShareWindow;
import com.fengyuxing.tuyu.view.RoomUpmaiWindow;
import com.fengyuxing.tuyu.view.RoomValueWindow;
import com.fengyuxing.tuyu.view.RoomruleWindow;
import com.fengyuxing.tuyu.yunxin.CustomAttachment;
import com.fengyuxing.tuyu.yunxin.MainDataBean;
import com.fengyuxing.tuyu.yunxin.UserData;
import com.fengyuxing.tuyu.zego.EntityConversion;
import com.fengyuxing.tuyu.zego.PrefUtils;
import com.fengyuxing.tuyu.zego.RecyclerGridViewAdapter;
import com.fengyuxing.tuyu.zego.StreamState;
import com.fengyuxing.tuyu.zego.SystemUtil;
import com.zego.zegoaudioroom.ZegoAudioAVEngineDelegate;
import com.zego.zegoaudioroom.ZegoAudioDeviceEventDelegate;
import com.zego.zegoaudioroom.ZegoAudioLiveEvent;
import com.zego.zegoaudioroom.ZegoAudioLiveEventDelegate;
import com.zego.zegoaudioroom.ZegoAudioLivePlayerDelegate;
import com.zego.zegoaudioroom.ZegoAudioLivePublisherDelegate;
import com.zego.zegoaudioroom.ZegoAudioLiveRecordDelegate;
import com.zego.zegoaudioroom.ZegoAudioPrepDelegate2;
import com.zego.zegoaudioroom.ZegoAudioPrepareDelegate;
import com.zego.zegoaudioroom.ZegoAudioRoom;
import com.zego.zegoaudioroom.ZegoAudioRoomDelegate;
import com.zego.zegoaudioroom.ZegoAudioStream;
import com.zego.zegoaudioroom.ZegoAudioStreamType;
import com.zego.zegoaudioroom.ZegoAuxData;
import com.zego.zegoaudioroom.ZegoLoginAudioRoomCallback;
import com.zego.zegoavkit2.ZegoMediaPlayer;
import com.zego.zegoavkit2.soundlevel.IZegoSoundLevelCallback;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelMonitor;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoIM;
import com.zego.zegoliveroom.entity.ZegoAudioFrame;
import com.zego.zegoliveroom.entity.ZegoBigRoomMessage;
import com.zego.zegoliveroom.entity.ZegoConversationMessage;
import com.zego.zegoliveroom.entity.ZegoPlayStreamQuality;
import com.zego.zegoliveroom.entity.ZegoPublishStreamQuality;
import com.zego.zegoliveroom.entity.ZegoRoomMessage;
import com.zego.zegoliveroom.entity.ZegoUserState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.kevin.floatingeditor.EditorCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

import static com.fengyuxing.tuyu.activity.MainActivity.HasMainActivity;
import static com.fengyuxing.tuyu.util.RetrofitService.CancelSetReceptionistMike;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Play_BeginRetry;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Play_RetrySuccess;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Play_TempDisconnected;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Publish_BeginRetry;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Publish_RetrySuccess;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Publish_TempDisconnected;

//import com.zy.live.yunxin.CrapsAction;

public class MainRoomActivity extends BaseActivity implements View.OnClickListener, SensorEventListener, EditorCallback , MyReceiver.Message{
    //主房间页面
    private static final String TAG = "MainRoomActivity";
    @BindView(R.id.img_iv)
    CircleImageView imgIv;
    @BindView(R.id.roomtype_tv)
    TextView roomtypeTv;
    @BindView(R.id.roomid_tv)
    TextView roomidTv;
    @BindView(R.id.roompeonum_tv)
    TextView roompeonumTv;
    @BindView(R.id.setting_ll)
    LinearLayout setting_ll;
    @BindView(R.id.exit_iv)
    ImageView exitIv;
    @BindView(R.id.share_iv)
    ImageView shareIv;
    @BindView(R.id.top_ll)
    LinearLayout topLl;
    @BindView(R.id.rule_ll)
    LinearLayout ruleLl;
    @BindView(R.id.rank_iv1)
    CircleImageView rankIv1;
    @BindView(R.id.rank_iv2)
    CircleImageView rankIv2;
    @BindView(R.id.rank_iv3)
    CircleImageView rankIv3;

    @BindView(R.id.rank_rl_gx1)
    RelativeLayout rank_rl_gx1;
    @BindView(R.id.rank_rl_gx2)
    RelativeLayout rank_rl_gx2;
    @BindView(R.id.rank_rl_gx3)
    RelativeLayout rank_rl_gx3;

    @BindView(R.id.rank_ll)
    LinearLayout rankLl;
    @BindView(R.id.set_recyclerview)
    RecyclerView setRecyclerview;
    @BindView(R.id.talk_et)
    EditText talkEt;
    @BindView(R.id.mai_ll)
    LinearLayout mai_ll;
    @BindView(R.id.rowwheat_ll)
    LinearLayout rowwheat_ll;
    @BindView(R.id.gold_fl)
    FrameLayout gold_fl;

    @BindView(R.id.roomname)
    TextView roomname;
    @BindView(R.id.my_mike_type)
    TextView my_mike_type;
    @BindView(R.id.expression_ll)
    LinearLayout expressionLl;
    @BindView(R.id.gift_ll)
    LinearLayout giftLl;
    @BindView(R.id.bottom_ll)
    LinearLayout bottomLl;
    @BindView(R.id.main_rv)
    RelativeLayout mainRv;
    @BindView(R.id.imggold_iv)
    CircleImageView imggoldIv;
    @BindView(R.id.mic_phone_cb)
    CheckBox mic_phone_cb;
    @BindView(R.id.gold_name_tv)
    TextView gold_name_tv;
    @BindView(R.id.main_bg_iv)
    ImageView mainBgIv;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.animation_eggs_view)
    LottieAnimationView animation_eggs_view;


    @BindView(R.id.talk_ll)
    LinearLayout talkLl;
    @BindView(R.id.room_follow_iv)
    ImageView room_follow_iv;


    @BindView(R.id.rank_tv)
    TextView rank_tv;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.chat_ll)
    LinearLayout chatLl;
    @BindView(R.id.onlinenum_ll)
    LinearLayout onlinenum_ll;

    @BindView(R.id.news_recyclerview)
    RecyclerView newsRecyclerview;
    @BindView(R.id.gift_iv)
    ImageView giftIv;
    @BindView(R.id.gift_iv2)
    ImageView giftIv2;
    @BindView(R.id.gift_iv3)
    ImageView giftIv3;
    @BindView(R.id.gift_iv4)
    ImageView giftIv4;
    @BindView(R.id.gift_iv5)
    ImageView giftIv5;
    @BindView(R.id.gift_iv6)
    ImageView giftIv6;
    @BindView(R.id.gift_iv7)
    ImageView giftIv7;
    @BindView(R.id.gift_iv8)
    ImageView giftIv8;
    @BindView(R.id.sound_iv)
    ImageView soundIv;
    @BindView(R.id.sound_iv2)
    ImageView soundIv2;
    @BindView(R.id.sound_iv3)
    ImageView soundIv3;
    @BindView(R.id.sound_iv4)
    ImageView soundIv4;
    @BindView(R.id.sound_iv5)
    ImageView soundIv5;
    @BindView(R.id.sound_iv6)
    ImageView soundIv6;
    @BindView(R.id.sound_iv7)
    ImageView soundIv7;
    @BindView(R.id.sound_iv8)
    ImageView soundIv8;
//    @BindView(R.id.test_iv)
//    ImageView test_iv;

    @BindView(R.id.set_ll)
    LinearLayout setLl;
    @BindView(R.id.hoster_tv)
    TextView hosterTv;
    @BindView(R.id.nums_tv)
    TextView numsTv;
    @BindView(R.id.cardiac_ll)
    LinearLayout cardiacLl;
    @BindView(R.id.main_center_ll)
    LinearLayout mainCenterLl;
    private ArrayList<GiftList> listgift = new ArrayList<>();
    private List<DataList> datalist = new ArrayList<>();
    private MainRoomSetAdapter recyAdapter;
    private RoomNewsRecyAdapter roomrecyAdapter;
    private RoomGoldWindow mRoomGoldWindow;
    private RoomruleWindow mRoomruleWindow;
    private RoomPeoWindow mRoomPeoWindow;
    private RoomShareWindow mRoomShareWindow;
    private RoomSetingWindow mRoomSetingWindow;
    private RoomUpmaiWindow mRoomUpmaiWindow;
    private RoomValueWindow mRoomValueWindow;
    private RoomAdminWindow mRoomAdminWindow;
    private RoomBGWindow mRoomBGWindow;
    private RoomAdminSetWindow mRoomAdminSetWindow;
    private RoomGiftWindow mRoomGiftWindow;
    private RoomGiftWindowAll mRoomGiftWindowAll;
    private RoomLookInfoWindow mRoomLookInfoWindow;
    private DataList roominfo;
    private Boolean isManager = false;
    private List<String> newslist = new ArrayList<>();//
    private List<NewsArray> newsArrays = new ArrayList<>();//
    private NewsArray newsArraydata;
    private List<MikeArray> mikeArray = new ArrayList<>();//
    private List<MikeArray> giftArray = new ArrayList<>();//
    private List<MikeArray> pakagegiftArray = new ArrayList<>();//背包礼物
    private MainDTO roomdata;
    private String Roomid = "";
    private String Mangerid = "";
    private String Roomblacknum = "";
    private List<DataList> onlineList = new ArrayList<>();
    private Boolean IsShowUPMike = false;
    private RoomMikeLineWindow mRoomMikeLineWindow;
    private GiftWindow mGiftAnimWindow;
    private ExitRoomWindow mExitRoomWindow;
    private LinearLayoutManager layoutManager;
    private boolean lookgold = false;
    private boolean CannotTalk = false;
    private String CannotTalkname = "";
    private String ShowCharm = "false";
    private String LookID = "";
    private String LookName = "";
    private String Lookmanger = "";
    private String Roompic = "";
    //----------------------------即构配置-------------------------------------
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private ZegoAudioRoom zegoAudioRoom;
    private String currentRoomId;
    private String appTitle;
    private ZegoUserState zegoUserState = new ZegoUserState();
//    private RecyclerGridViewAdapter recyAdapter;
    /**
     * 是否已经推流
     */
    private boolean hasPublish = false;
    private String publishStreamId = null;
    private boolean hasLogin = false;
    private boolean hasBoom = false;
    private ZegoMediaPlayer zegoMediaPlayer = null;
    private Handler handler;
    final int RESTART_PUBLSH_MSG = 1;
    private HandlerThread handlerThread;
    int restartCount = 0;
    private boolean isPromptToast = true;
    //调用距离传感器，控制屏幕
    private SensorManager mManager;//传感器管理对象
    //屏幕开关
    private PowerManager localPowerManager = null;//电源管理对象
    private PowerManager.WakeLock localWakeLock = null;//电源锁
    private Boolean IsEnterRoom = false;
    private Boolean IsStartPush = false;
    private String YXRoomid = "";
    private String Description = "";//房间公告
    private String Welcome = "";//房间公告
    private EmojiWindow mEmojiWindow;
    private String Roompass = "";
    private Boolean IsCollect = false;
    private String ExpRank = "";
    private RoomLookInfoWindowRoomHost mRoomLookInfoWindowRoomHost;
    private InPutWindow mInPutWindow;
    private Observer<List<ChatRoomMessage>> incomingChatRoomMsg;
    private Boolean ISshownews = true;
    private Boolean OpenMick = false;
    private String Roomnumber = "";
    private String Ismiker = "";
    private String takeblackerId = "";
    // 初始化定时器
    private Timer timer = new Timer();
    private ZegoPhoneStateListener mPhoneStateListener;
    private Boolean isFollow = false;
    private Boolean LookMIkeUser = false;
    private int LookPostion = -1;
    private int[] giftAraay = {R.drawable.emoji1, R.drawable.emoji2, R.drawable.emoji3, R.drawable.emoji4, R.drawable.emoji5, R.drawable.emoji6, R.drawable.emoji7, R.drawable.emoji8, R.drawable.emoji9
            , R.drawable.emoji10, R.drawable.emoji11, R.drawable.emoji12, R.drawable.emoji13, R.drawable.emoji14, R.drawable.emoji15, R.drawable.emoji16, R.drawable.emoji17, R.drawable.emoji18,};
    private String OnlineType = "";//打开在线列表类型
    private RoomEggsWindow mRoomEggsWindow;
    private String MyRoleType = "";
    private AnimationDrawable animationDrawable, animationDrawabletest, animationDrawable2, animationDrawable3, animationDrawable4, animationDrawable5, animationDrawable6, animationDrawable7, animationDrawable8;
    private BitmapDrawable animationDrawableqwe;
    private Boolean Updatanews = true;
    public static boolean returnActivityB;//是否直接返回房间  在Mainactivity判断
    public static boolean returnActivitySmall;//是否直接返回房间  在Mainactivity判断
    private AudioManager audioManager;
    MyReceiver myReceiver;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_room;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        Log.e("MainRoomLive", "initView");
//        test_iv.setVisibility(View.VISIBLE);
//        test_iv.setImageResource(R.drawable.room_man_soundgif);
//        animationDrawabletest = (AnimationDrawable) test_iv.getDrawable();
//        animationDrawabletest.start();
        PlayEggs();

        regisgerPhoneCallingListener();
        startTimer();//开始定时器

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        MyApplication.getInstance().setMaiRoomid("");
        MyApplication.getInstance().setExitRoomid("");
        MyApplication.getInstance().setExitRoomdata("");
        newsRecyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉上下拉效果
        //八个座位框
        setRecyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉上下拉效果
        setRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        roominfo = (DataList) getIntent().getSerializableExtra("roomdata");//接收房间信息对象
        if (roominfo != null) {
            if (getIntent().getStringExtra("roomid") != null) {
                Roomid = getIntent().getStringExtra("roomid");//房间id
                if (getIntent().getStringExtra("roompass") != null) {
                    Roompass = getIntent().getStringExtra("roompass");//房间密码
                } else {
                    Roompass = "";//
                }
                Log.e("Roomid", "Roomid=" + Roomid);
                getFindRoomInfo();//查询直播间信息
                initZego();//既构初始化用户信息 登陆
                MyApplication.getInstance().setMaiRoomid(Roomid);
            }
        } else {
            Log.e("MainRoom roominfo", "roominfo = null");
        }
        getBackgroundImgPath();//获取主页背景图
        FindGift();//查询礼物列表
        FindPersonalGift();//查询背包礼物列表
        //聊天室消息监听
        incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
            @Override
            public void onEvent(List<ChatRoomMessage> messages) {

                Log.e("incomingChatRoomMsg", "messages=" + messages.size());
                // 处理新收到的消息
                for (int i = 0; i < messages.size(); i++) {
                    Log.e("incomingChatRoomMsg", "i= " + i + "  getFromAccount=" + messages.get(i).getFromAccount() +
                            "  getMsgType=" + messages.get(i).getMsgType()
                            + "  getContent=" + messages.get(i).getContent()
                            + "  getFromNick=" + messages.get(i).getFromNick());

                    if (messages.get(i).getMsgType() == MsgTypeEnum.custom) {//接收自定义消息
                        final CustomAttachment attachment = (CustomAttachment) messages.get(i).getAttachment();   //实例化一个attachment
                        if (attachment == null) {
                            Log.e("incomingChatRoomMsg", "attachment=" + null);
                        } else {
                            Log.e("incomingChatRoomMsg", "attachment=不为空");
                            // TODO 根据返回的type 处理操作
                            if (attachment.getMainDataBean().getType() != null) {
                                String strJson = JSON.toJSONString(attachment.getMainDataBean());
                                String type = attachment.getMainDataBean().getType();//消息类型
                                Log.e("incomingChatRoomMsg", "mainDataBean getType=" + attachment.getMainDataBean().getType() + "    strJson=" + strJson);
                                if (type.equals("2")) { // 收藏房间
//                                    newslist.add(attachment.getMainDataBean().getFromUser().getUsername() + " 收藏了房间");//添加消息数据到列表
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody(attachment.getMainDataBean().getFromUser().getUsername() + " 收藏了房间");
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArrays.add(newsArraydata);

                                } else if (type.equals("4")) {//收到进入房间消息
                                    FindCacheRoomInfo();//刷新直播间信息
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody("欢迎 " + attachment.getMainDataBean().getFromUser().getUsername() + " 进入房间");
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArraydata.setUserid(attachment.getMainDataBean().getFromUser().getUserId());
                                    newsArrays.add(newsArraydata);

//                                    newslist.add("欢迎 " + attachment.getMainDataBean().getFromUser().getUsername() + " 进入房间");//添加消息数据到列表
                                } else if (type.equals("5")) { // 上麦    如果toUser.getuserid=0  说明是自己上下麦操作  用来判断是否弹出抱人上麦弹框
                                    FindCacheRoomInfo();//刷新直播间信息
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody(attachment.getMainDataBean().getToUser().getUsername() + " " + attachment.getMainDataBean().getBody());
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArrays.add(newsArraydata);
                                } else if (type.equals("6")) {//下麦    如果toUser.getuserid=0  说明是自己上下麦操作  用来判断是否弹出抱人上麦弹框
                                    FindCacheRoomInfo();//刷新直播间信息
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody(attachment.getMainDataBean().getToUser().getUsername() + " " + attachment.getMainDataBean().getBody());
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArrays.add(newsArraydata);
                                } else if (type.equals("8")) {//接收禁言消息
                                    newslist.add(attachment.getMainDataBean().getFromUser().getUsername() + " 被管理员禁言");//添加消息数据到列表
                                    if (attachment.getMainDataBean().getFromUser().getUsername().equals(MyApplication.getInstance().getUserName())) {//如果自己被禁言
                                        FindCacheRoomInfo();//刷新直播间信息
                                    }
                                } else if (type.equals("10")) {//聊天消息
                                    //    newslist.add(attachment.getMainDataBean().getFromUser().getUsername() + ":" + attachment.getMainDataBean().getBody());//添加消息数据到列表
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody(attachment.getMainDataBean().getFromUser().getUsername() + ":" + attachment.getMainDataBean().getBody());
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArraydata.setUserid(attachment.getMainDataBean().getFromUser().getUserId());
                                    newsArrays.add(newsArraydata);
                                } else if (type.equals("11")) {//送礼消息
                                    //队列播放
                                    final BaseSyncTask task1 = new BaseSyncTask() {
                                        @Override
                                        public void doTask() {
                                            Log.e("doTask", "task1" + "   " + attachment.getMainDataBean().getGift().getFileIdentifier());
                                            if (attachment.getMainDataBean().getGift().getFileIdentifier() != null) {
                                                PlaygiftAnim(attachment.getMainDataBean().getGift().getFileIdentifier());
                                            }
                                        }
                                    };
                                    TinySyncExecutor.getInstance().enqueue(task1);//添加队列
                                    FindCacheRoomInfo();//刷新直播间信息
//                                    newslist.add(attachment.getMainDataBean().getFromUser().getUsername() + " 打赏 " + attachment.getMainDataBean().getToUser().getUsername() + " " + getGiftName(attachment.getMainDataBean().getGift().getGiftId()) + " X" + attachment.getMainDataBean().getGift().getCount());//添加消息数据到列表
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody(attachment.getMainDataBean().getFromUser().getUsername() + " 打赏 " + attachment.getMainDataBean().getToUser().getUsername() + " " + getGiftName(attachment.getMainDataBean().getGift().getGiftId()) + " X" + attachment.getMainDataBean().getGift().getCount());
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArraydata.setUserid(attachment.getMainDataBean().getFromUser().getUserId());
                                    newsArrays.add(newsArraydata);
//                                    if (attachment.getMainDataBean().getGift().getGiftId() != null) {
//                                        GiftAnimWindow(attachment.getMainDataBean().getGift().getGiftId());
//                                    }
                                } else if (type.equals("12")) {//设置管理员
                                    if (attachment.getMainDataBean().getToUser().getUserId().equals(MyApplication.getInstance().getUserId())) {
//                                        newslist.add(attachment.getMainDataBean().getBody());//添加消息数据到列表
                                        NewsArray newsArraydata = new NewsArray();
                                        newsArraydata.setBody(attachment.getMainDataBean().getBody());
                                        newsArrays.add(newsArraydata);
                                    }
                                    getFindRoomInfo();//刷新直播间信息
//                                    FindCacheRoomInfo();//刷新直播间信息
                                } else if (type.equals("13")) {//取消设置管理员
                                    if (attachment.getMainDataBean().getToUser().getUserId().equals(MyApplication.getInstance().getUserId())) {
//                                        newslist.add(attachment.getMainDataBean().getBody());//添加消息数据到列表
                                        NewsArray newsArraydata = new NewsArray();
                                        newsArraydata.setBody(attachment.getMainDataBean().getBody());
                                        newsArrays.add(newsArraydata);
                                    }
//                                    FindCacheRoomInfo();//刷新直播间信息
                                    getFindRoomInfo();//刷新直播间信息
                                } else if (type.equals("14")) {//拉黑用户
                                    if (MyApplication.getInstance().getUserId().equals(attachment.getMainDataBean().getFromUser().getExpRank())) {//自己被拉黑
                                        Toast.makeText(mContext, "你已被管理员拉黑,即将退出房间", Toast.LENGTH_SHORT).show();
                                        OutLiveRoom();//退出房间
                                    }
                                } else if (type.equals("15")) {//取消拉黑用户
//                                    FindCacheRoomInfo();//刷新直播间信息
                                } else if (type.equals("17")) {//设置显示心动值
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("18")) {//设置不显示心动值
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("19")) {//清空心动值
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("20")) {//修改背景图片
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("21")) {//修改房间公告
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("22")) {//修改房间信息
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("23")) {//闭麦
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("24")) {//取消闭麦
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("25")) {//锁麦
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("26")) {//取消锁麦
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("27")) {//设置老板位
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("28")) {//取消设置老板位
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("29")) {//设置主持位
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("30")) {//取消设置主持位
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("31")) {//设置麦位需要排队
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("32")) {//取消设置麦位需要排队
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("33")) {//退出房间
                                    FindCacheRoomInfo();//刷新直播间信息
                                    Updatanews = false;
                                } else if (type.equals("34")) {//房间公屏警告
                                    //   房间公屏显示警告内容(公告的样式)
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody("警告: " + attachment.getMainDataBean().getBody());
//                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArrays.add(newsArraydata);

//                                    newslist.add("警告: " + attachment.getMainDataBean().getBody());//添加消息数据到列表
                                } else if (type.equals("35")) {//封禁房间
                                    //1.提示房间被封     2.所有人离开直播间，回到上一级页面
                                    Toast.makeText(mContext, "当前房间已被封禁,将退出房间", Toast.LENGTH_SHORT).show();
                                    OutLiveRoom();//退出房间
                                } else if (type.equals("36")) {//收到表情消息
                                    Updatanews = false;
                                    if (attachment.getMainDataBean().getBody().length() < 3) {
                                        int giftindex = Integer.parseInt(attachment.getMainDataBean().getBody());//要播放的表情的索引
                                        int mikenumber = 0;
                                        Log.e("incomingChatRoomMsg", "getUserId=" + attachment.getMainDataBean().getFromUser().getUserId());
                                        for (int k = 0; k < roomdata.getMikeArray().size(); k++) {
                                            if (roomdata.getMikeArray().get(k).getMikerId() != null) {
                                                if (roomdata.getMikeArray().get(k).getMikerId().equals(attachment.getMainDataBean().getFromUser().getUserId())) {
                                                    mikenumber = Integer.parseInt(roomdata.getMikeArray().get(k).getMikeNumber()) - 1;
                                                }
                                            }
                                        }
                                        RoomPlayemoji(giftindex, mikenumber);
                                        Log.e("incomingChatRoomMsg", "giftindex表情索引=" + giftindex + "  mikenumber位置索引=" + mikenumber);
                                    }
                                } else if (type.equals("37")) {//展示公屏
                                    newsArrays.clear();
                                    if (roomrecyAdapter != null) {
                                        roomrecyAdapter.notifyDataSetChanged();
                                    }
                                    newsRecyclerview.setVisibility(View.VISIBLE);
                                    FindCacheRoomInfo();//刷新直播间信息
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody("系统消息: 公屏已开启");
                                    newsArrays.add(newsArraydata);

                                    NewsArray newsArraydata2 = new NewsArray();
                                    newsArraydata2.setBody("官方公告：官方倡导绿色互动，请勿发布违法、低俗、暴力、广告等内容，禁止违规交易，违规者将被封禁账号。");
                                    newsArrays.add(newsArraydata2);
                                } else if (type.equals("38")) {//关闭公屏
                                    //清空公屏消息
                                    newsArrays.clear();
                                    if (roomrecyAdapter != null) {
                                        roomrecyAdapter.notifyDataSetChanged();
                                    }
                                    newsRecyclerview.setVisibility(View.VISIBLE);
                                    FindCacheRoomInfo();//刷新直播间信息
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody("系统消息: 公屏已被关闭，暂不能发言");
                                    newsArrays.add(newsArraydata);
                                } else if (type.equals("39")) {//砸蛋中奖消息
                                    NewsArray newsArraydata = new NewsArray();
                                    newsArraydata.setBody(attachment.getMainDataBean().getFromUser().getUsername() + " 真幸运! 打中了 " + attachment.getMainDataBean().getGift().getGiftName() + " X " + attachment.getMainDataBean().getGift().getCount());
                                    newsArraydata.setExpRank(attachment.getMainDataBean().getFromUser().getExpRank());
                                    newsArraydata.setUserid(attachment.getMainDataBean().getFromUser().getUserId());
                                    newsArrays.add(newsArraydata);
                                }
                            } else {
                                Log.e("incomingChatRoomMsg", "mainDataBean getType=" + attachment.getMainDataBean().getType() + "    ");
                            }
                        }
                    } else {//文字消息
                        if (messages.get(i).getContent() != null) {
                        }
                    }
                }
                if (roomrecyAdapter == null) {
                    roomrecyAdapter = new RoomNewsRecyAdapter(mContext, newsArrays);
                    layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    layoutManager.setStackFromEnd(true);
                    newsRecyclerview.setLayoutManager(layoutManager);
                    newsRecyclerview.scrollToPosition(roomrecyAdapter.getItemCount() - 1);
                    roomrecyAdapter.setOnItemClickListener(mOnItemClickListener2);
                    newsRecyclerview.setAdapter(roomrecyAdapter);
                } else {
                    if (Updatanews) {//是否需要刷新消息 针对收到云信消息但是没有添加消息数据 最后一条消息抖动
                        newsRecyclerview.scrollToPosition(roomrecyAdapter.getItemCount() - 1);
                        //单条插入 解决短时间快速刷新抖动问题
                        roomrecyAdapter.notifyItemInserted(roomrecyAdapter.getItemCount() - 1);//通知演示插入动画
                        roomrecyAdapter.notifyItemRangeChanged(roomrecyAdapter.getItemCount() - 1, roomrecyAdapter.getItemCount());//通知数据与界面重新绑定
                    } else {//
                        Updatanews = true;
                    }
                }
            }
        };
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, true);
        Log.e("NIMClient", "incomingChatRoomMsg注册");
    }

//    //添加数据
//    public void addItem(int position, Object data) {
//        mDatas.add(position, data);
//        notifyItemInserted(position);//通知演示插入动画
//        notifyItemRangeChanged(position,mDatas.size()-position);//通知数据与界面重新绑定
//    }

    public void RoomPlayemoji(int emojiindex, int emojiposition) {
        if (emojiposition == 0) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition0=" + emojiposition);
            giftIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv);
            giftIv.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv.clearAnimation();//清除动画
                    giftIv.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 1) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv2.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv2);
            giftIv2.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv2.clearAnimation();//清除动画
                    giftIv2.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 2) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv3.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv3);
            giftIv3.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv3.clearAnimation();//清除动画
                    giftIv3.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 3) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv4.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv4);
            giftIv4.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv4.clearAnimation();//清除动画
                    giftIv4.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 4) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv5.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv5);
            giftIv5.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv5.clearAnimation();//清除动画
                    giftIv5.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 5) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv6.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv6);
            giftIv6.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv6.clearAnimation();//清除动画
                    giftIv6.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 6) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv7.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv7);
            giftIv7.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv7.clearAnimation();//清除动画
                    giftIv7.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        } else if (emojiposition == 7) {
            Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
            giftIv8.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(giftAraay[emojiindex]).into(giftIv8);
            giftIv8.postDelayed(new Runnable() {
                @Override
                public void run() {//两秒后清除动画并隐藏控件
                    giftIv8.clearAnimation();//清除动画
                    giftIv8.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        }
    }

    public void EnterChatRoom(final String Yxroomid) {//进入聊天室
        // roomId 表示聊天室ID

        EnterChatRoomData data = new EnterChatRoomData(Yxroomid);
        // 以登录一次不重试为例
        NIMClient.getService(ChatRoomService.class).enterChatRoomEx(data, 10).setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                IsEnterRoom = true;
                YXRoomid = Yxroomid;
                // 登录成功
                Log.e("EnterChatRoom", "onSuccess=" + result.getAccount() + "   YXRoomid=" + Yxroomid);
                MainDataBean mainDataBean = new MainDataBean();
                mainDataBean.setType("4");
                UserData data = new UserData();
                data.setExpRank(MyApplication.getInstance().getUserRank());
                data.setPortraitPath(MyApplication.getInstance().getUserImg());
                data.setUserId(MyApplication.getInstance().getUserId());
                data.setUsername(MyApplication.getInstance().getUserName());
                mainDataBean.setFromUser(data);
                //对象转字符串
                Gson gson = new Gson();
                String jsondata = gson.toJson(mainDataBean);
                System.out.println(result);
                ChatRoomSendMSG2("4", "", "", "", jsondata);
                Log.e("进入房间", "jsondata=" + jsondata);
                //TODO-----------------------------------------------------------------
            }

            @Override
            public void onFailed(int code) {
                IsEnterRoom = false;
                // 登录失败
                Log.e("EnterChatRoom", "onFailed   code=" + code + "   YXRoomid=" + Yxroomid);
            }

            @Override
            public void onException(Throwable exception) {
                // 错误
                IsEnterRoom = false;
                Log.e("EnterChatRoom", "onException   =" + exception + "   YXRoomid=" + Yxroomid);
            }
        });
    }

    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    //发送自定义消息
    public void ChatRoomSendMSG2(final String type, final String body, final String fromname, final String fromrank, final String jsondata) {

        //创建自定义消息
        CustomAttachment customAttachment = new CustomAttachment();
        MainDataBean mainDataBean = new MainDataBean();
        UserData userData = new UserData();
        userData.setUsername(fromname);
        userData.setExpRank(fromrank);
        mainDataBean.setBody(body);
        mainDataBean.setType(type);
        mainDataBean.setFromUser(userData);
//        customAttachment.setMainDataBean(mainDataBean);//此方法传入的为对象和后台不一致
        if (jsondata.length() > 0) {//传递后台返回的json数据
            customAttachment.fromJson(jsondata); //自己要传的json字符串
        } else {//传递自己创建的josn数据
            String strJson = JSON.toJSONString(mainDataBean);//将自己要传的对象转为字符串 和后台一致  然后发送出去
            customAttachment.fromJson(strJson); //自己要传的json字符串
        }
        Log.e("ChatRoomCustomMessage", "YXRoomid=" + YXRoomid);
        ChatRoomMessage message3 = ChatRoomMessageBuilder.createChatRoomCustomMessage(YXRoomid, customAttachment);//创建自定义消息
        // 将自定义消息发送出去
        NIMClient.getService(ChatRoomService.class).sendMessage(message3, true)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        final MainDataBean model = new Gson().fromJson(jsondata,
                                new TypeToken<MainDataBean>() {
                                }.getType());
                        // 成功
                        Log.e("ChatRoomSendMSG2", "onSuccess  =" + " josndata=" + jsondata + "  type=" + type + "  body=" + body);
                        if (type.equals("10")) {//发送聊天消息
                            NewsArray newsArraydata = new NewsArray();
                            newsArraydata.setBody(MyApplication.getInstance().getUserName() + ":" + model.getBody());
                            newsArraydata.setExpRank(MyApplication.getInstance().getUserRank());
                            newsArrays.add(newsArraydata);
                        } else if (type.equals("4")) {//发送进入房间消息
                            if (Welcome.length() > 0) {//房间公告
                                newslist.add(Welcome);//添加消息数据到列表
                                NewsArray newsArraydata = new NewsArray();
                                newsArraydata.setBody(Welcome);
                                newsArrays.add(newsArraydata);
                            }
                            NewsArray newsArraydata = new NewsArray();
                            newsArraydata.setBody("欢迎 " + model.getFromUser().getUsername() + " 进入房间");
                            newsArraydata.setExpRank(model.getFromUser().getExpRank());
                            newsArrays.add(newsArraydata);

                            NewsArray newsArraydata2 = new NewsArray();
                            newsArraydata2.setBody("官方公告：官方倡导绿色互动，请勿发布违法、低俗、暴力、广告等内容，禁止违规交易，违规者将被封禁账号。");
                            newsArrays.add(newsArraydata2);
                        } else if (type.equals("2")) {//发送收藏房间消息
//                            newslist.add(fromname + " 收藏了房间");//添加消息数据到列表

                            NewsArray newsArraydata = new NewsArray();
                            newsArraydata.setBody(fromname + " 收藏了房间");
                            newsArraydata.setExpRank(fromrank);
                            newsArrays.add(newsArraydata);
                        } else if (type.equals("8")) {//发送禁言消息
//                            newslist.add(fromname + " 被管理员禁言");//添加消息数据到列表

                            NewsArray newsArraydata = new NewsArray();
                            newsArraydata.setBody(fromname + " 被管理员禁言");
                            newsArraydata.setExpRank(fromrank);
                            newsArrays.add(newsArraydata);

                        } else if (type.equals("11")) {//发送送礼物消息
                            FindCacheRoomInfo();//刷新直播间信息
                            if (jsondata.length() > 0) {
//                                newslist.add(model.getFromUser().getUsername() + " 打赏 " + model.getToUser().getUsername() + " " + getGiftName(model.getGift().getGiftId()) + " X" + model.getGift().getCount());//添加消息数据到列表
                                NewsArray newsArraydata = new NewsArray();
                                newsArraydata.setBody(model.getFromUser().getUsername() + " 打赏 " + model.getToUser().getUsername() + " " + getGiftName(model.getGift().getGiftId()) + " X" + model.getGift().getCount());
                                newsArraydata.setExpRank(model.getFromUser().getExpRank());
                                newsArrays.add(newsArraydata);//设置公屏显示消息
                                if (model.getGift().getGiftId() != null) {//播放礼物 火箭
//                                    GiftAnimWindow(model.getGift().getGiftId());
                                    //队列播放
                                    final BaseSyncTask task1 = new BaseSyncTask() {
                                        @Override
                                        public void doTask() {
                                            Log.e("doTask", "task1");
                                            if (model.getGift().getFileIdentifier() != null) {
                                                PlaygiftAnim(model.getGift().getFileIdentifier());
                                            }
                                        }
                                    };
                                    TinySyncExecutor.getInstance().enqueue(task1);//添加队列
                                }
                            }
                        } else if (type.equals("36")) {//发送表情消息
                            Updatanews = false;
                            int giftindex = Integer.parseInt(model.getBody());//要播放的表情的索引
                            int mikenumber = -1;//播放表情的位置
//                            int mikenumber = Integer.parseInt(fromname) - 1;//播放表情的位置
                            for (int i = 0; i < mikeArray.size(); i++) {
                                if (mikeArray.get(i).getMikerId() != null) {
                                    if (mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {//自己在麦上
                                        mikenumber = i;
                                    }
                                }
                            }
//                            recyAdapter.PlayEmoji(giftindex, mikenumber);//播放表情   index 要播放的表情的索引  postion 播放表情的位置
                            RoomPlayemoji(giftindex, mikenumber);
                        }
                        if (roomrecyAdapter == null) {
                            roomrecyAdapter = new RoomNewsRecyAdapter(mContext, newsArrays);
                            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            layoutManager.setStackFromEnd(true);
                            newsRecyclerview.setLayoutManager(layoutManager);
                            newsRecyclerview.scrollToPosition(roomrecyAdapter.getItemCount() - 1);
                            roomrecyAdapter.setOnItemClickListener(mOnItemClickListener2);
                            newsRecyclerview.setAdapter(roomrecyAdapter);
                        } else {
                            if (Updatanews) {//是否需要刷新消息 针对收到云信消息但是没有添加消息数据 最后一条消息抖动
                                newsRecyclerview.scrollToPosition(roomrecyAdapter.getItemCount() - 1);
                                //单条插入 解决短时间快速刷新抖动问题
                                roomrecyAdapter.notifyItemInserted(roomrecyAdapter.getItemCount() - 1);//通知演示插入动画
                                roomrecyAdapter.notifyItemRangeChanged(roomrecyAdapter.getItemCount() - 1, roomrecyAdapter.getItemCount());//通知数据与界面重新绑定
                            } else {//
                                Updatanews = true;
                            }
//                            newsRecyclerview.scrollToPosition(roomrecyAdapter.getItemCount() - 1);
////                            roomrecyAdapter.notifyDataSetChanged();
//
//                            roomrecyAdapter.notifyItemInserted(roomrecyAdapter.getItemCount() - 1);//通知演示插入动画
//                            roomrecyAdapter.notifyItemRangeChanged(roomrecyAdapter.getItemCount() - 1, roomrecyAdapter.getItemCount());//通知数据与界面重新绑定
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Log.e("ChatRoomSendMSG2", "onFailed   code=" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Log.e("ChatRoomSendMSG2", "onException   exception=" + exception);
                    }
                });
    }


    @Subscribe
    public void onEventMainThread(TabCheckEvent event) {//popwindow 弹出提示
        Log.e("onEventMainRoom", "TabCheckEvent.getMsg()= " + event.getMsg());
        if (event.getMsg() != null) {
            if (event.getMsg().equals("sucess")) {
                FindCacheRoomInfo();//查询直播间信息 更新信息
            } else if (event.getMsg().equals("退出房间")) {
                finish();
            } else if (event.getMsg().equals("关闭弹窗")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRoomEggsWindow.dismiss();
                    }
                }, 100); // 延时0.1秒
            } else {
                if (event.getMsg().startsWith("提示")) {
                    String toast = event.getMsg().replace("提示", "");
                    Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
                }
//            else if (event.getMsg().startsWith("look")) {
//                String lookid = event.getMsg().replace("look", "");
//                if (lookid.equals(MyApplication.getInstance().getUserId())) {
//                    RoomLookInfoWindow("1", lookid, "2", -1);//在线人数查看用户信息 没有麦位postion
//                } else {
//                    RoomLookInfoWindow("2", lookid, "2", -1);//在线人数查看用户信息 没有麦位postion
//                }
//            }
                else if (event.getMsg().startsWith("表情")) {
//                    Log.e("发送表情", "表情索引=" + MyApplication.getInstance().getEmojiindex() + "  表情位置=" + MyApplication.getInstance().getMikeNumber());
//                    ChatRoomSendMSG2("36", MyApplication.getInstance().getEmojiindex(), MyApplication.getInstance().getMikeNumber(), "", "");//发送表情消息

                    MainDataBean mainDataBean = new MainDataBean();
                    mainDataBean.setBody(MyApplication.getInstance().getEmojiindex());
                    mainDataBean.setType("36");
                    UserData data = new UserData();
                    data.setExpRank(MyApplication.getInstance().getUserRank());
                    data.setPortraitPath(MyApplication.getInstance().getUserImg());
                    data.setUserId(MyApplication.getInstance().getUserId());
                    data.setUsername(MyApplication.getInstance().getUserName());
                    mainDataBean.setFromUser(data);
                    //对象转字符串
                    Gson gson = new Gson();
                    String jsondata = gson.toJson(mainDataBean);
                    ChatRoomSendMSG2("36", "", "", "", jsondata);

                } else if (event.getMsg().startsWith("取消管理员")) {
                    String RemoveManid = event.getMsg().replace("取消管理员", "");
                    FindCacheRoomInfo();
//                    ChatRoomSendMSG2("13", "你已被撤销管理员", RemoveManid, "", "");  //发送取消管理员消息  设置的对象更新房间信息

                    MainDataBean mainDataBean = new MainDataBean();
                    mainDataBean.setBody("你已被撤销管理员");
                    mainDataBean.setType("13");
                    UserData data = new UserData();
                    data.setExpRank(MyApplication.getInstance().getUserRank());
                    data.setPortraitPath(MyApplication.getInstance().getUserImg());
                    data.setUserId(MyApplication.getInstance().getUserId());
                    data.setUsername(MyApplication.getInstance().getUserName());
                    UserData data2 = new UserData();
                    data2.setUserId(RemoveManid);
                    mainDataBean.setFromUser(data);
                    mainDataBean.setToUser(data2);
                    //对象转字符串
                    Gson gson = new Gson();
                    String jsondata = gson.toJson(mainDataBean);
                    ChatRoomSendMSG2("13", "", "", "", jsondata);
                    Log.e("ChatRoomSendMSG212", "jsondata=" + jsondata);


                } else if (event.getMsg().startsWith("发送消息")) {
                    String sendnes = event.getMsg().replace("发送消息", "");
                    LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(sendnes, "****");//，然后判断result.getOperator()的值，如果为1，可以通过result.getContent()获取替换后的文本。
                    //  Android端检查结果类型：0 未命中，1 命中本地替换库，2 命中本地拦截库，3 命中服务端拦截库
                    Log.e("LocalAntiSpamResult", "getOperator=" + result.getOperator());
                    if (result.getOperator() == 2) {//敏感文字
                        Toast.makeText(mContext, "发送失败，兔语提醒您文明用语", Toast.LENGTH_LONG).show();
                    } else {
                        MainDataBean mainDataBean = new MainDataBean();
                        mainDataBean.setBody(sendnes);
                        mainDataBean.setType("10");
                        UserData data = new UserData();
                        data.setExpRank(MyApplication.getInstance().getUserRank());
                        data.setPortraitPath(MyApplication.getInstance().getUserImg());
                        data.setUserId(MyApplication.getInstance().getUserId());
                        data.setUsername(MyApplication.getInstance().getUserName());
                        mainDataBean.setFromUser(data);
                        //对象转字符串
                        Gson gson = new Gson();
                        String jsondata = gson.toJson(mainDataBean);
                        ChatRoomSendMSG2("10", "", "", "", jsondata);
                    }
                } else if (event.getMsg().startsWith("冲榜")) {
                    FindPersonalGift();//查询背包礼物列表
                    RoomGiftWindowAll("2", "1", giftArray, Roomid, 0, roomdata, pakagegiftArray);
                }
            }
        }
        if (event.getMsg1() != null && event.getMsg2() != null) {
            if (event.getMsg1().startsWith("look")) {
                String lookid = event.getMsg1().replace("look", "");
                Ismiker = event.getMsg2().replace("look", "");//是否在麦上

                if (lookid.equals(MyApplication.getInstance().getUserId())) {
                    RoomLookInfoWindow("1", lookid, "2", -1, isManager, "");//在线人数查看用户信息 没有麦位postion
                } else {
                    RoomLookInfoWindow("2", lookid, "2", -1, isManager, "");//在线人数查看用户信息 没有麦位postion
                }
            }
        }
    }


    @Subscribe//接收数组
    public void onEventMainThread(TabCheckEventList event) {
        if (event.getArray() != null && event.getArray().size() > 0) {//送礼物数据
            for (int i = 0; i < event.getArray().size(); i++) {
                ChatRoomSendMSG2("11", "", "", "", event.getArray().get(i));//发送礼物消息
                Log.e("onEventMainThread", "ChatRoomSendMSG2= " + event.getArray().get(i));
            }
        }
    }

    private MainRoomSetAdapter.OnItemClickListener mOnItemClickListener = new MainRoomSetAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {//麦位点击事件
            Log.e("onItemClick", "getMikerId=" + mikeArray.get(position).getMikerId() + "  isManager=" + isManager + "  getUserId=" + MyApplication.getInstance().getUserId() + "  getStatus=" + mikeArray.get(position).getStatus());
            if (mikeArray.get(position).getMikerId() == null) {//麦位没人
                if (isManager) {//管理员
                    RoomAdminSetWindow("1", position);//显示管理员设置列表
                } else {//普通人点空麦位
                    if (mikeArray.get(position).getStatus().equals("2")) {//麦位已锁
                        Log.e("MainRoomSetAdapter", "MainRoomSetAdapter");
                        EventBus.getDefault().post(new TabCheckEvent("提示" + "该麦位已锁"));
                    } else {
                        if (my_mike_type.getText().equals("我要上麦")) {//在麦上就不做操作
                            getLineForMike(mikeArray.get(position).getMikeId());//按麦位排麦
                        } else {
                            Toast.makeText(mContext, "你已经在麦上了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {//麦位有人
                if (mikeArray.get(position).getMikerId().equals(MyApplication.getInstance().getUserId())) { //自己
                    RoomLookInfoWindow("1", MyApplication.getInstance().getUserId(), "2", position, isManager, mikeArray.get(position).getStatus());//自己的麦位信息
                } else { //别人
                    LookMIkeUser = true;
                    LookPostion = position;
                    FindRoomUserInfo(mikeArray.get(position).getMikerId());//查询用户直播间内信息
                }
            }
        }
    };


    private RoomNewsRecyAdapter.OnItemClickListener mOnItemClickListener2 = new RoomNewsRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (newsArrays.get(position).getUserid() != null) {
                Log.e("onItemClick", "点击的消息item id=" + newsArrays.get(position).getUserid());
                if (!newsArrays.get(position).getUserid().equals(MyApplication.getInstance().getUserId())) {
                    if (isManager) {//管理员
                        RoomLookInfoWindowHost(newsArrays.get(position).getUserid(), "", isManager);
                    } else {
                        RoomLookInfoWindowHost(newsArrays.get(position).getUserid(), "", isManager);
                    }
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        returnActivityB = true;
        MyApplication.getInstance().setIsRoomto("");//设置私聊入口
        Log.e("MainRoomLive", "onResume");
        Log.e("音频模块", "onResume恢复");
//        //获取音频服务
//        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
//        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE , 50);//取消静音
        if (zegoAudioRoom != null) {
            zegoAudioRoom.resumeAudioModule();//  // 恢复音频模块
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//         Boolean ISOUT= isApplicationInBackground(mContext);//是否处于后台模式
//        if(ISOUT){
//            zegoAudioRoom.pauseAudioModule();//  // 暂停音频模块
//        }
//        Log.e("MainRoomLive", "ISOUT="+ISOUT);
        Log.e("MainRoomLive", "onPause");
        MyApplication.getInstance().setIsInRoom("true");
        MyApplication.getInstance().setRoomBack("roomback");
        MyApplication.getInstance().setExitRoomid(Roomid);
//        Log.e("onPause","    onPause="+validateMicAvailability());
//        if(validateMicAvailability()){//没有占用恢复音频模块
//            zegoAudioRoom.resumeAudioModule();//  // 恢复音频模块
//        }else {//占用了暂停音频模块
//            zegoAudioRoom.pauseAudioModule();//  // 暂停音频模块
//        }
//        zegoAudioRoom.pauseAudioModule();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(roominfo);
        Log.e("最小化", "jsonStr=" + jsonStr);
        MyApplication.getInstance().setExitRoomdata(jsonStr);
        Log.e("onEventMainThread", "2getExitRoomid= " + MyApplication.getInstance().getExitRoomid() + "  getExitRoomdata=" + MyApplication.getInstance().getExitRoomdata());
        EventBus.getDefault().post(new TabCheckEvent("最小化"));
//                                finish();
        MyApplication.getInstance().setMyRoomid(Roomid);//保存自己的房间号 下次进来判断
    }


    @Override
    protected void customTitleCenter(TextView titleCenter) {
//        titleCenter.setText("登陆");
    }

    public void PlaygiftAnim(String fileIdentifier) {
        if (fileIdentifier.equals("like")) {//播放礼物 玫瑰
            animationView.setImageAssetsFolder("img"); //java代码 设置路径
            animationView.setAnimation("like.json");
        } else if (fileIdentifier.equals("icecream")) {//播放礼物 情书
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("icecream.json");//设置动画文件
        } else if (fileIdentifier.equals("666")) {//播放礼物 666
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("666.json");
        } else if (fileIdentifier.equals("microphone")) {//播放礼物 么么哒
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("microphone.json");//设置动画文件
        } else if (fileIdentifier.equals("kiss")) {//播放礼物 丘比特
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("kiss.json");//设置动画文件
        } else if (fileIdentifier.equals("cupid")) {//播放礼物 520
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("cupid.json");//设置动画文件
        } else if (fileIdentifier.equals("520")) {//播放礼物 钻戒
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("520.json");//设置动画文件
        } else if (fileIdentifier.equals("glass_slipper")) {//播放礼物 一见倾心
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("glass_slipper.json");//设置动画文件
        } else if (fileIdentifier.equals("cannon")) {//播放礼物 火箭
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("cannon.json");
        } else if (fileIdentifier.equals("double_heart")) {//播放礼物 城堡
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("double_heart.json");//设置动画文件
        } else if (fileIdentifier.equals("flowermoon")) {//播放礼物 火箭
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("flowermoon.json");
        } else if (fileIdentifier.equals("rocket")) {//播放礼物 城堡
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("rocket.json");//设置动画文件
        } else if (fileIdentifier.equals("horse")) {//播放礼物 火箭
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("horse.json");
        } else if (fileIdentifier.equals("puppylove")) {//播放礼物 城堡
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("puppylove.json");//设置动画文件
        } else if (fileIdentifier.equals("castle")) {//播放礼物 城堡
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("castle.json");//设置动画文件
        } else if (fileIdentifier.equals("throne")) {//播放礼物 城堡
            animationView.setImageAssetsFolder("img");
            animationView.setAnimation("throne.json");//设置动画文件
        }


        animationView.playAnimation();
    }

    public void PlayEggs() {
        animation_eggs_view.setImageAssetsFolder("egg_images");
        animation_eggs_view.setAnimation("gold_eggs.json");//设置动画文件
        animation_eggs_view.playAnimation();
    }


    @Override
    protected void initEventListeners() {
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override//动画监听
            public void onAnimationStart(Animator animation) {
                animationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
                TinySyncExecutor.getInstance().finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mic_phone_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override //麦克风开关监听
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    enableMic(true);//打麦克风
                    for (int i = 0; i < mikeArray.size(); i++) {
                        if (mikeArray.get(i).getMikerId() != null) {
                            if (mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {
                                CancelCloseMike(mikeArray.get(i).getMikeId());//开麦操作
                            }
                        }

                    }
                } else {
                    enableMic(false);//关闭麦克风
                    for (int i = 0; i < mikeArray.size(); i++) {
                        if (mikeArray.get(i).getMikerId() != null) {
                            if (mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {
                                getCloseMike(mikeArray.get(i).getMikeId());//闭麦操作
                            }
                        }
                    }


                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnActivityB = true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        setTranslucentStatus(this);
        setStatusBarLightTheme(this, true);
        Log.e("MainRoomLive", "onCreate");

        //注册广播接收器
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.Record.broadcasereceiver.MYRECEIVER");
        registerReceiver(myReceiver, intentFilter);
        //因为这里需要注入Message，所以不能在AndroidManifest文件中静态注册广播接收器
        myReceiver.setMessage( this);
    }


    @OnClick({R.id.img_iv, R.id.room_follow_iv, R.id.animation_eggs_view, R.id.onlinenum_ll, R.id.main_rv, R.id.rowwheat_ll, R.id.setting_ll, R.id.chat_ll, R.id.gold_fl, R.id.exit_iv, R.id.share_iv, R.id.mai_ll, R.id.expression_ll, R.id.gift_ll, R.id.rule_ll, R.id.rank_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_iv://房主头像
                if (!roomdata.getHosterId().equals(MyApplication.getInstance().getUserId())) {
                    RoomLookInfoWindowHost(roomdata.getHosterId(), roomdata.getHosterName(), isManager);
                }//自己不能查看房主头像
                break;
//            case R.id.roompeonum_tv://在线人数
//                IsShowUPMike = false;//不显示抱用户上麦
//                OnlineType="normal";//打开在线人数类型  普通类型
//                FindOnlineAudience();//查询直播间在线人数
//                break;
            case R.id.onlinenum_ll://在线人数
                IsShowUPMike = false;//不显示抱用户上麦
                OnlineType = "normal";//打开在线人数类型  普通类型
                FindOnlineAudience();//查询直播间在线人数
                break;
            case R.id.room_follow_iv://收藏
                if (IsCollect) {
                    CancelCollectRoom();
                } else {
                    CollectRoom();
                }
                break;
            case R.id.exit_iv://退出
                ExitRoomWindow();

                break;
            case R.id.share_iv://分享
//                RoomShareWindow();
                break;
            case R.id.setting_ll://设置
                Log.e("setting_ll", "当前公屏状态" + roomdata.getShowChat());
                RoomSetingWindow(roomdata.getShowChat());
                break;
            case R.id.rule_ll://玩法说明
                RuleWindow(isManager, Description);
                break;
            case R.id.gold_fl://神豪
                lookgold = true;
                FindCacheRoomInfo();//查询直播间信息
                break;
            case R.id.rank_ll://贡献榜
                //TODO
                Intent intent = new Intent(mContext, MainRankActivity.class);
                intent.putExtra("Type", "Room");
                intent.putExtra("Roomid", Roomid);
                startActivity(intent);
                break;
            case R.id.mai_ll://上麦
                RoomUpmaiWindow();
                break;
            case R.id.rowwheat_ll://排麦
                MyApplication.getInstance().setMangerLink("");
//                FindLiner();
                //判断是否有老板位
                Boolean hasboss = false;
                for (int i = 0; i < mikeArray.size(); i++) {
                    if (mikeArray.get(i).getIsBoss().contains("true")) {
                        hasboss = true;
                        break;
                    }
                }
                RoomMikeLineWindow(Roomid, hasboss, isManager);
                break;
            case R.id.chat_ll://聊天
                if (roomdata.getShowChat().equals("false")) {
                    Toast.makeText(mContext, "当前公屏已关闭", Toast.LENGTH_SHORT).show();
                } else {
                    InPutWindow("");
                }
                break;
            case R.id.expression_ll://表情
                if (my_mike_type.getText().equals("我在麦上")) {
                    MyApplication.getInstance().setMikeNumber("");
                    MyApplication.getInstance().setEmojiindex("");
                    EmojiWindow(mikeArray);
                } else {
                    Toast.makeText(mContext, "上麦了在来发送吧", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gift_ll://礼物
                FindPersonalGift();//查询背包礼物列表
                RoomGiftWindowAll("2", "1", giftArray, Roomid, 0, roomdata, pakagegiftArray);
                break;
            case R.id.animation_eggs_view://砸蛋
                EggsWindow();
                break;


        }
    }

    private void EggsWindow() {//砸蛋弹出窗口
        mRoomEggsWindow = new RoomEggsWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
//                            case 4://取消
//                                mRoomEggsWindow.dismiss();
//                                break;
                        }
                    }
                }, Roomid);
        mRoomEggsWindow.setClippingEnabled(false);
        mRoomEggsWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    @SuppressLint("WrongConstant")
    private void InPutWindow(String text) {//
        mInPutWindow = new InPutWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mInPutWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {

                        }
                    }
                }, text);

//        mInPutWindow.setFocusable(true);
//        mInPutWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        mInPutWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        mInPutWindow.setClippingEnabled(false);
        mInPutWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    EditorCallback editorCallback2 = new EditorCallback() {
        @Override
        public void onCancel() {
            Toast.makeText(mContext, "cancel", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSubmit(String content) {
//            tv.setText(content);
            Log.e("EditorCallback", "content=" + content);
            if (roomdata.getIsForbid() != null && roomdata.getIsForbid().equals("true")) {//被禁言
                Toast.makeText(mContext, "您已经被禁言", Toast.LENGTH_SHORT).show();
            } else {
                LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(content, "****");//，然后判断result.getOperator()的值，如果为1，可以通过result.getContent()获取替换后的文本。
                //  Android端检查结果类型：0 未命中，1 命中本地替换库，2 命中本地拦截库，3 命中服务端拦截库
                Log.e("LocalAntiSpamResult", "getOperator=" + result.getOperator());
                ChatRoomSendMSG2("10", content, MyApplication.getInstance().getUserName(), "", "");//发送消息通知房间所有人
            }
        }

        @Override
        public void onAttached(final ViewGroup rootView) {
            final View flFaces = rootView.findViewById(R.id.fl_faces);
            rootView.findViewById(R.id.iv_face).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flFaces.getVisibility() == View.VISIBLE) {
                        flFaces.setVisibility(View.GONE);
                    } else {
                        flFaces.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    };


    @Override
    public void onCancel() {
        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmit(String content) {
//        tv.setText(content);
        Log.e("EditorCallback", "content=" + content);
    }

    @Override
    public void onAttached(ViewGroup rootView) {

    }


    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(
            final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }


    private void GiftAnimWindow(String giftid) {//播放礼物动画窗口
        mGiftAnimWindow = new GiftWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {

                    }
                }, giftid);
        mGiftAnimWindow.setClippingEnabled(false);
        mGiftAnimWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void RuleWindow(Boolean ismanger, final String Description) {//玩法说明弹出窗口
        mRoomruleWindow = new RoomruleWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomruleWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomruleWindow.dismiss();
                                break;
                            case 5://编辑
                                mRoomruleWindow.dismiss();
                                Intent intent = new Intent(mContext, RuleEditActivity.class);
                                intent.putExtra("Roomid", Roomid);
                                intent.putExtra("Description", Description);
                                startActivity(intent);
                                break;
                        }
                    }
                }, ismanger, Description);
        mRoomruleWindow.setClippingEnabled(false);
        mRoomruleWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
//        //修正后代码
//        mainRv.post(new Runnable() {
//            @Override
//            public void run() {
//                mRoomruleWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);
//
//            }
//        });
    }




    private void ExitRoomWindow() {//退出窗口
        mExitRoomWindow = new ExitRoomWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mExitRoomWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://最小化
                                mExitRoomWindow.dismiss();
//                                Log.e("云信退出房间", "YXRoomid=" + YXRoomid);
//                                NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, false);//注销监听
//                                NIMClient.getService(ChatRoomService.class).exitChatRoom(YXRoomid);//云信退出房间
                                MyApplication.getInstance().setExitRoomid(Roomid);
                                Gson gson = new Gson();
                                String jsonStr = gson.toJson(roominfo);
                                Log.e("最小化", "jsonStr=" + jsonStr);
                                MyApplication.getInstance().setExitRoomdata(jsonStr);
                                Log.e("onEventMainThread", "2getExitRoomid= " + MyApplication.getInstance().getExitRoomid() + "  getExitRoomdata=" + MyApplication.getInstance().getExitRoomdata());
                                EventBus.getDefault().post(new TabCheckEvent("最小化"));
//                                finish();
                                MyApplication.getInstance().setMyRoomid(Roomid);//保存自己的房间号 下次进来判断
                                moveTaskToBack(true);//singleTask模式会返回到桌面  singleInstance模式会返回主页面 但是会出现白屏问题
//                                onBackPressed();//调用键盘返回键 避免返回到桌面
                                returnActivityB = false;
                                Log.e("HasMainActivity","HasMainActivity="+HasMainActivity);
//                                if(HasMainActivity){
//                                    returnActivitySmall=true;
////                                    MyApplication.getInstance().setExitRoomid(Roomid);
////                                    Gson gson2 = new Gson();
////                                    String jsonStr2 = gson2.toJson(roominfo);
////                                    Log.e("最小化", "jsonStr=" + jsonStr2);
////                                    MyApplication.getInstance().setExitRoomdata(jsonStr2);
////                                    Log.e("onEventMainThread", "2getExitRoomid= " + MyApplication.getInstance().getExitRoomid() + "  getExitRoomdata=" + MyApplication.getInstance().getExitRoomdata());
////                                    EventBus.getDefault().post(new TabCheckEvent("最小化"));
//                                    MyApplication.getInstance().setMyRoomid(Roomid);//保存自己的房间号 下次进来判断
//                                    Intent intent=new Intent(mContext,MainActivity.class);
//                                    startActivity(intent);
//                                }
//                                if(HasMainActivity){
//                                    Intent intent=new Intent(mContext,MainActivity.class);
//                                    startActivity(intent);
//                                }
                                break;
                            case 5://退出
                                mExitRoomWindow.dismiss();
                                Log.e("云信退出房间", "YXRoomid" + YXRoomid);
                                NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, false);//注销监听
                                NIMClient.getService(ChatRoomService.class).exitChatRoom(YXRoomid);//云信退出房间
                                OutRoom();//清除即构相关信息
                                getQuitRoom();//退出房间
                                EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                                MyApplication.getInstance().setMaiRoomid("");
                                finish();
                                returnActivityB = false;
//                                if(HasMainActivity){
//                                    Intent intent=new Intent(mContext,MainActivity.class);
//                                    startActivity(intent);
//                                }
                                break;
                        }
                    }
                });
        mExitRoomWindow.setClippingEnabled(false);
        mExitRoomWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {

            ExitRoomWindow();//弹出提示框是否退出房间或者最小化
//            NIMClient.getService(ChatRoomService.class).exitChatRoom(YXRoomid);//云信退出房间
//            MyApplication.getInstance().setExitRoomid(Roomid);
//            Gson gson = new Gson();
//            String jsonStr = gson.toJson(roominfo);
//            MyApplication.getInstance().setExitRoomdata(jsonStr);
//            Log.e("onEventMainThread", "2getExitRoomid= " + MyApplication.getInstance().getExitRoomid() + "  getExitRoomdata=" + MyApplication.getInstance().getExitRoomdata());
//            EventBus.getDefault().post(new TabCheckEvent("最小化"));
////            finish();
//            moveTaskToBack(true);//singleTask模式会返回到桌面  singleInstance模式会返回主页面 但是会出现白屏问题
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void RoomShareWindow() {//分享
        mRoomShareWindow = new RoomShareWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomShareWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://发布到首页
                                mRoomShareWindow.dismiss();
                                break;
                            case 5://兔语好友
                                mRoomShareWindow.dismiss();
                                break;
                            case 6://通知房间粉丝
                                mRoomShareWindow.dismiss();
                                break;
                            case 7://微信
                                mRoomShareWindow.dismiss();
                                Log.e("微信分享", "http://ty.fengyugo.com/h5/h5/share.html" + "?number=" + Roomnumber + "&id=" + MyApplication.getInstance().getTuercode());
                                ShareUtils.shareWeb(MainRoomActivity.this, "http://ty.fengyugo.com/h5/h5/share.html" + "?number=" + Roomnumber + "&id=" + MyApplication.getInstance().getTuercode()
                                        , "邀请你进入超好玩的聊天室"
                                        , "恋爱脱单全新玩法,进入房间" + Roomnumber + ",遇见让你心动的声音!", Roompic, R.mipmap.rabblt_icon, SHARE_MEDIA.WEIXIN);
                                break;
                            case 8://朋友圈
                                mRoomShareWindow.dismiss();
                                ShareUtils.shareWeb(MainRoomActivity.this, "http://ty.fengyugo.com/h5/h5/share.html" + "?number=" + Roomnumber + "&id=" + MyApplication.getInstance().getTuercode()
                                        , "邀请你进入超好玩的聊天室"
                                        , "恋爱脱单全新玩法,进入房间" + Roomnumber + ",遇见让你心动的声音!", Roompic, R.mipmap.rabblt_icon, SHARE_MEDIA.WEIXIN_CIRCLE);
                                break;
                            case 9://QQ
                                mRoomShareWindow.dismiss();
                                ShareUtils.shareWeb(MainRoomActivity.this, "http://ty.fengyugo.com/h5/h5/share.html" + "?number=" + Roomnumber + "&id=" + MyApplication.getInstance().getTuercode()
                                        , "邀请你进入超好玩的聊天室"
                                        , "恋爱脱单全新玩法,进入房间" + Roomnumber + ",遇见让你心动的声音!", Roompic, R.mipmap.rabblt_icon, SHARE_MEDIA.QQ);
                                break;
                            case 10://QQ空间
                                mRoomShareWindow.dismiss();
                                ShareUtils.shareWeb(MainRoomActivity.this, "http://ty.fengyugo.com/h5/h5/share.html" + "?number=" + Roomnumber + "&id=" + MyApplication.getInstance().getTuercode()
                                        , "邀请你进入超好玩的聊天室"
                                        , "恋爱脱单全新玩法,进入房间" + Roomnumber + ",遇见让你心动的声音!", Roompic, R.mipmap.rabblt_icon, SHARE_MEDIA.QZONE);
                                break;
                            case 11://微博
                                mRoomShareWindow.dismiss();
                                ShareUtils.shareWeb(MainRoomActivity.this, "http://ty.fengyugo.com/h5/h5/share.html" + "?number=" + Roomnumber + "&id=" + MyApplication.getInstance().getTuercode()
                                        , "邀请你进入超好玩的聊天室"
                                        , "恋爱脱单全新玩法,进入房间" + Roomnumber + ",遇见让你心动的声音!", "", R.mipmap.rabblt_icon, SHARE_MEDIA.SINA);
                                Log.e("微博", "SINA");
                                break;
                            case 12://举报
                                mRoomShareWindow.dismiss();
                                Intent intent = new Intent(mContext, WebViewActivity2.class);//  http://ty.fengyugo.com/h5/h5/help.html
                                intent.putExtra("webview_title", "举报");
                                intent.putExtra("type", "room");
                                intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/report.html" + "?userId=" + MyApplication.getInstance().getUserId() + "&roomId=" + Roomid);
                                startActivity(intent);
                                break;
                            case 13://收藏
                                mRoomShareWindow.dismiss();
                                if (IsCollect) {
                                    CancelCollectRoom();//取消收藏房间
                                } else {
                                    CollectRoom();//收藏房间
                                }
                                break;

                        }
                    }
                });
        mRoomShareWindow.setClippingEnabled(false);
        mRoomShareWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    //友盟分享
//    public void qq(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
//        );
//    }
//
//    public void weiXin(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
//        );
//    }
//
//    public void weixinCircle(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
//        );
//    }
//
//    public void sina(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
//        );
//    }
//
//    public void Qzone(View view) {
//        ShareUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
//                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
//        );
//    }


    private void RoomUpmaiWindow() {//上麦
        mRoomUpmaiWindow = new RoomUpmaiWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomUpmaiWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomUpmaiWindow.dismiss();
                                break;
                            case 5://老板位
                                mRoomUpmaiWindow.dismiss();
                                getLineForMike2("boss");
                                break;
                            case 6://普通位
                                mRoomUpmaiWindow.dismiss();
                                getLineForMike2("normal");
                                break;

                        }
                    }
                });
        mRoomUpmaiWindow.setClippingEnabled(false);
        mRoomUpmaiWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void EmojiWindow(final List<MikeArray> mikeArray) {//表情
        mEmojiWindow = new EmojiWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mEmojiWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://点击管理按钮打开设置页面
                                mEmojiWindow.dismiss();
                                break;
                        }
                    }
                }, mikeArray);
        mEmojiWindow.setClippingEnabled(false);
        mEmojiWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    private void RoomGiftWindowAll(String clicktype, final String isman,
                                   List<MikeArray> giftArray, String roomid,
                                   final int position, MainDTO roomdata, List<MikeArray> pakagegiftArray) {//点击底部送礼页面
//        FindRoomUserInfo(mikeArray.get(position).getMikerId());//查询用户直播间内信息
        mRoomGiftWindowAll = new RoomGiftWindowAll(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomGiftWindowAll.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://点击管理按钮打开设置页面
                                mRoomGiftWindowAll.dismiss();
                                RoomAdminSetWindow("2", position);
                                break;
                            case 6://用户信息
                                RoomLookInfoWindow("2", mikeArray.get(position).getMikerId(), isman, position, isManager, mikeArray.get(position).getStatus());
                                break;
                        }
                    }
                }, clicktype, isman, mikeArray, giftArray, roomid, position, roomdata, isFollow, pakagegiftArray);
        mRoomGiftWindowAll.setClippingEnabled(false);
        mRoomGiftWindowAll.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mRoomGiftWindowAll.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void RoomLookInfoWindow(String isme, final String mikerid, String type, final int position, Boolean isManager, String mikestatus) {//礼物弹出窗口 点击用户头像查看用户信息
        FindRoomUserInfo(mikerid);//查询用户直播间内信息
        mRoomLookInfoWindow = new RoomLookInfoWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://
                                mRoomLookInfoWindow.dismiss();
                                break;
                            case 5://下麦旁听
                                mRoomLookInfoWindow.dismiss();
                                //麦位抱自己下麦
                                getPickDownForMike(mikeArray.get(position).getMikeId());
                                break;
                            case 6://换我上麦
                                mRoomLookInfoWindow.dismiss();
                                //先抱麦上的人下麦  在报自己上麦
                                getPickDownForMike(mikeArray.get(position).getMikeId());
                                getPickUpForMike(mikeArray.get(position).getMikeId(), MyApplication.getInstance().getUserId());
                                break;
                            case 7://设为闭麦位 取消闭麦位
                                mRoomLookInfoWindow.dismiss();
                                //0正常  1闭麦 2锁麦
                                if (mikeArray.get(position).getStatus().equals("1")) {//闭麦状态
                                    CancelCloseMike(mikeArray.get(position).getMikeId());//取消闭麦位
                                } else {//其他状态
                                    getCloseMike(mikeArray.get(position).getMikeId());//设为闭麦位
                                }
                                break;
                            case 8://抱他下麦
                                mRoomLookInfoWindow.dismiss();
                                getPickDownForMike(mikeArray.get(position).getMikeId());
                                break;
                            case 9://私聊
                                mRoomLookInfoWindow.dismiss();
                                //   根据用户id查询云信账号
                                if (position >= 0) {
                                    FindUserInfo(mikerid, mikeArray.get(position).getMikerName());
                                } else {
                                    FindUserInfo(mikerid, "");
                                }
                                break;
                            case 10://点击头像 进入个人主页
                                mRoomLookInfoWindow.dismiss();
                                Intent intent = new Intent(mContext, UserInfoctivity.class);//
                                intent.putExtra("userinfoid", mikerid);
                                intent.putExtra("type", "room");
                                startActivity(intent);
                                break;
                            case 11://管理
                                mRoomLookInfoWindow.dismiss();
                                LookID = mikerid;
                                RoomAdminSetWindow("2", -1);
                                break;
                            case 12://@Ta
                                mRoomLookInfoWindow.dismiss();
                                if (roomdata.getShowChat().equals("false")) {
                                    Toast.makeText(mContext, "当前公屏已关闭", Toast.LENGTH_SHORT).show();
                                } else {
                                    InPutWindow(LookName);//@ta
                                }
                                break;
                        }
                    }
                }, isme, mikerid, type, Ismiker, isManager, mikestatus);
        mRoomLookInfoWindow.setClippingEnabled(false);
        mRoomLookInfoWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    private void RoomLookInfoWindowHost(final String lookid, final String lookname, Boolean isManager) {// 点击房主头像查看用户信息  id  昵称     isManager自己是否是管理员
        FindRoomUserInfo(lookid);//查询用户直播间内信息
        mRoomLookInfoWindowRoomHost = new RoomLookInfoWindowRoomHost(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomLookInfoWindowRoomHost.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://关闭
                                mRoomLookInfoWindowRoomHost.dismiss();
                                break;
                            case 9://私聊
                                mRoomLookInfoWindowRoomHost.dismiss();
                                //   根据用户id查询云信账号
                                FindUserInfo(lookid, lookname);
                                break;
                            case 10://点击卡片头像 到个人主页
                                mRoomLookInfoWindowRoomHost.dismiss();
                                Intent intent = new Intent(mContext, UserInfoctivity.class);//
                                intent.putExtra("userinfoid", lookid);
                                intent.putExtra("type", "room");
                                startActivity(intent);
                                break;
                            case 11://点击卡片 管理
                                mRoomLookInfoWindowRoomHost.dismiss();
                                LookID = lookid;
                                RoomAdminSetWindow("2", -1);
                                break;
                            case 12://@Ta
                                mRoomLookInfoWindowRoomHost.dismiss();
                                if (roomdata.getShowChat().equals("false")) {
                                    Toast.makeText(mContext, "当前公屏已关闭", Toast.LENGTH_SHORT).show();
                                } else {
                                    InPutWindow(LookName);//@ta
                                }
                                break;
                        }
                    }
                }, lookid, isManager);
        mRoomLookInfoWindowRoomHost.setClippingEnabled(false);
        mRoomLookInfoWindowRoomHost.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置

    }


    private void RoomSetingWindow(final String showCharm) {//设置
        mRoomSetingWindow = new RoomSetingWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomSetingWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://基本信息
                                mRoomSetingWindow.dismiss();
                                Intent intent = new Intent(mContext, RoomBaseinfoActivity.class);
                                intent.putExtra("roominfo", (Serializable) roomdata);//传递房间数据
                                intent.putExtra("type", "room");
                                startActivity(intent);
                                break;
                            case 5://房间背景
                                mRoomSetingWindow.dismiss();
                                RoomBGWindow(datalist, Roomid);
                                break;
                            case 6://背景音乐
                                mRoomSetingWindow.dismiss();

                                break;
                            case 7://在线人数
                                mRoomSetingWindow.dismiss();
                                IsShowUPMike = false;//不显示抱用户上麦
                                OnlineType = "normal";//打开在线人数类型  普通类型
                                FindOnlineAudience();//查询直播间在线人数
                                break;
                            case 8://管理员
                                mRoomSetingWindow.dismiss();
                                FindManager();
                                break;
                            case 9://公屏开关
                                mRoomSetingWindow.dismiss();
                                if (showCharm.equals("true")) {
                                    CancelShowChat(); //关闭公屏
                                } else {
                                    ShowChat();  //打开公屏
                                    //清空公屏消息
                                    newsArrays.clear();
                                    if (roomrecyAdapter != null) {
                                        roomrecyAdapter.notifyDataSetChanged();
                                    }
                                    newsRecyclerview.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 10://心动值统计
                                mRoomSetingWindow.dismiss();
                                RoomValueWindow(roomdata.getShowCharm());
                                break;
                        }
                    }
                }, showCharm);
        mRoomSetingWindow.setClippingEnabled(false);
        mRoomSetingWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    private void RoomAdminSetWindow(String type, final int index
    ) {//管理员点击空麦位和有人麦位弹窗  type=1 空麦位 type=2有人的麦位
//        Log.e("mRoomAdminSetWindow", "getIsManager()=" + mikeArray.get(index).getIsManager() + "   index=" + index);
        mRoomAdminSetWindow = new RoomAdminSetWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomAdminSetWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomAdminSetWindow.dismiss();
                                break;
                            case 5://上麦
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    getPickUpForMike(mikeArray.get(index).getMikeId(), MyApplication.getInstance().getUserId());
                                }
                                break;
                            case 6://抱用户上麦
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    if (mikeArray.get(index).getMikerPortraitPath() == null) {
                                        //麦位没人打开在线人数列表
                                        IsShowUPMike = true;//显示抱用户上麦
                                        OnlineType = "spapel";//打开在线人数类型  普通类型
                                        FindOnlineAudience();
                                        MyApplication.getInstance().setUpMikeid(mikeArray.get(index).getMikeId());
                                    }
                                }
                                break;
                            case 7://排麦上麦
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    if (mikeArray.get(index).getNeedLine().equals("true")) {
                                        getCancelSetLineMike(mikeArray.get(index).getMikeId());
                                    } else {
                                        getSetLineMike(mikeArray.get(index).getMikeId());
                                    }
                                }
                                break;
                            case 8://设置老板位
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    if (mikeArray.get(index).getIsBoss().equals("true")) {
                                        getCancelSetBossMike(mikeArray.get(index).getMikeId());
                                    } else {
                                        getSetBossMike(mikeArray.get(index).getMikeId());
                                    }
                                }
                                break;
                            case 9://设置主持位
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    if (mikeArray.get(index).getIsReceptionist().equals("true")) {
                                        getCancelSetReceptionistMike(mikeArray.get(index).getMikeId());
                                    } else {
                                        getSetReceptionistMike(mikeArray.get(index).getMikeId());
                                    }
                                }
                                break;
                            case 10://锁麦
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    if (mikeArray.get(index).getStatus().equals("2")) {//锁麦状态
                                        getCancelLockMike(mikeArray.get(index).getMikeId());
                                    } else if (mikeArray.get(index).getStatus().equals("0")) {//正常状态
                                        getLockMike(mikeArray.get(index).getMikeId());
                                    }
                                }
                                break;
                            case 11://设为管理员
                                mRoomAdminSetWindow.dismiss();
                                if (index >= 0) {
                                    if (mikeArray.get(index).getIsManager() != null) {
                                        if (mikeArray.get(index).getIsManager().equals("true")) {
                                            DeleteManager(mikeArray.get(index).getMikerId());   //取消管理员
                                        } else {
                                            AddManager(mikeArray.get(index).getMikerId());  //设置管理员
                                        }
                                    }
                                } else {
                                    Log.e("查看当前人员", "Lookmanger=" + Lookmanger);
                                    if (Lookmanger != null) {
                                        if (Lookmanger.equals("true")) {
                                            DeleteManager(LookID);   //取消管理员
                                        } else {
                                            AddManager(LookID);  //设置管理员
                                        }
                                    }
                                }
                                break;
                            case 12://禁言
                                if (index >= 0) {
                                    CannotTalkname = mikeArray.get(index).getMikerName();
                                    AddForbider(mikeArray.get(index).getMikerId());
                                } else {
                                    CannotTalkname = LookName;
                                    AddForbider(LookID);
                                }
                                mRoomAdminSetWindow.dismiss();
                                break;
                            case 14://从房间中拉黑
                                if (index >= 0) {
                                    AddBlacker(mikeArray.get(index).getMikerId());
                                } else {
                                    AddBlacker(LookID);
                                }
                                mRoomAdminSetWindow.dismiss();
                                break;
                        }
                    }
                }, type, index, mikeArray);
        mRoomAdminSetWindow.setClippingEnabled(false);
        mRoomAdminSetWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void RoomGoldWindow(String imgpath, String goldname, String goldtime) {//神豪弹出窗口
        mRoomGoldWindow = new RoomGoldWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomGoldWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://点击神豪位头像
                                mRoomGoldWindow.dismiss();
                                if (!roomdata.getRicherId().equals(MyApplication.getInstance().getUserId())) {
                                    RoomLookInfoWindowHost(roomdata.getRicherId(), roomdata.getRicherName(), isManager);
                                } else {
                                    Intent intent = new Intent(mContext, UserInfoctivity.class);//
                                    intent.putExtra("userinfoid", MyApplication.getInstance().getUserId());
                                    intent.putExtra("type", "room");
                                    startActivity(intent);
                                }//自己不能查看房主头像
                                break;
                        }
                    }
                }, imgpath, goldname, goldtime);
        mRoomGoldWindow.setClippingEnabled(false);
        mRoomGoldWindow.showAtLocation(mainRv, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void PeoWindow(String type, String roletype, List<DataList> datalist, Boolean
            IsShowUPMike, Boolean isManager, String onlineType) {//在线人数列表弹出窗口，排麦人数列表窗口 公用
        mRoomPeoWindow = new RoomPeoWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        mRoomPeoWindow.dismiss();
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomPeoWindow.dismiss();
                                break;

                        }
                    }
                }, type, roletype, datalist, IsShowUPMike, isManager, onlineType);
        mRoomPeoWindow.setClippingEnabled(false);
        mRoomPeoWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    private void RoomBGWindow(List<DataList> datalist, String roomid) {//房间背景图片
        mRoomBGWindow = new RoomBGWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomBGWindow.dismiss();
//                                //执行更换背景 image 方法
//                                mainBgIv.setImageResource(R.mipmap.scroll_bg);
//                                if (MyApplication.getInstance().getRoombg() != null) {
//                                Glide.with(mContext)
//                                        .load(MyApplication.getInstance().getRoombg())
//                                        .apply(new RequestOptions().placeholder(R.mipmap.room_main_bg).error(R.mipmap.room_main_bg).dontAnimate())
//                                        .into(mainBgIv);
//                                Log.e("RoomBGWindow", "getImgPath=" + MyApplication.getInstance().getRoombg());
//                            }
                                break;

                        }
                    }
                }, datalist, roomid);
        mRoomBGWindow.setClippingEnabled(false);
        mRoomBGWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    private void RoomAdminWindow(String roomid) {//房间管理员
        mRoomAdminWindow = new RoomAdminWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomAdminWindow.dismiss();
                                break;
                        }
                    }
                }, roomid);
        mRoomAdminWindow.setClippingEnabled(false);
//        mRoomValueWindow.setAnimationStyle(R.style.pop_animation);//动画效果
        mRoomAdminWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    private void RoomMikeLineWindow(String roomid, Boolean hasboos, Boolean isManager) {//房间管理员
        mRoomMikeLineWindow = new RoomMikeLineWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomMikeLineWindow.dismiss();
                                break;
                        }
                    }
                }, roomid, hasboos, isManager);
        mRoomMikeLineWindow.setClippingEnabled(false);
        mRoomMikeLineWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    private void RoomValueWindow(String showcharm) {//房间心动值统计
        mRoomValueWindow = new RoomValueWindow(MainRoomActivity.this,
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mRoomValueWindow.dismiss();
                                break;
                            case 5://统计清零
                                mRoomValueWindow.dismiss();
                                ClearCharm();
                                break;
                            case 6://心动值状态
                                mRoomValueWindow.dismiss();
                                if (roomdata.getShowCharm().equals("true")) {
                                    CancelShowCharm();
                                } else {
                                    ShowCharm();
                                }
                                break;

                        }
                    }
                }, showcharm);
        mRoomValueWindow.setClippingEnabled(false);
//        mRoomValueWindow.setAnimationStyle(R.style.pop_animation);//动画效果
        mRoomValueWindow.showAtLocation(mainRv, Gravity.BOTTOM, 0, 0);//或者显示在指定父布局上边的指定位置
    }


    public void getQuitRoom() {//退出房间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.QuitRoom, map);
    }

    public void getBackgroundImgPath() {//查询直播间背景
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindBackgroundImgPath, map);
    }

    public void ShowChat() {//打开公屏
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("roomId", Roomid);
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.ShowChat, map);
    }

    public void CancelShowChat() {//关闭公屏
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.CancelShowChat, map);
    }

    public void getFindRoomInfo() {//进入直播间查询信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        if (Roompass.length() > 0) {//有密码才传这个字段
            map.put("password", Roompass);
        }
        postRequest(RetrofitService.FindRoomInfo, map);
    }

    public void FindCacheRoomInfo() {//进入直播间查询缓存信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.FindCacheRoomInfo, map);
    }


    public void DeleteForbider(String forbiderId) {//移除禁言
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("forbiderId", forbiderId);
        postRequest(RetrofitService.DeleteForbider, map);
    }

    public void AddForbider(String forbiderId) {//禁言
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("forbiderId", forbiderId);
        postRequest(RetrofitService.AddForbider, map);
    }

    public void FindRoomUserInfo(String infoUserId) {//查询直播间内用户信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("infoUserId", infoUserId);
        postRequest(RetrofitService.FindRoomUserInfo, map);
    }


    public void getSetBossMike(String mikeId) {//设置老板位
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);
        postRequest(RetrofitService.SetBossMike, map);
    }

    public void getCancelSetBossMike(String mikeId) {//取消设置老板位
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);
        postRequest(RetrofitService.CancelSetBossMike, map);
    }

    public void getSetReceptionistMike(String mikeId) {//设置主持位
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);
        postRequest(RetrofitService.SetReceptionistMike, map);
    }

    public void getCancelSetReceptionistMike(String mikeId) {//取消设置主持位
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);
        postRequest(CancelSetReceptionistMike, map);
    }

    public void getPickUpForMike(String mikeId, String linerId) {//抱人上麦操作
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);
        map.put("linerId", linerId);//被抱上麦的用户ID
        postRequest(RetrofitService.PickUpForMike, map);
    }

    public void getPickDownForMike(String mikeId) {//抱人下麦操作
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);
        postRequest(RetrofitService.PickDownForMike, map);
    }


    public void getLineForMike(String mikeId) {//直播间点击麦位排麦
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.LineForMike, map);
    }


    public void getLineForMike2(String type) {//直播间按麦位类型排麦
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("type", type);//麦位类型 normal/boss
        postRequest(RetrofitService.LineForMike, map);
    }

    public void getCloseMike(String mikeId) {//闭麦
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.CloseMike, map);
    }


    public void CancelCloseMike(String mikeId) {//取消闭麦
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.CancelCloseMike, map);
    }


    public void getLockMike(String mikeId) {//锁麦
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.LockMike, map);
    }

    public void getCancelLockMike(String mikeId) {//取消锁麦
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.CancelLockMike, map);
    }

    public void getSetLineMike(String mikeId) {//给麦位设置排队操作
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.SetLineMike, map);
    }

    public void getCancelSetLineMike(String mikeId) {//取消麦位设置排队操作
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("mikeId", mikeId);//麦位ID
        postRequest(RetrofitService.CancelSetLineMike, map);
    }


    public void EditRoomBGI() {//修改直播间背景
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("imageId", "");
        postRequest(RetrofitService.EditRoomBGI, map);
    }

    public void CollectRoom() {//收藏直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.CollectRoom, map);
    }

    public void CancelCollectRoom() {//取消收藏直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.CancelCollectRoom, map);
    }


    public void ClearCharm() {//心动值清零
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.ClearCharm, map);
    }

    public void ShowCharm() {//展示心动值
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.ShowCharm, map);
    }

    public void CancelShowCharm() {//不展示心动值
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.CancelShowCharm, map);
    }

    public void FindOnlineAudience() {//查询直播间观众
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("page", "0");
        postRequest(RetrofitService.FindOnlineAudience, map);
    }

    public void ConnectRoom() {//设置心跳连接
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        postRequest(RetrofitService.ConnectRoom, map);
    }


    public void FindLiner() {//查询直播间排麦人员
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("page", "0");
        postRequest(RetrofitService.FindLiner, map);
    }

    public void FindBlacker() {//查询直播间黑名单
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("page", "0");
        postRequest(RetrofitService.FindBlacker, map);
    }

    public void FindManager() {//查询直播间管理员
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("page", "0");
        postRequest(RetrofitService.FindManager, map);
    }

    public void FindGift() {//查询礼物
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindGift, map);
    }

    public void FindPersonalGift() {//查询背包礼物
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindPersonalGift, map);
    }

    public void AddManager(String managerId) {//设置管理员
        Mangerid = managerId;
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("managerId", managerId);
        Log.e("AddManager", "roomId=" + Roomid + "  managerId=" + managerId);
        postRequest(RetrofitService.AddManager, map);
    }

    public void DeleteManager(String managerId) {//取消设置管理员
        Mangerid = managerId;
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("managerId", managerId);
        Log.e("DeleteManager", "roomId=" + Roomid + "  managerId=" + managerId);
        postRequest(RetrofitService.DeleteManager, map);
    }


    public void AddBlacker(String blackerId) {//从房间拉黑
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("blackerId", blackerId);
        takeblackerId = blackerId;
        Log.e("AddManager", "roomId=" + Roomid + "  blackerId=" + blackerId);
        postRequest(RetrofitService.AddBlacker, map);
    }

    public void FindUserInfo(String infoUserId, String username) {
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", infoUserId);
        map.put("username", username);
        Log.e("FindUserInfo", "infoUserId=" + infoUserId + "  username=" + username);
        postRequest(RetrofitService.FindUserInfo, map);
    }

    private void FollowUser(String followId) {//关注用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("followId", followId);
        postRequest(RetrofitService.FollowUser, map);
    }

    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String
            result, String url) {
        super.onCalllBack(call, response, result, url);
        Log.e("onCalllBack", "url=" + url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindBackgroundImgPath)) {//查询直播间背景
            Log.e("FindBackgroundImgPath", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            if (mMainArrayModel.getCode().equals("1")) {
                if (mMainArrayModel.getData() != null) {
                    if (mMainArrayModel.getData().size() > 0) {
//                        Glide.with(mContext)
//                                .load(mMainArrayModel.getData().get(0).getImgPath())
//                                .apply(new RequestOptions().placeholder(R.mipmap.room_main_bg).error(R.mipmap.room_main_bg).dontAnimate())
//                                .into(mainBgIv);//默认设置第一个图片为背景图
                        for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                            datalist.add(mMainArrayModel.getData().get(i));
                        }
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
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindRoomInfo)) {//进入直播间查询信息
            Log.e("FindRoomInfo", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    MyRoleType = mMainModel.getData().getRoleType();//获取权限类型
                    roomdata = mMainModel.getData();
                    if (mMainModel.getData().getIsCollect().equals("true")) {
                        IsCollect = true;
                        room_follow_iv.setImageResource(R.mipmap.icon_collect_no);
                    } else {
                        IsCollect = false;
                        room_follow_iv.setImageResource(R.mipmap.icon_collect_full);
                    }
                    if (mMainModel.getData().getShowChat().equals("true")) {//公屏的显示
                        newsRecyclerview.setVisibility(View.VISIBLE);
                    } else {//不显示
//                        newsRecyclerview.setVisibility(View.INVISIBLE);
                    }
                    Glide.with(mContext)
                            .load(mMainModel.getData().getHosterPortraitPath())
                            .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                            .into(imgIv);
                    Roompic = mMainModel.getData().getHosterPortraitPath();
                    roomidTv.setText("房间号:" + mMainModel.getData().getNumber());
                    Roomnumber = mMainModel.getData().getNumber();
                    roomname.setText(mMainModel.getData().getRoomName());
                    roomtypeTv.setText(mMainModel.getData().getClassifyName());
                    roompeonumTv.setText("在线:" + mMainModel.getData().getOnlineCount() + ">");
                    if (mMainModel.getData().getDescription() != null && mMainModel.getData().getDescription().length() > 0) {
                        Description = mMainModel.getData().getDescription();
                    }
                    if (mMainModel.getData().getWelcomeWord() != null && mMainModel.getData().getDescription().length() > 0) {
                        Welcome = mMainModel.getData().getWelcomeWord();
                    }
                    if (mMainModel.getData().getBackgroundImgPath() != null) {
                        Glide.with(mContext)
                                .load(mMainModel.getData().getBackgroundImgPath())
                                .apply(new RequestOptions().placeholder(R.mipmap.room_main_bg).error(R.mipmap.room_main_bg).dontAnimate())
                                .into(mainBgIv);
                        MyApplication.getInstance().setRoombg(mMainModel.getData().getBackgroundImgPath());
                    }
                    if (mMainModel.getData().getYunXinRoomId() != null) {
//                        EnterChatRoom(mMainModel.getData().getYunXinRoomId());
//                        MyApplication.getInstance().setYunXinRoomId(mMainModel.getData().getYunXinRoomId());
                        //进入云信聊天室
                        if (!IsEnterRoom) {
                            EnterChatRoom(mMainModel.getData().getYunXinRoomId());
                            MyApplication.getInstance().setYunXinRoomId(mMainModel.getData().getYunXinRoomId());
                            Log.e("EnterChatRoom ", "IsEnterRoom=" + IsEnterRoom + " 没有进入聊天室");
                        } else {
                            Log.e("EnterChatRoom ", "IsEnterRoom=" + IsEnterRoom + "   已经进入过聊天室");
                        }
                    }
                    mMainModel.getData().getIdentityStatus();//0    1闭麦   2锁麦

                    //  private String  roleType;// 0  房主  1 管理员  2 普通用户
                    if (mMainModel.getData().getRoleType() != null) {
                        if (mMainModel.getData().getRoleType().equals("1")) {
                            isManager = true;//是管理员
                            setting_ll.setVisibility(View.VISIBLE);
                        } else if (mMainModel.getData().getRoleType().equals("0")) {
                            isManager = true;//不是管理员 是房主
                            setting_ll.setVisibility(View.VISIBLE);
                        } else {
                            isManager = false;//不是管理员
                            setting_ll.setVisibility(View.GONE);
                        }
                    }
                    if (mMainModel.getData().getRicherName() == null) {
                        gold_name_tv.setText("虚位以待");
                    } else {
                        gold_name_tv.setText(mMainModel.getData().getRicherName());
                    }
                    if (mMainModel.getData().getRicherPortraitPath() != null) {
                        Glide.with(mContext)
                                .load(mMainModel.getData().getRicherPortraitPath())
                                .apply(new RequestOptions().placeholder(R.mipmap.boos_1).error(R.mipmap.boos_1).dontAnimate())
                                .into(imggoldIv);
                    } else {
                        imggoldIv.setImageResource(R.mipmap.boos_1);
                    }
                    if (lookgold) {
                        lookgold = false;
                        if (mMainModel.getData().getRicherName() != null && mMainModel.getData().getRicherName().length() > 0 && mMainModel.getData().getSurplusTime() != null) {
                            RoomGoldWindow(mMainModel.getData().getRicherPortraitPath(), mMainModel.getData().getRicherName(), mMainModel.getData().getSurplusTime());
                        } else {
//                            Toast.makeText(mContext, "当前房间暂无神豪", Toast.LENGTH_SHORT).show();
                            RoomGoldWindow(null, null, null);
                        }
                    }
                    mikeArray.clear();
                    my_mike_type.setText("我要上麦");
                    if (mMainModel.getData().getMikeArray() != null && mMainModel.getData().getMikeArray().size() == 8) {
                        for (int i = 0; i < mMainModel.getData().getMikeArray().size(); i++) {
                            mikeArray.add(mMainModel.getData().getMikeArray().get(i));//保存用户信息
                            if (mikeArray.get(i).getMikerId() != null) {
                                if (mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {//自己在麦上
                                    my_mike_type.setText("我在麦上");

                                    //先不管静音
                                    if (mikeArray.get(i).getStatus().equals("1")) {//闭麦
                                        OpenMick = false;//闭麦状态
//                                        mic_phone_cb.setChecked(false);
//                                        enableMic(false);
                                    } else {
                                        OpenMick = true;//非闭麦状态
//                                        mic_phone_cb.setChecked(true);
//                                        enableMic(true);
                                    }
                                }
                            }
                        }
                        recyAdapter = new MainRoomSetAdapter(mContext, mMainModel.getData().getMikeArray());
                        setRecyclerview.setAdapter(recyAdapter);
                        recyAdapter.setOnItemClickListener(mOnItemClickListener);
                        Log.e("getShowCharm", "getShowCharm=" + mMainModel.getData().getShowCharm());
                        if (mMainModel.getData().getShowCharm().equals("true")) {
                            if (mMainModel.getData().getHosterCharm() != null) {
                                float num1 = Float.parseFloat(mMainModel.getData().getHosterCharm());
                                if (num1 >= 10000) {
                                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                                    String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
                                    numsTv.setText(strPrice + "W");//心动值 //保留两位小数
                                } else {
                                    numsTv.setText(mMainModel.getData().getHosterCharm());//心动值
                                }
                            }
                            cardiacLl.setVisibility(View.VISIBLE);//显示房主心动值
                            hosterTv.setVisibility(View.GONE);//不显示房主
                            recyAdapter.changetShowcharm(true);
                        } else {
                            hosterTv.setVisibility(View.VISIBLE);
                            cardiacLl.setVisibility(View.GONE);
                            recyAdapter.changetShowcharm(false);
                        }
                        recyAdapter.notifyDataSetChanged();
//                        if (my_mike_type.getText().equals("我在麦上")) {
//                            my_mike_type.setVisibility(View.GONE);
//                            mic_phone_cb.setVisibility(View.VISIBLE);
//                            if (IsStartPush) {
//                                //已经在发布直播
//                                if(OpenMick){
//                                    mic_phone_cb.setChecked(true);
//                                        enableMic(true);
//                                }else {
//                                    mic_phone_cb.setChecked(false);
//                                    enableMic(false);
//                                }
//                            } else {
//                                zegoAudioRoom.startPublish();//开始发布直播
//                                builtinSpeakerOn(true);
////                                enableMic(true);
//                                IsStartPush = true;
//                                if(OpenMick){
//                                    mic_phone_cb.setChecked(true);
//                                    enableMic(true);
//                                }else {
//                                    mic_phone_cb.setChecked(false);
//                                    enableMic(false);
//                                }
//                            }
//                        } else {//不在麦上
//                            mic_phone_cb.setChecked(false);
//                            enableMic(false);
//                            enableMic(false);
//                            my_mike_type.setVisibility(View.VISIBLE);
//                            mic_phone_cb.setVisibility(View.GONE);
////                            //todo  不取消发布 只是不能说话
//                            zegoAudioRoom.stopPublish();//取消发布直播
//                        }
                        //todo---------------------------------------------------------------------------

                        if (my_mike_type.getText().equals("我在麦上")) {
//                            mai_ll.setBackgroundResource(R.drawable.upmai_bg);
                            my_mike_type.setVisibility(View.GONE);
                            mic_phone_cb.setVisibility(View.VISIBLE);
                            if (IsStartPush) {
                                Log.e("我的麦状态", "已经在发布直播");
                            } else {// 发布直播
                                zegoAudioRoom.startPublish();//开始发布直播
                                IsStartPush = true;
                                Log.e("我的麦状态", "开始发布直播");
                            }
                        } else {//不再麦上
//                            mai_ll.setBackgroundResource(R.mipmap.btn_microphone_user);
                            my_mike_type.setVisibility(View.VISIBLE);
                            mic_phone_cb.setVisibility(View.GONE);
                            if (IsStartPush) {
                                zegoAudioRoom.stopPublish();//取消发布直播
                                IsStartPush = false;
                                Log.e("我的麦状态", "取消发布直播");
                            } else {
                                Log.e("我的麦状态", "已经取消发布直播");
                            }
                        }
                        if (OpenMick) {//如果正常状态
                            mic_phone_cb.setChecked(true);
                        } else {//闭麦状态
                            mic_phone_cb.setChecked(false);
                        }

                    }
                    //贡献榜数据
                    if (mMainModel.getData().getRankingArray() != null) {
                        if (mMainModel.getData().getRankingArray().size() > 0) {
                            if (mMainModel.getData().getRankingArray().size() == 1) {
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv1);
//                                rankIv1.setVisibility(View.VISIBLE);
//                                rankIv2.setVisibility(View.GONE);
//                                rankIv3.setVisibility(View.GONE);

                                rank_rl_gx1.setVisibility(View.VISIBLE);
                                rank_rl_gx2.setVisibility(View.GONE);
                                rank_rl_gx3.setVisibility(View.GONE);

                            } else if (mMainModel.getData().getRankingArray().size() == 2) {
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv1);
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(1))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv2);
//                                rankIv1.setVisibility(View.VISIBLE);
//                                rankIv2.setVisibility(View.VISIBLE);
//                                rankIv3.setVisibility(View.GONE);

                                rank_rl_gx1.setVisibility(View.VISIBLE);
                                rank_rl_gx2.setVisibility(View.VISIBLE);
                                rank_rl_gx3.setVisibility(View.GONE);
                            } else if (mMainModel.getData().getRankingArray().size() == 3) {
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv1);
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(1))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv2);
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(2))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv3);
//                                rankIv1.setVisibility(View.VISIBLE);
//                                rankIv2.setVisibility(View.VISIBLE);
//                                rankIv3.setVisibility(View.VISIBLE);

                                rank_rl_gx1.setVisibility(View.VISIBLE);
                                rank_rl_gx2.setVisibility(View.VISIBLE);
                                rank_rl_gx3.setVisibility(View.VISIBLE);
                            }
                            rank_tv.setText("");
                        } else {
                            rank_tv.setText("排行榜");
//                            rankIv1.setVisibility(View.GONE);
//                            rankIv2.setVisibility(View.GONE);
//                            rankIv3.setVisibility(View.GONE);
                            rank_rl_gx1.setVisibility(View.GONE);
                            rank_rl_gx2.setVisibility(View.GONE);
                            rank_rl_gx3.setVisibility(View.GONE);
                        }
                    } else {
                        rank_tv.setText("排行榜");
//                        rankIv1.setVisibility(View.GONE);
//                        rankIv2.setVisibility(View.GONE);
//                        rankIv3.setVisibility(View.GONE);

                        rank_rl_gx1.setVisibility(View.GONE);
                        rank_rl_gx2.setVisibility(View.GONE);
                        rank_rl_gx3.setVisibility(View.GONE);
                    }
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindRoomUserInfo)) {//查询直播间内用户信息
            Log.e("FindRoomUserInfo", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                Lookmanger = mMainModel.getData().getIsManager();//查看的用户是否是管理员
                isFollow = mMainModel.getData().getFollow();
                if (Lookmanger.equals("true")) {
                    MyApplication.getInstance().setLookforManger(Lookmanger);
                } else {
                    MyApplication.getInstance().setLookforManger(Lookmanger);
                }
                LookName = mMainModel.getData().getUsername();//查看的用户的昵称
                if (LookMIkeUser) {//查看麦上非自己用户信息
                    if (isManager) {//管理员
                        FindPersonalGift();//查询背包礼物列表
                        RoomGiftWindowAll("1", "1", giftArray, Roomid, LookPostion, roomdata, pakagegiftArray);
                    } else {//普通
                        FindPersonalGift();//查询背包礼物列表
                        RoomGiftWindowAll("1", "2", giftArray, Roomid, LookPostion, roomdata, pakagegiftArray);
                    }
                    LookMIkeUser = false;
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindCacheRoomInfo)) {//查询直播间缓存信息
            Log.e("FindCacheRoomInfo", "result=" + result);
            MainModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    roomdata = mMainModel.getData();
                    if (mMainModel.getData().getIsCollect() != null) {
                        if (mMainModel.getData().getIsCollect().equals("true")) {
                            IsCollect = true;
                            room_follow_iv.setImageResource(R.mipmap.icon_collect_no);
                        } else {
                            IsCollect = false;
                            room_follow_iv.setImageResource(R.mipmap.icon_collect_full);
                        }
                    }
                    if (mMainModel.getData().getShowChat().equals("true")) {//公屏的显示
                        newsRecyclerview.setVisibility(View.VISIBLE);
                    } else {//不显示
//                        newsRecyclerview.setVisibility(View.INVISIBLE);
                    }
                    Glide.with(mContext)
                            .load(mMainModel.getData().getHosterPortraitPath())
                            .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                            .into(imgIv);
                    roomidTv.setText("房间号:" + mMainModel.getData().getNumber());
                    Roomnumber = mMainModel.getData().getNumber();
                    roomname.setText(mMainModel.getData().getRoomName());
                    roomtypeTv.setText(mMainModel.getData().getClassifyName());
                    roompeonumTv.setText("在线:" + mMainModel.getData().getOnlineCount() + ">");
                    if (mMainModel.getData().getDescription() != null && mMainModel.getData().getDescription().length() > 0) {
                        Description = mMainModel.getData().getDescription();
                    }
                    if (mMainModel.getData().getWelcomeWord() != null && mMainModel.getData().getDescription().length() > 0) {
                        Welcome = mMainModel.getData().getWelcomeWord();
                    }
                    if (mMainModel.getData().getBackgroundImgPath() != null) {
                        Glide.with(mContext)
                                .load(mMainModel.getData().getBackgroundImgPath())
                                .apply(new RequestOptions().placeholder(R.mipmap.room_main_bg).error(R.mipmap.room_main_bg).dontAnimate())
                                .into(mainBgIv);
                        MyApplication.getInstance().setRoombg(mMainModel.getData().getBackgroundImgPath());
                    }
                    if (mMainModel.getData().getYunXinRoomId() != null) {
                        //进入云信聊天室
                        if (!IsEnterRoom) {
                            EnterChatRoom(mMainModel.getData().getYunXinRoomId());
                            MyApplication.getInstance().setYunXinRoomId(mMainModel.getData().getYunXinRoomId());
                            Log.e("EnterChatRoom ", "IsEnterRoom=" + IsEnterRoom + " 没有进入聊天室");
                        } else {
                            Log.e("EnterChatRoom ", "IsEnterRoom=" + IsEnterRoom + "   已经进入过聊天室");
                        }
                    }
                    mMainModel.getData().getIdentityStatus();//0    1闭麦   2锁麦

                    //  private String  roleType;// 0  房主  1 管理员  2 普通用户
                    if (mMainModel.getData().getRoleType() != null) {
                        if (mMainModel.getData().getRoleType().equals("1")) {
                            isManager = true;//是管理员
                            setting_ll.setVisibility(View.VISIBLE);
                        } else if (mMainModel.getData().getRoleType().equals("0")) {
                            isManager = true;//不是管理员 是房主
                            setting_ll.setVisibility(View.VISIBLE);
                        } else {
                            isManager = false;//不是管理员
                            setting_ll.setVisibility(View.GONE);
                        }
                    }
                    if (mMainModel.getData().getRicherName() == null) {
                        gold_name_tv.setText("虚位以待");
                    } else {
                        gold_name_tv.setText(mMainModel.getData().getRicherName());
                    }
                    if (mMainModel.getData().getRicherPortraitPath() != null) {
                        Glide.with(mContext)
                                .load(mMainModel.getData().getRicherPortraitPath())
                                .apply(new RequestOptions().placeholder(R.mipmap.boos_1).error(R.mipmap.boos_1).dontAnimate())
                                .into(imggoldIv);
                    } else {
                        imggoldIv.setImageResource(R.mipmap.boos_1);
                    }
                    if (lookgold) {
                        lookgold = false;
                        if (mMainModel.getData().getRicherName() != null && mMainModel.getData().getRicherName().length() > 0 && mMainModel.getData().getSurplusTime() != null) {
                            RoomGoldWindow(mMainModel.getData().getRicherPortraitPath(), mMainModel.getData().getRicherName(), mMainModel.getData().getSurplusTime());
                        } else {
//                            Toast.makeText(mContext, "当前房间暂无神豪", Toast.LENGTH_SHORT).show();
                            RoomGoldWindow(null, null, null);
                        }
                    }
                    mikeArray.clear();
                    my_mike_type.setText("我要上麦");
                    if (mMainModel.getData().getMikeArray() != null && mMainModel.getData().getMikeArray().size() == 8) {
                        for (int i = 0; i < mMainModel.getData().getMikeArray().size(); i++) {
                            mikeArray.add(mMainModel.getData().getMikeArray().get(i));//保存用户信息
                            if (mikeArray.get(i).getMikerId() != null) {
                                if (mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {//自己在麦上
                                    my_mike_type.setText("我在麦上");

                                    //先不管静音
                                    if (mikeArray.get(i).getStatus().equals("1")) {//闭麦
                                        OpenMick = false;//闭麦状态
//                                        mic_phone_cb.setChecked(false);
//                                        enableMic(false);
                                    } else {
                                        OpenMick = true;//非闭麦状态
//                                        mic_phone_cb.setChecked(true);
//                                        enableMic(true);
                                    }
                                }
                            }
                        }
                        recyAdapter = new MainRoomSetAdapter(mContext, mMainModel.getData().getMikeArray());
                        setRecyclerview.setAdapter(recyAdapter);
                        recyAdapter.setOnItemClickListener(mOnItemClickListener);
                        Log.e("getShowCharm", "getShowCharm=" + mMainModel.getData().getShowCharm());
                        if (mMainModel.getData().getShowCharm().equals("true")) {
                            if (mMainModel.getData().getHosterCharm() != null) {
                                float num1 = Float.parseFloat(mMainModel.getData().getHosterCharm());
                                if (num1 >= 10000) {
                                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                                    String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
                                    numsTv.setText(strPrice + "W");//心动值 //保留两位小数
                                } else {
                                    numsTv.setText(mMainModel.getData().getHosterCharm());//心动值
                                }
                            }
                            cardiacLl.setVisibility(View.VISIBLE);//显示房主心动值
                            hosterTv.setVisibility(View.GONE);//不显示房主
                            recyAdapter.changetShowcharm(true);
                        } else {
                            hosterTv.setVisibility(View.VISIBLE);
                            cardiacLl.setVisibility(View.GONE);
                            recyAdapter.changetShowcharm(false);
                        }
                        recyAdapter.notifyDataSetChanged();

                        if (my_mike_type.getText().equals("我在麦上")) {
//                            mai_ll.setBackgroundResource(R.drawable.upmai_bg);
                            my_mike_type.setVisibility(View.GONE);
                            mic_phone_cb.setVisibility(View.VISIBLE);
                            if (IsStartPush) {
                                Log.e("我的麦状态", "已经在发布直播");
                            } else {// 发布直播
                                zegoAudioRoom.startPublish();//开始发布直播
                                IsStartPush = true;
                                Log.e("我的麦状态", "开始发布直播");
                            }
                        } else {//不再麦上
//                            mai_ll.setBackgroundResource(R.mipmap.btn_microphone_user);
                            my_mike_type.setVisibility(View.VISIBLE);
                            mic_phone_cb.setVisibility(View.GONE);
                            if (IsStartPush) {
                                zegoAudioRoom.stopPublish();//取消发布直播
                                IsStartPush = false;
                                Log.e("我的麦状态", "取消发布直播");
                            } else {
                                Log.e("我的麦状态", "已经取消发布直播");
                            }
                        }
                        if (OpenMick) {//如果正常状态
                            mic_phone_cb.setChecked(true);
                        } else {//闭麦状态
                            mic_phone_cb.setChecked(false);
                        }

                    }
                    //贡献榜数据
                    if (mMainModel.getData().getRankingArray() != null) {
                        if (mMainModel.getData().getRankingArray().size() > 0) {
                            if (mMainModel.getData().getRankingArray().size() == 1) {
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv1);
//                                rankIv1.setVisibility(View.VISIBLE);
//                                rankIv2.setVisibility(View.GONE);
//                                rankIv3.setVisibility(View.GONE);

                                rank_rl_gx1.setVisibility(View.VISIBLE);
                                rank_rl_gx2.setVisibility(View.GONE);
                                rank_rl_gx3.setVisibility(View.GONE);
                            } else if (mMainModel.getData().getRankingArray().size() == 2) {
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv1);
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(1))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv2);
//                                rankIv1.setVisibility(View.VISIBLE);
//                                rankIv2.setVisibility(View.VISIBLE);
//                                rankIv3.setVisibility(View.GONE);

                                rank_rl_gx1.setVisibility(View.VISIBLE);
                                rank_rl_gx2.setVisibility(View.VISIBLE);
                                rank_rl_gx3.setVisibility(View.GONE);
                            } else if (mMainModel.getData().getRankingArray().size() == 3) {
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv1);
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(1))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv2);
                                Glide.with(mContext)
                                        .load(mMainModel.getData().getRankingArray().get(2))
                                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                        .into(rankIv3);
//                                rankIv1.setVisibility(View.VISIBLE);
//                                rankIv2.setVisibility(View.VISIBLE);
//                                rankIv3.setVisibility(View.VISIBLE);

                                rank_rl_gx1.setVisibility(View.VISIBLE);
                                rank_rl_gx2.setVisibility(View.VISIBLE);
                                rank_rl_gx3.setVisibility(View.VISIBLE);
                            }
                            rank_tv.setText("");
                        } else {
                            rank_tv.setText("排行榜");
//                            rankIv1.setVisibility(View.GONE);
//                            rankIv2.setVisibility(View.GONE);
//                            rankIv3.setVisibility(View.GONE);

                            rank_rl_gx1.setVisibility(View.GONE);
                            rank_rl_gx2.setVisibility(View.GONE);
                            rank_rl_gx3.setVisibility(View.GONE);
                        }
                    } else {
                        rank_tv.setText("排行榜");
//                        rankIv1.setVisibility(View.GONE);
//                        rankIv2.setVisibility(View.GONE);
//                        rankIv3.setVisibility(View.GONE);

                        rank_rl_gx1.setVisibility(View.GONE);
                        rank_rl_gx2.setVisibility(View.GONE);
                        rank_rl_gx3.setVisibility(View.GONE);
                    }
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.CollectRoom)) {//收藏直播间
            Log.e("CollectRoom", "result=" + result);
            StringModel mStringModel = new Gson().fromJson(result,
                    new TypeToken<StringModel>() {
                    }.getType());
            if (mStringModel.getCode().equals("1")) {
                getFindRoomInfo();//刷新直播间信息
                Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                //发送消息收藏房间
                ChatRoomSendMSG2("2", "收藏了房间", MyApplication.getInstance().getUserName(), MyApplication.getInstance().getUserRank(), "");
            } else if (mStringModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelCollectRoom)) {//取消收藏直播间
            Log.e("CancelCollectRoom", "result=" + result);
            StringModel mStringModel = new Gson().fromJson(result,
                    new TypeToken<StringModel>() {
                    }.getType());
            if (mStringModel.getCode().equals("1")) {
                getFindRoomInfo();//刷新直播间信息
                Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
            } else if (mStringModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.ClearCharm)) {//心动值清零
            Log.e("ClearCharm", "result=" + result);
            StringModel mStringModel = new Gson().fromJson(result,
                    new TypeToken<StringModel>() {
                    }.getType());
            if (mStringModel.getCode().equals("1")) {
                Toast.makeText(mContext, "清除成功", Toast.LENGTH_SHORT).show();
            } else if (mStringModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.ShowCharm)) {//展示心动值
            Log.e("ShowCharm", "result=" + result);
            StringModel mStringModel = new Gson().fromJson(result,
                    new TypeToken<StringModel>() {
                    }.getType());
            if (mStringModel.getCode().equals("1")) {
//                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                recyAdapter.changetShowcharm(true);
                FindCacheRoomInfo();//刷新直播间信息
            } else if (mStringModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.ConnectRoom)) {//心动链接
            Log.e("ConnectRoom", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelShowCharm)) {//不展示心动值
            Log.e("CancelShowCharm", "result=" + result);
            StringModel mStringModel = new Gson().fromJson(result,
                    new TypeToken<StringModel>() {
                    }.getType());
            if (mStringModel.getCode().equals("1")) {
//                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                recyAdapter.changetShowcharm(false);
                FindCacheRoomInfo();//刷新直播间信息
            } else if (mStringModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mStringModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindOnlineAudience)) {//查询直播间在线人数
            Log.e("FindOnline", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                onlineList.clear();
                if (mainModel.getData().getArray().size() > 0) {
                    for (int i = 0; i < mainModel.getData().getArray().size(); i++) {
                        onlineList.add(mainModel.getData().getArray().get(i));
                    }
                    PeoWindow("1", MyRoleType, onlineList, IsShowUPMike, isManager, OnlineType);
                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        } else if (url.equals(RetrofitService.Head + RetrofitService.FindLiner)) {//查询直播间排麦人员
            Log.e("FindLiner", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                onlineList.clear();
                if (mainModel.getData().getArray().size() > 0) {
                    for (int i = 0; i < mainModel.getData().getArray().size(); i++) {
                        onlineList.add(mainModel.getData().getArray().get(i));
                    }
                    IsShowUPMike = false;//不显示抱用户上麦
                    PeoWindow("2", MyRoleType, onlineList, IsShowUPMike, isManager, "");
                } else {
                    Toast.makeText(mContext, "暂无排麦人员", Toast.LENGTH_SHORT).show();
                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        } else if (url.equals(RetrofitService.Head + RetrofitService.FindBlacker)) {//查询直播间黑名单
            Log.e("FindLiner", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                Roomblacknum = mainModel.getData().getCount();
                if (mainModel.getData().getCount().length() > 0) {
                    int nums = Integer.valueOf(mainModel.getData().getCount()).intValue();
                    MyApplication.getInstance().setRoomBlacknums(nums);
                }
                Log.e("RoomAdminWindow", "getRoomAdminnums=" + MyApplication.getInstance().getRoomAdminnums() + " getRoomBlacknums=" + MyApplication.getInstance().getRoomBlacknums());
                RoomAdminWindow(Roomid);
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindManager)) {//查询直播间管理员
            Log.e("FindLiner", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                mainModel.getData().getCount();
                if (mainModel.getData().getCount().length() > 0) {
                    int nums = Integer.valueOf(mainModel.getData().getCount()).intValue();
                    MyApplication.getInstance().setRoomAdminnums(nums);
                }
                FindBlacker();
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        } else if (url.equals(RetrofitService.Head + RetrofitService.FindGift)) {//查询礼物
            Log.e("FindGift", "result=" + result);
            MainGiftModel model = new Gson().fromJson(result,
                    new TypeToken<MainGiftModel>() {
                    }.getType());
            giftArray.clear();
            if (model.getCode().equals("1")) {
                if (model.getData().size() > 0) {
                    for (int i = 0; i < model.getData().size(); i++) {
                        giftArray.add(model.getData().get(i));
                    }
                }
            } else if (model.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, model.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindPersonalGift)) {//查询背包礼物
            Log.e("FindPersonalGift", "result=" + result);
            MainGiftModel model = new Gson().fromJson(result,
                    new TypeToken<MainGiftModel>() {
                    }.getType());
            pakagegiftArray.clear();
            if (model.getCode().equals("1")) {
                if (model.getData().size() > 0) {
                    for (int i = 0; i < model.getData().size(); i++) {
                        pakagegiftArray.add(model.getData().get(i));
                    }
                }
            } else if (model.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, model.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindUserInfo)) {//查询用户信息
            Log.e("FindUserInfo", "result=" + result);
            MainModel model = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (model.getCode().equals("1")) {
//                zegoAudioRoom.pauseAudioModule(); // 暂停音频模块
                MyApplication.getInstance().setIsRoomto("true");//设置私聊入口
                NimUIKit.startP2PSession(mContext, model.getData().getAccid());//进入私聊页面
                //todo
            } else if (model.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, model.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.SetBossMike)) {//设置老板位
            Log.e("SetBossMike", "result=" + result);
            GetToast(result, "1");
        } else if (url.equals(RetrofitService.Head + RetrofitService.SetReceptionistMike)) {//设置主持位
            Log.e("SetReceptionistMike", "result=" + result);
            GetToast(result, "2");
        } else if (url.equals(RetrofitService.Head + RetrofitService.LineForMike)) {//点击排麦类型   点击麦位排麦
            Log.e("LineForMike", "result=" + result);
//            GetToast(result, "3");
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                FindCacheRoomInfo();//更新直播间信息
                if (mainModel.getData().getPush().equals("true")) {//上麦成功
                } else {//上麦失败
                    EventBus.getDefault().post(new TabCheckEvent("提示" + "排麦成功"));
                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FollowUser)) {//关注用户
            Log.e("FollowUser", "result=" + result);
            GetToast(result, "4");
        } else if (url.equals(RetrofitService.Head + RetrofitService.CloseMike)) {//设置闭麦
            Log.e("SetReceptionistMike", "result=" + result);
//            GetToast(result, "4");
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelCloseMike)) {//设置取消闭麦
            Log.e("CancelCloseMike", "result=" + result);
//            GetToast(result, "4");
        } else if (url.equals(RetrofitService.Head + RetrofitService.LockMike)) {//设置锁麦
            Log.e("SetReceptionistMike", "result=" + result);
            GetToast(result, "5");
        } else if (url.equals(RetrofitService.Head + RetrofitService.SetLineMike)) {//设置麦位排队
            Log.e("SetReceptionistMike", "result=" + result);
            GetToast(result, "6");
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelLockMike)) {//取消锁麦
            Log.e("CancelLockMike", "result=" + result);
            GetToast(result, "7");
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelSetLineMike)) {//取消麦位排队
            Log.e("CancelSetLineMike", "result=" + result);
            GetToast(result, "8");
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelSetReceptionistMike)) {//取消主持位
            Log.e("CancelSetRecepMike", "result=" + result);
            GetToast(result, "9");
        } else if (url.equals(RetrofitService.Head + RetrofitService.CancelSetBossMike)) {//取消老板位
            Log.e("CancelSetBossMike", "result=" + result);
            GetToast(result, "10");
        } else if (url.equals(RetrofitService.Head + RetrofitService.AddManager)) {//设置管理员
            Log.e("AddManager", "result=" + result);
            GetToast(result, "AddManager");
        } else if (url.equals(RetrofitService.Head + RetrofitService.DeleteManager)) {//撤销管理员
            Log.e("DeleteManager", "result=" + result);
            GetToast(result, "DeleteManager");
        } else if (url.equals(RetrofitService.Head + RetrofitService.AddBlacker)) {//从房间中拉黑
            Log.e("AddBlacker", "result=" + result);
            GetToast(result, "AddBlacker");
        } else if (url.equals(RetrofitService.Head + RetrofitService.AddForbider)) {//禁言
            Log.e("AddForbider", "result=" + result + "   CannotTalkname=" + CannotTalkname);
            ChatRoomSendMSG2("8", "被管理员禁言", CannotTalkname, "", "");
            GetToast(result, "10");
        } else if (url.equals(RetrofitService.Head + RetrofitService.DeleteForbider)) {//取消禁言
            Log.e("DeleteForbider", "result=" + result);
            GetToast(result, "10");
        } else if (url.equals(RetrofitService.Head + RetrofitService.PickDownForMike)) {//抱用户下麦
            Log.e("PickDownForMike", "result=" + result);
            GetToast(result, "11");//
//            停止推流 下麦旁听
            if (zegoAudioRoom != null) {
                zegoAudioRoom.stopPublish();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.PickUpForMike)) {//抱用户上麦
            Log.e("PickUpForMike", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
//                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                FindCacheRoomInfo();//更新直播间信息
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        } else if (url.equals(RetrofitService.Head + RetrofitService.QuitRoom)) {//退出房间
            Log.e("QuitRoom", "result=" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
//                OutRoom();//清除即构相关信息
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void GetToast(String result, String type) {
        MainModel mainModel = new Gson().fromJson(result,
                new TypeToken<MainModel>() {
                }.getType());
        if (mainModel.getCode().equals("1")) {
//            Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
            if (type.equals("DeleteManager")) {
                getFindRoomInfo();
//                ChatRoomSendMSG2("13", "你已被撤销管理员", Mangerid, "", "");  //发送取消管理员消息  设置的对象更新房间信息
                MainDataBean mainDataBean = new MainDataBean();
                mainDataBean.setBody("你已被撤销管理员");
                mainDataBean.setType("13");
                UserData data = new UserData();
                data.setExpRank(MyApplication.getInstance().getUserRank());
                data.setPortraitPath(MyApplication.getInstance().getUserImg());
                data.setUserId(MyApplication.getInstance().getUserId());
                data.setUsername(MyApplication.getInstance().getUserName());
                UserData data2 = new UserData();
                data2.setUserId(Mangerid);
                mainDataBean.setFromUser(data);
                mainDataBean.setToUser(data2);
                //对象转字符串
                Gson gson = new Gson();
                String jsondata = gson.toJson(mainDataBean);
                ChatRoomSendMSG2("13", "", "", "", jsondata);
                Log.e("ChatRoomSendMSG212", "jsondata=" + jsondata);


            } else if (type.equals("AddManager")) {
                getFindRoomInfo();
//                ChatRoomSendMSG2("12", "你已成为管理员", Mangerid, "", ""); //发送设置管理员消息  设置的对象更新房间信息
//                {"fromUser":{"expRank":"0","portraitPath":"http://cdn.tuerapp.com/img/user/61_1561964265692.png",
//                        "userId":"61","username":"我是真的不知道为什么"},"toUser":{"portraitPath":"","userId":"62"},"type":"12"}

                MainDataBean mainDataBean = new MainDataBean();
                mainDataBean.setBody("你已成为管理员");
                mainDataBean.setType("12");
                UserData data = new UserData();
                data.setExpRank(MyApplication.getInstance().getUserRank());
                data.setPortraitPath(MyApplication.getInstance().getUserImg());
                data.setUserId(MyApplication.getInstance().getUserId());
                data.setUsername(MyApplication.getInstance().getUserName());
                UserData data2 = new UserData();
                data2.setUserId(Mangerid);
                mainDataBean.setFromUser(data);
                mainDataBean.setToUser(data2);
                //对象转字符串
                Gson gson = new Gson();
                String jsondata = gson.toJson(mainDataBean);
                ChatRoomSendMSG2("12", "", "", "", jsondata);
                Log.e("ChatRoomSendMSG212", "jsondata=" + jsondata);


            } else if (type.equals("AddBlacker")) {
                FindCacheRoomInfo();
                ChatRoomSendMSG2("14", "你已被管理员拉黑", Mangerid, takeblackerId, ""); //发送设置管理员消息  设置的对象更新房间信息
            } else {
                FindCacheRoomInfo();//更新直播间信息
            }
        } else if (mainModel.getCode().equals("0")) {
            Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
            MyApplication.getInstance().setUserId("");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getGiftName(String giftid) {//根据礼物id查询礼物名称
        String giftname = "";
        for (int i = 0; i < giftArray.size(); i++) {
            if (giftid.equals(giftArray.get(i).getGiftId())) {
                giftname = giftArray.get(i).getGiftName();
            }
        }
        return giftname;
    }

    public void OutRoom() {
        if (hasLogin) {
            logout();
        }
        removeCallbacks();
        if (mManager != null) {
            localWakeLock.setReferenceCounted(false);
            localWakeLock.release();//释放电源锁，如果不释放finish这个acitivity后仍然会有自动锁屏的效果
            mManager.unregisterListener(this);//注销传感器监听
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handlerThread.quit();
        }
        if (mReceiver != null) {
            //TODO   导致  Receiver not registered: com.zanyu.live.activity.MainRoomActivity$HeadSetReceiver@77f5476
//            unregisterReceiver(mReceiver);
        }
//        NIMClient.getService(ChatRoomService.class).exitChatRoom(YXRoomid);//云信退出房间
    }

    //上麦
    public void UpMai() {

    }

    @SuppressLint("InvalidWakeLockTag")
    public void initZego() {
        Log.e("ZegoohoneActivity", "initZego初始化");
        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取系统服务POWER_SERVICE，返回一个PowerManager对象
        localPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        localWakeLock = this.localPowerManager.newWakeLock(32, "MyPower");//第一个参数为电源锁级别，第二个是日志tag
        handlerThread = new HandlerThread("music");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == RESTART_PUBLSH_MSG) {

                    //计数器
                    if (restartCount <= 10) {
                        restartCount++;
                        /**
                         * 重新推流
                         */
                        boolean restartState = zegoAudioRoom.restartPublishStream();
                        Log.d("ZegoohoneActivity", " Handler handleMessa" + restartState + "");
                        /**
                         * 超过10次后给用户提示,只提示1次
                         */
                    } else if (isPromptToast) {
                        isPromptToast = false;
                        Log.d("ZegoohoneActivity", " Handler handleMessage" + restartCount + "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainRoomActivity.this, getString(R.string.zg_text_app_restart_publish_failure), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                return false;
            }
        });
        zegoAudioRoom = ((MyApplication) getApplication()).getAudioRoomClient();
        appTitle = "房间号" + Roomid;
        setupCallbacks();
        login(Roomid);
        builtinSpeakerOn(true);//扬声器开关
        zegoAudioRoom.enableAEC(true);

        registerHeadsetPlug();

        // 开启音浪监听
        soundLevel();
    }


    HeadSetReceiver mReceiver;

    private void registerHeadsetPlug() {
        mReceiver = new HeadSetReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mReceiver, intentFilter);
    }

    private void login(String roomId) {
        Log.e("ZegoohoneActivity", "开始登陆房间" + "roomId=" + roomId);
        // TODO 开始登陆房间
        zegoAudioRoom.setUserStateUpdate(true);
        zegoAudioRoom.enableAux(false);
        zegoAudioRoom.enableMic(true);
        zegoAudioRoom.enableSelectedAudioRecord(ZegoConstants.AudioRecordMask.NoRecord, 44100);
        zegoAudioRoom.enableSpeaker(true);
        boolean success = zegoAudioRoom.
                loginRoom(roomId, new ZegoLoginAudioRoomCallback() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onLoginCompletion(int state) {
                        Log.e("ZegoohoneActivity", "登陆成功: %d" + state + "  ");
                        if (state == 0) {
                            Log.e("ZegoohoneActivity", "zegoUserState" + MyApplication.getInstance().getUserId() + "  " + MyApplication.getInstance().getUserName());
                            hasLogin = true;
                            zegoUserState.userID = MyApplication.getInstance().getUserId();
                            zegoUserState.userName = MyApplication.getInstance().getUserName();
                            if (recyAdapter != null) {
                                recyAdapter.addUser(zegoUserState);
                                recyAdapter.setSelfZegoUser(zegoUserState);
                            }
                            enableMic(false);
                        } else {
                            Toast.makeText(mContext, String.format("Login Error: %d", state), Toast.LENGTH_LONG).show();
                            // TODO 登陆失败错误码
                        }
                    }
                });
        if (!success) {
            // TODO 登陆失败
            Log.e("ZegoohoneActivity", "登陆失败" + success);
            Toast.makeText(mContext, "登陆语音聊天失败", Toast.LENGTH_LONG).show();
        }
    }

    private void setupCallbacks() {
        Log.e("ZegoohoneActivity", "setupCallbacks  " + "zegoAudioRoom=" + zegoAudioRoom);
        zegoAudioRoom.setAudioRoomDelegate(new ZegoAudioRoomDelegate() {
            @Override
            public void onKickOut(int errorCode, String roomId) {
                Log.d("ZegoohoneActivity", "onKickOut room:" + roomId + "  " + errorCode);
                // TODO 被踢出房间
            }

            @Override
            public void onDisconnect(int errorCode, String roomId) {
                Log.d("ZegoohoneActivity", "onDisconnect room:" + roomId + "  " + errorCode);
                // TODO 与房间断开连接, 错误码
                recyAdapter.streamStateUpdateAll(1);
            }

            @Override
            public void onStreamUpdate(final ZegoAudioStreamType zegoAudioStreamType, final ZegoAudioStream zegoAudioStream) {
                Log.d("ZegoohoneActivity", "onStreamUpdate, " + zegoAudioStreamType + "  " + zegoAudioStream.getStreamId());
                ZegoUserState zegoUserState = new ZegoUserState();
//                zegoUserState.userID = zegoAudioStream.getUserId();
//                zegoUserState.userName = zegoAudioStream.getUserName();
                zegoUserState.userID = MyApplication.getInstance().getUserId();
                zegoUserState.userName = MyApplication.getInstance().getUserName();
                switch (zegoAudioStreamType) {
                    case ZEGO_AUDIO_STREAM_ADD:
                        // TODO 新增流
                        if (recyAdapter != null) {
                            recyAdapter.addUser(zegoUserState);
                            recyAdapter.bindUserInfoStreamID(zegoAudioStream);
                        }
                        String extraInfo = zegoAudioStream.getExtraInfo();
                        if (extraInfo != null) {
                            JSONObject jsonObject = JSONObject.parseObject(extraInfo);
                            if (jsonObject != null) {
                                StreamState streamState = jsonObject.getObject("stream_state", StreamState.class);
                                if (recyAdapter != null) {
                                    recyAdapter.updateMuteState(streamState.getEnable_mic(), zegoAudioStream.getUserName());
                                }
                            }
                        }
                        break;
                    case ZEGO_AUDIO_STREAM_DELETE:
                        // TODO 删除流
                        recyAdapter.removeUser(zegoUserState);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onUserUpdate(ZegoUserState[] userList, int updateType) {
                Log.d("ZegoohoneActivity", "onUserUpdate, " + " " + (updateType == ZegoIM.UserUpdateType.Increase ? "Increase" : "Total"));
                for (ZegoUserState user : userList) {

                    Log.d("ZegoohoneActivity", "onUserUpdate," + " " + user.userName + "; updateFlag: " + (user.updateFlag == ZegoIM.UserUpdateFlag.Added ? "Add" : "Delete"));
                    //更新房间信息
                    FindCacheRoomInfo();
                    if (user.updateFlag == ZegoIM.UserUpdateFlag.Added) {
                        // TODO 新增用户
                        if (recyAdapter != null) {
                            recyAdapter.addUser(user);
                        }
                    } else {
                        // TODO 删除用户
                        if (recyAdapter != null) {
                            recyAdapter.removeUser(user);
                        }
                    }
                }
            }

            @Override
            public void onUpdateOnlineCount(String s, int i) {
                Log.d("online count: %d", "   " + i);
            }

            @Override
            public void onRecvRoomMessage(String roomId, ZegoRoomMessage[] messages) {
                // TODO 房间消息
            }

            @Override
            public void onRecvConversationMessage(String roomId, String conversationId, ZegoConversationMessage message) {
                Log.d("ZegoohoneActivity", "onRecvConversationMessage " + roomId + "   " + conversationId);
            }

            @Override
            public void onRecvBigRoomMessage(String s, ZegoBigRoomMessage[] zegoBigRoomMessages) {
                Log.d("ZegoohoneActivity", "onRecvBigRoomMessage " + s);
            }

            @Override
            public void onRecvCustomCommand(String userId, String userName, String content, String roomId) {
                Log.d("ZegoohoneActivity", "onRecvCustomCommand " + userId + "  " + roomId + "  " + content);

            }

            @Override
            public void onStreamExtraInfoUpdated(final ZegoAudioStream[] zegoStreamInfos, String s) {
                // TODO 流信息额外更新
                for (ZegoAudioStream zegoStreamInfo : zegoStreamInfos) {
                    String extraInfo = zegoStreamInfo.getExtraInfo();
                    if (extraInfo != null) {
                        JSONObject jsonObject = JSONObject.parseObject(extraInfo);
                        StreamState streamState = jsonObject.getObject("stream_state", StreamState.class);
                        if (streamState != null)
                            recyAdapter.updateMuteState(streamState.getEnable_mic(), zegoStreamInfo.getUserName());
                    }
                }
            }
        });

        zegoAudioRoom.setAudioPublisherDelegate(new ZegoAudioLivePublisherDelegate() {

            @Override
            public void onPublishStateUpdate(int stateCode, String streamId, HashMap<String, Object> info) {
                Log.e("ZegoohoneActivity", "发布直播状态回调onPublishStateUpdate " + stateCode + "  " + streamId + "  " + info);
//                stateCode = 0	直播成功
//                        stateCode = 3	直播遇到严重错误。stateCode=1,2,3 这三个基本不会出现
//                        stateCode = 4	创建直播流失败。请确认 userid，username 是否为空
//                stateCode = 5	获取流信息失败
//                        stateCode = 6	流不存在。请检查：(1) AppID 是否相同，要保证一致; (2) 是否同时开启测试环境或同时在正式环境下
//                        stateCode = 7	媒体服务器连接失败。请确认推流端是否正常推流、正式环境和测试环境是否设置都是同一个、网络是否正常
//                        stateCode = 8	DNS 解析失败
//                stateCode = 9	未 loginRoom 就直接 play/publish
//                        stateCode = 10	逻辑服务器网络错误(网络断开约 3 分钟时会返回该错误)
                ZegoAudioStream myStream = new ZegoAudioStream(streamId, PrefUtils.getUserId(), PrefUtils.getUserName());
                if (stateCode == 0) {
                    Log.e("ZegoohoneActivity", "直播成功onPublishStateUpdate " + stateCode + "  " + streamId);
                    hasPublish = true;
                    publishStreamId = streamId;

                    recyAdapter.bindUserInfoStreamID(myStream);

                    /**
                     * 清空重试计数器
                     */

                } else {
                    // TODO 推流失败
                    recyAdapter.streamStateUpdate(1, myStream);
                    /**
                     * 延时10秒后开启重新推流
                     */
                    handler.removeMessages(RESTART_PUBLSH_MSG);
                    handler.sendMessageDelayed(handler.obtainMessage(RESTART_PUBLSH_MSG), 10000);
                }

            }

            @Override
            public ZegoAuxData onAuxCallback(int dataLen) {

                return null;
            }

            @Override
            public void onPublishQualityUpdate(String streamId, ZegoPublishStreamQuality zegoStreamQuality) {
                Log.d("ZegoohoneActivity", "onPublishQualityUpdate " +
                        streamId + "  " + zegoStreamQuality.quality + "  " + zegoStreamQuality.akbps);
                RecyclerGridViewAdapter.CommonStreamQuality commonStreamQuality = EntityConversion.publishQualityToCommonStreamQuality(zegoStreamQuality);

                // 推流质量更新
                if (recyAdapter != null) {
                    recyAdapter.updateQualityUpdate(streamId, commonStreamQuality);
                }

            }
        });
        zegoAudioRoom.setAudioPlayerDelegate(new ZegoAudioLivePlayerDelegate() {
            @Override
            public void onPlayStateUpdate(int stateCode, ZegoAudioStream zegoAudioStream) {
                Log.d("ZegoohoneActivity", "onPlayStateUpdate " + stateCode + "    " + zegoAudioStream.getStreamId());
                if (stateCode == 0) {
                    recyAdapter.bindUserInfoStreamID(zegoAudioStream);
                } else {
                    recyAdapter.streamStateUpdate(1, zegoAudioStream);
                }
            }

            @Override
            public void onPlayQualityUpdate(String streamId, ZegoPlayStreamQuality zegoStreamQuality) {

                Log.d("ZegoohoneActivity", "onPlayQualityUpdate " +
                        streamId + zegoStreamQuality.quality + "  " + zegoStreamQuality.audioBreakRate);
                RecyclerGridViewAdapter.CommonStreamQuality commonStreamQuality = EntityConversion.playQualityToCommonStreamQuality(zegoStreamQuality);
                // 拉流质量更新
                if (recyAdapter != null) {
                    recyAdapter.updateQualityUpdate(streamId, commonStreamQuality);
                }
                ZegoAudioStream zegoAudioStream = new ZegoAudioStream();
                zegoAudioStream.setStreamId(streamId);
            }
        });
        zegoAudioRoom.setAudioLiveEventDelegate(new ZegoAudioLiveEventDelegate() {
            @Override
            public void onAudioLiveEvent(ZegoAudioLiveEvent zegoAudioLiveEvent, HashMap<String, String> info) {
                Log.d("ZegoohoneActivity", "setAudioLiveEventDelegate " + " " + zegoAudioLiveEvent + "" + info);

                ZegoAudioStream mZegoAudioStream = new ZegoAudioStream();
                if (info.get("StreamID") != null) {
                    String streamID = info.get("StreamID");
                    mZegoAudioStream.setStreamId(streamID);
                }
                // 开始
                if (zegoAudioLiveEvent == Audio_Play_BeginRetry || zegoAudioLiveEvent == Audio_Publish_BeginRetry) {
                    recyAdapter.streamStateUpdate(2, mZegoAudioStream);
                } else if (zegoAudioLiveEvent == Audio_Play_RetrySuccess || zegoAudioLiveEvent == Audio_Publish_RetrySuccess) {
                    Log.e("ZegoohoneActivity", "Audio_Play_RetrySuccess   getUserId=" + mZegoAudioStream.getUserId() + "  getUserName=" + mZegoAudioStream.getUserName() + "  getStreamId=" + mZegoAudioStream.getStreamId());
                    recyAdapter.streamStateUpdate(0, mZegoAudioStream);
                } else if (zegoAudioLiveEvent == Audio_Play_TempDisconnected || zegoAudioLiveEvent == Audio_Publish_TempDisconnected) {
                    if (zegoAudioLiveEvent == Audio_Play_TempDisconnected) {
//                        Toast.makeText(mContext,"语音聊天拉流失败,正在重连中",Toast.LENGTH_SHORT).show();
                        Log.e("ZegoohoneActivity", "语音聊天拉流失败,正在重连中");
                    } else if (zegoAudioLiveEvent == Audio_Publish_TempDisconnected) {
//                        Toast.makeText(mContext,"语音聊天推流失败,正在重连中",Toast.LENGTH_SHORT).show();
                        Log.e("ZegoohoneActivity", "语音聊天推流失败,正在重连中");
                    }

                    recyAdapter.streamStateUpdate(1, mZegoAudioStream);
                }
            }
        });
        zegoAudioRoom.setAudioRecordDelegate(new ZegoAudioLiveRecordDelegate() {
            private long lastCallbackTime = 0;

            @Override
            public void onAudioRecord(byte[] audioData, int sampleRate, int numberOfChannels, int bitDepth, int type) {

            }
        });
        zegoAudioRoom.setAudioDeviceEventDelegate(new ZegoAudioDeviceEventDelegate() {
            @Override
            public void onAudioDevice(String deviceName, int errorCode) {
                Log.d("ZegoohoneActivity", "setAudioDeviceEventDelegate " + deviceName + " " + errorCode);
            }
        });
        zegoAudioRoom.setAudioPrepareDelegate(new ZegoAudioPrepareDelegate() {

            @Override
            public void onAudioPrepared(ByteBuffer inData, int sampleCount, int bitDepth, int sampleRate, ByteBuffer outData) {
                if (inData != null && outData != null) {
                    inData.position(0);
                    outData.position(0);
                    // outData的长度固定为sampleCount * bitDepth
                    // 不可更改
                    outData.limit(sampleCount * bitDepth);
                    // 将处理后的数据返回sdk
                    outData.put(inData);
                }
            }
        });

        zegoAudioRoom.setAudioPrepDelegate2(new ZegoAudioPrepDelegate2() {
            @Override
            public ZegoAudioFrame onAudioPrep(ZegoAudioFrame zegoAudioFrame) {
                return zegoAudioFrame;
            }
        });
        zegoAudioRoom.setAudioAVEngineDelegate(new ZegoAudioAVEngineDelegate() {
            @Override
            public void onAVEngineStart() {
                Log.d("ZegoohoneActivity", "onAVEngineStart " + "onAVEngineStart");
            }

            @Override
            public void onAVEngineStop() {
                Log.d("ZegoohoneActivity", "onAVEngineStop " + "onAVEngineStop");
            }
        });
    }

    public String splitData(String str, String strStart, String strEnd) {
        String tempStr;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        return tempStr;

    }

    private void soundLevel() {//声浪监听
        ZegoSoundLevelMonitor.getInstance().setCallback(new IZegoSoundLevelCallback() {
            @Override
            public void onSoundLevelUpdate(ZegoSoundLevelInfo[] zegoSoundLevelInfos) {
                for (ZegoSoundLevelInfo zegoSoundLevelInfo : zegoSoundLevelInfos) {
                    if (recyAdapter != null) {
                        recyAdapter.soundLevelUpdate(zegoSoundLevelInfo);
                        //获取 当前说话的流ID  说话的声音级别
                        Log.e("soundLevel", "onSoundLevelUpdate" + "  soundLevel=" + zegoSoundLevelInfo.soundLevel + "    streamID=" + zegoSoundLevelInfo.streamID);
                        String talkid = splitData(zegoSoundLevelInfo.streamID, "-", "-");
                        Log.e("talkid", "talkid=" + talkid);
                        for (int i = 0; i < mikeArray.size(); i++) {
                            if (mikeArray.get(i).getMikerId() != null) {
                                if (mikeArray.get(i).getMikerId().equals(talkid)) {//说话人的id
                                    Log.e("getMikeNumber", "getMikeNumber=" + mikeArray.get(i).getMikeNumber());
                                    int mikenumber = Integer.parseInt(mikeArray.get(i).getMikeNumber()) - 1;//说话人的坐位
                                    if (zegoSoundLevelInfo.soundLevel > 1) {//大于1开始播放
//                                        RoomPlayemoji(0, mikenumber);// 开始播放声音动画
                                        Palysoundbg(mikenumber, mikeArray.get(i).getMikerGender());// 开始播放声音动画
                                    } else {//停止播放
                                        Stopsoundbg(mikenumber, mikeArray.get(i).getMikerGender());// 停止播放声音动画
                                    }
                                }
                            } else {
                                int mikenumber = Integer.parseInt(mikeArray.get(i).getMikeNumber()) - 1;//空的坐位
                                Stopsoundbg(mikenumber, mikeArray.get(i).getMikerGender());// 停止播放声音动画
                            }
                        }
                    }
                }
            }

            @Override
            public void onCaptureSoundLevelUpdate(ZegoSoundLevelInfo zegoSoundLevelInfo) {
                if (recyAdapter != null) {
                    recyAdapter.soundLevelUpdate(zegoSoundLevelInfo);
                    //获取 自己当前说话的流ID  说话的声音级别
                    Log.e("soundLevel", "onCaptureSoundLevelUpdate" + "  soundLevel=" + zegoSoundLevelInfo.soundLevel + "    streamID=" + zegoSoundLevelInfo.streamID);
                    for (int i = 0; i < mikeArray.size(); i++) {
                        if (mikeArray.get(i).getMikerId() != null) {
                            if (mikeArray.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {//自己的id
                                Log.e("getMikeNumber", "getMikeNumber=" + mikeArray.get(i).getMikeNumber());
                                int mikenumber = Integer.parseInt(mikeArray.get(i).getMikeNumber()) - 1;//自己麦上的坐位
                                if (zegoSoundLevelInfo.soundLevel > 1 && OpenMick) {//大于1开始播放并且没有闭麦
//                                        RoomPlayemoji(0, mikenumber);// 开始播放声音动画
                                    Palysoundbg(mikenumber, mikeArray.get(i).getMikerGender());// 开始播放声音动画
                                } else {//停止播放
                                    Stopsoundbg(mikenumber, mikeArray.get(i).getMikerGender());// 停止播放声音动画
                                }
                            }
                        } else {
                            int mikenumber = Integer.parseInt(mikeArray.get(i).getMikeNumber()) - 1;//空的坐位
                            Stopsoundbg(mikenumber, mikeArray.get(i).getMikerGender());// 停止播放声音动画
                        }
                    }
                }
            }
        });

        ZegoSoundLevelMonitor.getInstance().start();
    }

    //播放说话动效
    public void Palysoundbg(int mikenumber, String mikerGender) {
        int gender = -1;
        if (mikerGender.equals("男")) {
            gender = R.drawable.room_man_soundgif;
        } else {
            gender = R.drawable.room_women_soundgif;
        }
        if (mikenumber == 0) {
            soundIv.setVisibility(View.VISIBLE);
            soundIv.setImageResource(gender);
            animationDrawable = (AnimationDrawable) soundIv.getDrawable();
            animationDrawable.start();
        } else if (mikenumber == 1) {
            soundIv2.setVisibility(View.VISIBLE);
            soundIv2.setImageResource(gender);
            animationDrawable2 = (AnimationDrawable) soundIv2.getDrawable();
            animationDrawable2.start();
        } else if (mikenumber == 2) {
            soundIv3.setVisibility(View.VISIBLE);
            soundIv3.setImageResource(gender);
            animationDrawable3 = (AnimationDrawable) soundIv3.getDrawable();
            animationDrawable3.start();
        } else if (mikenumber == 3) {
            soundIv4.setVisibility(View.VISIBLE);
            soundIv4.setImageResource(gender);
            animationDrawable4 = (AnimationDrawable) soundIv4.getDrawable();
            animationDrawable4.start();
        } else if (mikenumber == 4) {
            soundIv5.setVisibility(View.VISIBLE);
            soundIv5.setImageResource(gender);
            animationDrawable5 = (AnimationDrawable) soundIv5.getDrawable();
            animationDrawable5.start();
        } else if (mikenumber == 5) {
            soundIv6.setVisibility(View.VISIBLE);
            soundIv6.setImageResource(gender);
            animationDrawable6 = (AnimationDrawable) soundIv6.getDrawable();
            animationDrawable6.start();
        } else if (mikenumber == 6) {
            soundIv7.setVisibility(View.VISIBLE);
            soundIv7.setImageResource(gender);
            animationDrawable7 = (AnimationDrawable) soundIv7.getDrawable();
            animationDrawable7.start();
        } else if (mikenumber == 7) {
            soundIv8.setVisibility(View.VISIBLE);
            soundIv8.setImageResource(gender);
            animationDrawable8 = (AnimationDrawable) soundIv8.getDrawable();
            animationDrawable8.start();
        }
    }

    //停止说话动效
    public void Stopsoundbg(int mikenumber, String mikerGender) {
        if (mikenumber == 0) {
            if (animationDrawable != null) {
//                animationDrawable = (AnimationDrawable) soundIv.getDrawable();
//                animationDrawable.stop();
                soundIv.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 1) {
            if (animationDrawable2 != null) {
                animationDrawable2 = (AnimationDrawable) soundIv2.getDrawable();
                animationDrawable2.stop();
                soundIv2.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 2) {
            if (animationDrawable3 != null) {
                animationDrawable3 = (AnimationDrawable) soundIv3.getDrawable();
                animationDrawable3.stop();
                soundIv3.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 3) {
            if (animationDrawable4 != null) {
                animationDrawable = (AnimationDrawable) soundIv4.getDrawable();
                animationDrawable.stop();
                soundIv4.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 4) {
            if (animationDrawable5 != null) {
                animationDrawable5 = (AnimationDrawable) soundIv5.getDrawable();
                animationDrawable5.stop();
                soundIv5.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 5) {
            if (animationDrawable6 != null) {
                animationDrawable6 = (AnimationDrawable) soundIv6.getDrawable();
                animationDrawable6.stop();
                soundIv6.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 6) {
            if (animationDrawable7 != null) {
                animationDrawable7 = (AnimationDrawable) soundIv7.getDrawable();
                animationDrawable7.stop();
                soundIv7.setVisibility(View.INVISIBLE);
            }
        } else if (mikenumber == 7) {
            if (animationDrawable8 != null) {
                animationDrawable8 = (AnimationDrawable) soundIv8.getDrawable();
                animationDrawable8.stop();
                soundIv8.setVisibility(View.INVISIBLE);
            }
        }
    }


    /**
     * 开启麦克风
     *
     * @param enable true为关闭麦克风 false为开启麦克风
     */
    private void enableMic(final boolean enable) {

        if (zegoAudioRoom != null) {
            if (zegoAudioRoom.enableMic(enable)) {
                StreamState streamState = new StreamState();
                streamState.setEnable_mic(enable);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("stream_state", streamState);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                zegoAudioRoom.updateStreamExtraInfo(jsonObject.toString());

                if (recyAdapter != null) {
                    recyAdapter.updateMuteState(enable, PrefUtils.getUserName());
                }
            }
        }

    }


    String path = null;

    boolean speaker = false;


    private void unregisterSensor() {
        localWakeLock.setReferenceCounted(false);
        localWakeLock.release();
        mManager.unregisterListener(this);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void registerSensor() {
        this.localPowerManager.newWakeLock(32, "MyPower");
        mManager.registerListener(this, mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),// 距离感应器
                SensorManager.SENSOR_DELAY_NORMAL);//注册传感器，第一个参数为距离监听器，第二个是传感器类型，第三个是延迟类型
    }

    private void builtinSpeakerOn(boolean enable) {
        if (zegoAudioRoom != null) {
            if (enable) {
                unregisterSensor();
            } else {
                registerSensor();
            }
            zegoAudioRoom.setBuiltinSpeakerOn(enable);
        }
    }

    private void stopMusic() {
        zegoMediaPlayer.stop();
    }

    private void playMusic() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                path = SystemUtil.copyAssetsFile2Phone(MainRoomActivity.this, "test.mp3");
                if (path != null) {
                    zegoMediaPlayer.start(path, false);
                } else {
                    Toast.makeText(mContext, getString(R.string.zg_toast_text_play_path_error), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void getMsg(String str1, String str2) {
        //通过实现MyReceiver.Message接口可以在这里对MyReceiver中的数据进行处理
        Log.e("MyReceiver", "收到广播 " + str1 + " " + str2);//用户云心账号 昵称
        if (str1.equals("start")) {
            zegoAudioRoom.pauseAudioModule(); // 暂停音频模块
        }else if(str1.equals("stop")){
            zegoAudioRoom.resumeAudioModule(); // 恢复音频模块
        }
    }

    /**
     * receiver监听
     */
    public class HeadSetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                    //Bluetooth headset is now disconnected
                }
            } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
//                        btnSpeaker.setEnabled(true);
//                        btnSpeaker.setAlpha(1f);
                    } else if (intent.getIntExtra("state", 0) == 1) {
//                        btnSpeaker.setEnabled(false);
//                        btnSpeaker.setAlpha(0.5f);
                    }
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("MainRoomLive", "onStop");
//        mHandler.removeCallbacks(scrollRunnable);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new TabCheckEvent("退出房间"));
        MyApplication.getInstance().setIsInRoom("false");
        MyApplication.getInstance().setExitRoomid("");
        MyApplication.getInstance().setExitRoomdata("");
        Log.e("MainRoomLive", "onDestroy");
        if (mGiftAnimWindow != null) {
            mGiftAnimWindow.dismiss();
        }
        Log.e("音频模块", "onPause暂停");
        if(zegoAudioRoom!=null){
            zegoAudioRoom.pauseAudioModule(); // 暂停音频模块
        }
        Log.e("云信退出房间", "YXRoomid" + YXRoomid);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, false);//注销监听
        NIMClient.getService(ChatRoomService.class).exitChatRoom(YXRoomid);//云信退出房间
        OutRoom();//清除即构相关信息
//        getQuitRoom();//退出房间
        EventBus.getDefault().unregister(this);
        stopTimer();
        unregisgerPhoneCallingListener();//注销来电监听
        MyApplication.getInstance().setMyRoomid("");
        MyApplication.getInstance().setMaiRoomid("");
        unregisterReceiver(myReceiver);     //注销广播接收器
    }


    private void logout() {
        boolean success = zegoAudioRoom.logoutRoom();
        zegoAudioRoom.stopPublish();
        hasLogin = false;
        hasPublish = false;

        Log.d("logout: %s", success + "  ");
    }


    private void removeCallbacks() {
        if (zegoAudioRoom != null) {
            zegoAudioRoom.setAudioRoomDelegate(null);
            zegoAudioRoom.setAudioPublisherDelegate(null);
            zegoAudioRoom.setAudioPlayerDelegate(null);
            zegoAudioRoom.setAudioLiveEventDelegate(null);
            zegoAudioRoom.setAudioRecordDelegate(null);
            zegoAudioRoom.setAudioDeviceEventDelegate(null);
            zegoAudioRoom.setAudioPrepareDelegate(null);
            zegoAudioRoom.setAudioAVEngineDelegate(null);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] its = event.values;
        //Log.d(TAG,"its array:"+its+"sensor type :"+event.sensor.getType()+" proximity type:"+Sensor.TYPE_PROXIMITY);
        if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            // boolean temp =  speaker;
            System.out.println("its[0]:" + its[0]);

            //经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
            if (its[0] == 0.0) {// 贴近手机
//                activityZegoPhoneBinding.btnSpeaker.setChecked(false);
//                speaker = temp;
                Log.d("zego", "hands up in calling activity");
                if (localWakeLock.isHeld()) {
                    return;
                } else {
                    localWakeLock.acquire();// 申请设备电源锁
                }
            } else {// 远离手机

                System.out.println("hands moved");
                Log.d("zego", "hands moved in calling activity");
//                if(!speaker){
//                    activityZegoPhoneBinding.btnSpeaker.setChecked(true);
//                }
                //    speaker = temp;
                if (localWakeLock.isHeld()) {
                    return;
                } else {
                    localWakeLock.setReferenceCounted(false);
                    localWakeLock.release(); // 释放设备电源锁
                }
            }
        }

    }


    // 开始定时器
    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("lzp", "timer excute");
                ConnectRoom();//房间心跳连接
            }
        }, 30000, 30000);
    }

    // 停止定时器
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    /**
     * 电话状态监听.
     */
    private void regisgerPhoneCallingListener() {
        mPhoneStateListener = new ZegoPhoneStateListener();

        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 注销电话状态监听.
     */
    private void unregisgerPhoneCallingListener() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        mPhoneStateListener = null;
    }

    private class ZegoPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e("TelephonyManager", ": call state idle");
                    if (zegoAudioRoom != null) {
                        zegoAudioRoom.resumeAudioModule();  // 结束通话，恢复音频模块
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e("TelephonyManager", ": call state ringing");
                    if (zegoAudioRoom != null) {
                        zegoAudioRoom.pauseAudioModule();    // 来电，暂停音频模块
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }
    }

    public void OutLiveRoom() {//退出房间
        Log.e("云信退出房间", "YXRoomid" + YXRoomid);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, false);//注销监听
        NIMClient.getService(ChatRoomService.class).exitChatRoom(YXRoomid);//云信退出房间
        OutRoom();//清除即构相关信息
        getQuitRoom();//退出房间
        EventBus.getDefault().post(new TabCheckEvent("退出房间"));
        MyApplication.getInstance().setMaiRoomid("");
        finish();
    }

    //    /**
//     * 菜单、返回键响应
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if(keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            exitBy2Click();        //调用双击退出函数
//        }
//        return false;
//    }
    private boolean validateMicAvailability() {//判断麦克风是否被占用
//        返回true就是没有被占用。
//        返回false就是被占用。
        Boolean available = true;
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try {
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                available = false;

            }

            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                recorder.stop();
                available = false;

            }
            recorder.stop();
        } finally {
            recorder.release();
            recorder = null;
        }
        return available;
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
}
