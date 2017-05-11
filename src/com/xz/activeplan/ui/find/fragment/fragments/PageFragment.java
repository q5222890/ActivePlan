package com.xz.activeplan.ui.find.fragment.fragments;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.LiveCategoryActivity;
import com.xz.activeplan.ui.live.activity.LiveChatRoomActivity;
import com.xz.activeplan.ui.live.activity.MoreLatestLiveActivity;
import com.xz.activeplan.ui.live.activity.MorePlaybackActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.UtilesBlannerViewPager;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * 首页已经完成布局---处理点击事件
 * 访问网络时间间隔Ok
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends BaseFragment implements View.OnClickListener, ClassObserver {
    private static PageFragment instance = null;
    /**
     * 更多直播 、更多回放
     */
    View.OnClickListener onMoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_morenewlive: //更多最新
                    startActivity(new Intent(getActivity(), MoreLatestLiveActivity.class));
                    break;
                case R.id.tv_moreplayback:  //更多回放
                    startActivity(new Intent(getActivity(), MorePlaybackActivity.class));
                    break;
            }
        }
    };
    private GridView gridViewNewLive;
    private GridView gridViewPlayBack;
    private FragmentActivity activity;
    private ImageView imageViewAdvertisement;
    private CommonAdapter<LiveTvBean> liveNewAdapter;
    private CommonAdapter<LiveTvBean> playBackAdapter;
    private UtileStringRequst<LiveTvBean> utileStringRequst;
    private HandlerLiveTvPage handlerLiveTvPage;
    private ArrayList<LiveTvBean> listBlackPlay = new ArrayList<LiveTvBean>();  //回放
    private List<LiveTvBean> listNew = new ArrayList<LiveTvBean>(); //直播
    private List<LiveTvBean> listBanner = new ArrayList<>();  //轮播
    private List<LiveTvBean> listAdvertisemen = new ArrayList<>();  //广告
    private UtileStringRequst<LiveTvBean> advertisementBeanUtileStringRequst;
    private SimpleDateFormat formatTime;
    private Date date;
    private int page = 0;
    private UtilesBlannerViewPager utilesBlannerViewPager;
    private View viewViewPager;
    private ArrayList<String> listImageUrls = new ArrayList<String>();
    private ArrayList<String> listplayStatus = new ArrayList<String>();
    private UserInfoBean userInfo;
    private boolean bBlanner = false;
    private TextView morenewlive;
    private ImageView ImageFieldactivity, ImageOutDorrs, ImageAllLive, ImageAllKind;
    private TextView moreplayback;
    private String TAG = "PageFragment";
    private SwipeRefreshLayout srl;
    private PullToRefreshScrollView scrollView;
    private String timeUpStr;
    private String timeDolweStr;

    public PageFragment() {
    }

    public static PageFragment getInstance() {
        if (instance == null) {
            instance = new PageFragment();
        }
        return instance;
    }

    private void listClear() {
        listBanner.clear();
        listNew.clear();
        listAdvertisemen.clear();
        listBlackPlay.clear();
        listImageUrls.clear();
    }

    /***
     * 在分离
     */
    public void onDetach() {
        Utiles.log("生命周期：onDetach");
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = SharedUtil.getUserInfo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        utilesBlannerViewPager = UtilesBlannerViewPager.getInstance();
        activity = getActivity();
        if (handlerLiveTvPage == null || utileStringRequst == null || advertisementBeanUtileStringRequst == null) {
            handlerLiveTvPage = new HandlerLiveTvPage();
            utileStringRequst = new UtileStringRequst<LiveTvBean>();
            advertisementBeanUtileStringRequst = new UtileStringRequst<LiveTvBean>();
        }
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            Utiles.log(" 融云断开重连 ");
            Utiles.connectRongIM(getActivity());
        }

        initView(view);
        NetWorkData();
        return view;
    }

    public void NetWorkData() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
            return;
        } else {
            //banner图 轮播
            if (listBanner.size() == 0) {
                advertisementBeanUtileStringRequst.UtileStringRequst(getActivity(), UrlsManager.URL_LIVE_BANNER + 1,
                        LiveTvBean.class, handlerLiveTvPage, 0);
                return;
            } else {
                if (bBlanner == false) {

                    utilesBlannerViewPager.UtilesBlannerViewPager(activity, viewViewPager, listBanner, listImageUrls, listplayStatus, new UtilesBlannerViewPager.GetPosition() {
                        @Override
                        public void getPosition(int position) {
                            if (listBanner.size() > 0) {
                                LiveTvBean liveTvBean = listBanner.get(position);
                                Utiles.log("livetvbean:  " + liveTvBean.toString());
                                // String status = liveTvBean.getStatus();  //播放状态
                                Intent playbackIntent = new Intent(getActivity(), TestChatRoomActivity.class);
                                playbackIntent.putExtra("data", liveTvBean);
                                startActivity(playbackIntent);
                            }
                        }
                    });
                    bBlanner = true;
                } else {
                    utilesBlannerViewPager.onStartBlannr();
                }
            }
            /**
             *最新直播
             */
            if (listNew.size() == 0) {
                utileStringRequst.UtileStringRequst(activity, UrlsManager.URL_NEWLIVE,
                        LiveTvBean.class, handlerLiveTvPage, 1);
                return;
            }
            /**
             * 固定广告
             */
            if (listAdvertisemen.size() == 0) {
                advertisementBeanUtileStringRequst.UtileStringRequst
                        (activity, UrlsManager.URL_LIVETVPAGE_TITLEBANNER + "1&param=0",
                                LiveTvBean.class, handlerLiveTvPage, 2);
                return;
            } else {
                Picasso.with(activity).load(R.drawable.default_details_image)
                        .placeholder(R.drawable.thumb).error(R.drawable.thumb).fit()
                        .centerCrop().into(imageViewAdvertisement);
            }

            /**
             * 最新回放  page=8
             */
            if (listBlackPlay.size() == 0) {
                utileStringRequst.UtileStringRequst(activity,
                        UrlsManager.URL_NEWBLACKPLAY + "&currpage=" + page + "&pagesize=20",
                        LiveTvBean.class, handlerLiveTvPage, 3);
            }

        }
    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    public void onBackPressed() {

    }

    public void initView(View view) {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
        }
        morenewlive = (TextView) view.findViewById(R.id.tv_morenewlive);  //更多最新直播
        morenewlive.setOnClickListener(onMoreClick);
        moreplayback = (TextView) view.findViewById(R.id.tv_moreplayback);  //更多回放
        moreplayback.setOnClickListener(onMoreClick);
        viewViewPager = view.findViewById(R.id.id_ViewViewPager);
        imageViewAdvertisement = (ImageView) view.findViewById(R.id.id_ImageAdvertisement);
        scrollView = (PullToRefreshScrollView) view.findViewById(R.id.ptr_scrollview);
        getTime("onPullDownToRefresh");
        getTime("onPullUpToRefresh");
        scrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        // 上拉、下拉设定
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新label的设置
                scrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeDolweStr);
                scrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                int time = getTime("onPullDownToRefresh");
                page = 0;
                listClear();
                utilesBlannerViewPager.stopViewPager();
                NetWorkData();
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新label的设置
                scrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeUpStr);
                scrollView.getLoadingLayoutProxy().setPullLabel("上拉刷新");
                scrollView.onRefreshComplete();
                getTime("onPullUpToRefresh");
                utileStringRequst.UtileStringRequst(activity, UrlsManager.URL_NEWBLACKPLAY + page + "&pagesize=4",
                        LiveTvBean.class, handlerLiveTvPage, 3);
            }
        });
        ImageFieldactivity = (ImageView) view.findViewById(R.id.id_ImageFieldactivity);
        ImageFieldactivity.setOnClickListener(this);
        ImageOutDorrs = (ImageView) view.findViewById(R.id.id_ImageOutDorrs);
        ImageOutDorrs.setOnClickListener(this);
        ImageAllLive = (ImageView) view.findViewById(R.id.id_ImageAllLive);
        ImageAllLive.setOnClickListener(this);
        ImageAllKind = (ImageView) view.findViewById(R.id.id_ImageAllKind);
        ImageAllKind.setOnClickListener(this);
        gridViewNewLive = (GridView) view.findViewById(R.id.id_GraidViewNewLive);
        gridViewPlayBack = (GridView) view.findViewById(R.id.id_GraidViewNewPlayBack);
        /**
         * 最新直播跳转事件
         */
        gridViewNewLive.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, LiveChatRoomActivity.class);
                intent.putExtra("data", listNew.get(position));
                startActivity(intent);
            }
        });
        /**
         * 最新回放视频
         */
        gridViewPlayBack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(activity, TestChatRoomActivity.class);
                intent.putExtra("data", listBlackPlay.get(position));
                startActivity(intent);
            }
        });
        liveNewAdapter = new CommonAdapter<LiveTvBean>(activity, listNew, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                // holder.setImageURL(R.id.iv_livecover, liveTvBean.getCoverurl());  //视频封面
                ImageView ivCover = holder.getView(R.id.iv_livecover);
                Picasso.with(activity).load(liveTvBean.getCoverurl()).fit().centerCrop().error(R.drawable.default_details_image)
                        .placeholder(R.drawable.default_details_image).into(ivCover);
                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());  //视频标题
                holder.setText(R.id.tv_lable, liveTvBean.getStatus());
            }
        };

        playBackAdapter = new CommonAdapter<LiveTvBean>(activity, listBlackPlay, R.layout.griditem_livecategory) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
