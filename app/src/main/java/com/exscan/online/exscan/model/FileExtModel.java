package com.exscan.online.exscan.model;

/**
 * Created by sreed on 8/10/2017.
 */
public class FileExtModel implements IModel {
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

    @Override
    public String getFilename() {
        return extName;
    }

    @Override
    public long getSize() {
        return frequency;
    }
}
