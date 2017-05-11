package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.UserInfoJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.ui.personal.adapter.ChartUserInfoAdapter;
import com.xz.activeplan.utils.CalendarUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ScrollGridView;

import java.io.IOException;
import java.util.ArrayList;

import io.rong.ApiHttpClient;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.UserInfo;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;

/**
 * 付款成功界面activity 
 * @author johnny
 *
 */
public class PaySuccess2Activity extends BaseFragmentActivity implements OnClickListener{
	
	private static final String TAG = PaySuccess2Activity.class.getSimpleName() ;
	private ImageView iv_datails_back;
	private TextView tvHeadTitle;
	
	private TextView accu_title,accu_time,accu_place,accu_ticket_price,accu_ticket_type ;
	
	private Button circle_enter,circle_enter_attion;
	
	private ChartUserInfoAdapter mChartUserInfoAdapter;
	private ScrollGridView mScrollGridView;
	
	
	private ActiveinfosBean mActiveinfosBean ;
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 1 && msg.arg1 == 200){
				RongIM.getInstance().startGroupChat(PaySuccess2Activity.this, mActiveinfosBean.getActiveid()+"",mActiveinfosBean.getName());
				PaySuccess2Activity.this.finish();
			}
		}
	};
	private TicketAddBean bean;
	private UserInfoBean mUserInfoBean ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_pay_success2) ;
		initViews();
	}
	
	private void initViews() {
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);

		accu_title = (TextView) findViewById(R.id.accu_title);
		accu_time = (TextView) findViewById(R.id.accu_time);
		accu_place = (TextView) findViewById(R.id.accu_place);
		accu_ticket_price = (TextView) findViewById(R.id.accu_ticket_price);
		accu_ticket_type = (TextView) findViewById(R.id.accu_ticket_type);

		mScrollGridView = (ScrollGridView)findViewById(R.id.grid) ;

		circle_enter = (Button)findViewById(R.id.circle_enter) ;
		circle_enter_attion = (Button)findViewById(R.id.circle_enter_attion) ;

		circle_enter.setOnClickListener(this);
		circle_enter_attion.setOnClickListener(this);

