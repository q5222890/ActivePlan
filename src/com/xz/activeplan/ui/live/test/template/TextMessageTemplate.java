package com.xz.activeplan.ui.live.test.template;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.xz.activeplan.ui.live.adapter.FaceConversionUtil;
import com.xz.activeplan.utils.LogUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.views.CircleImageView;

import java.io.IOException;

import io.rong.imkit.model.UIMessage;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 文本消息
 */
@TemplateTag(messageContent = TextMessage.class)
public class TextMessageTemplate implements BaseMessageTemplate, View.OnClickListener {
    private final static String TAG = "TextMessageTemplate";
    public android.view.View.OnClickListener listener;
    private Context mContext;
    private MyVideoFansBean fansBean;
    private UserInfo info;
    private String headURL;
    public TextMessageTemplate(Context context) {
        this.mContext = context;
    }

    @Override
    public View getView(View convertView, int position, ViewGroup parent, UIMessage data) {
        Log.e(TAG, "getView " + position + " " + convertView);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_text_message, null);
            holder.username = (TextView) convertView.findViewById(R.id.rc_username);
            holder.content = (TextView) convertView.findViewById(R.id.rc_content);
            holder.header = (CircleImageView) convertView.findViewById(R.id.rc_header);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.rc_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            //convertView.clearFocus();
        }
        Message msg = data.getMessage();
        info = msg.getContent().getUserInfo();
        TextMessage textMsg = (TextMessage) msg.getContent();
       // LogUtil.show(TAG, "---------消息内容" + textMsg.getContent().toString());
        if(textMsg.getContent().toString().contains("进来了"))
        {
            holder.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else
        {
            holder.linearLayout.setOrientation(LinearLayout.VERTICAL);
        }
       /* if (!TextUtils.isEmpty(textMsg.getExtra()))     //
        {
            if (textMsg.getExtra().toString().equals("{\"type\":1}")) {
                holder.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LogUtil.show(TAG, "---------userinfo" + textMsg.getExtra().toString());
            } else
            {
              //  holder.linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }*/
        if (info != null) {
//            Utiles.log("info"+info.getName());
            if (Utils.checkMobileNumber(info.getName())) {
                String maskNumber = Utils.formatMobileNumber(info.getName());
                holder.username.setText(maskNumber + ": ");
            } else {
                if (info.getName().length() > 11) {
                    String substring = info.getName().substring(0, 10);
                    holder.username.setText(substring + "... : ");
                } else {
                    holder.username.setText(info.getName() + ": ");
                }
            }
        } else {
            holder.username.setText(msg.getSenderUserId() + ": ");
        }
        //发送消息
        if (msg.getMessageDirection() == Message.MessageDirection.SEND) {
//            if (!TextUtils.isEmpty(msg.getExtra()) && msg.getExtra().equals("{\"type\":1}"))     //
//            {
//                holder.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                LogUtil.show(TAG, "---------userinfo" + msg.getExtra().toString());
//            }
            if(info !=null) {
                headURL = info.getPortraitUri().toString();
            }
            if (!"".equals(headURL)) {
                Picasso.with(mContext).load(headURL).placeholder(R.drawable.default_head_bg).
                        error(R.drawable.default_head_bg).fit().centerCrop().into(holder.header);
            }
            holder.username.setTextColor(parent.getContext().getResources().getColor(R.color.gray));
            //接受消息
        } else if (msg.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            holder.username.setTextColor(parent.getContext().getResources().getColor(R.color.gray));
            if(null !=msg.getContent().getUserInfo())
            if(!TextUtils.isEmpty(msg.getContent().getUserInfo().getPortraitUri().toString()))
            {
                headURL = msg.getContent().getUserInfo().getPortraitUri().toString();
                LogUtil.show(TAG, "---------" + headURL);
                Picasso.with(mContext).load(headURL).placeholder(R.drawable.default_head_bg).
                        error(R.drawable.default_head_bg).fit().centerCrop().into(holder.header);
            }
        }
        SpannableString spannableString = FaceConversionUtil.getInstace().
                getExpressionString(mContext, textMsg.getContent().toString());
        holder.content.setText(spannableString);

       // holder.header.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onItemClick(View view, int position, UIMessage data) {
        Utiles.log("textMessage--------"+position);
    }

    @Override
    public void onItemLongClick(View view, int position, UIMessage data) {

    }

    @Override
    public void destroyItem(ViewGroup group, Object template) {

    }


    public void setOnClickListener(android.view.View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rc_header:
               /* if (listener != null) {
                    listener.onClick(v);
                }*/
                alertDialog();
                break;
        }
    }

    /**
     * 弹出主播的资料
     */
    private void alertDialog() {
/*        getFansCount();
        View view = LayoutInflater.from(ConversationActivity.getInstance).inflate(
                R.layout.video_alert, null);
        ImageView close = (ImageView) view.findViewById(R.id.alert_close);
        ImageView headImageView = (CircleImageView) view.findViewById(R.id.alert_circleImageView);

        TextView videoCount = (TextView) view.findViewById(R.id.alert_videoCount);  //视频数量
        TextView fansCount = (TextView) view.findViewById(R.id.alert_fansCount);    //粉丝数量
        TextView followCount = (TextView) view.findViewById(R.id.alert_followCount);//关注数量
        TextView anchorName = (TextView) view.findViewById(R.id.alert_anchorName);  //主播名字
        if (fansBean != null) {
            anchorName.setText(info.getName()+"");
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
        if (!TextUtils.isEmpty(headURL))
            Picasso.with(ConversationActivity.getInstance).load(headURL).placeholder(R.drawable.default_head_bg).
                    error(R.drawable.default_head_bg).fit().centerCrop().into(headImageView);
        final UserInfoBean userInfo = SharedUtil.getUserInfo(ConversationActivity.getInstance);
        final AnchorAlertDialog  alertDialog = new AnchorAlertDialog(ConversationActivity.getInstance, view).builder();
        alertDialog.setNegativeButton("回复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (null != info && info.getUserId().length() > 0) {
            OkHttpClientManager.Param userid1 = new OkHttpClientManager.Param("userid", info.getUserId() + "");
            OkHttpClientManager.getInstance();
            Utiles.log(TAG+"----userid--"+info.getUserId());
            OkHttpClientManager.postAsyn(UrlsManager.URL_VIDEO_FANSCOUNT, new MyStringCallBack(), userid1);
        }
    }

    private class ViewHolder {
        TextView username;
        TextView content;
        CircleImageView header;
        LinearLayout linearLayout;
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
