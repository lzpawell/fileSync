package xin.awell.filesync.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.List;

import xin.awell.filesync.model.LoadProgress;
import xin.awell.filesync.model.LoadProgressVO;
import xin.awell.filesync.storage.TransferDBHelper;

public class TransferService extends Service {

    List<LoadProgress> transferMetaDataList;
    List<LoadProgressVO> loadProgressVOList;

    TransferDBHelper transferDBHelper;
    HandlerThread handlerThread;
    Handler handler;



    public TransferService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TransferBinder();
    }


    public class TransferBinder extends Binder{
        public TransferService getService(){
            return TransferService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        /*
        * 加载元数据
        * */
        transferDBHelper = new TransferDBHelper(this);
        transferMetaDataList = transferDBHelper.getLoadProgressList();

        // init handler handlerThread
        handlerThread = new HandlerThread("transferHandlerThread");
        handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {

            }
        };


        handlerThread.start();
    }


    public void requestDownloadFile(String ownerId,  String remoteFilePath, String localFilePath){
        Log.i("transferService", "requestDownloadFile  ownerId : " + ownerId
                + ",  remoteFilePath:  " + remoteFilePath
                + ",  localFilePath" + localFilePath);
    }


    public void startAll(){

    }

    public void start(LoadProgress loadProgress){

    }

    public void stop(LoadProgress loadProgress){

    }


    @Override
    public void onDestroy() {
        handlerThread.quit();

        for(LoadProgress progress : transferMetaDataList){
            if(progress.getStatus().equals(LoadProgress.STATUS_DOWNLOADING))
                progress.setStatus(LoadProgress.STATUS_PAUSE_DOWNLOAD);

            if(progress.getStatus().equals(LoadProgress.STATUS_UPLOADING))
                progress.setStatus(LoadProgress.STATUS_PAUSE_UPLOAD);

            transferDBHelper.saveProgress(progress);
        }
        super.onDestroy();
    }


}
