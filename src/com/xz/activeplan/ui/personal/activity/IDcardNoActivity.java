package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ClearableEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDcardNoActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ClearableEditText edUpdateContent;
    private Button updateBtn;
    private String idNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_idcard_no);

        initView();
    }

    private void initView() {
        Utiles.headManager(this, R.string.idcardno);
        edUpdateContent = (ClearableEditText) findViewById(R.id.edUpdateContent);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateBtn:
                idNo = edUpdateContent.getText().toString().trim();
                if (TextUtils.isEmpty(idNo)) {
                    ToastUtil.showShort("请输入身份证号");
                    return;
                }else {
                    Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
                    Matcher idNumMatcher = idNumPattern.matcher(idNo);
                    Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日
                    Matcher birthDateMatcher = birthDatePattern.matcher(idNo);
                    //判断用户输入是否为身份证号
                    if (!birthDateMatcher.matches()) {
                        ToastUtil.showShort("请输入正确的身份证号");
                        return;
                    }
                }
                Intent intent = new Intent(this, GoAuthenticationActivity.class);
                intent.putExtra("idno", idNo);
                startActivity(intent);
                finish();
                break;
        }
    }
}
