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
import com.fengyuxing.tuyu.bean.DataList;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class GiftRecyAdapter extends RecyclerView.Adapter<GiftRecyAdapter.ViewHolder> implements View.OnClickListener {
        //收到礼物
    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;
    public GiftRecyAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.giftrc_item, parent, false);
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
        holder.nums_tv.setText(datas.get(position).getGiftCount());
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
        ImageView img_iv;
        LinearLayout main_ll;
        TextView nums_tv;
        public ViewHolder(View itemView) {
            super(itemView);
            img_iv = itemView.findViewById(R.id.img_iv);
            main_ll= itemView.findViewById(R.id.main_ll);
            nums_tv= itemView.findViewById(R.id.nums_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int tag);
    }
}

