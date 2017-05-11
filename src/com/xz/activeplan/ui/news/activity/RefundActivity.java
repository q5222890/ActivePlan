package com.xz.activeplan.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退款页面
 */
public class RefundActivity extends BaseFragmentActivity implements ClassObserver {
    @Bind(R.id.id_EditRefundReason)   //退款原因（线型布局）
    EditText idEditRefundReason;
    @Bind(R.id.id_EditRefundAmount)
    EditText idEditRefundAmount;
    @Bind(R.id.id_EditRefundInstructionsss)
    EditText idEditRefundInstructions;
    @Bind(R.id.id_LineRefundInstructions)
    LinearLayout idLineRefundInstructions;
    @Bind(R.id.id_TextViewSubmitRefund)
    TextView idTextViewSubmitRefund;
    @Bind(R.id.id_LineUserMessage)
    LinearLayout idLineUserMessage;
    @Bind(R.id.relativeLayout_mid)
    LinearLayout relativeLayoutMid;
    @Bind(R.id.id_ImageMore001)
    ImageView idImageMore;
    private OderDeatilBean oderBean;
    private Intent intent;
    private Bundle bundle;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
        ClassConcrete.getInstance().addObserver(this);
        Utiles.headManager(this, R.string.string_ApplyForARefund);
        oderBean = (OderDeatilBean) getIntent().getSerializableExtra("data");
        idEditRefundReason.setText("许长安退款测试标题");
        idEditRefundAmount.setText(5000+"");
        idEditRefundInstructions.setText("许长安退款测试原因");
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


    /**
     * 非空判断
     *
     * @param editText
     * @param strUnll
     */
    private boolean isEmpty(TextView editText, String strUnll, String strHint) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setHint(strUnll);
            editText.setHintTextColor(getResources().getColor(R.color.red2));
        } else {
            editText.setHint(strHint);
            editText.setHintTextColor(getResources().getColor(R.color.gary5));
            return true;
        }
        return false;
    }

    private boolean getJudgmentCondition() {
        boolean Reason01 = isEmpty(idEditRefundReason, getResources().getString(R.string.title_Refund01),
                getResources().getString(R.string.title_RefundReason01));
        boolean Reason02 = isEmpty(idEditRefundAmount, getResources().getString(R.string.title_Refund02),
                getResources().getString(R.string.title_RefundReason02));
        /**
         * 判断是不是为数字
         */
        int i = Utiles.judgingString(idEditRefundAmount.getText().toString());
        if (i!=1){
        ToastUtil.showShort(getResources().getString(R.string.string_PleaseEnterAmount));
            return false;
        }
        boolean Reason03 = isEmpty(idEditRefundInstructions, getResources().getString(R.string.title_Refund03),
                getResources().getString(R.string.title_RefundReason03));
         if (Reason01 == true && Reason02 == true && Reason03 == true) {
            return true;
        } else {
            return false;
        }
    }
    @OnClick({R.id.id_LineRefundInstructions, R.id.id_TextViewSubmitRefund,R.id.id_ImageMore001})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_ImageMore001:
                showRefundDialog();
                break;
            case R.id.id_LineRefundInstructions:
                Utiles.getKeyBoard(idEditRefundInstructions,"open");
                break;
            case R.id.id_TextViewSubmitRefund:
                boolean judgmentCondition = getJudgmentCondition();
                if (judgmentCondition != true) {
                    return;
                }
                UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok==null){
                    return;
                }
                final CustomProgressDialog progressDialog = new CustomProgressDialog(this);
                progressDialog.show();
                CelebrityNewWorkImpl.getInstance().refund(oderBean.getInv_id(), idEditRefundReason.getText().toString(),
                        idEditRefundAmount.getText().toString(),
                        idEditRefundInstructions.getText().toString(), new OkHttpClientManager.StringCallback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Utiles.loadFailed();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                                if (statusBean.getCode() == 0) {
                                    if (statusBean.getData() != null) {
                                        List<OderDeatilBean> oderDeatilBeen = JSON.parseArray(statusBean.getData(), OderDeatilBean.class);
                                        if (oderDeatilBeen.size() != 0) {
                                            intent = new Intent();
                                            bundle = new Bundle();
                                            bundle.putSerializable("data", oderDeatilBeen.get(0));
                                            intent.putExtras(bundle);
                                            setResult(2, intent);
                                            finish();
                                        }
                                    } else {
                                        ToastUtil.showShort("加载数据失败！");
                                    }
                                }
                            }
                        });
                break;
        }
    }

    /**
     * 弹出退款说明的对话框
     */
    private void showRefundDialog() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_refund, null);   //退款视图
        final TextView msg1 = (TextView) view.findViewById(R.id.txt_msg1);
        final TextView msg2 = (TextView) view.findViewById(R.id.txt_msg2);
        final TextView msg3= (TextView) view.findViewById(R.id.txt_msg3);
        alertDialog = new AlertDialog(this, view).builder();
        alertDialog.show();
        alertDialog.setTitle("请选择退款原因")
                .show();
        msg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                idEditRefundReason.setText(msg1.getText().toString());
            }
        });
        msg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                idEditRefundReason.setText(msg2.getText().toString());

            }
        });
        msg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                idEditRefundReason.setText(msg3.getText().toString());

            }
        });

    }
}
