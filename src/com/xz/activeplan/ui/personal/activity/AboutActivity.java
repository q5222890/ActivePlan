package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.SplashActivity;
import com.xz.activeplan.utils.Utiles;

/**
 * 关于界面activity
 * @author johnny
 * 
 */
public class AboutActivity extends BaseFragmentActivity implements OnClickListener{
	
	private LinearLayout mLyBack ,mLyGuide,lySina;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_about_accuvally);
		initViews() ;
	}
	
	private void initViews(){
		mLyBack = (LinearLayout)findViewById(R.id.ly_back) ;
		mLyGuide = (LinearLayout)findViewById(R.id.lyGuide) ;
		lySina = (LinearLayout)findViewById(R.id.lySina);
		
		mLyBack.setOnClickListener(this);
		mLyGuide.setOnClickListener(this);
		lySina.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null ;
		switch(v.getId()){
		case R.id.ly_back:
			onBackPressed();
			break; 
		case R.id.lyGuide:
			//intent = new Intent(AboutActivity.this,GideActivity.class) ;
			intent = new Intent(AboutActivity.this,SplashActivity.class) ;
			intent.putExtra("flag", true) ;
			startActivity(intent);
			break;
		case R.id.lySina:
			Uri uri = Uri.parse("http://weibo.com/uxingzhe");
             intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
			break;
		}
		
	}
	
}
