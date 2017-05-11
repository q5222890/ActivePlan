package com.xz.activeplan.ui.live.test.template;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.utils.BitmapUtil;
import com.xz.activeplan.views.CircleImageView;

import io.rong.imkit.model.UIMessage;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;

@TemplateTag(messageContent = InformationNotificationMessage.class)
public class InformationNotificationMessageTemplate implements BaseMessageTemplate {
    private final static String TAG = "InformationNotificationMessageTemplate";
    private Bitmap bitmapFromUrl;

    @Override
    public View getView(View convertView, int position, ViewGroup parent, UIMessage data) {
        //RLog.e( TAG, "getView " + position + " " + convertView);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_infor_message, null);

            holder.content = (TextView) convertView.findViewById(R.id.rc_content);
            holder.ivHeader = (CircleImageView) convertView.findViewById(R.id.rc_ivHeader);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message msg = data.getMessage();
        final UserInfo userInfo = msg.getContent().getUserInfo();
        if(userInfo!=null)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmapFromUrl = BitmapUtil.getBitmapFromUrl(userInfo.getPortraitUri().toString());
                }
            }).start();
            holder.ivHeader.setImageBitmap(bitmapFromUrl);
        }
        InformationNotificationMessage infoMsg = (InformationNotificationMessage) msg.getContent();
        holder.content.setText(infoMsg.getMessage());
        return convertView;
    }

    @Override
    public void onItemClick(View view, int position, UIMessage data) {

    }

    @Override
    public void onItemLongClick(View view, int position, UIMessage data) {

    }

    @Override
    public void destroyItem(ViewGroup group, Object template) {

    }

    private class ViewHolder {
        TextView content;
        CircleImageView ivHeader;
    }
}