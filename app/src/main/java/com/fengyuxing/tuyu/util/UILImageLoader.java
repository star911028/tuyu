package com.fengyuxing.tuyu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.fengyuxing.tuyu.R;

/**
 * Created by Administrator on 2017/8/7.
 */
//public class UILImageLoader implements ImageLoader {
public class UILImageLoader {
    private Bitmap.Config mConfig;
    private Context context;

    public UILImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public UILImageLoader(Bitmap.Config config) {
        this.mConfig = config;
    }

    public UILImageLoader(Context context) {
        this.context = context;
    }

    /**
     * 加载正方形图片
     *
     * @param img_url
     * @param imageView
     */
    public void loadImage(String img_url, final ImageView imageView) {
        /*if (imageView.getTag() != null) {//列表加载
            if (StringUtils.isNotEmpty(img_url)  && imageView.getTag().equals(img_url)) {
                Picasso.with(context).load(img_url).config(mConfig).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(imageView);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        } else {//单张图片加载未设置tag的
            if (StringUtils.isNotEmpty(img_url) ) {
                Picasso.with(context).load(img_url).config(mConfig).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(imageView);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }*/
        if (StringUtils.isNotEmpty(img_url) ) {
            Picasso.with(context).load(img_url).config(mConfig).error(R.drawable.rabblt_icon).placeholder(R.drawable.rabblt_icon).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.rabblt_icon);
        }
    }


    public void loadImage2(String img_url, final ImageView imageView) {
        /*if (imageView.getTag() != null) {//列表加载
            if (StringUtils.isNotEmpty(img_url)  && imageView.getTag().equals(img_url)) {
                Picasso.with(context).load(img_url).config(mConfig).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(imageView);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        } else {//单张图片加载未设置tag的
            if (StringUtils.isNotEmpty(img_url) ) {
                Picasso.with(context).load(img_url).config(mConfig).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(imageView);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }*/
        if (StringUtils.isNotEmpty(img_url) ) {
            Picasso.with(context).load(img_url).config(mConfig).error(R.color.white).placeholder(R.color.white).into(imageView);
        } else {
            imageView.setImageResource(R.color.white);
        }
    }

  /*  @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(mConfig)
                .resize(width, height)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }*/
}
