package com.xz.activeplan.ui.recommend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.SponsorsSupportBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class SponsorsSupportAdapter extends BaseAdapter{

    private List<SponsorsSupportBean> list =new ArrayList<>();
    private Context context;

    public SponsorsSupportAdapter(List<SponsorsSupportBean> supportsList, Context context) {
        this.list = supportsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView =View.inflate(context, R.layout.gridview_item_support,null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        SponsorsSupportBean bean = (SponsorsSupportBean) getItem(position);

//        holder.conunt.setText(bean.getSponsorscounts()+"人赞助");
//        holder.sponsortype.setText(bean.getSupporttype());
//        holder.paybackcontent.setText(bean.getSponsorcontent());
//        holder.sponsorPrice.setText("￥："+bean.getSupportprice());
//        holder.contactname.setText(bean.getContactname());
//        holder.contactphone.setText(bean.getContactphone());
        holder.tvcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.checkimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.gosponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return null;
    }

    class ViewHolder{
        TextView conunt,sponsortype,sponsorPrice,paybackcontent,contactname,contactphone,
                tvcheck,gosponsor,checkimage;
        public ViewHolder(View view) {

            conunt = (TextView) view.findViewById(R.id.tv_sponsors_count);
            sponsortype = (TextView) view.findViewById(R.id.tv_sponsortype);
            sponsorPrice = (TextView) view.findViewById(R.id.tv_sponsor_price);
            paybackcontent = (TextView) view.findViewById(R.id.tv_payback_content);
            contactname = (TextView) view.findViewById(R.id.tv_contact_name);
            contactphone = (TextView) view.findViewById(R.id.tv_contact_phone);

            tvcheck = (TextView) view.findViewById(R.id.tv_check);
            gosponsor = (TextView) view.findViewById(R.id.tv_go_sponsor);
            checkimage = (TextView) view.findViewById(R.id.tv_check_image);

            view.setTag(this);
        }

    }
}
