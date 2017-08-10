package com.exscan.online.exscan;

import java.io.File;

/**
 * Created by sreed on 8/9/2017.
 */
public class FileModel {
    long size;
    String filename;
    File file;

    FileModel() {
        size = 0;
        filename = "";
    }
    FileModel(String afilename, long s, File afile) {
        filename = afilename;
        size = s;
        file = afile;
    }
    void setSize(long i) {
        size = i;
    }
    long getSize() {
        return size;
    }

    public String getFilename() {
        return filename;
    }

    public File getFile() {
        return file;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
