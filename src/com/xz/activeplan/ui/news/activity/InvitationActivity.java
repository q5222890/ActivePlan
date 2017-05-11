package com.xz.activeplan.ui.news.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePopupWindow;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.InvitationCelebriteBean;
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.news.time.RushBuyCountDownTimerView;
import com.xz.activeplan.ui.recommend.activity.PoiSearchActivity;
import com.xz.activeplan.ui.recommend.activity.SureOrderActivity;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 名人堂-邀请
 */
public class InvitationActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener {
    private ImageView imageViewProgress02,imageViewProgress03,imageViewProgress04,imageViewProgress05,imageViewHeadPhoto,imageMessage02,imageCell02;
    private TextView textViewMoneyHours,textViewTitleHead,textViewLess20Words,textViewNoCantNull,mTvHeadTitle,textViewCelebrityTitle,
            textViewCelebrityContent,textViewOrderNumber, textViewStataTitle01,textViewPriceL,textViewJob,textViewConfirm,textViewRefundAmountAndTime,
            textViewActivityIntroduction,textViewPrice,textViewStartTime02,textViewAddress02, textViewStataCtent02, textViewRefund,textViewReasonsForrefusal,
            textViewTheme, textViewEstimateAmount,textViewHrous, textViewStataTitle02, textViewRemind,textViewCell02,textViewMessage02;
    private EditText editTextInvitationTheme, editTextYouPorblem, editTextTel,editTextName,editTextStartTime,editTextAddress;
    private TeacherBean teacherBean;
    private TimePopupWindow timePopupWindow;
    private String time;
    private int hours=1;
    private InvitationCelebriteBean invitiationBean;
    private View viewOnePage,viewTwoPage, viewRefuse,viewCellAndMessage,viewCell,viewMessage,viewReasonForFefusal;
    private Dialog selectorDialog;
    private RushBuyCountDownTimerView timerView;
    private OderDeatilBean oderBean;
    private CustomProgressDialog progressDialog;
    private Intent intent;
    private Bundle bundle;
    private String command;
    private Date date0;
    private int broadcastReceiver;
    /**
     * 定义一个BroadcastReceiver广播接收类：
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(UrlsManager.END_ONE_DAY)){
                oderBean.setStatus(6);
                oderBean.setRef_reason(getResources().getString(R.string.string_Obsolete));
                fillData();
            }
            if(action.equals(UrlsManager.END_SEVEN_DAY)){
                oderBean.setStatus(6);
                oderBean.setRef_reason(getResources().getString(R.string.string_Obsolete));
                orderCancel(2);
            }
        }

    };

    /**
     * 给布局加载数据
     * @param
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public  void fillData()  {
        command="data";
        Utiles.log("-----状态 InvitationActivity:"+oderBean.getStatus());
        Utiles.setVisibility(viewTwoPage);
        Utiles.setGone(textViewRefundAmountAndTime,viewReasonForFefusal,viewOnePage);
        switch (oderBean.getStatus()){//待确认=1，取消=6 待付款=2  见面=3 评价=4 完成=5
            case 1:
                changerProgressRed(imageViewProgress02);//进度条
                Utiles.intRes(R.string.string_WaitForCeletiryConfirm, R.string.PleaseWait);
                Utiles.setTextSrc(textViewStataTitle02, textViewStataCtent02);
                Utiles.setGone(textViewConfirm,viewRefuse);//确按键隐藏
                break;
            case 2://待付款=2
                command="celebrite";
                registerBoradcastReceiver();
                Utiles.sleep(200);
                getDataFifference(1);//到几时
                Utiles.intRes(R.string.string_ResidualTime, R.string.string_GoPayment);
                Utiles.setTextSrc(textViewStataTitle02, textViewConfirm);//改变状态内容
                textViewPriceL.setText("￥"+oderBean.getAmount());
                changerProgressRed(imageViewProgress03,imageViewProgress02);//进度条变色
                Utiles.setVisibility(textViewConfirm,viewRefuse,timerView);//前往付款显示,//总金额显示---底部按键
                Utiles.setGone(textViewStataTitle02,textViewStataCtent02,textViewRemind);//待付款是好下面要隐藏//状态内容
                break;
            case 3://见面=3
                SimpleDateFormat formatTime = new SimpleDateFormat("yyyyMMddHHmmss");
                String startTime = formatTime.format(new Date(Long.valueOf(oderBean.getStarttime()).longValue()));
                String nowTime = formatTime.format(new Date(System.currentTimeMillis()));
                long start = Long.valueOf(startTime).longValue();
                long now = Long.valueOf(nowTime).longValue();
                if (now>=start){
                    textViewConfirm.setBackgroundResource(R.color.header_blue);
                }else {
                    textViewConfirm.setBackgroundResource(R.color.gary);
                }
                Utiles.setGone(viewRefuse,textViewRemind,timerView);
                Utiles.setVisibility(viewCellAndMessage,textViewRefund,textViewStataTitle02,textViewStataCtent02);
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.intRes(R.string.string_WaitAttended, R.string.string_AlreadyPaidWaitAttended, R.string.string_Attended);
                Utiles.setTextSrc(textViewStataTitle02, textViewStataCtent02,textViewConfirm);
                break;
            case 4://评价
                Utiles.setGone(viewRefuse,textViewRemind,timerView,textViewConfirm);
                Utiles.setVisibility(viewCellAndMessage,textViewRefund,textViewStataTitle02,textViewStataCtent02);
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.intRes(R.string.string_NotEvaluatedTitle, R.string.string_NotEvaluatedContent);
                Utiles.setTextSrc(textViewStataTitle02, textViewStataCtent02);
                skipClass();
                viewCell.setEnabled(false);
                viewMessage.setEnabled(false);
                break;
            case 5://完成
                if (TextUtils.isEmpty(oderBean.getComment().getContent())){
                    Utiles.log("   null");
                    Utiles.setVisibility(textViewRefund);
                    changerProgressGary(imageViewProgress05);
                }else {
                    Utiles.log("   !=null");
                    changerProgressRed(imageViewProgress05);
                    Utiles.setGone(textViewRefund);
                }
                Utiles.log("评价InvitionActivity:"+oderBean.getComment());
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.setGone(viewRefuse,textViewConfirm,textViewRemind,textViewStataCtent02);
                Utiles.setVisibility(viewCellAndMessage);
                Utiles.intRes(R.color.gary, R.color.gary);
                Utiles.setTextColor(textViewCell02,textViewMessage02);
                Utiles.intRes(R.drawable.cell_gray2, R.drawable.message_gray2);
                Utiles.setImageSrc(imageCell02,imageMessage02);
                Utiles.intRes(R.string.string_ToEvaluate, R.string.string_OrderCompletion);
                Utiles.setTextSrc(textViewRefund,textViewStataTitle02);
                viewCell.setEnabled(false);
                viewMessage.setEnabled(false);
                break;
            case 6://取消
                Utiles.log("  oder:"+oderBean.toString());
                if (!TextUtils.isEmpty(oderBean.getRefuseOrder().getRemark())){
                    registerBoradcastReceiver();
                    orderCancel(oderBean.getRefuseOrder().getStatus());
                    Utiles.log("InvitionActivity："+"有退款"+oderBean.getRefuseOrder().toString());
                    add_Data();
                    return;
                }else {
                    Utiles.log("InvitionActivity："+"无退款"+oderBean.getRefuseOrder().toString());
                }
                changerProgressRed(imageViewProgress02);
                changerProgressGary(imageViewProgress04,imageViewProgress05);
                if (oderBean.getRef_reason()!=null){
                    textViewStataCtent02.setText(oderBean.getRef_reason());//拒绝原因
                }else {
                    Utiles.setGone(textViewStataTitle02);
                }

                Utiles.setVisibility(textViewRemind,textViewStataTitle02,textViewStataCtent02);
                textViewRemind.setTextColor(getResources().getColor(R.color.gary));
                textViewRemind.setBackgroundResource(R.drawable.shape_line_gray);
                Utiles.setGone(viewRefuse, textViewConfirm,timerView,viewCellAndMessage);
                Utiles.intRes(R.string.string_AlreadyCancel);
                Utiles.setTextSrc(textViewStataTitle02);
                break;
        }
        add_Data();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_invitation);
        ClassConcrete.getInstance().addObserver(this);
        initView();//初始化控件
    }

    /**
     * 订单的退款状态
     * @param oder
     */
    private void orderCancel(int oder){
        textViewRefund.setEnabled(false);
        switch (oder){
            case 1:
                   //剩余退款时间
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.setVisibility(viewCell,viewMessage,timerView,viewCellAndMessage,textViewRefund,textViewRefundAmountAndTime);
                getDataFifference(7);//到几时
                Utiles.setGone(textViewStataTitle02,viewRefuse,textViewConfirm,textViewRemind);
                Utiles.intRes(R.color.header_blue, R.color.header_blue);
                Utiles.setTextColor(textViewCell02,textViewMessage02);
                Utiles.intRes(R.drawable.cell_blue, R.drawable.message_blue);
                Utiles.setImageSrc(imageCell02,imageMessage02);
                Utiles.intRes(R.string.string_HandleRefund, R.string.string_RuningRefund);
                Utiles.setTextSrc(textViewStataCtent02,textViewRefund);
                viewCell.setEnabled(true);
                viewCell.setEnabled(true);
                textViewRefundAmountAndTime.setText(getResources().getString(R.string.string_Total)+oderBean.getAmount()+
                        "  "+getResources().getString(R.string.string_Common)+oderBean.getDuration()+getResources().getString(R.string.string_CommonHours));
                break;
            case 2:
                 //退款成功
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.setGone(timerView,viewRefuse,textViewConfirm,textViewRemind);
                Utiles.setVisibility(textViewStataCtent02,textViewStataTitle02,viewCellAndMessage,textViewRefund,textViewRefundAmountAndTime);
                Utiles.intRes(R.string.string_RefundSuccess, R.string.string_RefundSuccessContent, R.string.string_RefundSuccess02);
                Utiles.setTextSrc(textViewStataTitle02,textViewStataCtent02,textViewRefund);
                Utiles.intRes(R.color.header_blue, R.color.header_blue);
                Utiles.setTextColor(textViewCell02,textViewMessage02);
                Utiles.intRes(R.drawable.cell_blue, R.drawable.message_blue);
                Utiles.setImageSrc(imageCell02,imageMessage02);
                viewCell.setEnabled(true);
                viewMessage.setEnabled(true);
                 textViewRefundAmountAndTime.setText(getResources().getString(R.string.string_Total)+oderBean.getAmount()+
                        "  "+getResources().getString(R.string.string_Common)+"这里换成小时数"+getResources().getString(R.string.string_CommonHours));
                break;
            case 3:
                //拒绝退款
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.setGone(timerView,viewRefuse,textViewConfirm,textViewRemind);
                Utiles.setVisibility(textViewStataCtent02,textViewStataTitle02,
                        viewCellAndMessage,textViewRefund,textViewRefundAmountAndTime,
                        textViewReasonsForrefusal);
                Utiles.intRes(R.string.string_RefundRefuse, R.string.string_RefundRefuseContent, R.string.string_RefundRefuse);
                Utiles.setTextSrc(textViewStataTitle02,textViewStataCtent02,textViewRefund);
                Utiles.intRes(R.color.header_blue, R.color.header_blue);
                Utiles.setTextColor(textViewCell02,textViewMessage02);
                Utiles.intRes(R.drawable.cell_blue, R.drawable.message_blue);
                Utiles.setImageSrc(imageCell02,imageMessage02);
                viewCell.setEnabled(true);
                viewMessage.setEnabled(true);
                textViewRefundAmountAndTime.setText(getResources().getString(R.string.string_Total)+oderBean.getAmount()+
                        "  "+getResources().getString(R.string.string_Common)+oderBean.getDuration()+getResources().getString(R.string.string_CommonHours));
                textViewReasonsForrefusal.setText(oderBean.getRefuseOrder().getRemark());
                break;
            case 4:
                timerView.stop();
                //官方介入成功
                Utiles.intRes(R.color.gary, R.color.gary);
                Utiles.setTextColor(textViewCell02,textViewMessage02);
                Utiles.intRes(R.drawable.cell_gray2, R.drawable.message_gray2);
                Utiles.setImageSrc(imageCell02,imageMessage02);
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.setGone(timerView,viewRefuse,textViewConfirm,textViewRemind,timerView);
                Utiles.setVisibility(textViewStataCtent02,textViewStataTitle02,viewCellAndMessage,textViewRefund,textViewRefundAmountAndTime);
                Utiles.intRes(R.string.string_RefundOfficialSuccess, R.string.string_RefundOfficialSuccessContent, R.string.string_RefundSuccess02);
                Utiles.setTextSrc(textViewStataTitle02,textViewStataCtent02,textViewRefund);
                viewCell.setEnabled(false);
                viewMessage.setEnabled(false);
                 textViewRefundAmountAndTime.setText(getResources().getString(R.string.string_Total)+oderBean.getAmount()+
                        "  "+getResources().getString(R.string.string_Common)+oderBean.getDuration()+getResources().getString(R.string.string_CommonHours));
                break;
            case 5:
                timerView.stop();
                Utiles.intRes(R.color.gary, R.color.gary);
                Utiles.setTextColor(textViewCell02,textViewMessage02);
                Utiles.intRes(R.drawable.cell_gray2, R.drawable.message_gray2);
                Utiles.setImageSrc(imageCell02,imageMessage02);
                changerProgressRed(imageViewProgress02,imageViewProgress03,imageViewProgress04);
                Utiles.setGone(timerView,viewRefuse,textViewConfirm,textViewRemind);
                Utiles.setVisibility(textViewStataCtent02,textViewStataTitle02,viewCellAndMessage,textViewRefund,textViewRefundAmountAndTime);
                Utiles.intRes(R.string.string_RefundOfficialFaile, R.string.string_RefundFaileContent, R.string.string_RefundFaile);
                Utiles.setTextSrc(textViewStataTitle02,textViewStataCtent02,textViewRefund);
                textViewRefundAmountAndTime.setText(getResources().getString(R.string.string_Total)+oderBean.getAmount()+
                        "  "+getResources().getString(R.string.string_Common)+oderBean.getDuration()+getResources().
                        getString(R.string.string_CommonHours));
                viewCell.setEnabled(false);
                viewMessage.setEnabled(false);
                break;
        }

    }

