package com.xz.activeplan.ui.recommend.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xz.activeplan.R;
import com.xz.activeplan.utils.PoiOverlay;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import java.util.ArrayList;
import java.util.List;


/**
 * 演示poi搜索功能
 */
public class PoiSearchActivity extends FragmentActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener, View.OnClickListener {
    private PoiSearch mPoiSearch = null;   //poi检索
    private SuggestionSearch mSuggestionSearch = null;  //建议检索结果
    private MapView mMapView;
    private BaiduMap mBaiduMap = null;  //百度地图
    private AutoCompleteTextView keyWorldsView = null;  //地址输入框
    private int loadIndex = 0;   //下标
    private ImageView iv_backspace;  //返回键

    int searchType = 0;  // 搜索的类型，在显示时区分

    private PopupWindow pop;
    private String address = "";

    private LocationClient mLocationClient;
    private MyLocationListenner listener = new MyLocationListenner();
    private ImageView iv_deletetext;
    private String mycity = "";
    private boolean isFirstLoc = true; // 是否首次定位
    private ListView mListView;
    private ArrayAdapter<String> sugAdapter = null;
    private List<String> suggest;
    private String addrStr = "";
    private Geocoder geocoder; //位置服务

    public PoiSearchActivity() {
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_poisearch);
        initView();
    }

    private void initView() {
        iv_backspace = (ImageView) findViewById(R.id.iv_backspace);
        iv_backspace.setOnClickListener(this);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mListView = (ListView) findViewById(R.id.listview_address);
        iv_deletetext = (ImageView) findViewById(R.id.iv_deletetext);
        iv_deletetext.setOnClickListener(this);
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        keyWorldsView.setOnClickListener(this);
        keyWorldsView.clearListSelection(); //清除选择列表
        suggest = new ArrayList<String>();
        sugAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);  //至少输入1个字才会提示
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap(); //获取地图界面
        mBaiduMap.setMyLocationEnabled(true); //开启定位图层

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                Utiles.log("当前城市：" + mycity);
                if (!TextUtils.isEmpty(mycity)) {
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(cs.toString()).city(mycity));
                } else {
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(cs.toString()).city("深圳市"));
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //点击选中地址 保存到搜索框
                String selectedaddress = sugAdapter.getItem(position);
                keyWorldsView.setText(selectedaddress);
//                EventBean data = new EventBean(EventType.MAP_ADDRESS_TYPE);
//                data.setObj(address);
//                ClassConcrete.getInstance().postDataUpdate(data);
//                finish();
            }
        });
    }

    /**
     * 响应城市内搜索按钮点击事件
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
//        searchType = 1;
        String keystr = keyWorldsView.getText().toString();
//        mPoiSearch.searchInCity((new PoiCitySearchOption())
//                .city(mycity).keyword(keystr).pageNum(loadIndex));
        EventBean data = new EventBean(EventType.MAP_ADDRESS_TYPE);
        data.setObj(keystr);
        ClassConcrete.getInstance().postDataUpdate(data);
        mListView.setVisibility(View.GONE);
        finish();
    }

    public void goToNextPage(View v) {
        loadIndex++;
        searchButtonProcess(null);
    }

    /**
     * 响应周边搜索按钮点击事件
     *
     * @param v
     */
//    public void searchNearbyProcess(View v) {
//        searchType = 2;
//        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(keyWorldsView.getText()
//                .toString()).sortType(PoiSortType.distance_from_near_to_far).location(center)
//                .radius(radius).pageNum(loadIndex);
//        mPoiSearch.searchNearby(nearbySearchOption);
//    }

    /**
     * 响应区域搜索按钮点击事件
     *
     * @param v
     */
//    public void searchBoundProcess(View v) {
//        searchType = 3;
//        mPoiSearch.searchInBound(new PoiBoundSearchOption().bound(searchbound)
//                .keyword(keyWorldsView.getText().toString()));
//    }


    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param result
     */
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            ToastUtil.showShort("未找到结果");
            return;
        }
        result.getAllPoi();
        for (PoiInfo info : result.getAllPoi()) {
            Utiles.log("address:" + info.address + "name:" + info.name);
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
//            switch (searchType) {
//                case 2:
//                    showNearbyArea(center, radius);
//                    break;
//                case 3:
//                    showBound(searchbound);
//                    break;
//                default:
//                    break;
//            }

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            ToastUtil.showShort("未找到结果,请更改关键字重试");
        }
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     *
     * @param result
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
        address = result.getAddress() + result.getName();
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PoiSearchActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            //点击地图上显示的结果数据进行选择
            View view = LayoutInflater.from(PoiSearchActivity.this).inflate(R.layout.popwindow_mapaddress, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_name_address);
            Button cancel = (Button) view.findViewById(R.id.but_cancelselect);
            Button confirm = (Button) view.findViewById(R.id.but_confirmselect);
            WindowManager wm = this.getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();
            pop = new PopupWindow(view, width * 7 / 10, ActionBar.LayoutParams.WRAP_CONTENT, true);
            pop.setBackgroundDrawable(getResources().getDrawable(R.color.light_blue));
            pop.setFocusable(true);
            pop.setTouchable(true);
            pop.showAtLocation(getCurrentFocus(), Gravity.CENTER, 0, 0);
            tv.setText(address);
            cancel.setOnClickListener(this);
            confirm.setOnClickListener(this);
            Utiles.log("搜索结果   详细地址:" + result.getAddress() + result.getName());

        }
    }


    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param res
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                String add = info.city + info.district + info.key;
                suggest.add(add);
            }
        }
        sugAdapter = new ArrayAdapter<String>(PoiSearchActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
        mListView.setVisibility(View.VISIBLE);
        mListView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backspace:
                finish();
                break;
            case R.id.searchkey:
                keyWorldsView.setHint("");
                break;
            case R.id.but_cancelselect:
                pop.dismiss();
                break;
            case R.id.but_confirmselect:
                EventBean data = new EventBean(EventType.MAP_ADDRESS_TYPE);
                data.setObj(address);
                ClassConcrete.getInstance().postDataUpdate(data);
                finish();
                break;
            case R.id.iv_deletetext:
                keyWorldsView.setText("");
                break;
            case R.id.search:
                break;
        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
//             if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
//             }
            return true;
        }
    }

    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                Utiles.log("当前坐标：" + ll);
            }

            mycity = bdLocation.getCity();
            String addrStr = bdLocation.getAddrStr();//当前完整地址（包括国家）
            String countrya = bdLocation.getCountry(); //国家
            String province = bdLocation.getProvince(); //省
            String number = bdLocation.getStreetNumber(); //街道编号
            String district = bdLocation.getDistrict(); //地区
            String street = bdLocation.getStreet(); //街道
            String floor = bdLocation.getFloor(); //
            String describe = bdLocation.getLocationDescribe();
            String add = province + mycity + district + street + number;

            List<Poi> poilist = bdLocation.getPoiList();
            Utiles.log("定位地址：" + add);
        }

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mLocationClient.stop(); //销毁定位
        mBaiduMap.setMyLocationEnabled(false); //关闭定位图层
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
