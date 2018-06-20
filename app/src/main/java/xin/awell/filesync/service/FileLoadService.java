package xin.awell.filesync.service;


import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.util.App;


/*
*负责加载文件并且保存历史列表
 *  */
public interface FileLoadService{

    /*
     * 加载文件， 成功返回一个list， 失败return null
     * */
    List<MyFile> loadFile(String owner, String path) throws IOException, Exception;

    List<MyFile> rockback();

    List<MyFile> refresh() throws IOException, Exception;

    void setOnTitleChangeListener(OnTitleChangeListener listener);

    interface OnTitleChangeListener{
        void onTitleChange(String title);
    }

    interface OnGetFileListCallback {
        void getFileList(List<MyFile> list, Exception ex);
    }
}
