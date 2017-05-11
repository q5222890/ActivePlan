package com.xz.activeplan.ui.personal.text;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.utils.Utiles;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 这里是自定义的会话列表
 */
public class mConversationListAdapter extends BaseAdapter {
	private Context context;
	private List<Conversation> data = new ArrayList<Conversation>();
	private List<ActiveinfosBean>activeinfosBeanList;
	private String userType;

	public mConversationListAdapter(Context context, List<Conversation> mList, ArrayList<ActiveinfosBean> listActiveinfosBean) {
		this.data = mList;
		this.context = context;
		this.activeinfosBeanList = listActiveinfosBean;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		Conversation item = (Conversation) getItem(position);

		//得到聊天类型
		//聊天室
		Conversation.ConversationType chatroom = Conversation.ConversationType.CHATROOM;
		//应用公共服务
		Conversation.ConversationType appPublicService = Conversation.ConversationType.APP_PUBLIC_SERVICE;
		//客户服务
		Conversation.ConversationType customerService = Conversation.ConversationType.CUSTOMER_SERVICE;
		//讨论
		Conversation.ConversationType discussion = Conversation.ConversationType.DISCUSSION;
		//组
		Conversation.ConversationType group = Conversation.ConversationType.GROUP;
		//没有人
		Conversation.ConversationType none = Conversation.ConversationType.NONE;
		//私人
		Conversation.ConversationType aPrivate = Conversation.ConversationType.PRIVATE;
		//公共服务
		Conversation.ConversationType publicService = Conversation.ConversationType.PUBLIC_SERVICE;
		//推送
		Conversation.ConversationType pushService = Conversation.ConversationType.PUSH_SERVICE;
		//系统
		Conversation.ConversationType system = Conversation.ConversationType.SYSTEM;


		//获取未读消息数
		int a = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.DISCUSSION,item.getTargetId());
		//得到发送时间
		long sentTime = item.getSentTime();
		//发送人的名字
		String conversationTitle = item.getConversationTitle();
		Utiles.log("========未读消息："+a+" 条"+"  发送人的名字："+conversationTitle);
		item.getPortraitUrl();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.conversation_list_item,
					null);
			holder = new Holder();
			holder.imageHead = (ImageView) convertView.findViewById(R.id.image);
			holder.textViewName = (TextView) convertView.findViewById(R.id.id_TextViewConversationName);
			holder.textViewContent = (TextView) convertView.findViewById(R.id.id_TextViewConversationContent);
			holder.textViewTime = (TextView) convertView.findViewById(R.id.id_TextViewConversationTime);
			holder.textViewNoReadingMessage = (TextView) convertView.findViewById(R.id.no_reading);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		String textMessageContent;
		UserInfo userInfo = null;
		if (a!=0) {
			holder.textViewNoReadingMessage.setVisibility(View.VISIBLE);
			if (a>99)
				holder.textViewNoReadingMessage.setText("...");
			else
				holder.textViewNoReadingMessage.setText(a+"");
		}else {
			holder.textViewNoReadingMessage.setVisibility(View.GONE);
		}
		String conversationTitle1 = item.getConversationTitle();
		Utiles.log("  "+conversationTitle1);
		String portraitUrl = item.getPortraitUrl();
		Utiles.log("  "+portraitUrl);
		MessageContent latestMessage = item.getLatestMessage();
		TextMessage latestMessage1 = (TextMessage) latestMessage;
		;

		Utiles.log("  "+latestMessage1.getContent());

		String senderUserName = item.getSenderUserName();
		Utiles.log("  "+senderUserName);
		int i = item.describeContents();
		Utiles.log("  "+i);
		String draft = item.getDraft();
		Utiles.log("  "+draft);
		int latestMessageId = item.getLatestMessageId();
		Utiles.log("  "+latestMessageId);
		/*Conversation.ConversationNotificationStatus notificationStatus = item.getNotificationStatus();
		String string = notificationStatus.toString();
		Utiles.log(" string: "+string+"  int:");*/
		String objectName = item.getObjectName();
		Utiles.log("  "+objectName);

		Message.ReceivedStatus receivedStatus = item.getReceivedStatus();

		Utiles.log("  "+receivedStatus);

		//信息内容
		MessageContent messageContent = item.getLatestMessage();
		if (messageContent instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) messageContent;
			textMessageContent = textMessage.getContent();
			userInfo = textMessage.getUserInfo();
			holder.textViewContent.setText(textMessageContent);
		}
		//图片消息
		else if (messageContent instanceof ImageMessage) {
			ImageMessage imageMessage = (ImageMessage) messageContent;
			String extra = imageMessage.getExtra();
			if (extra != null && !extra.equals("")) {
				String result = extra.substring(2, extra.length());
				if (result.equals("红包"))
					holder.textViewContent.setText("[同城达人币]");
				else 
					holder.textViewContent.setText("[图片]");
			} else
				holder.textViewContent.setText("[图片]");
			userInfo = imageMessage.getUserInfo();
		}
		//语音消息
		else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			holder.textViewContent.setText("[语音]");
			userInfo = voiceMessage.getUserInfo();
		}
		// 小灰条消息
		else if (messageContent instanceof InformationNotificationMessage) {
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			userInfo = informationNotificationMessage.getUserInfo();
		}
		// 图文消息
		else if (messageContent instanceof RichContentMessage) {
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			holder.textViewContent.setText("[同城达人活动]");
		}// 位置消息
		else if (messageContent instanceof LocationMessage) {
			LocationMessage locationMessage = (LocationMessage) messageContent;
			holder.textViewContent.setText("[位置]");
		} //其他消息
		else {
			holder.textViewContent.setText("");
		}

		if (userInfo == null) {
		}


		//收到消息时间
		Date date = new Date(sentTime);
		String friendly_time = StringUtils.friendly_times(date);
		holder.textViewTime.setText(friendly_time);
		return convertView;
	}

	class Holder {
		ImageView imageHead; // 图标
		TextView textViewName; // 姓名
		TextView textViewContent; // 内容
		TextView textViewTime; // 时间
		TextView textViewNoReadingMessage;//未读消息数״̬
	}

}
