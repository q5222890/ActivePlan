package com.xz.activeplan.ui.find.adapter.commonAdapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DiskLruCacheUtil {
    private static final long MAX_SIZE = 20 * 1024 * 1024; // 默认文件大小不超过10M

    /**
     * @param context
     * @param cacheDirectory
     * @return
     */
    public static DiskLruCache open(Context context, String cacheDirectory) {
        try {
            File cacheDir = getDiskLruCacheDir(context, cacheDirectory);
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            return DiskLruCache.open(cacheDir, getAppVersion(context), 1, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param diskLruCache
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmap(DiskLruCache diskLruCache, String imageUrl) {
        imageUrl = getHashKeyByMD5(imageUrl);
        Bitmap bitmap = null;

        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(imageUrl);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(inputStream);
                snapshot.close();
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param diskLruCache
     * @param uniqueNameOrUrl
     * @param bitmap
     */
    public static void putBitmap(DiskLruCache diskLruCache, String uniqueNameOrUrl, Bitmap bitmap) {
        uniqueNameOrUrl = getHashKeyByMD5(uniqueNameOrUrl);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(uniqueNameOrUrl);
            OutputStream outputStream = editor.newOutputStream(0);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uniqueNameOrUrl
     * @return
     */
    public static String getHashKeyByMD5(String uniqueNameOrUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(uniqueNameOrUrl.getBytes());
            return bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return String.valueOf(uniqueNameOrUrl.hashCode());
        }
    }

    /**
     * @param digest
     * @return
     */
    private static String bytesToHexString(byte[] digest) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }

    /**
     * @param context
     * @param cacheDirectory
     * @return
     */
    @SuppressLint("NewApi")
    public static File getDiskLruCacheDir(Context context, String cacheDirectory) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + cacheDirectory);
    }

    /**
     * @param context
     * @return application version code
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     *
     */
    public static class BitmapCache {
        private LruCache<String, Bitmap> mCache;
        private DiskLruCache mDiskLruCache;

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        public BitmapCache(Context context) {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
            mDiskLruCache = DiskLruCacheUtil.open(context, "image");
        }


        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        public Bitmap getBitmap(String s) {
            Bitmap bitmap = null;
            if ((bitmap = mCache.get(s)) == null) {
                bitmap = DiskLruCacheUtil.getBitmap(mDiskLruCache, s);
            }
            return bitmap;
        }

         public void putBitmap(String s, Bitmap bitmap) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                 mCache.put(s, bitmap);
             }
             DiskLruCacheUtil.putBitmap(mDiskLruCache, s, bitmap);
        }
    }
}
