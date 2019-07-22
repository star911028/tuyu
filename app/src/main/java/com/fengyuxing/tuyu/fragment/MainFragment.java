package com.fengyuxing.tuyu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sonnyjack.widget.dragview.SonnyJackDragView;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.LoginActivity;
import com.fengyuxing.tuyu.activity.MainRankActivity;
import com.fengyuxing.tuyu.activity.MainRoomActivity;
import com.fengyuxing.tuyu.activity.MainSearchActivity;
import com.fengyuxing.tuyu.activity.WebViewActivity;
import com.fengyuxing.tuyu.adapter.MainBankRecyAdapter;
import com.fengyuxing.tuyu.adapter.MainhotRecyAdapter;
import com.fengyuxing.tuyu.adapter.ViewPagerAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.MainArrayModel;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.bean.RoomModel;
import com.fengyuxing.tuyu.http.HttpManager;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.UILImageLoader;
import com.fengyuxing.tuyu.view.NeedPWDWindow;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import retrofit2.Call;
import retrofit2.Response;


//首页
public class MainFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    Unbinder unbinder;
    @BindView(R.id.rv_RecyclerView2)
    RecyclerView rvRecyclerView2;
    @BindView(R.id.rank_ll)
    LinearLayout rank_ll;
    @BindView(R.id.main_ll)
    LinearLayout main_ll;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.view_pagerrank)
    ViewPager view_pagerrank;

    @BindView(R.id.search_iv)
    ImageView search_iv;
    @BindView(R.id.room_iv)
    ImageView home_iv;

    @BindView(R.id.rank_ll_no)
    LinearLayout rank_ll_no;
    @BindView(R.id.banner_load)
    LinearLayout banner_load;
    @BindView(R.id.vf)
    ViewFlipper vf;

    @BindView(R.id.main_appbar)
    AppBarLayout main_appbar;
    @BindView(R.id.main_banner)
    BGABanner mainBanner;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String Roomid = "";
    private Boolean toastnews = false;
    private UILImageLoader mUILImageLoader;
    private Context mContext;
    private View view;
    private HttpManager mHttpManager;
    private Integer[] imgArray = {R.drawable.rabblt_icon, R.drawable.rabblt_icon, R.drawable.rabblt_icon};
    private ArrayList<Integer> datas;
    private MainBankRecyAdapter recyAdapter;
    private MainhotRecyAdapter recyAdapter2;
    //    private Handler mHandler = new Handler();
    private LinearLayoutManager layoutManager;
    private String[] titles = new String[]{"关注", "发现"};//, "视频"
    private ArrayList<String> data = new ArrayList<>();
    private List<DataList> tabdata = new ArrayList<>();
    private List<DataList> bannerdata = new ArrayList<>();

    private List<Fragment> fragments = new ArrayList<>();
    private int page = 0;
    private List<MikeArray> rankdatas = new ArrayList<>();
    private SonnyJackDragView mSonnyJackDragView;
    private List<Fragment> fragmentsrank = new ArrayList<>();
    private String[] titlesrank = new String[]{"关注", "发现"};//, "视频"
    private int nowindex = 0;
    private int changeindex = 1;
    private static long time = 1000; //自动播放时间
    private static boolean autoPlay = true; //是否自动播放
    private List<DataList> hotdata = new ArrayList<>();
    private DataList carddatapos;
    private NeedPWDWindow mNeedPWDWindow;
    private DataList userdata = new DataList();
    private Boolean Needpass = false;
    private TextView typeTv1, typeTv2;
    private ImageView imgIvgx1, imgIvgx2, imgIvgx3, imgIvml1, imgIvml2, imgIvml3;
    private RelativeLayout  img_rl_gx1,img_rl_gx2,img_rl_gx3,img_rl_ml1,img_rl_ml2,img_rl_ml3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MyApplication.getInstance().setRoomBack("");
        mContext = getContext();
        Log.e("MainFragment", "onCreateView");
        mUILImageLoader = new UILImageLoader(mContext);
        mHttpManager = HttpManager.getInstance();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, null);
            unbinder = ButterKnife.bind(this, view);
            initView();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    /**
     * 播放，根据autoplay
     */
    public void play() {
        if (autoPlay) {
            handler.postDelayed(runnable, time);
            Log.e("autoPlay", "true");
        } else {
            handler.removeCallbacks(runnable);
            Log.e("autoPlay", "false");
        }
    }

    /**
     * 取消播放
     */
    public void cancel() {
        handler.removeCallbacks(runnable);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            play();
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e("autoPlay", "run" + "  nowindex=" + nowindex);
            view_pagerrank.setCurrentItem(changeindex);
        }
    };


    private void initView() {
        Log.e("MainFragment", "initView");
        int hight = getStatusBarHeight();//状态栏的高度
        Log.e("hasnavbar", "状态栏" + " hight=" + hight);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navbar_ll.getLayoutParams();
//        lp.height = hight-30;
//        navbar_ll.setLayoutParams(lp);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                            rvRecyclerView2添加头布局
        rvRecyclerView2.setLayoutManager(layoutManager);
        //下拉刷新
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale));
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
//                page = 0;
//                carddata.clear();
//                if (recyAdapter3 != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
//                    recyAdapter3.notifyDataSetChanged();
//                }
//                getData();//刷新数据
            }
        });
        //加载更多
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                initData();
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        refreshLayout.finishLoadmore();
//                    }
//                }, 3000);
//                 //evenbus传入page  type刷新页面
//                page++;
//                if (recyAdapter3 != null) { //防止返回时候快速滑动崩溃问题  Inconsistency detected.
//                    recyAdapter3.notifyDataSetChanged();
//                }
//                getData();//刷新数据
//            }
//        });

        //排行榜滚动条
        vf.addView(View.inflate(getActivity(), R.layout.view_advertisement01, null));//贡献榜
        vf.addView(View.inflate(getActivity(), R.layout.view_advertisement02, null));//魅力榜
        typeTv1 = (TextView) vf.findViewById(R.id.type_tv1);
        imgIvgx1 = (ImageView) vf.findViewById(R.id.img_ivgx1);
        imgIvgx2 = (ImageView) vf.findViewById(R.id.img_ivgx2);
        imgIvgx3 = (ImageView) vf.findViewById(R.id.img_ivgx3);
        img_rl_gx1= (RelativeLayout) vf.findViewById(R.id.img_rl_gx1);
        img_rl_gx2= (RelativeLayout) vf.findViewById(R.id.img_rl_gx2);
        img_rl_gx3= (RelativeLayout) vf.findViewById(R.id.img_rl_gx3);

        typeTv2 = (TextView) vf.findViewById(R.id.type_tv2);
        imgIvml1 = (ImageView) vf.findViewById(R.id.img_ivml1);
        imgIvml2 = (ImageView) vf.findViewById(R.id.img_ivml2);
        imgIvml3 = (ImageView) vf.findViewById(R.id.img_ivml3);


        img_rl_ml1= (RelativeLayout) vf.findViewById(R.id.img_rl_ml1);
        img_rl_ml2= (RelativeLayout) vf.findViewById(R.id.img_rl_ml2);
        img_rl_ml3= (RelativeLayout) vf.findViewById(R.id.img_rl_ml3);

        view_pagerrank.setOffscreenPageLimit(0);
        for (int i = 0; i < 2; i++) {
            fragmentsrank.add(new MainRankautoFragment());
        }
        Log.e("SetTab", "titlesrank=" + titlesrank.length + " fragmentsrank=" + fragmentsrank.size());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), titlesrank, fragmentsrank);
        view_pagerrank.setAdapter(viewPagerAdapter);
        view_pagerrank.setCurrentItem(0);


