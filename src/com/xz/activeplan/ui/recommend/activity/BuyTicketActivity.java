package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
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
 * 购票界面activity  填写报名表单
 *
 * @author johnny
 */
public class BuyTicketActivity extends BaseFragmentActivity implements OnClickListener, ClassObserver {
    private ImageView iv_datails_back;
    private TextView tvHeadTitle, tvSecondNext;
    private EditText ed_phone, ed_weixin, ed_zhiye, ed_gs, ed_realname;
    private ActiveinfosBean mActiveinfosBean;
    private TicketAddBean bean;
    private UserInfoBean mUserInfoBean;
    private View view;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
//        Utiles.setStatusBar(this);
        ClassConcrete.getInstance().addObserver(this);
        setContentView(R.layout.activity_buy_ticket_second);
        view =findViewById(R.id.view_top);
        view.setVisibility(View.GONE);
        initViews();
    }

    private void initViews() {
        iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        tvSecondNext = (TextView) findViewById(R.id.tvSecondNext);

        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_weixin = (EditText) findViewById(R.id.ed_weixin);
        ed_zhiye = (EditText) findViewById(R.id.ed_zhiye);
        ed_gs = (EditText) findViewById(R.id.ed_gs);
        ed_realname = (EditText) findViewById(R.id.ed_realname);
        if (SharedUtil.isLogin(this)) {
            mUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            ToastUtil.showShort("请登录用户!");
            finish();
            return;
        }

        Intent intent = getIntent();
        if (intent != null) {
            mActiveinfosBean = (ActiveinfosBean) intent.getSerializableExtra("data");
            bean = (TicketAddBean) intent.getSerializableExtra("TicketAddBean");
        }

        tvHeadTitle.setText("填写报名表单");
        iv_datails_back.setOnClickListener(this);
        tvSecondNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.tvSecondNext:
                if (!checkInput()) {  //检查输入是否正确
                    return;
                }

                Utiles.log("票务类型：" + bean.getType());
                Intent intent = null;
                if (bean.getType() == 1) {  //判断是收费票或免费票
                    //如果是免费票
                    //intent = new Intent(BuyTicketActivity.this,PaySuccess2Activity.class);z
                    payfree(mActiveinfosBean.getActiveid());
                    return;
                } else if (bean.getType() == 2) {

                    paymoney(mActiveinfosBean.getActiveid());
                    intent = new Intent(BuyTicketActivity.this, SureOrderActivity.class);
                }
                intent.putExtra("data", mActiveinfosBean);
                intent.putExtra("TicketAddBean", bean);
                intent.putExtra("telphone", ed_phone.getText().toString().trim());
                intent.putExtra("weixin", ed_weixin.getText().toString().trim());
                intent.putExtra("company", ed_zhiye.getText().toString().trim());
                intent.putExtra("position", ed_gs.getText().toString().trim());
                intent.putExtra("realname", ed_realname.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }

    private boolean checkInput() {
        String phone = ed_phone.getText().toString().trim();
        String weixin = ed_weixin.getText().toString().trim();
        String zhiye = ed_zhiye.getText().toString().trim();
        String gs = ed_gs.getText().toString().trim();
        String realname = ed_realname.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShort("请输入手机号码!");
            return false;
        }
        if (Utils.checkMobileNumber(phone) == false) {
            ToastUtil.showShort("请输入正确的手机号码!");
            return false;
        }
        if (TextUtils.isEmpty(realname)) {
            ToastUtil.showShort("请输入姓名!");
            return false;
        }

        return true;
    }

    //免费票报名
    private void payfree(int activeid) {


        Utiles.log("免费票报名");
        String phone = ed_phone.getText().toString().trim();
        String weixin = ed_weixin.getText().toString().trim();
        String position = ed_zhiye.getText().toString().trim();
        String company = ed_gs.getText().toString().trim();
        String realname = ed_realname.getText().toString().trim();

        ActiveServiceImpl.getInstance().applyActive(mUserInfoBean.getUserid(),
                activeid, "", 0, 0, phone, weixin, company, position,
                bean.getId() + "", realname, new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            Utiles.log("免费票statusBean:" + statusBean);
                            if (statusBean.getCode() == 0) {

                                Utiles.log("statusBean.getData():" + statusBean.getData());
                                ClassConcrete.getInstance().postDataUpdate(
                                        new EventBean(EventType.ACCOUNT_BAOMING_TYPE));
                                Intent intent = new Intent(BuyTicketActivity.this, PaySuccess2Activity.class);
                                intent.putExtra("data", mActiveinfosBean);
                                intent.putExtra("TicketAddBean", bean);
                                BuyTicketActivity.this.startActivity(intent);
                                finish();
                            } else {
                                ToastUtil.showShort("报名失败1，请重试!");
                            }
                        } else {
                            ToastUtil.showShort("报名失败2，请重试!");
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("报名失败3，请重试!");
                    }
                });
    }

    private void paymoney(int activeid) {
        Utiles.log("收费票报名，进入订单页");
        String phone = ed_phone.getText().toString().trim();
        String weixin = ed_weixin.getText().toString().trim();
        String position = ed_zhiye.getText().toString().trim();
        String company = ed_gs.getText().toString().trim();
        String realname = ed_realname.getText().toString().trim();

        Utiles.log("活动金额:" + bean.getMoney());
        ActiveServiceImpl.getInstance().applychargeActive(mUserInfoBean.getUserid(),
                activeid, "", bean.getMoney(), 0, phone, weixin, company, position,
                bean.getId() + "", realname, new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {

                                Utiles.log("提交成功");
                                Utiles.log("介是收费票success:" + statusBean.toString());
                            } else {
                                Utiles.log("介是收费票fail:" + statusBean.toString());
                                return;
                            }
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                        return;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClassConcrete.getInstance().removeObserver(this);
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.ACCOUNT_BAOMING_TYPE) {
            finish();
        }
        return false;
    }
}
