package com.xz.activeplan.ui.find.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.ui.find.activity.VideoCollectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频收藏的适配器
 */
public class VideoCollectAdapter extends BaseAdapter {
	private List<LiveTvBean> list;
	private Context mCtx ;
	private boolean isState = false;
	private ViewHolder holder;

	public VideoCollectAdapter(Context context, List<LiveTvBean> list) {
		this.list = list ;
		this.mCtx = context ;
	}

	public void setIsState(boolean isState) {
		this.isState = isState;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	public void setData(List<LiveTvBean> data) {
		this.list = data;
	}
	@Override
	public LiveTvBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArrayList<Boolean> list1 = ((VideoCollectActivity)mCtx).getSelectItems();
		String status = (((VideoCollectActivity)mCtx).tvSave).getText().toString();

		LiveTvBean item = getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mCtx.getApplicationContext(), R.layout.griditem_video_collect, null);
			holder.cover = (ImageView) convertView.findViewById(R.id.iv_livecover);
			holder.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
			holder.tv_livetitle = (TextView) convertView.findViewById(R.id.tv_livetitle);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		if(null != item)
		{
			Picasso.with(mCtx).load(item.getCoverurl()).fit().centerCrop().error(R.drawable.default_details_image)
					.placeholder(R.drawable.default_details_image).into(holder.cover);
			holder.tv_livetitle.setText(item.getTitle());
		}

		holder.tv_lable.setVisibility(View.GONE);
		if(status.equals("取消编辑"))
		{
			holder.checkBox.setVisibility(isState? View.VISIBLE : View.GONE);
			if (list1.size() != 0)
				holder.checkBox.setChecked(list1.get(position));
		}else {
			holder.checkBox.setVisibility(View.GONE);
		}
		return convertView;
	}

   public void setCheckBoxVisibility(boolean b)
   {
	   if(b==true)
	   {
		   holder.checkBox.setVisibility(View.VISIBLE);
	   }else
	   {
		   holder.checkBox.setVisibility(View.GONE);
	   }
   }

	class ViewHolder {
		ImageView cover;
		TextView tv_lable,tv_livetitle;
        CheckBox checkBox;
	}

}
