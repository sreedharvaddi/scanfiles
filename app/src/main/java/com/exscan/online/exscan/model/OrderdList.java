package com.exscan.online.exscan.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sreed on 8/11/2017.
 */

public abstract class OrderdList {
    public int numberOfItems;
    int max_size = 10;
    Node head = null;
    Node tail = null;
    
    OrderdList(int max) {
        max_size = max;
    }

    public List<IModel> getList() {
        ArrayList<IModel> lstFiles = new ArrayList<>();
        Node temp = head;
        while(temp != null) {
            lstFiles.add(temp.getModel());
            temp = temp.next;
        }
        return lstFiles;
    }

    abstract public Node createNewNode(IModel newmodel);
//    {
//        Node newNode = new Node<T>(filemodel);
//        if (numberOfItems < max_size) {
//            numberOfItems++;
//        }
//        return newNode;
//        }

    public int size() {
        return numberOfItems;
    }

    public boolean insert(IModel newmodel) {
        if (numberOfItems < max_size) {
            Node newNode = createNewNode(newmodel);
            if (head == null) {
                head = newNode;
                tail = newNode;
                return true;
            }
            // check for head
            if (newNode.model.getSize() >= head.model.getSize()) {
                newNode.next = head;
                head = newNode;
            }
            else if (newNode.model.getSize() <= tail.model.getSize()) {
                tail.next = newNode;
                tail = newNode;
            }
            else {
                Node temp = head;
                while (temp.next != null && newNode.model.getSize() < temp.next.model.getSize()) {
                    temp = temp.next;
                }
                newNode.next = temp.next;
                temp.next = newNode;
            }
        }
        else {
            if (newmodel.getSize() <= tail.model.getSize()) {
                return false;
            }

            Node newNode = createNewNode(newmodel);
            Node temp = head;

            if (newmodel.getSize() >= head.model.getSize()) {
                newNode.next = head;
                head = newNode;
            }
            else {
                temp = head;
                while (temp.next != null && newNode.model.getSize() < temp.next.model.getSize()) {
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
        }
        printList();
        return true;
    }
    public IModel get(int index) {
        Node temp = head;
        while (temp != null && index > 0){
            index--;
            temp = temp.next;
        }
        if (index == 0) {
            return temp.model;
        }
        return null;
    }

    public void printList() {
        Node temp = head;
        String str = "";
        while (temp != null) {
            str += "->"+temp.model.getSize();
            temp = temp.next;
        }
        Log.d("FilesModel", str);
    }
    void clear() {
        numberOfItems = 0;
        head = null;
        tail = null;
    }
}
