package com.xz.activeplan.ui.recommend.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.find.activity.AccuvallyDetailsActivity;
import com.xz.activeplan.ui.news.activity.CelebrityActivity;
import com.xz.activeplan.ui.personal.activity.CooperationActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.ui.personal.activity.MessageActivity;
import com.xz.activeplan.ui.recommend.activity.ActiviteActivity;
import com.xz.activeplan.ui.recommend.activity.SelectTicketActivity;
import com.xz.activeplan.ui.recommend.adapter.BannerPagerAdapter;
import com.xz.activeplan.ui.recommend.adapter.GridViewPagerAdapter;
import com.xz.activeplan.ui.recommend.adapter.RecommendAdapter;
import com.xz.activeplan.ui.recommend.charts.SponsorChartsActivity;
import com.xz.activeplan.utils.DensityUtil;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.xlistview.XListView;
import com.xz.activeplan.views.xlistview.XListView.IXListViewListener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 推荐fragment界面
 *
 * @author johnny
 */
public class RecommendFragment extends BaseFragment implements OnClickListener, IXListViewListener, ClassObserver {
    private static final String TAG = RecommendFragment.class.getSimpleName();
    public static ArrayList<ActiveinfosBean> listActiveinfosBean = new ArrayList<ActiveinfosBean>();
    private View mView;// 处理整个页面的view
    private ViewPager mViewPager, mViewPagerPoint;
    private LinearLayout cicleIndicator, cicleIndicatorPoint;
    private LinearLayout llyt_tuisong, llyt_mxjs, lt_tuisong, ly_mxjs;
    private LinearLayout llHomeTypeHeart, llHomeTypeBlind, llHomeTypeNear, llHomeTypeFellow, llHomeTypeEnterprise, llHomeTypeSport, llHomeTypeStudy, llHomeTypeTravel;
    private LinearLayout llHomeTypeLife, llHomeTypeSchool, llHomeTypeStarts, llHomeTypeSamesex, llHomeTypeOther;
    private ArrayList<View> viewListPoint;
    private ArrayList<ActiveinfosBean> mBannerActiveinfoBeans = new ArrayList<ActiveinfosBean>();
    private BannerPagerAdapter mBannerAdapter;
    private GridViewPagerAdapter mGridViewAdapterPoint;
    private Button mPreSelectedBt, mPreSelectedBtPoint;
    private FragmentActivity context;
    private TextView tvMainHeaderCity, tvMainHeaderTitle;
    private ImageView ivMainHeaderSearch, ivMainHeaderAdd;
    private XListView listView;
    private RecommendAdapter mRecommendAdapter;
    private int currpage = 0;
    private int pagesize = 10;
    private int currentItem, item; //当前页面
    private ScheduledExecutorService scheduledExecutorService;
    private RelativeLayout rlMainHeaderSearch;
    private EditText etMainSearch;
    private UserInfoBean mUserInfoBean;
    private CustomProgressDialog customProgressDialog;
    private PopupWindow popupWindow;
    OnClickListener onHeadClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvMainHeaderCity:   //城市
                    ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.MAIN_HEAD_CITY));
                    break;
                case R.id.ivMainHeaderSearch:

                case R.id.etMainSearch: //搜索活动
                    ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.MAIN_HEAD_SEARCH));
                    break;
                case R.id.ivMainHeaderAdd:  //显示更多菜单
                    showPopUpWindow(v);
                    break;
                default:
                    break;
            }

        }
    };
    private RelativeLayout re_viewpager;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //设置当前页面
            mViewPager.setCurrentItem(currentItem);
        }

    };
    private int userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ClassConcrete.getInstance().addObserver(this);
        context = getActivity();
        mView = inflater.inflate(R.layout.include_home_recommend, container, false);
        initViews();
        //加载数据
        loadData();
        loadRecommend();
        return mView;
    }

    @Override
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

    @Override
    public void onStart() {
        super.onStart();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        //每隔5秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 5, TimeUnit.SECONDS);
    }

    public int getDisPlayWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width * 45 / 100;
    }

    /**
     * 初始化
     */
    @SuppressLint("NewApi")
    private void initViews() {
        View headView = LayoutInflater.from(context).inflate(
                R.layout.listitem_recommend_head, null);
        mViewPager = (ViewPager) headView.findViewById(R.id.viewpager);
        mViewPagerPoint = (ViewPager) headView
                .findViewById(R.id.viewpager_point);
        cicleIndicator = (LinearLayout) headView
                .findViewById(R.id.cicleIndicator);
        cicleIndicatorPoint = (LinearLayout) headView.findViewById(R.id.cicleIndicator_point);
        llyt_tuisong = (LinearLayout) headView.findViewById(R.id.llyt_tuisong);//精彩推送
        llyt_mxjs = (LinearLayout) headView.findViewById(R.id.llyt_mxjs);
        lt_tuisong = (LinearLayout) headView.findViewById(R.id.lt_tuisong);
        ly_mxjs = (LinearLayout) headView.findViewById(R.id.ly_mxjs);
        tvMainHeaderCity = (TextView) mView.findViewById(R.id.tvMainHeaderCity);
        ivMainHeaderSearch = (ImageView) mView.findViewById(R.id.ivMainHeaderSearch);
        ivMainHeaderAdd = (ImageView) mView.findViewById(R.id.ivMainHeaderAdd);
        tvMainHeaderTitle = (TextView) mView.findViewById(R.id.tvMainHeaderTitle);
        etMainSearch = (EditText) mView.findViewById(R.id.etMainSearch);
        rlMainHeaderSearch = (RelativeLayout) mView.findViewById(R.id.rlMainHeaderSearch);
        tvMainHeaderCity.setText(SharedUtil.getCity(context));
        re_viewpager = (RelativeLayout) headView.findViewById(R.id.re_viewpager);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) re_viewpager.getLayoutParams();
        lp.height = this.getDisPlayWidth();
        re_viewpager.setLayoutParams(lp);
        listView = (XListView) mView.findViewById(R.id.id_XListview);

        listView.addHeaderView(headView);

        LayoutInflater lf = LayoutInflater.from(context);

        //爱心吧
        View viewPoint1 = lf
                .inflate(R.layout.include_home_category_type1, null);
        //生活吧
        View viewPoint2 = lf
                .inflate(R.layout.include_home_category_type2, null);

        llHomeTypeHeart = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeHeart);
        llHomeTypeBlind = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeBlind);
        llHomeTypeNear = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeNear);
        llHomeTypeFellow = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeFellow);
        llHomeTypeEnterprise = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeEnterprise);
        llHomeTypeSport = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeSport);
        llHomeTypeStudy = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeStudy);
        llHomeTypeTravel = (LinearLayout) viewPoint1.findViewById(R.id.llHomeTypeTravel);

        llHomeTypeLife = (LinearLayout) viewPoint2.findViewById(R.id.llHomeTypeLife);
        llHomeTypeSchool = (LinearLayout) viewPoint2.findViewById(R.id.llHomeTypeSchool);
        llHomeTypeStarts = (LinearLayout) viewPoint2.findViewById(R.id.llHomeTypeStarts);
        llHomeTypeSamesex = (LinearLayout) viewPoint2.findViewById(R.id.llHomeTypeSamesex);
        llHomeTypeOther = (LinearLayout) viewPoint2.findViewById(R.id.llHomeTypeOther);

        llHomeTypeHeart.setOnClickListener(this);
        llHomeTypeBlind.setOnClickListener(this);
        llHomeTypeNear.setOnClickListener(this);
        llHomeTypeFellow.setOnClickListener(this);
        llHomeTypeEnterprise.setOnClickListener(this);
        llHomeTypeSport.setOnClickListener(this);
        llHomeTypeStudy.setOnClickListener(this);
        llHomeTypeTravel.setOnClickListener(this);

        llHomeTypeLife.setOnClickListener(this);
        llHomeTypeSchool.setOnClickListener(this);
        llHomeTypeStarts.setOnClickListener(this);
        llHomeTypeSamesex.setOnClickListener(this);
        llHomeTypeOther.setOnClickListener(this);

        tvMainHeaderCity.setOnClickListener(onHeadClick);
        //	ivMainHeaderSearch.setOnClickListener(onHeadClick) ;
        ivMainHeaderAdd.setOnClickListener(onHeadClick);
        rlMainHeaderSearch.setOnClickListener(onHeadClick);
        etMainSearch.setOnClickListener(onHeadClick);

        //tvMainHeaderTitle.setVisibility(View.GONE) ;   //标题
        tvMainHeaderCity.setVisibility(View.VISIBLE);  //城市
        //	ivMainHeaderSearch.setVisibility(View.GONE) ; //搜索
        ivMainHeaderAdd.setVisibility(View.VISIBLE);  //主办方排行榜
        rlMainHeaderSearch.setVisibility(View.VISIBLE);//搜索

        viewListPoint = new ArrayList<View>();
        viewListPoint.add(viewPoint1);
        viewListPoint.add(viewPoint2);

        //tvMainHeaderTitle.setText("约吧") ;

        mRecommendAdapter = new RecommendAdapter(context, listActiveinfosBean);
        listView.setAdapter(mRecommendAdapter);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);
        mGridViewAdapterPoint = new GridViewPagerAdapter(viewListPoint);
        mViewPagerPoint.setAdapter(mGridViewAdapterPoint);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.home_new_point);

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (mPreSelectedBt != null) {
                    mPreSelectedBt
                            .setBackgroundResource(R.drawable.home_new_point);
                }

                Button currentBt = (Button) cicleIndicator.getChildAt(position);
                currentBt.setBackgroundResource(R.drawable.home_new_cur);
                mPreSelectedBt = currentBt;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        for (int i = 0; i < viewListPoint.size(); i++) {
            Button bt = new Button(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    bitmap.getWidth(), bitmap.getHeight());

            params.leftMargin = DensityUtil.dip2px(context, 2);
            params.rightMargin = DensityUtil.dip2px(context, 2);
            bt.setLayoutParams(params);
            if (i == 0) {
                bt.setBackgroundResource(R.drawable.home_new_cur);
                mPreSelectedBtPoint = bt;
            } else {
                bt.setBackgroundResource(R.drawable.home_new_point);
            }
            cicleIndicatorPoint.addView(bt);
        }

        mViewPagerPoint.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (mPreSelectedBtPoint != null) {
                    mPreSelectedBtPoint
                            .setBackgroundResource(R.drawable.home_new_point);
                }
                Button currentBt = (Button) cicleIndicatorPoint
                        .getChildAt(position);
                currentBt.setBackgroundResource(R.drawable.home_new_cur);
                mPreSelectedBtPoint = currentBt;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    // 初始化管广告横幅
    private void initBanner() {
        mBannerAdapter = new BannerPagerAdapter(mBannerActiveinfoBeans, context);
        mViewPager.setAdapter(mBannerAdapter);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.home_new_point);
        cicleIndicator.removeAllViews();
        for (int i = 0; i < mBannerActiveinfoBeans.size(); i++) {
            Button bt = new Button(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    bitmap.getWidth(), bitmap.getHeight());

            params.leftMargin = DensityUtil.dip2px(context, 2);
            params.rightMargin = DensityUtil.dip2px(context, 2);
            bt.setLayoutParams(params);
            if (i == 0) {
                bt.setBackgroundResource(R.drawable.home_new_cur);
                mPreSelectedBt = bt;
            } else {
                bt.setBackgroundResource(R.drawable.home_new_point);
            }
            cicleIndicator.addView(bt);
        }
    }

    private void loadData() {

        Utiles.log("loadData");
        if (!NetworkInfoUtil.checkNetwork(context)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
        }

        /**
         * 广告轮播
         */

        UserInfoServiceImpl.getInstance().pushactive(SharedUtil.getCity(context), 1, 0, 6,
                new StringCallback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(String response) {
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                ActiveinfosJson collectJosn = new ActiveinfosJson();
                                obj = collectJosn
                                        .analysisJson2Object(statusBean
                                                .getData());
                                if (obj != null) {
                                    ArrayList<ActiveinfosBean> datas = (ArrayList<ActiveinfosBean>) obj;
                                    mBannerActiveinfoBeans.clear();
                                    mBannerActiveinfoBeans.addAll(datas);
                                    initBanner();
                                }
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                    }
                });

        /**
         * 推送活动  精彩推送
         */
        UserInfoServiceImpl.getInstance().pushactive(SharedUtil.getCity(context), 2, 0, 6,
                new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                ActiveinfosJson collectJosn = new ActiveinfosJson();
                                obj = collectJosn
                                        .analysisJson2Object(statusBean
                                                .getData());
                                if (obj != null) {
                                    @SuppressWarnings("unchecked")
                                    ArrayList<ActiveinfosBean> datas = (ArrayList<ActiveinfosBean>) obj;
                                    initSplendid(datas);
                                }
                            } else {

                            }
                        }

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                    }
                });

        //明星讲师
        CelebrityNewWorkImpl.getInstance().getTeacherRecommend("", new StringCallback() {
            public void onResponse(String response) {
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
//                        TeacherJson teacherJson = new TeacherJson();
//                        obj = teacherJson
//                                .analysisJson2Object(statusBean
//                                        .getData());
//                        if (obj != null) {
                        @SuppressWarnings("unchecked")//禁止显示警告
                                List<TeacherBean> datas = JSON.parseArray(statusBean.getData(), TeacherBean.class);
                        Log.i("=============", "名师推荐datas:" + datas);
                        initTeacher(datas);
//                        }
                    } else {

                    }
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "onFailure = " + e.getMessage());
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initSplendid(ArrayList<ActiveinfosBean> datas) {
        if (datas == null || (datas != null && datas.size() <= 0)) {
            return;
        }
        llyt_tuisong.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            LayoutParams mLayoutParams = new LayoutParams(DensityUtil.dip2px(context, 135), LayoutParams.WRAP_CONTENT);
            mLayoutParams.leftMargin = DensityUtil.dip2px(context, 5);
            mLayoutParams.rightMargin = DensityUtil.dip2px(context, 5);
            View view = LayoutInflater.from(context).inflate(R.layout.listitem_category_item, null);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            TextView txt = (TextView) view.findViewById(R.id.txt);
            ActiveinfosBean bean = datas.get(i);
            Picasso.with(XZApplication.getInstance()).load(bean.getActiveurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(img);
            txt.setText(bean.getName());
            img.setOnClickListener(new MyOnClickListener(bean));
            llyt_tuisong.addView(view, mLayoutParams);
        }
        llyt_tuisong.setVisibility(View.VISIBLE);
        lt_tuisong.setVisibility(View.VISIBLE);

    }

    /**
     * 加载明星讲师布局
     * @param datas
     */
    @SuppressLint("InflateParams")
    private void initTeacher(List<TeacherBean> datas) {
        if (datas == null || (datas != null && datas.size() <= 0)) {
            return;
        }
        llyt_mxjs.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            LayoutParams mLayoutParams = new LayoutParams(DensityUtil.dip2px(context, 135), LayoutParams.WRAP_CONTENT);
            mLayoutParams.leftMargin = DensityUtil.dip2px(context, 5);
            mLayoutParams.rightMargin = DensityUtil.dip2px(context, 5);
            View view = LayoutInflater.from(context).inflate(R.layout.listitem_category_item, null);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            TextView txt = (TextView) view.findViewById(R.id.txt);
            TeacherBean bean = datas.get(i);
            if (!TextUtils.isEmpty(bean.getPushurl())) {
                Picasso.with(XZApplication.getInstance()).load(bean.getPushurl()).placeholder(R.drawable.default_details_image).error(R.drawable.default_details_image).fit().centerCrop().into(img);
                img.setOnClickListener(new TeacherOnClickListener(bean));
            }
            if (bean!=null) {
                txt.setText(bean.getRealname());
            }
            llyt_mxjs.addView(view, mLayoutParams);
        }


        llyt_mxjs.setVisibility(View.VISIBLE);
        ly_mxjs.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        int categoryid = 0;
        switch (v.getId()) {
            case R.id.llHomeTypeHeart:
                categoryid = 1;
                break;
            case R.id.llHomeTypeBlind:
                categoryid = 2;
                break;
            case R.id.llHomeTypeNear:
                categoryid = 3;
                break;
            case R.id.llHomeTypeFellow:
                categoryid = 4;
                break;
            case R.id.llHomeTypeEnterprise:
                categoryid = 5;
                break;
            case R.id.llHomeTypeSport:
                categoryid = 6;
                break;
            case R.id.llHomeTypeStudy:
                categoryid = 7;
                break;
            case R.id.llHomeTypeTravel:
                categoryid = 8;
                break;
            case R.id.llHomeTypeLife:
                categoryid = 9;
                break;
            case R.id.llHomeTypeSchool:
                categoryid = 10;
                break;
            case R.id.llHomeTypeStarts:
                categoryid = 11;
                break;
            case R.id.llHomeTypeSamesex:
                categoryid = 12;
                break;
            case R.id.llHomeTypeOther:
                categoryid = 13;
                break;
        }
        Intent intent = new Intent(context, ActiviteActivity.class);
        intent.putExtra("categoryid", categoryid);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        currpage = 0;
        loadData();
        loadRecommend();
    }

    @Override
    public void onLoadMore() {
        if (listActiveinfosBean.size() == 0) {
            currpage = 0;
        } else {
            currpage++;
        }

        loadRecommend();
    }

    private void loadRecommend() {
        Utiles.log("loadRecommend");
        if (!NetworkInfoUtil.checkNetwork(context)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
        }
        /**
         * 最新活动列表数据
         */

        UserInfoServiceImpl.getInstance().myCategory(SharedUtil.getCity(context), 0, currpage, pagesize, new StringCallback() {
            @Override
            public void onResponse(String response) {
                if (currpage <= 0) {
                    listView.stopRefresh();
                } else {
                    listView.stopLoadMore();
                }
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ActiveinfosJson activeinfosJson = new ActiveinfosJson();
                        obj = activeinfosJson.analysisJson2Object(statusBean.getData());
                        if (obj != null) {
                            @SuppressWarnings("unchecked")
                            ArrayList<ActiveinfosBean> activeinfosBeans = (ArrayList<ActiveinfosBean>) obj;
                            if (activeinfosBeans != null) {
                                if (currpage <= 0) {       //当前为第一页
                                    listActiveinfosBean.clear();
                                }

                                if (activeinfosBeans.size() < pagesize) {
                                    listView.setPullLoadEnable(false);
                                } else {
                                    listView.setPullLoadEnable(true);
                                }
                                listActiveinfosBean.addAll(activeinfosBeans);
                                mRecommendAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                    }
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                 if (currpage <= 0) {
                    listView.stopRefresh();
                } else {
                    listView.stopLoadMore();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        Utiles.log(" onDestory	View");
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroyView();
    }

    @Override
    public boolean onDataUpdate(Object data) {
        Utiles.log("=========>onDataUpdate");
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.CITY_NOTIFI_TYPE) {
            loadData();
            onRefresh();
            Utiles.log("Recommend:onDate Update 01");
            tvMainHeaderCity.setText(SharedUtil.getCity(context));
        } else if (event.getType() == EventType.LOGIN_NOTIFI_TYPE) {
            Utiles.log("Recommend:onDate Update 02");
            loadData();
            onRefresh();
        }
        return false;
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 右上角更多菜单显示
     * @param view  布局
     */
    public void showPopUpWindow(View view) {
        View mView = LayoutInflater.from(context).inflate(R.layout.popwindow_messages, null);
        TextView textViewmessage = (TextView) mView.findViewById(R.id.textview_message);
        TextView textViewsponsor = (TextView) mView.findViewById(R.id.textview_sponsor);
        TextView textViewcooperate = (TextView) mView.findViewById(R.id.textview_cooperate);
        TextView textViewscan = (TextView) mView.findViewById(R.id.textview_Scan);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int SCREEN_WIDTH = dm.widthPixels;
        int SCREEN_HEIGHT = dm.heightPixels;
        popupWindow = new PopupWindow(mView, SCREEN_WIDTH * 5 / 10, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.home_org_bg));

        popupWindow.showAsDropDown(view);
        backgroundAlpha(0.4f);
        textViewmessage.setOnClickListener(new OnMessageClick());
        textViewsponsor.setOnClickListener(new OnMessageClick());
        textViewcooperate.setOnClickListener(new OnMessageClick());
        textViewscan.setOnClickListener(new OnMessageClick());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

    }

    /**
     * 加载选择验票活动的数据
     */
    private void loadTicketOrActive()
    {
        customProgressDialog = new CustomProgressDialog(context);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        if(SharedUtil.isLogin(getActivity())){
           userId = SharedUtil.getUserInfo(context).getUserid();
        }
        ActiveServiceImpl.getInstance().selectTicketAction(userId , new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("加载失败...");
            }
            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0)//成功返回
                {
                    List<ActiveinfosBean> list = JSON.parseArray(statusBean.getData(), ActiveinfosBean.class);
                    ArrayList<ActiveinfosBean> list1 = new ArrayList<ActiveinfosBean>();
                    list1.addAll(list);
                    if(list1.size()==0)
                    {
                        ToastUtil.showCenterToast(context,"您还没有发布活动!");
                        customProgressDialog.dismiss();
                        return;
                    }else {
                        Intent intent = new Intent(getActivity(),SelectTicketActivity.class);
                        intent.putExtra("data",list1);
                        startActivity(intent);
                        customProgressDialog.dismiss();
                    }

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //切换图片
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            if (mBannerActiveinfoBeans.size() > 0) {
                currentItem = (currentItem + 1) % mBannerActiveinfoBeans.size();
            }

            if (viewListPoint.size() > 0) {
                item = (item + 1) % viewListPoint.size();
            }
            //更新界面
            handler.obtainMessage().sendToTarget();
        }
    }

    /**
     * 活动详情
     */
    class MyOnClickListener implements OnClickListener {
        private ActiveinfosBean mActiveinfosBean;

        public MyOnClickListener(ActiveinfosBean bean) {
            this.mActiveinfosBean = bean;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,
                    AccuvallyDetailsActivity.class);
            intent.putExtra("data", mActiveinfosBean);
            context.startActivity(intent);
        }

    }

    class TeacherOnClickListener implements OnClickListener {
        private TeacherBean mTeacherBean;

        public TeacherOnClickListener(TeacherBean bean) {
            this.mTeacherBean = bean;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,
                    CelebrityActivity.class);
            intent.putExtra("data", mTeacherBean);
            context.startActivity(intent);
        }
    }

    private class OnMessageClick implements OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.textview_message:  //消息
                    if (SharedUtil.isLogin(context)) {
                        mUserInfoBean = SharedUtil.getUserInfo(context);
                        Utiles.skipNoData(MessageActivity.class);
                    } else {
                        Utiles.skipNoData(LoginActivity.class);
                        popupWindow.dismiss();
                        return;
                    }
                   // Utiles.skipNoData(ScanActivity.class);
                    break;
                case R.id.textview_sponsor:  //主办方
                    startActivity(new Intent(getActivity(), SponsorChartsActivity.class));
                    break;
                case R.id.textview_cooperate: //推广合作
                    startActivity(new Intent(getActivity(), CooperationActivity.class));
                    break;

                case R.id.textview_Scan: //扫一扫            ScanActivity
                    if (SharedUtil.isLogin(context)) {
                        loadTicketOrActive();
                    } else {
                        Utiles.skipNoData(LoginActivity.class);
                    }

                    break;
            }
            popupWindow.dismiss();
        }
    }
}
