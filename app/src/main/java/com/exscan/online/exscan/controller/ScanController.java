package com.exscan.online.exscan.controller;

import com.exscan.online.exscan.model.FilesModel;

/**
 * Created by sreed on 8/10/2017.
 */
public class ScanController {
    static ScanController instance;
    FilesModel filesModel;
    public static ScanController getInstance() {
        if (instance == null) {
            instance = new ScanController();
        }
        return instance;
    }
    ScanController() {
        filesModel = new FilesModel();
    }

    public FilesModel getFilesModel() {
        return filesModel;
    }


    public void clear() {
        filesModel.clear();
    }

}
