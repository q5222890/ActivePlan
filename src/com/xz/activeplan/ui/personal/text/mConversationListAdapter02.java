package com.xz.activeplan.ui.personal.text;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.json.UserInfoJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.recommend.fragment.RecommendFragment;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.EventType;

import java.io.IOException;

import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2016/6/27.
 */
public class mConversationListAdapter02 extends mBaseAdapter<UIConversation>{
    private LayoutInflater inflater;
    private Context context;
    private String title;
    private String activeurl;

    public mConversationListAdapter02(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public long getItemId(int position) {
        UIConversation conversation = getItem(position);
        return conversation == null?0L:(long)conversation.hashCode();
    }

    //pub发现聚集的位置
    public int findGatherPosition(Conversation.ConversationType type) {
        int index = this.getCount();
        int position = -1;
        if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
            while(index-- > 0) {
                if(getItem(index).getConversationType().equals(type)) {
                    position = index;
                    break;
                }
            }
        }

        return position;
    }
    //找到位置
    public int findPosition(Conversation.ConversationType type, String targetId) {
        int index = this.getCount();
        int position = -1;
        if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
            while(index-- > 0) {
                if(this.getItem(index).getConversationType().equals(type)) {
                    position = index;
                    break;
                }
            }
        } else {
            while(index-- > 0) {
                if(this.getItem(index).getConversationType().equals(type) && this.getItem(index).getConversationTargetId().equals(targetId)) {
                    position = index;
                    break;
                }
            }
        }