/*                LinearLayout view = holder.getView(R.id.id_Line);
                Drawable background = view.getBackground();
                background.setAlpha(150);
                view.setBackground(background);

                long startDate = liveTvBean.getStartdate();
                String date = TimeUtils.formatDate(startDate);
                holder.setText(R.id.id_TextTime, liveTvBean.getTitle());
                holder.setText(R.id.id_TextViewContent, liveTvBean.getGambit());*/
                //异常:OOM
                //holder.setImageURL(R.id.id_ImageGrideNew, liveTvBean.getCoverurl());
                ImageView ivCover = holder.getView(R.id.iv_livecover);
                Picasso.with(activity).load(liveTvBean.getCoverurl()).fit().centerCrop().error(R.drawable.default_details_image)
                        .placeholder(R.drawable.default_details_image).into(ivCover);
                holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());  //视频标题
                holder.setText(R.id.tv_lable, liveTvBean.getStatus());
            }
        };
        gridViewNewLive.setAdapter(liveNewAdapter);
        gridViewPlayBack.setAdapter(playBackAdapter);
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
    public void onResume() {
        super.onResume();
        Utiles.log("PageFragment_onResume");
        NetWorkData();
    }


    @Override
    public void onClick(View v) {
        int livecategoryid = 0;
        switch (v.getId()) {
            case R.id.id_ImageFieldactivity://现场活动
                livecategoryid = 1;
                break;
            case R.id.id_ImageOutDorrs://户外现场
                livecategoryid = 2;
                break;
            case R.id.id_ImageAllLive://全名直播
                livecategoryid = 3;
                break;
            case R.id.id_ImageAllKind://全部分类
                livecategoryid = 0;
                break;

        }
        //跳转到视频分类
        Intent intent = new Intent(getActivity(), LiveCategoryActivity.class);
        intent.putExtra("livecategoryid", livecategoryid);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        Utiles.log("生命周期：onStop");
        if (utilesBlannerViewPager != null) {
            utilesBlannerViewPager.stopViewPager();
            bBlanner = false;
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Utiles.log("生命周期：onDestoryView");
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroyView();
    }

    class HandlerLiveTvPage extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Utiles.log("what:" + msg.what);
            switch (msg.what) {
                case 0://广告轮播
                    listBanner.addAll(advertisementBeanUtileStringRequst.list);
                    if (listBanner.size() != 0) {
                        for (int i = 0; i < listBanner.size(); i++) {
                            listImageUrls.add(listBanner.get(i).getCoverurl());
                            listplayStatus.add(listBanner.get(i).getStatus());
                        }
                    }
                    NetWorkData();
                    break;
                case 1://最新直播
                    Utiles.log("utileStringRequst.list：" + utileStringRequst.list);
                    listNew.addAll(utileStringRequst.list);
                    Utiles.log("最新直播数量：" + listNew.size());
                    if (listNew.size() != 0) {
                        liveNewAdapter.notifyDataSetChanged();
                    }
                    NetWorkData();
                    break;
                case 2://广告
                    listAdvertisemen.addAll(advertisementBeanUtileStringRequst.list);
                    NetWorkData();
                    break;
                case 3://最新回放
                    if (utileStringRequst.list != null) {
                        if (utileStringRequst.list.size() != 0) {
                            listBlackPlay.addAll(utileStringRequst.list);
                            page++;
                            playBackAdapter.notifyDataSetChanged();
                        }
                    }
                    NetWorkData();
                    break;
            }
            if (utileStringRequst.list != null) {
                utileStringRequst.list.clear();
            }
            if (advertisementBeanUtileStringRequst.list != null) {
                advertisementBeanUtileStringRequst.list.clear();
            }
            handlerLiveTvPage.removeMessages(msg.what);

        }

    }


}
