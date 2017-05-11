package com.xz.activeplan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.xz.activeplan.R;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.views.CircleIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 首次进入引导页
 */
public class SplashActivity extends AppCompatActivity implements OnClickListener, OnPageChangeListener {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private CircleIndicator circleIndicator;
    //引导图片资源
    private static final int[] pics = {R.drawable.guide1_bg,
            R.drawable.guide2_bg, R.drawable.guide3_bg};

    //底部小店图片
    private ImageView[] dots;

    //记录当前选中位置
    private int currentIndex;

    private boolean flag = false;
    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        if (intent != null) {
            flag = intent.getBooleanExtra("flag", false);
        }

        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicator);
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        circleIndicator.setViewPager(vp);
        //绑定回调
        vp.addOnPageChangeListener(this);

        //初始化底部小点
//        initDots();

        try {
            Field leftEdgeField = vp.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = vp.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(vp);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(vp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        if(rightEdge!=null&&!rightEdge.isFinished()){//最后一页向左滑动跳转至首页
            if(!flag) {
                SharedUtil.saveSplash(SplashActivity.this);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            SplashActivity.this.finish();
        }
    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
//        setCurDot(arg0);

    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
//        setCurDot(position);
    }

    public class ViewPagerAdapter extends PagerAdapter {

        //界面列表
        private List<View> views;

        public ViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        //销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        //获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }

            return 0;
        }


        //初始化arg1位置的界面
        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {

            ((ViewPager) arg0).addView(views.get(arg1), 0);

            return views.get(arg1);
        }

        //判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }
}
