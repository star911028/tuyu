package com.fengyuxing.tuyu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.activity.MainRoomActivity;
import com.fengyuxing.tuyu.adapter.MainCardRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.UILImageLoader;
import com.fengyuxing.tuyu.view.NeedPWDWindow;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//主页卡片列表
public class MainCardFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    //    @BindView(R.id.refreshLayout)
//    SmartRefreshLayout refreshLayout;
//    @BindView(R.id.main_ns)
//    NestedScrollView main_ns;
    @BindView(R.id.main_ll)
    LinearLayout main_ll;

    private UILImageLoader mUILImageLoader;
    private Context mContext;
    private View view;
    private HttpManager mHttpManager;
    private LinearLayoutManager layoutManager;
    private MainCardRecyAdapter recyAdapter3;
    private List<DataList> carddata = new ArrayList<>();
    private DataList carddatapos;
    private String Roomid = "";
    private int page = 0;
    String Type = "最近";
    private NeedPWDWindow mNeedPWDWindow;
    private Boolean toastnews = false;
    private Boolean Needpass = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mUILImageLoader = new UILImageLoader(mContext);
        mHttpManager = HttpManager.getInstance();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_card, null);
            initView();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Subscribe
    public void onEventMainThread(TabCheckEvent event) {//
        Log.e("onEventMainThreadMain", "TabCheckEvent.getMsg()= " + event.getMsg());
        if (event.getMsg() != null) {
            if (event.getMsg().equals("最近") || event.getMsg().equals("推荐") || event.getMsg().equals("其他")) {
                Type = event.getMsg();
                Log.e("onEventMainThreadMain", " Type1=" + Type);
                page = 0;
                carddata.clear();
                if (recyAdapter3 != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
                    recyAdapter3.notifyDataSetChanged();
                }
                getData();
            }
        }
    }


    public void getData() {
        if (Type.equals("最近")) {
            FindRecentRoom();
        } else if (Type.equals("推荐")) {
            FindRecommendRoom();
        } else {
            if (Type.contains("其他")) {
                String typeid = Type.replace("其他", "");
                FindClassifyRoom(typeid);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void initView() {
        unbinder = ButterKnife.bind(this, view);
        //   底部推荐列表
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);

        //下拉刷新
//        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
//        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale));
//        //刷新
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                page = 0;
//                carddata.clear();
//                if (recyAdapter3 != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
//                    recyAdapter3.notifyDataSetChanged();
//                }
//                getData();//刷新数据
//            }
//        });
//        //加载更多
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                page++;
//                if (recyAdapter3 != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
//                    recyAdapter3.notifyDataSetChanged();
//                }
//                getData();//刷新数据
//            }
//        });
//        getData();//刷新数据
    }


    public void FindRecentRoom() {
        //查询最近访问的直播间
        Log.e("FindRecentRoom", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken());
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("page", "0").add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).build();
        final Request request = new Request.Builder().url(NetConstant.API_FindRecentRoom).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() == null) {
                    return;
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e("FindRecentRoom", "responseStr=" + responseStr);
                if (getActivity() == null) {
                    return;
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainArrayModel mMainArrayModel = new Gson().fromJson(responseStr,
                                    new TypeToken<MainArrayModel>() {
                                    }.getType());
                            if (mMainArrayModel.getCode().equals("1")) {

                                for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                                    carddata.add(mMainArrayModel.getData().get(i));
                                }
                                if (recyAdapter3 == null) {
                                    recyAdapter3 = new MainCardRecyAdapter(mContext, carddata);
                                    recycleView.setAdapter(recyAdapter3);
                                    recyAdapter3.setOnItemClickListener(mOnItemClickListener);
                                } else {
                                    recyAdapter3.notifyDataSetChanged();
                                }
//                                refreshLayout.finishRefresh();
//                                refreshLayout.finishLoadmore();
                            } else if (mMainArrayModel.getCode().equals("0")) {
                                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                                MyApplication.getInstance().setUserId("");
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private MainCardRecyAdapter.OnItemClickListener mOnItemClickListener = new MainCardRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Roomid = carddata.get(position).getRoomId();
            toastnews = false;
            getFindRoomInfo();
        }

        @Override
        public void onItemLongClick(int position) {
            Log.e("onItemLongClick", "长按" + position);
        }
    };

    public void FindRecommendRoom() {
        //查询推荐的直播间
        Log.e("FindRecommendRoom", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken());
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("page", "0").add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).build();
        final Request request = new Request.Builder().url(NetConstant.API_FindRecommendRoom).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e("FindRecommendRoom", "responseStr=" + responseStr);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainArrayModel mMainArrayModel = new Gson().fromJson(responseStr,
                                    new TypeToken<MainArrayModel>() {
                                    }.getType());
                            if (mMainArrayModel.getCode().equals("1")) {
                                if (mMainArrayModel.getData() != null) {

                                    for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                                        carddata.add(mMainArrayModel.getData().get(i));
                                    }
                                    if (recyAdapter3 == null) {
                                        recyAdapter3 = new MainCardRecyAdapter(mContext, carddata);
                                        recycleView.setAdapter(recyAdapter3);
                                        recyAdapter3.setOnItemClickListener(mOnItemClickListener);
                                    } else {
                                        recyAdapter3.notifyDataSetChanged();
                                    }
//                                    refreshLayout.finishRefresh();
//                                    refreshLayout.finishLoadmore();
                                }
                            } else if (mMainArrayModel.getCode().equals("0")) {
                                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                                MyApplication.getInstance().setUserId("");
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void FindClassifyRoom(String classifyId) {
        //查询分类带直播间
        Log.e("FindClassifyRoom", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken());
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                .add("classifyId", classifyId).add("page", page + "")
                .build();
        final Request request = new Request.Builder().url(NetConstant.API_FindClassifyRoom).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_request_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e("FindClassifyRoom", "responseStr=" + responseStr);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainArrayModel mMainArrayModel = new Gson().fromJson(responseStr,
                                    new TypeToken<MainArrayModel>() {
                                    }.getType());
                            carddata.clear();
                            if (mMainArrayModel.getCode().equals("1")) {
                                if (mMainArrayModel.getData() != null) {

                                    for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                                        carddata.add(mMainArrayModel.getData().get(i));
                                    }
                                    if (recyAdapter3 == null) {
                                        recyAdapter3 = new MainCardRecyAdapter(mContext, carddata);
                                        recycleView.setAdapter(recyAdapter3);
                                        recyAdapter3.setOnItemClickListener(mOnItemClickListener);
                                    } else {
                                        recyAdapter3.notifyDataSetChanged();
                                    }
//                                    refreshLayout.finishRefresh();
//                                    refreshLayout.finishLoadmore();
                                }
                            } else if (mMainArrayModel.getCode().equals("0")) {
                                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                                MyApplication.getInstance().setUserId("");
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }


    private void initListener() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(getActivity());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //密码弹出框
    private void NeedPWDwindow() {//
        mNeedPWDWindow = new NeedPWDWindow(getActivity(),
                new View.OnClickListener() {
                    // 全部
                    @Override
                    public void onClick(View view) {
                        Integer point = (Integer) view.getTag();
                        switch (point) {
                            case 4://取消
                                mNeedPWDWindow.dismiss();
                                break;
                            case 5://立即进入
                                mNeedPWDWindow.dismiss();
                                toastnews = true;
                                getFindRoomInfo();
                                //TODO
                                break;
                        }
                    }
                });
        mNeedPWDWindow.setClippingEnabled(false);
        mNeedPWDWindow.showAtLocation(main_ll, Gravity.CENTER, 0, 0);//或者显示在指定父布局上边的指定位置
    }

    public void getFindRoomInfo() {//进入直播间查询信息
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        if (Needpass) {//有密码才传这个字段
            map.put("password", MyApplication.getInstance().getInputpass());
        }
        Log.e("getFindRoomInfo", "password=" + MyApplication.getInstance().getInputpass());
        postRequest(RetrofitService.FindRoomInfo, map);
    }


    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindRoomInfo)) {//进入直播间查询信息
            Log.e("FindRoomInfo", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                Log.e("JoinRoom", "getMyRoomid=" + MyApplication.getInstance().getMyRoomid() + "  getRoomId=" + mMainModel.getData().getRoomId());
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
        }
    }


}
