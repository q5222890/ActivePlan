package com.xz.activeplan.ui.personal.text;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public abstract class mBaseAdapter<T> extends android.widget.BaseAdapter {
    Context context;
    List<T> list;

    public mBaseAdapter() {
        list = new ArrayList();
    }


    public mBaseAdapter(Context context) {
        context = context;
        list = new ArrayList();
    }

    protected <T extends View> View findViewById(View view, int id) {
        return  view.findViewById(id);

    }

    public int findPosition(T message) {
        int index = getCount();
        int position = -1;

        while (index-- > 0) {
            if (message.equals(getItem(index))) {
                position = index;
                break;
            }
        }

        return position;
    }

    public int findPosition(long id) {
        int index = getCount();
        int position = -1;

        while (index-- > 0) {
            if (this.getItemId(index) == id) {
                position = index;
                break;
            }
        }

        return position;
    }

    public void addCollection(Collection<T> collection) {
        list.addAll(collection);
    }

    public void addCollection(T... collection) {
        Object[] arr$ = collection;
        int len$ = collection.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Object t = arr$[i$];
            list.add((T) t);
        }

    }

    public void add(T t) {
        list.add(t);
    }

    public void add(T t, int position) {
        list.add(position, t);
    }

    public void remove(int position) {
        list.remove(position);
    }

    public void removeAll() {
        list.clear();
    }

    public void clear() {
        list.clear();
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public T getItem(int position) {
        return list == null ? null : (position >= list.size() ? null : list.get(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = newView(context, position, parent);
        }

        this.bindView(view, position, getItem(position));
        return view;
    }

    protected abstract View newView(Context var1, int var2, ViewGroup var3);

    protected abstract void bindView(View var1, int var2, T var3);
}
