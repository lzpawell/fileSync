package xin.awell.filesync.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xin.awell.filesync.R;
import xin.awell.filesync.databinding.FragmentLoginBinding;
import xin.awell.filesync.model.LoginResult;
import xin.awell.filesync.model.NetworkError;
import xin.awell.filesync.model.User;
import xin.awell.filesync.model.UserLoginSettings;
import xin.awell.filesync.service.INetService;
import xin.awell.filesync.service.NetService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED = -1;


    private FragmentLoginBinding binding;

    private OnLoginListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        initView();

        return binding.getRoot();
    }

    private void initView() {
        binding.tvBtnRegistry.setOnClickListener(this);
        binding.btnLogin.setOnClickListener((view)->{
            login(binding.edtUserId.getText().toString().trim(), binding.edtPassword.getText().toString().trim());
        });


        SharedPreferences preferences = getContext().getSharedPreferences("loginSettings", Context.MODE_PRIVATE);

        String loginSettings = preferences.getString("userLoginSettings", null);

        if(loginSettings == null){
            return;
        }else{
            UserLoginSettings settings = JSON.parseObject(loginSettings, UserLoginSettings.class);

            if(settings.isAutoLogin()){
                login(settings.getUserId(), settings.getPassword());
                return;
            }

            binding.edtUserId.setText(settings.getUserId());
            binding.edtPassword.setText(settings.getPassword());
            binding.cbRememberMe.setChecked(true);
        }

        //UserLoginSettings userLoginSettings = ()

        //editor.putString("userLoginSettings", null);
    }


    private INetService netService = NetService.getInstance();

    public void login(String userId, String password){
        Toast.makeText(getContext(), "start login!", Toast.LENGTH_SHORT).show();
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        Call<LoginResult> call = netService.login(user);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.code() == 200){
                    //if login success
                    Log.i("login", "success! " + response.body().getJwt());
                    User.setCurrentUser(response.body().getUser());
                    mListener.onLoginResult(LOGIN_SUCCESS);
                }else{
                    try {
                        NetworkError error = JSON.parseObject(response.errorBody().string(), NetworkError.class);
                        onFailure(call, new Exception("server response code not 200! " + error.getMessage()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onFailure(Call<LoginResult> call, Throwable throwable) {
                Log.i("login", "failed! " + throwable.getMessage());
                mListener.onLoginResult(LOGIN_FAILED);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginListener) {
            mListener = (OnLoginListener) context;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_btn_registry:
                mListener.onBtnRegistryPressed();
                break;

            default:
                break;
        }
    }

    public interface OnLoginListener {
        void onLoginResult(int result);
        void onBtnRegistryPressed();
    }
}
