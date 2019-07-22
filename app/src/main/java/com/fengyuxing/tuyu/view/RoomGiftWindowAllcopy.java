package com.fengyuxing.tuyu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.adapter.GiftMainRecyAdapter;
import com.fengyuxing.tuyu.adapter.GiftNumsRecyAdapter;
import com.fengyuxing.tuyu.adapter.GiftPicRecyAdapter;
import com.fengyuxing.tuyu.bean.MainModel;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.constant.NetConstant;
import com.fengyuxing.tuyu.util.TabCheckEvent;
import com.fengyuxing.tuyu.util.TabCheckEventList;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.fengyuxing.tuyu.view.ConstantsString.TAG;


//房间点击底部礼物弹窗

public class RoomGiftWindowAllcopy extends PopupWindow {
    private View conentView;
    private Context context;
    private LinearLayout pop_layout, info_ll, all_user_gift_ll, one_user_gift_ll, gift_nums_ll, sec_nums_ll;
    private RecyclerView recycler_pic, recycler_gift, recycler_gift_nums;
    private ImageView img_iv;
    private TextView pay_tv, follow_tv, setadmin_tv, give_tv, name_tv, nums_tv;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager layoutManager2;

    private GiftPicRecyAdapter recyAdapter;
    private GiftMainRecyAdapter recyAdapter2;
    private GiftNumsRecyAdapter recyAdapter3;
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

    public RoomGiftWindowAllcopy(final Activity context, OnClickListener l, final String clicktype, final String isman, final List<MikeArray> datas, final List<MikeArray> giftArray, final String roomid, final int position) {//datas是当前所有用户信息  giftArray是当前所有礼物信息
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.room_giftall_window, null);
        pop_layout = (LinearLayout) conentView.findViewById(R.id.pop_layout);
        all_user_gift_ll = (LinearLayout) conentView.findViewById(R.id.all_user_gift_ll);
        one_user_gift_ll = (LinearLayout) conentView.findViewById(R.id.one_user_gift_ll);
        gift_nums_ll = (LinearLayout) conentView.findViewById(R.id.gift_nums_ll);
        name_tv = (TextView) conentView.findViewById(R.id.name_tv);
        sec_nums_ll = (LinearLayout) conentView.findViewById(R.id.sec_nums_ll);
        info_ll = (LinearLayout) conentView.findViewById(R.id.info_ll);
        recycler_pic = (RecyclerView) conentView.findViewById(R.id.recycler_pic);
        recycler_gift = (RecyclerView) conentView.findViewById(R.id.recycler_gift);
        recycler_gift_nums = (RecyclerView) conentView.findViewById(R.id.recycler_gift_nums);
        nums_tv = (TextView) conentView.findViewById(R.id.nums_tv);
        main_cb = (CheckBox) conentView.findViewById(R.id.main_cb);
        follow_tv = (TextView) conentView.findViewById(R.id.follow_tv);
        img_iv = (ImageView) conentView.findViewById(R.id.img_iv);
        pay_tv = (TextView) conentView.findViewById(R.id.pay_tv);
        setadmin_tv = (TextView) conentView.findViewById(R.id.setadmin_tv);
        give_tv = (TextView) conentView.findViewById(R.id.give_tv);
        if (clicktype.equals("1")) {//麦上用户打开单人礼物盘
            all_user_gift_ll.setVisibility(View.GONE);
            one_user_gift_ll.setVisibility(View.VISIBLE);
            if (isman.equals("1")) {//管理员
                setadmin_tv.setVisibility(View.VISIBLE);
            } else {//普通用户
                setadmin_tv.setVisibility(View.GONE);
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
            follow_tv.setOnClickListener(new OnClickListener() {
                @Override//关注
                public void onClick(View v) {
                    if (follow_tv.getText().equals("+关注")) {//取消关注
                        showtext = "1";
                        getData(handler, datas.get(position).getMikerId(), "1");
                    } else {//关注
                        showtext = "0";
                        getData(handler, datas.get(position).getMikerId(), "0");

                    }

                }
            });
            toUserIdArray = datas.get(position).getMikerId();
        } else {//底部礼物盘
            one_user_gift_ll.setVisibility(View.GONE);
            all_user_gift_ll.setVisibility(View.VISIBLE);
        }

        picdata.clear();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getMikerPortraitPath() != null) {
                picdata.add(datas.get(i));
            }
        }
        //房间人数头像
        recyAdapter = new GiftPicRecyAdapter(context, picdata);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_pic.setLayoutManager(layoutManager);
        recycler_pic.setAdapter(recyAdapter);


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

        //礼物列表  GridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout)
        recyAdapter2 = new GiftMainRecyAdapter(context, giftArray);
        recycler_gift.setLayoutManager(new GridLayoutManager(context, 4,GridLayoutManager.VERTICAL,false));//设置横向滚动
