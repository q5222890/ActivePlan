package com.xz.activeplan.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.recommend.activity.PhotoPickerActivity;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2016/9/17.
 */
public class EditorPopUpWindow extends PopupWindow {
    private static final int EDIT_TAKING_PICTURES = 201;
    private static final int REQUEST_CODE_PICK_IMAGE = 202;
    private ImageButton actiontextsize, actioninsertimage, actioninsertvideo, actionbold, actionunderline, actiontextcolor;
    private Context context;
    private ImageView ivdeletegarbage;
    private int keyboardHeight;
    private RichEditor mEditor;
    private PopupWindow popupwindowColor;
    private int popupHeight;
    private int popupWidth;
    private PopupWindow popupwindowTextSize;
    private PopupWindow popupwindowInsertImage;
    private Activity activity;
    private String mPicPath;

    public EditorPopUpWindow(Context context,int keyboardHeight,RichEditor mEditor,Activity activity) {
        super(context);
        this.context =context;
        this.keyboardHeight =keyboardHeight;
        this.mEditor =mEditor;
        this.activity =activity;
        initView();
    }

    private void initView() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_editortoolbar, null);
        actionbold = (ImageButton) view.findViewById(R.id.action_bold);
        actionunderline = (ImageButton) view.findViewById(R.id.action_underline);
        actiontextcolor = (ImageButton) view.findViewById(R.id.action_txt_color);
        actiontextsize = (ImageButton) view.findViewById(R.id.action_txt_size);
        actioninsertimage = (ImageButton) view.findViewById(R.id.action_insert_image);
        actioninsertvideo = (ImageButton) view.findViewById(R.id.action_insert_video);

        actionbold.setOnClickListener(new onActionListener());
        actionunderline.setOnClickListener(new onActionListener());
        actiontextsize.setOnClickListener(new onActionListener());
        actiontextcolor.setOnClickListener(new onActionListener());
        actioninsertimage.setOnClickListener(new onActionListener());
        actioninsertvideo.setOnClickListener(new onActionListener());

        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setClippingEnabled(true);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        showAtLocation(activity.getCurrentFocus(), Gravity.BOTTOM, 0, keyboardHeight);
    }

    private class onActionListener implements View.OnClickListener {
        boolean isBold;
        boolean isUnderline;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_bold:
                    mEditor.setBold();
                    actionbold.setImageResource(isBold ? R.drawable.bold_gray : R.drawable.bold_blue);
                    isBold = !isBold;
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
                    selectColor(v);
                    break;
                case R.id.action_txt_size:
                    /**
                     * 选择字体大小 16 14 12
                     */
                    selectorTextSize(v);
                    break;
                case R.id.action_insert_image:  //插入图片
                    InsertImage();
                    break;
                case R.id.action_insert_video:  //插入视频
                    EditDialogFragment dialog = new EditDialogFragment();
                    dialog.show(activity.getFragmentManager(), "EditDialogFragment");
                    break;

            }
        }
    }


    /**
     * 设置字体颜色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void selectColor(View v) {

        View view = View.inflate(context, R.layout.pop_listgridview, null);

        view.findViewById(R.id.viewtxtcolor1).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor2).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor3).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor4).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor5).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor6).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor7).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor8).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor9).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor10).setOnClickListener(new txtcolorclick());

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        popupwindowColor = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupwindowColor.setTouchable(true);
        popupwindowColor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_edittextcolor));
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        popupwindowColor.showAtLocation(activity.getCurrentFocus(), Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }

    /**
     * 设置字体大小
     */
    private void selectorTextSize(View v) {
        //设置字体
        View view = View.inflate(context, R.layout.pop_textsize, null);
        TextView tv16 = (TextView) view.findViewById(R.id.textsize16);
        TextView tv14 = (TextView) view.findViewById(R.id.textsize14);
        TextView tv12 = (TextView) view.findViewById(R.id.textsize12);

        tv16.setOnClickListener(new OnTextSizeChange());
        tv14.setOnClickListener(new OnTextSizeChange());
        tv12.setOnClickListener(new OnTextSizeChange());

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        popupwindowTextSize = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupwindowTextSize.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_edittextcolor));
        popupwindowTextSize.setTouchable(true);
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        popupwindowTextSize.showAtLocation(activity.getCurrentFocus(), Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);

    }

    private class txtcolorclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.viewtxtcolor1:
                    actiontextcolor.setImageResource(R.drawable.red3);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.red3));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor2:
                    actiontextcolor.setImageResource(R.drawable.magenta);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.magenta));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor3:
                    actiontextcolor.setImageResource(R.drawable.purple);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.purple));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor4:
                    actiontextcolor.setImageResource(R.drawable.yellow2);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.yellow2));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor5:
                    actiontextcolor.setImageResource(R.drawable.darkgreen);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.darkgreen));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor6:
                    actiontextcolor.setImageResource(R.drawable.young);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.young));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor7:
                    actiontextcolor.setImageResource(R.drawable.blue2);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.blue2));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor8:
                    actiontextcolor.setImageResource(R.drawable.darkblue);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.darkblue));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor9:
                    actiontextcolor.setImageResource(R.drawable.gray2);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.gray2));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor10:
                    actiontextcolor.setImageResource(R.drawable.black2);
                    mEditor.setTextColor(ContextCompat.getColor(context, R.color.black2));
                    popupwindowColor.dismiss();
                    break;
            }
        }
    }

    private class OnTextSizeChange implements View.OnClickListener {
        boolean ischangesize;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textsize16:
                    mEditor.setTextSize(4);
                    ischangesize = !ischangesize;
                    actiontextsize.setImageResource(R.drawable.plus_textsize);
                    popupwindowTextSize.dismiss();
                    break;
                case R.id.textsize14:
                    mEditor.setTextSize(3);
                    ischangesize = !ischangesize;
                    actiontextsize.setImageResource(R.drawable.normal_textsize);
                    popupwindowTextSize.dismiss();
                    break;
                case R.id.textsize12:
                    mEditor.setTextSize(2);
                    ischangesize = !ischangesize;
                    actiontextsize.setImageResource(R.drawable.small_textsize);
                    popupwindowTextSize.dismiss();
                    break;
            }
        }
    }

    /**
     * 选择图片插入
     */
    public void InsertImage() {

        View view = View.inflate(context, R.layout.pop_insertpicture, null);
        view.findViewById(R.id.tv_photoalbum).setOnClickListener(new InsertImageListener());
        view.findViewById(R.id.tv_takepicture).setOnClickListener(new InsertImageListener());
        view.findViewById(R.id.tv_cancelselect).setOnClickListener(new InsertImageListener());
        popupwindowInsertImage = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupwindowInsertImage.setTouchable(true);
        popupwindowInsertImage.setBackgroundDrawable(context.getResources().getDrawable(R.color.all_bg));
        popupwindowInsertImage.showAtLocation(activity.getCurrentFocus(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.3f);
        popupwindowInsertImage.setOnDismissListener(new PoponDismissListener());
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    private class InsertImageListener implements View.OnClickListener {
        Intent intent;

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_takepicture:
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mPicPath = getPictureName();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(mPicPath)));
                    activity.startActivityForResult(intent, EDIT_TAKING_PICTURES);
                    popupwindowInsertImage.dismiss();
                    break;
                case R.id.tv_photoalbum:
                    activity.startActivityForResult(new Intent(context, PhotoPickerActivity.class), REQUEST_CODE_PICK_IMAGE);
                    popupwindowInsertImage.dismiss();
                    break;
                case R.id.tv_cancelselect:
                    popupwindowInsertImage.dismiss();
                    break;
            }
        }
    }

    private String getPictureName() {
        String fileRootDir = FileUtil.getRootPath() + "TempImage/";
        if (FileUtil.isFileExist(fileRootDir) == false) {
            FileUtil.createDir(fileRootDir);
        }
        Utiles.log("图片名:" + fileRootDir + UUID.randomUUID().toString() + ".jpg");
        return fileRootDir + UUID.randomUUID().toString() + ".jpg";
    }

    private class PoponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }
}
