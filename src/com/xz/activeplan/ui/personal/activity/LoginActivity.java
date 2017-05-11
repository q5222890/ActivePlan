package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.UserInfoJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.LoginServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.ClearableEditText;
import com.xz.activeplan.views.ClearableEditText.TextChange;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录界面activity
 *
 * @author johnny
 */
public class LoginActivity extends BaseFragmentActivity implements OnClickListener, TextChange, Callback, PlatformActionListener {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private TextView mTvHeadTitle, mTvLoginAndReg, mForgetPassword;
    private RelativeLayout mLlytSave_ly;
    private Button buttonLogin;
    private ClearableEditText login_user_name, login_password;
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_login_new);
        initViews();
    }

    private void initViews() {
        Utiles.headManager(this, R.string.login);
        mProgressDialog = new CustomProgressDialog(this);

        //返回框背景
        View viewHeald = findViewById(R.id.relativeLayout_toolbar);
        viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
        //返回键
        ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(this);
        //头部字体
        mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
        mTvLoginAndReg = (TextView) findViewById(R.id.tvLoginAndReg);
        mTvLoginAndReg.setVisibility(View.VISIBLE);
        mTvLoginAndReg.setTextColor(getResources().getColor(R.color.white));

        mForgetPassword = (TextView) findViewById(R.id.forget_password);  //忘记密码
        mLlytSave_ly = (RelativeLayout) findViewById(R.id.save_ly);
        findViewById(R.id.id_TextViewLoginQQ).setOnClickListener(this);
        findViewById(R.id.id_TextViewLoginWeiXin).setOnClickListener(this);
        buttonLogin = (Button) findViewById(R.id.id_ButtonLogin);
        login_user_name = (ClearableEditText) findViewById(R.id.login_user_name);
        login_password = (ClearableEditText) findViewById(R.id.login_password);
        mLlytSave_ly.setVisibility(View.VISIBLE);
        mTvHeadTitle.setText(getString(R.string.login_btn));
        mTvLoginAndReg.setText(getString(R.string.activity_right_rigister_str));
        mTvLoginAndReg.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        login_user_name.setTextChange(this);
        login_password.setTextChange(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.forget_password:   //忘记密码
                intent = new Intent(this, FindPasswordActivity.class);
                intent.putExtra("data","find");
                startActivity(intent);
                break;
            case R.id.tvLoginAndReg:
                Utiles.skipNoData(RegisterActivity.class);
                break;
            case R.id.id_ButtonLogin:
                login();
                break;
            case R.id.id_TextViewLoginQQ:
                authorize(new QZone(this));
                break;
            case R.id.id_TextViewLoginWeiXin:
                authorize(new Wechat(this));
                break;
        }

    }

    private void QZoneUserinfo(String userId,HashMap<String, Object> res) {
        //解析部分用户资料字段

        String id = res.get("nickname").toString() + res.get("province").toString() + res.get("city").toString()+res.get("gender").toString();//ID
        String name = res.get("nickname").toString();//用户名
        String profile_image_url = res.get("figureurl_1").toString();//头像链接
        String sex = res.get("gender").toString();
        thirdLogin("qq", userId, name, "男".equals(sex) ? "1" : "0", profile_image_url);
    }

    private void wexinUserinfo(String userId,HashMap<String, Object> res) {
        //解析部分用户资料字段
        Iterator iter = res.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object val =  entry.getValue();
            Utiles.log("WeChat键："+key+"; 值："+val.toString());
        }

        String id = res.get("unionid").toString();//ID
        String name = res.get("nickname").toString();//用户名
        String headimgurl = res.get("headimgurl").toString();//头像链接
        String sex = res.get("sex").toString();

        thirdLogin("weixin", userId, name, sex, headimgurl);
    }

    private void thirdLogin(final String type, String id, String name, String sex, String headimgurl) {
        Utiles.log("type" + type + "ID：" + id + "姓名：" + name + "头像Url:" + headimgurl + "性别：" + sex);
        mProgressDialog.show();
        UserInfoServiceImpl.getInstance().thirdlogin(type, id, name, sex, headimgurl, new StringCallback() {

            @Override
            public void onResponse(String response) {
                Utiles.log("登录成功response：" + response);
                mProgressDialog.dismiss();
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ToastUtil.showShort("登录成功!");
                        UserInfoJson userInfoJson = new UserInfoJson();
                        obj = userInfoJson.analysisJson2Object(statusBean.getData());
                        if (obj != null) {
                            UserInfoBean userInfoBean = (UserInfoBean) obj;
                            userInfoBean.setLoginID(type);//加入第三方登录的名称  weixin / qq
                            Utiles.log("登录成功userInfoBean：" + userInfoBean.toString());
                            if (userInfoBean != null) {
                                SharedUtil.saveUserInfo(LoginActivity.this, userInfoBean);
                                SharedUtil.saveLogin(LoginActivity.this, true, true);
                                // 登录通知发送
                                ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.LOGIN_NOTIFI_TYPE));
                                finish();
                            }
                        }
                    } else {
                        ToastUtil.showShort("登录失败，请检查账号重新登录!");
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("登录失败，请重新登录!");
                Utiles.log("错误信息 ：" + e.getMessage().toString());
                mProgressDialog.dismiss();
            }

        });
    }

    /**
     * 登录
     */
    public void login() {
        String phone = login_user_name.getText().toString().trim();
        String password = login_password.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShort("请输入电话号码!");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort("请输入密码!");
            return;
        }
        if (!Utils.checkMobileNumber(phone)) {
            ToastUtil.showShort("请输入正确的电话号码!");
            return;
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        LoginServiceImpl.getInstance().login(phone, password, new StringCallback() {

            @Override
            public void onResponse(String s) {
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(s);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ToastUtil.showShort("登录成功!");
                        UserInfoJson userInfoJson = new UserInfoJson();
                        obj = userInfoJson.analysisJson2Object(statusBean.getData());
                        if (obj != null) {
                            UserInfoBean userInfoBean = (UserInfoBean) obj;
                            Utiles.log("登录userInfoBean:" + userInfoBean.toString());
                            if (userInfoBean != null) {
                                SharedUtil.saveUserInfo(LoginActivity.this, userInfoBean);
                                SharedUtil.saveLogin(LoginActivity.this, true, false);
                                // 登录通知发送
                                ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.LOGIN_NOTIFI_TYPE));
                                finish();
                            }
                        }
                    } else {
                        ToastUtil.showShort("登录失败，请检查账号重新登录!");
                    }
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("登录失败，请重新登录!");
                mProgressDialog.dismiss();
            }
        });
    }


    @Override
    public void onTextChange() {
        String phone = login_user_name.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            buttonLogin.setBackgroundResource(R.drawable.selector_click_btn2);
            buttonLogin.setEnabled(true);
        } else {
            buttonLogin.setBackgroundResource(R.drawable.selector_wane_gray2);
            buttonLogin.setEnabled(false);
        }
    }

    private void authorize(Platform plat) {
//        isClientValid客户端是否可用（针对需要客户端分享的平台）
//        isAuthValid授权是否有效（是否有授权，或授权是否过期）
//		if(plat.isValid()) {
//			String userId = plat.getDb().getUserId();
//			if (!TextUtils.isEmpty(userId)) {
//				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//				login(plat.getName(), userId, null);
//				return;
//			}
//		}
        mProgressDialog.show();
        plat.setPlatformActionListener(this);//实现onComplete onError onCancel三个方法

        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {

        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
        System.out.println("res:"+res);
        Utiles.log("------User Name ---------" + platform.getDb().getUserName()); //userName
        Utiles.log("------User ID ---------" + platform.getDb().getUserId()); //openId
    }

    public void onError(Platform platform, int action, Throwable t) {

        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {

        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(String name, String userId, HashMap<String, Object> userInfo) {

        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = name;
        msg.obj = userId;
        UIHandler.sendMessage(msg, this);
        if (userInfo != null) {
            if (QZone.NAME.equals(name)) {
                Message m = Message.obtain();
                msg.what = 66;
                msg.obj =new Object[]{userId, userInfo};
                UIHandler.sendMessage(m, this);
            } else if (Wechat.NAME.equals(name)) {
                Message m = Message.obtain();
                msg.what = 77;
                msg.obj =new Object[]{userId, userInfo};
                UIHandler.sendMessage(m, this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case MSG_USERID_FOUND: { //匹配UserId
                ToastUtil.showShort(getResources().getString(R.string.userid_found));
            }
            break;
            case MSG_LOGIN: {  //登录
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
                String text = getString(R.string.logining, msg.obj);
                ToastUtil.showShort(text);

            }
            break;
            case MSG_AUTH_CANCEL: {  //授权取消
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.showShort(getResources().getString(R.string.auth_cancel));
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: { //授权出错
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.showShort(getResources().getString(R.string.auth_error));
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {  //完成授权
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.showShort(getResources().getString(R.string.auth_complete));
                System.out.println("--------MSG_AUTH_COMPLETE-------");
            }
            break;
            case 66:
                Object[] objs = (Object[]) msg.obj;
                String userId = (String) objs[0];
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                QZoneUserinfo(userId,res);
                break;
            case 77:
                Object[] obj = (Object[]) msg.obj;
                String  id= (String) obj[0];
                HashMap<String, Object> ress = (HashMap<String, Object>) obj[1];
                wexinUserinfo(id,ress);
                break;

        }
        return false;
    }
}
