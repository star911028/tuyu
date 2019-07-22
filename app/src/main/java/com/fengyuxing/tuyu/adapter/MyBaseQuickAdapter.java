package com.fengyuxing.tuyu.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengyuxing.tuyu.util.HttpChannel;
import com.fengyuxing.tuyu.view.ConstantsString;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBaseQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T,K> implements View.OnClickListener {

    public MyBaseQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(K helper, T item) {

    }

    public void postRequest(String url, WeakHashMap weakHashMap) {
        weakHashMap.put("key", ConstantsString.key);
        weakHashMap.put("source",ConstantsString.source);
        Call<String> call = HttpChannel.getInstance().getRetrofitService().post(url, weakHashMap);
        call.enqueue(callback);

    }

    public void postFile(String url, WeakHashMap<String, RequestBody> map, File file, String type){
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(type, file.getName(), requestFile);
        map.put("key",toRequestBody(ConstantsString.key));
        map.put("source",toRequestBody(ConstantsString.source));
        Call<String> call = HttpChannel.getInstance().getRetrofitService().postFile(url,map, body);
        call.enqueue(callback);
    }

    protected RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

    Callback<String> callback = new Callback<String>() {

        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.body() == null) {
                Log.i(TAG, "onResponse: body=null");
                return;
            }
            Log.i(TAG, "http返回：" + call.request().url() + " 结果： " + response.body() + "");
            onCalllBack(call, response, response.body(), call.request().url().toString());
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.i(TAG, "onFailure: url= " + call.request().url() + " exception: " + t.toString());
        }
    };

    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url) {

    }

    @Override
    public void onClick(View view) {

    }
}
