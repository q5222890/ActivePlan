package com.xz.activeplan.ui.personal.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.MyActiveFansBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.find.activity.TommorrowPersonActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.personal.adapter.LiveFansBean;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FansFragment extends BaseFragment implements XListView.IXListViewListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private View mView;
    private XListView xListView;
    private int fansType;  // 1 =名人堂粉丝 2 =直播粉丝 3 =活动粉丝
    private List<MyActiveFansBean> myFansList = new ArrayList<>();  //活动粉丝列表
    private List<LiveFansBean> livefansList = new ArrayList<>();  //直播粉丝列表
    private List<LiveFansBean> listCelebris = new ArrayList<>();  //名人堂粉丝
    private CommonAdapter<MyActiveFansBean> fansAdapter;  //活动粉丝适配器
    private CommonAdapter<LiveFansBean> livefansAdapter;  //直播粉丝适配器
    private int currpage = 0;
    private int pagesize = 20;
    private int userid = 0;
    private TextView tv_noData;
    private Intent intent;
    private Bundle bundle;

    public FansFragment(int type, int userid) {
        this.fansType = type;
        this.userid = userid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fans, container, false);
        initView();
        return mView;
    }

    private void initView() {

        xListView = (XListView) mView.findViewById(R.id.xlistview_fans);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        tv_noData = (TextView) mView.findViewById(R.id.tv_noData);
        tv_noData.setOnClickListener(this);
        xListView.setEmptyView(tv_noData);
        xListView.setOnItemClickListener(this);
        //加载活动粉丝
        fansAdapter = new CommonAdapter<MyActiveFansBean>(getActivity(), myFansList, R.layout.listitem_myfans) {
            @Override
            public void convert(ViewHolder holder, MyActiveFansBean myActiveFansBean, int position) {

                holder.setText(R.id.tv_fansname, myActiveFansBean.getUsername());
                holder.setText(R.id.tv_fanscity, myActiveFansBean.getCity());
                ImageView imageView = holder.getView(R.id.circle_headview);
                if (!TextUtils.isEmpty(myActiveFansBean.getHeadurl()))
                    Picasso.with(mContext).load(myActiveFansBean.getHeadurl())
                            .error(R.drawable.default_head_bg)
                            .placeholder(R.drawable.default_head_bg).fit().centerCrop()
                            .into(imageView);
            }
        };

        //直播粉丝的适配器
        livefansAdapter = new CommonAdapter<LiveFansBean>(getActivity(), livefansList, R.layout.listitem_myfans) {
            @Override
            public void convert(ViewHolder holder, LiveFansBean liveFansBean, int position) {
                holder.setText(R.id.tv_fansname, liveFansBean.getUsername());
                holder.setText(R.id.tv_fanscity, liveFansBean.getCity());

                ImageView imageView = holder.getView(R.id.circle_headview);
                if (!TextUtils.isEmpty(liveFansBean.getHeadurl()))
                    Picasso.with(mContext).load(liveFansBean.getHeadurl())
                            .error(R.drawable.default_head_bg)
                            .placeholder(R.drawable.default_head_bg).fit().centerCrop()
                            .into(imageView);
            }
        };
        if (fansType == 1) {

        } else if (fansType == 2) {
            xListView.setAdapter(livefansAdapter);
            if (livefansList.size() == 0) {
                loadLiveFansData();
            } else {
                if (livefansList.size() < pagesize) {  //小于10条不能上啦加载
                    xListView.setPullLoadEnable(false);
                } else {
                    xListView.setPullLoadEnable(true);
                }
                livefansAdapter.notifyDataSetChanged();
            }
        } else if (fansType == 3) {
            xListView.setAdapter(fansAdapter);
            if (myFansList.size() == 0) {
                loadActiveFansData();
            } else {
                if (myFansList.size() < pagesize) {  //小于10条不能上啦加载
                    xListView.setPullLoadEnable(false);
                } else {
                    xListView.setPullLoadEnable(true);
                }
                fansAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 活动粉丝数据
     */
    private void loadActiveFansData() {

        UserInfoBean userinfo = SharedUtil.getUserInfo(getActivity());
        if (userinfo == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().getMyFans(userinfo.getUserid(), currpage, pagesize, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (currpage <= 0) {
                    xListView.stopRefresh();
                } else {
                    xListView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (currpage <= 0) {
                    xListView.stopRefresh();
                } else {
                    xListView.stopLoadMore();
                }
                StatusJson statusJson = new StatusJson();
                StatusBean statusBean = (StatusBean) statusJson.analysisJson2Object(response);
                if (statusBean != null && statusBean.getCode() == 0) {
                    List<MyActiveFansBean> myFansBeen = JSON.parseArray(statusBean.getData(), MyActiveFansBean.class);
                    if (currpage <= 0) {
                        myFansList.clear();
                    }
                    if (myFansBeen.size() < pagesize) {  //小于10条不能上啦加载
                        xListView.setPullLoadEnable(false);
                    } else {
                        xListView.setPullLoadEnable(true);
                    }
                    if (myFansBeen.size() > 0) {
                        currpage++;
                    }
                    myFansList.addAll(myFansBeen);
                    fansAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShort("数据获取失败，请刷新重试!");
                }
            }
        });
    }

    /**
     * 直播粉丝数据
     */
    private void loadLiveFansData() {
        UserInfoBean userinfoBean = SharedUtil.getUserInfo(getActivity());
        if (userinfoBean == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().getLiveFans(userinfoBean.getUserid(), currpage, pagesize, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (currpage <= 0) {
                    xListView.stopRefresh();
                } else {
                    xListView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (currpage <= 0) {
                    xListView.stopRefresh();
                } else {
                    xListView.stopLoadMore();
                }
                StatusJson statusJson = new StatusJson();
                StatusBean statusBean = (StatusBean) statusJson.analysisJson2Object(response);
                if (statusBean != null && statusBean.getCode() == 0) {
                    List<LiveFansBean> liveFansBeen = JSON.parseArray(statusBean.getData(), LiveFansBean.class);
                    if (currpage <= 0) {
                        livefansList.clear();
                    }
                    if (liveFansBeen.size() < pagesize) {
                        xListView.setPullLoadEnable(false);
                    } else {
                        xListView.setPullLoadEnable(true);
                    }
                    if (liveFansBeen.size() > 0) {
                        currpage++;
                    }
                    livefansList.addAll(liveFansBeen);
                    livefansAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShort("数据获取失败，请刷新重试!");
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        currpage = 0;
        if (fansType == 1) {
            tv_noData.setText(R.string.fans_empty);
        } else if (fansType == 2) {
            loadLiveFansData();
        } else if (fansType == 3) {
            loadActiveFansData();
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_noData:
                onRefresh();
                break;
        }
    }

    /**
     * listView的item点击事件
     * 根据粉丝类型判断跳转
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        if (fansType == 1) { //名人堂粉丝


        } else if (fansType == 2) {   //直播粉丝

            LiveFansBean bean =livefansList.get(position-1); //因加了headerView
            Utiles.log("我关注的主播bean:"+bean.toString());
            intent =new Intent(getActivity(), TommorrowPersonActivity.class);
            LiveTvBean livetvBean =new LiveTvBean();
            livetvBean.setHeadurl(bean.getHeadurl());
            livetvBean.setUserid(bean.getOtheruserid());
            livetvBean.setIsattend(bean.isIsAttend());
            livetvBean.setFans(bean.getAttendcount()+"");
            livetvBean.setUsername(bean.getUsername());
            livetvBean.setSeenum(bean.getLivecount());
            intent.putExtra("data",livetvBean);
            startActivity(intent);
        } else {   //活动粉丝

        }

    }
}
