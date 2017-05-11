package com.xz.activeplan.ui.personal.text;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.provider.TextMessageItemProvider;
import io.rong.message.TextMessage;

/**
 * 自定义会话页面
 * Created by Administrator on 2016/6/25.
 */
@ProviderTag( messageContent = TextMessage.class , showPortrait = true ,showSummaryWithName =true )
public class MyTextMessageItemProvider extends TextMessageItemProvider{
}
