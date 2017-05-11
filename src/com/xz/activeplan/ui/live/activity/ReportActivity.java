package com.xz.activeplan.ui.live.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 视频举报页面
 */
public class ReportActivity extends BaseFragmentActivity implements View.OnClickListener, TextWatcher {

    TextView tvSubmit;
    EditText etContent, etQQ;
    private int uid;
    private int liveid;
    private TextView tvChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        UserInfoBean bean = SharedUtil.getUserInfo(this);
        uid = bean.getUserid();
        liveid = getIntent().getIntExtra("liveid", 0);
        Utiles.setStatusBar(this);
        initView();
    }

    /**
     * 提交数据
     * @param content  举报内容
     * @param qq     联系方式  qq
     */
    private void submitData(String content, String qq) {
        OkHttpClientManager.Param userid = new OkHttpClientManager.Param("userid", uid + "");
        OkHttpClientManager.Param liveid = new OkHttpClientManager.Param("liveid", this.liveid + "");
        OkHttpClientManager.Param QQ = new OkHttpClientManager.Param("QQ", qq);
        OkHttpClientManager.Param describe1 = new OkHttpClientManager.Param("describe1", content);
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_REPORT_VIDEO, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showCenterToast(ReportActivity.this,"举报失败");
            }

            @Override
            public void onResponse(String response) {
                Utiles.log("report----"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    if( !TextUtils.isEmpty(msg) && msg.equals("success")) {

                        ToastUtil.showCenterToast(ReportActivity.this,"举报成功");
                        finish();
                    }else
                    {
                        ToastUtil.showCenterToast(ReportActivity.this,"举报失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },userid,liveid,QQ,describe1);


    }

    /**
     * 初始化视图
     */
    private void initView() {
        Utiles.headManager(this, "举报");
        tvSubmit = (TextView) findViewById(R.id.tvLoginAndReg);
        tvSubmit.setVisibility(View.VISIBLE);
        tvSubmit.setText("提交 ");
        tvSubmit.setOnClickListener(this);
        etContent = (EditText) findViewById(R.id.report_etContent);
        etQQ = (EditText) findViewById(R.id.report_etQQ);
        tvChange = (TextView) findViewById(R.id.report_tvChange);
        tvChange.setText("0/200");
        etContent.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == tvSubmit)   //提交
        {
            String content  = etContent.getText().toString().trim();
            String qq = etQQ.getText().toString().trim();
            if(TextUtils.isEmpty(content))
            {
                ToastUtil.showCenterToast(this,"请输出举报内容");
                return;
            }
            if(TextUtils.isEmpty(qq))
            {
                ToastUtil.showCenterToast(this,"请输出您的联系方式");
                return;
            }
            submitData(content,qq);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = s.length();
        tvChange.setText(length+"/200");
        if(length>=200)
        {
            ToastUtil.showCenterToast(this,"最多只能输入200字");
        }
    }
}
