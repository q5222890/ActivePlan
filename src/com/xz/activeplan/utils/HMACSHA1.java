package com.xz.activeplan.utils;

import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * HMAC_SHA1加密
 */
public class HMACSHA1 {

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /*
     * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥
     * @return
     * @throws Exception
     *
      public static String initMacKey() throws Exception {
      //得到一个 指定算法密钥的密钥生成器
      KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME);
      //生成一个密钥
      SecretKey secretKey =keyGenerator.generateKey();
      return null;
      }
     */

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {
        byte[] data=encryptKey.getBytes();
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes();
        //完成 Mac 操作
        return mac.doFinal(text);
    }

    public static  String hmac_sha1(String key, String datas)
     {
     String reString = "";

     try
     {
     byte[] data = key.getBytes("UTF-8");
     //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
     SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
     //生成一个指定 Mac 算法 的 Mac 对象
     Mac mac = Mac.getInstance("HmacSHA1");
     //用给定密钥初始化 Mac 对象
     mac.init(secretKey);

     byte[] text = datas.getBytes("UTF-8");
     //完成 Mac 操作
     byte[] text1 = mac.doFinal(text);

     reString = Base64.encodeToString(text1, Base64.DEFAULT);

     } catch (Exception e)
     {
     // TODO: handle exception
     }

     return reString;
     }

}
