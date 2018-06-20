package xin.awell.filesync.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import xin.awell.filesync.R;
import xin.awell.filesync.databinding.ActivityMainBinding;
import xin.awell.filesync.model.User;
import xin.awell.filesync.service.INetService;
import xin.awell.filesync.ui.fragment.FileFragment;
import xin.awell.filesync.ui.fragment.MeFragment;
import xin.awell.filesync.ui.fragment.RecentFragment;
import xin.awell.filesync.ui.fragment.TransferFragment;
import xin.awell.filesync.ui.fragment.WorkGroupFragment;

public class MainActivity extends MyBaseActivity implements View.OnClickListener{


    private ActivityMainBinding binding;

    private FileFragment fileFragment;
    private MeFragment meFragment;
    private RecentFragment recentFragment;
    private TransferFragment transferFragment;
    private WorkGroupFragment workGroupFragment;

    private static final String FRAGMENT_TAG = "fragmentTag";
    private static final String FILE_FRAGMENT_TAG = "fileFragment";
    public static final String RECENT_FRAGMENT_TAG = "recentFragment";
    private static final String ME_FRAGMENT_TAG = "meFragment";
    private static final String TRANSFER_FRAGMENT_TAG = "transferFragment";
    private static final String WORK_GROUP_FRAGMENT_TAG = "workGroupFragment";

