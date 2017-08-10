package com.exscan.online.exscan;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    ListView lstStatus;
    Button btnScan;
    FilesModel filesModel;
    FileListAdapter fileListAdapter;
    ProgressBar pbStatus;
    AsyncTask<String, String, String> newAsyncTask;

    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstStatus = (ListView) findViewById(R.id.lstFiles);
        btnScan = (Button) findViewById(R.id.btnScan);
        pbStatus = (ProgressBar) findViewById(R.id.pbStatus);
        pbStatus.setMax(5);
        btnScan.setOnClickListener(this);
        filesModel = new FilesModel();
        //mock();
        fileListAdapter = new FileListAdapter(this, filesModel.getList());
        lstStatus.setAdapter(fileListAdapter);
        filesModel.addObserver(this);
    }

    public void mock() {
        filesModel.insert(new FileModel("file1", 100, new File("")));
        filesModel.insert(new FileModel("file2", 56, new File("")));
        filesModel.insert(new FileModel("file3", 89, new File("")));
        filesModel.insert(new FileModel("file4", 88, new File("")));
        filesModel.insert(new FileModel("file5", 556, new File("")));
        filesModel.insert(new FileModel("file6", 33, new File("")));
        filesModel.insert(new FileModel("file7", 11, new File("")));
    }

    private void sleepTill(int secs) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scan(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File eachFile : files) {
                if (eachFile.isFile()) {
                    filesModel.insert(new FileModel(eachFile.getName(), eachFile.length(), eachFile));
                }
                else if (eachFile.isDirectory()) {
                    scan(eachFile);
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnScan ) {
            if (((Button)v).getText().toString().equalsIgnoreCase("Scan")) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                final File folder = Environment.getExternalStorageDirectory();// Folder Name

                newAsyncTask = new AsyncTask<String, String, String>() {

                    @Override
                    protected String doInBackground(String... params) {
                        filesModel.clear();
                        publishProgress("processing");
                        scan(folder);
                        return "finished";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        pbStatus.setVisibility(View.GONE);
                        btnScan.setText("Scan");
                        super.onPostExecute(s);
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);
                        pbStatus.setVisibility(View.VISIBLE);
                        pbStatus.setProgress(filesModel.size());
                    }

                    @Override
                    protected void onCancelled(String s) {
                        super.onCancelled(s);
                        pbStatus.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                        pbStatus.setVisibility(View.GONE);
                    }
                };
                newAsyncTask.execute("Update");
                ((Button)v).setText("Stop");
            }
            else {
                newAsyncTask.cancel(true);
                ((Button)v).setText("Scan");
            }
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        if (FilesModel.class.isInstance(o)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fileListAdapter.update(filesModel.getList());
                    lstStatus.invalidate();
                }
            });
        }
    }
}
