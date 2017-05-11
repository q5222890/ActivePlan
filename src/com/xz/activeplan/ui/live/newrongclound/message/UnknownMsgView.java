package com.xz.activeplan.ui.live.newrongclound.message;

import android.content.Context;
import android.view.LayoutInflater;

import com.xz.activeplan.R;

import io.rong.imlib.model.MessageContent;

/**
 * 未知消息
 */
public class UnknownMsgView extends BaseMsgView {

    public UnknownMsgView(Context context) {
        super(context);                                 //msg_unknown_view
       LayoutInflater.from(getContext()).inflate(R.layout.msg_unknown_view, this);
    }

    @Override
    public void setContent(MessageContent msgContent) {
    }
}
