package com.xz.activeplan.ui.personal.viewpagetest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentsAdapter extends FragmentPagerAdapter {

    private ArrayList<TabInfo> tabs ;
    private Context context ;

    public FragmentsAdapter(Context context, FragmentManager fm, ArrayList<TabInfo> tabs) {
        super(fm);
        this.tabs = tabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        return tabs.get(index).getFragment();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tabs.size();
    }

}
