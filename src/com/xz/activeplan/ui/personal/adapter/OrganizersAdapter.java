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
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.ui.recommend.activity.SponsorDetailActivity;

import java.util.List;

/**
 * 活动适配器
 */
public class OrganizersAdapter extends BaseAdapter {
	
	private List<OrganizersBean> mAppList;
	
	private Context mCtx ;
	
	public  OrganizersAdapter(Context context,List<OrganizersBean> list) {
		this.mAppList = list ;
		this.mCtx = context ;
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public OrganizersBean getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.listitem_organizers, null);
			new ViewHolder(convertView);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		final OrganizersBean item = getItem(position);
		
		if(!TextUtils.isEmpty(item.getHostintro()) && !"null".equals(item.getHostintro())){
			holder.tvOrgHostintro.setText(item.getHostintro()+"") ;
		}else{
			holder.tvOrgHostintro.setText("未知") ;
		}
		
		if(!TextUtils.isEmpty(item.getHostname()) && !"null".equals(item.getHostname())){
			holder.tvOrgName.setText(item.getHostname()+"");
		}else{
			holder.tvOrgName.setText("未知");
		}
		
		holder.tvOrgNum.setText(item.getFollownum()+"");
		
		if(!TextUtils.isEmpty(item.getHosthearurl()) && !"null".equals(item.getHosthearurl())){
			Picasso.with(mCtx).load(item.getHosthearurl()).placeholder(R.drawable.default_details_image).error(R.drawable.default_details_image).into(holder.ivOrgLogoUrl);
		}else{
			Picasso.with(mCtx).load(R.drawable.default_details_image).placeholder(R.drawable.default_details_image).into(holder.ivOrgLogoUrl) ; 
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent(mCtx,SponsorDetailActivity.class) ;
				 item.setIsattend(true);
				 intent.putExtra("OrganizersBean",item) ;
				 mCtx.startActivity(intent);
			}
		}) ;
		
		return convertView;
	}

	class ViewHolder {
		ImageView ivOrgLogoUrl;
		TextView tvOrgName,tvOrgNum,tvOrgHostintro;

		public ViewHolder(View view) {
			ivOrgLogoUrl = (ImageView)view.findViewById(R.id.ivOrgLogoUrl) ;
			tvOrgName = (TextView)view.findViewById(R.id.tvOrgName) ;
			tvOrgNum = (TextView)view.findViewById(R.id.tvOrgNum) ;
			tvOrgHostintro = (TextView)view.findViewById(R.id.tvOrgHostintro) ;
			
			view.setTag(this);
		}
	}

}
