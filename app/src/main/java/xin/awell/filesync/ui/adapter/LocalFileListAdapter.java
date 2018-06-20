package xin.awell.filesync.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import xin.awell.filesync.R;
import xin.awell.filesync.util.Format;

public class LocalFileListAdapter extends BaseAdapter {
    private List<File> myFiles;

    private LayoutInflater layoutInflater;

    private Activity activity;

    private OnFolderClickListener listener;

    public LocalFileListAdapter(Activity activity, List<File> files, OnFolderClickListener listener) {
        this.activity = activity;
        this.layoutInflater = activity.getLayoutInflater();
        myFiles = files;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return myFiles.size();
    }

    @Override
    public Object getItem(int i) {
        return myFiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    class ViewHolder {
        ImageView imgHeader;
        TextView tvFileName;
        View divisionView;
        TextView tvFileSize;
        TextView tvFileTime;
        ViewGroup rootView;

        public ViewHolder(View view) {
            imgHeader = view.findViewById(R.id.img_header);
            tvFileName = view.findViewById(R.id.tv_file_name);
            divisionView = view.findViewById(R.id.v_line);
            tvFileSize = view.findViewById(R.id.tv_file_size);
            tvFileTime = view.findViewById(R.id.tv_file_time);
            rootView = (ViewGroup) view;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_show_local_files, null);
        }

        LocalFileListAdapter.ViewHolder holder = new LocalFileListAdapter.ViewHolder(view);

        File data = myFiles.get(i);

        holder.tvFileName.setText(data.getName());
        holder.tvFileTime.setText(Format.formatFileModifiedTime(data.lastModified()));

        if (data.isDirectory()) {
            holder.imgHeader.setImageResource(R.drawable.folder);
            holder.tvFileSize.setText(data.listFiles().length + "");
        } else {
            holder.imgHeader.setImageResource(R.drawable.file);
            holder.tvFileSize.setText(Format.formatFileSize(data.length()));
        }

        holder.rootView.setOnClickListener(v -> {
            if (data.isDirectory()) {
                this.listener.onFolderClick(data.getAbsolutePath());
            }
        });

        return view;
    }


    /*
     * set data and
     * */
    public void setDataAndRefreshList(List<File> dataList) {
        myFiles = dataList;
        LocalFileListAdapter.this.notifyDataSetChanged();
    }


    public interface OnFolderClickListener{
        void onFolderClick(String folderPath);
    }
}