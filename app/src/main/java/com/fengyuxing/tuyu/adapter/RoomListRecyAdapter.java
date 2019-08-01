package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.fengyuxing.tuyu.util.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class RoomListRecyAdapter extends RecyclerView.Adapter<RoomListRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;
    private Boolean ismangner = false;

    public RoomListRecyAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.roomlist_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemMaiUpClick(int position);

        void onItemRemoveClick(int position);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (datas.get(position).getPortraitPath() != null) {
            Glide.with(context)
                    .load(datas.get(position).getPortraitPath())
                    .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                    .into(holder.img_iv);
        }
        holder.title_tv.setText(datas.get(position).getUsername());

        if (datas.get(position).getGender() != null) {
            if (datas.get(position).getGender().equals("男")) {
                holder.sex_ll.setBackgroundResource(R.drawable.room_sex);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.man);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.sex_tv.setCompoundDrawables(drawable, null, null, null);
            } else {
                holder.sex_ll.setBackgroundResource(R.drawable.room_sex2);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.woman);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.sex_tv.setCompoundDrawables(drawable, null, null, null);
            }
        }
        int levelnum = Integer.valueOf(datas.get(position).getExpRank()).intValue();//用户等级
        Picasso.with(context)
                .load(RetrofitService.bgAraay[levelnum])//加载过程中的图片显示
                .placeholder(RetrofitService.bgAraay[0])//加载失败中的图片显示
                .error(RetrofitService.bgAraay[0])//如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                .into(holder.level_iv);//用户等级
        holder.sex_tv.setText(datas.get(position).getAge());
        holder.level_tv.setText("LV." + datas.get(position).getExpRank());
        holder.itemView.setTag(position);
        holder.remove_iv.setOnClickListener(new View.OnClickListener() {
            @Override//移除
            public void onClick(View v) {
                onItemClickListener.onItemRemoveClick(position);
            }
        });
        if (ismangner) {
            holder.mai_up_tv.setVisibility(View.VISIBLE);
        } else {
            holder.mai_up_tv.setVisibility(View.GONE);
        }
        if (datas.get(position).getIsMiker() != null) {
            if (datas.get(position).getIsMiker().equals("true")) {//已经在麦上的用户不显示抱上麦按钮
                holder.type_tv.setText("麦上");
                holder.mai_type_ll.setVisibility(View.VISIBLE);
                holder.mai_up_tv.setVisibility(View.GONE);
            } else {
                holder.mai_type_ll.setVisibility(View.GONE);
            }
        }
        holder.mai_up_tv.setOnClickListener(new View.OnClickListener() {
            @Override//抱ta上麦按钮
            public void onClick(View v) {
                onItemClickListener.onItemMaiUpClick(position);
            }
        });
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
        ImageView img_iv, remove_iv,level_iv;
        LinearLayout main_ll, mai_type_ll,sex_ll;
        TextView type_tv, title_tv, sex_tv, level_tv, mai_up_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            img_iv = itemView.findViewById(R.id.img_iv);
            mai_up_tv = itemView.findViewById(R.id.mai_up_tv);
            main_ll = itemView.findViewById(R.id.main_ll);
            sex_ll= itemView.findViewById(R.id.sex_ll);
            type_tv = itemView.findViewById(R.id.type_tv);
            level_tv = itemView.findViewById(R.id.level_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
            mai_type_ll = itemView.findViewById(R.id.mai_type_ll);
            sex_tv = itemView.findViewById(R.id.sex_tv);
            remove_iv = itemView.findViewById(R.id.remove_iv);
            level_iv = itemView.findViewById(R.id.level_iv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    //设置是否显示抱用户上麦
    public void changetIsMangner(boolean ismangner) {
        this.ismangner = ismangner;
        notifyDataSetChanged();
    }
}

