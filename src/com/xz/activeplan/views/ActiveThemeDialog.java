package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;

public class ActiveThemeDialog  extends AlertDialog  implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private TextView mTxt1,mTxt2,mTxt3,mTxt4,mTxt5,mTxt6,mTxt7,mTxt8 ;
	

	public ActiveThemeDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_activite_theme, null);
		mTxt1 = (TextView)mView.findViewById(R.id.txt1) ;
		mTxt2 = (TextView)mView.findViewById(R.id.txt2) ;
		mTxt3 = (TextView)mView.findViewById(R.id.txt3) ;
		mTxt4 = (TextView)mView.findViewById(R.id.txt4) ;
		mTxt5 = (TextView)mView.findViewById(R.id.txt5) ;
		mTxt6 = (TextView)mView.findViewById(R.id.txt6) ;
		mTxt7 = (TextView)mView.findViewById(R.id.txt7) ;
		mTxt8 = (TextView)mView.findViewById(R.id.txt8) ;
		
		mTxt1.setOnClickListener(this) ;
		mTxt2.setOnClickListener(this) ;
		mTxt3.setOnClickListener(this) ;
		mTxt4.setOnClickListener(this) ;
		mTxt5.setOnClickListener(this) ;
		mTxt6.setOnClickListener(this) ;
		mTxt7.setOnClickListener(this) ;
		mTxt8.setOnClickListener(this) ;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}
	private android.view.View.OnClickListener listener ;

	
	public void setOnClickListener(android.view.View.OnClickListener listener){
		this.listener = listener ;
	}

	@Override
	public void onClick(View arg0) {
		dismiss();
		if(this.listener != null){
			this.listener.onClick(arg0) ;
		}
	}


	
}
