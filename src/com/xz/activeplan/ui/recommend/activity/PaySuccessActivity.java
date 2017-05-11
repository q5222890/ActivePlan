package com.xz.activeplan.ui.recommend.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;

/**
 * 付款成功界面activity 
 * @author johnny
 *
 */
public class PaySuccessActivity extends BaseFragmentActivity implements OnClickListener{
	
	
	private ImageView iv_datails_back;
	private TextView tvHeadTitle;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_pay_success) ;
		initViews();
	}
	
	private void initViews() {
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		tvHeadTitle.setText("报名成功");
		iv_datails_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		}
	}
}
