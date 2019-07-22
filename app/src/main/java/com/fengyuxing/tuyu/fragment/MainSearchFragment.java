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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.activity.MainRoomActivity;
import com.fengyuxing.tuyu.activity.UserInfoctivity;
import com.fengyuxing.tuyu.adapter.MainSearchRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.view.NeedPWDWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;


//我的联系人
public class MainSearchFragment extends BaseFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.main_ll)
    LinearLayout main_ll;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder2;
    @BindView(R.id.emety_ll)
    LinearLayout emetyLl;
    private Context mContext;
    private View view;
    private MainSearchRecyAdapter adapter;
    int page = 0;
    private String SearchType = "房间";//默认房间
    private DataList carddatapos;
    private String Roomid = "";
    //    private List<FollowModel> data = new ArrayList<>();
    private List<DataList> data = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private String searchtext = "";
    private DataList userdata2;
    private NeedPWDWindow mNeedPWDWindow;
    private Boolean toastnews = false;
    private Boolean Needpass = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mContext = getContext();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mycontacs, null);
            initView();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        unbinder2 = ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe
    public void onEventMainThread(TabCheckEvent event) {//
        Log.e("onEventMainThread", "msg1= " + event.getMsg1() + "  msg2=" + event.getMsg2());
        if (event.getMsg2() != null) {
            if (event.getMsg1().startsWith("搜索")) {
                searchtext = event.getMsg1().replace("搜索", "");//搜索的内容
                SearchType = event.getMsg2();//搜索的类型
                if (SearchType != null) {
                    page = 0;
                    data.clear();
                    if (adapter != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
                        adapter.notifyDataSetChanged();
                    }
                    getData();//刷新数据
                } else {
                    data.clear();
                    recyclerview.removeAllViews();
                    adapter.notifyDataSetChanged();
                }

            }
        }

    }

    private void initView() {
        unbinder = ButterKnife.bind(this, view);
        //   底部推荐列表
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale));
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                data.clear();
                if (adapter != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
                    adapter.notifyDataSetChanged();
                }
                getData();//刷新数据
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                if (adapter != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
                    adapter.notifyDataSetChanged();
                }
                getData();//刷新数据
            }
        });
    }

    private void getData() {//获取数据
        if (SearchType.equals("房间")) {//
            if (searchtext.length() == 0) {
                Toast.makeText(mContext, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh();
            } else {
                SearchRoom(searchtext);//搜索房间
            }
        } else if (SearchType.equals("用户")) {
            if (searchtext.length() == 0) {
                Toast.makeText(mContext, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh();
            } else {
                SearchUser(searchtext);//搜索用户
            }
        }
    }


    private void SearchRoom(String searchKey) {//搜索房间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("searchKey", searchKey);
        map.put("page", page);
        postRequest(RetrofitService.SearchRoom, map);
    }

    private void SearchUser(String searchKey) {//搜索用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("searchKey", searchKey);
        map.put("page", page);
        postRequest(RetrofitService.SearchUser, map);
    }

    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindRoomInfo)) {//进入直播间查询信息
            Log.e("FindRoomInfo", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
//                if (Needpass) {
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
//                    intentyh.putExtra("roompass", MyApplication.getInstance().getInputpass());//传递房间密码
//                    intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
//                    startActivity(intentyh);
//                } else {
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
//                    intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
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
                if(mMainModel.getErrorMsg().contains("拉黑")){
                    Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }else {
                    Needpass = false;//重新打开密码输入框
                    if(toastnews){
                        Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }else {
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
        } else {
            if (url.contains(RetrofitService.Head + RetrofitService.SearchRoom)) {
                Log.e("SearchRoom", " result=" + result);
            } else if (url.contains(RetrofitService.Head + RetrofitService.SearchUser)) {
                Log.e("SearchUser", " result=" + result);
            }
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            List<DataList> datalist = mMainArrayModel.getData();
            if (datalist.size() == 0 && page == 0) {//
                emetyLl.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            } else {
                emetyLl.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < datalist.size(); i++) {
                data.add(datalist.get(i));
            }
            if (adapter == null) {
                adapter = new MainSearchRecyAdapter(mContext, data);
                recyclerview.setAdapter(adapter);
                adapter.setOnItemClickListener(mOnItemClickListener);
            } else {
                adapter.notifyDataSetChanged();
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }


    private MainSearchRecyAdapter.OnItemClickListener mOnItemClickListener = new MainSearchRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Log.e("onItemClick", "position=" + position + " getClassifyName=" + data.get(position).getClassifyName());
            if (data.get(position).getClassifyName() != null) {
                //进入房间
                Roomid = data.get(position).getRoomId();
                toastnews = false;
                getFindRoomInfo();

//
//                if (data.get(position).getNeedPassword().equals("true")) {//需要密码  打开弹窗
//                    Roomid = data.get(position).getRoomId();
//                    carddatapos = data.get(position);
//                    MyApplication.getInstance().setInputpass("");//重置
//                    NeedPWDwindow();
//                } else {//不需要密码
//                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                    intentyh.putExtra("roomid", data.get(position).getRoomId());//传递房间id
//                    intentyh.putExtra("roomdata", (Serializable) data.get(position));//传递房间数据
//                    startActivity(intentyh);
//                }
            } else {
                //进入个人主页
//                getFindUserInfo(data.get(position).getUserId());
                Intent intent = new Intent(getActivity(), UserInfoctivity.class);//
                intent.putExtra("userdata", (Serializable) userdata2);
                intent.putExtra("userinfoid", data.get(position).getUserId());
                startActivity(intent);
            }
        }

        @Override
        public void onItemLongClick(int position) {
            Log.e("onItemLongClick", "长按" + position);
        }
    };

//    public void getFindUserInfo(String otherid) {//查询他人资料
//        WeakHashMap map = new WeakHashMap();
//        map.put("userId", MyApplication.getInstance().getUserId());
//        map.put("token", MyApplication.getInstance().getToken());
//        map.put("infoUserId", otherid);
//        postRequest(RetrofitService.FindUserInfo, map);
//    }

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
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder2.unbind();
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


}
