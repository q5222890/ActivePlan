package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.UserInfoJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.personal.adapter.ChartUserInfoAdapter;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.ScrollGridView;

import java.io.IOException;
import java.util.ArrayList;

import io.rong.ApiHttpClient;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;

/**
 * 群成员界面
 * 
 * @author johnny
 * 
 */
public class GroupMemberActivity extends BaseFragmentActivity implements
		OnClickListener, OnCheckedChangeListener,OnItemClickListener {
	private TextView tvGroupName;
	private ImageView iv_datails_back;
	private Button btQuitCircle;

	private String title;
	private String mTargetId;
	private Conversation.ConversationType mConversationType;

	private ChartUserInfoAdapter mChartUserInfoAdapter;
	private ScrollGridView mScrollGridView;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.arg1 == 200) {
				@SuppressWarnings("unchecked")
				ArrayList<UserInfoBean> list = (ArrayList<UserInfoBean>) msg.obj;
				mChartUserInfoAdapter = new ChartUserInfoAdapter(
						GroupMemberActivity.this, list);
				mScrollGridView.setAdapter(mChartUserInfoAdapter);
			} else if (msg.what == 2 && msg.arg1 == 200) {
				EventBean data = new EventBean(EventType.QUITE_CHART_TYPE);
				ClassConcrete.getInstance().postDataUpdate(data);
				finish();
			}
		}
	};
	private CheckBox mCheckBox;
	private UserInfoBean mUserInfoBean;
	private CustomProgressDialog mProgressDialog ;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_group_member);
		initViews();
	}

	private void initViews() {
		mProgressDialog = new CustomProgressDialog(this) ;
		Utiles.headManager(this,"群成员");
		tvGroupName = (TextView) findViewById(R.id.tvGroupName);
		mScrollGridView = (ScrollGridView) findViewById(R.id.grid);//群成员
		btQuitCircle = (Button) findViewById(R.id.btQuitCircle);

		mCheckBox = (CheckBox) findViewById(R.id.cbDND);

		btQuitCircle.setOnClickListener(this);

		mCheckBox.setOnCheckedChangeListener(this);

		mScrollGridView.setOnItemClickListener(this) ;

		if (SharedUtil.isLogin(this)) {
			mUserInfoBean = SharedUtil.getUserInfo(this);
		}



		Intent intent = getIntent();
		if (intent != null) {
			title = intent.getStringExtra("title");
			mTargetId = intent.getStringExtra("mTargetId");
			mConversationType = (Conversation.ConversationType)intent.getSerializableExtra("mConversationType") ;
			tvGroupName.setText(title);

			initChart();

			RongIMClient.getInstance().getConversationNotificationStatus(mConversationType, mTargetId, new ResultCallback<Conversation.ConversationNotificationStatus>(){

				@Override
				public void onError(ErrorCode arg0) {

				}
				@Override
				public void onSuccess(ConversationNotificationStatus arg0) {
					if(arg0 == ConversationNotificationStatus.DO_NOT_DISTURB){
						mCheckBox.setChecked(true);
					}else{
						mCheckBox.setChecked(false);
					}
				}
			}) ;

		}
	}

	private void initChart() {

		UserInfoServiceImpl.getInstance().groupmember(mTargetId, new StringCallback(){

			@Override
			public void onFailure(Request request, IOException e) {
//				Toast.makeText(GroupMemberActivity.this, "群用户信息获取失败", Toast.LENGTH_LONG).show() ;
				ToastUtil.showShort("群用户信息获取失败") ;
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
										GroupMemberActivity.this, datas);
								mScrollGridView.setAdapter(mChartUserInfoAdapter);
							}
						}
					}else{
//						Toast.makeText(GroupMemberActivity.this, "群用户信息获取失败", Toast.LENGTH_LONG).show() ;
						ToastUtil.showShort("群用户信息获取失败") ;
					}
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btQuitCircle:
			quitGroup();
			break;
		}

	}

	private void quitGroup() {
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				SdkHttpResult result = null;

				// 获取token
				try {

					result = ApiHttpClient.quitGroup(UrlsManager.key, UrlsManager.secret,
							mUserInfoBean.getUserid() + "", mTargetId,
							FormatType.json);
					Message msg = Message.obtain();
					msg.what = 2;
					msg.arg1 = result.getHttpCode();

					handler.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			setNotifition(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) ;
		}else{
			setNotifition(Conversation.ConversationNotificationStatus.NOTIFY) ;
		}
	}
	
	private void setNotifition(final Conversation.ConversationNotificationStatus type){
		RongIMClient.getInstance().setConversationNotificationStatus(mConversationType, mTargetId, type, new ResultCallback<Conversation.ConversationNotificationStatus>(){

			@Override
			public void onError(ErrorCode arg0) {
				Log.e(">>>>>>>>>>>", "" + arg0.getMessage()) ;
			}

			@Override
			public void onSuccess(ConversationNotificationStatus arg0) {
				Log.e(">>>>>>>>>>>", "" + arg0) ;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		UserInfoBean bean = (UserInfoBean)mChartUserInfoAdapter.getItem(position);
		getUserInfo(bean.getUserid());
	}
	
	private void getUserInfo(int userid){
		mProgressDialog.show() ;
		UserInfoServiceImpl.getInstance().getUserinfo(userid, new StringCallback() {
			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss();
				StatusJson statusJosn = new StatusJson() ;
				Object obj = statusJosn.analysisJson2Object(response) ;
				if(obj != null){
					StatusBean statusBean = (StatusBean)obj ;
					if(statusBean.getCode() == 0){
						UserInfoJson userInfoJson = new UserInfoJson() ;
						obj = userInfoJson.analysisJson2Object(statusBean.getData()) ;
						if(obj != null){
							UserInfoBean userInfoBean = (UserInfoBean)obj;
							if(userInfoBean != null){
								Intent intent = new Intent(GroupMemberActivity.this,UserDetailActivity.class) ;
								intent.putExtra("userinfo", userInfoBean) ;
								startActivity(intent);
							}
						}
					}
				}

			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				mProgressDialog.dismiss();
			}
		});
	}

}
