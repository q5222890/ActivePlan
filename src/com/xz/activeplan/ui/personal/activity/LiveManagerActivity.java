package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.fragment.LiveManagerFragment;
import com.xz.activeplan.ui.personal.viewpagetest.FragmentsAdapter;
import com.xz.activeplan.ui.personal.viewpagetest.TabInfo;
import com.xz.activeplan.ui.personal.viewpagetest.TitleIndicator;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;

/**
 * 直播管理
 */
public class LiveManagerActivity extends BaseFragmentActivity{

    protected ArrayList<TabInfo> tabs;
    private ViewPager viewPager;
    private FragmentsAdapter mFragmentsAdapter;
    private TitleIndicator title;
    private int defaultTab = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_live_manager);
        initView();
        initFragment();
    }

    private void initFragment() {
        Utiles.headManager(this, R.string.liveManagerTitle);
        UserInfoBean userinfobean = SharedUtil.getUserInfo(this);
        if (userinfobean != null) {
            tabs = new ArrayList<>();
            tabs.add(new TabInfo(0, "我的视频",  new LiveManagerFragment(1,userinfobean.getUserid())));
            tabs.add(new TabInfo(1, "购买视频",  new LiveManagerFragment(2,userinfobean.getUserid())));
        }
        mFragmentsAdapter = new FragmentsAdapter(this, getSupportFragmentManager(), tabs);
        viewPager.setAdapter(mFragmentsAdapter);
        viewPager.setCurrentItem(0);
        title.init(tabs, viewPager);
        //判断默认页面数值是否溢出
        try {
            if (0 > defaultTab || defaultTab >= tabs.size()) {
                Log.v("default", String.valueOf(defaultTab));
                throw new IndexOutOfBoundsException();
            } else {
                title.setDefaultTab(defaultTab);
                viewPager.setCurrentItem(defaultTab);
            }

        } catch (NullPointerException e) {
            throw e;

        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                title.onScroll((viewPager.getWidth() + viewPager.getPageMargin()) * i + i1);
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
    /**
     * 初始化视图
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.liveManager_viewPager);
        title = (TitleIndicator) findViewById(R.id.liveManager_title);
    }
}
