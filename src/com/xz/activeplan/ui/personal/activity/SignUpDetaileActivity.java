package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.SignUpBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;

public class SignUpDetaileActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView callPhone, textMessage, signupName, signupPhone;
    private SignUpBean signUpBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_sign_up_detaile);
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        title.setText("会员资料");
        Intent intent =getIntent();
        if(intent!=null) {
            signUpBean = (SignUpBean) intent.getSerializableExtra("data");
            Utiles.log("signUpBean:"+signUpBean.toString());
        }
        initView();
    }

    private void initView() {
        callPhone = (TextView) findViewById(R.id.call_phone);
        textMessage = (TextView) findViewById(R.id.text_message);
        signupPhone = (TextView) findViewById(R.id.signup_phone);
        signupName = (TextView) findViewById(R.id.signup_name);
        signupPhone.setText(signUpBean.getTelphone());
        signupName.setText(signUpBean.getUsername());
        callPhone.setOnClickListener(this);
        textMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.call_phone:
                Intent telIntent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + signUpBean.getTelphone()));
                telIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(telIntent);
                break;
            case R.id.text_message:
                Intent smsIntent = new Intent();
                //系统默认的action，用来打开默认的短信界面
                smsIntent.setAction(Intent.ACTION_SENDTO);
                //需要发短息的号码
                smsIntent.setData(Uri.parse("smsto:"+signUpBean.getTelphone()));
                SignUpDetaileActivity.this.startActivity(smsIntent);
                break;
        }
    }
}
