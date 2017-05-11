package com.xz.activeplan.ui.recommend.activity;

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
import com.xz.activeplan.utils.NetworkInfoUtil;
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
 * 填写主办方信息
 * @author johnny
 *
 */
public class ModifyHostInfoActivity extends BaseFragmentActivity  implements OnClickListener {
	public static final int LOCAL_PHOTO_ALBUM = 0; // Local photo album
	public static final int TAKING_PICTURES = 1; // Taking pictures
	public static final int CUT_OUT_THE_PICTURE = 2; // Cut out the picture
	
	private String mPicPath = "";
	
	private Bitmap mBitmap;
	
	private int mOutputX = 300, mOutputY = 300;

	private boolean isCutPicture = true;
	
	private ImageView iv_datails_back;
	private TextView tvHeadTitle ;
	private SelectPhotoDialog mSelectPhotoDialog;
	
	private ImageView img_photo ;
	
	private Button btn_modity_host;
	private EditText edt_hostname,edt_hostcontact,edt_hostphone,edt_hostmail,edt_hostintro,edt_hosturl;
	
	private UserInfoBean mUserInfoBean ;
	
	private CustomProgressDialog mProgressDialog ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_modify_host) ;
		initViews();
	}
	
	private void initViews(){
		mProgressDialog = new CustomProgressDialog(this) ;
		
		iv_datails_back = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		tvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		
		img_photo = (ImageView)findViewById(R.id.img_photo) ;
		
		btn_modity_host = (Button)findViewById(R.id.btn_modity_host) ;
		edt_hostname = (EditText)findViewById(R.id.edt_hostname) ;
		edt_hostcontact = (EditText)findViewById(R.id.edt_hostcontact) ;
		edt_hostphone = (EditText)findViewById(R.id.edt_hostphone) ;
		edt_hostmail = (EditText)findViewById(R.id.edt_hostmail) ;
		edt_hostintro = (EditText)findViewById(R.id.edt_hostintro) ;
		edt_hosturl = (EditText)findViewById(R.id.edt_hosturl) ;
		
		tvHeadTitle.setText("填写主办方信息");
		iv_datails_back.setOnClickListener(this);
		
		img_photo.setOnClickListener(this) ;
		
		btn_modity_host.setOnClickListener(this) ;
		
		mSelectPhotoDialog = new SelectPhotoDialog(this);
		mSelectPhotoDialog.setOnClickListener(this);

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		case R.id.img_photo:
			mSelectPhotoDialog.show();
			break;
		case R.id.tvPhotograph:
			mSelectPhotoDialog.dismiss();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mPicPath = getPictureName();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
			startActivityForResult(intent, TAKING_PICTURES);
			break;
		case R.id.tvAlbum:
			mSelectPhotoDialog.dismiss();
			intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, LOCAL_PHOTO_ALBUM);
			break;
		case R.id.btn_modity_host:
			modifyHost();
			break;
		}
	}
	

	private void modifyHost() {
		if(!SharedUtil.isLogin(this)){
			ToastUtil.showShort("请登录用户!") ;
			return;
		}

		mUserInfoBean = SharedUtil.getUserInfo(this) ;
		
		int userid = mUserInfoBean.getUserid() ;		
		String hostname = edt_hostname.getText().toString() ;	
		String hostcontact = edt_hostcontact.getText().toString() ;			
		String hostphone = edt_hostphone.getText().toString();			
		String hostmail	= edt_hostmail.getText().toString() ;		
		String hostintro = edt_hostintro.getText().toString();			
		String hostheadurl = SharedUtil.getHostHeadUrl(this);
		String hosturl	= edt_hosturl.getText().toString();
		Utiles.log("主办方头像hosthearurl:"+hostheadurl);
		if(TextUtils.isEmpty(hostname)){
			ToastUtil.showShort("请输入主办单位!") ;
			return;
		}
		
		if(TextUtils.isEmpty(hostcontact)){
			ToastUtil.showShort("请输入选择联系人!") ;
			return;
		}
		
		if(TextUtils.isEmpty(hostphone)){
			ToastUtil.showShort("请输入主办方联系号码!") ;
			return;
		}
		
		if(TextUtils.isEmpty(hostmail)){
			ToastUtil.showShort("请输入主办方邮箱!") ;
			return;
		}
		
		if(TextUtils.isEmpty(hostintro)){
			ToastUtil.showShort("请输入主办方简介!") ;
			return;
		}

		if(TextUtils.isEmpty(hostheadurl)){
			ToastUtil.showShort("请选择头像!") ;
			return;
		}

		Utiles.log("头像URL：hosthearurl："+hostheadurl);

		if(!NetworkInfoUtil.checkNetwork(this)){
			ToastUtil.showShort("网络无连接，请检查网络!") ;
		}
		mProgressDialog.show();
		UserInfoServiceImpl.getInstance().modifyHost(userid, hostname, hostcontact, hostphone, hostmail, hostintro, hostheadurl, hosturl, new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						OrganizersJson json = new OrganizersJson();
						OrganizersBean bean = json.parseJson(statusBean.getData()) ;
						if(bean != null){
							Intent intent = new Intent(ModifyHostInfoActivity.this,
									PostActiveActivity.class);
							intent.putExtra("hostid", bean.getHostid()) ;
							startActivity(intent);
						}
					}else{
						ToastUtil.showShort("主办方信息提交失败，请稍后重试!") ;
					}
				}else{
					ToastUtil.showShort("主办方信息提交失败，请稍后重试!") ;
				}
				mProgressDialog.dismiss();
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				mProgressDialog.dismiss() ;      
				ToastUtil.showShort("主办方信息提交失败，请稍后重试!") ;
			}
		}) ;
		
	}

	private void cutPicture(final Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");	
		List<ResolveInfo> list = this.getPackageManager().queryIntentActivities(intent, 0);
		int size = list.size();
		if (size == 0) {
//			ToastUtil.show("主办方信息提交失败，请稍后重试!", 1) ;
			return;
		}
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX",1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", mOutputX);
		intent.putExtra("outputY", mOutputY);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CUT_OUT_THE_PICTURE); 
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode==Activity.RESULT_OK) {
				if(requestCode == LOCAL_PHOTO_ALBUM){
					if (isCutPicture) {
						cutPicture(data.getData());
					}else {
						Uri originalUri = data.getData();
						ContentResolver resolver = this.getContentResolver();
						String[] proj = {MediaStore.Images.Media.DATA};
						Cursor cursor = resolver.query(originalUri, proj, null, null, null);
						int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						mPicPath=cursor.getString(column_index);
						initImg();
						Utiles.log("图片地址mPicpath："+mPicPath);
					}
				}else if(requestCode == TAKING_PICTURES) {
					if (isCutPicture) {
						File temp = new File(mPicPath);
						cutPicture(Uri.fromFile(temp));
					}
				} else if(requestCode==CUT_OUT_THE_PICTURE){
					mBitmap = data.getParcelableExtra("data");
					mPicPath=getPictureName();
					FileUtil.saveBitmapToSdcard(mPicPath, mBitmap);
					initImg();
					Utiles.log("图片地址mPicpath："+mPicPath);
					SharedUtil.saveHostHeadUrl(ModifyHostInfoActivity.this,mPicPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mPicPath="";
			mBitmap=null;
		}
	}
	
	private void initImg(){
		if (!TextUtils.isEmpty(mPicPath)) {
			Picasso.with(ModifyHostInfoActivity.this).load(new File(mPicPath)).placeholder(R.drawable.thumb).error(R.drawable.thumb).into(img_photo);
		}
		Utiles.log("图片地址mPicpath："+mPicPath);
	}
	
	private String getPictureName() {
		String fileRootDir = FileUtil.getRootPath() + "TempImage/";
		if (FileUtil.isFileExist(fileRootDir)==false) {
			FileUtil.createDir(fileRootDir);
		}
		return fileRootDir + UUID.randomUUID().toString() + ".jpg";
	}
	
}
