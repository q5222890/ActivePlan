package com.xz.activeplan.ui.live.rongclound.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.live.rongclound.EmojiManager;

import io.rong.imlib.model.MessageContent;

public class GiftMsgView extends BaseMsgView {

    private TextView username;
    private TextView content;

    public GiftMsgView(Context context) {
        super(context);                                             //msg_gif_view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.msg_info_view, this);
        username = (TextView) view.findViewById(R.id.username);
       content = (TextView) view.findViewById(R.id.content);
    }

    @Override
    public void setContent(MessageContent msgContent) {
        GiftMessage msg = (GiftMessage) msgContent;
        username.setText(msg.getUserInfo().getName() + " ");
        content.setText(EmojiManager.parse(msg.getContent(), content.getTextSize()));
    }
}
