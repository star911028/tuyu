package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MikeArray;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/1/5.
 */

public class GiftPicRecyAdapter extends RecyclerView.Adapter<GiftPicRecyAdapter.ViewHolder> implements View.OnClickListener {
    //送礼房间人数
    private Context context;
    private List<MikeArray> datas;
    private OnItemClickListener onItemClickListener;
    private Boolean isCheck = false;
    private Boolean isCheckall = false;
    private int Checktag = 0;
    private Boolean isopen = false;
    private ArrayList<Integer> Checktass = new ArrayList<>();
    public GiftPicRecyAdapter(Context context, List<MikeArray> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.giftpic_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Glide.with(context)
                .load(datas.get(position).getMikerPortraitPath())
                .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                .into(holder.img_iv);


        holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
        if (position == 0) {
            if(MyApplication.getInstance().getGiftHosterid() !=null){//判断第一个是不是房主
                if(MyApplication.getInstance().getGiftHosterid().equals(datas.get(0).getMikerId())){
                    holder.type2_tv.setVisibility(View.VISIBLE);
                }else {
                    holder.type2_tv.setVisibility(View.GONE);
                    if(datas.get(position).getIsReceptionist().equals("true")){//主持位
                        holder.type_tv.setText("主持");
                    }else {
                        holder.type_tv.setText(datas.get(position).getMikeNumber());
                    }
                }
            }
        } else {
            Log.e("getIsReceptionist","getIsReceptionist="+datas.get(position).getIsReceptionist()+"  position="+position);
            if(datas.get(position).getIsReceptionist().equals("true")){//主持位
                holder.type_tv.setText("主持");
            }else {
                holder.type_tv.setText(datas.get(position).getMikeNumber());
            }
            holder.type2_tv.setVisibility(View.GONE);
        }

        holder.img_iv.setBorderColor(context.getResources().getColor(R.color.white));
        //单选
        if (isCheck) {//是否选中
            Checktass.add(Checktag);//添加选中
            if (Checktag == position) {//选中的索引
                holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                holder.type2_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.type2_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.img_iv.setBorderColor(context.getResources().getColor(R.color.mine_safe_bg));
            } else {
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                holder.type2_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                holder.type2_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                holder.img_iv.setBorderColor(context.getResources().getColor(R.color.white));
            }
        } else {
            holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
            holder.img_iv.setBorderColor(context.getResources().getColor(R.color.white));
        }

        if (isopen) { //是否执行多选 默认不执行
            //多选
            if (isCheckall) {
                datas.get(position).setChecked(true);//设置选中状态
                holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.type2_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                holder.type2_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.img_iv.setBorderColor(context.getResources().getColor(R.color.mine_safe_bg));
            } else {
                datas.get(position).setChecked(false);//设置选中状态
                holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                holder.type2_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                holder.type2_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                holder.img_iv.setBorderColor(context.getResources().getColor(R.color.white));
            }
        }else {
            Log.e("getChecked","positiongetChecked="+datas.get(position).getChecked());
            if(datas.get(position).getChecked()){
                if(position==0){
                    holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                    holder.type_tv.setTextColor(context.getResources().getColor(R.color.white));
                    holder.type2_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                    holder.type2_tv.setTextColor(context.getResources().getColor(R.color.white));
                    holder.img_iv.setBorderColor(context.getResources().getColor(R.color.mine_safe_bg));
                }else {
                    holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg2);
                    holder.type_tv.setTextColor(context.getResources().getColor(R.color.white));
                    holder.img_iv.setBorderColor(context.getResources().getColor(R.color.mine_safe_bg));
                }
            }else {
                if(position==0){
                    holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                    holder.type_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                    holder.type2_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                    holder.type2_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                    holder.img_iv.setBorderColor(context.getResources().getColor(R.color.white));
                }else {
                    holder.type_tv.setBackgroundResource(R.drawable.gift_pic_bg);
                    holder.type_tv.setTextColor(context.getResources().getColor(R.color.color_nosec));
                    holder.img_iv.setBorderColor(context.getResources().getColor(R.color.white));
                }
            }
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
        CircleImageView img_iv;
        LinearLayout main_ll;
        TextView type_tv, type2_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            img_iv = itemView.findViewById(R.id.img_iv);
            main_ll = itemView.findViewById(R.id.main_ll);
            type_tv = itemView.findViewById(R.id.type_tv);
            type2_tv = itemView.findViewById(R.id.type2_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void ChangeBG(Boolean isCheck, int Checktag) {//是否选中  选中的index
        this.isCheck = isCheck;
        this.Checktag = Checktag;
        this.isopen = false;
        notifyDataSetChanged();
    }



    public void ChangeBGall(Boolean isCheckall, Boolean isopen) {//是否全选
        this.isCheckall = isCheckall;
        this.isopen = isopen;
        notifyDataSetChanged();
    }
    //获得选中条目的结果
    public ArrayList<String> getSelectedItem() {
        ArrayList<String> selectList = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getChecked()) {
                selectList.add(datas.get(i).getMikerId());
            }
        }
        return selectList;
    }
}

