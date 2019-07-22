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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.util.RetrofitService;

import java.util.List;

/**
 * 首页搜索
 */

public class MainSearchRecyAdapter extends RecyclerView.Adapter<MainSearchRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;

    public MainSearchRecyAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainsearch_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (datas.get(position).getPortraitPath() != null) {
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(6);
//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            Glide.with(context)
                    .load(datas.get(position).getPortraitPath())
                    .apply(options.placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                    .into(holder.img_iv);
        }
        holder.itemView.setTag(position);
        holder.main_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(position);
                return false;
            }
        });
        if (datas.get(position).getClassifyName() != null) {
            //房间信息
            holder.room_ll.setVisibility(View.VISIBLE);
            holder.user_ll.setVisibility(View.GONE);
            holder.name_tv.setText(datas.get(position).getRoomName());
            holder.type_tv.setText(datas.get(position).getClassifyName());
            holder.tuercode_tv.setText("兔语号: "+datas.get(position).getTuId());
            holder.numpeo_tv.setText(datas.get(position).getOnlineCount() + "人在玩");
            if (datas.get(position).getNeedPassword().equals("true")) {  //房间带密码
                holder.lock_ll.setVisibility(View.VISIBLE);
            } else {
                holder.lock_ll.setVisibility(View.GONE);
            }
        } else {
            //用户信息
            holder.tuercode_tv.setText("兔语号: "+datas.get(position).getTuId());
            holder.room_ll.setVisibility(View.GONE);
            holder.user_ll.setVisibility(View.VISIBLE);
            holder.name_tv.setText(datas.get(position).getUsername());
            holder.sex_tv.setText(datas.get(position).getAge());
            holder.addres_tv.setText(datas.get(position).getAddress());
            if (datas.get(position).getGender().equals("男")) {
                Drawable drawable = context.getResources().getDrawable(R.mipmap.man);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.sex_tv.setCompoundDrawables(drawable, null, null, null);
                holder.sex_ll.setBackgroundResource(R.drawable.room_sex);
            } else {
                Drawable drawable = context.getResources().getDrawable(R.mipmap.woman);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.sex_tv.setCompoundDrawables(drawable, null, null, null);
                holder.sex_ll.setBackgroundResource(R.drawable.room_sex2);
            }
            int levelnum = Integer.valueOf(datas.get(position).getExpRank()).intValue();//用户等级
            Glide.with(context)
                    .load(RetrofitService.bgAraay[levelnum ])
                    .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                    .into(holder.level_iv);
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
        ImageView img_iv, talk_iv, level_iv;
        LinearLayout main_ll, user_ll, room_ll, lock_ll, sex_ll;
        TextView  name_tv, sex_tv, tuercode_tv, type_tv, numpeo_tv,addres_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            level_iv = itemView.findViewById(R.id.level_iv);
            lock_ll = itemView.findViewById(R.id.lock_ll);
            addres_tv= itemView.findViewById(R.id.addres_tv);
            img_iv = itemView.findViewById(R.id.img_iv);
            talk_iv = itemView.findViewById(R.id.talk_iv);
            sex_ll = itemView.findViewById(R.id.sex_ll);
            type_tv = itemView.findViewById(R.id.type_tv);
            numpeo_tv = itemView.findViewById(R.id.numpeo_tv);
            main_ll = itemView.findViewById(R.id.main_ll);
            name_tv = itemView.findViewById(R.id.name_tv);
            sex_tv = itemView.findViewById(R.id.sex_tv);
            user_ll = itemView.findViewById(R.id.user_ll);
            room_ll = itemView.findViewById(R.id.room_ll);
            tuercode_tv = itemView.findViewById(R.id.tuercode_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


}

