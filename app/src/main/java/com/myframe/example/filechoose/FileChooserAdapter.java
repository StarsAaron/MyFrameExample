package com.myframe.example.filechoose;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myframe.R;


public class FileChooserAdapter extends BaseAdapter {

    private ArrayList<FileInfo> mFileLists;
    private LayoutInflater mLayoutInflater = null;
    private Context context = null;

    private static ArrayList<String> PPT_SUFFIX = new ArrayList<String>();

    public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists) {
        super();
        mFileLists = fileLists;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFileLists.size();
    }

    @Override
    public FileInfo getItem(int position) {
        return mFileLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            view = mLayoutInflater.inflate(R.layout.item_filechooser, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        FileInfo fileInfo = getItem(position);
        holder.tvFileName.setText(fileInfo.getFileName());
        if (fileInfo.isDirectory()) { // 文件夹
            holder.imgFileIcon.setImageResource(R.mipmap.ic_folder);
            holder.tvFileName.setTextColor(Color.WHITE);
        } else if (fileInfo.isTextFile()) { // text文件
            holder.imgFileIcon.setImageResource(R.mipmap.ic_file_txt);
            holder.tvFileName.setTextColor(Color.RED);
        } else { // 未知文件
            holder.imgFileIcon.setImageResource(R.mipmap.ic_file_other);
            holder.tvFileName.setTextColor(Color.WHITE);
        }
        return view;
    }

    static class ViewHolder {
        ImageView imgFileIcon;
        TextView tvFileName;

        public ViewHolder(View view) {
            imgFileIcon = (ImageView) view.findViewById(R.id.imgFileIcon);
            tvFileName = (TextView) view.findViewById(R.id.tvFileName);
        }
    }
}
