package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.activity.AccuvallyDetailsActivity;
import com.xz.activeplan.ui.find.activity.MapActivity;
import com.xz.activeplan.utils.DensityUtil;
import com.xz.activeplan.utils.QRCodeUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CircleImageView;

import java.io.IOException;

/**
 * 票务详情界面activity
 * @author johnny
 *
 */
public class TicketVoumeDetalActivity extends BaseFragmentActivity implements OnClickListener{
	
	private TextView mTvHeadTitle ,tv_title,txt_num,tvUserName,tvDetailsTime,tvDetailsAddress,tvAccuvallyDetail;
	private ImageView iv_datails_back,img_qr;
	private TicketBean mTicketBean; 
	private UserInfoBean mUserInfoBean ;
	private CircleImageView ivUserLogo;
	private LinearLayout lyDetailsAddr;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_ticket_volume_details) ;
		initViews();
	}
	
	private void initViews(){
		mTvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;
		iv_datails_back = (ImageView)findViewById(R.id.id_ImageHeadTitleBlack) ;
		
		tv_title = (TextView)findViewById(R.id.tv_title) ;
		txt_num = (TextView)findViewById(R.id.txt_num) ;
		tvUserName = (TextView)findViewById(R.id.tvUserName) ;
		tvDetailsTime = (TextView)findViewById(R.id.tvDetailsTime) ;
		tvDetailsAddress = (TextView)findViewById(R.id.tvDetailsAddress) ;
		tvAccuvallyDetail  = (TextView)findViewById(R.id.tvAccuvallyDetail) ;
		img_qr = (ImageView)findViewById(R.id.img_qr) ;
		ivUserLogo = (CircleImageView)findViewById(R.id.ivUserLogo);
		
		lyDetailsAddr = (LinearLayout)findViewById(R.id.lyDetailsAddr) ;
		
		mTvHeadTitle.setText("票券");
		
		iv_datails_back.setOnClickListener(this);
		tvAccuvallyDetail.setOnClickListener(this);
		lyDetailsAddr.setOnClickListener(this) ;
		
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
		}else{
//			Toast.makeText(this, "请登录用户!",Toast.LENGTH_LONG).show() ;
			ToastUtil.showShort("请登录用户!") ;
			finish();
		}
		
		Intent intent = getIntent() ;
		if(intent != null){
			mTicketBean = (TicketBean)intent.getSerializableExtra("data") ;

			if(mTicketBean != null){
				Utiles.log("票务："+mTicketBean.toString());
				tv_title.setText(mTicketBean.getName());

				txt_num.setText("票号:" + mTicketBean.getTicket());
				
				if(!TextUtils.isEmpty(mUserInfoBean.getRealname()) && !"null".equals(mUserInfoBean.getRealname())){
					tvUserName.setText(mUserInfoBean.getRealname());
				}else{
					tvUserName.setText(mUserInfoBean.getPhonenum());
				}
				
				tvDetailsTime.setText(TimeUtils.getTime(mTicketBean.getStartdate()) + " --- " + TimeUtils.getTime(mTicketBean.getEnddate())) ;
				
				tvDetailsAddress.setText(mTicketBean.getAddress());
				
				if(!TextUtils.isEmpty(mUserInfoBean.getHeadurl())){
					Picasso.with(this).load(mUserInfoBean.getHeadurl()).error(R.drawable.default_square_image).into(ivUserLogo);
				}
				
				Bitmap bitmap  =QRCodeUtil.createQRImage(mTicketBean.getTicket(), DensityUtil.dip2px(this, 120), DensityUtil.dip2px(this, 120));
				if(bitmap != null){
					img_qr.setImageBitmap(bitmap);
				}
			}
		}
		
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish() ;
			break;
		case R.id.tvAccuvallyDetail:
			loadData(mTicketBean.getActiveid());
			break;
		case R.id.lyDetailsAddr:
			Intent intent = new Intent(TicketVoumeDetalActivity.this,MapActivity.class) ;
			intent.putExtra("address", mTicketBean.getAddress()) ;
			startActivity(intent);
			break;
		}
		
	}
	
	private void loadData(int activeid){
		int userid = -1 ;
		if(mUserInfoBean != null){
			userid = mUserInfoBean.getUserid();
		}
		
		ActiveServiceImpl.getInstance().getActive(userid,activeid, new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						ActiveinfosJson activeinfosJson = new ActiveinfosJson();
						obj = activeinfosJson
								.parseJson(statusBean
										.getData());
						if (obj != null) {
							ActiveinfosBean mActiveinfosBean = (ActiveinfosBean)obj;
							Intent intent = new Intent(TicketVoumeDetalActivity.this,
									AccuvallyDetailsActivity.class);
							intent.putExtra("data",mActiveinfosBean) ;
							startActivity(intent);
						}
					}
				}


			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				
			}
		}) ;
	}
}
