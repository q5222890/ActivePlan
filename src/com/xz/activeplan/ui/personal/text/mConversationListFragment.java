package com.xz.activeplan.ui.personal.text;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xz.activeplan.R;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.EventType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.model.Draft;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ReadReceiptMessage;
import io.rong.message.VoiceMessage;

public class mConversationListFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private mConversationListAdapter02 adapter;
    private ListView listView;
    private TextView mNotificationBar;
    private boolean isShowWithoutConnected = false;
    private ArrayList<Conversation.ConversationType> listConversationType = new ArrayList();
    private RongIMClient.ResultCallback<List<Conversation>> callback = new RongIMClient.ResultCallback<List<Conversation>>() {
        @Override
        public void onSuccess(List<Conversation> conversations) {
            Utiles.log("   con");
            for (int i = 0; i < conversations.size(); i++) {
                if (conversations.get(i).getConversationType()!=EventType.CONVERSATION_TYPE){
                    Utiles.log("conversations:移除："+conversations.get(i).getConversationType());
                    conversations.remove(conversations.get(i));
                }else {
                    Utiles.log("conversations:不用清楚："+conversations.get(i).getConversationType());
                }
            }
            if(adapter == null) {
              //adapter = new MyConversationListAdapter(getActivity(),RongContext.getInstance());
              adapter= new mConversationListAdapter02(RongContext.getInstance());
            }

            if(conversations != null && conversations.size() != 0) {
                makeUiConversationList(conversations);
                if(listView != null && listView.getAdapter() != null) {
                    adapter.notifyDataSetChanged();
               //   new RongIMClient.ResultCallback<List<>>()

                }
            } else {
              adapter.notifyDataSetChanged();
            }
        }

        public void onError(RongIMClient.ErrorCode e) {
            if(e.equals(RongIMClient.ErrorCode.IPC_DISCONNECT)) {
               isShowWithoutConnected = true;
            }

        }
    };


    public mConversationListFragment() {
    }

    public static mConversationListFragment getInstance() {
        return new mConversationListFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listConversationType.clear();
        //登记
        RongContext.getInstance().getEventBus().register(this);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    protected void initFragment(Uri uri) {
        Conversation.ConversationType[] conversationType =
                new Conversation.ConversationType[]
                        {Conversation.ConversationType.PRIVATE
                               ,Conversation.ConversationType.GROUP,
                                Conversation.ConversationType.DISCUSSION,
                                Conversation.ConversationType.SYSTEM,
                                Conversation.ConversationType.CUSTOMER_SERVICE,
                                Conversation.ConversationType.CHATROOM,
                                Conversation.ConversationType.PUBLIC_SERVICE,
                                Conversation.ConversationType.APP_PUBLIC_SERVICE
                        };
        if(adapter == null) {
            //adapter = new MyConversationListAdapter(getActivity(),RongContext.getInstance());
           adapter = new mConversationListAdapter02(RongContext.getInstance());
        }

        if(uri == null) {
            //获取回话列表集合
            RongIM.getInstance().getRongIMClient().getConversationList(callback);
        } else {
            Conversation.ConversationType[] arr$ = conversationType;
            int len$ = conversationType.length;


            for(int i$ = 0; i$ < len$; ++i$) {
                Conversation.ConversationType type = arr$[i$];
                if(uri.getQueryParameter(type.getName()) != null) {
                    listConversationType.add(type);
                    if("true".equals(uri.getQueryParameter(type.getName()))) {
                        RongContext.getInstance().setConversationGatherState(type.getName(), Boolean.valueOf(true));
                    } else if("false".equals(uri.getQueryParameter(type.getName()))) {
                        RongContext.getInstance().setConversationGatherState(type.getName(), Boolean.valueOf(false));
                    }
                }
            }

            if(RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                if(listConversationType.size() > 0) {
                    RongIM.getInstance().getRongIMClient().getConversationList(callback, (Conversation.ConversationType[])this.listConversationType.toArray(new Conversation.ConversationType[this.listConversationType.size()]));
                } else {
                    RongIM.getInstance().getRongIMClient().getConversationList(callback);
                }

            } else {
                Log.d("ConversationListFr", "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
                isShowWithoutConnected = true;
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fr_conversationlist, container, false);
        mNotificationBar = this.findViewById(view, R.id.rc_status_bar);
        mNotificationBar.setVisibility(View.GONE);
        listView = this.findViewById(view,R.id.rc_list);
        TextView mEmptyView = this.findViewById(view, 16908292);
        if(RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            mEmptyView.setText(RongContext.getInstance().getResources().getString(R.string.rc_conversation_list_empty_prompt));
        } else {
            mEmptyView.setText(RongContext.getInstance().getResources().getString(R.string.rc_conversation_list_not_connected));
        }

        this.listView.setEmptyView(mEmptyView);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(this.adapter == null) {
           //adapter = new MyConversationListAdapter(getActivity(),RongContext.getInstance());
            adapter = new mConversationListAdapter02(RongContext.getInstance());
        }

        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();

        if(RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
          //  RongNotificationManager.getInstance().onRemoveNotification();
            RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus();
            Drawable drawable = this.getActivity().getResources().getDrawable(R.drawable.rc_notification_network_available);
            int width = (int)this.getActivity().getResources().getDimension(R.dimen.rc_message_send_status_image_size);
            drawable.setBounds(0, 0, width, width);
            mNotificationBar.setCompoundDrawablePadding(16);
            mNotificationBar.setCompoundDrawables(drawable, null, null, null);
            if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
            //    RongIM.getInstance().getRongIMClient().reconnect(null);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick));
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                mNotificationBar.setVisibility(View.GONE);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting));
            }

        } else {
            Log.d("ConversationListFr", "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
            isShowWithoutConnected = true;
        }
    }

    public void onDestroy() {
        RongContext.getInstance().getEventBus().unregister(this);
        this.getHandler().removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Utiles.log("=====单击");
        UIConversation conversation = (UIConversation)parent.getAdapter().getItem(position);
        Conversation.ConversationType type = conversation.getConversationType();
        //获取会话收集状态
        if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
            RongIM.getInstance().startSubConversationList(getActivity(), type);
        } else {
            //获取会话列表行为侦听器
            if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(getActivity(), view, conversation);
                if(isDefault) {
                    return;
                }
            }
            conversation.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(getActivity(), type, conversation.getConversationTargetId(), conversation.getUIConversationTitle());
        }

    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = adapter.getItem(position);
        String type = uiConversation.getConversationType().getName();
        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
            if(isDealt) {
                return true;
            }
        }

        if(!RongContext.getInstance().getConversationGatherState(type).booleanValue()) {
            this.buildMultiDialog(uiConversation);
            return true;
        } else {
            this.buildSingleDialog(uiConversation);
            return true;
        }
    }
    //建立多对话框
    private void buildMultiDialog(final UIConversation uiConversation) {
        String[] items = new String[2];
        if(uiConversation.isTop()) {
            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top);
        }
        items[1] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove);
        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(
                new ArraysDialogFragment.OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    RongIM.getInstance().getRongIMClient().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {

                        public void onSuccess(Boolean aBoolean) {
                            if(uiConversation.isTop()) {
                                Toast.makeText(RongContext.getInstance(), mConversationListFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RongContext.getInstance(), mConversationListFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top), Toast.LENGTH_SHORT).show();
                            }

                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                } else if(which == 1) {
                    RongIM.getInstance().getRongIMClient().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                }

            }
        }).show(this.getFragmentManager());
    }
    //建立单对话框
    private void buildSingleDialog(final UIConversation uiConversation) {
        String[] items = new String[]{RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove)};
        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {

                    public void onSuccess(List<Conversation> conversations) {
                        if(conversations != null && conversations.size() != 0) {
                            Iterator i$ = conversations.iterator();

                            while(i$.hasNext()) {
                                Conversation conversation = (Conversation)i$.next();
                                RongIM.getInstance().getRongIMClient().removeConversation(conversation.getConversationType(), conversation.getTargetId());
                            }

                        }
                    }

                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                }, uiConversation.getConversationType());
            }
        }).show(this.getFragmentManager());
    }
    //制作用户界面会话列表
    private void makeUiConversationList(List<Conversation> conversationList) {
        UIConversation uiConversation;
        for(Iterator i$ = conversationList.iterator(); i$.hasNext();
            refreshUnreadCount(uiConversation.getConversationType(),
                    uiConversation.getConversationTargetId())) {
            Conversation conversation = (Conversation)i$.next();
            Conversation.ConversationType conversationType = conversation.getConversationType();
            boolean gatherState = RongContext.getInstance().getConversationGatherState(conversationType.getName()).booleanValue();
            int originalIndex = adapter.findPosition(conversationType, conversation.getTargetId());
            uiConversation = UIConversation.obtain(conversation, gatherState);
            if(originalIndex < 0) {
                if (uiConversation.getConversationType()==EventType.CONVERSATION_TYPE){
                    adapter.add(uiConversation);
                }
            }
        }

    }
    //让UI对话
    private UIConversation makeUiConversation(Message message, int pos) {
        UIConversation uiConversation;
        if(pos >= 0) {
            uiConversation = adapter.getItem(pos);
            if(uiConversation != null) {
                uiConversation.setMessageContent(message.getContent());
                if(message.getMessageDirection() == Message.MessageDirection.SEND) {
                    uiConversation.setUIConversationTime(message.getSentTime());
                    if(RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                        uiConversation.setConversationSenderId(RongIM.getInstance().getRongIMClient().getCurrentUserId());
                    }
                } else {
                    uiConversation.setUIConversationTime(message.getSentTime());
                    uiConversation.setConversationSenderId(message.getSenderUserId());
                }

                uiConversation.setConversationTargetId(message.getTargetId());
                uiConversation.setConversationContent(uiConversation.buildConversationContent(uiConversation));
                uiConversation.setSentStatus(message.getSentStatus());
                uiConversation.setLatestMessageId(message.getMessageId());
            }
        } else {
            uiConversation = UIConversation.obtain(message, RongContext.getInstance().getConversationGatherState(message.getConversationType().getName()).booleanValue());
        }

        return uiConversation;
    }
    //从列表中进行用户界面对话
    private UIConversation makeUIConversationFromList(List<Conversation> conversations) {
        int unreadCount = 0;
        boolean topFlag = false;
        Conversation newest = conversations.get(0);

        Conversation conversation;
        for(Iterator uiConversation = conversations.iterator(); uiConversation.hasNext(); unreadCount += conversation.getUnreadMessageCount()) {
            conversation = (Conversation)uiConversation.next();
            if(newest.isTop()) {
                if(conversation.isTop() && conversation.getSentTime() > newest.getSentTime()) {
                    newest = conversation;
                }
            } else if(conversation.isTop() || conversation.getSentTime() > newest.getSentTime()) {
                newest = conversation;
            }

            if(conversation.isTop()) {
                topFlag = true;
            }
        }

        UIConversation uiConversation1 = UIConversation.obtain(newest, RongContext.getInstance().getConversationGatherState(newest.getConversationType().getName()).booleanValue());
        uiConversation1.setUnReadMessageCount(unreadCount);
        uiConversation1.setTop(topFlag);
        return uiConversation1;
    }
    //刷新未读计数
    private void refreshUnreadCount(final Conversation.ConversationType type, final String targetId) {
        if(RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
                RongIM.getInstance().getRongIMClient().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {

                    public void onSuccess(Integer count) {
                        int curPos = adapter.findPosition(type, targetId);
                        if(curPos >= 0) {
                            adapter.getItem(curPos).setUnReadMessageCount(count.intValue());
                            adapter.getView(curPos, listView.getChildAt(curPos - listView.getFirstVisiblePosition()), mConversationListFragment.this.listView);
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                        System.err.print("Throw exception when get unread message count from ipc remote side!");
                    }
                }, type);
            } else {
                RongIM.getInstance().getRongIMClient().getUnreadCount(type, targetId, new RongIMClient.ResultCallback<Integer>() {

                    public void onSuccess(Integer integer) {
                        int curPos = adapter.findPosition(type, targetId);
                        if(curPos >= 0) {
                            adapter.getItem(curPos).setUnReadMessageCount(integer.intValue());
                            adapter.getView(curPos, listView.getChildAt(curPos - listView.getFirstVisiblePosition()), listView);
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                });
            }
        }

    }

    public mConversationListAdapter02 getAdapter() {
        return adapter;
    }

    public void setAdapter(mConversationListAdapter02 adapter) {
        if(adapter != null) {
            adapter.clear();
        }

        this.adapter = adapter;
        if(listView != null && this.getUri() != null) {
            listView.setAdapter(adapter);
            initFragment(this.getUri());
        }

    }

    public void onEventMainThread(Event.ConnectEvent event) {
   Utiles.log("   onEventMainThread  01");
        if(isShowWithoutConnected) {
            if(listConversationType.size() > 0) {
                RongIM.getInstance().getRongIMClient().getConversationList(callback, (Conversation.ConversationType[])listConversationType.toArray(new Conversation.ConversationType[this.listConversationType.size()]));
            } else {
                RongIM.getInstance().getRongIMClient().getConversationList(callback);
            }

            TextView mEmptyView = (TextView)listView.getEmptyView();
            mEmptyView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
            isShowWithoutConnected = false;
        }
    }

    public void onEventMainThread(Event.ReadReceiptEvent event) {
        Utiles.log("   onEventMainThread  02");
        if(this.adapter == null) {
        } else {
            int originalIndex = adapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
            boolean gatherState = RongContext.getInstance().getConversationGatherState(event.getMessage().getConversationType().getName()).booleanValue();
            if(!gatherState && originalIndex >= 0) {
                UIConversation conversation = adapter.getItem(originalIndex);
                ReadReceiptMessage content = (ReadReceiptMessage)event.getMessage().getContent();
                if(content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                    conversation.setSentStatus(Message.SentStatus.READ);
                    this.adapter.getView(originalIndex, listView.getChildAt(originalIndex - listView.getFirstVisiblePosition()), listView);
                }
            }

        }
    }

    public void onEventMainThread(final Event.OnReceiveMessageEvent event) {
        Utiles.log("   onEventMainThread    03 "+event.getMessage().getConversationType()+"           "+EventType.CONVERSATION_TYPE);
        if((listConversationType.size() == 0 |
                listConversationType.contains(event.getMessage().getConversationType()))
                && (listConversationType.size() != 0 || event.getMessage().getConversationType()
                != Conversation.ConversationType.CHATROOM && event.getMessage().getConversationType()
                != Conversation.ConversationType.CUSTOMER_SERVICE)) {

            int originalIndex = adapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
            Utiles.log("========  03   01    originalIndex:    "+originalIndex+"    CONVERSATION_TYPE    "
                    +EventType.CONVERSATION_TYPE+"     Type()    "+event.getMessage().getConversationType());

            if (event.getMessage().getConversationType()!= EventType.CONVERSATION_TYPE){
                Utiles.log("=======onEventMainThread 03 >移除不是当前状态消息");
                adapter.remove(originalIndex);
                return;
            }
            UIConversation uiConversation = makeUiConversation(event.getMessage(), originalIndex);
            int newPosition = mConversationListUtils.findPositionForNewConversation(uiConversation, adapter);
            if(originalIndex < 0) {
                Utiles.log("========  03   02");
                adapter.add(uiConversation, newPosition);
            } else if(originalIndex != newPosition) {
                Utiles.log("========  03   03");
                adapter.remove(originalIndex);
                adapter.add(uiConversation, newPosition);
            }
            adapter.notifyDataSetChanged();
            MessageTag msgTag = event.getMessage().getContent().getClass().getAnnotation(MessageTag.class);
            if(msgTag != null && (msgTag.flag() & 3) == 3) {
                Utiles.log("========  03   04");
                this.refreshUnreadCount(event.getMessage().getConversationType(), event.getMessage().getTargetId());
            }

            if(RongContext.getInstance().getConversationGatherState(event.getMessage().getConversationType().getName()).booleanValue()) {
                Utiles.log("========  03   05");
                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    public void onSuccess(List<Conversation> conversations) {
                        Iterator i$ = conversations.iterator();
                        while(true) {
                            if(i$.hasNext()) {
                                Conversation conv = (Conversation)i$.next();
                                if(conversations == null || conversations.size() == 0) {
                                    return;
                                }

                                if(!conv.getConversationType().equals(event.getMessage().getConversationType()) || !conv.getTargetId().equals(event.getMessage().getTargetId())) {
                                    continue;
                                }

                                int pos = adapter.findPosition(conv.getConversationType(), conv.getTargetId());
                                if(pos >= 0) {
                                    adapter.getItem(pos).setDraft(conv.getDraft());
                                    if(TextUtils.isEmpty(conv.getDraft())) {
                                        adapter.getItem(pos).setSentStatus(null);
                                    } else {
                                        adapter.getItem(pos).setSentStatus(conv.getSentStatus());
                                    }

                                    adapter.getView(pos, listView.getChildAt(pos - listView.getFirstVisiblePosition()), listView);
                                }
                            }

                            return;
                        }
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                }, event.getMessage().getConversationType());
            }

        } else {
        }
    }

    public void onEventMainThread(Message message) {

        if (message.getConversationType()!=EventType.CONVERSATION_TYPE){
            Utiles.log("   onEventMainThread  04  消息类型不相同  ");
            return;
        }


        Utiles.log("   onEventMainThread  04");
        if(listConversationType.size() != 0 && !listConversationType.contains(message.getConversationType()) || listConversationType.size() == 0 && (message.getConversationType() == Conversation.ConversationType.CHATROOM || message.getConversationType() == Conversation.ConversationType.CUSTOMER_SERVICE)) {
        } else {
            int originalIndex = adapter.findPosition(message.getConversationType(), message.getTargetId());
            UIConversation uiConversation = this.makeUiConversation(message, originalIndex);
            int newPosition = mConversationListUtils.findPositionForNewConversation(uiConversation, adapter);
            if(originalIndex >= 0) {
                if(newPosition == originalIndex) {
                    adapter.getView(newPosition, listView.getChildAt(newPosition - this.listView.getFirstVisiblePosition()), this.listView);
                } else {
                    adapter.remove(originalIndex);
                    adapter.add(uiConversation, newPosition);
                    adapter.notifyDataSetChanged();
                }
            } else {
                this.adapter.add(uiConversation, newPosition);
                this.adapter.notifyDataSetChanged();
            }

        }
    }

    public void onEventMainThread(MessageContent content) {
        Utiles.log("   onEventMainThread  05");
        for(int index = 0; index < adapter.getCount(); ++index) {
            UIConversation tempUIConversation = adapter.getItem(index);
            if(content != null && tempUIConversation.getMessageContent() != null && tempUIConversation.getMessageContent() == content) {
                tempUIConversation.setMessageContent(content);
                tempUIConversation.setConversationContent(tempUIConversation.buildConversationContent(tempUIConversation));
                if(index >= listView.getFirstVisiblePosition()) {
                    adapter.getView(index, listView.getChildAt(index - listView.getFirstVisiblePosition()), listView);
                }
            }
        }

    }

    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        Utiles.log("   onEventMainThread  06");
        if(this.isResumed()) {
            Drawable drawable = getActivity().getResources().getDrawable(R.drawable.rc_notification_network_available);
            int width = (int)this.getActivity().getResources().getDimension(io.rong.imkit.R.dimen.rc_message_send_status_image_size);
            drawable.setBounds(0, 0, width, width);
            mNotificationBar.setCompoundDrawablePadding(16);
            mNotificationBar.setCompoundDrawables(drawable, null, null, null);
            if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
             //   RongIM.getInstance().getRongIMClient().reconnect((RongIMClient.ConnectCallback)null);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(getResources().getString(io.rong.imkit.R.string.rc_notice_tick));
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                mNotificationBar.setVisibility(View.GONE);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.setText(getResources().getString(io.rong.imkit.R.string.rc_notice_connecting));
            }
        }

    }

    public void onEventMainThread(Event.CreateDiscussionEvent createDiscussionEvent) {
        Utiles.log("   onEventMainThread  07");
        UIConversation conversation = new UIConversation();
        conversation.setConversationType(Conversation.ConversationType.DISCUSSION);
        if(createDiscussionEvent.getDiscussionName() != null) {
            conversation.setUIConversationTitle(createDiscussionEvent.getDiscussionName());
        } else {
            conversation.setUIConversationTitle("");
        }

        conversation.setConversationTargetId(createDiscussionEvent.getDiscussionId());
        conversation.setUIConversationTime(System.currentTimeMillis());
        boolean isGather = RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.DISCUSSION.getName()).booleanValue();
        conversation.setConversationGatherState(isGather);
        if(isGather) {
            String gatherPosition = RongContext.getInstance().getGatheredConversationTitle(conversation.getConversationType());
            conversation.setUIConversationTitle(gatherPosition);
        }

        int gatherPosition1 = adapter.findGatherPosition(Conversation.ConversationType.DISCUSSION);
        if(gatherPosition1 == -1) {
            adapter.add(conversation, mConversationListUtils.findPositionForNewConversation(conversation, adapter));
            adapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Draft draft) {
        Utiles.log("   onEventMainThread  08");
        Conversation.ConversationType curType = Conversation.ConversationType.setValue(draft.getType().intValue());
        if(curType == null) {
            throw new IllegalArgumentException("the type of the draft is unknown!");
        } else {
            int position = adapter.findPosition(curType, draft.getId());
            if(position >= 0) {
                UIConversation conversation = adapter.getItem(position);
                if(conversation.getConversationTargetId().equals(draft.getId())) {
                    conversation.setDraft(draft.getContent());
                    if(!TextUtils.isEmpty(draft.getContent())) {
                        conversation.setSentStatus(null);
                    }

                    adapter.getView(position, listView.getChildAt(position - listView.getFirstVisiblePosition()), this.listView);
                }
            }

        }
    }

    public void onEventMainThread(Group groupInfo) {
        Utiles.log("   onEventMainThread  09");
        int count = this.adapter.getCount();
        if(groupInfo.getName() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation item = adapter.getItem(i);
                if(item != null && item.getConversationType().equals(Conversation.ConversationType.GROUP) && item.getConversationTargetId().equals(groupInfo.getId())) {
                    boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName()).booleanValue();
                    if(gatherState) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        Spannable messageData = RongContext.getInstance().getMessageTemplate(item.getMessageContent().getClass()).getContentSummary(item.getMessageContent());
                        if(item.getMessageContent() instanceof VoiceMessage) {
                            boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(item.getConversationType(), item.getConversationTargetId()).getReceivedStatus().isListened();
                            if(isListened) {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                            } else {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                            }
                        }

                        builder.append(groupInfo.getName()).append(" : ").append(messageData);
                        item.setConversationContent(builder);
                        if(groupInfo.getPortraitUri() != null) {
                            item.setIconUrl(groupInfo.getPortraitUri());
                        }
                    } else {
                        item.setUIConversationTitle(groupInfo.getName());
                        if(groupInfo.getPortraitUri() != null) {
                            item.setIconUrl(groupInfo.getPortraitUri());
                        }
                    }

                    this.adapter.getView(i, this.listView.getChildAt(i - this.listView.getFirstVisiblePosition()), this.listView);
                }
            }

        }
    }

    public void onEventMainThread(Discussion discussion) {
        Utiles.log("   onEventMainThread  09");
        int count = this.adapter.getCount();
        for(int i = 0; i < count; ++i) {
            UIConversation item = adapter.getItem(i);
            if(item != null && item.getConversationType().equals(Conversation.ConversationType.DISCUSSION) && item.getConversationTargetId().equals(discussion.getId())) {
                boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName()).booleanValue();
                if(gatherState) {
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    Spannable messageData = RongContext.getInstance().getMessageTemplate(item.getMessageContent().getClass()).getContentSummary(item.getMessageContent());
                    if(messageData != null) {
                        if(item.getMessageContent() instanceof VoiceMessage) {
                            boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(item.getConversationType(), item.getConversationTargetId()).getReceivedStatus().isListened();
                            if(isListened) {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                            } else {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                            }
                        }

                        builder.append(discussion.getName()).append(" : ").append(messageData);
                    } else {
                        builder.append(discussion.getName());
                    }

                    item.setConversationContent(builder);
                } else {
                    item.setUIConversationTitle(discussion.getName());
                }

                this.adapter.getView(i, this.listView.getChildAt(i - this.listView.getFirstVisiblePosition()), this.listView);
            }
        }

    }

    public void onEventMainThread(Event.GroupUserInfoEvent event) {
        Utiles.log("   onEventMainThread  10");
        int count = adapter.getCount();
        GroupUserInfo userInfo = event.getUserInfo();
        if(userInfo != null && userInfo.getNickname() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation uiConversation = adapter.getItem(i);
                Conversation.ConversationType type = uiConversation.getConversationType();
                boolean gatherState = RongContext.getInstance().getConversationGatherState(uiConversation.getConversationType().getName()).booleanValue();
                boolean isShowName;
                if(uiConversation.getMessageContent() == null) {
                    isShowName = false;
                } else {
                    isShowName = RongContext.getInstance().getMessageProviderTag(uiConversation.getMessageContent().getClass()).showSummaryWithName();
                }

                if(!gatherState && isShowName && type.equals(Conversation.ConversationType.GROUP) && uiConversation.getConversationSenderId().equals(userInfo.getUserId())) {
                    Spannable messageData = RongContext.getInstance().getMessageTemplate(uiConversation.getMessageContent().getClass()).getContentSummary(uiConversation.getMessageContent());
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    if(uiConversation.getMessageContent() instanceof VoiceMessage) {
                        boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId()).getReceivedStatus().isListened();
                        if(isListened) {
                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                        } else {
                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                        }
                    }

                    if(uiConversation.getConversationTargetId().equals(userInfo.getGroupId())) {
                        uiConversation.addNickname(userInfo.getUserId());
                        builder.append(userInfo.getNickname()).append(" : ").append(messageData);
                        uiConversation.setConversationContent(builder);
                    }

                    adapter.getView(i, listView.getChildAt(i - listView.getFirstVisiblePosition()), listView);
                }
            }

        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        Utiles.log("   onEventMainThread  11");
        int count = adapter.getCount();
        if(userInfo.getName() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation temp = adapter.getItem(i);
                String type = temp.getConversationType().getName();
                boolean gatherState = RongContext.getInstance().getConversationGatherState(temp.getConversationType().getName()).booleanValue();
                if(!temp.hasNickname(userInfo.getUserId())) {
                    boolean isShowName;
                    if(temp.getMessageContent() == null) {
                        isShowName = false;
                    } else {
                        isShowName = RongContext.getInstance().getMessageProviderTag(temp.getMessageContent().getClass()).showSummaryWithName();
                    }

                    Spannable messageData;
                    SpannableStringBuilder builder;
                    boolean isListened;
                    if(!gatherState && isShowName && (type.equals("group") || type.equals("discussion")) && temp.getConversationSenderId().equals(userInfo.getUserId())) {
                        messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                        builder = new SpannableStringBuilder();
                        if(temp.getMessageContent() instanceof VoiceMessage) {
                            isListened = RongIM.getInstance().getRongIMClient().getConversation(temp.getConversationType(), temp.getConversationTargetId()).getReceivedStatus().isListened();
                            if(isListened) {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                            } else {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                            }
                        }

                        builder.append(userInfo.getName()).append(" : ").append(messageData);
                        temp.setConversationContent(builder);
                        adapter.getView(i, listView.getChildAt(i - listView.getFirstVisiblePosition()), listView);
                    } else if(temp.getConversationTargetId().equals(userInfo.getUserId())) {
                        if(gatherState || type != "private" && type != "system") {
                            if(isShowName) {
                                messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                                builder = new SpannableStringBuilder();
                                if(messageData != null) {
                                    if(temp.getMessageContent() instanceof VoiceMessage) {
                                        isListened = RongIM.getInstance().getRongIMClient().getConversation(temp.getConversationType(), temp.getConversationTargetId()).getReceivedStatus().isListened();
                                        if(isListened) {
                                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                                        } else {
                                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                                        }
                                    }

                                    builder.append(userInfo.getName()).append(" : ").append(messageData);
                                } else {
                                    builder.append(userInfo.getName());
                                }

                                temp.setConversationContent(builder);
                                temp.setIconUrl(userInfo.getPortraitUri());
                            }
                        } else {
                            temp.setUIConversationTitle(userInfo.getName());
                            temp.setIconUrl(userInfo.getPortraitUri());
                        }

                        this.adapter.getView(i, this.listView.getChildAt(i - this.listView.getFirstVisiblePosition()), this.listView);
                    }
                }
            }

        }
    }

    public void onEventMainThread(PublicServiceProfile accountInfo) {
        Utiles.log("   onEventMainThread  12");
        int count = adapter.getCount();
        boolean gatherState = RongContext.getInstance().getConversationGatherState(accountInfo.getConversationType().getName()).booleanValue();

        for(int i = 0; i < count; ++i) {
            if(adapter.getItem(i).getConversationType().equals(accountInfo.getConversationType()) && this.adapter.getItem(i).getConversationTargetId().equals(accountInfo.getTargetId()) && !gatherState) {
                adapter.getItem(i).setUIConversationTitle(accountInfo.getName());
                adapter.getItem(i).setIconUrl(accountInfo.getPortraitUri());
                adapter.getView(i, listView.getChildAt(i -listView.getFirstVisiblePosition()), listView);
                break;
            }
        }

    }

    public void onEventMainThread(Event.PublicServiceFollowableEvent event) {
        Utiles.log("   onEventMainThread  13");
        if(event != null && !event.isFollow()) {
            int originalIndex = adapter.findPosition(event.getConversationType(), event.getTargetId());
            if(originalIndex >= 0) {
                adapter.remove(originalIndex);
                adapter.notifyDataSetChanged();
            }
        }

    }

    public void onEventMainThread(final Event.ConversationUnreadEvent unreadEvent) {

        Utiles.log("   onEventMainThread  14  01");
        int targetIndex = adapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
        if(targetIndex >= 0) {
            UIConversation temp = adapter.getItem(targetIndex);
            Utiles.log("   onEventMainThread  14  02");
            boolean gatherState = temp.getConversationGatherState();
            if(gatherState) {
                RongIM.getInstance().getRongIMClient().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        int pos = adapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
                        if(pos >= 0) {
                            adapter.getItem(pos).setUnReadMessageCount(integer.intValue());
                            adapter.getView(pos, listView.getChildAt(pos - listView.getFirstVisiblePosition()), listView);
                        }
                    }
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                }, unreadEvent.getType());
            } else {
                temp.setUnReadMessageCount(0);
                adapter.getView(targetIndex, listView.getChildAt(targetIndex - listView.getFirstVisiblePosition()), listView);
            }
        }

    }

    public void onEventMainThread(final Event.ConversationTopEvent setTopEvent) throws IllegalAccessException {
        Utiles.log("   onEventMainThread  15");
        int originalIndex = adapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
        if(originalIndex >= 0) {
            UIConversation temp = adapter.getItem(originalIndex);
            boolean originalValue = temp.isTop();
            if(originalValue != setTopEvent.isTop()) {
                if(temp.getConversationGatherState()) {
                    RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversations) {
                            if(conversations != null && conversations.size() != 0) {
                                UIConversation newConversation = makeUIConversationFromList(conversations);
                                int pos = adapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
                                if(pos >= 0) {
                                    adapter.remove(pos);
                                }

                                int newIndex = mConversationListUtils.findPositionForNewConversation(newConversation, mConversationListFragment.this.adapter);
                                adapter.add(newConversation, newIndex);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    }, temp.getConversationType());
                } else {
                    int newIndex;
                    if(originalValue) {
                        temp.setTop(false);
                        newIndex = mConversationListUtils.findPositionForCancleTop(originalIndex, adapter);
                    } else {
                        temp.setTop(true);
                        newIndex = mConversationListUtils.findPositionForSetTop(temp, adapter);
                    }

                    if(originalIndex == newIndex) {
                        this.adapter.getView(newIndex, this.listView.getChildAt(newIndex - this.listView.getFirstVisiblePosition()), this.listView);
                    } else {
                        this.adapter.remove(originalIndex);
                        this.adapter.add(temp, newIndex);
                        this.adapter.notifyDataSetChanged();
                    }
                }

            }
        } else {
            throw new IllegalAccessException("the item has already been deleted!");
        }
    }

    public void onEventMainThread(final Event.ConversationRemoveEvent removeEvent) {
        Utiles.log("   onEventMainThread  16");
        int removedIndex = adapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
        boolean gatherState = RongContext.getInstance().getConversationGatherState(removeEvent.getType().getName()).booleanValue();
        if(!gatherState) {
            if(removedIndex >= 0) {
                adapter.remove(removedIndex);
                adapter.notifyDataSetChanged();
            }
        } else if(removedIndex >= 0) {
            RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {

                public void onSuccess(List<Conversation> conversationList) {
                    int oldPos = adapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
                    if(conversationList != null && conversationList.size() != 0) {
                        UIConversation newConversation = makeUIConversationFromList(conversationList);
                        if(oldPos >= 0) {
                            adapter.remove(oldPos);
                        }

                        int newIndex = mConversationListUtils.findPositionForNewConversation(newConversation, mConversationListFragment.this.adapter);
                       adapter.add(newConversation, newIndex);
                        adapter.notifyDataSetChanged();
                    } else {
                        if(oldPos >= 0) {
                            adapter.remove(oldPos);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            }, removeEvent.getType());
        }

    }

    public void onEventMainThread(Event.MessageDeleteEvent event) {
        Utiles.log("   onEventMainThread  17");
        int count = adapter.getCount();

        for(int i = 0; i < count; ++i) {
            if(event.getMessageIds().contains(Integer.valueOf(adapter.getItem(i).getLatestMessageId()))) {
                boolean gatherState = adapter.getItem(i).getConversationGatherState();
                if(gatherState) {
                    RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() != 0) {
                                UIConversation uiConversation = makeUIConversationFromList(conversationList);
                                int oldPos = adapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if(oldPos >= 0) {
                                    adapter.remove(oldPos);
                                }
                                int newIndex = mConversationListUtils.findPositionForNewConversation(uiConversation, mConversationListFragment.this.adapter);
                                adapter.add(uiConversation, newIndex);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, adapter.getItem(i).getConversationType());
                } else {

                    RongIM.getInstance().getRongIMClient().getConversation(adapter.getItem(i).getConversationType(), this.adapter.getItem(i).getConversationTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation conversation) {
                            if(conversation == null) {
                            } else {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = adapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    adapter.remove(pos);
                                }

                                int newPosition = mConversationListUtils.findPositionForNewConversation(temp, mConversationListFragment.this.adapter);
                                adapter.add(temp, newPosition);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {

        Utiles.log("   onEventMainThread  18");
        int originalIndex = adapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
        if(originalIndex >= 0) {
            adapter.getView(originalIndex, listView.getChildAt(originalIndex - listView.getFirstVisiblePosition()), listView);
        }

    }

    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
        Utiles.log("   onEventMainThread  18");
        final int position = adapter.findPosition(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());
        if(position >= 0) {
            boolean gatherState = RongContext.getInstance().getConversationGatherState(clearMessagesEvent.getType().getName()).booleanValue();
            if(gatherState) {
                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    public void onSuccess(List<Conversation> conversationList) {
                        Utiles.log(" 18onEventMainThread  01 ");
                        if(conversationList != null && conversationList.size() != 0) {
                            UIConversation uiConversation = makeUIConversationFromList(conversationList);
                            int pos = adapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                            if (uiConversation.getConversationType()==EventType.CONVERSATION_TYPE) {
                                if (pos >= 0) {
                                    adapter.remove(pos);
                                }
                                int newIndex = mConversationListUtils.findPositionForNewConversation(uiConversation, mConversationListFragment.this.adapter);
                                adapter.add(uiConversation, newIndex);
                                adapter.notifyDataSetChanged();
                            }else {
                                adapter.remove(pos);
                            }
                        }
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                }, Conversation.ConversationType.GROUP);

            } else {
                RongIMClient.getInstance().getConversation(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                    public void onSuccess(Conversation conversation) {
                        Utiles.log(" 18onEventMainThread  02 ");
                        if (conversation.getConversationType()==EventType.CONVERSATION_TYPE) {
                            UIConversation uiConversation = UIConversation.obtain(conversation, false);
                            mConversationListFragment.this.adapter.remove(position);
                            int newPos = mConversationListUtils.findPositionForNewConversation(uiConversation, mConversationListFragment.this.adapter);
                            mConversationListFragment.this.adapter.add(uiConversation, newPos);
                            mConversationListFragment.this.adapter.notifyDataSetChanged();
                        }else {
                            adapter.remove(position);
                        }
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                });
            }
        }

    }

    public void onEventMainThread(Event.OnMessageSendErrorEvent sendErrorEvent) {
        Utiles.log("   onEventMainThread  20");
        int index = adapter.findPosition(sendErrorEvent.getMessage().getConversationType(), sendErrorEvent.getMessage().getTargetId());
        if(index >= 0) {
            UIConversation temp = adapter.getItem(index);
            temp.setUIConversationTime(sendErrorEvent.getMessage().getSentTime());
            temp.setMessageContent(sendErrorEvent.getMessage().getContent());
            temp.setConversationContent(temp.buildConversationContent(temp));
            temp.setSentStatus(Message.SentStatus.FAILED);
            adapter.remove(index);
            int newPosition = mConversationListUtils.findPositionForNewConversation(temp, adapter);
            adapter.add(temp, newPosition);
            adapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.QuitDiscussionEvent event) {
        Utiles.log("   onEventMainThread  21");
        int index = adapter.findPosition(Conversation.ConversationType.DISCUSSION, event.getDiscussionId());
        if(index >= 0) {
            adapter.remove(index);
            adapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.QuitGroupEvent event) {
        Utiles.log("   onEventMainThread  22");
        final int index = adapter.findPosition(Conversation.ConversationType.GROUP, event.getGroupId());
        boolean gatherState = RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.GROUP.getName()).booleanValue();
        if(index >= 0 && gatherState) {
            RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>(){
                public void onSuccess(List<Conversation> conversationList) {
                    if(conversationList != null && conversationList.size() != 0) {
                        UIConversation uiConversation = makeUIConversationFromList(conversationList);
                        int pos = adapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                        if(pos >= 0) {
                            mConversationListFragment.this.adapter.remove(pos);
                        }

                        int newIndex = mConversationListUtils.findPositionForNewConversation(uiConversation, mConversationListFragment.this.adapter);
                        adapter.add(uiConversation, newIndex);
                        adapter.notifyDataSetChanged();
                    } else {
                        if(index >= 0) {
                            adapter.remove(index);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            }, Conversation.ConversationType.GROUP);
        } else if(index >= 0) {
            this.adapter.remove(index);
            this.adapter.notifyDataSetChanged();
        }

    }
/*
    public void onEventMainThread(Event.MessageListenedEvent event) {
        Utiles.log("   onEventMainThread  23");
        int originalIndex = adapter.findPosition(event.getConversationType(), event.getTargetId());
        if(originalIndex >= 0) {
            UIConversation temp = (UIConversation)adapter.getItem(originalIndex);
            if(temp.getLatestMessageId() == event.getLatestMessageId()) {
                temp.setConversationContent(temp.buildConversationContent(temp));
            }

            adapter.getView(originalIndex, listView.getChildAt(originalIndex - listView.getFirstVisiblePosition()), listView);
        }

    }*/

}
