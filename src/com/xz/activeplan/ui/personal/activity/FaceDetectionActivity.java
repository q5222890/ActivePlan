package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

/**
 * 脸部识别
 */
public class FaceDetectionActivity extends BaseFragmentActivity implements View.OnClickListener {


    private static final int REQUEST_CODE = 0x1200;  //请求码
    private Button face_btInput;
    private TextView tvContent;
    private String thirid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        Utiles.setStatusBar(this);
        thirid= SharedUtil.getUserInfo(this).getThirdid();
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        Utiles.headManager(this, "脸部识别");
        face_btInput = (Button) findViewById(R.id.face_btInput);
        tvContent=  (TextView) findViewById(R.id.face_tv) ;
        face_btInput.setOnClickListener(this);
        if (TextUtils.isEmpty(thirid))  //未录入
        {
            tvContent.setText(R.string.face_inputText);
        }else
        {
            tvContent.setText(R.string.faece_inputedText);//已经录入
            face_btInput.setText("已录入");
            face_btInput.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_btInput:  //确定录入
                Utiles.skipNoData(FaceDetectionStartTakeActivity.class);
                Intent intent = new Intent(this,FaceDetectionStartTakeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            String data1 = data.getStringExtra("data");
            if(data1.equals("success"))
            {
                //ToastUtil.showCenterToast(this,"");
                tvContent.setText(R.string.faece_inputedText);//已经录入
                face_btInput.setText("您已成功录入");
                //face_btInput.setEnabled(false);
                // 成功的通知发送
                ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.INPUT_FACE_SUCCESS));
                finish();
            }
        }
    }
}
