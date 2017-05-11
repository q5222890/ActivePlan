package com.xz.activeplan.ui.recommend.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.TicketInfoBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;

/**
 * 扫脸成功页面
 */
public class ScanSuccessActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView mSuccessTvName;
    private TextView mSuccessTvPhone;
    private TextView mSuccessTvTitle;
    private TextView mSuccessTvType;
    private TextView mSuccessTvNumber;
    private Button mSuccessBtGo;
    private TicketInfoBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_scan_success);
        initData();
        initView();
    }

    /**
     * 得到扫脸的结果
     */
    private void initData() {
        bean = (TicketInfoBean) getIntent().getSerializableExtra("data");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        Utiles.headManager(this,"验票成功");
        mSuccessTvName = (TextView) findViewById(R.id.success_tvName);
        mSuccessTvPhone = (TextView) findViewById(R.id.success_tvPhone);
        mSuccessTvTitle = (TextView) findViewById(R.id.success_tvTitle);
        mSuccessTvType = (TextView) findViewById(R.id.success_tvType);
        mSuccessTvNumber = (TextView) findViewById(R.id.success_tvNumber);
        mSuccessBtGo = (Button) findViewById(R.id.success_btGo);
        mSuccessBtGo.setOnClickListener(this);

        //赋值
        mSuccessTvName.setText(bean.getRealname()+"");   //姓名
        mSuccessTvPhone.setText(bean.getTelphone()+"");  //手机号
        mSuccessTvTitle.setText(bean.getName()+"");      //活动名称
        mSuccessTvType.setText(bean.getTicket()+"");                 //票务类型
        mSuccessTvNumber.setText(bean.getNum()+"");     //购票数量
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.success_btGo:   //继续验票
                finish();
                break;
        }
    }
}
