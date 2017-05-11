package com.xz.activeplan.ui.news.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

/***
 *   名人堂 -入驻
 */
public class SettledActivity extends BaseFragmentActivity implements View.OnClickListener, BDLocationListener, ClassObserver {

    private EditText editTextIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        ClassConcrete.getInstance().addObserver(this) ;
        setContentView(R.layout.activity_settled);
        initView();

    }

    private void initView() {
        findViewById(R.id.id_ImageSettledBlack).setOnClickListener(this);
         findViewById(R.id.id_LineEditeText).setOnClickListener(this);//介绍内容页面
         editTextIntroduce = (EditText) findViewById(R.id.id_EditTextIntroduce);//介绍Content
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_LineEditeText:
                editTextIntroduce.requestFocus();
                Utiles.getKeyBoard(editTextIntroduce);
                break;
            case R.id.id_ImageSettledBlack:
                this.finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        Utiles.log("======>onStop");
        super.onStop();
    }
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClassConcrete.getInstance().removeObserver(this);
    }
    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.TEACHER_INVITE_TYPE) {
            finish();
        }
        return false;
    }
}
