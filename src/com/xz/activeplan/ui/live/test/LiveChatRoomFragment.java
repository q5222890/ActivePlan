package com.xz.activeplan.ui.live.test;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.baofengclound.BFMediaPlayerControllerLive;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveRed;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.MyVideoFansBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment1;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.activity.ReportActivity;
import com.xz.activeplan.ui.live.adapter.ChatMsgEntity;
import com.xz.activeplan.ui.live.test.message.GiftMessage;
import com.xz.activeplan.ui.live.view.ActionSheetDialog;
import com.xz.activeplan.ui.live.view.AnchorAlertDialog;
import com.xz.activeplan.ui.live.view.Video_AlertDialog;
import com.xz.activeplan.utils.LogUtil;
import com.xz.activeplan.utils.MD5;
import com.xz.activeplan.utils.ShareSDKUtils;
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

import bf.cloud.android.mxlbarrage.MxlBarrage;
import bf.cloud.android.playutils.LivePlayer;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 直播聊天室
 * 1.使用低延时
 * 2.BFMediaPlayerControllerLive
 */

public class LiveChatRoomFragment extends BaseFragment1 implements RongIMClient.OnReceiveMessageListener, Handler.Callback, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener, ClassObserver {

    public static final int LIVE_CHAT_ROOM_WHAT = 1001;

