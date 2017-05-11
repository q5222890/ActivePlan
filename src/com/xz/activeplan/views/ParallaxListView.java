package com.xz.activeplan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.xz.activeplan.R;

public class ParallaxListView extends ListView {

	private ImageView mImageView;
	// 初始高度
	private int mImageViewHeight = -1;
	// 最大拉伸高度
	private int mDrawableMaxHeight = -1;

	public ParallaxListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * 设置拉伸的图片
	 * 
	 * @param imageView
	 */
	public void setParallaxImageView(ImageView imageView) {
		this.mImageView = imageView;
		// 设置伸缩类型 -- 居中填充
		this.mImageView.setScaleType(ScaleType.CENTER_CROP);
	}

	/**
	 * 初始化图片加载进来最初的高度
	 * 
	 */
	public void setViewBounds() {
		if (mImageViewHeight == -1) {
			mImageViewHeight = mImageView.getHeight();
			if (mImageViewHeight < 0) {
				mImageViewHeight = getContext().getResources()
						.getDimensionPixelSize(R.dimen.size_default);
			}
		}

	}

	/**
	 * 滑动过头的时候回调
	 */
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// 控制ImageView的高度不断增加
		boolean isCollapse = resizeOverScrollBy(deltaY);

		// return true:下拉到边界的某个地方的时候不再往下拉
		return isCollapse ? true : super.overScrollBy(deltaX, deltaY, scrollX,
				scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
				maxOverScrollY, isTouchEvent);
	}

	/**
	 *  监听ListView滑动
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		// 获得ImageView的父控件
		View header = (View) mImageView.getParent();
		if (header.getTop() < 0 && mImageView.getHeight() > mImageViewHeight) {
			// 减小ImageView的高度 -- 不能超过图片最原始的高度
			mImageView.getLayoutParams().height = Math.max(
					mImageView.getHeight() + header.getTop(), mImageViewHeight);
			// ImageView所在的容器的高度也要变化：getTop--->0
			header.layout(header.getLeft(), 0, header.getRight(),
					header.getHeight());
			mImageView.requestLayout();
		}

	}
	

	private boolean resizeOverScrollBy(int deltaY) {
		// 下拉的过程当中，不断地控制ImageView的高度
		/**
		 * deltaY:是在超出滑动的时候每毫秒滑动的距离 -- 增量 (-往下拉过渡，+往上拉过渡) 大小：根据用户滑动的速度决定 一般滑动的速度
		 * -50~50
		 */
		if (deltaY < 0) {
			// 下拉过渡，不断增加ImageView的高度，deltay是负数，增加高度就是减去
			mImageView.getLayoutParams().height = mImageView.getHeight()
					- deltaY;
			// View重新调整宽高
			mImageView.requestLayout(); // onMeasure-->onLayout
		} else {
			// 上拉过渡，不断减小ImageView的高度，deltay是正数，减小高度还是减去
			if (mImageView.getHeight()>mImageViewHeight) {
				mImageView.getLayoutParams().height = Math.max(
						mImageView.getHeight() - deltaY, mImageViewHeight);
				// View重新调整宽高
				mImageView.requestLayout(); // onMeasure-->onLayout
			}
			
		}

		return false;
	}
	
	/**
	 * 监听触摸 -- 松开手
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 判断
		if (ev.getAction()== MotionEvent.ACTION_UP) {
			// 开启回弹的动画
			ResetAnimation animation=new ResetAnimation(mImageView,mImageViewHeight);
			animation.setDuration(300);
			mImageView.startAnimation(animation);
		}
		
		return super.onTouchEvent(ev);
	}
	
	public class ResetAnimation extends Animation{
		
		
		
		private ImageView mView;
		
		private int targetHeight;
		// 动画执行前的高度
		private int originalHeight;
		// 高度差
		private int extraHeight;

		public ResetAnimation(ImageView mImageView,int targetHeight) {
			this.mView=mImageView;
			this.targetHeight=targetHeight;
			this.originalHeight=mImageView.getHeight();
			extraHeight=originalHeight-targetHeight;
		}
		
		/**
		 * 动画不断地执行的时候会回调该方法
		 * interpolatedTime：范围是0
		 * 0ms-------------->300ms
		 * 当前的图片高度--->动画执行之前的高度-高度差*interpolatedTime
		 * extraHeight------>0
		 */
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			
			mView.getLayoutParams().height=(int) (originalHeight-extraHeight*interpolatedTime);
			mView.requestLayout();
			super.applyTransformation(interpolatedTime, t);
		}
	}

}
