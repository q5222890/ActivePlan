package com.xz.activeplan.ui.recommend.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utils;

/**
 * Created by Administrator on 2016/8/29.
 */
public class SponsorInfoActivity extends BaseFragmentActivity implements View.OnClickListener {

    private EditText realname, company,phone, address;
    private TextView tvSecondNext;
    private TextView title;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sponsorinfo);
        title =(TextView)findViewById(R.id.id_TextViewHeadTitle);
        title.setText(R.string.setsponsors);
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        initView();
    }

    private void initView() {

        realname = (EditText) findViewById(R.id.ed_realname);
        company = (EditText) findViewById(R.id.ed_company);
        phone = (EditText) findViewById(R.id.ed_phone);
        address = (EditText) findViewById(R.id.ed_address);
        tvSecondNext = (TextView) findViewById(R.id.tvSecondNext);
        tvSecondNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;

            case R.id.tvSecondNext:
                //先判断是否为空 不为空跳至支付页
                checkInfoPay();
                break;
        }
    }

    private void checkInfoPay() {
        String name =realname.getText().toString().trim();
        String gongsi =company.getText().toString().trim();
        String number =phone.getText().toString().trim();
        String add =address.getText().toString().trim();

        if (TextUtils.isEmpty(number)) {
            ToastUtil.showShort("请输入手机号码!");
            return;
        }
        if (Utils.checkMobileNumber(number) == false) {
            ToastUtil.showShort("请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShort("请输入姓名!");
            return;
        }
        if (TextUtils.isEmpty(gongsi)) {
            ToastUtil.showShort("请输入公司名称!");
            return;
        }


    }
}
