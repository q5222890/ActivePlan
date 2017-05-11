package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;

public class SelectPhotoDialog  extends AlertDialog implements android.view.View.OnClickListener{
	private Context mCtx;

	private View mView;
	
	private TextView tvPhotograph,tvAlbum ;
	
	private android.view.View.OnClickListener listener ;

	public SelectPhotoDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_select_photo, null);
		tvPhotograph = (TextView)mView.findViewById(R.id.tvPhotograph) ;
		tvAlbum = (TextView)mView.findViewById(R.id.tvAlbum) ;
		tvPhotograph.setOnClickListener(this) ;
		tvAlbum.setOnClickListener(this) ;
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
	public void onClick(View arg0) {
		if(this.listener != null){
			this.listener.onClick(arg0) ;
		}
	}


	
}
