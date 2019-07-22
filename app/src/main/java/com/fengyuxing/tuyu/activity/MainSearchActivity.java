package com.fengyuxing.tuyu.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.ViewPagerAdapter;
import com.fengyuxing.tuyu.fragment.MainSearchFragment;
import com.fengyuxing.tuyu.util.ClearableEditText;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//首页搜索
public class MainSearchActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private static final String TAG = "MainSearchActivity";
    @BindView(R.id.main_et)
    ClearableEditText mainEt;
    @BindView(R.id.canel_tv)
    TextView canelTv;
    @BindView(R.id.top_ll)
    LinearLayout topLl;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.follow_sv)
    HorizontalScrollView followSv;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.main_ll)
    LinearLayout mainLl;
    private String[] titles = new String[]{"房间", "用户"};
    private List<Fragment> fragments = new ArrayList<>();
    private int index = 0;
    private String searchtype="房间";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mainsearch;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MainSearchActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        SetTab();
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
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {//当前页
                if (position == 0) {
                    searchtype="房间";
                    if(mainEt.getText().toString().trim().length()>0){
                        EventBus.getDefault().post(new TabCheckEvent("搜索"+mainEt.getText().toString().trim(),searchtype));
                    }
                } else if (position == 1) {
                    searchtype="用户";
                    if(mainEt.getText().toString().trim().length()>0){
                        EventBus.getDefault().post(new TabCheckEvent("搜索"+mainEt.getText().toString().trim(),searchtype));
                    }
                }
                Log.e("onEventMainThread", "tab.getPosition()=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//键盘搜索框
                    Log.e("onEditorAction","搜索");
                    //开始搜索
                    if(mainEt.getText().toString().trim().length()>0){
                        EventBus.getDefault().post(new TabCheckEvent("搜索"+mainEt.getText().toString().trim(),searchtype));
                    }else {
                        Toast.makeText(mContext, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                    }
                    //关闭软键盘
                    hintKeyBoard();
                    return true;
                }
                return false;
            }
        });
        mainEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if(mainEt.getText().toString().trim().length()==0){
                    Log.e("afterTextChanged","mainEt="+mainEt.getText().toString().trim());
                    EventBus.getDefault().post(new TabCheckEvent("搜索"+mainEt.getText().toString().trim(),searchtype));
                }
            }
        });
    }
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @OnClick(R.id.canel_tv)
    public void onViewClicked() {
        finish();
    }

    public void SetTab() {
        //设置TabLayout标签的显示方式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        setIndicatorWidth(tabLayout, 60);
        //循环注入标签
        for (String tab : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }

//        设置TabLayout点击事件
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(0);
        for (int i = 0; i < titles.length; i++) {
            fragments.add(new MainSearchFragment());
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.e("SetTab", "length=" + titles.length + "    index=" + index);
        viewPager.setCurrentItem(index);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.transparent20));//transparent20   mine_item_text
        updateTabTextView(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()), true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabTextView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View getTabView(int currentPosition) {//tab item样式
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(titles[currentPosition]);
        return view;
    }

    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (isSelect) {
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setText(tab.getText());
            tabSelect.setTextSize(18);
            Drawable drawableBottom = getResources().getDrawable(
                    R.drawable.tabline);
            tabSelect.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, drawableBottom);
            tabSelect.setCompoundDrawablePadding(0);
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setText(tab.getText());
            tabUnSelect.setTextSize(15);
            Drawable drawableBottom = getResources().getDrawable(
                    R.drawable.tab_unsec);
            tabUnSelect.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, drawableBottom);
            tabUnSelect.setCompoundDrawablePadding(0);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
