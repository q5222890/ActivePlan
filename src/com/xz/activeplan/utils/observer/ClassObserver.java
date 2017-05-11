package com.xz.activeplan.utils.observer;

/**
 * 抽象观察者接口
 * 只要实现该接口，就可以与其他类数据交互
 * @since4.0
 */
public interface ClassObserver {
	//类收到更新数据回调(循环传递),返回true时拦截,不再继续传递
	public boolean onDataUpdate(Object data);
}
