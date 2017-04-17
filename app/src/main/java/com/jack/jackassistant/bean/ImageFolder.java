package com.jack.jackassistant.bean;

/**
 * Created by xiaofeng on 2017/4/9.
 */

public class ImageFolder {


    private String dirPath;
    private String dirName;
    private int dirCount;
    private String firstImagePath;


    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int lastIndex = this.dirPath.lastIndexOf("/");
        this.dirName = this.dirPath.substring(lastIndex + 1);
    }

    public String getDirName() {
        return dirName;
    }

    public int getDirCount() {
        return dirCount;
    }

    public void setDirCount(int dirCount) {
        this.dirCount = dirCount;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }
}
