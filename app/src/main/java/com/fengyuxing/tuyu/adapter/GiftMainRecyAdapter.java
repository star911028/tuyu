package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MikeArray;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class GiftMainRecyAdapter extends RecyclerView.Adapter<GiftMainRecyAdapter.ViewHolder> implements View.OnClickListener {
        //礼物
    private Context context;
    private List<MikeArray> datas;
    private OnItemClickListener onItemClickListener;
    private Boolean isCheck = false;
    private int Checktag = -2;
    public GiftMainRecyAdapter(Context context, List<MikeArray> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.giftmain_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Glide.with(context)
                .load(datas.get(position).getImgPath())
                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                .into(holder.img_iv);

        holder.type_tv.setText(datas.get(position).getGiftName());
        holder.price_tv.setText(datas.get(position).getPrice());

//        holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
        //单选
        if (isCheck) {
            if (Checktag == position) {
//                holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                holder.sec_bg_ll.setBackgroundResource(R.drawable.gift_bg);
            } else {
//                holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                holder.sec_bg_ll.setBackgroundResource(R.drawable.ear_bg);
            }
        } else {
//            holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
            holder.sec_bg_ll.setBackgroundResource(R.drawable.ear_bg);
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
        ImageView img_iv;
        LinearLayout main_ll,sec_bg_ll;
        TextView type_tv,price_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            img_iv = itemView.findViewById(R.id.img_iv);
            main_ll = itemView.findViewById(R.id.main_ll);
            price_tv= itemView.findViewById(R.id.price_tv);
            sec_bg_ll = itemView.findViewById(R.id.sec_bg_ll);
            type_tv = itemView.findViewById(R.id.type_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void ChangeBG(Boolean isCheck, int Checktag) {
        this.isCheck = isCheck;
        this.Checktag = Checktag;
        notifyDataSetChanged();
    }

}

