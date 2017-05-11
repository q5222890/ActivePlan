package com.xz.activeplan.ui.personal.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xz.activeplan.ui.BaseFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

	List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();


	public FragmentAdapter(FragmentManager fm,List<BaseFragment > fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