    /******************************
     * 支 付 宝 支 付
     *****************************************/
    // 商户PID
    public static final String PARTNER = "2088222576426703";
    // 商户收款账号
    public static final String SELLER = "admin@uxingzhe.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDmAFaAOsBgKcNhAZWFn1DnI7BPhYy39y5uHm0s/43Zbjpnevs8PWUhkWqzAVscdAe3MS/Ytb+AsW6KKMp1wU3yqLpZ+8GHovlRUWEt7frSDkXedpZ9SO1bkMzLAtXQbwGjGKiUtt2rb7cOv3dle+j2m0SOn7rYcZkj0loUXb8FUwIDAQABAoGACjAjpMR1dw0CiY5/aA7Zj5OGIM+inxeE5/3TCjY7u1Dlp2XMMSvrCeSeHFXICmedW+EC5I+QbwvtAJOz/ClW4X5WfxC4+sqDvSpFH7FEz2/52LWNEhSnq/CgltnmxzBqQPJeJTsKa/RXt1h0cO7khuCDlW66bkNS30oD9wbyyBECQQDz40VZG2BvgZZLuBfzMj3DrKg35Sv+wuveDTE3kmXgsRf/UL96fwxP376hbaX2jNsRxS5HFbGPaYG3IfZDRNsLAkEA8WyEnEu39OXmwsqC2mAbO5UBvW7ybV+PJ167WVyKdjN/qxqrETCW8alO3h9+2ichAD3cIUb4sko2AHoTcNOr2QJBAOmkkIsM7xXjz1g6xLb3KrSKc50Yr00g71WKzduvJGpdPeAaO1Xe4KykbLu5j4Ti6/vaKrtuzOvW563Jm2JWVIECQG6WSthdRtKDTQUFRpNJWrAcPUPMwnefQi/CkQcKANLMHDsAaEPjggEIkPvmaXD8Y3182IzVua3RmJL3WFhiXWECQQDEGa72r0R9Lv8+xZtEzeUw5jiuhu3gpK02mxizaq9aAaYHEZsDGeqH5lIGd3rhUkhMib448simR2otNxQfMHyJ";
    //public static final String RSA_PRIVATE="MIICXAIBAAKBgQDEBByoL8KInIPHKJx4EkcytBb9aYmgGExwY2vIkVBAG3fMN7mOD441aau4iUGn7BSMa8BOzGFv0juHufgjMRm4KRpw/OC+5L7wwnuV9pp7RdyryPF5gFyV8OOODqbRFKc57ISGJApqFW+E6N9eW1ZrLctWnXS7DHF1SUUUmkLzGQIDAQABAoGAQ/pJFeq6RDZDXSYwXVO5bVQXRPr8x5ClsbRJBN2gPxtHiIMqzqGGZ++lpfGz21B8L9aOcsVeZTE7g+5yp4G3sRKVF6tsBKMIGPfqPgq3cJvtJ24wdqcfTkyAv+lAwVS97IanFSV310uVEZfU1w6jK0aD43ZbIh+YPSXHebTAPAECQQDvuCMew0rSpNiXOXoS1P71qtErelVWoLuzojD2Clhb9VToD8e7z/vAOe3ZgGIomW9i5ktFYR7P03hiBoBOk9IxAkEA0VQhtVgdIEktKgz3szshcgLHeWxGDHio/y5NCL6YBHws33h8Tk+Nj+lMqjI2o8zSO7iCFSQvWq/dv3RKRLlNaQJAS+UjkWDJNqmkPTSgVogC9KqRMM+5ZwTaiIPoNm2ODgc9S397gBXYIcL1b/A6o5n4bvp0iFG4XJoaOgbO5pHccQJAeNPsRSXgYhN7K2uiyuUV0B6Ej7UM1gQL4IgUVTjWvLgEmtWMQxOubeRHqXIkkfYXX/uHsrZZUypygCGbAQJJCQJBAL6MbFnzSNqO22bp5ajUkZEKlm8YxrKLW7QvOtIwQs8uyBt3hgiY5vp9lLJce5nhMfb0xLoZfgEa7rnc59Sx35w=" ;
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final String TAG = "LiveChatRoomFragment";
    private static final int SDK_PAY_FLAG = 1;
    private static final int REFRESH_NUMBER= 2;  //定时刷新
    private static LiveTvBean liveTvBean;
    public static Handler LIVE_CHAT_ROOM_HANDLER = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == LIVE_CHAT_ROOM_WHAT) {
                liveTvBean = (LiveTvBean) msg.obj;
                Utiles.log("livechat-----------" + liveTvBean.toString());
            }
        }
    };
    private static MyVideoFansBean fansBean;
    private static PopupWindow mpopupWindow;
    public UserInfo info;
    /**
     * 打赏中的控件
     */
    Button shang_btn1, shang_btn2, shang_btn3;
    EditText shang_et1;
    LinearLayout llzhibubao;
    LinearLayout llweixin;
    private int total;   //在线人数
    private ListView listView;
    private LiveChatListAdapter liveChatListAdapter;
    private Conversation.ConversationType conversationType;
    private String targetId;
    private ImageView mTabLine1, mTabLine2, mTabLine3;
    private View root;
    private TextView mGuest, mComment, mDetail;
    private String videoURL;    //视频播放地址
    private ShareDialog mShareDialog;
    private RelativeLayout rootView;
    private TextView tv_input;
    private int heightDifference;
    private float MONEY;  //金额
    private View shangView = null;
    private int shangIndex;
    private String headURL;   //头像
    private CircleImageView headImageView;
    private AnchorAlertDialog alertDialog;
    private Video_AlertDialog video_alertDialog;
    private LivePlayer mLivePlayer = null;
    private BFMediaPlayerControllerLive controllerLive = null;
    private MxlBarrage mBarrage = null;
    /**
     * 详情页面的控件
     */

    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((String) msg.obj);
            /**
             同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
             * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
             * docType=1) 建议商户依赖异步通知
             **/
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                String resultStatus = payResult.getResultStatus();

                Utiles.log("支付结果信息：" + resultInfo + "===resultStatus===" + resultStatus);
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
//					Toast.makeText(SureOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    ToastUtil.showShort("支付成功!");
                    ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.ACCOUNT_BAOMING_TYPE));

                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
