package com.xz.activeplan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings.Global;
/**
 * 文件缓存
 */
public class CacheHelper {

	public static boolean existPath(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static Serializable readCacheObject(Context context, String key) {

		String path = context.getCacheDir() + File.separator + key;

		if (!existPath(path)) {
			return null;
		}

		InputStream in = null;
		ObjectInputStream ois = null;
		try {
			in = new FileInputStream(path);
			ois = new ObjectInputStream(in);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if (e instanceof InvalidClassException) {
				File data = new File(path);
				data.deleteOnExit();
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	public static boolean saveObject(Context context,Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			File f = new File(context.getCacheDir()+File.separator+file);
			if(!f.exists()){
			f.createNewFile();
			}
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	   /** * 清除本应用缓存 * * @param context */
	public static void cleanCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
             Environment.MEDIA_MOUNTED)) {
         deleteFilesByDirectory(context.getExternalCacheDir());
		}
		//cleanCustomCache(Global.APK_DIR);
	}
	
	
	   /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

	
    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
    
    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }


    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
	
}
