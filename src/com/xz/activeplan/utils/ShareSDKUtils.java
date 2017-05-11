package com.xz.activeplan.utils;

import android.content.Context;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareSDKUtils {

    /**
     * @param context
     * @param platform   分享平台
     * @param txtContext 分享内容
     * @param imgPath    本地图片路径
     * @param imgUrl     网络图片路径。进行微信朋友圈分享必须要网络图片
     */
    public static void showShare(Context context, String platform, String txtContext, String imgPath, String imgUrl, PlatformActionListener paListener) {
        ShareParams sp = new ShareParams();
        sp.setTitle("约吧互动");
        sp.setTitleUrl("http://www.yueba.cc"); // 标题的超链接
        sp.setText("约吧互动");
        sp.setImageUrl("http://139.196.152.82/tryst_images/logo/icon.png");
        //sp.setSite("发布分享的网站名称");
        //sp.setSiteUrl("发布分享网站的地址");

        Platform p = ShareSDK.getPlatform(platform);
        p.setPlatformActionListener(paListener); // 设置分享事件回调
        // 执行图文分享
        p.share(sp);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle(R.string.share);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(context);
    }

    public static void shareSina(String txtAndUrl,String imageUrl) {

        ShareParams sp = new ShareParams();
        sp.setText(txtAndUrl);  //内容加上链接
        sp.setImageUrl(imageUrl); //图片地址  或者使用本地图片setimagePath

        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.removeAccount(true);
        ShareSDK.removeCookieOnAuthorize(true);
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                ToastUtil.showShort("分享失败");
                Utiles.log("Platform: " + arg0.toString() + " ;arg1: " + arg1 + " ;arg2: " + arg2.getMessage());
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                ToastUtil.showShort("onComplete");
            }

            @Override

            public void onCancel(Platform arg0, int arg1) {
                ToastUtil.showShort("分享被取消");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        weibo.share(sp);
    }

    public static void shareQZone(String title,String titleUrl,String text, String imageUrl) {
        ShareParams sp = new ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(titleUrl); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sp.setSite("约吧互动");
        sp.setSiteUrl("http://www.yueba.cc/activty/activtyinfo/index.html?activeid=1102");

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                ToastUtil.showShort("分享失败");
                Utiles.log("Platform: " + arg0.toString() + " ;arg1: " + arg1 + " ;arg2: " + arg2.getMessage());
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                ToastUtil.showShort("onComplete");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                ToastUtil.showShort("取消分享");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        qzone.share(sp);
    }

    public static void shareWechat(String title,String text,String imageUrl,String url) {
        ShareParams sp = new Wechat.ShareParams();

        sp.setTitle(title); //标题
        sp.setText(text);   //内容
		sp.setImageUrl(imageUrl); //分享的图片
		sp.setUrl(url) ;    //点击跳转的web页
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform qzone = ShareSDK.getPlatform(Wechat.NAME);
        qzone.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                ToastUtil.showShort("分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                ToastUtil.showShort("onComplete");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                ToastUtil.showShort("取消分享");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        qzone.share(sp);
    }

    public static void shareWechatMoments(String title, String imageurl, String url) {

        ShareParams sp = new WechatMoments.ShareParams();

        sp.setTitle(title); //标题
//        sp.setText("移动开发烽火台：Android大咖秀"); //朋友圈不显示此字段
        sp.setImageUrl(imageurl);  //图片url
        sp.setUrl(url);  //点击跳转的链接
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
        wechatMoments.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                ToastUtil.showShort("分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                ToastUtil.showShort("onComplete");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                ToastUtil.showShort("分享被取消");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        wechatMoments.share(sp);
    }

    public static void shareShortMessage(String textandurl){

        ShareParams sp =new ShortMessage.ShareParams();
        sp.setText(textandurl);
        sp.setShareType(Platform.SHARE_TEXT);
        Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
        shortMessage.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtil.showShort("onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtil.showShort("分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {

                ToastUtil.showShort("分享被取消");
            }
        });
        shortMessage.share(sp);
    }


    public static void loginThird(Context context, String platform, PlatformActionListener paListener) {
        Platform plat = ShareSDK.getPlatform(platform);
        plat.setPlatformActionListener(paListener);
        plat.showUser(null);
    }
}
