package com.xz.activeplan.ui.recommend.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.recommend.activity.PostActiveActivity;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ToolDialogfragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View toolsview = inflater.inflate(R.layout.pop_editortoolbar, container);
        ImageButton actionbold = (ImageButton) toolsview.findViewById(R.id.action_bold);
        ImageButton actionunderline = (ImageButton) toolsview.findViewById(R.id.action_underline);
        ImageButton actiontextcolor = (ImageButton) toolsview.findViewById(R.id.action_txt_color);
        ImageButton actiontextsize = (ImageButton) toolsview.findViewById(R.id.action_txt_size);
        ImageButton actioninsertimage = (ImageButton) toolsview.findViewById(R.id.action_insert_image);
        ImageButton actioninsertvideo = (ImageButton) toolsview.findViewById(R.id.action_insert_video);
        ImageView ivdeletegarbage = (ImageView) toolsview.findViewById(R.id.iv_delete_garbage);

        actionbold.setOnClickListener(new onDialogActionListener());
        actionunderline.setOnClickListener(new onDialogActionListener());
        actiontextsize.setOnClickListener(new onDialogActionListener());
        actiontextcolor.setOnClickListener(new onDialogActionListener());
        actioninsertimage.setOnClickListener(new onDialogActionListener());
        actioninsertvideo.setOnClickListener(new onDialogActionListener());
        ivdeletegarbage.setOnClickListener(new onDialogActionListener());
        return toolsview;
    }

    private class onDialogActionListener implements View.OnClickListener {
        PostActiveActivity active = (PostActiveActivity) getActivity();
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.action_bold:

                    break;
                case R.id.action_underline:
                    break;
                case R.id.action_txt_color:
                    break;
                case R.id.action_txt_size:
                    break;
                case R.id.action_insert_image:
                    break;
                case R.id.action_insert_video:
                    break;
                case R.id.iv_delete_garbage:
                    break;

            }
        }
    }
}