        return position;
    }

    protected View newView(Context context, int position, ViewGroup group) {
        View view = inflater.inflate(R.layout.rc_item_conversation, null);
        mConversationListAdapter02.ViewHolder holder = new mConversationListAdapter02.ViewHolder();
        holder.layout = this.findViewById(view, R.id.rc_item_conversation);
        holder.leftImageLayout = this.findViewById(view,R.id.rc_item1);
        holder.rightImageLayout = this.findViewById(view, R.id.rc_item2);
        holder.leftImageView = (AsyncImageView)this.findViewById(view, R.id.rc_left);
        holder.rightImageView = (AsyncImageView)this.findViewById(view, R.id.rc_right);
        holder.contentView = (ProviderContainerView)this.findViewById(view, R.id.rc_content);
        holder.unReadMsgCount = (TextView)this.findViewById(view, R.id.rc_unread_message);
         holder.imageUnReadMessage = (ImageView)this.findViewById(view, R.id.id_ImageUnReadMessage);
        holder.unReadMsgCountRightIcon = (ImageView)this.findViewById(view, R.id.rc_unread_message_icon_right);
        view.setTag(holder);
       UIConversation  item = getItem(position);
        if (item!=null){
            Utiles.log("  itme !=null");
            if (item.getConversationType()!=EventType.CONVERSATION_TYPE){
                Utiles.log("  Type   !=  "+item.getConversationType());
                this.remove(position);
            }else {
                Utiles.log("  Type   ==  "+item.getConversationType());
            }
        }else {
            Utiles.log("  itme=null");
        }
        return view;
    }
    @Override
    protected void bindView(View v, int position, final UIConversation conversation) {
        mConversationListAdapter02.ViewHolder holder = (mConversationListAdapter02.ViewHolder)v.getTag();
        if (conversation==null){
            return;
            }
        if(conversation != null) {
            IContainerItemProvider.ConversationProvider provider = RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName());
            View view = holder.contentView.inflate(provider);
            provider.bindView(view, position, conversation);
            if(conversation.isTop()) {
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rc_item_top_list_selector));
            } else {
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rc_item_list_selector));
            }

            ConversationProviderTag tag = RongContext.getInstance().getConversationProviderTag(conversation.getConversationType().getName());
            if(tag.portraitPosition() == 1) {
                holder.leftImageLayout.setVisibility(0);
                if(conversation.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                    holder.leftImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_default_group_portrait));
                } else if(conversation.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                    holder.leftImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_default_discussion_portrait));
                } else {
                    holder.leftImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_default_portrait));
                }

                holder.leftImageLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(RongContext.getInstance() != null && RongContext.getInstance().getConversationListBehaviorListener() != null) {
                            RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(mConversationListAdapter02.this.context, conversation.getConversationType(), conversation.getConversationTargetId());
                        }

                    }
                });
                holder.leftImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if(RongContext.getInstance() != null && RongContext.getInstance().getConversationListBehaviorListener() != null) {
                            RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(mConversationListAdapter02.this.context, conversation.getConversationType(), conversation.getConversationTargetId());
                        }

                        return true;
                    }
                });
                if(conversation.getIconUrl() != null) {
                    Picasso.with(context).load(conversation.getIconUrl()).placeholder(R.drawable.rc_default_group_portrait).
                            error(R.drawable.rc_default_group_portrait).
                            fit().centerCrop().into(holder.leftImageView);
                } else {
                    holder.leftImageView.setResource(null);
                }

                if(conversation.getUnReadMessageCount() > 0) {
                    holder.imageUnReadMessage.setVisibility(0);
                    if(conversation.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                        if(conversation.getUnReadMessageCount() > 99) {
                            holder.unReadMsgCount.setText(context.getResources().getString(R.string.rc_message_unread_count));
                        } else {
                            holder.unReadMsgCount.setText(Integer.toString(conversation.getUnReadMessageCount()));
                        }
                        holder.unReadMsgCount.setVisibility(0);
                        holder.imageUnReadMessage.setImageResource(R.drawable.rc_unread_count_bg);
                    } else {
                        holder.unReadMsgCount.setVisibility(8);
                        holder.imageUnReadMessage.setImageResource(R.drawable.rc_unread_remind_without_count);
                    }
                } else {
                    holder.imageUnReadMessage.setVisibility(8);
                }

                holder.rightImageLayout.setVisibility(8);
            } else if(tag.portraitPosition() == 2) {
                holder.rightImageLayout.setVisibility(0);
                holder.rightImageLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
                            RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(mConversationListAdapter02.this.context, conversation.getConversationType(), conversation.getConversationTargetId());
                        }

                    }
                });
                holder.rightImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(mConversationListAdapter02.this.context, conversation.getConversationType(), conversation.getConversationTargetId());
                        return true;
                    }
                });
                if(conversation.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                    holder.rightImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(io.rong.imkit.R.drawable.rc_default_group_portrait));
                } else if(conversation.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                    holder.rightImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(io.rong.imkit.R.drawable.rc_default_discussion_portrait));
                } else {
                    holder.rightImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(io.rong.imkit.R.drawable.rc_default_portrait));
                }
                if(conversation.getIconUrl() != null) {
                    Picasso.with(context).load(conversation.getIconUrl()).placeholder(R.drawable.rc_default_group_portrait).
                            error(R.drawable.rc_default_group_portrait).
                            fit().centerCrop().into(holder.rightImageView);
                } else {
                    holder.rightImageView.setResource(null);
                }

                if(conversation.getUnReadMessageCount() > 0) {
                    holder.unReadMsgCountRightIcon.setVisibility(0);
                    if(conversation.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                        holder.unReadMsgCount.setVisibility(0);
                        if(conversation.getUnReadMessageCount() > 99) {
                            holder.unReadMsgCountRight.setText(this.context.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                        } else {
                            holder.unReadMsgCountRight.setText(Integer.toString(conversation.getUnReadMessageCount()));
                        }

                        holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                    } else {
                        holder.unReadMsgCount.setVisibility(8);
                        holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_without_count);
                    }
                } else {
                    holder.imageUnReadMessage.setVisibility(8);
                    holder.unReadMsgCount.setVisibility(8);
                }
                holder.leftImageLayout.setVisibility(8);
            } else {
                if(tag.portraitPosition() != 3) {
                    throw new IllegalArgumentException("the portrait position is wrong!");
                }
                holder.rightImageLayout.setVisibility(8);
                holder.leftImageLayout.setVisibility(8);
            }
            if (conversation.getConversationType().equals(Conversation.ConversationType.DISCUSSION))
                Utiles.log(" 讨论");
                conversation.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);

            if(conversation.getConversationType().equals(
                    Conversation.ConversationType.GROUP)){

                UserInfoBean userInfoId = SharedUtil.getUserInfoId(context, conversation.getConversationTargetId()+"GROUP");
                if (TextUtils.isEmpty(userInfoId.getHeadurl())){
                    Utiles.log("--------GROUP == null>"+conversation.getConversationTargetId());
                    title = SharedUtil.get(context, conversation.getConversationTargetId() + "-GROUP");
                    for (int i = 0; i < RecommendFragment.listActiveinfosBean.size(); i++) {
                        if (RecommendFragment.listActiveinfosBean.get(i).getName().equals(title)) {
                           activeurl = RecommendFragment.listActiveinfosBean.get(i).getActiveurl();
                        }
                    }
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUserid(Integer.parseInt(conversation.getConversationTargetId()));
                    userInfoBean.setUsername(title);
                    userInfoBean.setHeadurl(activeurl);
                    SharedUtil.saveUserInfoId(context,conversation.getConversationTargetId()+"GROUP",userInfoBean);
                }else {
                    conversation.setUIConversationTitle(userInfoId.getUsername()+"");
                    conversation.setIconUrl(Uri.parse(userInfoId.getHeadurl()));
                }

            }else if(conversation.getConversationType().equals(
                    Conversation.ConversationType.PRIVATE)){
                UserInfoBean userInfoId = SharedUtil.getUserInfoId(context, conversation.getConversationTargetId()+"PRIVATE");
                if (TextUtils.isEmpty(userInfoId.getHeadurl())){
                    getUserInfo(Integer.parseInt(conversation.getConversationTargetId()),conversation);
                }else {
                    Utiles.log("--------对象!== null>"+conversation.getConversationTargetId());
                    conversation.setUIConversationTitle(userInfoId.getUsername()+"");
                    conversation.setIconUrl(Uri.parse(userInfoId.getHeadurl()));
                }

            }

        }
    }

    class ViewHolder {
        View layout;
        View leftImageLayout;
        View rightImageLayout;
        AsyncImageView leftImageView;
        TextView unReadMsgCount;
        ImageView imageUnReadMessage;
        AsyncImageView rightImageView;
        TextView unReadMsgCountRight;
        ImageView unReadMsgCountRightIcon;
        ProviderContainerView contentView;
        ViewHolder() {
        }
    }
    private void getUserInfo(final int userid,  final UIConversation conversation) {
        try {
            UserInfoServiceImpl.getInstance().getUserinfo(userid, new OkHttpClientManager.StringCallback() {
                @Override
                public void onResponse(String response) {
                    Utiles.log("--------;response"+response);
                    StatusJson statusJosn = new StatusJson();
                    Object obj = statusJosn.analysisJson2Object(response);
                    if (obj != null) {
                        StatusBean statusBean = (StatusBean) obj;
                        if (statusBean.getCode() == 0) {
                            UserInfoJson userInfoJson = new UserInfoJson();
                            obj = userInfoJson.analysisJson2Object(statusBean.getData());
                            if (obj != null) {
                                UserInfoBean userInfoBean = (UserInfoBean) obj;
                                if (userInfoBean != null) {
                                    if (conversation.getConversationType().equals( Conversation.ConversationType.PRIVATE)) {
                                        SharedUtil.saveUserInfoId(context, userid + "PRIVATE", userInfoBean);
                                    }else if (conversation.getConversationType().equals( Conversation.ConversationType.GROUP)){
                                        SharedUtil.saveUserInfoId(context, userid + "GROUP", userInfoBean);
                                    }else if (conversation.getConversationType().equals( Conversation.ConversationType.SYSTEM)){
                                        SharedUtil.saveUserInfoId(context, userid + "SYSTEM", userInfoBean);
                                    }
                                    Utiles.log("------头像："+userInfoBean.getHeadurl());
                                    conversation.setUIConversationTitle(userInfoBean.getUsername()+"");
                                    conversation.setIconUrl(Uri.parse(userInfoBean.getHeadurl()));
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Request request, IOException e) {
                }
            });
        } catch (Exception e) {

        }
    }
}
