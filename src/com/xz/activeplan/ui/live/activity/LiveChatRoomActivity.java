package com.xz.activeplan.ui.live.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.baofengclound.BFMediaPlayerControllerLive;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.db.VideoCollectDao;
import com.xz.activeplan.entity.LiveRed;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.MyVideoFansBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.live.impl.LiveInfoServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.rongclound.LiveKit;
import com.xz.activeplan.ui.live.rongclound.adapter.ChatListAdapter;
import com.xz.activeplan.ui.live.rongclound.adapter.ChatListView;
import com.xz.activeplan.ui.live.rongclound.widget.InputPanel;
import com.xz.activeplan.ui.live.view.ActionSheetDialog;
import com.xz.activeplan.ui.live.view.AnchorAlertDialog;
import com.xz.activeplan.ui.live.view.Video_AlertDialog;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.utils.LogUtil;
import com.xz.activeplan.utils.MD5;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Util;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.alipay.PayResult;
import com.xz.activeplan.utils.alipay.SignUtils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CircleImageView;
import com.xz.activeplan.views.ShareDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import bf.cloud.android.playutils.LivePlayer;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 新版融云直播聊天室
 */
public class LiveChatRoomActivity extends BaseFragmentActivity implements View.OnClickListener,
        Handler.Callback, ClassObserver, InputPanel.InputPanelListener {

    public static final int VOD_CHAT_ROOM_WHAT = 1002;
    private static final String TAG = "LiveChatRoomActivity";
    private static int count;
    private static PopupWindow mpopupWindow;
    public UserInfo info;
    public FragmentActivity activity =this;
    /**
     * 打赏中的控件
     */
    private Button shang_btn1, shang_btn2, shang_btn3;
    private EditText shang_et1;
    private LinearLayout llzhibubao;
    private LinearLayout llweixin;
    private MyVideoFansBean fansBean;
    private Handler handler = new Handler(this);
    private ChatListAdapter chatListAdapter;
    private ChatListView chatListView;
    private String roomId;
    private LiveTvBean liveTvBean;
    private IWXAPI msgApi = null;
    private ImageView mTabLine1, mTabLine2, mTabLine3;
    private LivePlayer mLivePlayer = null;
    
    private BFMediaPlayerControllerLive controllerLive = null;
    private TextView mGuest, mComment, mDetail;
    private String videoURL;    //视频播放地址
    private ShareDialog mShareDialog;
    private RelativeLayout rootView;
    private View face;
    private float MONEY;  //金额
    private View shangView = null;
    private int shangIndex;
    private String headURL;   //头像
    private CircleImageView headImageView;
    private AnchorAlertDialog alertDialog;
    private Video_AlertDialog video_alertDialog;
    private TextView tv_input;
    private VideoCollectDao collectDao;  //收藏
    private String seetype;  //观看类型

    private TextView tvPassWord;  //密码
    private ImageView ivCover;    //封面（只在密码和付费功能时使用）
    private InputPanel inputPanel;

    private EditText etDescribe;
    private String describe = "";//点赞时描述
    private boolean flag;
    private ImageView ivCoverBack;
    private ImageView ivNoComment;
    private  boolean isFlag= false;  //是否已经关注

    //表情
    private RelativeLayout ll_facechoose;
    private EditText comment_edit;
    private ImageView send;
    private ImageView ivEmoji;
    private ImageView vod_ivFace;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UrlsManager.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();

                    Utiles.log("支付结果信息：" + resultInfo + "===resultStatus===" + resultStatus);
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showShort("支付成功!");
                        ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.A_REWARD));

                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showShort("支付结果确认中!");
                        } else {
                            ToastUtil.showShort("支付失败!");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /******************************************* 微   信   支  付
     *********************************************************/
    private PayReq req;
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;
    private boolean login;
    private int total;
    /**
     * 定时获取在线人数
     */
    private Runnable mRunnable = new Runnable() {

        public void run() {
            getchatRoomNumber();  //获取聊天室人数
            Log.e(TAG, Thread.currentThread().getName() + " ");
            controllerLive.setNumber(total + "");

            //每10秒执行一次
            handler.postDelayed(mRunnable, 10000);
        }
    };
    private int userid;

    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "127.0.0.1";
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public static void hiddenpopupWindow() {
        if (null != mpopupWindow)
            mpopupWindow.dismiss();
    }

    @Override
    public void onStart() {
        //不建议在生命周期直接调用player。建议添加异步调用
        Utiles.log("周期   onStart");

        checkLogin();   //检查是否已经登录了
        super.onStart();
    }

    /**
     * 判断该视频是否已经购买了
     * 参数：
     * 1.当前userid
     * 2.liveid
     */
    private void checkVideoBuy() {
        OkHttpClientManager.getInstance();
        OkHttpClientManager.Param uid = new OkHttpClientManager.Param("userid", userid + "");
        OkHttpClientManager.Param lid = new OkHttpClientManager.Param("liveid", liveTvBean.getLiveid() + "");
        OkHttpClientManager.postAsyn(UrlsManager.URL_Already_Buy, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(String response) {
                Utiles.log("response-------" + response);
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0)  //已经购买了视频
                {
                    Utiles.log("已经购买了视频");
                    tvPassWord.setVisibility(View.GONE);
                    ivCover.setVisibility(View.GONE);
                    ivCoverBack.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLivePlayer.start();
                        }
                    }, 100);
                } else      //还未购买视频
                {
                    Utiles.log("还未购买视频");
                    Picasso.with(activity).load(liveTvBean.getCoverurl()).fit().centerCrop().into(ivCover);
                    ivCover.setVisibility(View.VISIBLE);
                    tvPassWord.setVisibility(View.VISIBLE);
                    ivCoverBack.setVisibility(View.VISIBLE);  //封面阴影
                    tvPassWord.setText("付费 ¥" + liveTvBean.getSeetype());
                }
            }
        }, uid, lid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.log("LiveChatRoomActivity--------onCreate");
        setContentView(R.layout.activity_live_chat_room);
        ClassConcrete.getInstance().addObserver(this);
        LiveKit.addEventHandler(handler);
        msgApi=  WXAPIFactory.createWXAPI(activity, null);
        getIntentData();  //获取intent的数据
        initView();       //初始化视图
        if(SharedUtil.isLogin(activity)==true)
        userid = SharedUtil.getUserInfo(activity).getUserid();  //当然登录的用户id
        //加入聊天室 roomId为视频的频道Id
        roomId = liveTvBean.getChannelid();
        Utiles.log("聊天室的id:   "+roomId);
        joinChatRoom(roomId);

        //通过Handler启动线程
        handler.post(mRunnable);
        //得到粉丝数量
        getFansCount();
    }
    /**
     * 获取传过来的数据
     * 1 data: liveTvBean  视频对象
     * 2. videotype:   视频类型  live(直播)   video(点播)
     */
    private void getIntentData() {
        liveTvBean = (LiveTvBean) getIntent().getSerializableExtra("data");
        videoURL = liveTvBean.getUrl();       //视频地址
        headURL = liveTvBean.getHeadurl();   //视频主播头像
        Utiles.log(TAG+"---headURL"+liveTvBean.toString());
        seetype  = liveTvBean.getSeetype();
    }
    /**
     * 初始化数据
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        //根布局（空白位置就隐藏键盘）
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        //  输入面板
        inputPanel = (InputPanel) findViewById(R.id.input_panel);
        inputPanel.setPanelListener(this);
        //消息的listView
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        chatListView.setSelection(chatListAdapter.getCount() - 1);
        //表情
        vod_ivFace = (ImageView) findViewById(R.id.iv_face);
        vod_ivFace.setOnClickListener(this);
        //未登录布局
        ivNoComment = (ImageView) findViewById(R.id.live_ivNoComment);
        ivNoComment.setOnClickListener(this);
        //输入密码
        tvPassWord = (TextView) findViewById(R.id.tvPassWord);
        tvPassWord.setOnClickListener(this);
        //封面
        ivCover = (ImageView) findViewById(R.id.ivCover);
        //封面阴影
        ivCoverBack = (ImageView) findViewById(R.id.ivCoverBack);
        Drawable drawable = ivCoverBack.getBackground();
        drawable.setAlpha(150);
        ivCoverBack.setBackground(drawable);
         //头像
        headImageView = (CircleImageView) findViewById(R.id.video_header);
        controllerLive = (BFMediaPlayerControllerLive) findViewById(R.id.media_controller_live_new);
        findViewById(R.id.video_ivBack).setOnClickListener(this);
                mDetail = (TextView) findViewById(R.id.video_tvDetail);
        //评论、嘉宾、详情
        mGuest = (TextView) findViewById(R.id.video_tvGuest);
        mComment = (TextView) findViewById(R.id.video_tvComment);
        mGuest.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mDetail.setOnClickListener(this);
        mComment.setTextColor(getResources().getColor(R.color.header_blue));
        initTabLine();
        findViewById(R.id.video_ivShang).setOnClickListener(this);
        findViewById(R.id.video_header).setOnClickListener(this);
        face = findViewById(R.id.live_face);   //底部输入框布局
        tv_input = (TextView) findViewById(R.id.tv_input);
        tv_input.setOnClickListener(this);

        //暴风播放器
        mLivePlayer = (LivePlayer) controllerLive.getPlayer();
        if (!TextUtils.isEmpty(videoURL)) {
            mLivePlayer.setDataSource(videoURL);
            //mLivePlayer.setVideoAspectRatio(BasePlayer.RATIO_TYPE.TYPE_FULL);
        }
        Utiles.log("视频地址：" + videoURL);
        controllerLive.setOnClickListener(this);
        /**
         * 详情页
         */
        TextView tvVideoTitle = (TextView) findViewById(R.id.live_tvVideoTitle);
        TextView tvVideoTime = (TextView) findViewById(R.id.live_tvVideoTime);
        TextView tvVideoPerson = (TextView) findViewById(R.id.live_tvVideoPerson);
        if (null != liveTvBean) {
            tvVideoTitle.setText(liveTvBean.getTitle());
            long startdate = liveTvBean.getStartdate();
            String time = TimeUtils.formatTime(startdate);
            tvVideoTime.setText(time);
            tvVideoPerson.setText(liveTvBean.getUsername());
        }

        //主播头像  导致OOM
        if (!TextUtils.isEmpty(headURL))
            Picasso.with(this).load(headURL).placeholder(R.drawable.default_head_bg).
                    error(R.drawable.default_head_bg).fit().centerCrop().into(headImageView);
    }

    /**
     * 获取聊天室的人数
     */
    private void getchatRoomNumber() {

        OkHttpClientManager.getAsyn(UrlsManager.URL_CHAT_ROOM_NUMBER + liveTvBean.getChannelid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log(TAG + "---fail--" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Utiles.log(TAG + "---response--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    JSONObject jsonObject1 = new JSONObject(result);
                    total = jsonObject1.getInt("total"); //人数
                    String id = null;
                    JSONArray users = jsonObject1.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject json2 = users.getJSONObject(i);
                        id = json2.getString("id"); //人员id
                    }
                    Utiles.log("总人数：" + total + ";userId：" + id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 加载tab线
     */
    private void initTabLine() {
        mTabLine1 = (ImageView) findViewById(R.id.video_iv_tabLine1);
        mTabLine2 = (ImageView) findViewById(R.id.video_iv_tabLine2);
        mTabLine3 = (ImageView) findViewById(R.id.video_iv_tabLine3);
        mTabLine1.setVisibility(View.VISIBLE);
    }

    /**
     * 检查是否登录
     */
    private void checkLogin() {
        if (seetype.equals("0"))   //公开
        {
            if (login == true)  //已经登录了
            {
                Utiles.log("周期   onStart  已经登录了");
              //  Utiles.setGone(ivNoComment);
               // Utiles.setVisibility(chatListView);
            } else   //还未登录
            {
               // Utiles.setVisibility(ivNoComment);
               // Utiles.setInVisible(chatListView);
            }
            LogUtil.show(TAG, "-----------公开");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLivePlayer.start();
                }
            }, 100);
        } else if (seetype.equals("*"))   //密码
        {
            LogUtil.show(TAG, "------------密码");
            Picasso.with(activity).load(liveTvBean.getCoverurl()).fit().centerCrop().into(ivCover);
            ivCover.setVisibility(View.VISIBLE);
            tvPassWord.setVisibility(View.VISIBLE);
            ivCoverBack.setVisibility(View.VISIBLE);  //封面阴影
            tvPassWord.setText("请输入密码");
        } else    //付费
        {
            /**判断该视频是否已经被用户购买过了**/
            checkVideoBuy();
            LogUtil.show(TAG, "------------付费");
        }

    }

    /**
     * 加入聊天室
     *
     * @param roomId 聊天室ID为视频的频道Id
     */
    private void joinChatRoom(String roomId) {
        LiveKit.joinChatRoom(roomId, 10, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Utiles.log("joinChatRoom成功");
                TextMessage textMessage = TextMessage.obtain("进来了");
                textMessage.setExtra("{\"type\" : 1}");
                LiveKit.sendMessage(textMessage);
                int msglistcount =chatListAdapter.getCount();
                Utiles.log("获取消息数："+msglistcount);
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Utiles.log("joinChatRoom失败:" + errorCode);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        Utiles.log("聊天室：  "+msg.what);
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {  //接收消息
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {   //发送消息
                MessageContent content = (MessageContent) msg.obj;
                if(content!=null) {
                    chatListAdapter.addMessage(content);
                }
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {  //错误消息
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    /**
     * 单击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivReport:   //举报
                Intent intent = new Intent(activity, ReportActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.video_ivBack:     //返回
                this.finish();
                break;
            case R.id.video_header:   //头像（发布者头像）
                showAnchorDialog();
                break;
            case R.id.vod_ivShare:    //分享
                mShareDialog.show();
                break;
            case R.id.video_ivShang:   //打赏
                alertShangDialog();
                break;
            case R.id.btn_send:     //发送
                sendTextMessage();
                break;
            case R.id.video_tvComment:  //评论
                mComment.setTextColor(getResources().getColor(R.color.header_blue));
                mDetail.setTextColor(getResources().getColor(R.color.black));
                mGuest.setTextColor(getResources().getColor(R.color.black));
                findViewById(R.id.video_rlComment).setVisibility(View.VISIBLE);
                findViewById(R.id.video_rlDetail).setVisibility(View.GONE);
                findViewById(R.id.video_rlGuest).setVisibility(View.GONE);
                mTabLine1.setVisibility(View.VISIBLE);
                mTabLine2.setVisibility(View.INVISIBLE);
                mTabLine3.setVisibility(View.INVISIBLE);
                break;
            case R.id.video_tvDetail:  //详情
                mDetail.setTextColor(getResources().getColor(R.color.header_blue));
                mComment.setTextColor(getResources().getColor(R.color.black));
                mGuest.setTextColor(getResources().getColor(R.color.black));
                findViewById(R.id.video_rlDetail).setVisibility(View.VISIBLE);
                findViewById(R.id.video_rlComment).setVisibility(View.GONE);
                findViewById(R.id.video_rlGuest).setVisibility(View.GONE);
                mTabLine2.setVisibility(View.VISIBLE);
                mTabLine1.setVisibility(View.INVISIBLE);
                mTabLine3.setVisibility(View.INVISIBLE);
                break;
            case R.id.video_tvGuest:  //嘉宾
                mGuest.setTextColor(getResources().getColor(R.color.header_blue));
                mComment.setTextColor(getResources().getColor(R.color.black));
                mDetail.setTextColor(getResources().getColor(R.color.black));
                findViewById(R.id.video_rlGuest).setVisibility(View.VISIBLE);
                findViewById(R.id.video_rlDetail).setVisibility(View.GONE);
                findViewById(R.id.video_rlComment).setVisibility(View.GONE);
                mTabLine3.setVisibility(View.VISIBLE);
                mTabLine2.setVisibility(View.INVISIBLE);
                mTabLine1.setVisibility(View.INVISIBLE);
                break;
            case R.id.alert_shang_bt1:  //打赏1
                changeViewColor(1, shang_btn1);
                break;
            case R.id.alert_shang_bt2://打赏2
                changeViewColor(2, shang_btn2);
                break;
            case R.id.alert_shang_bt3://打赏3
                changeViewColor(3, shang_btn3);
                break;
            case R.id.alert_shang_et1://打赏4
                changeViewColor(4, shang_et1);
                break;
            case R.id.alert_shang_llZhifubao:
                /*********************          支付宝支付      **********************/
                zhifubaoPay();
                break;
            case R.id.alert_shang_llWeixin:
                /*********************           微信支付      **********************
                 *  先请求app后台，根据返回结果在进行微信支付
                 * **********************/
                weixinPay();
                break;
            case R.id.tvPassWord:   //密码和付费两用

                if (seetype.equals("*")) //密码
                {
                    showPassWordDialog();

                } else                 //付费
                {
                    showMoneyDialog();
                }

                break;
            case R.id.live_ivNoComment:  //当前未登录状态（点击跳转到登录页面）
                Utiles.skipNoData(LoginActivity.class);
                break;

            case R.id.tv_input:   //弹出输入 文本框
                ReplyBox();
                popupInputMethodWindow();
                break;
            case R.id.btn_face:               //poppup上面的表情按钮
                //点击表情的时候切换viewPager表情布局
                Utiles.closeKeyBoard(activity, comment_edit);
                ll_facechoose.setVisibility(View.VISIBLE);
                if (!flag)  //说明第一次点击了的是face
                {
                    flag = true;
                    ivEmoji.setImageResource(R.drawable.inputview_icon_keyboard);

                } else {
                    flag = false;
                    ivEmoji.setImageResource(R.drawable.inputview_icon_emoji);
                    popupInputMethodWindow();
                    ll_facechoose.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_face:         //点击表情按钮
                ReplyBox();
                flag = true;
                ll_facechoose.setVisibility(View.VISIBLE);
                ivEmoji.setImageResource(R.drawable.inputview_icon_keyboard);
                break;

        }
    }

    /**
     *支付宝支付
     */
    private void zhifubaoPay() {
        String money = shang_et1.getText().toString();
        if (TextUtils.isEmpty(money) && MONEY == 0) {
            ToastUtil.showShort("请输入或选择金额");
            return;
        }
        if ("0".equals(money) || "000".equals(money) || "0.0".equals(money) || "00".equals(money)) {
            ToastUtil.showShort("金额不能为0");
            return;
        }
        if (money.length() > 0) {
            MONEY = Float.parseFloat(money);
        }
        Utiles.log("-----------chat-----money---" + MONEY);
        describe = etDescribe.getText().toString().trim();

        String body = "{'userid':21,'liveid:" + liveTvBean.getLiveid() + ",'describe':'" + describe + "','type':'红包'}";
        Utiles.log("vodChatRoom ---------" + body);
        //测试数据
        String orderInfo = getOrderInfo("直播支付", "{'userid':21,'liveid':195,'describe':'描述','type':'红包'}", 0.01 + "", getOutTradeNo());
        Utiles.log("支付宝价格：" + 0.01);
        alipayPay(orderInfo);
        video_alertDialog.cancel();
    }

    /**
     * 微信支付
     */
    private void weixinPay() {
        String money1 = shang_et1.getText().toString().trim(); //金额
        describe = etDescribe.getText().toString().trim();  //描述（可以为空）
        if (TextUtils.isEmpty(money1) && MONEY == 0) {
            ToastUtil.showShort("请输入或选择金额");
            return;
        }
        if ("0".equals(money1) || "000".equals(money1) || "0.0".equals(money1) || "00".equals(money1)) {
            ToastUtil.showShort("金额不能为0");
            return;
        }
        if (money1.length() > 0) {
            MONEY = Float.parseFloat(money1);
        }
        //传金额和描述
        DecimalFormat fnum = new DecimalFormat("0.00");
        String mon = fnum.format(MONEY);
        requestWeiXinPay(mon, describe);
    }

    /**
     * 付费视频
     * (ps: 在付款成功后需将ivCoverBack设置为隐藏 )
     */
    private void showMoneyDialog() {
        new ActionSheetDialog(activity)
                .builder()
                .setTitle("请选择付款方式")
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("支付宝", ActionSheetDialog.SheetItemColor.Blue
                        , new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //得到付费的金额
                                String string = tvPassWord.getText().toString();
                                String money = string.substring(string.indexOf("¥") + 1, string.length());
                                int userid = SharedUtil.getUserInfo(activity).getUserid();
                                int liveId = liveTvBean.getLiveid();
                                requestAliPay(userid, liveId, money, 1);
                            }
                        })
                .addSheetItem("微信", ActionSheetDialog.SheetItemColor.Blue
                        , new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //得到付费的金额
                                String string = tvPassWord.getText().toString();
                                String money = string.substring(string.indexOf("¥") + 1, string.length());
                                int userid = SharedUtil.getUserInfo(activity).getUserid();
                                int liveId = liveTvBean.getLiveid();
                                requestAliPay(userid, liveId, money, 2);
                            }
                        }).show();
    }

    /**
     * 购买视频（含 微信、支付宝支付方式）
     *
     * @param userid 购买人的Id
     * @param liveId 视频Id
     * @param money  金额
     * @param type   类型  1： 支付宝  2：微信
     */
    private void requestAliPay(int userid, int liveId, String money, final int type) {

        OkHttpClientManager.getInstance();
        OkHttpClientManager.Param uid = new OkHttpClientManager.Param("userid", userid + "");
        OkHttpClientManager.Param lid = new OkHttpClientManager.Param("liveid", liveId + "");
        OkHttpClientManager.Param payamount = new OkHttpClientManager.Param("payamount", money + "");
        OkHttpClientManager.postAsyn(UrlsManager.URL_BUY_VIDEO, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log("vodChatRoom-------购买视频失败" + e.getMessage().toString());
            }

            @Override
            public void onResponse(String response) {
                Utiles.log("vodChatRoom-------购买视频" + response);
                StatusBean bean = JSON.parseObject(response, StatusBean.class);
                if (bean.getCode() == 0)  //返回成功
                {
                    try {
                        JSONObject jsonObject = new JSONObject(bean.getData());
                        String orderNo = jsonObject.getString("orderno");  //订单编号
                        double money = jsonObject.getDouble("payamount");  //购买金额
                        if (type == 1) {   //支付宝

                            String orderInfo = getOrderInfo("购买视频", "购买视频", 0.01 + "", orderNo);
                            alipayPay(orderInfo);
                            Utiles.log("vodChatRoom-------进入支付宝支付");
                        } else            //微信
                        {
                            weixinPay("0.01", "CC" + orderNo, "购买视频");
                            Utiles.log("vodChatRoom-------进入微信支付");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            }
        }, uid, lid, payamount);
    }

    /**
     * 密码dialog
     */
    private void showPassWordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input_password, null);
        final com.xz.activeplan.ui.live.view.AlertDialog dialog = new
                com.xz.activeplan.ui.live.view.AlertDialog(activity, view, 0.65f).builder();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setTitle("请输入密码");
        view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
        final EditText et = (EditText) view.findViewById(R.id.etPwd);
        Utiles.getKeyBoard(et, "open");
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utiles.closeKeyBoard(activity, et);
                dialog.cancel();
            }
        });
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge(et);   //判读密码是否正确
                Utiles.closeKeyBoard(activity, et);
                dialog.cancel();
            }
        });

    }

    /**
     * 判读密码是否正确
     *
     * @param et 输入框
     */
    private void judge(EditText et) {
        String etContent = et.getText().toString();
        String url = UrlsManager.URL_VIDOE_PASSWORD + liveTvBean.getLiveid() + "&password=" + etContent;
        Utiles.log("--pwd---url-" + url);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log("--pwd---fail-" + e.getMessage().toString());
                ToastUtil.showCenterToast(activity, "密码错误~");
            }

            @Override
            public void onResponse(String response) {
                Utiles.log("--pwd---response-" + response);
                StatusBean status = JSON.parseObject(response, StatusBean.class);
                if (status.getCode() == 0) {
                    Utiles.log("--pwd---response-验证密码正确");
                    ToastUtil.showCenterToast(activity, "密码正确~");
                    tvPassWord.setVisibility(View.GONE);
                    ivCover.setVisibility(View.GONE);
                    ivCoverBack.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLivePlayer.start();
                        }
                    }, 100);
                } else {
                    ToastUtil.showCenterToast(activity, "密码错误~");
                }
            }
        });
    }
    /**
     * 点击改变试图的颜色
     */
    private void changeViewColor(int i, View view) {
        if (shangView == null) {
            shangView = view;
            shangIndex = i;
        } else if (shangView != view || view == shangView) {
            switch (shangIndex) {
                case 1:
                    shang_btn1 = (Button) shangView;
                    shang_btn1.setBackgroundResource(R.drawable.shape_round_edt);
                    MONEY = 0;
                    break;
                case 2:
                    shang_btn2 = (Button) shangView;
                    shang_btn2.setBackgroundResource(R.drawable.shape_round_edt);
                    MONEY = 0;
                    break;
                case 3:
                    shang_btn3 = (Button) shangView;
                    shang_btn3.setBackgroundResource(R.drawable.shape_round_edt);
                    MONEY = 0;
                    break;
                case 4:
                    shang_et1 = (EditText) shangView;
                    shang_et1.setBackgroundResource(R.drawable.shape_round_edt);
                    break;
            }
            if (view == shangView) {
                shangIndex = 0;
                shangView = null;
                return;
            }
        }
        switch (i) {   //选中状态
            case 1:
                shang_btn1 = (Button) view;
                shang_btn1.setBackgroundResource(R.drawable.shape_round_edt1);
                MONEY = Float.parseFloat(shang_btn1.getText().toString());
                if (shang_et1.getText().toString().length() > 0) {
                    shang_et1.setText("");
                }
                break;
            case 2:
                shang_btn2 = (Button) view;
                shang_btn2.setBackgroundResource(R.drawable.shape_round_edt1);
                MONEY = Float.parseFloat(shang_btn2.getText().toString());
                if (shang_et1.getText().toString().length() > 0) {
                    shang_et1.setText("");
                }
                break;
            case 3:
                shang_btn3 = (Button) view;
                shang_btn3.setBackgroundResource(R.drawable.shape_round_edt1);
                MONEY = Float.parseFloat(shang_btn3.getText().toString());
                if (shang_et1.getText().toString().length() > 0) {
                    shang_et1.setText("");
                }
                break;
            case 4:
                shang_et1 = (EditText) view;
                shang_et1.setBackgroundResource(R.drawable.shape_round_edt1);
                break;
        }
        shangIndex = i;
        shangView = view;
    }

    /**
     * 弹出打赏的Dialog
     */
    private void alertShangDialog() {

        View view = LayoutInflater.from(this).inflate(
                R.layout.video_alert_shang, null);
        ImageView close = (ImageView) view.findViewById(R.id.alert_close);
        shang_btn1 = (Button) view.findViewById(R.id.alert_shang_bt1);
        shang_btn2 = (Button) view.findViewById(R.id.alert_shang_bt2);
        shang_btn3 = (Button) view.findViewById(R.id.alert_shang_bt3);
        shang_et1 = (EditText) view.findViewById(R.id.alert_shang_et1);
        llzhibubao = (LinearLayout) view.findViewById(R.id.alert_shang_llZhifubao);
        llweixin = (LinearLayout) view.findViewById(R.id.alert_shang_llWeixin);
        etDescribe = (EditText) view.findViewById(R.id.etDescribe);  //描述
        shang_btn1.setOnClickListener(this);
        shang_btn2.setOnClickListener(this);
        shang_btn3.setOnClickListener(this);
        shang_et1.setOnClickListener(this);
        llzhibubao.setOnClickListener(this);
        llweixin.setOnClickListener(this);

        video_alertDialog = new Video_AlertDialog(this, view).builder();
        video_alertDialog.show();

        //关闭
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utiles.closeKeyBoard(activity, shang_et1);
                video_alertDialog.cancel();
            }
        });
    }

    /**
     * 弹出主播的资料
     *   （关注和回复 ）
     * */
    private void showAnchorDialog() {
        final UserInfoBean userInfo = SharedUtil.getUserInfo(activity);
        alertDialog = new AnchorAlertDialog(this, 0.75f).builder();
        alertDialog.show();
        if (fansBean != null) {
            Utiles.log("fansBean:"+fansBean.toString());
            alertDialog.setAnchorInfo(liveTvBean.getUsername() + "",fansBean.getLivecount(),
                    fansBean.getFanscount(),fansBean.getAttendcount(),headURL);

            Utiles.log("是否已经关注："+fansBean.getIsAttend());
            if(userInfo.getUserid()==liveTvBean.getUserid())   //当前账号和主播是一个人
            {
                alertDialog.setIsFollowStatus(false);
            }else
            {
                if(fansBean.getIsAttend()==false)
                    alertDialog.setIsFollowStatus(false);
                else
                    alertDialog.setIsFollowStatus(true);
            }
        }
        if (!TextUtils.isEmpty(headURL))
            Picasso.with(this).load(headURL).placeholder(R.drawable.default_head_bg).
                    error(R.drawable.default_head_bg).fit().centerCrop().into(headImageView);
        alertDialog.setReplyBotton("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前用户是否是该视频的发布者
                if (!TextUtils.isEmpty(userInfo.getUserid() + "") &&
                        !TextUtils.isEmpty(liveTvBean.getUserid() + "") && userInfo.getUserid() == liveTvBean.getUserid()) {
                    ToastUtil.showCenterToast(activity, "自己不能回复自己哦~");
                } else {
                    RongIM.getInstance().startPrivateChat(activity, liveTvBean.getUserid() + "", liveTvBean.getUsername() + "");
                    alertDialog.dismiss();
                }
            }
        }).setFollowBotton("", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userInfo.getUserid() == liveTvBean.getUserid()) {
                            ToastUtil.showCenterToast(activity, "不能关注自己哦~");
                            return;
                        }
                        if (!isFlag) {
                            isFlag = true;
                            alertDialog.setIsFollowStatus(true);   //关注
                            fansBean.setAttend(true);
                            ToastUtil.show("关注成功", 0);
                        } else
                        {
                            isFlag = false;
                            alertDialog.setIsFollowStatus(false);   //已经关注了
                            fansBean.setAttend(false);
                            ToastUtil.show("取消关注成功", 1);
                            ToastUtil.show("关注成功", 0);
                        }
                    }
                });

    }

    /**
     * 点击发布者头像获取该发布者的粉丝数量
     */
    private void getFansCount() {
        if (null != liveTvBean && liveTvBean.getUserid() > 0) {
            LiveInfoServiceImpl.getInstance().getCheckHeader(liveTvBean.getUserid(),userid,new MyStringCallBack());
        }
    }

    public void ReplyBox() {
    }

    //这里给大家一个弹出和隐藏软键盘的代码，非常实用。主要是要保持mpopupWindow和键盘同时出现和消失。
    private void popupInputMethodWindow() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 0);
    }

    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage() {
        String contString = comment_edit.getText().toString();
        if (contString.length() > 0) {
            TextMessage textMessage = TextMessage.obtain(contString);
            LiveKit.sendMessage(textMessage);
            comment_edit.setText("");
            ll_facechoose.setVisibility(View.GONE);  //emoji隐藏
            ivEmoji.setImageResource(R.drawable.inputview_icon_emoji);//设置emoji为初始值
            hideInput(activity, comment_edit);
        }
    }
    /**
     * 横竖屏切换
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏

            findViewById(R.id.video_ivBack).setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            findViewById(R.id.video_ivBack).setVisibility(View.VISIBLE);

        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void alipayPay(String orderInfo) {
        if (TextUtils.isEmpty(UrlsManager.PARTNER) || TextUtils.isEmpty(UrlsManager.RSA_PRIVATE) || TextUtils.isEmpty(UrlsManager.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //  .finish();
                        }
                    }).show();
            return;
        }
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
            Utiles.log("签名：" + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Utiles.log("签名：" + sign);

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(LiveChatRoomActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                android.os.Message msg = new android.os.Message();
                msg.what = UrlsManager.SDK_PAY_FLAG;
                msg.obj = result;
              //  mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price, String outTradeNo) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + UrlsManager.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + UrlsManager.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://yueba.cc/tryst/pay/alipay/notify_url.jsp" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, UrlsManager.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * 请求后台微信支付的接口（打赏）
     *
     * @param MONEY    金额
     * @param describe 描述
     */
    private void requestWeiXinPay(String MONEY, String describe) {
        OkHttpClientManager.getInstance();
        int currentUserId = SharedUtil.getUserInfo(this).getUserid();
        OkHttpClientManager.Param userid = new OkHttpClientManager.Param("userid", currentUserId + "");
        final OkHttpClientManager.Param liveid = new OkHttpClientManager.Param("liveid", liveTvBean.getLiveid() + "");
        OkHttpClientManager.Param payamount = new OkHttpClientManager.Param("payamount", MONEY + "");
        OkHttpClientManager.Param describe1 = new OkHttpClientManager.Param("describe1", describe + "");
        OkHttpClientManager.postAsyn(UrlsManager.URL_REWARD_WEIXIN_PAY, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log("requestWeiXinPay--------onFailure  " + e.getMessage().toString());
            }

            @Override
            public void onResponse(String response) {
                Utiles.log("requestWeiXinPay--------" + response);
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0)  //返回成功
                {
                    LiveRed liveRed = JSON.parseObject(statusBean.getData(), LiveRed.class);
                    //进行微信支付
                    weixinPay(liveRed.getPayamount() + "", "BB" + liveRed.getNumber(), "打赏主播");
                    video_alertDialog.cancel();
                }
            }
        }, userid, liveid, payamount, describe1);
    }

    /**
     * 1、判断微信支付的回调
     * 2、判断登录是否成功回调
     */
    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.WEIXIN_PAY_SUCCESS) {
            ToastUtil.showCenterToast(activity, "微信支付成功");
            Utiles.log("vodChatRoom----------支付成功");
        } else if (event.getType() == EventType.WEIXIN_PAY_ERROR) {
            ToastUtil.showCenterToast(activity, "微信支付失败");
            Utiles.log("vodChatRoom----------支付失败");
        } else if (event.getType() == EventType.LOGIN_NOTIFI_TYPE) {
            Utiles.log("vodChatRoom----------登录成功");
            Utiles.setVisibility(chatListView);
            Utiles.setGone(ivNoComment);
            login = true;

        }
        return false;
    }

    @Override
    public void onPause() {
        Utiles.log("周期   onPause");
       // mHistory = mLivePlayer.getCurrentPosition();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mLivePlayer.stop();
            }
        });
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utiles.log("周期   onResume");
    }


    @Override
    protected void onDestroy() {


        Utiles.log("退出聊天室："+fansBean.getIsAttend());

        /**
         * 关注和取消关注
         *
         */
        Utiles.log("关注状态："+fansBean.getIsAttend());
        if(fansBean.getIsAttend()==false)  //已经关注了
        {
            cancelFollow(liveTvBean);
        }else
        {
            follow(liveTvBean);
        }
        LiveKit.removeEventHandler(handler);
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        handler.removeCallbacks(mRunnable);
        ClassConcrete.getInstance().removeObserver(this);
        mLivePlayer.release();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (video_alertDialog != null) {
            video_alertDialog.dismiss();
        }
        try {
            controllerLive.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        super.onDestroy();
    }

    /**
     * 新版发送消息。。。
     * @param text
     */
    @Override
    public void onSendClick(String text) {
        final TextMessage content = TextMessage.obtain(text);
        LiveKit.sendMessage(content);
        //隐藏软键盘
        hideInput(this,inputPanel.textEditor);
        inputPanel.emojiBoard.setVisibility(View.GONE);
        inputPanel.emojiBtn.setImageResource(R.drawable.inputview_icon_emoji);
    }

    /**
     * back键或者空白区域点击事件处理
     *
     * @return 已处理true, 否则false
     */
    public boolean onBackAction() {
       if (inputPanel.onBackAction()) {
            return true;
        }
        return false;
    }

    /**
     * 关注
     */
    private void follow(LiveTvBean bean) {
        if (!NetworkInfoUtil.checkNetwork(XZApplication.getInstance())) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return;
        }
        UserInfoServiceImpl.getInstance().followPerson(userid, bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("关注： "+response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() == 0) {

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
    private void cancelFollow(LiveTvBean bean) {
        if (!NetworkInfoUtil.checkNetwork(XZApplication.getInstance())) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return;
        }
        UserInfoServiceImpl.getInstance().cancelFollowPerson(userid, bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("取消关注： "+response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() != 0) {
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
     * ****************************                       ******************************
     * ****************************      微信支付         ******************************
     * ****************************                       ******************************
     * **********************************************************************************
     * 1.金额
     * 2.订单号
     * 3.支付标签(直播支付，购买视频)
     */
    private void weixinPay(String price, String orderNumber, String payType) {
        req = new PayReq();
        sb = new StringBuffer();

        msgApi.registerApp(UrlsManager.APP_ID);

        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();

        //微信价格单位是分 所以要*100
        float weixinprice = Float.parseFloat(price);
        Utiles.log("微信支付价格：" + weixinprice);
        getPrepayId.execute(payType, ((int) (weixinprice * 100)) + "", orderNumber);

    }

    /**
     * 生成签名  step1
     */

    private String genPackageSign(List<NameValuePair> params) {

        Utiles.log("genPackageSign");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(UrlsManager.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion-packageSign：", packageSign);
        return packageSign;
    }

    //step5
    private String genAppSign(List<NameValuePair> params) {
        Utiles.log("genAppSign");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(UrlsManager.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion-appSign：", appSign);
        return appSign;
    }

    //step2
    private String toXml(List<NameValuePair> params) {
        Utiles.log("toXml");
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion-sb.toString()：", sb.toString());
        return sb.toString();
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion-e.toString()：", e.toString());
        }
        return null;

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取预支付订单号:
     *
     * @param body        商品描述
     * @param price       商品价格
     *                    要根据字典排序
     * @param orderNumber 商户订单号
     */
    private String genProductArgs(String body, String price, String orderNumber) {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            //应用ID
            packageParams.add(new BasicNameValuePair("appid", UrlsManager.APP_ID));
            //商品描述
            packageParams.add(new BasicNameValuePair("body", body));
            //商品详情
            packageParams.add(new BasicNameValuePair("detail", body));
            //商户号
            packageParams.add(new BasicNameValuePair("mch_id", UrlsManager.MCH_ID));
            //随机字符串
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            //通知地址
            packageParams.add(new BasicNameValuePair("notify_url", "http://yueba.cc/tryst/weixnotify"));
            //商户订单号
            packageParams.add(new BasicNameValuePair("out_trade_no", orderNumber));
            //终端IP
            packageParams.add(new BasicNameValuePair("spbill_create_ip", getPhoneIp()));
            //总金额
            packageParams.add(new BasicNameValuePair("total_fee", price));
            //交易类型
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            //签名
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);
            //解决body传中文报签名错误的问题，生成的xml请求参数转为字节数组后，用“ISO8859-1”编码格式进行编码为字符
            return new String(xmlstring.getBytes(), "ISO8859-1");

        } catch (Exception e) {
            Log.e("SureOrderActivity", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

//    private String genOutTradNo() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }

    /***
     * 获取二次签名sign  step6
     */
    private void genPayReq() {

        req.appId = UrlsManager.APP_ID;
        req.partnerId = UrlsManager.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        Log.e("orion-signParams：", signParams.toString());
    }

    /***
     * 起调微信支付
     */
    private void sendPayReq() {
        Utiles.log("起调微信支付sendPayReq");
        msgApi.registerApp(UrlsManager.APP_ID);
        msgApi.sendReq(req);
    }

    /**
     * 得到粉丝数量的回调
     */
    class MyStringCallBack implements OkHttpClientManager.StringCallback {

        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String response) {
            Utiles.log("-----------fans-----------" + response);
            try {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0)   //返回状态
                {
                    fansBean = JSON.parseObject(statusBean.getData(), MyVideoFansBean.class);
                }
            } catch (Exception e) {
                Utiles.log("----未知错误-----");
            }
        }
    }

    /***
     * 获取预支付订单
     */
    private class GetPrepayIdTask extends AsyncTask<String, Void, Map<String, String>> {

        private android.app.ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = android.app.ProgressDialog.show(
                    activity, getString(R.string.app_tip), getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            resultunifiedorder = result;
            if (resultunifiedorder != null) {
                genPayReq();
                sendPayReq();
            }
            Utiles.log("vodChatRoom--------微信支付--" + result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs(params[0], params[1], params[2]);

            Log.e("orion-entity：", entity);  //step3

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion-content：", content); //step4
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }
}
