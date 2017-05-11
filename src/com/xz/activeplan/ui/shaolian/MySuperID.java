package com.xz.activeplan.ui.shaolian;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.isnc.facesdk.aty.Aty_FaceDetect;
import com.isnc.facesdk.aty.Aty_NormalFaceDetect;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.DebugMode;
import com.isnc.facesdk.common.MResource;
import com.isnc.facesdk.common.SDKConfig;
import com.isnc.facesdk.common.SuperIDUtils;
import com.isnc.facesdk.net.MsdkActive;
import com.isnc.facesdk.net.MsdkAppAddBehavior;
import com.isnc.facesdk.net.MsdkAppToken;
import com.isnc.facesdk.net.MsdkAuthorized;
import com.isnc.facesdk.net.MsdkCheckAppToken;
import com.isnc.facesdk.net.MsdkFakeIDUser;
import com.isnc.facesdk.net.MsdkLogout;
import com.isnc.facesdk.net.MsdkOpenIDAuthorized;
import com.isnc.facesdk.net.MsdkSoUpdate;
import com.isnc.facesdk.net.MsdkUidAuthorized;
import com.isnc.facesdk.net.MsdkUpdateAppInfo;
import com.isnc.facesdk.net.MsdkUpdateAppUid;
import com.isnc.facesdk.net.MsdkUserAddBehavior;
import com.isnc.facesdk.soloader.SoDownloadManager;
import com.xz.activeplan.utils.Util;
import com.xz.activeplan.utils.Utiles;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/20.
 */
public class MySuperID {
    private static volatile MySuperID sSuperID;
    private int mCameraType = 1;
    private int mFrame = 15;
    private String mIsbusiness = "false";
    private int mFaceLimit = 150;
    private int mOrientation = -1;
    private HashMap mHashMap;
    private static final int REQUEST_CODE = 100;

    public void setHashMap(HashMap var1) {
        Iterator var2 = var1.entrySet().iterator();

        while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Object var4 = var3.getKey();
            Object var5 = var3.getValue();
            String var6 = var4.toString();
            byte var7 = -1;
            switch(var6.hashCode()) {
            case -1881861323:
                if(var6.equals("is_business")) {
                    var7 = 0;
                }
                break;
            case -1439500848:
                if(var6.equals("orientation")) {
                    var7 = 4;
                }
                break;
            case 97692013:
                if(var6.equals("frame")) {
                    var7 = 3;
                }
                break;
            case 911903577:
                if(var6.equals("face_limit")) {
                    var7 = 2;
                }
                break;
            case 2059187220:
                if(var6.equals("camera_type")) {
                    var7 = 1;
                }
            }

            switch(var7) {
            case 0:
                this.setIsbusiness(var5.toString());
                break;
            case 1:
                this.setCameraType(Integer.valueOf(var5.toString()).intValue());
                break;
            case 2:
                this.setFaceLimit(Integer.valueOf(var5.toString()).intValue());
                break;
            case 3:
                this.setFrame(Integer.valueOf(var5.toString()).intValue());
                break;
            case 4:
                this.setOrientation(Integer.valueOf(var5.toString()).intValue());
            }
        }

