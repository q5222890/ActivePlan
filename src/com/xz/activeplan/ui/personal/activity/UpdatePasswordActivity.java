package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.LoginServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.views.ClearableEditText;
import com.xz.activeplan.views.ClearableEditText.TextChange;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 修改密码界面activity
 * @author johnny
 *
 */
public class UpdatePasswordActivity extends BaseFragmentActivity implements OnClickListener ,TextChange{
	private String TAG = UpdatePasswordActivity.class.getSimpleName() ;
	private TextView tvHeadTitle ;
	
	private ClearableEditText edPhone,edCodeNumber,edOldPass,edNewPass;
	private TextView sendCodeTV;
	private Button updatePassBtn ;
	
	private SmsCallBack mSmsCallBack = new SmsCallBack();
	private CustomProgressDialog dialog = null;

	private int recLen = 60;
	private Timer timer = new Timer();
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();

			if (msg.arg2 == SMSSDK.RESULT_COMPLETE) {
				// 回调完成
				if (msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					updatePwd() ;


					Log.e(TAG, "提交验证码成功  evnet = " + msg.arg1 + "    result = "
							+ msg.arg2 + "   obj = " + msg.obj) ;

					// 提交验证码成功
//					Toast.makeText(
//							UpdatePasswordActivity.this,
//							"提交验证码成功  evnet = " + msg.arg1 + "    result = "
//									+ msg.arg2 + "   obj = " + msg.obj,
//							Toast.LENGTH_LONG).show();
				} else if (msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Log.e(TAG, "获取验证码成功  evnet = " + msg.arg1 + "    result = "
									+ msg.arg2 + "   obj = " + msg.obj) ;
					// 获取验证码成功
//					Toast.makeText(
//							UpdatePasswordActivity.this,
//							"获取验证码获取成功 ",
//							Toast.LENGTH_LONG).show();
					ToastUtil.showShort("获取验证码获取成功!") ;
					timer = new Timer();
					recLen = 60;
					timer.schedule(new MyTimer(), 1000, 1000); // timeTask

				} else if (msg.arg1 == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// 返回支持发送验证码的国家列表
				}
			} else {
				((Throwable) msg.obj).printStackTrace();
				Log.e(TAG, "" + msg.obj) ;
				if(timer != null){
					timer.cancel();
				}
				sendCodeTV
						.setOnClickListener(UpdatePasswordActivity.this);
				sendCodeTV.setText("获取验证码");
//				Toast.makeText(UpdatePasswordActivity.this, "错误，请重试!", Toast.LENGTH_LONG).show() ;
				ToastUtil.showShort("错误，请重试!") ;
			}
		}
	};
	 
	 @Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		 Utiles.setStatusBar(this);
		setContentView(R.layout.activity_update_password) ;

		SMSSDK.initSDK(this, "1249232cb72cc",
				"88cf0a9be651922ac7b38df306e99964");

		SMSSDK.registerEventHandler(mSmsCallBack); // 注册短信回调

		dialog = new CustomProgressDialog(this);

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
		 tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		 tvHeadTitle.setTextColor(getResources().getColor(R.color.white));

		 edPhone = (ClearableEditText)findViewById(R.id.edPhone) ;
		 edCodeNumber = (ClearableEditText)findViewById(R.id.edCodeNumber) ;
		 edNewPass = (ClearableEditText)findViewById(R.id.edNewPass) ;
		 edOldPass = (ClearableEditText)findViewById(R.id.edOldPass) ;
		 sendCodeTV = (TextView)findViewById(R.id.sendCodeTV) ;
		 updatePassBtn = (Button)findViewById(R.id.updatePassBtn) ;


		 tvHeadTitle.setText("修改密码");

		 updatePassBtn.setOnClickListener(this) ;
		 sendCodeTV.setOnClickListener(this) ;

		 edPhone.setTextChange(this);
		 edCodeNumber.setTextChange(this);
		 edOldPass.setTextChange(this);
		 edNewPass.setTextChange(this);
	 }
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.updatePassBtn:
			submitVerificationCode() ;

			break;
		case R.id.sendCodeTV:
			getVerificationCode() ;
			break;
		}

	}
	
	/**
	 * 提交短信验证码，在监听中返回
	 */
	private void submitVerificationCode(){
		String code = edCodeNumber.getText().toString().trim() ;
		String phone = edPhone.getText().toString().trim();

		String oldPwd = edOldPass.getText().toString().trim() ;
		String newPwd = edNewPass.getText().toString().trim() ;

		if(!NetworkInfoUtil.checkNetwork(this)){
			ToastUtil.showShort("网络无连接，请检查网络") ;
		}

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShort("请输入电话号码") ;
			return;
		}

		if(!Utils.checkMobileNumber(phone)){
			ToastUtil.showShort("请输入正确的电话号码") ;
			return;
		}

		if(TextUtils.isEmpty(code)){
			ToastUtil.showShort("请输入验证码") ;
			return;
		}

		if(TextUtils.isEmpty(oldPwd)){
			ToastUtil.showShort("请输入旧密码") ;
			return;
		}

		if(newPwd.length() < 6){
			ToastUtil.showShort("请输入6位以上的旧密码") ;
			return;
		}

		if(TextUtils.isEmpty(newPwd)){
			ToastUtil.showShort("请输入新密码") ;
			return;
		}

		if(newPwd.length() < 6){
			ToastUtil.showShort("请输入6位以上的新密码") ;
			return;
		}

		updatePwd() ;

	}
	
	/**
	 * 获取验证码
	 */
	private void getVerificationCode() {
		String phone = edPhone.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShort("请输入电话号码") ;
			return;
		}

		if(!Utils.checkMobileNumber(phone)){
			ToastUtil.showShort("请输入正确的电话号码") ;
			return;
		}

		sendCodeTV.setOnClickListener(null);

		dialog.show();

		LoginServiceImpl.getInstance().getCode(phone,"2","randomCode", new StringCallback() {

			@Override
			public void onResponse(String response) {
				dialog.dismiss();
				StatusJson statusJson = new StatusJson() ;
				StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(statusBean != null && statusBean.getCode()== 0){
//					Toast.makeText(UpdatePasswordActivity.this, "获取验证码成功!", Toast.LENGTH_LONG).show() ;
					ToastUtil.showShort("获取验证码成功!") ;
					timer = new Timer();
					recLen = 60;
					timer.schedule(new MyTimer(), 1000, 1000); // timeTask
				}else{
					ToastUtil.showShort("获取验证码失败，请重试!") ;
				}
			}

			@Override
			public void onFailure(Request request, IOException e) {
				dialog.dismiss();
				if(timer != null){
					timer.cancel();
				}
				sendCodeTV
						.setOnClickListener(UpdatePasswordActivity.this);
				sendCodeTV.setText("获取验证码");
				ToastUtil.showShort("获取验证码失败，请重试!") ;
			}
		});

	}
	
	private void updatePwd(){
		String userName = edPhone.getText().toString().trim() ;
		String oldPwd = edOldPass.getText().toString().trim() ;
		String newPwd = edNewPass.getText().toString().trim() ;
		String code = edCodeNumber.getText().toString().trim() ;

		LoginServiceImpl.getInstance().modifyPwd(userName, oldPwd, newPwd,code, new StringCallback() {

			@Override
			public void onResponse(String response) {
				//Log.e(TAG, "onResponse = " + response) ;
				StatusJson statusJson = new StatusJson() ;
				StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(statusBean != null && statusBean.getCode()== 0){
//					Toast.makeText(UpdatePasswordActivity.this, "密码修改成功!", Toast.LENGTH_LONG).show() ;
					ToastUtil.showShort("密码修改成功!") ;
					finish() ;
				}else if(statusBean != null && statusBean.getCode()== 3){
					ToastUtil.showShort("验证码错误，请输入正确的验证码!") ;
				}else{
					ToastUtil.showShort("密码修改失败，请重试!") ;
				}

			}

			@Override
			public void onFailure(Request request, IOException e) {
				Log.e(TAG, "onFailure = " + e.getMessage()) ;
				ToastUtil.showShort("密码修改失败，请重试!") ;
			}
		}) ;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterEventHandler(mSmsCallBack);
		if (timer != null) {
			timer.cancel();
		}
	}
	
	@Override
	public void onTextChange() {
		String userName = edPhone.getText().toString().trim() ;
		String oldPwd = edOldPass.getText().toString().trim() ;
		String newPwd = edNewPass.getText().toString().trim() ;
		String phone = edPhone.getText().toString().trim();
		if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(oldPwd) && !TextUtils.isEmpty(newPwd)  && !TextUtils.isEmpty(phone) ){
			updatePassBtn.setBackgroundResource(R.drawable.selector_click_btn2);
			updatePassBtn.setEnabled(true);
		}else{
			updatePassBtn.setBackgroundResource(R.drawable.selector_wane_gray2);
			updatePassBtn.setEnabled(false);
		}

		if(!TextUtils.isEmpty(phone)){
			sendCodeTV.setBackgroundResource(R.drawable.selector_click_btn2);
		}else{
			sendCodeTV.setBackgroundResource(R.drawable.selector_wane_gray2);
		}

	}
	
	class SmsCallBack extends EventHandler {
		@Override
		public void afterEvent(int event, int result, Object data) {

			Message msg = Message.obtain();
			msg.arg1 = event;
			msg.arg2 = result;
			msg.obj = data;
			handler.sendMessage(msg);

		}
	}
	
	class MyTimer extends TimerTask{

		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					sendCodeTV.setText("" + recLen + "s");
					if (recLen <= 0) {
						timer.cancel();
						sendCodeTV
								.setOnClickListener(UpdatePasswordActivity.this);
						sendCodeTV.setText("获取验证码");
					}
				}
			});
		}

	}
}
