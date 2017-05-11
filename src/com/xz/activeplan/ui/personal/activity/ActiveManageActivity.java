package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.CollectBean;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.SignUpBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.personal.adapter.CollectAdapter;
import com.xz.activeplan.ui.recommend.activity.SelectTicketActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/1.
 * 活动管理
 */
public class ActiveManageActivity extends BaseFragmentActivity implements View.OnClickListener, ClassObserver, AdapterView.OnItemClickListener, XListView.IXListViewListener {
    public static int hostid = 1;
    public static int type = -1; //1=已发布活动, 0=已结束活动
    public OrganizersBean organizersBean;
    private TextView textView01, textView02;
    private TextView involved, sponsor, textViewMessage, textViewSignUp;
    private View imageMannager, imageSingnUp, viewBlackGround;
    private int one = 0;
    private int two = 0;
    private XListView listView;
    private List<String> listString = new ArrayList<String>();
    private List<CollectBean> listCollect = new ArrayList<CollectBean>();
    private List<SignUpBean> listSignUp = new ArrayList<SignUpBean>();
    private CommonAdapter<String> commonAdapter;
    private int num;
    private UserInfoBean userInfoBean;
    private int currpageMyCollect = 0;
    private int currpageSingeup = 0;
    private int pagesize = 10;
    private CollectAdapter collectAdapter;
    private CustomProgressDialog mProgressDialog;
    private CommonAdapter<SignUpBean> signUpCommonAdapter;
    private CustomProgressDialog customProgressDialog;
    private int userId;

