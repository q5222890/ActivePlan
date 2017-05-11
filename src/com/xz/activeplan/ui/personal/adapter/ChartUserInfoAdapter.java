package com.xz.activeplan.ui.personal.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.UserInfoBean;

public class ChartUserInfoAdapter extends BaseAdapter {

	private Context mCtx ;

	private ArrayList<UserInfoBean> mDatas ;
	
	public  ChartUserInfoAdapter(Context context,ArrayList<UserInfoBean> list) {
		this.mCtx = context ;
		this.mDatas = list ;
	}

	@Override
	public int getCount() {
		if(mDatas == null){
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.listitem_chart_info, null);
			new ViewHolder(convertView);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		UserInfoBean bean = (UserInfoBean)getItem(position);
		
		if(!TextUtils.isEmpty(bean.getUsername())){
			holder.tv_nick.setText(bean.getUsername()) ;
		}else{
			holder.tv_nick.setText(bean.getPhonenum()) ;
		}
		
		if(!TextUtils.isEmpty(bean.getHeadurl())){
			Picasso.with(mCtx).load(bean.getHeadurl()).placeholder(R.drawable.default_square_image).error(R.drawable.default_square_image).into(holder.iv_head);
		}else{
			Picasso.with(mCtx).load(R.drawable.default_square_image).placeholder(R.drawable.default_square_image).into(holder.iv_head);
		}
		
		return convertView;
	}

	class ViewHolder {
		ImageView iv_head;
		TextView tv_nick;

		public ViewHolder(View view) {
			iv_head = (ImageView)view.findViewById(R.id.iv_head) ;
			tv_nick = (TextView)view.findViewById(R.id.tv_nick) ;
			view.setTag(this);
		}
	}

}
