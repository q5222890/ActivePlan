package com.xz.activeplan.ui.personal.text;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Aaron on 2016/7/2.
 * 不可滑动的ViewPager
 */
public class UnScrollViewPager extends ViewPager{

    private boolean noScroll = false;

    public UnScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnScrollViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(noScroll){
            return false;
        }else{
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(noScroll){
            return false;
        }else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }
}
