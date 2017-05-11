package com.xz.activeplan.ui.live.fragment;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.EndLiveActivity;
import com.xz.activeplan.ui.live.bfcould.VideoMgrTest;
import com.xz.activeplan.ui.live.test.BusEvent;
import com.xz.activeplan.ui.live.test.LiveChatListAdapter;
import com.xz.activeplan.ui.live.test.MyRongIM;
import com.xz.activeplan.ui.live.test.message.GiftMessage;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.ui.live.view.LivePersonAlertDialog;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bf.cloud.android.datasource.BFCaptor;
import bf.cloud.android.datasource.CameraPreview;
import bf.cloud.android.datasource.CameraRecorder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 *    发起直播页面
 *
 *  1.点击发起直播按钮  创建一个聊天室（编号唯一）
 *  2.观看直播的只需加入该聊天室即可
 *
 */

public class TestChatRoomFragment extends BaseFragment implements RongIMClient.OnReceiveMessageListener,Handler.Callback,BFCaptor.BFCaptorEventListener, View.OnClickListener, BFCaptor.BFCaptorErrorListener {

    private static final int MSG_CAPTOR_TIME_CHANGED = 102;
    private static final int MSG_REFRESH= 103;
    private static int currVolume = 0;
    ImageView mClose, mChange, mChangeAudio, mChangeFlash, mChat;
    CameraPreview cameraPreview;
    TextView mSpeed, mTime;
    private ListView listView;
    private Conversation.ConversationType conversationType;
    private String targetId;
    private boolean flag = true;  //标记是否被点击
    private BFCaptor mBFCaptor = null;
    private String channelid;
    private EditText editText;
    private FrameLayout rootView;
    private CustomProgressDialog mProgressDialog;  //正在加载中...
    private LiveChatListAdapter liveChatListAdapter;
    private UserInfo info;
    private String TAG ="TestChatRoomFragment";
    private FragmentActivity activity;
    private TextView tvNumber;
    private int total ; //聊天室人数
    private int currentUserId;  //当前聊天室的用户id
    private List list = new ArrayList();   //用户Id集合
    private List<UserInfoBean> infoBeanList = new ArrayList<>();
    private Handler mHandler= new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what==MSG_CAPTOR_TIME_CHANGED)
            {
                mTime.setText(makeTime(mBFCaptor.getTimeStamp()));
                if (!"00:00:00".equals(mTime.getText().toString())) {
                    if(null != mProgressDialog)
                        mProgressDialog.dismiss();
                }
            }

        }
    };
    private CommonAdapter<UserInfoBean> commonAdapter;
    private TextView tvMoney;
    /**
     * 监听返回键
     */
    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                    showAlertDialog();
                }
            }
            return false;
        }
    };
    private Runnable mRunnable  = new Runnable() {
        @Override
        public void run() {
            //放你要做的事情
            refresh();
            tvNumber.setText(total+"");  //人数
            //每隔10秒请求网络
            mHandler.postDelayed(mRunnable,10000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FragmentActivity) getActivity();
        MyRongIM.getInstance().getEventBus().register(this);
        Uri uri = getActivity().getIntent().getData();
        String typeStr = uri.getLastPathSegment().toUpperCase();
        conversationType = Conversation.ConversationType.valueOf(typeStr);
        targetId = uri.getQueryParameter("targetId");
        mBFCaptor = BFCaptor.getInstance(getActivity());
        mBFCaptor.setDirection(BFCaptor.DirectionType.LANDSCAPE);
        mBFCaptor.registBFCaptorEventListener(this);
        mBFCaptor.registBFCaptorErrorListener(this);
        Utiles.log(TAG + "infoText   targetId=" + targetId + "-----onCreate");
        channelid  = targetId;

        mHandler.post(mRunnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_chat_room, container, false);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(backlistener);
        Utiles.log(TAG + "infoText   targetId=" + targetId + "-----onCreateView");
        initView(view);
        joinChatRoom();    //连接聊天室
        // chatRoomNumber();  //统计聊天室的人数
        return view;
    }

    /**
     * 获取聊天室的人数
     */
    private void chatRoomNumber() {
        OkHttpClientManager.getAsyn(UrlsManager.URL_CHAT_ROOM_NUMBER + channelid, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log(TAG + "---fail--"+e.getMessage());
            }
            @Override
            public void onResponse(String response) {
                Utiles.log(TAG + "---response--"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    JSONObject jsonObject1 = new JSONObject(result);
                    int total = jsonObject1.getInt("total"); //人数
                    String id =null;
                    JSONArray users = jsonObject1.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject json2 = users.getJSONObject(i);
                        id = json2.getString("id"); //人员id
                    }
                    Utiles.log(TAG+"---total   --"+total+"-------"+id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加入聊天室
     */
    private void joinChatRoom() {
        RongIMClient.getInstance().joinChatRoom(targetId, 0, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                UserInfoBean userInfo = SharedUtil.getUserInfo(activity);
                info = new UserInfo(String.valueOf(userInfo.getUserid()), userInfo.getUsername(), Uri.parse(userInfo.getHeadurl()));
                MyRongIM.getInstance().setUserInfoProvider(new MyRongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String userId) {
                        return info;
                    }
                });
                RongIM.getInstance().refreshUserInfoCache(info);
                TextMessage textMessage = TextMessage.obtain("进来了");
                Message msg = Message.obtain(targetId, conversationType, textMessage);
                textMessage.setExtra("{\"type\":1}");
                msg.setMessageDirection(Message.MessageDirection.SEND);
                textMessage.setUserInfo(info);
                MyRongIM.getInstance().sendMessage(msg);
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     *  初始化试图
     */
    private void initView(View view) {
        mProgressDialog = new CustomProgressDialog(getActivity());
        rootView = (FrameLayout) view.findViewById(R.id.start_live_rootView);
        cameraPreview = (CameraPreview) view.findViewById(R.id.start_live_cameraPreview);
        mBFCaptor.setPreview(cameraPreview);
        mClose = (ImageView) view.findViewById(R.id.start_live_close);
        mChange = (ImageView) view.findViewById(R.id.start_live_change);
        mChangeAudio = (ImageView) view.findViewById(R.id.start_live_change_audio);
        mChangeFlash = (ImageView) view.findViewById(R.id.start_live_flash);
        mSpeed = (TextView) view.findViewById(R.id.start_live_speed);
        mChat = (ImageView) view.findViewById(R.id.start_live_chat);
        mTime = (TextView) view.findViewById(R.id.start_live_tvTime);
        mTime.setText(makeTime(mBFCaptor.getTimeStamp()));
        tvMoney = (TextView) view.findViewById(R.id.start_live_tvMoney);
        editText = (EditText) view.findViewById(R.id.et_sendmessage);
        listView = (ListView) view.findViewById(R.id.start_live_listView);
        view.findViewById(R.id.start_live_llPersonNumber).setOnClickListener(this);
        tvNumber= (TextView) view.findViewById(R.id.start_live_tvNumber);
        //监听
        mChat.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mChange.setOnClickListener(this);
        mChangeAudio.setOnClickListener(this);
        mChangeFlash.setOnClickListener(this);
        mProgressDialog.show();
        //mProgressDialog.setCancelable(false);   //设置外部点击无效
        //开始直播
        mBFCaptor.startPreview();
        mBFCaptor.start(channelid);

        liveChatListAdapter = new LiveChatListAdapter();
        listView.setAdapter(liveChatListAdapter);

        listView.setEnabled(false);
        listView.setSelection(liveChatListAdapter.getCount()-1);
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

    @Override
    public void onBackPressed() {
        // showAlertDialog();
    }

    @Override
    public boolean onReceived(Message message, int left) {
        if (message.getConversationType() == conversationType && message.getTargetId().equals(targetId)) {
            Log.d("=======00>", "onReceived" + left + " message:" + message.getContent());
            liveChatListAdapter.addMessage(message);
            liveChatListAdapter.notifyDataSetChanged();
            listView.setSelection(liveChatListAdapter.getCount()-1);
        }
        return false;
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        String type = Integer.toString(msg.what);
        Message message = Message.obtain(targetId, conversationType, new GiftMessage(type));
        message.getContent().setUserInfo(MyRongIM.getInstance().getCurrentUserInfo());
        MyRongIM.getInstance().sendMessage(message);
        return false;
    }

    @Override
    public void onEvent(int i) {
        switch (i) {
            case BFCaptor.EVENT_STATE_TIME_CHANGED:
                mHandler.sendEmptyMessage(MSG_CAPTOR_TIME_CHANGED);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_live_close:   //关闭
                showAlertDialog();
                break;
            case R.id.start_live_change:   //切换摄像头
                changeCamera();
                break;
            case R.id.start_live_change_audio:   //打开和关闭声音
                changeAudio();
                break;
            case R.id.start_live_flash:          //开启或关闭闪光灯
                changeFlash();
                break;
            case R.id.start_live_chat:
                break;
            case R.id.start_live_llPersonNumber:   //弹出观看人列表
                 showLiveDialog();
                break;
        }
    }

    /**
     * 显示人数Dialog
     * 当用户点击
     */
    private void showLiveDialog() {
        Utiles.log(TAG+"弹出了对话框。。。"+currentUserId);
        if(list.size()>0)
        {
            for (int i = 0; i < list.size(); i++) {

                Utiles.log(TAG+"弹出了对话框。。。"+list.size()+"   >>"+list.get(i));
                requestUserInfo((Integer) list.get(i));
            }
        }
        getCurrentUserInfo(); //获取用户信息
        View view = LayoutInflater.from(activity).inflate(
                R.layout.live_alert_person_number, null);
        ImageView close = (ImageView) view.findViewById(R.id.alert_close);
        TextView tvNumber= (TextView) view.findViewById(R.id.live_alert_person);
        ListView listView = (ListView) view.findViewById(R.id.live_alert_listView);
        tvNumber.setText("人数("+total+")");
        final LivePersonAlertDialog alertDialog = new LivePersonAlertDialog(activity, view).builder();
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        list.clear();
        listView.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();
    }

    /**
     *  加载用户信息
     */
    private void getCurrentUserInfo() {
        commonAdapter = new CommonAdapter<UserInfoBean>(activity,infoBeanList,R.layout.listitem_stat_live_person) {
            @Override
            public void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
                holder.setText(R.id.tvName,userInfoBean.getUsername());
                ImageView header = holder.getView(R.id.ivHead);
                if(!TextUtils.isEmpty(userInfoBean.getHeadurl()))
                Picasso.with(activity).load(userInfoBean.getHeadurl()).centerCrop().fit().error(R.drawable.default_head_bg)
                        .error(R.drawable.default_head_bg).into(header);
                UserInfoBean item = getItem(position);
                Utiles.log("获取到的用户信息"+item.toString());
            }
        };
    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mBFCaptor.startPreview();
        // mBFCaptor.setDataRate(90);
        if (!"00:00:00".equals(mTime.getText().toString())) {
            mTime.setText("00:00:00");
        }
    }

    @Override
    public void onPause() {  //暂停
        super.onPause();
        if (mBFCaptor != null) {
            mBFCaptor.stop();
            mBFCaptor.onTextureDestroyed();
        }
    }

    /**
     * 接收
     */
    public void onEventMainThread(BusEvent.MessageReceived event) {
        Log.d("", "BusEvent.MessageReceived left = 接收" + event.left);
        Message msg = event.message;
        if (targetId.equals(msg.getTargetId()) && conversationType == Conversation.ConversationType.CHATROOM) {
            liveChatListAdapter.addMessage(msg);
            liveChatListAdapter.notifyDataSetChanged();
            listView.setSelection(liveChatListAdapter.getCount()-1);

        }
    }

    /**
     * 发送
     */
    public void onEventMainThread(BusEvent.MessageSent event) {
        Log.d("", "BusEvent.MessageReceived left =发送 " );
        Message msg = event.message;
        if (targetId.equals(msg.getTargetId()) && conversationType == Conversation.ConversationType.CHATROOM) {
            int errorCode = event.code;
            if (errorCode == 0) {
                liveChatListAdapter.addMessage(msg);
                liveChatListAdapter.notifyDataSetChanged();
                listView.setSelection(liveChatListAdapter.getCount()-1);

            } else {
                Toast toast = Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBFCaptor != null) {
            mBFCaptor.stop();
            mBFCaptor.onTextureDestroyed();
        }
        if(mProgressDialog!=null)
            mProgressDialog.dismiss();
        mHandler.removeCallbacks(mRunnable);  //移除线程
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

    private void changeAudio() {
        //获取音频服务
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        //设置声音模式
        audioManager.setMode(AudioManager.STREAM_SYSTEM);  //系统声音
        if (flag) {
            flag = false;
            OpenSpeaker();
            mChangeAudio.setImageResource(R.drawable.live_record_stop);
        } else {
            flag = true;
            //打开扬声器
            CloseSpeaker();
            //ToastUtil.showShort("开启声音");
            mChangeAudio.setImageResource(R.drawable.live_record);
        }
    }

    //打开扬声器
    public void OpenSpeaker() {
        try {

            //判断扬声器是否在打开
            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.STREAM_SYSTEM );
            //获取当前通话音量
            currVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM );
            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM ,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM ),
                        AudioManager.STREAM_SYSTEM );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭扬声器
    public void CloseSpeaker() {
        try {
            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                if (audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM , currVolume,
                            AudioManager.STREAM_SYSTEM );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****
     * 退出直播弹框
     */
    private void showAlertDialog() {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_yesorno, null);
         AlertDialog dialog = new AlertDialog(activity, view,0.45f).builder();
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
                             //   test.deleteChannel(channelid);
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

    /**
     * 获取在线人数
     */
    private void refresh()
    {
        OkHttpClientManager.getAsyn(UrlsManager.URL_CHAT_ROOM_NUMBER + channelid, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.log(TAG + "---fail--"+e.getMessage());
            }
            @Override
            public void onResponse(String response) {
                Utiles.log(TAG + "---response--"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    JSONObject jsonObject1 = new JSONObject(result);
                    total = jsonObject1.getInt("total"); //人数
                    JSONArray users = jsonObject1.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject json2 = users.getJSONObject(i);
                        currentUserId = Integer.parseInt(json2.getString("id")); //在线人员id
                        list.add(currentUserId);
                    }

                    removeDuplicate(list);
                    Utiles.log(TAG+"---total   --"+total+"-------"+list.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public   void  removeDuplicate(List list)   {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )   {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )   {
                if  (list.get(j).equals(list.get(i)))   {
                    list.remove(j);
                }
            }
        }
        System.out.println(list);
    }

    /**
     * 请求聊天室的用户信息
     * @param currentUserId
     */
    private void requestUserInfo(final int currentUserId) {

        UserInfoServiceImpl.getInstance().getUserinfo(currentUserId, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(String response) {
                Utiles.log(TAG+"----getuserinfo--"+response+"----"+currentUserId);
                StatusBean statusBean = JSON.parseObject(response,StatusBean.class);
                if(statusBean.getCode()==0)
                {
                    UserInfoBean userInfoBean = JSON.parseObject(statusBean.getData(),UserInfoBean.class);
                    infoBeanList.add(userInfoBean);
                }
            }
        });
    }
}
