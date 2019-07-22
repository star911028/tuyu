package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;
import com.fengyuxing.tuyu.util.UILImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 * 首页 热门房间
 */

public class MainhotRecyAdapter extends RecyclerView.Adapter<MainhotRecyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;
    private UILImageLoader mUILImageLoader;
    private int[] bgAraay = {R.mipmap.gradient_1, R.mipmap.gradient_2, R.mipmap.gradient_3, R.mipmap.gradient_4, R.mipmap.gradient_5, R.mipmap.gradient_6, R.mipmap.gradient_7};

    public MainhotRecyAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
        mUILImageLoader = new UILImageLoader(context);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainhot_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.iv_img.setImageResource(datas.get(position));
        holder.itemView.setTag(position);
        //设置背景图片
        int length = bgAraay.length;
        int index = position;
        Log.e("onBindViewHolder", "length=" + length + " datas=" + datas.size() + " position=" + position);
        holder.main_ll.setBackgroundResource(bgAraay[index % length]);
        holder.title_tv.setText(datas.get(position).getRoomName());
        holder.numpeo_tv.setText(datas.get(position).getOnlineCount() + "人在玩");
        holder.type_tv.setText(datas.get(position).getClassifyName());
        mUILImageLoader.loadImage(datas.get(position).getPortraitPath(), holder.img_iv);
        if (position >= 3 && position == datas.size() - 1) {
//            setMargins(holder.main_ll,20, 0, 20, 0);
            int width = getScreenWidth(context);
            if (width == 1080) {
                setMargins(holder.main_ll,30, 0, 30, 0);
            } else {
                setMargins(holder.main_ll,20, 0, 20, 0);
            }
            Log.e("getScreenWidth", "width=" + width);
        }

    }
    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
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
        ImageView img_iv;
        LinearLayout main_ll;
        TextView numpeo_tv, type_tv, title_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            img_iv = itemView.findViewById(R.id.img_iv);
            numpeo_tv = itemView.findViewById(R.id.numpeo_tv);
            type_tv = itemView.findViewById(R.id.type_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
            main_ll = itemView.findViewById(R.id.main_ll);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


}

