package com.exscan.online.exscan.model;

/**
 * Created by sreed on 8/11/2017.
 */

public class Node {
    IModel model;
    Node next;
    Node(IModel aModel) {
        model = aModel;
        next = null;
    }
    IModel getModel() {
        return model;
    }
}