//        optMiz(recycler_gift, new GridLayoutManager(context, 4));//解决netscrollview 嵌套recyclerview引起的滑动卡顿问题。
        recycler_gift.setAdapter(recyAdapter2);

        sec_nums_ll.setOnClickListener(new OnClickListener() {
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
        give_tv.setOnClickListener(new OnClickListener() {
            @Override//赠送
            public void onClick(View v) {
                Log.e("onClick", "main_cb.isChecked()=" + main_cb.isChecked());
                gift_nums_ll.setVisibility(View.GONE);
                if (!giftid.equals("") && !giftnums.equals("")) {
                    if (toUserIdArray.length() > 0) {
                        getData(handler, roomid);//更新数据
                    } else {
                        EventBus.getDefault().post(new TabCheckEvent("提示" + "请选择送礼用户"));
                    }
                } else {
                    EventBus.getDefault().post(new TabCheckEvent("提示" + "请选择礼物"));
                }
            }
        });
        recyAdapter.setOnItemClickListener(new GiftPicRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//单选
                Log.e("recyAdapterOnClick", "tag=" + tag);
                recyAdapter.ChangeBG(true, tag);
                toUserIdArray = picdata.get(tag).getMikerId();
                Log.e("recyAdapterOnClick", "toUserIdArray=" + toUserIdArray);
            }
        });
        main_cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "main_cb.isChecked()=" + main_cb.isChecked());
                if (main_cb.isChecked()) {//多选
                    recyAdapter.ChangeBGall(true, true);
                    if (picdata.size() > 0) {
                        String result = "";
                        for (int i = 0; i < picdata.size(); i++) {
                            picdata.get(i).getMikerId();
                            result += picdata.get(i).getMikerId() + ",";
                        }
                        toUserIdArray = result.substring(0, result.length() - 1);
                        Log.e("onClick", "result1=" + result + "   toUserIdArray=" + toUserIdArray);
                    }
                } else {
                    recyAdapter.ChangeBGall(false, true);
                    toUserIdArray = "";
                }
            }
        });

        recyAdapter2.setOnItemClickListener(new GiftMainRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//单选
                Log.e("onClick", "tag=" + tag);
                recyAdapter2.ChangeBG(true, tag);
                giftid = giftArray.get(tag).getGiftId();//选择的礼物ID
            }
        });
        recyAdapter3.setOnItemClickListener(new GiftNumsRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag) {//单选
                Log.e("onClick", "tag=" + tag);
                recyAdapter3.ChangeBG(true, tag);
                giftnums = numsdata.get(tag).getMikeNumber();//选择的礼物数量
                nums_tv.setText("X" + numsdata.get(tag).getMikeNumber());
                gift_nums_ll.setVisibility(View.GONE);
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
     * 发起网络请求
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
                        Log.e("getData", "responseStr=" + responseStr);
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
     * 接收解析后传过来的数据
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 22) {
                MainModel model = (MainModel) msg.obj;
                if (model != null) {
                    if (model.getCode().equals("1")) {
//                        {"code":1,"errorMsg":"","data":{"diamond":99996,"attachArray":["{\"fromUser\":{\"expRank\":1,\"portraitPath\":\"http://www.tuerapp.com/img/user/482275_1558147445296.png\",\"userId\":3,\"username\":\"Alexander\"},\"gift\":{\"count\":1,\"giftId\":10},\"roomId\":14,\"toUser\":{\"expRank\":0,\"portraitPath\":\"http://www.tuerapp.com/img/user/7_1559354548265.png\",\"userId\":7,\"username\":\"啊喵啊狗\"},\"type\":11}"]}}
                        EventBus.getDefault().post(new TabCheckEvent("提示" + "赠送成功"));
                        EventBus.getDefault().post(new TabCheckEventList(model.getData().getAttachArray()));
                        EventBus.getDefault().post(new TabCheckEvent("礼物" + Sendgiftid));
                        dismiss();
                    } else {
                        EventBus.getDefault().post(new TabCheckEvent("提示" + model.getErrorMsg()));
                    }
                }
            } else if (msg.what == 23) {
                MainModel model = (MainModel) msg.obj;
                if (model.getCode().equals("1")) {
                    if (showtext.equals("0")) {
                        follow_tv.setText("+关注");
                    } else {
                        follow_tv.setText("取消关注");
                    }
                    dismiss();
                } else {
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
                        Log.e("getData", "responseStr=" + responseStr);
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


}
