package com.xz.activeplan.ui.recommend.activity;

import android.view.View;
import android.widget.Button;

import com.isnc.facesdk.aty.Aty_BaseGroupCompare;
import com.xz.activeplan.R;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;

import org.json.JSONObject;

/**
 * 扫脸
 */
public class LoseFaceActivity extends Aty_BaseGroupCompare implements View.OnClickListener {


    private String thirid;
    private Button button;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lose_face;
    }
    @Override
    protected void initView() {

        button = (Button) findViewById(R.id.bt);
        thirid = SharedUtil.getUserInfo(this).getThirdid();
        Utiles.log("当前的用户脸信息：" +thirid);
        //初始化多人脸,size为数组，{width，height}设置surfaceview宽高，宽高比例必须为3:4,否则会变形
        //若为null，则为全屛
        int[] size = {480,640};
        initFacesFeature(size);
        //设置摄像头 前1 后0
        setCameraType(1);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initActions() {
        //开始执行人脸检测，sourceID、groupID参照后端文档获得
        facesDetect("0095e0fa6697b7734894e57b","011b13b6cee7749b766a8b7532c732fa");
    }

    @Override
    protected void doFacesCallBack(JSONObject jsonObject) {
        super.doFacesCallBack(jsonObject);
        Utiles.log("人脸检索回调： "+jsonObject.toString());
    }

    @Override
    public void onClick(View v) {
    }
}
