package xin.awell.filesync.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xin.awell.filesync.R;
import xin.awell.filesync.databinding.FragmentFileBinding;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.model.User;
import xin.awell.filesync.service.FileLoadService;
import xin.awell.filesync.service.FileLoadServiceImpl;
import xin.awell.filesync.service.INetService;
import xin.awell.filesync.service.NetService;
import xin.awell.filesync.ui.activity.LocalSavePosSelectActivity;
import xin.awell.filesync.ui.adapter.NormalFileListAdapter;
import xin.awell.filesync.ui.popup.PopupWinFileOptMenu;
import xin.awell.filesync.ui.popup.PopupWinMkDir;
import xin.awell.filesync.ui.popup.PopupWinRename;
import xin.awell.filesync.util.Format;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileFragment extends Fragment {

    private FragmentFileBinding binding;

    public FileFragment() {
        // Required empty public constructor
    }




    public static FileFragment newInstance() {
        FileFragment fragment = new FileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_file, container, false);
        fileLoadService = new FileLoadServiceImpl(title -> currentPath = title);
        initView();

                    /*Arrays.asList(new MyFile(){{
                this.setName("test");
                this.setModifiedTime(System.currentTimeMillis());
                this.setPath("/abc");
                this.setSize(12580);
                this.setDirectory(false);
            }});*/

        new Thread(()->{
            List<MyFile> data = null;
            try {
                data = fileLoadService.loadFile(User.getCurrentUser().getUserId(), currentPath);
                handleData(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        return binding.getRoot();
    }


    private void handleData(List<MyFile> dataList){
        listAdapter.setDataAndRefreshList(dataList);
    }

    private FileLoadService fileLoadService;
    private PopupWinMkDir popupWinMkDir;


    private INetService netService = NetService.getInstance();
    private NormalFileListAdapter listAdapter;

    private String currentPath = "/";


    /*
    * 处理这个fragment处于首页时， 一些返回键按压的效果
    * 成功return true
    * */
    public boolean onKeyBackPressed(){
        List<MyFile> dataList = fileLoadService.rockback();
        if(dataList == null){
            return false;
        }else{
            listAdapter.setDataAndRefreshList(dataList);
            return true;
        }
    }


    private void initView() {
       /*
       * init popup
       * */
        popupWinMkDir = new PopupWinMkDir(getActivity(), binding.layoutRoot, new PopupWinMkDir.OnGetDirNameCallback() {
            @Override
            public void getDirName(String dirName) {
                String folderPath = currentPath +dirName;
                Call<JSONObject> call = netService.createDir(User.getCurrentUser().getUserId(), folderPath);
                Log.i("retrofit", "create dir : "  + folderPath);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        //Log.i("retrofit", response.body() + "");
                        //创建成功后， refresh一下页面
                        Log.i("retrofit", "created");
                        new Thread(()->{
                            try {
                                handleData(fileLoadService.refresh());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        Log.i("retrofit", "refresh failed");
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable throwable) {
                        Log.i("retrofit", throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
            }
        });

        /*
        * init toolbar
        * */
        binding.toolbar.setTitle("文件");
        binding.toolbar.inflateMenu(R.menu.file_toolbar_menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_refresh:
                        new Thread(()->{
                            List<MyFile> data = new ArrayList<>();
                            try {
                                data = fileLoadService.refresh();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handleData(data);
                        }).start();
                        break;
                    case R.id.item_mk_dir:
                        Log.i("fileFragment", item.getItemId() + " mkdir");
                        popupWinMkDir.show();
                        break;

                    default:break;
                }
                return false;
            }
        });

        SearchView sv = (SearchView) binding.toolbar.getMenu().findItem(R.id.item_search).getActionView();
        //设置监听
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("fileFragment", "search! query  " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("fileFragment", "search! new " + newText);
                return false;
            }
        });

        listAdapter = new NormalFileListAdapter(this.getActivity(), fileLoadService, binding.layoutRoot);

        binding.lvFile.setAdapter(listAdapter);
    }


    public void requestSelectPathForDownload(String targetFilePath){
        Intent intent = new Intent(getActivity(), LocalSavePosSelectActivity.class);
        startActivityForResult(intent, LocalSavePosSelectActivity.REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocalSavePosSelectActivity.REQUEST_CODE
                && resultCode == LocalSavePosSelectActivity.RESULT_CODE_Y) {
            Log.i("balala", "aowubalalalaa");
            String localPath = data.getStringExtra(LocalSavePosSelectActivity.SELECTED_FILE_PATH);
        }
    }



}
