package com.xz.activeplan.ui.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.MyGridView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 直播分类视频页面
 * <p/>
 * 含  活动现场、户外现场、全名直播、全部分类
 */
public class LiveCategoryActivity extends Activity implements View.OnClickListener {

    private int livecategoryid;
    private ImageView iv_back;
    private TextView headertitle;
    private int currpage = 0;
    private int pagesize = 16;
    private MyGridView myGridView;
    private PullToRefreshScrollView scrollView;
    private CommonAdapter<LiveTvBean> categoryAdapter;
    private List<LiveTvBean> categorylist = new ArrayList<>();
    private SimpleDateFormat formatTime;
    private Date date;
    private String timeUpStr;
    private String timeDolweStr;
    private OnRefreshListener mOnRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_livecategory);
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            Utiles.log(" 融云断开重连 ");
            Utiles.connectRongIM(this);
        }
        initView();
        getIntentData();

    }

    /**
     * 得到Intent 数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            livecategoryid = intent.getIntExtra("livecategoryid", 0);
        }
        switch (livecategoryid) {
            case 1:   //活动现场
                headertitle.setText("活动现场");
                initNetData(1, 0, 10);
                break;
            case 2:   //户外现场
                headertitle.setText("户外现场");
                initNetData(2, 0, 10);
                break;
            case 3:   //全民直播
                headertitle.setText("全民直播");
                initNetData(3, 0, 10);
                break;
            case 0:   //全部分类
                headertitle.setText("全部分类");
                initNetData(0, 0, 10);
                break;
        }
    }

    /**
     * 加载网络数据
     * categoryid=0&currpage=0&pagesize=16
     */
    private void initNetData(int livecategoryid, int currPage, int pageSize) {

        this.livecategoryid = livecategoryid;
        this.currpage = currPage;
        this.pagesize = pageSize;
        OkHttpClientManager.Param paramcategoryid = new OkHttpClientManager.Param("categoryid", livecategoryid + "");
        OkHttpClientManager.Param paramcurrpage = new OkHttpClientManager.Param("currpage", currPage + "");
        OkHttpClientManager.Param parampagesize = new OkHttpClientManager.Param("pagesize", pageSize + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_LIVECATEGOTY, new MyStringCallBack(),
                paramcategoryid, paramcurrpage, parampagesize);

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_Back);
        headertitle = (TextView) findViewById(R.id.tv_headertitle);
        iv_back.setOnClickListener(this);
        myGridView = (MyGridView) findViewById(R.id.gridview_livecategory);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mOnRefreshListener = new OnRefreshListener();
        scrollView.setOnRefreshListener(mOnRefreshListener);
        categoryAdapter = new CommonAdapter<LiveTvBean>(this, categorylist, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {

                // holder.setImageURL(R.id.iv_livecover, liveTvBean.getCoverurl()); //内存溢出
                ImageView cover = holder.getView(R.id.iv_livecover);
                if (!TextUtils.isEmpty(liveTvBean.getCoverurl())) {
                    Picasso.with(LiveCategoryActivity.this).load(liveTvBean.getCoverurl()).centerCrop().fit()
                            .error(R.drawable.default_details_image)
                            .placeholder(R.drawable.default_details_image)
                            .into(cover);
                }
                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());
                holder.setText(R.id.tv_lable, liveTvBean.getStatus());
                // holder.setText(R.id.tv_anchorusername, liveCategoryBean.getUsername());

            }
        };

        myGridView.setAdapter(categoryAdapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiveTvBean liveTvBean = categorylist.get(position);
                Utiles.log("---liveTvBean:  " + liveTvBean.toString());
                if (RongIM.getInstance().getCurrentConnectionStatus()
                        .equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                    if (liveTvBean.getStatus().equals("直播")) {
                        Intent liveIntent = new Intent(LiveCategoryActivity.this, LiveChatRoomActivity.class);
                        liveIntent.putExtra("data", liveTvBean);
                        startActivity(liveIntent);
                    } else {
                        Intent liveIntent = new Intent(LiveCategoryActivity.this, TestChatRoomActivity.class);
                        liveIntent.putExtra("data", liveTvBean);
                        startActivity(liveIntent);
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_Back:
                finish();
                break;
        }
    }

    /**
     * 获取刷新时间
     */
    private int getTime(String RefreshTime) {
        long time = System.currentTimeMillis();
        Date d1 = new Date(time);
        if (formatTime == null) {
            formatTime = new SimpleDateFormat("HH:mm:ss");
            date = d1;
        }
        String timeStr = formatTime.format(d1);
        if (RefreshTime.equals("onPullUpToRefresh")) {
            timeUpStr = timeStr;
        } else {
            long startTime = date.getTime();
            int i = (int) ((time - startTime) / 1000);
            if (i > 30) {
                date = d1;
            }
            timeDolweStr = timeStr;
            return i;
        }
        return 0;
    }

    private class MyStringCallBack implements OkHttpClientManager.StringCallback {

        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String response) {
            StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
            if (statusBean.getCode() == 0) {
                List<LiveTvBean> liveTvBeanList = JSON.parseArray(statusBean.getData(), LiveTvBean.class);
                for (LiveTvBean liveTvBean : liveTvBeanList) {
                    Utiles.log("------------LiveCategory---------" + liveTvBean.toString());
                }
                if (liveTvBeanList != null) {
                    if (currpage <= 0) {
                        categorylist.clear();
                    }
                    if (liveTvBeanList.size() > 0) {
                        currpage++;
                    }
                }

                categorylist.addAll(liveTvBeanList);
                categoryAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.showShort("数据获取失败，请刷新重试!");
            }
        }
    }

    public class OnRefreshListener implements PullToRefreshBase.OnRefreshListener2<ScrollView> {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            // 刷新label的设置
            scrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeDolweStr);
            scrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            int time = getTime("onPullDownToRefresh");
            currpage = 0;
            initNetData(livecategoryid, currpage, pagesize);
            scrollView.onRefreshComplete();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            scrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeUpStr);
            scrollView.getLoadingLayoutProxy().setPullLabel("上拉显示更多内容");
            getTime("onPullUpToRefresh");
            initNetData(livecategoryid, currpage, pagesize);
            scrollView.onRefreshComplete();
        }
    }
}
