package com.xz.activeplan.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.views.CustomProgressDialog;

/**
 * 购票界面activity
 * @author johnny
 *
 */
public class TeacherZLActivity extends BaseFragmentActivity implements OnClickListener{

	private ImageView iv_datails_back;
	private TextView tvHeadTitle,tvSecondNext;
	private EditText ed_phone,ed_weixin,ed_zhiye,ed_gs ,ed_bz;
	private LinearLayout llytBz ;
	
	private UserInfoBean mUserInfoBean ;
	
	private TeacherBean mData;
	
	private CustomProgressDialog mProgressDialog ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_buy_ticket_second) ;
		initViews();
	}
	
	private void initViews() {
		mProgressDialog = new CustomProgressDialog(this) ;
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		tvSecondNext = (TextView)findViewById(R.id.tvSecondNext) ;
		
		ed_bz = (EditText)findViewById(R.id.ed_bz) ;
		llytBz = (LinearLayout)findViewById(R.id.llyt_bz);
		llytBz.setVisibility(View.VISIBLE);
		
		
		ed_phone = (EditText)findViewById(R.id.ed_phone) ;
		ed_weixin = (EditText)findViewById(R.id.ed_weixin) ;
		ed_zhiye = (EditText)findViewById(R.id.ed_zhiye) ;
		ed_gs = (EditText)findViewById(R.id.ed_gs) ;
		
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
		}else{
			Intent intent = new Intent(TeacherZLActivity.this,LoginActivity.class);
			startActivity(intent);
			return;
		}
		
		tvHeadTitle.setText("填写报名表单");
		iv_datails_back.setOnClickListener(this);
		tvSecondNext.setOnClickListener(this) ;
		
		Intent intent = getIntent() ;
		if(intent != null ){
			mData = (TeacherBean)intent.getSerializableExtra("data");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.tvSecondNext:
			if(!checkInput() ){
				Utiles.log("-----01");
				return;
			}
			inviteTeacher();
			Utiles.log("-----02");
			break;
		}
	}
	
	private boolean checkInput(){
		String phone = ed_phone.getText().toString().trim();
		String weixin = ed_weixin.getText().toString().trim();
		String zhiye = ed_zhiye.getText().toString().trim();
		String gs = ed_gs.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
//			Toast.makeText(TeacherZLActivity.this, "请输入电话号码", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入电话号码!") ;
			return false;
		}
		if(Utils.checkMobileNumber(phone)==false)
		{
			ToastUtil.showShort("请输入正确的手机号码!") ;
			return false;
		}
		/*if(!Utils.checkMobileNumber(phone)){
//			Toast.makeText(TeacherZLActivity.this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入正确的电话号码!") ;
			return false;
		}*/
		
		/*if(TextUtils.isEmpty(weixin)){
//			Toast.makeText(TeacherZLActivity.this, "请输入微信号", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入微信号码!") ;
			return false;
		}*/
		
		/*if(TextUtils.isEmpty(zhiye)){
//			Toast.makeText(TeacherZLActivity.this, "请输入职业", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入职业名称!") ;
			return false;
		}*/
		/*
		if(TextUtils.isEmpty(gs)){
//			Toast.makeText(TeacherZLActivity.this, "请输入公司", Toast.LENGTH_LONG).show();
			ToastUtil.showShort("请输入公司名称!") ;
			return false;
		}*/
		return true;
	}
	
	private void inviteTeacher() {
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
		}else{
			Intent intent = new Intent(TeacherZLActivity.this,LoginActivity.class);
			startActivity(intent);
			return;
		}
		
		if(mData == null){
			return  ;
		}
		
		String phone = ed_phone.getText().toString().trim();
		String weixin = ed_weixin.getText().toString().trim();
		String zhiye = ed_zhiye.getText().toString().trim();
		String gs = ed_gs.getText().toString().trim();
		
		mProgressDialog.show();
		/*CelebrityNewWorkImpl.getInstance().inviteCelebrity(mUserInfoBean.getUserid(), mData.getTea_id(), phone,weixin,gs,zhiye,ed_bz.getText().toString().trim(),new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						ToastUtil.showShort("邀请名师成功,我们尽快联系名师!") ;
						ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.TEACHER_INVITE_TYPE));
						finish();
					}else{
						ToastUtil.showShort("邀请名师失败，请稍后重试!") ;
					}
				}else{
					ToastUtil.showShort("邀请名师失败，请稍后重试!") ;
				}
				mProgressDialog.dismiss();
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				mProgressDialog.dismiss() ;      
				ToastUtil.showShort("邀请名师失败，请稍后重试!") ;
			}
		}) ;*/
		
	}
	
	
}
