package com.xz.activeplan.ui.live.view;


import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CircleImageView;

/**
 * 主播的Dialog
 */
@SuppressWarnings(value = {"all"})
public class AnchorAlertDialog {
    View view;
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private Display display;
    private float size;
    private Button btn_follow, btn_reply;
    private boolean flag;
    private CircleImageView mAlertCircleImageView;
    private TextView mAlertAnchorName;
    private ImageView mAlertClose;
    private RelativeLayout mVideoAlertRl;
    private TextView mAlertVideoCount;
    private TextView mAlertFansCount;
    private TextView mAlertFollowCount;
    private LinearLayout mIdLinePersonMessage;
    private Button mBtnNeg;
    private ImageView mImgLine;
    private Button mBtnPos;
    private LinearLayout mLLayoutBg;

    public AnchorAlertDialog(Context context, float size) {
        this.context = context;
        this.size = size;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AnchorAlertDialog builder() {
        // 获取Dialog布局
        view = LayoutInflater.from(context).inflate(
                R.layout.video_alert, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * size), LayoutParams.WRAP_CONTENT));
        return this;
    }

    /**
     * 布局
     */
    private void setLayout() {
        mAlertAnchorName= (TextView)view.findViewById(R.id.alert_anchorName);
        mAlertVideoCount = (TextView) view.findViewById(R.id.alert_videoCount);
        mAlertFansCount = (TextView) view.findViewById(R.id.alert_fansCount);
        mAlertFollowCount = (TextView) view.findViewById(R.id.alert_followCount);
        mAlertCircleImageView = (CircleImageView) view.findViewById(R.id.alert_circleImageView);
        mAlertClose = (ImageView) view.findViewById(R.id.alert_close);
        mAlertClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
        btn_follow = (Button) view.findViewById(R.id.btn_follow);  //关注
        btn_reply = (Button)view.findViewById(R.id.btn_reply);  //回复
    }

    /**
     * 设置主播相关信息
     * @param anchorName  主播名
     * @param videoCount  视频数量
     * @param fansCount   粉丝数量
     * @param followCount 关注数量
     * @param headUrl     主播头像
     */
    public void setAnchorInfo(String anchorName,int videoCount, int fansCount, int followCount, String headUrl) {
        mAlertAnchorName.setText(anchorName+"");
        mAlertVideoCount.setText(videoCount + "");
        mAlertFansCount.setText(fansCount + "");
        mAlertFollowCount.setText(followCount + "");
        Picasso.with(context).load(headUrl).fit().error(R.drawable.default_head_bg)
                .placeholder(R.drawable.default_head_bg).into(mAlertCircleImageView);

    }

    /**
     * 设置关注样式
     * @param isSelected
     * ps:未关注为  false
     */
    public void setIsFollowStatus(boolean isSelected) {
        btn_follow.setSelected(isSelected);
        Utiles.log("字体颜色："+isSelected);
        if(isSelected==false)
        {
            btn_follow.setTextColor(context.getResources().getColor(R.color.white));
            Utiles.log("字体颜色："+isSelected+"   白");
            btn_follow.setText("关注");

        }else
        {
            btn_follow.setTextColor(context.getResources().getColor(R.color.textColor));
            btn_follow.setText("已关注");
        }
    }

    public void setFollowButtonTextColor(int resColorId) {
        btn_follow.setTextColor(resColorId);
    }
    /**
     * 设置外部是否可以点击
     *
     * @param cancel
     * @return
     */
    public AnchorAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 回复
     */
    public AnchorAlertDialog setReplyBotton(String text,
                                               final OnClickListener listener) {
        btn_reply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);

            }
        });
        return this;
    }

    /**
     *
     *关注
     */
    public AnchorAlertDialog setFollowBotton(String text,
                                               final OnClickListener listener) {
        btn_follow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }


    /**
     * 显示
     */
    public void show() {

        setLayout();
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭
     */
    public void cancel() {
        dialog.cancel();
    }

    /**
     *
     */
    public void dismiss() {
        dialog.dismiss();
    }
}

