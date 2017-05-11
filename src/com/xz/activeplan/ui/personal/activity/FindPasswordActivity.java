package com.xz.activeplan.ui.personal.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.xz.activeplan.network.personal.impl.LoginServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.ImageTools;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.views.ClearableEditText;
import com.xz.activeplan.views.ClearableEditText.TextChange;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 找回密码界面activiyt
 * 
 * @author johnny
 * 
 */
public class FindPasswordActivity extends BaseFragmentActivity implements
		OnClickListener  ,TextChange{
	private static final String TAG = FindPasswordActivity.class
			.getSimpleName();
	private static final int UPDATEPASS = 0x120;
	private TextView mTvHeadTitle;


	private TextView acpuire_verification_code;

	private ClearableEditText register_phone, verification_code,
			find_password_password, again_submit_password;

	private Button find_password_submit_button;

	//private SmsCallBack mSmsCallBack = new SmsCallBack();

	private CustomProgressDialog dialog = null;

	private int recLen = 60;
	private Timer timer = new Timer();
	private Bitmap bitmap;
	private ImageView ivYZM;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			//dialog.dismiss();
			if(msg.what==UPDATEPASS)
			{
				ivYZM.setImageBitmap(bitmap);
			}
/*			if (msg.arg2 == SMSSDK.RESULT_COMPLETE) {
				// 回调完成
				if (msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

					updatePwd();

					Log.e(TAG, "提交验证码成功  evnet = " + msg.arg1 + "    result = "
							+ msg.arg2 + "   obj = " + msg.obj);

					// 提交验证码成功
//					Toast.makeText(
//							FindPasswordActivity.this,
//							"提交验证码成功  evnet = " + msg.arg1 + "    result = "
//									+ msg.arg2 + "   obj = " + msg.obj,
//							Toast.LENGTH_LONG).show();
				} else if (msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Log.e(TAG, "获取验证码成功  evnet = " + msg.arg1 + "    result = "
							+ msg.arg2 + "   obj = " + msg.obj);
					// 获取验证码成功
//					Toast.makeText(FindPasswordActivity.this, "获取验证码获取成功 ",
//							Toast.LENGTH_LONG).show();
					ToastUtil.showShort("获取验证码获取成功") ;
					if (timer != null) {
						timer.cancel();
					}
					timer = new Timer();
					recLen = 60;
					timer.schedule(new MyTimer(), 1000, 1000); // timeTask

				} else if (msg.arg1 == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// 返回支持发送验证码的国家列表
				}
			} else {
				((Throwable) msg.obj).printStackTrace();
				if (timer != null) {
					timer.cancel();
				}
				acpuire_verification_code
						.setOnClickListener(FindPasswordActivity.this);
				acpuire_verification_code.setText("获取验证码");
//				Toast.makeText(FindPasswordActivity.this, "验证码错误，请重试!",
//						Toast.LENGTH_LONG).show();
				ToastUtil.showShort("验证码错误，请重试") ;
			}*/
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_find_password);

		/*SMSSDK.initSDK(this, "1249232cb72cc",
				"88cf0a9be651922ac7b38df306e99964");

		SMSSDK.registerEventHandler(mSmsCallBack); // 注册短信回调*/

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


		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		acpuire_verification_code = (TextView) findViewById(R.id.acpuire_verification_code);
		register_phone = (ClearableEditText) findViewById(R.id.register_phone);
		verification_code = (ClearableEditText) findViewById(R.id.verification_code);
		find_password_password = (ClearableEditText) findViewById(R.id.find_password_password);
		again_submit_password = (ClearableEditText) findViewById(R.id.again_submit_password);

		find_password_submit_button = (Button) findViewById(R.id.find_password_submit_button);

		if(!TextUtils.isEmpty(getIntent().getStringExtra("data")) && getIntent().getStringExtra("data").equals("update"))
		{
			mTvHeadTitle.setText("修改密码");
		}else
		{
			mTvHeadTitle.setText(getString(R.string.activity_find_password_head_title));
		}


		find_password_submit_button.setOnClickListener(this);
		acpuire_verification_code.setOnClickListener(this);


		 register_phone.setTextChange(this);
		 verification_code.setTextChange(this);
		 find_password_password.setTextChange(this);
		 again_submit_password.setTextChange(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.acpuire_verification_code:
			getVerificationCode();
			break;
		case R.id.find_password_submit_button:
			submitVerificationCode();
			break;
		}
	}

	/**
	 * 提交短信验证码，在监听中返回
	 */
	private void submitVerificationCode() {
		String code = verification_code.getText().toString().trim();
		String phone = register_phone.getText().toString().trim();

		String password1 = find_password_password.getText().toString().trim();
		String password2 = again_submit_password.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShort("请输入电话号码!") ;
			return;
		}

		if(!Utils.checkMobileNumber(phone)){
			ToastUtil.showShort("请输入正确的电话号码!") ;
			return;
		}

		if (TextUtils.isEmpty(code)) {
			ToastUtil.showShort("请输入验证码!") ;
			return;
		}

		if (TextUtils.isEmpty(password1)) {
			ToastUtil.showShort("请输入密码!") ;
			return;
		}

		if (password1.length() < 6) {
			ToastUtil.showShort("请输入6位以上的密码!") ;
			return;
		}

		if (!password1.equals(password2)) {
			ToastUtil.showShort("两次密码输入不一样!") ;
			return;
		}

		//SMSSDK.submitVerificationCode("+86", phone, code);

		updatePwd();

	}

	/**
	 * 获取验证码
	 */
	private void getVerificationCode() {
		String phone = register_phone.getText().toString().trim();


		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShort("请输入电话号码!") ;
			return;
		}

		if(!Utils.checkMobileNumber(phone)){
			ToastUtil.showShort("请输入正确的电话号码!") ;
			return;
		}
		showRandomDialog(phone);
	}

	/**
	 * 得到bitmap
	 */
	private void getBitmap(final String phone) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				bitmap= ImageTools.getUrlToBitmap(UrlsManager.URL_REG_YZM+"&phonenum="+phone);
				handler.sendEmptyMessage(UPDATEPASS);
			}
		}).start();
	}
	/**
	 * 显示随机安全码的 Dialog
	 * @param phone 手机号
	 */
	private void showRandomDialog(final String phone)
	{
		getBitmap(phone);//得到随机码
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_random_code, null);
		final AlertDialog dialog = new AlertDialog(this, view,0.7f).builder();
		dialog.setCancelable(false);
		dialog.setTitle("请输入图片中的内容");
		view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
		view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
		view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
		((TextView) view.findViewById(R.id.txt_msg)).setText("安全验证,点击图片换一张");
		final EditText etYZM= (EditText)view.findViewById(R.id.random_etYZM);
		ivYZM= (ImageView) view.findViewById(R.id.random_ivYZM);
		ivYZM.setImageBitmap(bitmap);
		ivYZM.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getBitmap(phone);//每次点击刷新随机码
			}
		});
		dialog.setPositiveButton("确认", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String trim = etYZM.getText().toString().trim();
				if(trim.length()==0)
				{
					ToastUtil.showCenterToast(FindPasswordActivity.this,"验证码不能为空!");
					return;
				}
				else
				{
					getYZM(phone,trim);  //获取验证码
				}
				dialog.cancel();
			}
		});
		dialog.setNegativeButton("取消", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	private void getYZM(String phone, String randomCode) {

		LoginServiceImpl.getInstance().getCode(phone,"2", randomCode,new StringCallback() {

			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson() ;
				StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(statusBean != null && statusBean.getCode()== 0){
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
				if(timer != null){
					timer.cancel();
				}
				acpuire_verification_code
						.setOnClickListener(FindPasswordActivity.this);
				acpuire_verification_code.setText("获取验证码");
//				Toast.makeText(FindPasswordActivity.this, "获取验证码失败，请重试!", Toast.LENGTH_LONG).show() ;
				ToastUtil.showShort("获取验证码失败，请重试!") ;
			}
		});
	}

	private void updatePwd() {

		String phone = register_phone.getText().toString().trim();

		String password1 = find_password_password.getText().toString().trim();
		
		String code = verification_code.getText().toString().trim();

		LoginServiceImpl.getInstance().forgetPwd(phone, password1,code,
				new StringCallback() {

					@Override
					public void onResponse(String response) {
						//Log.e(TAG, "onResponse = " + response);
						StatusJson statusJson = new StatusJson() ;
						StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
						if(statusBean != null && statusBean.getCode()== 0){
//							Toast.makeText(FindPasswordActivity.this, "密码修改成功!", Toast.LENGTH_LONG).show() ;
							ToastUtil.showShort("密码修改成功") ;
							finish() ;
						}else if(statusBean != null && statusBean.getCode()== 3){
							ToastUtil.showShort("验证码错误，请输入正确的验证码") ;
						}else{
							ToastUtil.showShort("密码修改失败，请重试!") ;
						}
					}

					@Override
					public void onFailure(Request request, IOException e) {
						Log.e(TAG, "onFailure = " + e.getMessage());
						ToastUtil.showShort("密码修改失败，请重试!") ;
					}
				});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	//	SMSSDK.unregisterEventHandler(mSmsCallBack);
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onTextChange() {
		String code = verification_code.getText().toString().trim();
		String phone = register_phone.getText().toString().trim();

		String password1 = find_password_password.getText().toString().trim();
		String password2 = again_submit_password.getText().toString().trim();
		if(!TextUtils.isEmpty(code) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)){
			find_password_submit_button.setBackgroundResource(R.drawable.selector_click_btn2);
			find_password_submit_button.setEnabled(true);
		}else{
			find_password_submit_button.setBackgroundResource(R.drawable.selector_wane_gray2);
			find_password_submit_button.setEnabled(false);
		}

		if(!TextUtils.isEmpty(phone)){
			acpuire_verification_code.setBackgroundResource(R.drawable.selector_click_btn2);
		}else{
			acpuire_verification_code.setBackgroundResource(R.drawable.selector_wane_gray2);
		}

	}
/*
	class SmsCallBack extends EventHandler {
		@Override
		public void afterEvent(int event, int result, Object data) {

			Message msg = Message.obtain();
			msg.arg1 = event;
			msg.arg2 = result;
			msg.obj = data;
			handler.sendMessage(msg);

		}
	}*/
	
	class MyTimer extends TimerTask{

		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					acpuire_verification_code.setText("" + recLen + "s");
					if (recLen <= 0) {
						timer.cancel();
						acpuire_verification_code
								.setOnClickListener(FindPasswordActivity.this);
						acpuire_verification_code.setText("获取验证码");
					}
				}
			});
		}

	}

}
