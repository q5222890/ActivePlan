package com.xz.activeplan.ui.find.fragment.fragments;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.AdvertisementBean;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.LiveChatRoomActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.UtilesBlannerViewPager;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.views.MyGridView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 直播——推荐
 */

public class RecommendationFragment extends BaseFragment implements ClassObserver, AdapterView.OnItemClickListener {

    private static RecommendationFragment instance = null;

    private View mView;  //整个页面的View
    private UtileStringRequst<LiveTvBean> utileStringRequst; //获取请求网络对象

    private List<LiveTvBean> listBlannr = new ArrayList<>();   //推荐轮播
    private HandlerRecommendation handlerRecommendation;          //处理播放数据
    private CommonAdapter<LiveTvBean> weekChartsAdapter;        //本周排行榜适配器
    private ArrayList<LiveTvBean> weekChartsList = new ArrayList<LiveTvBean>();        //用于存放本周排行数据
    private FragmentActivity activity;                 //fragment父类
    private PullToRefreshScrollView scrollView; //下拉刷新控件
    private SimpleDateFormat formatTime;
    private String timeUpStr;
    private String timeDolweStr;
    private int timeDonwe = 0;
    private Date date;
    private MyGridView gv_recommendanchor;  //推荐主播gridview
    private List<AdvertisementBean> mAdvertList = new ArrayList<AdvertisementBean>();//广告
    private LinearLayout llyt_weekcharts;  //显示本周排行榜LinearLayout

    private UtileStringRequst<LiveTvBean> advertisementBeanUtileStringRequst;
    private UtilesBlannerViewPager instanceViewPager;
    private List<String> listImageUrls = new ArrayList<String>();
    private View viewViewPager;
    private int page = 0;
    private boolean bBlanner = false;
    private List<String> listplayStatus = new ArrayList<>();

