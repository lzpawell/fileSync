package xin.awell.filesync.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import xin.awell.filesync.R;
import xin.awell.filesync.databinding.FragmentMeBinding;
import xin.awell.filesync.model.User;
import xin.awell.filesync.ui.activity.LocalSavePosSelectActivity;
import xin.awell.filesync.ui.activity.LoginRegistryActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment implements View.OnClickListener{

    private FragmentMeBinding binding;

    public MeFragment() {
        // Required empty public constructor
    }


    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        initView();
        initData();
        return binding.getRoot();
    }

    private void initData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("defaultSettings", Context.MODE_PRIVATE);
        String filePath = preferences.getString("localCachePath", null);
        if(filePath == null){
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            filePath = file.getAbsolutePath();
            preferences.edit().putString("localCachePath", filePath).commit();
            ((TextView)binding.lyCacheLocation.findViewById(R.id.tv_local_cache_pos)).setText(filePath);
        }
    }

    private void initView() {
        binding.lyQuitAccount.setOnClickListener(this);
        binding.lyCacheLocation.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();

        User user = User.getCurrentUser();
        if(user != null)
            binding.tvNickname.setText(user.getNickname());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_quit_account:

                Intent intent = new Intent(getContext(), LoginRegistryActivity.class);
                startActivity(intent);
                break;

            case R.id.ly_cache_location:
                Intent intent2 = new Intent(getContext(), LocalSavePosSelectActivity.class);
                startActivityForResult(intent2, LocalSavePosSelectActivity.REQUEST_CODE);
                break;
            default:break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocalSavePosSelectActivity.REQUEST_CODE
                && resultCode == LocalSavePosSelectActivity.RESULT_CODE_Y) {
            Log.i("balala", "aowubalalalaa");
            setCacheLocation(data.getStringExtra(LocalSavePosSelectActivity.SELECTED_FILE_PATH));
        }
    }

    public void setCacheLocation(@NonNull String localPath){
        SharedPreferences preferences = getActivity().getSharedPreferences("defaultSettings", Context.MODE_PRIVATE);
        preferences.edit().putString("localCachePath", localPath).commit();
        Toast.makeText(this.getActivity(),"balala  " + localPath, Toast.LENGTH_SHORT).show();
        ((TextView)binding.lyCacheLocation.findViewById(R.id.tv_local_cache_pos)).setText(localPath);

    }
}
