package com.xz.activeplan.ui.personal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.TicketBean;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.Utiles;

import java.util.List;

public class TicketAdapter extends BaseAdapter {
	private List<TicketBean> mAppList;
	private Context mCtx ;
	private int type = 1; //1=未过期, 0=已过期
	
	public  TicketAdapter(Context context,List<TicketBean> list,int type) {
		this.mAppList = list ;
		this.mCtx = context ;
		this.type = type ;
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public TicketBean getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.listitem_robticket, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		TicketBean item = getItem(position);
		Utiles.log("item:::"+item);
		holder.tvRobTitle.setText(item.getName()) ;
		
		holder.tvRobAddress.setText(item.getAddress()) ;
		
		holder.tvRobTime.setText(TimeUtils.getTime(item.getStartdate())) ;
		
		if(item.isCharge()){
			holder.tvRobPrice.setText("收费") ;
		}else{
			holder.tvRobPrice.setText("免费") ;
		}
		
		if(type == 1){
			holder.tvRobState.setText("正在热抢") ;
		}else{
			holder.tvRobState.setText("过期") ;
		}
		
		if(!TextUtils.isEmpty(item.getActiveurl())){
			Picasso.with(mCtx).load(item.getActiveurl()).error(R.drawable.default_details_image).into(holder.iv_icon);
		}else{
			Picasso.with(mCtx).load(R.drawable.default_details_image).into(holder.iv_icon) ;
		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tvRobTitle,tvRobAddress,tvRobTime,tvRobPrice,tvRobState,tvPrimeCost;

		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.ivRobImage);
			tvRobTitle = (TextView) view.findViewById(R.id.tvRobTitle);
			tvRobAddress = (TextView) view.findViewById(R.id.tvRobAddress);
			tvRobTime = (TextView) view.findViewById(R.id.tvRobTime);
			tvRobPrice = (TextView) view.findViewById(R.id.tvRobPrice);
			tvRobState = (TextView) view.findViewById(R.id.tvRobState);
			tvPrimeCost = (TextView) view.findViewById(R.id.tvPrimeCost);
			
			view.setTag(this);
		}
	}

}
