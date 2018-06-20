package xin.awell.filesync.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xin.awell.filesync.R;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.model.User;
import xin.awell.filesync.service.FileLoadService;
import xin.awell.filesync.service.INetService;
import xin.awell.filesync.service.NetService;
import xin.awell.filesync.ui.popup.PopupMenuNormalFileMore;
import xin.awell.filesync.ui.popup.PopupWinRename;
import xin.awell.filesync.util.App;
import xin.awell.filesync.util.Format;

public class NormalFileListAdapter extends BaseAdapter {
    private List<MyFile> myFiles;

    private INetService netService;

    private LayoutInflater layoutInflater;

    private FileLoadService fileLoadService;

    private Activity activity;

    private View rootView;

    private PopupMenu initItemMorePopupMenu(Context context, View anchorView){
        PopupMenu popupMenu = new PopupMenu(context, anchorView);
        popupMenu.inflate(R.menu.file_normal_more);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        Log.i("popup", "item delete");
                        break;
                    case R.id.item_download_select_pos:
                        Log.i("popup", "item select pos download");
                        break;
                    case R.id.item_download_default_pos:
                        Log.i("popup", "item default download");
                        break;
                    case R.id.item_move:
                        Log.i("popup", "item move");
                        break;
                    case R.id.item_rename:
                        Log.i("popup", "item rename");
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        return popupMenu;
    }



    public NormalFileListAdapter(Activity activity, FileLoadService fileLoadService, View rootView){
        this.activity = activity;
        this.myFiles = new ArrayList<>();
        this.layoutInflater = activity.getLayoutInflater();
        netService = NetService.getInstance();
        this.fileLoadService = fileLoadService;
        this.rootView = rootView;
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


    class ViewHolder{
        ImageView imgHeader;
        TextView tvFileName;
        View divisionView;
        TextView tvFileSize;
        TextView tvFileTime;
        AppCompatImageButton btnMoreOperation;
        ViewGroup rootView;

        public ViewHolder(View view){
            imgHeader = view.findViewById(R.id.img_header);
            tvFileName = view.findViewById(R.id.tv_file_name);
            divisionView = view.findViewById(R.id.v_line);
            tvFileSize = view.findViewById(R.id.tv_file_size);
            tvFileTime = view.findViewById(R.id.tv_file_time);
            rootView = (ViewGroup) view;
            btnMoreOperation = view.findViewById(R.id.btn_file_more_operation);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = layoutInflater.inflate(R.layout.list_item_show_files, null);
        }

        ViewHolder holder = new ViewHolder(view);

        MyFile data = myFiles.get(i);

        holder.tvFileName.setText(data.getName());
        holder.tvFileTime.setText(Format.formatFileModifiedTime(data.getModifiedTime()));

        if(data.isDirectory()){
            holder.imgHeader.setImageResource(R.drawable.folder);
            holder.tvFileSize.setText(data.getSubFileCount() + "");
        }else{
            holder.imgHeader.setImageResource(R.drawable.file);
            holder.tvFileSize.setText(Format.formatFileSize(data.getSize()));
        }

        holder.btnMoreOperation.setOnClickListener(view1 -> {
            if(data.isDirectory()){
                PopupMenu popupMenu = new PopupMenu(NormalFileListAdapter.this.activity, holder.btnMoreOperation);
                popupMenu.inflate(R.menu.folder_normal_more);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_delete:
                                String deleteUrl = "/api/file/" + User.getCurrentUser().getUserId()  +  data.getPath();
                                Call<JSONObject> call = netService.deleteFile(deleteUrl);
                                call.enqueue(new Callback<JSONObject>() {
                                    @Override
                                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                        if(response.code() == 200){
                                            Log.i("retrofit", "delete success! " + data.getPath());
                                            myFiles.remove(data);
                                            NormalFileListAdapter.this.notifyDataSetChanged();
                                        }
                                        else {
                                            onFailure(call, new Throwable(response.code() + ""));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable throwable) {
                                        Log.i("retrofit", "delete failed! " + data.getPath());
                                        throwable.printStackTrace();
                                        Log.i("retrofit", "delete url  " + call.request().url().toString());
                                    }
                                });
                                break;
                            case R.id.item_move:
                                Log.i("popup", "item move");
                                break;
                            case R.id.item_share:
                                Log.i("popup", "item rename");
                                break;
                            case R.id.item_rename:
                                Log.i("popup", "item rename");
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();

            }else {
                PopupMenu popupMenu = new PopupMenu(NormalFileListAdapter.this.activity, holder.btnMoreOperation);
                popupMenu.inflate(R.menu.file_normal_more);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_delete:
                                String deleteUrl = "/api/file/" + User.getCurrentUser().getUserId()  +  data.getPath();
                                Call<JSONObject> call = netService.deleteFile(deleteUrl);
                                call.enqueue(new Callback<JSONObject>() {
                                    @Override
                                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                        if(response.code() == 200){
                                            Log.i("retrofit", "delete success! " + data.getPath());
                                            myFiles.remove(data);
                                            NormalFileListAdapter.this.notifyDataSetChanged();
                                        }
                                        else {
                                            onFailure(call, new Throwable(response.code() + ""));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable throwable) {
                                        Log.i("retrofit", "delete failed! " + data.getPath());
                                        throwable.printStackTrace();
                                        Log.i("retrofit", "delete url  " + call.request().url().toString());
                                    }
                                });                                break;
                            case R.id.item_download_select_pos:
                                Log.i("popup", "item select pos download");
                                break;
                            case R.id.item_download_default_pos:
                                Log.i("popup", "item default download");
                                break;
                            case R.id.item_move:
                                Log.i("popup", "item move");
                                break;
                            case R.id.item_rename:
                                Log.i("popup", "item rename");
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }

            //loadData(User.getCurrentUser().getUserId(), data.getPath());
        });

        holder.rootView.setOnClickListener(v ->{
            if(data.isDirectory()){
                new Thread(()->{
                    try {
                        setDataAndRefreshList(fileLoadService.loadFile(User.getCurrentUser().getUserId(), data.getPath()));
                    } catch (Exception e) {
                        Log.i("retrofit","load file meta data error!" + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();


            }else
                holder.btnMoreOperation.performClick();
        });

        return view;
    }


    /*
    * set data and
    * */
    public void setDataAndRefreshList(List<MyFile> dataList){
        activity.runOnUiThread(()->{
            myFiles = dataList;
            NormalFileListAdapter.this.notifyDataSetChanged();
        });
    }


    public void renameFile(String fileUrl){
        PopupWinRename popupWinRename = new PopupWinRename(activity, rootView, name -> {
            if(name != null && !name.equals("")){
                //TODO
            }
        });
    }

}
