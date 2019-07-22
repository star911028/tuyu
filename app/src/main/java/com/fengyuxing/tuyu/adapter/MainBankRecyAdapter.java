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

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class MainBankRecyAdapter extends RecyclerView.Adapter<MainBankRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<String> datas;
    private OnItemClickListener onItemClickListener;
    private int[] bgAraay = {R.drawable.main_bank_bg1, R.drawable.main_bank_bg2,};//R.drawable.main_bank_bg3
    private int[] corAraay = {R.color.bank_col1, R.color.bank_col2};//,R.color.bank_col3
    private int[] textAraay = {R.string.bank_tv1, R.string.bank_tv2};//,R.string.bank_tv3

    public MainBankRecyAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainbank_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        int length = bgAraay.length;
        int index = position;
        holder.main_ll.setBackgroundResource(bgAraay[index % length]);  //设置背景颜色
        holder.type_tv.setTextColor(context.getResources().getColor(corAraay[index % length]));  //设置文字颜色
        holder.type_tv.setText(textAraay[index % length]);  //设置文字
        if (datas.size() > 0) {
            if (datas.size() == 1) {
                Glide.with(context)
                        .load(datas.get(0))
                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                        .into(holder.img_iv1);
                holder.img_iv1.setVisibility(View.VISIBLE);
                holder.img_iv2.setVisibility(View.GONE);
                holder.img_iv3.setVisibility(View.GONE);
            } else if (datas.size() == 2) {
                Glide.with(context)
                        .load(datas.get(0))
                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                        .into(holder.img_iv1);
                Glide.with(context)
                        .load(datas.get(1))
                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                        .into(holder.img_iv2);
                holder.img_iv1.setVisibility(View.VISIBLE);
                holder.img_iv2.setVisibility(View.VISIBLE);
                holder.img_iv3.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(datas.get(0))
                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                        .into(holder.img_iv1);
                Glide.with(context)
                        .load(datas.get(1))
                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                        .into(holder.img_iv2);
                Glide.with(context)
                        .load(datas.get(2))
                        .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                        .into(holder.img_iv3);
                holder.img_iv1.setVisibility(View.VISIBLE);
                holder.img_iv2.setVisibility(View.VISIBLE);
                holder.img_iv3.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img, img_iv1, img_iv2, img_iv3;
        LinearLayout main_ll;
        TextView type_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            img_iv1 = itemView.findViewById(R.id.img_iv1);
            img_iv2 = itemView.findViewById(R.id.img_iv2);
            img_iv3 = itemView.findViewById(R.id.img_iv3);
            main_ll = itemView.findViewById(R.id.main_ll);
            type_tv = itemView.findViewById(R.id.type_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int tag);
    }
}

