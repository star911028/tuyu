package com.fengyuxing.tuyu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zego.zegoaudioroom.ZegoAudioStream;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;
import com.zego.zegoliveroom.entity.ZegoUserState;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MikeArray;
import com.fengyuxing.tuyu.zego.RecyclerGridViewAdapter;
import com.fengyuxing.tuyu.zego.ZegoUserInfo;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/1/5.
 */

public class MainRoomSetAdapter extends RecyclerView.Adapter<MainRoomSetAdapter.ViewHolder> implements View.OnClickListener {
    //主房间座位
    private Context context;
    private List<MikeArray> datas;
    private OnItemClickListener onItemClickListener;
    private Boolean isShow = false;
    private ZegoUserState selfZegoUser = null;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private volatile List<ZegoUserInfo> zegoUserList = new ArrayList<>();
    private int[] giftAraay = {R.drawable.emoji1, R.drawable.emoji12, R.drawable.emoji13, R.drawable.emoji14, R.drawable.emoji15, R.drawable.emoji16, R.drawable.emoji17, R.drawable.emoji18, R.drawable.emoji9
            , R.drawable.emoji10, R.drawable.emoji11, R.drawable.emoji2, R.drawable.emoji13, R.drawable.emoji14, R.drawable.emoji15, R.drawable.emoji16, R.drawable.emoji17, R.drawable.emoji18,};
    private int emojiindex = -1;
    private int emojiposition = -1;

    public MainRoomSetAdapter(Context context, List<MikeArray> datas) {
        this.context = context;
        this.datas = datas;
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.UP);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_room_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        if (datas.get(position).getMikerPortraitPath() != null) {
            if(datas.get(position).getStatus().equals("1")){//通过这个状态判断 直播间是否打开麦克风说话
                holder.closemic_iv.setVisibility(View.VISIBLE);
            }else {
                holder.closemic_iv.setVisibility(View.GONE);
            }
            Glide.with(context)//用户头像
                    .load(datas.get(position).getMikerPortraitPath())
                    .apply(new RequestOptions().placeholder(R.mipmap.room_seat).error(R.mipmap.room_seat).dontAnimate())
                    .into(holder.img_iv);
        } else {
            if(datas.get(position).getNeedLine().equals("true")){//需要排麦上麦
                holder.needlinemic_iv.setVisibility(View.VISIBLE);
            }else {//不需要排麦上麦
                holder.needlinemic_iv.setVisibility(View.GONE);
            }
            holder.closemic_iv.setVisibility(View.GONE);
            //麦位没人时图标显示    0正常  1闭麦 2锁麦
            Log.e("","");
            if (datas.get(position).getStatus().equals("0")) {
                holder.img_iv.setImageResource(R.mipmap.room_seat);
            } else if (datas.get(position).getStatus().equals("1")) {
//                holder.closemic_iv.setVisibility(View.VISIBLE);
            } else if (datas.get(position).getStatus().equals("2")) {
                holder.img_iv.setImageResource(R.mipmap.publish_private);
            }
        }

