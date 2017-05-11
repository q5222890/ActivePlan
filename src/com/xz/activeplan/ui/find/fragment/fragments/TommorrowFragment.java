package com.xz.activeplan.ui.find.fragment.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.activity.TommorrowPersonActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.LiveChatRoomActivity;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.UtileStringRequst;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 直播——明日之星
 * 头像地址需要改变
 */
public class TommorrowFragment extends BaseFragment implements View.OnClickListener {
    private static TommorrowFragment instance = null;
    public boolean isattend = false;   //是否关注
    private XListView listView;
    private FragmentActivity activity;
    private UtileStringRequst<LiveTvBean> utileStringRequst;
    private HandlerTommorrow handlerTommorrow;
    private List<LiveTvBean> listLiveTvBeen = new ArrayList<>();
    private SimpleDateFormat formatTime;
    private CommonAdapter<LiveTvBean> commonAdapter;
    private String timeUpStr;
    private Date date;
    private int page = 0;
    private LiveTvBean bean;

    public static TommorrowFragment getInstance() {
        if (instance == null) {
            instance = new TommorrowFragment();
        }
        return instance;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tommorrow, container, false);
        initView(view);//初始化数据
        if (listLiveTvBeen.size() == 0) {
            loadDataXListView();//加载数据
        }
        return view;
    }

    private void initView(View view) {
        listView = (XListView) view.findViewById(R.id.id_XListview);
        listView.setPullLoadEnable(true);
        activity = getActivity();
        utileStringRequst = new UtileStringRequst<LiveTvBean>();
        handlerTommorrow = new HandlerTommorrow();
        listView.setXListViewListener(new XListView.IXListViewListener() {
            /**
             * 下拉刷新
             */
            @Override
            public void onRefresh() {
                listView.setRefreshTime(timeUpStr);
                    loadDataXListView();
                    page = 0;
                listView.stopRefresh();
            }
            /**
             * 上拉刷新
             */
            @Override
            public void onLoadMore() {
                loadDataXListView();
                listView.stopLoadMore();
            }
        });

        Utiles.log("   list:" + listLiveTvBeen.size());
        /**
         * 加载适配器 并为控件加载数据
         */
        commonAdapter = new CommonAdapter<LiveTvBean>(activity, listLiveTvBeen, R.layout.tomorrow_list_item) {

            @Override
            public void convert(final ViewHolder holder, final LiveTvBean liveTvBean, final int position) {
                View view = holder.getConvertView();
                bean = getItem(position);
                //用户头像
                ImageView imageViewHeadPhoto = holder.getView(R.id.id_ImageHeadPhoto);
                //视频封面
                ImageView imageViewActivity = holder.getView(R.id.id_ImageViewActivity);
                //粉丝数量
                TextView textViewFlans = holder.getView(R.id.id_TextViewFlans);
                //作者人名
                TextView textViewTommorrowTitle = holder.getView(R.id.id_TextViewTommorrowTitle);
                //标题
                TextView textViewTommorrowContent = holder.getView(R.id.id_TextViewTommorrowContent);
                //预约
                TextView textViewAppointment = holder.getView(R.id.id_TextAppointment);
                //排序
                TextView textViewNum = holder.getView(R.id.id_TextViewNum);
                //关注
                final Button btFollow = holder.getView(R.id.idButtonGuanZhu);
                //视频
                LinearLayout llVideo = holder.getView(R.id.tomorrow_item_llVideo);

                //预约1、正在直播2、回放3
                TextView tvStatus = holder.getView(R.id.id_TextAppointment);
                String status = getItem(position).getStatus();
                if (!TextUtils.isEmpty(status)) {
                    tvStatus.setText(status);
                }
                //手机号码
                if (Utils.checkMobileNumber(bean.getUsername()) == true) {
                    String number = Utils.formatMobileNumber(bean.getUsername());
                    textViewTommorrowTitle.setText(number);
                } else {
                    textViewTommorrowTitle.setText(bean.getUsername());
                }
                //点击视频封面跳转到视频播放页面
                llVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getVideoPlayCount(bean.getLiveid());
                        LiveTvBean lv = getItem(position);
                        String status = lv.getStatus(); //播放状态
                        if (status.equals("回放"))
                        {
                            Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,lv);
                        }else
                        {
                            Utiles.skipActivityForLiveTvBean(LiveChatRoomActivity.class,lv);
                        }
                    }
                });
                Picasso.with(activity).load(bean.getCoverurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(imageViewActivity);
                Picasso.with(activity).load(bean.getHeadurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(imageViewHeadPhoto);
                imageViewActivity.setScaleType(ImageView.ScaleType.FIT_XY);
                textViewNum.setText(position + 1 + "");
                //粉丝数量*
                textViewFlans.setText(bean.getFans() + activity.getResources().getString(R.string.Flans));

                // 视频的标题
                textViewTommorrowContent.setText(bean.getTitle());

                if (position == 0 || position == 1) {
                    textViewNum.setTextColor(activity.getResources().getColor(R.color.red2));
                } else {
                    textViewNum.setTextColor(activity.getResources().getColor(R.color.black));
                }
                // 用户头像跳转到主播详情页面
                imageViewHeadPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, TommorrowPersonActivity.class);
                        intent.putExtra("data", getItem(position));
                        activity.startActivity(intent);
                    }
                });
                //初始化关注与已关注
                if (listLiveTvBeen.get(position).isIsattend()) {
                    btFollow.setText("已关注");
                    btFollow.setSelected(true);
                    isattend = true;
                } else {
                    btFollow.setText("关注");
                    btFollow.setSelected(false);
                    isattend = false;
                }

                //点击关注或已关注按钮*
                btFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(SharedUtil.isLogin(activity)==true)
                        {
                            if (btFollow.getText().toString().equals("已关注")) {
                                btFollow.setText("关注");
                                btFollow.setSelected(false);
                                liveTvBean.setIsattend(false);
                                cancelFollow(liveTvBean);
                            } else {
                                liveTvBean.setIsattend(true);
                                btFollow.setText("已关注");
                                btFollow.setSelected(true);
                                follow(liveTvBean);
                            }
                            listLiveTvBeen.set(position, liveTvBean);
                        }else
                        {
                            Utiles.skipNoData(LoginActivity.class);
                        }
                    }
                });

            }
        };
        listView.setAdapter(commonAdapter);
    }

    /**
     * 获取视频的播放数量
     */
    private void getVideoPlayCount(int liveId) {
        OkHttpClientManager.Param liveID = new OkHttpClientManager.Param("liveid", liveId + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_VIDEO_PLAY_COUNT, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(String response) {

            }
        }, liveID);
    }

    /**
     * 加载数据
     */
    public void loadDataXListView() {
        if (!NetworkInfoUtil.checkNetwork(activity)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return;
        } else {
            getTime(true);
            if (listLiveTvBeen.size() == 0) {
                UserInfoBean userInfo = SharedUtil.getUserInfo(getActivity());
                if (null != userInfo) {
                    utileStringRequst.UtileStringRequst(getActivity(), UrlsManager.URL_LIVE_TOMMORROW + page + "&pagesize=20&userid=" + userInfo.getUserid(),
                            LiveTvBean.class, handlerTommorrow, 10);
                }
            } else {
                commonAdapter.notifyDataSetChanged();
            }
        }
    }

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

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        // loadDataXListView();
        super.onResume();

    }

    /**
     * 关注
     */
    private void follow(LiveTvBean bean) {
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().followPerson(networkAndLogin_ok.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("======>:02" + response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() == 0) {
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.loadFailed();
            }
        });
    }


    /**
     * 取消关注
     */
    private void cancelFollow(LiveTvBean bean) {
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok == null) {
            return;
        }
        UserInfoServiceImpl.getInstance().cancelFollowPerson(networkAndLogin_ok.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("  01:" + response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() != 0) {
                } else {
                    Utiles.loadFailed();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                //ToastUtil.showShort("取消关注失败!") ;
            }
        });
    }

    private class HandlerTommorrow extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    List<LiveTvBean> list = utileStringRequst.list;
                    if (list.size() > 0 && null != list) {
                        listLiveTvBeen.addAll(list);
                        page++;
                        commonAdapter.notifyDataSetChanged();
                    }
                    if (list.size() < 4) {
                        listView.setPullLoadEnable(false);
                    }

                    if (page <= 0) {       //当前为第一页
                        listLiveTvBeen.clear();
                    }

                    if (listLiveTvBeen.size() < 20) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                    break;
            }
            if (utileStringRequst.list != null) {
                utileStringRequst.list.clear();
            }
            handlerTommorrow.removeMessages(msg.what);
        }
    }
}
