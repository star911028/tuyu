package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.MikeArray;

import java.util.ArrayList;


/**
 * author：Administrator on 2016/12/26 15:03
 * description:文件说明
 * version:版本
 */
///定影GridView的Adapter
public class GiftGridViewAdapter extends BaseAdapter {
    private int page;
    private int count;
    private ArrayList<MikeArray> gifts;
    private Context context;
    private Boolean isCheck = false;
    private int Checktag = -2;
    private int    needdelnums=-1;
    private GiftMainRecyAdapter.OnItemClickListener onItemClickListener;
    private String CheckgiftId="";
    public void setGifts(ArrayList<MikeArray> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }

    public GiftGridViewAdapter(Context context, int page, int count) {
        this.page = page;
        this.count = count;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return count;
    }

    @Override
    public MikeArray getItem(int position) {
        // TODO Auto-generated method stub
        return gifts.get(page * count + position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(gifts.size()>page * 8 + position){
            ViewHolder viewHolder = null;
            Log.e("背包礼物数量","position="+position);
            final MikeArray catogary = gifts.get(page * 8 + position);
            //todo  java.lang.IndexOutOfBoundsException: Index: 2, Size: 2

//        if(page==2){
//            Log.e("getViewitempage2", "count="+count+"  position="+position);
//           catogary = gifts.get(page * 8 + position);
//        }else {
//           catogary = gifts.get(page * count + position);
//        }
            Log.e("getViewitem", "getViewitem=" + (page * count + position));
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_gift, null);
                viewHolder.grid_fragment_home_item_img =
                        (ImageView) convertView.findViewById(R.id.grid_fragment_home_item_img);
                viewHolder.grid_fragment_home_item_txt =
                        (TextView) convertView.findViewById(R.id.grid_fragment_home_item_txt);
                viewHolder.price_tv =
                        (TextView) convertView.findViewById(R.id.price_tv);
                viewHolder.count_tv =
                        (TextView) convertView.findViewById(R.id.count_tv);
                viewHolder.sec_bg_ll =
                        (FrameLayout) convertView.findViewById(R.id.sec_bg_ll);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context)//context.getApplicationContext()
                    .load(catogary.getImgPath())
                    .apply(new RequestOptions().placeholder(R.drawable.rabblt_icon).error(R.drawable.rabblt_icon).dontAnimate())
                    .into(viewHolder.grid_fragment_home_item_img);
            viewHolder.grid_fragment_home_item_txt.setText(catogary.getGiftName());
            viewHolder.price_tv.setText(catogary.getPrice());
            if(catogary.getCount()!=null){
                viewHolder.count_tv.setVisibility(View.VISIBLE);
                viewHolder.count_tv.setText("x"+catogary.getCount());
            }else {
                viewHolder.count_tv.setVisibility(View.GONE);
            }
            if(CheckgiftId.length()>0){//如果赠送了背包礼物
                if(catogary.getGiftId().equals(CheckgiftId)){
                    int nums = Integer.parseInt(catogary.getCount())-needdelnums;
                    viewHolder.count_tv.setText("x"+nums);
                    Log.e("CheckgiftId","nums="+nums);
                    catogary.setCount(nums+"");//重新设置礼物数量
                    CheckgiftId="";
                    if(nums==0){
                        gifts.remove(position);
                    }
                }
            }
            //单选
            Log.e("ChangeBG", "isCheck=" + isCheck + "  Checktag=" + Checktag + "  position=" + position);
            if (isCheck) {
                if (Checktag - page * 8 == position) {
                    viewHolder.sec_bg_ll.setBackgroundResource(R.drawable.gift_bg);
                } else {
                    viewHolder.sec_bg_ll.setBackgroundResource(R.drawable.ear_bg);
                }
            } else {
                viewHolder.sec_bg_ll.setBackgroundResource(R.drawable.ear_bg);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(page * 8 + position);
                }
            });

        }
        return convertView;
    }


    public class ViewHolder {
        public ImageView grid_fragment_home_item_img;
        public TextView grid_fragment_home_item_txt, price_tv,count_tv;
        public FrameLayout sec_bg_ll;
    }

//    public OnGridViewClickListener onGridViewClickListener;
//
//    public void setOnGridViewClickListener(OnGridViewClickListener onGridViewClickListener) {
//        this.onGridViewClickListener = onGridViewClickListener;
//    }

//    public interface OnGridViewClickListener {
//        void click(MikeArray gift);
//    }
//
    public void setOnItemClickListener(GiftMainRecyAdapter.OnItemClickListener listener) {
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


    public void ChangeCount( String CheckgiftId,int needdelnums) {
        this.CheckgiftId = CheckgiftId;
        this.needdelnums = needdelnums;
        notifyDataSetChanged();
    }

}
