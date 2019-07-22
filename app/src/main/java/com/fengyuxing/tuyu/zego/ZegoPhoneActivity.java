package com.fengyuxing.tuyu.zego;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
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
import com.zego.zegoavkit2.IZegoMediaPlayerCallback;
import com.zego.zegoavkit2.ZegoMediaPlayer;
import com.zego.zegoavkit2.soundlevel.IZegoSoundLevelCallback;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelMonitor;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoIM;
import com.zego.zegoliveroom.entity.ZegoAudioFrame;
import com.zego.zegoliveroom.entity.ZegoConversationMessage;
import com.zego.zegoliveroom.entity.ZegoPlayStreamQuality;
import com.zego.zegoliveroom.entity.ZegoPublishStreamQuality;
import com.zego.zegoliveroom.entity.ZegoRoomMessage;
import com.zego.zegoliveroom.entity.ZegoUserState;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.BaseActivity;

import java.nio.ByteBuffer;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Play_BeginRetry;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Play_RetrySuccess;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Play_TempDisconnected;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Publish_BeginRetry;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Publish_RetrySuccess;
import static com.zego.zegoaudioroom.ZegoAudioLiveEvent.Audio_Publish_TempDisconnected;

public class ZegoPhoneActivity extends BaseActivity implements SensorEventListener {

