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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.recommend.activity.ChooseCityActivity;
import com.xz.activeplan.utils.FileUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;
import com.xz.activeplan.views.CircleImageView;
import com.xz.activeplan.views.SelectPhotoDialog;
import com.xz.activeplan.views.UpdateSexDialog;
import com.xz.activeplan.views.UpdateSexDialog.OnCheckSexListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 个人信息界面activity
 *
 * @author johnny
 *
 */
public class PersonalInfoActivity extends BaseFragmentActivity implements OnClickListener,OnCheckSexListener,ClassObserver {



	public static final int LOCAL_PHOTO_ALBUM = 0; // Local photo album
	public static final int TAKING_PICTURES = 1; // Taking pictures
	public static final int CUT_OUT_THE_PICTURE = 2; // Cut out the picture

	private String mPicPath = "";

	private Bitmap mBitmap;

	private int mOutputX = 300, mOutputY = 300;

	private boolean isCutPicture = true;

	private TextView mTvHeadTitle;

	private CircleImageView mCircleImageView;

	private TextView tvNickName, tvRealName, tvSex, tvCity, tvSignature,
			tvPhoneActivated, tvUpdatePass,tvalipayusername,tvalipayaccount;

	private RelativeLayout rlSex,rlUpdateCity;

	private LinearLayout rlPersonality, rlName,rlzb,rlalipayusername,rlalipayaccount;

	private UserInfoBean mUserInfoBean;

	private SelectPhotoDialog mSelectPhotoDialog;

	private UpdateSexDialog mUpdateSexDialog ;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ClassConcrete.getInstance().addObserver(this) ;
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_personal);
		initViews();
	}

	private void initViews() {
		//返回框背景
		View viewHeald = findViewById(R.id.relativeLayout_toolbar);
		viewHeald.setBackgroundColor(getResources().getColor(R.color.header_blue));
		//返回键
		ImageView imageBlack = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);
		imageBlack.setOnClickListener(this);
		//头部字体
		mTvHeadTitle = (TextView) findViewById(R.id.id_TextViewHeadTitle);
		mTvHeadTitle.setTextColor(getResources().getColor(R.color.white));
		tvNickName = (TextView) findViewById(R.id.tvNickName);
		tvRealName = (TextView) findViewById(R.id.tvRealName);
		tvSex = (TextView) findViewById(R.id.tvSex);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvSignature = (TextView) findViewById(R.id.tvSignature);
		tvPhoneActivated = (TextView) findViewById(R.id.tvPhoneActivated);
		tvUpdatePass = (TextView) findViewById(R.id.tvUpdatePass);
		mCircleImageView = (CircleImageView) findViewById(R.id.ivHead);

		tvalipayusername  = (TextView) findViewById(R.id.tvalipayusername);
		tvalipayaccount = (TextView) findViewById(R.id.tvalipayaccount);

		rlalipayusername = (LinearLayout) findViewById(R.id.rlalipayusername);
		rlalipayaccount = (LinearLayout) findViewById(R.id.rlalipayaccount);

//		rlalipayusername.setOnClickListener(this);
//		rlalipayaccount.setOnClickListener(this);

		findViewById(R.id.rlNickName).setOnClickListener(this);
		rlName = (LinearLayout) findViewById(R.id.rlName);
		rlPersonality = (LinearLayout) findViewById(R.id.rlPersonality);
		rlzb = (LinearLayout)findViewById(R.id.rlzb) ;

		rlSex = (RelativeLayout)findViewById(R.id.rlSex) ;
		rlUpdateCity = (RelativeLayout)findViewById(R.id.rlUpdateCity);

		mTvHeadTitle.setText(getResources().getString(R.string.personal_title));


		tvUpdatePass.setOnClickListener(this);
		rlUpdateCity.setOnClickListener(this);

