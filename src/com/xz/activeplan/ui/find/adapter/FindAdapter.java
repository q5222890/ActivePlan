package com.xz.activeplan.ui.find.adapter;

import java.util.List;

import com.xz.activeplan.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindAdapter extends BaseAdapter {
	private List<ApplicationInfo> mAppList;
	private Context mCtx ;
	public  FindAdapter(Context context,List<ApplicationInfo> list) {
		this.mAppList = list ;
		this.mCtx = context ;
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public ApplicationInfo getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// menu type count
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		// current menu type
		return position % 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.item_list_app, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ApplicationInfo item = getItem(position);
		holder.iv_icon.setImageDrawable(item.loadIcon(mCtx.getPackageManager()));
		holder.tv_name.setText(item.loadLabel(mCtx.getPackageManager()));
		return convertView;
	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;

		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(this);
		}
	}

}
