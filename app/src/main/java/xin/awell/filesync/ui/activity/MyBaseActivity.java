package xin.awell.filesync.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


/*
* 配合ActivityManager处理清acitivity栈
* */
public class MyBaseActivity extends AppCompatActivity {

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        ActivityManager.getActivityManager().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getActivityManager().popActivity();
        Log.i("balala", "destroy: " + this.getClass().toString());
    }
}
