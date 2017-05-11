package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.ui.personal.adapter.FragmentAdapter;
import com.xz.activeplan.ui.recommend.fragment.ActiveFragment;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CircleImageView;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.ShareDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家界面activity
 * @author johnny
 *
 */
public class SponsorDetailActivity extends BaseFragmentActivity  implements OnClickListener{
	private ImageView ivBack,ivSponsorLogo,ivHasFollowed;
	private TextView tvSponsorName,tvSponsorDesc,tvSponsorFollows;
	private ViewPager mViewPager ;
	private ImageView share_ly;  //分享
	private List<BaseFragment> mFragmentList = new ArrayList<BaseFragment>();
	private FragmentAdapter mFragmentAdapter;
	/**
	 * Tab显示内容TextView
	 */
	private TextView mTabYTicket, mTabNTicket;
	
	private LinearLayout id_tab_yticket_ll,id_tab_nticket_ll;

	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	private ActiveFragment startFragment;
	private ActiveFragment endFragment;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	
	private OrganizersBean mOrganizersBean ;
	
	private ActiveinfosBean mActiveinfosBean;
	
	private ShareDialog mShareDialog ;
	
	private UserInfoBean mUserInfoBean ;
	
	private CustomProgressDialog mProgressDialog ;
	
	private boolean isAttend = false ;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_sponsor_detail) ;
		initViews();
		init();
		initTabLineWidth() ;
	}
	
	private void initViews() {
		mProgressDialog = new CustomProgressDialog(this) ;
		ivBack = (ImageView)findViewById(R.id.ivBack) ;
		share_ly = (ImageView) findViewById(R.id.ivShare) ;
		tvSponsorFollows = (TextView)findViewById(R.id.tvSponsorFollows) ;
		 
		tvSponsorName = (TextView)findViewById(R.id.tvSponsorName) ;
		ivSponsorLogo = (CircleImageView)findViewById(R.id.ivSponsorLogo) ;
		ivHasFollowed = (ImageView)findViewById(R.id.ivHasFollowed) ;
		tvSponsorDesc = (TextView)findViewById(R.id.tvSponsorDesc) ;
		
		mViewPager = (ViewPager)findViewById(R.id.id_stickynavlayout_viewpager) ;
		
		mTabYTicket = (TextView) this.findViewById(R.id.id_chat_tv);
		mTabNTicket = (TextView) this.findViewById(R.id.id_friend_tv);

		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		
		id_tab_yticket_ll = (LinearLayout)this.findViewById(R.id.id_tab_chat_ll) ;
		id_tab_nticket_ll = (LinearLayout)this.findViewById(R.id.id_tab_friend_ll) ;
		
		id_tab_yticket_ll.setOnClickListener(this) ;
		id_tab_nticket_ll.setOnClickListener(this) ;
		ivBack.setOnClickListener(this) ;
		share_ly.setOnClickListener(this) ;

		ivHasFollowed.setOnClickListener(this) ;
		
		mShareDialog = new ShareDialog(this) ;
		mShareDialog.setOnClickListener(this) ;
		
		Intent intent = getIntent() ;
		if(intent != null){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
			mActiveinfosBean = (ActiveinfosBean)intent.getSerializableExtra("data") ;
			mOrganizersBean = (OrganizersBean)intent.getSerializableExtra("OrganizersBean");
			if(mOrganizersBean != null){
				//fillData(mOrganizersBean) ;
				loadData(mOrganizersBean.getHostid());
			}else{
				loadData(mActiveinfosBean.getHostid());
			}
			
		}

	}
	
	private void loadData(int hostid){
		
		int userid = -1 ;
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
			userid = mUserInfoBean.getUserid() ;
		}
		
		