//						Toast.makeText(SureOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        ToastUtil.showShort("支付结果确认中!");
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						Toast.makeText(SureOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        ToastUtil.showShort("支付失败!");
                    }
                }
            }
           /* if(msg.what == REFRESH_NUMBER)
            {
                controllerLive.setNumber(total+"");
            }*/

        }
    };
    private FragmentActivity activity;
    private TextView tvNumber;
    private String seetype;
    private TextView tvPassWord;
    private ImageView ivCover;
    private ImageView ivCoverBack;
    private EditText etDescribe;
    private String describe;
    //表情
    private RelativeLayout ll_facechoose;
    private EditText comment_edit;
    private ImageView send;
    private ImageView ivEmoji;
    private ImageView vod_ivFace;
    private boolean flag;

    /******************************************
     * 微   信   支  付
     *********************************************************/
    private PayReq req;
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;
    private IWXAPI msgApi;
    private boolean mRunning =false;
    /**
     * 定时获取在线人数
     */
    private Runnable mRunnable = new Runnable() {

        public void run() {
            chatRoomNumber();
            Log.e(TAG, Thread.currentThread().getName() + " ");
            controllerLive.setNumber(total + "");

            //每10秒执行一次
            mHandler.postDelayed(mRunnable, 10000);
        }
    };

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
        if (mpopupWindow != null) {
            mpopupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        MyRongIM.getInstance().logout();
    }

    @Override
    public void onStart() {

        if (seetype.equals("0"))   //公开
        {
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

            //  controllerLive.setVisibility(View.INVISIBLE);  //直播(ps:加入表情后不能隐藏)
            // controllerLive.setFocusable(false);
            ivCoverBack.setVisibility(View.VISIBLE);  //封面阴影
            controllerLive.setClickable(false);
            tvPassWord.setText("请输入密码");
        } else    //付费
        {
            /**判断该视频是否已经被用户购买过了**/
            checkVideoBuy();
            LogUtil.show(TAG, "------------付费");
        }
        super.onStart();
    }

    /**
     * 判断该视频是否已经购买了
     * 参数：
     * 1.当前userid
     * 2.liveid
     */
    private void checkVideoBuy() {
        int userid = SharedUtil.getUserInfo(activity).getUserid();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FragmentActivity) getActivity();
        MyRongIM.getInstance().getEventBus().register(this);
        msgApi = WXAPIFactory.createWXAPI(activity, null);
        Uri uri = getActivity().getIntent().getData();
        String typeStr = uri.getLastPathSegment().toUpperCase();
        conversationType = Conversation.ConversationType.valueOf(typeStr);
        targetId = uri.getQueryParameter("targetId");
        Utiles.log(TAG + "infoText   targetId=" + targetId + "-----onCreate");
        //得到视频播放地址videoURL
        if (null != liveTvBean) {
            videoURL = liveTvBean.getUrl();
            headURL = liveTvBean.getHeadurl();
            seetype = liveTvBean.getSeetype();    //视频类型
            Utiles.log(" ------------------liveChatRoom" + liveTvBean.toString());
        }
        getFansCount();  //点击主播头像获取信息
        //通过Handler启动线程
        mHandler.post(mRunnable);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.rc_fragment_live_chatroom, container, false);
        //根布局
        rootView = (RelativeLayout) root.findViewById(R.id.live_rootView);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        initViews(root);

        joinChatRoom();  //连接聊天室
        //判断隐藏软键盘是否弹出
        if (getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            //隐藏软键盘
            getActivity().getWindow().setSoftInputMode
                    (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return root;
    }

    /**
     * 加入聊天室
     */
    private void joinChatRoom() {
        RongIMClient.getInstance().joinChatRoom(targetId, 0, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Utiles.log("------liveChatRoom" + activity);
                UserInfoBean userInfo = SharedUtil.getUserInfo(activity);
                info = new UserInfo(String.valueOf(userInfo.getUserid()), userInfo.getUsername(), Uri.parse(userInfo.getHeadurl()));
                MyRongIM.getInstance().setUserInfoProvider(new MyRongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String userId) {
                        return info;
                    }
                });
                RongIM.getInstance().refreshUserInfoCache(info);
                TextMessage textMessage = TextMessage.obtain("进来了");
                textMessage.setExtra("{\"type\":1}");
                Message msg = Message.obtain(targetId, conversationType, textMessage);
                msg.setMessageDirection(Message.MessageDirection.SEND);
                textMessage.setUserInfo(info);
                MyRongIM.getInstance().sendMessage(msg);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 初始化试图
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initViews(View root) {
        //表情
        vod_ivFace = (ImageView) root.findViewById(R.id.iv_face);
        vod_ivFace.setOnClickListener(this);
        //输入密码
        tvPassWord = (TextView) root.findViewById(R.id.tvPassWord);
        tvPassWord.setOnClickListener(this);
        //封面
        ivCover = (ImageView) root.findViewById(R.id.ivCover);
        //封面阴影
        ivCoverBack = (ImageView) root.findViewById(R.id.ivCoverBack);
        Drawable drawable = ivCoverBack.getBackground();
        drawable.setAlpha(150);
        ivCoverBack.setBackground(drawable);

        headImageView = (CircleImageView) root.findViewById(R.id.video_header);
        if (!TextUtils.isEmpty(headURL))
            Picasso.with(getActivity()).load(headURL).placeholder(R.drawable.default_head_bg).
                    error(R.drawable.default_head_bg).fit().centerCrop().into(headImageView);
        listView = (ListView) root.findViewById(R.id.live_list);
        //直播
        controllerLive = (BFMediaPlayerControllerLive) root.findViewById(R.id.media_controller_live);
        root.findViewById(R.id.video_ivBack).setOnClickListener(this);
        //评论、嘉宾、详情
        mGuest = (TextView) root.findViewById(R.id.video_tvGuest);
        mComment = (TextView) root.findViewById(R.id.video_tvComment);
        mDetail = (TextView) root.findViewById(R.id.video_tvDetail);
        //返回键
        mGuest.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mDetail.setOnClickListener(this);
        mComment.setTextColor(getResources().getColor(R.color.header_blue));
        initTabLine();

        root.findViewById(R.id.video_ivShang).setOnClickListener(this);
        root.findViewById(R.id.video_header).setOnClickListener(this);
        //tvNumber = (TextView)root.findViewById(R.id.start_live_tvNumber);
        tv_input = (TextView) root.findViewById(R.id.tv_input);
        tv_input.setOnClickListener(this);
        //分享
        initShare();
        //暴风播放器
        mLivePlayer = (LivePlayer) controllerLive.getPlayer();
        if (!TextUtils.isEmpty(videoURL)) {
            mLivePlayer.setDataSource(videoURL);
        }
        if (liveTvBean != null) {
            mBarrage = new MxlBarrage(liveTvBean.getGcid());
        }
        mBarrage.registBarrageListener(new MxlBarrage.BarrageListener() {

            @Override
            public void onMessage(String channelId, String msg) {
                if (controllerLive != null)
                    controllerLive.addDanmaku(false, msg);
            }
        });
        Utiles.log("视频地址：" + videoURL);
        controllerLive.setOnClickListener(this);
        mLivePlayer.setLowLatency(true);    //使用低延时参数
        //聊天列表适配器
        liveChatListAdapter = new LiveChatListAdapter();
        listView.setAdapter(liveChatListAdapter);
        //定位到最后一条数据
        listView.setSelection(liveChatListAdapter.getCount() - 1);
        /**
         * 详情页
         */
        TextView tvVideoTitle = (TextView) root.findViewById(R.id.live_tvVideoTitle);
        TextView tvVideoTime = (TextView) root.findViewById(R.id.live_tvVideoTime);
        TextView tvVideoPerson = (TextView) root.findViewById(R.id.live_tvVideoPerson);
        if (null != liveTvBean) {
            tvVideoTitle.setText(liveTvBean.getTitle());
            long startdate = liveTvBean.getStartdate();
            String time = TimeUtils.formatTime(startdate);
            tvVideoTime.setText(time);
            tvVideoPerson.setText(liveTvBean.getUsername());
        }
    }

    /**
     * 加载tab线
     */
    private void initTabLine() {
        mTabLine1 = (ImageView) root.findViewById(R.id.video_iv_tabLine1);
        mTabLine2 = (ImageView) root.findViewById(R.id.video_iv_tabLine2);
        mTabLine3 = (ImageView) root.findViewById(R.id.video_iv_tabLine3);
        mTabLine1.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        if (mBarrage != null && mBarrage.getIsWorking())
            mBarrage.stop();
        mLivePlayer.stop();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mBarrage != null && !mBarrage.getIsWorking())
            mBarrage.start();
        mRunning =true;
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utiles.log("--------chatroom---------onDestroyView");
    }

    @Override
    public void onDestroy() {
        mLivePlayer.stop();
        mLivePlayer.release();
        ClassConcrete.getInstance().removeObserver(this);
        try {
            controllerLive.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (video_alertDialog != null) {
            video_alertDialog.dismiss();
        }
        mHandler.removeCallbacks(mRunnable);
        Utiles.log("--------chatRoom---------onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onReceived(Message message, int left) {
        if (message.getConversationType() == conversationType && message.getTargetId().equals(targetId)) {
            liveChatListAdapter.addMessage(message);
            liveChatListAdapter.notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        String type = Integer.toString(msg.what);
        Message message = Message.obtain(targetId, conversationType, new GiftMessage(type));
        message.getContent().setUserInfo(MyRongIM.getInstance().getCurrentUserInfo());
        MyRongIM.getInstance().sendMessage(message);
        return false;
    }

    /**
     * 接收
     */
    public void onEventMainThread(BusEvent.MessageReceived event) {
        Message msg = event.message;
        if (targetId.equals(msg.getTargetId()) && conversationType == Conversation.ConversationType.CHATROOM) {
            liveChatListAdapter.addMessage(msg);
            liveChatListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 发送
     */
    public void onEventMainThread(BusEvent.MessageSent event) {
        Message msg = event.message;
        if (targetId.equals(msg.getTargetId()) && conversationType == Conversation.ConversationType.CHATROOM) {
            int errorCode = event.code;
            if (errorCode == 0) {
                liveChatListAdapter.addMessage(msg);
                liveChatListAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.showCenterToast(getActivity(), "发送失败");
            }
        }
    }

    @Override
    public void onStop() {
        mRunning = false;
        super.onStop();

    }

    /**
     * 单击 事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivReport: //举报视频
                Intent intent = new Intent(activity, ReportActivity.class);
                intent.putExtra("liveid",liveTvBean.getLiveid());
                activity.startActivity(intent);

                break;
            case R.id.video_ivBack:     //返回
                controllerLive.backToPortrait();
                break;
            case R.id.video_header:   //头像（发布者头像）
                alertDialog();
                break;
            case R.id.vod_ivShare:    //分享
                mShareDialog.show();
                break;
            case R.id.video_ivShang:   //打赏
                alertShangDialog();
                break;
            case R.id.video_tvComment:  //评论
                mComment.setTextColor(getResources().getColor(R.color.header_blue));
                mDetail.setTextColor(getResources().getColor(R.color.black));
                mGuest.setTextColor(getResources().getColor(R.color.black));
                root.findViewById(R.id.video_rlComment).setVisibility(View.VISIBLE);
                root.findViewById(R.id.video_rlDetail).setVisibility(View.GONE);
                root.findViewById(R.id.video_rlGuest).setVisibility(View.GONE);
                mTabLine1.setVisibility(View.VISIBLE);
                mTabLine2.setVisibility(View.INVISIBLE);
                mTabLine3.setVisibility(View.INVISIBLE);
                break;
            case R.id.video_tvDetail:  //详情
                mDetail.setTextColor(getResources().getColor(R.color.header_blue));
                mComment.setTextColor(getResources().getColor(R.color.black));
                mGuest.setTextColor(getResources().getColor(R.color.black));
                root.findViewById(R.id.video_rlDetail).setVisibility(View.VISIBLE);
                root.findViewById(R.id.video_rlComment).setVisibility(View.GONE);
                root.findViewById(R.id.video_rlGuest).setVisibility(View.GONE);
                mTabLine2.setVisibility(View.VISIBLE);
                mTabLine1.setVisibility(View.INVISIBLE);
                mTabLine3.setVisibility(View.INVISIBLE);
                break;
            case R.id.video_tvGuest:  //嘉宾
                mGuest.setTextColor(getResources().getColor(R.color.header_blue));
                mComment.setTextColor(getResources().getColor(R.color.black));
                mDetail.setTextColor(getResources().getColor(R.color.black));
                root.findViewById(R.id.video_rlGuest).setVisibility(View.VISIBLE);
                root.findViewById(R.id.video_rlDetail).setVisibility(View.GONE);
                root.findViewById(R.id.video_rlComment).setVisibility(View.GONE);
                mTabLine3.setVisibility(View.VISIBLE);
                mTabLine2.setVisibility(View.INVISIBLE);
                mTabLine1.setVisibility(View.INVISIBLE);
                break;
            case R.id.alert_shang_bt1:
                changeViewColor(1, shang_btn1);
                break;
            case R.id.alert_shang_bt2:
                changeViewColor(2, shang_btn2);
                break;
            case R.id.alert_shang_bt3:
                changeViewColor(3, shang_btn3);
                break;
            case R.id.alert_shang_et1:
                changeViewColor(4, shang_et1);
                break;
            /*********************          支付宝支付      **********************/
            case R.id.alert_shang_llZhifubao:
                String money = shang_et1.getText().toString();
                if (TextUtils.isEmpty(money) && MONEY == 0) {
                    ToastUtil.showShort("请输入或选择金额");
                    break;
                }
                if ("0".equals(money) || "000".equals(money) || "0.0".equals(money) || "00".equals(money)) {
                    ToastUtil.showShort("金额不能为0");
                    break;
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
                break;
            /*********************           微信支付      **********************
             *
             *  先请求app后台，根据返回结果在进行微信支付
             *
             *
             * **********************/
            case R.id.alert_shang_llWeixin:
                String money1 = shang_et1.getText().toString().trim(); //金额
                describe = etDescribe.getText().toString().trim();  //描述（可以为空）
                if (TextUtils.isEmpty(money1) && MONEY == 0) {
                    ToastUtil.showShort("请输入或选择金额");
                    break;
                }
                if ("0".equals(money1) || "000".equals(money1) || "0.0".equals(money1) || "00".equals(money1)) {
                    ToastUtil.showShort("金额不能为0");
                    break;
                }
                if (money1.length() > 0) {
                    MONEY = Float.parseFloat(money1);
                }
                //传金额和描述
                DecimalFormat fnum = new DecimalFormat("0.00");
                String mon = fnum.format(MONEY);
                requestWeiXinPay(mon, describe);
                break;
            case R.id.tvPassWord:   //密码和付费两用
                if (seetype.equals("*")) //密码
                {
                    showPassWordDialog();

                } else    //付费
                {
                    showMoneyDialog();
                }

                break;
            case R.id.tv_input:       //点击弹出键盘和显示popwindow
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
     * 付费视频
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_input_password, null);
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
                judge(et);
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
                    controllerLive.setVisibility(View.VISIBLE);
                    ivCoverBack.setVisibility(View.GONE);  //封面阴影
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLivePlayer.start();
                        }
                    }, 10);
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

        View view = LayoutInflater.from(getActivity()).inflate(
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
        video_alertDialog = new Video_AlertDialog(getActivity(), view).builder();
        video_alertDialog.show();

        //关闭
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_alertDialog.cancel();
            }
        });
    }

    /**
     * 弹出主播的资料
     */
    private void alertDialog() {
       /* View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.video_alert, null);
        ImageView close = (ImageView) view.findViewById(R.id.alert_close);
        headImageView = (CircleImageView) view.findViewById(R.id.alert_circleImageView);
        TextView videoCount = (TextView) view.findViewById(R.id.alert_videoCount);  //视频数量
        TextView fansCount = (TextView) view.findViewById(R.id.alert_fansCount);    //粉丝数量
        TextView followCount = (TextView) view.findViewById(R.id.alert_followCount);//关注数量
        TextView anchorName = (TextView) view.findViewById(R.id.alert_anchorName); //主播名字

        if (fansBean != null) {
            anchorName.setText(liveTvBean.getUsername() + "");
            videoCount.setText(fansBean.getLivecount() + "");
            fansCount.setText(fansBean.getFanscount() + "");
            followCount.setText(fansBean.getAttendcount() + "");
            Utiles.log("-----------chat-----money---" + fansBean.toString());
        }
        final String[] msg = {"关注"};
        if (liveTvBean.isIsattend() == true && null != liveTvBean) {
            msg[0] = "已关注";
        } else {
            msg[0] = "关注";
        }
        if (!TextUtils.isEmpty(headURL))
            Picasso.with(getActivity()).load(headURL).placeholder(R.drawable.default_head_bg).
                    error(R.drawable.default_head_bg).fit().centerCrop().into(headImageView);
        alertDialog = new AnchorAlertDialog(getActivity(), view,0.80f).builder();
        final UserInfoBean userInfo = SharedUtil.getUserInfo(activity);
        alertDialog.setNegativeButton("回复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前用户是否是该视频的发布者
                if (!TextUtils.isEmpty(userInfo.getUserid() + "") &&
                        !TextUtils.isEmpty(liveTvBean.getUserid() + "") && userInfo.getUserid() == liveTvBean.getUserid()) {
                    ToastUtil.showCenterToast(activity, "自己不能回复自己哦~");
                } else {
                    Utiles.x = -1;
                    // getActivity().finish();
                    RongIM.getInstance().startPrivateChat(BaseFragmentActivity.activity, liveTvBean.getUserid() + "", liveTvBean.getUsername() + "");
                }
            }
        })
                .setPositiveButton(msg[0], new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(userInfo.getUserid() + "") &&
                                !TextUtils.isEmpty(liveTvBean.getUserid() + "") && userInfo.getUserid() == liveTvBean.getUserid()) {
                            ToastUtil.showCenterToast(activity, "不能关注自己哦~");
                        } else {
                            if (liveTvBean.isIsattend() == true)  //已经关注（点击一下就取消了关注）
                            {
                                // ToastUtil.showCenterToast(activity, "取消关注");
                                cancelFollow(liveTvBean);
                                msg[0] = "关注";
                            } else {
                                follow(liveTvBean);
                                msg[0] = "已关注";
                            }
                        }
                    }
                });
        alertDialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });*/
    }

    /**
     * 关注
     */
    private void follow(final LiveTvBean bean) {
        final UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().followPerson(networkAndLogin_ok.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("  01:" + response + "------" + networkAndLogin_ok.getUserid() + "---------" + bean.getUserid());
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() == 0) {
                    ToastUtil.showCenterToast(activity, "关注成功~");
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
        final UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().cancelFollowPerson(networkAndLogin_ok.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("  01:" + response + "------" + networkAndLogin_ok.getUserid() + "---------" + bean.getUserid());
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() != 0) {
                    ToastUtil.showCenterToast(activity, "取消关注成功~");
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
     * 点击发布者头像获取该发布者的粉丝数量
     */
    private void getFansCount() {
        if (null != liveTvBean && liveTvBean.getUserid() > 0) {
            OkHttpClientManager.Param userid1 = new OkHttpClientManager.Param("userid", liveTvBean.getUserid() + "");
            OkHttpClientManager.getInstance();
            OkHttpClientManager.postAsyn(UrlsManager.URL_VIDEO_FANSCOUNT, new MyStringCallBack(), userid1);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage() {
        String contString = comment_edit.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setMsgType(false);
            entity.setText(contString);
            TextMessage textMessage = TextMessage.obtain(entity.getText());
            Message message = Message.obtain(targetId, conversationType, textMessage);
            message.getContent().setUserInfo(info);
            message.setMessageDirection(Message.MessageDirection.SEND);
            MyRongIM.getInstance().sendMessage(message);
            comment_edit.setText("");
            ll_facechoose.setVisibility(View.GONE);  //emoji隐藏
            ivEmoji.setImageResource(R.drawable.inputview_icon_emoji);//设置emoji为初始值
            hideInput(getActivity(), comment_edit);
        }
    }

    /**
     * 分享
     */
    private void initShare() {
        ShareSDK.initSDK(getActivity());
        mShareDialog = new ShareDialog(getActivity());
        mShareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvShareSina:
                        ShareSDKUtils.shareSina( "【约吧互动直播】" +liveTvBean.getTitle()+liveTvBean.getUrl(),liveTvBean.getCoverurl());
                        break;
                    case R.id.tvShareQzone:
                        ShareSDKUtils.shareQZone( "【约吧互动直播】" +liveTvBean.getTitle(),liveTvBean.getUrl(),liveTvBean.getStatus(),liveTvBean.getCoverurl());
                        break;
                    case R.id.tvSharewx:
                        ShareSDKUtils.shareWechat("【约吧互动直播】" + liveTvBean.getTitle(),liveTvBean.getStatus(),liveTvBean.getCoverurl(),liveTvBean.getUrl());
                        break;
                    case R.id.tvSharewxpy:
                        ShareSDKUtils.shareWechatMoments("【约吧互动直播】" + liveTvBean.getTitle(),liveTvBean.getCoverurl(),liveTvBean.getUrl());
                        break;
                    case R.id.tvShareMsg:
                        ShareSDKUtils.shareShortMessage("【约吧互动直播】"+liveTvBean.getTitle()+liveTvBean.getUrl());
                        break;
                }
            }
        });
    }

    public void ReplyBox() {
        final View view = View.inflate(activity,
                R.layout.custom_facerelativelayout, null);
        comment_edit = (EditText) view.findViewById(R.id.et_sendmessage);
        ll_facechoose = (RelativeLayout) view.findViewById(R.id.ll_facechoose);
        ivEmoji = (ImageView) view.findViewById(R.id.btn_face);
        ivEmoji.setVisibility(View.VISIBLE);
        ivEmoji.setOnClickListener(this);
        send = (ImageView) view.findViewById(R.id.btn_send);
        // TextView dismiss = (TextView) view.findViewById(R.id.message_replytop);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(comment_edit.getText().toString().trim())) {
                    Toast.makeText(activity, "请填写回复内容", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    sendTextMessage();
                    // ToastUtil.showCenterToast(activity,comment_edit.getText().toString());
                }
            }
        });
       /* dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) comment_edit
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mpopupWindow.dismiss();
            }
        });*/

        view.startAnimation(AnimationUtils.loadAnimation(activity,
                R.anim.fade_in));
        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow();
            mpopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

            mpopupWindow.setFocusable(true);
            mpopupWindow.setTouchable(true);
            mpopupWindow.setOutsideTouchable(true);
            mpopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            //这行代码非常关键，如果不加上，弹出的键盘会讲mpopupWindow的布局遮挡一部分，一定要加，切记切记。。。。。
            mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        mpopupWindow.setContentView(view);
        comment_edit.requestFocus();
