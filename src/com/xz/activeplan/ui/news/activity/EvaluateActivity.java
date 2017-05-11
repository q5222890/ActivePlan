package com.xz.activeplan.ui.news.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.CommentBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评价页面
 */
public class EvaluateActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener {

    private XListView listView;
    private List<CommentBean>listEvent = new ArrayList<>();
    private CommonAdapter<CommentBean> commonAdapter;
    private TextView textViewNoDate;
    private int teaID;
    private String timeUpStr;
    private SimpleDateFormat formatTime;
    private Date date;
    private int page;
    private int size=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_evaluate);
        ClassConcrete.getInstance().addObserver(this);
        initView();
    }
    private void initView() {
        String stringExtra = getIntent().getStringExtra(Utiles.DATA);
        if (TextUtils.isEmpty(stringExtra)){
            finish();
        }else {
            teaID = Integer.parseInt(stringExtra);
        }
        Utiles.headManager(EvaluateActivity.this,R.string.string_StudentEvaluate);
        //没有数据的时候显示的TextView
         textViewNoDate= (TextView) findViewById(R.id.id_TextViewNoData);
        listView = (XListView) findViewById(R.id.id_XListview);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            /**
             * 下拉刷新
             */
            @Override
            public void onRefresh() {
                // 刷新label的设置
                listView.setRefreshTime(timeUpStr);
                int time = getTime(false);
                if (time > 30) {
                    loadDate();
                    page = 0;
                } else {
                    ToastUtil.showShort("已经是最新数据！");
                }
                listView.stopRefresh();
            }

            /**
             * 上拉刷新
             */
            @Override
            public void onLoadMore() {
                loadDate();
                try {
                    listView.stopLoadMore();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        //xListView
         listView.setPullLoadEnable(true);
         listView.setPullRefreshEnable(true);
         listView.setEmptyView(textViewNoDate);
        commonAdapter = new CommonAdapter<CommentBean>(EvaluateActivity.this, listEvent, R.layout.view_evaluate_item) {
            @Override
            public void convert(ViewHolder holder, CommentBean commentBean, int position) {
                ImageView imageView = holder.getView(R.id.id_ImageHead);
                Picasso.with(mContext).load(commentBean.getUserInfo().getHeadurl()).error(R.drawable.default_head_bg)
                        .placeholder(R.drawable.default_head_bg).fit().centerCrop().into(imageView);
                holder.setText(R.id.id_TextViewName,commentBean.getUserInfo().getUsername());
                holder.setText(R.id.id_TextViewEvaluateContent,commentBean.getContent());
                holder.setText(R.id.id_TextViewTitle,commentBean.getGam_title());
                String s = TimeUtils.formatDate(commentBean.getTime());
                holder.setText(R.id.id_TextViewTime,s);
            }
        };
        listView.setAdapter(commonAdapter);
        loadDate();
    }

    /***
     * 加载数据
     */
 private void loadDate(){
     if (!NetworkInfoUtil.checkNetwork(EvaluateActivity.this)) {
         ToastUtil.showShort(getResources().getString(R.string.No_NetWork));
         textViewNoDate.setText(getResources().getString(R.string.network_check));
         return;
     }else {
         getTime(true);
         getCeleviteEvaluate01();
     }
 }
    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.TEACHER_INVITE_TYPE) {
            finish();
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
        }
    }
    /**
     * 这里实现用户评价
     */
    public void  getCeleviteEvaluate01(){
        CelebrityNewWorkImpl.getInstance().getCeleviteEvaluateList(teaID,page,size, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                        if (statusBean != null) {
                            if (!TextUtils.isEmpty(statusBean.getData())) {
                                List<CommentBean> commentBeen = JSON.parseArray(statusBean.getData(), CommentBean.class);
                                if (commentBeen.size()!=0) {
                                    page++;
                                    listEvent.addAll(commentBeen);
                                    commonAdapter.notifyDataSetChanged();
                                    if (commentBeen.size()<size){
                                        listView.setPullLoadEnable(false);
                                    }else {
                                        listView.setPullLoadEnable(true);
                                    }
                                }
                            }

                        }
                    }
                }
        )
        ;    }

    /**
     * 获取刷新时间
     */
    private int getTime(boolean b) {
        long time = System.currentTimeMillis();
        Date d1 = new Date(time);
        int i = 0;
        //时间
        if (formatTime == null) {
            formatTime = new SimpleDateFormat("HH:mm:ss");
        }
        if (b == true) {
            date = d1;
        }
        timeUpStr = formatTime.format(d1);
        if (date != null) {
            long startTime = date.getTime();
            i = (int) ((time - startTime) / 1000);
        }
        return i;
    }


}
