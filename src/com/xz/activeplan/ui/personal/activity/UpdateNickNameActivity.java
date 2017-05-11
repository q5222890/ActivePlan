package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.ClearableEditText;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;

/**
 * 更改昵称
 * 
 * @author johnny
 * 
 */
public class UpdateNickNameActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageView iv_datails_back;

	private Button updateBtn;

	private TextView tvPrompts, tvHeadTitle;

	private ClearableEditText edUpdateContent;

	private UserInfoBean mUserInfoBean;
	
	private CustomProgressDialog mProgressDialog ;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_update_data);

		initViews();

	}

	private void initViews() {
		mProgressDialog = new CustomProgressDialog(this) ;
		//返回框背景
		View viewHeald = findViewById(R.id.relativeLayout_toolbar);
		viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
		//返回键
		ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imageBlack.setOnClickListener(this);
		//头部字体
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		tvHeadTitle.setTextColor(getResources().getColor(R.color.white));
		tvPrompts = (TextView) findViewById(R.id.tvPrompts);

		updateBtn = (Button) findViewById(R.id.updateBtn);

		edUpdateContent = (ClearableEditText) findViewById(R.id.edUpdateContent);

		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);

		tvHeadTitle.setText("更改昵称");
		tvPrompts.setText("好名字让你的人气更高");
		edUpdateContent.setHint("请输入昵称");

		iv_datails_back.setOnClickListener(this);
		updateBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.updateBtn:
			updateData();
			break;
		}
	}

	private void updateData() {
		if (SharedUtil.isLogin(this)) {
			mUserInfoBean = SharedUtil.getUserInfo(this);
		}

		if (mUserInfoBean == null) {
//			Toast.makeText(this, "请登录!", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请登录!") ;
			return;
		}

		final String content = edUpdateContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
//			Toast.makeText(this, "请输入更改内容", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入更改内容!") ;
			return;
		}
		
		if(!NetworkInfoUtil.checkNetwork(this)){
//			Toast.makeText(this, "网络无连接，请检查网络", Toast.LENGTH_LONG).show() ;
			ToastUtil.showShort("网络无连接，请检查网络!") ;
		}
		mProgressDialog.show();
		UserInfoServiceImpl.getInstance().saveUserInfo(
				mUserInfoBean.getUserid(), content, mUserInfoBean.getCity(),
				mUserInfoBean.getRealname(), mUserInfoBean.getSignature(),
				mUserInfoBean.getPhonenum(), mUserInfoBean.getSex()+"", "",mUserInfoBean.getAlipayaccount(),mUserInfoBean.getAlipayusername(),
				new StringCallback() {

					@Override
					public void onResponse(String response) {
						mProgressDialog.dismiss();
						StatusJson statusJosn = new StatusJson();
						Object obj = statusJosn.analysisJson2Object(response);
						if (obj != null) {
							StatusBean statusBean = (StatusBean) obj;
							if (statusBean.getCode() == 0) {
//								Toast.makeText(UpdateNickNameActivity.this,
//										"更改成功!", Toast.LENGTH_LONG).show();
								ToastUtil.showShort("更改成功!") ;
								edUpdateContent.setText("") ;
								mUserInfoBean.setUsername(content);
								SharedUtil.saveUserInfo(UpdateNickNameActivity.this, mUserInfoBean);
								ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.LOGIN_NOTIFI_TYPE)) ;
								finish();
							}
						}
					}

					@Override
					public void onFailure(Request request, IOException e) {
						mProgressDialog.dismiss();
//						Toast.makeText(UpdateNickNameActivity.this,
//								"更改失败，请稍后重试!", Toast.LENGTH_LONG).show();
						ToastUtil.showShort("更改失败，请稍后重试!") ;
					}
				});
	}
}
