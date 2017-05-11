package com.xz.activeplan.ui.recommend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;

public class ChartInfoAdapter extends BaseAdapter {

	private Context mCtx ;

	
	
	
	public  ChartInfoAdapter(Context context) {
		this.mCtx = context ;


	}

	@Override
	public int getCount() {
		
		return 20;
	}

	@Override
	public Object getItem(int position) {
		
		return "";
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
