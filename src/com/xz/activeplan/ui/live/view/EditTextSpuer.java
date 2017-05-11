package com.xz.activeplan.ui.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xz.activeplan.ui.live.test.LiveChatRoomFragment;
import com.xz.activeplan.ui.live.test.VodChatRoomFragment;

/**
 * Created by Administrator on 2016/7/21.
 */
public class EditTextSpuer extends EditText{
    InputMethodManager imm;
    public EditTextSpuer(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER ) {
            Log.v("xpf","--------------------+onKeyDown---------enter");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public boolean onKeyPreIme (int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN ) {
            //调用了fragment中的静态方法，关闭mpopupWindow和键盘，这里最为关键。
            LiveChatRoomFragment.hiddenpopupWindow();
            VodChatRoomFragment.hiddenpopupWindow();
            Log.v("xpf","--------------------+onKeyPreIme---------back");
            return true;
        }

        return super.onKeyPreIme(keyCode, event);
    }

}
