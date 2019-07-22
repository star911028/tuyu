package com.fengyuxing.tuyu.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zhy.http.okhttp.request.RequestCall;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.util.CommonUtils;
import com.fengyuxing.tuyu.util.L;
import com.fengyuxing.tuyu.util.SPUtils;
import com.fengyuxing.tuyu.util.StringUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class HttpManager extends BaseManager<Context> {

    private static final String TAG = HttpManager.class.getSimpleName();

    private static HttpManager sInstance;

    private Handler mHandler;
    private OkHttpClient mOkHttpClient;

    /**
     * 需要读取网络状态
     */
    private NetworkManager mNetworkManager;

    public static Context mContext;

    public static void initialize(Context context) {
        L.d(TAG, "HttpManager initializing...");
        if (sInstance == null) {
            synchronized (HttpManager.class) {
                if (sInstance == null) {
                    sInstance = new HttpManager(context);
                }
            }
        }
        L.d(TAG, "HttpManager initialized.");
    }

    public static HttpManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        } else {
            throw new IllegalStateException("HttpManager was not initialized.");
        }
    }

    private HttpManager(Context context) {
        if (NetworkManager.isInitialized()) {
            this.mContext = context;
            mHandler = new Handler(context.getMainLooper());
            mOkHttpClient = new OkHttpClient();
            mNetworkManager = NetworkManager.getInstance();

        } else {
            throw new IllegalStateException(
                    "NetworkManager must be initialized first.");
        }
    }


    /**
     * 同步请求
     *
     * @param
     * @param parser
     * @throws IOException
     */
    private <T> T request(IHttpRequest httpRequest, final IParser<T> parser) {
        // URL
        final String url = httpRequest.getPath();
        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        L.d(TAG, url);

        // 设置HTTP头
        final Map<String, String> headers = httpRequest.getHeaders();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }

        // 设置HTTP参数
        final Map<String, String> parameters = httpRequest.getParameters();
        final FormBody.Builder parameterBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            parameterBuilder.add(parameter.getKey(), parameter.getValue());
        }
        requestBuilder.post(parameterBuilder.build());

        String data = null;
        try {
            Response response = mOkHttpClient.newCall(requestBuilder.build())
                    .execute();
            data = response.body().string();
            L.d(TAG, data);
            return parser.parse(data, mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步请求
     *
     * @param
     * @param parser
     * @param callback
     */
    private <T> void request(final IHttpRequest httpRequest,
                             final IParser<T> parser, final ICallback<T> callback) {
        final Request.Builder requestBuilder = new Request.Builder();
        final Map<String, String> headers = httpRequest.getHeaders();
        // 设置HTTP参数
        final Map<String, String> parameters = httpRequest.getParameters();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        if (httpRequest.getMethed() == HttpRequest.METHOD_GET) {
            requestBuilder.url(httpRequest.getPath() + "&token=" + CommonUtils.getToken(mContext));
            requestBuilder.get();
        } else {
            requestBuilder.url(httpRequest.getPath());
            final FormBody.Builder parameterBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                parameterBuilder.add(parameter.getKey(), parameter.getValue() == null ? "" : parameter.getValue());
            }
            if (!StringUtils.isEmpty(CommonUtils.getToken(mContext))) {
                parameterBuilder.add("token", CommonUtils.getToken(mContext));
            }
            requestBuilder.post(parameterBuilder.build());
        }
        OkHttpRequest okHttpRequest = new OkHttpRequest(httpRequest.getPath(), null, parameters, headers, httpRequest.getId()) {
            @Override
            protected RequestBody buildRequestBody() {
                return requestBuilder.build().body();
            }

            @Override
            protected Request buildRequest(RequestBody requestBody) {
                return requestBuilder.build();
            }
        };
        RequestCall requestCall = new RequestCall(okHttpRequest);
        requestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (callback != null) {
                    callback.onError(httpRequest.getId(), HttpCallback.ERROR_UNKNOWN);
                }
                return;
            }

            @Override
            public void onResponse(String response, int id) {
                if (callback != null) {
                    Log.e("onResponsecallback","response="+response+"  parser.parse(response, mContext)="+parser.parse(response, mContext));
                    callback.onSuccess(httpRequest.getId(), parser.parse(response, mContext));

                }
            }
        });


    }


    public static abstract class HttpCallback<T> implements ICallback<T> {

        /**
         * 未知错误类型
         */
        public static final int ERROR_UNKNOWN = -1;
        /**
         * 无网络连接
         */
        public static final int ERROR_UNAVAILABLE = 0;

        @Override
        public void onStart(int id) {
        }

        @Override
        public void onError(int id, int errorCode) {
            SPUtils.showToast(mContext, mContext.getResources().getString(R.string.network_request_failure) + id);
        }

        @Override
        public abstract void onSuccess(int id, T result);
    }

