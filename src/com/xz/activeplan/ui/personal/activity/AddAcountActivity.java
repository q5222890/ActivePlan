package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ClearableEditText;

public class AddAcountActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ClearableEditText EditTextname;
    private ClearableEditText EditTextacount;
    private TextView tvsubmit;
    private String name;
    private String acount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_add_acount);
        initView();
    }

    private void initView() {

        Utiles.headManager(this,R.string.addacount);
        EditTextname = (ClearableEditText) findViewById(R.id.clearedt_name);
        EditTextacount = (ClearableEditText) findViewById(R.id.clearedt_acount);
        tvsubmit = (TextView) findViewById(R.id.tv_submit);
        tvsubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.tv_submit:

                name =EditTextname.getText().toString().trim();
                acount =EditTextacount.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.showShort("请填写支付宝姓名");
                    return;
                }
                if(TextUtils.isEmpty(acount)){
                    ToastUtil.showShort("请填写支付宝账号");
                    return;
                }
                Intent intent =new Intent(this,GoAuthenticationActivity.class);
                intent.putExtra("alipayname",name);
                intent.putExtra("alipayacount",acount);
                startActivity(intent);
                finish();
                break;

        }

    }
}
