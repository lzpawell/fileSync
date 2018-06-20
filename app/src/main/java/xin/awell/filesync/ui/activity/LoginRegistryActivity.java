package xin.awell.filesync.ui.activity;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


import xin.awell.filesync.R;
import xin.awell.filesync.ui.fragment.LoginFragment;
import xin.awell.filesync.ui.fragment.RegistryFragment;

public class LoginRegistryActivity extends MyBaseActivity implements LoginFragment.OnLoginListener{

    private LoginFragment loginFragment;
    private RegistryFragment registryFragment;

    private String currentFragmentTag;

    private static final String TAG_LOGIN_FRAGMENT = "TAG_LOGIN_FRAGMENT";
    private static final String TAG_REGISTRY_FRAGMENT = "TAG_REGISTRY_FRAGMENT";

    public static final int TAG_REQUEST_LOGIN = 0x11;
    public static final int TAG_LOGIN_SUCCESS = 0x22;
    public static final int TAG_LOGIN_FAILED = 0x33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFragment();

    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if((loginFragment = (LoginFragment) fragmentManager.findFragmentByTag(TAG_LOGIN_FRAGMENT))
                == null)
            loginFragment = LoginFragment.newInstance();

        if((registryFragment = (RegistryFragment) fragmentManager.findFragmentByTag(TAG_REGISTRY_FRAGMENT))
                == null)
            registryFragment = RegistryFragment.newInstance();

        fragmentTransaction.add(R.id.content_v, registryFragment, TAG_REGISTRY_FRAGMENT);
        fragmentTransaction.hide(registryFragment);
        fragmentTransaction.add(R.id.content_v, loginFragment, TAG_LOGIN_FRAGMENT);

        fragmentTransaction.commit();

        currentFragmentTag = TAG_LOGIN_FRAGMENT;
    }





    private long lastBackPressed = 0;

    @Override
    public void onBackPressed() {
        if(currentFragmentTag.equals(TAG_REGISTRY_FRAGMENT)){
            //跳转到登陆
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.hide(registryFragment);
            transaction.show(loginFragment);

            transaction.commit();

            currentFragmentTag = TAG_LOGIN_FRAGMENT;
            return;
        }

        if(System.currentTimeMillis() - lastBackPressed > 2000){
            lastBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.i("balala", "current time: " + lastBackPressed);
            ActivityManager.getActivityManager().popAllActivity();
        }
    }

    @Override
    public void onLoginResult(int result) {
        if(result == LoginFragment.LOGIN_SUCCESS){
            this.setResult(TAG_LOGIN_SUCCESS);
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            this.finish();
        }else{

        }
    }

    @Override
    public void onBtnRegistryPressed() {
        //跳转到注册
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(loginFragment);
        transaction.show(registryFragment);

        transaction.commit();
        currentFragmentTag = TAG_REGISTRY_FRAGMENT;
    }
}
