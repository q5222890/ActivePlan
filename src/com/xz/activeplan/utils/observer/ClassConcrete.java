package com.xz.activeplan.utils.observer;

import android.util.Log;

import java.util.Stack;

/**
 * 抽象接口主题角色的支持类
 * 
 * @since4.0
 */
public class ClassConcrete {
    private static Stack<ClassObserver> observerStack;
    private static ClassConcrete instance;

    /**
     * 单一实例
     */
    public synchronized static ClassConcrete getInstance() {
        if (instance == null) {
            instance = new ClassConcrete();
        }
        if (observerStack == null) {
            observerStack = new Stack<ClassObserver>();
        }
        return instance;
    }

    /**
     * 获取所有监听接口
     * 
     * @return Stack
     */
    public static Stack<ClassObserver> getObserverStack() {
        return observerStack;
    }

    /**
     * 增加一个类观察者
     */
    public boolean addObserver(ClassObserver observer) {
        return observerStack.add(observer);
    }

    /**
     * 移除一个视图观察者
     */
    public boolean removeObserver(ClassObserver observer) {
        return observerStack.remove(observer);
    }

    /**
     * 移除一个视图观察者
     */
    public boolean removeObserver(Class<?> clas) {
        for (ClassObserver obs : observerStack) {
            if (clas.getClass().equals(obs.getClass())) {
                return removeObserver(obs);
            }
        }
        return false;
    }

    /**
     * 获取一个监听
     * 
     * @param clas
     * @return
     */
    public ClassObserver getObserver(Class<?> clas) {
        for (ClassObserver obs : observerStack) {
            if (clas.getClass().equals(obs.getClass())) {
                return obs;
            }
        }
        return null;
    }

    /**
     * 移除所有视图观察者
     */
    public boolean removeAll() {
        observerStack.clear();
        return true;
    }

    /**
     * 提醒所有观察者更新数据
     */
    public void postDataUpdate(Object data) {
        for (ClassObserver obs : observerStack) {
            if (obs.onDataUpdate(data)) {
                break;
            }
        }
    }

}
