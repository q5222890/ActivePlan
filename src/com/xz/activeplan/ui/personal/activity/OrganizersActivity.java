package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.adapter.OrganizersAdapter;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.xlistview.XListView;
import com.xz.activeplan.views.xlistview.XListView.IXListViewListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 关注的主办方
 */
public class OrganizersActivity  extends BaseFragmentActivity implements OnClickListener,IXListViewListener{

	private TextView mTvHeadTitle ;
	private ImageView iv_datails_back;

	private XListView mListView ;
	private TextView mTxtNodata ;


	private UserInfoBean mUserInfoBean ;
	private int currpage = 0 ;
	private int pagesize = 10 ;
	
	private OrganizersAdapter mOrganizersAdapter; 
	private ArrayList<OrganizersBean> arr = new ArrayList<OrganizersBean>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.xlistview_main) ;
		initViews();
	}

	private void initViews(){
		mTvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;
		iv_datails_back = (ImageView)findViewById(R.id.id_ImageHeadTitleBlack) ;
		mListView = (XListView)findViewById(R.id.id_XListview) ;
		mTxtNodata = (TextView)findViewById(R.id.txt_nodata) ;
		mListView.setPullLoadEnable(true);
		mListView.setEmptyView(mTxtNodata) ;
		mListView.setXListViewListener(this) ;


		mOrganizersAdapter = new OrganizersAdapter(this, arr) ;
		mListView.setAdapter(mOrganizersAdapter) ;

		mTxtNodata.setOnClickListener(this) ;
		
		mTxtNodata.setText("当前没有关注的主办方") ;
		
		mTvHeadTitle.setText("关注的主办方");
		
		iv_datails_back.setOnClickListener(this) ;
		Intent intent = getIntent() ;
		
		if(intent != null){
			mUserInfoBean = (UserInfoBean)intent.getSerializableExtra("userinfo") ;
			loadData();
		}else{
			ToastUtil.showShort("请退出选中用户!") ;
		}
	}
	
	private void loadData(){
		
		if(mUserInfoBean == null){
			ToastUtil.showShort("请退出选中用户!") ;
			return;
		}
		
		UserInfoServiceImpl.getInstance().myHost(mUserInfoBean.getUserid(), currpage, pagesize,new StringCallback() {
			
			@SuppressWarnings("unchecked")
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
						OrganizersJson userInfoJson = new OrganizersJson() ;
						obj = userInfoJson.analysisJson2Object(statusBean.getData()) ;
						if(obj != null){
							ArrayList<OrganizersBean> collectBeans = (ArrayList<OrganizersBean>)obj;
							if(collectBeans != null){
								if(currpage  <= 0){
									arr.clear() ;
								}
								
								if(collectBeans.size() < pagesize){
									mListView.setPullLoadEnable(false);
								}else{
									mListView.setPullLoadEnable(true);
								}
								
								if(collectBeans.size() > 0){
									currpage++ ;
								}
								arr.addAll(collectBeans) ;
								mOrganizersAdapter.notifyDataSetChanged() ;
							}
						}
					}else{
						ToastUtil.showShort("数据获取失败，请刷新重试!") ;
					}
				}
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
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