//		mProgressDialog.show();
		
		UserInfoServiceImpl.getInstance().getHostInfo(userid, hostid, new StringCallback() {
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						OrganizersJson organizersJson = new OrganizersJson();
						obj = organizersJson.parseJson(statusBean.getData());
						if (obj != null) {
							mOrganizersBean = (OrganizersBean)obj;
							fillData(mOrganizersBean) ;
						}
					}
				}
				mProgressDialog.dismiss();

			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				ToastUtil.showShort("主办方信息获取失败");
				mProgressDialog.dismiss() ;
			}
		}) ;
	}
	
	private void fillData(OrganizersBean bean){
		if(!TextUtils.isEmpty(bean.getHosthearurl())){
			Picasso.with(this).load(bean.getHosthearurl()).placeholder(R.drawable.default_square_image).error(R.drawable.default_square_image).into(ivSponsorLogo);
		}else{
			Picasso.with(this).load(R.drawable.default_square_image).placeholder(R.drawable.default_square_image).into(ivSponsorLogo);
		}
		
		if(!TextUtils.isEmpty(bean.getHostname())){
			tvSponsorName.setText(bean.getHostname()) ;
		}else{
			tvSponsorName.setText("主办方");
		}
		
		tvSponsorFollows.setText(bean.getFollownum() + "人关注") ;
		
		if(!TextUtils.isEmpty(bean.getHostintro())){
			tvSponsorDesc.setText(bean.getHostintro() + "") ;
		}else{
			tvSponsorDesc.setText("") ;
		}
		
		if(bean.isIsattend()){
			isAttend = true; 
			ivHasFollowed.setBackgroundResource(R.drawable.has_attention) ;
		}else{
			isAttend = false; 
			ivHasFollowed.setBackgroundResource(R.drawable.bt_guanzhu) ;
		}
		
	}
	
	private void init() {
		if(mOrganizersBean != null){
			startFragment = new ActiveFragment(1,mOrganizersBean.getHostid());
			endFragment = new ActiveFragment(0,mOrganizersBean.getHostid());
		}else{
			startFragment = new ActiveFragment(1,mActiveinfosBean.getHostid());
			endFragment = new ActiveFragment(0,mActiveinfosBean.getHostid());
		}
			
		
		mFragmentList.add(startFragment);
		mFragmentList.add(endFragment);

		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setCurrentItem(0);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记2个页面,
				 * 从左到右分别为0,1 
				 * 0->1; 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2)) ;

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2));

				} 
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					mTabYTicket.setTextColor(Color.parseColor("#00BBFE"));
					break;
				case 1:
					mTabNTicket.setTextColor(Color.parseColor("#00BBFE"));
					break;
				}
				currentIndex = position;
			}
		});

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/2(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 2;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		mTabYTicket.setTextColor(Color.DKGRAY);
		mTabNTicket.setTextColor(Color.DKGRAY);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.ivHasFollowed:
			if(SharedUtil.isLogin(SponsorDetailActivity.this)){
				mUserInfoBean = SharedUtil.getUserInfo(SponsorDetailActivity.this) ;
			}else{
				Intent intent = new Intent(SponsorDetailActivity.this,LoginActivity.class);
				SponsorDetailActivity.this.startActivity(intent);
				return;
			}
			if(!isAttend){
				attendHost();
			}else{
				cancelHost();
			}
			break;
		case R.id.id_tab_chat_ll:
			mViewPager.setCurrentItem(0) ;
			break;
		case R.id.id_tab_friend_ll:
			mViewPager.setCurrentItem(1) ;
			break;
		case R.id.ivShare:
			mShareDialog.show() ;
			break;
		case R.id.tvShareSina:
	
		case R.id.tvShareQzone:

		case R.id.tvSharewx:

		case R.id.tvSharewxpy:

		case R.id.tvShareMsg:

			break;
		}
	}

	private void cancelHost() {
		if(mOrganizersBean == null){
			ToastUtil.showShort("主办方信息异常，无法取消关注") ;
			finish();
			return;
		}
		
		UserInfoServiceImpl.getInstance().cancelHost(mUserInfoBean.getUserid(), mOrganizersBean.getHostid(), new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						ToastUtil.showShort("取消关注成功") ;
						isAttend = false; 
						ivHasFollowed.setBackgroundResource(R.drawable.bt_guanzhu) ;
					}else{
						ToastUtil.showShort("取消关注失败") ;
					}
				}else{
					ToastUtil.showShort("取消关注失败") ;
				}
				mProgressDialog.dismiss();
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				ToastUtil.showShort("取消关注失败") ;
				mProgressDialog.dismiss();
			}
		});
	}

	private void attendHost() {
		if(mOrganizersBean == null){
			ToastUtil.showShort("主办方信息异常，无法关注") ;
			finish();
			return;
		}
		
		mProgressDialog.show();
		UserInfoServiceImpl.getInstance().attendHost(mUserInfoBean.getUserid(), mOrganizersBean.getHostid(), new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						ToastUtil.showShort("关注成功") ;
						isAttend = true; 
						ivHasFollowed.setBackgroundResource(R.drawable.has_attention);
					}else{
						ToastUtil.showShort("关注失败") ;
					}
				}
				mProgressDialog.dismiss();
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				ToastUtil.showShort("关注失败") ;
				mProgressDialog.dismiss();
			}
		});
		
	}
}