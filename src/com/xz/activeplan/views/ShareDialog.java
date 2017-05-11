package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;

public class ShareDialog  extends AlertDialog implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private TextView tvCancel,tvShareSina,tvShareQzone,tvSharewx,tvSharewxpy,tvShareMsg ;
	
	private android.view.View.OnClickListener listener ;

	public ShareDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_share, null);
		
		tvCancel = (TextView)mView.findViewById(R.id.tvCancel) ;
		tvShareSina = (TextView)mView.findViewById(R.id.tvShareSina) ;
		tvShareQzone = (TextView)mView.findViewById(R.id.tvShareQzone) ;
		tvSharewx = (TextView)mView.findViewById(R.id.tvSharewx) ;
		tvSharewxpy = (TextView)mView.findViewById(R.id.tvSharewxpy) ;
		tvShareMsg = (TextView)mView.findViewById(R.id.tvShareMsg) ;
		
		tvCancel.setOnClickListener(this) ;
		tvShareSina.setOnClickListener(this) ;
		tvShareQzone.setOnClickListener(this) ;
		tvSharewx.setOnClickListener(this) ;
		tvSharewxpy.setOnClickListener(this) ;
		tvShareMsg.setOnClickListener(this) ;
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
		case R.id.tvCancel:
			dismiss() ;
			break;
		case R.id.tvShareSina:
		case R.id.tvShareQzone:
		case R.id.tvSharewx:
		case R.id.tvSharewxpy:
		case R.id.tvShareMsg:
			if(listener != null){
				listener.onClick(v) ;
			}
			break;
		}
	}


	
}
