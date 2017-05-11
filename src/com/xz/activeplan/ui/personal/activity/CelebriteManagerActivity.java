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
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.news.activity.InvitationActivity;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.xlistview.XListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CelebriteManagerActivity extends BaseFragmentActivity implements View.OnClickListener, ClassObserver, AdapterView.OnItemClickListener {
    private TextView textViewRecivedInvitation,textViewSendInvitation,textView,textViewNoData;;
    private View viewRecivedInvitation, viewSendInvitation,viewBlackGround;
    private List<OderDeatilBean> listRecivedInvitation01 = new ArrayList<OderDeatilBean>();
    private List<OderDeatilBean> listSendInvitation02 = new ArrayList<OderDeatilBean>();
    private XListView listView;
    private int pagesize=10;
    private int currpage=0;
    private int selectList =-1;
    private CommonAdapter<OderDeatilBean> adapterRecivedInvitation01;//我收到邀请是适配器
    private CommonAdapter<OderDeatilBean> adapterSendInvitation02;//我发起的邀请的适配器
    private CustomProgressDialog mProgressDialog;
    private Intent intent;
    private Bundle bundle;
    private View viewReceivedInvitation;
    private View sendInvitation;
    private int index;

    private void listClean(){
        listRecivedInvitation01.clear();
        listSendInvitation02.clear();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utiles.log("  onCreate");
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_celebrite_manager);
        ClassConcrete.getInstance().addObserver(this);
        initView();
    }



    private void initView() {
        Utiles.headManager(this, R.string.string_MyCelebrite);
        mProgressDialog = new CustomProgressDialog(this) ;
        //收到的邀请
         viewReceivedInvitation = findViewById(R.id.id_LineReceivedInvitation);
        textViewRecivedInvitation= (TextView) findViewById(R.id.id_TextViewReceivedInvitation);
         viewRecivedInvitation =findViewById(R.id.id_ImageReceivedInvitation);
        viewReceivedInvitation.setOnClickListener(this);
        //发起的邀请
        sendInvitation = findViewById(R.id.id_LineSendInvitation);
        sendInvitation.setOnClickListener(this);
        textViewSendInvitation=(TextView)findViewById(R.id.id_TextViewSendInvitation);
        viewSendInvitation =findViewById(R.id.id_ImageSendInvitation);
        //没有数据显示
         textViewNoData= (TextView) findViewById(R.id.id_TextViewNoDate);
        //ListView
         listView = (XListView) findViewById(R.id.id_XListview);
        listView.setOnItemClickListener(this);
         listView.setEmptyView(textViewNoData);
        //收到邀请适配器
        adapterRecivedInvitation01 = new CommonAdapter<OderDeatilBean>
                (this, listRecivedInvitation01, R.layout.view_invitation_item) {
            @Override
            public void convert(ViewHolder holder, OderDeatilBean invitatonBean, int position) {
                ImageView view = (ImageView) holder.getView(R.id.id_ImageInvitationHead);
                if (invitatonBean.getHeadurl()!=null) {
                    if (!TextUtils.isEmpty(invitatonBean.getHeadurl()))
                        Picasso.with(CelebriteManagerActivity.this)
                                .load(invitatonBean.getHeadurl()).
                                placeholder(R.drawable.rc_default_portrait).error(R.drawable.rc_default_portrait).fit().centerCrop().into(view);
                }
                Utiles.log(" uSER:"+invitatonBean.getHeadurl());
                holder.setText(R.id.id_TextViewNumber,getResources().getString(R.string.string_Order_Number)+" "+invitatonBean.getNumber());
                holder.setText(R.id.id_TextViewTime,getResources().getString(R.string.string_StratTimeNotBeEmpty)+": "+
                TimeUtils.formatTime(Long.valueOf(invitatonBean.getStarttime()).longValue()));
                holder.setText(R.id.id_TextViewName,invitatonBean.getLinkman());
                holder.setText(R.id.id_TextViewTheme,invitatonBean.getInv_title());
                judgeStata(invitatonBean.getStatus(),holder,invitatonBean);
            }
        };
        adapterSendInvitation02 = new CommonAdapter<OderDeatilBean>
                (this, listSendInvitation02, R.layout.view_invitation_item) {
            @Override
            public void convert(ViewHolder holder, OderDeatilBean invitatonBean, int position) {
                ImageView view = (ImageView) holder.getView(R.id.id_ImageInvitationHead);
                if (invitatonBean.getHeadurl()!=null) {
                    if (!TextUtils.isEmpty(invitatonBean.getHeadurl()))
                    Picasso.with(CelebriteManagerActivity.this)
                            .load(invitatonBean.getHeadurl()).
                            placeholder(R.drawable.rc_default_portrait).error(R.drawable.rc_default_portrait).fit().centerCrop().into(view);
                }

                holder.setText(R.id.id_TextViewNumber,getResources().getString(R.string.string_Order_Number)+" "+invitatonBean.getNumber());
                holder.setText(R.id.id_TextViewTime,getResources().getString(R.string.string_StratTimeNotBeEmpty)+": "+
                        TimeUtils.formatTime(Long.valueOf(invitatonBean.getStarttime()).longValue()));
                holder.setText(R.id.id_TextViewName,invitatonBean.getLinkman());
                    Utiles.log("  2:"+invitatonBean.toString());
                holder.setText(R.id.id_TextViewTheme,invitatonBean.getInv_title());
                judgeStataMy(invitatonBean.getStatus(),holder,invitatonBean);
            }
        };
        //第一时间点击事件
        onClick(viewReceivedInvitation);
    }

    /**
     * 判断ListView状态
     * @param status
     * @param holder
     * @param
     */
    private void judgeStata(int status, ViewHolder holder, OderDeatilBean oderDeatilBean) {
        switch (status){
            case 1:
            holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitYouConfirm));
                break;
            case 2:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitPayment));
                break;
            case 3:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitMeet));
                break;
            case 4:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitEvaluate));
                break;
            case 5:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_Over));
                break;
            case 6:
                if (!TextUtils.isEmpty(oderDeatilBean.getRefuseOrder().getRemark())) {
                    switch (oderDeatilBean.getRefuseOrder().getStatus()) {
                        case 1:
                            holder.setText(R.id.id_TextViewState, getResources().getString(R.string.string_HandleRefund01));
                            break;
                        case 2:
                            holder.setText(R.id.id_TextViewState, getResources().getString(R.string.string_RefundSuccess02));
                            break;
                        case 3:
                            holder.setText(R.id.id_TextViewState, getResources().getString(R.string.string_RefundRefuse));
                            break;
                        case 4:
                            holder.setText(R.id.id_TextViewState, getResources().getString(R.string.string_RefundSuccess02));
                            break;
                        case 5:
                            holder.setText(R.id.id_TextViewState, getResources().getString(R.string.string_RefundFaile));
                            break;
                    }
                }else {
                    holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_AlreadyCancel));
                }
                break;
        }
    }

    /**
     * 判断ListView状态
     * @param status
     * @param holder
     */
    private void judgeStataMy(int status, ViewHolder holder, OderDeatilBean oderDeatilBean) {
        Utiles.log(" CelebriteManagerActivity: Stata:"+status);
        switch (status){
            case 1:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitYouConfirm02));
                break;
            case 2:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitPayment02));
                break;
            case 3:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitMeet02));
                break;
            case 4:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_WaitEvaluate02));
                break;
            case 5:
                holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_Over));
                break;
            case 6:
                if (!TextUtils.isEmpty(oderDeatilBean.getRefuseOrder().getRemark())){
                  switch (oderDeatilBean.getRefuseOrder().getStatus()){
                      case 1:
                          holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_HandleRefund));
                          break;
                      case 2:
                          holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_RefundSuccess02));
                          break;
                      case 3:
                          holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_RefundRefuse));
                          break;
                      case 4:
                          holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_RefundSuccess02));
                          break;
                      case 5:
                          holder.setText(R.id.id_TextViewState,getResources().getString(R.string.string_RefundFaile));
                          break;
                  }
                }else {
                    holder.setText(R.id.id_TextViewState, getResources().getString(R.string.string_AlreadyCancel));
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_LineReceivedInvitation://收到的邀请
                mProgressDialog.show();
                selectList=1;
                textViewNoData.setText(getResources().getString(R.string.string_NoReceivedInvitation));
                changeLineColor(textViewRecivedInvitation, viewRecivedInvitation);
                listView.setAdapter(adapterRecivedInvitation01);
                if (listRecivedInvitation01.size()==0){
                    getData();
                }else {
                    mProgressDialog.dismiss();
                }
                break;
            case R.id.id_LineSendInvitation:
                mProgressDialog.show();
                selectList=2;
                textViewNoData.setText(getResources().getString(R.string.string_NoSendInvitation));
                listView.setAdapter(adapterSendInvitation02);
                changeLineColor(textViewSendInvitation, viewSendInvitation);
                if (listSendInvitation02.size()==0){
                    getData();
                }else {
                    mProgressDialog.dismiss();
                }
                break;
        }
    }

    /**C
     * 加载数据
     * @param
     */
    private void getData() {
        switch (selectList){
            case 1:
                UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok==null){
                    return;
                }
                CelebrityNewWorkImpl.getInstance().InviteMyPeople(pagesize, currpage, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                     Utiles.loadFailed();
                        mProgressDialog.dismiss();
                    }
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        List<OderDeatilBean> json = getJson(response);
                        if (json!=null){
                        judgeRefresh(1,json);//判断是否刷新
                        if (json.size()!=0) {
                            listRecivedInvitation01.addAll(json);
                            Utiles.log("------->Url:"+listRecivedInvitation01.get(0).toString());
                         adapterRecivedInvitation01.notifyDataSetChanged();
                           }
                        }
                    }
                });
                break;
            case 2:
                UserInfoBean networkAndLogin_ok02 = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok02==null){
                    return;
                }
                CelebrityNewWorkImpl.getInstance().sendInvation(pagesize, currpage, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Utiles.loadFailed();
                        mProgressDialog.dismiss();
                    }
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        List<OderDeatilBean> json = getJson(response);
                        if (json!=null)
                                judgeRefresh(1,json);//判断是否刷新
                                if (json.size()!=0) {

                                    listSendInvitation02.addAll(json);
                                    adapterSendInvitation02.notifyDataSetChanged();

                        }else {
                            Utiles.loadFailed();
                        }
                    }
                });
                break;
        }


    }

    /**
     * 判断是否刷新
     */
    private void judgeRefresh(int i,List<OderDeatilBean> list) {
        switch (i){
            case 2:
                if (list.size()<pagesize){
                    listView.setPullRefreshEnable(true);
                    listView.setPullLoadEnable(true);
                }else {
                    listView.setPullRefreshEnable(false);
                    listView.setPullLoadEnable(false);
                }
                break;
            case 1:
                if (list.size()<pagesize){
                    listView.setPullRefreshEnable(false);
                    listView.setPullLoadEnable(false);
                }else {
                    listView.setPullRefreshEnable(true);
                    listView.setPullLoadEnable(true);
                }
                break;
        }



    }

    /**
     * 改变字体颜色
     * @param tv
     * @param iv
     */
    private void changeLineColor(TextView tv, View iv) {
        if (tv != textView && iv != viewBlackGround) {
            if (textView != null && viewBlackGround != null) {
                textView.setTextColor(getResources().getColor(R.color.textColor_Black));
                viewBlackGround.setBackgroundColor(getResources().getColor(R.color.white));
            }
            tv.setTextColor(getResources().getColor(R.color.header_blue));
            iv.setBackgroundColor(getResources().getColor(R.color.header_blue));
            textView = tv;
            viewBlackGround = iv;
        }
    }

    @Override
    protected void onDestroy() {
        Utiles.log("  onDestroy");
        listClean();
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

    /**
     * listVie 点击事件处理
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index=position-1;
        switch (selectList){
            case 1:
                if (listRecivedInvitation01.size()!=0){
                intent = new Intent(this, OrderdetailActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("data", listRecivedInvitation01.get(position-1));
                intent.putExtras(bundle);
                    startActivityForResult(intent,10);
            }
                break;
            case 2:
                if (listRecivedInvitation01.size()!=0) {
                    intent = new Intent(this, InvitationActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("dataIntivition", listRecivedInvitation01.get(position-1));
                    intent.putExtras(bundle);
                    startActivityForResult(intent,30);
                }
                break;
        }
    }

    /**
     * 解析
     * @param response
     * @return
     */
    public List<OderDeatilBean> getJson(String response){
        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
        if (statusBean!=null){
            if (!TextUtils.isEmpty(statusBean.getData())){
                Utiles.log("  JIXI:"+statusBean.getData());
                List<OderDeatilBean> oderDeatilBeen = JSON.parseArray(statusBean.getData(), OderDeatilBean.class);
                return oderDeatilBeen;
            }
        }else {
            Utiles.loadFailed();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==20&&requestCode==10){
            OderDeatilBean oderDeatilBean = (OderDeatilBean) data.getSerializableExtra("data");
            if (oderDeatilBean!=null) {
                if (selectList==1){
                   listRecivedInvitation01.set(index,oderDeatilBean);
                   adapterRecivedInvitation01.notifyDataSetChanged();
                }
            }
        }if (resultCode==40&&requestCode==30){
            OderDeatilBean oderDeatilBean02 = (OderDeatilBean) data.getSerializableExtra("data");
            if (oderDeatilBean02!=null) {
                listSendInvitation02.set(index,oderDeatilBean02);
                adapterSendInvitation02.notifyDataSetChanged();
            }
        }

    }

}
