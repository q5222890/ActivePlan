package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.TicketJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.adapter.TicketAdapter;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;


/**
 * 已完成票务
 */
public class FinishedTicketActivity extends BaseFragmentActivity implements View.OnClickListener, XListView.IXListViewListener {

    private ImageView pressback;
    private TextView headertitle;
    private TextView noitem;
    private XListView mXlistView;
    private TicketAdapter ticketAdapter;
    private ArrayList<TicketBean> ticketList = new ArrayList<TicketBean>() ;
    private UserInfoBean userInfoBean ;
    private int type = 0; //1=未过期, 0=已过期
    private int currpage = 0 ;
    private int pagesize = 10 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);

        setContentView(R.layout.activity_fnished_ticket);
        initView();
    }

    private void initView() {

        pressback = (ImageView) findViewById(R.id.iv_Back);
        headertitle = (TextView) findViewById(R.id.tv_headertitle);
        noitem = (TextView) findViewById(R.id.id_TextViewNoData);
        mXlistView = (XListView) findViewById(R.id.xlistview_finished);

        mXlistView.setPullRefreshEnable(true);
        mXlistView.setXListViewListener(this);

        noitem.setText("暂无数据,请点击刷新");
        noitem.setOnClickListener(this);
        mXlistView.setEmptyView(noitem);
        pressback.setOnClickListener(this);
        headertitle.setText("已完成票务");

        ticketAdapter =new TicketAdapter(this,ticketList,type);
        mXlistView.setAdapter(ticketAdapter);

        mXlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TicketBean bean = (TicketBean)ticketAdapter.getItem(position-1);
                Intent intent = new Intent(FinishedTicketActivity.this,TicketVoumeDetalActivity.class);
                intent.putExtra("data", bean);
                startActivity(intent);
            }
        });
        if(SharedUtil.isLogin(this)){
            userInfoBean = SharedUtil.getUserInfo(this) ;
            loadData() ;
        }else{
            ToastUtil.showShort("请登录!") ;
        }
    }

    private void loadData(){

        if(userInfoBean == null){
            noitem.setText("暂无数据!");
            return;
        }

        UserInfoServiceImpl.getInstance().myTicket(userInfoBean.getUserid(), type, currpage, pagesize,new OkHttpClientManager.StringCallback(){
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(String response) {
                if(currpage <= 0){
                    mXlistView.stopRefresh();
                }else{
                    mXlistView.stopLoadMore() ;
                }
                StatusJson statusJson = new StatusJson() ;
                StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
                if(statusBean != null && statusBean.getCode() == 0){
                    TicketJson ticketJson = new TicketJson() ;
                    ArrayList<TicketBean> ticketBeans = (ArrayList<TicketBean>)ticketJson.analysisJson2Object(statusBean.getData()) ;
                    if(currpage <= 0){
                        ticketList.clear();
                    }
                    if(ticketBeans.size() < pagesize){
                        mXlistView.setPullLoadEnable(false);
                    }else{
                        mXlistView.setPullLoadEnable(true);
                    }
                    if(ticketBeans.size() > 0){
                        currpage++;
                    }
                    ticketList.addAll(ticketBeans) ;
                    ticketAdapter.notifyDataSetChanged() ;
                }else{
                    ToastUtil.showShort("数据获取失败，请刷新重试!") ;
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("数据获取失败，请刷新重试!") ;
                if(currpage <= 0){
                    mXlistView.stopRefresh();
                }else{
                    mXlistView.stopLoadMore() ;
                }
            }
        }) ;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.id_TextViewNoData:
                onRefresh();
                break;
            case R.id.iv_Back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {

        currpage =0;
        loadData();
    }

    @Override
    public void onLoadMore() {

        loadData();
    }
}
