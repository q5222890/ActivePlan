package com.xz.activeplan.ui.personal.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.activeplan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class BalanceToggleAdapter extends PagerAdapter {

    private Context context;
    private List<LinearLayout> balancelist =new ArrayList<>();

    public BalanceToggleAdapter(Context context, List<LinearLayout> balancelist) {
        this.context = context;
        this.balancelist = balancelist;
    }

    @Override
    public int getCount() {
        return balancelist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return false;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager)container).removeView(balancelist.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.vp_balancelistitem,null);
        TextView balance = (TextView) view.findViewById(R.id.tv_balance);
        TextView money = (TextView) view.findViewById(R.id.tv_money);


        ((ViewPager)container).addView(view);

        return view;
    }
}
