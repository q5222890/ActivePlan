package com.xz.activeplan.ui.personal.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.FollowAnchorBean;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.live.impl.LiveInfoServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.find.activity.TommorrowPersonActivity;
import com.xz.activeplan.ui.personal.adapter.AnchorFollowAdapter;
import com.xz.activeplan.ui.personal.adapter.OrganizersAdapter;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注
 */
@SuppressLint("ValidFragment")
public class FollowingFragment extends BaseFragment implements XListView.IXListViewListener, View.OnClickListener {


    private View mView;
    private OrganizersAdapter mOrganizersAdapter;
    private AnchorFollowAdapter mAnchorFollowAdapter;
    private List<FollowAnchorBean> anchorfollowList =new ArrayList<>();
    private List<OrganizersBean> mOrganizersList = new ArrayList<OrganizersBean>();
    private TextView textViewNoData;
    private XListView mListView;
    private int currpage = 0;
    private int pagesize = 10;
    private int type = 1;  // 1=关注的主播 2 =关注的主办方 3 =关注的名人
    private int userid = 0;

    public FollowingFragment(int type, int userid) {
        this.type = type;
        this.userid = userid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_following, container, false);
        initView();
        return mView;
    }

    private void initView() {
        Utiles.log("  initView");
        mListView = (XListView) mView.findViewById(R.id.xlistview_following);
        //没有数据TextView
        textViewNoData = (TextView) mView.findViewById(R.id.id_TextViewNoData);
        textViewNoData.setOnClickListener(this);
        mListView.setEmptyView(textViewNoData);
        mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(true);

        if(type==1){
            loadAnchorData();
        }
        if (type == 2) {
            loadOrganizersData();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utiles.log("   type:"+type);
                if(type==1){
                    if(anchorfollowList.size()>0){
                            FollowAnchorBean bean =anchorfollowList.get(position-1);
                            Utiles.log("我关注的主播bean:"+bean.toString());
                            Intent intent =new Intent(getActivity(), TommorrowPersonActivity.class);
                            LiveTvBean livetvBean =new LiveTvBean();
                            livetvBean.setHeadurl(bean.getHeadurl());
                            livetvBean.setUserid(bean.getOtheruserid());
                            livetvBean.setIsattend(bean.isIsAttend());
                            livetvBean.setFans(bean.getAttendcount()+"");
                            livetvBean.setUsername(bean.getUsername());
                            livetvBean.setSeenum(bean.getLivecount());
                            intent.putExtra("data",livetvBean);
                            startActivity(intent);
                    }
                }
            }
        });
    }


    /**
     * 获取我关注的主办方
     */
    private void loadOrganizersData() {
        mOrganizersAdapter = new OrganizersAdapter(getActivity(), mOrganizersList);
        mListView.setAdapter(mOrganizersAdapter);

        UserInfoBean mUserInfoBean = Utiles.getNetworkAndLogin_OK();
        if (mUserInfoBean==null){
            return;
        }
        UserInfoServiceImpl.getInstance().myHostList(mUserInfoBean.getUserid(), type, currpage, pagesize, new OkHttpClientManager.StringCallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(String response) {
                if (currpage <= 0) {
                    mListView.stopRefresh();
                } else {
                    mListView.stopLoadMore();
                }
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        OrganizersJson userInfoJson = new OrganizersJson();
                        obj = userInfoJson.analysisJson2Object(statusBean.getData());
                        if (obj != null) {
                            ArrayList<OrganizersBean> collectBeans = (ArrayList<OrganizersBean>) obj;
                            if (collectBeans != null) {
                                if (currpage <= 0) {
                                    mOrganizersList.clear();
                                }

                                if (collectBeans.size() < pagesize) {
                                    mListView.setPullLoadEnable(false);
                                } else {
                                    mListView.setPullLoadEnable(true);
                                }

                                if (collectBeans.size() > 0) {
                                    currpage++;
                                }
                                mOrganizersList.addAll(collectBeans);
                                mOrganizersAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                    }
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("数据获取失败，请刷新重试!");
                if (currpage <= 0) {
                    mListView.stopRefresh();
                } else {
                    mListView.stopLoadMore();
                }
            }
        });
    }


    /**
     * 获取我关注的主播
     */
    public void loadAnchorData() {
        mAnchorFollowAdapter =new AnchorFollowAdapter(getActivity(),anchorfollowList);
        mListView.setAdapter(mAnchorFollowAdapter);
        UserInfoBean mUserInfoBean = Utiles.getNetworkAndLogin_OK();
        if (mUserInfoBean==null){
            return;
        }
        LiveInfoServiceImpl.getInstance().getLiveFollow(mUserInfoBean.getUserid(), type, currpage, pagesize,
                new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("数据获取失败，请刷新重试!");
                if (currpage <= 0) {
                    mListView.stopRefresh();
                } else {
                    mListView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (currpage <= 0) {
                    mListView.stopRefresh();
                } else {
                    mListView.stopLoadMore();
                }
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    List<FollowAnchorBean> collectBeans = JSON.parseArray(statusBean.getData(), FollowAnchorBean.class);
                    if (collectBeans != null) {
                        if (currpage <= 0) {
                            anchorfollowList.clear();
                        }

                        if (collectBeans.size() < pagesize) {
                            mListView.setPullLoadEnable(false);
                        } else {
                            mListView.setPullLoadEnable(true);
                        }

                        if (collectBeans.size() > 0) {
                            currpage++;
                        }
                        anchorfollowList.addAll(collectBeans);
                        mAnchorFollowAdapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtil.showShort("数据获取失败，请刷新重试!");
                }


            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onRefresh() {

        currpage = 0;
        if(type==1){
            loadAnchorData();
        }
        if (type == 2) {
            loadOrganizersData();
        }

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_TextViewNoData:
                onRefresh();
                break;
        }
    }
}
