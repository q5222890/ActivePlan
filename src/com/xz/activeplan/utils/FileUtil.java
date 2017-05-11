/**
 * Copyright (c) 2012 SXH Hedoo Studio.
 *
 * @Description FileUtil
 * @FileName FileUtil.java
 * @author kenny
 * @data 2012-8-28
 * @version 1.0
 */
package com.xz.activeplan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FileUtil <T>{
    private static final int FILE_SIZE = 1024;
    private static final String FILE_DIR = "";
    private static Date date_start = null;
    private static Date date_end = null;
    private static String FILE_PATH;
    private static SimpleDateFormat sdf;

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FILE_PATH = Environment.getExternalStorageDirectory() + "/";
        } else {
            FILE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/com.xz.activeplan/file/";
        }
    }

    /**
     * 删除文件
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }


    /**
     * 在SD卡或手机内存上删除文件
     */
    public static boolean deleteFilePath(String fileName) {
        Boolean blnResult = true;
        if (fileName.indexOf(FILE_PATH) == -1)
            fileName = FILE_PATH + fileName;
        if (isFileExist(fileName)) {
            File file = new File(fileName);
            try {
                blnResult = file.delete();
            } catch (Exception e) {
                blnResult = false;
            }
        }
        return blnResult;
    }

    /**
     * 在SD卡或手机内存上创建目录，如存在就删除原目录，再创建
     */
    public static File createDir(String dirName) {
        File dir = null;
        try {
            if (dirName.indexOf(FILE_PATH) == -1)
                dirName = FILE_PATH + dirName;
            dir = new File(dirName);
            if (dir.exists())
                dir.delete();
            dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }

    /**
     * 判断SD卡或手机内存上的文件夹是否存在
     */
    public static boolean isFileExist(String fileName) {
        if (fileName.indexOf(FILE_PATH) == -1)
            fileName = FILE_PATH + fileName;
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 获取文件夹下面的路径
     */
    public static File getFileAbsolute(String fileName) {
        if (fileName.indexOf(FILE_PATH) == -1)
            fileName = FILE_PATH + fileName;
        File file = new File(fileName);
        return file;
    }

    /**
     * 获取存储设备根目录
     */
    public static String getRootPath() {
        return FILE_PATH;
    }

    /**
     * Bitmap保存成Jpeg保存到SdCard
     */
    public static void saveBitmapToSdcard(String filePath, Bitmap img) {
        try {
            if (filePath.equals(""))
                return;
            deleteFilePath(filePath);
            File myCaptureFile = new File(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            img.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件，并返回文件路径
     * @param userNo
     *            以用户帐号创建目录
     * @return String 新建文件路径
     * @throws
     */
    public static String createFile(String userNo) {
        Calendar calendar = Calendar.getInstance();
        Random ran = new Random(100000);
        String currentFileDir = FILE_DIR;
        File sampleDir = new File(currentFileDir);
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String currentFilePath = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH) + ""
                + calendar.get(Calendar.DAY_OF_MONTH) + ""
                + calendar.get(Calendar.HOUR_OF_DAY) + ""
                + calendar.get(Calendar.MINUTE) + ""
                + calendar.get(Calendar.SECOND) + ""
                + calendar.get(Calendar.MILLISECOND) + "" + ran.nextInt(100000);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(currentFilePath, ".jpg", sampleDir);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (imageFile == null) {
                imageFile = new File(currentFileDir + currentFilePath + ".jpg");
            }
        }
        return imageFile.getAbsolutePath();
    }

    /**
     * 删除文件夹
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    /**
     * 根据文件路径读取文件
     * @param path
     *            文件路径
     * @return byte[] 文件的byte[]
     * @throws FileNotFoundException
     *             没有该文件
     * @throws IOException
     */
    public static byte[] readFileToByte(String path)
            throws FileNotFoundException, IOException {

        byte buffer[] = null;

        File file = new File(path);
        InputStream is = new FileInputStream(file);
        buffer = new byte[is.available()];
        is.read(buffer);
        is.close();

        return buffer;
    }

    /**
     * 根据时间随机一个数字串
     *
     * @return String 返回随机数
     * @author kenny
     */
    public static String randomNum() {

        Calendar calendar = Calendar.getInstance();
        Random ran = new Random(100000);

        String filePath = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH) + ""
                + calendar.get(Calendar.DAY_OF_MONTH) + ""
                + calendar.get(Calendar.HOUR_OF_DAY) + ""
                + calendar.get(Calendar.MINUTE) + ""
                + calendar.get(Calendar.SECOND) + ""
                + calendar.get(Calendar.MILLISECOND) + "" + ran.nextInt(100000);

        return filePath;
    }

    /**
     * 产生一个随机字符串
     * @param
     * @return
     */
    public static String getRandomStr(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char) result));
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char) result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 返回当前时间：格式：xxxx-xx-xx xx:xx:xx
     *
     * @return String 当前时间
     * @author kenny
     */
    public static String getCurrDateTime() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(date).toString();
    }

    /**
     * 比较两个时间差
     * @param date1
     * @param date2
     * @return 返回两个时间差（单位秒）
     */
    public static long compareDateStr(String date1, String date2) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化为 hhmmss
        long diff = 0;
        try {
            diff = f.parse(date1).getTime() - f.parse(date2).getTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (diff < 0) {
            diff = -diff;
        }
        return diff / 1000;
    }

    public static String getRandomFileName() {

        Calendar calendar = Calendar.getInstance();
        Random ran = new Random(100000);
        return calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH) + ""
                + calendar.get(Calendar.DAY_OF_MONTH) + ""
                + calendar.get(Calendar.HOUR_OF_DAY) + ""
                + calendar.get(Calendar.MINUTE) + ""
                + calendar.get(Calendar.SECOND) + ""
                + calendar.get(Calendar.MILLISECOND) + "" + ran.nextInt(100000);
    }

    /**
     * 在文件里面的指定行插入一行数据
     *
     * @param inFile
     *            文件
     * @param lineno
     *            行号 从0开始
     * @param lineToBeInserted
     *            要插入的数据
     */
    public static void insertStringInFile(File inFile, int lineno, String lineToBeInserted) {

        BufferedReader in = null;
        PrintWriter out = null;
        File outFile = null;
        try {
            File inFilepath = inFile.getParentFile();
            // 临时文件
            outFile = File.createTempFile("name", ".tmp", inFilepath);
            if (!inFile.exists()) {
                inFile.createNewFile();
            }
            // 输入
            FileInputStream fis = new FileInputStream(inFile);
            in = new BufferedReader(new InputStreamReader(fis));

            // 输出
            FileOutputStream fos = new FileOutputStream(outFile);
            out = new PrintWriter(fos);

            // 保存一行数据
            String thisLine;
            // 行号从0开始
            int i = 0;
            while ((thisLine = in.readLine()) != null) {
                // 如果行号等于目标行，则输出要插入的数据
                if (i == lineno) {
                    out.println(lineToBeInserted);
                }
                // 输出读取到的数据
                out.println(thisLine);
                // 行号增加
                i++;
            }

            if (i == 0) {  // 第一次读
                out.println(lineToBeInserted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (inFile != null) {
                // 删除原始文件
                inFile.delete();
                // 把临时文件改名为原文件名
                if (outFile != null)
                    outFile.renameTo(inFile);
            }
        }
    }

    /**
     * 获取链接的后缀名
     *
     * @return
     */
    public static String parseSuffix(String url) {
        String name = "temp.jpg";
        int index = url.lastIndexOf("/");
        if (index != -1) {
            try {
                name = url.substring(index + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return name;
    }

    /**
     * 获取两个日期之间的间隔天数
     * @return
     */
    public static int getGapCount(String endDate) {
       // public static int getGapCount(Date startDate, Date endDate) {
        if (sdf==null) {
            Utiles.log(" date = null");
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            date_start = sdf.parse(endDate);
            date_end = sdf.parse(getCurrDateTime());
        } catch (ParseException e) {
        }
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string,String symbol){
        String[] strings = null;
         strings = string.split(symbol);
        return strings;
    }

    /**
     * 读取表情配置文件
     * @param context
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji.txt");//文件名称
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文件保存到sd上
     */
    public void saveBeanToSdcard(File file, T object, String s) {
        FileOutputStream fos = null;
        try {
            if(!file.exists()){
                    file.createNewFile();
            }
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);//
            fos.close(); //
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
