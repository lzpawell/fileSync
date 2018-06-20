package xin.awell.filesync.util;

import android.app.Application;
import android.content.Context;

import xin.awell.filesync.service.NetService;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        NetService.init();
        PermissionChecker.init(this);
        app = this;
    }


    public static Context getContext(){
        return app;
    }

}
