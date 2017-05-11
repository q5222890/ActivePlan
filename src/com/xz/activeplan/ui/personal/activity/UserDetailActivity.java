package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;

import io.rong.imkit.RongIM;

/**
 * 用户信息 界面activity
 * @author johnny
 *
 */
public class UserDetailActivity extends BaseFragmentActivity implements OnClickListener{
	
	private ImageView iv_datails_back ,ivUserLogo;
	private TextView tvHeadTitle,tvBrief,tvRegion;
	private LinearLayout linFollows;
	private Button btSend ;
	
	private UserInfoBean mFriendUserInfo ;
	private UserInfoBean mUserInfoBean;
	
	private CustomProgressDialog mProgressDialog ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_user_detail) ;
		initViews() ;
	}
	
	private void initViews(){
		mProgressDialog = new CustomProgressDialog(this);
		Utiles.headManager(this,"用户资料");
		ivUserLogo = (ImageView)findViewById(R.id.ivUserLogo) ;
		tvBrief = (TextView) findViewById(R.id.tvBrief);
		tvRegion = (TextView) findViewById(R.id.tvRegion);
		linFollows = (LinearLayout)findViewById(R.id.linFollows) ;
		btSend = (Button)findViewById(R.id.btSend) ;
		
		linFollows.setOnClickListener(this);
		btSend.setOnClickListener(this) ;
		
		Intent intent = getIntent();
		if(intent != null){
			mFriendUserInfo = (UserInfoBean)intent.getSerializableExtra("userinfo") ;
			
			if(!TextUtils.isEmpty(mFriendUserInfo.getUsername())){
				tvBrief.setText(mFriendUserInfo.getUsername()) ;
			}else{
				tvBrief.setText(mFriendUserInfo.getPhonenum()) ;
			}
			if(!TextUtils.isEmpty(mFriendUserInfo.getCity())){
				tvRegion.setText(mFriendUserInfo.getCity()) ;
			}
			
			
			if(!TextUtils.isEmpty(mFriendUserInfo.getHeadurl())){
				Picasso.with(this).load(mFriendUserInfo.getHeadurl()).placeholder(R.drawable.default_square_image).error(R.drawable.default_square_image).into(ivUserLogo);
			}else{
				Picasso.with(this).load(R.drawable.default_square_image).placeholder(R.drawable.default_square_image).into(ivUserLogo);
			}
			
			if(SharedUtil.isLogin(this)){
				mUserInfoBean = SharedUtil.getUserInfo(this);
				if(mUserInfoBean.getUserid() == mFriendUserInfo.getUserid()){
					btSend.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.linFollows:
			Intent intent = new Intent(UserDetailActivity.this,OrganizersActivity.class) ;
			intent.putExtra("userinfo", mFriendUserInfo) ;
			startActivity(intent);
			break;
		case R.id.btSend:
			attendFriend() ;
			break;
		
		}
		
	}
	
	private void attendFriend(){
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this);
		}else{
			Intent intent = new Intent(UserDetailActivity.this,LoginActivity.class);
			startActivity(intent);
			return;
		}
		mProgressDialog.show() ;
		UserInfoServiceImpl.getInstance().attendFriend(mUserInfoBean.getUserid(), mFriendUserInfo.getUserid(), new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				StatusJson statusJosn = new StatusJson() ;
				Object obj = statusJosn.analysisJson2Object(response) ;
				if(obj != null){
					StatusBean statusBean = (StatusBean)obj ;
					if(statusBean.getCode() == 0){
						RongIM.getInstance().startPrivateChat(UserDetailActivity.this, mFriendUserInfo.getUserid()+"", mFriendUserInfo.getUsername());
					}else{
						ToastUtil.showShort("请求失败") ;
					}
				}else{
					ToastUtil.showShort("请求失败") ;
				}
				mProgressDialog.dismiss();


			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				mProgressDialog.dismiss();
				ToastUtil.showShort("请求失败") ;
			}
		}) ;
	}
	
}