    private String currentFragmentTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        initData();
    }

    private INetService netService;

    private void initData() {
        //已进入主页， 先load数据看看用户是否登陆，如果未登录， 跳转到登陆页面
/*        User user = new User();
        user.setUserId("awell");
        user.setPassword("awell");
        user.setNickname("awell");
        User.setCurrentUser(user);*/
        if(User.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginRegistryActivity.class);
            startActivityForResult(intent, LoginRegistryActivity.TAG_REQUEST_LOGIN);
        }else{
            initView();
        }
        //如果可以后台自动登陆， 尝试自动登陆
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LoginRegistryActivity.TAG_REQUEST_LOGIN){
            if(resultCode == LoginRegistryActivity.TAG_LOGIN_FAILED || User.getCurrentUser() == null)
                finish();
            else
                initView();
        }

    }

    private void initView() {
        binding.lyFile.setOnClickListener(this);
        binding.lyGroup.setOnClickListener(this);
        binding.lyMe.setOnClickListener(this);
        binding.lyRecent.setOnClickListener(this);
        binding.lyTransfer.setOnClickListener(this);
        binding.tvFile.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.imgFile.setBackgroundResource(R.mipmap.my_choose);

        initFragment();
        setCurrentFragment();
    }

    private void setCurrentFragment() {
        if(currentFragmentTag == null){
            currentFragmentTag = FILE_FRAGMENT_TAG;
        }


        resetView();

        Fragment currentFragment = null;

        switch (currentFragmentTag){
            case FILE_FRAGMENT_TAG:
                binding.imgFile.setBackgroundResource(R.mipmap.my_not_choose);
                binding.tvFile.setTextColor(ContextCompat.getColor(this,R.color.textColor));

                currentFragment = fileFragment;
                break;

            case RECENT_FRAGMENT_TAG:
                binding.imgRecent.setBackgroundResource(R.mipmap.my_not_choose);
                binding.tvRecent.setTextColor(ContextCompat.getColor(this,R.color.textColor));

                currentFragment = recentFragment;
                break;

            case WORK_GROUP_FRAGMENT_TAG:
                binding.imgGroup.setBackgroundResource(R.mipmap.my_not_choose);
                binding.tvGroup.setTextColor(ContextCompat.getColor(this,R.color.textColor));

                currentFragment = workGroupFragment;
                break;

            case TRANSFER_FRAGMENT_TAG:
                binding.imgTransfer.setBackgroundResource(R.mipmap.my_not_choose);
                binding.tvTransfer.setTextColor(ContextCompat.getColor(this,R.color.textColor));

                currentFragment = transferFragment;
                break;

            case ME_FRAGMENT_TAG:
                binding.imgMe.setBackgroundResource(R.mipmap.my_not_choose);
                binding.tvMe.setTextColor(ContextCompat.getColor(this,R.color.textColor));

                currentFragment = meFragment;
                break;

            default:
                break;
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Log.i("balala", "fragment" + currentFragment.getClass());
        fragmentTransaction.show(currentFragment);
        fragmentTransaction.commit();

    }

    private void resetView(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fileFragment);
        transaction.hide(recentFragment);
        transaction.hide(workGroupFragment);
        transaction.hide(transferFragment);
        transaction.hide(meFragment);
        transaction.commit();

        binding.imgFile.setBackgroundResource(R.mipmap.my_not_choose);
        binding.imgRecent.setBackgroundResource(R.mipmap.my_not_choose);
        binding.imgGroup.setBackgroundResource(R.mipmap.my_not_choose);
        binding.imgTransfer.setBackgroundResource(R.mipmap.my_not_choose);
        binding.imgMe.setBackgroundResource(R.mipmap.my_not_choose);

        binding.tvFile.setTextColor(ContextCompat.getColor(this,R.color.textColor));
        binding.tvRecent.setTextColor(ContextCompat.getColor(this,R.color.textColor));
        binding.tvGroup.setTextColor(ContextCompat.getColor(this,R.color.textColor));
        binding.tvTransfer.setTextColor(ContextCompat.getColor(this,R.color.textColor));
        binding.tvMe.setTextColor(ContextCompat.getColor(this,R.color.textColor));
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        fileFragment = (FileFragment) getSupportFragmentManager().findFragmentByTag(FILE_FRAGMENT_TAG);
        if(fileFragment == null){
            fileFragment = FileFragment.newInstance();
            transaction.add(R.id.v_content, fileFragment, FILE_FRAGMENT_TAG);
        }

        recentFragment = (RecentFragment) getSupportFragmentManager().findFragmentByTag(RECENT_FRAGMENT_TAG);
        if(recentFragment == null){
            recentFragment = RecentFragment.newInstance();
            transaction.add(R.id.v_content, recentFragment, RECENT_FRAGMENT_TAG);
        }

        workGroupFragment = (WorkGroupFragment) getSupportFragmentManager().findFragmentByTag(WORK_GROUP_FRAGMENT_TAG);
        if(workGroupFragment == null){
            workGroupFragment = WorkGroupFragment.newInstance();
            transaction.add(R.id.v_content, workGroupFragment, WORK_GROUP_FRAGMENT_TAG);
        }

        transferFragment = (TransferFragment) getSupportFragmentManager().findFragmentByTag(TRANSFER_FRAGMENT_TAG);
        if(transferFragment == null){
            transferFragment = TransferFragment.newInstance();
            transaction.add(R.id.v_content, transferFragment, TRANSFER_FRAGMENT_TAG);
        }

        meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(ME_FRAGMENT_TAG);
        if(meFragment == null){
            meFragment = MeFragment.newInstance();
            transaction.add(R.id.v_content, meFragment, ME_FRAGMENT_TAG);
        }

        //把所有的fragment都托管给fragmentManager， 让fragmentManager帮我们管理这些fragment的状态
        transaction.hide(fileFragment);
        transaction.hide(recentFragment);
        transaction.hide(workGroupFragment);
        transaction.hide(transferFragment);
        transaction.hide(meFragment);

        transaction.commit();
        Toast.makeText(this, currentFragmentTag, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

        Toast.makeText(this, "balala " + currentFragmentTag, Toast.LENGTH_LONG).show();


        switch (view.getId()){
            case R.id.ly_file:
                currentFragmentTag = FILE_FRAGMENT_TAG;
                setCurrentFragment();
                break;
            case R.id.ly_recent:
                currentFragmentTag = RECENT_FRAGMENT_TAG;
                setCurrentFragment();
                break;
            case R.id.ly_group:
                currentFragmentTag = WORK_GROUP_FRAGMENT_TAG;
                setCurrentFragment();
                break;
            case R.id.ly_transfer:
                currentFragmentTag = TRANSFER_FRAGMENT_TAG;
                setCurrentFragment();
                break;
            case R.id.ly_me:
                currentFragmentTag = ME_FRAGMENT_TAG;
                setCurrentFragment();
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if(currentFragmentTag.equals(FILE_FRAGMENT_TAG) && fileFragment.onKeyBackPressed())
            ;
        else
            super.onBackPressed();
    }
}