//        play();//自动播放
        //banner 轮播图
        mainBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                //设置圆角图片
                Glide.with(mContext)
                        .load(model)
                        .apply(new RequestOptions()
                                .transforms(new CenterCrop(), new RoundedCorners(20)
                                ))
                        .into(itemView);
            }
        });

        mainBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override//banner图点击事件
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                if (bannerdata.size() > 0) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);//
                    intent.putExtra("webview_title", bannerdata.get(position).getTitle());//h5 跳转的标题
                    intent.putExtra("webview_url", bannerdata.get(position).getH5TurnPath());//h5 跳转路径
                    startActivity(intent);
                }
            }
        });
        List<String> imagdata = new ArrayList<>();
        List<String> imagdatatips = new ArrayList<>();//图片的描述 设置空 不显示
        for (int i = 0; i < imagdata.size(); i++) {
            imagdatatips.add("");
        }
        initData();
    }

    private int getStatusBarHeight() {
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Status height:" + height);
        return height;
    }

    private void initData() {
        Log.e("MainFragment", "initData");
        data.clear();
        FindBanner();//查询首页banner
        getFindHotRoom();//查询热门的直播间
        getFindClassify();//查询分类
        FindRankingTop3();//查询首页排行榜前三名
        getFindUserInfo();
    }

    public void getFindUserInfo() {//查询个人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", MyApplication.getInstance().getUserId());
        postRequest(RetrofitService.FindUserInfo, map);
        Log.e("getFindUserInfo", "userId=" + MyApplication.getInstance().getUserId() + "  token=" + MyApplication.getInstance().getToken());
    }

    private void FindBanner() {//查询首页banner
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindBanner, map);
    }

    private void getFindHotRoom() {//查询热门的直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("page", page);
        postRequest(RetrofitService.FindHotRoom, map);
    }


    private void getFindClassify() {//查询分类
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindClassify, map);
    }

    private void FindRankingTop3() {//查询首页排行榜前三名
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.FindRankingTop3, map);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MainFragment", "onResume" + "  getRoomBack=" + MyApplication.getInstance().getRoomBack());
        if (MyApplication.getInstance().getRoomBack() != null && MyApplication.getInstance().getRoomBack().equals("roomback")) {
            initData();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("MainFragment", "onStop");
//        mHandler.removeCallbacks(scrollRunnable);
    }

    private void initListener() {
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {//当前页
                if (position == 0) {
                    EventBus.getDefault().post(new TabCheckEvent("最近"));
                } else if (position == 1) {
                    EventBus.getDefault().post(new TabCheckEvent("推荐"));
                } else {
                    Log.e("onEventMainThread", "tabdata=" + tabdata.size() + "  position=" + position);
                    EventBus.getDefault().post(new TabCheckEvent("其他" + tabdata.get(position - 2).getClassifyId()));
                }
                Log.e("onEventMainThread", "tab.getPosition()=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        view_pagerrank.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {//当前页
                if (position == 0) {
                    nowindex = 0;
                    changeindex = 1;
                    EventBus.getDefault().post(new TabCheckEvent("魅力榜"));
                } else if (position == 1) {
                    nowindex = 1;
                    changeindex = 0;
                    EventBus.getDefault().post(new TabCheckEvent("贡献榜"));
                }
                Log.e("onEventMainThread", "tab.getPosition()=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    view_pagerrank.setCurrentItem(nowindex, false);
                    play();
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    cancel();
                }
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MainFragment", "onDestroy");
        OkHttpUtils.getInstance().cancelTag(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.contains(RetrofitService.Head + RetrofitService.FindHotRoom)) {//热门房间
            Log.e("FindHotRoom", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            hotdata.clear();
            if (mMainArrayModel.getCode().equals("1")) {
                if (mMainArrayModel.getData() != null) {
//                    hotdata = mMainArrayModel.getData();
                    for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                        hotdata.add(mMainArrayModel.getData().get(i));
                    }
                    if (mMainArrayModel.getData().size() > 0) {
                        Log.e("getData", "getOnlineCount=" + mMainArrayModel.getData().get(0).getOnlineCount());
                        if (recyAdapter2 == null) {
                            recyAdapter2 = new MainhotRecyAdapter(mContext, hotdata);
                            recyAdapter2.setOnItemClickListener(mOnItemClickListener2);
                            rvRecyclerView2.setAdapter(recyAdapter2);
                            Log.i("initData", "MCircleAdapter==null");
                        } else {
                            recyAdapter2.notifyDataSetChanged();
                            Log.i("initData", "MCircleAdapter==notifyDataSetChanged");
                        }


                    }
                }
            } else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.contains(RetrofitService.Head + RetrofitService.FindClassify)) {//tab标题
            Log.e("FindClassify", "result=" + result);
            MainArrayModel mMainArrayModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            tabdata.clear();
            data.clear();
            if (mMainArrayModel.getCode().equals("1")) {
                if (mMainArrayModel.getData() != null) {
                    data.add("最近");
                    data.add("推荐");
                    if (mMainArrayModel.getData().size() > 0) {
                        for (int i = 0; i < mMainArrayModel.getData().size(); i++) {
                            data.add(mMainArrayModel.getData().get(i).getClassifyName());
                            tabdata.add(mMainArrayModel.getData().get(i));//保存分类数据
                        }
                        titles = new String[data.size()];
                        for (int k = 0; k < data.size(); k++) {
                            titles[k] = data.get(k);
                        }
                    } else {
                        titles = new String[data.size()];
                        for (int k = 0; k < data.size(); k++) {
                            titles[k] = data.get(k);
                        }
                    }
                    Log.e("titles", titles[0] + " titlessize=" + titles.length);
                    SetTab();//设置tab标题
                }
            } else if (mMainArrayModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainArrayModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.contains(RetrofitService.Head + RetrofitService.FindBanner)) {//轮播图
            Log.e("FindBanner", "result==" + result);
            MainArrayModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainArrayModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                bannerdata.clear();
                if (mainModel.getData().size() > 0) {
                    List<String> imagdata = new ArrayList<>();
                    List<String> imagdatatips = new ArrayList<>();//图片的描述 设置空 不显示
                    for (int i = 0; i < mainModel.getData().size(); i++) {
                        imagdata.add(mainModel.getData().get(i).getImgPath());
                        imagdatatips.add("");
                        bannerdata.add(mainModel.getData().get(i));
                    }
                    mainBanner.setData(imagdata, imagdatatips);
                    mainBanner.setVisibility(View.VISIBLE);
                    banner_load.setVisibility(View.GONE);
                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.contains(RetrofitService.Head + RetrofitService.FindRankingTop3)) {//查询首页排行榜前三名
            Log.e("FindRankingTop3", "result==" + result);
            MainModel mainModel = new Gson().fromJson(result,
                    new TypeToken<MainModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                //首页贡献榜
                Log.e("首页贡献榜", "getContributionTopArray().size()=" + mainModel.getData().getContributionTopArray().size());
                if (mainModel.getData().getContributionTopArray().size() > 0) {
                    vf.setVisibility(View.VISIBLE);
                    rank_ll_no.setVisibility(View.GONE);
                    typeTv1.setText("贡献榜");
                    if (mainModel.getData().getContributionTopArray().size() == 1) {
                        Glide.with(mContext)
                                .load(mainModel.getData().getContributionTopArray().get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvgx1);
//                        imgIvgx1.setVisibility(View.VISIBLE);
//                        imgIvgx2.setVisibility(View.GONE);
//                        imgIvgx3.setVisibility(View.GONE);

                        img_rl_gx1.setVisibility(View.VISIBLE);
                        img_rl_gx2.setVisibility(View.GONE);
                        img_rl_gx3.setVisibility(View.GONE);
                    } else if (mainModel.getData().getContributionTopArray().size() == 2) {
                        Glide.with(mContext)
                                .load(mainModel.getData().getContributionTopArray().get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvgx1);
                        Glide.with(mContext)
                                .load(mainModel.getData().getContributionTopArray().get(1))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvgx2);
//                        imgIvgx1.setVisibility(View.VISIBLE);
//                        imgIvgx2.setVisibility(View.VISIBLE);
//                        imgIvgx3.setVisibility(View.GONE);

                        img_rl_gx1.setVisibility(View.VISIBLE);
                        img_rl_gx2.setVisibility(View.VISIBLE);
                        img_rl_gx3.setVisibility(View.GONE);
                    } else if (mainModel.getData().getContributionTopArray().size() == 3) {
                        Glide.with(mContext)
                                .load(mainModel.getData().getContributionTopArray().get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvgx1);
                        Glide.with(mContext)
                                .load(mainModel.getData().getContributionTopArray().get(1))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvgx2);
                        Glide.with(mContext)
                                .load(mainModel.getData().getContributionTopArray().get(2))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvgx3);
//                        imgIvgx1.setVisibility(View.VISIBLE);
//                        imgIvgx2.setVisibility(View.VISIBLE);
//                        imgIvgx3.setVisibility(View.VISIBLE);

                        img_rl_gx1.setVisibility(View.VISIBLE);
                        img_rl_gx2.setVisibility(View.VISIBLE);
                        img_rl_gx3.setVisibility(View.VISIBLE);
                    }

                } else {
                    vf.setVisibility(View.GONE);
                    rank_ll_no.setVisibility(View.VISIBLE);
                }
                //  首页魅力榜
                Log.e("首页魅力榜", "getCharmTopArray().size()=" + mainModel.getData().getCharmTopArray().size());
                if (mainModel.getData().getCharmTopArray().size() > 0) {
                    typeTv2.setText("魅力榜");
                    if (mainModel.getData().getCharmTopArray().size() == 1) {
                        Glide.with(mContext)
                                .load(mainModel.getData().getCharmTopArray().get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvml1);
//                        imgIvml1.setVisibility(View.VISIBLE);
//                        imgIvml2.setVisibility(View.GONE);
//                        imgIvml3.setVisibility(View.GONE);

                        img_rl_ml1.setVisibility(View.VISIBLE);
                        img_rl_ml2.setVisibility(View.GONE);
                        img_rl_ml3.setVisibility(View.GONE);
                    } else if (mainModel.getData().getCharmTopArray().size() == 2) {
                        Glide.with(mContext)
                                .load(mainModel.getData().getCharmTopArray().get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvml1);
                        Glide.with(mContext)
                                .load(mainModel.getData().getCharmTopArray().get(1))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvml2);
//                        imgIvml1.setVisibility(View.VISIBLE);
//                        imgIvml2.setVisibility(View.VISIBLE);
//                        imgIvml3.setVisibility(View.GONE);

                        img_rl_ml1.setVisibility(View.VISIBLE);
                        img_rl_ml2.setVisibility(View.VISIBLE);
                        img_rl_ml3.setVisibility(View.GONE);
                    } else if (mainModel.getData().getCharmTopArray().size() == 3) {
                        Glide.with(mContext)
                                .load(mainModel.getData().getCharmTopArray().get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvml1);
                        Glide.with(mContext)
                                .load(mainModel.getData().getCharmTopArray().get(1))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvml2);
                        Glide.with(mContext)
                                .load(mainModel.getData().getCharmTopArray().get(2))
                                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                                .into(imgIvml3);
//                        imgIvml1.setVisibility(View.VISIBLE);
//                        imgIvml2.setVisibility(View.VISIBLE);
//                        imgIvml3.setVisibility(View.VISIBLE);

                        img_rl_ml1.setVisibility(View.VISIBLE);
                        img_rl_ml2.setVisibility(View.VISIBLE);
                        img_rl_ml3.setVisibility(View.VISIBLE);
                    }
                }

            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindRoomInfo)) {//进入直播间查询信息
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
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindUserInfo)) {
            Log.e("MineFragment", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                if (mMainModel.getData() != null) {
                    userdata = mMainModel.getData();
                    if (mMainModel.getData().getPortraitPathArray().length > 0) {
                        MyApplication.getInstance().setUserImg(mMainModel.getData().getPortraitPathArray()[0]);
                    }
                    MyApplication.getInstance().setUserDiamond(mMainModel.getData().getDiamond());
                    MyApplication.getInstance().setUserName(mMainModel.getData().getUsername());
                    MyApplication.getInstance().setUserRank(mMainModel.getData().getExpRank());
                }
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.OpenMyRoom)) {//开启直播间
            Log.e("OpenMyRoom", "result=" + result);
            RoomModel mMainModel = new Gson().fromJson(result,
                    new TypeToken<RoomModel>() {
                    }.getType());
            if (mMainModel.getCode().equals("1")) {
                Intent intent = new Intent(getActivity(), MainRoomActivity.class);//
                intent.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
                intent.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
                startActivity(intent);
            } else if (mMainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mMainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private MainhotRecyAdapter.OnItemClickListener mOnItemClickListener2 = new MainhotRecyAdapter.OnItemClickListener() {
        @Override//热门房间进入房间
        public void onItemClick(int position) {
            Roomid = hotdata.get(position).getRoomId();
            toastnews = false;
            getFindRoomInfo();//查询房间信息
        }
    };


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        view_pager.setCurrentItem(tab.getPosition());
    }

    private View getTabView(int currentPosition) {//tab item样式
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(titles[currentPosition]);
        return view;
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


    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (tab.getCustomView() == null) {
            return;
        } else {
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

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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


    public void SetTab() {
        //设置TabLayout标签的显示方式
        view_pager.removeAllViews();
        tabLayout.removeAllTabs();//先移除所有tab 防止重复越界
        fragments.clear();//防止重复越界
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        setIndicatorWidth(tabLayout, 30);
        //循环注入标签
        for (String tab : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
            Log.e("SetTab", "addTab=" + tab);
        }
        //设置TabLayout点击事件
//        tabLayout.setOnTabSelectedListener(this);
        view_pager.setOffscreenPageLimit(2);
        for (int i = 0; i < titles.length; i++) {
            fragments.add(new MainCardFragment());
        }
        Log.e("SetTab", "titles=" + titles.length + " fragments=" + fragments.size());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), titles, fragments);
        view_pager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(view_pager);
        view_pager.setCurrentItem(1);//默认选中推荐

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
        initAppbar();

    }


    //    /防止AppBarLayout头部滑动不了，需要在数据加载出来后调用该方法
    public void initAppbar() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) main_appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
    }

    @OnClick({R.id.room_iv, R.id.search_iv, R.id.rank_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.room_iv://房间
                if (userdata.getMyRoomId() != null) {
//                    Intent intent = new Intent(getActivity(), MainRoomActivity.class);//
//                    intent.putExtra("roomid", userdata.getMyRoomId());//传递房间id
//                    intent.putExtra("roomdata", (Serializable) userdata);//传递房间数据
//                    startActivity(intent);

                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
                        if (!MyApplication.getInstance().getMyRoomid().equals(userdata.getMyRoomId())) {//不是同一个房间 先退出MainRoomActivity
                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
                        }
                        Intent intent = new Intent(getActivity(), MainRoomActivity.class);//
                        intent.putExtra("roomid", userdata.getMyRoomId());//传递房间id
                        intent.putExtra("roomdata", (Serializable) userdata);//传递房间数据
                        startActivity(intent);
                    } else {//当前没有在房间
                        Intent intent = new Intent(getActivity(), MainRoomActivity.class);//
                        intent.putExtra("roomid", userdata.getMyRoomId());//传递房间id
                        intent.putExtra("roomdata", (Serializable) userdata);//传递房间数据
                        startActivity(intent);
                    }
                } else {//还没有自己的房间
//                    Toast.makeText(mContext, "您还没有自己的房间", Toast.LENGTH_SHORT).show();
                    getOpenMyRoom();
                }
                break;
            case R.id.search_iv://搜索
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);//
                startActivity(intent);
                MyApplication.getInstance().setRoomBack("");
                break;
            case R.id.rank_ll://搜索
                Log.e("rvRecyclerView", "onClick2");
                intent = new Intent(mContext, MainRankActivity.class);
                intent.putExtra("Type", "Hoom");
                startActivity(intent);
                MyApplication.getInstance().setRoomBack("");
                break;

        }
    }

//    public void getFindRoomInfo() {//进入直播间查询信息
//        WeakHashMap map = new WeakHashMap();
//        map.put("userId", MyApplication.getInstance().getUserId());
//        map.put("token", MyApplication.getInstance().getToken());
//        map.put("roomId", Roomid);
//        map.put("password", MyApplication.getInstance().getInputpass());
//        postRequest(RetrofitService.FindRoomInfo, map);
//    }

    public void getOpenMyRoom() {//开启直播间
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        postRequest(RetrofitService.OpenMyRoom, map);
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
