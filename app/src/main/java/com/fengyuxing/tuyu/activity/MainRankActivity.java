package com.fengyuxing.tuyu.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.MainRankItemAdapter;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.bean.RankDTO;
import com.fengyuxing.tuyu.bean.RankModel;
import com.fengyuxing.tuyu.util.RetrofitService;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class MainRankActivity extends BaseActivity implements View.OnClickListener {
    //排行榜
    private static final String TAG = "MainRankActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.tv_recommend)
    TextView tv_recommend;
    @BindView(R.id.view_recommend)
    View view_recommend;
    @BindView(R.id.lay_manneger)
    LinearLayout layManneger;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.view_follow)
    View view_follow;
    @BindView(R.id.lay_black)
    LinearLayout layBlack;
    @BindView(R.id.rank_day_tv)
    TextView rankDayTv;
    @BindView(R.id.rank_week_tv)
    TextView rankWeekTv;
    @BindView(R.id.rank_all_tv)
    TextView rankAllTv;
    public int white;
    @BindView(R.id.img_iv2)
    CircleImageView imgIv2;
    @BindView(R.id.name2_tv)
    TextView name2Tv;
    @BindView(R.id.sex2_tv)
    TextView sex2Tv;
    @BindView(R.id.sexbg_ll2)
    LinearLayout sexbgLl2;
    @BindView(R.id.num_tv2)
    TextView numTv2;
    @BindView(R.id.img_iv1)
    CircleImageView imgIv1;
    @BindView(R.id.name_tv1)
    TextView nameTv1;
    @BindView(R.id.sex_tv1)
    TextView sexTv1;
    @BindView(R.id.sexbg_ll1)
    LinearLayout sexbgLl1;

    @BindView(R.id.num_tv1)
    TextView numTv1;
    @BindView(R.id.img_iv3)
    CircleImageView imgIv3;
    @BindView(R.id.name_tv3)
    TextView nameTv3;
    @BindView(R.id.sex_tv3)
    TextView sexTv3;
    @BindView(R.id.sexbg_ll3)
    LinearLayout sexbgLl3;
    @BindView(R.id.num_tv3)
    TextView numTv3;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.rank_tv)
    TextView rankTv;
    @BindView(R.id.img_ivbt)
    CircleImageView imgIvbt;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.induce_tv)
    TextView induceTv;
    @BindView(R.id.gorank_tv)
    TextView gorankTv;
    @BindView(R.id.pic_two_ll)
    LinearLayout picTwoLl;
    @BindView(R.id.pic_one_ll)
    LinearLayout picOneLl;
    @BindView(R.id.pic_three_ll)
    LinearLayout picThreeLl;
    @BindView(R.id.level_iv2)
    ImageView levelIv2;
    @BindView(R.id.level_iv1)
    ImageView levelIv1;
    @BindView(R.id.level_iv3)
    ImageView levelIv3;
    @BindView(R.id.bottom_ll)
    LinearLayout bottom_ll;

    private LinearLayoutManager layoutManager;
    private MainRankItemAdapter recyAdapter;
    private List<DataList> datas = new ArrayList<>();
    private String Roomid = "";
    private String Timesec = "day";
    private String RankType = "contribution";
    private DataList userdata2;
    private RankDTO rankdata;
    private String getType = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_rank;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(MainRankActivity.this, getResources().getColor(R.color.mine_safe_bg), true);//isLightColor   透明或者不透明
        white = ContextCompat.getColor(mContext, R.color.white);
        if (getIntent().getStringExtra("Type") != null) {
            getType = getIntent().getStringExtra("Type");
            if (getType.equals("Hoom")) {
                FindHomeRankingData("contribution", "day");
                gorankTv.setVisibility(View.INVISIBLE);
            } else if (getType.equals("Room")) {
                gorankTv.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("Roomid") != null) {
                    Roomid = getIntent().getStringExtra("Roomid");
                }
                FindRoomRankingData("contribution", "day");
            }

        }
        changePage(0);
        Changedata(0);

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
//        DataList mMessage2 = new DataList();
//        for (int i = 0; i < 10; i++) {
//            mMessage2.setPortraitPath("http://pic29.nipic.com/20130601/12122227_123051482000_2.jpg");
//            datas.add(mMessage2);
//        }
        rvRecyclerView.setNestedScrollingEnabled(false); //
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecyclerView.setLayoutManager(layoutManager);
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


    private void changePage(int i) {
        Log.e("changePage2", "i=" + i);
        switch (i) {
            case 0:
                Spannable span1 = new SpannableString("贡献榜");//TODO注意span的长度
                span1.setSpan(new RelativeSizeSpan(1.4f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_recommend.setText(span1);
                tv_recommend.setTextColor(white);
                tv_recommend.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//加粗 抗锯齿
                view_recommend.setVisibility(View.VISIBLE);
                Spannable span2 = new SpannableString("魅力榜");
                span2.setSpan(new RelativeSizeSpan(1.0f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_follow.setText(span2);
                tv_follow.setTextColor(white);
//                tv_follow.getPaint().setFlags(Paint.EMBEDDED_BITMAP_TEXT_FLAG);
                view_follow.setVisibility(View.INVISIBLE);
                break;
            case 1:
                Spannable span3 = new SpannableString("贡献榜");
                span3.setSpan(new RelativeSizeSpan(1.0f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_recommend.setText(span3);
                tv_recommend.setTextColor(white);
//                tv_recommend.getPaint().setFlags(Paint.EMBEDDED_BITMAP_TEXT_FLAG);
                view_recommend.setVisibility(View.INVISIBLE);
                Spannable span4 = new SpannableString("魅力榜");
                span4.setSpan(new RelativeSizeSpan(1.4f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗 抗锯齿
                tv_follow.setText(span4);
                tv_follow.setTextColor(white);
                tv_follow.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                view_follow.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.back_iv, R.id.gorank_tv, R.id.lay_manneger, R.id.lay_black, R.id.rank_day_tv, R.id.rank_week_tv, R.id.rank_all_tv, R.id.img_iv1, R.id.img_iv2, R.id.img_iv3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                if(getType.equals("Room")){
                    finish();
                    Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                    intentyh.putExtra("roomid", Roomid);//传递房间id
                    intentyh.putExtra("roomdata", (Serializable)null);//传递房间数据
                    startActivity(intentyh);
//                    if (MyApplication.getInstance().getMyRoomid() != null && MyApplication.getInstance().getMyRoomid().length() > 0) {//如果当前在别的房间
//                        if (!MyApplication.getInstance().getMyRoomid().equals(mMainModel.getData().getRoomId())) {//不是同一个房间 先退出MainRoomActivity
//                            EventBus.getDefault().post(new TabCheckEvent("退出房间"));
//                        }
//                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
//                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
//                        startActivity(intentyh);
//                    } else {//当前没有在房间
//                        Intent intentyh = new Intent(mContext, MainRoomActivity.class);
//                        intentyh.putExtra("roomid", mMainModel.getData().getRoomId());//传递房间id
//                        intentyh.putExtra("roomdata", (Serializable) mMainModel.getData());//传递房间数据
//                        startActivity(intentyh);
//                    }
                }else {
                    finish();
                }
                break;
            case R.id.lay_manneger:
                changePage(0);
                RankType = "contribution";
                getData();
                break;
            case R.id.lay_black:
                changePage(1);
                RankType = "charm";
                getData();
                break;
            case R.id.rank_day_tv:
                Timesec = "day";
                Changedata(0);
                getData();
                break;
            case R.id.rank_week_tv:
                Timesec = "week";
                Changedata(1);
                getData();
                break;
            case R.id.rank_all_tv:
                Timesec = "total";
                Changedata(2);
                getData();
                break;
            case R.id.img_iv1:
                if (rankdata.getRankingArray().size() > 0) {
//                    getFindUserInfo(rankdata.getRankingArray().get(0).getUserId());
                    Intent intent = new Intent(mContext, UserInfoctivity.class);//
                    intent.putExtra("userdata", (Serializable) userdata2);
                    intent.putExtra("userinfoid", rankdata.getRankingArray().get(0).getUserId());
                    startActivity(intent);
                }
                break;
            case R.id.img_iv2:
                if (rankdata.getRankingArray().size() > 1) {
//                    getFindUserInfo(rankdata.getRankingArray().get(1).getUserId());
                    Intent intent = new Intent(mContext, UserInfoctivity.class);//
                    intent.putExtra("userdata", (Serializable) userdata2);
                    intent.putExtra("userinfoid", rankdata.getRankingArray().get(1).getUserId());
                    startActivity(intent);
                }
                break;
            case R.id.img_iv3:
                if (rankdata.getRankingArray().size() > 2) {
//                    getFindUserInfo(rankdata.getRankingArray().get(2).getUserId());
                    Intent intent = new Intent(mContext, UserInfoctivity.class);//
                    intent.putExtra("userdata", (Serializable) userdata2);
                    intent.putExtra("userinfoid", rankdata.getRankingArray().get(2).getUserId());
                    startActivity(intent);
                }
                break;
            case R.id.gorank_tv:
                finish();
                EventBus.getDefault().post(new TabCheckEvent("冲榜"));
                break;


        }
    }


    //获取数据
    public void getData() {
        if (getType.equals("Room")) {
            FindRoomRankingData(RankType, Timesec);
        } else if (getType.equals("Hoom")) {
            FindHomeRankingData(RankType, Timesec);
        }
    }

    public void Changedata(int poistion) {
        rankDayTv.setTextColor(mContext.getResources().getColor(R.color.white50));
        rankWeekTv.setTextColor(mContext.getResources().getColor(R.color.white50));
        rankAllTv.setTextColor(mContext.getResources().getColor(R.color.white50));
        if (poistion == 0) {
            rankDayTv.setTextColor(mContext.getResources().getColor(R.color.white));
            rankDayTv.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else if (poistion == 1) {
            rankWeekTv.setTextColor(mContext.getResources().getColor(R.color.white));
            rankWeekTv.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            rankAllTv.setTextColor(mContext.getResources().getColor(R.color.white));
            rankAllTv.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
    }


    private void FindRoomRankingData(String rankingType, String timeType) {//查询房间排行榜数据
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("roomId", Roomid);
        map.put("rankingType", rankingType);
        map.put("timeType", timeType);
        Log.e("FindRoomRankingData", "rankingType=" + rankingType + "  timeType=" + timeType);
        postRequest(RetrofitService.FindRoomRankingData, map);
    }


    private void FindHomeRankingData(String rankingType, String timeType) {//查询首页排行榜数据
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("rankingType", rankingType);
        map.put("timeType", timeType);
        Log.e("FindHomeRankingData", "rankingType=" + rankingType + "  timeType=" + timeType);
        postRequest(RetrofitService.FindHomeRankingData, map);
    }


    @Override
    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
        super.onCalllBack(call, response, result, url);
        if (url.equals(RetrofitService.Head + RetrofitService.FindRoomRankingData)) {//查询房间排行榜数据
            Log.e("FindRoomRankingData2", "rankingType=" + RankType + "  timeType=" + Timesec);
            Log.e("FindRoomRankingData2", "result=" + result);
            RankModel mainModel = new Gson().fromJson(result,
                    new TypeToken<RankModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                datas.clear();
                if (mainModel.getData() != null) {
                    rankdata = mainModel.getData();
                    nameTv.setText(mainModel.getData().getMyRank().getUsername());
                    if (mainModel.getData().getMyRank().getOrderNumber() == null) {//未上榜
                        induceTv.setText(mainModel.getData().getMyRank().getDiamond() + "钻可进入排名，前50可上榜");

                        rankTv.setText("未上榜");
                    } else {
//                        induceTv.setText(mainModel.getData().getMyRank().getDiamond() + "钻，送礼越多排名越高");
                        float num1 = Float.parseFloat(mainModel.getData().getMyRank().getDiamond());
                        if (num1 >= 10000) {
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
                            induceTv.setText(strPrice + "W" + "钻，送礼越多排名越高");//保留两位小数
                        } else {
                            induceTv.setText(mainModel.getData().getMyRank().getDiamond() + "钻，送礼越多排名越高");
                        }
                        rankTv.setText(mainModel.getData().getMyRank().getOrderNumber());
                    }
                    SetIMG(mainModel.getData().getMyRank().getPortraitPath(), imgIvbt);
                    picOneLl.setVisibility(View.INVISIBLE);
                    picTwoLl.setVisibility(View.INVISIBLE);
                    picThreeLl.setVisibility(View.INVISIBLE);
                    if (mainModel.getData().getRankingArray().size() > 0) {
                        if (mainModel.getData().getRankingArray().size() == 1) {
                            picOneLl.setVisibility(View.VISIBLE);
                            SetSex(mainModel.getData().getRankingArray().get(0).getGender(), mainModel.getData().getRankingArray().get(0).getAge(), sexTv1, sexbgLl1);
                            SetLevel(mainModel.getData().getRankingArray().get(0).getExpRank(), levelIv1);
                            SetIMG(mainModel.getData().getRankingArray().get(0).getPortraitPath(), imgIv1);
                            nameTv1.setText(mainModel.getData().getRankingArray().get(0).getUsername());
                            SetDiomand(mainModel.getData().getRankingArray().get(0).getDiamond(), numTv1);
                        } else if (mainModel.getData().getRankingArray().size() == 2) {
                            picOneLl.setVisibility(View.VISIBLE);
                            picTwoLl.setVisibility(View.VISIBLE);
                            SetSex(mainModel.getData().getRankingArray().get(0).getGender(), mainModel.getData().getRankingArray().get(0).getAge(), sexTv1, sexbgLl1);
                            SetSex(mainModel.getData().getRankingArray().get(1).getGender(), mainModel.getData().getRankingArray().get(1).getAge(), sex2Tv, sexbgLl2);
                            SetLevel(mainModel.getData().getRankingArray().get(0).getExpRank(), levelIv1);
                            SetLevel(mainModel.getData().getRankingArray().get(1).getExpRank(), levelIv2);
                            SetIMG(mainModel.getData().getRankingArray().get(0).getPortraitPath(), imgIv1);
                            SetIMG(mainModel.getData().getRankingArray().get(1).getPortraitPath(), imgIv2);
                            nameTv1.setText(mainModel.getData().getRankingArray().get(0).getUsername());
                            name2Tv.setText(mainModel.getData().getRankingArray().get(1).getUsername());
                            SetDiomand(mainModel.getData().getRankingArray().get(0).getDiamond(), numTv1);
                            SetDiomand(mainModel.getData().getRankingArray().get(1).getDiamond(), numTv2);
                        } else if (mainModel.getData().getRankingArray().size() >= 3) {
                            picOneLl.setVisibility(View.VISIBLE);
                            picTwoLl.setVisibility(View.VISIBLE);
                            picThreeLl.setVisibility(View.VISIBLE);
                            SetSex(mainModel.getData().getRankingArray().get(0).getGender(), mainModel.getData().getRankingArray().get(0).getAge(), sexTv1, sexbgLl1);
                            SetSex(mainModel.getData().getRankingArray().get(1).getGender(), mainModel.getData().getRankingArray().get(1).getAge(), sex2Tv, sexbgLl2);
                            SetSex(mainModel.getData().getRankingArray().get(2).getGender(), mainModel.getData().getRankingArray().get(2).getAge(), sexTv3, sexbgLl3);
                            SetLevel(mainModel.getData().getRankingArray().get(0).getExpRank(), levelIv1);
                            SetLevel(mainModel.getData().getRankingArray().get(1).getExpRank(), levelIv2);
                            SetLevel(mainModel.getData().getRankingArray().get(2).getExpRank(), levelIv3);
                            SetIMG(mainModel.getData().getRankingArray().get(0).getPortraitPath(), imgIv1);
                            SetIMG(mainModel.getData().getRankingArray().get(1).getPortraitPath(), imgIv2);
                            SetIMG(mainModel.getData().getRankingArray().get(2).getPortraitPath(), imgIv3);
                            nameTv1.setText(mainModel.getData().getRankingArray().get(0).getUsername());
                            name2Tv.setText(mainModel.getData().getRankingArray().get(1).getUsername());
                            nameTv3.setText(mainModel.getData().getRankingArray().get(2).getUsername());

                            SetDiomand(mainModel.getData().getRankingArray().get(0).getDiamond(), numTv1);
                            SetDiomand(mainModel.getData().getRankingArray().get(1).getDiamond(), numTv2);
                            SetDiomand(mainModel.getData().getRankingArray().get(2).getDiamond(), numTv3);
                        }

                        if (mainModel.getData().getRankingArray().size() > 3) {
                            for (int i = 0; i < mainModel.getData().getRankingArray().size(); i++) {
                                if (i > 2) {
                                    datas.add(mainModel.getData().getRankingArray().get(i));
                                }
                            }

                            if (recyAdapter == null) {
                                recyAdapter = new MainRankItemAdapter(mContext, datas);
                                rvRecyclerView.setAdapter(recyAdapter);
                                recyAdapter.setOnItemClickListener(mOnItemClickListener);
                            } else {
                                recyAdapter.notifyDataSetChanged();
                            }
//                            recyAdapter = new MainRankItemAdapter(mContext, datas);
//                            layoutManager = new LinearLayoutManager(mContext);
//                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                            rvRecyclerView.setLayoutManager(layoutManager);
//                            rvRecyclerView.setAdapter(recyAdapter);
                        } else {
                            if (recyAdapter == null) {
                                recyAdapter = new MainRankItemAdapter(mContext, datas);
                                rvRecyclerView.setAdapter(recyAdapter);
                                recyAdapter.setOnItemClickListener(mOnItemClickListener);
                            } else {
                                recyAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        } else if (url.equals(RetrofitService.Head + RetrofitService.FindHomeRankingData)) {//查询首页排行榜数据
            Log.e("FindHomeRankingData", "rankingType=" + RankType + "  timeType=" + Timesec);
            Log.e("FindHomeRankingData", "result=" + result);
            RankModel mainModel = new Gson().fromJson(result,
                    new TypeToken<RankModel>() {
                    }.getType());
            if (mainModel.getCode().equals("1")) {
                datas.clear();
                if (mainModel.getData() != null) {
                    rankdata = mainModel.getData();
                    nameTv.setText(mainModel.getData().getMyRank().getUsername());
                    if (mainModel.getData().getMyRank().getOrderNumber() == null) {//未上榜
                        induceTv.setText(mainModel.getData().getMyRank().getDiamond() + "钻可进入排名，前50可上榜");

                        rankTv.setText("未上榜");
                    } else {
//                        induceTv.setText(mainModel.getData().getMyRank().getDiamond() + "钻，送礼越多排名越高");
                        float num1 = Float.parseFloat(mainModel.getData().getMyRank().getDiamond());
                        if (num1 >= 10000) {
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
                            induceTv.setText(strPrice + "W" + "钻，送礼越多排名越高");//保留两位小数
                        } else {
                            induceTv.setText(mainModel.getData().getMyRank().getDiamond() + "钻，送礼越多排名越高");
                        }
                        rankTv.setText(mainModel.getData().getMyRank().getOrderNumber());
                    }
                    SetIMG(mainModel.getData().getMyRank().getPortraitPath(), imgIvbt);
                    picOneLl.setVisibility(View.INVISIBLE);
                    picTwoLl.setVisibility(View.INVISIBLE);
                    picThreeLl.setVisibility(View.INVISIBLE);
                    if (mainModel.getData().getRankingArray().size() > 0) {
                        if (mainModel.getData().getRankingArray().size() == 1) {
                            picOneLl.setVisibility(View.VISIBLE);
                            SetSex(mainModel.getData().getRankingArray().get(0).getGender(), mainModel.getData().getRankingArray().get(0).getAge(), sexTv1, sexbgLl1);
                            SetLevel(mainModel.getData().getRankingArray().get(0).getExpRank(), levelIv1);
                            SetIMG(mainModel.getData().getRankingArray().get(0).getPortraitPath(), imgIv1);
                            nameTv1.setText(mainModel.getData().getRankingArray().get(0).getUsername());
                            SetDiomand(mainModel.getData().getRankingArray().get(0).getDiamond(), numTv1);
                        } else if (mainModel.getData().getRankingArray().size() == 2) {
                            picOneLl.setVisibility(View.VISIBLE);
                            picTwoLl.setVisibility(View.VISIBLE);
                            SetSex(mainModel.getData().getRankingArray().get(0).getGender(), mainModel.getData().getRankingArray().get(0).getAge(), sexTv1, sexbgLl1);
                            SetSex(mainModel.getData().getRankingArray().get(1).getGender(), mainModel.getData().getRankingArray().get(1).getAge(), sex2Tv, sexbgLl2);
                            SetLevel(mainModel.getData().getRankingArray().get(0).getExpRank(), levelIv1);
                            SetLevel(mainModel.getData().getRankingArray().get(1).getExpRank(), levelIv2);
                            SetIMG(mainModel.getData().getRankingArray().get(0).getPortraitPath(), imgIv1);
                            SetIMG(mainModel.getData().getRankingArray().get(1).getPortraitPath(), imgIv2);
                            nameTv1.setText(mainModel.getData().getRankingArray().get(0).getUsername());
                            name2Tv.setText(mainModel.getData().getRankingArray().get(1).getUsername());
                            SetDiomand(mainModel.getData().getRankingArray().get(0).getDiamond(), numTv1);
                            SetDiomand(mainModel.getData().getRankingArray().get(1).getDiamond(), numTv2);
                        } else if (mainModel.getData().getRankingArray().size() >= 3) {
                            picOneLl.setVisibility(View.VISIBLE);
                            picTwoLl.setVisibility(View.VISIBLE);
                            picThreeLl.setVisibility(View.VISIBLE);
                            SetSex(mainModel.getData().getRankingArray().get(0).getGender(), mainModel.getData().getRankingArray().get(0).getAge(), sexTv1, sexbgLl1);
                            SetSex(mainModel.getData().getRankingArray().get(1).getGender(), mainModel.getData().getRankingArray().get(1).getAge(), sex2Tv, sexbgLl2);
                            SetSex(mainModel.getData().getRankingArray().get(2).getGender(), mainModel.getData().getRankingArray().get(2).getAge(), sexTv3, sexbgLl3);
                            SetLevel(mainModel.getData().getRankingArray().get(0).getExpRank(), levelIv1);
                            SetLevel(mainModel.getData().getRankingArray().get(1).getExpRank(), levelIv2);
                            SetLevel(mainModel.getData().getRankingArray().get(2).getExpRank(), levelIv3);
                            SetIMG(mainModel.getData().getRankingArray().get(0).getPortraitPath(), imgIv1);
                            SetIMG(mainModel.getData().getRankingArray().get(1).getPortraitPath(), imgIv2);
                            SetIMG(mainModel.getData().getRankingArray().get(2).getPortraitPath(), imgIv3);
                            nameTv1.setText(mainModel.getData().getRankingArray().get(0).getUsername());
                            name2Tv.setText(mainModel.getData().getRankingArray().get(1).getUsername());
                            nameTv3.setText(mainModel.getData().getRankingArray().get(2).getUsername());
                            SetDiomand(mainModel.getData().getRankingArray().get(0).getDiamond(), numTv1);
                            SetDiomand(mainModel.getData().getRankingArray().get(1).getDiamond(), numTv2);
                            SetDiomand(mainModel.getData().getRankingArray().get(2).getDiamond(), numTv3);
                        }
                        Log.e("getRankingArray", "size=" + mainModel.getData().getRankingArray().size());
                        if (mainModel.getData().getRankingArray().size() > 3) {
                            for (int i = 0; i < mainModel.getData().getRankingArray().size(); i++) {
                                if (i > 2) {
                                    datas.add(mainModel.getData().getRankingArray().get(i));
                                }
                            }
                            if (recyAdapter == null) {
                                recyAdapter = new MainRankItemAdapter(mContext, datas);
                                rvRecyclerView.setAdapter(recyAdapter);
                                recyAdapter.setOnItemClickListener(mOnItemClickListener);
                            } else {
                                recyAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (recyAdapter == null) {
                                recyAdapter = new MainRankItemAdapter(mContext, datas);
                                rvRecyclerView.setAdapter(recyAdapter);
                                recyAdapter.setOnItemClickListener(mOnItemClickListener);
                            } else {
                                recyAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }
            } else if (mainModel.getCode().equals("0")) {
                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setUserId("");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(mContext, mainModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }
//        else if (url.contains(RetrofitService.Head + RetrofitService.FindUserInfo)) {
//            Log.e("FindUserInfo", "result=" + result);
//            RoomModel roomModel = new Gson().fromJson(result,
//                    new TypeToken<RoomModel>() {
//                    }.getType());
//            if (roomModel.getCode().equals("1")) {
//                if (roomModel.getData() != null) {
//                    userdata2 = roomModel.getData();
//                    Intent intent = new Intent(mContext, UserInfoctivity.class);//
//                    intent.putExtra("userdata", (Serializable) userdata2);
//                    intent.putExtra("userinfoid", "1");
//                    startActivity(intent);
//                }
//            } else if (roomModel.getCode().equals("0")) {
//                Toast.makeText(mContext, R.string.login_token, Toast.LENGTH_SHORT).show();
//                MyApplication.getInstance().setUserId("");
//                Intent intent = new Intent(mContext, LoginActivity.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(mContext, roomModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    public void SetLevel(String level, ImageView imageView) {
        //设置用户等级
        int levelnum = Integer.valueOf(level).intValue();
        Glide.with(mContext)
                .load(RetrofitService.bgAraay[levelnum])
                .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                .into(imageView);
    }

    public void SetSex(String sex, String age, TextView textView, LinearLayout linearLayout) {
        //设置用户性别 年龄
        textView.setText(age);
        if (sex.equals("男")) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.man);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
            linearLayout.setBackgroundResource(R.drawable.room_sex);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.woman);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
            linearLayout.setBackgroundResource(R.drawable.room_sex2);
        }
    }


    public void SetIMG(String path, ImageView img) {
        Glide.with(mContext)
                .load(path)
                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                .into(img);
    }

    public void SetDiomand(String diomand, TextView textView) {
        float num1 = Float.parseFloat(diomand);
        if (num1 >= 10000) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
            textView.setText(strPrice + "W");//保留两位小数

        } else {
            textView.setText(diomand);
        }
    }

    private MainRankItemAdapter.OnItemClickListener mOnItemClickListener = new MainRankItemAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public void onHeadClick(int position) {
//            getFindUserInfo(datas.get(position).getUserId());
            Intent intent = new Intent(mContext, UserInfoctivity.class);//
            intent.putExtra("userdata", (Serializable) userdata2);
            intent.putExtra("userinfoid", datas.get(position).getUserId());
            startActivity(intent);
        }
    };

    public void getFindUserInfo(String otherid) {//查询他人资料
        WeakHashMap map = new WeakHashMap();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("token", MyApplication.getInstance().getToken());
        map.put("infoUserId", otherid);
        postRequest(RetrofitService.FindUserInfo, map);
    }
}
