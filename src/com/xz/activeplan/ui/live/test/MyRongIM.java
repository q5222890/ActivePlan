package com.xz.activeplan.ui.live.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.xz.activeplan.ui.live.test.template.BaseMessageTemplate;
import com.xz.activeplan.ui.live.test.template.TemplateTag;
import com.xz.activeplan.ui.live.test.template.TextMessageTemplate;

import java.util.HashMap;
import java.util.Map;

import io.rong.eventbus.EventBus;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

;

public class MyRongIM{

    private static final String TAG = "RongIM";

    private static MyRongIM instance;
    private static Context gContext;

    private EventBus eventBus;
    private Map<Class<? extends MessageContent>, BaseMessageTemplate> msgTmpMap = new HashMap<Class<? extends MessageContent>, BaseMessageTemplate>();
    private UserInfoProvider userInfoProvider;
    private UserInfo currentUserInfo;

    private MyRongIM() {
        eventBus = EventBus.getDefault();
        registerMessageTemplate(new TextMessageTemplate(gContext));
        registerMessageEvent();

    }

    public static void init(Context context, String appKey) {
        gContext = context.getApplicationContext();
        RongIMClient.init(context, appKey);
    }

    public static void init(Context context) {
        gContext = context.getApplicationContext();
        RongIMClient.init(context);
    }

    public static MyRongIM getInstance() {
        if (instance == null && gContext != null) {
            instance = new MyRongIM();
        }
        return instance;
    }

    public void connect(String token, final RongIMClient.ConnectCallback callback) {
        RongIMClient.connect(token, callback);
    }

    public void logout() {
        currentUserInfo = null;
        RongIMClient.getInstance().logout();
    }

    public void startConversation(Context context, Conversation.ConversationType conversationType, String targetId) {
        Uri uri = Uri.parse("rong://" + context.getApplicationInfo().processName).buildUpon()
                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build();
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void registerMessageType(Class<? extends MessageContent> messageContentClass) {
        try {
            RongIMClient.registerMessageType(messageContentClass);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerMessageTemplate(BaseMessageTemplate template) {
        TemplateTag tag = template.getClass().getAnnotation(TemplateTag.class);
        msgTmpMap.put(tag.messageContent(), template);
    }

    public BaseMessageTemplate getMessageTemplate(Class<? extends MessageContent> messageContent) {
        return msgTmpMap.get(messageContent);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setUserInfoProvider(UserInfoProvider provider) {
        userInfoProvider = provider;
    }

    public UserInfo getCurrentUserInfo() {
        if (currentUserInfo == null) {
            if (userInfoProvider != null) {
                currentUserInfo = userInfoProvider.getUserInfo(RongIMClient.getInstance().getCurrentUserId());
            } else {
                currentUserInfo = new UserInfo(RongIMClient.getInstance().getCurrentUserId(), RongIMClient.getInstance().getCurrentUserId(), Uri.parse(""));
            }
        }
        return currentUserInfo;
    }

    public void sendMessage(final Message msg) {
        RongIMClient.SendMessageCallback callback = new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                eventBus.post(new BusEvent.MessageSent(msg, 0));
            }

            @Override
            public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                eventBus.post(new BusEvent.MessageSent(msg, e.getValue()));
            }
        };
        RongIMClient.ResultCallback<Message> result = new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
            }
        };
        RongIMClient.getInstance().sendMessage(msg, null, null, callback, result);
    }

    private void registerMessageEvent() {
        RongIMClient.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int left) {
                Log.d(TAG, "onReceived left = " + left);
                eventBus.post(new BusEvent.MessageReceived(message, left));
                return false;
            }
        });
    }
    public interface UserInfoProvider {
        UserInfo getUserInfo(String userId);
    }

}
