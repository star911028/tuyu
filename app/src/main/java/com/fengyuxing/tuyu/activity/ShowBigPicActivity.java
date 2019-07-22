package com.fengyuxing.tuyu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.githang.statusbar.StatusBarCompat;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.showpic.MyImageAdapter;
import com.fengyuxing.tuyu.showpic.PhotoViewPager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ShowBigPicActivity extends BaseActivity implements View.OnClickListener {
    //查看大图
    private static final String TAG = "ShowBigPicActivity";
    @BindView(R.id.main_lv)
    RelativeLayout mainLv;
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private TextView mTvImageCount, tv_save;
    private TextView mTvSaveImage;
    private List<String> Urls;
    private MyImageAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_showbigpic;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(ShowBigPicActivity.this, getResources().getColor(R.color.black), false);//isLightColor   透明或者不透明
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pagerrank);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        tv_save = (TextView) findViewById(R.id.tv_save);
        Urls = (List<String>) getIntent().getSerializableExtra("imgdatas");//接收房间信息对象
        currentPosition = getIntent().getIntExtra("currentPosition", 0);
        initData();
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
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        adapter = new MyImageAdapter(Urls, this); //图片地址 Urls
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void customTitleCenter(TextView titleCenter) {
    }


    @Override
    protected void initEventListeners() {
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override//保存图片
            public void onClick(View v) {
                getImageCameraPermission(Urls.get(currentPosition));
            }
        });
        mainLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //    保存文件的方法：
    public void SaveBitmapFromView(String imageurl) {
        final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Glide.get(mContext).clearMemory();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(mContext).load(imageurl).apply(requestOptions).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                saveBitmap(drawable2Bitmap(resource), format.format(new Date()) + ".JPEG");
            }
        });
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public void saveBitmap(Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;

        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
// 插入图库
                MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), bitName, null);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        Log.e("saveBitmap", "保存成功,图片保存到相册DCIM文件夹下" + fileName);
        Toast.makeText(mContext, "保存成功,图片保存到相册DCIM文件夹下", Toast.LENGTH_LONG).show();
    }


    /**
     * 获取权限 Permission
     */
    public void getImageCameraPermission(final String imageurl) {
        //判断版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检查权限是否被授予：
            int hasExternalPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasExternalPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ShowBigPicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
            } else {
                Glide.get(mContext).clearMemory();
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop();
                Glide.with(mContext).load(imageurl).apply(requestOptions).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        SaveBitmapFromView(imageurl);
                    }
                });
            }
        } else {
            Glide.get(mContext).clearMemory();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            Glide.with(mContext).load(imageurl).apply(requestOptions).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    SaveBitmapFromView(imageurl);
                }
            });
        }
    }


    Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 12:
                boolean permissionsIsAgree = false;// 许可
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionsIsAgree = true;
                }
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && permissionsIsAgree) {
                    SaveBitmapFromView(Urls.get(currentPosition)); // 许可
                } else {
                    Toast.makeText(mContext, "很遗憾你把相机权限禁用了。", Toast.LENGTH_SHORT).show();
                }
                break;
            case 11:
            case 10:
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    SaveBitmapFromView(Urls.get(currentPosition));// 许可
                } else {
                    // Permission Denied
                    Toast.makeText(mContext, "很遗憾你把相机权限禁用了。", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SaveBitmapFromView(Urls.get(currentPosition));
                } else {
                    // Permission Denied
                    Toast.makeText(mContext, "很遗憾你把相册权限禁用了", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