    public static RecommendationFragment getInstance() {
        if (instance == null) {
            instance = new RecommendationFragment();
        }
        return instance;
    }
    private void listClear() {
        if (weekChartsList != null) {
            weekChartsList.clear();
        }
        if (llyt_weekcharts != null) {
            llyt_weekcharts.removeAllViews();
        }
        listBlannr.clear();
        mAdvertList.clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ClassConcrete.getInstance().addObserver(this);
        mView = inflater.inflate(R.layout.fragment_recommendation, container, false);
        activity = getActivity();
        initViews();
        NetWorkData();
        return mView;
    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    //底部gridView点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LiveTvBean liveTvBean =  weekChartsList.get(i);
        String status = liveTvBean.getStatus();  //播放状态
        if (status.equals("回放"))
        {
            Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,liveTvBean);
        }else
        {
            Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class,liveTvBean);
        }
        Utiles.log("----------recommend----" + status);
    }

    private void initViews() {
        if (handlerRecommendation == null || handlerRecommendation == null || advertisementBeanUtileStringRequst == null) {
            handlerRecommendation = new HandlerRecommendation();
            utileStringRequst = new UtileStringRequst<>();
            advertisementBeanUtileStringRequst = new UtileStringRequst<>();
        }
        llyt_weekcharts = (LinearLayout) mView.findViewById(R.id.llyt_weekcharts);
        instanceViewPager = UtilesBlannerViewPager.getInstance();
        viewViewPager = mView.findViewById(R.id.id_ViewViewPager);

        gv_recommendanchor = (MyGridView) mView.findViewById(R.id.gridview_recommendanchor);
        gv_recommendanchor.setOnItemClickListener(this);

        scrollView = (PullToRefreshScrollView) mView.findViewById(R.id.scrollView_recommendation);
        scrollView.getLoadingLayoutProxy().setReleaseLabel("松开即可刷新");
        getTime("onPullDownToRefresh");
        getTime("onPullUpToRefresh");

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新label的设置
                scrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeDolweStr);
                scrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                getTime("onPullDownToRefresh");
                getTime("onPullUpToRefresh");
                int time = getTime("onPullDownToRefresh");
                    listClear();
                    NetWorkData();
                    timeDonwe = time;
                scrollView.onRefreshComplete();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新label的设置
                scrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeUpStr);
                scrollView.getLoadingLayoutProxy().setPullLabel("上拉刷新");
                scrollView.onRefreshComplete();
                getTime("onPullUpToRefresh");
            }
        });
        /***底部gridView*/
        weekChartsAdapter = new CommonAdapter<LiveTvBean>(getActivity(), weekChartsList, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {

                ImageView imagaView = holder.getView(R.id.iv_livecover);
                Picasso.with(getActivity()).load(liveTvBean.getCoverurl()).fit().centerCrop().centerCrop()
                        .error(R.drawable.default_rectangle_image).placeholder(R.drawable.default_rectangle_image).into(imagaView);
               // holder.setImageURL(R.id.iv_livecover, liveTvBean.getCoverurl());  //视频封面
                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());  //视频标题
                holder.setText(R.id.tv_lable, liveTvBean.getStatus());    //视频状态
            }
        };
        gv_recommendanchor.setAdapter(weekChartsAdapter);

    }

    /**
     *
     */
    @SuppressLint("InflateParams")
    private void initSplendid(ArrayList<LiveTvBean> datas) {
        if (datas == null || (datas != null && datas.size() <= 0)) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            ActionBar.LayoutParams mLayoutParams = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_weekcharts, null);
            ImageView img = (ImageView) view.findViewById(R.id.image_weekcharts_coverurl);
            TextView txt = (TextView) view.findViewById(R.id.text_weekcharts_covertitle);
            TextView tvlable = (TextView) view.findViewById(R.id.tv_lable);
            LiveTvBean bean = datas.get(i);
            Picasso.with(XZApplication.getInstance()).load(bean.getCoverurl())
                    .placeholder(R.drawable.default_details_image)
                    .error(R.drawable.default_details_image)
                    .fit().centerCrop().into(img);
            txt.setText(bean.getTitle());
            tvlable.setText(bean.getStatus());
            img.setOnClickListener(new MyOnClickListener(bean));

            llyt_weekcharts.addView(view, mLayoutParams);
        }
        llyt_weekcharts.setVisibility(View.VISIBLE);
    }

    public void NetWorkData() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
        } else {
            //banner图 轮播
            if (listBlannr.size() == 0) {
                advertisementBeanUtileStringRequst.UtileStringRequst(getActivity(), UrlsManager.URL_LIVE_BANNER + 2,
                        LiveTvBean.class, handlerRecommendation, 0);
                return;
            } else {
                if (bBlanner == false) {
                    instanceViewPager.UtilesBlannerViewPager(activity, viewViewPager, listBlannr, listImageUrls, listplayStatus, new UtilesBlannerViewPager.GetPosition() {
                        @Override
                        public void getPosition(int position) {
                            if (listBlannr.size() > 0) {
                                LiveTvBean liveTvBean = listBlannr.get(position);
                                String status = liveTvBean.getStatus();  //播放状态
                                if (status.equals("回放"))
                                {

                                    Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,liveTvBean);
                                }else
                                {
                                    Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class,liveTvBean);
                                }
                                Utiles.log("----------recommend----" + status);
                            }
                        }
                    });
                    bBlanner = true;
                } else {
                    instanceViewPager.onStartBlannr();
                }
            }
            //直播 -本周排行榜
            if (weekChartsList.size() == 0) {
                utileStringRequst.UtileStringRequst(activity, UrlsManager.URL_LIVE_RECOMMEND_WEEKCHARTS + page + "&pagesize=16",
                        LiveTvBean.class, handlerRecommendation, 1);
                return;
            } else {
                //initSplendid(weekChartsList);
            }
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

    @Override
    public void onStop() {
        super.onStop();
        if (instanceViewPager != null) {
            instanceViewPager.stopViewPager();
            bBlanner = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NetWorkData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private class HandlerRecommendation extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://头部轮播
                    List<LiveTvBean> list = advertisementBeanUtileStringRequst.list;
                    if (list.size() != 0) {
                        listBlannr.addAll(list);
                        for (int i = 0; i < listBlannr.size(); i++) {
                            listImageUrls.add(listBlannr.get(i).getCoverurl());
                            listplayStatus.add(listBlannr.get(i).getStatus());
                        }
                    }
                    NetWorkData();
                    break;
                case 1://本周排行榜
                    List<LiveTvBean> listWeek = utileStringRequst.list;
                    if (listWeek.size() != 0) {
                        weekChartsList.addAll(utileStringRequst.list);
                        if (weekChartsList.size() != 0) {
                            initSplendid(weekChartsList);
                            weekChartsAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
//                case 3://广告
//                    recommendAnchor.addAll(utileStringRequst.list);
//                    playBackAdapter.notifyDataSetChanged();
//                    break;
            }
            if (advertisementBeanUtileStringRequst.list != null) {
                advertisementBeanUtileStringRequst.list.clear();
            }
            if (utileStringRequst.list != null) {
                utileStringRequst.list.clear();
            }
            handlerRecommendation.removeMessages(msg.what);
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        private LiveTvBean mLiveTvBean;

        public MyOnClickListener(LiveTvBean bean) {
            this.mLiveTvBean = bean;
        }

        @Override
        public void onClick(View v) {
            Utiles.log("----------recommed-------" + mLiveTvBean.toString());
            String status = mLiveTvBean.getStatus();  //播放状态
            if (status.equals("回放"))
            {

                Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,mLiveTvBean);
            }else
            {
                Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class,mLiveTvBean);
            }
        }
    }


}
