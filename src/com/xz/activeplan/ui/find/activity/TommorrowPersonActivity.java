package com.xz.activeplan.ui.find.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.LiveChatRoomActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.utils.LogUtil;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.views.MyGridView;
import com.xz.activeplan.views.ObservableScrollView;
import com.xz.activeplan.views.ShareDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主播详情
 * 头像 名字 播放数量 关注人数
 */
public class TommorrowPersonActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener, ObservableScrollView.ScrollViewListener, AdapterView.OnItemClickListener {

    private final static String TAG = "TommorrowPersonActivity";
    private TextView textViewPopularity, textViewName, textViewGuanZhuPersonNum, textViewPlayNum, textViewTitle, textViewPlayDate;
    private MyGridView gridView;
    private List<LiveTvBean> listLiveTvBean;
    private CommonAdapter<LiveTvBean> commonAdapter;
    private LiveTvBean liveTvBean;
    private SimpleDateFormat formatTime;
    private Date date;
    private String timeUpStr;
    private String timeDolweStr;
    private int page = 0;
    private ImageView imageViewBackground;
    private RelativeLayout relativeLayout_toolbar;
    private int CurrentY;
    private ShareDialog shareDialog;

    private Drawable background;
    private Button follow;   //关注
    private UtileStringRequst<LiveTvBean> utileStringRequst;
    private TommrrowHandler tommrrowHandler;
    private boolean isattend = false;
    private int otheruserid;
    private ScrollView scrollView;
    private PullToRefreshScrollView pullToRefreshScrollView;

    @Override
    protected void onDestroy() {
        if (listLiveTvBean != null && listLiveTvBean.size() != 0) {
            listLiveTvBean.clear();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_tommorrow_person);
        ClassConcrete.getInstance().addObserver(this);
        initView();//初始化
        NetWorkData();//加载数据

    }

