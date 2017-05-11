package com.xz.activeplan;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.find.adapter.commonAdapter.DiskLruCacheUtil;
import com.xz.activeplan.ui.live.rongclound.LiveKit;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class XZApplication extends Application {

    public static RequestQueue queue;
    public static DiskLruCacheUtil.BitmapCache sBitmapCache;
    private static XZApplication mInstance;
    private static UserInfo userInfo;
    Set<Activity> activitys = new HashSet<Activity>();
    private QtylLocationListener locationListener = new QtylLocationListener(this);
    private LocationClient locationManager;
    private ArrayList<TicketAddBean> ticketTypes;
    private String title;
    private UserInfoBean user;

    public static XZApplication getInstance() {
        return mInstance;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static RequestQueue getQueue() {
        return queue;
    }

    public static UserInfo getCurrentUserInfo() {
        return userInfo;
    }

    public static void setCurrentUserInfo(UserInfo info) {
        userInfo = info;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utiles.log("XZApplication");
        queue = Volley.newRequestQueue(getApplicationContext());
        sBitmapCache = new DiskLruCacheUtil.BitmapCache(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        ShareSDK.initSDK(getApplicationContext());
        mInstance = this;
        String currProcessName = getCurProcessName(this);
        Utiles.log("当前进程：" + currProcessName);

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(getApplicationContext());
            LiveKit.init(getApplicationContext());
            init();
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(300000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        //option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        this.locationManager = new LocationClient(this);
        this.locationManager.setLocOption(option);
    }

    @SuppressWarnings("static-access")
    private void init() {
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
        };

        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PUSH_SERVICE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.SYSTEM, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, provider);


    }

    public void exit() {
        finishAllActivity();
    }

    public void finishAllActivity() {
        for (Activity activity : activitys) {
            activity.finish();
        }
    }

    /**
     * 在application中增加已被使用的activity。
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 将application中已经记录的activity移除。
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    public void unRegisterLocationListener() {
        this.locationManager.unRegisterLocationListener(this.locationListener);
        this.locationManager.stop();
    }

    public LocationClient getLocationManager() {
        return locationManager;
    }

    public ArrayList<TicketAddBean> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(ArrayList<TicketAddBean> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
