package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xz.activeplan.R;

public class CheckBoxDialog  extends AlertDialog {
	private Context mCtx;

	private View mView;
	
	private Button bt_checkbox_submit ;
	private LinearLayout ll_add_checkbox_info ;

	public CheckBoxDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_checkbox, null);
		
		ll_add_checkbox_info = (LinearLayout)mView.findViewById(R.id.ll_add_checkbox_info) ;
		bt_checkbox_submit = (Button)mView.findViewById(R.id.bt_checkbox_submit) ;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}


	
}
