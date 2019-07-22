package com.fengyuxing.tuyu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.umeng.socialize.Config;
import com.fengyuxing.tuyu.bean.MemberDTO;
import com.fengyuxing.tuyu.constant.SPConstant;

import java.lang.reflect.Field;


/**
 * Created by LGJ on 2018/06/09.
 */

public class CommonUtils {



    public CommonUtils() {
        throw new RuntimeException("别玩坏了");
    }



    /* *//*****
     * 更新用户信息
     *
     * @param mContext
     *//*
    public static void uppUserInfo(final Context mContext) {
        HttpManager.getInstance().getUserInfo(new HttpManager.HttpCallback<Result<LoginBean>>() {
            @Override
            public void onSuccess(int id, Result<LoginBean> result) {
                if (result.isOK()) {
                    LoginBean loginBean = result.getObject();
                    Gson gson2 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
                    String s = gson2.toJson(loginBean);
                    SPUtils.put(mContext, SPConstant.USER_INFO, s);

                }
            }

        });
    }

    *//**
     * 获取当前用户信息
     *
     * @return UserInfo.DataBean
     *//*
    public static LoginBean getUserInfo(Context context) {
        String user_info = (String) SPUtils.get(context, SPConstant.USER_INFO, "");
        LoginBean userInfo = new Gson().fromJson(user_info, LoginBean.class);
        return userInfo != null ? userInfo : null;
    }*/


    public static String getToken(Context context) {
        String token = (String) SPUtils.get(context, SPConstant.TOKEN, "");
        return token.equals("") ? null : token;
    }

    /**
     * 获取用户id
     *
     * @return userID
     */
    public static String getUserId(Context context) {
        return SPUtils.get(context, SPConstant.USER_ID, 0L) + "";
    }


    /**
     * 获取用户信息
     *
     * @return userID
     */
    public static MemberDTO getUserInfo(Context context) {
        String userinfo = (String) SPUtils.get(context, SPConstant.USER_INFO,"");
        if(StringUtils.isNotEmpty(userinfo)){
            return new Gson().fromJson(userinfo, MemberDTO.class);
        }else{
            return null;
        }
    }


//    public static Config getConfig(Context context) {
//        String userinfo = (String) SPUtils.get(context, SPConstant.CONFIG,"");
//        if(StringUtils.isNotEmpty(userinfo)){
//            return new Gson().fromJson(userinfo, Config.class);
//        }else{
//            return null;
//        }
//    }


    public static Config getConfig(Context context) {
        String userinfo = (String) SPUtils.get(context, SPConstant.CONFIG,"");
        if(StringUtils.isNotEmpty(userinfo)){
            return new Gson().fromJson(userinfo, Config.class);
        }else{
            return null;
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return -1;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }


}
