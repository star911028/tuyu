package com.fengyuxing.tuyu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


//主页卡片列表
public class MainRankautoFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.img_iv1)
    CircleImageView imgIv1;
    @BindView(R.id.img_iv2)
    CircleImageView imgIv2;
    @BindView(R.id.img_iv3)
    CircleImageView imgIv3;
    @BindView(R.id.main_ll)
    LinearLayout mainLl;
    Unbinder unbinder1;
    private Context mContext;
    private View view;
    private HttpManager mHttpManager;
    private LinearLayoutManager layoutManager;
    private List<DataList> carddata = new ArrayList<>();
    private DataList carddatapos;
    private String Roomid = "";
    private int page = 0;
    String Type = "最近";
    private int[] bgAraay = {R.drawable.main_bank_bg1, R.drawable.main_bank_bg2,};//R.drawable.main_bank_bg3
    private int[] corAraay = {R.color.bank_col1, R.color.bank_col2};//,R.color.bank_col3
    private int[] textAraay = {R.string.bank_tv1, R.string.bank_tv2};//,R.string.bank_tv3
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mHttpManager = HttpManager.getInstance();
        if (view == null) {
            view = inflater.inflate(R.layout.minerank_auto, null);
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
            Type = event.getMsg();
            Log.e("onEventMainThreadMain", " Type1=" + Type);
            if (Type.equals("魅力榜")) {
                mainLl.setBackgroundResource(bgAraay[0]);  //设置背景颜色
                typeTv.setTextColor(mContext.getResources().getColor(corAraay[0]));  //设置文字颜色
                typeTv.setText(textAraay[0]);  //设置文字
            } else {
                mainLl.setBackgroundResource(bgAraay[1]);  //设置背景颜色
                typeTv.setTextColor(mContext.getResources().getColor(corAraay[1]));  //设置文字颜色
                typeTv.setText(textAraay[1]);  //设置文字
            }
        }

    }


    public void getData() {

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void initView() {
        unbinder = ButterKnife.bind(this, view);

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


}
