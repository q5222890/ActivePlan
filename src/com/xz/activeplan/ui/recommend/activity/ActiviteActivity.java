package com.xz.activeplan.ui.recommend.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.CategoryInfoBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.recommend.adapter.RecommendAdapter;
import com.xz.activeplan.ui.recommend.fragment.RecommendFragment;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ExpandTabView;
import com.xz.activeplan.views.ViewLeft;
import com.xz.activeplan.views.ViewMiddle;
import com.xz.activeplan.views.ViewRight;
import com.xz.activeplan.views.xlistview.XListView;
import com.xz.activeplan.views.xlistview.XListView.IXListViewListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动分类
 */

public class ActiviteActivity extends BaseFragmentActivity implements
        OnClickListener, IXListViewListener {
    private static final String TAG = RecommendFragment.class.getSimpleName();

    private TextView mTvHeadTitle;

    private ImageView iv_datails_back;

    private ArrayList<ActiveinfosBean> arr = new ArrayList<ActiveinfosBean>();

    private XListView mListView;
    private TextView mTxtNodata;

    private RecommendAdapter mRecommendAdapter;

    private int currpage = 0;
    private int pagesize = 20;
    private int categoryid;
    private String comprehensive = "hot";
    private int startdata = 10000;
    private int charge = 0;

    private ImageView categoryimg; //分类图片
    private TextView catename, activenum, activeintroduce;//标题，活动数量,活动介绍
    private RelativeLayout relativeLayout_toolbar;
    private LinearLayout shareactive;
    private ImageView share;
    private ImageView advert;

    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewLeft;
    private ViewMiddle viewMiddle;
    private ViewRight viewRight;
    private LinearLayout listheaderview;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        setContentView(R.layout.xlistview_main);
        Intent intent = getIntent();
        if (intent != null) {
            categoryid = intent.getIntExtra("categoryid", 0);
        }
        initViews();
        init();
    }

    private void init() {
        String headName = "最新";
        int cateimage = 0;
        switch (categoryid) {
            case 0:
                headName = "最新";
                break;
            case 1:
                headName = "爱心吧";
                cateimage = R.drawable.ic_heart;
                break;
            case 2:
                headName = "相亲吧";
                cateimage = R.drawable.ic_blind;
                break;
            case 3:
                headName = "商务吧";
                cateimage = R.drawable.ic_business;
                break;
            case 4:
                headName = "老乡吧";
                cateimage = R.drawable.ic_fellow;
                break;
            case 5:
                headName = "创业吧";
                cateimage = R.drawable.ic_enterprise;
                break;
            case 6:
                headName = "运动吧";
                cateimage = R.drawable.ic_sport;
                break;
            case 7:
                headName = "学习吧";
                cateimage = R.drawable.ic_study;
                break;
            case 8:
                headName = "旅游吧";
                cateimage = R.drawable.ic_travel;
                break;
            case 9:
                headName = "生活吧";
                cateimage = R.drawable.ic_life;
                break;
            case 10:
                headName = "校园吧";
                cateimage = R.drawable.ic_shcool;
                break;
            case 11:
                headName = "群星吧";
                cateimage = R.drawable.ic_stars;
                break;
            case 12:
                headName = "同性吧";
                cateimage = R.drawable.ic_samesex;
                break;
            case 13:
                headName = "其它吧";
                cateimage = R.drawable.ic_other;
                break;
        }
        mTvHeadTitle.setText(headName);
        mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
        categoryimg.setImageResource(cateimage);
        catename.setText(headName);

    }

    /**
     * 初始化
     */
    @SuppressLint("NewApi")
    private void initViews() {

        View view = findViewById(R.id.view_top);
        view.setVisibility(View.VISIBLE);
        mListView = (XListView) findViewById(R.id.id_XListview);
        mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);

        mTxtNodata = (TextView) findViewById(R.id.txt_nodata);
        iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        relativeLayout_toolbar = (RelativeLayout) findViewById(R.id.relativeLayout_toolbar);
        relativeLayout_toolbar.setBackgroundColor(getResources().getColor(R.color.header_blue));
        shareactive = (LinearLayout) findViewById(R.id.share_ly);
        shareactive.setVisibility(View.VISIBLE);
        share = (ImageView) findViewById(R.id.two_img);
        share.setVisibility(View.GONE);
        mListView.setEmptyView(mTxtNodata);
        mTxtNodata.setOnClickListener(this);
        mTxtNodata.setText("当前没有数据");
        iv_datails_back.setOnClickListener(this);

//        mListView.setPullLoadEnable(true);
        mRecommendAdapter = new RecommendAdapter(this, arr);
        mListView.setAdapter(mRecommendAdapter);
        mListView.setXListViewListener(this);

