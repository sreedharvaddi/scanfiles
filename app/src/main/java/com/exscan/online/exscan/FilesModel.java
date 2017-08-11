package com.exscan.online.exscan;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Created by sreed on 8/9/2017.
 */

public class FilesModel extends Observable {
    int numberOfItems;
    final int max_size = 10;
    Node head = null;
    Node tail = null;

    HashMap<String, Integer> mostFreequentFileExtensions = new HashMap<>();
    NodeFileExt headFileExt = null;
    NodeFileExt tailFileExt = null;
    final int max_files_ext = 5;
    int numberOfFileExtItems;
    long scannedFilesCount = 0;
    double totalFilesSize = 0;

    public double getAvgFileSize() {
        return totalFilesSize / scannedFilesCount;
    }

    private class NodeFileExt {
        public FileExtModel fileExtModel;
        public NodeFileExt next;
        NodeFileExt(FileExtModel afileModel) {
            fileExtModel = afileModel;
            next = null;
        }
    }
    public List<FileExtModel> getFileExtList() {
        ArrayList<FileExtModel> lstFiles = new ArrayList<>();
        NodeFileExt temp = headFileExt;
        while(temp != null) {
            lstFiles.add(temp.fileExtModel);
            temp = temp.next;
        }
        return lstFiles;
    }

    private NodeFileExt createFileExtNewNode(FileExtModel newFileModel) {
        NodeFileExt newNode = new NodeFileExt(newFileModel);
        if (numberOfFileExtItems < max_files_ext) {
            numberOfFileExtItems++;
        }
        return newNode;
    }

    public int sizeFileExt() {
        return numberOfFileExtItems;
    }

    public void insertFileExt(final FileExtModel newFileModel) {

        if (numberOfFileExtItems < max_files_ext) {
            NodeFileExt newNode = createFileExtNewNode(newFileModel);
            if (headFileExt == null) {
                headFileExt = newNode;
                tailFileExt = newNode;
                return;
            }
            // check for head
            if (newNode.fileExtModel.getFrequency() >= headFileExt.fileExtModel.getFrequency()) {
                newNode.next = headFileExt;
                headFileExt = newNode;
            }
            else if (newNode.fileExtModel.getFrequency() <= tailFileExt.fileExtModel.getFrequency()) {
                tailFileExt.next = newNode;
                tailFileExt = newNode;
            }
            else {
                NodeFileExt temp = headFileExt;
                while (temp.next != null && newNode.fileExtModel.getFrequency() < temp.next.fileExtModel.getFrequency()) {
                    temp = temp.next;
                }
                newNode.next = temp.next;
                temp.next = newNode;
            }
            setChanged();
            notifyObservers();
        }
        else {
            if (newFileModel.getFrequency() <= tailFileExt.fileExtModel.getFrequency()) {
                return;
            }

            NodeFileExt newNode = createFileExtNewNode(newFileModel);
            NodeFileExt temp = headFileExt;

            if (newFileModel.getFrequency() >= headFileExt.fileExtModel.getFrequency()) {
                newNode.next = headFileExt;
                headFileExt = newNode;
            }
            else {
                temp = headFileExt;
                while (temp.next != null && newNode.fileExtModel.getFrequency() < temp.next.fileExtModel.getFrequency()) {
                    temp = temp.next;
                }
                newNode.next = temp.next;
                temp.next = newNode;
            }
            // remove tail
            temp = headFileExt;
            while (temp.next != tailFileExt) {
                temp = temp.next;
            }
            temp.next = null;
            tailFileExt = temp;
            setChanged();
            notifyObservers();
        }
        printListExt();
    }
    public FileExtModel getFileExt(int index) {
        NodeFileExt temp = headFileExt;
        while (temp != null && index > 0){
            index--;
            temp = temp.next;
        }
        if (index == 0) {
            return temp.fileExtModel;
        }
        return null;
    }

