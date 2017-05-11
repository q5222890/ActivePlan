package com.xz.activeplan.ui.recommend.charts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.recommend.activity.SponsorDetailActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动--主办方排行榜
 */
public class SponsorChartsActivity extends BaseFragmentActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    private List<OrganizersBean> SponsorgridViewList;  //获取数据的list
    private CommonAdapter<OrganizersBean> SponsorgridViewAdapter;  //适配器
    private UtileStringRequst<OrganizersBean> utileStringRequst;  //请求网络
    private GridView mGridView;
    private int currpage = 0;
    private int pagesize = 20;
    private OrganizersBean mOrganizersBean;
    private UserInfoBean userInfoBean;
    private int userid;
    private boolean isFirstIn =true;
    private PullToRefreshGridView pullToRefreshGridView;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 第一次进入自动刷新
        if (isFirstIn) {
            isFirstIn = false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_organizers);
        initView();//初始化

    }

    private void initView() {
        Utiles.headManager(this,"主办方排行");
        SponsorgridViewList = new ArrayList<OrganizersBean>();
        utileStringRequst = new UtileStringRequst<OrganizersBean>();
        pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gv_sponsorcharts);
        pullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);
        mGridView = pullToRefreshGridView.getRefreshableView();
        SponsorgridViewAdapter = new CommonAdapter<OrganizersBean>
                (this, SponsorgridViewList, R.layout.gridview_charts_item) {
            @Override
            public void convert(ViewHolder holder, OrganizersBean organizersBean, int position) {
                if (!TextUtils.isEmpty(organizersBean.getHosthearurl())&&!TextUtils.isEmpty(organizersBean.getHostname())) {
                    Utiles.log("主办方头像："+organizersBean.getHosthearurl()+"，主办方名称："+organizersBean.getHostname());
                    holder.setImageURL(R.id.iv_charts_gridview, organizersBean.getHosthearurl());
                    holder.setText(R.id.tv_charts_gridview, organizersBean.getHostname());
                    mOrganizersBean = organizersBean;
                }
            }
        };
        pullToRefreshGridView.setOnRefreshListener(new pullToRefreshListener());
        mGridView.setAdapter(SponsorgridViewAdapter);
        mGridView.setOnItemClickListener(this);

        userInfoBean = SharedUtil.getUserInfo(this);
        userid = userInfoBean.getUserid();
        initData();
    }

    private void ListClean() {
        SponsorgridViewList.clear();
    }

    //访问网络数据
    private void initData() {
        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
        }

        mOrganizersBean = new OrganizersBean();
        UserInfoServiceImpl.getInstance().getHostOrderInfo(userid, mOrganizersBean.getHostid(),
                currpage, pagesize, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                        ToastUtil.showShort("获取网络数据错误：" + request + ":" + e);
                        Utiles.log("错误：" + e);
                    }

                    @Override
                    public void onResponse(String response) {

                        StatusBean statusbean = JSON.parseObject(response, StatusBean.class);
                        if (statusbean != null && statusbean.getCode() == 0) {
                            List<OrganizersBean> organnizersList = JSON.parseArray
                                    (statusbean.getData(), OrganizersBean.class);

                            if (organnizersList != null) {
                                if (currpage <= 0) {
                                    SponsorgridViewList.clear();
                                }
                                if (organnizersList.size() > 0) {
                                    currpage++;
                                }
                                SponsorgridViewList.addAll(organnizersList);
                                SponsorgridViewAdapter.notifyDataSetChanged();

                            }

                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mOrganizersBean = SponsorgridViewAdapter.getItem(position);
        if (mOrganizersBean != null) {
            Intent intent = new Intent(SponsorChartsActivity.this, SponsorDetailActivity.class);
            intent.putExtra("OrganizersBean", mOrganizersBean);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
    }

    private class pullToRefreshListener implements PullToRefreshBase.OnRefreshListener2<GridView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
            initData();
            refreshView.onRefreshComplete();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            initData();
            refreshView.onRefreshComplete();
        }
    }
}
