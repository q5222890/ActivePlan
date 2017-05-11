package com.xz.activeplan.ui.live.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.find.activity.TommorrowPersonActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.MyGridView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * 主播搜索的结果页面
 */
public class LiveSearchResultActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final String TAG = "LiveSearchResultActivity";
    public List<LiveTvBean> gridListBean = new ArrayList<>();  //主播的集合
    ImageView imageView;
    private List<LiveTvBean> gridVideoListBean = new ArrayList<>();  //相关视频的集合
    private TextView mTvHeadTitle;
    private String searchName;
    private GridView gridView;   //搜索主播的结果
    private CommonAdapter<LiveTvBean> commonAdapter;
    private GridView gvVideo;    //相关视频
    private String[] orderBy = {"timeasc", "timedesc", "seedesc"};  //排序
    private CommonAdapter<LiveTvBean> VideoCommonAdapter;
    private PopupWindow popupWindow;
    private TextView tvOrderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_live_search_result);
        initView();
        loadAnchorData();
        loadVideoData(orderBy[1]);
        TextView txt = (TextView) findViewById(R.id.tv);
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        txt.setTypeface(font);
    }

    /***
     * 相关视频
     *
     */
    private void loadVideoData(String str) {
        int page = 0;  //当前页面
        int currentPageSize = 20;  //每页的数量
        final String url = UrlsManager.URL_SEARCH_VIDEO + page + "&pagesize=" + currentPageSize + "&orderby=" + str + "&key=" + searchName;
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    List<LiveTvBean> list = JSON.parseArray(statusBean.getData(), LiveTvBean.class);
                    gridVideoListBean.addAll(list);
                    Utiles.log(TAG + "---listVideo----" + gridVideoListBean.toString() + url);
                    if(gridVideoListBean.size()>0)
                    {
                        findViewById(R.id.liveSearch_rl).setVisibility(View.VISIBLE);  //相关视频
                        imageView.setVisibility(View.GONE);
                    }else if(gridListBean.size() <= 0 && gridVideoListBean.size() <= 0)
                    {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    VideoCommonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /***
     * 相关主播
     **/
    private void loadAnchorData() {
        UserInfoBean info = SharedUtil.getUserInfo(this);
        int userid = 0;
        if (!TextUtils.isEmpty(info.getUserid() + "")) {
            userid = info.getUserid();
        }
        final String url = UrlsManager.URL_ANCHOR_SEARCH + "0&pagesize=4&userid=" + userid + "&key=" + searchName;
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    List<LiveTvBean> list = JSON.parseArray(statusBean.getData(), LiveTvBean.class);
                    gridListBean.addAll(list);
                    Utiles.log(TAG + "---list----" + gridListBean.toString());
                    if (gridListBean.size() > 0) {
                        findViewById(R.id.search_rl).setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                    }else if(gridListBean.size() <= 0 && gridVideoListBean.size() <= 0)
                    {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    VideoCommonAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void initView() {
        tvOrderBy = (TextView) findViewById(R.id.search_tvOrderBy);
        findViewById(R.id.search_tvMore).setOnClickListener(this);  //更多

        findViewById(R.id.search_rl).setVisibility(View.GONE);   //相关主播
        findViewById(R.id.liveSearch_rl).setVisibility(View.GONE);  //相关视频
        imageView = (ImageView) findViewById(R.id.search_iv_no);   //没有数据就显示无数据视图
        imageView.setVisibility(View.GONE);
        mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        tvOrderBy.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            searchName = intent.getStringExtra("searchname");
            mTvHeadTitle.setText(searchName + "");
        }

        //相关视频的适配器
        gvVideo = (GridView) findViewById(R.id.search_gridView1);
        VideoCommonAdapter = new CommonAdapter<LiveTvBean>(this, gridVideoListBean, R.layout.griditem_livecategory) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                holder.setText(R.id.tv_livetitle, liveTvBean.getUsername());
                holder.getView(R.id.tv_lable).setVisibility(View.GONE);  //隐藏
                ImageView cover = holder.getView(R.id.iv_livecover);
                Picasso.with(LiveSearchResultActivity.this).load(liveTvBean.getCoverurl())
                        .placeholder(R.drawable.default_details_image).fit().centerCrop().error(R.drawable.default_details_image)
                        .into(cover);
            }
        };
        gvVideo.setAdapter(VideoCommonAdapter);
        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,gridVideoListBean.get(position));
            }
        });
        //相关主播
        gridView = (MyGridView) findViewById(R.id.search_gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(LiveSearchResultActivity.this, TommorrowPersonActivity.class);
                intent.putExtra("data",gridListBean.get(position));
                startActivity(intent);
            }
        });
        commonAdapter = new CommonAdapter<LiveTvBean>(this, gridListBean, R.layout.view_gridview_search) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                holder.setText(R.id.gridItem_name, liveTvBean.getUsername() + "");
                ImageView header = holder.getView(R.id.gridItem_header);
                Picasso.with(LiveSearchResultActivity.this).load(liveTvBean.getHeadurl())
                        .placeholder(R.drawable.default_head_bg).fit().centerCrop().error(R.drawable.default_head_bg)
                        .into(header);
            }
        };
        gridView.setAdapter(commonAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:   //返回
                finish();
                break;
            case R.id.search_tvOrderBy:     //综合排序
                showPopUpWindow(v);
                break;
            case R.id.search_tvMore:     //更多
                ToastUtil.showCenterToast(this,"暂无更多哦.");
                break;
        }
    }
    //右上角更多菜单显示
    public void showPopUpWindow(View view) {

        View mView = LayoutInflater.from(this).inflate(R.layout.popwindow_search_orderby, null);
        //默认
        TextView tvDefault = (TextView) mView.findViewById(R.id.textView_default);
        //升序
        TextView tvAsc = (TextView) mView.findViewById(R.id.textView_asc);
        //降序
        TextView tvDesc = (TextView) mView.findViewById(R.id.textView_desc);
        //关注量
        TextView tvRecommend = (TextView) mView.findViewById(R.id.textView_recommend);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int SCREEN_WIDTH = dm.widthPixels;
        int SCREEN_HEIGHT = dm.heightPixels;
        popupWindow = new PopupWindow(mView, SCREEN_WIDTH * 5 / 18, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.home_org_bg));

        popupWindow.showAsDropDown(view);
        backgroundAlpha(0.4f);
        tvDefault.setOnClickListener(new OnMessageClick());
        tvAsc.setOnClickListener(new OnMessageClick());
        tvDesc.setOnClickListener(new OnMessageClick());
        tvRecommend.setOnClickListener(new OnMessageClick());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Utiles.log("ondestory------------");
        super.onDestroy();
        if(gridVideoListBean.size()>0 || gridListBean.size()>0)
        {
            Utiles.log("ondestory------------"+gridListBean.size()+"--video-----"+gridVideoListBean.size());
            gridVideoListBean.clear();
            gridListBean.clear();
        }
    }

    /***
     * popupWindow的点击事件
     */
    private class OnMessageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.textView_default:  //默认排序（就是降序）
                    gridVideoListBean.clear();
                    loadVideoData(orderBy[1]);
                    popupWindow.dismiss();
                    tvOrderBy.setText("默认排序");
                    break;
                case R.id.textView_asc:    //升
                    gridVideoListBean.clear();
                    loadVideoData(orderBy[0]);
                    popupWindow.dismiss();
                    tvOrderBy.setText("时间升序");
                    break;
                case R.id.textView_desc:   //降
                    gridVideoListBean.clear();
                    loadVideoData(orderBy[1]);
                    popupWindow.dismiss();
                    tvOrderBy.setText("时间降序");
                    break;
                case R.id.textView_recommend:   //关注量
                    gridVideoListBean.clear();
                    loadVideoData(orderBy[2]);
                    popupWindow.dismiss();
                    tvOrderBy.setText("关注量");
                    break;
            }
        }
    }
}
