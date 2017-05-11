package com.xz.activeplan.ui.live.rongclound.message;

import android.content.Context;
import android.widget.RelativeLayout;

import io.rong.imlib.model.MessageContent;

/**
 * 消息基类
 */
public abstract class BaseMsgView extends RelativeLayout {

    public BaseMsgView(Context context) {
        super(context);
    }

    public abstract void setContent(MessageContent msgContent);
}
