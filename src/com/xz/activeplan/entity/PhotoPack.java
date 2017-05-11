package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by hitomi on 2016/6/14.
 */
public class PhotoPack implements Serializable{

    private String pathName;

    private String title;

    private int fileCount;

    private String firstPhotoPath;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public String getFirstPhotoPath() {
        return firstPhotoPath;
    }

    public void setFirstPhotoPath(String firstPhotoPath) {
        this.firstPhotoPath = firstPhotoPath;
    }

    @Override
    public String toString() {
        return "PhotoPack{" +
                "pathName='" + pathName + '\'' +
                ", title='" + title + '\'' +
                ", fileCount=" + fileCount +
                ", firstPhotoPath='" + firstPhotoPath + '\'' +
                '}';
    }
}
