package com.fengyuxing.tuyu.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.util.HttpChannel;
import com.fengyuxing.tuyu.util.L;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "WebViewActivity";
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_ll)
    LinearLayout titleLl;


    private WebSettings settings;
    private String title = "";
    private String url = "";
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public ValueCallback<Uri> mUploadMessage;
    public final static int FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 2;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;// 表单的结果回调
    private Uri imageUri;
    private String Type = "";

    @Override
    protected int getLayoutId() {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        return R.layout.activity_webview;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        if (getIntent().getStringExtra("type") != null) {
            Type = getIntent().getStringExtra("type");
        }
        StatusBarCompat.setStatusBarColor(WebViewActivity.this, getResources().getColor(R.color.white), true);//isLightColor   透明或者不透明
        url = getIntent().getStringExtra("webview_url");
        title = getIntent().getStringExtra("webview_title");
        titleTv.setText(title);
        settings = webview.getSettings();
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
//        titleCenter.setText(StringUtils.isNotEmpty(title) ? title : "兔语");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initData() {
        webViewSetting();
        webview.loadUrl(url);
    }

    private void webViewSetting() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);                       //可执行js
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);   //设置页面默认缩放密度
        webSettings.setDefaultTextEncodingName("UTF-8");              //设置默认的文本编码名称，以便在解码html页面时使用
        webSettings.setAllowContentAccess(true);                      //启动或禁用WebView内的内容URL访问
        webSettings.setAppCacheEnabled(false);                        //设置是否应该启用应用程序缓存api
        webSettings.setBuiltInZoomControls(false);                    //设置WebView是否应该使用其内置的缩放机制
        webSettings.setUseWideViewPort(true);                         //设置WebView是否应该支持viewport
        webSettings.setLoadWithOverviewMode(true);                    //不管WebView是否在概述模式中载入页面，将内容放大适合屏幕宽度
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);          //重写缓存的使用方式
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);   //告知js自动打开窗口
        webSettings.setLoadsImagesAutomatically(true);                //设置WebView是否应该载入图像资源
        webSettings.setAllowFileAccess(true);                         //启用或禁用WebView内的文件访问
        webSettings.setDomStorageEnabled(true);                       //设置是否启用了DOM存储API,默认为false

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("alipays://platformapi/startApp?") && isAliPayInstalled(mContext)) {
                    if (isAliPayInstalled(mContext)) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mContext.startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(mContext, "未检测到支付宝客户端，请安装后重试。", Toast.LENGTH_SHORT);
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


        //H5界面加载进度监听
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
            }

            // For Android < 5.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

            }

            // For Android => 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
                                             FileChooserParams fileChooserParams) {

                return false;
            }

        });


    }

    public boolean isAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        ComponentName componentName = intent.resolveActivity(context.getPackageManager());

        return componentName != null;

    }

    //点击回退按钮不是退出应用程序，而是返回上一个页面
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
//            webview.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void initEventListeners() {
        initData();
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
                    // 返回键退回
                    webview.goBack();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        if (webview.canGoBack()) {
            // 返回键退回
            webview.goBack();
        } else {
            if (Type.equals("room")) {
                finish();
                Intent intentyh = new Intent(mContext, MainRoomActivity.class);
                intentyh.putExtra("roomid", "");//传递房间id
                intentyh.putExtra("roomdata", (Serializable) null);//传递房间数据
                startActivity(intentyh);
            } else {
                finish();
            }
        }
    }

    class Web {
        @JavascriptInterface
        public void callJS() {
            L.e("点了联系客服");
        }

        @JavascriptInterface
        public void setTitles(String title) {
            if (!TextUtils.isEmpty(title)) {
                // finish_header_tv_title.setText(title);
            }
        }
    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
