package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fengyuxing.tuyu.activity.UserInfoctivity;
import com.fengyuxing.tuyu.adapter.MyBlackerRecyAdapter;
import com.fengyuxing.tuyu.bean.MainGiftModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.activity.WebViewActivity;
import com.fengyuxing.tuyu.adapter.GiftGridViewAdapter;
import com.fengyuxing.tuyu.adapter.GiftMainRecyAdapter;
import com.fengyuxing.tuyu.adapter.GiftNumsRecyAdapter;
import com.fengyuxing.tuyu.adapter.GiftPicRecyAdapter;
import com.fengyuxing.tuyu.bean.Gift;
import com.fengyuxing.tuyu.bean.MainDTO;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.TabCheckEventList;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;


//房间点击底部礼物弹窗

public class RoomGiftWindowAll extends PopupWindow {
    private View conentView;
    private Context context;
    private LinearLayout pop_layout, main_ll, emety_pakage_ll, info_ll, all_user_gift_ll, one_user_gift_ll, gift_nums_ll, sec_nums_ll, navbar_ll;
    private RecyclerView recycler_pic, recycler_gift_nums;
    private ImageView img_iv;
    private TextView pay_tv, pakage_tv, gift_tv, follow_tv, setadmin_tv, give_tv, name_tv, nums_tv, money_tv, info_tv;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager layoutManager2;
    private EditText num_et;
    private GiftPicRecyAdapter recyAdapter;
    private GiftMainRecyAdapter recyAdapter2;
    private GiftNumsRecyAdapter recyAdapter3;
    private GiftGridViewAdapter myGridViewAdapter1, myGridViewAdapter2, myGridViewAdapterpk1, myGridViewAdapterpk2;
    private List<MikeArray> picdata = new ArrayList<>();
    private List<MikeArray> numsdata = new ArrayList<>();
    private CheckBox main_cb;
    private int[] nums = {1314, 770, 520, 188, 99, 10, 1};
    private String[] names = {"一生一世", "亲亲你", "我爱你", "要抱抱", "天长地久", "十全十美", "一心一意"};
    private String giftid = "";
    private String giftnums = "1";
    private String toUserIdArray = "";
    private String Sendgiftid = "";
    private String Sendgiftname = "";
    private String showtext = "0";
    private ViewPager view_pager, view_pager2;
    private List<View> gridViews, gridViews2;
    private LayoutInflater layoutInflater;
    private ArrayList<Gift> catogarys;
    private List<MikeArray> giftbgArray = new ArrayList<>();//
    private GridView gridViewpk1, gridViewpk2;
    private List<String> secgiftids = new ArrayList<>();
    private String SendType = "gift";
    private LinearLayout dot_layout;
    private ImageView[] dotViews;
    private List<String> sendids = new ArrayList<>();

    public void setDrawableAndTextColor(int id, TextView textview) {
        Drawable drawable = context.getResources().getDrawable(id);
        textview.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        textview.setTextColor(context.getResources().getColor(R.color.color_text_02));
    }

    public void setDrawableAndTextColor(int id, TextView textview, int color) {
        Drawable drawable = context.getResources().getDrawable(id);
        textview.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        textview.setTextColor(color);
    }

