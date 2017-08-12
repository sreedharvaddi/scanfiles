package com.exscan.online.exscan.model;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Created by sreed on 8/9/2017.
 */

public class FilesModel extends Observable {

    FileModelOrderdList fileModelOrderList = new FileModelOrderdList(10);
    FileExtModelOrderList fileExtModelOrderList = new FileExtModelOrderList(5);

    HashMap<String, Integer> mostFreequentFileExtensions = new HashMap<>();
    long scannedFilesCount = 0;
    double totalFilesSize = 0;
    private boolean mNewEntryUpdated;

    public boolean isNewEntryUpdate() { return mNewEntryUpdated; }

    public double getAvgFileSize() {
        return totalFilesSize / scannedFilesCount;
    }

    public List<FileExtModel> getFileExtList() {
        return fileExtModelOrderList.getFileExtList();
    }

    public int sizeFileExt() {
        return fileExtModelOrderList.numberOfItems;
    }

    public void insertFileExt(FileExtModel newFileModel) {
        if (fileExtModelOrderList.insert(newFileModel)) {
            setChanged();
            notifyObservers();
        }
    }
    public FileExtModel getFileExt(int index) {
        return (FileExtModel)fileModelOrderList.get(index);
    }

    public List<FileModel> getList() {
        return fileModelOrderList.getFileModelList();
    }



    public void insert(FileModel newFileModel) {
        // get extension and delegate
        String filefullname = newFileModel.getFilename();
        boolean extUpdated = false;
        int index = filefullname.lastIndexOf('.');
        if (index > 0 && filefullname.length() > 0) {
            String ext = filefullname.substring(filefullname.lastIndexOf('.'), filefullname.length() - 1);
            if (mostFreequentFileExtensions.get(ext) == null) {
                mostFreequentFileExtensions.put(ext, 1);
            } else {
                mostFreequentFileExtensions.put(ext, mostFreequentFileExtensions.get(ext) + 1);
            }
            extUpdated = fileExtModelOrderList.insert(new FileExtModel(ext, mostFreequentFileExtensions.get(ext)));
        }
        boolean fileUpdated = fileModelOrderList.insert(newFileModel);
        mNewEntryUpdated = fileUpdated || extUpdated;
        scannedFilesCount++;
        totalFilesSize += newFileModel.getSize();
        setChanged();
        notifyObservers();

    }
    public FileModel get(int index) {
        return (FileModel) fileModelOrderList.get(index);
    }

    public void clear() {
        fileExtModelOrderList.clear();
        fileModelOrderList.clear();
        mostFreequentFileExtensions.clear();
        totalFilesSize = 0;
        scannedFilesCount = 0;
        setChanged();
        notifyObservers();
    }

    public long getScannedFilesCount() {
        return scannedFilesCount;
    }
}
