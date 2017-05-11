package com.xz.activeplan.ui.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

/**
 * 谁可以看 页面
 * （主要分为 公开、付费、密码）
 */
public class CanSeeActivity extends BaseFragmentActivity implements View.OnClickListener {

    private  static int  tag = 1;
    boolean  flag =true;
    EditText etPass,etMoney;
    private ImageView iv_datails_back;
    private TextView tvHeadTitle;
    private LinearLayout llGongkai,llFufei,llMima,llFufeiItem,llMimaItem;
    private RelativeLayout ll_save;
    private TextView tvSave;
    private int money =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_can_see);
        initView();
    }
    /**
     * 初始化试图
     */
    private void initView() {
        iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        ll_save = (RelativeLayout) findViewById(R.id.save_ly);
        tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        tvHeadTitle.setText("谁可以看");
        ll_save.setVisibility(View.VISIBLE);

        tvSave = (TextView) ll_save.getChildAt(0).findViewById(R.id.tvLoginAndReg);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText("完成");

        llFufei = (LinearLayout) findViewById(R.id.see_ll_fufei);
        llFufeiItem = (LinearLayout) findViewById(R.id.see_ll_fufei_item);
        llGongkai = (LinearLayout) findViewById(R.id.see_ll_gongkai);
        llMima = (LinearLayout) findViewById(R.id.see_ll_mima);
        llMimaItem = (LinearLayout) findViewById(R.id.see_ll_mima_item);
        etPass  = (EditText) findViewById(R.id.see_etPass);
        etMoney = (EditText)findViewById(R.id.see_etMoney);
        iv_datails_back.setOnClickListener(this);
        llFufei.setOnClickListener(this);
        llGongkai.setOnClickListener(this);
        llMima.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        findViewById(R.id.see_iv_check1).setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_ImageHeadTitleBlack:   //返回
                finish();
                break;
            case R.id.see_ll_fufei:     //付费
                tag =2;
                findViewById(R.id.see_iv_check1).setVisibility(View.INVISIBLE);
                findViewById(R.id.see_iv_check2).setVisibility(View.VISIBLE);
                findViewById(R.id.see_iv_check3).setVisibility(View.INVISIBLE);
                findViewById(R.id.see_iv_arrow1).setSelected(true);
                findViewById(R.id.see_iv_arrow2).setSelected(false);
                llMimaItem.setVisibility(View.GONE);
                if(flag)
                {
                    flag =false;
                    llFufeiItem.setVisibility(View.VISIBLE);
                    findViewById(R.id.see_iv_arrow1).setSelected(true);
                }else
                {
                    flag = true;
                    llFufeiItem.setVisibility(View.GONE);
                    findViewById(R.id.see_iv_arrow1).setSelected(false);
                }
                break;
            case R.id.see_ll_gongkai:   //公开
                tag =1;
                findViewById(R.id.see_iv_check1).setVisibility(View.VISIBLE);
                findViewById(R.id.see_iv_check2).setVisibility(View.INVISIBLE);
                findViewById(R.id.see_iv_check3).setVisibility(View.INVISIBLE);
                break;
            case R.id.see_ll_mima:    // 密码
                tag = 3;
                findViewById(R.id.see_iv_check1).setVisibility(View.INVISIBLE);
                findViewById(R.id.see_iv_check2).setVisibility(View.INVISIBLE);
                findViewById(R.id.see_iv_check3).setVisibility(View.VISIBLE);
                llFufeiItem.setVisibility(View.GONE);
                findViewById(R.id.see_iv_arrow2).setSelected(true);
                findViewById(R.id.see_iv_arrow1).setSelected(false);
                if(flag)
                {
                    flag =false;
                    llMimaItem.setVisibility(View.VISIBLE);
                    findViewById(R.id.see_iv_arrow2).setSelected(true);
                }else
                {
                    flag = true;
                    llMimaItem.setVisibility(View.GONE);
                    findViewById(R.id.see_iv_arrow2).setSelected(false);
                }
                break;
            case R.id.tvLoginAndReg:   //完成  （将用户选择的信息传到）
                finishChoice();
                break;
        }
    }

    /**
     * 选择谁可以看
     */
    private void finishChoice() {
        switch (tag)
        {
            case 1:
                money = 0;
                break;
            case 2:         //付费
                if("".equals(etMoney.getText().toString().trim()))
                {
                    ToastUtil.showShort("金额不能为空");
                    return;
                }
                int money= Integer.parseInt(etMoney.getText().toString());
                if(money<1 || money>500)
                {
                    ToastUtil.showShort("输入的金额不符合要求");
                    return;
                }
                this.money= money;
                break;
            case 3:          //密码
                if(TextUtils.isEmpty(etPass.getText().toString().trim()) || etPass.getText().toString().trim().length()!=6)
                {
                    ToastUtil.showShort("请输入6位数密码");
                    return;
                }
                this.money = Integer.parseInt(etPass.getText().toString().trim());
                break;
        }
        Intent intent = new Intent();
        intent.putExtra("money",money);
        setResult(102,intent);
        finish();
    }
}
