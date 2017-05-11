package com.xz.activeplan.ui.recommend.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
/**
 * 城市选择 界面activity
 * @author johnny
 *
 */
public class ChooseCityActivity extends BaseFragmentActivity  implements OnClickListener{
	private TextView locationCityName ;
	private GridView gridView ;

	private TextView mTvHeadTitle ;
	
	private ImageView iv_prompt_select_city ;
	
	private String[] city_items ;
	
	private CityAdapter mCityAdapter ;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_choose_city) ;
		initViews() ;
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
		mTvHeadTitle.setText(getResources().getString(R.string.personal_update_city));

		locationCityName = (TextView)findViewById(R.id.locationCityName) ;
		gridView = (GridView)findViewById(R.id.gridView) ;
		iv_prompt_select_city = (ImageView)findViewById(R.id.iv_prompt_select_city) ;
		city_items = getResources().getStringArray(R.array.city_data);
		mCityAdapter = new CityAdapter();
		gridView.setAdapter(mCityAdapter) ;

		locationCityName.setText(SharedUtil.getLocationCity(ChooseCityActivity.this)) ;
		Utiles.log("定位城市:"+SharedUtil.getLocationCity(ChooseCityActivity.this));
		Utiles.log("保存的城市"+SharedUtil.getCity(ChooseCityActivity.this));
		locationCityName.setOnClickListener(this) ;
		
		if(SharedUtil.getLocationCity(ChooseCityActivity.this).equals(SharedUtil.getCity(ChooseCityActivity.this))){
			iv_prompt_select_city.setVisibility(View.VISIBLE) ;
		}else{
			iv_prompt_select_city.setVisibility(View.GONE);
		}

	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			onBackPressed();
			break;
		case R.id.locationCityName:
			SharedUtil.saveCity(ChooseCityActivity.this, SharedUtil.getLocationCity(ChooseCityActivity.this)) ;
			iv_prompt_select_city.setVisibility(View.VISIBLE) ;
			mCityAdapter.notifyDataSetChanged();
			EventBean data = new EventBean(EventType.CITY_NOTIFI_TYPE) ;
			ClassConcrete.getInstance().postDataUpdate(data) ;
			ChooseCityActivity.this.finish();
			break;
		}

	}

	class CityAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(city_items != null){
				return city_items.length ;
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			if(city_items != null && city_items.length > arg0){
				return city_items[arg0] ;
			}
			return "";
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ChooseCityActivity.this, R.layout.activity_city, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			String city = (String)getItem(position);
			String selectCity = SharedUtil.getCity(ChooseCityActivity.this) ;
			holder.locationCityName.setText(city) ;
			if(city.equals(selectCity)){
				holder.iv_prompt_select_city.setVisibility(View.VISIBLE) ;
			}else{
				holder.iv_prompt_select_city.setVisibility(View.GONE) ;
			}

			if(city.equals("深圳市")||city.equals("全国")) {

				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String city = (String) getItem(position);
						SharedUtil.saveCity(ChooseCityActivity.this, city);
						if (SharedUtil.getLocationCity(ChooseCityActivity.this).equals(SharedUtil.getCity(ChooseCityActivity.this))) {
							iv_prompt_select_city.setVisibility(View.VISIBLE);
						} else {
							iv_prompt_select_city.setVisibility(View.GONE);
						}
						notifyDataSetChanged();
						EventBean data = new EventBean(EventType.CITY_NOTIFI_TYPE);
						ClassConcrete.getInstance().postDataUpdate(data);
						ChooseCityActivity.this.finish();
					}
				});

			}else{
				convertView.setOnClickListener(null);
				holder.locationCityName.setBackgroundResource(R.color.alertdialog_line);
				holder.locationCityName.setTextColor(getResources().getColor(R.color.white));
			}

			return convertView;
		}

		class ViewHolder {
			TextView locationCityName;
			ImageView iv_prompt_select_city;

			public ViewHolder(View view) {
				locationCityName = (TextView)view.findViewById(R.id.locationCityName) ;
				iv_prompt_select_city = (ImageView)view.findViewById(R.id.iv_prompt_select_city) ;
				view.setTag(this);
			}
		}
	}
}