//        loadRecommend();
        inflateView();
        loadOrderRecommend();
        loadRecommend();

    }

    private void inflateView() {
        listheaderview = (LinearLayout) findViewById(R.id.line_listheaderview);
        listheaderview.setVisibility(View.VISIBLE);
        categoryimg = (ImageView) findViewById(R.id.iv_categoryimage);
        catename = (TextView) findViewById(R.id.tv_categoryname);
        activenum = (TextView) findViewById(R.id.tv_activenumber);
        activeintroduce = (TextView) findViewById(R.id.tv_activeintroduce);
        //广告位（点击X隐藏）

        advert = (ImageView) findViewById(R.id.iv_advert);
        advert.setVisibility(View.VISIBLE);
        findViewById(R.id.iv_hide).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.rl_hide).setVisibility(View.GONE);
            }
        });
        //初始化菜单
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(this);
        viewMiddle = new ViewMiddle(this);
        viewRight = new ViewRight(this);

        mViewArray.add(viewLeft);
        mViewArray.add(viewMiddle);
        mViewArray.add(viewRight);

        ArrayList<String> mTextArray = new ArrayList<String>();

        mTextArray.add("全类型");
        mTextArray.add("全时段");
        mTextArray.add("全价位");
        expandTabView.setValue(mTextArray, mViewArray);
        expandTabView.setTitle(viewLeft.getShowText(), 0);
        expandTabView.setTitle(viewMiddle.getShowText(), 1);
        expandTabView.setTitle(viewRight.getShowText(), 2);

       // Display display = getWindowManager().getDefaultDisplay();
        //int width = display.getWidth();
        //int height = display.getHeight();
        //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) advert.getLayoutParams();
       // lp.height = width * 1 / 5;  //设置广告高度
       // advert.setLayoutParams(lp);

        //calculate(advert);
        initListener();

    }

    private void initListener() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewLeft, showText);
            }
        });

        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewMiddle, showText);
            }

        });
        viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewRight, showText);
            }
        });

    }

    private void onRefresh(View view, String showText) {

        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        if (showText.equals("热门")) {
            comprehensive = "hot";
        } else if (showText.equals("综合")) {
            comprehensive = "";
        }
        if (showText.equals("一天内")) {
            startdata = 1;
        } else if (showText.equals("两天内")) {
            startdata = 2;
        } else if (showText.equals("一周内")) {
            startdata = 7;
        } else if (showText.equals("一月内")) {
            startdata = 30;
        } else if (showText.equals("全时段")) {
            startdata = 100000;
        }

        if (showText.equals("免费")) {
            charge = 0;
        } else if (showText.equals("收费")) {
            charge = 1;
        }

        onRefresh();
        mRecommendAdapter.notifyDataSetChanged();
    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {

        if (!expandTabView.onPressBack()) {
            finish();
        }

    }

    public void calculate(final View view) {
        //计算控件宽高
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int height = view.getMeasuredHeight();
                int width = view.getMeasuredWidth();
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.txt_nodata:
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        currpage = 0;
        loadRecommend();
        loadOrderRecommend();
    }

    @Override
    public void onLoadMore() {
        loadOrderRecommend();
    }

    private void loadRecommend() {

        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
        }

        UserInfoServiceImpl.getInstance().getActiveCategoryInfo(categoryid, new StringCallback() {

            @Override
            public void onResponse(String response) {
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ActiveinfosJson activeinfosBean = new ActiveinfosJson();
                        obj = activeinfosBean
                                .analysisJson2Object(statusBean
                                        .getData());
                        if (obj != null) {
                            List<CategoryInfoBean> categoryInfoBean = JSON.parseArray(statusBean.getData(), CategoryInfoBean.class);

                            if (categoryInfoBean != null) {

                                for (CategoryInfoBean bean : categoryInfoBean) {
                                    catename.setText(bean.getContent());
                                    activenum.setText(bean.getTotal()+"");
                                    activeintroduce.setText(bean.getIntroduce());
                                    Picasso.with(ActiviteActivity.this).load(bean.getAdPlaceUrl())
                                            .error(R.drawable.default_rectangle_image).into(advert);
                                }
                            }
                        }
                    } else {
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        });
    }

    //活动分类排序
    private void loadOrderRecommend() {
        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
        }
        UserInfoServiceImpl.getInstance().myCategoryOrder(SharedUtil.getCity(this),
                categoryid, currpage, pagesize, comprehensive, startdata, charge, new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        Utiles.log("currpage1:"+currpage);
                        //Log.e(TAG, "onResponse = " + response);
                        if (currpage <= 0) { //当前为第一页或为空
                            mListView.stopRefresh(); //停止下拉刷新
                        } else {
                            mListView.stopLoadMore();//停止上拉加载
                        }
                        StatusJson statusJosn = new StatusJson();
                        Object obj = statusJosn.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                ActiveinfosJson activeinfosBean = new ActiveinfosJson();
                                obj = activeinfosBean
                                        .analysisJson2Object(statusBean
                                                .getData());
                                if (obj != null) {
                                    ArrayList<ActiveinfosBean> collectBeans = (ArrayList<ActiveinfosBean>) obj;
                                    if (collectBeans != null) {
                                        if (currpage <= 0) {
                                            arr.clear();
                                        }
                                        if (collectBeans.size() > 0) {
                                            currpage++;
                                        }

                                        if(collectBeans.size()<pagesize){
                                            mListView.setPullLoadEnable(false);
                                        }else{
                                            mListView.setPullLoadEnable(true);
                                        }
                                        arr.addAll(collectBeans);
                                        mRecommendAdapter.notifyDataSetChanged();
                                        Utiles.log("数量：" + arr.size());
                                    }
                                }
                            } else {
                                ToastUtil.showShort("数据获取失败，请刷新重试!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.e(TAG, "onFailure = " + e.getMessage());
                        if (currpage <= 0) {
                            mListView.stopRefresh();
                        } else {
                            mListView.stopLoadMore();
                        }
                    }
                });
    }
}
