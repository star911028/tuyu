package com.fengyuxing.tuyu.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.util.HttpChannel;
import com.fengyuxing.tuyu.util.TabCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


//砸蛋窗口

public class RoomEggsWindow extends PopupWindow {
    private ImageView close_iv;
    private View conentView;
    private Context context;
    private TextView edit_tv, rule_tv;
    private WebView main_wb;

    public void setDrawableAndTextColor(int id, TextView textview) {
        Drawable drawable = context.getResources().getDrawable(id);
        textview.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        textview.setTextColor(context.getResources().getColor(R.color.color_text_02));
    }

    public void setDrawableAndTextColor(int id, TextView textview, int color) {
        Drawable drawable = context.getResources().getDrawable(id);
        textview.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        textview.setTextColor(color);
    }

    public RoomEggsWindow(final Activity context, OnClickListener l,String Roomid) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.eggs_window, null);
        main_wb = (WebView) conentView.findViewById(R.id.main_wb);

        //支持javascript
        main_wb.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        main_wb.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        main_wb.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        main_wb.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        main_wb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        main_wb.getSettings().setLoadWithOverviewMode(true);

        main_wb.addJavascriptInterface(new JSInterface(context), "android");


//        main_wb.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
//                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
//                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
////                if (url.toString().contains("sina.cn")){
////                    view.loadUrl("http://ask.csdn.net/questions/178242");
////                    return true;
////                }
//                Log.e("shouldOverride","url="+url);
//
//                return false;
//            }
//        });
        //如果不设置WebViewClient，请求会跳转系统浏览器
        main_wb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                    return true;
                } else if (url.startsWith("alipays://platformapi/startApp?") && isAliPayInstalled(context)) {
                    if (isAliPayInstalled(context)) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(  context, "未检测到支付宝客户端，请安装后重试。", Toast.LENGTH_SHORT);
                    }
                } else {
                    //H5微信支付要用，不然说"商家参数格式有误"  注意 一定要加http
                    Map<String, String> extraHeaders = new HashMap<String, String>();
                    extraHeaders.put("Referer", HttpChannel.baseUrl);
                    view.loadUrl(url, extraHeaders);
                }
                return true;
            }
        });

//        main_wb.addJavascriptInterface(new WebAppInterface(context), "android");



        Log.e("loadUrl","token="+"http://ty.fengyugo.com/h5/h5/eggs.html" + "?uid=" + MyApplication.getInstance().getUserId() + "&token=" + MyApplication.getInstance().getToken()+"&roomId="+Roomid);
        main_wb.loadUrl("http://ty.fengyugo.com/h5/h5/eggs.html" + "?uid=" + MyApplication.getInstance().getUserId() + "&token=" + MyApplication.getInstance().getToken()+"&roomId="+Roomid);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x01000000);
        this.setBackgroundDrawable(dw);

        conentView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = conentView.findViewById(R.id.ipopwindowlayout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
//                        dismiss();
                    }
                }
                return true;
            }
        });

    }


    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    private final class JSInterface{
        Context mContext;
        JSInterface(Context c) {
            mContext = c;
        }
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void closewb(){
            Log.e("JSInterface","register");
//            dismiss();
            EventBus.getDefault().post(new TabCheckEvent("关闭弹窗"));

        }
    }


    public boolean isAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        ComponentName componentName = intent.resolveActivity(context.getPackageManager());

        return componentName != null;

    }

}
