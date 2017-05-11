package com.xz.activeplan.ui.personal.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.ui.find.activity.AccuvallyDetailsActivity;
import com.xz.activeplan.ui.personal.activity.TicketListActivity;
import com.xz.activeplan.utils.TimeUtils;

public class HostActiveAdapter extends BaseAdapter {

	private Context mCtx ;
	private ArrayList<ActiveinfosBean> mListDatas ;
	
	Drawable img_off;  
	
	public  HostActiveAdapter(Context context,ArrayList<ActiveinfosBean> lists) {
		this.mCtx = context ;
		this.mListDatas = lists ;
		Resources res = mCtx.getResources();  
		img_off = res.getDrawable(R.drawable.yanjing);  
	}

	@Override
	public int getCount() {
		if(mListDatas == null){
			return 0 ;
		}
		return mListDatas.size();
	}
	@Override
	public Object getItem(int position) {
		if(mListDatas == null){
			return null;
		}
		return mListDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.listitem_home_recommend, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ActiveinfosBean bean = (ActiveinfosBean)getItem(position);
		if(!TextUtils.isEmpty(bean.getActiveurl())){
			Picasso.with(mCtx).load(bean.getActiveurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).into(holder.ivItemRecommendImg);
		}else{
			Picasso.with(mCtx).load(R.drawable.thumb).placeholder(R.drawable.thumb).into(holder.ivItemRecommendImg);
		}
		holder.tvItemTitle.setText(bean.getName()) ;
		holder.tvItemAddress.setText(bean.getAddress()) ;
		holder.tvItemTime.setText(TimeUtils.getTime(bean.getStartdate()+""));
		holder.tvItemVisitNum.setText(bean.getSeenum()+"") ;
		
		if(bean.isCharge()){
			holder.tvItemPriceArea.setText("￥" + bean.getMoney()) ;
		}else{
			holder.tvItemPriceArea.setText("免费") ;
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mCtx,
						TicketListActivity.class);
				intent.putExtra("activeid",((ActiveinfosBean)getItem(position)).getActiveid()) ;
				mCtx.startActivity(intent);
			}
		}) ;
		return convertView;
	}
	class ViewHolder {
		ImageView ivItemRecommendImg;
		TextView tvItemTitle,tvItemAddress,tvItemTime,tvItemVisitNum,tvItemPriceArea;

		public ViewHolder(View view) {
			ivItemRecommendImg = (ImageView) view.findViewById(R.id.ivItemRecommendImg);
			tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);
			tvItemAddress = (TextView) view.findViewById(R.id.tvItemAddress);
			tvItemTime = (TextView) view.findViewById(R.id.tvItemTime);
			tvItemVisitNum = (TextView) view.findViewById(R.id.tvItemVisitNum);
			tvItemPriceArea = (TextView) view.findViewById(R.id.tvItemPriceArea);
			view.setTag(this);
		}
	}

}
