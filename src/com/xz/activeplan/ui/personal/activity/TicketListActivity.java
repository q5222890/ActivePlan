package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketListBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.TicketListJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CircleImageView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 4.1.31.	活动报名者列表
 * @author johnny
 *
 */
public class TicketListActivity extends BaseFragmentActivity implements OnClickListener{

	
private TextView mTvHeadTitle ;
	
	private ImageView iv_datails_back;
	
	private ListView mListView ;
	
	private TicketListAdapter mTicketListAdapter ;
	
	private ArrayList<TicketListBean> arr = new ArrayList<TicketListBean>();
	
	private int activeid;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_ticket_list) ;
		initViews();
		
	}
	
	private void initViews(){
		mTvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;
		iv_datails_back = (ImageView)findViewById(R.id.id_ImageHeadTitleBlack) ;
		mListView = (ListView)findViewById(R.id.id_XListview);

		mTicketListAdapter = new TicketListAdapter();
		mListView.setAdapter(mTicketListAdapter) ;

		mTvHeadTitle.setText("参加人员");
		
		iv_datails_back.setOnClickListener(this) ;
		Intent intent = getIntent() ;
		
		if(intent != null){
			activeid = intent.getIntExtra("activeid", 0) ;
		}
		
		loadData(activeid);
	}
	
	private void loadData(int activeid){
		UserInfoServiceImpl.getInstance().ticketList(activeid, new StringCallback() {
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						TicketListJson ticketJson = new TicketListJson() ;
						Object data = ticketJson.analysisJson2Object(statusBean.getData()) ;
						if(data !=null){
							ArrayList<TicketListBean> list = (ArrayList<TicketListBean>)data;
							arr.clear();
							arr.addAll(list);
							mTicketListAdapter.notifyDataSetChanged() ;
						}
					}else{
						ToastUtil.showShort("加载失败,请稍后重试!") ;
					}
				}else{
					ToastUtil.showShort("加载失败,请稍后重试!") ;
				}

			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				ToastUtil.showShort("加载失败,请稍后重试!") ;
			}
		}) ;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish() ;
			break;
		}
	}
	
	public class TicketListAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return arr.size();
		}

		@Override
		public TicketListBean getItem(int position) {
			return arr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(TicketListActivity.this, R.layout.listitem_ticket_list, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			TicketListBean bean = getItem(position);
			
			if(!TextUtils.isEmpty(bean.getUsername())){
				holder.tv_name.setText("姓名:"+bean.getUsername() + "") ;
			}else if(!TextUtils.isEmpty(bean.getPhonenum())){
				holder.tv_name.setText("姓名:"+bean.getPhonenum() + "") ;
			}else{
				holder.tv_name.setText("姓名:"+"未知") ;
			}
			if(!TextUtils.isEmpty(bean.getHeadurl())){
				Picasso.with(TicketListActivity.this).load(bean.getHeadurl()).error(R.drawable.icon).into(holder.img);
			}else{
				Picasso.with(TicketListActivity.this).load(R.drawable.icon);
			}
			
			if(!TextUtils.isEmpty(bean.getTicket())){
				holder.tv_ticket_num.setText("票号："+ bean.getTicket());
			}
			
			if(bean.isTicketcheck()){
				holder.tv_ticket.setText("已验票") ;
			}else{
				holder.tv_ticket.setText("未验票") ;
			}
			
			return convertView;
		}

		class ViewHolder {
			TextView tv_name,tv_ticket,tv_ticket_num;
			CircleImageView img;

			public ViewHolder(View view) {
				tv_name = (TextView)view.findViewById(R.id.tv_name);
				tv_ticket = (TextView)view.findViewById(R.id.tv_ticket) ;
				img = (CircleImageView)view.findViewById(R.id.img) ;
				tv_ticket_num = (TextView)view.findViewById(R.id.tv_ticket_num);
				view.setTag(this);
			}
		}

	}
	
}
