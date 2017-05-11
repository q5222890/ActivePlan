package com.xz.activeplan.ui.find.adapter.commonAdapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.activeplan.XZApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/2/15.
 * 万能ViewHolder
 */
public class ViewHolder {

    /**
     * 缓存item视图以便复用item view的容器
     */
    private SparseArray<View> mViews;
    private int mPosition;//占时用不上
    private View mConvertView;
    private int t;
    private int i;
    private Context context;
    //    private ImageLoader imageLoader;
    private String path = "";//存储到SD卡
//    private AsyncImageLoader asyncLoader;
    public ViewHolder() {

    }

    private ViewHolder(Context context, ViewGroup parent, int layoutID) {
        this.mViews = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutID, parent, false);
        mConvertView.setTag(this);
    }
    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.context = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 获取ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutID    item视图 布局ID
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutID) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutID);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    /**
     * 通过ViewId获取控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 给TextView赋值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        t++;
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 给控件设置背景
     *
     * @param viewId
     * @param background
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ViewHolder setViewBackground(int viewId, Drawable background) {
        View view = getView(viewId);
        view.setBackground(background);
        return this;
    }

    /**
     * 给图片赋值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 给Bitmap赋值
     *
     * @param viewId
     * @param bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Url访问网络赋值-图片-还未实现
     *
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageURL(int viewId, String url) {
        i++;
        ImageView iv = getView(viewId);
        loadBitmap(url, iv);
        return this;
    }

    /**
     * 根据ImageView加载图片
     *
     * @param iv
     * @param url
     * @return
     */
    public ViewHolder setImageURL(ImageView iv, String url) {
        loadBitmap(url, iv);
        return this;
    }

    /**
     * @param url   所需要加载的图片的url，以String形式传进来，可以把这个url作为缓存图片的key
     * @param image ImageView 控件
     */
       public void loadBitmap(String url, ImageView image) {
        Bitmap bitmap = XZApplication.sBitmapCache.getBitmap(url);
        if (bitmap == null) {
            new AsyncImageLoader(image).execute(url);
        } else {
            image.setImageBitmap(bitmap);
        }
    }

    /** 存储到本地SDk卡
     *
     * @param
     * @param
     * @return
     */
    public void setImageInternal(String path) {
        this.path = path;
    }

    /**
     * 保存图片到SD卡上
     *
     * @param
     * @param
     */
    public void saveFile(Bitmap bitmap, String url) {
        try {
            // 获得文件名字
            final String fileNa = url.substring(url.lastIndexOf("/") + 1,
                    url.length()).toLowerCase();
            File file = new File(path + "/image/" + fileNa);
            // 创建图片缓存文件夹
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                File maiduo = new File(path + "/image");
                // 如果文件夹不存在*/
                if (!maiduo.exists()) {
                    // 按照指定的路径创建文件夹
                    maiduo.mkdirs();
                    // 如果文件夹不存在
                }
                // 检查图片是否存在
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {

        }
    }

}
