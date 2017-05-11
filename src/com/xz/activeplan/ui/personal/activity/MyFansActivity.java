package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.fragment.FansFragment;
import com.xz.activeplan.ui.personal.viewpagetest.FragmentsAdapter;
import com.xz.activeplan.ui.personal.viewpagetest.TabInfo;
import com.xz.activeplan.ui.personal.viewpagetest.TitleIndicator;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的粉丝
 */
public class MyFansActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener{
    protected ArrayList<TabInfo> tabs;
    private List<BaseFragment> fragmentlist = new ArrayList<>();
    private ViewPager mViewPager;
    private FragmentsAdapter mFragmentsAdapter;
    private TitleIndicator title;
    private int defaultTab = 0;
    private View viewtop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassConcrete.getInstance().addObserver(this);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_my_fans);
        initView();
        initfragment();
    }




    private void initView() {
        viewtop =findViewById(R.id.view_top);
        viewtop.setVisibility(View.VISIBLE);
        Utiles.headManager(this,R.string.string_MyFans);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_fans);
    }


    private void initfragment() {
        UserInfoBean userinfobean = SharedUtil.getUserInfo(this);
        if (userinfobean != null) {
            tabs = new ArrayList<>();
            tabs.add(new TabInfo(0, "名人堂粉丝",  new FansFragment(1,userinfobean.getUserid())));
            tabs.add(new TabInfo(1, "直播粉丝",  new FansFragment(2,userinfobean.getUserid())));
            tabs.add(new TabInfo(2, "活动粉丝",  new FansFragment(3,userinfobean.getUserid())));
        }
        mFragmentsAdapter = new FragmentsAdapter(this, getSupportFragmentManager(), tabs);
        mViewPager.setAdapter(mFragmentsAdapter);
        mViewPager.setCurrentItem(0);
        title = (TitleIndicator) findViewById(R.id.title);

        title.init(tabs, mViewPager);

        //判断默认页面数值是否溢出
        try {
            if (0 > defaultTab || defaultTab >= tabs.size()) {
                Log.v("default", String.valueOf(defaultTab));
                throw new IndexOutOfBoundsException();
            } else {
                title.setDefaultTab(defaultTab);
                mViewPager.setCurrentItem(defaultTab);
            }

        } catch (NullPointerException e) {
            throw e;

        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                title.onScroll((mViewPager.getWidth() + mViewPager.getPageMargin()) * i + i1);
            }

            @Override
            public void onPageSelected(int i) {
                title.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public boolean onDataUpdate(Object data) {

        return false;
    }

}