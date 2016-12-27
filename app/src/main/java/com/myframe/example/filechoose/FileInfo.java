package com.myframe.example.filechoose;

import java.io.Serializable;

/**
 * Created by zhyling on 2016/3/14.
 */
public class FileInfo implements Serializable {
    private boolean isDirectory;//文件类型
    private String fileName;//文件名
    private String filePath;//文件路径

    public FileInfo(String filePath, String fileName, boolean isDirectory) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.isDirectory = isDirectory;
    }

    public boolean isTextFile() {
        if (fileName.lastIndexOf(".") < 0) {//无后缀
            return false;
        }
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (!isDirectory() && fileSuffix.toLowerCase().equals(".txt")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileInfo [isDirectory=" + isDirectory + ", fileName=" + fileName + ", filePath=" + filePath + "]";
    }
}
