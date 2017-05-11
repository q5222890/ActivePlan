package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.AccountDeatilBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;

/**
 * 账号明细界面activity
 * 
 * @author johnny
 * 
 */
public class MoneyDetailActivity extends BaseFragmentActivity implements
		OnClickListener {

	private TextView mTvHeadTitle,lbl_num,tv_num,tv_type,tv_time,tv_phonenum,tv_intro;

	private ImageView iv_datails_back;


	private AccountDeatilBean mAccountDeatilBean ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_money_detail);

		initViews();
		Intent intent = getIntent();
		if (intent != null) {
			mAccountDeatilBean = (AccountDeatilBean) intent.getSerializableExtra("data");
			init();
		}
		
	}

	private void initViews() {
		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);

		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		
		
		lbl_num = (TextView)findViewById(R.id.lbl_num) ;
		tv_num = (TextView)findViewById(R.id.tv_num) ;
		tv_type = (TextView)findViewById(R.id.tv_type) ;
		tv_time = (TextView)findViewById(R.id.tv_time) ;
		tv_phonenum = (TextView)findViewById(R.id.tv_phonenum) ;
		tv_intro = (TextView)findViewById(R.id.tv_intro) ;

		mTvHeadTitle.setText("钱包明细");

		iv_datails_back.setOnClickListener(this);
	}

	private void init() {
		if(mAccountDeatilBean.getType() == 1){
			lbl_num.setText("入账金额") ;
			tv_num.setText(mAccountDeatilBean.getNum()+"") ;
			tv_num.setTextColor(Color.GREEN) ;
			tv_type.setText("收入");
		}else{
			lbl_num.setText("出账金额") ;
			tv_num.setText(mAccountDeatilBean.getNum()+"") ;
			tv_num.setTextColor(Color.RED) ;
			tv_type.setText("支出");
		}

		
		tv_time.setText(mAccountDeatilBean.getCreatetime());
		
		tv_phonenum.setText(mAccountDeatilBean.getPhonenum());
		
		tv_intro.setText(mAccountDeatilBean.getIntro());
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
	}
}
