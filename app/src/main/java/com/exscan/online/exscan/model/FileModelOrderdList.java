package com.exscan.online.exscan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreed on 8/11/2017.
 */
public class FileModelOrderdList extends OrderdList {
    public FileModelOrderdList(int i) {
        super(i);
    }

    @Override
    public Node createNewNode(IModel newmodel) {
        return createNode((FileModel) newmodel);
    }

    public Node createNode(FileModel filemodel) {
        Node newNode = new Node(filemodel);
        if (numberOfItems < max_size) {
            numberOfItems++;
        }
        return newNode;
    }

    public List<FileModel> getFileModelList() {
        ArrayList<FileModel> arrayList = new ArrayList<>();
        List<IModel> list = getList();
        if (list != null) {
            for (IModel model : list) {
                arrayList.add((FileModel) model);
            }
        }
        return arrayList;
    }
}
