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
import com.xz.activeplan.entity.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class MyFansAdapter extends BaseAdapter {

    private List<UserInfoBean> fansList = new ArrayList<>();
    private Context mContext;
    private int fansType = 0;

    public MyFansAdapter(List<UserInfoBean> fansList, Context mContext, int fansType) {
        this.fansList = fansList;
        this.mContext = mContext;
        this.fansType = fansType;
    }

    @Override
    public int getCount() {
        return fansList.size();
    }

    @Override
    public Object getItem(int position) {
        return fansList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder(convertView);

        if (convertView == null) {
            convertView = View.inflate(mContext.getApplicationContext(), R.layout.listitem_myfans, null);
            new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserInfoBean item = (UserInfoBean) getItem(position);
        if (!TextUtils.isEmpty(item.getHeadurl())) {
            Picasso.with(mContext).load(item.getHeadurl()).error(R.drawable.default_details_image).into(holder.headView);
        } else {
            Picasso.with(mContext).load(R.drawable.default_details_image).into(holder.headView);
        }
        holder.fanscity.setText(item.getCity());
        holder.fansname.setText(item.getUsername());
        return convertView;
    }


    class ViewHolder {
        ImageView headView;
        TextView fansname, fanscity;

        public ViewHolder(View view) {
            headView = (ImageView) view.findViewById(R.id.circle_headview);
            fansname = (TextView) view.findViewById(R.id.tv_fansname);
            fanscity = (TextView) view.findViewById(R.id.tv_fanscity);
            view.setTag(this);
        }
    }
}
