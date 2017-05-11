package com.xz.activeplan.ui.live.newrongclound.message;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.live.newrongclound.EmojiManager;
import com.xz.activeplan.views.CircleImageView;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 文本消息
 */
public class TextMsgView extends BaseMsgView {

    TextView username;
    TextView content;
    CircleImageView header;
    LinearLayout linearLayout;

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
        username.setText(msg.getUserInfo().getName() + ": ");
        Picasso.with(getContext()).load(msg.getUserInfo().getPortraitUri()).fit().error(R.drawable.default_head_bg)
        .placeholder(R.drawable.default_head_bg).into(header);
        content.setText(EmojiManager.parse(msg.getContent(), content.getTextSize()));
        if(!TextUtils.isEmpty(msg.getExtra()) && msg.getExtra().equals("{type:'1'}"))
        {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            linearLayout.setOrientation(LinearLayout.VERTICAL);
        }

    }
}
