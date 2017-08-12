package com.exscan.online.exscan.model;

import java.io.File;

/**
 * Created by sreed on 8/9/2017.
 */
public class FileModel  implements IModel {
    long size;
    String filename;
    File file;

    FileModel() {
        size = 0;
        filename = "";
    }
    public FileModel(String afilename, long s, File afile) {
        filename = afilename;
        size = s;
        file = afile;
    }
    void setSize(long i) {
        size = i;
    }
    public long getSize() {
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
