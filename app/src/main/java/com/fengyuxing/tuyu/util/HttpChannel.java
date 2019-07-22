package com.fengyuxing.tuyu.util;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dai on 2018/11/30.
 */

public class HttpChannel {

    private static HttpChannel httpChannel;
    private RetrofitService retrofitService;
//    public static  String baseUrl = "https://www.tuerapp.com";//正式服务器
//    public static String baseUrl = "https://www.fengyugo.com";//测试服务器
//    https://www.tuerapp.com/tuer/
//    https://ty.fengyugo.com/tuyu/
    public static String baseUrl = "https://ty.fengyugo.com";//兔语服务器


    public static HttpChannel getInstance() {
        return httpChannel == null ? httpChannel = new HttpChannel() : httpChannel;
    }

    private HttpChannel() {
        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create()) // json解析
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava
                .client(RetrofitUtils.getOkHttpClient()) // 打印请求参数
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

//    /**
//     * 发送消息
//     *
//     * @param observable Observable<? extends BaseBean>
//     * @param urlOrigin  请求地址
//     */
//    public void sendMessage(Observable<? extends MyBaseModel> observable, final String urlOrigin) {
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<MyBaseModel>() {
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(MyBaseModel baseBean) {
//                        Log.i("http返回：", baseBean.toString() + "");
//
//                    }
//                });
//    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }

}
