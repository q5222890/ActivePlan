package com.xz.activeplan.ui.find.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.AdvertisementBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
public class BannerViewPagerImageAdapter extends PagerAdapter {

    private List<AdvertisementBean> list;
    private Context context;
    public BannerViewPagerImageAdapter(List<AdvertisementBean> listdatas, Context context) {
        this.list = listdatas;//构造方法，参数是我们的页卡，这样比较方便。
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) 	{
        ((ViewPager)container).removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {	//这个方法用来实例化页卡
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, null) ;
        ImageView imgPhoto = (ImageView)view.findViewById(R.id.img_photo) ;
        Picasso.with(context).load(list.get(position).getUrl())
                .placeholder(R.drawable.default_details_image).error(R.drawable.default_details_image).fit().centerCrop().into(imgPhoto);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//这里是要挑战
             /*   Intent intent = new Intent(context,AccuvallyDetailsActivity.class);
                intent.putExtra("data", imageViews.get(position));
                context.startActivity(intent);*/
            }
        });

        ((ViewPager) container).addView(view, 0);
        return view ;
    }

    @Override
    public int getCount() {
        return  list.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }

}

