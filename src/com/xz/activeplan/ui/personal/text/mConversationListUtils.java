package com.xz.activeplan.ui.personal.text;

import com.xz.activeplan.utils.Utiles;

import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.BaseAdapter;

/**
 * Created by Administrator on 2016/7/1.
 */
public class mConversationListUtils {
    public mConversationListUtils() {
    }

    public static int findPositionForNewConversation(UIConversation uiconversation, mBaseAdapter<UIConversation> mAdapter) {
        int count = mAdapter.getCount();
        int position = 0;

        for(int i = 0; i < count; ++i) {
            if(uiconversation.isTop()) {
                if(!((UIConversation)mAdapter.getItem(i)).isTop() || ((UIConversation)mAdapter.getItem(i)).getUIConversationTime() <= uiconversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            } else {
                if(!((UIConversation)mAdapter.getItem(i)).isTop() && ((UIConversation)mAdapter.getItem(i)).getUIConversationTime() <= uiconversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            }
        }

        return position;
    }

    public static int findPositionForSetTop(UIConversation uiconversation, mBaseAdapter<UIConversation> mAdapter) {
        int count = mAdapter.getCount();
        int position = 0;
        for(int i = 0; i < count && ((UIConversation)mAdapter.getItem(i)).isTop() && ((UIConversation)mAdapter.getItem(i)).getUIConversationTime() > uiconversation.getUIConversationTime(); ++i) {
            ++position;
        }

        return position;
    }

    public static int findPositionForCancleTop(int index, mBaseAdapter<UIConversation> mAdapter) {
        int count = mAdapter.getCount();
        int tap = 0;
        if(index > count) {
            throw new IllegalArgumentException("the index for the position is error!");
        } else {
            for(int i = index + 1; i < count && (((UIConversation)mAdapter.getItem(i)).isTop() || ((UIConversation)mAdapter.getItem(index)).getUIConversationTime() < ((UIConversation)mAdapter.getItem(i)).getUIConversationTime()); ++i) {
                ++tap;
            }

            return index + tap;
        }
    }
}