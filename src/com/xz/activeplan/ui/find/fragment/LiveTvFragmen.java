package com.xz.activeplan.ui.find.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.find.activity.VideoCollectActivity;
import com.xz.activeplan.ui.find.adapter.LiveFragmentAdapter;
import com.xz.activeplan.ui.find.fragment.fragments.AnchorFragment;
import com.xz.activeplan.ui.find.fragment.fragments.GuanZhuFragment;
import com.xz.activeplan.ui.find.fragment.fragments.PageFragment;
import com.xz.activeplan.ui.find.fragment.fragments.RecommendationFragment;
import com.xz.activeplan.ui.find.fragment.fragments.TommorrowFragment;
import com.xz.activeplan.ui.live.activity.LiveSearchActivity;
import com.xz.activeplan.ui.personal.activity.MessageActivity;
import com.xz.activeplan.utils.Utiles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * 直播碎片
 * A simple {@link Fragment} subclass.
 */
public class LiveTvFragmen extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static LiveTvFragmen instance = null;
    private final int BtextViewColor = 0xff3780c4;
    private final int HtextViewColor = 0xff000000;
    private View view;
    private EditText editText;
    private List<TextView> textViewList = new ArrayList<TextView>();
    private TextView textViewPage, textViewRecommendation, textViewGuanZhu, textViewTomorrow, textViewAnchor;
    private ViewPager viewPager;
    private List<BaseFragment> baseFragments = new ArrayList<BaseFragment>();
    private LiveFragmentAdapter liveFragmentAdapter;
    private TextView textView;
    private ArrayList<ImageView> imageViews;
    private PageFragment pageFragment;
    private RecommendationFragment recommendationFragment;
    private GuanZhuFragment guanZhuFragment;
    private TommorrowFragment tommorrowFragment;
    private AnchorFragment anchorFragment;
    private BaseFragment baseFragment;
    private TextView hello;


    public LiveTvFragmen() {
    }

    public static LiveTvFragmen getInstance() {
        if (instance == null) {
            instance = new LiveTvFragmen();
        }
        return instance;
    }

    @Override
    public void onDestroy() {
        baseFragments.clear();
        textViewList.clear();

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_live_tv, container, false);
        initView();
        return view;
    }

    private void initView() {
        view.findViewById(R.id.ivCollect).setOnClickListener(this);//我的收藏
        //  view.findViewById(R.id.id_ImageSearch).setOnClickListener(this);//开始搜索
        view.findViewById(R.id.id_ImageMassage).setOnClickListener(this);//信息
        view.findViewById(R.id.id_ImageMore).setOnClickListener(this);//更多
        // editText= (EditText) view.findViewById(R.id.id_EditTextContext);//搜索内容
        textViewPage = (TextView) view.findViewById(R.id.id_TextViewPage);
        textViewRecommendation = (TextView) view.findViewById(R.id.id_TextViewRecommendation);
        textViewGuanZhu = (TextView) view.findViewById(R.id.id_TextViewGuanZhu);
        textViewTomorrow = (TextView) view.findViewById(R.id.id_TextViewTomorrow);
        textViewAnchor = (TextView) view.findViewById(R.id.id_TextViewAnchor);
         editText = (EditText) view.findViewById(R.id.etLiveSearch);
        textViewList.add(textViewPage);
        textViewList.add(textViewRecommendation);
        textViewList.add(textViewGuanZhu);
        textViewList.add(textViewTomorrow);
        textViewList.add(textViewAnchor);
        textViewPage.setOnClickListener(this);
        textViewRecommendation.setOnClickListener(this);
        textViewGuanZhu.setOnClickListener(this);
        textViewTomorrow.setOnClickListener(this);
        textViewAnchor.setOnClickListener(this);

        editText.setOnClickListener(this);

        if (baseFragments.size() == 0) {
            pageFragment = PageFragment.getInstance();
            baseFragments.add(pageFragment);
            recommendationFragment = RecommendationFragment.getInstance();
            baseFragments.add(recommendationFragment);
            guanZhuFragment = GuanZhuFragment.getInstance();
            baseFragments.add(guanZhuFragment);
            tommorrowFragment = TommorrowFragment.getInstance();
            baseFragments.add(tommorrowFragment);
            anchorFragment = AnchorFragment.getInstance();
            baseFragments.add(anchorFragment);
            liveFragmentAdapter = new LiveFragmentAdapter(getChildFragmentManager(), baseFragments);
        }
        ChangeTextViewColor(textViewPage);
        replaceFragment(pageFragment,0);
    }

    private void ChangeTextViewColor(TextView tv) {//改变字体颜色
        if (textView != tv) {
            if (textView != null) {
                textView.setTextColor(HtextViewColor);
            }
            tv.setTextColor(BtextViewColor);
            textView = tv;
        }
    }
    /**
     * 替换fragment
     *
     * @param fragment 替换的fragment
     * @param index    替换的下标
     *
     */

    private void replaceFragment(BaseFragment fragment, int index) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        synchronized (this) {
            if (fragment.isAdded()) {
                return;
            }
            ft.replace(R.id.id_FrameLayout_LiveTv, fragment);
            ft.commit();
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCollect: //我的收藏
                Utiles.skipNoData(VideoCollectActivity.class);
                break;
            case R.id.id_ImageMassage: //消息
                Utiles.skipNoData(MessageActivity.class);
                break;
            case R.id.id_ImageMore:
                break;

            case R.id.id_TextViewPage://首页
               replaceFragment(pageFragment,0);
                ChangeTextViewColor(textViewList.get(0));
                break;
            case R.id.id_TextViewRecommendation://推荐
               replaceFragment(recommendationFragment,1);
                ChangeTextViewColor(textViewList.get(1));
                break;
            case R.id.id_TextViewGuanZhu://关注
                replaceFragment(guanZhuFragment,2);
                ChangeTextViewColor(textViewList.get(2));
                break;
            case R.id.id_TextViewTomorrow://明日之星
                replaceFragment(tommorrowFragment,3);
                ChangeTextViewColor(textViewList.get(3));
                break;
            case R.id.id_TextViewAnchor://主播
                replaceFragment(anchorFragment,4);
                ChangeTextViewColor(textViewList.get(4));
                break;
            case R.id.etLiveSearch:    //直播搜索页面
                Utiles.skipNoData(LiveSearchActivity.class);
                break;
        }
    }
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        ChangeTextViewColor(textViewList.get(i));
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
