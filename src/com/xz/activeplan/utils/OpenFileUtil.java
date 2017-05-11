package com.xz.activeplan.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OpenFileUtil {
	
	public static Intent getAllIntent(String paramString) {
		Intent localIntent = new Intent();
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setAction("android.intent.action.VIEW");
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "*/*");
		return localIntent;

	}

	public static Intent getApkFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setAction("android.intent.action.VIEW");
		localIntent.setDataAndType(Uri.parse("file://" + paramString), "application/vnd.android.package-archive");
		return localIntent;
	}

	public static Intent getAudioFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		localIntent.putExtra("oneshot", 0);
		localIntent.putExtra("configchange", 0);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "audio/*");
		return localIntent;
	}

	public static Intent getChmFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "application/x-chm");
		return localIntent;
	}

	public static Intent getExcelFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "application/vnd.ms-excel");
		return localIntent;
	}

	public static Intent getHtmlFileIntent(String paramString) {
		Uri localUri = Uri.parse(paramString).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content")
				.encodedPath(paramString).build();
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.setDataAndType(localUri, "text/html");
		return localIntent;
	}

	public static Intent getImageFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "image/*");
		return localIntent;
	}

	public static Intent getPdfFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "application/pdf");
		return localIntent;
	}

	public static Intent getPptFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "application/vnd.ms-powerpoint");
		return localIntent;
	}

	public static Intent getTextFileIntent(String paramString, boolean paramBoolean) {

		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean)
			localIntent.setDataAndType(Uri.parse(paramString), "text/plain");
		else {
			localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "text/plain");
		}

		return localIntent;
	}

	public static Intent getVideoFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "video/*");
		return localIntent;
	}

	public static Intent getWordFileIntent(String paramString) {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.addCategory("android.intent.category.DEFAULT");
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(new File(paramString)), "application/msword");
		return localIntent;
	}

	public static void openFile(Context context, String paramString) {

		try {
			File localFile = new File(paramString);
			Intent localIntent = null;
			if (!localFile.exists()) {
				localIntent = getAllIntent(paramString);
			} else {
				String str = localFile.getName()
						.substring(1 + localFile.getName().lastIndexOf("."), localFile.getName().length()).toLowerCase();

				if ((str.equals("m4a")) || (str.equals("mp3")) || (str.equals("mid")) || (str.equals("xmf"))
						|| (str.equals("ogg")) || (str.equals("wav")))
					localIntent = getAudioFileIntent(paramString);
				if ((str.equals("3gp")) || (str.equals("mp4")) || (str.equals("flv")) || (str.equals("mpg"))
						|| (str.equals("rm")))
					localIntent = getVideoFileIntent(paramString);
				if ((str.equals("jpg")) || (str.equals("gif")) || (str.equals("png")) || (str.equals("jpeg"))
						|| (str.equals("bmp")))
					localIntent = getImageFileIntent(paramString);
				if (str.equals("apk"))
					localIntent = getApkFileIntent(paramString);
				if (str.equals("ppt"))
					localIntent = getPptFileIntent(paramString);
				if (str.equals("xls"))
					localIntent = getExcelFileIntent(paramString);
				if (str.equals("doc"))
					localIntent = getWordFileIntent(paramString);
				if (str.equals("pdf"))
					localIntent = getPdfFileIntent(paramString);
				if (str.equals("chm"))
					localIntent = getChmFileIntent(paramString);
				if (str.equals("txt"))
					localIntent = getTextFileIntent(paramString, false);
			}
			
			if(localIntent == null){
				localIntent = getAllIntent(paramString);
			}
			if(localIntent != null)
				context.startActivity(localIntent);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 打开链接
	 */
	public static void openUrl(Context context,String url){
		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
