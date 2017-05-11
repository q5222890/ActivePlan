package com.xz.activeplan.ui.personal.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.LiveChatRoomActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.ui.personal.activity.LiveManagerInfoActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.MyGridView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的直播管理  碎片
 */
@SuppressLint("ValidFragment")
public class LiveManagerFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    private TextView tvNothing;
    private GridView gridView;
    private CommonAdapter<LiveTvBean> MyVideoCommonAdapter;
    private CommonAdapter<LiveTvBean> BuyVideoCommonAdapter;
    private List<LiveTvBean> listLives = new ArrayList<>();  //我的的视频列表
    private List<LiveTvBean> BuyListLives = new ArrayList<>();
    private int page = 0;
    private int pageSize = 8;
    private PullToRefreshScrollView pullView;
    private BaseFragmentActivity activity;
    private int userid;
    private int type;
    private CustomProgressDialog dialog;

    public LiveManagerFragment() {

    }

    public LiveManagerFragment(int type, int userid) {
        this.userid = userid;
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseFragmentActivity) getActivity();
        dialog = new CustomProgressDialog(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_live_manager, container, false);
        initView(root);
        return root;
    }

    /***
     * 初始化视图
     *
     * @param root
     */
    private void initView(View root) {
        tvNothing = (TextView) root.findViewById(R.id.tv_noData);
        tvNothing.setOnClickListener(this);
        gridView = (MyGridView) root.findViewById(R.id.liveManager_gridView);
        pullView = (PullToRefreshScrollView) root.findViewById(R.id.liveManager_pull);
        pullView.getLoadingLayoutProxy().setReleaseLabel("松开即可刷新");
        pullView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //下拉刷新
                if (type == 1)   //刷新我的视频
                {
                    page = 0;
                    pullView.onRefreshComplete();
                    MyVideoCommonAdapter.notifyDataSetChanged();
                }else
                {
                    page = 0;
                    pullView.onRefreshComplete();
                    BuyVideoCommonAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //上拉加载更多
                if (type == 1)   //刷新我的视频
                {
                    if (listLives.size() == 0) {
                        page = 0;
                    } else {
                        page++;
                        loadMyVideoData();
                    }
                    pullView.onRefreshComplete();//刷新完成
                    MyVideoCommonAdapter.notifyDataSetChanged();
                }else    //加载更多 购买视频
                {
                    if (BuyListLives.size() == 0)
                    {
                        page= 0;
                    }else {
                        page++;
                        loadBuyVideoData();
                    }
                    pullView.onRefreshComplete();//刷新完成
                    BuyVideoCommonAdapter.notifyDataSetChanged();
                }
            }
        });

        Utiles.log("type   " + type);
        initAdapter();
        gridView.setOnItemClickListener(this);
    }


    /**
     * 加载我的视频
     */
    private void loadMyVideoData() {
        UserInfoBean userinfoBean = SharedUtil.getUserInfo(activity);
        if (userinfoBean == null) {
            return;
        }
        String url = UrlsManager.URL_VIDEO_LIST + page + "&pagesize=" + pageSize + "&userid=" + userinfoBean.getUserid();
        Utiles.log("------我的视频 -------" + url.toString());
        dialog.show();
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                tvNothing.setText("点击重新加载...");
                gridView.setEmptyView(tvNothing);
            }

            @Override
            public void onResponse(String response) {
                Utiles.log("------直播管理 response-------" + response.toString());
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    dialog.cancel();
                    List<LiveTvBean> list = JSON.parseArray(statusBean.getData(), LiveTvBean.class);

                    if (list.size() > 0) {
                        Utiles.log("------直播管理-------" + list.toString());
                        listLives.addAll(list);
                        MyVideoCommonAdapter.notifyDataSetChanged();
                    } else {
                        tvNothing.setText("暂无数据");
                        pullView.setVisibility(View.GONE);
                        gridView.setEmptyView(tvNothing);
                    }
                }
            }
        });
    }

    /**
     * 加载购买视频
     */
    private void loadBuyVideoData() {

        UserInfoBean userinfoBean = SharedUtil.getUserInfo(activity);
        if (userinfoBean == null) {
            return;
        }
        String url = UrlsManager.URL_MY_BUYVIDEO + page + "&pagesize=" + pageSize + "&userid=" + userinfoBean.getUserid();
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                tvNothing.setText("点击重新加载...");
                gridView.setEmptyView(tvNothing);

            }

            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    List<LiveTvBean> list = JSON.parseArray(statusBean.getData(), LiveTvBean.class);
                    Utiles.log("------购买视频-------" + list.size());
                    if (list.size() > 0) {
                        BuyListLives.addAll(list);
                        BuyVideoCommonAdapter.notifyDataSetChanged();
                        Utiles.log("------购买视频-----11--" + listLives.size());
                    } else {
                        tvNothing.setText("您还没有购买视频记录~");
                        pullView.setVisibility(View.GONE);
                        gridView.setEmptyView(tvNothing);
                    }
                }
            }
        });
    }

    /**
     * 加载适配器
     */
    private void initAdapter() {
        MyVideoCommonAdapter = new CommonAdapter<LiveTvBean>(activity, listLives, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean bean, int position) {
                holder.setImageURL(R.id.iv_livecover, bean.getCoverurl());  //视频封面
                holder.setText(R.id.tv_livetitle, bean.getTitle());  //视频标题
                holder.setText(R.id.tv_lable, "回放");  //标签（自己直播完后的视频都是回放视频）
                holder.getView(R.id.tv_lable).setVisibility(View.GONE);
            }
        };
        BuyVideoCommonAdapter = new CommonAdapter<LiveTvBean>(activity, BuyListLives, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean bean, int position) {
                holder.setImageURL(R.id.iv_livecover, bean.getCoverurl());  //视频封面
                holder.setText(R.id.tv_livetitle, bean.getTitle());  //视频标题
                holder.setText(R.id.tv_lable, "回放");  //标签（自己直播完后的视频都是回放视频）
                holder.getView(R.id.tv_lable).setVisibility(View.GONE);
            }
        };
        if (type == 1)   //我的视频
        {
            gridView.setAdapter(MyVideoCommonAdapter);
            if (listLives.size() == 0) {
                loadMyVideoData();
            }
            MyVideoCommonAdapter.notifyDataSetChanged();
        } else {
            gridView.setAdapter(BuyVideoCommonAdapter);
            if (BuyListLives.size() == 0) {
                loadBuyVideoData();
            }

            BuyVideoCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvNothing) {

        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (type == 1) {
            LiveTvBean bean = listLives.get(position);
            Intent intent = new Intent(activity, LiveManagerInfoActivity.class);
            intent.putExtra("data", bean);
            startActivity(intent);
        } else {
            LiveTvBean bean = BuyListLives.get(position);
            String status = bean.getStatus();  //播放状态
            if (status.equals("回放")) {
                Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class, bean);
            } else {
                Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class, bean);
            }
        }
    }


}
