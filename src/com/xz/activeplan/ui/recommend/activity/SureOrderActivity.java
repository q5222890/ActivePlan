package com.xz.activeplan.ui.recommend.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.TicketInfoBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.MD5;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Util;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.alipay.PayResult;
import com.xz.activeplan.utils.alipay.SignUtils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.AddAndSubView;
import com.xz.activeplan.views.CircleImageView;
import com.xz.activeplan.views.ExplainDialog;
import com.xz.activeplan.views.CustomProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 订单付款界面activity
 *
 * @author johnny
 */
public class SureOrderActivity extends BaseFragmentActivity implements OnClickListener, ClassObserver {

    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private ImageView ivOrderTicketImg, iamgeView;
    private TextView tvOrderTicketTitle, orderTicketName, orderTicketPrice, textViewCelebeiteJob, textViewCelebeitePrice,
            tvPayUserName, tvPayUserPhone, tvTotalPrice, textViewCelebeiteTitle, textViewCelebeiteName;
    private LinearLayout llRefundContent;
    private ImageView ivCheckAli, ivCheckWx;
    private boolean isAli = true;
    private ActiveinfosBean mActiveinfosBean;
    private UserInfoBean mUserInfoBean;
    private boolean flag = false;
    private ExplainDialog mExplainDialog;
    private CustomProgressDialog dialog = null;
    private TicketAddBean bean;
    private String telphone;
    private String weixin;
    private String company;
    private String position;
    private String realname;
    private OderDeatilBean oderBean;
    private boolean isFlag = false;
    private LinearLayout line_addandsubview;
    private AddAndSubView addAndSubView;
    private String price;
    private String iamgeUrl;
    private View viewRelativeCelebriteOrder, viewLineOther, viewAddOtherNumber;
    private CircleImageView imageCelebeitePhoto;
    private int ticketnum = 1;
    private TicketInfoBean ticketinfobean;
    private String ticket ;
    private int surplusTickets;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UrlsManager.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();

