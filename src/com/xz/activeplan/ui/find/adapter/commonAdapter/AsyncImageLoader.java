package com.xz.activeplan.ui.find.adapter.commonAdapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.xz.activeplan.XZApplication;
import com.xz.activeplan.utils.Utiles;

import java.io.IOException;


/**
 * Created by liaoyuan on 2016/4/7.
 */
public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {
    public static boolean bToRoundCorner = false;
    private static AsyncImageLoader instance = null;
    private ImageView image;
    private Bitmap bitmap;

    public AsyncImageLoader() {
    }

    /**
     * 构造方法，需要把ImageView控件和LruCache 对象传进来
     * @param image 加载图片到此 {@code}ImageView
     */
    public AsyncImageLoader(ImageView image) {
        super();
        this.image = image;
    }

    public static AsyncImageLoader getInstance(){
        if (instance==null){
            instance = new AsyncImageLoader();
        }
        return instance;
    }

    @Override
    public Bitmap doInBackground(String... params) {
        try {
             bitmap = SimpleImageLoader.getInstance().getBitmap(params[0]);
            if (bToRoundCorner == true){
                 bitmap = Utiles.toRoundCorner(bitmap, 8);
            }
            SimpleImageLoader.getInstance().bitmapS= null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        addBitmapToMemoryCache(params[0], bitmap);
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
    //调用LruCache的put 方法将图片加入内存缓存中，要给这个图片一个key 方便下次从缓存中取出来
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
      if (key != null && bitmap!=null) {
           XZApplication.sBitmapCache.putBitmap(key, bitmap);
       }
    }

    /**
     * 清除bitmap
     */
    public void clearBitmap()
    {
        if (null != bitmap && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }
}
