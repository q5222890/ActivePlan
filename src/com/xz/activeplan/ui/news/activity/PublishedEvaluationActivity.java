package com.xz.activeplan.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评价页面
 */
public class PublishedEvaluationActivity extends BaseFragmentActivity implements ClassObserver {
    @Bind(R.id.view_top)
    View viewTop;
    @Bind(R.id.id_ImageHeadTitleBlack)
    ImageView idImageHeadTitleBlack;
    @Bind(R.id.id_TextViewHeadTitle)
    TextView idTextViewHeadTitle;
    @Bind(R.id.id_ImageHead)
    ImageButton idImageHead;
    @Bind(R.id.two_img)
    ImageView twoImg;
    @Bind(R.id.share_ly)
    LinearLayout shareLy;
    @Bind(R.id.tvLoginAndReg)
    TextView tvLoginAndReg;
    @Bind(R.id.save_ly)
    LinearLayout saveLy;
    @Bind(R.id.relativeLayout_toolbar)
    RelativeLayout relativeLayoutToolbar;
    @Bind(R.id.id_EditTextEvaluationContent)
    EditText idEditTextEvaluationContent;
    @Bind(R.id.id_LineEvaluation)
    LinearLayout idLineEvaluation;
    @Bind(R.id.id_ImageStarOne)
    ImageView idImageStarOne;
    @Bind(R.id.id_ImageStarTwo)
    ImageView idImageStarTwo;
    @Bind(R.id.id_ImageStarThree)
    ImageView idImageStarThree;
    @Bind(R.id.id_ImageStarFour)
    ImageView idImageStarFour;
    @Bind(R.id.id_ImageStarFive)
    ImageView idImageStarFive;
    @Bind(R.id.id_TextViewSendEvaluate)
    TextView idTextViewSendEvaluate;
    private OderDeatilBean oderBean;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_published_evaluation);
        ButterKnife.bind(this);
        ClassConcrete.getInstance().addObserver(this);
        WindowManager wm = getWindowManager();
        int height = wm.getDefaultDisplay().getHeight()/3;
        idLineEvaluation.setMinimumHeight(height);
        Utiles.headManager(this, R.string.string_SendEvaluate);
        Utiles.setVisibility(saveLy);
        tvLoginAndReg.setText(getResources().getString(R.string.string_Skip));
        tvLoginAndReg.setTextSize(17);
        tvLoginAndReg.setTextColor(getResources().getColor(R.color.white));
        oderBean = (OderDeatilBean) getIntent().getSerializableExtra("data");
        idEditTextEvaluationContent.setText("许长安评价测试！");
    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }
    @OnClick({R.id.id_ImageHeadTitleBlack, R.id.id_TextViewSendEvaluate, R.id.id_LineEvaluation,R.id.tvLoginAndReg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_TextViewSendEvaluate:
                if (TextUtils.isEmpty(idEditTextEvaluationContent.getText().toString())) {
                    ToastUtil.showShort(getResources().getString(R.string.string_EvaluateNull));
                    return;
                }
                carryData();
                break;
            case R.id.id_LineEvaluation://出现键盘
                Utiles.getKeyBoard(idEditTextEvaluationContent, "open");
                break;
            case R.id.tvLoginAndReg://跳过
                UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok==null){
                    return;
                }
                CelebrityNewWorkImpl.getInstance().skip(oderBean.getInv_id(), oderBean.getTea_id(), new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Utiles.loadFailed();
                    }
                    @Override
                    public void onResponse(String response) {
                        Utiles.log("  跳过 PublishedEvaluationActivity:"+response);
                        jsonData(response);
                    }
                });
                break;
        }
    }

    /**
     * 发表评论去后台
     */
    private void carryData() {
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok==null){
            return;
        }
        CelebrityNewWorkImpl.getInstance().carryCelebriteEvaluation(4, idEditTextEvaluationContent.getText().toString(),
                oderBean, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Utiles.loadFailed();
                    }
                    @Override
                    public void onResponse(String response) {
                        jsonData(response);
                    }
    });

    }
                /**
                 * 解析Json
                 * @param response
                 */
    private void jsonData(String response) {
        Utiles.log(this.getClass().getName()+"  "+response);
        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
        if (statusBean.getCode() == 0) {
            if (statusBean.getData() != null) {
                List<OderDeatilBean> oderDeatilBeen = JSON.parseArray(statusBean.getData(), OderDeatilBean.class);
                if (oderDeatilBeen.size() != 0) {
                    oderBean = oderDeatilBeen.get(0);
                    intent = new Intent();
                    bundle = new Bundle();
                    bundle.putSerializable("data", oderBean);
                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();
                }
            } else {
                ToastUtil.showShort("加载数据失败！");
            }
        }
    }
}
