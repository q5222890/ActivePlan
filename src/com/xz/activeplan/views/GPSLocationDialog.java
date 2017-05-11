package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;

public class GPSLocationDialog  extends AlertDialog implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private TextView tvCancel,tvSure ;
	
	private OnClickTrueListener mOnClickTrueListener ;

	public GPSLocationDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_gps_location, null);
		tvCancel = (TextView)mView.findViewById(R.id.tvCancel) ;
		tvSure = (TextView)mView.findViewById(R.id.tvSure) ;
		
		tvCancel.setOnClickListener(this) ;
		tvSure.setOnClickListener(this) ;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}
	
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tvCancel:
			if(mOnClickTrueListener != null){
				mOnClickTrueListener.onClickTrue(false) ;
			}
			break; 
		case R.id.tvSure:
			if(mOnClickTrueListener != null){
				mOnClickTrueListener.onClickTrue(true) ;
			}
			break ;
		}
	}
	
	public void setOnClickTrueListener(OnClickTrueListener listener){
		this.mOnClickTrueListener = listener ;
	}

	interface OnClickTrueListener{
		public void onClickTrue(boolean flag);
	}
	
}