        if (datas.get(position).getMikerName() != null) {
            holder.name_tv.setText(datas.get(position).getMikerName());
//            holder.cardiac_ll.setVisibility(View.VISIBLE);//心动值显示
            Log.e("getShowCharm", "isShow=" + isShow);
            if (isShow) {
                holder.cardiac_ll.setVisibility(View.VISIBLE);//心动值显示
            } else {
                holder.cardiac_ll.setVisibility(View.INVISIBLE);//心动值不显示
            }
            if (datas.get(position).getIsBoss().equals("true")) {
                holder.img_iv.setBorderColor(context.getResources().getColor(R.color.color_yellow));
                holder.img_iv.setBorderWidth(2);
            }
            holder.name_tv.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            if (datas.get(position).getIsBoss().equals("true")) {
                holder.name_tv.setText("老板位");
                holder.name_tv.setTextColor(context.getResources().getColor(R.color.color_yellow));
                holder.cardiac_ll.setVisibility(View.INVISIBLE);
                //老板麦位没人    0正常  1闭麦 2锁麦
                if (datas.get(position).getStatus().equals("0")) {
                    holder.img_iv.setImageResource(R.mipmap.room_bossset);
                } else if (datas.get(position).getStatus().equals("1")) {

                } else if (datas.get(position).getStatus().equals("2")) {
                    holder.img_iv.setImageResource(R.mipmap.publish_private);
                }
            } else {
                holder.name_tv.setTextColor(context.getResources().getColor(R.color.white));
                //麦位没人时图标显示    0正常  1闭麦 2锁麦
                if (datas.get(position).getStatus().equals("0")) {
                    holder.img_iv.setImageResource(R.mipmap.room_seat);
                } else if (datas.get(position).getStatus().equals("1")) {

                } else if (datas.get(position).getStatus().equals("2")) {
                    holder.img_iv.setImageResource(R.mipmap.publish_private);
                }
                holder.name_tv.setText(position + 1 + "号麦");
                holder.cardiac_ll.setVisibility(View.INVISIBLE);

            }
        }

        if (datas.get(position).getIsReceptionist() != null) {//是否是主持位
            if (datas.get(position).getIsReceptionist().equals("true")) {
                holder.isrec_tv.setVisibility(View.VISIBLE);
                //     0正常  1闭麦 2锁麦
                if (datas.get(position).getStatus().equals("0")) {
                    if(datas.get(position).getMikerPortraitPath()==null){//主持位 没人时 正常状态
                        holder.img_iv.setImageResource(R.mipmap.room_seat);
                    }
                } else if (datas.get(position).getStatus().equals("1")) {

                } else if (datas.get(position).getStatus().equals("2")) {
                    holder.img_iv.setImageResource(R.mipmap.publish_private);
                }
            } else {
                holder.isrec_tv.setVisibility(View.GONE);
            }
        }

