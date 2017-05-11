package com.xz.activeplan.ui.personal.text;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.LoginServiceImpl;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.ImageTools;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 绑定手机号布局的dialog
 *
 */
public class BingPhoneViewDialog implements View.OnClickListener {

    private static final int BINGPHONEMSG = 0x151;
    View view;
    private Context context;
    private Dialog dialog;
    private Display display;
    private float width ;
    private LinearLayout lLayout_bg;
    private Button btBingPhone;
    private Button btGetYZM;
    private EditText etPhone;
    private EditText etYZM;
    private int recLen;
    private Timer timer;
    private ImageView ivYZM;
    private Bitmap bitmap;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==BINGPHONEMSG)
            {
                ivYZM.setImageBitmap(bitmap);
            }
            if(msg.what==1)
            {
                recLen--;
                btGetYZM.setText("" + recLen + "s");
                if (recLen <= 0) {
                    timer.cancel();
                    btGetYZM.setEnabled(true);
                    btGetYZM.setOnClickListener(BingPhoneViewDialog.this);
                    btGetYZM.setText("获取验证码");
                    btGetYZM.setBackgroundResource(R.drawable.shape_blue);
                }

            }
            super.handleMessage(msg);
        }
    };


    public BingPhoneViewDialog(Context context, View view) {
        this.context = context;
        this.view = view;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public BingPhoneViewDialog(Context context, View view, float width)
    {
        this.context = context;
        this.view = view;
        this.width = width;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

    }
    public BingPhoneViewDialog(Context context, float width)
    {
        this.context = context;
        this.view = View.inflate(context,R.layout.dialog_bing_phone,null);
        this.width = width;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

    }
    public BingPhoneViewDialog builder() {
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        btBingPhone = (Button)view.findViewById(R.id.phone_btBingPhone);  //绑定手机
        btGetYZM = (Button)view.findViewById(R.id.phone_btGetYZM);  //得到验证码
        etPhone = (EditText)view.findViewById(R.id.phone_etPhone);  //手机号
        etYZM = (EditText)view.findViewById(R.id.phone_etYZM);  //验证码

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * width), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    private void setLayout()
    {
        btGetYZM.setOnClickListener(this);
        btBingPhone.setOnClickListener(this);
    }

    /**
     * 显示
     */
    public void show() {
        dialog.show();
        setLayout();
    }

    /**
     * 关闭
     */
    public void cancel()
    {
        dialog.cancel();
    }

    @Override
    public void onClick(View v) {

        if(v==btGetYZM)  //获取验证码
        {
            String phone = etPhone.getText().toString().trim();
            if(TextUtils.isEmpty(phone))
            {
                ToastUtil.showCenterToast(context,"手机号不能为空!");
                return;
            }
            if(!Utils.checkMobileNumber(phone))
            {
                ToastUtil.showCenterToast(context,"手机号格式错误!");
                return;
            }
            //显示随机安全码
             showRandomDialog(phone);
        }else if(v==btBingPhone)   //绑定手机号
        {

            String phone = etPhone.getText().toString().trim();  //手机号
            String yzm = etYZM.getText().toString().trim();  //验证码

            if(TextUtils.isEmpty(phone))
            {
                ToastUtil.showCenterToast(context,"手机号不能为空!");
                return;
            }
            if(!Utils.checkMobileNumber(phone))
            {
                ToastUtil.showCenterToast(context,"手机号格式错误!");
                return;
            }
            if(TextUtils.isEmpty(yzm))
            {
                ToastUtil.showCenterToast(context,"验证码不能为空!");
                return;
            }
            UserInfoBean userInfo = SharedUtil.getUserInfo(context);
            Utiles.log("绑定手机+-----"+userInfo.toString());
            bingPhone(phone,yzm);  //绑定

        }

    }

    /**
     * 得到bitmap
     */
    private void getBitmap(final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap= ImageTools.getUrlToBitmap(UrlsManager.URL_REG_YZM+"&phonenum="+phone);
                handler.sendEmptyMessage(BINGPHONEMSG);
            }
        }).start();
    }

    /**
     * 显示随机安全码的 Dialog
     * @param phone 手机号
     */
    private void showRandomDialog(final String phone)
    {
        getBitmap(phone);//得到随机码
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_random_code, null);
        final AlertDialog dialog = new AlertDialog(context, view,0.7f).builder();
        dialog.setCancelable(false);
        dialog.setTitle("请输入图片中的内容");
        view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
        view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.txt_msg)).setText("安全验证,点击图片换一张");
        final EditText etYZM= (EditText)view.findViewById(R.id.random_etYZM);
        ivYZM= (ImageView) view.findViewById(R.id.random_ivYZM);
        ivYZM.setImageBitmap(bitmap);
        ivYZM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBitmap(phone);//每次点击刷新随机码
            }
        });
        dialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = etYZM.getText().toString().trim();
                if(trim.length()==0)
                {
                    ToastUtil.showCenterToast(context,"验证码不能为空!");
                    return;
                }
                else
                {
                    getYZM(phone,trim);  //获取验证码
                }
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    /**
     * 得到验证码
     * @param phone
     * @param trim
     */
    private void getYZM(String phone, String randomCode) {

        LoginServiceImpl.getInstance().getCode(phone,"3", randomCode,new OkHttpClientManager.StringCallback() {

            @Override
            public void onResponse(String response) {
                Utiles.log("验证码：response"+response);
                StatusJson statusJson = new StatusJson() ;
                StatusBean statusBean = (StatusBean)statusJson.analysisJson2Object(response) ;
                if(statusBean != null && statusBean.getCode()== 0){
                    ToastUtil.showShort("获取验证码成功!") ;
                    timer = new Timer();
                    recLen = 60;
                    timer.schedule(new MyTimer(), 1000, 1000); // timeTask
                    btGetYZM.setEnabled(false);
                    btGetYZM.setBackgroundResource(R.drawable.shape_gray);
                }else{
                    ToastUtil.showShort("获取验证码失败，请重试!") ;
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                dialog.dismiss();
                if(timer != null){
                    timer.cancel();
                }
                btGetYZM.setOnClickListener(BingPhoneViewDialog.this);
                btGetYZM.setText("获取验证码");
                ToastUtil.showShort("获取验证码失败，请重试!") ;
            }
        });
    }
    /**
     * 请求服务器——将手机绑定
     * @param phone
     * @param yzm
     */
    private void bingPhone(final String phone, String yzm) {
        if(SharedUtil.isLogin(context)==true)
        {
            final UserInfoBean userInfo = SharedUtil.getUserInfo(context);
            Utiles.log("绑定手机+-----"+userInfo.toString());
            Utiles.log("类型： "+userInfo.getLoginID());
            Utiles.log("参数值："+userInfo.getLoginID()+"-----phone:"+phone+"openid: "+userInfo.getOpenid()+
            "name: "+userInfo.getUsername()+"  sex "+userInfo.getSex()+"----headurl: " +userInfo.getHeadurl()
            +"yzm "+yzm+"----" );


            LoginServiceImpl.getInstance().bingPhone(userInfo.getLoginID(), phone, userInfo.getOpenid()+"", userInfo.getUsername(),
                    userInfo.getSex(), userInfo.getHeadurl(), yzm, new OkHttpClientManager.StringCallback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Utiles.log("绑定手机：失败"+e.getMessage().toString());
                            ToastUtil.showCenterToast(context,"绑定失败..");
                        }

                        @Override
                        public void onResponse(String response) {
                            Utiles.log("绑定手机：response"+response);
                            StatusBean statusBean  = JSON.parseObject(response,StatusBean.class);
                            if(statusBean.getCode()==0)  //成功
                            {
                                ToastUtil.showCenterToast(context,"绑定成功");
                                UserInfoBean bean = JSON.parseObject(statusBean.getData(),UserInfoBean.class);
                                SharedUtil.saveUserInfo(context,bean);
                                dialog.dismiss();
                            }else
                            {
                                ToastUtil.showCenterToast(context,"绑定失败..");
                                dialog.dismiss();
                            }

                        }
                    });
        }




    }

    /**
     * 绑定按钮
     * @param listener 监听
     */
   public void setBingOnclick(final View.OnClickListener listener)
   {
       btBingPhone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.onClick(v);
               dialog.dismiss();
           }
       });
   }
    class MyTimer extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }
    }
}
