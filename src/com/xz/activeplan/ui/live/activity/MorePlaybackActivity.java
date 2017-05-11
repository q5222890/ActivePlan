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
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MorePlaybackActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private UtileStringRequst<LiveTvBean> utileStringRequst;
    private CommonAdapter<LiveTvBean> playbackAdapter;
    private List<LiveTvBean> playbackList = new ArrayList<>();
    private int currpage = 0;
    private int pagesize = 10;
    private View view_top;
    private PullToRefreshGridView pulltorefreshgridview;

    public MorePlaybackActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_moreplayback);
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            Utiles.log(" 融云断开重连 ");
            Utiles.connectRongIM(this);
        }
        initView();
        loadPlayBack();  //加载网络数据
    }

    private void initView() {
        view_top = findViewById(R.id.view_top);
        view_top.setVisibility(View.VISIBLE);
        Utiles.headManager(this, "最新回放");
        pulltorefreshgridview = (PullToRefreshGridView) findViewById(R.id.pulltorefreshgridview);
        pulltorefreshgridview.setOnItemClickListener(this);
        pulltorefreshgridview.setMode(PullToRefreshBase.Mode.BOTH);
        pulltorefreshgridview.setOnRefreshListener(new OnRefresh());
        playbackAdapter = new CommonAdapter<LiveTvBean>(this, playbackList, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                ImageView ivCover = holder.getView(R.id.iv_livecover);
                Picasso.with(MorePlaybackActivity.this).load(liveTvBean.getCoverurl()).fit().centerCrop().error(R.drawable.default_details_image)
                        .placeholder(R.drawable.default_details_image).into(ivCover);

                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());
                holder.setText(R.id.tv_anchorusername, liveTvBean.getUsername());
                holder.setText(R.id.tv_lable, liveTvBean.getStatus());
            }
        };
        pulltorefreshgridview.setAdapter(playbackAdapter);
    }

    /**
     * 加载数据
     */
    public void loadPlayBack() {
        LiveInfoServiceImpl.getInstance().getMorePlayback(currpage, pagesize,
                new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                    }

                    @Override
                    public void onResponse(String response) {
                        StatusBean statusbean = JSON.parseObject(response, StatusBean.class);
                        if (statusbean.getCode() == 0) {
                            List<LiveTvBean> playbackbean = JSON.parseArray(statusbean.getData(), LiveTvBean.class);
                            Utiles.log("playbackbean.size:" + playbackbean.size());

                            if (playbackbean != null) {
                                if (currpage <= 0) {
                                    playbackList.clear();
                                }
                                if (playbackbean.size() < pagesize) {

                                }
                                playbackList.addAll(playbackbean);
                                playbackAdapter.notifyDataSetChanged();
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
        //视频播放
        LiveTvBean liveTvBean = playbackList.get(position);
        Intent intent = new Intent(MorePlaybackActivity.this, TestChatRoomActivity.class);
        intent.putExtra("data", liveTvBean);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utiles.log("更多回放 onResume ");
    }

    private class OnRefresh implements PullToRefreshBase.OnRefreshListener2<android.widget.GridView> {


        @Override
        public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
            //下拉刷新
            currpage = 0;
            loadPlayBack();
            pulltorefreshgridview.onRefreshComplete();
            playbackAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            if (playbackList.size() == 0) {
                currpage = 0;
            } else {
                currpage++;
                loadPlayBack();
            }
            pulltorefreshgridview.onRefreshComplete();
            playbackAdapter.notifyDataSetChanged();
        }
    }
}