//        comment_edit.setText("");
//        comment_edit.setHint(" 我来说两句");
//        comment_edit.setHintTextColor(getResources().getColor(R.color.textColor_Black));
        mpopupWindow.showAtLocation(tv_input, Gravity.BOTTOM, 0, 0);
        mpopupWindow.update();
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
     * 获取软键盘的高度
     * 返回的px   heightDifference
     */
    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = rootView.getRootView().getHeight();
        heightDifference = screenHeight - (r.bottom - r.top);
        Log.e("chatRoom", "--------" + heightDifference);
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
     * 检测软键盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 18) {
            // When SDK Level >= 18, the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        return softInputHeight;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void alipayPay(String orderInfo) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            // getActivity().finish();
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
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                android.os.Message msg = new android.os.Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
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
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

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
        return SignUtils.sign(content, RSA_PRIVATE);
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
        int currentUserId = SharedUtil.getUserInfo(activity).getUserid();
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
     * **********************************************************************************
     * ****************************        微信支付         ******************************
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

//    private String genOutTradNo() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }

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
     * 判断微信支付的回调
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
        }
        return false;
    }

    /***
     * 刷新在线直播人数
     */
    private void refresh() {
        final String url = UrlsManager.URL_CHAT_ROOM_NUMBER + targetId;
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log(TAG + "---fail--" + e.getMessage() + "--------" + url);
            }

            @Override
            public void onResponse(String response) {
                Utiles.log(TAG + "---response--" + response + "--------url" + url);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    JSONObject jsonObject1 = new JSONObject(result);
                    total = jsonObject1.getInt("total"); //人数
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Utiles.log(TAG + "---fail--" + "--------" + url);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 60);
                mHandler.sendMessage(mHandler.obtainMessage());
                Utiles.log(TAG + "---fail--" + "--------" + mHandler.obtainMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取聊天室的人数
     */
    private void chatRoomNumber() {

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
                    Utiles.log(TAG + "---total   --" + total + "-------" + id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * 定时获取在线人数
     */
    /*
    class MyThread implements Runnable {
        @Override
        public void run() {
            while (mRunning) {
                try {
                    chatRoomNumber();
                    Thread.sleep(10000);// 线程暂停10秒，单位毫秒
                    //虽然Message的构造函数是public的，但是最好是使用Message.obtain( )或Handler.obtainMessage( )函数来获取Message对象，因为Message的实现中包含了回收再利用的机制，可以提供效率。
                    android.os.Message message = mHandler.obtainMessage();
                    message.what = REFRESH_NUMBER;
                    mHandler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }*/

    /***
     * 获取粉丝数量的回调
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
}