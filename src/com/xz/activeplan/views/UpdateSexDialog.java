package com.xz.activeplan.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xz.activeplan.R;

public class UpdateSexDialog extends AlertDialog implements OnCheckedChangeListener {
	private Context mCtx;


	private View mView;

	private RadioButton radioButton1, radioButton2;
	private RadioGroup radioGroup;
	
	private OnCheckSexListener mOnCheckSexListener ;

	public UpdateSexDialog(Context context,int flag) {
		super(context, R.style.dialog);
		this.mCtx = context;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		mView = inflater.inflate(R.layout.dialog_update_sex, null);
		radioButton1 = (RadioButton) mView.findViewById(R.id.radioButton1);
		radioButton2 = (RadioButton) mView.findViewById(R.id.radioButton2);
		radioGroup = (RadioGroup) mView.findViewById(R.id.radioGroup);

		radioGroup.setOnCheckedChangeListener(this);
		
		if(flag==1){
			radioButton1.setChecked(true) ;
		}else{
			radioButton2.setChecked(true) ;
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getCheckedRadioButtonId()) {
		case R.id.radioButton1:
			if(mOnCheckSexListener != null){
				dismiss();
				mOnCheckSexListener.onCkeckSex(1);
			}
			break;
		case R.id.radioButton2:
			if(mOnCheckSexListener != null){
				dismiss();
				mOnCheckSexListener.onCkeckSex(0);
			}
			break;
		}
	}
	
	public void setOnCheckSexListener(OnCheckSexListener listener){
		this.mOnCheckSexListener = listener ;
	}
	
	public interface OnCheckSexListener{
		public void onCkeckSex(int flag) ;
	}

}
