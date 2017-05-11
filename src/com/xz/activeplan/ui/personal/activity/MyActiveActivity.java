package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.adapter.HostActiveAdapter;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;

public class MyActiveActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener, XListView.IXListViewListener {
    private TextView mTxtNoData ;
    private XListView listView;

    private View mView;

    private HostActiveAdapter mHostActiveAdapter ;

    private ArrayList<ActiveinfosBean> arr = new ArrayList<ActiveinfosBean>() ;

    private UserInfoBean userInfoBean ;
    private int type = -1; //1=已发布活动, 0=已结束活动
    private int currpage = 0;
    private int pagesize = 10 ;
    private int hostid = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ClassConcrete.getInstance().addObserver(this);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_my_active);
        initView();

    }

    private void initView() {
         type = ActiveManageActivity.type;
         hostid = ActiveManageActivity.hostid;
        String stringExtra = getIntent().getStringExtra(Utiles.DATA);
        //返回框背景
        View viewHeald=findViewById(R.id.relativeLayout_toolbar);
        viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
        //返回键
        ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(this);
        imageBlack.setImageResource(R.drawable.ic_nav_back_white);
        TextView textViewHealeTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        textViewHealeTitle.setTextColor(getResources().getColor(R.color.white));
        //头部标题
        if (stringExtra.equals("AlreadyReleased")){
            textViewHealeTitle.setText(getResources().getString(R.string.string_announcement));
        }else {
            textViewHealeTitle.setText(getResources().getString(R.string.string_finished));
        }

        mTxtNoData = (TextView)findViewById(R.id.txt_nodata) ;
        listView = (XListView)findViewById(R.id.id_XListview);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this) ;
        mTxtNoData.setOnClickListener(this) ;
        listView.setEmptyView(mTxtNoData) ;
        mHostActiveAdapter = new HostActiveAdapter(this, arr) ;
        listView.setAdapter(mHostActiveAdapter) ;

        if(SharedUtil.isLogin(this)){
            userInfoBean = SharedUtil.getUserInfo(this) ;
            loadData() ;
        }else{
            ToastUtil.showShort("请登录!") ;

        }
    }
    private void loadData(){
        UserInfoServiceImpl.getInstance().hostPostActive(hostid, type, currpage, pagesize, new OkHttpClientManager.StringCallback(){
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
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.QUITE_CHART_TYPE) {
            finish();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.txt_nodata:
                onRefresh() ;
                break;
        }
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
}
