package com.xz.activeplan.ui.find.fragment.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.LiveChatRoomActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 直播---关注页面
 * A simple {@link Fragment} subclass.
 */
public class GuanZhuFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static GuanZhuFragment instance = null;
    private GridView gridView;
    private List<LiveTvBean> list = new ArrayList<LiveTvBean>();
    private CommonAdapter<LiveTvBean> commonAdapter;
    private UtileStringRequst<LiveTvBean> utileStringRequst;

    private FragmentActivity activity;
    private HandlerLiveGuanZhu handlerLiveGuanZhu;
    private SimpleDateFormat formatTime;
    private String timeUpStr;
    private String timeDolweStr;
    private Date date;
    private int page = 0;
    private PullToRefreshGridView mPullToRefreshGridView;

    public GuanZhuFragment() {
    }

    public static GuanZhuFragment getInstance() {
        if (instance == null) {
            instance = new GuanZhuFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guan_zhu_blank, container, false);
        initView(view);//初始化
        NetWorkData();//加载网络数据
        return view;
    }

    private void listClear() {
        if (list.size() > 0) {
            list.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(View view) {
        activity = getActivity();
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
        }
        handlerLiveGuanZhu = new HandlerLiveGuanZhu();
        utileStringRequst = new UtileStringRequst<LiveTvBean>();
        mPullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.id_PullRefreshGridView_GuanZhu);
        gridView = mPullToRefreshGridView.getRefreshableView();
        getTime("onPullDownToRefresh");
        getTime("onPullUpToRefresh");

        mPullToRefreshGridView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新" );
        getTime("onPullDownToRefresh");
        getTime("onPullUpToRefresh");

//            mPullToRefreshGridView.getLoadingLayoutProxy().setReleaseLabel(
//                    "松开即可刷新" );
        commonAdapter = new CommonAdapter<LiveTvBean>(getActivity(), list, R.layout.griditem_livecategory) {
            @Override
            public void convert(final ViewHolder holder, LiveTvBean liveTvBean, int position) {
                //视频封面
                holder.setImageURL(R.id.iv_livecover, liveTvBean.getCoverurl());
                //标题
                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());
                //作者名
                holder.setText(R.id.tv_anchorusername, liveTvBean.getUsername());
                //标签
                holder.setText(R.id.tv_lable, liveTvBean.getStatus());
            }
        };
        gridView.setAdapter(commonAdapter);
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);

        gridView.setOnItemClickListener(this);
        mPullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

                mPullToRefreshGridView.getLoadingLayoutProxy().setLastUpdatedLabel(  "上次刷新时间:" +timeDolweStr);
                mPullToRefreshGridView.getLoadingLayoutProxy().setPullLabel( "下拉刷新");
                int time = getTime("onPullDownToRefresh");
                    listClear();
                    NetWorkData();
                    //commonAdapter.notifyDataSetChanged();
                    page=0;
                mPullToRefreshGridView.onRefreshComplete();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                // 刷新label的设置
                mPullToRefreshGridView.getLoadingLayoutProxy().setLastUpdatedLabel(  "上次刷新时间:" +timeUpStr);
                mPullToRefreshGridView.getLoadingLayoutProxy().setPullLabel( "上拉刷新");
                mPullToRefreshGridView.onRefreshComplete();
                getTime("onPullUpToRefresh");
                NetWorkData();
                mPullToRefreshGridView.onRefreshComplete();
            }
        });
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
            Utiles.log(" Tiem:" + i);
            timeDolweStr = timeStr;
            return i;
        }
        return 0;
    }

    /**
     * 通过网络加载数据
     */
    public void NetWorkData() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
            return;
        } else {
            if (list.size() == 0) {
                Utiles.log("------guanzhu---000-" + list.size());
                UserInfoBean userInfo = SharedUtil.getUserInfo(getActivity());
                if (null != userInfo) {
                    String url = UrlsManager.URL_FOLLOW_LIST + page + "&pagesize=16&userid=" + userInfo.getUserid();
                    utileStringRequst.UtileStringRequst(getActivity(), url,
                            LiveTvBean.class, handlerLiveGuanZhu, 0);
                    Utiles.log("--------guanzhu----url---" + url);
                }
            } else {
                commonAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onBackPressed() {

    }

    /**
     * Item 点击跳转到视频播放页面
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        LiveTvBean liveTvBean = list.get(position);
        Utiles.log("-----guanzhu--" + liveTvBean.toString());
        String status = liveTvBean.getStatus();  //播放状态
        if (status.equals("回放")) {
            Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class, liveTvBean);
        } else {
            Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class, liveTvBean);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class HandlerLiveGuanZhu extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<LiveTvBean> list = utileStringRequst.list;
                    Utiles.log("-------guanzhu--------" + utileStringRequst.list);
                    if (list.size() != 0) {
                        page++;
                        GuanZhuFragment.this.list.addAll(utileStringRequst.list);
                        commonAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            if (utileStringRequst.list != null) {
                utileStringRequst.list.clear();
            }
            handlerLiveGuanZhu.removeMessages(msg.what);
        }
    }


}
