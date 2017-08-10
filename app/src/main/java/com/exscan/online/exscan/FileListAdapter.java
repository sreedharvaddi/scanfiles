package com.exscan.online.exscan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sreed on 8/9/2017.
 */

public class FileListAdapter extends BaseAdapter {

    List<FileModel> fileModels;
    Activity ctx;
    FileListAdapter(Activity ctx, List<FileModel> files) {
        this.ctx = ctx;
        fileModels = files;
    }
    public void update(List<FileModel> afiles) {
        fileModels = afiles;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return fileModels.size();
    }

    @Override
    public Object getItem(int position) {
        return fileModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (rootView == null) {
            LayoutInflater inflater = ctx.getLayoutInflater();
            rootView = inflater.inflate(R.layout.file_item, null);
            ViewHolder vh = new ViewHolder();
            vh.filename = (TextView) rootView.findViewById(R.id.filename);
            vh.filesize = (TextView) rootView.findViewById(R.id.filesize);
            rootView.setTag(vh);
        }
        ViewHolder vh = (ViewHolder) rootView.getTag();
        vh.filename.setText(fileModels.get(position).getFilename());
        vh.filesize.setText("Size :"+fileModels.get(position).getSize());
        return rootView;
    }

    static class ViewHolder {
        public TextView filename;
        public TextView filesize;
    }
}
