package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;

public class SubConversationListActivtiy extends BaseFragmentActivity implements OnClickListener{
	
	 private TextView mTxtBack ;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Utiles.setStatusBar(this);
        setContentView(R.layout.activity_subconversationlist_layout);

        mTxtBack = (TextView)findViewById(R.id.id_TextViewName) ;
		mTxtBack.setOnClickListener(this);
		
		mTxtBack.setText("群组消息");
      }
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_TextViewName:
			finish() ;
			break;
		}
	}
}
