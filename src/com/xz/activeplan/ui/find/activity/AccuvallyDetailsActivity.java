package com.xz.activeplan.ui.find.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.TicketAddJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.ui.recommend.activity.SignUpTicketActivity;
import com.xz.activeplan.ui.recommend.activity.SponsorDetailActivity;
import com.xz.activeplan.ui.recommend.activity.WebActivity;
import com.xz.activeplan.utils.JsonUtils;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.ShareDialog;

import java.io.IOException;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import io.rong.ApiHttpClient;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;

/**
 * 活动详情界面activity
 *
 * @author johnny
 */
public class AccuvallyDetailsActivity extends BaseFragmentActivity implements OnClickListener, ClassObserver {
    private static final String TAG = AccuvallyDetailsActivity.class.getSimpleName();
    private static final String WEBVIEW_URL = "http://www.yueba.cc/h5/activty.html?activtyid=";
    public static ActiveinfosBean mActiveinfosBean;
    public static ArrayList<TicketAddBean> ticketTypes;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Utiles.log("handleMessage  ___msg.what:" + msg.what + ";msg.arg1:" + msg.arg1);
            if (msg.what == 1 && msg.arg1 == 200) {
                RongIM.getInstance().startGroupChat(AccuvallyDetailsActivity.this, mActiveinfosBean.getActiveid() + "", mActiveinfosBean.getName());
            }
        }
    };
    private LinearLayout hsvChildll, share_ly;
    private ImageView ivDetailsLogo, iv_org_logo;
    private TextView tvHeadTitle, tvDetailsOrgName, tvDetailsRegTicket, tv_accu_brief;
    private TextView tvDetailsTitle, tvVisitNum, tvLikeNum, tvDetailsTime, tvDetailsAddress, tvDetailsTicket;
    private ActiveinfosBean mTempActiveinfosBean;
    private UserInfoBean myUserInfoBean;
    private ImageView llCollect;
    private ImageView ivsupport, ivgroup;
    private boolean isCollect = false;
    private ArrayList<ActiveinfosBean> tuijians;
    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_accuvally_details);
        ClassConcrete.getInstance().addObserver(this);
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            Utiles.log(" 融云断开重连 ");
            Utiles.connectRongIM(this);
        }
        init();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            mTempActiveinfosBean = (ActiveinfosBean) intent.getSerializableExtra("data");
            if (mTempActiveinfosBean != null) {
                loadData(mTempActiveinfosBean.getActiveid());
            } else {
                finish();
            }
        }
    }

    public int getDisPlayWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width * 58 / 100;
    }

    private void init() {
        findViewById(R.id.lyDetailsAddr).setOnClickListener(this);
        tvDetailsRegTicket = (TextView) findViewById(R.id.tvDetailsRegTicket);
        findViewById(R.id.lyDetailsOrg).setOnClickListener(this);  //主办方信息
        hsvChildll = (LinearLayout) findViewById(R.id.hsvChildll);
        share_ly = (LinearLayout) findViewById(R.id.share_ly);
        share_ly.setVisibility(View.VISIBLE);
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        findViewById(R.id.two_img).setOnClickListener(this); //分享
        ivDetailsLogo = (ImageView) findViewById(R.id.ivDetailsLogo);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDisPlayWidth());
        ivDetailsLogo.setLayoutParams(lp);
        tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        tv_accu_brief = (TextView) findViewById(R.id.tv_accu_brief);
        findViewById(R.id.id_TextViewMoreIntroduction).setOnClickListener(this);//更多详细

        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        tvVisitNum = (TextView) findViewById(R.id.tvVisitNum);
        tvLikeNum = (TextView) findViewById(R.id.tvLikeNum);
        tvDetailsTime = (TextView) findViewById(R.id.tvDetailsTime);
        tvDetailsAddress = (TextView) findViewById(R.id.tvDetailsAddress);
        tvDetailsTicket = (TextView) findViewById(R.id.tvDetailsTicket);
        findViewById(R.id.tvContactOrganizer).setOnClickListener(this);

        iv_org_logo = (ImageView) findViewById(R.id.iv_org_logo);
        tvDetailsOrgName = (TextView) findViewById(R.id.tvDetailsOrgName);

        findViewById(R.id.relyDetailsTicket).setOnClickListener(this); //票务

        llCollect = (ImageView) findViewById(R.id.llCollect);//收藏的图标
        ivsupport = (ImageView) findViewById(R.id.iv_support);
        ivgroup = (ImageView) findViewById(R.id.iv_groupchart);  //群聊
        ivgroup.setOnClickListener(this);
        ivsupport.setOnClickListener(this);
        share_ly.setOnClickListener(this);

        Utiles.headManager(this, R.string.title_activedetail);
        ShareSDK.initSDK(AccuvallyDetailsActivity.this);
        mShareDialog = new ShareDialog(this);

        mShareDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvShareSina:
                        ShareSDKUtils.shareSina( "我在约吧互动上发现了一个好活动 快来看看吧 "
                                + mTempActiveinfosBean.getName()+UrlsManager.ACTIVEINFO_URL+mTempActiveinfosBean.getActiveid(),
                                mTempActiveinfosBean.getActiveurl());
                        mShareDialog.dismiss();
                        break;
                    case R.id.tvShareQzone:
                        ShareSDKUtils.shareQZone("【活动】"+mTempActiveinfosBean.getName(),UrlsManager.ACTIVEINFO_URL+mTempActiveinfosBean.getActiveid(),
                                mTempActiveinfosBean.getContent(),mTempActiveinfosBean.getActiveurl());
                        mShareDialog.dismiss();
                        break;
                    case R.id.tvSharewx:
                        ShareSDKUtils.shareWechat("【活动】"+mTempActiveinfosBean.getName(),mTempActiveinfosBean.getContent(),
                                mTempActiveinfosBean.getActiveurl(),UrlsManager.ACTIVEINFO_URL+mTempActiveinfosBean.getActiveid());
                        mShareDialog.dismiss();
                        break;
                    case R.id.tvSharewxpy:
                        ShareSDKUtils.shareWechatMoments( "【活动】"+ mTempActiveinfosBean.getName(),
                                mTempActiveinfosBean.getActiveurl(),UrlsManager.ACTIVEINFO_URL+mTempActiveinfosBean.getActiveid());
                        mShareDialog.dismiss();
                        break;
                    case R.id.tvShareMsg:
                        ShareSDKUtils.shareShortMessage( "我在约吧互动上发现了一个好活动 快来看看吧 "
                                + mTempActiveinfosBean.getName()+UrlsManager.ACTIVEINFO_URL+mTempActiveinfosBean.getActiveid());
                        break;
                }
            }
        });

        if (SharedUtil.isLogin(this)) {
            myUserInfoBean = SharedUtil.getUserInfo(this);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mTempActiveinfosBean = (ActiveinfosBean) intent.getSerializableExtra("data");
            Utiles.log(" Bean:" + mTempActiveinfosBean.toString());
            if (mTempActiveinfosBean != null) {
                //fillData(mTempActiveinfosBean) ;
                loadData(mTempActiveinfosBean.getActiveid());
            } else {
                finish();
            }
        }
        llCollect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utiles.log("点击开始动画2");
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Utiles.log("按下");
                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator animatory = ObjectAnimator.ofFloat(llCollect, "scaleY", 1.0f, 0.5f);
                    ObjectAnimator animatorx = ObjectAnimator.ofFloat(llCollect, "scaleX", 1.0f, 0.5f);
                    animatorSet.setDuration(450);
                    BounceInterpolator bounce = new BounceInterpolator();
                    animatorSet.setInterpolator(bounce);
                    animatorSet.playTogether(animatorx, animatory);
                    animatorSet.start();
                }

                if (event.getAction() == KeyEvent.ACTION_UP) {

                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator animatorx = ObjectAnimator.ofFloat(llCollect, "scaleX", 0.5f, 1.5f, 1.0f);
                    ObjectAnimator animatory = ObjectAnimator.ofFloat(llCollect, "scaleY", 0.5f, 1.5f, 1.0f);
                    animatorSet.setDuration(450);
                    animatorSet.setInterpolator(new BounceInterpolator());
                    animatorSet.playTogether(animatorx, animatory);
                    animatorSet.start();
                    if (isCollect) {
                        cancelCollect();
                    } else {
                        collect();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {

                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator animatorx = ObjectAnimator.ofFloat(llCollect, "scaleX", 0.5f, 1.5f, 1.0f);
                    ObjectAnimator animatory = ObjectAnimator.ofFloat(llCollect, "scaleY", 0.5f, 1.5f, 1.0f);
                    animatorSet.setDuration(450);
                    BounceInterpolator bounce = new BounceInterpolator();
                    animatorSet.setInterpolator(bounce);
                    animatorSet.playTogether(animatorx, animatory);
                    animatorSet.start();
                    if (isCollect) {
                        cancelCollect();
                    } else {
                        collect();
                    }
                }
                return true;
            }
        });
    }

    private void loadData(int activeId) {

        int myUserId = -1;//代表没有登录
        if (myUserInfoBean != null) {
            myUserId = myUserInfoBean.getUserid();
        } else {
            myUserInfoBean = SharedUtil.getUserInfo(AccuvallyDetailsActivity.this);
            myUserId = myUserInfoBean.getUserid();
        }
        /**
         * 得到活动的详细信息
         */
        ActiveServiceImpl.getInstance().getActive(myUserId, activeId, new StringCallback() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(String response) {
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ActiveinfosJson activeinfosJson = new ActiveinfosJson();
                        obj = activeinfosJson
                                .parseJson(statusBean
                                        .getData());
                        if (obj != null) {
                            mActiveinfosBean = (ActiveinfosBean) obj;
                            try {
                                mActiveinfosBean.setIssellout(JsonUtils.getBoolean("issellout", statusBean
                                        .getData()));
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            fillData(mActiveinfosBean);
                        }
                        try {
                            TicketAddJson ticketAddJosn = new TicketAddJson();

                            ticketTypes = (ArrayList<TicketAddBean>) ticketAddJosn.analysisJson2Object(JsonUtils.getString("ticketTypes", statusBean
                                    .getData()));
                            tuijians = (ArrayList<ActiveinfosBean>) activeinfosJson
                                    .analysisJson2Object(JsonUtils.getString("pushActives", statusBean
                                            .getData()));

                            addTuiJian();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtil.showShort("活动获取失败!");
                    }
                } else {
                    ToastUtil.showShort("活动获取失败!");
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "onFailure = " + e.getMessage());
                ToastUtil.showShort("活动获取失败!");
            }
        });
    }

    private void fillData(ActiveinfosBean bean) {
        if (!TextUtils.isEmpty(bean.getActiveurl())) {
            Picasso.with(this).load(bean.getActiveurl()).placeholder(R.drawable.default_details_image).error(R.drawable.default_details_image).into(ivDetailsLogo);
        }
        if (!TextUtils.isEmpty(bean.getHosthearurl())) {
            Picasso.with(this).load(bean.getHosthearurl()).placeholder(R.drawable.default_square_image).error(R.drawable.default_square_image).into(iv_org_logo);
        }
        tvDetailsTitle.setText(bean.getName());

        tvVisitNum.setText("浏览" + bean.getSeenum() + "次");

        if (myUserInfoBean != null) {
            //登录活动的数据才有收藏次数，没登录活动的数据没有收藏次数
            tvLikeNum.setText("收藏" + bean.getCollectnum() + "次");
        }

        tvDetailsTime.setText(TimeUtils.getTime(bean.getStartdate() + "") + " - " + TimeUtils.getTime(bean.getEnddate() + ""));

        tvDetailsAddress.setText(bean.getAddress());
        tv_accu_brief.setText(Html.fromHtml(bean.getContent()));
        if (bean.isCharge()) {
            tvDetailsTicket.setText("￥0-" + bean.getMoney());
        } else {
            tvDetailsTicket.setText("免费");
        }

        tvDetailsOrgName.setText(bean.getHostname());

        if (bean.isIscollect()) {
            llCollect.setImageResource(R.drawable.colection_red);
            isCollect = true;

        } else {
            llCollect.setImageResource(R.drawable.collection_wihte);
            isCollect = false;
        }
        if (bean.isIssellout()) {
            tvDetailsRegTicket.setText("名额已满");
            tvDetailsRegTicket.setOnClickListener(null);
            tvDetailsRegTicket.setBackgroundColor(Color.parseColor("#cccccc"));
        } else if (bean.isIsapply()) {
            tvDetailsRegTicket.setText("已报名活动");
            tvDetailsRegTicket.setOnClickListener(null);
            tvDetailsRegTicket.setBackgroundColor(Color.parseColor("#cccccc"));
        } else {
            tvDetailsRegTicket.setText("立即报名");
            tvDetailsRegTicket.setOnClickListener(this);
            tvDetailsRegTicket.setBackgroundResource(R.drawable.selector_click_btn);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:  //返回
                finish();
                break;
            case R.id.lyDetailsAddr:    //详细地址
                if (mActiveinfosBean != null) {
                    intent = new Intent(AccuvallyDetailsActivity.this, MapActivity.class);
                    intent.putExtra("address", mActiveinfosBean.getAddress());
                    startActivity(intent);
                }
                break;
            case R.id.relyDetailsTicket:  //点击进入报名
                if(!TextUtils.isEmpty(userInfoBean.getPhonenum())){
                    if (mActiveinfosBean != null) {
                        intent = new Intent(AccuvallyDetailsActivity.this, SignUpTicketActivity.class);
                        intent.putExtra("data", mActiveinfosBean);
                        startActivity(intent);
                    }
                }else{
                    Utils.showBingPhone(AccuvallyDetailsActivity.this);
                }

                break;
            case R.id.tvContactOrganizer:  //联系主办方 私聊
                if (SharedUtil.isLogin(this)) {
                    if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus()
                            .equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        RongIM.getInstance().startPrivateChat(AccuvallyDetailsActivity.this, mTempActiveinfosBean.getUserid() + "", mTempActiveinfosBean.getHostname());
                    }
                } else {
                    intent = new Intent(AccuvallyDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.lyDetailsOrg:  //主办方信息
                if (mActiveinfosBean != null) {
                    intent = new Intent(AccuvallyDetailsActivity.this, SponsorDetailActivity.class);
                    intent.putExtra("data", mActiveinfosBean);
                    startActivity(intent);
                }
                break;
            case R.id.tvDetailsRegTicket:   //立即报名//tvDetailsRegTicket
                XZApplication.getInstance().setTicketTypes(null);
                if (SharedUtil.isLogin(this)) {
                    myUserInfoBean = SharedUtil.getUserInfo(this);
                    if (mActiveinfosBean != null) {
                        if (!mActiveinfosBean.isIsapply()) {
                            intent = new Intent(AccuvallyDetailsActivity.this, SignUpTicketActivity.class);
                            intent.putExtra("data", mActiveinfosBean);
                            XZApplication.getInstance().setTicketTypes(ticketTypes);
                            startActivity(intent);
                        } else {
                            ToastUtil.showShort("你已经报名!");
                        }
                    }
                } else {
                    intent = new Intent(AccuvallyDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_groupchart://群聊

                if (!SharedUtil.isLogin(this)) {
                    intent = new Intent(AccuvallyDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (mActiveinfosBean != null) {
                    if (!mActiveinfosBean.isIsapply()) {
                        ToastUtil.showShort("亲，赶紧报名参加吧!");
                        return;
                    }
                }
                connectChart();
                break;
            case R.id.id_TextViewMoreIntroduction:
                Utiles.log("-------->更多详情");
                intent = new Intent(AccuvallyDetailsActivity.this, WebActivity.class);
                if (mActiveinfosBean != null) {
                    intent.putExtra("url", WEBVIEW_URL + mActiveinfosBean.getActiveid());
                }
                startActivity(intent);
                break;
            case R.id.two_img:
            case R.id.share_ly:
                mShareDialog.show();
                break;
            case R.id.iv_support://赞助
                Utiles.skipNoData(SponsorAcitivty.class);
                break;

        }
    }

    private void joinGroup() {

        Utiles.log("joinGroup");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SdkHttpResult result = null;
                //获取token
                try {
                    //加入群
                    result = ApiHttpClient.joinGroup(
                            UrlsManager.key, UrlsManager.secret,  //AppKey AppSecrt
                            myUserInfoBean.getUserid() + "", mActiveinfosBean.getActiveid() + "",  //登录用户ID//活动的ID//活动的名称
                            mActiveinfosBean.getName(), FormatType.json);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.arg1 = result.getHttpCode();
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void connectChart() {
        if (SharedUtil.isLogin(this)) {
            myUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            Intent intent = new Intent(AccuvallyDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus()
                .equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {

            Utiles.log("加入群聊");
            joinGroup();

            return;
        }

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                return new UserInfo(myUserInfoBean.getUserid() + "", myUserInfoBean.getUsername(), Uri.parse(myUserInfoBean.getHeadurl()));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }
        }, true);
    }

    //取消收藏
    private void cancelCollect() {
        if (SharedUtil.isLogin(this)) {
            myUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            Intent intent = new Intent(AccuvallyDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (mActiveinfosBean == null || myUserInfoBean == null) {
            return;
        }

        UserInfoServiceImpl.getInstance().cancelCollect(myUserInfoBean.getUserid(), mActiveinfosBean.getActiveid(), new StringCallback() {
            @Override
            public void onResponse(String response) {
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ToastUtil.showShort("取消收藏成功!");
                        llCollect.setImageResource(R.drawable.collection_wihte);
                        isCollect = false;
                    } else {
                        ToastUtil.showShort("取消收藏失败!");
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("取消收藏失败!");
            }
        });

    }

    //收藏
    private void collect() {
        if (SharedUtil.isLogin(this)) {
            myUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            Intent intent = new Intent(AccuvallyDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (mActiveinfosBean == null || myUserInfoBean == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().collectAction(myUserInfoBean.getUserid(), mActiveinfosBean.getActiveid(), new StringCallback() {

            @Override
            public void onResponse(String response) {
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ToastUtil.showShort("收藏成功!");
                        llCollect.setImageResource(R.drawable.colection_red);
                        isCollect = true;
                    } else {
                        ToastUtil.showShort("收藏失败!");
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("收藏失败!");
            }
        });

    }

    private void addTuiJian() {
        if (tuijians == null) {
            return;
        }
        hsvChildll.removeAllViews();
        for (int i = 0; i < tuijians.size(); i++) {
            final ActiveinfosBean bean = tuijians.get(i);
            //发生OOM异常
            View view = View.inflate(this, R.layout.listitem_home_recommend, null);
            TextView tvItemTitle, tvItemAddress, tvItemTime, tvItemVisitNum, tvItemPriceArea;
            ImageView ivItemRecommendImg;
            ivItemRecommendImg = (ImageView) view.findViewById(R.id.ivItemRecommendImg);
            tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);
            tvItemAddress = (TextView) view.findViewById(R.id.tvItemAddress);
            tvItemTime = (TextView) view.findViewById(R.id.tvItemTime);
            tvItemVisitNum = (TextView) view.findViewById(R.id.tvItemVisitNum);
            tvItemPriceArea = (TextView) view.findViewById(R.id.tvItemPriceArea);
            Picasso.with(this).load(bean.getActiveurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(ivItemRecommendImg);
            tvItemTitle.setText(bean.getName());
            tvItemAddress.setText(bean.getAddress());
            tvItemTime.setText(TimeUtils.getTime1(bean.getStartdate() + ""));

            tvItemVisitNum.setText(bean.getSeenum() + "");
            if (bean.isCharge()) {
                tvItemPriceArea.setText("￥" + bean.getMoney());
            } else {
                tvItemPriceArea.setText("免费");
            }

            hsvChildll.addView(view);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(AccuvallyDetailsActivity.this,
                            AccuvallyDetailsActivity.class);
                    intent.putExtra("data", bean);
                    AccuvallyDetailsActivity.this.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActiveinfosBean != null) {
            mActiveinfosBean = null;
        }
        if (ticketTypes != null) {
            ticketTypes = null;
        }
        ClassConcrete.getInstance().removeObserver(this);
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.ACCOUNT_BAOMING_TYPE) {
            if (mActiveinfosBean != null) {
                mActiveinfosBean.setIsapply(true);
            }
        } else if (event.getType() == EventType.LOGIN_NOTIFI_TYPE) {
            if (mTempActiveinfosBean != null) {
                loadData(mTempActiveinfosBean.getActiveid());
            }
        }
        return false;
    }
}