    public void printListExt() {
        NodeFileExt temp = headFileExt;
        String str = "Extensions ";
        while (temp != null) {
            str += "->"+temp.fileExtModel.getExtName()+" : "+temp.fileExtModel.getFrequency();
            temp = temp.next;
        }
        Log.d("FilesModel", str);
    }
    private class Node {
        FileModel fileModel;
        Node next;
        Node(FileModel afileModel) {
            fileModel = afileModel;
            next = null;
        }
    }


    public List<FileModel> getList() {
        ArrayList<FileModel> lstFiles = new ArrayList<>();
        Node temp = head;
        while(temp != null) {
            lstFiles.add(temp.fileModel);
            temp = temp.next;
        }
        return lstFiles;
    }
    private Node createNewNode(FileModel newFileModel) {
        Node newNode = new Node(newFileModel);
        if (numberOfItems < max_size) {
            numberOfItems++;
        }
        return newNode;
    }

    public int size() {
        return numberOfItems;
    }

    public void insert(final FileModel newFileModel) {
        // get extension and delegate
        String filefullname = newFileModel.getFilename();
        String ext = filefullname.substring(filefullname.lastIndexOf('.'), filefullname.length() - 1);
        if (mostFreequentFileExtensions.get(ext) == null) {
            mostFreequentFileExtensions.put(ext, 1);
        }
        else {
            mostFreequentFileExtensions.put(ext, mostFreequentFileExtensions.get(ext)+1);
        }
        insertFileExt(new FileExtModel(ext, mostFreequentFileExtensions.get(ext)));
        scannedFilesCount++;
        totalFilesSize += newFileModel.getSize();
        /////
        if (numberOfItems < max_size) {
            Node newNode = createNewNode(newFileModel);
            if (head == null) {
                head = newNode;
                tail = newNode;
                return;
            }
            // check for head
            if (newNode.fileModel.getSize() >= head.fileModel.getSize()) {
                newNode.next = head;
                head = newNode;
            }
            else if (newNode.fileModel.getSize() <= tail.fileModel.getSize()) {
                tail.next = newNode;
                tail = newNode;
            }
            else {
                Node temp = head;
                while (temp.next != null && newNode.fileModel.getSize() < temp.next.fileModel.getSize()) {
                    temp = temp.next;
                }
                newNode.next = temp.next;
                temp.next = newNode;
            }
            setChanged();
            notifyObservers();
        }
        else {
            if (newFileModel.getSize() <= tail.fileModel.getSize()) {
                return;
            }

            Node newNode = createNewNode(newFileModel);
            Node temp = head;

            if (newFileModel.getSize() >= head.fileModel.getSize()) {
                newNode.next = head;
                head = newNode;
            }
            else {
                temp = head;
                while (temp.next != null && newNode.fileModel.getSize() < temp.next.fileModel.getSize()) {
                    temp = temp.next;
                }
                newNode.next = temp.next;
                temp.next = newNode;
            }
            // remove tail
            temp = head;
            while (temp.next != tail) {
                temp = temp.next;
            }
            temp.next = null;
            tail = temp;
            setChanged();
            notifyObservers();
        }
        printList();
    }
    public FileModel get(int index) {
        Node temp = head;
        while (temp != null && index > 0){
            index--;
            temp = temp.next;
        }
        if (index == 0) {
            return temp.fileModel;
        }
        return null;
    }

    public void printList() {
        Node temp = head;
        String str = "";
        while (temp != null) {
            str += "->"+temp.fileModel.getSize();
            temp = temp.next;
        }
        Log.d("FilesModel", str);
    }
    void clear() {
        numberOfItems = 0;
        head = null;
        tail = null;
        numberOfFileExtItems = 0;
        headFileExt = null;
        tailFileExt = null;
        mostFreequentFileExtensions.clear();
        totalFilesSize = 0;
        scannedFilesCount = 0;
        setChanged();
        notifyObservers();
    }
}
