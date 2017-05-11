package com.xz.activeplan.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.recommend.activity.ScanSuccessActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {

    ImageView iv_back;
    TextView tv_title;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ActiveinfosBean bean;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.log("onCreate");
        setContentView(R.layout.activity_capture);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        View viewTop = findViewById(R.id.view_top);
        viewTop.setVisibility(View.GONE);
        tv_title = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        iv_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        tv_title.setText("扫码");
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MipcaActivityCapture.this.finish();
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        bean = (ActiveinfosBean) getIntent().getSerializableExtra("data");
    }

    public void scanFace(View v) {
        Utiles.log("scanFace");
        //扫脸
        if (bean != null) {
            Intent intent = new Intent(this, FaceGroupCompareActivity.class);
            intent.putExtra("data", bean);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utiles.log("Code_onResume");
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
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
    protected void onPause() {
        super.onPause();
        Utiles.log("Code_onPause");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utiles.log("Code_onDestroy");
        inactivityTimer.shutdown();
    }

//    @Override
//    public void finish() {
//        super.finish();
//        onPause();
//        hasSurface = false;
//        onDestroy();
//    }


    @Override
    protected void onStop() {
        super.onStop();
        Utiles.log("Code_onStop");
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        Utiles.log("handleDecode");
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Utiles.log("扫描失败! " + resultString);
        } else {
            checkTicket(result.getText());
        }

    }

    private void checkTicket(String ticket) {
        Utiles.log("checkTicket");
        Utiles.log("验票bean:"+bean.toString());
        try {
            UserInfoServiceImpl.getInstance().checkTicket(ticket, bean.getActiveid(), new StringCallback() {

                @Override
                public void onResponse(String response) {
                    StatusJson statusJson = new StatusJson();
                    Object obj = statusJson.analysisJson2Object(response);
                    if (obj != null) {
                        StatusBean statusBean = (StatusBean) obj;
                        if (statusBean.getCode() == 0) {
                            if (statusBean.getMsg().equals("该票已经验过了")) {
                                ToastUtil.showShort("该票已经验过了");
                                finish();
                            } else {
                                TicketInfoBean bean = JSON.parseObject(statusBean.getData(), TicketInfoBean.class);
                                ToastUtil.showShort("验票成功");
                                Intent intent = new Intent(MipcaActivityCapture.this, ScanSuccessActivity.class);
                                intent.putExtra("data", bean);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            ToastUtil.showShort("验票出错1，请重试!");
                            finish();
                        }
                    } else {
                        ToastUtil.showShort("验票出错2，请重试!");
                        finish();
                    }
                    finish();
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.showShort("验票出错3，请重试!");
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        Utiles.log("initCamera");
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Utiles.log("surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Utiles.log("surfaceCreated");
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Utiles.log("Code_surfaceDestroyed");
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        Utiles.log("getViewfinderView");
        return viewfinderView;
    }

    public Handler getHandler() {
//		Log.i("===========","getHandler");
        return handler;
    }

    public void drawViewfinder() {
        Utiles.log("drawViewfinder");
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        Utiles.log("initBeepSound");
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

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        Utiles.log("playBeepSoundAndVibrate");
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}