package com.xz.activeplan.ui;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.isnc.facesdk.aty.Aty_BaseGroupCompare;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketInfoBean;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.recommend.activity.ScanSuccessActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

/**
 * 扫一扫 和扫脸
 */
public class ScanActivity extends Aty_BaseGroupCompare implements Callback, View.OnClickListener {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private static final String TAG = "扫一扫";
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private TextView mTvHeadTitle;
    private ImageView ivBack;  //返回
    private ImageView btnCode;
    private ImageView btnFace;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView ivFlash;
    private String thirid;
    private ActiveinfosBean bean;
    private FrameLayout FaceControl; //扫脸布局
    private FrameLayout relativeLayout; //扫码布局

    @Override
    protected int getContentLayoutId() {
        Utiles.setStatusBar(this);
        Utiles.log(TAG + "-------getContent");
        return R.layout.activity_capture1;
    }

    @Override
    protected void initView() {
        Utiles.log(TAG + "-------initView");
        CameraManager.init(getApplication());
        /**
         * -------------------扫码-------------------
         */
        int[] size = {480, 640};
        initFacesFeature(size);
        relativeLayout = (FrameLayout) findViewById(R.id.capture_rlCode);
        /**
         * 头部管理器
         */
        headManager();

        /**
         * 扫码 、扫脸按钮切换
         */
        btnCode = (ImageView) findViewById(R.id.capture_btScanCode);
        btnFace = (ImageView) findViewById(R.id.capture_btScanFace);
        btnCode.setOnClickListener(this);
        btnFace.setOnClickListener(this);


        FaceControl = (FrameLayout) findViewById(R.id.container);

        mFacesGroupCompareView.pause();
        mFacesGroupCompareView.stopPreview();
        mFacesGroupCompareView.setmPreviewing(false);

    }

    /**
     * 单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.two_img:          //闪光灯(open/close)和 摄像头前后切换
                break;
            case R.id.capture_btScanCode:  //扫码
                relativeLayout.setVisibility(View.VISIBLE);
                viewfinderView.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.VISIBLE);
                changeCamera();
                FaceControl.setVisibility(View.GONE);
                break;
            case R.id.capture_btScanFace:  //扫脸
                FaceControl.setVisibility(View.VISIBLE);
                CameraManager.get().closeDriver();
                relativeLayout.setVisibility(View.GONE);
                surfaceView.setVisibility(View.GONE);
                viewfinderView.setVisibility(View.GONE);

                setCameraType(0);
                facesDetect("0095e0fa6697b7734894e57b", "e5ca4af98ede2f8bcc2c265c1cd4862f");
                break;
            case R.id.id_ImageHeadTitleBlack:  //返回
                finish();
                break;
        }
    }

    @Override
    protected void initFacesFeature(int[] ints) {
        super.initFacesFeature(ints);
        Utiles.log(TAG + "初始化扫脸布局。。。。");
    }

    /**
     * 头部管理器
     */
    private void headManager() {
        mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        ivBack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        mTvHeadTitle.setText("扫一扫");
        findViewById(R.id.share_ly).setVisibility(View.VISIBLE);
        ivFlash = (ImageView) findViewById(R.id.two_img);
        ivFlash.setImageResource(R.drawable.scan_flash_yes);
        ivFlash.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bean = (ActiveinfosBean) getIntent().getSerializableExtra("data");

        Utiles.log(TAG + "-------initData" + bean.toString());
    }

    @Override
    protected void initActions() {
        Utiles.log(TAG + "-------initActions");
    }

    @Override
    public void onResume() {
        super.onResume();
        Utiles.log(TAG + "-------onResume");
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        Utiles.log(TAG + "-------onPause");
        if (null != handler) {
            handler.quitSynchronously();
            handler = null;
        } else {
            return;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        CameraManager.get().closeDriver();
        inactivityTimer.shutdown();
        Utiles.log(TAG + "-------onDestroy");
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            ToastUtil.showShort("Scan failed!");
        } else {
            checkTicket(resultString, bean.getActiveid());
        }

    }

