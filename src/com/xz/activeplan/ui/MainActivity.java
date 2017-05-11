package com.xz.activeplan.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.isnc.facesdk.SuperID;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.views.ExpandableButtonMenu;
import com.xz.activeplan.views.ExpandableMenuOverlay;
import com.xz.activeplan.ui.find.fragment.LiveTvFragmen;
import com.xz.activeplan.ui.live.activity.SendLiveActivity;
import com.xz.activeplan.ui.live.rongclound.LiveKit;
import com.xz.activeplan.ui.news.activity.SearchTeacherActivity;
import com.xz.activeplan.ui.news.fragment.CelebrityFragment;
import com.xz.activeplan.ui.personal.activity.HostInfoActivity;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.ui.personal.fragment.PersonalFragment;
import com.xz.activeplan.ui.recommend.activity.ChooseCityActivity;
import com.xz.activeplan.ui.recommend.activity.ModifyHostInfoActivity;
import com.xz.activeplan.ui.recommend.activity.PostActiveActivity;
import com.xz.activeplan.ui.recommend.activity.SearchActivity;
import com.xz.activeplan.ui.recommend.fragment.RecommendFragment;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.JsonUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.UserInfo;

/**
 * 主界面activity
 */
public class MainActivity extends BaseFragmentActivity implements
        OnClickListener, BDLocationListener, ClassObserver {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ExpandableMenuOverlay menuOverlay;
    private boolean isPressedBackKey;
    private BaseFragment mFindFragment, mTeacherFragment, mPersonalFragment,
            mRecommendFragment, liveTvFragmen;

    // 当前显示的fragment
    private BaseFragment mCurrentFragment;
    private int mCurrentFragmentIndex;
    private TextView tvMainBottomHome, tvMainBottomSelect, tvMainBottomMessage,
            tvMainBottomSession;
    private ImageView tvMainButtonInitiate;
    private LinearLayout linClickTv;

    private Drawable[] myImageSelect, myImageNor;

    private UserInfoBean mUserInfoBean;

   // private ProgressDialog mProgressDialog;
    private boolean ispanded =false;
    private boolean isFirstIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_main_new);
        ClassConcrete.getInstance().addObserver(this);
        init();
        //检测版本更新
        BDAutoUpdateSDK.uiUpdateAction(MainActivity.this, new MyUICheckUpdateCallback());
        //初始化一登
        SuperID.initFaceSDK(this);
        SuperID.setDebugMode(true);//开启debug

    }
    /**
     * 更新版本
     */
    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
        @Override
        public void onCheckComplete() {
        }
    }



    /**
     * 更新版本
     */
