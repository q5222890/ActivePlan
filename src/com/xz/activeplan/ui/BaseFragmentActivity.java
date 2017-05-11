package com.xz.activeplan.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.live.rongclound.LiveKit;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * activity基类,该类继承于FragmentActivity，负责一些activity通用的方法的编写。
 * <p/>
 * 同时也负责一部分activity被管理的职能，QCloud中的activity应该继承于该类。
 *
 * @author cy
 */
public class BaseFragmentActivity extends AppCompatActivity {
    private final static int MXG = 0x12000;
    public static BaseFragmentActivity activity;
    public UserInfoBean userInfoBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MXG) {
                showOfflineDialog();
            }
            super.handleMessage(msg);
        }
    };

    protected XZApplication getApp() {
        return (XZApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R.style.AppCompat_NoActionBar);
        getApp().addActivity(this);
        activity = this;
        if(SharedUtil.isLogin(this))
        {
            RongIMClient.getInstance().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                @Override
                public void onChanged(ConnectionStatus connectionStatus) {
                    if (connectionStatus.getValue() == 3) {
                        Utiles.log("您的账号在另一台手机上登录,您已被迫下线。");
                        handler.sendEmptyMessage(MXG);
                    }
                }
            });
        }

        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            Utiles.log("BaseFragmentActivity__ConnectRongIm");
            if (SharedUtil.isLogin(this)) {
                //正常连接融云
                userInfoBean = SharedUtil.getUserInfo(this);
                UserInfo userInfo = new UserInfo(userInfoBean.getUserid() + "", userInfoBean.getUsername(), Uri.parse(userInfoBean.getHeadurl()));
                ConnectRongIm(userInfoBean.getToken(), userInfo);
            } else {
                //游客连接登入
                ConnectRongIm(UrlsManager.RONG_DISCONNECT_TOKEN, new UserInfo("00", "游客", Uri.EMPTY));
            }
        }
        boolean isWear = getPackageManager().hasSystemFeature("android.hardware.type.watch");

        // 设置AppKey
        // StatService.setAppKey("a9e2ad84a2"); // appkey必须在mtj网站上注册生成，该设置建议在AndroidManifest.xml中填写，代码设置容易丢失
        /*
         * 设置渠道的推荐方法。该方法同setAppChannel（String）， 如果第三个参数设置为true（防止渠道代码设置会丢失的情况），将会保存该渠道，每次设置都会更新保存的渠道，
         * 如果之前的版本使用了该函数设置渠道，而后来的版本需要AndroidManifest.xml设置渠道，那么需要将第二个参数设置为空字符串,并且第三个参数设置为false即可。
         * appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以 该参数也可以设置在AndroidManifest.xml中
         */
        // StatService.setAppChannel(this, "RepleceWithYourChannel", true);
        // 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
        StatService.setSessionTimeOut(30);
        // setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG，打开崩溃错误收集，默认是关闭的
        StatService.setOn(this, StatService.EXCEPTION_LOG);
        /*
         * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/> 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
         *
         * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/> 这个参数的设置暂时只支持代码加入，
         * 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
         */
        StatService.setLogSenderDelayed(0);
        /*
         * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
         *
         * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum. SET_TIME_INTERVAL, 1, false); 第二个参数可选：
         * SendStrategyEnum.APP_START SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
         * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
         * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
         */
        StatService.setSendLogStrategy(this, SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
        // 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
        StatService.setDebugOn(true);

    }

    /**
     * 被迫下线的dialog
     */
    private void showOfflineDialog() {
        Utiles.log("下线了。。。");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_yesorno, null);
        AlertDialog dialog = new AlertDialog(this, view, 0.7f).builder();
        dialog.setCancelable(false);
        view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
        view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
        view.findViewById(R.id.img_line).setVisibility(View.GONE);
        dialog.setTitle("提示")
                .setMsg("您的账号在另一台手机上登录,您已被迫下线。")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedUtil.saveLogin(activity, false, false);
                        ToastUtil.showShort("退出登录成功!");
                        // 退出融云连接
                        RongIMClient.getInstance().logout();
                        ClassConcrete.getInstance().postDataUpdate(
                                new EventBean(EventType.LOGOUT_NOTIFI_TYPE));
                    }
                }).show();  //  此处发生异常WindowManager$BadTokenException
    }

    private void ConnectRongIm(String token, final UserInfo userInfo) {
        LiveKit.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String userId) {
                Utiles.log("connect onSuccess");
                LiveKit.setCurrentUser(userInfo);
            }

            @Override
            public void onError(RongIMClient.ErrorCode arg0) {
                Utiles.log("connect onError" + arg0);

            }

            @Override
            public void onTokenIncorrect() {
                Utiles.log("connect onTokenIncorrect");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（注意： 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 如果该FragmentActivity包含了几个全页面的fragment，那么可以在fragment里面加入就可以了，这里可以不加入。如果不加入将不会记录该Activity页面。
         */
        StatService.onResume(this);
    }

    protected void onPause() {
        super.onPause();

        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getApp().removeActivity(this);
    }

    private class MySendMessageListener implements RongIM.OnSendMessageListener {
        /**
         * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
         *
         * @param message 发送的消息实例。
         * @return 处理后的消息实例。
         */
        @Override
        public Message onSend(Message message) {
            //开发者根据自己需求自行处理逻辑
            return message;
        }

        /**
         * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
         *
         * @param message              消息实例。
         * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
         * @return true 表示走自已的处理方式，false 走融云默认处理方式。
         */
        @Override
        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
            if (message.getSentStatus() == Message.SentStatus.FAILED) {
                if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                    //不在聊天室
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                    //不在讨论组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                    //不在群组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                    //你在他的黑名单中
                }
            }
            return false;
        }
    }
}
