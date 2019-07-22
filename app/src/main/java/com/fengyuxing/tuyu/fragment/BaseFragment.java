package com.fengyuxing.tuyu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
import static com.fengyuxing.tuyu.view.ConstantsString.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    InputMethodManager inputMethodManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
    }

    protected void onCalllBack(Call<String> call, Response<String> response, String result, String url){

    }

    public void postRequest(String url, WeakHashMap weakHashMap){
        weakHashMap.put("key",ConstantsString.key);
        weakHashMap.put("source",ConstantsString.source);
        Call<String> call = HttpChannel.getInstance().getRetrofitService().post(url, weakHashMap);
        call.enqueue(callback);

    }

    protected void showInput(View view){
        if (view != null) {
            view.requestFocus();
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
        }
    }

    public void postFile(String url, WeakHashMap<String, RequestBody> map, File file,String type){
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
            if (response.body()==null){
                Log.i(TAG, "onResponse: body=null");
                return;
            }
            Log.i(TAG, "http返回："+call.request().url()+" 结果： "+response.body()+ "");
            if (getActivity()!=null && !getActivity().isDestroyed()){
                onCalllBack(call,response,response.body(),call.request().url().toString());
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.i(TAG, "onFailure: url= "+call.request().url()+" exception: "+ t.toString());
        }
    };

    @Override
    public void onClick(View v) {

    }


    public String formatTime(int time) {
        String s = "";
        if (time < 10) {
            s = "00:0" + time;
        } else if (time < 60 && time >= 10) {
            s = "00:" + time;
        } else {
            int minute = time / 60;
            int second = time % 60;
            s = "0" + minute + ":" + second;
        }
        return s;
    }

//    /**
//     * 申请权限
//     */
//    private PermissionListener mListener;
//    public void getPermission(
//            String[] permissions, PermissionListener listener) {
//        mListener = listener;
//        List<String> permissionList = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= 23) {
//            // 遍历每一个申请的权限，把没有通过的权限放在集合中
//            for (String permission : permissions) {
//                if (ContextCompat.checkSelfPermission(getContext(), permission) !=
//                        PackageManager.PERMISSION_GRANTED) {
//                    permissionList.add(permission);
//                }
//            }
//            // 申请权限
//            if (!permissionList.isEmpty()) {
//                BaseFragment.this.requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
//            } else {
//                mListener.granted();
//            }
//        } else {
//            mListener.granted();
//        }
//
//    }
//
//    /**
//     * 申请后的处理
//     */
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0) {
//            List<String> deniedList = new ArrayList<>();
//            // 遍历所有申请的权限，把被拒绝的权限放入集合
//            for (int i = 0; i < grantResults.length; i++) {
//                int grantResult = grantResults[i];
//                if (grantResult == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    deniedList.add(permissions[i]);
//                }
//            }
//            if (!deniedList.isEmpty()) {
//                ToastUtils.show("您没有授予相关权限,请在设置中授予相关权限");
//                mListener.denied(deniedList);
//            } else {
//                mListener.granted();
//            }
//        }
//    }
}
