package com.xz.activeplan.ui.personal.text;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xz.activeplan.R;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.model.Draft;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utils.ConversationListUtils;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.adapter.SubConversationListAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;
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

/**
 * Created by Administrator on 2016/6/29.
 */
public class mSubConversationListFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private SubConversationListAdapter mAdapter;
    private Conversation.ConversationType currentType;
    private TextView mNotificationBar;
    private ListView mList;
    RongIMClient.ResultCallback<List<Conversation>> mCallback = new RongIMClient.ResultCallback<List<Conversation>>() {
        public void onSuccess(List<Conversation> conversations) {
            if(conversations != null && conversations.size() != 0) {
                ArrayList uiConversationList = new ArrayList();
                Iterator i$ = conversations.iterator();

                while(i$.hasNext()) {
                    Conversation conversation = (Conversation)i$.next();
                    if(mSubConversationListFragment.this.mAdapter.getCount() > 0) {
                        int uiConversation =mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                        if(uiConversation < 0) {
                            UIConversation uiConversation1 = UIConversation.obtain(conversation, false);
                            uiConversationList.add(uiConversation1);
                        }
                    } else {
                        UIConversation uiConversation2 = UIConversation.obtain(conversation, false);
                        uiConversationList.add(uiConversation2);
                    }
                }

                mAdapter.addCollection(uiConversationList);
                if(mList != null && mList.getAdapter() != null) {
                    mAdapter.notifyDataSetChanged();
                }

            }
        }

        public void onError(RongIMClient.ErrorCode e) {
        }
    };
    private int x;

    public mSubConversationListFragment() {
    }

    public static ConversationListFragment getInstance() {
        return new ConversationListFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongContext.getInstance().getEventBus().register(this);
        if(getActivity().getIntent() != null && getActivity().getIntent().getData() != null) {
            if(mAdapter == null) {
                mAdapter = new SubConversationListAdapter(getActivity());
            }

        } else {
            throw new IllegalArgumentException();
        }
    }

    public void initFragment(Uri uri) {
        String type = uri.getQueryParameter("type");
        Conversation.ConversationType value = null;
        Utiles .log("   mSubConversationListFragent:"+type);
        this.currentType = null;
        Conversation.ConversationType[] defaultTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION, Conversation.ConversationType.GROUP, Conversation.ConversationType.CHATROOM, Conversation.ConversationType.CUSTOMER_SERVICE, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};
        Conversation.ConversationType[] arr$ = defaultTypes;
        int len$ = defaultTypes.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Conversation.ConversationType conversationType = arr$[i$];
            if(conversationType.getName().equals(type)) {
                this.currentType = conversationType;
                value = conversationType;
                break;
            }
        }

        if(value != null) {
            RongIM.getInstance().getRongIMClient().getConversationList(mCallback, new Conversation.ConversationType[]{value});
        } else {
            throw new IllegalArgumentException("Unknown conversation type!!");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(io.rong.imkit.R.layout.rc_fr_conversationlist, (ViewGroup)null);
        mNotificationBar = (TextView)this.findViewById(view, R.id.rc_status_bar);
        mList = (ListView)this.findViewById(view, io.rong.imkit.R.id.rc_list);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
        mList.setOnItemLongClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus();
        Drawable drawable = this.getActivity().getResources().getDrawable(R.drawable.rc_notification_network_available);

        int width = (int)this.getActivity().getResources().getDimension(R.dimen.rc_message_send_status_image_size);
        drawable.setBounds(0, 0, width, width);
        this.mNotificationBar.setCompoundDrawablePadding(16);
        this.mNotificationBar.setCompoundDrawables(drawable, (Drawable)null, (Drawable)null, (Drawable)null);
        if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
            this.mNotificationBar.setVisibility(0);
            this.mNotificationBar.setText(this.getResources().getString(R.string.rc_notice_network_unavailable));
            //RongIM.getInstance().getRongIMClient().reconnect((RongIMClient.ConnectCallback)null);
        } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
            this.mNotificationBar.setVisibility(0);
            this.mNotificationBar.setText(this.getResources().getString(R.string.rc_notice_tick));
        } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            this.mNotificationBar.setVisibility(8);
        } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            this.mNotificationBar.setVisibility(0);
            this.mNotificationBar.setText(this.getResources().getString(R.string.rc_notice_network_unavailable));
        } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
            this.mNotificationBar.setVisibility(0);
            this.mNotificationBar.setText(this.getResources().getString(R.string.rc_notice_connecting));
        }

    }
