package com.xz.activeplan.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.TeacherJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.news.adapter.TeacherGridViewAdapter;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 搜索界面activity
 * @author johnny
 *
 */
public class SearchTeacherListActivity extends BaseFragmentActivity implements OnClickListener{
	private TextView mTvHeadTitle ;
	
	private ImageView iv_datails_back;

	private String searchName ="";
	
	private TeacherGridViewAdapter mTeacherGridViewAdapter ;
	
	private ArrayList<TeacherBean> datas = new ArrayList<>();
	
	private CustomProgressDialog mProgressDialog ;
	
	private GridView grid;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_search_teacher_list);
		initViews();
	}
	
	private void initViews(){
		mProgressDialog = new CustomProgressDialog(this) ;
		
		mTvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;
		iv_datails_back = (ImageView)findViewById(R.id.id_ImageHeadTitleBlack) ;
		grid = (GridView)findViewById(R.id.grid);

		Intent intent = getIntent() ;
		if(intent != null){
			searchName = intent.getStringExtra("searchname") ;
			mTvHeadTitle.setText( searchName + "");
		}
		
		mTeacherGridViewAdapter = new TeacherGridViewAdapter(this, datas) ;
		grid.setAdapter(mTeacherGridViewAdapter);
		
		iv_datails_back.setOnClickListener(this) ;
		
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SearchTeacherListActivity.this,
						CelebrityActivity.class);
				intent.putExtra("data",mTeacherGridViewAdapter.getItem(arg2)) ;
				SearchTeacherListActivity.this.startActivity(intent);
			}
		}) ;
		
		loadData();
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;

		}
	}

	/**
	 * 加载数据
	 */
	private void loadData(){
		
		CelebrityNewWorkImpl.getInstance().searchCelebrity(searchName, 0, 100, new StringCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onResponse(String response) {
				mProgressDialog.dismiss() ;
				StatusJson statusJson = new StatusJson() ;
				StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(statusBean != null && statusBean.getCode() == 0){
					TeacherJson teacherJson = new TeacherJson() ;
					ArrayList<TeacherBean> teacherBeans = (ArrayList<TeacherBean>)teacherJson.analysisJson2Object(statusBean.getData()) ;
					datas.clear() ;
					datas.addAll(teacherBeans) ;
					mTeacherGridViewAdapter.notifyDataSetChanged() ;
				}else{
					ToastUtil.showShort("数据获取失败，请刷新重试!") ;
				}
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				mProgressDialog.dismiss() ;
				ToastUtil.showShort("数据获取失败，请刷新重试!") ;
			}
		}) ;
	}
}
