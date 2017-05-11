package com.xz.activeplan.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;

import java.util.Stack;

public abstract class BaseFragment extends Fragment {
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏 一些手机如果有虚拟键盘的话,虚拟键盘就会变成透明的,挡住底部按钮点击事件所以,最后不要用
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
}
