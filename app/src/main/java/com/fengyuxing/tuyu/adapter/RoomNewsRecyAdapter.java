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
import com.fengyuxing.tuyu.bean.NewsArray;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class RoomNewsRecyAdapter extends RecyclerView.Adapter<RoomNewsRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<NewsArray> datas;
    private OnItemClickListener onItemClickListener;

    public RoomNewsRecyAdapter(Context context, List<NewsArray> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_news_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        int index = position;
//        holder.main_ll.setBackgroundResource(bgAraay[index % length]);  //设置背景颜色
        holder.content_tv.setText(datas.get(position).getBody());  //设置文字
        if(datas.get(position).getExpRank()!=null&&datas.get(position).getExpRank().length()>0){
            holder.level_iv.setVisibility(View.VISIBLE);
            int levelnum = Integer.valueOf(datas.get(position).getExpRank()).intValue();//用户等级
            Glide.with(context)
                    .load(RetrofitService.bgAraay[levelnum])
                    .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                    .into(holder.level_iv);
        }else {
            holder.level_iv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

//    @Override
//    public void onClick(View view) {
//        if (onItemClickListener != null) {
//            onItemClickListener.onItemClick(view, (Integer) view.getTag());
//        }
//    }


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
        ImageView level_iv;
        LinearLayout main_ll;
        TextView content_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            level_iv = itemView.findViewById(R.id.level_iv);
            content_tv = itemView.findViewById(R.id.content_tv);
            main_ll = itemView.findViewById(R.id.main_ll);
        }
    }

//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.onItemClickListener = listener;
//    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, int tag);
//    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

