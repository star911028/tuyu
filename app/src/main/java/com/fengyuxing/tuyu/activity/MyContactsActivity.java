package com.fengyuxing.tuyu.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.ViewPagerAdapter;
import com.fengyuxing.tuyu.fragment.MyContatsFragment;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyContactsActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    //我的联系人
    private static final String TAG = "MyContactsActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.follow_sv)
    HorizontalScrollView followSv;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private String[] titles = new String[]{"关注", "粉丝", "好友"};
    private List<Fragment> fragments = new ArrayList<>();
    private int index = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mycontacts;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MyContactsActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
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
                Log.e("MyContactsActivity","onPageScrolled position="+position);
//                if (position == 0) {
//                    EventBus.getDefault().post(new TabCheckEvent("关注"));
//                } else if (position == 1) {
//                    EventBus.getDefault().post(new TabCheckEvent("粉丝"));
//                } else if (position == 2) {
//                    EventBus.getDefault().post(new TabCheckEvent("好友"));
//                }
            }

            @Override
            public void onPageSelected(int position) {//当前页
                Log.e("MyContactsActivity","onPageSelected position="+position);
                if (position == 0) {
                    EventBus.getDefault().post(new TabCheckEvent("关注"));
                } else if (position == 1) {
                    EventBus.getDefault().post(new TabCheckEvent("粉丝"));
                } else if (position == 2) {
                    EventBus.getDefault().post(new TabCheckEvent("好友"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void SetTab() {
        if (getIntent().getStringExtra("type") != null) {
            index = Integer.parseInt(getIntent().getStringExtra("type"));
            Log.e("MyContactsActivity","initView index="+index);
            MyApplication.getInstance().setCheckIndex(""+index);
        }

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
            fragments.add(new MyContatsFragment());
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

    public void setIndicatorWidth(@NonNull final TabLayout tabLayout, final int margin) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    // 拿到tabLayout的slidingTabIndicator属性
                    Field slidingTabIndicatorField = tabLayout.getClass().getDeclaredField("slidingTabIndicator");
                    slidingTabIndicatorField.setAccessible(true);
                    LinearLayout mTabStrip = (LinearLayout) slidingTabIndicatorField.get(tabLayout);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        Field textViewField = tabView.getClass().getDeclaredField("textView");
                        textViewField.setAccessible(true);
                        TextView mTextView = (TextView) textViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        // 因为想要的效果是字多宽线就多宽，所以测量mTextView的宽度
                        int width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        // 设置tab左右间距,注意这里不能使用Padding,因为源码中线的宽度是根据tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;

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
