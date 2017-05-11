package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ShareDialog;

/**
 *  直播管理详情页面
 */
public class LiveManagerInfoActivity extends BaseFragmentActivity implements View.OnClickListener {

    private LiveTvBean liveTvBean;
    private ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_live_manager_info);
        liveTvBean = (LiveTvBean) getIntent().getSerializableExtra("data");
        initView();

    }

    /**
     * 加载视图
     */
    private void initView() {
        Utiles.headManager(this,liveTvBean.getTitle());
        findViewById(R.id.tvLoginAndReg);
        ImageView ivCover = (ImageView) findViewById(R.id.liveInfo_ivCover);
        TextView tvTime = (TextView) findViewById(R.id.liveInfo_tvTime);
        Picasso.with(this).load(liveTvBean.getCoverurl()).centerCrop().fit().error(R.drawable.default_details_image)
                .placeholder(R.drawable.default_details_image).into(ivCover);
        String time = TimeUtils.formatTime(liveTvBean.getStartdate());
        tvTime.setText(time);
        Utiles.log("liveManager-------"+liveTvBean.toString());

        findViewById(R.id.liveInfo_tvSeeBack).setOnClickListener(this);  //观看回放
        findViewById(R.id.liveInfo_tvShare).setOnClickListener(this);   //分享

        shareDialog = new ShareDialog(this);
        shareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvShareSina:
                        ShareSDKUtils.shareSina( "分享视频"+liveTvBean.getTitle()+liveTvBean.getUrl(),liveTvBean.getCoverurl());
                        shareDialog.dismiss();
                        break;
                    case R.id.tvShareQzone:
                        ShareSDKUtils.shareQZone( "分享视频"+liveTvBean.getTitle(),liveTvBean.getUrl(),",",liveTvBean.getCoverurl());
                        shareDialog.dismiss();
                        break;
                    case R.id.tvSharewx:
                        ShareSDKUtils.shareWechat( "分享视频"+liveTvBean.getTitle(),"",liveTvBean.getCoverurl(),liveTvBean.getUrl());
                        shareDialog.dismiss();
                        break;
                    case R.id.tvSharewxpy:
                        ShareSDKUtils.shareWechatMoments( "分享视频"+liveTvBean.getTitle(),liveTvBean.getCoverurl(),liveTvBean.getUrl());
                        shareDialog.dismiss();
                        break;
                    case R.id.tvShareMsg:
                        ShareSDKUtils.shareShortMessage("分享视频"+liveTvBean.getTitle()+liveTvBean.getUrl());
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.liveInfo_tvSeeBack:  //观看回放 //跳转到视频播放页面
                Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,liveTvBean);
                break;
            case R.id.liveInfo_tvShare:   //分享
                shareDialog.show();
                break;
        }
    }
}
