package com.exscan.online.exscan;

/**
 * Created by sreed on 8/10/2017.
 */
public class FileExtModel {
    String extName;
    int frequency;
    FileExtModel(String name, int feq) {
        extName = name;
        frequency = feq;
    }
    public String getExtName() {
        return extName;
    }
    public int getFrequency() {
        return frequency;
    }
}
