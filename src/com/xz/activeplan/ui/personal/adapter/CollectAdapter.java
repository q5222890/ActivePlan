package com.xz.activeplan.ui.personal.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.xz.activeplan.entity.CollectBean;
import com.xz.activeplan.ui.find.activity.AccuvallyDetailsActivity;
import com.xz.activeplan.utils.TimeUtils;

import java.util.List;

public class CollectAdapter extends BaseAdapter {
	private List<CollectBean> mAppList;
	private Context mCtx ;
	public  CollectAdapter(Context context,List<CollectBean> list) {
		this.mAppList = list ;
		this.mCtx = context ;
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public CollectBean getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			//OOM异常
			convertView = View.inflate(mCtx, R.layout.listitem_collection, null);
			new ViewHolder(convertView);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		final CollectBean item = getItem(position);
		
		holder.tvSelTitle.setText(item.getName());
		
		holder.tvSelAddress.setText(item.getAddress());
		
		holder.tvSelItemTime.setText(TimeUtils.getTime(item.getStartdate()));
		
		holder.tvSelLikeNum.setText(item.getSeenum() + "");
		
		
		
		
		if(item.isCharge()){
			holder.tvState.setText("收费");
			holder.tvSelPriceArea.setText("￥"+ item.getMoney()+"");
		}else{
			holder.tvState.setText("免费");
			holder.tvSelPriceArea.setText("免费");
		}
		
		if(!TextUtils.isEmpty(item.getActiveurl())){
			Picasso.with(mCtx).load(item.getActiveurl()).error(R.drawable.icon).into(holder.ivSelImage);
		}else{
			Picasso.with(mCtx).load(R.drawable.default_details_image).into(holder.ivSelImage) ;
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActiveinfosBean bean = new ActiveinfosBean(item);
				Intent intent = new Intent(mCtx,AccuvallyDetailsActivity.class);
				intent.putExtra("data", bean);
				mCtx.startActivity(intent);
			}
		}) ;
		
		return convertView;
	}

	class ViewHolder {
		ImageView ivSelImage;
		TextView tvSelTitle,tvSelAddress,tvSelItemTime,tvSelPriceArea,tvSelLikeNum,tvState;

		public ViewHolder(View view) {
			ivSelImage = (ImageView) view.findViewById(R.id.ivSelImage);
			tvSelTitle = (TextView) view.findViewById(R.id.tvSelTitle);
			tvSelAddress = (TextView) view.findViewById(R.id.tvSelAddress);
			tvSelItemTime = (TextView) view.findViewById(R.id.tvSelItemTime);
			tvSelPriceArea = (TextView) view.findViewById(R.id.tvSelPriceArea);
			tvSelLikeNum = (TextView) view.findViewById(R.id.tvSelLikeNum);
			tvState = (TextView) view.findViewById(R.id.tvState);
			
			view.setTag(this);
		}
	}

}
