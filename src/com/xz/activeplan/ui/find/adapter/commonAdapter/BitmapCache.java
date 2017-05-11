package com.xz.activeplan.ui.find.adapter.commonAdapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

/**
 * Created by Administrator on 2016/6/2.
 */
public class BitmapCache {
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

    // @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public  Bitmap getBitmap(String s) {
        Bitmap bitmap = null;
        if ((bitmap = mCache.get(s)) == null) {
            bitmap = DiskLruCacheUtil.getBitmap(mDiskLruCache, s);
        }
        return bitmap;
    }

    // @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void putBitmap(String s, Bitmap bitmap) {
        mCache.put(s, bitmap);
        DiskLruCacheUtil.putBitmap(mDiskLruCache, s, bitmap);
    }

}
