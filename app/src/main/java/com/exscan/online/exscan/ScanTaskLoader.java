package com.exscan.online.exscan;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.exscan.online.exscan.controller.ScanController;
import com.exscan.online.exscan.model.FileModel;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sreed on 8/10/2017.
 */

public class ScanTaskLoader extends AsyncTaskLoader<String> {

    public ScanTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public void scan(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File eachFile : files) {
                if(isLoadInBackgroundCanceled()) {
                    return;
                }
                if (eachFile.isFile()) {
                    ScanController.getInstance().getFilesModel().insert(new FileModel(eachFile.getName(), eachFile.length(), eachFile));
// testing backgound cases for recording video
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
                else if (eachFile.isDirectory()) {
                    scan(eachFile);
                }
            }
        }
    }
    @Override
    public String loadInBackground() {
        File folder = Environment.getExternalStorageDirectory();
        scan(folder);
        Log.d("ScanTaskFinshed", "scan finished");
        return "finished";
    }

    @Override
    public void onStopLoading() {
        cancelLoad();
    }
}
