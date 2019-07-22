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

import java.text.DecimalFormat;
import java.util.List;


public class MainRankItemAdapter extends RecyclerView.Adapter<MainRankItemAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;

    public MainRankItemAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainrank_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onHeadClick(int position);
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
        holder.title_tv.setText(datas.get(position).getUsername());//昵称
//        holder.value_tv.setText(datas.get(position).getDiamond());//钻石
        float num1 = Float.parseFloat(datas.get(position).getDiamond());
        if (num1 >= 10000) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String strPrice = decimalFormat.format(num1 / 10000);//返回字符串
            holder.value_tv.setText(strPrice + "W");//钻石 //保留两位小数
        } else {
            holder.value_tv.setText(datas.get(position).getDiamond());//钻石
        }
        int index = position + 4;
        holder.num_tv.setText(index + "");
        holder.sex_tv.setText(datas.get(position).getAge());
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
                .load(RetrofitService.bgAraay[levelnum])
                .apply(new RequestOptions().placeholder(RetrofitService.bgAraay[0]).error(RetrofitService.bgAraay[0]).dontAnimate())
                .into(holder.level_iv);
        holder.itemView.setTag(position);
        holder.img_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//进入个人主页
                onItemClickListener.onHeadClick(position);
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
        ImageView img_iv, level_iv;
        LinearLayout main_ll, sex_ll;
        TextView sex_tv, title_tv, value_tv, num_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            num_tv = itemView.findViewById(R.id.num_tv);
            img_iv = itemView.findViewById(R.id.img_iv);
            sex_ll = itemView.findViewById(R.id.sex_ll);
            main_ll = itemView.findViewById(R.id.main_ll);
            sex_tv = itemView.findViewById(R.id.sex_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
            value_tv = itemView.findViewById(R.id.value_tv);
            level_iv = itemView.findViewById(R.id.level_iv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}

