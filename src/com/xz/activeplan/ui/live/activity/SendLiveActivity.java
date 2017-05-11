package com.xz.activeplan.ui.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bigkoo.pickerview.TimePopupWindow;
import com.oginotihiro.cropview.CropView;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.LiveDataBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.bfcould.VideoMgr;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.HMACSHA1;
import com.xz.activeplan.utils.JsonUtil;
import com.xz.activeplan.utils.LogUtil;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.alipay.Base64;
import com.xz.activeplan.views.CustomProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 发起直播页面
 */
public class SendLiveActivity extends BaseFragmentActivity implements View.OnClickListener, SurfaceHolder.Callback {

    public static final int LOCAL_PHOTO_ALBUM = 0; // Local photo album
    public static final int TAKING_PICTURES = 1; // Taking pictures
    private static final int CHANNEL_MSG = 0x110;
    private static final int AGREE_MSG = 2;
    public String channelid;  //频道ID
    TextView mCancel, mStart, mSee, mClassify;
    Bitmap mBitmap;
    ImageView mFengmian, mSina, mQQ, mWenxin, mFriend;
    private AlertDialog alertDialog;
    private Button startLive;
    private String mPicPath = "";
    private boolean flag = true;  //标记是否被点击
    private String cansee;   //谁可以看
    private JSONObject jsonObject1;
    private TimePopupWindow timePopupWindow;  //时间选择器
    private SurfaceView mySurfaceView;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private EditText mTitle;
    private String time;  //开始时间
    private int money = 0;  //默认公开
    private LiveDataBean bean;
    private String title1 = "";
    private String classify;
    private String liveURL;
    private String gcid;
    private ImageView image = null;
    private int index;
    private CustomProgressDialog mProgressDialog;
    private CropView cropview;
    private LinearLayout btnlay;
    private Button cancelBtn, doneBtn;
    private Bitmap croppedBitmap;
    private boolean isAgree =false;
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CHANNEL_MSG) {
                ToastUtil.showCenterToast(SendLiveActivity.this, "发起直播失败...");
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_live);
        initView();
        long log = System.currentTimeMillis() / 1000;
    }

    /**
     * 创建直播频道  获取Token
     */
    private void post() {
        String Data = JsonUtil.object2Json(bean);
        String EncodeData = Base64.encode(Data.getBytes());
        try {
            String str = HMACSHA1.hmac_sha1("ocddhokn1jLflQpZb667Qd09bXAUvKxt9i6hTfiT", EncodeData);
            String AccessKey = "=X6dhokn1S09lQY51667QdQs9c7jPYnXWZQPd3Y-";
            String Token = AccessKey + ":" + str + ":" + EncodeData;
            LogUtil.show("send--------Token--", Token);
            String replace = Token.replace("\n", "");
            String urlToken = URLEncoder.encode(replace, "UTF-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("token", replace);
            jsonObject1 = new JSONObject(map);
            sendPost();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求暴风云
     */
    private void sendPost() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UrlsManager.BF_CREATE_LIVE, jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.show("send----------", jsonObject.toString());

                try {
                    channelid = jsonObject.getString("channelid");  //频道ID
                    gcid = jsonObject.getString("gcid");  //使用id
                    liveURL = jsonObject.getString("url");   //视频播放地址
                    LogUtil.show("send----------", "channelid" + channelid);
                    if (channelid.length() > 0) {
                        postServer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.show("send----------", "error" + volleyError.getMessage());
            }
        });
        XZApplication.getQueue().add(jsonObjectRequest);

    }

    /**
     * 初始化视图
     */
    private void initView() {
        mProgressDialog = new CustomProgressDialog(this);
        mTitle = (EditText) findViewById(R.id.live_etTitle);
        mCancel = (TextView) findViewById(R.id.live_cancel);
        mSee = (TextView) findViewById(R.id.live_see);
        mClassify = (TextView) findViewById(R.id.live_classify);
        mFengmian = (ImageView) findViewById(R.id.live_fengmian);
        startLive = (Button) findViewById(R.id.live_bt_start_live);
        mSina = (ImageView) findViewById(R.id.send_ivSina);
        mQQ = (ImageView) findViewById(R.id.send_ivQQ);
        mWenxin = (ImageView) findViewById(R.id.send_ivWenxin);
        mFriend = (ImageView) findViewById(R.id.send_ivFriend);
        mFengmian.setOnClickListener(this);
        mStart = (TextView) findViewById(R.id.live_tv_start);
        findViewById(R.id.live_llStartTime).setOnClickListener(this);
        findViewById(R.id.live_llCanSee).setOnClickListener(this);
        findViewById(R.id.live_llClassify).setOnClickListener(this);
        mySurfaceView = (SurfaceView) findViewById(R.id.live_surfaceView);
        surfaceHolder = mySurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        setListener();

        cropview = (CropView) findViewById(R.id.cropView);
        btnlay = (LinearLayout) findViewById(R.id.btnlay);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        doneBtn = (Button) findViewById(R.id.doneBtn);

        cancelBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);

    }

    /**
     * 设置监听
     */
    private void setListener() {
        timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.ALL);
        //时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                //格式化时间
                time = TimeUtils.formatTime(date);
                //得到当前系统时间
                String currentDateString = TimeUtils.getCurrentDateString();
                //得到时间差
                boolean before = TimeUtils.minutesBefore(time, currentDateString);
                LogUtil.show("time---------", "---" + time + "--------" + currentDateString);
                if (before == false) {
                    ToastUtil.showShort("选择时间不能小于当前时间");
                    mStart.setText("立即直播");
                    time = currentDateString;
                } else {
                    mStart.setText(time);
                }
            }
        });
        mCancel.setOnClickListener(this);
        startLive.setOnClickListener(this);
        mSina.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mWenxin.setOnClickListener(this);
        mFriend.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_fengmian:   //设置封面
                showCustomDialog();
                break;
            case R.id.live_cancel:    //关闭
                finish();
                break;
            case R.id.live_llStartTime:   //开始时间
                timePopupWindow.showAtLocation(mCancel, Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.live_llCanSee:   //谁可以看
                Intent intent = new Intent(this, CanSeeActivity.class);
                startActivityForResult(intent, AGREE_MSG);
                break;
            case R.id.live_llClassify:   //分类
                showClassifyDialog();
                break;
            case R.id.live_bt_start_live:   //立即直播
                if (!NetworkInfoUtil.checkNetwork(this)) {
                    ToastUtil.showShort("网络无连接，请检查网络!");
                    return;
                }
                checkInfo();
                break;
            case R.id.send_ivSina:   //新浪微博分享
                changeImage(1, mSina);
                break;
            case R.id.send_ivQQ:     //qq
                changeImage(2, mQQ);
                break;
            case R.id.send_ivWenxin:   //微信
                changeImage(3, mWenxin);
                break;
            case R.id.send_ivFriend:   //朋友圈
                changeImage(4, mFriend);
                break;
            case R.id.cancelBtn:
                reset();
                break;
            case R.id.doneBtn:
                final android.app.ProgressDialog dialog = android.app.ProgressDialog.show(
                        SendLiveActivity.this, null, "请稍等...", true, false);

                cropview.setVisibility(View.GONE);
                btnlay.setVisibility(View.GONE);
                mFengmian.setVisibility(View.VISIBLE);

                new Thread() {
                    public void run() {
                        croppedBitmap = cropview.getOutput();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFengmian.setImageBitmap(croppedBitmap);
                            }
                        });

                        mPicPath = getPictureName();
                        FileUtil.saveBitmapToSdcard(mPicPath, croppedBitmap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                    }
                }.start();
                break;
        }
    }

    private void changeImage(int i, ImageView iv) {
        if (image == null) {
            image = iv;
            index = i;
        } else if (image != iv || iv == image) {
            switch (index) {
                case 1:
                    image.setImageResource(R.drawable.icon_sina_default);
                    break;
                case 2:
                    image.setImageResource(R.drawable.icon_qq_default);
                    break;
                case 3:
                    image.setImageResource(R.drawable.icon_wechat_default);
                    break;
                case 4:
                    image.setImageResource(R.drawable.icon_wefri_default);
                    break;
            }
            if (iv == image) {
                index = 0;
                image = null;
                return;
            }
        }
        switch (i) {
            case 1:
                iv.setImageResource(R.drawable.share_sina_icon_bg_selected);
                ShareSDKUtils.shareSina( "我正在约吧互动直播","");
                break;
            case 2:
                iv.setImageResource(R.drawable.login_qq_icon_bg);
                ShareSDKUtils.shareQZone("约吧互动直播","","","");
                break;
            case 3:
                iv.setImageResource(R.drawable.share_wx_icon_bg_normal);
                ShareSDKUtils.shareWechat( "约吧互动直播","","","");
                break;
            case 4:
                iv.setImageResource(R.drawable.share_wxpy_icon_bg_selected);
                ShareSDKUtils.shareWechatMoments("约吧互动直播","","");
                break;
        }
        index = i;
        image = iv;
    }

    /**
     * 检查直播信息是否填写
     */
    private void checkInfo() {
        title1 = mTitle.getText().toString();  //标题
        classify = mClassify.getText().toString();   //类别

        if (TextUtils.isEmpty(mPicPath)) {
            ToastUtil.showShort("请设置直播封面");
            return;
        }
        if (TextUtils.isEmpty(title1)) {
            ToastUtil.showShort("请填写标题");
            return;
        }
       /* if ("请选择类别".equals(classify)) {
            ToastUtil.showShort("请选择类别");
            return;
        }*/
        if(isAgree==false) {
            Intent intent = new Intent(this,LiveAgreementActivity.class);
            startActivityForResult(intent,AGREE_MSG);
        }else
        {
            mProgressDialog.show();
            /******************************请求暴风云后台  创建直播频道  **********************************************/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    VideoMgr videoMgr = new VideoMgr(UrlsManager.SecretKey, UrlsManager.AccessKey);
                    try {
                        videoMgr.createChannel(title1, 529, 1, 1, title1, 0, 3600*2, 1, UrlsManager.URL_BF_CALLBACK);
                        channelid = videoMgr.getChannelId();  //频道ID
                        gcid = videoMgr.getGCId();  //使用id
                        liveURL = videoMgr.getURL();   //视频播放地址
                        if (!TextUtils.isEmpty(channelid) && channelid.length() > 0) {

                            postServer();   //post到服务器

                        } else {
                            mProgressDialog.dismiss();
                            mHanlder.sendEmptyMessage(CHANNEL_MSG);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        Utiles.log("---------" + mPicPath + "------" + mTitle.getText().toString() + "-----" + mClassify.getText().toString());

    }

    /**
     * 将数据上传到服务器
     */
    private void postServer() {

        OkHttpClientManager.getInstance();
        String tl = "";
        //标题
        try {
            tl = new String(title1.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpClientManager.Param title = new OkHttpClientManager.Param("title", tl);
        //开始时间
        if (TextUtils.isEmpty(time)) {   //当用户不选择时间，默认为系统时间
            time = TimeUtils.getCurrentDateString();
        }
        long date = TimeUtils.getLongTime13(time);
        OkHttpClientManager.Param startdate = new OkHttpClientManager.Param("startdate", date + "");
        //谁可以看
        OkHttpClientManager.Param seetype = new OkHttpClientManager.Param("seetype", money + "");
        //分类
        Utiles.log("sendLive--------date-" + date + "---------" + time);
        int i = 1;
        if (classify.equals("活动现场")) {
            i = 1;
        } else if (classify.equals("户外现场")) {
            i = 2;
        } else if (classify.equals("全名直播")) {
            i = 3;
        }
        OkHttpClientManager.Param categoryid = new OkHttpClientManager.Param("categoryid", i + "");
        //封面URL （传文件）

        //观看人数
        OkHttpClientManager.Param seenum = new OkHttpClientManager.Param("seenum", 0 + "");
        //状态  预约1 直播2 回放3
        OkHttpClientManager.Param status = new OkHttpClientManager.Param("status", 2 + "");
        //发布者ID
        int userid = 0;
        UserInfoBean userInfo = SharedUtil.getUserInfo(this);
        if (userInfo != null) {
            userid = userInfo.getUserid();
        }
        OkHttpClientManager.Param userid1 = new OkHttpClientManager.Param("userid", userid + "");
        //频道ID
        OkHttpClientManager.Param channelid1 = new OkHttpClientManager.Param("channelid", channelid);
        //使用Id
        OkHttpClientManager.Param gcid1 = new OkHttpClientManager.Param("gcid", gcid);
        //视频url
        OkHttpClientManager.Param url = new OkHttpClientManager.Param("url", liveURL);
        try {
            OkHttpClientManager.postAsyn(UrlsManager.URL_SEND_LIVE, new MyStringCallback(), new File(mPicPath), "headfile",
                    title, startdate, seetype, categoryid, seenum, status, userid1, channelid1, gcid1, url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (channelid.length() > 0) {

            Intent intent = new Intent(this,StartLiveActivity.class);
            intent.putExtra("data",channelid);
            startActivity(intent);
            mProgressDialog.dismiss();
            SendLiveActivity.this.finish();
        }
    }

    /**
     * 分类弹框
     * (活动现场、户外现场、全名直播)
     */
    private void showClassifyDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_classify_dialog, null);
        final TextView huodong = (TextView) view.findViewById(R.id.txt_msg1);
        final TextView huwai = (TextView) view.findViewById(R.id.txt_msg2);
        final TextView quanming = (TextView) view.findViewById(R.id.txt_msg3);
        alertDialog = new AlertDialog(this, view, 0.85f).builder();
        alertDialog.setMsg("分类")
                .show();
        huodong.setOnClickListener(new View.OnClickListener() {    //活动现场
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                mClassify.setText(huodong.getText().toString());

            }
        });
        huwai.setOnClickListener(new View.OnClickListener() {     //户外现场
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                mClassify.setText(huwai.getText().toString());
            }
        });
        quanming.setOnClickListener(new View.OnClickListener() {   //全名直播
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                mClassify.setText(quanming.getText().toString());
            }
        });
    }

    /**
     * 设置封面
     * （拍照、相册获取）
     */
    private void showCustomDialog() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.custom_dialog, null);
        TextView pazhao = (TextView) view.findViewById(R.id.txt_msg1);
        TextView xiangce = (TextView) view.findViewById(R.id.txt_msg2);
        alertDialog = new AlertDialog(this, view, 0.85f).builder();
        alertDialog.setMsg("设置直播封面")
                .show();
        pazhao.setOnClickListener(new View.OnClickListener() {   //拍照
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                if (camera != null) {
                    camera.stopPreview();
                    getImageFromCamera();
                }
            }
        });
        xiangce.setOnClickListener(new View.OnClickListener() {   //相册
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                getImageFromAlbum();
            }
        });
    }

    /**
     * 从相册获取照片
     */
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, LOCAL_PHOTO_ALBUM);
    }

    /**
     * 拍照
     */
    protected void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPicPath = getPictureName();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, TAKING_PICTURES);
    }

    private String getPictureName() {
        String fileRootDir = FileUtil.getRootPath() + "TempImage/";
        if (FileUtil.isFileExist(fileRootDir) == false) {
            FileUtil.createDir(fileRootDir);
        }
        return fileRootDir + UUID.randomUUID().toString() + ".jpg";
    }

    /**
     * 回调activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == LOCAL_PHOTO_ALBUM) {

                Uri source = data.getData();
                cropview.setVisibility(View.VISIBLE);
                btnlay.setVisibility(View.VISIBLE);
                cropview.of(source).asSquare().initialize(SendLiveActivity.this);
            } else if (resultCode == Activity.RESULT_OK && requestCode == TAKING_PICTURES) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                } else {
                    uri = Uri.fromFile(new File(mPicPath));
                }
                cropview.setVisibility(View.VISIBLE);
                btnlay.setVisibility(View.VISIBLE);
                cropview.of(uri).asSquare().initialize(SendLiveActivity.this);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mPicPath = "";
            mBitmap = null;
        }

        /**
         *   获取谁可以看页面的返回的值
         */
        if (resultCode == 102) {
            money = data.getIntExtra("money", 0);  //只需将money传到后台
            if (money == 0) {
                cansee = "公开";
            } else if (money > 0 && money < 500) {
                cansee = "付费";
            } else if (money > 500) {
                cansee = "密码";
            }
            mSee.setText(cansee);
        }
        /**
         * 直播协议同意回调
         * LiveAgreementActivity
         * 返回true则表示已经同意了
         */

        if(requestCode==AGREE_MSG && resultCode==201) {
            String isAgree = data.getStringExtra("agree");
            Utiles.log("isAgree--"+isAgree);
            if(!TextUtils.isEmpty(isAgree) && isAgree.equals("yes"))
            {
                 this.isAgree = true;
                 //ToastUtil.showCenterToastToLong(this,"开始你的直播之旅吧");
            }else
            {
                ToastUtil.showCenterToastToLong(this,"请先同意直播协议哦");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {  //恢复
        super.onResume();
       /* mBFCaptor.startPreview();*/
        LogUtil.show("sendlive", "------onResume");
        if (camera != null) {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        }
    }

    @Override
    protected void onPause() {  //暂停
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceView创建时，建立Camera和SurfaceView的联系
        Utiles.log("surfaceCreated");
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showCenterToast(this,"请打开摄像头权限");
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        // 取能适用的最大的SIZE
        Camera.Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
        if (camera != null) {
            Utiles.log("surfaceChanged");
            Camera.Parameters parameters = camera.getParameters();// 获取mCamera的参数对象
            Camera.Size largestSize = getBestSupportedSize(parameters
                    .getSupportedPreviewSizes());
            parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
            largestSize = getBestSupportedSize(parameters
                    .getSupportedPictureSizes());// 设置捕捉图片尺寸
            parameters.setPictureSize(largestSize.width, largestSize.height);
            camera.setParameters(parameters);

            try {
                camera.startPreview();
            } catch (Exception e) {
                if (camera != null) {
                    camera.release();
                    camera = null;
                }

            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceView销毁时，取消Camera预览
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private void reset() {
        cropview.setVisibility(View.GONE);
        btnlay.setVisibility(View.GONE);
        mFengmian.setImageBitmap(null);
    }

    class MyStringCallback implements OkHttpClientManager.StringCallback {

        @Override
        public void onFailure(com.squareup.okhttp.Request request, IOException e) {

        }

        @Override
        public void onResponse(String response) {

        }
    }
}