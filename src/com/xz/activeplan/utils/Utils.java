package com.xz.activeplan.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.ui.personal.text.BingPhoneViewDialog;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * 手机号正则表达式
	 **/
	public final static String MOBLIE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|6|7|8]))\\d{8}$";
	/**
	 * description: 转换文件大小
	 * 
	 * @param size
	 *            ：文件大小
	 * @return 文件大小字符串
	 */
	public static String FormatKBToMB(int size) {
		double fSize;
		int nsize = size / 1024;
		String str = "";
		if (nsize >= 1024) {
			fSize = nsize / (1024.0);
			str = new DecimalFormat("0.0").format(fSize) + "M";
		} else {
			fSize = nsize / (1.0);
			str = new DecimalFormat("0.0").format(fSize) + "KB";
		}

		return str;
	}

	/**
	 * 复制
	 * 
	 * @param context
	 * @param text
	 */
	@SuppressLint({ "NewApi", "NewApi" })
	public static void copyText(Context context, String text) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clipData = ClipData.newPlainText("label", text);
			clipboardManager.setPrimaryClip(clipData);
		} else {
			android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setText(text);
		}

	}

	/**
	 * 声音和震动
	 */
	public static void soundAndVibrate(Context context) {
		if (!SystemUtil.isBackgroundRunning(context)) {
			SharedPreferences shared = PrefManager
					.getSharedPreferences(context);
			if (shared.getBoolean("sound", true)) {
				// soundManager.playVoiceFriendevent();
				RingtoneManager
						.getRingtone(
								context,
								RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
						.play();
			}
			if (shared.getBoolean("vibrate", true)) {
				Vibrator vibrator = (Vibrator) context
						.getSystemService(context.VIBRATOR_SERVICE);
				// long [] pattern = {100,400,100,400}; // 停止 开启 停止 开启
				vibrator.vibrate(1000L);
			}
		}
	}

	/**
	 * 发短信
	 */
	public static void sendSMS(Context context, String somebody, String smsBody) {

		Uri smsToUri = Uri.parse("smsto:" + somebody);

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		context.startActivity(intent);

	}

	/**
	 * 验证手机号码
	 * @param
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile(MOBLIE_PHONE_PATTERN);
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	/**
	 * 将手机号码的其中的四位数替换为 *
	 *
	 */
	public static String formatMobileNumber(String mobileNumber)
	{
		String maskNumber = mobileNumber.substring(0,3)+"****"+mobileNumber.substring(8,mobileNumber.length());
		return maskNumber;
	}

	public static String getRealPathFromUri(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * 切换摄像头
	 */
	private void changeCamera(Camera camera, SurfaceView mSurfaceView) {
		int cameraPosition = 1;    // 0代表前置摄像头，1代表后置摄像头
		//切换前后摄像头
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
		for (int i = 0; i < cameraCount;i++) {
			Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
			if (cameraPosition == 1) {
				//现在是后置，变更为前置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					//代表摄像头的方位，CAMERA_FACING_FRONT前置   CAMERA_FACING_BACK后置
					camera.stopPreview();//停掉原来摄像头的预览
					camera.release();//释放资源
					camera = null;//取消原来摄像头
					camera = Camera.open(i);//打开当前选中的摄像头
					try {
						camera.setPreviewDisplay(mSurfaceView.getHolder());//通过surfaceview显示取景画面
					} catch (IOException e) {
						e.printStackTrace();
					}
					camera.startPreview();//开始预览
					cameraPosition = 0;
					break;
				}
			} else {
				//现在是前置， 变更为后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
					camera.stopPreview();//停掉原来摄像头的预览
					camera.release();//释放资源
					camera = null;//取消原来摄像头
					camera = Camera.open(i);//打开当前选中的摄像头
					try {
						camera.setPreviewDisplay(mSurfaceView.getHolder());//通过surfaceview显示取景画面
					} catch (IOException e) {
						e.printStackTrace();
					}
					camera.startPreview();//开始预览
					cameraPosition = 1;
					break;
				}
			}
		}
	}

	/**
	 *  绑定手机号码 dialog
	 */

	public static void showBingPhone(final Context context) {
		View view = View.inflate(context, R.layout.dialog_yesorno,null);
		AlertDialog alertDialog = new AlertDialog(context,view,0.6f).builder();
		view.findViewById(R.id.id_TextViewName).setVisibility(View.VISIBLE);
		alertDialog.setTitle("提示");
		alertDialog.setMsg("您尚未绑定手机,请先绑定");
		view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
		view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
		view.findViewById(R.id.img_line).setVisibility(View.VISIBLE);
		alertDialog.setPositiveButton("去绑定", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BingPhoneViewDialog dialog  = new BingPhoneViewDialog(context,0.85f).builder();
				dialog.show();
			}
		});
		alertDialog.setNegativeButton("取消", new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		alertDialog.show();
	}
}
