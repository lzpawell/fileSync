package xin.awell.filesync.ui.activity;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

public class ActivityManager{
    private static final String TAG = "ActivityManager";
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;
    private Activity currActivity;
    private ActivityManager(){
    }

    public static ActivityManager getActivityManager(){
        if(instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    //退出栈顶Activity
    public void popActivity(){
        if(activityStack == null || activityStack.size() == 0){
            return;
        }

        activityStack.pop().finish();
    }

    public void destroyActivity(Activity activity){
        if(activity == null){
            return;
        }
        activity.finish();
        if(activityStack.contains(activity)){
            activityStack.remove(activity);
        }
        activity=null;
    }

    //获得当前栈顶Activity
    public Activity currentActivity(){
        if(activityStack == null||activityStack.empty()){
            return null;
        }
        return activityStack.peek();
    }

    //将当前Activity推入栈中
    public void pushActivity(Activity activity){
        if(activityStack == null){
            activityStack = new Stack<Activity>();
        }
        activityStack.push(activity);
    }

    //退出栈中除指定的Activity外所有
    public void popAllActivityExceptOne(Class cls){
        while(true){
            Activity activity = currentActivity();
            if(activity == null || activity.getClass().equals(cls)){
                break;
            }

            destroyActivity(activity);
        }
    }


    //退出栈中所有Activity
    public void popAllActivity(){
        popAllActivityExceptOne(null);
    }


    public int getActivityStackSize(){
        int size = 0;
        if(activityStack != null){
            size = activityStack.size();
        }
        return size;
    }

}

