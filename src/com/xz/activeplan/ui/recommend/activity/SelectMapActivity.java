package com.xz.activeplan.ui.recommend.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

/**
 * 地图界面activity
 * 
 * @author johnny
 *
 */
public class SelectMapActivity extends BaseFragmentActivity implements OnClickListener{
	private MapView mMapView;  //地图界面
	private BaiduMap mBaiduMap;  //百度地图
	private ImageView iv_datails_back ; //返回箭头
	private TextView tvHeadTitle ;    //头部显示地址
	private View view ;   //标记当前位置的控件
	private GeoCoder mSearch ;  //位置服务
	OnMapStatusChangeListener listener = new OnMapStatusChangeListener() {
	    /**
	    * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
	    * @param status 地图状态改变开始时的地图状态
	    */
	    public void onMapStatusChangeStart(MapStatus status){
	    }
	    /**
	    * 地图状态变化中
	    * @param status 当前地图状态
		 *
	    */
	    public void onMapStatusChange(MapStatus status){
	    	//定义用于显示该InfoWindow的坐标点
			LatLng pt = new LatLng(status.target.latitude, status.target.longitude);
			//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
			InfoWindow mInfoWindow = new InfoWindow(view, pt, -47);
			//显示InfoWindow
			mBaiduMap.showInfoWindow(mInfoWindow);
	    }
	    /**
	    * 地图状态改变结束
	    * @param status 地图状态改变结束后的地图状态
	    */
	    public void onMapStatusChangeFinish(MapStatus status){
	    	//定义用于显示该InfoWindow的坐标点
			LatLng pt = new LatLng(status.target.latitude, status.target.longitude);
			//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
			InfoWindow mInfoWindow = new InfoWindow(view, pt, -47);
			//显示InfoWindow
			mBaiduMap.showInfoWindow(mInfoWindow);

			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(pt)) ;
	    }
	};
	private RelativeLayout save_ly;  //保存按钮
	private String address = "" ;  //地址
	OnGetGeoCoderResultListener listener1 = new OnGetGeoCoderResultListener() {
	    public void onGetGeoCodeResult(GeoCodeResult result) {
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            //没有检索到结果
	        }
	        //获取地理编码结果
	    }

	    @Override
	    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            //没有找到检索结果
	        	ToastUtil.showShort("地理位置获取失败") ;
	        	return;
	        }
	        //获取反向地理编码结果

//	        ToastUtil.showShort("" + result.getAddress()) ;
	        address = result.getAddress() ;
	        tvHeadTitle.setText(address);

	    }
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_map);
		init();
	}
	
	@SuppressLint("InflateParams")
	private void init() {

		iv_datails_back = (ImageView)findViewById(R.id.id_ImageHeadTitleBlack) ;
		save_ly = (RelativeLayout)findViewById(R.id.save_ly) ;
		tvHeadTitle = (TextView)findViewById(R.id.id_TextViewHeadTitle) ;

		save_ly.setVisibility(View.VISIBLE);

		tvHeadTitle.setText("地图详情") ;
		iv_datails_back.setOnClickListener(this) ;

		save_ly.setOnClickListener(this);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap  = mMapView.getMap();

		//创建InfoWindow展示的view
		view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_clip, null) ;

		//设定中心点坐标
        LatLng cenpt = new LatLng(22.568688,114.062465);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
        .target(cenpt)
        .zoom(18)
        .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mBaiduMap.setOnMapStatusChangeListener(listener) ;

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener1);
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
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_ImageHeadTitleBlack:
			finish() ;
			break;
		case R.id.save_ly:
			EventBean data = new EventBean(EventType.MAP_ADDRESS_TYPE) ;
	        data.setObj(address);
			ClassConcrete.getInstance().postDataUpdate(data) ;
			finish();
			break;
		}
	}
}