//事件主线程
    public void onEventMainThread(Event.ReadReceiptEvent event) {
        if(this.mAdapter == null) {
        } else {
            int originalIndex = this.mAdapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
            if(originalIndex >= 0) {
                UIConversation conversation = (UIConversation)this.mAdapter.getItem(originalIndex);
                ReadReceiptMessage content = (ReadReceiptMessage)event.getMessage().getContent();
                if(content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                    conversation.setSentStatus(Message.SentStatus.READ);
                    this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(Message message) {
        int originalIndex = this.mAdapter.findPosition(message.getConversationType(), message.getTargetId());
        if(message.getConversationType().equals(this.currentType)) {
            UIConversation uiConversation = null;
            if(originalIndex >= 0) {
                uiConversation = this.makeUiConversation(message, originalIndex);
                int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
                if(newPosition == originalIndex) {
                    this.mAdapter.getView(newPosition, this.mList.getChildAt(newPosition - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.remove(originalIndex);
                    this.mAdapter.add(uiConversation, newPosition);
                    this.mAdapter.notifyDataSetChanged();
                }
            } else {
                uiConversation = UIConversation.obtain(message, false);
                this.mAdapter.add(uiConversation, ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter));
                this.mAdapter.notifyDataSetChanged();
            }

        }
    }

    public void onEventMainThread(Event.OnReceiveMessageEvent onReceiveMessageEvent) {
        this.onEventMainThread(onReceiveMessageEvent.getMessage());
    }

    public void onEventMainThread(MessageContent content) {
        for(int index = mList.getFirstVisiblePosition(); index < this.mList.getLastVisiblePosition(); ++index) {
            UIConversation tempUIConversation = (UIConversation)this.mAdapter.getItem(index);
            if(tempUIConversation.getMessageContent().equals(content)) {
                tempUIConversation.setMessageContent(content);
                Spannable messageData = RongContext.getInstance().getMessageTemplate(content.getClass()).getContentSummary(content);
                if(tempUIConversation.getMessageContent() instanceof VoiceMessage) {
                    boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(tempUIConversation.getConversationType(), tempUIConversation.getConversationTargetId()).getReceivedStatus().isListened();
                    if(isListened) {
                        messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                    } else {
                        messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                    }
                }

                tempUIConversation.setConversationContent(messageData);
                this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(Draft draft) {
        Conversation.ConversationType curType = Conversation.ConversationType.setValue(draft.getType().intValue());
        if(curType == null) {
            throw new IllegalArgumentException("the type of the draft is unknown!");
        } else {
            int position = this.mAdapter.findPosition(curType, draft.getId());
            if(position >= 0) {
                UIConversation conversation = (UIConversation)this.mAdapter.getItem(position);
                if(draft.getContent() == null) {
                    conversation.setDraft("");
                } else {
                    conversation.setDraft(draft.getContent());
                }

                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }

        }
    }

    public void onEventMainThread(Group groupInfo) {
        int count = this.mAdapter.getCount();
        if(groupInfo.getName() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation temp = (UIConversation)this.mAdapter.getItem(i);
                if(temp.getConversationTargetId().equals(groupInfo.getId())) {
                    temp.setUIConversationTitle(groupInfo.getName());
                    if(groupInfo.getPortraitUri() != null) {
                        temp.setIconUrl(groupInfo.getPortraitUri());
                    }

                    this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(Event.GroupUserInfoEvent event) {
        GroupUserInfo userInfo = event.getUserInfo();
        if(userInfo != null && userInfo.getNickname() != null) {
            RongContext context = RongContext.getInstance();
            if(context != null) {
                int count = this.mAdapter.getCount();

                for(int i = 0; i < count; ++i) {
                    UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                    String type = uiConversation.getConversationType().getName();
                    MessageContent messageContent = uiConversation.getMessageContent();
                    if(messageContent != null) {
                        IContainerItemProvider.MessageProvider provider = context.getMessageTemplate(messageContent.getClass());
                        if(provider != null) {
                            Spannable messageData = provider.getContentSummary(messageContent);
                            if(messageData != null && type.equals(Conversation.ConversationType.GROUP.getName()) && uiConversation.getConversationSenderId().equals(userInfo.getUserId())) {
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

                                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                            }
                        }
                    }
                }

            }
        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        if(userInfo != null && userInfo.getName() != null) {
            RongContext context = RongContext.getInstance();
            if(context != null) {
                int count = this.mAdapter.getCount();

                for(int i = 0; i < count; ++i) {
                    UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                    String type = uiConversation.getConversationType().getName();
                    MessageContent messageContent = uiConversation.getMessageContent();
                    if(!uiConversation.hasNickname(userInfo.getUserId()) && messageContent != null) {
                        IContainerItemProvider.MessageProvider provider = context.getMessageTemplate(messageContent.getClass());
                        if(provider != null) {
                            Spannable messageData = provider.getContentSummary(messageContent);
                            if(messageData != null) {
                                SpannableStringBuilder builder;
                                boolean isListened;
                                if((type.equals(Conversation.ConversationType.GROUP.getName()) || type.equals(Conversation.ConversationType.DISCUSSION.getName())) && uiConversation.getConversationSenderId().equals(userInfo.getUserId())) {
                                    builder = new SpannableStringBuilder();
                                    if(uiConversation.getMessageContent() instanceof VoiceMessage) {
                                        isListened = RongIM.getInstance().getRongIMClient().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId()).getReceivedStatus().isListened();
                                        if(isListened) {
                                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                                        } else {
                                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                                        }
                                    }

                                    builder.append(userInfo.getName()).append(" : ").append(messageData);
                                    uiConversation.setConversationContent(builder);
                                } else if(uiConversation.getConversationTargetId().equals(userInfo.getUserId())) {
                                    if(type.equals(Conversation.ConversationType.PRIVATE.getName())) {
                                        uiConversation.setUIConversationTitle(userInfo.getName());
                                    } else {
                                        builder = new SpannableStringBuilder();
                                        if(uiConversation.getMessageContent() instanceof VoiceMessage) {
                                            isListened = RongIM.getInstance().getRongIMClient().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId()).getReceivedStatus().isListened();
                                            if(isListened) {
                                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                                            } else {
                                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                                            }
                                        }

                                        builder.append(userInfo.getName()).append(" : ").append(messageData);
                                        uiConversation.setConversationContent(builder);
                                        uiConversation.setIconUrl(userInfo.getPortraitUri());
                                    }
                                }

                                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                            }
                        }
                    }
                }

            }
        }
    }

    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        if(this.isResumed()) {
            Drawable drawable = this.getActivity().getResources().getDrawable(R.drawable.rc_notification_network_available);
            int width = (int)this.getActivity().getResources().getDimension(io.rong.imkit.R.dimen.rc_message_send_status_image_size);
            drawable.setBounds(0, 0, width, width);
            this.mNotificationBar.setCompoundDrawablePadding(16);
            this.mNotificationBar.setCompoundDrawables(drawable, (Drawable)null, (Drawable)null, (Drawable)null);
            if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
                this.mNotificationBar.setVisibility(0);
                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
                //RongIM.getInstance().getRongIMClient().reconnect((RongIMClient.ConnectCallback)null);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                this.mNotificationBar.setVisibility(0);
                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick));
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                this.mNotificationBar.setVisibility(8);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                this.mNotificationBar.setVisibility(0);
                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                this.mNotificationBar.setVisibility(0);
                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting));
            }
        }

    }

    public void onEventMainThread(Discussion discussion) {
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(i);
            RongContext.getInstance().getConversationGatherState(temp.getConversationType().getName()).booleanValue();
            if(temp.getConversationTargetId().equals(discussion.getId())) {
                temp.setUIConversationTitle(discussion.getName());
                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                break;
            }
        }

    }

    public void onEventMainThread(PublicServiceProfile accountInfo) {
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            if(((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId().equals(accountInfo.getTargetId())) {
                ((UIConversation)this.mAdapter.getItem(i)).setIconUrl(accountInfo.getPortraitUri());
                ((UIConversation)this.mAdapter.getItem(i)).setUIConversationTitle(accountInfo.getName());
                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                break;
            }
        }

    }

    public void onEventMainThread(Event.ConversationUnreadEvent unreadEvent) {
        int targetIndex = this.mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
        if(targetIndex >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(targetIndex);
            temp.setUnReadMessageCount(0);
            this.mAdapter.getView(targetIndex, this.mList.getChildAt(targetIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(Event.ConversationTopEvent setTopEvent) throws IllegalAccessException {
        int originalIndex = this.mAdapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
        if(originalIndex >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(originalIndex);
            boolean originalValue = temp.isTop();
            int newIndex;
            if(originalValue) {
                temp.setTop(false);
                newIndex = ConversationListUtils.findPositionForCancleTop(originalIndex, this.mAdapter);
            } else {
                temp.setTop(true);
                newIndex = ConversationListUtils.findPositionForSetTop(temp, this.mAdapter);
            }

            if(originalIndex == newIndex) {
                this.mAdapter.getView(newIndex, this.mList.getChildAt(newIndex - this.mList.getFirstVisiblePosition()), this.mList);
            } else {
                this.mAdapter.remove(originalIndex);
                this.mAdapter.add(temp, newIndex);
                this.mAdapter.notifyDataSetChanged();
            }

        } else {
            throw new IllegalAccessException("the item has already been deleted!");
        }
    }

    public void onEventMainThread(Event.ConversationRemoveEvent removeEvent) {
        int originalIndex = this.mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
        if(originalIndex >= 0) {
            this.mAdapter.remove(originalIndex);
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {
        int originalIndex = this.mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
        if(originalIndex >= 0) {
            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
        int originalIndex = this.mAdapter.findPosition(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());
        if(clearMessagesEvent != null && originalIndex >= 0) {
            Conversation temp = RongIMClient.getInstance().getConversation(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());
            UIConversation uiConversation = UIConversation.obtain(temp, false);
            this.mAdapter.remove(originalIndex);
            this.mAdapter.add(UIConversation.obtain(temp, false), ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter));
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.MessageDeleteEvent event) {
        int count = this.mAdapter.getCount();

        for( int i = 0; i < count; ++i) {
            x=i;
            if(event.getMessageIds().contains(Integer.valueOf(((UIConversation)this.mAdapter.getItem(i)).getLatestMessageId()))) {
                RongIM.getInstance().getRongIMClient().getConversation(((UIConversation)mAdapter.getItem(i)).getConversationType(), ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                    public void onSuccess(Conversation conversation) {
                        if(conversation == null) {
                        } else {
                            UIConversation temp = UIConversation.obtain(conversation, false);
                            mAdapter.remove(x);
                            int newPosition = ConversationListUtils.findPositionForNewConversation(temp, mAdapter);
                            mAdapter.add(temp, newPosition);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                });
                break;
            }
        }

    }

    public void onEventMainThread(Event.OnMessageSendErrorEvent sendErrorEvent) {
        int index = this.mAdapter.findPosition(sendErrorEvent.getMessage().getConversationType(), sendErrorEvent.getMessage().getTargetId());
        if(index >= 0) {
            ((UIConversation)this.mAdapter.getItem(index)).setSentStatus(Message.SentStatus.FAILED);
            this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(Event.QuitDiscussionEvent event) {
        int index = this.mAdapter.findPosition(Conversation.ConversationType.DISCUSSION, event.getDiscussionId());
        if(index >= 0) {
            this.mAdapter.remove(index);
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.QuitGroupEvent event) {
        int index = this.mAdapter.findPosition(Conversation.ConversationType.GROUP, event.getGroupId());
        if(index >= 0) {
            this.mAdapter.remove(index);
            this.mAdapter.notifyDataSetChanged();
        }

    }
/*

    public void onEventMainThread(Event.MessageListenedEvent event) {
        int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
        if(originalIndex >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(originalIndex);
            if(temp.getLatestMessageId() == event.getLatestMessageId()) {
                Spannable content = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(event.getConversationType(), event.getTargetId()).getReceivedStatus().isListened();
                if(isListened) {
                    content.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, content.length(), 33);
                } else {
                    content.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, content.length(), 33);
                }

                temp.setConversationContent(content);
            }

            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }
*/

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiconversation = (UIConversation)mAdapter.getItem(position);
        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean type = RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(getActivity(), view, uiconversation);
            if(type) {
                return;
            }
        }

        Conversation.ConversationType type1 = uiconversation.getConversationType();
        uiconversation.setUnReadMessageCount(0);
        RongIM.getInstance().startConversation(getActivity(), type1, uiconversation.getConversationTargetId(), uiconversation.getUIConversationTitle());
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation)mAdapter.getItem(position);
        String title = uiConversation.getUIConversationTitle();
        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean builder = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(getActivity(), view, uiConversation);
            if(builder) {
                return true;
            }
        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(title);
        this.buildMultiDialog(uiConversation);
        return true;
    }

    private void buildMultiDialog(final UIConversation uiConversation) {
        String[] items = new String[2];
        if(uiConversation.isTop()) {
            items[0] = this.getActivity().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = this.getActivity().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top);
        }

        items[1] = this.getActivity().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove);
        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    RongIM.getInstance().getRongIMClient().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                        public void onSuccess(Boolean aBoolean) {
                            if(uiConversation.isTop()) {
                                Toast.makeText(RongContext.getInstance(), mSubConversationListFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_popup_cancel_top), 0).show();
                            } else {
                                Toast.makeText(RongContext.getInstance(), mSubConversationListFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top), 0).show();
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

    public boolean onBackPressed() {
        return false;
    }

    public void onDestroy() {
        RongContext.getInstance().getEventBus().unregister(this);
        getHandler().removeCallbacksAndMessages((Object)null);
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
    }

    public SubConversationListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(SubConversationListAdapter adapter) {
        if(mAdapter != null) {
            mAdapter.clear();
        }

        mAdapter = adapter;
        if(mList != null && this.getUri() != null) {
            mList.setAdapter(adapter);
            initFragment(this.getUri());
        }

    }

    private UIConversation makeUiConversation(Message message, int pos) {
        UIConversation uiConversation = null;
        if(pos >= 0) {
            uiConversation = (UIConversation)mAdapter.getItem(pos);
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
                MessageTag tag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
                if(message.getMessageDirection() == Message.MessageDirection.RECEIVE && (tag.flag() & 3) != 0) {
                    uiConversation.setUnReadMessageCount(uiConversation.getUnReadMessageCount() + 1);
                    List infoList = RongContext.getInstance().getCurrentConversationList();
                    Iterator i$ = infoList.iterator();

                    while(i$.hasNext()) {
                        ConversationInfo info = (ConversationInfo)i$.next();
                        if(info != null && info.getConversationType().equals(message.getConversationType()) && info.getTargetId().equals(message.getTargetId())) {
                            uiConversation.setUnReadMessageCount(0);
                        }
                    }
                } else {
                    uiConversation.setUnReadMessageCount(0);
                }
            }
        }

        return uiConversation;
    }
}