    /**
     * 访问网络---获取判断
     */
    private void getdata() {
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok==null){
            return;
        }
        CelebrityNewWorkImpl.getInstance().InvitationStatus(teacherBean.getTea_id(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("获取数据失败！");
            }
            @Override
            public void onResponse(String response) {
                jsonData(response);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_ImgaeStratTime://开始时间
                timePopupWindow.showAtLocation(editTextStartTime, Gravity.BOTTOM, 0, 0, new Date());
                setListener(editTextStartTime);
                break;
            case R.id.id_ImageAddHours://加时间
                if (teacherBean!=null) {
                    hours++;
                    if (hours >= 24) {
                        hours = 24;
                    }
                    textViewHrous.setText(hours + " " + getResources().getString(R.string.string_Hours));
                    textViewEstimateAmount.setText(getResources().getString(R.string.string_EstimatedAmount) + " ￥" + getAmount(hours));
                }
                break;
            case R.id.id_ImageReduceHours://减时间
                if (teacherBean!=null) {
                    hours--;
                    if (hours <= 1) {
                        hours = 1;
                    }
                    textViewHrous.setText(hours + " " + getResources().getString(R.string.string_Hours));
                    textViewEstimateAmount.setText(getResources().getString(R.string.string_EstimatedAmount) + " ￥" + getAmount(hours));
                }
                break;
            case R.id.id_ImgaeAddress://地图
                 intent = new Intent(InvitationActivity.this,
                        PoiSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.id_TextViewConfirm://第一页的确认
                if (teacherBean!=null) {
                    boolean judgmentCondition = getJudgmentCondition();
                    if (judgmentCondition == true) {//如果条件成立
                        setDataToservice();
                    }
                }
                if (oderBean!=null){
                    if (command.equals("celebrite")){
                        skipClass();
                    }else {
                        progressDialog.show();
                        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                        if (networkAndLogin_ok==null){
                            return;
                        }
                        CelebrityNewWorkImpl.getInstance().aonfirmedAttendance(oderBean.getInv_id(), new OkHttpClientManager.StringCallback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                progressDialog.dismiss();
                                Utiles.loadFailed();
                            }
                            @Override
                            public void onResponse(String response) {
                                jsonData(response);
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
                break;
            case R.id.id_LineCuaranteeWhatDoing:
                editTextInvitationTheme.requestFocus();
                Utiles.getKeyBoard(editTextInvitationTheme);
                break;
            case R.id.id_LineMyProblemContent:
                editTextYouPorblem.requestFocus();
                Utiles.getKeyBoard(editTextYouPorblem);
                break;
            case R.id.id_TextRemind:
                CelebrityNewWorkImpl.getInstance().remindCelebrities(oderBean.getInv_id(),new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)){
                            StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                            if (statusBean.getCode()==0){
                                ToastUtil.showShort("提醒成功！");

                            }
                        }
                    }
                });
                break;
            case R.id.id_TextViewIKonw:
                selectorDialog.dismiss();
                break;
            case R.id.id_LineCell:
                Utiles.log("  电话");
                intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + oderBean.getPhone());
                intent.setData(data);
                this.startActivity(intent);
                break;
            case R.id.id_LineMessage:
                Utiles.log("  短信");
              //  系统发短信界面：
                 intent = new Intent(Intent.ACTION_SENDTO);
                //需要发短息的号码
                intent.setData(Uri.parse("smsto:"+oderBean.getPhone()));
                this.startActivity(intent);
                break;
            case R.id.id_TextViewRefund:
                switch (oderBean.getStatus()){
                    case 3:
                        if (command.equals("data")) {
                            intent = new Intent(this, RefundActivity.class);
                            bundle = new Bundle();
                            bundle.putSerializable(command, oderBean);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 2);
                        }
                        break;
                    case 5:
                        skipClass();
                        ToastUtil.showShort("去评价页面");
                        break;
                }

