package com.xz.activeplan.ui.live.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.bfcould.VideoMgrTest;
import com.xz.activeplan.ui.live.rongclound.LiveKit;
import com.xz.activeplan.ui.live.rongclound.adapter.ChatListAdapter;
import com.xz.activeplan.ui.live.view.LivePersonAlertDialog;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bf.cloud.android.datasource.BFCaptor;
import bf.cloud.android.datasource.CameraPreview;
import bf.cloud.android.datasource.CameraRecorder;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 开始直播
 * （默认是横屏）
 */

public class StartLiveActivity extends BaseFragmentActivity implements View.OnClickListener, BFCaptor.BFCaptorEventListener, BFCaptor.BFCaptorErrorListener,Handler.Callback{

    private static final int MSG_CAPTOR_TIME_CHANGED = 102;
    private static final String TAG = "StartLiveActivity" ;
    ImageView mClose, mChange,mChangeFlash, mChat;
    CameraPreview cameraPreview;
    TextView mSpeed, mTime;
    private BFCaptor mBFCaptor = null;
    private String channelid;
    private Handler mHandler = new Handler(this);
    private EditText editText;
    private FrameLayout rootView;
    private CustomProgressDialog mProgressDialog;  //正在加载中...
    private ChatListAdapter adapter;
    private ListView listView;
    private int total;
    private TextView tvMoney;
    private StartLiveActivity activity = this;
    private Handler handler = new Handler(this);
    private TextView tvNumber;   //在线人数
    /**
     * 定时获取在线人数
     */
    private Runnable mRunnable = new Runnable() {

        public void run() {
            getchatRoomNumber();  //获取聊天室人数
            Log.e(TAG, Thread.currentThread().getName() + " ");
            tvNumber.setText(total + "");

            //每10秒执行一次
            handler.postDelayed(mRunnable, 10000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        LiveKit.addEventHandler(mHandler);
        mBFCaptor = BFCaptor.getInstance(this);
        mBFCaptor.setDirection(BFCaptor.DirectionType.LANDSCAPE);
        mBFCaptor.registBFCaptorEventListener(this);
        mBFCaptor.registBFCaptorErrorListener(this);
        setContentView(R.layout.activity_start_live);
        initData();
        initView();
        //加入聊天室
        joinChatRoom(channelid);
        //通过Handler启动线程
        handler.post(mRunnable);
    }

    /**
     * 获取直播频道
     */
    private void initData() {
        channelid = getIntent().getStringExtra("data");
        Utiles.log("-------startLive-" + channelid);
    }
    /**
     * 初始化视图
     */
    private void initView() {
        mProgressDialog = new CustomProgressDialog(this);
        rootView = (FrameLayout) findViewById(R.id.start_live_rootView);
        cameraPreview = (CameraPreview) findViewById(R.id.start_live_cameraPreview);
        mBFCaptor.setPreview(cameraPreview);
        mClose = (ImageView) findViewById(R.id.start_live_close);
        mChange = (ImageView) findViewById(R.id.start_live_change);
        mChangeFlash = (ImageView) findViewById(R.id.start_live_flash);
        mSpeed = (TextView) findViewById(R.id.start_live_speed);
        mChat = (ImageView) findViewById(R.id.start_live_chat);
        mTime = (TextView) findViewById(R.id.start_live_tvTime);
        mTime.setText(makeTime(mBFCaptor.getTimeStamp()));
        editText = (EditText) findViewById(R.id.et_sendmessage);
        tvMoney = (TextView) findViewById(R.id.start_live_tvMoney);
        tvNumber = (TextView) findViewById(R.id.start_live_tvNumber);
        //聊天list
        listView = (ListView) findViewById(R.id.start_live_listView);
        adapter = new ChatListAdapter();
        listView.setAdapter(adapter);
        //监听
        findViewById(R.id.start_live_llPersonNumber).setOnClickListener(this);
        mChat.setOnClickListener(this);
        mClose.setOnClickListener(this);  //关闭
        mChange.setOnClickListener(this);  //前后摄像头
        mChangeFlash.setOnClickListener(this); //闪光
        mProgressDialog.show();  //bug
        mBFCaptor.startPreview();
        mBFCaptor.start(channelid);


    }

    /**
     * 获取聊天室的人数
     */
    private void getchatRoomNumber() {

        OkHttpClientManager.getAsyn(UrlsManager.URL_CHAT_ROOM_NUMBER + channelid, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log(TAG + "---fail--" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Utiles.log(TAG + "---response--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    JSONObject jsonObject1 = new JSONObject(result);
                    total = jsonObject1.getInt("total"); //人数
                    String id = null;
                    JSONArray users = jsonObject1.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject json2 = users.getJSONObject(i);
                        id = json2.getString("id"); //人员id
                    }
                    Utiles.log(TAG + "---total   --" + total + "-------" + id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 时间
     */
    private String makeTime(long time) {
        String hour = String.format("%02d", time / 3600);
        String min = String.format("%02d", time / 60);
        String sec = String.format("%02d", time % 60);
        return hour + ":" + min + ":" + sec;
    }
    /**
     * 返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            showAlertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加入聊天室
     *
     * @param roomId 聊天室ID为视频的频道Id
     */
    private void joinChatRoom(final String roomId) {
        LiveKit.joinChatRoom(roomId, 10, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Utiles.log("聊天室加入成功");
                TextMessage textMessage = TextMessage.obtain("进来了");
                textMessage.setExtra("{type:'1'}");
                LiveKit.sendMessage(textMessage);

            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Utiles.log("聊天室加入失败" + errorCode.getMessage().toString());
            }
        });
    }

    /**
     * 开始
     */
    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * 单机事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_live_close:   //关闭
                showAlertDialog();
                break;
            case R.id.start_live_change:   //切换摄像头
                changeCamera();
                break;
            case R.id.start_live_flash:          //开启或关闭闪光灯
                changeFlash();
                break;
            case R.id.start_live_chat:
                Utiles.log("弹出软键盘");
                Utiles.getKeyBoard(editText, "open");
                break;
            case R.id.start_live_llPersonNumber:   //弹出人数
                showLiveDialog();
                break;
        }
    }

    /**
     * 显示人数
     */
    private void showLiveDialog() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.live_alert_person_number, null);
        ImageView close = (ImageView) view.findViewById(R.id.alert_close);
        final LivePersonAlertDialog alertDialog = new LivePersonAlertDialog(this, view).builder();
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    /**
     * 开启或关闭闪光灯
     */
    private void changeFlash() {
        if (mBFCaptor.getFlashState()) {
            mBFCaptor.changeFlash(false);
            mChangeFlash.setImageResource(R.drawable.live_flash_cancel);
        } else {
            mBFCaptor.changeFlash(true);
            mChangeFlash.setImageResource(R.drawable.live_flash);
        }
    }

    /**
     * 切换摄像头
     */
    private void changeCamera() {
        if (mBFCaptor != null) {
            if (mBFCaptor.getCameraType() == CameraRecorder.CameraType.BACK)
                mBFCaptor.setCameraType(CameraRecorder.CameraType.FRONT);
            else
                mBFCaptor.setCameraType(CameraRecorder.CameraType.BACK);
        }
    }

    /**
     * 恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
        mBFCaptor.startPreview();
        // mBFCaptor.setDataRate(90);
        if (!"00:00:00".equals(mTime.getText().toString())) {
            mTime.setText("00:00:00");
        }
    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mBFCaptor != null) {
            mBFCaptor.stop();
            mBFCaptor.onTextureDestroyed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveKit.removeEventHandler(mHandler);
        handler.removeCallbacks(mRunnable);
        if (!"00:00:00".equals(mTime.getText().toString())) {
            mTime.setText("00:00:00");
        }
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        Utiles.log("----------------startLive-----------onDestroy-");
    }

    @Override
    public void onEvent(int eventCode) {
        switch (eventCode) {
            case BFCaptor.EVENT_STATE_TIME_CHANGED:
                mHandler.sendEmptyMessage(MSG_CAPTOR_TIME_CHANGED);
                break;
        }
    }

    @Override
    public void onError(int i) {

    }

    /****
     * 退出直播弹框
     */
    private void showAlertDialog() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_yesorno, null);
        com.xz.activeplan.ui.live.view.AlertDialog dialog = new com.xz.activeplan.ui.live.view.AlertDialog(activity, view,0.45f).builder();
        dialog.show();
        view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
        view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.txt_msg)).setText("确定要退出直播吗?");
        dialog.setPositiveButton("继续直播", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setNegativeButton("结束",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBFCaptor.stop();
                        mBFCaptor.release();
                        //跳转到打赏页面
                        if(!TextUtils.isEmpty(channelid))
                        {
                            Intent intent = new Intent(activity, EndLiveActivity.class);
                            intent.putExtra("total",total);  //观看人数
                            intent.putExtra("money",Float.valueOf(tvMoney.getText().toString()));    //打赏金额
                            startActivity(intent);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    VideoMgrTest test = new VideoMgrTest();
                                    try {
                                           test.deleteChannel(channelid);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            activity.finish();
                        }
                    }
                });
    }

    @Override
    public boolean handleMessage(Message msg) {
        Utiles.log("聊天室：  "+msg.what);
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {  //接收消息
                MessageContent content = (MessageContent) msg.obj;
                Utiles.log("接收到的消息："+content.getUserInfo().getName());
                adapter.addMessage(content);
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
                break;
            }
            case LiveKit.MESSAGE_SENT: {   //发送消息
                MessageContent content = (MessageContent) msg.obj;
                Utiles.log("发送的消息："+content.getUserInfo().getName());
                adapter.addMessage(content);
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {  //错误消息
                break;
            }
            case MSG_CAPTOR_TIME_CHANGED:
                mTime.setText(makeTime(mBFCaptor.getTimeStamp()));
                if (!"00:00:00".equals(mTime.getText().toString())) {
                    mProgressDialog.dismiss();
                }
                break;
            default:
        }
        return false;
    }
}