        this.mHashMap = var1;
    }

    private void setCameraType(int var1) {
        this.mCameraType = var1;
    }

    private void setFrame(int var1) {
        this.mFrame = var1;
    }

    private void setIsbusiness(String var1) {
        this.mIsbusiness = var1;
    }

    private void setFaceLimit(int var1) {
        this.mFaceLimit = var1;
    }

    private void setOrientation(int var1) {
        this.mOrientation = var1;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getCameraType() {
        return this.mCameraType;
    }

    public int getFrame() {
        return this.mFrame;
    }

    public String getIsbusiness() {
        return this.mIsbusiness;
    }

    public int getFaceLimit() {
        return this.mFaceLimit;
    }

    private MySuperID() {
    }

    public static MySuperID getInstance() {
        if(sSuperID == null) {
            Class var0 = MySuperID.class;
            synchronized(MySuperID.class) {
                if(sSuperID == null) {
                    sSuperID = new MySuperID();
                }
            }
        }

        return sSuperID;
    }

    public static void initFaceSDK(Context var0) {
        initFaceSDK(var0, (String)null, (String)null);
    }

    public static void initFaceSDK(final Context var0, String var1, String var2) {
        recordActive(var0);
        if(SuperIDUtils.readSth("dsd12ds3poi0983d.bin").equals("")) {
            if(Cache.getCached(var0, "device_id").equals("")) {
                String var3 = SuperIDUtils.getDeviceID(var0) + System.currentTimeMillis();
                Cache.saveCached(var0, new String[]{"device_id", var3});
                SuperIDUtils.writeSth("dsd12ds3poi0983d.bin", var3);
            } else {
                SuperIDUtils.writeSth("dsd12ds3poi0983d.bin", Cache.getCached(var0, "device_id"));
            }
        } else if(!Cache.getCached(var0, "device_id").equals(SuperIDUtils.readSth("dsd12ds3poi0983d.bin"))) {
            Cache.saveCached(var0, new String[]{"device_id", SuperIDUtils.readSth("dsd12ds3poi0983d.bin")});
        }

        if(Cache.getCached(var0, "sdk_version").equals("")) {
            Cache.saveCached(var0, new String[]{"sdk_version", "20160603"});
        }

        if(Integer.valueOf("20160603").intValue() > Integer.valueOf(Cache.getCached(var0, "sdk_version")).intValue() && !Cache.getCached(var0, "app_token").equals("")) {
            new MsdkCheckAppToken(var0, Cache.getCached(var0, "app_token"), getDeviceInfo(var0), new com.isnc.facesdk.net.MsdkCheckAppToken.SuccessCallback() {
                public void onSuccess(JSONObject var1) {
                    Cache.saveCached(var0, new String[]{"sdk_version", "20160603"});
                }
            }, new com.isnc.facesdk.net.MsdkCheckAppToken.FailCallback() {
                public void onFail() {
                }
            });
            getSoInfo(var0, Cache.getCached(var0, "app_token"));
        } else if(Cache.getCached(var0, "app_token").equals("") || (long)Cache.getIntCached(var0, "expired", 0) < System.currentTimeMillis() / 1000L) {
            requestApptoken(var0, var1, var2, new MySuperID.SuccessCallback() {
                public void onSuccess(String var1) {
                }
            }, new MySuperID.FailCallback() {
                public void onFail() {
                }
            });
        }

    }

    public static String getSDKVersion() {
        return "20160603";
    }

    @SuppressLint({"SimpleDateFormat"})
    public static void recordActive(Context var0) {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMdd");
        Date var2 = new Date(System.currentTimeMillis());
        int var3 = Integer.valueOf(var1.format(var2)).intValue();
        if(Cache.getIntCached(var0, "useractive", 0) < var3 && !Cache.getCached(var0, "app_token").equals("")) {
            Cache.saveIntCached(var0, "useractive", var3);
            new MsdkActive(var0, Cache.getCached(var0, "app_token"), new com.isnc.facesdk.net.MsdkActive.SuccessCallback() {
                public void onSuccess(JSONObject var1) {
                }
            }, new com.isnc.facesdk.net.MsdkActive.FailCallback() {
                public void onFail(JSONObject var1) {
                }
            });
        }

    }

    private static String getDeviceInfo(Context var0) {
        JSONObject var1 = new JSONObject();

        try {
            var1.put("device", Build.MODEL);
            var1.put("deviceId", Cache.getCached(var0, "device_id"));
            var1.put("osVersion", String.valueOf(VERSION.SDK_INT));
            var1.put("screensize", SuperIDUtils.getScreenSizeArray(var0)[0] + "*" + SuperIDUtils.getScreenSizeArray(var0)[1]);
        } catch (JSONException var3) {
            var3.printStackTrace();
        }

        DebugMode.info(String.valueOf(var1));
        return String.valueOf(var1);
    }

    public static void requestApptoken(Context var0, MySuperID.SuccessCallback var1, MySuperID.FailCallback var2) {
        requestApptoken(var0, (String)null, (String)null, var1, var2);
    }

    public static void requestApptoken(final Context var0, String var1, String var2, final MySuperID.SuccessCallback var3, final MySuperID.FailCallback var4) {
        String var5 = "";
        String var6 = "";
        if(var1 == null && var2 == null) {
            HashMap var7 = SuperIDUtils.getappinfo(var0, new String[]{"SUPERID_APPKEY", "SUPERID_SECRET"});
            var5 = (String)var7.get("SUPERID_APPKEY");
            var6 = (String)var7.get("SUPERID_SECRET");
        } else {
            var5 = var1;
            var6 = var2;
        }

        String var8 = SuperIDUtils.getSign(var0, var0.getPackageName());
        new MsdkAppToken(var0, var5, var6, "Android", var0.getPackageName(), var8, Build.MODEL, getDeviceInfo(var0), new com.isnc.facesdk.net.MsdkAppToken.SuccessCallback() {
            @SuppressLint({"SimpleDateFormat"})
            public void onSuccess(JSONObject var1) {
                if(Cache.getCached(var0, "app_token").equals("")) {
                    MySuperID.getSoInfo(var0, var1.optString("app_token"));
                }

                Cache.saveCached(var0, new String[]{"app_token", var1.optString("app_token"), "app_action", var1.optString("app_action")});
                Cache.saveIntCached(var0, "expired", var1.optInt("expired"));
                if(Cache.getIntCached(var0, "useractive", 0) == 0) {
                    SimpleDateFormat var2 = new SimpleDateFormat("yyyyMMdd");
                    Date var3x = new Date(System.currentTimeMillis());
                    int var4 = Integer.valueOf(var2.format(var3x)).intValue();
                    Cache.saveIntCached(var0, "useractive", var4);
                }

                String var5 = var1.optString("app_token");
                if(var3 != null) {
                    var3.onSuccess(var5);
                }

            }
        }, new com.isnc.facesdk.net.MsdkAppToken.FailCallback() {
            public void onFail() {
                DebugMode.warn(var0.getString(MResource.getIdByName(var0, "string", "superid_tips_apptokenfail")));
                if(var4 != null) {
                    var4.onFail();
                }

            }
        });
    }

    public static void getSoInfo(final Context var0, String var1) {
        new MsdkSoUpdate(var0, var1, new com.isnc.facesdk.net.MsdkSoUpdate.SuccessCallback() {
            public void onSuccess(JSONObject var1) {
                JSONObject var2 = var1.optJSONObject("data");
                JSONArray var3 = var2.optJSONArray("files");
                if(var3.length() > 0) {
                    int var4 = 0;
                    String var5 = System.getProperty("os.arch");
                    String var6 = var5.substring(0, 3).toUpperCase();

                    for(int var7 = 0; var7 < var3.length(); ++var7) {
                        if(var3.optJSONObject(var7).optString("name").equals("mip-lib.zip") && var6.equals("MIP")) {
                            var4 = var7;
                            break;
                        }

                        if(var3.optJSONObject(var7).optString("name").equals("x86-lib.zip") && var6.equals("X86")) {
                            var4 = var7;
                            break;
                        }
                    }

                    Cache.saveCached(var0, new String[]{"sofoldername", ".superidLibs" + var2.optString("version"), "sosha1", var3.optJSONObject(var4).optString("sha1"), "sourl", var3.optJSONObject(var4).optString("url")});
                }

            }
        }, new com.isnc.facesdk.net.MsdkSoUpdate.FailCallback() {
            public void onFail() {
            }
        });
    }

    public static String formatInfo(Context var0, String... var1) {
        JSONObject var2 = new JSONObject();

        try {
            var2.put("Android_DeviceID", Cache.getCached(var0, "device_id"));

            for(int var3 = 0; var3 < var1.length; var3 += 2) {
                var2.put(var1[var3], var1[var3 + 1]);
            }
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return String.valueOf(var2);
    }

    public static String formatInfoObject(JSONObject var0) {
        return String.valueOf(var0);
    }

    public static void faceLogin(Activity var0) {
        Intent var1 = new Intent(var0, Aty_FaceDetect.class);
        var1.putExtra("intent_action", 1);
        var0.startActivityForResult(var1, 100);
    }

    public static void faceLogin(Activity var0, MySuperID.SoLoaderCallback var1) {
        if(SoDownloadManager.isSoExists(var0)) {
            faceLogin(var0);
        } else {
            var1.soLoader();
        }

    }

    public static void faceVerify(Activity var0, int var1) {
        Intent var2 = new Intent(var0, Aty_FaceDetect.class);
        var2.putExtra("intent_action", 3);
        var2.putExtra("intent_count", var1);
        var0.startActivityForResult(var2, 100);
    }

    public static void faceVerify(Activity var0, int var1, MySuperID.SoLoaderCallback var2) {
        if(SoDownloadManager.isSoExists(var0)) {
            faceVerify(var0, var1);
        } else {
            var2.soLoader();
        }

    }

    public static void faceLoginWithPhoneUid(Activity var0, String var1, String var2, String var3) {
        Intent var4 = new Intent(var0, Aty_FaceDetect.class);
        var4.putExtra("intent_phonenum", var1);
        var4.putExtra("intent_userinfo", var2);
        var4.putExtra("intent_appuid", var3);
        var4.putExtra("intent_action", 1);
        var0.startActivityForResult(var4, 100);
    }

    public static void faceLoginWithPhoneUid(Activity var0, String var1, String var2, String var3, MySuperID.SoLoaderCallback var4) {
        if(SoDownloadManager.isSoExists(var0)) {
            faceLoginWithPhoneUid(var0, var1, var2, var3);
        } else {
            var4.soLoader();
        }

    }

    public static void faceBundle(Activity context, String randomNumber, String userInfoStr) {
        Intent intent = new Intent(context, Aty_FaceDetect.class);
        intent.putExtra("intent_userinfo", userInfoStr);
        intent.putExtra("intent_appuid", randomNumber);
        intent.putExtra("intent_action", 2);
        context.startActivityForResult(intent, 100);
    }

    public static void faceBundle(Activity var0, String var1, String var2, MySuperID.SoLoaderCallback var3) {
        if(SoDownloadManager.isSoExists(var0)) {
            faceBundle(var0, var1, var2);
        } else {
            var3.soLoader();
        }

    }

    public static void faceBundleWithPhone(Activity var0, String var1, String var2, String var3) {
        Intent var4 = new Intent(var0, Aty_FaceDetect.class);
        var4.putExtra("intent_phonenum", var3);
        var4.putExtra("intent_userinfo", var2);
        var4.putExtra("intent_appuid", var1);
        var4.putExtra("intent_action", 2);
        var0.startActivityForResult(var4, 100);
    }

    public static void faceBundleWithPhone(Activity var0, String var1, String var2, String var3, MySuperID.SoLoaderCallback var4) {
        if(SoDownloadManager.isSoExists(var0)) {
            faceBundleWithPhone(var0, var1, var2, var3);
        } else {
            var4.soLoader();
        }

    }

    public static void faceFakeIDSignUp(Activity var0, String var1, String var2) {
        if(SuperIDUtils.appActionRight(var0, "fastSignup")) {
            Intent var3 = new Intent(var0, Aty_FaceDetect.class);
            var3.putExtra("intent_userinfo", var2);
            var3.putExtra("intent_appuid", var1);
            var3.putExtra("intent_action", 9);
            var0.startActivityForResult(var3, 100);
        } else {
            DebugMode.error("没有FakeID权限");
        }

    }

    public static void faceFakeIDSignUp(Activity var0, String var1, String var2, MySuperID.SoLoaderCallback var3) {
        if(SoDownloadManager.isSoExists(var0)) {
            faceFakeIDSignUp(var0, var1, var2);
        } else {
            var3.soLoader();
        }

    }

    public static void getFaceFeatures(Activity var0) {
        if(!Cache.getCached(var0, "app_token").equals("")) {
            Intent var1 = new Intent(var0, Aty_NormalFaceDetect.class);
            var1.putExtra("intent_action", 5);
            var0.startActivityForResult(var1, 100);
        } else {
            DebugMode.error(var0.getResources().getString(MResource.getIdByName(var0, "string", "superid_tips_emtokenerror")));
        }

    }

    public static void getFaceFeatures(Activity var0, MySuperID.SoLoaderCallback var1) {
        if(SoDownloadManager.isSoExists(var0)) {
            getFaceFeatures(var0);
        } else {
            var1.soLoader();
        }

    }

    public static void userCancelAuthorization(final Context var0, final MySuperID.IntSuccessCallback var1, final MySuperID.IntFailCallback var2) {
        String var3 = Cache.getCached(var0, "app_token");
        String var4 = Cache.getCached(var0, "access_token");
        if(!var4.equals("") && !var3.equals("")) {
            new MsdkLogout(var0, var3, var4, new com.isnc.facesdk.net.MsdkLogout.SuccessCallback() {
                public void onSuccess(int var1x) {
                    Cache.deleCached(var0, new String[]{"phone"});
                    if(var1 != null) {
                        var1.onSuccess(var1x);
                    }

                }
            }, new com.isnc.facesdk.net.MsdkLogout.FailCallback() {
                public void onFail(int var1) {
                    if(var2 != null) {
                        if(var1 == 1016) {
                            var2.onFail(117);
                        } else {
                            var2.onFail(var1);
                        }
                    }

                }
            });
        } else {
            var2.onFail(126);
            DebugMode.error(var0.getResources().getString(MResource.getIdByName(var0, "string", "superid_tips_emtokenerror")));
        }

    }

    public static void updateAppUid(Context var0, String var1, final MySuperID.IntSuccessCallback var2, final MySuperID.IntFailCallback var3) {
        String var4 = Cache.getCached(var0, "app_token");
        String var5 = Cache.getCached(var0, "access_token");
        if(!var5.equals("") && !var4.equals("")) {
            if(!TextUtils.isEmpty(var1)) {
                new MsdkUpdateAppUid(var0, var4, var5, var1, new com.isnc.facesdk.net.MsdkUpdateAppUid.SuccessCallback() {
                    public void onSuccess(int var1) {
                        if(var1 == 200) {
                            if(var2 != null) {
                                var2.onSuccess(var1);
                            } else if(var3 != null) {
                                var3.onFail(1000);
                            }
                        }

                    }
                }, new com.isnc.facesdk.net.MsdkUpdateAppUid.FailCallback() {
                    public void onFail(int var1) {
                        if(var3 != null) {
                            var3.onFail(var1);
                        }

                    }
                });
            } else {
                DebugMode.info(var0.getString(MResource.getIdByName(var0, "string", "superid_tips_uidnull")));
                if(var3 != null) {
                    var3.onFail(111);
                }
            }
        } else {
            DebugMode.info(var0.getString(MResource.getIdByName(var0, "string", "superid_tips_tokennull")));
            if(var3 != null) {
                var3.onFail(114);
            }
        }

    }

    public static void updateAppInfo(Context var0, String var1, final MySuperID.IntSuccessCallback var2, final MySuperID.IntFailCallback var3) {
        if(!TextUtils.isEmpty(var1)) {
            String var4 = Cache.getCached(var0, "app_token");
            String var5 = Cache.getCached(var0, "access_token");
            new MsdkUpdateAppInfo(var0, var4, var5, var1, new com.isnc.facesdk.net.MsdkUpdateAppInfo.SuccessCallback() {
                public void onSuccess(int var1) {
                    if(var1 == 200) {
                        if(var2 != null) {
                            var2.onSuccess(var1);
                        } else if(var3 != null) {
                            var3.onFail(1000);
                        }
                    }

                }
            }, new com.isnc.facesdk.net.MsdkUpdateAppInfo.FailCallback() {
                public void onFail(int var1) {
                    if(var3 != null) {
                        var3.onFail(var1);
                    }

                }
            });
        } else {
            DebugMode.info(var0.getString(MResource.getIdByName(var0, "string", "superid_tips_infonull")));
        }

    }

    public static void isUidAuthorized(Context var0, String var1, final MySuperID.IntSuccessCallback var2, final MySuperID.IntFailCallback var3) {
        String var4 = Cache.getCached(var0, "app_token");
        new MsdkUidAuthorized(var0, var4, var1, new com.isnc.facesdk.net.MsdkUidAuthorized.SuccessCallback() {
            public void onSuccess(int var1) {
                if(var2 != null) {
                    var2.onSuccess(var1);
                }

            }
        }, new com.isnc.facesdk.net.MsdkUidAuthorized.FailCallback() {
            public void onFail(int var1) {
                if(var3 != null) {
                    var3.onFail(var1);
                }

            }
        });
    }

    public static void isOpenIDAuthorized(Context var0, String var1, final MySuperID.IntSuccessCallback var2, final MySuperID.IntFailCallback var3) {
        String var4 = Cache.getCached(var0, "app_token");
        new MsdkOpenIDAuthorized(var0, var4, var1, new com.isnc.facesdk.net.MsdkOpenIDAuthorized.SuccessCallback() {
            public void onSuccess(int var1) {
                if(var2 != null) {
                    var2.onSuccess(var1);
                }

            }
        }, new com.isnc.facesdk.net.MsdkOpenIDAuthorized.FailCallback() {
            public void onFail(int var1) {
                if(var3 != null) {
                    var3.onFail(var1);
                }

            }
        });
    }

    public static void isFakeIDUser(Context var0, String var1, final MySuperID.IntSuccessCallback var2, final MySuperID.IntFailCallback var3) {
        String var4 = Cache.getCached(var0, "app_token");
        new MsdkFakeIDUser(var0, var4, var1, new com.isnc.facesdk.net.MsdkFakeIDUser.SuccessCallback() {
            public void onSuccess(int var1) {
                if(var2 != null) {
                    var2.onSuccess(var1);
                }

            }
        }, new com.isnc.facesdk.net.MsdkFakeIDUser.FailCallback() {
            public void onFail(int var1) {
                if(var3 != null) {
                    var3.onFail(var1);
                }

            }
        });
    }

    public static void isPhoneAuthorized(Context var0, String var1, final MySuperID.IntSuccessCallback var2, final MySuperID.IntFailCallback var3) {
        String var4 = Cache.getCached(var0, "app_token");
        new MsdkAuthorized(var0, var4, var1, (String)null, new com.isnc.facesdk.net.MsdkAuthorized.SuccessCallback() {
            public void onSuccess(int var1) {
                if(var2 != null) {
                    var2.onSuccess(var1);
                }

            }
        }, new com.isnc.facesdk.net.MsdkAuthorized.FailCallback() {
            public void onFail(int var1) {
                if(var3 != null) {
                    var3.onFail(var1);
                }

            }
        });
    }

    public static void faceLogout(Context var0) {
        Cache.deleCached(var0, new String[]{"access_token"});
        SuperIDUtils.delete(new File(SDKConfig.TEMP_PATH));
    }

    public static void setDebugMode(boolean var0) {
        DebugMode.isshow = var0;
    }

    public static void switchLanguage(Context var0, Locale var1) {
        Configuration var2 = var0.getResources().getConfiguration();
        Resources var3 = var0.getResources();
        DisplayMetrics var4 = var3.getDisplayMetrics();
        var2.locale = var1;
        var3.updateConfiguration(var2, var4);
    }

    public static void uploadUserBehaviourEvent(Context var0, String var1, JSONObject var2, JSONObject var3) {
        if(var2.length() > 0) {
            String var4 = "";
            if(var3.length() < 1) {
                var4 = String.valueOf(var2);
            } else {
                var4 = String.valueOf(var2).substring(0, String.valueOf(var2).length() - 1) + "," + String.valueOf(var3).substring(1);
            }

            String var5 = Cache.getCached(var0, "behaviour");
            JSONArray var6 = null;
            JSONObject var7 = new JSONObject();

            try {
                JSONObject var8 = new JSONObject(var4);
                if(!var5.equals("")) {
                    var6 = new JSONArray(var5);
                } else {
                    var6 = new JSONArray();
                }

                var7.put("resource_id", Cache.getCached(var0, "resource_id"));
                var7.put("tag", var1);
                var7.put("timestamp", System.currentTimeMillis() / 1000L);
                var7.put("attributes", var8);
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            var6.put(var7);
            Cache.saveCached(var0, new String[]{"behaviour", String.valueOf(var6)});
        }

    }

    public static void requestUploadData(final Context var0) {
        String var1 = Cache.getCached(var0, "access_token");
        String var2 = Cache.getCached(var0, "behaviour");
        if(!var2.equals("")) {
            int var3 = 5;

            try {
                JSONObject var4 = new JSONObject(Cache.getCached(var0, "app_action"));
                if(var4.optInt("behaviourCount") != 0) {
                    var3 = var4.optInt("behaviourCount");
                } else {
                    var3 = 5;
                }
            } catch (JSONException var7) {
                var7.printStackTrace();
            }

            try {
                JSONArray var8 = new JSONArray(var2);
                if(var8.length() >= var3) {
                    if(!TextUtils.isEmpty(var1)) {
                        new MsdkUserAddBehavior(var0, Cache.getCached(var0, "app_token"), var1, "", var2, new com.isnc.facesdk.net.MsdkUserAddBehavior.SuccessCallback() {
                            public void onSuccess(int var1) {
                                Cache.deleCached(var0, new String[]{"behaviour"});
                            }
                        }, new com.isnc.facesdk.net.MsdkUserAddBehavior.FailCallback() {
                            public void onFail(int var1) {
                            }
                        });
                    } else if(var1.equals("")) {
                        new MsdkAppAddBehavior(var0, Cache.getCached(var0, "app_token"), Cache.getCached(var0, "device_id"), "", var2, new com.isnc.facesdk.net.MsdkAppAddBehavior.SuccessCallback() {
                            public void onSuccess(int var1) {
                                Cache.deleCached(var0, new String[]{"behaviour"});
                            }
                        }, new com.isnc.facesdk.net.MsdkAppAddBehavior.FailCallback() {
                            public void onFail(int var1) {
                            }
                        });
                    }
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
        }

    }

    public interface SoLoaderCallback {
        void soLoader();
    }

    public interface IntFailCallback {
        void onFail(int var1);
    }

    public interface IntSuccessCallback {
        void onSuccess(int var1);
    }

    public interface FailCallback {
        void onFail();
    }

    public interface SuccessCallback {
        void onSuccess(String var1);
    }
}