    public RoomGiftWindowAll(final Activity context, OnClickListener l, final String clicktype, final String isman, final List<MikeArray> datas, final List<MikeArray> giftArray, final String roomid, final int position, MainDTO roomdata, Boolean isFollow, final List<MikeArray> pakagegiftArray) {//datas是当前所有用户信息  giftArray是当前所有礼物信息
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_giftall_window, null);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        dot_layout = (LinearLayout) conentView.findViewById(R.id.dot_layout);
        pakage_tv = (TextView) conentView.findViewById(R.id.pakage_tv);
        gift_tv = (TextView) conentView.findViewById(R.id.gift_tv);
        all_user_gift_ll = (LinearLayout) conentView.findViewById(R.id.all_user_gift_ll);
        one_user_gift_ll = (LinearLayout) conentView.findViewById(R.id.one_user_gift_ll);
        main_ll = (LinearLayout) conentView.findViewById(R.id.main_ll);
        emety_pakage_ll = (LinearLayout) conentView.findViewById(R.id.emety_pakage_ll);
        view_pager = (ViewPager) conentView.findViewById(R.id.view_pager);
        view_pager2 = (ViewPager) conentView.findViewById(R.id.view_pager2);
        gift_nums_ll = (LinearLayout) conentView.findViewById(R.id.gift_nums_ll);
        name_tv = (TextView) conentView.findViewById(R.id.name_tv);
        money_tv = (TextView) conentView.findViewById(R.id.money_tv);
        sec_nums_ll = (LinearLayout) conentView.findViewById(R.id.sec_nums_ll);
        navbar_ll = (LinearLayout) conentView.findViewById(R.id.navbar_ll);
        num_et = (EditText) conentView.findViewById(R.id.num_et);
        info_ll = (LinearLayout) conentView.findViewById(R.id.info_ll);
        recycler_pic = (RecyclerView) conentView.findViewById(R.id.recycler_pic);
        recycler_gift_nums = (RecyclerView) conentView.findViewById(R.id.recycler_gift_nums);
        nums_tv = (TextView) conentView.findViewById(R.id.nums_tv);
        main_cb = (CheckBox) conentView.findViewById(R.id.main_cb);
        follow_tv = (TextView) conentView.findViewById(R.id.follow_tv);
        img_iv = (ImageView) conentView.findViewById(R.id.img_iv);
        pay_tv = (TextView) conentView.findViewById(R.id.pay_tv);
        setadmin_tv = (TextView) conentView.findViewById(R.id.setadmin_tv);
        info_tv = (TextView) conentView.findViewById(R.id.info_tv);
        give_tv = (TextView) conentView.findViewById(R.id.give_tv);
        FindPersonalGift(handler);//查询背包礼物
        Boolean hasnavbar = checkDeviceHasNavigationBar(context);
        secgiftids.clear();
        if (hasnavbar) {
            int hight = getNavigationBarHeight();//虚拟键的高度
            Log.e("hasnavbar", "有虚拟键" + " hight=" + hight);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navbar_ll.getLayoutParams();
            lp.height = hight + 10;
            navbar_ll.setLayoutParams(lp);
        } else {
            Log.e("hasnavbar", "没有虚拟键");
        }
        num_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {//键盘完成
                    Log.e("onEditorAction", "搜索");
                    gift_nums_ll.setVisibility(View.GONE);//关闭弹出数字选择框
                    return true;
                }
                return false;
            }
        });

        num_et.addTextChangedListener(new TextWatcher() {
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
                if (!s.equals("0")) {
                    nums_tv.setText("X" + s);
                    giftnums = s + "";//选择的礼物数量
                    //参数：1，自己的EditText。2，时间。
//                    InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(num_et.getWindowToken(), 500);

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            gift_nums_ll.setVisibility(View.GONE);//关闭弹出数字选择框
//                        }
//                    },1000); // 延时2秒
                }
            }
        });
        main_ll.setOnClickListener(new View.OnClickListener() {
            @Override//
            public void onClick(View v) {
                gift_nums_ll.setVisibility(View.GONE);//关闭弹出数字选择框
            }
        });
        all_user_gift_ll.setOnClickListener(new View.OnClickListener() {
            @Override//
            public void onClick(View v) {
                gift_nums_ll.setVisibility(View.GONE);//关闭弹出数字选择框
            }
        });
        pakage_tv.setOnClickListener(new View.OnClickListener() {
            @Override//背包
            public void onClick(View v) {
                SendType = "pakage";
                Log.e("onClick", "pakage_tv");
                pakage_tv.setTextColor(context.getResources().getColor(R.color.white));
                gift_tv.setTextColor(context.getResources().getColor(R.color.white50));
                gift_tv.setBackgroundResource(R.mipmap.gift_select);
                pakage_tv.setBackgroundResource(R.mipmap.package_unselect);
                view_pager.setVisibility(View.GONE);
                view_pager2.setVisibility(View.VISIBLE);
                dot_layout.setVisibility(View.INVISIBLE);
                if (pakagegiftArray.size() > 0) {
                    emety_pakage_ll.setVisibility(View.GONE);
                } else {
                    emety_pakage_ll.setVisibility(View.VISIBLE);
                }
            }
        });
        gift_tv.setOnClickListener(new View.OnClickListener() {
            @Override//礼物
            public void onClick(View v) {
                SendType = "gift";
                Log.e("onClick", "gift_tv");
                pakage_tv.setTextColor(context.getResources().getColor(R.color.white50));
                gift_tv.setTextColor(context.getResources().getColor(R.color.white));
                gift_tv.setBackgroundResource(R.mipmap.gift_unselect);
                pakage_tv.setBackgroundResource(R.mipmap.package_select);
                view_pager.setVisibility(View.VISIBLE);
                view_pager2.setVisibility(View.GONE);
                emety_pakage_ll.setVisibility(View.GONE);
                dot_layout.setVisibility(View.VISIBLE);
            }
        });
        FindUserinfo(handler);//查询自己用户信息
        if (clicktype.equals("1")) {//麦上用户打开单人礼物盘
            all_user_gift_ll.setVisibility(View.GONE);
            one_user_gift_ll.setVisibility(View.VISIBLE);
            if (isman.equals("1")) {//管理员
                setadmin_tv.setVisibility(View.VISIBLE);
//                info_tv.setVisibility(View.VISIBLE);
            } else {//普通用户
                setadmin_tv.setVisibility(View.GONE);
//                info_tv.setVisibility(View.GONE);
            }
            name_tv.setText(datas.get(position).getMikerName());
            Glide.with(context)
                    .load(datas.get(position).getMikerPortraitPath())
                    .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                    .into(img_iv);
            setadmin_tv.setTag(4);//管理
            setadmin_tv.setOnClickListener(l);
            info_ll.setTag(6);//用户信息
            info_ll.setOnClickListener(l);
            info_tv.setTag(6);//用户信息
            info_tv.setOnClickListener(l);
            Log.e("isFollow", "isFollow=" + isFollow);
            if (isFollow) {
                follow_tv.setText("已关注");
                follow_tv.setBackgroundResource(R.drawable.gift_bg_admin);
            } else {
                follow_tv.setText("+关注");
                follow_tv.setBackgroundResource(R.drawable.gift_bg_follow);
            }
            follow_tv.setOnClickListener(new View.OnClickListener() {
                @Override//关注
                public void onClick(View v) {
                    if (follow_tv.getText().equals("+关注")) {//取消关注
                        showtext = "0";
                        getData(handler, datas.get(position).getMikerId(), "0");
                        follow_tv.setText("已关注");
                        follow_tv.setBackgroundResource(R.drawable.gift_bg_admin);
                    } else {//关注
                        showtext = "1";
                        getData(handler, datas.get(position).getMikerId(), "1");
                        follow_tv.setText("+关注");
                        follow_tv.setBackgroundResource(R.drawable.gift_bg_follow);
                    }

                    Log.e("isFollow", "getMikerId=" + datas.get(position).getMikerId());
                }
            });
            toUserIdArray = datas.get(position).getMikerId();
        } else {//底部礼物盘
            one_user_gift_ll.setVisibility(View.GONE);
            all_user_gift_ll.setVisibility(View.VISIBLE);
        }
        pay_tv.setOnClickListener(new View.OnClickListener() {
            @Override//充值
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);//
                intent.putExtra("webview_title", "我的钱包");
                intent.putExtra("webview_url", "http://ty.fengyugo.com/h5/h5/recharge.html" + "?id=" + MyApplication.getInstance().getUserId());
                context.startActivity(intent);
            }
        });


        picdata.clear();
        MyApplication.getInstance().setGiftHosterid(roomdata.getHosterId());
        MikeArray host = new MikeArray();
        host.setMikerId(roomdata.getHosterId());
        host.setMikerPortraitPath(roomdata.getHosterPortraitPath());
        Log.e("房间房主数据", "getHosterId=" + roomdata.getHosterId() + "  myuserid=" + MyApplication.getInstance().getUserId());
        if (!roomdata.getHosterId().equals(MyApplication.getInstance().getUserId())) {
            picdata.add(host);//添加房主信息 如果是自己就不添加
        }
        Log.e("房间人数数据", "datassize=" + datas.size() + "  myuserid=" + MyApplication.getInstance().getUserId() + "  getMikerId=" + datas.get(0).getMikerId());
        //TODO=======================================================================================================
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getMikerId() != null) {
                if (!datas.get(i).getMikerId().equals(MyApplication.getInstance().getUserId()) && !datas.get(i).getMikerId().equals(roomdata.getHosterId())) {
                    picdata.add(datas.get(i));//不是自己 不是房主才添加进来
                }
            }
        }
        if (picdata.size() == 0) {//除了房主外没有其他人的时候显示房主
            picdata.add(host);//添加房主信息 如果是自己就不添加
        }
        //房间人数头像
        recyAdapter = new GiftPicRecyAdapter(context, picdata);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_pic.setLayoutManager(layoutManager);
        recycler_pic.setAdapter(recyAdapter);

        if (all_user_gift_ll.getVisibility() == View.VISIBLE) {
            recyAdapter.ChangeBGall(false, true);
//            if (picdata.size() > 0) {
//                String result = "";
//                for (int i = 0; i < picdata.size(); i++) {
//                    if (!picdata.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {//去除掉自己
//                        result += picdata.get(i).getMikerId() + ",";
//                    }
//                }
//                if (result.length() > 0) {
//                    toUserIdArray = result.substring(0, result.length() - 1);
//                } else {
//                    toUserIdArray = MyApplication.getInstance().getUserId();
//                }
//
//                Log.e("onClick", "result1=" + result + "   toUserIdArray=" + toUserIdArray);
//            }
        }
        for (int i = 0; i < nums.length; i++) {
            MikeArray item1 = new MikeArray();
            item1.setMikeNumber(nums[i] + "");
            item1.setMikerName(names[i]);
            numsdata.add(item1);
        }

        recyAdapter3 = new GiftNumsRecyAdapter(context, numsdata);//giftArray   numsdata 测试数据
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_gift_nums.setLayoutManager(layoutManager);
        recycler_gift_nums.setAdapter(recyAdapter3);


        sec_nums_ll.setOnClickListener(new View.OnClickListener() {
            @Override//选择礼物框
            public void onClick(View v) {
                Log.e("onClick", "main_cb.isChecked()=" + main_cb.isChecked());
                if (gift_nums_ll.getVisibility() == View.VISIBLE) {
                    gift_nums_ll.setVisibility(View.GONE);
                } else {
                    gift_nums_ll.setVisibility(View.VISIBLE);
                }
            }
        });
        give_tv.setOnClickListener(new View.OnClickListener() {
            @Override//赠送
            public void onClick(View v) {
                give_tv.setEnabled(false);//重置按钮点击
                gift_nums_ll.setVisibility(View.GONE);
                if (!giftid.equals("") && !giftnums.equals("")) {
//                    if (secgiftids.size() > 0) {
////                         removeDuplicate(secgiftids);//去重
//                        String result = "";
//                        for (int i = 0; i < removeDuplicate(secgiftids).size(); i++) {//去重后转为字符串
//                            if (!removeDuplicate(secgiftids).get(i).equals(MyApplication.getInstance().getUserId())) {//去除掉自己
//                                result += removeDuplicate(secgiftids).get(i) + ",";
//                            }
//                        }
//                        if (result.length() > 0) {
//                            toUserIdArray = result.substring(0, result.length() - 1);
//                        } else {
//                            toUserIdArray = MyApplication.getInstance().getUserId();
//                        }
//                    }
                    if (recyAdapter.getSelectedItem().size() > 0) {//从适配器获取选中的ID
                        String finresult = "";
                        for (int f = 0; f < recyAdapter.getSelectedItem().size(); f++) {
                            finresult += recyAdapter.getSelectedItem().get(f) + ",";
                        }
                        if (finresult.length() > 0) {
                            toUserIdArray = finresult.substring(0, finresult.length() - 1);
                        } else {
                            toUserIdArray = MyApplication.getInstance().getUserId();
                        }
                    }
                    Log.e("赠送礼物", "toUserIdArray=" + toUserIdArray);
                    if (toUserIdArray.length() > 0) {
//                        dismiss();
                        //赠送成功需要刷新数据
                        Log.e("SendType", "" + SendType);
                        if (SendType.equals("gift")) {
                            getData(handler, roomid);//赠送礼物
                        } else if (SendType.equals("pakage")) {
                            SendPakage(handler, roomid);//赠送背包礼物
                        }
                    } else {
                        EventBus.getDefault().post(new TabCheckEvent("提示" + "请选择送礼用户"));
                    }
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + "请选择礼物"));
                }
            }
        });
        for (int i = 0; i < picdata.size(); i++) {
            picdata.get(i).setChecked(false);
        }
        recyAdapter.notifyDataSetChanged();
        recyAdapter.setOnItemClickListener(new GiftPicRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//多选
                recyAdapter.ChangeBGall(false, false);
                Log.e("recyAdapterOnClick", "tag=" + tag);
                if (picdata.get(tag).getChecked()) {
                    picdata.get(tag).setChecked(false);
                    for (int i = 0; i < secgiftids.size(); i++) {
                        if (picdata.get(tag).getMikerId().equals(secgiftids.get(i))) {//不重复就添加进来
                            if (tag < secgiftids.size()) {
                                secgiftids.remove(tag);//移除选中
                            }
                        }
                    }
                } else {
                    picdata.get(tag).setChecked(true);
                    if (secgiftids.size() > 0) {
                        for (int i = 0; i < secgiftids.size(); i++) {
                            if (!picdata.get(tag).getMikerId().equals(secgiftids.get(i))) {//不重复就添加进来
                                secgiftids.add(picdata.get(tag).getMikerId());//添加选中
                            }
                        }
                    } else {
                        secgiftids.add(picdata.get(tag).getMikerId());//添加选中
                    }
                }
                recyAdapter.notifyDataSetChanged();

//                Log.e("recyAdapterOnClick", "secgiftids=" + secgiftids.size());
//                String result = "";
//                for (int k = 0; k < removeDuplicate(secgiftids).size(); k++) {//去重后转为字符串
//                    result += removeDuplicate(secgiftids).get(k) + ",";
//                }
//                Log.e("secgiftidsgeti", "result=" + result);//选中的送礼用户

                String finresult = "";
                for (int f = 0; f < recyAdapter.getSelectedItem().size(); f++) {
                    finresult += recyAdapter.getSelectedItem().get(f) + ",";
                }
                Log.e("secgiftidsgeti", "finresult=" + finresult);//选中的送礼用户
            }

        });
        main_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "main_cb.isChecked()=" + main_cb.isChecked());
                if (main_cb.isChecked()) {//全选
                    recyAdapter.ChangeBGall(true, true);//选中所有
                    recyAdapter.getSelectedItem().clear();
                    if (picdata.size() > 0) {
                        String result = "";
                        for (int i = 0; i < picdata.size(); i++) {
                            if (!picdata.get(i).getMikerId().equals(MyApplication.getInstance().getUserId())) {//去除掉自己
                                result += picdata.get(i).getMikerId() + ",";
                            }
                        }
                        if (result.length() > 0) {
                            toUserIdArray = result.substring(0, result.length() - 1);
                        } else {
                            toUserIdArray = MyApplication.getInstance().getUserId();
                        }
                        Log.e("main_cb多选", "result1=" + result + "   toUserIdArray=" + toUserIdArray);
                    }

                    String finresult = "";
                    for (int f = 0; f < recyAdapter.getSelectedItem().size(); f++) {
                        finresult += recyAdapter.getSelectedItem().get(f) + ",";
                    }
                    Log.e("secgiftidsgeti", "全选=" + finresult);//选中的送礼用户
                } else {//全不选
                    secgiftids.clear();
                    recyAdapter.getSelectedItem().clear();
                    recyAdapter.ChangeBGall(false, true);//取消选中所有
                    toUserIdArray = "";


                    String finresult = "";
                    for (int f = 0; f < recyAdapter.getSelectedItem().size(); f++) {
                        finresult += recyAdapter.getSelectedItem().get(f) + ",";
                    }
                    Log.e("secgiftidsgeti", "全不选=" + finresult);//选中的送礼用户
                }
            }
        });
        recyAdapter3.setOnItemClickListener(new GiftNumsRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//单选  选择送礼数量
                Log.e("onClick", "tag=" + tag);
                recyAdapter3.ChangeBG(true, tag);
                giftnums = numsdata.get(tag).getMikeNumber();//选择的礼物数量
                nums_tv.setText("X" + numsdata.get(tag).getMikeNumber());
                gift_nums_ll.setVisibility(View.GONE);
            }
        });


        //-------------------------------------礼物列表礼物-------------------------------------------------
        layoutInflater = context.getLayoutInflater();
        gridViews = new ArrayList<View>();
        gridViews2 = new ArrayList<View>();
        ///定义第一个GridView


        GridView gridView1 =
                (GridView) layoutInflater.inflate(R.layout.grid_fragment_home, null);
        myGridViewAdapter1 = new GiftGridViewAdapter(context, 0, 8);//第一页数据
        gridView1.setAdapter(myGridViewAdapter1);
        myGridViewAdapter1.setGifts((ArrayList<MikeArray>) giftArray);
        myGridViewAdapter1.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//单选
                Log.e("onClick", "tag=" + tag);
                myGridViewAdapter2.ChangeBG(false, tag);
