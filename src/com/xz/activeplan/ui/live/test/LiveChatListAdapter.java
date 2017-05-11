package com.xz.activeplan.ui.live.test;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.live.test.template.BaseContainerView;
import com.xz.activeplan.ui.live.test.template.BaseMessageTemplate;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.model.UIMessage;
import io.rong.imlib.model.Message;

public class LiveChatListAdapter extends BaseAdapter {

    private static final String TAG = "LiveChatListAdapter";

    private List<UIMessage> uiMessageList = new ArrayList<UIMessage>();
    private UIMessage data;
    public void addMessage(Message message) {
        uiMessageList.add(UIMessage.obtain(message));
    }
    @Override
    public int getCount() {
        Utiles.log("消息的数量："+uiMessageList.size());
        return uiMessageList.size();
    }

    @Override
    public UIMessage getItem(int position) {
        return uiMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
       Log.d(TAG, "getView position = " + position + ", convertView = " + convertView);
       ViewHolder holder;
       final UIMessage data = uiMessageList.get(position);
       if (convertView == null) {
           convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_livechat_message, null);
           holder = new ViewHolder();
           // rc_infor_message
           //内容
           holder.baseContainerView = (BaseContainerView) convertView.findViewById(R.id.rc_content);
           //头像
         //  holder.baseContainerView = (BaseContainerView) convertView.findViewById(R.id.rc_ivHeader);
           convertView.setTag(holder);
       } else {
           holder = (ViewHolder) convertView.getTag();
       }

       Message msg = data.getMessage();
       final BaseMessageTemplate template = MyRongIM.getInstance().getMessageTemplate(msg.getContent().getClass());
       // TextMessageTemplate textMessageTemplate = (TextMessageTemplate) MyRongIM.getInstance().getMessageTemplate(msg.getContent().getClass());

     /*  if(textMessageTemplate!=null)
       {
           holder.baseContainerView.inflate(textMessageTemplate,position,msg.getObjectName(),data);
       }*/
       if (template != null) {
           View view = holder.baseContainerView.inflate(template, position, msg.getObjectName(), data);
          /* view.setOnClickListener(new View.OnClickListeOverridener() {
               @Override
               public void onClick(View v) {
                   LogUtil.show(TAG,"----点击了view");
                   template.onItemClick(v, position, data);
               }
           });*/
       }

       return convertView;
   }

    private class ViewHolder {
        public BaseContainerView baseContainerView;
    }
}
