package com.xz.activeplan.ui.personal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.text.StringUtils;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;


/***
 * 我的消息通知
 */
public class MessageActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener {
    private View viewSystemSingRed,  viewGroupSingRed, viewPrivateSingRed;
    private TextView textViewSystemMessage, textViewSystemTime, textViewPrivateMessage, textViewGroupTime,
            textViewPrivateTime, textViewGroupMessage;
    private MessageActivity activity;
    private ArrayList<Conversation> conversations;
    private Date date;
    private TextView textViewNoMessage;
    private View viewSystem, viewSystemView,viewGroup, viewGroupView, viewPrivate, viewPirvateView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    getMessageType(1);
                    getSystemMessageContent();
                    break;
                case 2:
                    getGroupMessageContent();
                    getMessageType(2);
                    break;
                case 3:
                    getPrivateMessageContent();
                    getMessageType(3);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_person_message);
        ClassConcrete.getInstance().addObserver(this);
        initView();
        Utiles.x=-1;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_LineSystem://系统消息
                EventType.CONVERSATION_TYPE= Conversation.ConversationType.SYSTEM;
                if (RongIM.getInstance() != null) {
                    Utiles.log("开启系统回话界面");
                    //系统会话
                    RongIM.getInstance().startConversation(activity, Conversation.ConversationType.SYSTEM, "82", "系统消息");
                }
                break;
            case R.id.id_LinePrivate://群聊
                EventType.CONVERSATION_TYPE= Conversation.ConversationType.PRIVATE;
                RongIM.getInstance().startConversationList(this);
                break;
            case R.id.id_LineGroup://单聊
                EventType.CONVERSATION_TYPE= Conversation.ConversationType.GROUP;
                RongIM.getInstance().startConversationList(this);
                break;
        }
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.QUITE_CHART_TYPE) {
            finish();
        }
        return false;
    }

    private void initView() {
        activity = this;
        Utiles.headManager(this, R.string.string_Message02);
        findViewById(R.id.id_LineSystem).setOnClickListener(this);
        textViewSystemMessage = (TextView) findViewById(R.id.id_TextViewSystemMessage);
        textViewSystemTime = (TextView) findViewById(R.id.id_TextViewSystemTime);
        viewSystemSingRed = findViewById(R.id.id_ImageSystemSignRed);
        findViewById(R.id.id_LinePrivate).setOnClickListener(this);
        textViewPrivateTime = (TextView) findViewById(R.id.id_TextViewPrivateChatTime);
        textViewPrivateMessage = (TextView) findViewById(R.id.id_TextViewPrivateMessage);
        viewPrivateSingRed = findViewById(R.id.id_ImagepPivateSignRed);
        findViewById(R.id.id_LineGroup).setOnClickListener(this);
        textViewGroupTime = (TextView) findViewById(R.id.id_TextViewGroupChatTime);
        textViewGroupMessage = (TextView) findViewById(R.id.id_TextViewGroupChat);
        viewGroupSingRed = findViewById(R.id.id_ImageGroupSignRed);
        textViewNoMessage = (TextView) findViewById(R.id.id_TextViewNoMessage);
         viewSystem = findViewById(R.id.id_LineSystem);
         viewSystemView = findViewById(R.id.id_LineSystemView);
         viewGroup = findViewById(R.id.id_LineGroup);
         viewGroupView = findViewById(R.id.id_LineGroupView);
         viewPrivate = findViewById(R.id.id_LinePrivate);
         viewPirvateView = findViewById(R.id.id_LinePrivateView);
    }

    public void getListener() {
        //设置接收消息侦听器
       /* RongIM.setOnReceivePushMessageListener(new RongIMClient.OnReceivePushMessageListener() {
            @Override
            public boolean onReceivePushMessage(PushNotificationMessage pushNotificationMessage) {
                Utiles.log("设置接收消息侦听器01");
                return false;
            }
        });*/


        //设置接收消息侦听器
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                Utiles.log("设置接收消息侦听器02");
                return false;
            }
        });

        //设置接收消息侦听器
        RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                Utiles.log("========03>message:设置接收消息侦听器");
                if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE)){
                    handler.sendEmptyMessage(3);
                }
               if (message.getConversationType().equals(Conversation.ConversationType.GROUP)){
                   handler.sendEmptyMessage(2);

               }
                if (message.getConversationType().equals(Conversation.ConversationType.SYSTEM)){
                    handler.sendEmptyMessage(1);
                }
                return false;
            }
        });
        //设置连接状态侦听器
        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                Utiles.log(" 设置连接状态侦听器04 ");
            }
        });
        //设置接收消息侦听器
       /* RongIMClient.setOnReceivePushMessageListener(new RongIMClient.OnReceivePushMessageListener() {
            @Override
            public boolean onReceivePushMessage(PushNotificationMessage pushNotificationMessage) {
                Utiles.log(" 设置接收消息侦听器 05");
                return false;
            }
        });*/
        //设置读接收侦听器
        RongIMClient.setReadReceiptListener(new RongIMClient.ReadReceiptListener() {
            @Override
            public void onReadReceiptReceived(Message message) {
                Conversation.ConversationType conversationType = message.getConversationType();

                String name = conversationType.getName();
                int value = conversationType.getValue();
                Utiles.log(" 设置读接收侦听器 06" + name + "   " + value);
            }
        });
        //设置输入状态侦听器
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType conversationType, String s, Collection<TypingStatus> collection) {
                Utiles.log(" 设置输入状态侦听器 07");
            }
        });
    }

    /**
     * 设置文本消息
     * @param textView
     * @param conversation
     */
    private void setTextViewMessage(TextView textView,Conversation conversation){
        MessageContent messageContent = conversation.getLatestMessage();
        if (messageContent!=null){
            if (messageContent instanceof TextMessage) {//文本消息
                TextMessage textMessage = (TextMessage) messageContent;
                String content = textMessage.getContent();
                if(!content.isEmpty()){
                    textView.setText(content);
                }
            } else if (messageContent instanceof ImageMessage) {//图片消息
                textView.setText(getResources().getString(R.string.string_PictureMessage));
            } else if (messageContent instanceof VoiceMessage) {//语音消息
                textView.setText(getResources().getString(R.string.string_VoiceMessage));
            } else if (messageContent instanceof RichContentMessage) {//图文消息
                textView.setText(getResources().getString(R.string.string_RichContentMessage));

            }
        }


    }
    //设置时间
    private void setTextViewTime(TextView textView,Conversation conversation){
        textView.setText(StringUtils.friendly_time(new java.util.Date(conversation.getReceivedTime())));
    }
    //获取群组消息内容
    private void getGroupMessageContent() {
        //群聊
        List<Conversation> conversationList03 = RongIM.getInstance().getRongIMClient().getConversationList(Conversation.ConversationType.GROUP);
        if (conversationList03 != null && conversationList03.size() != 0) {
            Conversation conversation = conversationList03.get(0);
               setTextViewMessage(textViewGroupMessage,conversation);
               setTextViewTime(textViewGroupTime,conversation);
               Utiles.setVisibility(viewGroup,viewGroupView);
        } else {
            Utiles.setGone(viewGroup,viewGroupView);
        }
    }
    //获取系统消息内容
    private void getSystemMessageContent(){
        //系统
        Conversation conversation = RongIMClient.getInstance().getConversation(Conversation.ConversationType.SYSTEM, "82");
        if (conversation != null ) {
                MessageContent latestMessage = conversation.getLatestMessage();
                    TextMessage latestMessage1 = (TextMessage) latestMessage;
            if (latestMessage1!=null) {
                setTextViewMessage(textViewSystemMessage,conversation);
               String content = latestMessage1.getContent();
                if (!TextUtils.isEmpty(content)) {
                    setTextViewMessage(textViewSystemMessage,conversation);
                    setTextViewTime(textViewSystemTime,conversation);
                    Utiles.setVisibility(viewSystem,viewSystemView);
                }else {
                    Utiles.setGone(viewSystem,viewSystemView);
                }
            }else {
                Utiles.setGone(viewSystem,viewSystemView);
            }
        } else {
            Utiles.setGone(viewSystem,viewSystemView);
        }
    }
    //获取私聊消息内容
    private void getPrivateMessageContent(){
        //单聊
        List<Conversation> conversationList01 = RongIM.getInstance().getRongIMClient().getConversationList(Conversation.ConversationType.PRIVATE);
        if (conversationList01 != null && conversationList01.size() != 0) {
            Conversation conversation = conversationList01.get(0);
            MessageContent latestMessage = conversation.getLatestMessage();
            if (latestMessage!=null) {
                setTextViewMessage(textViewPrivateMessage,conversation);
                setTextViewTime(textViewPrivateTime,conversation);
                Utiles.setVisibility(viewPirvateView,viewPrivate);
            }
        } else {
            Utiles.setGone(viewPirvateView,viewPrivate);
        }
    }

    //显示红点
    private void getMessageType(int i) {
        switch (i) {
            case 1:
                //系统未读个数
                int unReadSys = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.SYSTEM,"82");
                if (unReadSys > 0) {
                    viewSystemSingRed.setVisibility(View.VISIBLE);
                } else {
                    viewSystemSingRed.setVisibility(View.GONE);
                }
                break;
            case 2:
                //群聊未读个数
                int unReadGroup = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.GROUP);
                if (unReadGroup > 0) {
                    viewGroupSingRed.setVisibility(View.VISIBLE);
                } else {
                    viewGroupSingRed.setVisibility(View.GONE);
                }
                break;
            case 3:
                //私聊未读个数
                int unReadPrivate = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.PRIVATE);
                if (unReadPrivate > 0) {
                    viewPrivateSingRed.setVisibility(View.VISIBLE);
                } else {
                    viewPrivateSingRed.setVisibility(View.GONE);
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        getListener();//监听事件
        //红点显示
        getMessageType(1);
        getMessageType(2);
        getMessageType(3);
        //时间和内容
        getSystemMessageContent();
        getPrivateMessageContent();
        getGroupMessageContent();
        if (viewPrivate.getVisibility()==View.GONE &&
                viewSystem.getVisibility() ==
                View.GONE &&  viewGroup.getVisibility()==View.GONE){
            textViewNoMessage.setVisibility(View.VISIBLE);
        }else {
            textViewNoMessage.setVisibility(View.GONE);
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

}
