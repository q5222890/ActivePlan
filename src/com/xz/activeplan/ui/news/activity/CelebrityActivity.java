package com.xz.activeplan.ui.news.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.CommentBean;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.commonAdapter.AsyncImageLoader;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.ShareSDKUtils;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.JustifyTextView;
import com.xz.activeplan.views.ObservableScrollView;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.ShareDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 新名师详情页面
 * Created by Administrator on 2016/6/14.
 */
public class CelebrityActivity extends BaseFragmentActivity implements ClassObserver, View.OnClickListener, ObservableScrollView.ScrollViewListener {
    ArrayList<ImageView>iamgeViews;
    private ImageView imageViewHeadPhoto;
    private TextView textViewName, textViewAddress, textViewSpecialty, textViewCelebrityGuarantee,
            textViewTechnologyShare, textViewPersonNum, textViewMoneyMuch, textViewStudentGuarantee,textView,textViewCelebrity,
            textViewIntroductionContent;
    private JustifyTextView textViewTechnologyShareContent;
    private LinearLayout lineGuaranteeContent;
    private String[] arrayStudentGuarante;
    private CustomProgressDialog progressDialog;
    private TeacherBean teacherBean;
    private ImageView imageViewBackground;
    private ObservableScrollView mScrollView;  //滚动
    private RelativeLayout headerLayout,rootLayout;  //头部视图 根布局
    private Drawable background;
    private boolean bIntroduction=false,bTechnologyShare =false;
    private int CurrentY;
    private ListView listView;
    private Button butAuthstatus;
    private LinearLayout linePhoto,lineVedio;
    private List<LiveTvBean> liveTvBeen=new ArrayList<>();
    private int widthVedio;
    private Intent intent;
    private Bundle bundle;
    private ShareDialog shareDialog;
    private List<CommentBean> listCommentBean=new ArrayList<>();
    private CommonAdapter<CommentBean> commonAdapter;
    private ImageView imageGuanXin;
    private View viewF;
    private String[] arrayCelebriteGuarante;
    /**
     * 头部背景
     */
    private Bitmap imageBitmap;
    private Bitmap bitmap;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                if (imageBitmap!=null) {
                    bitmap = Utiles.doBlur(imageBitmap, 10, false);
                    imageViewBackground.setImageBitmap(bitmap);
                }
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (teacherBean.getFavicon()!=null) {
                imageBitmap = AsyncImageLoader.getInstance().doInBackground(teacherBean.getCover());
                handler.sendEmptyMessage(0);
            }
        }
    };
    private Dialog  selectorDialog;
    private ViewPager viewPager;
    public View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int no= (int) v.getTag();

        //将图片装载到数组中
        iamgeViews = new ArrayList<>();
        for (int i = 0; i <teacherBean.getGallery().size() ; i++) {
            ImageView image = (ImageView) LayoutInflater.from(CelebrityActivity.this).inflate(R.layout.view_image, null);
            Picasso.with(CelebrityActivity.this).load(teacherBean.getGallery().get(i).getImgurl()).
                    placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(image);
            image.setMaxWidth(100);
            iamgeViews.add(image);
        }
        Utiles.log("--------iamgesize:"+iamgeViews.size());
        viewPager = (ViewPager) LayoutInflater.from(CelebrityActivity.this).inflate(R.layout.view_viewpager_iamge, null);
        //设置Adapter
        viewPager.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        //viewPager.setOnPageChangeListener(CelebrityActivity.this);
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem(((iamgeViews.size()) * 100)+no);
        getDialogShow(viewPager);
        selectorDialog.show();
    }
};

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_celebrity);
        ClassConcrete.getInstance().addObserver(this);
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
        initView();//初始化
        initData();//得到数据
    }

    private void initData(){
        Intent intent = getIntent() ;
        if(intent != null){
            /**
             * 根据名师的id去后台查找他的详情
             */
              teacherBean = (TeacherBean) intent.getSerializableExtra("data");
             teacherBean.getRealname();
            if (!TextUtils.isEmpty(teacherBean.getRealname()))
            ((TextView)findViewById(R.id.id_TextViewCelebriteDetails))
                    .setText(teacherBean.getRealname()+getResources().getString(R.string.string_Details));

            if(teacherBean != null){
                  progressDialog.show();
                    CelebrityNewWorkImpl.getInstance().getCelebrityDetails(teacherBean.getTea_id(), new OkHttpClientManager.StringCallback() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                            if (statusBean.getCode() == 0) {
                                TeacherBean teacherBeen = JSON.parseObject(statusBean.getData(), TeacherBean.class);
                                if (teacherBeen != null) {
                                 fillData(teacherBeen);
                                } else {
                                    finish();
                                    ToastUtil.showShort("教师信息获取失败!");
                                }
                            } else {
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {
                            progressDialog.dismiss();
                            finish();
                            ToastUtil.showShort("教师信息获取失败!");
                        }
                    });

            }else{
                finish() ;
            }
        }
    }

    /**
     * 给控件设置
     * @param bean
     */
    protected void fillData(TeacherBean bean) {
        teacherBean = bean;
        new Thread(runnable).start();
        ////特长
        textViewSpecialty.setText(teacherBean.getSkill());
        if (!TextUtils.isEmpty(bean.getCover())&&imageViewHeadPhoto!=null) {
            //头像
            Picasso.with(CelebrityActivity.this).load(bean.getCover()).error(R.drawable.default_square_image).
                    placeholder(R.drawable.default_square_image).into(imageViewHeadPhoto);
        }
        textViewName.setText(bean.getRealname()) ;
        // 认证待验=1 通过=2 未通过=3
        if (teacherBean.getAuthstatus()==2){
            butAuthstatus.setText(getResources().getString(R.string.string_AuthenticationOk));
            butAuthstatus.setBackgroundResource(R.drawable.shape_authentication);
        }else if(teacherBean.getAuthstatus()==1){
            butAuthstatus.setBackgroundResource(R.drawable.shape_no_authentication);
            butAuthstatus.setText(getResources().getString(R.string.string_WaitAuthentication));
        }else {
            butAuthstatus.setBackgroundResource(R.drawable.shape_no_authentication);
            butAuthstatus.setText(getResources().getString(R.string.string_NoPassAuthentication));
        }
        //价格
        textViewMoneyMuch.setText("￥"+bean.getPrice()+"/小时");
        //地址
        textViewAddress.setText(bean.getAddress()) ;
        //技术分享Title
        textViewTechnologyShare.setText(bean.getCaption());
        //技术分享内容
        textViewTechnologyShareContent.setText(Utiles.getSubString(bean.getGambit()));
        //个人介绍
        textViewIntroductionContent.setText(Utiles.getSubString(bean.getIntro()));
        //评价
        getCeleviteEvaluate01();
        //相册
        List<TeacherBean.GalleryBean> gallery = teacherBean.getGallery();
        if (gallery.size()>0){
            findViewById(R.id.id_TextViewNoPhoto).setVisibility(View.GONE);
        }else {
            findViewById(R.id.id_TextViewNoPhoto).setVisibility(View.VISIBLE);
        }
        View viewById = findViewById(R.id.id_LineCelebrity_Introduction);
        int width = viewById.getWidth() / 4;
        int height = (int) ((viewById.getWidth() / 4)/0.71);
         widthVedio = viewById.getWidth() / 3;
        linePhoto.removeAllViews();
        linePhoto.setMinimumHeight(width);
        linePhoto.setMinimumHeight(height);
        lineVedio.setMinimumHeight(widthVedio);
        //关注
        setGuanXinNum();
        setGuanZhu();
        //相册
        if (gallery.size()!=0)
            for (int i = 0; i < gallery.size(); i++) {
                ImageView image = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_image, null);
                image.setPadding(2, 0, 2, 0);
                Utiles.setImageWH(width - 2, height - 2, image);
                Picasso.with(this).load(gallery.get(i).getImgurl()).
                        placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(image);
                linePhoto.addView(image);
                image.setTag(i);
                image.setOnClickListener(onClickListener);
            }

        if (selectorDialog==null) {
            selectorDialog = new Dialog(this, R.style.selectorDialog);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setCanceledOnTouchOutside(true);
        }
        getVedioPhoto();
    }

    /**
     * Daiglo显示布局
     * @param view
     */
    private void getDialogShow(View view) {
        selectorDialog.setContentView(view);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = selectorDialog.getWindow().getAttributes();
        lp.height=(int)(display.getHeight() * 0.7);
        lp.width= display.getWidth();
        selectorDialog.getWindow().setAttributes(lp);
        selectorDialog.show();
    }

    /**
     * 获取VedioPhoto
     */
    private void getVedioPhoto() {
        CelebrityNewWorkImpl.getInstance().getVedio(teacherBean.getTea_id(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean!=null){
                    if (statusBean.getData()!=null){
                        List<LiveTvBean> liveTvBeens = JSON.parseArray(statusBean.getData(), LiveTvBean.class);
                        if (liveTvBeens!=null) {
                            if (liveTvBeens.size() != 0) {
                                liveTvBeen.addAll(liveTvBeens);
                                fillVedioData();
                                if (liveTvBeens.size()>0){
                                    findViewById(R.id.id_TextViewNoLiveTv).setVisibility(View.GONE);
                                }else {
                                    findViewById(R.id.id_TextViewNoLiveTv).setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
                }
            }
        });
    }

    /**
     * 关心人数
     */
    private void setGuanXinNum() {
        if (teacherBean.getFollownum()>0) {
            //关心人数
            textViewPersonNum.setText(teacherBean.getFollownum() + "");
        }else {
            textViewPersonNum.setText(0 + "");
        }
    }

    /**
     * 设置关注
     */
    private void setGuanZhu() {
        if (teacherBean.isattend()==true){
            Utiles.intRes(R.drawable.guan_xin_red);
            Utiles.setImageSrc(imageGuanXin);
        }else {
            Utiles.intRes(R.drawable.guan_xin_gray);
            Utiles.setImageSrc(imageGuanXin);
        }
    }

    /**
     * 给视频复制
     */
    private void fillVedioData() {
        lineVedio.removeAllViews();
        //视频
            for ( int i = 0; i <liveTvBeen.size() ; i++) {
                View viewVedio = LayoutInflater.from(this).inflate(R.layout.view_image_vedio, null);
                ImageView imageVedio = (ImageView) viewVedio.findViewById(R.id.id_ImageVedio);
                imageVedio.setPadding(2,0,2,0);
                lineVedio.addView(viewVedio);
                Picasso.with(this).load(liveTvBeen.get(i).getCoverurl()).
                        placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(imageVedio);
                Utiles.setImageWH(widthVedio-2,widthVedio-2,imageVedio);
                //这里设置它的跳转到播放页面
                final int finalI = i;
                imageVedio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (RongIM.getInstance() == null || RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                            Utiles.log("PageFragment connect");
                            Utiles.connectRongIM(activity);
                        }
                        Intent intent = new Intent(activity, TestChatRoomActivity.class);
                        intent.putExtra("data",liveTvBeen.get(finalI));
                        startActivity(intent);
                      //  Utiles.joinVodChatRoom(CelebrityActivity.this,liveTvBeen.get(finalI),"");
                    }
                });

            }
    }

    /**
     * 评价
     * @param
     */
    private void getEvaluate(){
       commonAdapter = new CommonAdapter<CommentBean>(this, listCommentBean, R.layout.view_evaluate_item) {
            @Override
            public void convert(ViewHolder holder, CommentBean commentBean, int position) {
                if(commentBean !=null){
                    if (commentBean.getUserInfo()!=null) {
                        if (!TextUtils.isEmpty(commentBean.getUserInfo().getHeadurl())) {
                            ImageView imageView = holder.getView(R.id.id_ImageHead);
                            Picasso.with(mContext).load(commentBean.getUserInfo().getHeadurl()).error(R.drawable.default_head_bg)
                                    .placeholder(R.drawable.default_head_bg).fit().centerCrop().into(imageView);
                        }
                    holder.setText(R.id.id_TextViewName,commentBean.getUserInfo().getUsername()+"");
                    holder.setText(R.id.id_TextViewEvaluateContent,commentBean.getContent()+"");
                    holder.setText(R.id.id_TextViewTitle,commentBean.getGam_title()+"");
                    String s = TimeUtils.formatDate(commentBean.getTime());
                    holder.setText(R.id.id_TextViewTime,s+"");
                    }
                }
            }
        };
        listView.setAdapter(commonAdapter);
    }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
            if (selectorDialog==null) {
                selectorDialog = new Dialog(CelebrityActivity.this, R.style.selectorDialog);
                selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                selectorDialog.setCanceledOnTouchOutside(true);
            }
        View view=findViewById(R.id.id_ViewButton);
        view.findViewById(R.id.id_LineMessage).setOnClickListener(this);
        view.findViewById(R.id.id_TextViewInvitation).setOnClickListener(this);
            //返回按键
        findViewById(R.id.id_ImageBlack).setOnClickListener(this);
        //头部
        headerLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        headerLayout.setBackgroundColor(getResources().getColor(R.color.header_blue));
        selectBackColor(headerLayout,0);
        //滑动
        mScrollView = (ObservableScrollView) findViewById(R.id.celebrity_scrollView);
        mScrollView.setScrollViewListener(this);
        //个人资料
        imageViewHeadPhoto = (ImageView) findViewById(R.id.id_ImageViewHeadPhoto);//名人头像
        textViewName = (TextView) findViewById(R.id.id_TextViewName);//名字
        textViewAddress = (TextView) findViewById(R.id.id_TextViewAddress);//地址
        textViewSpecialty = (TextView) findViewById(R.id.id_TextViewSpecialty);//特长
        imageViewBackground = (ImageView) findViewById(R.id.id_ImageBackground);//头像背景
        butAuthstatus = (Button) findViewById(R.id.id_ButAuthstatus);
        View viewHeadBlackGround = findViewById(R.id.id_LineBlackGround);
        Drawable background = viewHeadBlackGround.getBackground();
        background.setAlpha(150);
        viewHeadBlackGround.setBackground(background);

        //技术分享
        textViewTechnologyShare = (TextView) findViewById(R.id.id_TextViewTechnologyShareTitle);//技术分享Title
        textViewTechnologyShareContent= (JustifyTextView) findViewById(R.id.id_TextViewTechnologyShareContent);//技术分享内容
        textViewTechnologyShareContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        textViewPersonNum = (TextView) findViewById(R.id.id_TextViewPersonNum);//关心人数
        textViewMoneyMuch = (TextView) findViewById(R.id.id_TextViewMoneyMuch);//多少钱
        findViewById(R.id.id_ImageTechnologyOpenMore).setOnClickListener(this);//打开更多
        //名人介绍
        findViewById(R.id.id_ImageCelebrityIntroductionOpenMore).setOnClickListener(this);//打开更多
        textViewIntroductionContent=(TextView) findViewById(R.id.id_TextViewCelebrityIntroductionContent);

        //名人学员保障
        textViewStudentGuarantee = (TextView) findViewById(R.id.id_TextViewStudentsGuarantee);//学员
        textViewCelebrityGuarantee = (TextView) findViewById(R.id.id_TextViewCelebrityGuarantee);//名人
        lineGuaranteeContent = (LinearLayout) findViewById(R.id.id_LineGuaranteeContent);//名人头像
        textViewStudentGuarantee.setOnClickListener(this);
        textViewCelebrityGuarantee.setOnClickListener(this);

        //邀请视频信息
        findViewById(R.id.id_TextViewInvitation).setOnClickListener(this);
        findViewById(R.id.id_LineMessage).setOnClickListener(this);
        findViewById(R.id.id_LineVedioToCelebrite).setOnClickListener(this);

        //查看更多评价
        findViewById(R.id.id_TextViewLookMoreEvaluate).setOnClickListener(this);
            findViewById(R.id.id_LineCelebrityEvaluate).setMinimumHeight(300);
        //评价ListView
         listView = (ListView) findViewById(R.id.id_ListViewStudentEvaluate);
        //没有数据
            listView.setEmptyView(findViewById(R.id.id_TextViewNoData));
        //图册
        linePhoto= (LinearLayout) findViewById(R.id.id_LinePhoto);
        //视频图片
        lineVedio= (LinearLayout) findViewById(R.id.id_LineVedio);
            //关心
            imageGuanXin = (ImageView) findViewById(R.id.id_ImageGuanXin);
            imageGuanXin.setOnClickListener(this);
            //评价适配器
            getEvaluate();
        //加载进度
        onClick(textViewStudentGuarantee);

            findViewById(R.id.id_ImageCelebrityShare).setOnClickListener(this);

            shareDialog = new ShareDialog(this) ;

            shareDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()){
                        case R.id.tvShareSina:
                            ShareSDKUtils.shareSina("约吧互动名师" +teacherBean.getIntro()+ teacherBean.getPushurl(),teacherBean.getCover()) ;
                            shareDialog.dismiss();
                            break;
                        case R.id.tvShareQzone:
                            ShareSDKUtils.shareQZone("约吧互动名师"+teacherBean.getRealname() ,teacherBean.getPushurl(),teacherBean.getIntro(),teacherBean.getCover()) ;
                            shareDialog.dismiss();
                            break;
                        case R.id.tvSharewx:
                            ShareSDKUtils.shareWechat("约吧互动名师"+ teacherBean.getRealname(),teacherBean.getIntro(),teacherBean.getCover(),teacherBean.getPushurl() ) ;
                            shareDialog.dismiss();
                            break;
                        case R.id.tvSharewxpy:
                            ShareSDKUtils.shareWechatMoments("约吧互动名师"+ teacherBean.getRealname(),teacherBean.getCover(),teacherBean.getPushurl()) ;
                            shareDialog.dismiss();
                            break;
                        case R.id.tvShareMsg:
                            ShareSDKUtils.shareShortMessage("约吧互动名师"+teacherBean.getIntro()+teacherBean.getPushurl());
                            break;
                    }
                }
            }) ;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void selectBackColor(View view, int alpha) {
        background = view.getBackground();
        background.setAlpha(alpha);
        view.setBackground(background);
    }

    /**
     * 改变字体颜色
     * @param tv
     */
    private void changeTextColorCelebrity(TextView tv) {
        if (textViewCelebrity!=tv){
            tv.setSelected(true);
            tv.setTextColor(getResources().getColor(R.color.white));
            if (textViewCelebrity!=null){
                textViewCelebrity.setSelected(false);
                textViewCelebrity.setTextColor(getResources().getColor(R.color.header_blue));
            }
            textViewCelebrity=tv;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(imageBitmap!=null){
            imageBitmap.recycle();
        }
        if(bitmap !=null){
            bitmap.recycle();
        }
        liveTvBeen.clear();
        listCommentBean.clear();
        ClassConcrete.getInstance().removeObserver(this);
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.TEACHER_INVITE_TYPE) {
            finish();
        }
        return false;
    }

    @Override
    public void  onClick(View v) {
        switch (v.getId()){
            case R.id.id_ImageCelebrityShare:
                shareDialog.show();
                break;
            case R.id.id_LineMessage://聊天页面
                Utiles.x=-1;
                UserInfoBean networkAndLogin_ok1 = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok1!=null) {
                    RongIM.getInstance().startPrivateChat(this, teacherBean.getUsr_id() + "", teacherBean.getRealname());
                }
                break;
            case R.id.id_ImageBlack://返回键
                this.finish();
                break;
            case R.id.id_TextViewStudentsGuarantee://学生保障
                changeTextColorCelebrity(textViewStudentGuarantee);
                if (arrayStudentGuarante ==null) {
                    arrayStudentGuarante = getResources().getStringArray(R.array.Arrays_StudentGuarrantee);
                }
                changBaoZhang(arrayStudentGuarante);
                break;
            case R.id.id_TextViewCelebrityGuarantee://名人保证
                changeTextColorCelebrity(textViewCelebrityGuarantee);
                if (arrayCelebriteGuarante ==null) {
                    arrayCelebriteGuarante = getResources().getStringArray(R.array.Arrays_CelebriteGuarrantee);
                }
                changBaoZhang(arrayCelebriteGuarante);
                break;
            case R.id.id_TextViewInvitation://点击邀请按键
                if(SharedUtil.isLogin(CelebrityActivity.this)){
                     intent = new Intent(this,InvitationActivity.class);
                     bundle = new Bundle();
                    bundle.putSerializable("data", teacherBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("请登录用户!") ;
                    return ;
                }
                break;
            case R.id.id_ImageBackground:
                finish();
                break;
            case R.id.id_ImageTechnologyOpenMore:
                if (bTechnologyShare ==false){
                    int maxValue = Integer.MAX_VALUE;
                    textViewTechnologyShareContent.setMaxLines(maxValue);
                    bTechnologyShare =true;
                }else {
                    textViewTechnologyShareContent.setMaxLines(5);
                    textViewIntroductionContent.setLines(5);
                    bTechnologyShare =false;
                }
                break;
            case R.id.id_ImageCelebrityIntroductionOpenMore://名人介绍的（更多）
                if (bIntroduction ==false){
                    int maxValue = Integer.MAX_VALUE;
                    textViewIntroductionContent.setMaxLines(maxValue);
                    bIntroduction =true;
                }else {
                    textViewIntroductionContent.setMaxLines(5);
                    textViewIntroductionContent.setLines(5);
                    bIntroduction =false;
                }
                break;
            case R.id.id_TextViewLookMoreEvaluate:  //更多评价
                if (listCommentBean.size()>0) {
                    Utiles.skipData(EvaluateActivity.class, teacherBean.getTea_id() + "");
                }
                break;
            case R.id.id_ImageGuanXin://关心
                UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
                if (networkAndLogin_ok!=null) {
                    getGuanXin();
                }else {
                  ToastUtil.showShort("请登录");
                }
                break;
            case R.id.id_LineVedioToCelebrite:   //视频
                ToastUtil.showShort(" Development, please pay attention to...");
                break;
        }
    }
