package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;

import java.util.List;

/**
 * 我的支出记录
 */

public class MyCashoutAdapter extends RecyclerView.Adapter<MyCashoutAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DataList> datas;
    private OnItemClickListener onItemClickListener;

    public MyCashoutAdapter(Context context, List<DataList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mydrawlog_item, parent, false);
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
        if (datas.get(position).getStatus() != null) {// "status": 0,   //  0待审核  1已通过  2未通过
            if (datas.get(position).getStatus().equals("0")) {
                holder.type_tv.setText(datas.get(position).getContent() + "(审核中)");
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (datas.get(position).getStatus().equals("1")) {
                holder.type_tv.setText(datas.get(position).getContent());
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.main_blue_col));
            } else if (datas.get(position).getStatus().equals("2")) {
                holder.type_tv.setText(datas.get(position).getContent() + "(未通过)");
                holder.type_tv.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
        holder.nums_tv.setText(datas.get(position).getCutCoin());
        holder.time_tv.setText(datas.get(position).getCreateTime());
        holder.code_tv.setText(datas.get(position).getOut_trade_no());

        holder.itemView.setTag(position);
        holder.main_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(position);
                return false;
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
        LinearLayout main_ll;
        TextView nums_tv, time_tv, code_tv, type_tv;


        public ViewHolder(View itemView) {
            super(itemView);
            nums_tv = itemView.findViewById(R.id.nums_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            main_ll = itemView.findViewById(R.id.main_ll);
            code_tv = itemView.findViewById(R.id.code_tv);
            type_tv = itemView.findViewById(R.id.type_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


}

