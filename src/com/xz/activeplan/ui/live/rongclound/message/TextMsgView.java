package com.xz.activeplan.ui.live.rongclound.message;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.MyVideoFansBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.rongclound.EmojiManager;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CircleImageView;

import java.io.IOException;

import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 文本消息
 */
public class TextMsgView extends BaseMsgView {

    TextView username;
    TextView content;
    CircleImageView header;
    LinearLayout linearLayout;
    private MyVideoFansBean fansBean;
    private UserInfo userInfo;
    public TextMsgView(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rc_text_message, this);
        username = (TextView) view.findViewById(R.id.rc_username);
        content = (TextView) view.findViewById(R.id.rc_content);
        header = (CircleImageView) view.findViewById(R.id.rc_header);
        linearLayout = (LinearLayout) view.findViewById(R.id.rc_ll);
    }

    @Override
    public void setContent(MessageContent msgContent) {
        TextMessage msg = (TextMessage) msgContent;
        //Utiles.log("消息标记："+msg.getExtra());
        userInfo = msg.getUserInfo();
        if(userInfo!=null)
        {
            username.setText(userInfo.getName() + ": ");
            Picasso.with(getContext()).load(userInfo.getPortraitUri()).fit().error(R.drawable.default_head_bg)
                    .placeholder(R.drawable.default_head_bg).into(header);
            content.setText(EmojiManager.parse(msg.getContent(), content.getTextSize()));
            if(!TextUtils.isEmpty(msg.getExtra()))
            {
                Utiles.log("信息类型："+msg.getExtra()+"---"+msg.getUserInfo().getName());
            }

            if(!TextUtils.isEmpty(msg.getExtra()) && msg.getExtra().equals("{\"type\" : 1}"))
            {
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            }else {
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
            //头像点击事件
            header.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utiles.log("用户id："+userInfo.getUserId()+"");
                    if(userInfo.getName().equals("游客"))
                    {

                    }else
                    {
                        alertDialog();
                    }
                }
            });
        }
    }

    /**
     *
     * 弹出主播的资料
     *
     */
    private void alertDialog() {
       /* getFansCount();
        View view = LayoutInflater.from(BaseFragmentActivity.activity).inflate(
                R.layout.video_alert, null);
        ImageView close = (ImageView) view.findViewById(R.id.alert_close);
        ImageView headImageView = (CircleImageView) view.findViewById(R.id.alert_circleImageView);

        TextView videoCount = (TextView) view.findViewById(R.id.alert_videoCount);  //视频数量
        TextView fansCount = (TextView) view.findViewById(R.id.alert_fansCount);    //粉丝数量
        TextView followCount = (TextView) view.findViewById(R.id.alert_followCount);//关注数量
        TextView anchorName = (TextView) view.findViewById(R.id.alert_anchorName);  //主播名字
        if (fansBean != null) {
            anchorName.setText(userInfo.getName()+"");
            videoCount.setText(fansBean.getLivecount() + "");
            fansCount.setText(fansBean.getFanscount() + "");
            followCount.setText(fansBean.getAttendcount() + "");
        }
        String msg = null;
        *//*if (liveTvBean.isIsattend() == true && null != liveTvBean) {
            msg = "已关注";
        } else {
            msg = "关注";
        }*//*
        if (!TextUtils.isEmpty(userInfo.getPortraitUri().toString()))
            Picasso.with(BaseFragmentActivity.activity).load(userInfo.getPortraitUri().toString()).placeholder(R.drawable.default_head_bg).
                    error(R.drawable.default_head_bg).fit().centerCrop().into(headImageView);
        final AnchorAlertDialog alertDialog = new AnchorAlertDialog(BaseFragmentActivity.activity, view).builder();
        final UserInfoBean userInfoBean = SharedUtil.getUserInfo(BaseFragmentActivity.activity);//当前用户id
        alertDialog.setNegativeButton("回复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(RongIM.getInstance().getCurrentConnectionStatus()== RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)
                {
                    if(userInfoBean.getUserid()==Integer.parseInt(userInfo.getUserId()))

                        ToastUtil.showCenterToast(BaseFragmentActivity.activity, "自己不能回复自己哦~");
                    else
                        RongIM.getInstance().startPrivateChat(BaseFragmentActivity.activity,userInfo.getUserId()+"",userInfo.getName()+"");
                }
            }
        })
                .setPositiveButton("关注", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });*/
    }

    /**
     * 点击发布者头像获取该发布者的粉丝数量
     */
    private void getFansCount() {
        if (null != userInfo && userInfo.getUserId().length() > 0) {
            OkHttpClientManager.Param userid1 = new OkHttpClientManager.Param("userid", userInfo.getUserId() + "");
            OkHttpClientManager.getInstance();
            Utiles.log("----userid--"+userInfo.getUserId());
            OkHttpClientManager.postAsyn(UrlsManager.URL_VIDEO_FANSCOUNT, new MyStringCallBack(), userid1);
        }
    }


    class MyStringCallBack implements OkHttpClientManager.StringCallback {

        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String response) {
            Utiles.log("-----------fans-----------" + response);
            try {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0)   //返回状态
                {
                    fansBean = JSON.parseObject(statusBean.getData(), MyVideoFansBean.class);
                }
            } catch (Exception e) {
                Utiles.log("----未知错误-----");
            }
        }
    }
}
