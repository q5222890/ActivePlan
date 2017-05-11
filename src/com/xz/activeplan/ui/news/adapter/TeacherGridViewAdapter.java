package com.xz.activeplan.ui.news.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.TeacherBean;

import java.util.List;

public class TeacherGridViewAdapter extends BaseAdapter {
	private List<TeacherBean> list;
	private Context context;
	public  TeacherGridViewAdapter(Context context,List<TeacherBean> list) {
		this.list = list ;
		this.context = context ;
	}

	@Override
	public int getCount() {
		if(list == null){
			return 0 ;
		}
		return list.size();
	}

	@Override
	public TeacherBean getItem(int position) {
		if(list == null){
			return null;
		}
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context.getApplicationContext(), R.layout.listitem_gridview_teacher, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		TeacherBean bean = getItem(position);
		if(bean != null){
			if(!TextUtils.isEmpty(bean.getRealname())){
				holder.textViewName.setText(bean.getRealname()) ;
			}else{
				holder.textViewName.setText("未知");
			}

			if(!TextUtils.isEmpty(bean.getCover())){
				Picasso.with(context).load(bean.getCover()).error(R.drawable.default_home_image_h).placeholder(R.drawable.default_home_image_h).fit().centerCrop().into(holder.imagePhone);
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imagePhone;
		TextView textViewName;

		public ViewHolder(View view) {
			imagePhone = (ImageView)view.findViewById(R.id.imgphoto) ;
			textViewName = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(this);
		}
	}

}
