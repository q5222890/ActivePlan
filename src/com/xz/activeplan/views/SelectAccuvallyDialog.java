package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xz.activeplan.R;

public class SelectAccuvallyDialog  extends AlertDialog {
	private Context mCtx;

	private View mView;
	
	private ImageView iv_close_select_accuvally_dialog ;
	private TextView tv_select_activity_submit ;
	private RadioButton rb_all_time,rb_start,rb_latest_week,rb_today,rb_tomorrow,rb_weeked ;
	private RadioButton rb_all_play_method,rb_free,rb_fee ;
	private RadioButton rb_default ,rb_nearly_accuvally,rb_hot_accuvally;
	private RadioGroup my_radio_group_time,rg_cost,rg_address;

	public SelectAccuvallyDialog(Context context) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_select_accuvally, null);
		
		iv_close_select_accuvally_dialog = (ImageView)mView.findViewById(R.id.iv_close_select_accuvally_dialog) ;
		tv_select_activity_submit = (TextView)mView.findViewById(R.id.tv_select_activity_submit) ;
		rb_all_time = (RadioButton)mView.findViewById(R.id.rb_all_time) ;
		rb_start= (RadioButton)mView.findViewById(R.id.rb_start) ;
		rb_latest_week= (RadioButton)mView.findViewById(R.id.rb_latest_week) ;
		rb_today= (RadioButton)mView.findViewById(R.id.rb_today) ;
		rb_tomorrow= (RadioButton)mView.findViewById(R.id.rb_tomorrow) ;
		rb_weeked = (RadioButton)mView.findViewById(R.id.rb_weeked) ;
		rb_all_play_method= (RadioButton)mView.findViewById(R.id.rb_all_play_method) ;
		rb_free= (RadioButton)mView.findViewById(R.id.rb_free) ;
		rb_fee = (RadioButton)mView.findViewById(R.id.rb_fee) ;
		rb_default = (RadioButton)mView.findViewById(R.id.rb_default) ;
		rb_nearly_accuvally= (RadioButton)mView.findViewById(R.id.rb_nearly_accuvally) ;
		rb_hot_accuvally= (RadioButton)mView.findViewById(R.id.rb_hot_accuvally) ;
		my_radio_group_time= (RadioGroup)mView.findViewById(R.id.my_radio_group_time) ;
		rg_cost= (RadioGroup)mView.findViewById(R.id.rg_cost) ;
		rg_address= (RadioGroup)mView.findViewById(R.id.rg_address) ;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}


	
}
