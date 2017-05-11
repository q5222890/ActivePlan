package com.xz.activeplan.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.find.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 图片轮播
 * Created by Administrator on 2016/6/21.
 */
public class UtilesBlannerViewPager<T> {
    private static UtilesBlannerViewPager instance = null;
    private ViewPager viewPager;
    private LinearLayout.LayoutParams params;
    private int pointIndex = 3;
    private ScheduledExecutorService scheduledExecutorService;
    private LinearLayout linearLayout;
    private android.view.View.OnClickListener listener;
    private List<T> list;
    private List<String> listImageUrl;
    private List<String> listPlayStatus;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    break;
            }
        }
    };

    public UtilesBlannerViewPager() {

    }

    public static UtilesBlannerViewPager getInstance() {
        if (instance == null) {
            instance = new UtilesBlannerViewPager();
        }
        return instance;
    }

    public void UtilesBlannerViewPager(final Context context, View view, final List<T> list, List<String> listImageUrl, List<String> listPlaystatus, final GetPosition getPosition) {
        this.list = list;
        this.listImageUrl = listImageUrl;
        this.listPlayStatus = listPlaystatus;
        viewPager = (ViewPager) view.findViewById(R.id.id_ViewPager);
        linearLayout = (LinearLayout) view.findViewById(R.id.id_LinePoints);
        List<View> listCircular = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            View from = LayoutInflater.from(context).inflate(R.layout.view_blanar_item, null);
            final ImageView imageView = (ImageView) from.findViewById(R.id.banner_iv);
            TextView textView = (TextView) from.findViewById(R.id.tv_lable);
            textView.setVisibility(View.GONE);
            // 设置广告图   //发生OOM异常
            Picasso.with(context).load(listImageUrl.get(i)).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(imageView);
           textView.setText(listPlaystatus.get(i));
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            listCircular.add(from);
            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) imageView.getTag();
                    getPosition.getPosition(tag);
                }
            });
            // 设置圆圈点
            View viewLay = new View(context);
            int width = DensityUtil.dip2px(context,5);
            int height = DensityUtil.dip2px(context,5);
            params = new LinearLayout.LayoutParams(width, height);
            params.leftMargin = 10;
            viewLay.setBackgroundResource(R.drawable.select_viewpager_shape);
            viewLay.setLayoutParams(params);
            viewLay.setEnabled(false);
            linearLayout.addView(viewLay);
        }
        BannerAdapter mAdapter = new BannerAdapter(listCircular);
        viewPager.setAdapter(mAdapter);
        if (null != linearLayout) {
            BannerListener bannerListener = new BannerListener(linearLayout);
            viewPager.addOnPageChangeListener(bannerListener);
        }
        viewPager.setCurrentItem(Integer.MAX_VALUE / 10000);
        if (linearLayout.getChildAt(pointIndex) != null) {
            linearLayout.getChildAt(pointIndex).setEnabled(true);
            onStartBlannr();
        }
    }

    public void stopViewPager() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        if (listImageUrl != null) {
            listImageUrl.clear();
        }
        if (list != null) {
            list.clear();
        }
    }

    public void onStartBlannr() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();//关机
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //每隔5秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 5, TimeUnit.SECONDS);
    }

    public void setOnClickListener(android.view.View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 获取图片的下标
     */
    public interface GetPosition {
        void getPosition(int position);
    }

    //切换图片
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            if (listImageUrl.size() > 0) {
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    //实现VierPager监听器接口
    class BannerListener implements ViewPager.OnPageChangeListener {
        private LinearLayout mLinearLayout;

        public BannerListener(LinearLayout mLinearLayout) {
            this.mLinearLayout = mLinearLayout;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            if (list.size() > 0 && null != mLinearLayout) {
                int newPosition = position % list.size();
                if (null != mLinearLayout.getChildAt(newPosition)) {
                    mLinearLayout.getChildAt(newPosition).setEnabled(true);
                }
                if (mLinearLayout.getChildAt(pointIndex) != null) {
                    mLinearLayout.getChildAt(pointIndex).setEnabled(false);
                }
                // 更新标志位
                pointIndex = newPosition;
            }
        }
    }
}