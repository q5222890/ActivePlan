package com.xz.activeplan.ui.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;

/**
 * 直播协议
 * 有回调  同意则可以直播
 */
public class LiveAgreementActivity extends BaseFragmentActivity {


    private WebView webView ;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_agreement);
        Utiles.setStatusBar(this);
        initView();
    }

    /**
     *
     */
    private void initView() {
        Utiles.headManager(this,"直播服务协议");
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("agree","no");
                setResult(201,intent);
                finish();
            }
        });
        webView = (WebView) findViewById(R.id.agreement_webView);
        //允许JavaScript执行
        webView.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
        webView.loadUrl("file:///android_asset/live_agreement.html");
        button  = (Button) findViewById(R.id.agreement_btAgree);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("agree","yes");
                setResult(201,intent);
                finish();
            }
        });

    }
}
