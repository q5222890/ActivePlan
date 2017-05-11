package com.xz.activeplan.ui.recommend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.ui.find.activity.AccuvallyDetailsActivity;

import java.util.ArrayList;


/**
 * 广告页面适配器
 * @author johnny
 *
 */
public class BannerPagerAdapter extends PagerAdapter {

	private ArrayList<ActiveinfosBean> mListDatas;
	private Context mCtx ;
	
	public BannerPagerAdapter(ArrayList<ActiveinfosBean> listdatas,Context context) {
		this.mListDatas = listdatas;//构造方法，参数是我们的页卡，这样比较方便。
		this.mCtx = context;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 	{	
		((ViewPager)container).removeView((View) object);
	}


	@Override
	public Object instantiateItem(ViewGroup container, final int position) {	//这个方法用来实例化页卡		
		
		View view = LayoutInflater.from(mCtx).inflate(R.layout.banner_layout, null) ;
		ImageView imgPhoto = (ImageView)view.findViewById(R.id.img_photo) ;
		
		Picasso.with(mCtx).load(mListDatas.get(position).getActiveurl()).placeholder(R.drawable.default_details_image).error(R.drawable.default_details_image).fit().centerCrop().into(imgPhoto);
		
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mCtx,AccuvallyDetailsActivity.class);
				intent.putExtra("data", mListDatas.get(position));
				mCtx.startActivity(intent);
			}
		});
		
		((ViewPager) container).addView(view, 0);
		return view ;
	}

	@Override
	public int getCount() {			
		return  mListDatas.size();//返回页卡的数量
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {			
		return arg0==arg1;//官方提示这样写
	}

}
