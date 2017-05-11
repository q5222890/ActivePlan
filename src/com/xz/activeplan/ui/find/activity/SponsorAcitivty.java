package com.xz.activeplan.ui.find.activity;

import android.os.Bundle;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.SponsorsSupportBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.recommend.adapter.SponsorsSupportAdapter;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.views.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 赞助页面
 */
public class SponsorAcitivty extends BaseFragmentActivity implements ClassObserver, XListView.IXListViewListener {

    private XListView xlistview;
    private List<SponsorsSupportBean> sponsorsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);
        Utiles.headManager(this, R.string.string_MyWantSponsor);
        Utiles.setGone(findViewById(R.id.view_top));
        ClassConcrete.getInstance().addObserver(this);
        xlistview = (XListView) findViewById(R.id.xlistview_support);
        xlistview.setPullRefreshEnable(false);
        xlistview.setPullLoadEnable(false);
        xlistview.setXListViewListener(this);

        initData();
    }

    private void initData() {

//        for (int i = 0; i <5 ; i++) {
//            SponsorsSupportBean bean =new SponsorsSupportBean();
//            bean.setContactname("王宝强"+i);
//            bean.setContactphone("13916847596"+i);
//            bean.setSponsorcontent("就凭借我的评价为我都快打开请问确定 去【框的气温达是的 的");
//            bean.setSupportprice("￥：500000.00"+i);
//            bean.setSupporttype("友情赞助"+i);
//            sponsorsList.add(bean);
//        }

        SponsorsSupportAdapter adapter = new SponsorsSupportAdapter(sponsorsList, this);
        xlistview.setAdapter(adapter);

    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