    private static final String TAG = "ZegoPhoneActivity";
    @BindView(R.id.log)
    TextView log;
    @BindView(R.id.advanced)
    TextView advanced;
    @BindView(R.id.user_recyclerView)
    RecyclerView userRecyclerView;
    @BindView(R.id.btn_mic)
    ToggleButton btnMic;
    @BindView(R.id.start_push)
    ToggleButton start_push;
    @BindView(R.id.stop_push)
    ToggleButton stop_push;
    @BindView(R.id.exit_phone)
    ImageButton exitPhone;
    @BindView(R.id.btn_speaker)
    ToggleButton btnSpeaker;
    @BindView(R.id.boom)
    LinearLayout boom;
    @BindView(R.id.play)
    Button play;
    @BindView(R.id.boom_advanced)
    LinearLayout boomAdvanced;
    private ZegoAudioRoom zegoAudioRoom;
    private String currentRoomId;
    private String appTitle;
    private ZegoUserState zegoUserState = new ZegoUserState();
    private RecyclerGridViewAdapter recyclerGridViewAdapter;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zego_phone;
    }


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void initView(Bundle savedInstanceState) {

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
                        Log.d(" Handler handleMessa", restartState + "");
                        /**
                         * 超过10次后给用户提示,只提示1次
                         */
                    } else if (isPromptToast) {
                        isPromptToast = false;
                        Log.d(" Handler handleMessage", restartCount + "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ZegoPhoneActivity.this, getString(R.string.zg_text_app_restart_publish_failure), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                return false;
            }
        });
        initZego();
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


    @Override
    protected void onDestroy() {
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
            unregisterReceiver(mReceiver);
        }

        super.onDestroy();
    }

    private void logout() {

        boolean success = zegoAudioRoom.logoutRoom();

        hasLogin = false;
        hasPublish = false;


      Log.d("logout: %s", success+"  ");
    }


    private void removeCallbacks() {
        zegoAudioRoom.setAudioRoomDelegate(null);
        zegoAudioRoom.setAudioPublisherDelegate(null);
        zegoAudioRoom.setAudioPlayerDelegate(null);
        zegoAudioRoom.setAudioLiveEventDelegate(null);
        zegoAudioRoom.setAudioRecordDelegate(null);
        zegoAudioRoom.setAudioDeviceEventDelegate(null);
        zegoAudioRoom.setAudioPrepareDelegate(null);
        zegoAudioRoom.setAudioAVEngineDelegate(null);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @OnClick({R.id.btn_mic, R.id.exit_phone, R.id.btn_speaker, R.id.play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_mic:
                break;
            case R.id.exit_phone:
                break;
            case R.id.btn_speaker:
                break;
            case R.id.play:
                break;
        }
    }

    public void initZego() {
          zegoAudioRoom = ((MyApplication) getApplication()).getAudioRoomClient();
        Intent startIntent = getIntent();
        String roomId = startIntent.getStringExtra("roomId");
        if (TextUtils.isEmpty(roomId)) {
            Toast.makeText(this, "房间号为空", Toast.LENGTH_LONG).show();
            finish();
        } else {
            currentRoomId = roomId;
            appTitle = "房间号";

            setupCallbacks();
            login(roomId);
        }

        initMediaPlayer();

        GridLayoutManager mgr = new GridLayoutManager(this, 3);
        userRecyclerView.setLayoutManager(mgr);
        recyclerGridViewAdapter = new RecyclerGridViewAdapter(this.getApplicationContext(), userRecyclerView);
        userRecyclerView.setAdapter(recyclerGridViewAdapter);

        zegoAudioRoom.enableAEC(true);


        btnMic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableMic(isChecked);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(ZegoPhoneActivity.this, LogsActivity.class);
                startActivity(startIntent);
            }
        });

        start_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                zegoAudioRoom.startPublish();
            }
        });

        stop_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                zegoAudioRoom.stopPublish();
            }
        });

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBoom) {
                    hasBoom = false;
                    boomAdvanced.setVisibility(View.GONE);
                } else {
                    hasBoom = true;
                    boomAdvanced.setVisibility(View.VISIBLE);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stops = getString(R.string.zg_btn_text_stop);
                String plays = getString(R.string.zg_btn_text_play);
                if (play.getText().equals(stops)) {
                    play.setText(plays);
                    stopMusic();
                } else {
                    play.setText(stops);
                    playMusic();
                }
            }
        });

        btnSpeaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                speaker = !isChecked;
                builtinSpeakerOn(isChecked);
            }
        });

        btnSpeaker.setChecked(true);
        builtinSpeakerOn(true);
        registerHeadsetPlug();

        exitPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                        Log.d("onLoginCompletion: %d", state + "  ");
                        if (state == 0) {
                            Log.d("zegoUserState", MyApplication.getInstance().getUserId() + "  "+MyApplication.getInstance().getUserName());
                            hasLogin = true;
                            zegoUserState.userID = MyApplication.getInstance().getUserId();
                            zegoUserState.userName = MyApplication.getInstance().getUserName();
                            recyclerGridViewAdapter.addUser(zegoUserState);
                            recyclerGridViewAdapter.setSelfZegoUser(zegoUserState);
                            enableMic(true);

                            // TODO 登陆成功
                        } else {
                            Toast.makeText(ZegoPhoneActivity.this, String.format("Login Error: %d", state), Toast.LENGTH_LONG).show();
                            // TODO 登陆失败错误码
                        }
                    }
                });

        Log.d("login: %s", "   " + success);
        if (!success) {
            // TODO 登陆失败
            reInitZegoSDK();
        }
    }
    private void reInitZegoSDK() {
        MyApplication.getInstance().reInitZegoSDK();
    }

    private void setupCallbacks() {
        Log.e("zegoAudioRoom","zegoAudioRoom="+zegoAudioRoom);
        zegoAudioRoom.setAudioRoomDelegate(new ZegoAudioRoomDelegate() {
            @Override
            public void onKickOut(int errorCode, String roomId) {
                Log.d("onKickOut room:", roomId + "  " + errorCode);
                // TODO 被踢出房间
            }

            @Override
            public void onDisconnect(int errorCode, String roomId) {
                Log.d("onDisconnect room:", roomId + "  " + errorCode);
                // TODO 与房间断开连接, 错误码
                recyclerGridViewAdapter.streamStateUpdateAll(1);
            }

            @Override
            public void onStreamUpdate(final ZegoAudioStreamType zegoAudioStreamType, final ZegoAudioStream zegoAudioStream) {
                Log.d("onStreamUpdate, ", zegoAudioStreamType + "  " + zegoAudioStream.getStreamId());
                ZegoUserState zegoUserState = new ZegoUserState();
                zegoUserState.userID = zegoAudioStream.getUserId();
                zegoUserState.userName = zegoAudioStream.getUserName();
                switch (zegoAudioStreamType) {
                    case ZEGO_AUDIO_STREAM_ADD:
                        // TODO 新增流
                        recyclerGridViewAdapter.addUser(zegoUserState);
                        recyclerGridViewAdapter.bindUserInfoStreamID(zegoAudioStream);
                        String extraInfo = zegoAudioStream.getExtraInfo();
                        if (extraInfo != null) {
                            JSONObject jsonObject = JSONObject.parseObject(extraInfo);
                            if (jsonObject != null) {
                                StreamState streamState = jsonObject.getObject("stream_state", StreamState.class);
                                recyclerGridViewAdapter.updateMuteState(streamState.getEnable_mic(), zegoAudioStream.getUserName());
                            }
                        }
                        break;
                    case ZEGO_AUDIO_STREAM_DELETE:
                        // TODO 删除流
                        recyclerGridViewAdapter.removeUser(zegoUserState);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onUserUpdate(ZegoUserState[] userList, int updateType) {
                Log.d("onUserUpdate, ", " " + (updateType == ZegoIM.UserUpdateType.Increase ? "Increase" : "Total"));
                for (ZegoUserState user : userList) {
                    Log.d("onUserUpdate,", " " + user.userName + "; updateFlag: " + (user.updateFlag == ZegoIM.UserUpdateFlag.Added ? "Add" : "Delete"));
                    if (user.updateFlag == ZegoIM.UserUpdateFlag.Added) {
                        // TODO 新增用户
                        recyclerGridViewAdapter.addUser(user);
                    } else {
                        // TODO 删除用户
                        recyclerGridViewAdapter.removeUser(user);
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
                Log.d("ZegoohoneActivity", roomId + "   " + conversationId);
            }

            @Override
            public void onRecvBigRoomMessage(String s, com.zego.zegoliveroom.entity.ZegoBigRoomMessage[] zegoBigRoomMessages) {
                Log.d("ZegoohoneActivity", s);
            }

            @Override
            public void onRecvCustomCommand(String userId, String userName, String content, String roomId) {
                Log.d("ZegoohoneActivity", userId + "  " + roomId + "  " + content);

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
                            recyclerGridViewAdapter.updateMuteState(streamState.getEnable_mic(), zegoStreamInfo.getUserName());
                    }
                }
            }
        });

        zegoAudioRoom.setAudioPublisherDelegate(new ZegoAudioLivePublisherDelegate() {

            @Override
            public void onPublishStateUpdate(int stateCode, String streamId, HashMap<String, Object> info) {
                Log.d("ZegoohoneActivity", stateCode + "  " + streamId + "  " + info);

                ZegoAudioStream myStream = new ZegoAudioStream(streamId, PrefUtils.getUserId(), PrefUtils.getUserName());
                if (stateCode == 0) {
                    hasPublish = true;
                    publishStreamId = streamId;

                    recyclerGridViewAdapter.bindUserInfoStreamID(myStream);

                    /**
                     * 清空重试计数器
                     */

                } else {
                    // TODO 推流失败
                    recyclerGridViewAdapter.streamStateUpdate(1, myStream);
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
                Log.d("ZegoohoneActivity",
                        streamId + "  " + zegoStreamQuality.quality + "  " + zegoStreamQuality.akbps);
                RecyclerGridViewAdapter.CommonStreamQuality commonStreamQuality = EntityConversion.publishQualityToCommonStreamQuality(zegoStreamQuality);

                // 推流质量更新
                recyclerGridViewAdapter.updateQualityUpdate(streamId, commonStreamQuality);
            }
        });
        zegoAudioRoom.setAudioPlayerDelegate(new ZegoAudioLivePlayerDelegate() {
            @Override
            public void onPlayStateUpdate(int stateCode, ZegoAudioStream zegoAudioStream) {
                Log.d("ZegoohoneActivity", stateCode + "    " + zegoAudioStream.getStreamId());
                if (stateCode == 0) {
                    recyclerGridViewAdapter.bindUserInfoStreamID(zegoAudioStream);
                } else {
                    recyclerGridViewAdapter.streamStateUpdate(1, zegoAudioStream);
                }

            }

            @Override
            public void onPlayQualityUpdate(String streamId, ZegoPlayStreamQuality zegoStreamQuality) {

                Log.d("ZegoohoneActivity",
                        streamId + zegoStreamQuality.quality + "  " + zegoStreamQuality.audioBreakRate);
                RecyclerGridViewAdapter.CommonStreamQuality commonStreamQuality = EntityConversion.playQualityToCommonStreamQuality(zegoStreamQuality);
                // 拉流质量更新
                recyclerGridViewAdapter.updateQualityUpdate(streamId, commonStreamQuality);
                ZegoAudioStream zegoAudioStream = new ZegoAudioStream();
                zegoAudioStream.setStreamId(streamId);
            }
        });
        zegoAudioRoom.setAudioLiveEventDelegate(new ZegoAudioLiveEventDelegate() {
            @Override
            public void onAudioLiveEvent(ZegoAudioLiveEvent zegoAudioLiveEvent, HashMap<String, String> info) {
                Log.d("ZegoohoneActivity", " " + zegoAudioLiveEvent + "" + info);

                ZegoAudioStream mZegoAudioStream = new ZegoAudioStream();
                String streamID = info.get("StreamID");
                mZegoAudioStream.setStreamId(streamID);
                // 开始
                if (zegoAudioLiveEvent == Audio_Play_BeginRetry || zegoAudioLiveEvent == Audio_Publish_BeginRetry) {
                    recyclerGridViewAdapter.streamStateUpdate(2, mZegoAudioStream);
                } else if (zegoAudioLiveEvent == Audio_Play_RetrySuccess || zegoAudioLiveEvent == Audio_Publish_RetrySuccess) {
                    recyclerGridViewAdapter.streamStateUpdate(0, mZegoAudioStream);
                } else if (zegoAudioLiveEvent == Audio_Play_TempDisconnected || zegoAudioLiveEvent == Audio_Publish_TempDisconnected) {
                    recyclerGridViewAdapter.streamStateUpdate(1, mZegoAudioStream);
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
                Log.d("ZegoohoneActivity", deviceName + " " + errorCode);
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

            }

            @Override
            public void onAVEngineStop() {
                Log.d("ZegoohoneActivity", "onAVEngineStop");
            }
        });
    }

    private void soundLevel() {
        ZegoSoundLevelMonitor.getInstance().setCallback(new IZegoSoundLevelCallback() {
            @Override
            public void onSoundLevelUpdate(ZegoSoundLevelInfo[] zegoSoundLevelInfos) {
                for (ZegoSoundLevelInfo zegoSoundLevelInfo : zegoSoundLevelInfos) {
                    if (recyclerGridViewAdapter != null) {
                        recyclerGridViewAdapter.soundLevelUpdate(zegoSoundLevelInfo);
                    }
                }
            }

            @Override
            public void onCaptureSoundLevelUpdate(ZegoSoundLevelInfo zegoSoundLevelInfo) {
                if (recyclerGridViewAdapter != null) {
                    recyclerGridViewAdapter.soundLevelUpdate(zegoSoundLevelInfo);
                }
            }
        });

        ZegoSoundLevelMonitor.getInstance().start();
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

                recyclerGridViewAdapter.updateMuteState(enable, PrefUtils.getUserName());
            }
        }

    }

    private void initMediaPlayer() {
        zegoMediaPlayer = new ZegoMediaPlayer();
        zegoMediaPlayer.init(ZegoMediaPlayer.PlayerTypeAux);


        zegoMediaPlayer.setCallback(new IZegoMediaPlayerCallback() {
            @Override
            public void onPlayStart() {
                play.post(new Runnable() {
                    @Override
                    public void run() {
                        play.setText(getString(R.string.zg_btn_text_stop));
                    }
                });
            }

            @Override
            public void onPlayError(int i) {

            }

            @Override
            public void onPlayPause() {

            }

            @Override
            public void onPlayStop() {

            }

            @Override
            public void onPlayResume() {

            }

            @Override
            public void onVideoBegin() {

            }

            @Override
            public void onAudioBegin() {

            }

            @Override
            public void onPlayEnd() {
                play.post(new Runnable() {
                    @Override
                    public void run() {
                        play.setText(getString(R.string.zg_btn_text_play));
                    }
                });
            }

            @Override
            public void onBufferBegin() {

            }

            @Override
            public void onBufferEnd() {

            }

            @Override
            public void onSeekComplete(int i, long l) {

            }

            @Override
            public void onSnapshot(Bitmap bitmap) {

            }

            @Override
            public void onLoadComplete() {

            }
        });


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
                path = SystemUtil.copyAssetsFile2Phone(ZegoPhoneActivity.this, "test.mp3");
                if (path != null) {
                    zegoMediaPlayer.start(path, false);
                } else {
                    Toast.makeText(ZegoPhoneActivity.this, getString(R.string.zg_toast_text_play_path_error), Toast.LENGTH_LONG).show();
                }
            }
        });

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
                        btnSpeaker.setEnabled(true);
                        btnSpeaker.setAlpha(1f);
                    } else if (intent.getIntExtra("state", 0) == 1) {
                        btnSpeaker.setEnabled(false);
                        btnSpeaker.setAlpha(0.5f);
                    }
                }
            }
        }
    }
}
