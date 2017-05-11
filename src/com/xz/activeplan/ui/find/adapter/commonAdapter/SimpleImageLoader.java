package com.xz.activeplan.ui.find.adapter.commonAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liaoyuan on 2016/4/7.
 */
public class SimpleImageLoader {
    public static SimpleImageLoader instance=null;
    public Bitmap bitmapS=null;
    public BitmapFactory.Options options;

    public static SimpleImageLoader getInstance(){
        if(instance == null){
            instance = new SimpleImageLoader();
        }
        return instance;
    }
    public Bitmap getBitmap(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5 * 1000);
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        if (options==null){
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;//width，hight设为原来的十
        bitmapS =BitmapFactory.decodeStream(is,null,options);
        is.close();
        return bitmapS;
    }
}
