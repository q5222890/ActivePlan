package com.xz.activeplan.ui.live.view;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	public static SurfaceHolder mHolder;
	private Camera mCamera;
	private List<Size> mSupportedPreviewSizes;

	public MySurfaceView(Context context) {
		super(context);
		init();
	}
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	private void init() {
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null)
				mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}
	 public void setCamera(Camera camera) {
	        mCamera = camera;
	        if (mCamera != null) {
	        	mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
				//获取硬件所支持的尺寸�
	            requestLayout();
	        }
	    }
	  public void switchCamera(Camera camera) {
		  setCamera(camera);
	       try {
	           camera.setPreviewDisplay(mHolder);
	       } catch (IOException exception) {
	    	    mCamera.release();
				mCamera = null;
	       }
	       Camera.Parameters parameters = camera.getParameters();
	       requestLayout();
//	       parameters.setPreviewSize(640, 480);
	       camera.setParameters(parameters);
	    }
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mCamera != null){
			Camera.Parameters parameters = mCamera.getParameters();
//			parameters.setPreviewSize(640, 480);//根据硬件所支持的分辨率设置预览尺寸�����Ԥ���ߴ�
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null){
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
}
