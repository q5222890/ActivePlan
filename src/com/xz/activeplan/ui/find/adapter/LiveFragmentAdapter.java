package com.xz.activeplan.ui.find.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xz.activeplan.ui.BaseFragment;

import java.util.List;
/**
 * 滑动碎片适配器
 */
public class LiveFragmentAdapter extends FragmentPagerAdapter{
    private FragmentManager supportFragmentManager;
    private List<BaseFragment> flist;
    public LiveFragmentAdapter(FragmentManager supportFragmentManager, List<BaseFragment> flist) {
        super(supportFragmentManager);
        this.supportFragmentManager = supportFragmentManager;
        this.flist = flist;
    }
    @Override
    public Fragment getItem(int position) {
        return flist.get(position);
    }

    @Override
    public int getCount() {
        return flist.size();
    }
}
