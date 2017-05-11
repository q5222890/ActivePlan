package com.xz.activeplan.ui.personal.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.AccountDeatilBean;
import com.xz.activeplan.ui.personal.activity.MoneyDetailActivity;

public class AccountDetailAdapter extends BaseAdapter {
	private List<AccountDeatilBean> mAppList;
	private Context mCtx ;
	public  AccountDetailAdapter(Context context,List<AccountDeatilBean> list) {
		this.mAppList = list ;
		this.mCtx = context ;
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public AccountDeatilBean getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.listitem_account_detail, null);
			new ViewHolder(convertView);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		final AccountDeatilBean item = getItem(position);
		
		holder.tv_intro.setText(item.getIntro() + "") ;
		if(!TextUtils.isEmpty(item.getCreatetime()) && !"null".equals(item.getCreatetime())){
			holder.tv_time.setText(item.getCreatetime() + "") ;
		}else{
			holder.tv_time.setText("") ;
		}
		
		
		if(item.getType() == 1){
			holder.tv_money.setText(" + " + item.getNum()) ;
			holder.tv_money.setTextColor(Color.GREEN) ;
		}else{
			holder.tv_money.setText(" - " + item.getNum()) ;
			holder.tv_money.setTextColor(Color.RED) ;
		}
		
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mCtx,MoneyDetailActivity.class);
				intent.putExtra("data", item) ;
				mCtx.startActivity(intent);
			}
		}) ;
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_intro,tv_time,tv_money ;

		public ViewHolder(View view) {
			tv_intro = (TextView)view.findViewById(R.id.tv_intro) ;
			tv_time = (TextView)view.findViewById(R.id.tv_time) ;
			tv_money = (TextView)view.findViewById(R.id.tv_money) ;
			
			view.setTag(this);
		}
	}

}
