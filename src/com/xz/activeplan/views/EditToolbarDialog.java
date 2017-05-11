package com.xz.activeplan.views;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xz.activeplan.R;

/**
 * Created by Administrator on 2016/8/13.
 */
public class EditToolbarDialog extends DialogFragment {


    private View view;
    private ImageButton actionbold,actionunderline,actiontextcolor,actiontextsize,actioninsertimage,actioninsertvideo;
    private ImageView deletegarbage;
    private RichEditor mEditor;
    private boolean isUnderline;
    private boolean isBold;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.pop_editortoolbar,container,false);

        initView();
        return view;
    }

    private void initView() {
        mEditor =new RichEditor(getActivity());
        actionbold = (ImageButton) view.findViewById(R.id.action_bold);
        actionunderline = (ImageButton) view.findViewById(R.id.action_underline);
        actiontextcolor = (ImageButton) view.findViewById(R.id.action_txt_color);
        actiontextsize = (ImageButton) view.findViewById(R.id.action_txt_size);
        actioninsertimage = (ImageButton) view.findViewById(R.id.action_insert_image);
        actioninsertvideo = (ImageButton) view.findViewById(R.id.action_insert_video);
        deletegarbage = (ImageView) view.findViewById(R.id.iv_delete_garbage);

        actionbold.setOnClickListener(new onActionListener());
        actionunderline.setOnClickListener(new onActionListener());
        actiontextsize.setOnClickListener(new onActionListener());
        actiontextcolor.setOnClickListener(new onActionListener());
        actioninsertimage.setOnClickListener(new onActionListener());
        actioninsertvideo.setOnClickListener(new onActionListener());
        deletegarbage.setOnClickListener(new onActionListener());

    }

    private class onActionListener implements View.OnClickListener {
        
         @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_bold:
                    actionbold.setImageResource(isBold ? R.drawable.bold_gray : R.drawable.bold_blue);
                    isBold = !isBold;
                    mEditor.setBold();
                    dismiss();
                    break;
                case R.id.action_underline:
                    mEditor.setUnderline();
                    
                    actionunderline.setImageResource(isUnderline ? R.drawable.underline_gray : R.drawable.underline_blue);
                    isUnderline = !isUnderline;
                    break;

                case R.id.action_txt_color:
                    /**
                     * 选择字体颜色 #C10000 #FF4C40 #920783 #FF9C00 #407600  #408080 #0070C1 #004080 #808080 #000000
                     */
//                    selectColor(v);
                    break;
                case R.id.action_txt_size:
                    /**
                     * 选择字体大小 16 14 12
                     */
//                    selectorTextSize(v);
                    break;
                case R.id.action_insert_image:  //插入图片
//                    InsertImage(v);
                    break;
                case R.id.action_insert_video:
                    EditDialogFragment dialog = new EditDialogFragment();
                    dialog.show(getFragmentManager(), "EditDialogFragment");
                    break;

                case R.id.iv_delete_garbage:
                    mEditor.setHtml("");
                    break;

            }
        }
    }


}
