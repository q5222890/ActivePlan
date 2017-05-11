package com.xz.activeplan.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;

import java.util.Stack;

/**
 *没有带 沉浸式状态栏
 * 目前用于 点播播放和观看直播
 */
public abstract class BaseFragment1 extends Fragment {
	protected Stack<Object> backStack = new Stack<Object>();
	public boolean isBackStackEmpty() {
		return backStack.isEmpty();
	}
	public void pushBackEvent(Object obj) {
		backStack.push(obj); 
	}
	public void clearBackStack() {
		while (!backStack.isEmpty()) {
			backStack.pop();
		}
	}
	public Object popBackEvent() {
		if (backStack.isEmpty())
			return null;
		return backStack.pop();
	}

	public abstract void onBackPressed();

	public XZApplication getApp() {
		return (XZApplication) getActivity().getApplication();
	}

	protected void replaceFragment(Fragment fragment, boolean isAddBackStack,
			int container) {
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
		ft.replace(container, fragment);
		if (isAddBackStack)
			ft.addToBackStack(null);
			ft.commit();
	}

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
}
