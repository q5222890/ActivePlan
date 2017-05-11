package com.xz.activeplan.ui.personal.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.OrganizersJson;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CustomProgressDialog;
import com.xz.activeplan.views.SelectPhotoDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 修改主办方信息
 *
 * @author johnny
 */
public class HostInfoActivity extends BaseFragmentActivity implements OnClickListener {
    public static final int LOCAL_PHOTO_ALBUM = 0; // Local photo album
    public static final int TAKING_PICTURES = 1; // Taking pictures
    public static final int CUT_OUT_THE_PICTURE = 2; // Cut out the picture

    private String mPicPath = "";  //主办方头像本地地址

    private Bitmap mBitmap;  //

    private int mOutputX = 300, mOutputY = 300;

    private boolean isCutPicture = true;

    private TextView tvHeadTitle;
    private SelectPhotoDialog mSelectPhotoDialog;

    private ImageView img_photo;

    private Button btn_modity_host;
    private EditText edt_hostname, edt_hostcontact, edt_hostphone, edt_hostmail, edt_hostintro, edt_hosturl;

    private UserInfoBean mUserInfoBean;

    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_modify_host);
        initViews();
    }

    private void initViews() {
        mProgressDialog = new CustomProgressDialog(this);
        Utiles.headManager(this,R.string.modifyhostinfo);

        img_photo = (ImageView) findViewById(R.id.img_photo);

        btn_modity_host = (Button) findViewById(R.id.btn_modity_host);
        edt_hostname = (EditText) findViewById(R.id.edt_hostname);
        edt_hostcontact = (EditText) findViewById(R.id.edt_hostcontact);
        edt_hostphone = (EditText) findViewById(R.id.edt_hostphone);
        edt_hostmail = (EditText) findViewById(R.id.edt_hostmail);
        edt_hostintro = (EditText) findViewById(R.id.edt_hostintro);
        edt_hosturl = (EditText) findViewById(R.id.edt_hosturl);


        img_photo.setOnClickListener(this);

        btn_modity_host.setOnClickListener(this);

        mSelectPhotoDialog = new SelectPhotoDialog(this);
        mSelectPhotoDialog.setOnClickListener(this);
        getHostId();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_photo:
                mSelectPhotoDialog.show();
                break;
            case R.id.tvPhotograph://打开相机
                mSelectPhotoDialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mPicPath = getPictureName();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
                startActivityForResult(intent, TAKING_PICTURES);
                break;
            case R.id.tvAlbum://打开相册
                mSelectPhotoDialog.dismiss();
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, LOCAL_PHOTO_ALBUM);
                break;
            case R.id.btn_modity_host:
                modifyHost();
                break;
        }
    }

    private void getHostId() {
        if (SharedUtil.isLogin(this)) {
            mUserInfoBean = SharedUtil.getUserInfo(this);
        } else {
            Toast.makeText(this, "请登录用户", Toast.LENGTH_LONG).show();
            return;
        }
        mProgressDialog.show();
        UserInfoServiceImpl.getInstance().getUserHostId(mUserInfoBean.getUserid(), new StringCallback() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    if (statusBean.getCode() == 0) {
                        OrganizersJson json = new OrganizersJson();
                        OrganizersBean bean = json.parseJson(statusBean.getData());
                        if (bean != null) {
                            fillData(bean);
                        }
                    } else {
                        Toast.makeText(HostInfoActivity.this, "加载失败,请稍后重试", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                mProgressDialog.dismiss();
                Toast.makeText(HostInfoActivity.this, "加载失败,请稍后重试", Toast.LENGTH_LONG).show();
            }
        });

    }


    protected void fillData(OrganizersBean bean) {
        edt_hostname.setText(bean.getHostname());
        edt_hostcontact.setText(bean.getHostcontact());
        edt_hostphone.setText(bean.getHostphone());
        edt_hostmail.setText(bean.getHostmail());
        edt_hostintro.setText(bean.getHostintro());
        edt_hosturl.setText(bean.getHosturl());

//        String hostheadurl = SharedUtil.getHostHeadUrl(this);
//        Utiles.log("hostheadurl:"+hostheadurl);
//        if(!TextUtils.isEmpty(hostheadurl)) {
//            Picasso.with(HostInfoActivity.this).load(new File(hostheadurl)).error(R.drawable.thumb).placeholder(R.drawable.thumb)
//                    .into(img_photo);
//        }
        if (!TextUtils.isEmpty(bean.getHosthearurl())) {
            Picasso.with(HostInfoActivity.this).load(bean.getHosthearurl()).error(R.drawable.thumb).placeholder(R.drawable.thumb)
                    .into(img_photo);
        } else {
            Picasso.with(HostInfoActivity.this).load(R.drawable.thumb).placeholder(R.drawable.thumb)
                    .into(img_photo);
        }
    }

    private void modifyHost() {
        if (!SharedUtil.isLogin(this)) {
            Toast.makeText(this, "请登录用户", Toast.LENGTH_LONG).show();
            return;
        }

        mUserInfoBean = SharedUtil.getUserInfo(this);

        int userid = mUserInfoBean.getUserid();
        String hostname = edt_hostname.getText().toString();
        String hostcontact = edt_hostcontact.getText().toString();
        String hostphone = edt_hostphone.getText().toString();
        String hostmail = edt_hostmail.getText().toString();
        String hostintro = edt_hostintro.getText().toString();
        String hostheadurl = SharedUtil.getHostHeadUrl(this);
        String hosturl = edt_hosturl.getText().toString();

        if (TextUtils.isEmpty(hostname)) {
            ToastUtil.showShort("请输入主办单位");
            return;
        }

        if (TextUtils.isEmpty(hostcontact)) {
            ToastUtil.showShort("请输入选择联系人");
            return;
        }

        if (TextUtils.isEmpty(hostphone)) {
            ToastUtil.showShort("请输入主办方联系号码");
            return;
        }
        Utiles.log("主办方联系号码：hostphone：" + hostphone);
        if (TextUtils.isEmpty(hostmail)) {
            ToastUtil.showShort("请输入主办方邮箱");
            return;
        }
        if (TextUtils.isEmpty(hostintro)) {
            ToastUtil.showShort("请输入主办方简介");
            return;
        }

        Utiles.log("头像：hosthearurl：" + hostheadurl);
        if (TextUtils.isEmpty(hostheadurl)) {
            ToastUtil.showShort("请选择头像");
            return;
        }
        mProgressDialog.show();

        UserInfoServiceImpl.getInstance().modifyHost(userid, hostname, hostcontact, hostphone, hostmail, hostintro, hostheadurl, hosturl, new StringCallback() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                StatusJson statusJson = new StatusJson();
                Object obj = statusJson.analysisJson2Object(response);
                Utiles.log("修改主办方obj:" + obj.toString());
                if (obj != null) {
                    StatusBean statusBean = (StatusBean) obj;
                    Utiles.log("修改主办方statusBean.getCode():" + statusBean.getCode());
                    if (statusBean.getCode() == 0) {
                        ToastUtil.showShort("主办方信息提交成功");
                        finish();
                    } else {
                        ToastUtil.showShort("主办方信息提交失败1，请稍后重试");
                    }
                } else {
                    ToastUtil.showShort("主办方信息提交失败2，请稍后重试");
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                mProgressDialog.dismiss();
                ToastUtil.showShort("主办方信息提交失败3，请稍后重试");
                Utiles.log("错误信息："+e);
            }
        });


    }

    private void cutPicture(final Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        List<ResolveInfo> list = this.getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            ToastUtil.showShort("Can not find image crop app");
            return;
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mOutputX);
        intent.putExtra("outputY", mOutputY);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CUT_OUT_THE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == LOCAL_PHOTO_ALBUM) {
                    if (isCutPicture) {
                        cutPicture(data.getData());
                    } else {
                        Uri originalUri = data.getData();
                        ContentResolver resolver = this.getContentResolver();
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = resolver.query(originalUri, proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        mPicPath = cursor.getString(column_index);
                        initImg();
                    }
                } else if (requestCode == TAKING_PICTURES) {
                    if (isCutPicture) {
                        File temp = new File(mPicPath);
                        cutPicture(Uri.fromFile(temp));
                    }
                } else if (requestCode == CUT_OUT_THE_PICTURE) {
                    mBitmap = data.getParcelableExtra("data");
                    mPicPath = getPictureName();
                    FileUtil.saveBitmapToSdcard(mPicPath, mBitmap);
                    initImg();
                    SharedUtil.saveHostHeadUrl(this,mPicPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPicPath = "";
            mBitmap = null;
        }
    }

    private void initImg() {
        if (!TextUtils.isEmpty(mPicPath)) {
            Picasso.with(HostInfoActivity.this).load(new File(mPicPath)).placeholder(R.drawable.thumb).error(R.drawable.thumb).into(img_photo);
        } else {
            Picasso.with(HostInfoActivity.this).load(R.drawable.thumb).placeholder(R.drawable.thumb)
                    .into(img_photo);
        }
    }

    private String getPictureName() {
        String fileRootDir = FileUtil.getRootPath() + "TempImage/";
        if (FileUtil.isFileExist(fileRootDir) == false) {
            FileUtil.createDir(fileRootDir);
        }
        return fileRootDir + UUID.randomUUID().toString() + ".jpg";
    }

}
