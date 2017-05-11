package com.xz.activeplan.ui.live;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoPublisher {

    public interface EventListener {
        public void onUploadProgress(int taskHandle, long pos, long max);
        public void onUploadComplete(int taskHandle, int errorCode, String url);
        public void onDeleteFile(int errorCode);
        public void onChangeFileInfo(int errorCode);
    }
    
    private volatile static VideoPublisher instance;
    private EventListener mEventListener;

    private final static int MSG_UPLOAD_PROGRESS = 1;
    private final static int MSG_UPLOAD_COMPLETE = 2;
    private final static int MSG_DELETE_FILE = 3;
    private final static int MSG_CHANGE_FILE_INFO = 5;

    private Handler msgHandler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what) {
                case MSG_UPLOAD_PROGRESS: {
                    Bundle data = msg.getData();
                    int taskHandle = data.getInt("taskHandle");
                    long pos = data.getLong("pos");
                    long max = data.getLong("max");
                    
                    if (mEventListener != null) {
                    	mEventListener.onUploadProgress(taskHandle, pos, max);
                    }

                    break;
                }

                case MSG_UPLOAD_COMPLETE: {
                    Bundle data = msg.getData();
                    int taskHandle = data.getInt("taskHandle");
                    int errorCode = data.getInt("errorCode");
                    String url = data.getString("url");

                    if (mEventListener != null) {
                    	mEventListener.onUploadComplete(taskHandle, errorCode, url);
                    }
                    break;
                }

                case MSG_DELETE_FILE: {
                    Bundle data = msg.getData();
                    int errorCode = data.getInt("errorCode");

                    if (mEventListener != null) {
                    	mEventListener.onDeleteFile(errorCode);
                    }
                    break;
                }

                case MSG_CHANGE_FILE_INFO: {
                    Bundle data = msg.getData();
                    int errorCode = data.getInt("errorCode");

                    if (mEventListener != null) {
                    	mEventListener.onChangeFileInfo(errorCode);
                    }
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

    private VideoPublisher() {}

    public static VideoPublisher getInstance() {
        if (instance == null) {
            synchronized (VideoPublisher.class) {
                if (instance == null) {
                    instance = new VideoPublisher();
                }
            }
        }
        return instance;
    }

    public void init() {
        libpubInit();
    }

    public void uninit() {
        libpubUninit();
    }

    public int uploadFile(String filePathName, String uploadToken) {
        return libpubUploadFile(filePathName, uploadToken);
    }
    
    public void pauseUpload(int taskHandle) {
    	libpubUploadPause(taskHandle);
    }

    public void resumeUpload(int taskHandle) {
    	libpubUploadResume(taskHandle);
    }

    public void cancelUpload(int taskHandle) {
    	libpubUploadCancel(taskHandle);
    }

    public void deleteFile(String deleteToken) {
        libpubDeleteFile(deleteToken);
    }

    public void changeFileInfo(String changeToken) {
        libpubChangeFile(changeToken);
    }
    
    public String getErrorMessage(int errorCode) {
    	return libpubGetErrorMsg(errorCode);
    }

    public void setEventListener(EventListener listener) {
    	mEventListener = listener;
    }

    public static void onUploadProgress(int taskHandle, long pos, long max) {
        Bundle data = new Bundle();
        data.putInt("taskHandle", taskHandle);
        data.putLong("pos", pos);
        data.putLong("max", max);

        Message msg = new Message();
        msg.what = MSG_UPLOAD_PROGRESS;
        msg.setData(data);
        VideoPublisher.getInstance().msgHandler.sendMessage(msg);
    }

    public static void onUploadComplete(int taskHandle, int errorCode, String url) {
        Bundle data = new Bundle();
        data.putInt("taskHandle", taskHandle);
        data.putInt("errorCode", errorCode);
        data.putString("url", url);

        Message msg = new Message();
        msg.what = MSG_UPLOAD_COMPLETE;
        msg.setData(data);
        VideoPublisher.getInstance().msgHandler.sendMessage(msg);
    }

    public static void onDeleteFile(int errorCode) {
        Bundle data = new Bundle();
        data.putInt("errorCode", errorCode);

        Message msg = new Message();
        msg.what = MSG_DELETE_FILE;
        msg.setData(data);
        VideoPublisher.getInstance().msgHandler.sendMessage(msg);
    }
    
    public static void onChangeFileInfo(int errorCode) {
        Bundle data = new Bundle();
        data.putInt("errorCode", errorCode);

        Message msg = new Message();
        msg.what = MSG_CHANGE_FILE_INFO;
        msg.setData(data);
        VideoPublisher.getInstance().msgHandler.sendMessage(msg);
    }
    
    static {
        try {
            System.loadLibrary("publish");
        } catch(UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    private native void libpubInit();
    private native void libpubUninit();
    private native int libpubUploadFile(String filePathName, String uploadToken);
    private native void libpubUploadPause(int taskHandle);
    private native void libpubUploadResume(int taskHandle);
    private native void libpubUploadCancel(int taskHandle);
    private native void libpubDeleteFile(String deleteToken);
    private native void libpubChangeFile(String changeToken);
    private native String libpubGetErrorMsg(int errorCode);
}
