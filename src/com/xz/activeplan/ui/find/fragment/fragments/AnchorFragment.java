package com.xz.activeplan.ui.find.fragment.fragments;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.activity.TommorrowPersonActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.UtilesBlannerViewPager;
import com.xz.activeplan.views.MyGridView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 直播----主播页面
 * A simple {@link Fragment} subclass.
 */
public class AnchorFragment extends BaseFragment {
    private static AnchorFragment instance = null;
    private MyGridView gridView;
    private FragmentActivity activity;
    private PullToRefreshScrollView scrollView;
    private SimpleDateFormat formatTime;
    private int timeDonwe = 0;
    private String timeUpStr;
    private String timeDolweStr;
    private HandlerLiveTvAnchor handlerLiveTvPage;
    private int currentPosition=-1;
    private List<LiveTvBean> listBanner = new ArrayList<>();   //轮播
    private List<LiveTvBean> listLiveBean = new ArrayList<LiveTvBean>();  //主播
    private UtileStringRequst<LiveTvBean> anchorBanner; //广告轮播
    private CommonAdapter<LiveTvBean> commonAdapter;

    private Date date;
    private int page = 0;
    private UtileStringRequst<LiveTvBean> utileStringRequstGuanZhu;
    private UtilesBlannerViewPager instanceViewPager;
    private View viewViewPager;
    private List<String> listImageUrls = new ArrayList<String>();
    private ArrayList<String> listplayStatus = new ArrayList<String>();
    private boolean bBlanner = false;
    private UserInfoBean userInfo;

    public AnchorFragment() {

    }

    public static AnchorFragment getInstance() {
        if (instance == null) {
            instance = new AnchorFragment();
        }
        return instance;
    }

    private void listClear() {
        listBanner.clear();
        listLiveBean.clear();
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
        View view = inflater.inflate(R.layout.fragment_anchor, container, false);
        initView(view);
        NetWorkData();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView(View view) {
        activity = getActivity();
        if (handlerLiveTvPage == null || anchorBanner == null || utileStringRequstGuanZhu == null) {
            handlerLiveTvPage = new HandlerLiveTvAnchor();
            anchorBanner = new UtileStringRequst<LiveTvBean>();

            utileStringRequstGuanZhu = new UtileStringRequst<LiveTvBean>();
        }
        instanceViewPager = UtilesBlannerViewPager.getInstance();

        viewViewPager = view.findViewById(R.id.anchor_viewPager);
        gridView = (MyGridView) view.findViewById(R.id.id_GridViewAchor);
        commonAdapter = new CommonAdapter<LiveTvBean>(getActivity(), listLiveBean, R.layout.view_gridview_livetv_guanzhu) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                View view = holder.getView(R.id.guanzhu_llBack);
                Drawable background = view.getBackground();
                background.setAlpha(150);
                view.setBackground(background);
                if (!TextUtils.isEmpty(liveTvBean.getHeadurl())) {
                    Picasso.with(getActivity()).load(liveTvBean.getHeadurl()).placeholder(R.drawable.thumb).error
                            (R.drawable.thumb).fit().centerCrop().into((ImageView) holder.getView(R.id.id_ImageGrideView_GuanZhu));
                }
                holder.setText(R.id.id_TextViewPersonNum, liveTvBean.getFans() + getResources().getString(R.string.Person));
            }
        };
        scrollView = (PullToRefreshScrollView) view.findViewById(R.id.id_ScrollViewAnchor);
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
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
                    listClear();
                    NetWorkData();
                    page = 0;
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
                //ToastUtil.showShort("AnchorFragment ：访加载数据第：" + page + "页");
                UserInfoBean userInfo = SharedUtil.getUserInfo(getActivity());
                if (null != userInfo) {
                    utileStringRequstGuanZhu.UtileStringRequst(getActivity(), UrlsManager.URL_LIVE_TOMMORROW + page + "&pagesize=20&userid=" + userInfo.getUserid(),
                            LiveTvBean.class, handlerLiveTvPage, 1);
                    scrollView.onRefreshComplete();
                }
            }
        });
        gridView.setAdapter(commonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TommorrowPersonActivity.class);
                intent.putExtra("data", listLiveBean.get(position));
                getActivity().startActivity(intent);
            }
        });

    }

    public void onBackPressed() {

    }

    public void NetWorkData() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return;
        }
        //banner图 轮播
        if (listBanner.size() == 0) {
            anchorBanner.UtileStringRequst(getActivity(), UrlsManager.URL_LIVE_BANNER + 3,
                    LiveTvBean.class, handlerLiveTvPage, 0);
            return;
        } else {
            if (bBlanner == false) {
                instanceViewPager.UtilesBlannerViewPager(activity, viewViewPager, listBanner, listImageUrls,listplayStatus, new UtilesBlannerViewPager.GetPosition() {
                    @Override
                    public void getPosition(int position) {
                        if(listBanner.size()>0)
                        {
                            LiveTvBean liveTvBean = listBanner.get(position);
                            Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,liveTvBean);
                        }
                    }
                });
                bBlanner = true;
            }else {
                instanceViewPager.onStartBlannr();
            }
        }
        //所有的主播
        if (listLiveBean.size() == 0) {
            if (null != userInfo) {
                utileStringRequstGuanZhu.UtileStringRequst(getActivity(), UrlsManager.URL_LIVE_TOMMORROW +
                        page + "&pagesize=20&userid=" + userInfo.getUserid(),
                        LiveTvBean.class, handlerLiveTvPage, 1);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        NetWorkData();
    }

    //获取刷新时间
    private int getTime(String RefreshTime) {
        long time = System.currentTimeMillis();
        Date d1 = new Date(time);
        //时间
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
        if (instanceViewPager != null) {
            instanceViewPager.stopViewPager();
            bBlanner = false;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class HandlerLiveTvAnchor extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Utiles.log("AnchorFragment:" + msg.what);
            switch (msg.what) {
                case 0:    //广告轮播
                    listBanner.addAll(anchorBanner.list);
                    if (listBanner.size() != 0) {
                        for (int i = 0; i < listBanner.size(); i++) {
                            listImageUrls.add(listBanner.get(i).getCoverurl());
                            listplayStatus.add(listBanner.get(i).getStatus());
                        }
                    }
                    NetWorkData();
                    break;
                case 1:   //最新直播
                    List<LiveTvBean> list = utileStringRequstGuanZhu.list;
                    //Utiles.log("----------主播---"+list.toString());
                    if (list.size() != 0) {
                        page++;
                        listLiveBean.addAll(list);
                        commonAdapter.notifyDataSetChanged();
                        list.clear();
                    }
                    break;
            }
            if (utileStringRequstGuanZhu.list != null) {
                utileStringRequstGuanZhu.list.clear();
            }
            if (anchorBanner.list != null) {
                anchorBanner.list.clear();
            }
            handlerLiveTvPage.removeMessages(msg.what);
        }
    }


}
