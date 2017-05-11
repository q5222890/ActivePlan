package com.xz.activeplan.ui.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.Utiles;

import java.text.DecimalFormat;

/***
 * 结束直播页面
 */
public class EndLiveActivity extends BaseFragmentActivity implements View.OnClickListener {


    private TextView endLive_tvNumbers;
    private TextView endLive_tvMoney;
    private Button endLive_btBack;
    private int total;
    private float money ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_end_live);
        total = getIntent().getIntExtra("total",0);     //观看人数
        money = getIntent().getFloatExtra("money",0);   //打赏金额

        initView();

    }

    private void initView() {
        endLive_tvNumbers = (TextView) findViewById(R.id.endLive_tvNumbers);  //观看人数
        endLive_tvMoney = (TextView) findViewById(R.id.endLive_tvMoney);  //打赏金额
        //分享
        findViewById(R.id.endLive_ivSina).setOnClickListener(this);
        findViewById(R.id.endLive_ivQQ).setOnClickListener(this);
        findViewById(R.id.endLive_ivWenxin).setOnClickListener(this);
        findViewById(R.id.endLive_ivFriend).setOnClickListener(this);

        //返回
        endLive_btBack = (Button) findViewById(R.id.endLive_btBack);

        endLive_btBack.setOnClickListener(this);

        //赋值
        DecimalFormat decimalFormat=new DecimalFormat("0.00"); //将金额格式化 例: 5.60元
        String p=decimalFormat.format(money);
        endLive_tvNumbers.setText(total+"");
        endLive_tvMoney.setText(p+"元");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.endLive_btBack:    //返回首页
                finish();
                break;
            case R.id.endLive_ivSina:
                ShareSDKUtils.shareSina("约吧直播","");
                break;
            case R.id.endLive_ivQQ:
                ShareSDKUtils.shareQZone("约吧直播","","","'");
                break;
            case R.id.endLive_ivWenxin:
                ShareSDKUtils.shareWechat("约吧直播","","","");
                break;
            case R.id.endLive_ivFriend:
                ShareSDKUtils.shareWechatMoments("约吧直播","","");
                break;
        }
    }
}