//                myGridViewAdapter3.ChangeBG(false, tag);
                myGridViewAdapter1.ChangeBG(true, tag);//重置其他选中状态
                giftid = giftArray.get(tag).getGiftId();//选择的礼物ID
            }
        });
        ///定义第二个GridView
        GridView gridView2 = (GridView)
                layoutInflater.inflate(R.layout.grid_fragment_home, null);
        myGridViewAdapter2 = new GiftGridViewAdapter(context, 1, giftArray.size() - 1 * 8);//第二页数据
        gridView2.setAdapter(myGridViewAdapter2);
        myGridViewAdapter2.setGifts((ArrayList<MikeArray>) giftArray);
        myGridViewAdapter2.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//单选
                Log.e("onClick", "tag=" + tag);
//                myGridViewAdapter3.ChangeBG(false, tag);
                myGridViewAdapter1.ChangeBG(false, tag);
                myGridViewAdapter2.ChangeBG(true, tag);//重置其他选中状态
                giftid = giftArray.get(tag).getGiftId();//选择的礼物ID
            }
        });
//
        gridViews.add(gridView1);
        gridViews.add(gridView2);
//        gridViews.add(gridView3);

        //-------------------------------------背包礼物-------------------------------------------------
        Log.e("emety_pakage_ll", "size=" + pakagegiftArray.size());
        ///定义第一个GridView
        gridViewpk1 =
                (GridView) layoutInflater.inflate(R.layout.grid_fragment_home, null);
        ///定义第二个GridView
        gridViewpk2 = (GridView)
                layoutInflater.inflate(R.layout.grid_fragment_home, null);

        gridViews2.add(gridViewpk1);
        gridViews2.add(gridViewpk2);
