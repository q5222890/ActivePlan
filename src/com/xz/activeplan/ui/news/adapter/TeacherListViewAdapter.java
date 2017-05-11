package com.xz.activeplan.ui.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.CelebriteAndAreaBean;

import java.util.List;

public class TeacherListViewAdapter extends BaseAdapter {
	private List<CelebriteAndAreaBean> mAppList;
	private Context mCtx ;
	private int selectIndex = 0 ;
	
	public  TeacherListViewAdapter(Context context,List<CelebriteAndAreaBean> list) {
		this.mAppList = list ;
		this.mCtx = context ;
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public CelebriteAndAreaBean getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelectIndex(int index){
		this.selectIndex = index ;
		notifyDataSetChanged() ;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.listitem_listview_teacher, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		CelebriteAndAreaBean item = getItem(position);
		holder.tv_name.setText(item.getContent());
		if(selectIndex == position){
			holder.tv_name.setTextColor(Color.parseColor("#049DD2")) ;
		}else{
			holder.tv_name.setTextColor(Color.parseColor("#666666")) ;
		}


		return convertView;
	}

	class ViewHolder {
		
		TextView tv_name;

		public ViewHolder(View view) {
			
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(this);
		}
	}

}
