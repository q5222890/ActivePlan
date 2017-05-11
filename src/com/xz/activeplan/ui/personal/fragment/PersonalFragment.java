package com.xz.activeplan.ui.personal.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.AuthenticationInfoBean;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.ui.personal.activity.ActiveManageActivity;
import com.xz.activeplan.ui.personal.activity.AuthenticationActivity;
import com.xz.activeplan.ui.personal.activity.CelebriteManagerActivity;
import com.xz.activeplan.ui.personal.activity.FaceDetectionActivity;
import com.xz.activeplan.ui.personal.activity.HostActivity;
import com.xz.activeplan.ui.personal.activity.LiveManagerActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.ui.personal.activity.MessageActivity;
import com.xz.activeplan.ui.personal.activity.MoneyAccountActivity;
import com.xz.activeplan.ui.personal.activity.MyFansActivity;
import com.xz.activeplan.ui.personal.activity.MyFollowingActivity;
import com.xz.activeplan.ui.personal.activity.PersonalInfoActivity;
import com.xz.activeplan.ui.personal.activity.SettingActivity;
import com.xz.activeplan.ui.personal.text.BingPhoneViewDialog;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CircleImageView;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.MyScrollView;
import com.xz.activeplan.views.ShareDialog;

import java.io.IOException;
import java.lang.reflect.Field;

import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 我的fragment界面
 *
 * @author johnny
 */
