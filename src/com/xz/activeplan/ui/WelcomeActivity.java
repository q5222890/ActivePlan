package com.xz.activeplan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
/**
 * 欢迎界面activity
 * @author johnny
 *
 */
public class WelcomeActivity extends AppCompatActivity {
	private ImageView dynamic_logo ;
	private TextView tv_skip ;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_welcome) ;
//		Uri uridata = this.getIntent().getData();
//		String mydata = uridata.getQueryParameter("data");
//		Utiles.log("网页启动："+mydata);
		initViews();

	}
	private void initViews(){
		dynamic_logo = (ImageView)findViewById(R.id.dynamic_logo) ;
		tv_skip = (TextView)findViewById(R.id.tv_skip) ;
		/*String gg = SharedUtil.get(this, "ggimgurl") ;
		Utiles.log("广告：   "+gg);
		if(!TextUtils.isEmpty(gg)){
			Picasso.with(this).load(new File(gg)).error(R.drawable.yue_qidong).
					placeholder(R.drawable.yue_qidong).fit().centerCrop().into(dynamic_logo);
		}*/
		//跳过按钮
		tv_skip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = null;
				if(SharedUtil.getSplash(WelcomeActivity.this)){
					 intent = new Intent(WelcomeActivity.this,MainActivity.class) ;
				}else{
					 intent = new Intent(WelcomeActivity.this,SplashActivity.class) ;
				}
				startActivity(intent) ;
				finish() ;
			}
		}) ;

		/*dynamic_logo.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = null;
				if(SharedUtil.getSplash(WelcomeActivity.this)){
					Utiles.log("------01");
					 intent = new Intent(WelcomeActivity.this,MainActivity.class) ;
				}else{
					Utiles.log("------02");
					 intent = new Intent(WelcomeActivity.this,SplashActivity.class) ;
				}
				startActivity(intent) ;
				finish() ;
				
			}
		}, 500) ;*/


		AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
		anima.setDuration(3000);// 设置动画显示时间
		dynamic_logo.startAnimation(anima);
		anima.setAnimationListener(new AnimationImpl());
	}


	private class AnimationImpl implements Animation.AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {
			dynamic_logo.setImageResource(R.drawable.yue_qidong);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			Intent intent = null;
			if(SharedUtil.getSplash(WelcomeActivity.this)){
				Utiles.log("------01");
				intent = new Intent(WelcomeActivity.this,MainActivity.class) ;
			}else{
				Utiles.log("------02");
				intent = new Intent(WelcomeActivity.this,SplashActivity.class) ;
			}
			startActivity(intent) ;
			finish() ;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}
	}
}
