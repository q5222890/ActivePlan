package com.xz.activeplan.ui.personal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.ActiveinfosJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;

import java.io.IOException;
import java.util.Locale;

import bf.cloud.android.playutils.VideoManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.UserInfo;

/**
 * 会话列表页面
 *
 * @author johnny
 */
public class ConversationActivity extends FragmentActivity implements OnClickListener, ClassObserver {

    private static final String TAG = "ConversationActivity";
    public static ConversationActivity getInstance;
    //目标 Id
    private String mTargetId;
    //刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
    private String mTargetIds;
    //会话类型
    private ConversationType mConversationType;
    private String title;
    private UserInfoBean user;
    private TextView textViewName;
    private ImageView imageButton;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getInstance = ConversationActivity.this;
        ClassConcrete.getInstance().addObserver(this);
        VideoManager.getInstance(this).startMediaCenter();
        int conversation = R.layout.activity_conversation_layout;
        int live_chat_room = R.layout.activity_test_live_chat_room;   //直播时
        int vod_chat_room = R.layout.activity_test_vod_chat_room;   //点播视频
        int startLivew_chatRoom = R.layout.fragment_start_live_chat_room;  //发起直播

//        if (Utiles.x == 0) {            //观看直播
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//            setContentView(live_chat_room);
//            return;
//        }else if(Utiles.x == 2)         //发起直播
//        {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
//            setContentView(startLivew_chatRoom);
//            return;
//        } else if(Utiles.x == 3)        //观看点播
//        {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//
//            setContentView(vod_chat_room);
//            return;
//        }
//         else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|
//            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//            setContentView(conversation);
//        }

       /* setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*/
        setContentView(conversation);
        //设置对哈语音听众
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        textViewName = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        imageButton = (ImageView) findViewById(R.id.id_ImageHead);
        imageButton.setOnClickListener(this);
        Intent intent = getIntent();
        getIntentDate(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     * 获取用户的名称和ID
     */
    private void getIntentDate(Intent intent) {
        title = intent.getData().getQueryParameter("title");
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        Utiles.log(" title:" + title + "  mTargetIdL:" + mTargetId);
        String lastPathSegment = intent.getData().getLastPathSegment();//获取会话类型：例如  群聊，单聊
        mConversationType = ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        if (mConversationType == ConversationType.SYSTEM) {
            textViewName.setText(getResources().getString(R.string.System_Message));
            getUserInfo01(Integer.parseInt(mTargetId));
        } else if (ConversationType.GROUP == mConversationType) {
            textViewName.setText(title);
            imageButton.setVisibility(View.VISIBLE);
            getHostInfo(Integer.parseInt(mTargetId));

        } else if (mConversationType == ConversationType.PRIVATE) {
            textViewName.setText(title);
            imageButton.setVisibility(View.GONE);
            getUserInfo01(Integer.parseInt(mTargetId));

        }
        enterFragment(mConversationType, title, mTargetId);
    }

    private void getUserInfo01(int i) {
        UserInfoServiceImpl.getInstance().getUserinfo(i, new StringCallback() {
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    final UserInfoBean userInfoBean = JSON.parseObject(statusBean.getData(), UserInfoBean.class);
                    if (userInfoBean != null) {
                        SharedUtil.save(ConversationActivity.this, mTargetId + "-PRIVATE", userInfoBean.getUsername());
                        //用户信息提供者---不要放在有生命周期的组件中
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                            @Override
                            public UserInfo getUserInfo(String userId) {
                                return new UserInfo(userInfoBean.getUserid() + "", userInfoBean.getUsername(),
                                        Uri.parse(userInfoBean.getHeadurl()));
                            }
                        }, true);
                        RongIM.getInstance().
                                refreshUserInfoCache(new UserInfo(userInfoBean.getUserid() + "", userInfoBean.getUsername(),
                                        Uri.parse(userInfoBean.getHeadurl())));
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }
        });
    }

    /**
     * 加载会话页面 MyConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         目标 Id
     */
    private void enterFragment(ConversationType mConversationType, String title, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("title", title)
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.id_ImageHead:
                Intent intent = new Intent(ConversationActivity.this, GroupMemberActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("mTargetId", mTargetId);
                intent.putExtra("mConversationType", mConversationType);
                startActivity(intent);
                break;

        }

    }

    private void getHostInfo(int activeid) {
        ActiveServiceImpl.getInstance().getActive(-1, activeid, new StringCallback() {
            @Override
            public void onResponse(String response) {
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        ActiveinfosJson activeinfosJson = new ActiveinfosJson();
                        obj = activeinfosJson
                                .parseJson(statusBean
                                        .getData());
                        if (obj != null) {
                            ActiveinfosBean mActiveinfosBean = (ActiveinfosBean) obj;
                            if (mActiveinfosBean != null) {
                                SharedUtil.save(ConversationActivity.this, mTargetId + "-GROUP", mActiveinfosBean.getName());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }
        });
    }


    @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this);
        super.onDestroy();
        Utiles.log("-----------Conversation-----------onDestroy");
    }

    @Override
    public boolean onDataUpdate(Object data) {
        return false;
    }

/*
    @Override
    public UserInfo getUserInfo(String s) {
        UserInfoServiceImpl.getInstance().getUserinfo(s, new StringCallback() {
            @Override
            public UserInfo onResponse(String response) {
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        UserInfoJson userInfoJson = new UserInfoJson();
                        obj = userInfoJson.analysisJson2Object(statusBean.getData());
                        if (obj != null) {
                            UserInfoBean userInfoBean = (UserInfoBean) obj;
                            if (userInfoBean != null) {
                                SharedUtil.save(ConversationActivity.this, mTargetId + "-PRIVATE", userInfoBean.getUsername());
                                RongIM.getInstance().
                                        refreshUserInfoCache(new UserInfo(userInfoBean.getUserid()+"", userInfoBean.getUsername(),
                                                Uri.parse(userInfoBean.getHeadurl())));
                                return new UserInfo(userInfoBean.getUserid()+"",userInfoBean.getUsername(),Uri.parse(userInfoBean.getHeadurl()));
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }
        });
        return null;
    }*/
}
