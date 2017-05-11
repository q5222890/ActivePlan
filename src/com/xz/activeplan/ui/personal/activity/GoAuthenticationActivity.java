package com.xz.activeplan.ui.personal.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePopupWindow;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class GoAuthenticationActivity extends BaseFragmentActivity implements View.OnClickListener {

    private RelativeLayout idcardphto, idcardname, idcardborndate, idcardNo;
    private TextView addalipayacount, submit;
    private TimePopupWindow timePopupWindow;
    private String bornDate = "";
    private TextView tv_borandate;
    private ImageView id_ImageHeadTitleBlack;
    private String name;
    private UserInfoBean mUserInfoBean;
    private String idNo;
    private String alipayname;
    private String alipayAcount;
    private TextView edtname, edtidnum;
    private TextView tvalipayname, tvalipayacount;
    private String frontpath;
    private String backpath;
    private ImageView ivfrontphoto;
    private ImageView ivbackphoto;
    private CustomProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_goauthentication);
        initView();
    }

    private void initView() {
        Utiles.headManager(this, R.string.string_shiming);
        id_ImageHeadTitleBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        idcardphto = (RelativeLayout) findViewById(R.id.relative_idcardphoto);
        idcardname = (RelativeLayout) findViewById(R.id.relative_idcardname);
        idcardborndate = (RelativeLayout) findViewById(R.id.relative_borndate);
        idcardNo = (RelativeLayout) findViewById(R.id.relative_idcardNo);
        submit = (TextView) findViewById(R.id.tv_submit);
        addalipayacount = (TextView) findViewById(R.id.tv_addalipayacount);
        tv_borandate = (TextView) findViewById(R.id.tv_borandate);
        ivfrontphoto = (ImageView) findViewById(R.id.ivfrontphoto);
        ivbackphoto = (ImageView) findViewById(R.id.ivbackphoto);
        edtname = (TextView) findViewById(R.id.tv_edtname);
        edtidnum = (TextView) findViewById(R.id.tv_edtidnum);

        tvalipayname = (TextView) findViewById(R.id.tv_alipayname);
        tvalipayacount = (TextView) findViewById(R.id.tv_alipayacount);

        idcardphto.setOnClickListener(this);
        idcardname.setOnClickListener(this);
        idcardborndate.setOnClickListener(this);
        idcardNo.setOnClickListener(this);
        submit.setOnClickListener(this);
        addalipayacount.setOnClickListener(this);
        id_ImageHeadTitleBlack.setOnClickListener(this);
        timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        progressDialog =new CustomProgressDialog(this);
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("idname");
            idNo = intent.getStringExtra("idno");
            alipayname = intent.getStringExtra("alipayname");
            alipayAcount = intent.getStringExtra("alipayacount");

            Utiles.log("name:" + name);
            Utiles.log("idNo:" + idNo);
            Utiles.log("alipayname:" + alipayname);
            Utiles.log("alipayAcount:" + alipayAcount);
        }

        if (!TextUtils.isEmpty(name)) {
            edtname.setText(name);
            edtname.setTextColor(ContextCompat.getColor(GoAuthenticationActivity.this, R.color.txt_gray));
        }
        if (!TextUtils.isEmpty(idNo)) {
            edtidnum.setText(idNo);
            edtidnum.setTextColor(ContextCompat.getColor(GoAuthenticationActivity.this, R.color.txt_gray));
        }
        if (!TextUtils.isEmpty(alipayname) && !TextUtils.isEmpty(alipayAcount)) {
            LinearLayout lineaddacount = (LinearLayout) findViewById(R.id.line_addacount);
            lineaddacount.setVisibility(View.VISIBLE);
            tvalipayname.setText(alipayname);
            tvalipayacount.setText(alipayAcount);
        }
        frontpath = getSharedPreferences("fronttemp", MODE_PRIVATE).getString("front", "");
        Utiles.log("frontpath:" + frontpath);
        backpath = getSharedPreferences("backtemp", MODE_PRIVATE).getString("back", "");
        Utiles.log("backpath:" + backpath);

        Uri fronturi = Uri.fromFile(new File(frontpath));
        Picasso.with(this).load(fronturi).placeholder(R.drawable.thumb).error(R.drawable.thumb).into(ivfrontphoto);
        Uri backuri = Uri.fromFile(new File(backpath));
        Picasso.with(this).load(backuri).placeholder(R.drawable.thumb).error(R.drawable.thumb).into(ivbackphoto);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initIntent();
    }

    private void setListener(final TextView view) {
        //时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onTimeSelect(Date date) {
                //格式化时间
                bornDate = TimeUtils.FormatsimpleDate(date);
                //得到当前系统时间
                Utiles.log("出生日期：" + bornDate);
                view.setText(bornDate);
                view.setTextColor(ContextCompat.getColor(GoAuthenticationActivity.this, R.color.txt_gray));
            }
        });

        timePopupWindow.setFocusable(true);
        backgroundAlpha(0.3f);
        timePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.relative_idcardphoto:
                Utiles.skipNoData(IDcardPhotoActivity.class);
                break;
            case R.id.relative_idcardname:
                Utiles.skipNoData(IDCardnameActivity.class);
                break;
            case R.id.relative_borndate:
                timePopupWindow.showAtLocation(submit, Gravity.BOTTOM, 0, 0, new Date());
                setListener(tv_borandate);
                break;
            case R.id.relative_idcardNo:
                Utiles.skipNoData(IDcardNoActivity.class);
                break;

            case R.id.tv_addalipayacount:
                Utiles.skipNoData(AddAcountActivity.class);
                break;
            case R.id.id_ImageHeadTitleBlack:  //提示是否退出认证
                View view = View.inflate(this, R.layout.dialog_yesorno, null);
                AlertDialog dialog = new AlertDialog(this, view, 0.85f).builder();
                dialog.show();
                view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
                view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.txt_msg)).setText("是否放弃实名认证?");
                dialog.setPositiveButton("继续认证", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                dialog.setNegativeButton("放弃", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                break;
            case R.id.tv_submit: //提交
                progressDialog.show();
                postAuthentication();
                break;
        }
    }

    private void postAuthentication() {

        String idName = edtname.getText().toString().trim();
        String idNum = edtidnum.getText().toString().trim();
        String alipayusername = tvalipayname.getText().toString().trim();
        String alipayacount = tvalipayacount.getText().toString().trim();

        initIntent();

        if (!NetworkInfoUtil.checkNetwork(this)) {
            progressDialog.dismiss();
            ToastUtil.showShort("网络连接失败！，请重试");
        }

        if (TextUtils.isEmpty(frontpath)) {
            progressDialog.dismiss();
            ToastUtil.showShort("身份证正面照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(backpath)) {
            progressDialog.dismiss();
            ToastUtil.showShort("身份证背面照片不能为空");
            return;
        }
        if (TextUtils.isEmpty(idName)) {
            progressDialog.dismiss();
            ToastUtil.showShort("姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(bornDate)) {
            progressDialog.dismiss();
            ToastUtil.showShort("出生日期不能为空");
            return;
        }
        if (TextUtils.isEmpty(idNum)) {
            progressDialog.dismiss();
            ToastUtil.showShort("身份证号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(alipayusername)) {
            progressDialog.dismiss();
            ToastUtil.showShort("支付宝姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(alipayacount)) {
            progressDialog.dismiss();
            ToastUtil.showShort("支付宝账号不能为空");
            return;
        }

        long birthday = Long.parseLong(bornDate);
        mUserInfoBean = SharedUtil.getUserInfo(this);
        Utiles.log("用户id:" + mUserInfoBean.getUserid());
        Utiles.log("前照:" + frontpath);
        Utiles.log("后照:" + backpath);
        Utiles.log("name:" + idName);
        Utiles.log("birthday:" + birthday);
        Utiles.log("idNo:" + idNum);
        Utiles.log("alipayname:" + alipayusername);
        Utiles.log("alipayAcount:" + alipayacount);
        UserInfoServiceImpl.getInstance().submitAnthentication(
                mUserInfoBean.getUserid(), frontpath, backpath, idName, birthday, idNum,
                alipayusername, alipayacount, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        progressDialog.dismiss();
                        ToastUtil.showShort(getString(R.string.submit_fail));
                        Utiles.log("----request:" + request + "      ----e:" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response != null) {
                            StatusBean statusbean = JSON.parseObject(response, StatusBean.class);
                            if (statusbean.getCode() == 0) {
                                ToastUtil.showShort(getString(R.string.submit_sussesful));
                                finish();
                            } else {
                                ToastUtil.showShort(getString(R.string.submit_fail));
                            }
                        } else {
                            ToastUtil.showShort(getString(R.string.submit_fail));
                        }
                    }
                }
        );
    }
}
