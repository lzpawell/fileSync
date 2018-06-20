package xin.awell.filesync.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import xin.awell.filesync.R;
import xin.awell.filesync.databinding.ActivityLocalSavePosSelectBinding;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.ui.adapter.LocalFileListAdapter;
import xin.awell.filesync.util.PermissionChecker;
import xin.awell.filesync.util.RootFileDirGetter;

public class LocalSavePosSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLocalSavePosSelectBinding binding;

    private List<File> currentFileList;
    private Stack<List<File>> fileStack;
    private Stack<String> filePathStack;
    private LocalFileListAdapter adapter;

    /*
    * 返回前 set一下result， == null表示没有选择一个路径
    * */
    public static final int REQUEST_CODE = 666;
    public static final int RESULT_CODE_Y = 0;
    public static final int RESULT_CODE_N = -1;
    public static final String SELECTED_FILE_PATH = "SELECTED_FILE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_local_save_pos_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().hide();


/*        if(PermissionChecker.lacksPermissions(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

            return;
        }*/

        initData();
        setView();
    }


    private void initData() {
        fileStack = new Stack<>();
        filePathStack = new Stack<>();
        filePathStack.push("/");
        currentFileList = RootFileDirGetter.getAvaliableStorage(this);
        Log.i("balala", currentFileList.size() + "");
    }

    private void setView() {
        binding.tvCurrentLocation.setText("/");
        binding.btnCancel.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);

        adapter = new LocalFileListAdapter(this, currentFileList, (filePath)->{
            this.filePathStack.push(filePath);
            this.fileStack.push(currentFileList);

            File file = new File(filePath);
            this.currentFileList = Arrays.asList(file.listFiles());
            adapter.setDataAndRefreshList(currentFileList);
            binding.tvCurrentLocation.setText(filePath);
        });

        binding.lvShowFile.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                setResult(RESULT_CODE_N);
                this.finish();
                break;

            case R.id.btn_submit:
                Intent intent = new Intent();
                intent.putExtra(SELECTED_FILE_PATH, filePathStack.peek());
                setResult(RESULT_CODE_Y, intent);
                Log.i("balala", "code y");
                this.finish();
                break;

                default:break;
        }
    }





    @Override
    public void onBackPressed(){
        if(fileStack.empty()){
            Log.i("balala", "123");
            setResult(RESULT_CODE_N);
            this.finish();
        }else{
            Log.i("balala", "123456");
            currentFileList = fileStack.pop();
            filePathStack.pop();
            binding.tvCurrentLocation.setText(filePathStack.peek());
            adapter.setDataAndRefreshList(currentFileList);
        }
    }
}
