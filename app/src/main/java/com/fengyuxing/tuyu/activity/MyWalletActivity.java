package com.fengyuxing.tuyu.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengyuxing.tuyu.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    //我的钱包
    private static final String TAG = "MyWalletActivity";
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.earnings_num_tv)
    TextView earningsNumTv;
    @BindView(R.id.earnings_list_tv)
    TextView earningsListTv;
    @BindView(R.id.main_lv)
    LinearLayout mainLv;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mywallet;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle savedInstanceState) {

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
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
    }


    @Override
    protected void initEventListeners() {
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @OnClick({R.id.back_iv, R.id.earnings_list_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.earnings_list_tv://充值记录
//                Intent intent = new Intent(mContext, SetsafeActivity.class);
//                intent.putExtra("userdata", (Serializable) userdata);
//                startActivity(intent);
                break;
        }
    }
}
