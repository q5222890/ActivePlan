package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.AccountBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
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

/**
 * 账号余额提现界面activity
 * 
 * @author johnny
 * 
 */
public class MoneyTiXinActivity extends BaseFragmentActivity implements
		OnClickListener, TextWatcher,ClassObserver {

	private TextView mTvHeadTitle, tv_money,tv_account;


	private EditText edt_money;

	private Button btnTiXian;
	
//	private LinearLayout llytAccount;

	private CustomProgressDialog dialog = null;

	private UserInfoBean mUserInfoBean;

	private AccountBean mAccountBean;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_tixin);
		ClassConcrete.getInstance().addObserver(this) ;
		dialog = new CustomProgressDialog(this);
		Intent intent = getIntent();
		if (intent != null) {
			mAccountBean = (AccountBean) intent.getSerializableExtra("data");
		}

		initViews();
	}

	private void initViews() {
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

		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_account = (TextView)findViewById(R.id.tv_account) ;
		edt_money = (EditText) findViewById(R.id.edt_money);

		btnTiXian = (Button) findViewById(R.id.btnTiXian);
		
//		llytAccount = (LinearLayout)findViewById(R.id.llyt_account) ;

		edt_money.addTextChangedListener(this);
		btnTiXian.setOnClickListener(this);
//		llytAccount.setOnClickListener(this);

		mTvHeadTitle.setText("余额提现");


		if (SharedUtil.isLogin(this)) {
			mUserInfoBean = SharedUtil.getUserInfo(this);
			tv_account.setText(mUserInfoBean.getAlipayaccount());
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void init() {
		String cuttedStr = edt_money.getText().toString().trim();
		if(TextUtils.isEmpty(cuttedStr)){
			ToastUtil.showShort("请输入提醒金额!") ;
			return;
		}
		try {
			double m = Double.parseDouble(cuttedStr);
			if (m == 0) {
				return;
			}

			if (m > mAccountBean.getTotal()) {
//				Toast.makeText(this, "输入的金额超过账户余额", Toast.LENGTH_LONG).show();
				ToastUtil.showShort("输入的金额超过账户余额!") ;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		dialog.show();
		UserInfoServiceImpl.getInstance().adddetail(
				mUserInfoBean.getUserid() + "",
				mUserInfoBean.getPhonenum() + "",
				Double.parseDouble(cuttedStr), 3, "账号余额提现",
				new StringCallback() {
					@Override
					public void onResponse(String response) {
						StatusJson statusJson = new StatusJson();
						Object obj = statusJson.analysisJson2Object(response);
						if (obj != null) {
							StatusBean statusBean = (StatusBean) obj;
							if (statusBean.getCode() == 0) {
								ToastUtil.showShort("账号余额提现成功，3个工作日内处理!") ;
								ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.ACCOUNT_TIXIANE_TYPE)) ;
								finish();
							} else {
								ToastUtil.showShort("账号余额提现失败，请退出重试!") ;
							}
						} else {
							ToastUtil.showShort("账号余额提现失败，请退出重试!") ;
						}
						dialog.dismiss();
					}

				@Override
					public void onFailure(Request request, IOException e) {
						dialog.dismiss();
						ToastUtil.showShort("账号余额提现失败，请退出重试!") ;
					}

				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.btnTiXian:
			init();
			break;
//		case R.id.llyt_account:
//			Intent intent = new Intent(MoneyTiXinActivity.this, UpdateAlipayAccountActivity.class);
//			startActivity(intent);
//			break;

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ClassConcrete.getInstance().removeObserver(this) ;
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (s.toString().contains(".")) {
			if (s.length() - 1 - s.toString().indexOf(".") > 2) {
				s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
				edt_money.setText(s);
				edt_money.setSelection(s.length());
			}
		}
		if (s.toString().trim().substring(0).equals(".")) {
			s = "0" + s;
			edt_money.setText(s);
			edt_money.setSelection(2);
		}

		if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
			if (!s.toString().substring(1, 2).equals(".")) {
				edt_money.setText(s.subSequence(0, 1));
				edt_money.setSelection(1);
				return;
			}
		}
		try {
			String str = edt_money.getText().toString().trim();
			if(TextUtils.isEmpty(str)){
				tv_money.setText("账户余额:" + mAccountBean.getTotal());
				return;
			}
			double m = Double.parseDouble(str);
			if (m > mAccountBean.getTotal()) {
				tv_money.setText("输入的金额超过账户余额");
			} else {
				tv_money.setText("账户余额:" + mAccountBean.getTotal());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onDataUpdate(Object data) {
		
		EventBean event = (EventBean)data ;
		if(event.getType() == EventType.ACCOUNT_CHANGE_TYPE){
			if (SharedUtil.isLogin(this)) {
				mUserInfoBean = SharedUtil.getUserInfo(this);
				tv_account.setText(mUserInfoBean.getAlipayaccount());
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
		return false;
	}
	
}
