package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 脸部识别——开始拍摄
 */
public class FaceDetectionStartTakeActivity extends BaseFragmentActivity implements View.OnClickListener {


    public static final int RESULT_CODE = 0x12;
    private Button btStart;
    private TextView tvPhone;
    private UserInfoBean userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection_start_take);
        Utiles.setStatusBar(this);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        Utiles.headManager(this, "脸部识别");

        btStart = (Button) findViewById(R.id.take_btStart);
        btStart.setOnClickListener(this);
        tvPhone = (TextView) findViewById(R.id.take_tvPhone);
        userInfo = SharedUtil.getUserInfo(this);
        String phonenum = userInfo.getPhonenum();
        String number = Utils.formatMobileNumber(phonenum);
        tvPhone.setText(number+""); //账号为当前用户账号
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_btStart:  //开始拍摄
                String info = SuperID.formatInfo(this,"name",userInfo.getUsername());
                SuperID.faceLoginWithPhoneUid(this,userInfo.getPhonenum(),info,"");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperID.faceLogout(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case SDKConfig.LOGINSUCCESS:
                //<!-- openid 为开发者应用的openid，若用户在调用faceLogin(Context context)进行注册授权，则系统将会自动生成一个openid，重新登录成功时返回此openid -->
               String openid = Cache.getCached(this, SDKConfig.KEY_OPENID);
               // <!-- userInfo 为SuperID用户信息，格式为json -->
               String userInfo = Cache.getCached(this, SDKConfig.KEY_APPINFO);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Utiles.log("用户信息："+userInfo);
                /***
                 * 用户信息：{"phone":"15820419680","name":"1233211234567","avatar":"https:\/\/dn-spapi1.qbox.me\/avatar\/2016\/08\/31\/z3fo1kkqxxs3spjh.jpg","regioncode":"86","persona":{"gender":"male","tags":["eyeglasses"],"location":{"country":"CN","province":"广东","city":"深圳"},"generation":"80s","character":"mature"},"group_uid":"c13cf124c5855c94ea6126de476d5328","server_request_token":"4a98b40d5a16e87fbb7ddef222b8b9db"}

                 */
                saveUserInfo(userInfo);
                break;
            case SDKConfig.LOGINFAIL:
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 将用户信息保存到服务器
     * @param userInfo   一登录入成功回调
     */
    private void saveUserInfo(String userInfo) {

        String token ="";
        try {
            JSONObject object = new JSONObject(userInfo);
            token = object.getString("server_request_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClientManager.Param userid = new OkHttpClientManager.Param("userid",this.userInfo.getUserid()+"");
        OkHttpClientManager.Param request_token = new OkHttpClientManager.Param("request_token",token+"");
        OkHttpClientManager.postAsyn(UrlsManager.URL_SuperID_INFO, new OkHttpClientManager.StringCallback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log("失败");
            }

            @Override
            public void onResponse(String response) {
                Utiles.log("一登录入结果："+response);
                /**
                 * 结果保存在UserInforBean  thirdid 字段
                 */
                StatusBean statusBean = JSON.parseObject(response,StatusBean.class);
                if(statusBean.getCode()==0)  //录入成功
                {
                      UserInfoBean bean  = JSON.parseObject(statusBean.getData(),UserInfoBean.class);
                      SharedUtil.saveUserInfo(FaceDetectionStartTakeActivity.this,bean);
                    String thirdid = bean.getThirdid();
                    if(!TextUtils.isEmpty(thirdid))
                    {
                       // ToastUtil.showCenterToastToLong(FaceDetectionStartTakeActivity.this,"人脸录入成功");
                        Intent intent = new Intent();
                        intent.putExtra("data","success");
                        setResult(RESULT_CODE,intent);
                        finish();
                    }

                }
            }
        },userid,request_token);
    }
}