//    private void updateVersion() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        UpdateManager manager = new UpdateManager(MainActivity.this);
//        manager.checkUpdate();
//
//    }

    private void initView() {
        //mProgressDialog = new ProgressDialog(this);
        tvMainBottomHome = (TextView) findViewById(R.id.tvMainBottomHome);
        tvMainBottomSelect = (TextView) findViewById(R.id.tvMainBottomSelect);
        tvMainBottomMessage = (TextView) findViewById(R.id.tvMainBottomMessage);
        tvMainBottomSession = (TextView) findViewById(R.id.tvMainBottomSession);
        tvMainButtonInitiate = (ImageView) findViewById(R.id.tvMainButtonInitiate);

        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);

        //点击发起按钮
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {

                //点击发布活动或直播
                switch (action) {
                    case MID:
                        Toast.makeText(MainActivity.this, "Mid pressed and dismissing...", Toast.LENGTH_SHORT).show();
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case LEFT:  //发起活动
                        ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.MAIN_HEAD_ADD));
                        menuOverlay.getButtonMenu().toggle();
                        menuOverlay.dismiss();
                        break;
                    case RIGHT:  //发起直播
                        ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.LAUNCH_LIVE));
                        menuOverlay.getButtonMenu().toggle();
                        menuOverlay.dismiss();
                        break;
                }
            }
        });

        linClickTv = (LinearLayout) findViewById(R.id.linClickTv);
        tvMainBottomHome.setOnClickListener(this);
        tvMainBottomSelect.setOnClickListener(this);
        tvMainBottomMessage.setOnClickListener(this);
        tvMainBottomSession.setOnClickListener(this);
        tvMainButtonInitiate.setOnClickListener(this);

        //linClickTv.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {
        XZApplication.getInstance().getLocationManager().registerLocationListener(this);
        XZApplication.getInstance().getLocationManager().start();
        myImageSelect = new Drawable[4];
        myImageNor = new Drawable[4];
        myImageSelect[0] = getResources().getDrawable(
                R.drawable.ic_menu_recommend_select);
        myImageSelect[1] = getResources().getDrawable(
                R.drawable.ic_menu_live_select);
        myImageSelect[2] = getResources().getDrawable(
                R.drawable.ic_menu_teacher_select);
        myImageSelect[3] = getResources().getDrawable(
                R.drawable.ic_menu_mine_select);

        myImageNor[0] = getResources()
                .getDrawable(R.drawable.ic_menu_recommend);
        myImageNor[1] = getResources().getDrawable(R.drawable.ic_menu_live);
        myImageNor[2] = getResources().getDrawable(R.drawable.ic_menu_teacher);
        myImageNor[3] = getResources().getDrawable(R.drawable.ic_menu_mine);

        initView();
        initFragment();


        //获取广告图片，下次进入app显示广告
        UserInfoServiceImpl.getInstance().getad(1, new StringCallback() {

            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(String response) {
                StatusJson statusJosn = new StatusJson();
                Object obj = statusJosn.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        try {
                            JSONArray arr = new JSONArray(statusBean.getData());
                            if (arr != null && arr.length() > 0) {
                                final JSONObject json = arr.getJSONObject(0);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Utiles.log("广告url:"+
                                                    JsonUtils.getString("url", json));
                                            savePic(JsonUtils.getString("url", json));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


    }

    //保存广告图片
    private void savePic(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }

        //httpGet连接对象
        HttpGet httpRequest = new HttpGet(imageUrl);
        //取得HttpClient 对象  
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //请求httpClient ，取得HttpRestponse  
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //取得相关信息 取得HttpEntiy  
                HttpEntity httpEntity = httpResponse.getEntity();
                //获得一个输入流  
                InputStream is = httpEntity.getContent();
                System.out.println(is.available());
                System.out.println("Get, Yes!");
                //Log.i(TAG, "savePic: ");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                FileUtil.saveBitmapToSdcard(FileUtil.getRootPath() + FileUtil.parseSuffix(imageUrl), bitmap);
                if(bitmap!=null&& !bitmap.isRecycled()){
                    bitmap.recycle();
                }

                //保存广告图片本地路径
                SharedUtil.save(MainActivity.this, "ggimgurl", FileUtil.getRootPath() + FileUtil.parseSuffix(imageUrl));
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化fragmnt
     */
    private void initFragment() {
        // 推荐
        mRecommendFragment = new RecommendFragment();
        // 直播
        liveTvFragmen = new LiveTvFragmen();
        // 名师
        mTeacherFragment = new CelebrityFragment();
        // 我的
        mPersonalFragment = new PersonalFragment();
        replaceFragment(mRecommendFragment, 0);

        replaceRGImg(0);
    }

    @Override
    public void onBackPressed() {
        pressBackKey();
    }

    private void pressBackKey() {
        if (isPressedBackKey) {
            getApp().exit();
        } else {
            isPressedBackKey = true;
            Toast.makeText(getApp(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPressedBackKey = false;
                }
            }, 3 * 1000);
        }

    }

    /**
     * 替换fragment
     *
     * @param fragment 替换的fragment
     * @param index    替换的下标
     */

    private void replaceFragment(BaseFragment fragment, int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        synchronized (this) {
            if (fragment.isAdded()) {
                return;
            }

            ft.replace(R.id.main_container, fragment);

            ft.commit();

            mCurrentFragment = (BaseFragment) fragment;
            mCurrentFragmentIndex = index;
        }
    }

    /**
     * 替换图片选中效果以及选中字体颜色
     *
     * @param index 选中选项下标
     */
    private void replaceRGImg(int index) {
        Resources res = getResources();
        tvMainBottomHome.setCompoundDrawablesWithIntrinsicBounds(null,
                myImageNor[0], null, null);
        tvMainBottomHome.setTextColor(res
                .getColor(R.color.home_bottom_color_normal));
        tvMainBottomSelect.setCompoundDrawablesWithIntrinsicBounds(null,
                myImageNor[1], null, null);
        tvMainBottomSelect.setTextColor(res
                .getColor(R.color.home_bottom_color_normal));
        tvMainBottomMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                myImageNor[2], null, null);
        tvMainBottomMessage.setTextColor(res
                .getColor(R.color.home_bottom_color_normal));
        tvMainBottomSession.setCompoundDrawablesWithIntrinsicBounds(null,
                myImageNor[3], null, null);
        tvMainBottomSession.setTextColor(res
                .getColor(R.color.home_bottom_color_normal));

        switch (index) {
            case 0:
                tvMainBottomHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        myImageSelect[0], null, null);
                tvMainBottomHome.setTextColor(res
                        .getColor(R.color.home_bottom_color_selected));

                break;
            case 1:
                tvMainBottomSelect.setCompoundDrawablesWithIntrinsicBounds(null,
                        myImageSelect[1], null, null);
                tvMainBottomSelect.setTextColor(res
                        .getColor(R.color.home_bottom_color_selected));

                break;
            case 2:
                tvMainBottomMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                        myImageSelect[2], null, null);
                tvMainBottomMessage.setTextColor(res
                        .getColor(R.color.home_bottom_color_selected));
                break;
            case 3:

                tvMainBottomSession.setCompoundDrawablesWithIntrinsicBounds(null,
                        myImageSelect[3], null, null);
                tvMainBottomSession.setTextColor(res
                        .getColor(R.color.home_bottom_color_selected));

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMainBottomHome:  //推荐
                replaceFragment(mRecommendFragment, 0);
                replaceRGImg(0);
                break;
            case R.id.tvMainBottomSelect:  //直播
                replaceFragment(liveTvFragmen, 1);
                replaceRGImg(1);
                break;
            case R.id.linClickTv:   //名师
            case R.id.tvMainBottomMessage:
                replaceFragment(mTeacherFragment, 2);
                replaceRGImg(2);
                break;
            case R.id.tvMainBottomSession:  //我的
                replaceFragment(mPersonalFragment, 3);
                replaceRGImg(3);
                break;
            case R.id.tvMainButtonInitiate:
                menuOverlay.show();
            default:
                break;
        }
    }

    private void getHostId() {
        if (SharedUtil.isLogin(this)) {
            mUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        //mProgressDialog.show();
        UserInfoServiceImpl.getInstance().getUserHostId(mUserInfoBean.getUserid(), new StringCallback() {
            @Override
            public void onResponse(String response) {
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        OrganizersJson json = new OrganizersJson();
                        OrganizersBean bean = json.parseJson(statusBean.getData());
                        if (bean != null &&
                                !TextUtils.isEmpty(bean.getHostname()) &&
                                !TextUtils.isEmpty(bean.getHostcontact()) &&
                                !TextUtils.isEmpty(bean.getHostphone()) &&
                                !TextUtils.isEmpty(bean.getHostmail()) &&
                                !TextUtils.isEmpty(bean.getHostintro()) &&
                                !TextUtils.isEmpty(bean.getHosthearurl())) {
                            Intent intent = new Intent(MainActivity.this,
                                    PostActiveActivity.class);
                            intent.putExtra("hostid", bean.getHostid());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this,
                                    HostInfoActivity.class);
                            intent.putExtra("data", bean);
                            startActivity(intent);
                        }
                    } else {
                        ToastUtil.showShort("加载失败,请稍后重试");
                        Intent intent = new Intent(MainActivity.this,
                                ModifyHostInfoActivity.class);
                        startActivity(intent);
                    }
                } else {
                    ToastUtil.showShort("加载失败,请稍后重试");
                }
                //mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Request request, IOException e) {
              //  mProgressDialog.dismiss();
                ToastUtil.showShort("加载失败,请稍后重试");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XZApplication.getInstance().getLocationManager()
                .unRegisterLocationListener(this);
        ClassConcrete.getInstance().removeObserver(this);
    }

    //定位当前城市
    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            ToastUtil.showShort("定位失败!");
            return;
        }
        String city = location.getCity();

        if (!TextUtils.isEmpty(city)) {
            //保存定位信息
            SharedUtil.saveLocationCity(MainActivity.this, city);
            SharedUtil.saveFloat(MainActivity.this, "getLatitude", location.getLatitude());
            SharedUtil.saveFloat(MainActivity.this, "getLongitude", location.getLongitude());
        } else {
            SharedUtil.saveLocationCity(MainActivity.this, "深圳市");
        }
        SharedUtil.saveCity(this, SharedUtil.getLocationCity(this));
        ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.CITY_NOTIFI_TYPE));
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.CITY_NOTIFI_TYPE) {
            //tvMainHeaderCity.setText(SharedUtil.getCity(MainActivity.this)) ;
        } else if (event.getType() == EventType.LOGIN_NOTIFI_TYPE) {
            setUserInfo();
        } else if (event.getType() == EventType.MAIN_HEAD_ADD) {   //发布活动
                getHostId();
        } else if (event.getType() == EventType.MAIN_HEAD_CITY) {
            Intent intent = new Intent(MainActivity.this,
                    ChooseCityActivity.class);
            startActivity(intent);
        } else if (event.getType() == EventType.MAIN_HEAD_SEARCH) {    //首页搜索活动
            Intent intent = new Intent(MainActivity.this,
                    SearchActivity.class);
            startActivity(intent);
        } else if (event.getType() == EventType.MAIN_HEAD_TEACHER_SEARCH) {
            Intent intent = new Intent(MainActivity.this, SearchTeacherActivity.class);
            startActivity(intent);
        }else if(event.getType()==EventType.LAUNCH_LIVE)  //发起直播
        {
            if (SharedUtil.isLogin(this)) {
                //mUserInfoBean = SharedUtil.getUserInfo(this);
                Intent intent =new Intent(MainActivity.this, SendLiveActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
        return false;
    }

    private void setUserInfo() {

        if (SharedUtil.isLogin(this)) {
            mUserInfoBean = SharedUtil.getUserInfo(this);
            if (mUserInfoBean != null) {
                contectChart(mUserInfoBean.getToken());
            }
        }
    }

    //融云服务连接
    private void contectChart(String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        LiveKit.connect(token, new ConnectCallback() {

            @Override
            public void onSuccess(String arg0) {
                Utiles.log("连接融云成功>MainActivity-----"+arg0);
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(mUserInfoBean.getUserid() + "", mUserInfoBean.getUsername(), Uri.parse(mUserInfoBean.getHeadurl())));
                    LiveKit.setCurrentUser(new UserInfo(mUserInfoBean.getUserid() + "", mUserInfoBean.getUsername(), Uri.parse(mUserInfoBean.getHeadurl())));
                }
                RongIM.getInstance().setMessageAttachedUserInfo(true);

            }

            @Override
            public void onError(ErrorCode arg0) {
                Utiles.log("连接融云失败>MainActivity-----"+arg0);
            }

            @Override
            public void onTokenIncorrect() {
                Utiles.log("token无效>MainActivity-----");
            }
        });

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                return new UserInfo(mUserInfoBean.getUserid() + "", mUserInfoBean.getUsername(), Uri.parse(mUserInfoBean.getHeadurl()));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }

        }, true);
    }


}
