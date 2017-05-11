package com.xz.activeplan.ui.personal.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.AsyncImageLoader;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.ui.recommend.activity.ChooseCityActivity;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.JsonUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.SystemUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CustomProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.rong.imlib.RongIMClient;

/**
 * 设置界面activity
 *
 * @author johnny
 */
public class SettingActivity extends BaseFragmentActivity implements
        OnClickListener, ClassObserver {
    private TextView mTvHeadTitle, tvSettingLogout, tvCurrentVersion, tvHomeLeftSize, tvHomeLeftCity, tvHomeLeftRemind;
    private CustomProgressDialog mProgressDialog;
    private ProgressDialog dialog;
    private NotificationManager manager;
    private Notification.Builder builder;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_setting);
        initViews();
    }

    private void initViews() {
        mProgressDialog =new CustomProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        ClassConcrete.getInstance().addObserver(this);
        //返回框背景
        View viewHeald = findViewById(R.id.relativeLayout_toolbar);
        viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
        //返回键
        ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(this);
        //头部字体
        mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));

        findViewById(R.id.llHomeLeftAbout).setOnClickListener(this);//我的城市
        findViewById(R.id.llHomeLeftFeedBack).setOnClickListener(this);//疑问反馈
        findViewById(R.id.llHomeLeftRemind).setOnClickListener(this);//活动设定
        findViewById(R.id.llVersion).setOnClickListener(this);//版本
        findViewById(R.id.llHomeLeftCity).setOnClickListener(this);//我的城市
        findViewById(R.id.llHomeLeftClean).setOnClickListener(this);//llHomeLeftClean
        findViewById(R.id.llHomeLeftcooperation).setOnClickListener(this);
        tvSettingLogout = (TextView) findViewById(R.id.tvSettingLogout);
        tvCurrentVersion = (TextView) findViewById(R.id.tvCurrentVersion);
        tvHomeLeftSize = (TextView) findViewById(R.id.tvHomeLeftSize);
        tvHomeLeftCity = (TextView) findViewById(R.id.tvHomeLeftCity);
        tvHomeLeftRemind = (TextView) findViewById(R.id.tvHomeLeftRemind);
        mTvHeadTitle.setText(getResources().getString(R.string.setting_title));
        tvSettingLogout.setOnClickListener(this);
        PackageInfo packageInfo = SystemUtil.getPackageInfo(this);
        tvCurrentVersion.setText(packageInfo.versionName);
        if (SharedUtil.isLogin(SettingActivity.this)) {
            tvSettingLogout.setBackgroundColor(Color.RED);
            tvSettingLogout.setText("退出登录");
        } else {
            tvSettingLogout.setVisibility(View.GONE);
        }

        tvHomeLeftCity.setText(SharedUtil.getCity(SettingActivity.this));

        String fileRootDir = FileUtil.getRootPath() + "TempImage/";
        tvHomeLeftSize.setText(String.format("%.2f", FileUtil.getDirSize(new File(fileRootDir))) + "MB");
        String type = SharedUtil.get(SettingActivity.this, "naozhong");
        if ("1".equals(type)) {
            mTvHeadTitle.setText("当前提醒");
        } else if ("2".equals(type)) {
            mTvHeadTitle.setText("提前一天提醒");
        } else if ("3".equals(type)) {
            mTvHeadTitle.setText("提前两天提醒");
        } else if ("4".equals(type)) {
            mTvHeadTitle.setText("不提醒");
        } else {
            mTvHeadTitle.setText("当前提醒");
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.llHomeLeftAbout:   //关于我们
                intent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.llHomeLeftFeedBack://疑问反馈
                intent = new Intent(SettingActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.llHomeLeftRemind://设定活动
                intent = new Intent(SettingActivity.this, RemindActivity.class);
                startActivity(intent);
                break;
            case R.id.llHomeLeftCity://我的城市
                intent = new Intent(SettingActivity.this, ChooseCityActivity.class);
                startActivity(intent);
                break;
            case R.id.llVersion:   //版本更新
//                updateVer();
                dialog.show();
                BDAutoUpdateSDK.cpUpdateCheck(this, new MyCPCheckUpdateCallback());
                break;
            case R.id.tvSettingLogout:  //退出登录
                if (SharedUtil.isLogin(SettingActivity.this)) {
                    SharedUtil.saveLogin(SettingActivity.this, false, false);
                    ToastUtil.showShort("退出登录成功!");
                    tvSettingLogout.setText("登录");
                    // 退出融云连接
                    RongIMClient.getInstance().logout();
                    tvSettingLogout.setBackgroundColor(Color
                            .parseColor("#ffffffff"));
                    ClassConcrete.getInstance().postDataUpdate(
                            new EventBean(EventType.LOGOUT_NOTIFI_TYPE));
                    SettingActivity.this.finish();
                } else {
                    intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.llHomeLeftClean:  //清除缓存
                mProgressDialog.show();
                String fileRootDir = FileUtil.getRootPath() + "TempImage/";
                FileUtil.delAllFile(fileRootDir);
                tvHomeLeftSize.setText(String.format("%.2f", FileUtil.getDirSize(new File(fileRootDir))) + "MB");
                mProgressDialog.dismiss();
                AsyncImageLoader.getInstance().clearBitmap();  //清除bitmap
                break;
            case R.id.id_ImageHeadTitleBlack:  //返回
                finish();
                break;
            case R.id.llHomeLeftcooperation:   //推广合作
                startActivity(new Intent(SettingActivity.this, CooperationActivity.class));
                break;
        }

    }

//    private void initData(String url,String logo,String pakagename,) {
//
//        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        builder = new Notification.Builder(this);
//        builder.
//        mNotification.contentView = new RemoteViews(pakagename, R.layout.activity_update_message);
//        mNotification.flags = Notification.FLAG_NO_CLEAR;
//    }
    private void updateVer() {
        mProgressDialog.show();
        PackageInfo packageInfo = SystemUtil.getPackageInfo(this);
        UserInfoServiceImpl.getInstance().updatever(packageInfo.versionCode + "",
                new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            final StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                String updateinfo = "";
                                int versioncode = 0;
                                String versionname = "";
                                try {
                                    JSONObject json = new JSONObject(statusBean.getData());
                                    updateinfo = JsonUtils.getString("updateinfo", json);
                                    versioncode = JsonUtils.getInt("versioncode", json);
                                    versionname = JsonUtils.getString("versionname", json);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                View view = View.inflate(SettingActivity.this, R.layout.dialog_yesorno, null);
                                AlertDialog dialog = new AlertDialog(SettingActivity.this, view, 0.85f).builder();
                                dialog.show();
                                ((TextView) view.findViewById(R.id.id_TextViewName)).setText("更新版本为 " + versionname);
                                view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
                                ((TextView) view.findViewById(R.id.txt_msg)).setText(updateinfo + " 确认更新版本?");
                                dialog.setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject json = new JSONObject(statusBean.getData());
                                            String url = JsonUtils.getString("url", json);
                                            Intent i = new Intent(
                                                    Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                dialog.setNegativeButton("稍后更新", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });


                            } else {
                                ToastUtil.showShort("未检查到新版本!");
                            }
                        } else {
                            ToastUtil.showShort("未检查到新版本!");
                        }
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        mProgressDialog.dismiss();
                        ToastUtil.showShort("未检查到新版本!");
                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ClassConcrete.getInstance().removeObserver(this);
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.CITY_NOTIFI_TYPE) {
            tvHomeLeftCity.setText(SharedUtil.getCity(SettingActivity.this));
        }
        return false;
    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
        @Override
        public void onCheckComplete() {
            dialog.dismiss();
        }
    }

    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
            if(info != null) {
                BDAutoUpdateSDK.uiUpdateAction(SettingActivity.this, new MyUICheckUpdateCallback());
            }else {
               ToastUtil.showShort(getString(R.string.islatestversion));
            }
            dialog.dismiss();
        }

    }
}
