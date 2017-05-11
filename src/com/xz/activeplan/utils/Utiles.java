package com.xz.activeplan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.ui.live.rongclound.LiveKit;
import com.xz.activeplan.ui.live.test.LiveChatRoomFragment;
import com.xz.activeplan.ui.live.test.MyRongIM;
import com.xz.activeplan.ui.live.test.VodChatRoomFragment;
import com.xz.activeplan.ui.personal.activity.LoginActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 工具类
 */
public class Utiles {
    public static List<Integer> resI = new ArrayList<>();
    public static Context context;
    public static String DATA = "data";
    public static int x = -1;
    private static BitmapFactory.Options options;
    private static TextView mTvHeadTitle;


    /**
     * 本地保存到手机
     *
     * @param str
     * @param
     * @param
     */
    public static void keepSaveNative(Context mcontext, String str, int mode, String content) {
        try {
            FileOutputStream fileOutputStream = mcontext.openFileOutput(str, mode);
            fileOutputStream.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 日志打印
     */
    public static void log(String s) {
        Log.i("--------------", s);
    }

    /**
     * 跳转---不传值
     */
    public static void skipNoData(Class c) {
        BaseFragmentActivity.activity.startActivity(new Intent(BaseFragmentActivity.activity, c));
    }

    /**
     * 跳转---传值
     */
    public static void skipData(Class c, String data) {
        if (data.length() != 0) {
            BaseFragmentActivity.activity.startActivity(new Intent(BaseFragmentActivity.activity, c).putExtra(DATA, data));
        } else {
            BaseFragmentActivity.activity.startActivity(new Intent(BaseFragmentActivity.activity, c));
        }
    }

    /**
     * 跳转页面
     * @param c   到哪个activity
     * @param data  对象： liveTvBean
     */
    public static void skipActivityForLiveTvBean(Class c, LiveTvBean data) {
        if (data!=null) {
            BaseFragmentActivity.activity.startActivity(new Intent(BaseFragmentActivity.activity, c).putExtra("data",data));
        } else {
            BaseFragmentActivity.activity.startActivity(new Intent(BaseFragmentActivity.activity, c));
        }
    }


    /**
     * 线程休眠
     *
     * @param sleepTime
     */
    public static void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Toast打印
     *
     * @param context
     * @param s
     */
    public static void toast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 本地保存到手机
     *
     * @param BaiJianHistoryName
     * @param
     * @param
     */
    public static void keepSaveNative(Context mcontext, String BaiJianHistoryName, String content) {
        try {

            File file = new File(BaiJianHistoryName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * 得到图片的bitmap
     */
    public static Bitmap getImageBitmap(String imageURL) {
        Bitmap bitmap = null;
        Bitmap sbitmap = XZApplication.sBitmapCache.getBitmap(imageURL);
        if (sbitmap != null) {
            return sbitmap;
        } else {
            // 显示网络上的图片

            HttpURLConnection conn = null;
            InputStream is = null;
            try {
                URL myFileUrl = new URL(imageURL);
                conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                if (options == null) {
                    options = new BitmapFactory.Options();
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = 1;//width，hight设为原来的十
                bitmap = BitmapFactory.decodeStream(is, null, options);
                XZApplication.sBitmapCache.putBitmap(imageURL, bitmap);
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
    /**
     * 弹出键盘
     */
    public static void getKeyBoard(EditText editText) {
        InputMethodManager systemService = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        systemService.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }
    /**
     * 关闭软键盘
     *
     */
    public static void closeKeyBoard(Context context,EditText editText)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken() , 0);
    }

    /**
     *  强制显示或者关闭系统键盘
     */
    public static void getKeyBoard(final EditText txtSearchKey, final String status) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    /**
     * 柔化效果(高斯模糊)(优化后比上面快三倍)
     *
     * @return
     */

    public static Bitmap doBlur(Bitmap sentBitmap, int radius,
                                boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 图片圆角
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 检查网络连接
     */
    public static UserInfoBean getNetworkAndLogin_OK() {
        UserInfoBean userInfoBean = null;
        if (!NetworkInfoUtil.checkNetwork(XZApplication.getInstance())) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return null;
        }
        if (!SharedUtil.isLogin(XZApplication.getInstance())) {
            return null;
        } else {
            userInfoBean = SharedUtil.getUserInfo(XZApplication.getInstance());
        }
        return userInfoBean;
    }


    /**
     * 检查是否登录
     *
     * @param clazz   要跳转的活动类
     * @param objects 参数  (key为 data)
     * @return
     */
    public static UserInfoBean IsLogin(Class clazz, Object... objects) {
        BaseFragmentActivity mcontext = BaseFragmentActivity.activity;
        UserInfoBean mUserInfoBean = SharedUtil.getUserInfo(mcontext);
        Intent intent = null;
        if (SharedUtil.isLogin(mcontext)) {
            intent = new Intent(mcontext, clazz);
            intent.putExtra("data", objects);
            mcontext.startActivity(intent);
            return mUserInfoBean;
        } else {
            intent = new Intent(mcontext, LoginActivity.class);
            mcontext.startActivity(intent);
            return null;
        }
    }

    /**
     * Toast加载失败
     */
    public static void loadFailed() {
        ToastUtil.showShort(BaseFragmentActivity.activity.getResources().getString(R.string.string_FailedToLoad));
    }

    /**
     * 头部管理
     */
    public static void headManager(final BaseFragmentActivity context, int stringId) {
        //返回键
        ImageView imageBlack = (ImageView) context.findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        //头部字体
        mTvHeadTitle = (TextView) context.findViewById(R.id.id_TextViewHeadTitle);
        mTvHeadTitle.setTextColor(context.getResources().getColor(R.color.white));
        mTvHeadTitle.setText(context.getResources().getString(stringId));
    }

    /**
     * 头部管理器（已实现返回键为finish）
     * @param context   context
     * @param headTitle  头部标题名称
     */
    public static void headManager(final BaseFragmentActivity context, String headTitle) {
        //返回键
        ImageView imageBlack = (ImageView) context.findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        //头部字体
        mTvHeadTitle = (TextView) context.findViewById(R.id.id_TextViewHeadTitle);
        mTvHeadTitle.setTextColor(context.getResources().getColor(R.color.white));
        mTvHeadTitle.setText(headTitle);
    }

    /**
     * 头部管理
     */
    public static void headManager(final BaseFragmentActivity context, int stringId, final ActivityResult result) {
        //返回键
        ImageView imageBlack = (ImageView) context.findViewById(R.id.id_ImageHeadTitleBlack);
        imageBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.resultBean();
                context.finish();
            }
        });
        //头部字体
        mTvHeadTitle = (TextView) context.findViewById(R.id.id_TextViewHeadTitle);
        mTvHeadTitle.setTextColor(context.getResources().getColor(R.color.white));
        mTvHeadTitle.setText(context.getResources().getString(stringId));
    }


    /**
     * 设置图片的宽高
     *
     * @param width
     * @param height
     */
    public static void setImageWH(int width, int height, ImageView... image) {
        for (ImageView imageView : image) {
            imageView.setMinimumWidth(width);
            imageView.setMaxWidth(width);
            imageView.setMaxHeight(height);
            imageView.setMinimumHeight(height);
        }
    }

    /**
     * 显示
     */
    public static void setVisibility(View... v) {
        for (View view : v) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏（位置被占用）
     * @param v 要隐藏的控件
     */
    public static void setInVisible(View...v)
    {
        for (View view:v)
        {
            view.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 隐藏(位置没有被占用)
     */
    public static void setGone(View... v) {
        for (View view : v) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 改变字体颜色
     */
    public static void setTextColor(TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(BaseFragmentActivity.activity.getResources().getColor(resI.get(i)));
        }
    }

    /**
     * 改变字体颜色
     */

    public static void setImageSrc(ImageView... iv) {
        for (int i = 0; i < resI.size(); i++) {
            iv[i].setImageDrawable(BaseFragmentActivity.activity.getResources().getDrawable(resI.get(i)));
        }
    }

    /**
     * 改变字体颜色
     */

    public static void setTextSrc(TextView... tv) {
        for (int i = 0; i < resI.size(); i++) {
            tv[i].setText(BaseFragmentActivity.activity.getResources().getString(resI.get(i)));
        }
    }

    /***
     * 添加所有的int行
     *
     * @param x
     */
    public static void intRes(int... x) {
        resI.clear();
        for (int i = 0; i < x.length; i++) {
            resI.add(x[i]);
        }
    }

    /**
     * 判断是不是数字，中文，字母
     *
     * @param s
     * @return
     */
    public static int judgingString(String s) {
        //输入的是数字
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            return 1;
        }
        //输入的是字母
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(s);
        if (m.matches()) {
            return 2;
        }
        //输入的是汉字
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(s);
        if (m.matches()) {
            return 3;
        }
        return 0;
    }

    public static String getSubString(String s) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(BaseFragmentActivity.activity.getAssets().open(
                    "1.txt")));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            s = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 设置状态栏透明
     */

    public static void setStatusBar(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 点播视频的聊天室
     *
     * @param context  context
     * @param bean     LiveTvBean基类
     * @param chatRoom 房间号
     */
    public static void joinVodChatRoom(final Context context, final LiveTvBean bean, final String chatRoom) {
            if (!TextUtils.isEmpty(bean.getUrl())) {
                Utiles.x = 3;
                MyRongIM.getInstance().startConversation(context, Conversation.ConversationType.CHATROOM, chatRoom);
                android.os.Message msg = new android.os.Message();
                msg.obj = bean;
                msg.what = VodChatRoomFragment.VOD_CHAT_ROOM_WHAT;
                VodChatRoomFragment.VOD_CHAT_ROOM_HANDLER.sendMessage(msg);
            } else {
                ToastUtil.showShort("视频链接地址无效");
            }
        }

    /**
     * 直播的聊天室
     *
     * @param context  context
     * @param bean     LiveTvBean基类
     * @param chatRoom 房间号
     */
    public static void joinLiveChatRoom(final Context context, final LiveTvBean bean, final String chatRoom) {
        if(SharedUtil.isLogin(context)==true)
        {
        if (!TextUtils.isEmpty(bean.getUrl())) {
            Utiles.x = 0;
            MyRongIM.getInstance().startConversation(context, Conversation.ConversationType.CHATROOM, chatRoom);
            Message msg = new Message();
            msg.obj = bean;
            msg.what = LiveChatRoomFragment.LIVE_CHAT_ROOM_WHAT;
            LiveChatRoomFragment.LIVE_CHAT_ROOM_HANDLER.sendMessage(msg);
        } else {
            ToastUtil.showShort("视频链接地址无效");
        }
        }else
        {
            Utiles.skipNoData(LoginActivity.class);
        }
    }

    /**
     * 解决SocllView 嵌套 ListView
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;//欠款
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /***
     * 生成圆角图片
     * @param bitmap
     * @return
     */
    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 14;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWindwWidth(){
        WindowManager wm = (WindowManager) BaseFragmentActivity.activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
      return width;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getWindwHeight(){
        WindowManager wm = (WindowManager) BaseFragmentActivity.activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getHeight();
        return width;
    }

    public static void connectRongIM(Context context){

        if(SharedUtil.isLogin(context)){
            //正常连接融云
            UserInfoBean userInfoBean =SharedUtil.getUserInfo(context);
            UserInfo userInfo = new UserInfo(userInfoBean.getUserid()+"",userInfoBean.getUsername(), Uri.parse(userInfoBean.getHeadurl()));
            connect(userInfoBean.getToken(), userInfo);
        }else{
            //游客连接登入
            connect(UrlsManager.RONG_DISCONNECT_TOKEN, new UserInfo("00","游客",Uri.EMPTY));
        }
    }

    public static void connect(String token , final UserInfo userInfo ){

        LiveKit.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                log("onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                log("onSuccess:"+s);
                LiveKit.setCurrentUser(userInfo);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                log("onError:"+errorCode);
            }
        });

    }


    public interface ActivityResult {
        void resultBean();
    }

}