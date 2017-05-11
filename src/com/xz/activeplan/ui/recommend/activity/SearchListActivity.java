package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.recommend.adapter.RecommendAdapter;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 搜索界面activity
 * @author johnny
 *
 */
public class SearchListActivity extends BaseFragmentActivity  implements OnClickListener{
	private TextView mTvHeadTitle ;
	
	private ImageView iv_datails_back;
	
	private ListView mListView ;
	private TextView mTxtNodata ;
	 
	private String searchName ="";
	

	
	private RecommendAdapter mRecommendAdapter;
	private ArrayList<ActiveinfosBean> arr = new ArrayList<ActiveinfosBean>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_listview);
		initViews();
	}
	
	private void initViews(){
		mTvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;
		iv_datails_back = (ImageView)findViewById(R.id.id_ImageHeadTitleBlack) ;
		mListView = (ListView)findViewById(R.id.listview) ;
		mTxtNodata = (TextView)findViewById(R.id.txt_nodata) ;
	
		mListView.setEmptyView(mTxtNodata) ;
		
		mRecommendAdapter = new RecommendAdapter(this,arr);
		mListView.setAdapter(mRecommendAdapter);
		
		mTxtNodata.setText("暂无数据，请点击刷新") ;
	
		Intent intent = getIntent() ;
		if(intent != null){
			searchName = intent.getStringExtra("searchname") ;
			mTvHeadTitle.setText( searchName + "");
		}
	
		iv_datails_back.setOnClickListener(this) ;
		mTxtNodata.setOnClickListener(this) ;
	
		loadData();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.txt_nodata:
			loadData(); 
			break;
		}
	}

	private void loadData(){
		ActiveServiceImpl.getInstance().searchActive(searchName, new StringCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onResponse(String response) {
				StatusJson statusJosn = new StatusJson() ;
				Object obj = statusJosn.analysisJson2Object(response) ;
				if(obj != null){
					StatusBean statusBean = (StatusBean)obj ;
					if(statusBean.getCode() == 0){
						ActiveinfosJson activeinfosBean = new ActiveinfosJson() ;
						obj = activeinfosBean.analysisJson2Object(statusBean.getData()) ;
						if(obj != null){
							ArrayList<ActiveinfosBean> activeinfosBeans = (ArrayList<ActiveinfosBean>)obj;
							if(activeinfosBeans != null){
								arr.clear();
								arr.addAll(activeinfosBeans) ;
								mRecommendAdapter.notifyDataSetChanged() ;
							}
						}
					}else{
						ToastUtil.showShort("数据获取失败，请刷新重试") ;
					}
				}
			}
			@Override
			public void onFailure(Request request, IOException e) {
			}
		});
	}
}
