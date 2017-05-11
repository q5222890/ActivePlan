package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xz.activeplan.R;

public class ExplainDialog  extends AlertDialog implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private ImageView ivCloseDialog ;

	public ExplainDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_explain, null);
		ivCloseDialog = (ImageView)mView.findViewById(R.id.ivCloseDialog) ;
		ivCloseDialog.setOnClickListener(this) ;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}

	@Override
	public void onClick(View arg0) {
		dismiss() ;
	}

}
