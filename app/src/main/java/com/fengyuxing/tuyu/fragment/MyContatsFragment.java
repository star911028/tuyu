package com.fengyuxing.tuyu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.bean.MainModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.UserInfoctivity;
import com.fengyuxing.tuyu.adapter.MyContatsRecyAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.UILImageLoader;

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
public class MyContatsFragment extends BaseFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder1;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder2;
    @BindView(R.id.emety_ll)
    LinearLayout emetyLl;
    private UILImageLoader mUILImageLoader;
    private Context mContext;
    private View view;
    private HttpManager mHttpManager;
    private MyContatsRecyAdapter adapter;
    int page = 0;
    boolean continueLoad = true;
    String Type = "";
    //    private List<FollowModel> data = new ArrayList<>();
    private List<DataList> data = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private DataList userdata2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mContext = getContext();
        mUILImageLoader = new UILImageLoader(mContext);
        mHttpManager = HttpManager.getInstance();
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
        Log.e("MyContatsFragment", "TabCheckEvent.getMsg()= " + event.getMsg());
        Type = event.getMsg();
        Log.e("MyContatsFragment", " Type1=" + Type);
        page = 0;
        data.clear();
        if (adapter != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
            recyclerview.removeAllViews();
            adapter.notifyDataSetChanged();
        }
        getData();//刷新数据

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
        getData();

    }

    private void getData() {//获取数据
        Log.e("MyContatsFragment", "getData=" + Type + "  " + MyApplication.getInstance().getCheckIndex());
        if (Type.equals("关注")) {//
            getMyFollows();//关注
        } else if (Type.equals("粉丝")) {
            getFans();//粉丝
        } else if (Type.equals("好友")) {
            getFriend();//好友
        } else {
            if (MyApplication.getInstance().getCheckIndex().equals("0")) {
                getMyFollows();//关注
            } else if (MyApplication.getInstance().getCheckIndex().equals("1")) {
                getFans();//粉丝
            } else if (MyApplication.getInstance().getCheckIndex().equals("2")) {
                getFriend();//好友
            }
        }
    }

    private void getFriend() {//好友
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindFriend, map);
    }

    private void getMyFollows() {//关注
        Log.e("MYonCalllBack", "getMyFollows");
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindFollow, map);
    }

    private void getFans() {//粉丝
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindFans, map);
    }

    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FollowUser)) {//关注
            Log.e("FollowUser","result="+result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                page = 0;
                data.clear();
                if (adapter != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
                    adapter.notifyDataSetChanged();
                }
                getFans();//刷新数据
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (url.contains(RetrofitService.Head + RetrofitService.FindFollow)) {
                Log.e("MyContats_FindFollow", " result=" + result);
            } else if (url.contains(RetrofitService.Head + RetrofitService.FindFriend)) {
                Log.e("MyContats_FindFriend", " result=" + result);
            } else if (url.contains(RetrofitService.Head + RetrofitService.FindFans)) {
                Log.e("MyContats_FindFans", " result=" + result);
            }
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            List<DataList> datalist = mMainArrayModel.getData();
            Log.e("MYonCalllBack", "result=" + result + " datalist.size()=" + datalist.size());
            if (datalist.size() == 0 && page == 0) {//
                emetyLl.setVisibility(View.VISIBLE);
                tvEmpty.setText("暂无数据");
                refreshLayout.setVisibility(View.GONE);
            } else {
                emetyLl.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < datalist.size(); i++) {
                data.add(datalist.get(i));
            }
            if (adapter == null) {
                adapter = new MyContatsRecyAdapter(mContext, data);
                recyclerview.setAdapter(adapter);
                adapter.setOnItemClickListener(mOnItemClickListener);
            } else {
                adapter.notifyDataSetChanged();
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();

        }

    }

    private MyContatsRecyAdapter.OnItemClickListener mOnItemClickListener = new MyContatsRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Log.e("onItemClick", "position=" + position + " getUserId=" + data.get(position).getUserId());
//            getFindUserInfo(data.get(position).getUserId());

            Intent intent = new Intent(getActivity(), UserInfoctivity.class);//
            intent.putExtra("userdata", (Serializable) userdata2);
            intent.putExtra("userinfoid", data.get(position).getUserId());
            startActivity(intent);
        }

        @Override
        public void onItemFollowClick(int position) {//关注
            Log.e("onItemFollowClick", "长按" + position);
            FollowUser(data.get(position).getUserId());
        }


        @Override
        public void onItemLongClick(int position) {
            Log.e("onItemLongClick", "长按" + position);
        }
    };

    private void FollowUser(String InfoID) {//关注用户
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("followId", InfoID);
        postRequest(RetrofitService.FollowUser, map);
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


}
