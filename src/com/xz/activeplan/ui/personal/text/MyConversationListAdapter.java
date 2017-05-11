package com.xz.activeplan.ui.personal.text;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.xz.activeplan.utils.SharedUtil;

import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2016/6/27.
 */
public class MyConversationListAdapter extends ConversationListAdapter {
   private Context activity;

    public MyConversationListAdapter(Context activity,Context context) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        return super.newView(context, position, group);
    }

    @Override
    protected void bindView(View v, int position, UIConversation data) {
        if (data.getConversationType().equals(
                Conversation.ConversationType.DISCUSSION))
            data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);

        if(data.getConversationType().equals(
                Conversation.ConversationType.GROUP)){
            data.setUIConversationTitle(SharedUtil.get(activity, data.getConversationTargetId() + "-GROUP" ));
        }else if(data.getConversationType().equals(
                Conversation.ConversationType.PRIVATE)){
            data.setUIConversationTitle(SharedUtil.get(activity, data.getConversationTargetId() + "-PRIVATE"));
        }
        data.setIconUrl(Uri.parse(SharedUtil.getUserInfo(activity).getHeadurl())) ;
        super.bindView(v, position, data);
    }

}