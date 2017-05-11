package com.xz.activeplan.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.isnc.facesdk.aty.Aty_BaseGroupCompare;
import com.isnc.facesdk.common.DebugMode;
import com.isnc.facesdk.common.SuperIDUtils;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.ui.recommend.activity.ScanSuccessActivity;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class FaceGroupCompareActivity extends Aty_BaseGroupCompare {

    TextView tv;
    FrameLayout mContainer;
    String groupId, appId;
    ImageView iv_back;
    TextView tv_title;
    LinearLayout tv_switch;
    private ActiveinfosBean bean;

    @Override
    protected int getContentLayoutId() {
        Utiles.log("getContentLayoutId");
        return R.layout.activity_faces;
    }

    @Override
    protected void initView() {
        Utiles.log("initView");
        View viewTop = findViewById(R.id.view_top);
        viewTop.setVisibility(View.GONE);
        tv_title = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        iv_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        tv_title.setText("扫脸");
        tv_switch = (LinearLayout) findViewById(R.id.header_llCamera);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceGroupCompareActivity.this.finish();
            }
        });
        mContainer = (FrameLayout) findViewById(R.id.container);
        tv = (TextView) findViewById(R.id.textView1);
        //初始化多人脸,size为数组，{width，height}设置surfaceview宽高
        //若为null，则为全屛
//        int[] size = {480, 640};
        //initFacesFeature(size);
        //横竖屏切换
        if(!TextUtils.isEmpty(bean.getGroupid())){
            groupId =bean.getGroupid();
            Utiles.log("groupId:"+groupId);
        }
        HashMap<String, String> infos = SuperIDUtils.getappinfo(this, "SUPERID_APPKEY", "SUPERID_SECRET");
        appId = infos.get("SUPERID_APPKEY");
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            initFacesFeature(size);
            isPortrait = true;
        } else {
            isPortrait = false;
            initFacesFeature(sizeLand);
            mFacesGroupCompareView.setPortrait(false, false);
        }
//        mFacesGroupCompareView.setPortrait(false, false);//横屏锁定时需要调用
        //设置摄像头 前1 后0
        setCameraType(1);
        //开始执行人脸检测
//        facesDetect();
        facesDetect(appId, groupId);
    }

    @Override
    protected void initData() {
        Utiles.log("Face_initData");
        Intent intent = getIntent();
        if (intent != null) {
            bean = (ActiveinfosBean) intent.getSerializableExtra("data");
        }
    }

    @Override
    protected void initActions() {
        Utiles.log("initActions");
        tv_switch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Utiles.log("Face_onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utiles.log("Face_onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Utiles.log("Face_onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utiles.log("Face_onDestroy");
    }

    public void scanCode(View v) {
        Utiles.log("scanCode");
        //点击扫码
        Intent intent = new Intent(FaceGroupCompareActivity.this, MipcaActivityCapture.class);
        intent.putExtra("data", bean);
        startActivity(intent);
        FaceGroupCompareActivity.this.finish();
    }

    //抓取人脸post服务器 返回数据操作
    @Override
    protected void doFacesCallBack(JSONObject result) {
        Utiles.log("doFacesCallBack");
        tv.setVisibility(View.VISIBLE);
        tv.setText(result.toString());
        Utiles.log("扫脸结果result:" + result.toString());
        try {
            JSONArray users = result.getJSONArray("users");
            if (users.length() == 0) {
                ToastUtil.showShort("验证失败,请重试！");
                return;
            } else {
                for (int i = 0; i < users.length(); i++) {
                    JSONObject jsonObject = users.getJSONObject(i);
                    String openId = jsonObject.getString("openId");
                    String groupUid = jsonObject.getString("groupUid");
                    String groupId = jsonObject.getString("groupId");
                    Utiles.log("人脸检索回调：(组里面的用户ID) " + groupUid);
                    requestData(groupUid);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.doFacesCallBack(result);
    }

    //抓去人脸返回失败处理
    @Override
    protected void doFacesCallBackFail(int errorCode) {
        Utiles.log("doFacesCallBackFail");
        tv.setText("获取失败,errorCode:" + errorCode);
        ToastUtil.showShort("获取失败！请重试");
        super.doFacesCallBackFail(errorCode);
    }

    @Override
    protected void requestFacesData() {
        Utiles.log("requestFacesData");
        tv.setVisibility(View.VISIBLE);
        tv.setText("请求数据中");
        super.requestFacesData();
    }

    //重试
    public void btnRetry(View v) {
        Utiles.log("btnRetry");
        DebugMode.debug(">>>>>>>>" + isPortrait);
        initFacesFeature(isPortrait ? null : sizeLand);
        tv.setVisibility(View.GONE);
        facesDetect(appId, groupId);
    }

    int[] sizeLand = {640, 480};//横屏时宽高比
    int[] size = {480, 640};//竖屏是宽高
    boolean isPortrait;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utiles.log("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                isPortrait = false;
                initFacesFeature(sizeLand);
                mFacesGroupCompareView.setPortrait(false, true);
                facesDetect(appId, groupId);
//                initFacesFeature(sizeLand);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                isPortrait = true;
                initFacesFeature(size);
                mFacesGroupCompareView.setPortrait(true, true);
                facesDetect(appId, groupId);
                break;
        }
    }


    /**
     * 请求人脸数据
     *
     * @param groupUid
     */
    private void requestData(String groupUid) {
        Utiles.log("requestData");
        if(!NetworkInfoUtil.checkNetwork(this)){
            ToastUtil.showShort("网络未连接，请检查网络");
        }

        ActiveServiceImpl.getInstance().checkFace(bean.getActiveid(), groupUid, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("验证失败，请重试！");
                finish();
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
                        ToastUtil.showShort("已通过验证");
                        finish();
                    } else {
                        ToastUtil.showShort("验证成功");
                        TicketInfoBean bean = JSON.parseObject(statusBean.getData(), TicketInfoBean.class);
                        Intent intent = new Intent(FaceGroupCompareActivity.this, ScanSuccessActivity.class);
                        intent.putExtra("data", bean);
                        startActivity(intent);
                        FaceGroupCompareActivity.this.finish();
                    }
                } else {
                    ToastUtil.showShort("验证失败");
                    finish();
                }

                finish();
            }
        });

    }
}
