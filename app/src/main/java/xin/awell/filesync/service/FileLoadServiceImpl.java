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

public class FileLoadServiceImpl implements FileLoadService{
    private String currentPath;
    private String currentOwner;
    private List<MyFile> currentFiles;

    OnTitleChangeListener listener;

    private Stack<List<MyFile>> historyStack;
    private Stack<String> historyPathStack;
    private Stack<String> historyOwnerStack;
    private INetService netService;

    public FileLoadServiceImpl(OnTitleChangeListener listener){
        historyStack = new Stack<>();
        historyPathStack = new Stack<>();
        historyOwnerStack = new Stack<>();
        netService = NetService.getInstance();
        this.listener = listener;
    }


    @Override
    public List loadFile(String owner, String path) throws Exception {
        if(currentPath != null){
            historyStack.push(currentFiles);
            historyOwnerStack.push(currentOwner);
            historyPathStack.push(currentPath);
        }

        currentPath = path;
        currentOwner = owner;
        listener.onTitleChange(currentPath);

        String url = "/api/file/list/" + owner + path;
        Call<List<MyFile>> call = netService.listFile(url);

        Log.i("retrofit", "" + url);

        Response<List<MyFile>> response = call.execute();

        if(response.code() == 200){
            currentFiles = response.body();
            return response.body();
        }

        return null;
    }

    @Override
    public List rockback() {
        if(historyStack.empty())
            return null;
        else{
            currentPath = historyPathStack.pop();
            currentFiles = historyStack.pop();
            currentOwner = historyOwnerStack.pop();
            listener.onTitleChange(currentPath);
            return currentFiles;
        }
    }

    @Override
    public List refresh() throws Exception {
        if(currentPath == null){
            throw new RuntimeException("currentPath is null!");
        }


        String url = "/api/file/list/" + currentOwner + currentPath;
        Call<List<MyFile>> call = netService.listFile(url);
        Response<List<MyFile>> response = call.execute();

        if(response.code() == 200){
            currentFiles = response.body();
            return response.body();
        }
        return null;
    }

    @Override
    public void setOnTitleChangeListener(OnTitleChangeListener listener) {
        this.listener = listener;
    }
}
