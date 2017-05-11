package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ClearableEditText;

/**
 * 省份证姓名
 */
public class IDCardnameActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView idcardName;
    private ImageView ivBack;
    private ClearableEditText editText;
    private Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_idcardname);
        initView();
    }

    private void initView() {

        idcardName = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        ivBack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        editText = (ClearableEditText) findViewById(R.id.edUpdateContent);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        idcardName.setText("姓名");

        ivBack.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.updateBtn:
               String name =editText.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.showShort("请填写姓名");
                    return;
                }
                Intent intent =new Intent(this,GoAuthenticationActivity.class);
                intent.putExtra("idname",name);
                startActivity(intent);
                finish();
                break;

        }
    }
}
