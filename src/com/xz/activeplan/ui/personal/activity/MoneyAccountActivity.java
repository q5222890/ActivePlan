package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.AccountBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.AccountJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 账号余额界面activity
 * 
 * @author johnny
 * 
 */
public class MoneyAccountActivity extends BaseFragmentActivity implements
		OnClickListener,ClassObserver {
	private TextView mTvHeadTitle, mTvLoginAndReg;
	private RelativeLayout save_ly;
	private Button btnTiXian;
	private CustomProgressDialog dialog = null;
	private UserInfoBean mUserInfoBean ;
	private AccountBean mAccountBean ;
	private TextView tvacountbalance, tvcashbalance;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_money_account);
		ClassConcrete.getInstance().addObserver(this) ;
		dialog = new CustomProgressDialog(this);

		initViews();
	}

	private void initViews() {

		//返回框背景
		View viewHeald = findViewById(R.id.relativeLayout_toolbar);
		viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
		//返回键
		ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imageBlack.setOnClickListener(this);
		//头部字体
		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
		mTvLoginAndReg = (TextView) findViewById(R.id.tvLoginAndReg);
		mTvLoginAndReg.setVisibility(View.VISIBLE);
		tvacountbalance = (TextView)findViewById(R.id.textView_acountbalance) ;
		tvcashbalance = (TextView) findViewById(R.id.textView_cashbalance);

		save_ly = (RelativeLayout)findViewById(R.id.save_ly);
		btnTiXian = (Button)findViewById(R.id.btnTiXian);
		mTvHeadTitle.setText("我的余额");
		save_ly.setVisibility(View.VISIBLE) ;
		mTvLoginAndReg.setTextColor(getResources().getColor(R.color.white));
		mTvLoginAndReg.setText("明细");
		mTvLoginAndReg.setOnClickListener(this);
		btnTiXian.setOnClickListener(this);
		
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
			init();
		}else{
			Intent intent  = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish(); 
		}
	}
	
	private void init(){
		dialog.show();
		UserInfoServiceImpl.getInstance().getMoneyAccount(mUserInfoBean.getUserid()+"", mUserInfoBean.getPhonenum(), new StringCallback(){

			@Override
			public void onResponse(String response) {
				
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						AccountJson json = new AccountJson();
						AccountBean bean = (AccountBean)json.analysisJson2Object(statusBean.getData()) ;
						if(bean != null){
							mAccountBean = bean ;
							String total =new DecimalFormat("0.00").format(bean.getTotal());
							tvacountbalance.setText( total );
							Double totalmoney =bean.getTotal();
							Double fronzenmoney =bean.getFronzen();
							String fronzen =new DecimalFormat("0.00").format(totalmoney-fronzenmoney);
							tvcashbalance.setText(fronzen);
							Utiles.log("账户余额："+total+";可提现余额："+fronzen);

						}
					}else{
						ToastUtil.showShort("账号余额获取失败，请退出重试!") ;
					}
				}else{
					ToastUtil.showShort("账号余额获取失败，请退出重试!") ;
				}
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				dialog.dismiss();
				ToastUtil.showShort("账号余额获取失败，请退出重试!") ;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.tvLoginAndReg://tvLoginAndReg
			Intent intent = new Intent(MoneyAccountActivity.this,MoneyListActivity.class);
			startActivity(intent);
			break;
		case R.id.btnTiXian:
			if(mAccountBean != null){
				intent = new Intent(MoneyAccountActivity.this,MoneyTiXinActivity.class);
				intent.putExtra("data", mAccountBean);
				startActivity(intent);
			}else{
				init();
			}
			break;

//			case R.id.line_blue:
//				lineblue.setLayoutParams(layoutParams1);
//				lineyellow.setLayoutParams(layoutParams2);
//				balance.setTextSize(30);
//				money.setTextSize(20);
//				cashmoney.setTextSize(14);
//				cash.setTextSize(14);
//				break;
//			case R.id.line_yellow:
//				lineblue.setLayoutParams(layoutParams2);
//				lineyellow.setLayoutParams(layoutParams1);
//				balance.setTextSize(14);
//				money.setTextSize(14);
//				cashmoney.setTextSize(20);
//				cash.setTextSize(30);
//				break;
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
		if(event.getType() == EventType.ACCOUNT_TIXIANE_TYPE){
			init();
		}
		return false;
	}

	
}
