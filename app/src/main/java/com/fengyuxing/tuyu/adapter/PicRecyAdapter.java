package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fengyuxing.tuyu.R;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PicRecyAdapter extends RecyclerView.Adapter<PicRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Integer> datas;
    private OnItemClickListener onItemClickListener;
    public PicRecyAdapter(Context context, List<Integer> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picset_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv_img.setImageResource(datas.get(position));
        holder.itemView.setTag(position);
        holder.main_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {
        if (mItemClickListener != null) {
            int position = (Integer) view.getTag();
            switch (view.getId()) {
                case R.id.main_ll:
                    mItemClickListener.onItemClick(position);
                    break;
            }
        }
    }
//    public void SetImg(Bitmap bitmap){
//        iv_img.setImageBitmap(bitmap);
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        LinearLayout main_ll;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            main_ll= itemView.findViewById(R.id.main_ll);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
}

