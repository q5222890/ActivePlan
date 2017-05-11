package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;

public class CollectDialog  extends AlertDialog implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private TextView tvDialogMistake,tvDialogRemove ;
	private android.view.View.OnClickListener listener ;

	public CollectDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_collect, null);
		
		tvDialogMistake = (TextView)mView.findViewById(R.id.tvDialogMistake) ;
		tvDialogRemove = (TextView)mView.findViewById(R.id.tvDialogRemove) ;
		
		tvDialogMistake.setOnClickListener(this) ;
		tvDialogRemove.setOnClickListener(this) ;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}
	
	public void setOnClickListener(android.view.View.OnClickListener listener){
		this.listener = listener ;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tvDialogMistake:
			dismiss() ;
			break ;
		case R.id.tvDialogRemove:
			if(listener != null){
				listener.onClick(v) ;
			}
			break ;
		}
	}

}
