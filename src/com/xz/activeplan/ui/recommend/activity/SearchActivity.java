package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
public class SearchActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageView iv_datails_back,imgsearch;
	private EditText edt_search;
	private GridView gridView;

	private String[] example_items;

	private ExampleAdapter mExampleAdapter;

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
		gridView = (GridView) findViewById(R.id.grid);
		gridView.setVisibility(View.VISIBLE);
		edt_search.setHint("关键字");

		example_items = getResources().getStringArray(R.array.example_data);

		mExampleAdapter = new ExampleAdapter();
		gridView.setAdapter(mExampleAdapter);

		iv_datails_back.setOnClickListener(this);

		edt_search.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 隐藏键盘
					
					String search = edt_search.getText().toString().trim();
					if(TextUtils.isEmpty(search)){
//						Toast.makeText(SearchActivity.this, "请输入关键字" , Toast.LENGTH_LONG).show();
						ToastUtil.showShort("请输入关键字") ;
					}else{
						Intent intent = new Intent(SearchActivity.this,
								SearchListActivity.class);
						intent.putExtra("searchname",search );
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
//					Toast.makeText(SearchActivity.this, "请输入关键字" , Toast.LENGTH_LONG).show();
					ToastUtil.showShort("请输入关键字") ;
				}else{
					Intent intent = new Intent(SearchActivity.this,
							SearchListActivity.class);
					intent.putExtra("searchname",search );
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

	class ExampleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (example_items != null) {
				return example_items.length;
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			if (example_items != null && example_items.length > arg0) {
				return example_items[arg0];
			}
			return "";
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(SearchActivity.this,
						R.layout.listiten_example, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			final String name = (String) getItem(position);

			holder.exampleName.setText(name);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(SearchActivity.this,
							SearchListActivity.class);
					intent.putExtra("searchname", name);
					startActivity(intent);
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView exampleName;

			public ViewHolder(View view) {
				exampleName = (TextView) view
						.findViewById(R.id.tv_example_name);

				view.setTag(this);
			}
		}
	}

}
