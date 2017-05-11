package com.xz.activeplan.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

/**
 * 搜索界面activity
 * 
 * @author johnny
 * 
 */
public class SearchTeacherActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageView iv_datails_back,imgsearch;
	private EditText edt_search;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_search);
		initViews();
	}

	private void initViews() {
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imgsearch = (ImageView)findViewById(R.id.imgsearch);
		edt_search = (EditText) findViewById(R.id.edt_search);
	
		iv_datails_back.setOnClickListener(this);

		edt_search.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 隐藏键盘
					String search = edt_search.getText().toString().trim();
					if(TextUtils.isEmpty(search)){
//						Toast.makeText(SearchTeacherActivity.this, "请输入关键字" , Toast.LENGTH_LONG).show();
						ToastUtil.showShort("请输入关键字!") ;
					}else{
						Intent intent = new Intent(SearchTeacherActivity.this,
								SearchTeacherListActivity.class);
						intent.putExtra("searchname", search);
						startActivity(intent);
					}
				}
				return false ;
			}
		});
		
		imgsearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String search = edt_search.getText().toString().trim();
				if(TextUtils.isEmpty(search)){
					Toast.makeText(SearchTeacherActivity.this, "请输入关键字" , Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent(SearchTeacherActivity.this,
							SearchTeacherListActivity.class);
					intent.putExtra("searchname", search);
					startActivity(intent);
				}
			}
		}) ;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		}

	}

}
