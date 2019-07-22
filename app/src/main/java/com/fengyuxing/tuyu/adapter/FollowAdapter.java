package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;

public class FollowAdapter extends MyBaseQuickAdapter<DataList, BaseViewHolder> {

    int orange, gray;
    int clickPosition;

    public FollowAdapter(int layoutResId, Context context) {
        super(layoutResId);
        orange = ContextCompat.getColor(context, R.color.color_ff7458);
        gray = ContextCompat.getColor(context, R.color.color_b6c0d6);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataList item) {
        int posi = helper.getLayoutPosition() - getHeaderLayoutCount();

        Glide.with(mContext).load(item.getPortraitPath()).into((ImageView) helper.getView(R.id.iv_pic));
        helper.setText(R.id.tv_title, item.getUsername());
        helper.setText(R.id.age_tv, item.getUsername());
        helper.setText(R.id.level_tv, item.getUsername());
        String sex = item.getGender();
        if ("女".equals(sex)) {
        } else {
        }
//        String bottom = "动态:" + item.getDynamicCount() + "  作品:" + item.getWritingCount() + "  粉丝:" + item.getFansCount();
//        helper.setText(R.id.tv_bottom, bottom);
//        if (item.getFollowStatus() == 0) {
////            helper.setBackgroundRes(R.id.tv_tofollow,R.drawable.shape_ff6633_14);
//            helper.setTextColor(R.id.tv_tofollow, ContextCompat.getColor(mContext, R.color.white));
//            helper.setText(R.id.tv_tofollow, "关注");
//        } else if (item.getFollowStatus() == 1) {
////            helper.setBackgroundRes(R.id.tv_tofollow,R.drawable.shape_f5f8fc_13);
//            helper.setText(R.id.tv_tofollow, "已关注");
//            helper.setTextColor(R.id.tv_tofollow, ContextCompat.getColor(mContext, R.color.color_b4bdcc));
//        } else if (item.getFollowStatus() == 2) {
////            helper.setBackgroundRes(R.id.tv_tofollow,R.drawable.shape_f5f8fc_13);
//            helper.setText(R.id.tv_tofollow, "互相关注");
//            helper.setTextColor(R.id.tv_tofollow, ContextCompat.getColor(mContext, R.color.color_b4bdcc));
//        }
//        helper.getView(R.id.tv_tofollow).setTag(posi);
//        helper.getView(R.id.tv_tofollow).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
//            case R.id.tv_tofollow:
//                clickPosition = (int) view.getTag();
//                if (getData().get(clickPosition).getFollowStatus() == 0) {//关注和取消关注
////                    WeakHashMap map = new WeakHashMap();
////                    map.put("authorId",getData().get(clickPosition).getUserId());
////                    map.put("fansId",ConstantsString.USERID);
////                    postRequest(RetrofitService.FOLLOWUSER,map);
//                } else {
////                    WeakHashMap map = new WeakHashMap();
////                    map.put("authorId",getData().get(clickPosition).getUserId());
////                    map.put("fansId",ConstantsString.USERID);
////                    postRequest(RetrofitService.CANCELFOLLOW,map);
//                }
//                break;
        }
    }

//    @Override
//    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {
//        super.onCalllBack(call, response, result, url);
//        if (url.contains(RetrofitService.FOLLOWUSER)){
//            try {
//                JSONObject jo = new JSONObject(result);
//                if (jo.has("error")){
//                    String error = jo.getString("error");
//                    ToastUtils.show(error);
//                }else{
//                    int followStatus = jo.getInt("followStatus");
//                    getData().get(clickPosition).setFollowStatus(followStatus);
//                    notifyDataSetChanged();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else if (url.contains(RetrofitService.CANCELFOLLOW)){
//            try {
//                JSONObject jo = new JSONObject(result);
//                if (jo.has("error")){
//                    String error = jo.getString("error");
//                    ToastUtils.show(error);
//                }else{
//                    getData().get(clickPosition).setFollowStatus(0);
//                    notifyDataSetChanged();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
