package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.adapter.FragmentAdapter;
import com.xz.activeplan.ui.personal.fragment.HostFragment;
import com.xz.activeplan.utils.DensityUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.List;

/**
 * 主办的活动
 * @author johnny
 *
 */
public class HostActivity extends BaseFragmentActivity implements OnClickListener{
	private ViewPager viewPager;
	private List<BaseFragment> mFragmentList = new ArrayList<BaseFragment>(
	);
	private FragmentAdapter mFragmentAdapter;
	/**
	 * Tab显示内容TextView
	 */
	private TextView mTabYTicket, mTabNTicket;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	private HostFragment mHostFragment1;
	private HostFragment mHostFragment2;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	private OrganizersBean mOrganizersBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_host_main);
		findById();
		init();
		initTabLineWidth();
	}

	private void findById() {
		mTabYTicket = (TextView) findViewById(R.id.id_chat_tv);
		mTabNTicket = (TextView) findViewById(R.id.id_friend_tv);
		mTabLineIv = (ImageView) findViewById(R.id.id_tab_line_iv);
		findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this); ;
		viewPager = (ViewPager) findViewById(R.id.id_page_vp);
		findViewById(R.id.id_tab_chat_ll).setOnClickListener(this); ;//已经发布活动
		findViewById(R.id.id_tab_friend_ll).setOnClickListener(this); ;//已经结束活动
	}

	private void init() {
		Intent intent = getIntent() ;
		if(intent != null){
			mOrganizersBean = (OrganizersBean)intent.getSerializableExtra("data") ;
		}else{
			ToastUtil.showShort("数据异常") ;
			finish() ;
			return;
		}
		mHostFragment1 = new HostFragment(1,mOrganizersBean.getHostid());
		mHostFragment2 = new HostFragment(0,mOrganizersBean.getHostid());
		mFragmentList.add(mHostFragment1);
		mFragmentList.add(mHostFragment2);

		mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager()
				, mFragmentList);
		viewPager.setAdapter(mFragmentAdapter);
		viewPager.setCurrentItem(0);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				Log.e("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记2个页面,
				 * 从左到右分别为0,1 
				 * 0->1; 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2)) + DensityUtil.dip2px(HostActivity.this, 40) ;

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 2) + currentIndex
							* (screenWidth / 2)) + DensityUtil.dip2px(HostActivity.this, 40);

				} 
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					mTabYTicket.setTextColor(Color.parseColor("#00BBFE"));
					break;
				case 1:
					mTabNTicket.setTextColor(Color.parseColor("#00BBFE"));
					break;
				}
				currentIndex = position;
			}
		});

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 3;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		mTabYTicket.setTextColor(Color.BLACK);
		mTabNTicket.setTextColor(Color.BLACK);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_tab_chat_ll:
			viewPager.setCurrentItem(0) ;
			break;
		case R.id.id_tab_friend_ll:
			viewPager.setCurrentItem(1) ;
			break;
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		}
	}

}
