package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.xz.activeplan.R;

public class LocationMapDialog  extends AlertDialog {
	private Context mCtx;

	private View mView;
	
	private ListView location_listview ;
	private CheckBox checkBox;

	public LocationMapDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_location_map, null);
		location_listview = (ListView)mView.findViewById(R.id.location_listview) ;
		checkBox = (CheckBox)mView.findViewById(R.id.checkBox) ;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}


	
}
