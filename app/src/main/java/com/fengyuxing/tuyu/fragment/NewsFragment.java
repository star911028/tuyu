package com.fengyuxing.tuyu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhy.http.okhttp.OkHttpUtils;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.MainActivity;
import com.fengyuxing.tuyu.activity.MyContactsActivity;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.util.UILImageLoader;
import com.fengyuxing.tuyu.yunxin.reminder.ReminderManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


//消息
public class NewsFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.friend_iv)
    ImageView friendIv;
    @BindView(R.id.close_push)
    ImageView closePush;
    @BindView(R.id.open_tv)
    TextView openTv;
    @BindView(R.id.push_tips_ll)
    LinearLayout pushTipsLl;
    private UILImageLoader mUILImageLoader;
    private Context mContext;
    private View view;
    private HttpManager mHttpManager;
    private RecentContactsFragment fragment;
    private static final int BAIDU_READ_PHONE_STATE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        mUILImageLoader = new UILImageLoader(mContext);
        mHttpManager = HttpManager.getInstance();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, null);
//            StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
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
        initData();
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
//        if (Build.VERSION.SDK_INT >= 23) {
//            showContacts();
//        }
    }

    private void initData() {

        addRecentContactsFragment();
    }


    // 将最近联系人列表fragment动态集成进来。
//    private void addRecentContactsFragment() {
//        fragment = new RecentContactsFragment();
//        // 设置要集成联系人列表fragment的布局文件
//        fragment.setContainerId(R.id.messages_fragment);
//
//        final UI activity = (UI) getActivity();
//
//        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
//        fragment = (RecentContactsFragment) activity.addFragment(fragment);
//    }

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();
        fragment.setContainerId(R.id.messages_fragment);

        final MainActivity activity = (MainActivity) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) activity.addFragment(fragment);

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                Log.e("onUnreadNumChanged", "unreadCount=" + unreadCount);
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                        break;
                    case Team:
                        NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
//                if (attachment instanceof GuessAttachment) {
//                    GuessAttachment guess = (GuessAttachment) attachment;
//                    return guess.getValue().getDesc();
//                } else if (attachment instanceof RTSAttachment) {
//                    return "[白板]";
//                } else if (attachment instanceof StickerAttachment) {
//                    return "[贴图]";
//                } else if (attachment instanceof SnapChatAttachment) {
//                    return "[阅后即焚]";
//                } else if (attachment instanceof RedPacketAttachment) {
//                    return "[红包]";
//                } else if (attachment instanceof RedPacketOpenedAttachment) {
//                    return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
//                }

                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }

                return null;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void initListener() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @OnClick({R.id.friend_iv, R.id.close_push, R.id.open_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.friend_iv://联系人
                // 打开单聊界面
//                NimUIKit.startP2PSession(mContext, "18571453917");
                Intent intent = new Intent(getActivity(), MyContactsActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
            case R.id.close_push:
                pushTipsLl.setVisibility(View.GONE);
                break;
            case R.id.open_tv:
                break;
        }
    }

    //请求权限
//    public void showContacts() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED
//                ) {
////            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
//            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
//            ActivityCompat.requestPermissions(getActivity(), new String[]{
//                    Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            }, BAIDU_READ_PHONE_STATE);
//        } else {
////            startLocation();
//            Log.e("onRequestPermissions", "获取到权限");
//        }
//    }

    //Android6.0申请权限的回调方法
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
//            case BAIDU_READ_PHONE_STATE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
////                    startLocation();
//                    Log.e("onRequestPermissions", "获取到权限");
//                } else {
//                    // 没有获取到权限，做特殊处理
//                    Toast.makeText(getActivity().getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }
}