                break;
        }
    }

    private void initView() {
        progressDialog = new CustomProgressDialog(this) ;
        progressDialog.show();
        //从名人堂跳转到页面
        teacherBean = (TeacherBean)getIntent().getSerializableExtra("data");
        //从我的——名人堂进入
        oderBean = (OderDeatilBean)getIntent().getSerializableExtra("dataIntivition");
        //弹窗
        if (selectorDialog==null) {
            selectorDialog = new Dialog(this, R.style.selectorDialog);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setCanceledOnTouchOutside(true);
        }
         viewOnePage = findViewById(R.id.id_ViewOne);
         viewTwoPage = findViewById(R.id.id_ViewTwo);
         Utiles.setGone(viewOnePage,viewTwoPage);
        Utiles.headManager(this, R.string.string_Orderdetail, new Utiles.ActivityResult() {
            @Override
            public void resultBean() {
                intent = new Intent();
                bundle = new Bundle();
                bundle.putSerializable("data", oderBean);
                intent.putExtras(bundle);
                setResult(40,intent);
            }
        });//头部管理
        //头部进度
         imageViewProgress02 = (ImageView) findViewById(R.id.id_ImageCelebrityConfirmation);
         imageViewProgress03 = (ImageView) findViewById(R.id.id_ImagePayment);
         imageViewProgress04 = (ImageView) findViewById(R.id.id_ImageMeet);
         imageViewProgress05 = (ImageView) findViewById(R.id.id_ImageEvaluate);
        //话题Title
        textViewStataTitle01 = (TextView) findViewById(R.id.id_TextViewTechnologyShareTitle);
        //多少钱每小时
         textViewMoneyHours=(TextView) findViewById(R.id.id_TextViewMoneyHours01);
        //状态标题
        textViewStataTitle02 = (TextView) findViewById(R.id.id_TextViewStataTitle);
        //状态内容
        textViewStataCtent02 =(TextView) findViewById(R.id.id_TextViewStataContent);
        //邀请名人做什么
         findViewById(R.id.id_LineCuaranteeWhatDoing).setOnClickListener(this);
         editTextInvitationTheme = (EditText) findViewById(R.id.id_TextViewInvitationCelebriteTheme);
         editTextInvitationTheme.setOnClickListener(this);
        Utiles.getKeyBoard(editTextInvitationTheme,"close");
        //说一下你遇到的问题
        findViewById(R.id.id_LineMyProblemContent).setOnClickListener(this);
         editTextYouPorblem = (EditText) findViewById(R.id.id_TextViewMyProblemContent);
         editTextYouPorblem.setOnClickListener(this);
        Utiles.getKeyBoard(editTextYouPorblem,"close");
        //头部邀请
         textViewTitleHead = (TextView) findViewById(R.id.id_TextViewTitleHead);
        //少于20字
         textViewLess20Words = (TextView) findViewById(R.id.id_TextViewLess20);
         textViewLess20Words.setVisibility(View.GONE);
        //不能为空
         textViewNoCantNull = (TextView) findViewById(R.id.id_TextViewNoCantNull);
         textViewNoCantNull.setVisibility(View.GONE);
        //开始时间
        findViewById(R.id.id_ImgaeStratTime).setOnClickListener(this);
        editTextStartTime = (EditText) findViewById(R.id.id_TextViewStartTime);
        Utiles.getKeyBoard(editTextStartTime,"close");
        //预计金额
        textViewEstimateAmount = (TextView) findViewById(R.id.id_TextViewEstimatedAmount);
        //名人出席小时数据
        textViewHrous = (TextView) findViewById(R.id.id_TextViewHrous);
        textViewHrous.setText(hours+" "+getResources().getString(R.string.string_Hours));
        if (teacherBean!=null) {
            textViewEstimateAmount.setText(getResources().getString(R.string.string_EstimatedAmount) + " ￥" + getAmount(hours));
            textViewStataTitle01.setText(teacherBean.getCaption());
            textViewMoneyHours.setText("￥"+ teacherBean.getPrice()+"/小时");
        }
        //加时间
        findViewById(R.id.id_ImageAddHours).setOnClickListener(this);
        //减时间
        findViewById(R.id.id_ImageReduceHours).setOnClickListener(this);
        //姓名
        editTextName= (EditText) findViewById(R.id.id_EditName);
        Utiles.getKeyBoard(editTextName,"close");
        //电话
        editTextTel= (EditText) findViewById(R.id.id_EditTel);
        Utiles.getKeyBoard(editTextTel,"close");
        //地图按键
         findViewById(R.id.id_ImgaeAddress).setOnClickListener(this);
         editTextAddress= (EditText) findViewById(R.id.id_TexViewtAddress);
        Utiles.getKeyBoard(editTextAddress,"close");
        //时间选择器
        timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.ALL);
        //总金额
         textViewPrice=(TextView)findViewById(R.id.id_TextViewPrice);
         textViewPriceL= (TextView) findViewById(R.id.id_TextViewPriceL);//价格
        //职位
        textViewJob= (TextView) findViewById(R.id.id_TextViewJob);
        //邀请主题
         textViewTheme = (TextView) findViewById(R.id.id_TextViewInvitationCelebriteTheme02);
        //邀约活动详情
         textViewActivityIntroduction = (TextView) findViewById(R.id.id_TextViewActivityIntroduction);
        //拒绝
        viewRefuse = findViewById(R.id.id_LineRefuse);
        //确认
        textViewConfirm = (TextView)findViewById(R.id.id_TextViewConfirm);
        textViewConfirm.setOnClickListener(this);
        /**
         * 以下是第二也
         */
        //第二页头像
         imageViewHeadPhoto= (ImageView) findViewById(R.id.img_ImageHeadPhoto);
        //标题
         textViewCelebrityTitle= (TextView) findViewById(R.id.id_TextViewCelebrityTitle);
        //内容
         textViewCelebrityContent= (TextView) findViewById(R.id.id_TextViewName);
        //订单号
         textViewOrderNumber= (TextView) findViewById(R.id.id_TextViewOrderNumber);
        //提醒
         textViewRemind = (TextView)findViewById(R.id.id_TextRemind);
        textViewRemind.setOnClickListener(this);
        //开始时间
        textViewStartTime02= (TextView) findViewById(R.id.id_TextViewStartTime02);
        //活动地点
        textViewAddress02= (TextView) findViewById(R.id.id_TexViewtAddress02);
        //订单相对布局
        timerView = (RushBuyCountDownTimerView) findViewById(R.id.id_Timer);
        //打电话发短信
         viewCellAndMessage = findViewById(R.id.id_LineCellAndMessage);
         viewCellAndMessage.setVisibility(View.GONE);
        imageCell02 = (ImageView) findViewById(R.id.id_ImageCell02);
        imageMessage02 =(ImageView) findViewById(R.id.id_ImageMessage02);
        textViewCell02=(TextView)findViewById(R.id.id_TextViewCell02);
        textViewMessage02=(TextView)findViewById(R.id.id_TextViewMessage02);
        viewCell=findViewById(R.id.id_LineCell);
        viewMessage=findViewById(R.id.id_LineMessage);
        viewCell.setOnClickListener(this);
        viewMessage.setOnClickListener(this);
        //退款
         textViewRefund = (TextView)findViewById(R.id.id_TextViewRefund);
         textViewRefund.setOnClickListener(this);
        //退款金额
        viewReasonForFefusal = findViewById(R.id.id_LineReasonsForFefusal);
        textViewRefundAmountAndTime = (TextView)findViewById(R.id.id_TextRefundAmountAndTime);
        textViewReasonsForrefusal = (TextView)findViewById(R.id.id_TextReasonsForrefusal);
        Utiles.setGone(textViewRefundAmountAndTime,textViewRefund,viewReasonForFefusal);
        if (teacherBean==null){
            if (oderBean!=null) {
                Utiles.log("  oderBean;"+oderBean.getTea_id()+"  STATA:"+oderBean.getStatus());
                getTeacherBean(oderBean.getTea_id());
                Utiles.setGone(viewOnePage);
                Utiles.setVisibility(viewTwoPage);
            }
        }else {
            getdata();
        }
    }

    /**
 * 改变进度条颜色红色
 */
    private void changerProgressRed(ImageView... iv){
        for (ImageView image : iv) {
            image.setImageResource(R.drawable.invite_progress_red);//进度条
        }
    }

    /**
     * 改变进度条颜色红色
     */
    private void changerProgressGary(ImageView... iv){
        for (ImageView image : iv) {
            image.setImageResource(R.drawable.invite_progress_gray);//进度条
        }
    }

    /**
     * 计算金额
     * @param hours
     */
    private double getAmount(int hours) {
        double v = hours * teacherBean.getPrice();
        BigDecimal bg = new BigDecimal(v);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

/**
 * 跳转---评价---支付
 */
    private void skipClass(){

        if (command.equals("celebrite")){
            intent = new Intent(this,SureOrderActivity.class);
        }
        if (command.equals("data")) {
            intent = new Intent(this, PublishedEvaluationActivity.class);

        }
        if (command!=null) {
            bundle = new Bundle();
            bundle.putSerializable(command, oderBean);
            intent.putExtras(bundle);
        }
        if (command.equals("data")){
            startActivityForResult(intent, 0);
        }
        if (command.equals("celebrite")){
            startActivity(intent);
        }
    }

/**
 * DialogShow
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

/**
 * 发起邀请---创建
 */
    private void setDataToservice(){
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok==null){
            return;
        }
        invitiationBean = new InvitationCelebriteBean();
        invitiationBean.setAmount(getAmount(hours)+"");//订单总金额
        invitiationBean.setInv_palce(editTextAddress.getText().toString());//地点
        invitiationBean.setDuration(hours+"");//时长
        invitiationBean.setStarttime(date0.getTime()+"");//开始时间
        invitiationBean.setLinkman(editTextName.getText().toString());//联系人
        invitiationBean.setPhone(editTextTel.getText().toString());//电话
         invitiationBean.setInv_title(editTextInvitationTheme.getText().toString());//邀约主题
        invitiationBean.setInv_content(editTextYouPorblem.getText().toString());//邀约内容
        invitiationBean.setGam_content(teacherBean.getCaption());//话题内容
        invitiationBean.setGam_title(teacherBean.getGambit());//话题标题
        invitiationBean.setTea_id(teacherBean.getTea_id()+"");//名人Id
        invitiationBean.setUsr_id(Utiles.getNetworkAndLogin_OK().getUserid()+"");//当前登录用户的Id
        final View view = LayoutInflater.from(this).inflate(R.layout.view_cemit_successfully, null);
        view.findViewById(R.id.id_TextViewIKonw).setOnClickListener(this);
        progressDialog.show();
        CelebrityNewWorkImpl.getInstance().InviteFamousPeople(invitiationBean, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                progressDialog.dismiss();
                ToastUtil.showLong("邀请失败");
            }
            @Override
            public void onResponse(String response) {
                Utiles.log("------->"+response);
                progressDialog.dismiss();
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode()==0){
                    if (!TextUtils.isEmpty(statusBean.getData())) {
                        List<OderDeatilBean> oderDeatilBean = JSON.parseArray(statusBean.getData(), OderDeatilBean.class);
                        if (oderDeatilBean != null) {
                            if (oderDeatilBean.size()!=0) {
                                oderBean = oderDeatilBean.get(0);
                                fillData();
                                getDialogShow(view);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     *给第二页加载数据
     */
   private void add_Data(){
       //头像
       Picasso.with(this).load(teacherBean.getCover()).placeholder(R.drawable.default_details_image)
               .error(R.drawable.default_details_image).fit().centerCrop().into(imageViewHeadPhoto);
       //标题
       textViewCelebrityTitle.setText(teacherBean.getCaption());
       //名字
       textViewCelebrityContent.setText(teacherBean.getRealname());
       //职位
       textViewJob.setText(teacherBean.getPosition());
       //单价
       textViewPrice.setText(" ￥"+teacherBean.getPrice());
       //邀约主题
       textViewStataTitle01.setText(getResources().getString(R.string.string_ResidualTime)+oderBean.getInv_title());
      Utiles.intRes(R.color.color_OderStat);
      Utiles.setTextColor(textViewStataTitle01);
       //时间
       textViewStartTime02.setText(TimeUtils.formatTime(Long.valueOf(oderBean.getStarttime()).longValue()));
       //地址
       textViewAddress02.setText(oderBean.getInv_palce());
       //邀约主题
       textViewTheme.setText(oderBean.getGam_title());
       //邀约活动详情
       textViewActivityIntroduction.setText(oderBean.getGam_content());
       //订单号
       textViewOrderNumber.setText(getResources().getString(R.string.string_Order_Number)+oderBean.getNumber());
   }

    /**
     * 判断条件
     * @return
     */
    private boolean getJudgmentCondition(){

    boolean bInvitation=false;
    boolean bYouPorblem=false;
    //地址不能为空
    boolean bAddress = isEmpty(editTextAddress, getResources().getString(R.string.string_AddressCanNotBeEmpty),
            getResources().getString(R.string.Please_Addrss));
    //开始时间不能为空
    boolean bStartTime = isEmpty(editTextStartTime, getResources().getString(R.string.string_StratTimeNotBeEmpty),
            getResources().getString(R.string.Start_Time));
    //姓名
    boolean bName = isEmpty(editTextName, getResources().getString(R.string.string_NameNotBeEmpty),
            "");
    //电话
    boolean bTel = isEmpty(editTextTel, getResources().getString(R.string.string_TelNotBeEmpty),
            "");
    int intInvitationCelebrity = editTextInvitationTheme.getText().length();
    int intYouPorblem = editTextYouPorblem.getText().length();
    if (TextUtils.isEmpty(editTextInvitationTheme.getText().toString())) {
        bInvitation=false;
        textViewLess20Words.setVisibility(View.VISIBLE);
    } else {
        bInvitation =true;
        textViewLess20Words.setVisibility(View.GONE);
    }
    if (intYouPorblem<100) {
        bYouPorblem =false;
        textViewNoCantNull.setVisibility(View.VISIBLE);
    } else {
        bYouPorblem =true;
        textViewNoCantNull.setVisibility(View.GONE);
    }
    if (bAddress==true&&bStartTime==true&&bName==true&&bTel==true&&bInvitation==true&&bYouPorblem==true){
               //判断电话号码
        if (Utils.checkMobileNumber(editTextTel.getText().toString())==false){
            ToastUtil.showLong(getResources().getString(R.string.string_PleaseEnterTheCorrectNumber));
            return false;
        }
        return true;
    }
    return false;
}

    /**
     * 非空判断
     * @param editText
     * @param strUnll
     */
    private boolean isEmpty(TextView editText, String strUnll,String strHint) {
        if (TextUtils.isEmpty(editText.getText().toString())){
            editText.setHint(strUnll);
            editText.setHintTextColor(getResources().getColor(R.color.red2));
        }else {
            editText.setHint(strHint);
            editText.setHintTextColor(getResources().getColor(R.color.gary5));
            return true;
        }
        return false;
    }

    /**
     * 时间选择
     * @param view
     */
    private void setListener(final EditText view) {
        //时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {


            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onTimeSelect(Date date) throws ParseException {
                date0=date;
                //格式化时间
                time = TimeUtils.formatTime(date);
                //得到当前系统时间
                String currentDateString = TimeUtils.getCurrentDateString();
                //得到时间差
                boolean before = TimeUtils.minutesBefore(time, currentDateString);
                if (before == false) {
                    ToastUtil.showShort("选择时间不能小于当前时间");
                } else {
                    view.setText(InvitationActivity.this.time);
                }
            }
        });

        timePopupWindow.setFocusable(true);
        backgroundAlpha(0.3f);
        timePopupWindow.setOnDismissListener(new PoponDismissListener());
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 解析Json
     * @param response
     */
    private void jsonData(String response){
        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
        if (statusBean.getCode()==0){
            if (statusBean.getData()!=null){
                List<OderDeatilBean> oderDeatilBeen = JSON.parseArray(statusBean.getData(), OderDeatilBean.class);
                if (oderDeatilBeen.size()!=0){
                    oderBean=oderDeatilBeen.get(0);
                    fillData();
                    Utiles.setVisibility(viewTwoPage);
                    Utiles.setGone(viewOnePage);
                }else {
                    Utiles.setVisibility(viewOnePage);
                    Utiles.setGone(viewTwoPage,viewRefuse);
                }
                progressDialog.dismiss();
            }
        }else {
            ToastUtil.showShort("加载数据失败！");
        }
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.MAP_ADDRESS_TYPE) {//百度地图类型
            editTextAddress.setText((String) event.getObj());
            Utiles.log(" 百度地图 ");
        }else
        if (event.getType()==EventType.ORDER_PAYMENT){
            oderBean.setStatus(3);
            fillData();
            Utiles.log(" 成功支付 ");
        }
        return false;
    }

    /**
     * 得到时间差
     * @param
     */
    public void getDataFifference(int days){
        long times;
        if (days==7){
             times = oderBean.getRefuseOrder().getApply_time();
        }else {
             times = oderBean.getSure_time();
        }
        Date date = new Date(Long.valueOf(times).longValue());
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
        Utiles.log(" 天："+day+" 小时："+hour+" "+min+" "+s);
        // 设置时间(hour,min,sec)
            timerView.setTime(days,Long.valueOf(day).intValue(),Long.valueOf(hour).intValue(), Long.valueOf(min).intValue(), Long.valueOf(s).intValue());
        //timerView.setTime(days,1,0, 0, 10);

        // 开始倒计时
        timerView.start();
    }
    /**
     * 注册一个广播
     */
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(UrlsManager.CELEBRITE_END);
        broadcastReceiver = 1;
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1://评价返回
                OderDeatilBean oderDeatilBean = (OderDeatilBean) data.getSerializableExtra("data");
                if (oderDeatilBean!=null) {
                    oderBean = oderDeatilBean;
                    fillData();
                }
                break;
            case 2://退款返回
                OderDeatilBean oderDeatilBean02 = (OderDeatilBean) data.getSerializableExtra("data");
                if (oderDeatilBean02!=null) {
                    oderBean = oderDeatilBean02;
                    fillData();
                }
                break;
        }


    }

    private void getTeacherBean(int tea_id){
        CelebrityNewWorkImpl.getInstance().getCelebrityDetails(tea_id, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss() ;
                if (TextUtils.isEmpty(response)){
                    ToastUtil.showShort("后台数据为空") ;
                }
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    TeacherBean teacherBeen = JSON.parseObject(statusBean.getData(), TeacherBean.class);
                    if (teacherBeen!=null) {
                        teacherBean=teacherBeen;
                        textViewEstimateAmount.setText(getResources().getString(R.string.string_EstimatedAmount) + " ￥" + getAmount(hours));
                        textViewStataTitle01.setText(teacherBean.getCaption());
                        textViewMoneyHours.setText("￥"+ teacherBean.getPrice()+"/小时");
                       fillData();
                    }else {
                        ToastUtil.showShort("名人信息获取失败!") ;
                    }
                } else {

                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                progressDialog.dismiss() ;
                ToastUtil.showShort("教师信息获取失败!") ;
            }
        }) ;
    }

    //退出当前Activity时被调用,调用之后Activity就结束了
    @Override
    protected void onDestroy() {
        if (teacherBean!=null){
            teacherBean=null;
        }
        if (oderBean!=null){
            oderBean=null;
        }
        if (broadcastReceiver==1){
            unregisterReceiver(mBroadcastReceiver);
            broadcastReceiver=0;
        }

        super.onDestroy();

    }

    /**
     *
     */
    private class PoponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

}
