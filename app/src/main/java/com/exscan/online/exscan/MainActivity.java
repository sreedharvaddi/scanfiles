package com.exscan.online.exscan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

import com.exscan.online.exscan.controller.ScanController;
import com.exscan.online.exscan.model.FilesModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer, LoaderManager.LoaderCallbacks<String>{

    private ListView lstStatus;
    private Button btnScan;
    private ProgressBar pbStatus;
    private TextView lblAvgFileSize;
    private ListView lstExtFiles;


    FilesModel filesModel;
    FileListAdapter fileListAdapter;
    LoaderManager mLoaderMgr;

    private int STORAGE_PERMISSION_CODE = 23;
    private FileExtListAdapter fileExtListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstStatus = (ListView) findViewById(R.id.lstFiles);
        btnScan = (Button) findViewById(R.id.btnScan);
        pbStatus = (ProgressBar) findViewById(R.id.pbStatus);
        lblAvgFileSize = (TextView) findViewById(R.id.lblAvgSize);
        lstExtFiles = (ListView) findViewById(R.id.lstExtFiles);

        pbStatus.setMax(5);
        pbStatus.setVisibility(View.GONE);
        btnScan.setOnClickListener(this);
        filesModel = ScanController.getInstance().getFilesModel();
        fileListAdapter = new FileListAdapter(this, filesModel.getList());
        fileExtListAdapter = new FileExtListAdapter(this, filesModel.getFileExtList());
        lstStatus.setAdapter(fileListAdapter);
        lstExtFiles.setAdapter(fileExtListAdapter);
        filesModel.addObserver(this);

        mLoaderMgr = getSupportLoaderManager();

        if (mLoaderMgr.getLoader(1) != null ) {
            mLoaderMgr.initLoader(1, null, this);
            updateUIStatus(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void cleanup() {
        ScanController.getInstance().getFilesModel().deleteObservers();
        if (mLoaderMgr.getLoader(1).isStarted()) {
            mLoaderMgr.getLoader(1).stopLoading();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cleanup();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnScan ) {
            if (((Button)v).getText().toString().equalsIgnoreCase("Scan")) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    return;
                }
                updateUIStatus(true);
                scanStart();
            }
            else if (((Button)v).getText().toString().equalsIgnoreCase("Stop")) {
                stopScan();
                updateUIStatus(false);
            }
        }
    }

    private void scanStart() {
        if (mLoaderMgr.getLoader(1) == null) {
            mLoaderMgr.initLoader(1, null, this);
        }
        else {
            ScanController.getInstance().clear();
            mLoaderMgr.getLoader(1).reset();
            mLoaderMgr.restartLoader(1, null, this);
        }
    }

    private void stopScan() {
        mLoaderMgr.getLoader(1).reset();
        mLoaderMgr.getLoader(1).stopLoading();
    }

    private void updateUIStatus(boolean scanInprogrss) {
        if (scanInprogrss) {
            btnScan.setText("Stop");
            pbStatus.setVisibility(View.VISIBLE);
            lblAvgFileSize.setText("files scanned till "+ScanController.getInstance().getFilesModel().getScannedFilesCount()+",Avarage file size : "+String.format("%.2f KB",ScanController.getInstance().getFilesModel().getAvgFileSize()/1024));
        }
        else {
            btnScan.setText("Scan");
            pbStatus.setVisibility(View.GONE);
            lblAvgFileSize.setText("files scanned till "+ScanController.getInstance().getFilesModel().getScannedFilesCount()+", Avarage file size : "+String.format("%.2f KB",ScanController.getInstance().getFilesModel().getAvgFileSize()/1024));
        }
    }

    @Override
    public void update(final Observable o, Object arg) {
        if (FilesModel.class.isInstance(o)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (((FilesModel)o).isNewEntryUpdate()) {
                        fileListAdapter.update(filesModel.getList());
                        fileExtListAdapter.update(filesModel.getFileExtList());
                        lstStatus.invalidate();
                        lstExtFiles.invalidate();
                    }
                    lblAvgFileSize.setText("files scanned till "+ScanController.getInstance().getFilesModel().getScannedFilesCount()+",Avarage file size : "+String.format("%.2f KB",ScanController.getInstance().getFilesModel().getAvgFileSize()/1024));
                }
            });
        }
    }
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ScanTaskLoader(this);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d("MainActivity", "onLoaderReset()");
        ScanController.getInstance().clear();
        pbStatus.setVisibility(View.GONE);
        btnScan.setText("Scan");
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d("MainActivity", "onLoadFinished");
        updateUIStatus(false);
    }
}