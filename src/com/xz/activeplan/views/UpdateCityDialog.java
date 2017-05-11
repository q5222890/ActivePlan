package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xz.activeplan.R;

public class UpdateCityDialog extends AlertDialog implements OnClickListener {
	private Context mCtx;

	private View mView;

	private TextView tvUpdateCancel, tvUpdateSure;
	private android.view.View.OnClickListener listener;

	public UpdateCityDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_update_city ,null);
		tvUpdateCancel = (TextView) mView.findViewById(R.id.tvUpdateCancel);
		tvUpdateSure = (TextView) mView.findViewById(R.id.tvUpdateSure);
		tvUpdateCancel.setOnClickListener(this);
		tvUpdateSure.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}

	public void setOnClickListener(android.view.View.OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View arg0) {
		if (this.listener != null) {
			this.listener.onClick(arg0);
		}
	}

}
