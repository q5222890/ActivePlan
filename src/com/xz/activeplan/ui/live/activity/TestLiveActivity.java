package com.xz.activeplan.ui.live.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

/**
 * 测试横竖屏切换
 */
public class TestLiveActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static PopupWindow mpopupWindow;
    TextView tvInput;
    private ImageView send;
    private EditText comment_edit;
    private ImageView tvEmoji;
    private RelativeLayout ll_facechoose;
    private ImageView ivFace;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_live);
        initView();

    }

    private void initView() {
        tvInput = (TextView) findViewById(R.id.tv_input);
        ivFace = (ImageView)findViewById(R.id.iv_face);
        ivFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ReplyBox();
                    flag  =true;
                    ll_facechoose.setVisibility(View.VISIBLE);
                    tvEmoji.setImageResource(R.drawable.inputview_icon_keyboard);
            }
        });
        tvInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyBox();
                popupInputMethodWindow();
            }
        });
        
    }

    public void ReplyBox() {
        final View view = View.inflate(this,
                R.layout.custom_facerelativelayout, null);
        comment_edit = (EditText) view.findViewById(R.id.et_sendmessage);
        ll_facechoose = (RelativeLayout)view.findViewById(R.id.ll_facechoose);
        tvEmoji = (ImageView) view.findViewById(R.id.btn_face);
        tvEmoji.setVisibility(View.VISIBLE);
        tvEmoji.setOnClickListener(this);
        send = (ImageView) view.findViewById(R.id.btn_send);
       // TextView dismiss = (TextView) view.findViewById(R.id.message_replytop);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(comment_edit.getText().toString().trim())) {
                    Toast.makeText(TestLiveActivity.this, "请填写回复内容", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    //sendTextMessage();
                    ToastUtil.showCenterToast(TestLiveActivity.this,comment_edit.getText().toString());
                }
            }
        });
       /* dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) comment_edit
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mpopupWindow.dismiss();
            }
        });*/

        view.startAnimation(AnimationUtils.loadAnimation(TestLiveActivity.this,
                R.anim.fade_in));
        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow();
            mpopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mpopupWindow.setBackgroundDrawable(new ColorDrawable());

            mpopupWindow.setFocusable(true);
            mpopupWindow.setTouchable(true);
            mpopupWindow.setOutsideTouchable(true);
            mpopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            //这行代码非常关键，如果不加上，弹出的键盘会讲mpopupWindow的布局遮挡一部分，一定要加，切记切记。。。。。
            mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        mpopupWindow.setContentView(view);
        comment_edit.requestFocus();
        comment_edit.setText("");
        comment_edit.setHint(" 我来说两句");
        comment_edit.setHintTextColor(getResources().getColor(R.color.textColor_Black));
        mpopupWindow.showAtLocation(tvInput, Gravity.BOTTOM, 0, 0);
        mpopupWindow.update();
    }

    //这里给大家一个弹出和隐藏软键盘的代码，非常实用。主要是要保持mpopupWindow和键盘同时出现和消失。
    private void popupInputMethodWindow() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) TestLiveActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_face)  //表情
        {
            //点击表情的时候切换viewPager表情布局
            Utiles.closeKeyBoard(this,comment_edit);
            ll_facechoose.setVisibility(View.VISIBLE);
            if(!flag)  //说明第一次点击了的是face
            {

                flag = true;
                tvEmoji.setImageResource(R.drawable.inputview_icon_keyboard);

            }else
            {
                flag =false;
                tvEmoji.setImageResource(R.drawable.inputview_icon_emoji);
                popupInputMethodWindow();
                ll_facechoose.setVisibility(View.GONE);
            }

            Utiles.log("dianji l ");
        }else
        {


        }
    }
}
