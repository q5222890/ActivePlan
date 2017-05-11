package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.AccountDeatilBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.AccountDetailJosn;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.adapter.AccountDetailAdapter;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.xlistview.XListView;
import com.xz.activeplan.views.xlistview.XListView.IXListViewListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 钱包明细界面activity
 * @author johnny
 *
 */
public class MoneyListActivity extends BaseFragmentActivity  implements OnClickListener,IXListViewListener{
	private TextView mTvHeadTitle ;
	

	private XListView mListView ;
	private TextView mTxtNodata ;
	
	private UserInfoBean mUserInfoBean ;
	private int currpage = 0;
	private int pagesize = 10 ;
	
	private AccountDetailAdapter mAccountDetailAdapter ;
	private ArrayList<AccountDeatilBean> arr = new ArrayList<AccountDeatilBean>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.xlistview_main) ;
		initViews();
	}
	private void initViews(){
		//返回框背景
		View viewHeald = findViewById(R.id.relativeLayout_toolbar);
		viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
		//返回键
		ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imageBlack.setOnClickListener(this);
		//头部字体
		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
		mListView = (XListView)findViewById(R.id.id_XListview) ;
		mTxtNodata = (TextView)findViewById(R.id.txt_nodata) ;
		
		mListView.setEmptyView(mTxtNodata) ;
		mListView.setXListViewListener(this) ;
		mListView.setPullLoadEnable(true);
		mAccountDetailAdapter = new AccountDetailAdapter(this, arr) ;
		mListView.setAdapter(mAccountDetailAdapter) ;
		mTxtNodata.setOnClickListener(this) ;
		mTxtNodata.setText("钱包暂无数据") ;
		mTvHeadTitle.setText("余额明细");
		
		loadData() ;
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this) ;
			
		}else{
//			Toast.makeText(this, "请登录!", Toast.LENGTH_LONG).show() ;
			ToastUtil.showShort("请登录!") ;
		}
	}
	
	private void loadData(){

		
		if(SharedUtil.isLogin(this)){
			mUserInfoBean = SharedUtil.getUserInfo(this);
		}else{
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			return;
		}
		
		UserInfoServiceImpl.getInstance().getDetail(mUserInfoBean.getUserid()+"", currpage, pagesize, new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				if(currpage <= 0){
					mListView.stopRefresh();
				}else{
					mListView.stopLoadMore() ;
				}
				StatusJson statusJosn = new StatusJson() ;
				Object obj = statusJosn.analysisJson2Object(response) ;
				if(obj != null){
					StatusBean statusBean = (StatusBean)obj ;
					if(statusBean.getCode() == 0){
						AccountDetailJosn accountDetailJosn = new AccountDetailJosn() ;
						obj = accountDetailJosn.analysisJson2Object(statusBean.getData()) ;
						if(obj != null){
							@SuppressWarnings("unchecked")
							ArrayList<AccountDeatilBean> accountDeatilBeans = (ArrayList<AccountDeatilBean>)obj;
							if(accountDeatilBeans != null){
								if(currpage <= 0){
									arr.clear() ;
								}
								
								if(accountDeatilBeans.size() < pagesize){
									mListView.setPullLoadEnable(false);
								}else{
									mListView.setPullLoadEnable(true);
								}
								
								if(accountDeatilBeans.size() > 0){
									currpage++ ;
								}
								arr.addAll(accountDeatilBeans) ;
								mAccountDetailAdapter.notifyDataSetChanged() ;
							}
						}
					}else{
//						Toast.makeText(MoneyListActivity.this, "数据获取失败，请刷新重试", Toast.LENGTH_LONG).show() ;
						ToastUtil.showShort("数据获取失败，请刷新重试!") ;
					}
				}
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
//				Toast.makeText(MoneyListActivity.this, "数据获取失败，请刷新重试", Toast.LENGTH_LONG).show() ;
				ToastUtil.showShort("数据获取失败，请刷新重试!") ;
				if(currpage <= 0){
					mListView.stopRefresh();
				}else{
					mListView.stopLoadMore() ;
				}
			}
		}) ;
		
		
	}
	
	@Override
	public void onRefresh() {
		currpage = 0;
		loadData();
	}

	@Override
	public void onLoadMore() {
		
		loadData() ;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish() ;
			break;
		case R.id.txt_nodata:
			onRefresh() ;
			break;
		}
	}
	
}
