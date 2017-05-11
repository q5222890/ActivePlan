package com.xz.activeplan.ui.personal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.personal.adapter.HostActiveAdapter;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.views.xlistview.XListView;
import com.xz.activeplan.views.xlistview.XListView.IXListViewListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 我的- 主办活动
 */
@SuppressLint("ValidFragment")
public class HostFragment extends BaseFragment implements OnClickListener,IXListViewListener{
	
	private TextView mTxtNoData ;
	private XListView listView;
	
	private View mView; 
	
	private HostActiveAdapter mHostActiveAdapter ;
	
	private ArrayList<ActiveinfosBean> arr = new ArrayList<ActiveinfosBean>() ;
	
	private UserInfoBean userInfoBean ;
	private int type = 1; //1=已发布活动, 0=已结束活动
	private int currpage = 0; 
	private int pagesize = 10 ;
	private int hostid = 1;
	
	public HostFragment(int type,int hostid){
		this.type = type ;
		this.hostid = hostid;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.fragment_activite_complete, container,false);
		initViews() ;
		return mView;
	}
	
	private void initViews(){
		mTxtNoData = (TextView)mView.findViewById(R.id.txt_nodata) ;
		listView = (XListView)mView.findViewById(R.id.id_XListview);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this) ;
		mTxtNoData.setOnClickListener(this) ;
		listView.setEmptyView(mTxtNoData) ;
		mHostActiveAdapter = new HostActiveAdapter(getActivity(), arr) ;
		listView.setAdapter(mHostActiveAdapter) ;
		
		if(SharedUtil.isLogin(getActivity())){
			userInfoBean = SharedUtil.getUserInfo(getActivity()) ;
			loadData() ;
		}else{
			ToastUtil.showShort("请登录!") ;
			
		}
	}
	
	private void loadData(){
		UserInfoServiceImpl.getInstance().hostPostActive(hostid, type, currpage, pagesize, new StringCallback(){
			@SuppressWarnings("unchecked")
			@Override
			public void onResponse(String response) {
				if(currpage <= 0){
					listView.stopRefresh();
				}else{
					listView.stopLoadMore() ;
				}
				StatusJson statusJson = new StatusJson() ;
				StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(statusBean != null && statusBean.getCode() == 0){
					ActiveinfosJson collectJson = new ActiveinfosJson() ;
					ArrayList<ActiveinfosBean> ticketBeans = (ArrayList<ActiveinfosBean>)collectJson.analysisJson2Object(statusBean.getData()) ;
					if(currpage <= 0){
						arr.clear();	 
					}
					if(ticketBeans.size() < pagesize){
						listView.setPullLoadEnable(false);
					}else{
						listView.setPullLoadEnable(true);
					}
					
					if(ticketBeans.size() > 0){
						currpage++;
					}
					
					arr.addAll(ticketBeans) ;
					mHostActiveAdapter.notifyDataSetChanged() ;
				}else{
					ToastUtil.showShort("数据获取失败，请刷新重试!") ;
				}
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				ToastUtil.showShort("数据获取失败，请刷新重试!") ;
				if(currpage <= 0){
					listView.stopRefresh();
				}else{
					listView.stopLoadMore() ;
				}
			}
		} );
	}

	@Override
	public void onRefresh() {
		currpage = 0 ;
		loadData() ;
	}

	@Override
	public void onLoadMore() {
		loadData() ;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.txt_nodata:
			onRefresh() ;
			break;
		}
		
	}

	@Override
	public void onBackPressed() {

	}
}
