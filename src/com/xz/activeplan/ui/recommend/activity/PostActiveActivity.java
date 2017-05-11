package com.xz.activeplan.ui.recommend.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePopupWindow;
import com.oginotihiro.cropview.CropView;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.entity.imageUrlBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.impl.ActiveServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.BitmapUtil;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.ImageTools;
import com.xz.activeplan.utils.LogUtil;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.ActiveCategoryDialog;
import com.xz.activeplan.views.ActiveThemeDialog;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.EditDialogFragment;
import com.xz.activeplan.views.RichEditor;
import com.xz.activeplan.views.SelectPhotoDialog;
import com.xz.activeplan.views.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 发布活动界面activity
 */
public class PostActiveActivity extends BaseFragmentActivity implements
        OnClickListener, ClassObserver, EditDialogFragment.InputListener, View.OnLayoutChangeListener {
    public static final int LOCAL_PHOTO_ALBUM = 0; // Local photo album
    public static final int TAKING_PICTURES = 1; // Taking pictures
    public static final int CUT_OUT_THE_PICTURE = 2; // Cut out the picture
    public static final int EDIT_LOCAL_PHOTOALBUM = 3; // 本地图库
    public static final int EDIT_TAKING_PICTURES = 4; // 拍照
    private static final int SCALE = 5;
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    boolean charge = false;
    double money = 0;
    private String mPicPath = "";
    private Bitmap mBitmap;
    //    private int mOutputX = 500, mOutputY = 500;
    private boolean isCutPicture = false;
    private TextView tvHeadTitle, tv_theme, tv_category;
    private LinearLayout llyt_group;
    private RelativeLayout rlyt_add;
    private List<String> imageList = new ArrayList<>();

    private ImageView img_photo;
    private CheckBox ck_no_momey, ck_yes_momey;
    private SelectPhotoDialog mSelectPhotoDialog;
    private ActiveThemeDialog mActiveThemeDialog;
    private ActiveCategoryDialog mActiveCategoryDialog;
    private int category = 0;
    private int theme = 0;
    private int style = 0;
    private RelativeLayout rlyt_category;
    private EditText mEdStartTime, mEdEndTime, edtmap;
    private Button mBtnStartTime, mBtnEndTime;
    private Button mBtnActivePost;
    private EditText ed_active_name, ed_active_num;
    private int hostid = 0;
    private UserInfoBean mUserInfoBean;
    private CustomProgressDialog mProgressDialog;
    private ArrayList<TicketAddBean> list = new ArrayList<TicketAddBean>();
    private TimePopupWindow timePopupWindow;  //时间选择器
    private String time;
    private CropView cropView;
    private LinearLayout btnlay;
    private Button cancelBtn, doneBtn;
    private Bitmap croppedBitmap;
    private RichEditor mEditor;
    private ImageButton actiontextsize, actioninsertimage, actioninsertvideo, actionbold, actionunderline, actiontextcolor;
    private String pictureUrl;
    private ImageView ivdeletegarbage;
    private String content;
    private LinearLayout line_editortool;
    private int popupHeight;
    private int popupWidth;
    private CheckBox needsupport, dontneedsupport;
    private PopupWindow popupwindowEditortoolbar;  //弹起工具条
    private PopupWindow popupwindowInsertImage;    //插入图片
    private PopupWindow popupwindowTextSize;      //选择文字大小
    private PopupWindow popupwindowColor;         //选择文字颜色
    private PopupWindow popUpchangeColor;
    private InputMethodManager imm;
    private String changedHtml = "";
    private RelativeLayout line_post;
    private ImageButton popactionbold, popactionunderline, popactiontextcolor, popactiontextsize,
            popactioninsertimage, popactioninsertvideo;
    private ImageView popivdeletegarbage;
    private int keyboardHeight;
    private TextView tvLoginAndReg;
    private TextView tv_sponsorship;
    private RelativeLayout relative_addsupport;
    private ObservableScrollView scrollView;
    private int scrollViewHeight;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ClassConcrete.getInstance().addObserver(this);
        setContentView(R.layout.activity_post_active);
        View view = findViewById(R.id.view_top);
        view.setVisibility(View.GONE);
        initViews();
        Intent intent = getIntent();
        if (intent != null) {
            hostid = intent.getIntExtra("hostid", 0);
        }

        if (SharedUtil.isLogin(this)) {
            mUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            ToastUtil.showShort("请先登录用户");
        }
    }

    private void initViews() {

        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);

        //返回框背景
        View viewHeald = findViewById(R.id.relativeLayout_toolbar);
        viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
        //返回键
        ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(this);
        //头部字体
        tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        tvHeadTitle.setTextColor(getResources().getColor(R.color.white));

        img_photo = (ImageView) findViewById(R.id.img_photo);

        rlyt_category = (RelativeLayout) findViewById(R.id.rlyt_category);

        mEdStartTime = (EditText) findViewById(R.id.ed_start_time);
        mEdEndTime = (EditText) findViewById(R.id.ed_end_time);
        edtmap = (EditText) findViewById(R.id.edtmap);
        ed_active_name = (EditText) findViewById(R.id.ed_active_name);//活动名称
        ed_active_num = (EditText) findViewById(R.id.ed_active_num);  //活动人数
        mBtnStartTime = (Button) findViewById(R.id.btn_start_time);
        mBtnEndTime = (Button) findViewById(R.id.btn_end_time);
        findViewById(R.id.btnmap).setOnClickListener(this);
        mBtnActivePost = (Button) findViewById(R.id.btn_active_post);

        tv_category = (TextView) findViewById(R.id.tv_category);

        ck_yes_momey = (CheckBox) findViewById(R.id.ck_yes_momey);

        llyt_group = (LinearLayout) findViewById(R.id.llyt_group);
        rlyt_add = (RelativeLayout) findViewById(R.id.rlyt_add);

        cropView = (CropView) findViewById(R.id.cropView);
        btnlay = (LinearLayout) findViewById(R.id.btnlay);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        doneBtn = (Button) findViewById(R.id.doneBtn);

        mEditor = (RichEditor) findViewById(R.id.richeditor);
        mEditor.setEditorHeight(200);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                ivdeletegarbage.setVisibility(View.GONE);
                int height =mEditor.getHeight();
                Utiles.log("编辑器高度："+height);