//		mChartInfoAdapter = new ChartInfoAdapter(this);
//		gridView.setAdapter(mChartInfoAdapter) ;


		tvHeadTitle.setText("报名成功");
		iv_datails_back.setOnClickListener(this);

		Intent intent = getIntent() ;
		if(intent != null ){
			mActiveinfosBean = (ActiveinfosBean)intent.getSerializableExtra("data");
			bean = (TicketAddBean)intent.getSerializableExtra("TicketAddBean") ;
			fillData();

			connectChart(2);
			//initChart();
			/**
			 * 设置行程闹钟
			 */
			String type = SharedUtil.get(PaySuccess2Activity.this, "naozhong");
			if("1".equals(type)){
				CalendarUtils.insert(PaySuccess2Activity.this, mActiveinfosBean.getName(), mActiveinfosBean.getAddress(), mActiveinfosBean.getStartdate()*1000, mActiveinfosBean.getEnddate()*1000, 60);
			}else if("2".equals(type)){
				CalendarUtils.insert(PaySuccess2Activity.this, mActiveinfosBean.getName(), mActiveinfosBean.getAddress(), mActiveinfosBean.getStartdate()*1000, mActiveinfosBean.getEnddate()*1000, 60 * 24);
			}else if("3".equals(type)){
				CalendarUtils.insert(PaySuccess2Activity.this, mActiveinfosBean.getName(), mActiveinfosBean.getAddress(), mActiveinfosBean.getStartdate()*1000, mActiveinfosBean.getEnddate()*1000, 60 * 48);
			}else if("4".equals(type)){
				CalendarUtils.insert(PaySuccess2Activity.this, mActiveinfosBean.getName(), mActiveinfosBean.getAddress(), mActiveinfosBean.getStartdate()*1000, mActiveinfosBean.getEnddate()*1000, 0);
			}else{
				CalendarUtils.insert(PaySuccess2Activity.this, mActiveinfosBean.getName(), mActiveinfosBean.getAddress(), mActiveinfosBean.getStartdate()*1000, mActiveinfosBean.getEnddate()*1000, 60);
			}
		}
	}
	
	private void fillData(){
		if(mActiveinfosBean == null){
			return;
		}
		accu_title.setText(mActiveinfosBean.getName() + "") ;
		accu_time.setText(TimeUtils.getTime(mActiveinfosBean.getStartdate()+"")) ;
		accu_place.setText(mActiveinfosBean.getAddress() + "") ;

		if(bean.getType() ==1){
			accu_ticket_price.setText("免费");
			accu_ticket_type.setText("免费票");
		}else{
			accu_ticket_price.setText("￥"+bean.getMoney());
			accu_ticket_type.setText("收费票");
		}

	}
	
	private void initChart() {

		UserInfoServiceImpl.getInstance().groupmember(mActiveinfosBean.getActiveid()+"", new StringCallback(){

			@Override
			public void onFailure(Request request, IOException e) {
				ToastUtil.showShort("群用户信息获取失败!") ;
			}

			@Override
			public void onResponse(String response) {
				StatusJson statusJosn = new StatusJson() ;
				Object obj = statusJosn.analysisJson2Object(response) ;
				if(obj != null){
					StatusBean statusBean = (StatusBean)obj ;
					if(statusBean.getCode() == 0){
						UserInfoJson userInfoJson = new UserInfoJson() ;
						obj = userInfoJson.parseJsonArr(statusBean.getData()) ;
						if(obj != null){
							@SuppressWarnings("unchecked")
							ArrayList<UserInfoBean> datas = (ArrayList<UserInfoBean>)obj;
							if(datas != null){

								mChartUserInfoAdapter = new ChartUserInfoAdapter(
										PaySuccess2Activity.this, datas);
								mScrollGridView.setAdapter(mChartUserInfoAdapter);
							}
						}
					}else{
						ToastUtil.showShort("群用户信息获取失败!") ;
					}
				}
			}
		});
	}

	private void connectChart(final int type){
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this);
		}else{
			Intent intent = new Intent(PaySuccess2Activity.this,LoginActivity.class);
			startActivity(intent);
			return ;
		}

		if(RongIM.getInstance()!=null && RongIM.getInstance().getCurrentConnectionStatus()
				.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)){
			joinGroup(type);
			return;
		}

		String token = mUserInfoBean.getToken();
		Utiles.log("群聊token："+token);
		RongIM.connect(token, new ConnectCallback() {

			@Override
			public void onSuccess(String arg0) {
				Log.v(TAG, "onSuccess " + arg0);
				if(RongIM.getInstance() != null){
					RongIM.getInstance().setCurrentUserInfo(new UserInfo(mUserInfoBean.getUserid()+"",
							mUserInfoBean.getUsername(), Uri.parse(mUserInfoBean.getHeadurl())));
				}
				RongIM.getInstance().setMessageAttachedUserInfo(true); //关联用户信息
				joinGroup(type);
			}

			@Override
			public void onError(ErrorCode arg0) {
				Log.v(TAG, "onError " + arg0);
			}

			@Override
			public void onTokenIncorrect() {
				Log.v(TAG, "onTokenIncorrect ");
			}
		});

		RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
		    @Override
		    public UserInfo getUserInfo(String userId) {
		        return new UserInfo(mUserInfoBean.getUserid()+"", mUserInfoBean.getUsername(), Uri.parse(mUserInfoBean.getHeadurl()));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
		    }
		}, true);
	}
	
	private void joinGroup(final int type){
		new Thread(new Runnable() {

			@Override
			public void run() {
//				String key = "8brlm7ufra0p3";//替换成您的appkey
//				String secret = "LMKw6KmyqKOGp";//替换成匹配上面key的secret

				SdkHttpResult result = null;

				//获取token
				try {
					//加入群
					result = ApiHttpClient.joinGroup(UrlsManager.key, UrlsManager.secret,
							mUserInfoBean.getUserid()+"", mActiveinfosBean.getActiveid()+"",
							mActiveinfosBean.getName(), FormatType.json);
						System.out.println("joinGroup=" + result);
					if(type == 1){
						Message msg= Message.obtain() ;
						msg.what = 1;
						msg.arg1 = result.getHttpCode();
						handler.sendMessage(msg);
					}else{
						initChart();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start() ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.circle_enter:
		case R.id.circle_enter_attion:
			connectChart(1);
			break;
		}
	}
	
	
	
}
