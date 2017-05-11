package com.xz.activeplan.ui.personal.activity;

import android.app.Activity;
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
import com.xz.activeplan.ui.personal.adapter.TicketAdapter;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;

public class UnfinishedTicketActivity extends Activity implements View.OnClickListener, XListView.IXListViewListener {

    private TextView noitem;
    private XListView mListView;
    private TicketAdapter mTicketAdapter ;
    private ArrayList<TicketBean> arr = new ArrayList<TicketBean>() ;
    private UserInfoBean userInfoBean ;
    private int type = 1;//1=未过期, 0=已过期
    private int currpage = 0;
    private int pagesize = 10 ;
    private TextView title;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_unfinished_ticket);
        initView();
    }

    private void initView() {
        noitem = (TextView) findViewById(R.id.tv_noitem);
        mListView = (XListView) findViewById(R.id.xlistview_unfinished);
        noitem.setText("暂无数据，请点击刷新");
        noitem.setOnClickListener(this);

        title = (TextView) findViewById(R.id.tv_headertitle);
        back = (ImageView) findViewById(R.id.iv_Back);
        title.setText("未完成票务");
        back.setOnClickListener(this);

        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.setEmptyView(noitem);

        mTicketAdapter =new TicketAdapter(this,arr,type);
        mListView.setAdapter(mTicketAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TicketBean bean = (TicketBean)mTicketAdapter.getItem(position-1);
                Intent intent = new Intent(UnfinishedTicketActivity.this,TicketVoumeDetalActivity.class);
                intent.putExtra("data", bean);
                Utiles.log("TicketBean:"+bean);
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

    private void loadData() {

        if(userInfoBean == null){
            return;
        }

        UserInfoServiceImpl.getInstance().myTicket(userInfoBean.getUserid(), type, currpage, pagesize,new OkHttpClientManager.StringCallback(){
            @Override
            public void onResponse(String response) {
                if(currpage <= 0){
                    mListView.stopRefresh();
                }else{
                    mListView.stopLoadMore() ;
                }

                StatusJson statusJson = new StatusJson() ;
                StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
                if(statusBean != null && statusBean.getCode() == 0){
                    TicketJson ticketJson = new TicketJson() ;
                    ArrayList<TicketBean> ticketBeans = (ArrayList<TicketBean>)ticketJson.analysisJson2Object(statusBean.getData()) ;

                    Utiles.log("未完成票务ticketBeans:"+ticketBeans);
                    if(currpage <= 0){
                        arr.clear();
                    }
                    if(ticketBeans.size() < pagesize){
                        mListView.setPullLoadEnable(false);
                    }else{
                        mListView.setPullLoadEnable(true);
                    }
                    if(ticketBeans.size() > 0){
                        currpage++;
                    }
                    arr.addAll(ticketBeans) ;
                    mTicketAdapter.notifyDataSetChanged() ;
                }else{
                    ToastUtil.showShort("数据获取失败，请刷新重试!") ;
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("数据获取失败，请刷新重试!") ;
                if(currpage <= 0){
                    mListView.stopRefresh();
                }else{
                    mListView.stopLoadMore() ;
                }
            }
        }) ;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_noitem:
                onRefresh() ;
                break;
            case R.id.iv_Back:
                finish();
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
