package com.xz.activeplan.ui.recommend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.EditInputFilter;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

/**
 * 活动票务设置界面activity
 *
 * @author johnny
 */
public class ActiveTicketctivity extends BaseFragmentActivity implements OnClickListener, OnCheckedChangeListener {

    private ImageView iv_datails_back;

    private TextView tvHeadTitle;

    private EditText ed_ticket_name, ed_money, ed_intro, ed_personnum;
    private CheckBox ck_yes_momey, ck_is_check;
    private Button btn_add;
    private int mNum = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_post_ticket);
        initViews();

    }

    private void initViews() {

        iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);

        ed_ticket_name = (EditText) findViewById(R.id.ed_ticket_name);
        ed_money = (EditText) findViewById(R.id.ed_money);
//        InputFilter[] filter = {new CashierInputFilter()};
        InputFilter[] filter = {new EditInputFilter()};
        ed_money.setFilters(filter);

        ed_intro = (EditText) findViewById(R.id.ed_intro);
        ed_personnum = (EditText) findViewById(R.id.ed_personnum);
        ck_yes_momey = (CheckBox) findViewById(R.id.ck_yes_momey);
        ck_is_check = (CheckBox) findViewById(R.id.ck_is_check);
        btn_add = (Button) findViewById(R.id.btn_add);

        tvHeadTitle.setText("添加票务");
        iv_datails_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        ck_yes_momey.setOnCheckedChangeListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            mNum = intent.getIntExtra("data", 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.btn_add:
                add();
                break;
        }
    }

    private void add() {
        String name = ed_ticket_name.getText().toString();
        String money = ed_money.getText().toString();
        String intro = ed_intro.getText().toString();
        String num = ed_personnum.getText().toString();

        if(money.equals("0.0")||money.equals("0.00")||money.equals("0")){
            ToastUtil.showShort("请输入正确收费金额格式");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShort("请输入票务名称!");
            return;
        }

        if (ck_yes_momey.isChecked() && TextUtils.isEmpty(money)) {
            ToastUtil.showShort("请输入收费金额!");
            return;
        }

        if (TextUtils.isEmpty(intro)) {
            ToastUtil.showShort("请输入票务说明!");
            return;
        }

        if (TextUtils.isEmpty(num)) {
            ToastUtil.showShort("请输入人数限制");
            return;
        }

        try {
            int total = Integer.parseInt(num);
            if (mNum < total) {
                ToastUtil.showShort("超过参加活动人数限制，最多只能设置" + mNum + "人");
                return;
            }
        } catch (Exception e) {
            ToastUtil.showShort("请输入正确人数限制");
            return;
        }

        TicketAddBean bean = new TicketAddBean();
        bean.setIntro(intro);
        bean.setIscheck(false);

        bean.setName(name);
        try {
            bean.setPersonnum(Integer.parseInt(num));
        } catch (Exception e) {
            ToastUtil.showShort("请输入正确人数格式");
            return;
        }
        if (ck_yes_momey.isChecked()) {
            bean.setType(2);  //收费
            try {
                bean.setMoney(Float.parseFloat(money));
            } catch (Exception e) {
                ToastUtil.showShort("请输入正确收费金额格式");
                return;
            }
        } else {
            bean.setType(1);  //免费
            bean.setMoney(0);
        }
        EventBean event = new EventBean(EventType.ACTIVITE_TICKET_ADD);
        event.setObj(bean);

        ClassConcrete.getInstance().postDataUpdate(event);

        finish();

    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        if (arg1) {
            ed_money.setVisibility(View.VISIBLE);
        } else {
            ed_money.setVisibility(View.GONE);
        }
    }
}
