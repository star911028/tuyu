package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.MyCashoutAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MyEarDrawListActivity extends BaseActivity implements View.OnClickListener {
    //我的收益--提现记录
    private static final String TAG = "MyEarDrawListActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.emety_ll)
    LinearLayout emetyLl;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;
    private int page = 0;
    private List<DataList> data = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private MyCashoutAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eartxlist;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyEarDrawListActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        //   底部推荐列表
        layoutManager = new LinearLayoutManager(mContext);
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
                initData();//刷新数据
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
                initData();//刷新数据
            }
        });
//        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initData() {
        FindDrawLog();
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
    }


    @Override
    protected void initEventListeners() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void FindDrawLog() {//支出记录
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindDrawLog, map);
    }

    @Override
    protected void onCalllBack(retrofit2.Call<String> call, retrofit2.Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindDrawLog)) {
            Log.e("FindDrawLog", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            List<DataList> datalist = mMainArrayModel.getData();


            //测试数据
//             List<DataList> datalist = new ArrayList<>();
//            for(int i=0;i<20;i++){
//                DataList datat=new DataList();
//                datat.setContent("兑换"+i+"钻石");
//                datalist.add(datat);
//            }

            Log.e("MYonCalllBack", "result=" + result + " datalist.size()=" + datalist.size());
            if (mMainArrayModel.getCode().equals("1")) {
                if (datalist.size() == 0 && page == 0) {//
                    emetyLl.setVisibility(View.VISIBLE);
                    tvEmpty.setText("暂无支出记录");
                    refreshLayout.setVisibility(View.GONE);
                } else {
                    emetyLl.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < datalist.size(); i++) {
                    data.add(datalist.get(i));
                }
                if (adapter == null) {
                    adapter = new MyCashoutAdapter(mContext, data);
                    recyclerview.setAdapter(adapter);
                    adapter.setOnItemClickListener(mOnItemClickListener);
                } else {
                    adapter.notifyDataSetChanged();
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
            } else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private MyCashoutAdapter.OnItemClickListener mOnItemClickListener = new MyCashoutAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Log.e("onItemClick", "position=" + position + " getUserId=" + data.get(position).getContent());
        }

        @Override
        public void onItemLongClick(int position) {
            Log.e("onItemLongClick", "长按" + position);
        }
    };


    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;

        }
    }

}
