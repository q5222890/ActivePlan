package com.xz.activeplan.ui.personal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oginotihiro.cropview.CropView;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.SelectPhotoDialog;

import java.io.File;
import java.util.UUID;

public class IDcardPhotoActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final int TAKING_PICTURES = 1;  //拍照
    public static final int REQUEST_PICK = 9162;   //获取相册
    private RelativeLayout relative_frontphoto, relative_backphoto;
    private ImageView ivfrontphoto, ivbackphoto;
    private TextView tvsubmit;
    private ImageView headerblack;
    private TextView headertitle;
    private SelectPhotoDialog mSelectPhotoDialog;
    private String mPicPath = "";
    private boolean isFlag ;

    private CropView cropView;
    private LinearLayout btnlay;
    private Button cancelBtn, doneBtn;
    private Bitmap croppedBitmap;
    private Bitmap roundbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_idcard_photo);
        initView();

    }

    private void initView() {

        relative_frontphoto = (RelativeLayout) findViewById(R.id.relative_frontphoto);
        relative_backphoto = (RelativeLayout) findViewById(R.id.relative_backphoto);
        ivfrontphoto = (ImageView) findViewById(R.id.iv_photofront);
        ivbackphoto = (ImageView) findViewById(R.id.iv_photoback);
        tvsubmit = (TextView) findViewById(R.id.tv_submit);
        headerblack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
        headertitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        headertitle.setText(R.string.idcard_photo);

        relative_backphoto.setOnClickListener(this);
        relative_frontphoto.setOnClickListener(this);
        tvsubmit.setOnClickListener(this);
        headerblack.setOnClickListener(this);

        mSelectPhotoDialog = new SelectPhotoDialog(this);
        mSelectPhotoDialog.setOnClickListener(this);

        cropView = (CropView) findViewById(R.id.cropView);
        btnlay = (LinearLayout) findViewById(R.id.btnlay);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        doneBtn = (Button) findViewById(R.id.doneBtn);

        doneBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.relative_frontphoto:
                mSelectPhotoDialog.show();
                isFlag =true;
                break;
            case R.id.relative_backphoto:
                mSelectPhotoDialog.show();
                isFlag = false;
                break;
            case R.id.tv_submit:  //确定时返回图片地址到
                if(ivfrontphoto.getDrawable()==null){
                    ToastUtil.showShort("请上传正面照片");
                    return;
                }
                if(ivbackphoto.getDrawable()==null){
                    ToastUtil.showShort("请上传反面照片");
                    return;
                }
                startActivity(new Intent(this,GoAuthenticationActivity.class));
                finish();
                break;
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.tvPhotograph:  //拍照
                mSelectPhotoDialog.dismiss();
                Intent captureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mPicPath = getPictureName();
                captureintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(mPicPath)));
                startActivityForResult(captureintent, TAKING_PICTURES);
                break;
            case R.id.tvAlbum:       //从相册获取
                mSelectPhotoDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                startActivityForResult(intent, REQUEST_PICK);
                break;
            case R.id.cancelBtn:
                reset();
                break;
            case R.id.doneBtn:
                final ProgressDialog dialog = ProgressDialog.show(IDcardPhotoActivity.this, null, "请稍候…", true, false);
                cropView.setVisibility(View.GONE);
                btnlay.setVisibility(View.GONE);

                new Thread() {
                    public void run() {
                        croppedBitmap = cropView.getOutput();
                        roundbitmap = Utiles.GetRoundedCornerBitmap(croppedBitmap);
                        if(isFlag ==true) {
                            mPicPath = getPictureName();
                            FileUtil.saveBitmapToSdcard(mPicPath, croppedBitmap);
                            SharedPreferences sp = getSharedPreferences("fronttemp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("front", mPicPath);
                            editor.commit();
                        }else{
                            mPicPath = getPictureName();
                            FileUtil.saveBitmapToSdcard(mPicPath, croppedBitmap);
                            SharedPreferences sp = getSharedPreferences("backtemp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("back", mPicPath);
                            editor.commit();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFlag==true) {
                                    ivfrontphoto.setImageBitmap(roundbitmap);
                                    relative_frontphoto.setBackgroundResource(R.color.transparent);
                                } else {
                                    ivbackphoto.setImageBitmap(roundbitmap);
                                    relative_backphoto.setBackgroundResource(R.color.transparent);
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
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK) {
            Uri source=null;
            if(data!=null) {
                source = data.getData();
            }
            cropView.setVisibility(View.VISIBLE);
            btnlay.setVisibility(View.VISIBLE);
            cropView.of(source).asSquare().initialize(IDcardPhotoActivity.this);
        }else if(resultCode ==RESULT_OK &&requestCode ==TAKING_PICTURES){
            Uri uri =null;
            if(data!=null){
                uri =data.getData();
            }else{
                uri =Uri.fromFile(new File(mPicPath));
            }
            cropView.setVisibility(View.VISIBLE);
            btnlay.setVisibility(View.VISIBLE);
            cropView.of(uri).asSquare().initialize(IDcardPhotoActivity.this);
        }

    }

    private String getPictureName() {
        String fileRootDir ="";
        if(isFlag ==true) {
            //如果是正面照片就保存到这个路劲
            fileRootDir = FileUtil.getRootPath() + "frontTempImage/";
        }else{
            fileRootDir = FileUtil.getRootPath() + "backTempImage/";
        }
        if (!FileUtil.isFileExist(fileRootDir)) {  //如果不存在就创建根目录
            FileUtil.createDir(fileRootDir);
        }
        String path =fileRootDir + UUID.randomUUID().toString() + ".jpg";
        Utiles.log("图片名:" + path);
        return path;
    }

    private void reset() {
        cropView.setVisibility(View.GONE);
        btnlay.setVisibility(View.GONE);
        ivfrontphoto.setImageBitmap(null);
        ivbackphoto.setImageBitmap(null);
    }

}
