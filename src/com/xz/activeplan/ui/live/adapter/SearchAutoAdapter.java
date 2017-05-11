package com.xz.activeplan.ui.live.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.live.activity.LiveSearchActivity;
import com.xz.activeplan.ui.live.view.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 搜索适配器
 *
 *
 */
public class SearchAutoAdapter extends BaseAdapter {
    private final Object mLock = new Object();
    private Context mContext;
    private ArrayList<String> mOriginalValues;// 所有的Item
    private List<String> mObjects;// 过滤后的item
    private int mMaxMatch = 10;// 最多显示多少个选项,负数表示全部

    public SearchAutoAdapter(Context context, int maxMatch) {
        this.mContext = context;
        this.mMaxMatch = maxMatch;
        initSearchHistory();
        mObjects = mOriginalValues;
    }

    //得到数据总数
    @Override
    public int getCount() {
        if (mObjects != null) {
            if (mObjects.isEmpty()) {
                return 0;
            } else {
                return mObjects.size() + 1;
            }
        } else {
            return 0;
        }
    }

    //得到每一条数据
    @Override
    public Object getItem(int position) {
        if (mObjects != null) {
            if (!mObjects.isEmpty()) {
                if (position == mObjects.size()) {
                    return 0;
                } else {
                    return mObjects.get(position);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    //得到项目的位置
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ListView中所显示的item都是通过调用Adapter对象的getView方法来得到一个View对象
     * 然后把这个View对象放在这个item中，这样的一个过程，这就是ListView和Adapter之间的关系
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AutoHolder holder;
        RelativeLayout cleanHistoryLayout;
        final int location = position;
        if (mObjects != null) {
            if (!mObjects.isEmpty()) {
                if (position == mObjects.size()) {
                    /**清除历史记录*/
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.seach_clean_history_item, parent, false);
                    cleanHistoryLayout = (RelativeLayout) convertView.findViewById(R.id.clean_history_layout);
                    cleanHistoryLayout.setOnClickListener(new OnClickListener() {

                        @SuppressLint("NewApi")
                        @Override
                        public void onClick(View v) {

                            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_yesorno, null);
                            AlertDialog dialog = new AlertDialog(mContext, view,0.85f).builder();
                            dialog.show();
                            view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
                            ((TextView) view.findViewById(R.id.txt_msg)).setText("确定要清空搜索历史吗?");
                            dialog.setPositiveButton("取消", new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            dialog.setNegativeButton("确定", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences sp = mContext.getSharedPreferences(
                                            LiveSearchActivity.SEARCH_HISTORY, 0);
                                    String longhistory = sp.getString(LiveSearchActivity.SEARCH_HISTORY, "");
                                    if (longhistory.isEmpty()) {
                                    } else {
                                        cleanHistory();
                                        mObjects.clear();
                                        mOriginalValues.clear();
                                        notifyDataSetChanged();
                                    }
                                }
                            });

                        }
                    });
                } else {
                    /**搜索历史的Item*/
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.listitem_search_live, parent, false);
                    holder = new AutoHolder();
                    holder.content = (TextView) convertView
                            .findViewById(R.id.auto_content);
                    holder.addButton = (ImageView) convertView
                            .findViewById(R.id.auto_add);

                    holder.content.setText(mObjects.get(position));
                    holder.addButton.setTag(mObjects.get(position));

                    holder.addButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteItem(position);
                        }
                    });
                }
            }
        }
        return convertView;
    }

    //清除搜索记录
    public void cleanHistory() {
        SharedPreferences sp = mContext.getSharedPreferences(LiveSearchActivity.SEARCH_HISTORY, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     */
    protected void deleteItem(int position) {
        String data = mObjects.get(position);
        mObjects.remove(position);
        SharedPreferences sp = mContext.getSharedPreferences(
                LiveSearchActivity.SEARCH_HISTORY, 0);
        String longhistory = sp.getString(LiveSearchActivity.SEARCH_HISTORY, "");
        String[] tmpHistory = longhistory.split(",");
        ArrayList<String> history = new ArrayList<String>(
                Arrays.asList(tmpHistory));
        //数组检查
        if (history.size() > 0) {
            int i;
            for (i = 0; i < history.size(); i++) {
                if (data.equals(history.get(i))) {

                    history.remove(i);
                    break;
                }
            }

            mOriginalValues = history;

            StringBuilder sb = new StringBuilder();
            for (i = 0; i < history.size(); i++) {
                sb.append(history.get(i) + ",");
            }
            sp.edit().putString(LiveSearchActivity.SEARCH_HISTORY, sb.toString()).commit();
            Log.i("xianhua tag", "sp ok");
        }

        notifyDataSetChanged();
    }

    /**
     * 读取历史搜索记录
     */
    public void initSearchHistory() {
        SharedPreferences sp = mContext.getSharedPreferences(
                LiveSearchActivity.SEARCH_HISTORY, 0);
        String longhistory = sp.getString(LiveSearchActivity.SEARCH_HISTORY, "");
        String[] hisArrays = longhistory.split(",");
        mOriginalValues = new ArrayList<String>();
        if (!longhistory.contains(",")) {
            return;
        }
        if (hisArrays.length < 1) {
            return;
        }
        if (hisArrays.length > 10) {
            for (int i = 0; i < 10; i++) {
                mOriginalValues.add(hisArrays[i]);
            }
        } else {
            for (int i = 0; i < hisArrays.length; i++) {
                mOriginalValues.add(hisArrays[i]);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 匹配过滤搜索内容
     *
     * @param prefix 输入框中输入的内容
     */
    public void performFiltering(CharSequence prefix) {
        if (prefix == null || prefix.length() == 0) {//搜索框内容为空的时候显示所有历史记录
            synchronized (mLock) {
                mObjects = mOriginalValues;
            }
        } else {
            String prefixString = prefix.toString();
            int count = mOriginalValues.size();
            ArrayList<String> newValues = new ArrayList<String>(
                    count);
            for (int i = 0; i < count; i++) {
                final String value = mOriginalValues.get(i);
                final String valueText = value;
                if (valueText.contains(prefixString))    //valueText中是否包含有prefixString字段
                {
                }
                if (valueText.startsWith(prefixString)) {
                    newValues.add(valueText);
                } else {
                    final String[] words = valueText.split(" ");
                    final int wordCount = words.length;
                    for (int k = 0; k < wordCount; k++) {
                        if (words[k].startsWith(prefixString)) {
                            newValues.add(value);
                            break;
                        }
                    }
                }
                if (mMaxMatch > 0) {
                    if (newValues.size() > mMaxMatch - 1) {
                        break;
                    }
                }
            }
            mObjects = newValues;
        }
        notifyDataSetChanged();
    }

    private class AutoHolder {
        TextView content;
        ImageView addButton;
    }
}
