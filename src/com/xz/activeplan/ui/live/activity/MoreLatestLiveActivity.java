package com.xz.activeplan.ui.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.live.impl.LiveInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/***
 * 所有 最新直播
 */
public class MoreLatestLiveActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshGridView gridview;
    private CommonAdapter<LiveTvBean> latestAdapter;
    private List<LiveTvBean> latestList = new ArrayList<>();
    private int currpage = 0;
    private int pagesize = 10; //每次刷新10条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_morelatestlive);
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            Utiles.log(" 融云断开重连 ");
            Utiles.connectRongIM(this);
        }
        initView();
        loadNewFreshLive();
    }

    private void initView() {

        Utiles.headManager(this,"最新直播");
        gridview = (PullToRefreshGridView) findViewById(R.id.pulltorefreshgridview);
        gridview.setMode(PullToRefreshBase.Mode.BOTH);
        gridview.setOnRefreshListener(new MyRefreshListener());
        latestAdapter = new CommonAdapter<LiveTvBean>(this, latestList, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                ImageView ivCover= holder.getView(R.id.iv_livecover);
                Picasso.with(MoreLatestLiveActivity.this).load(liveTvBean.getCoverurl()).fit().centerCrop().error(R.drawable.default_details_image)
                        .placeholder(R.drawable.default_details_image).into(ivCover);

                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());
                holder.setText(R.id.tv_anchorusername, liveTvBean.getUsername());
                holder.setText(R.id.tv_lable,liveTvBean.getStatus());
            }
        };
        gridview.setAdapter(latestAdapter);
        gridview.setOnItemClickListener(this);
        if(SharedUtil.isLogin(this)){
            Utiles.connectRongIM(this);
        }
    }

    public void loadNewFreshLive() {
        LiveInfoServiceImpl.getInstance().getNewFreshLive(currpage, pagesize,
                new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                    }

                    @Override
                    public void onResponse(String response) {
                        StatusBean statusbean = JSON.parseObject(response, StatusBean.class);
                        if (statusbean.getCode() == 0) {
                            List<LiveTvBean> newlivebean = JSON.parseArray(statusbean.getData(), LiveTvBean.class);
                            Utiles.log("newlivebean.size:更多" + newlivebean.size());
                            if (newlivebean.size()>0) {
                                if (currpage <= 0) {
                                    latestList.clear();
                                }
                                if(newlivebean.size()<pagesize)  //说明数据没有了
                                {
                                   // ToastUtil.showShort("数据已经全部加载完");
                                }
                                latestList.addAll(newlivebean);
                                latestAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtil.showShort("数据获取失败，请刷新重试!");
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          //直播
            Intent intent = new Intent(MoreLatestLiveActivity.this, LiveChatRoomActivity.class);
            intent.putExtra("data", latestList.get(position));
            startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        Utiles.log("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Utiles.log("onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Utiles.log("onResume");
        super.onResume();
    }

    private class MyRefreshListener implements PullToRefreshBase.OnRefreshListener2<GridView> {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
            currpage =0;
            //loadNewFreshLive();
            refreshView.onRefreshComplete();
            latestAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            if (latestList.size() == 0) {
                currpage = 0;
            } else {
                currpage++;
                loadNewFreshLive();
            }
            refreshView.onRefreshComplete();
            latestAdapter.notifyDataSetChanged();
        }
    }
}
