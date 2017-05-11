package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.AuthenticationInfoBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import java.io.IOException;

/**
 * 认证页面
 *
 */
public class AuthenticationActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener {

    private TextView mTvHeadTitle;
    private Button goButton;
    private int isadopt = 1; // 1：待审核；2：审核通过； 3：审核失败
    private AuthenticationInfoBean infobean;
    private UserInfoBean mUserInfoBean;
    private ImageView img_ImageHeadPhoto;
    private TextView tvMineNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassConcrete.getInstance().addObserver(this) ;
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_authentication);
        initView();
    }

    private void initView() {
        //返回键
        ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(this);
        //头部字体
        mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        mTvHeadTitle.setText(getResources().getString(R.string.string_Authentication));
        img_ImageHeadPhoto = (ImageView) findViewById(R.id.img_ImageHeadPhoto);
        tvMineNickName = (TextView) findViewById(R.id.tvMineNickName);
        goButton = (Button) findViewById(R.id.id_ButAuthentication);
        goButton.setOnClickListener(this);
        loadAuthenticationData();
    }

    private void loadAuthenticationData() {
        if(!NetworkInfoUtil.checkNetwork(this)){
            ToastUtil.showShort("网络异常！请检查网络");
        }
        mUserInfoBean = SharedUtil.getUserInfo(this);
        Utiles.log("mUserInfoBean.getUserid():"+mUserInfoBean.getUserid());
        UserInfoServiceImpl.getInstance().getAuthenticStatus(
                mUserInfoBean.getUserid(), new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("获取认证信息失败！");
                    }

                    @Override
                    public void onResponse(String response) {
                        Utiles.log("人症状太response :::"+response);
                        if (response != null) {
                            StatusBean statusbean = JSON.parseObject(response, StatusBean.class);
                            if (statusbean.getCode() == 0) {
                                infobean =JSON.parseObject(statusbean.getData(),AuthenticationInfoBean.class);
                                Utiles.log("认证状态statusbean.getData()::: "+statusbean.getData());

                                if(infobean!=null){
                                    if(infobean.getIsadopt()==1){
                                        Utiles.log("待审核");
                                        tvMineNickName.setText(R.string.waittoauditing);
                                        goButton.setText(R.string.waittoauditing);
                                        goButton.setBackgroundResource(R.color.gray);
                                        goButton.setOnClickListener(null);
                                    }else if(infobean.getIsadopt()==2){
                                        Utiles.log("审核通过");
                                        tvMineNickName.setText("已实名认证");
                                        goButton.setVisibility(View.GONE);
                                        img_ImageHeadPhoto.setImageResource(R.drawable.authenstatus);
                                    }else if(infobean.getIsadopt()==3){
                                        Utiles.log("审核失败");
                                        tvMineNickName.setText("审核未通过!请重新提交认证");
                                    }
                                }
                            }else if (statusbean.getCode() == 1) {
                                Utiles.log("还未提交审核");
                                tvMineNickName.setText("还未提交认证");
                            }

                        } else {
                            ToastUtil.showShort("获取认证信息失败！");
                        }
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClassConcrete.getInstance().removeObserver(this) ;
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean)data ;
        if(event.getType() == EventType.ACCOUNT_TIXIANE_TYPE){
            finish();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.id_ButAuthentication:
                if(mUserInfoBean!=null&&!TextUtils.isEmpty(mUserInfoBean.getPhonenum())) {
                    Utiles.skipNoData(GoAuthenticationActivity.class);
                }else{
                    Utils.showBingPhone(AuthenticationActivity.this);
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadAuthenticationData();
    }
}
