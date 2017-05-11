package com.xz.activeplan.ui.find.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 */
public class BannerAdapter extends PagerAdapter {
    //数据源
    private List<View> mList;

    public BannerAdapter(List<View> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        //取超大的数，实现无线循环效果
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //java.lang.IllegalStateException: The specified child already has a parent.
        // You must call removeView() on the child's parent first.
//        container.addView(mList.get(position % mList.size()));
        ViewGroup parent = (ViewGroup) mList.get(position % mList.size()).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(mList.get(position % mList.size()));
        return mList.get(position % mList.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position % mList.size()));
    }
}
