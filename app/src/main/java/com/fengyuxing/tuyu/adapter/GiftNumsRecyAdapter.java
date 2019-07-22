package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MikeArray;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class GiftNumsRecyAdapter extends RecyclerView.Adapter<GiftNumsRecyAdapter.ViewHolder> implements View.OnClickListener {
    //礼物数量
    private Context context;
    private List<MikeArray> datas;
    private OnItemClickListener onItemClickListener;
    private Boolean isCheck = false;
    private int Checktag = -2;


    public GiftNumsRecyAdapter(Context context, List<MikeArray> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.giftnums_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.nums_tv.setText(datas.get(position).getMikeNumber());
        holder.text_tv.setText(datas.get(position).getMikerName());
        //单选
        if (isCheck) {
            if (Checktag == position) {
                holder.main_ll.setBackgroundResource(R.drawable.gift_nums_itemsec_bg);
                holder.nums_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.text_tv.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.main_ll.setBackgroundResource(R.drawable.gift_nums_itemnor_bg);
                holder.nums_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                holder.text_tv.setTextColor(context.getResources().getColor(R.color.edit_text_col));
            }
        } else {
            holder.main_ll.setBackgroundResource(R.drawable.gift_nums_itemnor_bg);
            holder.nums_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
            holder.text_tv.setTextColor(context.getResources().getColor(R.color.edit_text_col));
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
        LinearLayout main_ll;
        TextView nums_tv, text_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            main_ll = itemView.findViewById(R.id.main_ll);
            text_tv = itemView.findViewById(R.id.text_tv);
            nums_tv = itemView.findViewById(R.id.nums_tv);
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

