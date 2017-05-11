package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.activity.AccuvallyDetailsActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.lang.reflect.InvocationTargetException;

/**
 * webView页面
 * 
 * @author johnny
 *
 */
public class WebActivity extends BaseFragmentActivity implements OnClickListener {
	private WebView webView;
	private String url;
	private UserInfoBean myUserInfoBean;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_web);
		initViews();
	}

	private void initViews() {
		Utiles.headManager(this,R.string.string_MoreDetail);
		Utiles.setGone(findViewById(R.id.iv_support),findViewById(R.id.iv_groupchart));
		findViewById(R.id.tvDetailsRegTicket).setOnClickListener(this);
		webView = (WebView) findViewById(R.id.webView);
		//启用支持javascript
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		Intent intent = getIntent();
		if (intent != null) {
			url = intent.getStringExtra("url") ;
			// WebView加载web资源
			webView.loadUrl(url);
//			webView.loadDataWithBaseURL(url, entity.getProductDetail(), "text/html", "UTF-8", null);
//			tv.setText(Html.fromHtml(getString(R.string.html_text)));
			// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
					view.loadUrl(url);
					return true;
				}
			});
		}


	}
	
	//改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
			case R.id.tvDetailsRegTicket:
				XZApplication.getInstance().setTicketTypes(null);
				if (SharedUtil.isLogin(this)) {
					myUserInfoBean = SharedUtil.getUserInfo(this);
					if (AccuvallyDetailsActivity.mActiveinfosBean != null) {
						if (!AccuvallyDetailsActivity.mActiveinfosBean.isIsapply()) {
							intent = new Intent(this, SignUpTicketActivity.class);
							intent.putExtra("data", AccuvallyDetailsActivity.mActiveinfosBean);
							XZApplication.getInstance().setTicketTypes(AccuvallyDetailsActivity.ticketTypes);
							startActivity(intent);
						} else {
							ToastUtil.showShort("你已经报名!");
						}
					}
				} else {
					intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
				}
				break;
		}
	}


	@Override
	protected void onPause() {
		super.onPause();

		try {
			webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