/**
 * 改变保障
 * @param array
 */
    private void changBaoZhang(String[] array){
        lineGuaranteeContent.removeAllViews();
        for (int i = 0; i < array.length; i++) {
            JustifyTextView textView = (JustifyTextView) LayoutInflater.from(this).inflate(R.layout.view_textview_student, null);
            if (array!=null&&i==array.length-1){
                int i1 = array[i].lastIndexOf("7");
                StringBuffer s = new StringBuffer(array[i]);
                s.replace(i1,i1+1,"7    ");
                textView.setText(s);
            }else {
                textView.setText(array[i]);
            }
            lineGuaranteeContent.addView(textView);
        }
    }
    /**
     * 关心接口
     */
    private void getGuanXin() {
        UserInfoBean networkAndLogin_ok = Utiles.getNetworkAndLogin_OK();
        if (networkAndLogin_ok==null){
            return;
        }
        CelebrityNewWorkImpl.getInstance().guanZhuCelebrities(networkAndLogin_ok.getUserid(),
                teacherBean.getTea_id(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(String response) {
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode()==0){
                    if (teacherBean.isattend()==true) {
                        teacherBean.setIsattend(false);
                        teacherBean.setFollownum(teacherBean.getFollownum()-1);
                    }else {
                        teacherBean.setIsattend(true);
                        teacherBean.setFollownum(teacherBean.getFollownum()+1);
                    }
                    setGuanXinNum();
                    setGuanZhu();
                }
            }
        });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        CurrentY = y;
        if(y<100)
        {
            selectBackColor(headerLayout,0);
        }else if(y>100 && y<200)
        {
            selectBackColor(headerLayout,50);
        }else if(y>=200 && y<300)
        {
            selectBackColor(headerLayout,100);
        }else if(y>300 && y<400)
        {
            selectBackColor(headerLayout,150);
        }else if(y>400)
        {
            selectBackColor(headerLayout,255);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        selectBackColor(headerLayout,255);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CurrentY<100)
        {
            selectBackColor(headerLayout,0);
        }else if(CurrentY>100 && CurrentY<200)
        {
            selectBackColor(headerLayout,50);
        }else if(CurrentY>=200 && CurrentY<300)
        {
            selectBackColor(headerLayout,100);
        }else if(CurrentY>300 && CurrentY<400)
        {
            selectBackColor(headerLayout,150);
        }else if(CurrentY>400)
        {
            selectBackColor(headerLayout,255);
        }
    }

    /**
     * 这里实现用户评价
     */
    public void  getCeleviteEvaluate01(){
        CelebrityNewWorkImpl.getInstance().getCeleviteEvaluateList(teacherBean.getTea_id(),0,2 ,new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }
                    @Override
                    public void onResponse(String response) {
                        StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                        if (statusBean != null) {
                            if (!TextUtils.isEmpty(statusBean.getData())) {
                                List<CommentBean> commentBeen = JSON.parseArray(statusBean.getData(), CommentBean.class);
                                if (commentBeen.size()!=0) {
                                    listCommentBean.addAll(commentBeen);
                                    if (commonAdapter!=null) {
                                        Utiles.setListViewHeightBasedOnChildren(listView);
                                        Utiles.log(" size:"+listCommentBean.size());
                                        commonAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            )
        ;
    }

    /**
     *
     * @author xiaanming
     *
     */
    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(iamgeViews.get(position%iamgeViews.size()));
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager)container).addView(iamgeViews.get(position%iamgeViews.size()), 0);
            }catch(Exception e){
                //handler something
            }
            return iamgeViews.get(position%iamgeViews.size());
        }



    }
}
