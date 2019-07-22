package com.fengyuxing.tuyu.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fengyuxing.tuyu.R;


public class ViewPagerAdapterRank extends PagerAdapter {
    private String[] resIds = {
            //最后一张图片
//            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523772064849&di=7502440dd81352d3e3d584eb810e0acc&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F265%2F5432CKKPF561_1000x500.jpg",
            //正真的5张图片
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523772064850&di=dd3588fdf916fce1527f7b53cc536293&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F088%2F9KF35LBMFO74.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523772064854&di=165a16c1167b38b067b3c82cb830c347&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201303%2F18%2F110902j0e5zjwj0yutt00j.jpg",
            //第一张图片
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523772064850&di=dd3588fdf916fce1527f7b53cc536293&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F088%2F9KF35LBMFO74.jpg",
    };

    private Context context ;
    public ViewPagerAdapterRank(Context context) {
        this.context = context ;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.vp_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_iv1);
        TextView ty_tv = (TextView) view.findViewById(R.id.type_tv);
        if(position==0){
            ty_tv.setText("今日魅力榜");
        }else {
            ty_tv.setText("今日贡献榜");
        }
        Glide.with(context)
                .load(resIds[position])
                .into(imageView);
//        imageView.setImageResource(resIds[position]);
        //添加到容器中
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
