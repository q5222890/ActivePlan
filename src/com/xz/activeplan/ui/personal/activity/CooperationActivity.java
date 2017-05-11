package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.utils.ScreenHelper;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.AutoHeightViewPager;
import com.xz.activeplan.views.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 推广合作
 */
public class CooperationActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoHeightViewPager viewPager;
    private List<ImageView> viewList = new ArrayList<>();
    private List<String> urlList =new ArrayList<>();
    private String[] picurl = {
            "http://139.196.152.82/tryst_images/extension/cooperation_one_1.jpg",
            "http://139.196.152.82/tryst_images/extension/cooperation_two_2.jpg",
            "http://139.196.152.82/tryst_images/extension/cooperation_three_3.jpg",
            "http://139.196.152.82/tryst_images/extension/cooperation_four_4.jpg",
            "http://139.196.152.82/tryst_images/extension/cooperation_five_5.jpg"
    };
    private ImageView ivback;
    private CircleIndicator circleindicator;
    private ImageView imageview;
    private int width =ScreenHelper.getScreenWidth(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_cooperation);

        initView();
    }

    private void initView() {

        ivback = (ImageView) findViewById(R.id.ivback);
        ivback.setOnClickListener(this);
        viewPager = (AutoHeightViewPager) findViewById(R.id.viewpager_cooperation);
        circleindicator = (CircleIndicator) findViewById(R.id.circleIndicator);

        for (String aPicurl : picurl) {
            urlList.add(aPicurl);
            imageview = new ImageView(this);
            ViewGroup.LayoutParams vl =new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(vl);
            viewList.add(imageview);
        }
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new cooperrationAdapter());
        circleindicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                resetViewPagerHeight(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivback:
                finish();
                break;
        }
    }

    /**
     * 重新设置viewPager高度
     *
     * @param position
     */
    public void resetViewPagerHeight(int position) {
        View child = viewPager.getChildAt(position);
        if (child != null) {
            child.measure(0, 0);
            int h = child.getMeasuredHeight();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
            params.height = h;
            params.width = width;
            viewPager.setLayoutParams(params);
        }
    }

    public class cooperrationAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView imageView = viewList.get(position);
            Picasso.with(CooperationActivity.this)
                    .load(urlList.get(position))
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.default_details_image)
                    .into(imageView);
            view.addView(imageView, width,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            return imageView;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

}
