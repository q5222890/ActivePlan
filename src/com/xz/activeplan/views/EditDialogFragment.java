package com.xz.activeplan.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xz.activeplan.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class EditDialogFragment extends DialogFragment implements View.OnClickListener {


    private String url;
    private ClearableEditText edtisnercvideo;
    private TextView cancel;
    private TextView tv_insertvideo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //必须在setContent之前设置才有效
        dialog.setContentView(R.layout.pop_insertvideo);
        dialog.setCanceledOnTouchOutside(true);
        edtisnercvideo = (ClearableEditText) dialog.findViewById(R.id.edt_insertvideo);
        cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tv_insertvideo = (TextView) dialog.findViewById(R.id.tv_insertvideo);
        tv_insertvideo.setOnClickListener(this);
        cancel.setOnClickListener(this);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_insertvideo:
                url = edtisnercvideo.getText().toString().trim();
                InputListener inputlistener = (InputListener) getActivity();
                inputlistener.onInputComplete(url);
                dismiss();

                break;
        }
    }

    public interface InputListener {
        void onInputComplete(String url);
    }
}
