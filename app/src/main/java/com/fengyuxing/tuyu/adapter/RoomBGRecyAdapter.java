package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class RoomBGRecyAdapter extends RecyclerView.Adapter<RoomBGRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;
    //被选中的item的位置，设置初始值为-1是为了解决默认第一个被选中的问题
    private int layoutPosition = 0;//默认选中第一个
    private int checkpostion=0;

    public RoomBGRecyAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.roombg_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (datas.get(position).getImgPath()!= null) {
            Glide.with(context)
                    .load(datas.get(position).getImgPath())
                    .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                    .into(holder.bg_iv);
            Log.e("RoomBGRecyAdapter","getImgPath="+datas.get(position).getImgPath());
            //更新单选之后的效果
            if (position == layoutPosition) {
                //获取要改变的背景图 保存下来
                holder.sec_iv.setVisibility(View.VISIBLE);
            } else {
                holder.sec_iv.setVisibility(View.GONE);
            }
            if (datas.get(position).getImgPath().equals(MyApplication.getInstance().getRoombg())) {
                //获取要改变的背景图 保存下来
                holder.sec_iv.setVisibility(View.VISIBLE);
            } else {
                holder.sec_iv.setVisibility(View.GONE);
            }
        }
        holder.itemView.setTag(position);
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
                    layoutPosition = position;
//                    notifyDataSetChanged();
                    break;
            }

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bg_iv, sec_iv;
        LinearLayout main_ll;

        public ViewHolder(View itemView) {
            super(itemView);
            bg_iv = itemView.findViewById(R.id.bg_iv);
            sec_iv = itemView.findViewById(R.id.sec_iv);
            main_ll = itemView.findViewById(R.id.main_ll);

        }
    }
    public void setSelectedPosition(int selectedPosition) {
        this.layoutPosition = selectedPosition;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}