        if (datas.get(position).getCharm() != null) {
//            float num1 = Float.parseFloat(datas.get(position).getCharm());
//            if (num1 >= 10000) {
//                DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
//                holder.nums_tv.setText(strPrice + "W");//心动值 //保留两位小数
//            } else {
//                holder.nums_tv.setText(datas.get(position).getCharm());//心动值
//            }
            holder.nums_tv.setText(datas.get(position).getCharm());//心动值
        }
        if (datas.get(position).getMikerGender() != null) {
            if (datas.get(position).getMikerGender().equals("男")) {
                holder.cardiac_ll.setBackgroundResource(R.drawable.room_cardiac_bg);
            } else {
                holder.cardiac_ll.setBackgroundResource(R.drawable.room_cardiac_bg2);
            }
        }
        if (emojiindex >= 0) {//emojiindex 表情的索引    emojiposition 坐位的索引
            if(emojiposition == position&&position==0){
                Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition0=" + emojiposition);
                holder.gift_iv.setVisibility(View.VISIBLE);
                Glide.with(context).load(giftAraay[emojiindex]).into(holder.gift_iv);
                holder.gift_iv.postDelayed(new Runnable() {
                    @Override
                    public void run() {//两秒后清除动画并隐藏控件
                        holder.gift_iv.clearAnimation();//清除动画
                        holder.gift_iv.setVisibility(View.GONE);
                    }
                }, 2000);
            }else if(emojiposition == position&&position==1){
                Log.e("MainRoomSetAdapter", "emojiindex=" + emojiindex + "   emojiposition1=" + emojiposition);
                holder.gift_iv2.setVisibility(View.VISIBLE);
                Glide.with(context).load(giftAraay[emojiindex]).into(holder.gift_iv2);
                holder.gift_iv2.postDelayed(new Runnable() {
                    @Override
                    public void run() {//两秒后清除动画并隐藏控件
                        holder.gift_iv2.clearAnimation();//清除动画
                        holder.gift_iv2.setVisibility(View.GONE);
                    }
                }, 2000);
            }


//            if (emojiposition == position) {
//                if(position==0){
//                    holder.gift_iv.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(giftAraay[emojiindex]).into(holder.gift_iv);
//                    holder.gift_iv.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {//两秒后清除动画并隐藏控件
//                            holder.gift_iv.clearAnimation();//清除动画
//                            holder.gift_iv.setVisibility(View.GONE);
//                        }
//                    }, 2000);
//                }else if(position==1){
//                    holder.gift_iv2.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(giftAraay[emojiindex]).into(holder.gift_iv2);
//                    holder.gift_iv2.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {//两秒后清除动画并隐藏控件
//                            holder.gift_iv2.clearAnimation();//清除动画
//                            holder.gift_iv2.setVisibility(View.GONE);
//                        }
//                    }, 2000);
//                }else if(position==2){
//                    holder.gift_iv3.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(giftAraay[emojiindex]).into(holder.gift_iv3);
//                    holder.gift_iv3.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {//两秒后清除动画并隐藏控件
//                            holder.gift_iv3.clearAnimation();//清除动画
//                            holder.gift_iv3.setVisibility(View.GONE);
//                        }
//                    }, 2000);
//                }
//            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            int position = (Integer) v.getTag();
            switch (v.getId()) {
                case R.id.main_ll:
                    onItemClickListener.onItemClick(position);
                    break;
            }

        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  gift_iv,gift_iv2,gift_iv3,closemic_iv,needlinemic_iv;
        LinearLayout main_ll, cardiac_ll;
        CircleImageView img_iv;
        TextView nums_tv, name_tv, isrec_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            needlinemic_iv= itemView.findViewById(R.id.needlinemic_iv);
            img_iv = itemView.findViewById(R.id.img_iv);
            closemic_iv= itemView.findViewById(R.id.closemic_iv);
            gift_iv = itemView.findViewById(R.id.gift_iv);
            gift_iv2= itemView.findViewById(R.id.gift_iv2);
            gift_iv3= itemView.findViewById(R.id.gift_iv3);
            main_ll = itemView.findViewById(R.id.main_ll);
            cardiac_ll = itemView.findViewById(R.id.cardiac_ll);
            nums_tv = itemView.findViewById(R.id.nums_tv);
            isrec_tv = itemView.findViewById(R.id.isrec_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //设置心动值是否显示
    public void changetShowcharm(boolean isShow) {
        this.isShow = isShow;
        notifyDataSetChanged();
    }


    public void setSelfZegoUser(ZegoUserState selfZegoUser) {
        this.selfZegoUser = selfZegoUser;
    }

    public void addUser(ZegoUserState zegoUserState) {
        synchronized (this) {
            for (ZegoUserInfo zegoUserInfo : zegoUserList) {
                if (zegoUserInfo.userName.equals(zegoUserState.userName)) {
                    return;
                }
            }
            ZegoUserInfo mZegoUserInfo = new ZegoUserInfo();
            mZegoUserInfo.userID = zegoUserState.userID;
            mZegoUserInfo.userName = zegoUserState.userName;
            zegoUserList.add(mZegoUserInfo);
            uiThreadNotifyDataSetChanged();
        }
    }

    public void removeUser(ZegoUserState zegoUserState) {
        synchronized (this) {
            int zegoUserIndex = -1;
            for (int i = 0; i < zegoUserList.size(); i++) {
                ZegoUserInfo zegoUser = zegoUserList.get(i);
                if (zegoUser.userName.equals(zegoUserState.userName) && zegoUser.userID.equals(zegoUserState.userID)) {
                    zegoUserIndex = i;
                }
            }
            if (zegoUserIndex != -1) {
                zegoUserList.remove(zegoUserIndex);
            }
            uiThreadNotifyDataSetChanged();
        }
    }

    public void uiThreadNotifyDataSetChanged() {
//        notifyDataSetChanged();
//        mUserRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                notifyDataSetChanged();
//            }
//        });

    }

    public void streamStateUpdate(int stateCode, ZegoAudioStream zegoAudioStream) {
        synchronized (this) {
            for (ZegoUserInfo zegoUserInfo : zegoUserList) {
                if (zegoUserInfo.streamID != null && zegoUserInfo.streamID.equals(zegoAudioStream.getStreamId())) {
                    zegoUserInfo.audioState = stateCode;
                    break;
                }
            }
        }
        uiThreadNotifyDataSetChanged();
    }

    public void streamStateUpdateAll(int stateCode) {
        synchronized (this) {
            for (ZegoUserInfo zegoUserInfo : zegoUserList) {
                zegoUserInfo.audioState = stateCode;
                break;
            }
        }
        uiThreadNotifyDataSetChanged();
    }

    /**
     * 用户绑定流ID
     */
    public void bindUserInfoStreamID(final ZegoAudioStream zegoAudioStream) {
        synchronized (this) {

            for (ZegoUserInfo zegoUserInfo : zegoUserList) {
                if (zegoAudioStream.getStreamId() != null && zegoUserInfo.userID.equals(zegoAudioStream.getUserId()) && zegoUserInfo.userName.equals(zegoAudioStream.getUserName())) {
                    zegoUserInfo.streamID = zegoAudioStream.getStreamId();
                    break;
                }
            }
            uiThreadNotifyDataSetChanged();
        }
    }

    public void updateMuteState(boolean mute, String userName) {
        synchronized (this) {
            for (ZegoUserInfo zegoUserInfo : zegoUserList) {
                if (zegoUserInfo.userName != null && zegoUserInfo.userName.equals(userName)) {
                    zegoUserInfo.mute = mute;
                    break;
                }
            }
            uiThreadNotifyDataSetChanged();
        }
    }

    @SuppressLint("StringFormatInvalid")
    public void updateQualityUpdate(String streamId, RecyclerGridViewAdapter.CommonStreamQuality zegoStreamQuality) {
//        for (RecyclerGridViewAdapter.MyHolder myHolder : myHolders) {
//            Object object = myHolder.mView.getTag();
//            if (object != null && streamId != null && streamId.equals(object.toString())) {
//                Double audioBreakRate = zegoStreamQuality.audioBreakRate;
//                int rtt = zegoStreamQuality.rtt;
//                int pktLostRate = (int) (zegoStreamQuality.pktLostRate / (2.6));
//                myHolder.textViewCard_frame_rate.setText(audioBreakRate == -1 ? "" : mContext.getString(R.string.zg_btn_text_audio_break_rate, numberFormat.format(audioBreakRate)));
//                myHolder.textViewDelay.setText(mContext.getString(R.string.zg_btn_text_delay, rtt));
//                myHolder.textViewPacket_loss_rate.setText(mContext.getString(R.string.zg_btn_text_packet_loss_rate, pktLostRate));
//                break;
//            }
//        }
    }


    /**
     * 音浪
     *
     * @param zegoSoundLevelInfo
     */
    public void soundLevelUpdate(ZegoSoundLevelInfo zegoSoundLevelInfo) {
        for (ZegoUserInfo zegoUserInfo : zegoUserList) {
            if (zegoUserInfo.streamID != null && zegoUserInfo.streamID.equals(zegoSoundLevelInfo.streamID)) {
                zegoUserInfo.progress = ((int) (zegoSoundLevelInfo.soundLevel));
                break;
            }
        }
        uiThreadNotifyDataSetChanged();
    }

    public void PlayEmoji(int emojiindex, int emojiposition) {
        this.emojiindex = emojiindex;
        this.emojiposition = emojiposition;
        notifyDataSetChanged();
    }



}