//    接口说明：banner列表接口
//    链接：
//    http://test.shangche.im/wxapi/common/bannerList?pushAddr=1
//    参数说明：pushAddr 上架位置 0 首页 1 社区

//    public void BannerList(String pushAddr, ICallback<Result<BannerExpand[]>> callback) {
//        HttpRequest request = new HttpRequest(1, HttpRequest.METHOD_POST, NetConstant.API_GetBannerList);
//        request.add("pushAddr", pushAddr);
//        request(request, new ResultParser<BannerExpand[]>() {
//            @Override
//            public Result<BannerExpand[]> parse(String json, Context context) {
//                Log.e("ResultParserjson","json="+json);
//                return super.parse(json, context);
//            }
//
//            @Override
//            public BannerExpand[] parseObject(String json) {
//                Gson gson = new Gson();
//                if (TextUtils.isEmpty(json)) {
//                    return null;
//                }
//                Log.e("parseObject","json="+json);
//                return gson.fromJson(json, BannerExpand[].class);
//            }
//        }, callback);
//    }


    /*****
     * 我的粉丝
     * @param page 第几页
     * @param callback
     */
//    public void getMyFans(String userid, int page, ICallback<Result<MemberFriendDTO[]>> callback) {
//        HttpRequest request = new HttpRequest(56, HttpRequest.METHOD_POST, NetConstant.API_CIRCLE_FANS_LIST);
//        request.add("userId", userid);
//        request.add("page", page + "");
//        request.add("pageSize", "10");
//        request(request, new ResultParser<MemberFriendDTO[]>() {
//            @Override
//            public Result<MemberFriendDTO[]> parse(String json, Context context) {
//                return super.parse(json, context);
//            }
//
//            @Override
//            public MemberFriendDTO[] parseObject(String json) {
//                Gson gson = new Gson();
//                if (TextUtils.isEmpty(json)) {
//                    return null;
//                }
//                return gson.fromJson(json, MemberFriendDTO[].class);
//            }
//        }, callback);
//    }



    //登录
//    public void login(String username, String password, ICallback<Result<LoginDTO>> callback) {
//        HttpRequest request = new HttpRequest(1, HttpRequest.METHOD_POST, NetConstant.API_USER_LOGIN);
//        request.add("username", username);
//        request.add("password", password);
//        request(request, new ResultParser<LoginDTO>() {
//            @Override
//            public Result<LoginDTO> parse(String json, Context context) {
//                return super.parse(json, context);
//            }
//
//            @Override
//            public LoginDTO parseObject(String json) {
//                Gson gson = new Gson();
//                if (TextUtils.isEmpty(json)) {
//                    return null;
//                }
//                SPUtils.put(mContext, SPConstant.USER_INFO, json);
//                return gson.fromJson(json, LoginDTO.class);
//            }
//        }, callback);
//    }

//
//    public void comment2(Long infoId, String content, Long parentId, Integer relType, String replyMemberId, ICallback<Result<String>> callback) {
//        HttpRequest request = new HttpRequest(5, HttpRequest.METHOD_POST, NetConstant.API_COMMEN_COMMENT);
//        request.add("userId", CommonUtils.getUserId(mContext));
//        request.add("infoId", infoId + "");
//        request.add("content", content);
//        request.add("parentId", parentId + "");
//        request.add("relType", relType + "");
//        request.add("replyMemberId", replyMemberId);
//
//        request(request, new StringResultParser(), callback);
//    }
//
//
//    /*****
//     * 获取社区动态1
//     * @param type 1：关注 2：发现
//     *             communtiType 不传 全部
//     * @param page 第几页
//     * @param callback
//     */
//    public void getInvestmentDynamic(int type, int page, ICallback<Result<InvestmentDTO[]>> callback) {
//        HttpRequest request = new HttpRequest(11, HttpRequest.METHOD_POST, NetConstant.API_INVESTMENT_QUERY);
//        request.add("type", type + "");
//        request.add("userId", CommonUtils.getUserId(mContext));
//        request.add("page", page + "");
//        request.add("pageSize", "10");
//        request(request, new ResultParser<InvestmentDTO[]>() {
//            @Override
//            public Result<InvestmentDTO[]> parse(String json, Context context) {
//                return super.parse(json, context);
//            }
//
//            @Override
//            public InvestmentDTO[] parseObject(String json) {
//                Gson gson = new Gson();
//                if (TextUtils.isEmpty(json)) {
//                    return null;
//                }
//                return gson.fromJson(json, InvestmentDTO[].class);
//            }
//        }, callback);
//    }


}