    /**
     * 通过网络加载数据
     */
    public void NetWorkData() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
            return;
        }
        if (listLiveTvBean.size() == 0) {
            if (SharedUtil.isLogin(this)) {
                utileStringRequst.UtileStringRequst(this, UrlsManager.URL_VIDEO_LIST + page + "&pagesize=20&userid=" + liveTvBean.getUserid(),
                        LiveTvBean.class, tommrrowHandler, 0);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            commonAdapter.notifyDataSetChanged();
        }
    }


    public void getFollowData() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
        }
        if (listLiveTvBean.size() == 0) {
            UserInfoBean userInfo = SharedUtil.getUserInfo(this);
            if (null != userInfo) {
                utileStringRequst.UtileStringRequst(this, UrlsManager.URL_VIDEO_LIST + page + "&pagesize=20&userid=" + otheruserid,
                        LiveTvBean.class, tommrrowHandler, 1);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            commonAdapter.notifyDataSetChanged();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        tommrrowHandler = new TommrrowHandler();
        utileStringRequst = new UtileStringRequst<>();
        /**
         * gridView数据集合
         */
        listLiveTvBean = new ArrayList<LiveTvBean>();

        /**
         * 介绍传过来的数据
         */
        Intent intent = getIntent();
        if (intent != null) {
            liveTvBean = (LiveTvBean) intent.getSerializableExtra("data");
            if (liveTvBean != null) {
                Utiles.log(TAG + "liveTvBean：-------" + liveTvBean.toString());
            }

        }

        //头部视图
        relativeLayout_toolbar = (RelativeLayout) findViewById(R.id.tomorrow_person_rlHeaderView);
        relativeLayout_toolbar.setBackgroundColor(getResources().getColor(R.color.header_blue));
        selectBackColor(relativeLayout_toolbar, 0);
        // ((ObservableScrollView) findViewById(R.id.tomorrow_scrollView)).setScrollViewListener(this);

        View viewHeadBlackGround = findViewById(R.id.id_LineBlackGround);
        Drawable background = viewHeadBlackGround.getBackground();
        background.setAlpha(150);
        viewHeadBlackGround.setBackground(background);

        findViewById(R.id.id_ImageBlack).setOnClickListener(this);
        findViewById(R.id.id_ImageShare).setOnClickListener(this);//分享
        initShare();
        //关注Button
        follow = (Button) findViewById(R.id.id_ButtonGuanZhuPerson);

        if (liveTvBean.isIsattend()) {
            follow.setSelected(true);
            follow.setText("已关注");
            isattend = true;
        } else {
            follow.setSelected(false);
            follow.setText("关注");
            isattend = false;
        }
        follow.setOnClickListener(this);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.id_PullToRefreshScrollView);
        getTime("onPullDownToRefresh");
        getTime("onPullUpToRefresh");
        pullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        ImageView imageViewHeadPhoto = (ImageView) findViewById(R.id.id_ImageViewHeadPhoto);//头像

        if(!TextUtils.isEmpty(liveTvBean.getHeadurl()))
        Picasso.with(this).load(liveTvBean.getHeadurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(imageViewHeadPhoto);

        //第一个大的图片
        ImageView imageViewLiveTv = (ImageView) findViewById(R.id.id_ImageLiveTv);//直播视频
        if(!TextUtils.isEmpty(liveTvBean.getCoverurl()))
        Picasso.with(this).load(liveTvBean.getCoverurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(imageViewLiveTv);
        imageViewLiveTv.setOnClickListener(this);
        imageViewBackground = (ImageView) findViewById(R.id.id_ImageBackground);
        findViewById(R.id.id_RelativeLiveTving).setVisibility(View.VISIBLE);
        //用户头像
        if(!TextUtils.isEmpty(liveTvBean.getHeadurl()))
        Picasso.with(this).load(liveTvBean.getHeadurl()).placeholder(R.drawable.default_details_image).
                error(R.drawable.default_details_image).fit().centerCrop().into(imageViewBackground);
        //名字
        textViewName = (TextView) findViewById(R.id.id_TextViewName);//名字
        textViewName.setText(liveTvBean.getUsername());

        textViewGuanZhuPersonNum = (TextView) findViewById(R.id.id_TextViewGuanZhuPerson);//关注人数
        textViewGuanZhuPersonNum.setText("   " + liveTvBean.getFans());

        textViewPlayNum = (TextView) findViewById(R.id.id_TextViewPlayMuch);//播放数量
        textViewPlayNum.setText("   " + liveTvBean.getSeenum());

        textViewTitle = (TextView) findViewById(R.id.id_TextViewTitle);//播放标题
        textViewPlayDate = (TextView) findViewById(R.id.id_TextViewPlayDate);//播放日期
        textViewPopularity = (TextView) findViewById(R.id.id_TextViewPopularity);//播放人气

        gridView = (MyGridView) findViewById(R.id.id_GridViewTommrorrowPerson);
        View viewRelativeButton = findViewById(R.id.id_RelativeBottom);
        viewRelativeButton.setVisibility(View.GONE);
        Drawable drawable = viewRelativeButton.getBackground();
        drawable.setAlpha(100);
        viewRelativeButton.setBackground(drawable);

        commonAdapter = new CommonAdapter<LiveTvBean>(this, listLiveTvBean, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                if (listLiveTvBean.size() == 0) {
                    //holder.getConvertView().findViewById(R.id.llRoot).setVisibility(View.GONE); //gridview的item布局
                    ToastUtil.showShort("暂时没有数据");
                    return;
                } else if (listLiveTvBean.size() > 0) {
                    Utiles.log("------主播详情页---------" + listLiveTvBean.toString());
                    ImageView imageView = holder.getView(R.id.iv_livecover);
                    if(!TextUtils.isEmpty(liveTvBean.getCoverurl()))
                    Picasso.with(TommorrowPersonActivity.this).load(liveTvBean.getCoverurl())
                            .placeholder(R.drawable.default_details_image).placeholder(R.drawable.default_details_image)
                            .centerCrop().fit().into(imageView);
                    holder.setText(R.id.tv_livetitle, liveTvBean.getTitle());  //视频标题
                    holder.setText(R.id.tv_lable, "回放");
                }
            }
        };

        gridView.setAdapter(commonAdapter);
        gridView.setOnItemClickListener(this);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新label的设置
                pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeDolweStr);
                pullToRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                int time = getTime("onPullDownToRefresh");
                Utiles.log("PageFragment ：" + time);
                NetWorkData();
                getFollowData();
                page = 0;
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新label的设置
                pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeUpStr);
                pullToRefreshScrollView.getLoadingLayoutProxy().setPullLabel("上拉刷新");
                getTime("onPullUpToRefresh");
                pullToRefreshScrollView.onRefreshComplete();
            }
        });
    }

    /**
     * 分享
     */
    private void initShare() {
        shareDialog = new ShareDialog(this);

        shareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvShareSina:
                        ShareSDKUtils.shareSina("", "");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvShareQzone:
                        ShareSDKUtils.shareQZone("", "", "", "");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvSharewx:
                        ShareSDKUtils.shareWechat("", "", "", "");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvSharewxpy:
                        ShareSDKUtils.shareWechatMoments("【主播】" + liveTvBean.getTitle(), liveTvBean.getCoverurl(), "");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvShareMsg:
                        ShareSDKUtils.shareShortMessage("主播" + liveTvBean.getTitle() + liveTvBean.getUrl());
                        break;
                }
            }
        });

    }

    /**
     * 获取刷新时间
     */
    private int getTime(String RefreshTime) {
        long time = System.currentTimeMillis();
        Date d1 = new Date(time);
        if (formatTime == null) {
            formatTime = new SimpleDateFormat("HH:mm:ss");
            date = d1;
        }
        String timeStr = formatTime.format(d1);
        if (RefreshTime.equals("onPullUpToRefresh")) {
            timeUpStr = timeStr;
        } else {
            long startTime = date.getTime();
            int i = (int) ((time - startTime) / 1000);
            if (i > 30) {
                date = d1;
            }
            timeDolweStr = timeStr;
            return i;
        }
        return 0;
    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageBlack:
                this.finish();
                break;
            case R.id.id_ImageShare:   //分享
                shareDialog.show();
                break;
            case R.id.id_ImageLiveTv:    //第一个大图的视频的跳转
                if(listLiveTvBean.size()>0) {
                    Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class, listLiveTvBean.get(0));
                }
                break;
            case R.id.id_ButtonGuanZhuPerson:   //关注
                if (SharedUtil.isLogin(this) == true) {
                    if (follow.getText().toString().equals("已关注")) {
                        follow.setText("关注");
                        follow.setSelected(false);
                        liveTvBean.setIsattend(false);
                        cancelFollow(liveTvBean);
                    } else {
                        liveTvBean.setIsattend(true);
                        follow.setText("已关注");
                        follow.setSelected(true);
                        follow(liveTvBean);
                    }
                } else {
                    Utiles.skipNoData(LoginActivity.class);
                }

                break;
        }
    }

    /**
     * 关注
     */
    private void follow(final LiveTvBean bean) {
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().followPerson(networkAndLogin_ok.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("======>:02" + response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() == 0) {
                    Utiles.log("---------tommorrow" + bean.toString());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.loadFailed();
            }
        });
    }

    /**
     * 取消关注
     */
    private void cancelFollow(final LiveTvBean bean) {

        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().cancelFollowPerson(networkAndLogin_ok.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("  01:" + response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() == 0) {
                    Utiles.log("---------tommorrow" + bean.toString());
                } else {
                    Utiles.loadFailed();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                //ToastUtil.showShort("取消关注失败!") ;
            }
        });
    }

    /**
     * 恢复页面时的透明度的变化
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onResume() {
        super.onResume();
        if (CurrentY < 100) {
            selectBackColor(relativeLayout_toolbar, 0);
        } else if (CurrentY > 100 && CurrentY < 200) {
            selectBackColor(relativeLayout_toolbar, 100);
        } else if (CurrentY >= 200 && CurrentY < 300) {
            selectBackColor(relativeLayout_toolbar, 150);
        } else if (CurrentY > 300 && CurrentY < 400) {
            selectBackColor(relativeLayout_toolbar, 255);
        } else if (CurrentY > 400) {
            selectBackColor(relativeLayout_toolbar, 255);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void selectBackColor(View view, int alpha) {
        background = view.getBackground();
        background.setAlpha(alpha);
        view.setBackground(background);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        CurrentY = y;
        if (CurrentY < 100) {
            selectBackColor(relativeLayout_toolbar, 0);
        } else if (CurrentY > 100 && CurrentY < 200) {
            selectBackColor(relativeLayout_toolbar, 100);
        } else if (CurrentY >= 200 && CurrentY < 300) {
            selectBackColor(relativeLayout_toolbar, 150);
        } else if (CurrentY > 300 && CurrentY < 400) {
            selectBackColor(relativeLayout_toolbar, 255);
        } else if (CurrentY > 400) {
            selectBackColor(relativeLayout_toolbar, 255);
        }
    }

    /**
     * Item点击 跳转到视频播放页面
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        LogUtil.show("Tag", "--------主播详情页" + listLiveTvBean.get(position).toString());
        LiveTvBean liveTvBean = listLiveTvBean.get(position);
        Utiles.log("-----guanzhu--" + liveTvBean.toString());
        String status = liveTvBean.getStatus();  //播放状态
        if (status.equals("回放")) {
            Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class, liveTvBean);
        } else {
            Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class, liveTvBean);
        }
    }


    private class TommrrowHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<LiveTvBean> list = utileStringRequst.list;
                    if (list.size() != 0) {
                        page++;
                        listLiveTvBean.addAll(utileStringRequst.list);
                        commonAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    List<LiveTvBean> followList = utileStringRequst.list;
                    if (followList.size() != 0) {
                        page++;
                        listLiveTvBean.addAll(utileStringRequst.list);
                        commonAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            if (utileStringRequst.list != null) {
                utileStringRequst.list.clear();
            }
            tommrrowHandler.removeMessages(msg.what);
        }
    }

}