                    Utiles.log("支付结果信息：" + resultInfo + "===resultStatus===" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (oderBean != null) {
                            ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.ORDER_PAYMENT));
                            finish();
                        } else {
                        }
                        isFlag = false;
                        if (mActiveinfosBean != null) {
                            //支付成功
                            paySuccess();
                        }

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                            ToastUtil.showShort("支付结果确认中!");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtil.showShort("支付失败!");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }


    };
    /******************************************* 微   信   支  付 *********************************************************/
    private PayReq req;
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;

    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "127.0.0.1";
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        ClassConcrete.getInstance().addObserver(this);
        setContentView(R.layout.activity_sure_order);
        initViews();
    }

    private void initViews() {
        dialog = new CustomProgressDialog(this);
        //名人订单
        viewRelativeCelebriteOrder = findViewById(R.id.id_RelativeCelebriteOrder);
        textViewCelebeiteTitle = (TextView) findViewById(R.id.id_TextViewCelebrityTitle);
        textViewCelebeiteName = (TextView) findViewById(R.id.id_TextViewCelebriteName);
        textViewCelebeiteJob = (TextView) findViewById(R.id.id_TextViewJob);
        textViewCelebeitePrice = (TextView) findViewById(R.id.id_TextViewPrice);
        imageCelebeitePhoto = (CircleImageView) findViewById(R.id.img_ImageHeadPhoto);
        //其他订单
        viewLineOther = findViewById(R.id.id_LineOther);
        viewAddOtherNumber = findViewById(R.id.id_LineAddOtherNumber);
        Utiles.headManager(this, R.string.string_OrderPayment);
        ivOrderTicketImg = (ImageView) findViewById(R.id.ivOrderTicketImg);
        tvOrderTicketTitle = (TextView) findViewById(R.id.tvOrderTicketTitle);
        orderTicketName = (TextView) findViewById(R.id.orderTicketName);
//		orderTicketNum = (TextView) findViewById(R.id.orderTicketNum);
        orderTicketPrice = (TextView) findViewById(R.id.orderTicketPrice);
        tvPayUserName = (TextView) findViewById(R.id.tvPayUserName);
        tvPayUserPhone = (TextView) findViewById(R.id.tvPayUserPhone);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        findViewById(R.id.alipayBtn).setOnClickListener(this);//前往付款
        findViewById(R.id.explainBtn);
        llRefundContent = (LinearLayout) findViewById(R.id.llRefundContent);
        findViewById(R.id.rlRefund).setOnClickListener(this);
        ivCheckAli = (ImageView) findViewById(R.id.ivCheckAli);
        ivCheckWx = (ImageView) findViewById(R.id.ivCheckWx);
        line_addandsubview = (LinearLayout) findViewById(R.id.line_addandsubview);
        addAndSubView = new AddAndSubView(this);
        addAndSubView.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int num) {
                if(num >surplusTickets){
                    ToastUtil.showShort("购票数量超出范围");
                    return;
                }
                SureOrderActivity.this.ticketnum = num;
                    DecimalFormat df = new DecimalFormat("#0.00");
                    price = df.format(bean.getMoney() * num);
                    Utiles.log("票价：" + price);
                    tvTotalPrice.setText("￥" + price);
                    updateticketInfo();
            }
        });

        line_addandsubview.addView(addAndSubView);
        ivCheckAli.setOnClickListener(this);
        ivCheckWx.setOnClickListener(this);
        Utiles.headManager(this, R.string.string_OrderPayment);
        mExplainDialog = new ExplainDialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            mActiveinfosBean = (ActiveinfosBean) intent.getSerializableExtra("data");
            bean = (TicketAddBean) intent.getSerializableExtra("TicketAddBean");
            telphone = intent.getStringExtra("telphone");
            weixin = intent.getStringExtra("weixin");
            company = intent.getStringExtra("telphone");
            position = intent.getStringExtra("position");
            realname = intent.getStringExtra("realname");
            oderBean = (OderDeatilBean) intent.getSerializableExtra("celebrite");
            fillData();
        }

        surplusTickets =bean.getSurpluspersonnum();
        if (bean != null) {
            price = bean.getMoney() + "";
            orderTicketPrice.setText("￥" + bean.getMoney());
            tvTotalPrice.setText("￥" + bean.getMoney());
            getTicketinfo();
        }
        if (oderBean != null) {
            orderTicketPrice.setText("￥" + oderBean.getAmount());
            tvTotalPrice.setText("￥" + oderBean.getAmount());
        }

    }

    private void fillData() {
        if (mActiveinfosBean != null) {//其他订单
            if (SharedUtil.isLogin(this)) {
                mUserInfoBean = SharedUtil.getUserInfo(this);
                tvPayUserName.setText(realname);
                tvPayUserPhone.setText(telphone);
            }
            if (!TextUtils.isEmpty(mActiveinfosBean.getActiveurl())) {
                if (mActiveinfosBean != null) {
                    //其他订单
                    if (!TextUtils.isEmpty(mActiveinfosBean.getActiveurl())){
                        iamgeUrl = mActiveinfosBean.getActiveurl();
                        iamgeView = ivOrderTicketImg;
                    }
                    setGone(viewRelativeCelebriteOrder);
                    setVisibility(viewLineOther);
                    if (!TextUtils.isEmpty(iamgeUrl)){
                        setImageView();
                    }
                    String[] strings = {mActiveinfosBean.getName(), "收费票", "￥" + mActiveinfosBean.getMoney(), "￥" + mActiveinfosBean.getMoney()};
                    setTextView(strings, tvOrderTicketTitle, orderTicketName, orderTicketPrice, tvTotalPrice);

                } else if (oderBean != null) {//名人订单
                    iamgeView = imageCelebeitePhoto;
                    setVisibility(viewRelativeCelebriteOrder);
                    setGone(viewLineOther, viewAddOtherNumber);
                    getCelebriteData();
                }

            }
        }
    }

    //获取票务信息
    public void getTicketinfo() {

        Utiles.log("getTicketinfo：=========");
        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort("网络未连接！请重试");
        }
        Utiles.log("mUserInfoBean.getUserid()：=========" + mUserInfoBean.getUserid());
        Utiles.log("mActiveinfosBean.getActiveid()：=========" + mActiveinfosBean.getActiveid());
        ActiveServiceImpl.getInstance().Ticketinfo(
                mUserInfoBean.getUserid(), mActiveinfosBean.getActiveid(), new StringCallback() {

                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("获取信息失败！请重试");
                    }

                    @Override
                    public void onResponse(String response) {
                        Utiles.log("获取票务信息response：" + response.toString());
                        StatusBean statusbean = JSON.parseObject(response, StatusBean.class);

                        if (statusbean.getCode() == 0) {
                            ticketinfobean = JSON.parseObject(statusbean.getData(), TicketInfoBean.class);
                            Utiles.log("订单票务信息：" + ticketinfobean.toString());
                            if (ticketinfobean != null) {
                                updateticketInfo();
                                ticket =ticketinfobean.getTicket();
                            }

                        }
                    }
                });
    }

    //更新票务数据
    public void updateticketInfo() {
        Utiles.log("updateticketInfo ：========");
        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort("网络无连接！请重试");
        }

        float payamount = Float.parseFloat(price);
        Utiles.log("payamount :" + payamount);
        Utiles.log("ticketnum :" + ticketnum);
        Utiles.log("ticketinfobean.getTicket() :" + ticketinfobean.getTicket());
        ActiveServiceImpl.getInstance().updateTicketNumInfo(mActiveinfosBean.getActiveid(),
                ticketnum, payamount, ticketinfobean.getTicket(), new StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("报名失败，请重试");
                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });

    }

    /**
     * 隐藏
     */
    private void setGone(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 显示
     */
    private void setVisibility(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 名人订单访问网络
     */
    private void getCelebriteData() {
        CelebrityNewWorkImpl.getInstance().getCelebrityDetails(oderBean.getTea_id(), new StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.loadFailed();
            }

            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    if (!TextUtils.isEmpty(statusBean.getData())) {
                        TeacherBean teacherBean = JSON.parseObject(statusBean.getData(), TeacherBean.class);
                        if (teacherBean != null) {
                            if (!TextUtils.isEmpty(teacherBean.getCover())) {
                                iamgeUrl = teacherBean.getCover();
                                setImageView();
                            }
                            String[] strings = {teacherBean.getCaption(),
                                    teacherBean.getRealname(),
                                    teacherBean.getPosition(),
                                    "￥" + teacherBean.getPrice() + "",
                                    "￥" + oderBean.getAmount() + "",
                            };
                            setTextView(strings, textViewCelebeiteTitle, textViewCelebeiteName, textViewCelebeiteJob,
                                    textViewCelebeitePrice, tvTotalPrice);
                        }
                    }
                } else {
                    Utiles.loadFailed();
                }


            }
        });
    }

    /**
     * 给TextView赋值
     *
     * @param strings
     * @param textViews
     */
    private void setTextView(String[] strings, TextView... textViews) {
        for (int i = 0; i < strings.length; i++) {
            textViews[i].setText(strings[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlRefund:
                if (flag) {
                    llRefundContent.setVisibility(View.GONE);
                    flag = false;
                } else {
                    llRefundContent.setVisibility(View.VISIBLE);
                    flag = true;
                }
                break;
            case R.id.alipayBtn:
//			Intent intent = new Intent(SureOrderActivity.this,PaySuccessActivity.class) ;
//			startActivity(intent) ;
                if (isAli) {
                    pay();
                } else {
                    weixinPay();
                }

                break;
            case R.id.ivCheckAli:
                if (!isAli) {
                    ivCheckAli.setBackgroundResource(R.drawable.alipay_right_bg_selected);
                    ivCheckWx.setBackgroundResource(R.drawable.alipay_right_bg_normal);
                    isAli = true;
                }
                break;
            case R.id.ivCheckWx:
                if (isAli) {
                    ivCheckAli.setBackgroundResource(R.drawable.alipay_right_bg_normal);
                    ivCheckWx.setBackgroundResource(R.drawable.alipay_right_bg_selected);
                    isAli = false;
                }
                break;
            case R.id.explainBtn:
                mExplainDialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClassConcrete.getInstance().removeObserver(this);
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.WEIXIN_PAY_SUCCESS) {
            ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.ACCOUNT_BAOMING_TYPE));
            isFlag = false;
            if (mActiveinfosBean != null) {
                paySuccess();
            }

        } else if (event.getType() == EventType.WEIXIN_PAY_ERROR) {
            ToastUtil.showShort("支付失败!");
        }
        return false;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
//    private String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
//        return key;
//    }

    /**
     * 给订单图片设置值
     */
    private void setImageView() {
        if (!TextUtils.isEmpty(iamgeUrl)) {
            Picasso.with(this).load(iamgeUrl).placeholder(R.drawable.default_square_image).error(R.drawable.default_square_image).fit().centerCrop().into(iamgeView);
        } else {
            Picasso.with(this).load(R.drawable.default_square_image).placeholder(R.drawable.default_square_image).into(ivOrderTicketImg);
        }
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        if (TextUtils.isEmpty(UrlsManager.PARTNER) || TextUtils.isEmpty(UrlsManager.RSA_PRIVATE) || TextUtils.isEmpty(UrlsManager.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo;
        if (oderBean != null) {
            orderInfo = getOrderInfo(" 长安支付01",
                    "长安支付02", 0.1 + "");
            Utiles.log("支付宝价格：" + oderBean.getAmount());
        } else {
            orderInfo = getOrderInfo("约吧票券", "活动票券", price + "");
            Utiles.log("支付宝价格：" + price);
        }

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
            Utiles.log("签名：" + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                Utiles.log("======>去支付");
                // 构造PayTask 对象
                PayTask alipay = new PayTask(SureOrderActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = UrlsManager.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /***
     * 支付成功
     */
    private void paySuccess() {
        dialog.show();
        //支付成功
        ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.ACCOUNT_BAOMING_TYPE));
        Intent intent = new Intent(SureOrderActivity.this, PaySuccess2Activity.class);
        intent.putExtra("data", mActiveinfosBean);
        intent.putExtra("TicketAddBean", bean);
        SureOrderActivity.this.startActivity(intent);
        dialog.dismiss();
        if (isFlag) {
            finish();
            dialog.dismiss();
        }

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
//		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
        ToastUtil.showShort(version);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + UrlsManager.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + UrlsManager.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + ticket + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://yueba.cc/tryst/pay/alipay/notify_url.jsp" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, UrlsManager.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     *
     */
    private void weixinPay() {
        req = new PayReq();
        sb = new StringBuffer();

        msgApi.registerApp(UrlsManager.APP_ID);

        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();

        //微信价格单位是分 所以要*100
        float weixinprice = Float.parseFloat(price);
        Utiles.log("微信支付价格：" + weixinprice);
        getPrepayId.execute("约吧活动票券", ((int)( weixinprice * 100)) + "");

    }

    /**
     * 生成签名  step1
     */

    private String genPackageSign(List<NameValuePair> params) {

        Utiles.log("genPackageSign");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(UrlsManager.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion-packageSign：", packageSign);
        return packageSign;
    }

    //step5
    private String genAppSign(List<NameValuePair> params) {
        Utiles.log("genAppSign");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(UrlsManager.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion-appSign：", appSign);
        return appSign;
    }

    //step2
    private String toXml(List<NameValuePair> params) {
        Utiles.log("toXml");
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion-sb.toString()：", sb.toString());
        return sb.toString();
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion-e.toString()：", e.toString());
        }
        return null;

    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

//    private String genOutTradNo() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }

    /**
     * 获取预支付订单号:
     *
     * @param body  商品描述
     * @param price 商品价格
     *  要根据字典排序
     */
    private String genProductArgs(String body, String price) {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", UrlsManager.APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("detail", body));
            packageParams.add(new BasicNameValuePair("mch_id", UrlsManager.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://yueba.cc/tryst/weixnotify"));
            packageParams.add(new BasicNameValuePair("out_trade_no", ticket));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", getPhoneIp()));
            packageParams.add(new BasicNameValuePair("total_fee", price));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);
            //解决body传中文报签名错误的问题，生成的xml请求参数转为字节数组后，用“ISO8859-1”编码格式进行编码为字符
            return new String(xmlstring.getBytes(), "ISO8859-1");

        } catch (Exception e) {
            Log.e("SureOrderActivity", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

    /***
     * 获取二次签名sign  step6
     */
    private void genPayReq() {

        req.appId = UrlsManager.APP_ID;
        req.partnerId = UrlsManager.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        Log.e("orion-signParams：", signParams.toString());
    }

    /***
     * 起调微信支付
     */
    private void sendPayReq() {
        Utiles.log("起调微信支付sendPayReq");
        msgApi.registerApp(UrlsManager.APP_ID);
        msgApi.sendReq(req);
    }

    /***
     *  获取预支付订单
     */
    private class GetPrepayIdTask extends AsyncTask<String, Void, Map<String, String>> {

        private android.app.ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = android.app.ProgressDialog.show(
                    SureOrderActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            resultunifiedorder = result;
            if (resultunifiedorder != null) {
                genPayReq();
                sendPayReq();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs(params[0], params[1]);

            Log.e("orion-entity：", entity);  //step3

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion-content：", content); //step4
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

}