//		rlName.setOnClickListener(this);
		rlPersonality.setOnClickListener(this);
		rlSex.setOnClickListener(this);

		mCircleImageView.setOnClickListener(this);

		mSelectPhotoDialog = new SelectPhotoDialog(this);
		mSelectPhotoDialog.setOnClickListener(this);

		rlzb.setOnClickListener(this);

		if(SharedUtil.isThirdLogin(this)){
			tvUpdatePass.setVisibility(View.GONE) ;
		}else{
			tvUpdatePass.setVisibility(View.VISIBLE) ;
		}

		initInfo();
	}

	private void initInfo(){
		if (SharedUtil.isLogin(PersonalInfoActivity.this)) {
			mUserInfoBean = SharedUtil.getUserInfo(PersonalInfoActivity.this);
			if (mUserInfoBean != null) {
				if(!TextUtils.isEmpty(mUserInfoBean.getUsername()) && !"null".equals(mUserInfoBean.getUsername())){
					tvNickName.setText(mUserInfoBean.getUsername());
				}else{
					tvNickName.setText("");
				}

				if(!TextUtils.isEmpty(mUserInfoBean.getRealname()) && !"null".equals(mUserInfoBean.getRealname())){
					tvRealName.setText(mUserInfoBean.getRealname());
				}else{
					tvRealName.setText(mUserInfoBean.getPhonenum());
				}

				if (mUserInfoBean.getSex()==1) {
					tvSex.setText("男");
				} else {
					tvSex.setText("女");
				}

				if(!TextUtils.isEmpty(mUserInfoBean.getCity())  && !"null".equals(mUserInfoBean.getCity())){
					tvCity.setText(mUserInfoBean.getCity());
				}else{
					tvCity.setText("");
				}

				if(!TextUtils.isEmpty(mUserInfoBean.getSignature())){
					tvSignature.setText(mUserInfoBean.getSignature());
				}else{
					tvSignature.setText("");
				}

				if(!TextUtils.isEmpty(mUserInfoBean.getPhonenum())){
					tvPhoneActivated.setText(mUserInfoBean.getPhonenum() + "");
				}else{
					tvPhoneActivated.setText("") ;
				}

				if(!TextUtils.isEmpty(mUserInfoBean.getAlipayaccount()) && !"null".equals(mUserInfoBean.getAlipayaccount())){
					tvalipayaccount.setText(mUserInfoBean.getAlipayaccount() + "");
					Utiles.log("支付宝账户："+mUserInfoBean.getAlipayaccount());
				}else{
					tvalipayaccount.setText("") ;
				}

				if(!TextUtils.isEmpty(mUserInfoBean.getAlipayusername())  && !"null".equals(mUserInfoBean.getAlipayusername())){
					tvalipayusername.setText(mUserInfoBean.getAlipayusername() + "");
					Utiles.log("支付宝用户名："+mUserInfoBean.getAlipayusername());
				}else{
					tvalipayusername.setText("") ;
				}

				if (!TextUtils.isEmpty(mUserInfoBean.getHeadurl())  && !"null".equals(mUserInfoBean.getHeadurl())) {
					Picasso.with(PersonalInfoActivity.this).load(mUserInfoBean.getHeadurl()).error(R.drawable.thumb).placeholder(R.drawable.thumb)
							.into(mCircleImageView);
				}else{
					Picasso.with(PersonalInfoActivity.this).load(R.drawable.thumb).placeholder(R.drawable.thumb)
							.into(mCircleImageView);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.id_ImageHeadTitleBlack:
				finish();
				break;
			case R.id.tvUpdatePass:  //修改密码
				intent = new Intent(PersonalInfoActivity.this, FindPasswordActivity.class);
				intent.putExtra("data","update");
				startActivity(intent);
				break;
			case R.id.rlNickName://rlNickName
				intent = new Intent(PersonalInfoActivity.this, UpdateNickNameActivity.class);
				startActivity(intent);
				break;
//			case R.id.rlName://rlNickName
//				intent = new Intent(PersonalInfoActivity.this, UpdateRealNameActivity.class);
//				startActivity(intent);
//				break;
			case R.id.rlPersonality:
				intent = new Intent(PersonalInfoActivity.this, UpdateSignatureActivity.class);
				startActivity(intent);
				break;
//			case R.id.rlalipayusername://rlalipayusername
//				intent = new Intent(PersonalInfoActivity.this, UpdateAlipayuserNameActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.rlalipayaccount:
//				intent = new Intent(PersonalInfoActivity.this, UpdateAlipayAccountActivity.class);
//				startActivity(intent);
//				break;
			case R.id.rlUpdateCity:
				intent = new Intent(PersonalInfoActivity.this,
						ChooseCityActivity.class);
				startActivity(intent);
				break;
			case R.id.ivHead:
				mSelectPhotoDialog.show();
				break;
			case R.id.rlSex:
				mUpdateSexDialog = new UpdateSexDialog(this,mUserInfoBean.getSex()) ;
				mUpdateSexDialog.setOnCheckSexListener(this);
				mUpdateSexDialog.show();
				break;
			case R.id.tvPhotograph:
				mSelectPhotoDialog.dismiss();
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
			case R.id.rlzb:
				intent = new Intent(PersonalInfoActivity.this, HostInfoActivity.class);
				startActivity(intent);
				break;
		}

	}

	private void cutPicture(final Uri uri){

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		List<ResolveInfo> list = this.getPackageManager().queryIntentActivities(intent, 0);
		int size = list.size();
		if (size == 0) {
			ToastUtil.showShort("Can not find image crop app") ;
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
		Log.v("SelPhotoFragment", "onActivityResult");
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
						uploadHeadLoad();
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
					uploadHeadLoad();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			mPicPath="";
			mBitmap=null;
		}
	}

	/**
	 * 头像上传
	 */
	private void uploadHeadLoad() {
		UserInfoServiceImpl.getInstance().uploadHeadImg(mUserInfoBean.getUserid(), mPicPath, new StringCallback() {

			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson() ;
				StatusBean bean = (StatusBean)statusJson.analysisJson2Object(response) ;
				if(bean.getCode() == 0){
					ToastUtil.showShort("头像上传成功") ;
					mUserInfoBean.setHeadurl(bean.getData());
					SharedUtil.saveUserInfo(PersonalInfoActivity.this, mUserInfoBean);
					initInfo();
					ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.LOGIN_NOTIFI_TYPE)) ;
				}else{
					ToastUtil.showShort("头像上传失败") ;
				}
			}

			@Override
			public void onFailure(Request request, IOException e) {
//				Toast.makeText(PersonalInfoActivity.this,"头像上传失败", Toast.LENGTH_LONG).show(); 
				ToastUtil.showShort("头像上传失败") ;
			}
		});
	}

	public void setCutPicture(boolean isCut,int pOutputX,int pOutputY){
		isCutPicture=isCut;
		if (pOutputX>0) {
			mOutputX=pOutputX;
		}
		if (pOutputY>0) {
			mOutputY=pOutputY;
		}
	}

	private String getPictureName() {
		String fileRootDir = FileUtil.getRootPath() + "TempImage/";
		if (FileUtil.isFileExist(fileRootDir)==false) {
			FileUtil.createDir(fileRootDir);
		}
		return fileRootDir + UUID.randomUUID().toString() + ".jpg";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ClassConcrete.getInstance().removeObserver(this) ;
	}

	@Override
	public boolean onDataUpdate(Object data) {
		EventBean event = (EventBean)data ;
		if(event.getType() == EventType.CITY_NOTIFI_TYPE){
			updateCity();
		}else if(event.getType() == EventType.LOGIN_NOTIFI_TYPE){
			initInfo();
		}
		return false;
	}

	/**
	 * 更新城市
	 */
	private void updateCity(){
		UserInfoServiceImpl.getInstance().saveUserInfo(
				mUserInfoBean.getUserid(), mUserInfoBean.getUsername(), SharedUtil.getCity(this),
				mUserInfoBean.getRealname(), mUserInfoBean.getSignature(),
				mUserInfoBean.getPhonenum(), mUserInfoBean.getSex()+"", "",mUserInfoBean.getAlipayaccount(),mUserInfoBean.getAlipayusername(),
				new StringCallback() {

					@Override
					public void onResponse(String response) {
						StatusJson statusJosn = new StatusJson();
						Object obj = statusJosn.analysisJson2Object(response);
						if (obj != null) {
							StatusBean statusBean = (StatusBean) obj;
							if (statusBean.getCode() == 0) {
								ToastUtil.showShort("更改成功") ;
								mUserInfoBean.setCity(SharedUtil.getCity(PersonalInfoActivity.this));
								SharedUtil.saveUserInfo(PersonalInfoActivity.this, mUserInfoBean);
							}
						}
					}

					@Override
					public void onFailure(Request request, IOException e) {
						ToastUtil.showShort("更改失败，请稍后重试") ;
					}
				});
	}

	@Override
	public void onCkeckSex(final int flag) {
		UserInfoServiceImpl.getInstance().saveUserInfo(
				mUserInfoBean.getUserid(), mUserInfoBean.getUsername(), mUserInfoBean.getCity(),
				mUserInfoBean.getRealname(), mUserInfoBean.getSignature(),
				mUserInfoBean.getPhonenum(), flag +"", "",mUserInfoBean.getAlipayaccount(),mUserInfoBean.getAlipayusername(),
				new StringCallback() {

					@Override
					public void onResponse(String response) {
						StatusJson statusJosn = new StatusJson();
						Object obj = statusJosn.analysisJson2Object(response);
						if (obj != null) {
							StatusBean statusBean = (StatusBean) obj;
							if (statusBean.getCode() == 0) {

								ToastUtil.showShort("更改成功!") ;
								mUserInfoBean.setSex(flag);
								if (mUserInfoBean.getSex()==1) {
									tvSex.setText("男");
								} else {
									tvSex.setText("女");
								}
								SharedUtil.saveUserInfo(PersonalInfoActivity.this, mUserInfoBean);
							}
						}
					}

					@Override
					public void onFailure(Request request, IOException e) {
						ToastUtil.showShort("更改失败，请稍后重试!") ;
					}
				});

	}


}
