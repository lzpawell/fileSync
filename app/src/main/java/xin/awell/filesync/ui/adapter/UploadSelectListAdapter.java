package xin.awell.filesync.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import xin.awell.filesync.R;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.util.Format;

public class UploadSelectListAdapter extends BaseAdapter {
    private List<MyFile> myFiles;

    private LayoutInflater layoutInflater;

    private Activity activity;

    private OnFolderClickListener listener;

    public UploadSelectListAdapter(Activity activity, List<MyFile> files, OnFolderClickListener listener) {
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

        UploadSelectListAdapter.ViewHolder holder = new UploadSelectListAdapter.ViewHolder(view);

        MyFile data = myFiles.get(i);

        holder.tvFileName.setText(data.getName());
        holder.tvFileTime.setText(Format.formatFileModifiedTime(data.getModifiedTime()));

        if (data.isDirectory()) {
            holder.imgHeader.setImageResource(R.drawable.folder);
            holder.tvFileSize.setText(data.getSubFileCount() + "");
        } else {
            holder.imgHeader.setImageResource(R.drawable.file);
            holder.tvFileSize.setText(Format.formatFileSize(data.getSize()));
        }

        holder.rootView.setOnClickListener(v -> {
            if (data.isDirectory()) {
                this.listener.onFolderClick(data.getPath());
            }
        });

        return view;
    }


    /*
     * set data and
     * */
    public void setDataAndRefreshList(List<MyFile> dataList) {
        myFiles = dataList;
        UploadSelectListAdapter.this.notifyDataSetChanged();
    }


    public interface OnFolderClickListener{
        void onFolderClick(String folderPath);
    }
}