    /**
     * 检票
     *
     * @param ticket
     */
    private void checkTicket(String ticket, int activeId) {
        try {
            UserInfoServiceImpl.getInstance().checkTicket(ticket, activeId, new StringCallback() {

                @Override
                public void onResponse(String response) {
                  /*  StatusJson statusJson = new StatusJson();
                    Object obj = statusJson.analysisJson2Object(response);
                    if (obj != null) {
                        StatusBean statusBean = (StatusBean) obj;
                        if (statusBean.getCode() == 0) {
                            ToastUtil.showShort("验票成功");
                        } else {
                            ToastUtil.showShort("验票出错，请重试!");
                        }
                    } else {
                        ToastUtil.showShort("验票出错，请重试!");
                    }
                    finish();*/
                    StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                    if (statusBean.getCode() == 0) {

                        if (statusBean.getMsg().equals("该票已经验过了")) {
                            ToastUtil.showCenterToast(ScanActivity.this, "该票已经验过了");
                            return;
                        } else {
                            TicketInfoBean bean = JSON.parseObject(statusBean.getData(), TicketInfoBean.class);
                            ToastUtil.showCenterToast(ScanActivity.this, "验票成功");
                            Intent intent = new Intent(ScanActivity.this, ScanSuccessActivity.class);
                            intent.putExtra("data", bean);
                            startActivity(intent);
                        }
                    } else {
                        ToastUtil.showCenterToast(ScanActivity.this, "验票出错，请重试!");
                    }
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.showShort("验票出错，请重试!");
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化相机和打开
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats,
//                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    protected void doFacesCallBack(JSONObject jsonObject) {
        super.doFacesCallBack(jsonObject);
        Utiles.log("人脸检索回调： " + jsonObject.toString());

        /**
         * {"source_app_id":"0095e0fa6697b7734894e57b","users":[{"openId":"ab93f42c8b921c5bdd7bc257943931f0","groupUid":"c13cf124c5855c94ea6126de476d5328","groupId":"e5ca4af98ede2f8bcc2c265c1cd4862f","rank":1}],"hack_detect_score":0.6639}
         */
        try {
            JSONArray users = null;
            try {
                users = jsonObject.getJSONArray("users");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (users.length() == 0) {
                ToastUtil.showCenterToast(this, "你的人脸检索不匹配,请重新刷脸!");
                return;
            } else {
                for (int i = 0; i < users.length(); i++) {
                    JSONObject jsonObject1 = users.getJSONObject(i);
                    String openId = jsonObject1.getString("openId");
                    String groupUid = jsonObject1.getString("groupUid");
                    String groupId = jsonObject1.getString("groupId");
                    Utiles.log("人脸检索回调：(组里面的用户ID) " + groupUid);  //c13cf124c5855c94ea6126de476d5328

                    requestData(groupUid);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求人脸数据
     *
     * @param groupUid
     */
    private void requestData(String groupUid) {

        ActiveServiceImpl.getInstance().checkFace(bean.getActiveid(), groupUid, new StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(String response) {
                Utiles.log("人脸检索回调——后台：" + response);
                /**
                 * {"code":0,"data":{"activeid":1140,"activeurl":"","address":"","charge":false,"company":null,"createdate":1473227346805,"enddate":0,"headurl":"","id":436,"isend":false,"name":"人脸测试","num":0,"payamount":0,"paynum":null,"paytype":0,"phonenum":"","position":null,"realname":"哈佛","startdate":0,"telphone":"15012984485","ticket":"80511403105","ticketcheck":1,"tickettypeid":1197,"userid":21,"username":"","weixin":null},"msg":"","total_size":0}
                 */
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {

                    //String msg = "{\"activeid\":1140,\"activeurl\":\"\",\"address\":\"\",\"charge\":false,\"company\":null,\"createdate\":1473227346805,\"enddate\":0,\"headurl\":\"\",\"id\":436,\"isend\":false,\"name\":\"人脸测试\",\"num\":0,\"payamount\":0,\"paynum\":null,\"paytype\":0,\"phonenum\":\"\",\"position\":null,\"realname\":\"哈佛\",\"startdate\":0,\"telphone\":\"15012984485\",\"ticket\":\"80511403105\",\"ticketcheck\":1,\"tickettypeid\":1197,\"userid\":21,\"username\":\"\",\"weixin\":null}";
                    if (statusBean.getMsg().equals("该票已经验过了")) {
                        ToastUtil.showCenterToast(ScanActivity.this, "您的人脸已经通过验证了!");
                        return;
                    } else {
                        TicketInfoBean bean = JSON.parseObject(statusBean.getData(), TicketInfoBean.class);
                        ToastUtil.showCenterToast(ScanActivity.this, "验票成功");
                        Intent intent = new Intent(ScanActivity.this, ScanSuccessActivity.class);
                        intent.putExtra("data", bean);
                        startActivity(intent);
                    }
                } else {
                    ToastUtil.showCenterToast(ScanActivity.this, "你的人脸检索不匹配,请重新刷脸!");
                }

            }
        });

    }
}