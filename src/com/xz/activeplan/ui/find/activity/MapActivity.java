package com.xz.activeplan.ui.find.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 地图界面activity
 * 
 * @author johnny
 * 
 */
public class MapActivity extends BaseFragmentActivity implements
		OnClickListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private ImageView iv_datails_back;
	private TextView tvHeadTitle;

	private String mAddress = "";
	private GeoCoder mSearch;

	private View view,tipView;
	
	private Button btnGo ;
	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
		public void onGetGeoCodeResult(final GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有检索到结果
				ToastUtil.showShort("未查询出此地址!") ;
				return;
			}
			// 获取地理编码结果
			// 定义用于显示该InfoWindow的坐标点
			LatLng pt = new LatLng(result.getLocation().latitude,
					result.getLocation().longitude);

			// 定义地图状态
			MapStatus mMapStatus = new MapStatus.Builder().target(pt).zoom(18)
					.build();
			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			// 改变地图状态
			mBaiduMap.setMapStatus(mMapStatusUpdate);

//			// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//			InfoWindow mInfoWindow = new InfoWindow(view, pt, -47);
//			// 显示InfoWindow
//			mBaiduMap.showInfoWindow(mInfoWindow);

			OverlayOptions options = new MarkerOptions()
		    .position(pt)  //设置marker的位置
		    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_global_butten_map))  //设置marker图标
		    .zIndex(9)  //设置marker所在层级
		    .draggable(true);  //设置手势拖拽

			final double getLatitude = SharedUtil.getFloat(MapActivity.this, "getLatitude");
			final double getLongitude = SharedUtil.getFloat(MapActivity.this, "getLongitude");

		//将marker添加到地图上
		mBaiduMap.addOverlay(options);

			 //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
			 InfoWindow window = new InfoWindow(tipView, pt, -47);
			 //显示InfoWindow
			 mBaiduMap.showInfoWindow(window);

			 btnGo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						@SuppressWarnings("deprecation")
						Intent intent = Intent
								.getIntent("intent://map/direction?origin=latlng:"+getLatitude +","+getLongitude+"|name:当前位置&destination="+result.getAddress()+"&mode=driving®ion="+result.getAddress()+"&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
						if (isInstallByread("com.baidu.BaiduMap")) {
							startActivity(intent); // 启动调用
							Log.e("GasStation", "百度地图客户端已经安装");
						}else {
							Log.e("GasStation", "没有安装百度地图客户端");
						}
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			});
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有找到检索结果
			}
			// 获取反向地理编码结果
		}
	};

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		Utiles.setStatusBar(this);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		init();
	}

	private void init() {
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(listener);
		Intent intent = getIntent();
		if (intent != null) {
			mAddress = intent.getStringExtra("address");
			mSearch.geocode(new GeoCodeOption().city(mAddress).address(mAddress));
		}
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		tvHeadTitle.setText("地图详情");
		iv_datails_back.setOnClickListener(this);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 创建InfoWindow展示的view
		 view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_clip, null) ;
		 tipView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.location_tip, null) ;
		  btnGo = (Button)tipView.findViewById(R.id.btnGo) ;
	}
	
	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mSearch.destroy();
		mMapView.onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
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
