package com.xz.activeplan.ui.news.activity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.xz.activeplan.R;
import com.xz.activeplan.utils.ImageUtils;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ProgressDialog;

public class UpdateActivityextends extends Activity implements View.OnClickListener {
    private TextView txt_log;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        findViewById(R.id.btn_ui).setOnClickListener(this);
        findViewById(R.id.btn_silence).setOnClickListener(this);
        findViewById(R.id.btn_as).setOnClickListener(this);
        findViewById(R.id.btn_noui).setOnClickListener(this);
        txt_log = (TextView) findViewById(R.id.txt_log);
        dialog = new ProgressDialog(this);
    //    dialog.setIndeterminate(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ui://UI更新
                txt_log.setText("UI更新");
                dialog.show();
                BDAutoUpdateSDK.uiUpdateAction(this, new MyUICheckUpdateCallback());
                break;
            case R.id.btn_silence://静默更新
                txt_log.setText("静默更新");
                BDAutoUpdateSDK.silenceUpdateAction(this);
                break;
            case R.id.btn_as://百度助手更新
                txt_log.setText("百度助手更新");
                dialog.show();
                BDAutoUpdateSDK.asUpdateAction(this, new MyUICheckUpdateCallback());
                break;
            case R.id.btn_noui://无UI更新
                txt_log.setText("无UI更新");
                dialog.show();
                BDAutoUpdateSDK.cpUpdateCheck(this, new MyCPCheckUpdateCallback());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
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
          /*  String appIconUrl = info.getAppIconUrl();
            Utiles.log("  appIconUrl:"+appIconUrl);
            String appPackage = info.getAppPackage();
            Utiles.log("  appPackage:"+appPackage);
            String appPath = info.getAppPath();
            Utiles.log("  appPath:"+appPath);
            long appSize = info.getAppSize();
            Utiles.log("  appSize:"+appSize);
            String appSname = info.getAppSname();
            Utiles.log("  appSname:"+appSname);
            String appUrl = info.getAppUrl();
            Utiles.log("  appUrl:"+appUrl);
            String appVersionName = info.getAppVersionName();
            Utiles.log("  appVersionName:"+appVersionName);*/

            if (infoForInstall==null){
                Utiles.log("infoForInstall  == null");
            }else {
                Utiles.log("infoForInstall  != null");
            }
            if (info==null){
                Utiles.log("info  == null");
            }else {
                Utiles.log("info !=null");
            }

         /*   String appSName = infoForInstall.getAppSName();
            Utiles.log("  appSName:"+appSName);
            String installPath = infoForInstall.getInstallPath();
            Utiles.log("  installPath:"+installPath);
            String appVersionName = infoForInstall.getAppVersionName();
            Utiles.log("  appVersionName:"+appVersionName);*/

            if(infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
                txt_log.setText(txt_log.getText() + "\n install info: " + infoForInstall.getAppSName() + ", \nverion=" + infoForInstall.getAppVersionName() + ", \nchange log=" + infoForInstall.getAppChangeLog());
                txt_log.setText(txt_log.getText() + "\n we can install the apk file in: " + infoForInstall.getInstallPath());
                BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), infoForInstall.getInstallPath());
            }else if(info != null) {
                BDAutoUpdateSDK.cpUpdateDownload(UpdateActivityextends.this, info, new UpdateDownloadCallback());
            }else {
                txt_log.setText(txt_log.getText() + "\n no update.");
            }
            dialog.dismiss();
        }

    }

    private class UpdateDownloadCallback implements CPUpdateDownloadCallback {
        @Override
        public void onDownloadComplete(String apkPath) {
            txt_log.setText(txt_log.getText() + "\n onDownloadComplete: " + apkPath);
            BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), apkPath);
        }

        @Override
        public void onStart() {
            txt_log.setText(txt_log.getText() + "\n Download onStart");
        }

        @Override
        public void onPercent(int percent, long rcvLen, long fileSize) {
            txt_log.setText(txt_log.getText() + "\n Download onPercent: " + percent + "%");
        }

        @Override
        public void onFail(Throwable error, String content) {
            txt_log.setText(txt_log.getText() + "\n Download onFail: " + content);
        }

        @Override
        public void onStop() {
            txt_log.setText(txt_log.getText() + "\n Download onStop");
        }
    }
}