package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;

/**
 * 反馈界面activity
 * @author johnny
 * 
 */
public class FeedBackActivity extends BaseFragmentActivity implements OnClickListener{
	private static final String TAG = FeedBackActivity.class.getSimpleName() ;
	
	private TextView mTvHeadTitle ;
	
	private Button mAddAccuvallyBtn ;
	

	private EditText edEmail,edContent ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_feedback);
		initViews() ;
	}
	
	private void initViews(){

		//返回框背景
		View viewHeald = findViewById(R.id.relativeLayout_toolbar);
		viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
		//返回键
		ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imageBlack.setOnClickListener(this);
		imageBlack.setImageResource(R.drawable.ic_nav_back_white);
		//头部字体
		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
		mTvHeadTitle.setText(getResources().getString(R.string.activity_feedback_str));
		mTvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;
		mAddAccuvallyBtn = (Button)findViewById(R.id.addAccuvallyBtn) ;

		edContent  = (EditText)findViewById(R.id.edContent) ;
		edEmail = (EditText)findViewById(R.id.edEmail) ;
		

		
		mAddAccuvallyBtn.setOnClickListener(this);

		if(SharedUtil.isLogin(this)){
			edEmail.setText("" + SharedUtil.getUserInfo(this).getPhonenum());
		}else{
			edEmail.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.addAccuvallyBtn:
			feedBack();
			break;
		case R.id.id_ImageHeadTitleBlack:
			onBackPressed();
			break; 
		}
		
	}

	private void feedBack() {
		String content = edContent.getText().toString();
		if(TextUtils.isEmpty(content)){
//			Toast.makeText(this, "请输入反馈内容", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入反馈内容!") ;
			return;
		}
		UserInfoServiceImpl.getInstance().feedback(content, edEmail.getText().toString().trim(), new StringCallback(){

			

			@Override
			public void onResponse(String response) {
				StatusJson statusJosn = new StatusJson();
				Object obj = statusJosn.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {

						ToastUtil.showShort("反馈成功!") ;
						finish();
					}else{

						ToastUtil.showShort("反馈失败!") ;
					}
				}else{

					ToastUtil.showShort("反馈失败!") ;
				}
			}
			@Override
			public void onFailure(Request request, IOException e) {
//				Toast.makeText(FeedBackActivity.this,
//						"反馈失败!", Toast.LENGTH_LONG).show();
				ToastUtil.showShort("反馈失败!") ;
			}
		});
	}
}
