package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;

/**
 * 活动分类dialog弹框
 * @author johnny
 *
 */
public class ActiveCategoryDialog  extends AlertDialog  implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private TextView mTxt1,mTxt2,mTxt3,mTxt4,mTxt5,mTxt6,mTxt7,mTxt8,mTxt9,mTxt10,mTxt11,mTxt12,mTxt13 ;
	

	public ActiveCategoryDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_active_category, null);
		mTxt1 = (TextView)mView.findViewById(R.id.category1) ;
		mTxt2 = (TextView)mView.findViewById(R.id.category2) ;
		mTxt3 = (TextView)mView.findViewById(R.id.category3) ;
		mTxt4 = (TextView)mView.findViewById(R.id.category4) ;
		mTxt5 = (TextView)mView.findViewById(R.id.category5) ;
		mTxt6 = (TextView)mView.findViewById(R.id.category6) ;
		mTxt7 = (TextView)mView.findViewById(R.id.category7) ;
		mTxt8 = (TextView)mView.findViewById(R.id.category8) ;
		mTxt9 = (TextView)mView.findViewById(R.id.category9) ;
		mTxt10 = (TextView)mView.findViewById(R.id.category10) ;
		mTxt11 = (TextView)mView.findViewById(R.id.category11) ;
		mTxt12 = (TextView)mView.findViewById(R.id.category12) ;
		mTxt13 = (TextView)mView.findViewById(R.id.category13) ;
		
		mTxt1.setOnClickListener(this) ;
		mTxt2.setOnClickListener(this) ;
		mTxt3.setOnClickListener(this) ;
		mTxt4.setOnClickListener(this) ;
		mTxt5.setOnClickListener(this) ;
		mTxt6.setOnClickListener(this) ;
		mTxt7.setOnClickListener(this) ;
		mTxt8.setOnClickListener(this) ;
		mTxt9.setOnClickListener(this) ;
		mTxt10.setOnClickListener(this) ;
		mTxt11.setOnClickListener(this) ;
		mTxt12.setOnClickListener(this) ;
		mTxt13.setOnClickListener(this) ;
		
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