//        if (pakagegiftArray.size() > 0) {
//            ///定义第一个GridView
//            gridViewpk1 =
//                    (GridView) layoutInflater.inflate(R.layout.grid_fragment_home, null);
//            ///定义第二个GridView
//            gridViewpk2 = (GridView)
//                    layoutInflater.inflate(R.layout.grid_fragment_home, null);
//            if (pakagegiftArray.size() <= 8) {
//                Log.e("背包礼物数量", "pakagegiftArray.size()=" + pakagegiftArray.size());
//                myGridViewAdapterpk1 = new GiftGridViewAdapter(context, 0, pakagegiftArray.size());//第一页数据
//                gridViewpk1.setAdapter(myGridViewAdapterpk1);
//                myGridViewAdapterpk1.setGifts((ArrayList<MikeArray>) pakagegiftArray);//设置数据
//                myGridViewAdapterpk1.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
//                    @Override//背包第一页数据点击事件
//                    public void onItemClick(int tag) {//单选
//                        Log.e("onClick", "tag=" + tag);
//                        myGridViewAdapterpk1.ChangeBG(true, tag);//重置其他选中状态
//                        giftid = pakagegiftArray.get(tag).getGiftId();//选择的礼物ID
//                    }
//                });
//                gridViews2.add(gridViewpk1);
//            } else {
//                myGridViewAdapterpk2 = new GiftGridViewAdapter(context, 1, pakagegiftArray.size() - 1 * 8);//第二页数据
//                gridViewpk2.setAdapter(myGridViewAdapterpk2);
//                myGridViewAdapterpk2.setGifts((ArrayList<MikeArray>) pakagegiftArray);
//                myGridViewAdapterpk2.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
//                    @Override//背包第二页数据点击事件
//                    public void onItemClick(int tag) {//单选
//                        Log.e("onClick", "tag=" + tag);
////                myGridViewAdapter3.ChangeBG(false, tag);
//                        myGridViewAdapterpk1.ChangeBG(false, tag);
//                        myGridViewAdapterpk2.ChangeBG(true, tag);//重置其他选中状态
//                        giftid = pakagegiftArray.get(tag).getGiftId();//选择的礼物ID
//                    }
//                });
//                gridViews2.add(gridViewpk1);
//                gridViews2.add(gridViewpk2);
//            }
//        }
        ///定义viewpager的PagerAdapter
        view_pager.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return gridViews.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(gridViews.get(position));
                //super.destroyItem(container, position, object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                Log.e("", "");
                container.addView(gridViews.get(position));
                return gridViews.get(position);
            }
        });
        ///定义viewpager的PagerAdapter
        view_pager2.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return gridViews2.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                Log.e("destroyItem", "position=" + position);
                // TODO Auto-generated method stub
                container.removeView(gridViews2.get(position));
                //super.destroyItem(container, position, object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                Log.e("instantiateItem", "position=" + position);
                container.addView(gridViews2.get(position));
                return gridViews2.get(position);
            }
        });

        //生成相应数量的导航小圆点
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置小圆点左右之间的间隔
        params.setMargins(0, 0, 0, 0);
        dotViews = new ImageView[2];
        for (int i = 0; i < 2; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.point_selecter);
            if (i == 0) {
                //默认启动时，选中第一个小圆点
                imageView.setSelected(true);
            } else {
                imageView.setSelected(false);
            }
            //得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            dotViews[i] = imageView;
            //添加到布局里面显示
            dot_layout.addView(imageView);
        }
        //for循环有多少图片就有多少小圆点 图片的长度

        ///注册viewPager页选择变化时的响应事件
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                Log.e("onPageScrolled", "arg0=" + arg0);
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotViews.length; i++) {
                    if (position == i) {
                        dotViews[i].setSelected(true);
                    } else {
                        dotViews[i].setSelected(false);
                    }
                }

                Log.e("onPageSelected", "position=" + position);
            }
        });
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x01000000);
        this.setBackgroundDrawable(dw);

        conentView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = conentView.findViewById(R.id.ipopwindowlayout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }


    /**
     * 发起网络请求  送礼物
     */
    public void getData(final Handler handler, final String roomId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("giftId", giftid).add("count", giftnums).add("toUserIdArray", toUserIdArray)//toUserIdArray  收礼人的用户id   例 3,4,5,6,
                            .add("roomId", roomId).build();
                    Log.e("getData", "userId=" + MyApplication.getInstance().getUserId() + "   token=" + MyApplication.getInstance().getToken() + "   roomId=" + roomId + "  giftId=" + giftid +
                            " count=" + giftnums + "  toUserIdArray=" + toUserIdArray);
                    final Request request = new Request.Builder().url(NetConstant.API_SendGift).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Sendgiftid = giftid;

                        final String responseStr = response.body().string();
                        Log.e("SendGift", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(22, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发起网络请求  送背包礼物
     */
    public void SendPakage(final Handler handler, final String roomId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("giftId", giftid).add("count", giftnums).add("toUserIdArray", toUserIdArray)//toUserIdArray  收礼人的用户id   例 3,4,5,6,
                            .add("roomId", roomId).build();
                    Log.e("SendPakage", "userId=" + MyApplication.getInstance().getUserId() + "   token=" + MyApplication.getInstance().getToken() + "   roomId=" + roomId + "  giftId=" + giftid +
                            " count=" + giftnums + "  toUserIdArray=" + toUserIdArray);
                    final Request request = new Request.Builder().url(NetConstant.API_SendPersonGift).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Sendgiftid = giftid;
                        final String responseStr = response.body().string();
                        Log.e("SendPakage", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(21, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 接收解析后传过来的数据
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 21) {//送背包礼物成功
                MainModel model = (MainModel) msg.obj;
                if (model != null) {
                    if (model.getCode().equals("1")) {
                        FindUserinfo(handler);//查询自己用户信息
                        //todo  发送消息通知适配器刷新页面 并把数字-1
//                        if (myGridViewAdapterpk1 != null) {
//                            int needdelnums = model.getData().getAttachArray().size() * Integer.parseInt(giftnums);//   送礼的数量= 送礼人数数组的id数量 X giftnums选择的礼物数量
//                            myGridViewAdapterpk1.ChangeCount(Sendgiftid, needdelnums);//重置礼物数字  参数 礼物ID  送出的礼物数量
//                            Log.e("送背包礼物成功", "model.getData().getAttachArray().size()=" + model.getData().getAttachArray().size() + "   giftnums=" + giftnums + "  needdelnums=" + needdelnums + " Sendgiftid=" + Sendgiftid);
//                        }
//                        {"code":1,"errorMsg":"","data":{"diamond":99996,"attachArray":["{\"fromUser\":{\"expRank\":1,\"portraitPath\":\"http://www.tuerapp.com/img/user/482275_1558147445296.png\",\"userId\":3,\"username\":\"Alexander\"},\"gift\":{\"count\":1,\"giftId\":10},\"roomId\":14,\"toUser\":{\"expRank\":0,\"portraitPath\":\"http://www.tuerapp.com/img/user/7_1559354548265.png\",\"userId\":7,\"username\":\"啊喵啊狗\"},\"type\":11}"]}}
                        EventBus.getDefault().post(new TabCheckEvent("提示" + "赠送成功"));
                        EventBus.getDefault().post(new TabCheckEventList(model.getData().getAttachArray()));
                        EventBus.getDefault().post(new TabCheckEvent("礼物" + Sendgiftid));
                        FindPersonalGift(handler);//查询背包礼物
                    } else {
                        give_tv.setEnabled(true);//重置按钮点击
                        EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                    }
                }
            } else if (msg.what == 22) {//送礼物成功
                MainModel model = (MainModel) msg.obj;
                if (model != null) {
                    if (model.getCode().equals("1")) {
                        FindUserinfo(handler);//查询自己用户信息
//                        {"code":1,"errorMsg":"","data":{"diamond":99996,"attachArray":["{\"fromUser\":{\"expRank\":1,\"portraitPath\":\"http://www.tuerapp.com/img/user/482275_1558147445296.png\",\"userId\":3,\"username\":\"Alexander\"},\"gift\":{\"count\":1,\"giftId\":10},\"roomId\":14,\"toUser\":{\"expRank\":0,\"portraitPath\":\"http://www.tuerapp.com/img/user/7_1559354548265.png\",\"userId\":7,\"username\":\"啊喵啊狗\"},\"type\":11}"]}}
                        EventBus.getDefault().post(new TabCheckEvent("提示" + "赠送成功"));
                        EventBus.getDefault().post(new TabCheckEventList(model.getData().getAttachArray()));
                        EventBus.getDefault().post(new TabCheckEvent("礼物" + Sendgiftid));
                    } else {
                        EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                    }
                }
                give_tv.setEnabled(true);//重置按钮点击
            } else if (msg.what == 23) {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
//                    if (showtext.equals("0")) {
//                        follow_tv.setText("+关注");
//                    } else {
//                        follow_tv.setText("已关注");
//                    }
                } else {
                    Toast.makeText(context, model.getErrorMsg(), Toast.LENGTH_SHORT);
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            } else if (msg.what == 24) {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    money_tv.setText(model.getData().getDiamond());
                } else {
                    Toast.makeText(context, model.getErrorMsg(), Toast.LENGTH_SHORT);
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            } else if (msg.what == 25) {
                final MainGiftModel model = (MainGiftModel) msg.obj;
                if (model.getCode().equals("1")) {
                    giftbgArray.clear();
                    if (model.getData().size() <= 8) {
                        Log.e("背包礼物数量", "pakagegiftArray.size()=" + model.getData().size());
                        myGridViewAdapterpk1 = new GiftGridViewAdapter(context, 0, model.getData().size());//第一页数据
                        gridViewpk1.setAdapter(myGridViewAdapterpk1);
                        myGridViewAdapterpk1.setGifts((ArrayList<MikeArray>) model.getData());
                        myGridViewAdapterpk1.setOnItemClickListener(mOnItemClickListener);
//                        gridViews2.add(gridViewpk1);
                    } else {
                        myGridViewAdapterpk1 = new GiftGridViewAdapter(context, 0, model.getData().size());//第一页数据
                        gridViewpk1.setAdapter(myGridViewAdapterpk1);
                        myGridViewAdapterpk1.setGifts((ArrayList<MikeArray>) model.getData());
                        myGridViewAdapterpk1.setOnItemClickListener(mOnItemClickListener);

                        myGridViewAdapterpk2 = new GiftGridViewAdapter(context, 1, model.getData().size() - 1 * 8);//第二页数据
                        gridViewpk2.setAdapter(myGridViewAdapterpk2);
                        myGridViewAdapterpk2.setGifts((ArrayList<MikeArray>) model.getData());
                        myGridViewAdapterpk2.setOnItemClickListener(mOnItemClickListener2);
//                        gridViews2.add(gridViewpk1);
//                        gridViews2.add(gridViewpk2);
                    }
                    for (int i = 0; i < model.getData().size(); i++) {
                        giftbgArray.add(model.getData().get(i));
                        if (giftid != null && giftid.length() > 0) {
                            if (model.getData().get(i).getGiftId().equals(giftid)) {
                                if (i <= 8) {
                                    myGridViewAdapterpk1.ChangeBG(true, i);//重置其他选中状态
                                } else {
                                    myGridViewAdapterpk1.ChangeBG(false, 0);
                                    myGridViewAdapterpk2.ChangeBG(true, i);//重置其他选中状态
                                }
                            }
                        }
                    }
                    give_tv.setEnabled(true);//重置按钮点击
                } else {
                    give_tv.setEnabled(true);//重置按钮点击
                    Toast.makeText(context, model.getErrorMsg(), Toast.LENGTH_SHORT);
                    EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                }
            }
        }
    };


    /**
     * 发起网络请求
     */
    public static void getData(final Handler handler, final String followId, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("followId", followId).build();
                    String Url = "";
                    if (type.equals("0")) {
                        Url = NetConstant.API_FollowUser;//关注
                    } else {
                        Url = NetConstant.API_CancelFollowUser;//取消关注
                    }
                    final Request request = new Request.Builder().url(Url).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("getDataFollow", "type=" + type + "   followId=" + followId);

                        Log.e("getDataFollow", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(23, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 发起网络请求
     */
    public static void FindUserinfo(final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken())
                            .add("infoUserId", MyApplication.getInstance().getUserId()).build();
                    final Request request = new Request.Builder().url(NetConstant.API_FindUserInfo).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("API_FindUserInfo", "responseStr=" + responseStr);
                        MainModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(24, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * 发起网络请求 查询背包礼物
     */
    public static void FindPersonalGift(final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder().add("userId", MyApplication.getInstance().getUserId()).add("token", MyApplication.getInstance().getToken()).build();
                    final Request request = new Request.Builder().url(NetConstant.API_FindPersonalGift).post(formBody).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseStr = response.body().string();
                        Log.e("API_FindPersonalGift", "responseStr=" + responseStr);
                        MainGiftModel mMainModel = new Gson().fromJson(responseStr,
                                new TypeToken<MainGiftModel>() {
                                }.getType());
                        handler.sendMessage(handler.obtainMessage(25, mMainModel));
                    } else {
                        Log.i(TAG, "okHttp is request error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    private int getNavigationBarHeight() {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }


    /**
     * 循环list中的所有元素然后删除重复
     *
     * @param list 待去重的list
     * @return 去重后的list
     */
    public static List<String> removeDuplicate(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }

        return list;
    }

    private GiftMainRecyAdapter.OnItemClickListener mOnItemClickListener = new GiftMainRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Log.e("onClick", "tag=" + position);
            myGridViewAdapterpk1.ChangeBG(true, position);//重置其他选中状态
            giftid = giftbgArray.get(position).getGiftId();//选择的礼物ID
        }
    };
    private GiftMainRecyAdapter.OnItemClickListener mOnItemClickListener2 = new GiftMainRecyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            myGridViewAdapterpk1.ChangeBG(false, position);
            myGridViewAdapterpk2.ChangeBG(true, position);//重置其他选中状态
            giftid = giftbgArray.get(position).getGiftId();//选择的礼物ID
        }
    };


//        myGridViewAdapterpk1.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
//        @Override//背包第一页数据点击事件
//        public void onItemClick(int tag) {//单选
//            Log.e("onClick", "tag=" + tag);
//            myGridViewAdapterpk1.ChangeBG(true, tag);//重置其他选中状态
//            giftid = pakagegiftArray.get(tag).getGiftId();//选择的礼物ID
//        }
//    });
//       myGridViewAdapterpk2.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
//        @Override//背包第二页数据点击事件
//        public void onItemClick(int tag) {//单选
//            Log.e("onClick", "tag=" + tag);
////                myGridViewAdapter3.ChangeBG(false, tag);
//            myGridViewAdapterpk1.ChangeBG(false, tag);
//            myGridViewAdapterpk2.ChangeBG(true, tag);//重置其他选中状态
//            giftid = pakagegiftArray.get(tag).getGiftId();//选择的礼物ID
//        }
//    });

}