    private void listClear() {
        listString.clear();
        listCollect.clear();
        listSignUp.clear();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ClassConcrete.getInstance().addObserver(this);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_activemanage);
        initView();
    }

    /**
     * 给ListView加载数据
     */
    private void loadData() {
        switch (one) {
            case 0:
                if (two == 0) {  //我的票券
                    listView.setPullLoadEnable(false);
                    listView.setPullRefreshEnable(false);
                    num = 1;
                    listString.clear();
                    listString.add(getResources().getString(R.string.string_CompletedTicket));
                    listString.add(getResources().getString(R.string.string_UnfinishedTicketing));
                    listView.setAdapter(commonAdapter);
                    commonAdapter.notifyDataSetChanged();
                }
                if (two == 1) {  //我的收藏
                    listView.setPullLoadEnable(true);
                    listView.setPullRefreshEnable(true);
                    num = 2;
                    if (listCollect.size() != 0) {
                        listView.setAdapter(collectAdapter);
                        collectAdapter.notifyDataSetChanged();
                        if (listCollect.size() < pagesize) {
                            listView.setPullLoadEnable(false);
                        }
                        return;
                    }
                    userInfoBean = SharedUtil.getUserInfo(ActiveManageActivity.this);
                    listView.setAdapter(collectAdapter);
                    loadDataMyCollection();
                }
                break;
            case 1:
                if (two == 0) {  //管理活动
                    listView.setPullLoadEnable(false);
                    listView.setPullRefreshEnable(false);
                    num = 3;
                    listString.clear();
                    listString.add(getResources().getString(R.string.string_announcement));
                    listString.add(getResources().getString(R.string.string_finished));
                    listString.add(getResources().getString(R.string.string_set_sponsorinfo));
                    listView.setAdapter(commonAdapter);
                    commonAdapter.notifyDataSetChanged();

                }
                if (two == 1) {  //管理报名
                    listView.setPullLoadEnable(true);
                    listView.setPullRefreshEnable(true);
                    if (listSignUp.size() != 0) {
                        listView.setAdapter(signUpCommonAdapter);
                        signUpCommonAdapter.notifyDataSetChanged();
                        if (listSignUp.size() < pagesize) {
                            listView.setPullLoadEnable(false);
                        }
                        return;
                    }
                    userInfoBean = SharedUtil.getUserInfo(ActiveManageActivity.this);
                    listView.setAdapter(signUpCommonAdapter);
                    loadDataSignUpManager();
                    num = 4;
                }
                break;
        }
    }

    /**
     * 报名管理加载网络
     */
    private void loadDataSignUpManager() {
        UserInfoServiceImpl.getInstance().getSignUp(userInfoBean.getUserid(),
                currpageSingeup, pagesize, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mProgressDialog.dismiss();
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                        if (currpageSingeup <= 0) {
                            listView.stopRefresh();
                        } else {
                            listView.stopLoadMore();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        Utiles.log("报名管理response：" + response);
                        if (currpageSingeup <= 0) {
                            listView.stopRefresh();
                        } else {
                            listView.stopLoadMore();
                        }
                        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);

                        if (statusBean.getCode() == 0) {
                            List<SignUpBean> signUpBeens = JSON.parseArray(statusBean.getData(), SignUpBean.class);
                            if (signUpBeens != null) {
                                if (currpageSingeup <= 0) {
                                    listSignUp.clear();
                                }
                                if (signUpBeens.size() > 0) {
                                    currpageSingeup++;
                                }

                                if (listSignUp.size() < pagesize) {
                                    listView.setPullLoadEnable(false);
                                } else {
                                    listView.setPullLoadEnable(true);
                                }
                                listSignUp.addAll(signUpBeens);
                                signUpCommonAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtil.showShort("数据获取失败，请刷新重试!");
                        }
                    }
                });
    }

    /**
     * 我的收藏加载网络
     */
    private void loadDataMyCollection() {
        UserInfoServiceImpl.getInstance().myCollect(userInfoBean.getUserid(), currpageMyCollect, pagesize, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("数据获取失败，请刷新重试!");
                if (currpageMyCollect <= 0) {
                    listView.stopRefresh();
                } else {
                    listView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (currpageMyCollect <= 0) {
                    listView.stopRefresh();
                } else {
                    listView.stopLoadMore();
                }
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    //json解析异常
//                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                    if (statusBean.getCode() == 0) {
                        List<CollectBean> collectBeens = JSON.parseArray(statusBean.getData(), CollectBean.class);
                        if (collectBeens != null) {
                            if (currpageMyCollect <= 0) {
                                listCollect.clear();
                            }
                            if (collectBeens.size() > 0) {
                                currpageMyCollect++;
                            }
                            if (listCollect.size() < pagesize) {
                                listView.setPullLoadEnable(false);
                            } else {
                                listView.setPullLoadEnable(true);
                            }
                            listCollect.addAll(collectBeens);
                            collectAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtil.showShort("数据获取失败，请刷新重试!");
                    }
                }
            }

        });
    }

    private void initView() {
        mProgressDialog = new CustomProgressDialog(ActiveManageActivity.this);
        findViewById(R.id.iv_backhome).setOnClickListener(this);//返回
        findViewById(R.id.iv_scanqrcode).setOnClickListener(this);//扫一扫
        sponsor = (TextView) findViewById(R.id.tv_sponsor);//参与者
        involved = (TextView) findViewById(R.id.tv_involved);//主办方
        sponsor.setOnClickListener(this);
        involved.setOnClickListener(this);
        View two = findViewById(R.id.id_LineActiveManager);
        findViewById(R.id.id_LineSignUp).setOnClickListener(this);
        two.setOnClickListener(this);
        textViewMessage = (TextView) findViewById(R.id.id_TextViewActiviteMessage);
        textViewSignUp = (TextView) findViewById(R.id.id_TextViewSignUp);
        imageMannager = findViewById(R.id.id_ImageActiviteMessage);
        imageSingnUp = findViewById(R.id.id_ImageSignUp);
        listView = (XListView) findViewById(R.id.id_ListViewActiveManage);
        listView.setEmptyView(findViewById(R.id.id_TextViewNoData));
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        //我的票圈适配器
        commonAdapter = new CommonAdapter<String>(this, listString, R.layout.view_textview_activiemanage) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.id_textViewListViewActiveManage, listString.get(position));
            }
        };
        //我的收藏适配器
        collectAdapter = new CollectAdapter(this, listCollect);
        //报名管理列表适配器
        signUpCommonAdapter = new CommonAdapter<SignUpBean>(ActiveManageActivity.this, listSignUp, R.layout.view_sigeup_listitem) {
            @Override
            public void convert(ViewHolder holder, SignUpBean signUpBean, int position) {

                Utiles.log(" signUpBean:" + signUpBean.toString());
                if (signUpBean != null) {
                    if (!TextUtils.isEmpty(signUpBean.getHeadurl())) {
                        Picasso.with(ActiveManageActivity.this).load(signUpBean.getHeadurl()).
                                placeholder(R.drawable.rc_default_portrait).error(R.drawable.rc_default_portrait).fit().
                                centerCrop().into(((ImageView) holder.getView(R.id.id_ImageHead)));
                    }
                    holder.setText(R.id.id_TextViewName, "姓名: " + signUpBean.getUsername());
                    holder.setText(R.id.id_TextViewTicketNum, "票号: " + signUpBean.getTicket());
                    if (signUpBean.isTicketcheck()) {
                        holder.setText(R.id.id_TextViewWhetherToCheck, getString(R.string.string_TicketCheack));
                    } else {
                        holder.setText(R.id.id_TextViewWhetherToCheck, getString(R.string.string_NoTicketCheack));
                    }
                }
            }
        };
        listView.setOnItemClickListener(this);
        onClick(involved);
        onClick(two);
    }

    /**
     * 改变第二层布局颜色
     *
     * @param tv
     * @param iv
     */

    private void changeLineColor(TextView tv, View iv) {
        if (tv != textView02 && iv != viewBlackGround) {
            if (textView02 != null && viewBlackGround != null) {
                textView02.setTextColor(getResources().getColor(R.color.textColor_Black));
                viewBlackGround.setBackgroundColor(getResources().getColor(R.color.white));
            }
            tv.setTextColor(getResources().getColor(R.color.header_blue));
            iv.setBackgroundColor(getResources().getColor(R.color.header_blue));
            textView02 = tv;
            viewBlackGround = iv;
        }
    }

    /**
     * 改变字体颜色
     *
     * @param tv
     */
    private void changeTextColor(TextView tv) {
        if (textView01 != tv) {
            tv.setSelected(true);
            tv.setTextColor(getResources().getColor(R.color.header_blue));
            if (textView01 != null) {
                textView01.setSelected(false);
                textView01.setTextColor(getResources().getColor(R.color.white));
            }
            textView01 = tv;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backhome:
                finish();
                break;
            case R.id.iv_scanqrcode:  //扫描
//                Intent intent = new Intent();
//                intent.setClass(ActiveManageActivity.this, ScanActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, 1);
                loadTicketOrActive();
                break;
            case R.id.tv_involved: //切换参与者一开始就设置被点击
                changeTextColor(involved);
                changeLineColor(textViewMessage, imageMannager);
                textViewMessage.setText(getResources().getString(R.string.string_piaoquan));
                textViewSignUp.setText(getResources().getString(R.string.string_shoucang));
                one = 0;
                two = 0;
                loadData();
                break;
            case R.id.tv_sponsor:  //切换主办方
                changeTextColor(sponsor);
                changeLineColor(textViewMessage, imageMannager);
                textViewMessage.setText(getResources().getString(R.string.string_activemanage));
                textViewSignUp.setText(getResources().getString(R.string.string_signupmanage));
                one = 1;
                two = 0;
                loadData();
                //onClick(textViewMessage);
                break;
            case R.id.id_LineActiveManager://活动管理一开始就设置被点击
                changeLineColor(textViewMessage, imageMannager);
                two = 0;
                loadData();
                break;
            case R.id.id_LineSignUp://管理报名
                changeLineColor(textViewSignUp, imageSingnUp);
                two = 1;
                loadData();
                break;
        }
    }


    /**
     * 加载选择验票活动的数据
     */
    private void loadTicketOrActive()
    {
        customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();

        if(SharedUtil.isLogin(this)){
            userId = SharedUtil.getUserInfo(this).getUserid();
        }
        ActiveServiceImpl.getInstance().selectTicketAction(userId, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("加载失败...");
            }
            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0)//成功返回
                {
                    List<ActiveinfosBean> list = JSON.parseArray(statusBean.getData(), ActiveinfosBean.class);
                    ArrayList<ActiveinfosBean> list1 = new ArrayList<ActiveinfosBean>();
                    list1.addAll(list);
                    if(list1.size()==0)
                    {
                        ToastUtil.showShort("您还没有发布活动!");
                        customProgressDialog.dismiss();
                        return;
                    }else {
                        Intent intent = new Intent(ActiveManageActivity.this, SelectTicketActivity.class);
                        intent.putExtra("data",list1);
                        startActivity(intent);
                        customProgressDialog.dismiss();
                    }

                }
            }
        });
    }
    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        listClear();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (num) {
            case 1://我的票务
                if (i == 1) {
                    Utiles.skipNoData(FinishedTicketActivity.class);
                }
                if (i == 2) {
                    Utiles.skipNoData(UnfinishedTicketActivity.class);
                }
                break;
            case 2://我的收藏适配器已经实现了
                break;
            case 3:
                listView.setPullRefreshEnable(false);
                listView.setPullLoadEnable(false);
                if (organizersBean == null) {
                    getHostId();
                }
                if (i == 1) {//已经发布活动
                    type = 1;
                    Utiles.skipData(MyActiveActivity.class, "AlreadyReleased");
                }
                if (i == 2) {//已经结束活动
                    type = 0;
                    Utiles.skipData(MyActiveActivity.class, "UnAlreadyReleased");
                }
                if (i == 3) {//设置主办方信息
                    Utiles.skipData(HostInfoActivity.class, "");
                }
                break;
            case 4://管理报名
                Intent intent = new Intent(ActiveManageActivity.this, SignUpDetaileActivity.class);
                SignUpBean signUpBean = listSignUp.get(i);
                intent.putExtra("data", signUpBean);
                startActivity(intent);
                break;
        }
    }

    /**
     * 根据主办方的Id去查找他发布的活动
     */
    private void getHostId() {
        if (SharedUtil.isLogin(ActiveManageActivity.this)) {
            userInfoBean = SharedUtil.getUserInfo(ActiveManageActivity.this);
        } else {
            Utiles.skipNoData(LoginActivity.class);
            return;
        }
        mProgressDialog.show();
        UserInfoServiceImpl.getInstance().getUserHostId(
                userInfoBean.getUserid(), new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                OrganizersJson json = new OrganizersJson();
                                organizersBean = json.parseJson(statusBean
                                        .getData());
                            } else {
                                ToastUtil.showShort("加载失败,请稍后重试!");
                            }
                        } else {
                            ToastUtil.showShort("加载失败,请稍后重试!");
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        mProgressDialog.dismiss();
                        ToastUtil.showShort("加载失败,请稍后重试!");
                    }
                });

    }

    @Override
    public void onRefresh() {
//        if (one == 0 && two == 1) {
            currpageMyCollect = 0;
//        }
//        if (one == 1 && two == 1) {
            currpageSingeup = 0;
//        }
        loadData();
    }

    @Override
    public void onLoadMore() {
//        if (one == 0 && two == 1) {
            loadDataMyCollection();
//        if (one == 1 && two == 1) {
            loadDataSignUpManager();
//        }
    }

}
