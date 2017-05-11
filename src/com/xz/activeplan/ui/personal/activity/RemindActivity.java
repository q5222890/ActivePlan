package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.CalendarUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;

/**
 * 活动设定界面activity
 * @author johnny
 * 
 */
public class RemindActivity extends BaseFragmentActivity implements OnClickListener,OnCheckedChangeListener{
	private static final String TAG = RemindActivity.class.getSimpleName() ;
	
	private TextView mTvHeadTitle ;
	

	private RadioGroup radioGroup ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_remind);
		initViews() ;
	}
	
	private void initViews(){
		//返回框背景
		View viewHeald = findViewById(R.id.relativeLayout_toolbar);
		viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
		//返回键
		ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imageBlack.setOnClickListener(this);
		//头部字体
		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
		mTvHeadTitle.setText(getResources().getString(R.string.activity_remind_str));
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup) ;
		
		radioGroup.setOnCheckedChangeListener(this);
		

		

		String type = SharedUtil.get(RemindActivity.this, "naozhong");
		if("1".equals(type)){
			((RadioButton)findViewById(R.id.radioButton11)).setChecked(true) ; 
		}else if("2".equals(type)){
			((RadioButton)findViewById(R.id.radioButton22)).setChecked(true) ;
		}else if("3".equals(type)){
			((RadioButton)findViewById(R.id.radioButton33)).setChecked(true) ;
		}else if("4".equals(type)){
			((RadioButton)findViewById(R.id.radioButton44)).setChecked(true) ;
		}else{
			((RadioButton)findViewById(R.id.radioButton11)).setChecked(true) ;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){

		case R.id.id_ImageHeadTitleBlack:
			onBackPressed();
			break; 
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(group.getCheckedRadioButtonId()){
		case R.id.radioButton11:
			SharedUtil.save(RemindActivity.this, "naozhong", "1");
			CalendarUtils.update(RemindActivity.this, 60);
			break;
		case R.id.radioButton22:
			SharedUtil.save(RemindActivity.this, "naozhong", "2");
			CalendarUtils.update(RemindActivity.this, 60 * 24);
			break;
		case R.id.radioButton33:
			SharedUtil.save(RemindActivity.this, "naozhong", "3");
			CalendarUtils.update(RemindActivity.this, 60 * 48);
			break;
		case R.id.radioButton44:
			SharedUtil.save(RemindActivity.this, "naozhong", "4");
			CalendarUtils.update(RemindActivity.this, 0);
			break;
		}
		finish();
	}
}