//                scrollView.scrollTo(0,scrollViewHeight+height);
            }
        });
        content = SharedUtil.get(PostActiveActivity.this, "recontent");
        Utiles.log("sp获取content:" + content);

        imageList = SharedUtil.loadArray(this);
        Utiles.log("sp获取草稿list：" + imageList);
        mEditor.setHtml(content);
        line_editortool = (LinearLayout) findViewById(R.id.line_editortool);
        actionbold = (ImageButton) findViewById(R.id.action_bold);
        actiontextcolor = (ImageButton) findViewById(R.id.action_txt_color);
        actiontextsize = (ImageButton) findViewById(R.id.action_txt_size);
        actionunderline = (ImageButton) findViewById(R.id.action_underline);
        actioninsertimage = (ImageButton) findViewById(R.id.action_insert_image);
        actioninsertvideo = (ImageButton) findViewById(R.id.action_insert_video);
        ivdeletegarbage = (ImageView) findViewById(R.id.iv_delete_garbage);

        tvLoginAndReg = (TextView) findViewById(R.id.tvLoginAndReg);
        tvLoginAndReg.setVisibility(View.VISIBLE);
        tvLoginAndReg.setText("发布");
        tvLoginAndReg.setTextSize(18);
        tvLoginAndReg.setOnClickListener(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!TextUtils.isEmpty(content)) {
            ivdeletegarbage.setVisibility(View.VISIBLE);
        }

        needsupport = (CheckBox) findViewById(R.id.cb_needsupport);
        dontneedsupport = (CheckBox) findViewById(R.id.cb_dontneed_support);

        ivdeletegarbage.setOnClickListener(new onActionListener());
        actionbold.setOnClickListener(new onActionListener());
        actionunderline.setOnClickListener(new onActionListener());
        actiontextcolor.setOnClickListener(new onActionListener());
        actiontextsize.setOnClickListener(new onActionListener());
        actioninsertimage.setOnClickListener(new onActionListener());
        actioninsertvideo.setOnClickListener(new onActionListener());

        cancelBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        rlyt_add.setOnClickListener(this);
        edtmap.setOnClickListener(this);
        mEdStartTime.setOnClickListener(this);
        mEdEndTime.setOnClickListener(this);
        tvHeadTitle.setText("发布活动");
        img_photo.setOnClickListener(this);

        mSelectPhotoDialog = new SelectPhotoDialog(this);
        mSelectPhotoDialog.setOnClickListener(this);

        mActiveThemeDialog = new ActiveThemeDialog(this);
        mActiveThemeDialog.setOnClickListener(this);

        mActiveCategoryDialog = new ActiveCategoryDialog(this);
        mActiveCategoryDialog.setOnClickListener(this);

        rlyt_category.setOnClickListener(this);
        mBtnStartTime.setOnClickListener(this);
        mBtnEndTime.setOnClickListener(this);
        mBtnActivePost.setOnClickListener(this);

        timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.ALL);

        tv_sponsorship = (TextView) findViewById(R.id.tv_sponsorship);
        relative_addsupport = (RelativeLayout) findViewById(R.id.relative_add_support);
        relative_addsupport.setOnClickListener(this);
        needsupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dontneedsupport.setChecked(false);
                    tv_sponsorship.setVisibility(View.VISIBLE);
                    relative_addsupport.setVisibility(View.VISIBLE);
                }
            }
        });
        dontneedsupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    needsupport.setChecked(false);
                    tv_sponsorship.setVisibility(View.GONE);
                    relative_addsupport.setVisibility(View.GONE);
                }
            }
        });

        int screenheight = getWindow().getDecorView().getRootView().getHeight();
        keyboardHeight = screenheight / 3;

        line_post = (RelativeLayout) findViewById(R.id.line_post);
        line_post.addOnLayoutChangeListener(this);

        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                Utiles.log("x:"+x+"y:"+y+";oldx:"+oldx+";oldy:"+oldy);
                scrollViewHeight =y;
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void setListener(final EditText view) {
        //时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onTimeSelect(Date date) {
                //格式化时间
                time = TimeUtils.formatTime(date);
                //得到当前系统时间
                String currentDateString = TimeUtils.getCurrentDateString();
                Utiles.log("当前时间：" + currentDateString);
                //得到时间差
                boolean before = TimeUtils.minutesBefore(time, currentDateString);
                LogUtil.show("time---------", "---" + time + "--------" + currentDateString);
                if (before == false) {
                    ToastUtil.showShort("不能小于当前时间");
                } else {
                    view.setText(time);
                }
            }
        });

        timePopupWindow.setFocusable(true);
        backgroundAlpha(0.3f);
        timePopupWindow.setOnDismissListener(new PoponDismissListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.tvLoginAndReg:
                if (hostid != 0) {
                    for (int i = 0; i < imageList.size(); i++) {
                        Utiles.log("获取图片列表：" + imageList.get(i));
                    }
                    Utiles.log("修改前:" + mEditor.getHtml());
                    Utiles.log("修改后:" + changedHtml);
                    if (imageList.size() > 0) {
                        updateImage();
                    } else {
                        postActive(mEditor.getHtml());
                    }
                } else {
                    ToastUtil.showShort("获取hostid失败！无法发布活动");
                }
                break;
            case R.id.btn_active_post:
                if (hostid != 0) {
                    if (imageList.size() > 0) {
                        updateImage();
                    } else {
                        postActive(mEditor.getHtml());
                    }
                } else {
                    ToastUtil.showShort("获取hostid失败！无法发布活动");
                }
                break;
            case R.id.rlyt_category:
                mActiveCategoryDialog.show();
                break;
            case R.id.ed_start_time:
            case R.id.btn_start_time:
                timePopupWindow.showAtLocation(mEdStartTime, Gravity.BOTTOM, 0, 0, new Date());
                setListener(mEdStartTime);
                break;
            case R.id.ed_end_time:
            case R.id.btn_end_time:
                timePopupWindow.showAtLocation(mEdEndTime, Gravity.BOTTOM, 0, 0, new Date());
                setListener(mEdEndTime);
                break;
            case R.id.edtmap://检索地图
//                break;
            case R.id.btnmap://查看地图
                Intent intent = new Intent(PostActiveActivity.this,
                        PoiSearchActivity.class);
                startActivity(intent);

                break;
            case R.id.img_photo:
                mSelectPhotoDialog.show();
                break;
            case R.id.tvPhotograph:  //拍照
                mSelectPhotoDialog.dismiss();
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mPicPath = getPictureName();
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(mPicPath)));
                startActivityForResult(intent, TAKING_PICTURES);
                break;
            case R.id.tvAlbum:       //从相册获取
                mSelectPhotoDialog.dismiss();
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, LOCAL_PHOTO_ALBUM);
                break;
            case R.id.rlyt_add:
                int num = getTicket();
                if (num > 0) {
                    intent = new Intent(PostActiveActivity.this,
                            ActiveTicketctivity.class);
                    intent.putExtra("data", num);
                    startActivity(intent);
                } else {
                    ToastUtil.showShort("超过参加人数限制，请设置更多活动人数!");
                }
                break;
            case R.id.txt1:
                tv_theme.setText("创业");
                theme = 1;
                break;
            case R.id.txt2:
                tv_theme.setText("商务");
                theme = 2;
                break;
            case R.id.txt3:
                tv_theme.setText("公益");
                theme = 3;
                break;
            case R.id.txt4:
                tv_theme.setText("社交");
                theme = 4;
                break;
            case R.id.txt5:
                tv_theme.setText("亲子");
                theme = 5;
                break;
            case R.id.txt6:
                tv_theme.setText("电影");
                theme = 6;
                break;
            case R.id.txt7:
                tv_theme.setText("娱乐");
                theme = 7;
                break;
            case R.id.txt8:
                tv_theme.setText("生活");
                theme = 8;
                break;
            case R.id.category1:
                tv_category.setText("爱心吧");
                category = 1;
                break;
            case R.id.category2:
                tv_category.setText("相亲吧");
                category = 2;
                break;
            case R.id.category3:
                tv_category.setText("商务吧");
                category = 3;
                break;
            case R.id.category4:
                tv_category.setText("老乡吧");
                category = 4;
                break;
            case R.id.category5:
                tv_category.setText("创业吧");
                category = 5;
                break;
            case R.id.category6:
                tv_category.setText("运动吧");
                category = 6;
                break;
            case R.id.category7:
                tv_category.setText("学习吧");
                category = 7;
                break;
            case R.id.category8:
                tv_category.setText("旅游吧");
                category = 8;
                break;
            case R.id.category9:
                tv_category.setText("生活吧");
                category = 9;
                break;
            case R.id.category10:
                tv_category.setText("校园吧");
                category = 10;
                break;
            case R.id.category11:
                tv_category.setText("群星吧");
                category = 11;
                break;
            case R.id.category12:
                tv_category.setText("同性吧");
                category = 12;
                break;
            case R.id.category13:
                tv_category.setText("其他吧");
                category = 13;
                break;
            case R.id.cancelBtn:
                reset();
                break;
            case R.id.doneBtn:
                cutOutPicture();
                break;
            case R.id.relative_add_support:
                startActivity(new Intent(PostActiveActivity.this, AddSupportActivity.class));
                break;
        }
    }

    /**
     * 弹出工具栏
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void popupEditortoolbar(int keyboardHeight) {

        View view = View.inflate(PostActiveActivity.this, R.layout.pop_editortoolbar, null);
        actionbold = (ImageButton) view.findViewById(R.id.action_bold);
        actionunderline = (ImageButton) view.findViewById(R.id.action_underline);
        actiontextcolor = (ImageButton) view.findViewById(R.id.action_txt_color);
        actiontextsize = (ImageButton) view.findViewById(R.id.action_txt_size);
        actioninsertimage = (ImageButton) view.findViewById(R.id.action_insert_image);
        actioninsertvideo = (ImageButton) view.findViewById(R.id.action_insert_video);
        ivdeletegarbage = (ImageView) view.findViewById(R.id.iv_delete_garbage);

        actionbold.setOnClickListener(new onActionListener());
        actionunderline.setOnClickListener(new onActionListener());
        actiontextsize.setOnClickListener(new onActionListener());
        actiontextcolor.setOnClickListener(new onActionListener());
        actioninsertimage.setOnClickListener(new onActionListener());
        actioninsertvideo.setOnClickListener(new onActionListener());
        ivdeletegarbage.setOnClickListener(new onActionListener());
        if (popupwindowEditortoolbar == null) {
            popupwindowEditortoolbar = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        popupwindowEditortoolbar.setClippingEnabled(true);
        popupwindowEditortoolbar.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupwindowEditortoolbar.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupwindowEditortoolbar.showAtLocation(getCurrentFocus(), Gravity.BOTTOM, 0, keyboardHeight);
    }
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void popupEditortoolbar(int keyboardHeight) {
//
//        View view = View.inflate(PostActiveActivity.this, R.layout.pop_editortoolbar2, null);
//        popactionbold = (ImageButton) view.findViewById(R.id.pop_action_bold);
//        popactionunderline = (ImageButton) view.findViewById(R.id.pop_action_underline);
//        popactiontextcolor = (ImageButton) view.findViewById(R.id.pop_action_txt_color);
//        popactiontextsize = (ImageButton) view.findViewById(R.id.pop_action_txt_size);
//        popactioninsertimage = (ImageButton) view.findViewById(R.id.pop_action_insert_image);
//        popactioninsertvideo = (ImageButton) view.findViewById(R.id.pop_action_insert_video);
//        popivdeletegarbage = (ImageView) view.findViewById(R.id.pop_action_garbage);
//
//        popactionbold.setOnClickListener(new onPopActionListener());
//        popactionunderline.setOnClickListener(new onPopActionListener());
//        popactiontextsize.setOnClickListener(new onPopActionListener());
//        popactiontextcolor.setOnClickListener(new onPopActionListener());
//        popactioninsertimage.setOnClickListener(new onPopActionListener());
//        popactioninsertvideo.setOnClickListener(new onPopActionListener());
//        popivdeletegarbage.setOnClickListener(new onPopActionListener());
//        if (popupwindowEditortoolbar == null) {
//            popupwindowEditortoolbar = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
//        popupwindowEditortoolbar.setClippingEnabled(true);
//        popupwindowEditortoolbar.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupwindowEditortoolbar.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//        popupwindowEditortoolbar.showAtLocation(getCurrentFocus(), Gravity.BOTTOM, 0, keyboardHeight);
//    }


    /**
     * 选择图片插入
     */
    public void InsertImage() {

        View view = View.inflate(PostActiveActivity.this, R.layout.pop_insertpicture, null);
        view.findViewById(R.id.tv_photoalbum).setOnClickListener(new InsertImageListener());
        view.findViewById(R.id.tv_takepicture).setOnClickListener(new InsertImageListener());
        view.findViewById(R.id.tv_cancelselect).setOnClickListener(new InsertImageListener());
        popupwindowInsertImage = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupwindowInsertImage.setTouchable(true);
        popupwindowInsertImage.setBackgroundDrawable(getResources().getDrawable(R.color.all_bg));
        popupwindowInsertImage.showAtLocation(getCurrentFocus(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.3f);
        popupwindowInsertImage.setOnDismissListener(new PoponDismissListener());
    }

    /**
     * 设置字体大小
     */
    private void selectorTextSize(View v) {
        //设置字体
        View view = View.inflate(PostActiveActivity.this, R.layout.pop_textsize, null);
        TextView tv16 = (TextView) view.findViewById(R.id.textsize16);
        TextView tv14 = (TextView) view.findViewById(R.id.textsize14);
        TextView tv12 = (TextView) view.findViewById(R.id.textsize12);

        tv16.setOnClickListener(new OnTextSizeChange());
        tv14.setOnClickListener(new OnTextSizeChange());
        tv12.setOnClickListener(new OnTextSizeChange());

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        popupwindowTextSize = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupwindowTextSize.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_edittextcolor));
        popupwindowTextSize.setTouchable(true);
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        popupwindowTextSize.showAtLocation(getCurrentFocus(), Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);

    }

    private void popSelectorTextSize(View v) {
        //设置字体
        View view = View.inflate(PostActiveActivity.this, R.layout.pop_textsize, null);
        TextView tv16 = (TextView) view.findViewById(R.id.textsize16);
        TextView tv14 = (TextView) view.findViewById(R.id.textsize14);
        TextView tv12 = (TextView) view.findViewById(R.id.textsize12);

        tv16.setOnClickListener(new onPopTextSizeChange());
        tv14.setOnClickListener(new onPopTextSizeChange());
        tv12.setOnClickListener(new onPopTextSizeChange());

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        popupwindowTextSize = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupwindowTextSize.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_edittextcolor));
        popupwindowTextSize.setTouchable(true);
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        popupwindowTextSize.showAtLocation(getCurrentFocus(), Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);

    }

    /**
     * 裁剪图片
     */
    private void cutOutPicture() {
        final android.app.ProgressDialog dialog = android.app.ProgressDialog.show(
                PostActiveActivity.this, null, "请稍候…", true, false);
        cropView.setVisibility(View.GONE);
        btnlay.setVisibility(View.GONE);

        new Thread() {
            public void run() {
                croppedBitmap = cropView.getOutput();
                mPicPath = getPictureName();
                FileUtil.saveBitmapToSdcard(mPicPath, croppedBitmap);
                SharedPreferences.Editor editor = getSharedPreferences("temp", Context.MODE_PRIVATE).edit();
                editor.putString("image", mPicPath);
                editor.commit();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Bitmap bitmap = BitmapFactory.decodeFile(mPicPath);
                        if (bitmap != null) {
                            Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                            bitmap.recycle();
                            img_photo.setImageBitmap(smallBitmap);
                        }

                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });

                if (null != croppedBitmap && !croppedBitmap.isRecycled()) {
                    croppedBitmap.recycle();
                    croppedBitmap = null;
                }
            }
        }.start();
    }

    /**
     * 设置字体颜色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void selectColor(View v) {

        View view = View.inflate(PostActiveActivity.this, R.layout.pop_listgridview, null);

        view.findViewById(R.id.viewtxtcolor1).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor2).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor3).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor4).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor5).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor6).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor7).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor8).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor9).setOnClickListener(new txtcolorclick());
        view.findViewById(R.id.viewtxtcolor10).setOnClickListener(new txtcolorclick());

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        popupwindowColor = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupwindowColor.setTouchable(true);
        popupwindowColor.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_edittextcolor));
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        popupwindowColor.showAtLocation(getCurrentFocus(), Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }


    /**
     * 设置字体颜色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void popSelectColor(View v) {

        View view = View.inflate(PostActiveActivity.this, R.layout.pop_listgridview2, null);

        view.findViewById(R.id.pop_viewtxtcolor1).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor2).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor3).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor4).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor5).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor6).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor7).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor8).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor9).setOnClickListener(new popTxtColorClick());
        view.findViewById(R.id.pop_viewtxtcolor10).setOnClickListener(new popTxtColorClick());

        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        popUpchangeColor = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popUpchangeColor.setTouchable(true);
        popUpchangeColor.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_edittextcolor));
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        popUpchangeColor.showAtLocation(getCurrentFocus(), Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }

    private void reset() {
        cropView.setVisibility(View.GONE);
        btnlay.setVisibility(View.GONE);
        img_photo.setImageBitmap(null);
    }

    /**
     * 根据活动人数对比设置票数
     */
    private int getTicket() {
        String active_num = ed_active_num.getText().toString();
        int num = 0;
        if (!TextUtils.isEmpty(active_num)) {
            try {
                num = Integer.parseInt(active_num);
                int total = 0;
                for (int i = 0; i < list.size(); i++) {
                    TicketAddBean bean = list.get(i);
                    total += bean.getPersonnum();
                }
                if (num > total) {
                    return num - total;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void postActive(String changedHtml) {
        String active_name = ed_active_name.getText().toString();
        String active_address = edtmap.getText().toString();
        String active_start_time = mEdStartTime.getText().toString();
        String active_end_time = mEdEndTime.getText().toString();
        String active_num = ed_active_num.getText().toString();

        if (mUserInfoBean == null) {
            ToastUtil.showShort("请登录用户!");
            return;
        }

        if (TextUtils.isEmpty(active_name)) {
            ToastUtil.showShort("请输入活动名称!");
            return;
        }

        if (TextUtils.isEmpty(active_address)) {
            ToastUtil.showShort("请选择活动地址!");
            return;
        }

        if (TextUtils.isEmpty(active_start_time)) {
            ToastUtil.showShort("请选择活动开始时间!");
            return;
        }

        if (TextUtils.isEmpty(active_end_time)) {
            ToastUtil.showShort("请选择活动结束时间!");
            return;
        }

        if (TimeUtils.minutesBefore(active_start_time, active_end_time)) {
            ToastUtil.showShort("活动结束时间不能在开始时间之前!");
            return;
        }

        if (TextUtils.isEmpty(mPicPath)) {
            ToastUtil.showShort("请选择活动海报!");
            return;
        }

        if (TextUtils.isEmpty(active_num)) {
            ToastUtil.showShort("请输入活动人数!");
            return;
        }

        if (category == 0) {
            ToastUtil.showShort("请选择活动分类!");
            return;
        }

        if (TextUtils.isEmpty(changedHtml)) {
            ToastUtil.showShort("请输入活动详情!");
            return;
        }

        if (list.size() <= 0) {
            ToastUtil.showShort("请添加票务信息");
            return;
        }

        String json = getJosn();

        update();

        String picPath = getPictureName();
        Utiles.log("picPath:" + picPath);
        Bitmap bitmap = BitmapUtil.getimage(mPicPath);
        FileUtil.saveBitmapToSdcard(picPath, bitmap);
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        mProgressDialog.show();
        Utiles.log("发布活动hostid:" + hostid);
        ActiveServiceImpl.getInstance().pushActive(mUserInfoBean.getUserid(),
                hostid, active_name, active_address,
                TimeUtils.getLongTime1(active_start_time),
                TimeUtils.getLongTime1(active_end_time), picPath,
                Integer.parseInt(active_num), theme, style, category, "",
                changedHtml, charge, money, json, new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Utiles.log("请求成功response:" + response);
                        StatusJson statusJson = new StatusJson();
                        Object obj = statusJson.analysisJson2Object(response);
                        if (obj != null) {
                            StatusBean statusBean = (StatusBean) obj;
                            if (statusBean.getCode() == 0) {
                                ToastUtil.showShort("活动发布成功!");
                                imageList.clear();
                                mEditor.setHtml("");
                                for (int i = 0; i < imageList.size(); i++) {
                                    imageList.remove(i);
                                }
                                finish();
                            } else {
                                ToastUtil.showShort("活动发布失败1，请稍后重试!");
                            }
                        } else {
                            ToastUtil.showShort("活动发布失败2，请稍后重试!");
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        mProgressDialog.dismiss();
                        ToastUtil.showShort("活动发布失败3，请稍后重试!");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == LOCAL_PHOTO_ALBUM) { //获取相册
            Uri source = null;
            if (data != null) {
                source = data.getData();
                Utiles.log("source:" + source);
            }
            cropView.setVisibility(View.VISIBLE);
            btnlay.setVisibility(View.VISIBLE);
            cropView.of(source).asSquare().initialize(PostActiveActivity.this);
        } else if (resultCode == RESULT_OK && requestCode == TAKING_PICTURES) {  //拍照
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            } else {
                uri = Uri.fromFile(new File(mPicPath));
            }
            cropView.setVisibility(View.VISIBLE);
            btnlay.setVisibility(View.VISIBLE);
            cropView.of(uri).asSquare().initialize(PostActiveActivity.this);
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {

            if (data != null) {
                String[] photoPaths = data.getStringArrayExtra(PhotoPickerActivity.INTENT_PHOTO_PATHS);
                for (int i = 0; i < photoPaths.length; i++) {
                    Log.i("-------", "图片地址： " + photoPaths[i]);
                    imageList.add(photoPaths[i]);
                    mEditor.insertImage(photoPaths[i], "");
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == EDIT_TAKING_PICTURES) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            } else {
                uri = Uri.fromFile(new File(mPicPath));
            }
            String uriPath = uri.getPath();
            Utiles.log("拍照path：" + uriPath);
            imageList.add(uriPath);
            mEditor.insertImage(uriPath, "");
        }
    }

    private void updateImage() {

        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort("网络无连接，请重试");
        }

        String active_name = ed_active_name.getText().toString();
        String active_address = edtmap.getText().toString();
        String active_start_time = mEdStartTime.getText().toString();
        String active_end_time = mEdEndTime.getText().toString();
        String active_detail = "";
        String active_num = ed_active_num.getText().toString();
        if (TextUtils.isEmpty(content)) {
            active_detail = mEditor.getHtml();
        } else {
            active_detail = content;
        }
        Utiles.log("活动详情：" + active_detail);
        if (TextUtils.isEmpty(active_detail)) {
            ToastUtil.showShort("请输入活动详情!");
            return;
        }
        if (mUserInfoBean == null) {
            ToastUtil.showShort("请登录用户!");
            return;
        }

        if (TextUtils.isEmpty(active_name)) {
            ToastUtil.showShort("请输入活动名称!");
            return;
        }

        if (TextUtils.isEmpty(active_address)) {
            ToastUtil.showShort("请选择活动地址!");
            return;
        }

        if (TextUtils.isEmpty(active_start_time)) {
            ToastUtil.showShort("请选择活动开始时间!");
            return;
        }

        if (TextUtils.isEmpty(active_end_time)) {
            ToastUtil.showShort("请选择活动结束时间!");
            return;
        }

        if (TimeUtils.minutesBefore(active_start_time, active_end_time)) {
            ToastUtil.showShort("活动结束时间不能在开始时间之前!");
            return;
        }

        if (TextUtils.isEmpty(mPicPath)) {
            ToastUtil.showShort("请选择活动海报!");
            return;
        }

        if (TextUtils.isEmpty(active_num)) {
            ToastUtil.showShort("请输入活动人数!");
            return;
        }

        if (category == 0) {
            ToastUtil.showShort("请选择活动分类!");
            return;
        }

        if (list.size() <= 0) {
            ToastUtil.showShort("请添加票务信息");
            return;
        }

        mProgressDialog.show();
        ActiveServiceImpl.getInstance().updateImage(imageList, new StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.showShort("上传失败" + e.getMessage());
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    mProgressDialog.dismiss();
                    StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                    if (statusBean.getCode() == 0) {
                        Utiles.log("response:" + response);
                        List<imageUrlBean> urlBean = JSON.parseArray(statusBean.getData(), imageUrlBean.class);
                        Utiles.log("获取图片数量" + urlBean.size() + "本地图片数量" + imageList.size());
                        if (TextUtils.isEmpty(mEditor.getHtml())) {
                            changedHtml = content;
                        } else {
                            changedHtml = mEditor.getHtml();
                        }
                        int size = imageList.size();

                        Utiles.log("替换前的Html文本:" + changedHtml);
                        if (imageList.size() == urlBean.size()) {
                            ToastUtil.showShort("正在上传图片");
                            for (int i = 0; i < imageList.size(); i++) {
                                size--;
                                Utiles.log("size:" + size);
                                pictureUrl = urlBean.get(i).getUrl();
                                Utiles.log("获取的pictureUrl:" + pictureUrl);
                                changedHtml = changedHtml.replace(imageList.get(i), pictureUrl);
                            }
                            if (size == 0) {
                                ToastUtil.showShort("图片上传成功");
                                postActive(changedHtml);
                            }
                            Utiles.log("替换后的Html：" + changedHtml);

                        }
                    }
                }
            }
        });

    }

    private String getPictureName() {
        String fileRootDir = FileUtil.getRootPath() + "TempImage/";
        if (FileUtil.isFileExist(fileRootDir) == false) {
            FileUtil.createDir(fileRootDir);
        }
        Utiles.log("图片名:" + fileRootDir + UUID.randomUUID().toString() + ".jpg");
        return fileRootDir + UUID.randomUUID().toString() + ".jpg";
    }

    private String getJosn() {
        String json = "";
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            for (int i = 0; i < list.size(); i++) {
                TicketAddBean bean = list.get(i);
                JSONObject ject = new JSONObject();
                ject.put("name", bean.getName());
                ject.put("type", bean.getType());
                ject.put("money", bean.getMoney());
                ject.put("intro", bean.getIntro());
                ject.put("ischeck", bean.isIscheck());
                ject.put("personnum", bean.getPersonnum());
                arr.put(ject);
            }
            obj.put("ticket_list", arr);
            json = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void update() {
        for (int i = 0; i < list.size(); i++) {
            TicketAddBean bean = list.get(i);
            if (bean.getType() == 2) {
                charge = true;
                if (money == 0) {
                    money = bean.getMoney();
                }
                if (money > bean.getMoney()) {
                    money = bean.getMoney();
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Utiles.log("onPause");
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        try {
            mEditor.getClass().getMethod("onPause").invoke(mEditor, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utiles.log("onResume");
        if (popupwindowEditortoolbar != null) {
            Utiles.log("dismiss");
            popupwindowEditortoolbar.dismiss();
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        line_post.addOnLayoutChangeListener(this);
        try {
            mEditor.getClass().getMethod("onResume").invoke(mEditor, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        Utiles.log("onStop");
        super.onStop();
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        SharedUtil.save(PostActiveActivity.this, "recontent", mEditor.getHtml());
        SharedUtil.saveArray(this, imageList);
        if (popupwindowEditortoolbar != null) {
            Utiles.log("dismiss");
            popupwindowEditortoolbar.dismiss();
            line_editortool.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClassConcrete.getInstance().removeObserver(this);
        Utiles.log("onDestroy");

    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean) data;
        if (event.getType() == EventType.MAP_ADDRESS_TYPE) {
            edtmap.setText((String) event.getObj());
        } else if (event.getType() == EventType.ACTIVITE_TICKET_ADD) {
            TicketAddBean bean = (TicketAddBean) event.getObj();
            list.add(bean);
            addLayout();
        }
        return false;
    }

    private void addLayout() {
        llyt_group.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final TicketAddBean bean = list.get(i);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.list_item_ticket, null);
            TextView txtName = (TextView) view.findViewById(R.id.txt_name);
            ImageView img_delete = (ImageView) view
                    .findViewById(R.id.img_delete);
            txtName.setText(bean.getName());
            img_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (list.contains(bean)) {
                        list.remove(bean);
                        addLayout();
                    }
                }
            });

            llyt_group.addView(view);
        }
    }

    @Override
    public void onInputComplete(String url) {
        Utiles.log("视频url:" + url);
        if (!TextUtils.isEmpty(url)) {
            mEditor.insertVideo(url);
        } else {
            ToastUtil.showShort("输入非法");
            return;
        }
        if (popupwindowEditortoolbar != null) {
            popupwindowEditortoolbar.dismiss();
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyboardHeight)) {
//            actionbold.setEnabled(false);
//            actionunderline.setEnabled(false);
//            actiontextcolor.setEnabled(false);
//            actiontextcolor.setEnabled(false);
//            actioninsertimage.setEnabled(false);
//            actioninsertvideo.setEnabled(false);
            line_editortool.setVisibility(View.GONE);
            int heightDifference = oldBottom - bottom;
            popupEditortoolbar(heightDifference);
            Utiles.log("弹出键盘" + "oldBottom：" + oldBottom + "bottom：" + bottom + "oldBottom-bottom：" + (oldBottom - bottom));

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyboardHeight)) {
            if (popupwindowEditortoolbar != null) {
                popupwindowEditortoolbar.dismiss();
                line_editortool.setVisibility(View.VISIBLE);
//                actionbold.setEnabled(true);
//                actionunderline.setEnabled(true);
//                actiontextcolor.setEnabled(true);
//                actiontextcolor.setEnabled(true);
//                actioninsertimage.setEnabled(true);
//                actioninsertvideo.setEnabled(true);
                Utiles.log("收起键盘 " + "高度差：" + (bottom - oldBottom));
            }
        }
    }

    private class PoponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private class txtcolorclick implements OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.viewtxtcolor1:
                    actiontextcolor.setImageResource(R.drawable.red3);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.red3));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor2:
                    actiontextcolor.setImageResource(R.drawable.magenta);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.magenta));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor3:
                    actiontextcolor.setImageResource(R.drawable.purple);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.purple));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor4:
                    actiontextcolor.setImageResource(R.drawable.yellow2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.yellow2));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor5:
                    actiontextcolor.setImageResource(R.drawable.darkgreen);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.darkgreen));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor6:
                    actiontextcolor.setImageResource(R.drawable.young);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.young));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor7:
                    actiontextcolor.setImageResource(R.drawable.blue2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.blue2));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor8:
                    actiontextcolor.setImageResource(R.drawable.darkblue);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.darkblue));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor9:
                    actiontextcolor.setImageResource(R.drawable.gray2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.gray2));
                    popupwindowColor.dismiss();
                    break;
                case R.id.viewtxtcolor10:
                    actiontextcolor.setImageResource(R.drawable.black2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.black2));
                    popupwindowColor.dismiss();
                    break;
            }
        }
    }

    private class OnTextSizeChange implements OnClickListener {
        boolean ischangesize;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textsize16:
                    mEditor.setTextSize(4);
                    ischangesize = !ischangesize;
                    actiontextsize.setImageResource(R.drawable.plus_textsize);
                    popupwindowTextSize.dismiss();
                    break;
                case R.id.textsize14:
                    mEditor.setTextSize(3);
                    ischangesize = !ischangesize;
                    actiontextsize.setImageResource(R.drawable.normal_textsize);
                    popupwindowTextSize.dismiss();
                    break;
                case R.id.textsize12:
                    mEditor.setTextSize(2);
                    ischangesize = !ischangesize;
                    actiontextsize.setImageResource(R.drawable.small_textsize);
                    popupwindowTextSize.dismiss();
                    break;
            }
        }
    }

    private class InsertImageListener implements OnClickListener {
        Intent intent;

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_takepicture:
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mPicPath = getPictureName();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(mPicPath)));
                    startActivityForResult(intent, EDIT_TAKING_PICTURES);
                    popupwindowInsertImage.dismiss();
                    break;
                case R.id.tv_photoalbum:
                    startActivityForResult(new Intent(PostActiveActivity.this, PhotoPickerActivity.class), REQUEST_CODE_PICK_IMAGE);
                    popupwindowInsertImage.dismiss();
                    break;
                case R.id.tv_cancelselect:
                    popupwindowInsertImage.dismiss();
                    break;
            }
        }
    }

    private class onActionListener implements OnClickListener {
        boolean isBold;
        boolean isUnderline;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_bold:
                    mEditor.setBold();
                    actionbold.setImageResource(isBold ? R.drawable.bold_gray : R.drawable.bold_blue);
                    isBold = !isBold;
                    break;
                case R.id.action_underline:
                    mEditor.setUnderline();
                    actionunderline.setImageResource(isUnderline ? R.drawable.underline_gray : R.drawable.underline_blue);
                    isUnderline = !isUnderline;
                    break;

                case R.id.action_txt_color:
                    /**
                     * 选择字体颜色 #C10000 #FF4C40 #920783 #FF9C00 #407600  #408080 #0070C1 #004080 #808080 #000000
                     */
                    selectColor(v);
                    break;
                case R.id.action_txt_size:
                    /**
                     * 选择字体大小 16 14 12
                     */
                    selectorTextSize(v);
                    break;
                case R.id.action_insert_image:  //插入图片
                    InsertImage();
                    break;
                case R.id.action_insert_video:  //插入视频
                    EditDialogFragment dialog = new EditDialogFragment();
                    dialog.show(getFragmentManager(), "EditDialogFragment");
                    break;

                case R.id.iv_delete_garbage:
                    mEditor.setHtml("");
                    imageList.clear();
                    break;
            }
        }
    }

    private class onPopActionListener implements OnClickListener {
        boolean isBold;
        boolean isUnderline;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_action_bold:
                    mEditor.setBold();
                    popactionbold.setImageResource(isBold ? R.drawable.bold_gray : R.drawable.bold_blue);
                    isBold = !isBold;
                    break;
                case R.id.pop_action_underline:
                    mEditor.setUnderline();
                    popactionunderline.setImageResource(isUnderline ? R.drawable.underline_gray : R.drawable.underline_blue);
                    isUnderline = !isUnderline;
                    break;

                case R.id.pop_action_txt_color:
                    /**
                     * 选择字体颜色 #C10000 #FF4C40 #920783 #FF9C00 #407600  #408080 #0070C1 #004080 #808080 #000000
                     */
                    popSelectColor(v);
                    break;
                case R.id.pop_action_txt_size:
                    /**
                     * 选择字体大小 16 14 12
                     */
                    popSelectorTextSize(v);
                    break;
                case R.id.pop_action_insert_image:  //插入图片
                    InsertImage();
                    Utiles.log("选择插入图片");
                    if (imm != null) {
                        Utiles.log("收起键盘");
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    break;
                case R.id.pop_action_insert_video:  //插入视频
                    EditDialogFragment dialog = new EditDialogFragment();
                    dialog.show(getFragmentManager(), "EditDialogFragment");
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    if (popupwindowEditortoolbar != null) {
                        popupwindowEditortoolbar.dismiss();
                    }
                    break;

                case R.id.pop_action_garbage:
                    mEditor.setHtml("");
                    imageList.clear();
                    break;
            }
        }
    }

    private class popTxtColorClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_viewtxtcolor1:
                    popactiontextcolor.setImageResource(R.drawable.red3);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.red3));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor2:
                    popactiontextcolor.setImageResource(R.drawable.magenta);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.magenta));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor3:
                    popactiontextcolor.setImageResource(R.drawable.purple);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.purple));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor4:
                    popactiontextcolor.setImageResource(R.drawable.yellow2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.yellow2));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor5:
                    popactiontextcolor.setImageResource(R.drawable.darkgreen);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.darkgreen));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor6:
                    popactiontextcolor.setImageResource(R.drawable.young);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.young));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor7:
                    popactiontextcolor.setImageResource(R.drawable.blue2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.blue2));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor8:
                    popactiontextcolor.setImageResource(R.drawable.darkblue);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.darkblue));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor9:
                    popactiontextcolor.setImageResource(R.drawable.gray2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.gray2));
                    popUpchangeColor.dismiss();
                    break;
                case R.id.pop_viewtxtcolor10:
                    popactiontextcolor.setImageResource(R.drawable.black2);
                    mEditor.setTextColor(ContextCompat.getColor(PostActiveActivity.this, R.color.black2));
                    popUpchangeColor.dismiss();
                    break;
            }
        }
    }

    private class onPopTextSizeChange implements OnClickListener {
        boolean ischangesize;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textsize16:
                    mEditor.setTextSize(4);
                    ischangesize = !ischangesize;
                    popactiontextsize.setImageResource(R.drawable.plus_textsize);
                    popupwindowTextSize.dismiss();
                    break;
                case R.id.textsize14:
                    mEditor.setTextSize(3);
                    ischangesize = !ischangesize;
                    popactiontextsize.setImageResource(R.drawable.normal_textsize);
                    popupwindowTextSize.dismiss();
                    break;
                case R.id.textsize12:
                    mEditor.setTextSize(2);
                    ischangesize = !ischangesize;
                    popactiontextsize.setImageResource(R.drawable.small_textsize);
                    popupwindowTextSize.dismiss();
                    break;
            }
        }
    }

    public int getViewHeight(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height =view.getMeasuredHeight();
        return height;
    }
}

