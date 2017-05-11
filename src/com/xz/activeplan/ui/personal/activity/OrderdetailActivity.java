package com.xz.activeplan.ui.personal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.activity.MapActivity;
import com.xz.activeplan.ui.news.time.RushBuyCountDownTimerView;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class OrderdetailActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener {
    private  ImageView image;
    private Dialog selectorDialog;
    private View view,  viewLineClause, viewNoTime,viewAgree;
    private ScrollView scrollView;
    private ImageView imageViewReadClause;
    private boolean bImageReadClause = false;
    private Intent intent;
    private OderDeatilBean oderBean;
    private ImageView imageViewHead;
    private TeacherBean teacherBean;
    private String strRefuseCause;
    private ImageView imageNoTime,imageOther,imageSchedulPull;
    private TextView textViewStataContent,textViewState,textViewEvaluate, textViewRefuse01, textViewConfirm01,textViewEvaluateAndRefund,textVieRrefundReason;
    private RushBuyCountDownTimerView timerView;
    private EditText editTextRefundRefuse;
    private TextView textViewDialogTitle;
    private Bundle bundle;

    /**
     * 改变状态
     */
    private void getStata() {
        //	订单状态	待确认=1 待付款=2 待见面=3 待评价=4 完成=5 取消=6
        scrollView.scrollTo(0, 0);
        Utiles.intRes(R.color.textColor_Black);
        Utiles.setTextColor(textViewState);
        Utiles.log("状态：" + oderBean.getStatus());
        switch (oderBean.getStatus()) {
            case 1:
                Utiles.setGone(textViewEvaluate, textViewEvaluateAndRefund);
                setStataValue(R.string.string_OrderNotProcessed, R.string.string_PleaseProcessed);
                break;
            case 2:
                Utiles.setGone(textViewRefuse01, viewLineClause, textViewConfirm01, textViewEvaluateAndRefund);
                setStataValue(R.string.string_WarmInvititionPayment, R.string.string_WarmPleaseWaitPatiently);
                break;
            case 3:
                setStataValue(R.string.string_PaymentSuccess, R.string.string_PaymentSuccess_Present);
                Utiles.setGone(textViewRefuse01, viewLineClause, textViewConfirm01, textViewEvaluate, textViewEvaluateAndRefund);
                break;
            case 4:
                setStataValue(R.string.string_WaitConfirm, R.string.string_WaitForTheInvitationToConfirm);
                Utiles.setGone(textViewRefuse01, viewLineClause, textViewConfirm01, textViewEvaluateAndRefund);
                break;
            case 5:
                if (oderBean.getComment() != null) {
                    if (!TextUtils.isEmpty(oderBean.getComment().getContent())) {
                        Utiles.intRes(R.string.string_InvationerEvaluate);
                        Utiles.setTextSrc(textViewEvaluateAndRefund);
                        textViewEvaluate.setText("" + oderBean.getComment().getContent());
                    }
                } else {
                    textViewEvaluate.setText(getResources().getString(R.string.string_InvationerNoEvaluate));
                }
                setStataValue(R.string.string_YiOver, R.string.string_OverWaitForTheInvitationToConfirm);
                Utiles.setGone(textViewRefuse01, viewLineClause, textViewConfirm01);
                Utiles.setVisibility(textViewEvaluate, textViewEvaluateAndRefund);
                break;
            case 6:
             if (!TextUtils.isEmpty(oderBean.getRefuseOrder().getReason())){
                    orderCancel(oderBean.getRefuseOrder().getStatus());
                 Utiles.log(" 用户退款单不为空  转态："+oderBean.getRefuseOrder().getStatus());
                    return;
                }else {
                 Utiles.log(" 用户退款单为空  转态：");
             }
                Utiles.intRes(R.string.string_HasBeenCanceled, R.string.string_HasBeenCanceledOver);
                Utiles.setTextSrc(textViewState,textViewStataContent);
                Utiles.intRes(R.color.color_OderStat);
                Utiles.setTextColor(textViewState);
                Utiles.setVisibility(textViewState,textViewStataContent);
                Utiles.setGone(textViewRefuse01,textViewConfirm01,viewLineClause);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_orderdetail);
        ClassConcrete.getInstance().addObserver(this);
        initView();//初始化
        getStata();//判断状态
        getTeacherData();

    }

    /**
     * 订单取消状态状态
     * @param status
     */
    private void orderCancel(int status) {
        Utiles.log("========>OrderdtailActivity:退款转态："+status);
        Utiles.intRes(R.string.string_RefundRefuse, R.string.string_AgreeRefundRefuse, R.string.string_RefundExplain);
        Utiles.setTextSrc(textViewRefuse01, textViewConfirm01,textViewEvaluateAndRefund);
        Utiles.setVisibility(textViewEvaluateAndRefund,textViewEvaluate,textVieRrefundReason);
        Utiles.setGone(viewLineClause);
        switch (status){//申请退款=1  完成退款=2  //拒绝退款=3  介入调查=4  已取消=5
            case 1://拒绝退款=3
                getDataFifference(7);//到几时
                Utiles.setGone(textViewState,textViewStataContent,textViewRefuse01);
                Utiles.setVisibility(textViewRefuse01,textViewConfirm01,timerView,textViewEvaluateAndRefund,textViewEvaluate,textVieRrefundReason);
                break;
            case 2://完成退款=2
                Utiles.setGone(timerView);
                Utiles.intRes(R.string.string_RefundSuccessOver, R.string.string_RefundSuccessContentOver);
                Utiles.setTextSrc(textViewState,textViewStataContent);
                Utiles.setVisibility(textViewState,textViewStataContent);
                Utiles.setGone(textViewRefuse01,textViewConfirm01);
                break;
            case 3://拒绝退款=3
                Utiles.intRes(R.string.string_WaitOfficial, R.string.string_WaitOfficialCenten);
                Utiles.setTextSrc(textViewState,textViewStataContent);
                Utiles.setVisibility(textViewState,textViewStataContent);
                Utiles.setGone(textViewRefuse01,textViewConfirm01);
                break;
            case 4://介入调查=4
                Utiles.intRes(R.string.string_RefundOfficialSuccess, R.string.string_RefundOfficialSuccessContent);
                Utiles.setTextSrc(textViewState,textViewStataContent);
                Utiles.setVisibility(textViewState,textViewStataContent);
                Utiles.setGone(textViewRefuse01,textViewConfirm01);
                break;
            case 5://  已取消=5
                Utiles.intRes(R.string.string_RefundOfficialFaile, R.string.string_RefundFaileContent);
                Utiles.setTextSrc(textViewState,textViewStataContent);
                Utiles.setVisibility(textViewState,textViewStataContent);
                Utiles.setGone(textViewRefuse01,textViewConfirm01);
                break;
        }

        if (oderBean.getRefuseOrder()!=null) {
            Utiles.setVisibility(textVieRrefundReason,textViewEvaluate);
            //退款原因
           textVieRrefundReason.setText(getResources().getString(R.string.string_RefundReason) + "：" + oderBean.getRefuseOrder ().getReason());
            textViewEvaluate.setTextColor(getResources().getColor(R.color.textColor));
            //退款说明
            textViewEvaluate.setText( "：" + oderBean.getRefuseOrder ().getRemark().toString());
        }
    }


    private void initView() {
         oderBean = (OderDeatilBean)getIntent().getSerializableExtra("data");
        //弹窗
        if (selectorDialog==null) {
            selectorDialog = new Dialog(OrderdetailActivity.this, R.style.selectorDialog);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setCanceledOnTouchOutside(true);
        }
        scrollView= (ScrollView) findViewById(R.id.id_SorollView);
        Utiles.headManager(this, R.string.string_Orderdetail, new Utiles.ActivityResult() {
            @Override
            public void resultBean() {
                intent = new Intent();
                bundle = new Bundle();
                bundle.putSerializable("data", oderBean);
                intent.putExtras(bundle);
                setResult(20,intent);
            }
        });
        //详细信息
        findViewById(R.id.id_RelativeInvitationDetailed).setOnClickListener(this);
        //同意
         textViewConfirm01 =(TextView) findViewById(R.id.id_TextViewConfirm01);
         textViewConfirm01.setBackgroundColor(getResources().getColor(R.color.gary));
         textViewConfirm01.setOnClickListener(this);
        //已读未读图片
         imageViewReadClause= (ImageView) findViewById(R.id.id_ImageReadClause);
         imageViewReadClause.setOnClickListener(this);
         onClick(imageViewReadClause);
        //底部的条款布局
        viewLineClause = findViewById(R.id.id_LineClause);
        //状态
        textViewState = (TextView)findViewById(R.id.id_TextViewState);
        //状态内容
        textViewStataContent=(TextView)findViewById(R.id.id_TextViewStataContent);
        //地址
        findViewById(R.id.id_LineAddress).setOnClickListener(this);
        //时间
        ((TextView)findViewById(R.id.id_TextViewTime)).setText(TimeUtils.formatTime(Long.valueOf(oderBean.getStarttime()).longValue()));
        //地点
        ((TextView)findViewById(R.id.id_TextViewAddress)).setText(oderBean.getInv_palce());
        //邀请标题
        ((TextView)findViewById(R.id.id_TextViewInvitationCelebrityContent)).setText("  "+ oderBean.getInv_title());
        //活动介绍
        ((TextView)findViewById(R.id.id_TextViewActivityIntroductionContent)).setText("  "+ oderBean.getInv_content());
        //订单编号
        ((TextView)findViewById(R.id.id_TextViewOrderNumber)).setText(getResources()
                .getString(R.string.string_Order_Number)+" "+ oderBean.getNumber());
        //邀请人
        ((TextView)findViewById(R.id.id_TextViewInvitationName)).setText(getResources()
                .getString(R.string.string_Name)+" "+ oderBean.getLinkman());
        //邀请人的电话
        ((TextView)findViewById(R.id.id_TextViewPhone)).setText(getResources()
                .getString(R.string.string_Phone)+" "+ oderBean.getPhone());
        //头像
        imageViewHead=(ImageView)findViewById(R.id.id_ImageHeadTitle);
        //拒绝
       textViewRefuse01 = (TextView)findViewById(R.id.id_TextViewRefuse01);
        Utiles.intRes(R.string.string_Refuse);
       textViewRefuse01.setOnClickListener(this);
        //金额
        ((TextView)findViewById(R.id.id_TextViewPriceDown)).setText(getResources().getString(R.string.string_AmountOfMoney)+": ￥"+ oderBean.getAmount()+"");

        //评价
        textViewEvaluateAndRefund = (TextView) findViewById(R.id.id_TextViewEvaluateAndRefund);
        textViewEvaluate = (TextView) findViewById(R.id.id_TextViewEvaluate0);
        //到几时
         timerView = (RushBuyCountDownTimerView) findViewById(R.id.id_TimerOrderDtaile);
        //退款原因
        textVieRrefundReason = (TextView) findViewById(R.id.id_TextVieRrefundReason);

        Utiles.setGone(timerView,textViewEvaluate,textVieRrefundReason);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_RelativeInvitationDetailed://详细信息
                break;
            /**************************************************************   拒绝   **************************************************************/
            case R.id.id_TextViewRefuse01://拒绝01
                getRefusedRefundPopup();//拒绝退款弹窗
                getRefusedInvitionPopup();//拒绝邀请弹窗
                break;
            case R.id.id_LineNoTime://拒绝理由----没时间
                changeImageState(imageNoTime);
                strRefuseCause = getResources().getString(R.string.string_NoTime);
                break;
            case R.id.id_LineSchedulePull://拒绝理由----档期排满
                changeImageState(imageSchedulPull);
                strRefuseCause = getResources().getString(R.string.string_SchedulePull);
                break;
            case R.id.id_LineOther://拒绝理由----其他
                changeImageState(imageOther);
                strRefuseCause = getResources().getString(R.string.string_Other);
                break;
            case R.id.id_TextViewInSee://弹窗在看看
                selectorDialog.dismiss();
                break;
            case R.id.id_TextViewDetermineCancel://确定取消订单
                selectorDialog.dismiss();
                CelebrityNewWorkImpl.getInstance().InviteOrderRefuse(oderBean.getInv_id(), oderBean.getUsr_id(), strRefuseCause, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Utiles.loadFailed();
                    }
                    @Override
                    public void onResponse(String response) {
                        JosonBean(response);
                    }
                });
                break;
            /************************************************************     同意     ****************************************************************/
            case R.id.id_TextViewAgree:
                getDetermine();//拒绝退款---确认键
                getRefuseIntivition();//收到邀请---确认出席
               break;
            case R.id.id_TextViewConfirm01://同意---邀请
                if (textViewConfirm01.getText().toString().equals(getResources().getString(R.string.string_AgreeRefundRefuse))) {
                    UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                    if (networkAndLogin_ok==null){
                        return;
                    }
                    CelebrityNewWorkImpl.getInstance().agreeRefund(oderBean.getInv_id(), new OkHttpClientManager.StringCallback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Utiles.loadFailed();
                        }

                        @Override
                        public void onResponse(String response) {
                            JosonBean(response);
                         /*  *//* StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                            if (statusBean.getCode()==0){
                                if (!TextUtils.isEmpty(statusBean.getData())){
                                    List<RefuseOrderBean> refuseOrderBeen = JSON.parseArray(statusBean.getData(), RefuseOrderBean.class);
                                    if (refuseOrderBeen!=null){
                                        if (refuseOrderBeen.size()!=0){
                                            oderBean.setRefuseOrder(refuseOrderBeen.get(0));
                                            getStata();
                                        }
                                    }

                                }
                            }
                            Utiles.log(" =======>同意退款："+response);
                            */

                        }
                    });
                }
                if (textViewConfirm01.getText().toString().equals(getResources().getString(R.string.string_Confirm))) {
                    if (viewAgree == null) {
                        viewAgree = LayoutInflater.from(this).inflate(R.layout.view_dialog_confirm_invitation, null);
                        viewAgree.findViewById(R.id.id_TextViewAgree).setOnClickListener(this);
                        textViewDialogTitle = (TextView) viewAgree.findViewById(R.id.id_TextViewTitleDialog);
                        editTextRefundRefuse = (EditText) viewAgree.findViewById(R.id.id_EditRefundRefuse);
                    }
                    editTextRefundRefuse.setFocusable(false);
                    editTextRefundRefuse.setEnabled(false);
                    textViewDialogTitle.setText(getResources().getString(R.string.string_WaitReason));
                    getDialogShow(viewAgree);
                }
                break;

            case R.id.id_ImageReadClause://条款
               if(bImageReadClause == false){
                   textViewConfirm01.setBackgroundColor(getResources().getColor(R.color.header_blue));
                   imageViewReadClause.setImageDrawable(getResources().getDrawable(R.drawable.read_caluse));
                   bImageReadClause =true;
               }else {
                   bImageReadClause=false;
                   textViewConfirm01.setBackgroundColor(getResources().getColor(R.color.gary));
                   imageViewReadClause.setImageDrawable(getResources().getDrawable(R.drawable.unrad_clause));
               }
                break;
            case R.id.id_LineAddress:
                intent = new Intent(this,MapActivity.class) ;
                intent.putExtra("address", "龙华新区") ;
                startActivity(intent);
                break;
        }
    }

    /**
     * 拒绝邀请弹窗
     */
    private void getRefusedInvitionPopup() {
        if (textViewRefuse01.getText().toString().equals(getResources().getString(R.string.string_Refuse))) {
            if (view == null) {
                view = LayoutInflater.from(this).inflate(R.layout.view_dialog, null);
                view.findViewById(R.id.id_TextViewInSee).setOnClickListener(this);
                view.findViewById(R.id.id_TextViewDetermineCancel).setOnClickListener(this);
                viewNoTime = view.findViewById(R.id.id_LineNoTime);
                viewNoTime.setOnClickListener(this);
                view.findViewById(R.id.id_LineSchedulePull).setOnClickListener(this);
                view.findViewById(R.id.id_LineOther).setOnClickListener(this);
                imageNoTime = (ImageView) view.findViewById(R.id.id_ImageNoTime);
                imageSchedulPull = (ImageView) view.findViewById(R.id.id_ImageSchedulePull);
                imageOther = (ImageView) view.findViewById(R.id.id_ImageOther);
            }
            getDialogShow(view);
            onClick(viewNoTime);
            return;
        }
    }

    /**
     *拒绝退款弹窗
     */
    private void getRefusedRefundPopup() {
        if (textViewRefuse01.getText().toString().equals(getResources().getString(R.string.string_RefundRefuse))) {
            if (viewAgree == null) {
                viewAgree = LayoutInflater.from(this).inflate(R.layout.view_dialog_confirm_invitation, null);
            }
            editTextRefundRefuse = (EditText) viewAgree.findViewById(R.id.id_EditRefundRefuse);
            editTextRefundRefuse.setText(null);
            editTextRefundRefuse.setHint(getResources().getString(R.string.string_PleaseEnterRefuseReason));
            textViewDialogTitle=(TextView) viewAgree.findViewById(R.id.id_TextViewTitleDialog);
            viewAgree.findViewById(R.id.id_TextViewAgree).setOnClickListener(this);
            viewAgree.findViewById(R.id.id_LinePopup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utiles.getKeyBoard(editTextRefundRefuse,"open");
                }
            });
            textViewDialogTitle.setText(getResources().getString(R.string.string_WaitReason));
            getDialogShow(viewAgree);
            return;
        }
    }

    /**
     * 拒绝邀请确定
     */
    private void getRefuseIntivition() {
        //接受邀请
        if (textViewRefuse01.getText().toString().equals(getResources().getString(R.string.string_Refuse))) {
            if (bImageReadClause != true) {
                return;
            }
            selectorDialog.dismiss();
            UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
            long timeMillis = System.currentTimeMillis();
            UserInfoBean networkAndLogin_ok1 = Utiles.getNetworkAndLogin_OK();
            if (networkAndLogin_ok1==null){
                return;
            }
            CelebrityNewWorkImpl.getInstance().InviteOrderAgree(oderBean.getInv_id(),
                    networkAndLogin_ok.getUserid(), timeMillis, new OkHttpClientManager.StringCallback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Utiles.loadFailed();
                        }

                        @Override
                        public void onResponse(String response) {
                            JosonBean(response);

                        }
                    });
            return;
        }
    }

    /**
     * 拒绝退款---确定键
     */
    private void getDetermine() {
        if (textViewRefuse01.getText().toString().equals(getResources().getString(R.string.string_RefundRefuse))) {
            selectorDialog.dismiss();
            String s = editTextRefundRefuse.getText().toString();
            if (TextUtils.isEmpty(s)){
                ToastUtil.showShort(getResources().getString(R.string.string_RefusedNotNull));
                return;
            }
            UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
            if (networkAndLogin_ok==null){
                return;
            }
            CelebrityNewWorkImpl.getInstance().refusedRefund(oderBean.getInv_id(),s, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Utiles.loadFailed();
                }
                @Override
                public void onResponse(String response) {
                    JosonBean(response);
                    Utiles.log("Response:"+this.getClass().getName()+"  aaa:"+response);

                }
            });

        }
    }

    /**
     * Daiglo显示布局
     * @param view
     */
    private void getDialogShow(View view) {
        selectorDialog.setContentView(view);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = selectorDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.7);
        selectorDialog.getWindow().setAttributes(lp);
        selectorDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * j解析为对象
     */
    private void JosonBean(String response){
        Utiles.log(response);
        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
        if (statusBean!=null){
            if (!TextUtils.isEmpty(statusBean.getData())){
                Utiles.log(":"
                +statusBean.getData());
                List<OderDeatilBean> oderDeatilBeen = JSON.parseArray(statusBean.getData(), OderDeatilBean.class);
                if (oderDeatilBeen.size()!=0) {
                    oderBean = oderDeatilBeen.get(0);
                            getStata();
                }
            }
        }else {
            Utiles.loadFailed();
        }
    }
    @Override
    protected void onDestroy() {

        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.QUITE_CHART_TYPE) {
            finish();
        }
        return false;
    }

    /**
     * 改变图片状态
     */
    public  void changeImageState(ImageView iv){
        if( image!=null){
            image.setImageDrawable(getResources().getDrawable(R.drawable.alipay_right_bg_normal));
        }
        iv.setImageDrawable(getResources().getDrawable(R.drawable.alipay_right_bg_selected));
        image=iv;
    }

    /**
     * 给状态提示栏赋值
     */
    public void setStataValue(int stringTitleId,int stringContentId){
        textViewState.setText(getResources().getString(stringTitleId));
        textViewStataContent.setText(getResources().getString(stringContentId));
    }


    /**
     * 获取名师详情
     */
    private void getTeacherData() {
        CelebrityNewWorkImpl.getInstance().getCelebrityDetails(oderBean.getTea_id(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.loadFailed();
            }
            @Override
            public void onResponse(String response) {
                if(TextUtils.isEmpty(response)){
                    Utiles.loadFailed();
                }
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if(TextUtils.isEmpty(statusBean.getData())){
                    Utiles.loadFailed();

                }
                teacherBean = JSON.parseObject(statusBean.getData(), TeacherBean.class);
                if (teacherBean==null){
                    Utiles.loadFailed();

                }
                //价格
                ((TextView)findViewById(R.id.id_TextViewPrice)).setText("￥ "+ teacherBean.getPrice()+"");
                //话题标题
                ((TextView)findViewById(R.id.id_TextViewTitle)).setText(teacherBean.getCaption()+"");
                //名字
                ((TextView)findViewById(R.id.id_TextViewName)).setText(teacherBean.getRealname());
                //职位
                ((TextView)findViewById(R.id.id_TextViewJob)).setText(teacherBean.getCompany());
                //头像
                Picasso.with(OrderdetailActivity.this).load(teacherBean.getCover()).placeholder(R.drawable.default_details_image)
                        .error(R.drawable.default_details_image).fit().centerCrop().into(imageViewHead);

            }
        });
    }
    /**
     * 得到时间差
     * @param
     */
    public void getDataFifference(int days){
            Date date = new Date(Long.valueOf(oderBean.getRefuseOrder().getApply_time()).longValue());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,days);
        Date time = cal.getTime();
        SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = formatTime.format(new Date(System.currentTimeMillis()));
        long between = 0;
        try {
            Date begin = formatTime.parse(formatTime.format(time));
            Date end = formatTime.parse(format);
            between = (begin.getTime() - end.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        // 设置时间(hour,min,sec)
          timerView.setTime(days,Long.valueOf(day).intValue(),Long.valueOf(hour).intValue(), Long.valueOf(min).intValue(), Long.valueOf(s).intValue());
        //timerView.setTime(7,1,0, 0, 10);

        // 开始倒计时
        timerView.start();
    }
}
