package com.xz.activeplan.ui.find.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.MyGridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/***
 * 播放历史界面
 * 获取本地数据需要修改为播放的真实时间
 */
public class PlayHistoryctivity extends FragmentActivity implements View.OnClickListener {
    public final static String PLAY_HISTORY = "YueBa/PlayHistory";

    private List<LiveTvBean> listLiveTvBeanOneWeek = new ArrayList<LiveTvBean>();
    private List<LiveTvBean> listLiveTvBeanOneWeekAgo = new ArrayList<LiveTvBean>();
    private TextView textViewTitle;
    private LinearLayout relativeLayoutOneWeek, relativeLayoutOneWeekAgo;
    private RelativeLayout relativeLayoutNoPlay;
    private MyGridView gridViewOnWeek, gridViewOnWeekAgo;
    private PullToRefreshScrollView scrollView;
    private CommonAdapter<LiveTvBean> commonAdapterOneWeek;
    private CommonAdapter<LiveTvBean> commonAdapterOneWeekAgo;
    private String nowTimeStr;
    private int datySeven = 7;
    private ImageView back;

    @Override
    protected void onDestroy() {
        listLiveTvBeanOneWeekAgo.clear();
        listLiveTvBeanOneWeek.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_historyctivity);
        nowTimeStr = FileUtil.getCurrDateTime();
        initView();
        loadData();

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        back.setOnClickListener(this);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.id_ScrollViewPlayHistory);
        // 上拉、下拉设定
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        textViewTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        textViewTitle.setText(getResources().getString(R.string.PlayHistory_Title));
        relativeLayoutOneWeek = (LinearLayout) findViewById(R.id.id_RelativeOneWeek);//一周以内布局
        relativeLayoutOneWeekAgo = (LinearLayout) findViewById(R.id.id_RelativeOneWeekAgo);//一周以前布局
        relativeLayoutNoPlay = (RelativeLayout) findViewById(R.id.id_RelativeNoPlay);//没用播放历史布局
        gridViewOnWeek = (MyGridView) findViewById(R.id.id_GridViewHistoryOnWeek);
        gridViewOnWeekAgo = (MyGridView) findViewById(R.id.id_GridViewHistoryOnWeekAgo);

        commonAdapterOneWeek = new CommonAdapter<LiveTvBean>(this, listLiveTvBeanOneWeek, R.layout.view_gridview_live) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                holder.setImageURL(R.id.id_ImageGride, liveTvBean.getCoverurl());
                holder.setText(R.id.id_TextViewGride, liveTvBean.getTitle());
                holder.setText(R.id.tv_lable,liveTvBean.getStatus());
            }
        };
        commonAdapterOneWeekAgo = new CommonAdapter<LiveTvBean>(this, listLiveTvBeanOneWeekAgo, R.layout.view_gridview_live) {
            @Override
            public void convert(ViewHolder holder, LiveTvBean liveTvBean, int position) {
                holder.setImageURL(R.id.id_ImageGride, liveTvBean.getCoverurl());
                holder.setText(R.id.id_TextViewGride, liveTvBean.getTitle());
                holder.setText(R.id.tv_lable,liveTvBean.getStatus());
                Utiles.log("liveTvBean.getUsername()2:" + liveTvBean.getUsername());
            }
        };
        gridViewOnWeekAgo.setAdapter(commonAdapterOneWeekAgo);
        gridViewOnWeek.setAdapter(commonAdapterOneWeek);

    }

    private void loadData() {
        boolean fileExist = FileUtil.isFileExist(PLAY_HISTORY);
        if (fileExist == false) {
            relativeLayoutNoPlay.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            File fileAbsolute = FileUtil.getFileAbsolute(PLAY_HISTORY);
            File[] files = fileAbsolute.listFiles();
            Utiles.log("PlayHistory 播放历史本地数据为:" + files.length + "  boolean:fileExist :" + fileExist);
            try {
                for (File file : files) {
                    String name = file.getName();
                    String s = name.split("\\.")[0];
                    int daty = FileUtil.getGapCount("2016-7-01");//这里需要改成enddata
                    FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    /**
                     * 这里是模拟数据七天为标准---后更新
                     */
                    if (ois != null) {
                        LiveTvBean liveTvBean = (LiveTvBean) ois.readObject();
                        if (daty > datySeven) {
                            listLiveTvBeanOneWeekAgo.add(liveTvBean);
                            listLiveTvBeanOneWeek.add(liveTvBean);
                        } else {
                            listLiveTvBeanOneWeek.add(liveTvBean);
                        }
                        fis.close();
                        ois.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * 这里数据是7天以前
             */
            if (listLiveTvBeanOneWeekAgo.size() == 0) {
                relativeLayoutOneWeekAgo.setVisibility(View.GONE);
            } else {
                relativeLayoutOneWeekAgo.setVisibility(View.VISIBLE);
                commonAdapterOneWeekAgo.notifyDataSetChanged();
            }
            /**
             * 这里数据是7天以前
             */
            if (listLiveTvBeanOneWeek.size() == 0) {
                relativeLayoutOneWeek.setVisibility(View.GONE);
            } else {
                commonAdapterOneWeek.notifyDataSetChanged();
                relativeLayoutOneWeek.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                this.finish();
                break;
        }
    }
}
