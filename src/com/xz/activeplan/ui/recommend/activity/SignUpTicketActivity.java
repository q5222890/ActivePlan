package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import java.util.ArrayList;

/**
 * 报名界面activity
 * 
 * @author johnny
 * 
 */
public class SignUpTicketActivity extends BaseFragmentActivity implements
		OnClickListener ,ClassObserver{
	private ImageView iv_datails_back;
	private TextView tvHeadTitle;
	
	private ListView mListView ;
	
	private TicketAdapter mTicketAdapter;
	
	private ActiveinfosBean mActiveinfosBean ;
	
	private UserInfoBean mUserInfoBean ;
	
	private ArrayList<TicketAddBean> ticketTypes ;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		ClassConcrete.getInstance().addObserver(this) ;
		setContentView(R.layout.activity_singup_ticket_layout);
		initViews();
	}

	private void initViews() {
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		mListView = (ListView)findViewById(R.id.id_XListview) ;
		
		tvHeadTitle.setText("报名");
		iv_datails_back.setOnClickListener(this);
		
		mTicketAdapter = new TicketAdapter();
		mListView.setAdapter(mTicketAdapter); 
		
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
		}else{
//			Toast.makeText(SignUpTicketActivity.this, "请登录用户", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请登录用户") ;
			finish();
			return;
		}
		
		ticketTypes = XZApplication.getInstance().getTicketTypes();
		
		Intent intent = getIntent() ;
		if(intent != null){
			mActiveinfosBean = (ActiveinfosBean)intent.getSerializableExtra("data") ;
		
			if(mActiveinfosBean == null){
				finish();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ClassConcrete.getInstance().removeObserver(this) ;
	}
	
	@Override
	public boolean onDataUpdate(Object data) {
		EventBean event = (EventBean)data ;
		if(event.getType() == EventType.ACCOUNT_BAOMING_TYPE){
			finish();
		}
		return false;
	}

	class TicketAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(ticketTypes == null){
				return 0 ;
			}
			return ticketTypes.size();
		}

		@Override
		public Object getItem(int arg0) {
			if(ticketTypes == null){
				return null;
			}
			return ticketTypes.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(SignUpTicketActivity.this, R.layout.listitem_tickets, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			final TicketAddBean bean = (TicketAddBean)getItem(position) ;
			Utiles.log("报名TicketAddBean:"+bean);
			if(bean.getType() == 2){
				holder.tvTicketPrice.setText("￥" + bean.getMoney());
				holder.tvTicketTitle.setText("" + bean.getName()) ;
				holder.tvTicketContent.setText(bean.getIntro() + "");
			}else{
				holder.tvTicketPrice.setText("免费");
				holder.tvTicketTitle.setText("" + bean.getName()) ;
				holder.tvTicketContent.setText(bean.getIntro() + "");
			}

			if(bean.isIssellout()){
				holder.tvTicketStatus.setBackgroundResource(R.drawable.btn_shape_bg2) ;
				holder.tvTicketStatus.setText("已售完") ;
			}else{
				holder.tvTicketStatus.setBackgroundResource(R.drawable.btn_shape_bg) ;
				holder.tvTicketStatus.setText("购票") ;
			}

			holder.surplusTickets.setText(bean.getSurpluspersonnum()+"");
//			holder.tvLineationPrice.setVisibility(View.GONE) ;
//			if(mActiveinfosBean != null && mActiveinfosBean.isCharge()){
//				holder.tvTicketPrice.setText("￥" + mActiveinfosBean.getMoney());
//				holder.tvTicketTitle.setText("收费票") ;
//				holder.tvTicketContent.setText("(收费票说明：费用包括了商务套票等所有费用)");
//
//			}else{
//				holder.tvTicketPrice.setText("免费");
//				holder.tvTicketTitle.setText("现场票") ;
//				holder.tvTicketContent.setText("(现场票说明：现场付费)");
//			}

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//if(mActiveinfosBean != null && mActiveinfosBean.isCharge()){
//						Intent intent = new Intent(SignUpTicketActivity.this,SureOrderActivity.class);
					if(!bean.isIssellout()){
						Intent intent = new Intent(SignUpTicketActivity.this,BuyTicketActivity.class);
							intent.putExtra("data", mActiveinfosBean) ;
							intent.putExtra("TicketAddBean", bean);
							SignUpTicketActivity.this.startActivity(intent) ;
					}
//					}else{
//						pay(mActiveinfosBean.getActiveid());
//					}
				}
			}) ;

			return convertView;
		}

		class ViewHolder {
			TextView tvTicketPrice,tvLineationPrice,tvTicketTitle,tvTicketContent,tvTicketStatus,surplusTickets ;

			public ViewHolder(View view) {
				tvTicketPrice = (TextView)view.findViewById(R.id.tvTicketPrice) ;
				tvLineationPrice = (TextView)view.findViewById(R.id.tvLineationPrice) ;
				tvTicketTitle = (TextView)view.findViewById(R.id.tvTicketTitle) ;
				tvTicketContent = (TextView)view.findViewById(R.id.tvTicketContent) ;
				tvTicketStatus = (TextView)view.findViewById(R.id.tvTicketStatus) ;
				surplusTickets = (TextView) view.findViewById(R.id.tv_surplusTickets);
				view.setTag(this);
			}
		}

	}
}
