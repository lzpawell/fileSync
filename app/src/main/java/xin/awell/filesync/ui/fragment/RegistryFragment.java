package xin.awell.filesync.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xin.awell.filesync.R;
import xin.awell.filesync.databinding.FragmentRegistryBinding;
import xin.awell.filesync.model.LoginResult;
import xin.awell.filesync.model.NetworkError;
import xin.awell.filesync.model.User;
import xin.awell.filesync.service.INetService;
import xin.awell.filesync.service.NetService;

/**
 * create an instance of this fragment.
 * 需要绑定 LoginResultListener
 */
public class RegistryFragment extends Fragment {

    private FragmentRegistryBinding binding;

    public RegistryFragment() {
        // Required empty public constructor
    }


    public static RegistryFragment newInstance() {
        RegistryFragment fragment = new RegistryFragment();
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registry,container, false);
        initView();
        return binding.getRoot();
    }


    private INetService netService = NetService.getInstance();

    private void initView(){
        binding.btnLogin.setOnClickListener((view)->{
            String userId;
            String password;
            String confirm;
            String nickname;

            userId = binding.edtUserId.getText().toString().trim();
            password = binding.edtPassword.getText().toString().trim();
            confirm = binding.edtConfirmPassword.getText().toString().trim();
            nickname = binding.edtNickname.getText().toString().trim();

            if(!userId.equals("") && password.equals(confirm) && !password.equals("")){
                User user = new User();
                user.setUserId(userId);
                user.setPassword(password);
                user.setNickname(nickname);

                //registry user
                Call<LoginResult> call = netService.registry(user);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.code() == 200){
                            Toast.makeText(getContext(), "registry success " + response.body().getJwt(), Toast.LENGTH_SHORT).show();
                            mListener.onLoginResult(LoginFragment.LOGIN_SUCCESS);
                        }else{
                            try {
                                NetworkError error = JSON.parseObject(response.errorBody().string(), NetworkError.class);
                                onFailure(call, new Exception(error.getMessage()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable throwable) {
                        Toast.makeText(getContext(), "registry failed "+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        mListener.onLoginResult(LoginFragment.LOGIN_FAILED);
                    }
                });

            }else{
                Toast.makeText(getContext(), "密码校验失败, 两次密码不一致！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private LoginFragment.OnLoginListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnLoginListener) {
            mListener = (LoginFragment.OnLoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