public class PersonalFragment extends BaseFragment implements OnClickListener,
        ClassObserver, MyScrollView.ScrollViewListener {
    private static final String TAG = PersonalFragment.class.getSimpleName();
    private View view;// 处理整个页面的view
    private Context context;
    private ImageView imagePoint;
    private CircleImageView mIvMineLogo;
    private UserInfoBean mUserInfoBean;
    private TextView tvMineNickName;
    private CustomProgressDialog mProgressDialog;
    private RelativeLayout toolbar;
    private Drawable background;
    private MyScrollView scrollView;
    private int CurrentY;
    private ShareDialog shareDialog;
    private ImageView id_ImageAuthentication;
    private AuthenticationInfoBean infobean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ClassConcrete.getInstance().addObserver(this);
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initViews();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initViews() {
        context = getActivity();
        mProgressDialog = new CustomProgressDialog(context);
        //滑动
        scrollView = (MyScrollView)view.findViewById(R.id.scrollView_personal);
        scrollView.setScrollViewListener(this);
        imagePoint = (ImageView) view.findViewById(R.id.id_imgpoint);
        //头像
        mIvMineLogo = (CircleImageView) view.findViewById(R.id.img_ImageHeadPhoto);
        tvMineNickName = (TextView) view.findViewById(R.id.tvMineNickName);//名字
        toolbar = (RelativeLayout) view.findViewById(R.id.relativeLayout_toollbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.header_blue));
        selectBackColor(toolbar, 0);
        View viewHeadBlackGround = view.findViewById(R.id.id_LinePersonMessage);
        Drawable background = viewHeadBlackGround.getBackground();
        background.setAlpha(100);
        viewHeadBlackGround.setBackground(background);

        view.findViewById(R.id.id_LineSetting).setOnClickListener(this);
        view.findViewById(R.id.id_ImageMessageCircle).setOnClickListener(this);
        view.findViewById(R.id.id_ImageBalance).setOnClickListener(this);
        view.findViewById(R.id.id_LineMyFriends).setOnClickListener(this);//我的好友
        mIvMineLogo.setOnClickListener(this);
        ShareSDK.initSDK(getActivity());
        view.findViewById(R.id.tv_activemanage).setOnClickListener(this);//活动管理
        view.findViewById(R.id.tv_liveManager).setOnClickListener(this);   //直播管理
        view.findViewById(R.id.id_TextViewActivityManager).setOnClickListener(this);//名人堂
        view.findViewById(R.id.id_LineMyGuanZhuPerson).setOnClickListener(this);//我的关注
        view.findViewById(R.id.id_LineMyFace).setOnClickListener(this);  //脸部识别
        id_ImageAuthentication = (ImageView) view.findViewById(R.id.id_ImageAuthentication);
        id_ImageAuthentication.setOnClickListener(this);//认证
        view.findViewById(R.id.id_ImageShare).setOnClickListener(this);
        view.findViewById(R.id.id_TextViewMyField).setOnClickListener(this);

        shareDialog = new ShareDialog(context);

        shareDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvShareSina:
                        ShareSDKUtils.shareSina( "我在约吧互动上面发现了一个好活动，快来看看吧 http://www.yueba.cc","http://139.196.152.82/tryst_images/20160830070251469057.png");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvShareQzone:
                        ShareSDKUtils.shareQZone("约吧互动","http://www.yueba.cc/index.html","我在约吧互动发现一个" +
                                "好活动快来看看吧，找好活动，看美女直播，" +
                                "听名师演讲 上约吧互动","http://139.196.152.82/tryst_images/20160830070251469057.png");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvSharewx:
                        ShareSDKUtils.shareWechat("约吧互动","我在约吧互动发现一个好活动快来看看吧，找好活动，看美女直播，听名师演讲 上约吧互动",
                                "http://139.196.152.82/tryst_images/20160830070251469057.png","http://www.yueba.cc/index.html");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvSharewxpy:
                        ShareSDKUtils.shareWechatMoments("约吧互动","http://139.196.152.82/tryst_images/20160830070251469057.png","http://www.yueba.cc/index.html");
                        shareDialog.dismiss();
                        break;
                    case R.id.tvShareMsg:
                        ShareSDKUtils.shareShortMessage("我在约吧互动上面发现了一个好活动，快来看看吧 http://www.yueba.cc");
                        shareDialog.dismiss();
                        break;
                }
            }
        });

        if (SharedUtil.isLogin(getActivity())) {
            mUserInfoBean = SharedUtil.getUserInfo(getActivity());
            Utiles.log("当前用户信息："+mUserInfoBean.toString());
        }

        if(SharedUtil.isLogin(getContext())) {
            getStatus();  //获取认证状态
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.id_ImageShare://分享
                shareDialog.show();
                break;
            case R.id.id_LineMyFriends://我的好友,我的粉丝
                if(SharedUtil.isLogin(getActivity())){
                    Utiles.skipNoData(MyFansActivity.class);
                }else{
                    Utiles.skipNoData(LoginActivity.class);
                }
                break;
            case R.id.id_LineSetting://系统设置
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.img_ImageHeadPhoto://头像
                if (SharedUtil.isLogin(getActivity())) {
                    intent = new Intent(getActivity(), PersonalInfoActivity.class);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.id_ImageAuthentication:   //认证页面
                if (SharedUtil.isLogin(getActivity())) {
                    intent = new Intent(getActivity(), AuthenticationActivity.class);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(intent);
                break;

            case R.id.id_ImageMessageCircle:  //消息
                if (SharedUtil.isLogin(getActivity())) {
                    Utiles.skipNoData(MessageActivity.class);
                } else {
                    Utiles.skipNoData(LoginActivity.class);
                    return;
                }
                break;
            case R.id.id_ImageBalance://余额
                Utiles.skipNoData(MoneyAccountActivity.class);
                break;

            case R.id.tv_activemanage://活动管理
                if(SharedUtil.isLogin(getActivity())){
                    Utiles.skipNoData(ActiveManageActivity.class);
                }else{
                    Utiles.skipNoData(LoginActivity.class);
                }

                break;
            case R.id.id_TextViewActivityManager://名人堂管理
                UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok!=null) {
                    Utiles.skipNoData(CelebriteManagerActivity.class);
                }
                break;

            case R.id.id_LineMyGuanZhuPerson:  //我的关注
                if(SharedUtil.isLogin(getActivity())){
                    Utiles.skipNoData(MyFollowingActivity.class);
                }else{
                    Utiles.skipNoData(LoginActivity.class);
                }

                break;
            case R.id.tv_liveManager:   //直播管理
                if (SharedUtil.isLogin(getActivity())) {
                    intent = new Intent(getActivity(), LiveManagerActivity.class);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.id_TextViewMyField:   //我的场地
              ToastUtil.showShort("In development...");
                break;
            case R.id.id_LineMyFace:   //脸部识别

                if (SharedUtil.isLogin(getActivity())) {
                    UserInfoBean bean = SharedUtil.getUserInfo(getActivity());
                    Utiles.log("对象： "+bean.toString());
                    if(TextUtils.isEmpty(bean.getPhonenum()))
                    {
                        showBingPhone();
                        break;
                    }else
                    {
                        intent = new Intent(getActivity(), FaceDetectionActivity.class);
                    }

                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    /**
     *  绑定手机号码 dialog
     */

    private void showBingPhone() {
        View view = View.inflate(getActivity(),R.layout.dialog_yesorno,null);
        AlertDialog alertDialog = new AlertDialog(getActivity(),view,0.6f).builder();
        view.findViewById(R.id.id_TextViewName).setVisibility(View.VISIBLE);
        alertDialog.setTitle("提示");
        alertDialog.setMsg("您尚未绑定手机,请先绑定");
        view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
        view.findViewById(R.id.img_line).setVisibility(View.VISIBLE);
        alertDialog.setPositiveButton("去绑定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                 showBingPhoneNumber();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alertDialog.show();
    }

    /**
     * 绑定手机号——获取验证码
     */
    private void showBingPhoneNumber() {

        //View view = View.inflate(getActivity(),R.layout.dialog_bing_phone,null);
        BingPhoneViewDialog dialog  = new BingPhoneViewDialog(getActivity(),0.85f).builder();
        dialog.show();
    }

    private void getMessage() {
        if (SharedUtil.isLogin(getActivity())) {
            int unreadCount = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.SYSTEM, "82");
            int unreadCount1 = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.GROUP);
            int unreadCount2 = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.PRIVATE);
            if (unreadCount > 0) {
                imagePoint.setVisibility(View.VISIBLE);
            } else if (unreadCount1 > 0) {
                imagePoint.setVisibility(View.VISIBLE);
            } else if (unreadCount2 > 0) {
                imagePoint.setVisibility(View.VISIBLE);
            } else {
                imagePoint.setVisibility(View.GONE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void selectBackColor(View view, int alpha) {
        background = view.getBackground();
        background.setAlpha(alpha);
        view.setBackground(background);
    }


    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
        getMessage();
        if (CurrentY < 100) {
            selectBackColor(toolbar, 0);
        } else if (CurrentY > 100 && CurrentY < 200) {
            selectBackColor(toolbar, 100);
        } else if (CurrentY >= 200 && CurrentY < 300) {
            selectBackColor(toolbar, 150);
        } else if (CurrentY > 300 && CurrentY < 400) {
            selectBackColor(toolbar, 255);
        } else if (CurrentY > 400) {
            selectBackColor(toolbar, 255);
        }
        if(SharedUtil.isLogin(getContext())) {
            getStatus();  //获取认证状态
        }
    }
    @Override
    public void onBackPressed() {

    }
    private void setUserInfo() {
        if (SharedUtil.isLogin(getActivity())) {
            mUserInfoBean = SharedUtil.getUserInfo(getActivity());
            if (mUserInfoBean != null) {

                if (!TextUtils.isEmpty(mUserInfoBean.getUsername())) {
                    if (Utils.checkMobileNumber(mUserInfoBean.getUsername())) {
                        String mobileNumber = Utils.formatMobileNumber(mUserInfoBean.getUsername());
                        tvMineNickName.setText(mobileNumber);
                    } else {
                        tvMineNickName.setText(mUserInfoBean.getUsername());
                    }
                }

                if (!TextUtils.isEmpty(mUserInfoBean.getSignature())
                        && !"null".equals(mUserInfoBean.getUsername())) {
                } else {

                }
                if (!TextUtils.isEmpty(mUserInfoBean.getHeadurl())
                        && !"null".equals(mUserInfoBean.getHeadurl())) {
                    Utiles.log(TAG+"头像---"+mUserInfoBean.getHeadurl());
                    Picasso.with(getActivity())
                            .load(mUserInfoBean.getHeadurl())
                            .placeholder(R.drawable.default_head_bg)
                            .error(R.drawable.default_head_bg)
                            .into(mIvMineLogo);
                } else {
                    Picasso.with(getActivity())
                            .load(R.drawable.default_head_bg)
                            .placeholder(R.drawable.default_head_bg)
                            .into(mIvMineLogo);
                }
                contectChart(mUserInfoBean.getToken());
            }
        } else {
            tvMineNickName.setText("我的名字");
            Picasso.with(getActivity()).load(R.drawable.default_head_bg)
                    .placeholder(R.drawable.default_head_bg).into(mIvMineLogo);
        }
    }

    private void contectChart(String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            return;
        }

        RongIM.connect(token, new ConnectCallback() {

            @Override
            public void onSuccess(String arg0) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance()
                            .setCurrentUserInfo(
                                    new UserInfo(
                                            mUserInfoBean.getUserid() + "",
                                            mUserInfoBean.getUsername(), Uri
                                            .parse(mUserInfoBean
                                                    .getHeadurl())));
                }
                RongIM.getInstance().setMessageAttachedUserInfo(true);
            }

            @Override
            public void onError(ErrorCode arg0) {
            }

            @Override
            public void onTokenIncorrect() {

            }
        });

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                return new UserInfo(mUserInfoBean.getUserid() + "",
                        mUserInfoBean.getUsername(), Uri.parse(mUserInfoBean
                        .getHeadurl()));// 根据 userId
                // 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }

        }, true);
    }

    /**
     * 获取认证的状态
     */
    private void getStatus(){

        if(!NetworkInfoUtil.checkNetwork(getActivity())){
            ToastUtil.showShort("无法获取网络连接");
        }
        UserInfoServiceImpl.getInstance().getAuthenticStatus(
                mUserInfoBean.getUserid(), new StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        ToastUtil.showShort("认证状态获取");
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            StatusBean statusbean = JSON.parseObject(response, StatusBean.class);
                            if (statusbean.getCode() == 0) {
                                infobean =JSON.parseObject(statusbean.getData(),AuthenticationInfoBean.class);
                                if(infobean!=null){
                                    if(infobean.getIsadopt()==1){
                                        id_ImageAuthentication.setImageResource(R.drawable.unauthentication);
                                    }else if(infobean.getIsadopt()==2){
                                        id_ImageAuthentication.setImageResource(R.drawable.authentication);
                                    }else if(infobean.getIsadopt()==3){
                                        id_ImageAuthentication.setImageResource(R.drawable.unauthentication);
                                    }
                                }
                            }else{
                                id_ImageAuthentication.setImageResource(R.drawable.unauthentication);
                            }
                        }
                    }
                });
    }
    private void getHostId() {
        if (SharedUtil.isLogin(context)) {
            mUserInfoBean = SharedUtil.getUserInfo(context);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        mProgressDialog.show();
        UserInfoServiceImpl.getInstance().getUserHostId(
                mUserInfoBean.getUserid(), new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                OrganizersJson json = new OrganizersJson();
                                OrganizersBean bean = json.parseJson(statusBean
                                        .getData());
                                if (bean != null) {
                                    Intent intent = new Intent(context,
                                            HostActivity.class);
                                    intent.putExtra("data", bean);
                                    startActivity(intent);
                                }
                            } else {
                                ToastUtil.showShort("加载失败,请稍后重试!");
                            }
                        } else {
                            ToastUtil.showShort("加载失败,请稍后重试!");
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        mProgressDialog.dismiss();
                        ToastUtil.showShort("加载失败,请稍后重试!");
                    }
                });

    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.LOGIN_NOTIFI_TYPE) {
            setUserInfo();
            getMessage();
        } else if (event.getType() == EventType.LOGOUT_NOTIFI_TYPE) {
            tvMineNickName.setText("我的名字");
            Picasso.with(getActivity()).load(R.drawable.default_head_bg)
                    .placeholder(R.drawable.default_head_bg).into(mIvMineLogo);
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ClassConcrete.getInstance().removeObserver(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        CurrentY = y;
        if (CurrentY < 10) {
            selectBackColor(toolbar, 0);
        } else if (CurrentY > 10 && CurrentY < 50) {
            selectBackColor(toolbar, 100);
        } else if (CurrentY >= 50 && CurrentY < 110) {
            selectBackColor(toolbar, 150);
        } else if (CurrentY > 100 && CurrentY < 150) {
            selectBackColor(toolbar, 200);
        } else if (CurrentY > 150) {
            selectBackColor(toolbar, 255);
        }
    }
}
