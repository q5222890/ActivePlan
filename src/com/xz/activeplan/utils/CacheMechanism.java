package com.xz.activeplan.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;



import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * 缓存资源优化机制
 */
public class CacheMechanism {

    private String TAG = "SD_SAVE";
    private final static int MB = 1024 * 1024;

    // 限制最多缓存30M图片资源
    private final static int CACHE_SIZE = 30 * MB;

    // 当外部存储卡空间少于5M时候清理过时缓存图片
    private final static int FREE_SD_SPACE_NEEDED_TO_CACHE = 5 * MB;

    // 图片资源文件名的头部
    public final static String UM_PIC_HEAD = "UM_BMCACHE_";
    private final static int TIME_OUTOFDATE = 60 * 60 * 24 * 3;

    /**
     * description:计算sdcard上的剩余空间
     * @author chenguanghui
     * @return 剩余空间大小
     */
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFree = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize());
        return (int) sdFree;
    }

    /**
     * description:修改文件的最后修改时间
     * @author chenguanghui
     * @param dir：文件路径
     * @param fileName ：文件名
     */
    private void updateFileTime(String dir, String fileName) {
        File file = new File(dir, fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * description:当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE清理缓存 
     * @author chenguanghui 
     * @throws
     */
    public synchronized void removeCache() {
        try {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }

            String dirPath = Environment.getExternalStorageDirectory()+"/cache";

            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files == null) {
                return;
            }
            int dirSize = 0;
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(UM_PIC_HEAD)) {
                    dirSize += files[i].length();
                }
            }

            if (dirSize > CACHE_SIZE || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
                int removeFactor = (int) ((0.2 * files.length) + 1);
                if (removeFactor > files.length) {
                    return;
                }
                Arrays.sort(files, new FileLastModifSort());
                Log.i(TAG, "Clear some expiredcache files ");
                for (int i = 0; i < removeFactor; i++) {
                    if (files[i].getName().contains(UM_PIC_HEAD)) {
                        files[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "DELETE PIC ERROR!");
        }

    }

    private void removeExpiredCache(String dirPath, String filename) {
        File file = new File(dirPath, filename);
        if (System.currentTimeMillis() - file.lastModified() > TIME_OUTOFDATE) {
            Log.i(TAG, "Clear some expiredcache files ");
            file.delete();
        }
    }


    /**
     * description:根据文件的最后修改时间进行排序
     * @author chenguanghui
     */
    class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
