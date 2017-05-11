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
import android.widget.RelativeLayout;
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

import cn.smssdk.EventHandler;

/**
 * 注册界面activity
 * 
 * @author johnny
 * 
 */
public class RegisterActivity extends BaseFragmentActivity implements
		OnClickListener ,TextChange{
	private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int YZMMSG= 0x10;
	private TextView mTvHeadTitle, mTvLoginAndReg, acpuire_verification_code;
	private RelativeLayout mLlytSave_ly;
	private Button register_submit_botton ;
	private SmsCallBack mSmsCallBack = new SmsCallBack();
	private ClearableEditText register_phone,verification_code,register_password;
	private CustomProgressDialog dialog = null;
	private int recLen = 60;
	private Timer timer = new Timer();
	private Bitmap bitmap;
	private ImageView ivYZM;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if(msg.what==YZMMSG)
			{
				ivYZM.setImageBitmap(bitmap);
			}

			/*dialog.dismiss();
			if (msg.arg2 == SMSSDK.RESULT_COMPLETE) {
				// 回调完成
				if (msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					register() ;
					// 提交验证码成功
				} else if (msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

					// 获取验证码成功
					ToastUtil.showShort("获取验证码成功!") ;
					timer = new Timer();
					recLen = 60;
					timer.schedule(new MyTimer(), 1000, 1000); // timeTask

				} else if (msg.arg1 == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// 返回支持发送验证码的国家列表
				}
			} else {
				//((Throwable) msg.obj).printStackTrace();
				if(timer != null){
					timer.cancel();
				}
				acpuire_verification_code
						.setOnClickListener(RegisterActivity.this);
				acpuire_verification_code.setText("获取验证码");
				ToastUtil.showShort("验证码错误，请重试!") ;
			}*/
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_register);

		//SMSSDK.initSDK(this, "1249232cb72cc","88cf0a9be651922ac7b38df306e99964");

		//SMSSDK.registerEventHandler(mSmsCallBack); // 注册短信回调

		dialog = new CustomProgressDialog(this);

		initViews();
	}

	/**
	 * 得到bitmap
	 */
	private void getBitmap(final String phone) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				bitmap= ImageTools.getUrlToBitmap(UrlsManager.URL_REG_YZM+"&phonenum="+phone);
				handler.sendEmptyMessage(YZMMSG);
			}
		}).start();
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
		mTvLoginAndReg.setTextColor(getResources().getColor(R.color.white));

		mLlytSave_ly = (RelativeLayout) findViewById(R.id.save_ly);
		acpuire_verification_code = (TextView) findViewById(R.id.acpuire_verification_code);
		register_phone = (ClearableEditText) findViewById(R.id.register_phone);
		verification_code =(ClearableEditText)findViewById(R.id.verification_code) ;
		register_submit_botton = (Button)findViewById(R.id.register_submit_botton) ;
		register_password = (ClearableEditText)findViewById(R.id.register_password) ;

		mLlytSave_ly.setVisibility(View.VISIBLE);

		mTvHeadTitle.setText(getString(R.string.register_user));
		mTvLoginAndReg
				.setText(getString(R.string.activity_right_rigister_str2));

		mTvLoginAndReg.setOnClickListener(this);
		acpuire_verification_code.setOnClickListener(this);
		register_submit_botton.setOnClickListener(this) ;


		register_phone.setTextChange(this);
		verification_code.setTextChange(this);
		register_password.setTextChange(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.acpuire_verification_code: //获取验证码
			getVerificationCode();
			break;
		case R.id.register_submit_botton:  //注册
			submitVerificationCode() ;
//			register();
			break;
		}

	}

	/**
	 * 提交短信验证码，在监听中返回
	 */
	private void submitVerificationCode(){
		String code = verification_code.getText().toString().trim() ;
		String phone = register_phone.getText().toString().trim();

		String password = register_password.getText().toString().trim() ;

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShort("请输入电话号码!") ;
			return;
		}

		if(!Utils.checkMobileNumber(phone)){
			ToastUtil.showShort("请输入正确的电话号码!") ;
			return;
		}

		if(TextUtils.isEmpty(code)){
			ToastUtil.showShort("请输入验证码!") ;
			return;
		}

		if(TextUtils.isEmpty(password)){
			ToastUtil.showShort("请输入密码!") ;
			return;
		}

		if(password.length() < 6){
			ToastUtil.showShort("请输入6位以上的密码!") ;
			return;
		}

		register();
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

		//acpuire_verification_code.setOnClickListener(null);
        showRandomDialog(phone);  //显示dialog
	}

	/**
	 * 注册
	 */
	private void register(){
		String name = register_phone.getText().toString().trim() ;
		String password = register_password.getText().toString().trim() ;
		String code = verification_code.getText().toString().trim() ;
		
		LoginServiceImpl.getInstance().register(name, password,code,new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				//Log.e(TAG, "onResponse = " + response) ;
				
				StatusJson statusJson = new StatusJson() ;
				StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(statusBean != null && statusBean.getCode()== 0){
//					Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_LONG).show() ;
					ToastUtil.showShort("注册成功!") ;
					finish() ;
				}else if(statusBean != null && statusBean.getCode()== 3){
					ToastUtil.showShort("验证码错误，请输入正确的验证码!") ;
				}else{
					ToastUtil.showShort("注册失败，请重试!") ;
				}
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				Log.e(TAG, "onFailure = " + e.getMessage()) ;
				ToastUtil.showShort("注册失败，请重试!!") ;
			}
		}) ;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//SMSSDK.unregisterAllEventHandler();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onTextChange() {
		String code = verification_code.getText().toString().trim() ;
		String phone = register_phone.getText().toString().trim();
		String password = register_password.getText().toString().trim() ;
		if(!TextUtils.isEmpty(code) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)){
			register_submit_botton.setBackgroundResource(R.drawable.selector_click_btn2);
			register_submit_botton.setEnabled(true);
		}else{
			register_submit_botton.setBackgroundResource(R.drawable.selector_wane_gray2);
			register_submit_botton.setEnabled(false);
		}

		if(!TextUtils.isEmpty(phone)){
			acpuire_verification_code.setBackgroundResource(R.drawable.selector_click_btn2);
		}else{
			acpuire_verification_code.setBackgroundResource(R.drawable.selector_wane_gray2);
		}

	}

	/**
	 * 弹出显示随机验证码的dialog
	 */
	private void showRandomDialog(final String phone)
	{
		getBitmap(phone);
		View view = LayoutInflater.from(activity).inflate(R.layout.dialog_random_code, null);
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
		ivYZM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                 getBitmap(phone);
			}
		});
		dialog.setPositiveButton("确认", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String trim = etYZM.getText().toString().trim();
				if(trim.length()==0)
				{
					ToastUtil.showCenterToast(RegisterActivity.this,"验证码不能为空!");
					return;
				}
				else
				{
					getYZM(phone,trim);  //获取验证码
				}
				dialog.cancel();
			}
		});
		dialog.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	/**
	 * 获取验证码
	 * @param phone 手机号
     */
	private void getYZM(String phone,String randomCode) {

		LoginServiceImpl.getInstance().getCode(phone,"3", randomCode,new StringCallback() {

            @Override
            public void onResponse(String response) {
				Utiles.log("验证码回调： " + response);
				//RegisterActivity.this.dialog.dismiss();
				StatusJson statusJson = new StatusJson();
				StatusBean statusBean = (StatusBean) statusJson.analysisJson2Object(response);
				if (statusBean != null)   //
				{
					if(statusBean.getCode()==0)  //成功
					{
						ToastUtil.showShort("获取验证码成功!");
						timer = new Timer();
						recLen = 60;
						timer.schedule(new MyTimer(), 1000, 1000); // timeTask
					}else    //该号码已经注册过了
					{
						ToastUtil.showCenterToast(RegisterActivity.this,"获取验证码失败，请重试!");
					//	ToastUtil.showCenterToast(RegisterActivity.this,"该号码已经注册了！");
					}
				}else
				{
					ToastUtil.showCenterToast(RegisterActivity.this,"获取验证码失败，请重试!");
				}
            }

            @Override
            public void onFailure(Request request, IOException e) {
               // RegisterActivity.this.dialog.dismiss();
                if(timer != null){
                    timer.cancel();
                }
                acpuire_verification_code
                        .setOnClickListener(RegisterActivity.this);
                acpuire_verification_code.setText("获取验证码");
                ToastUtil.showShort("获取验证码失败，请重试!") ;
            }
        });
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

	//倒计时
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
								.setOnClickListener(RegisterActivity.this);
						acpuire_verification_code.setText("获取验证码");
					}
				}
			});
		}
	}
}
