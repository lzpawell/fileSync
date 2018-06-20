package xin.awell.filesync.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.GetJwtCallback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.model.User;
import xin.awell.filesync.model.WorkGroup;
import xin.awell.filesync.util.JwtUtil;

public class NetService {

    private NetService(){

    }


    public static void init(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .readTimeout(6,TimeUnit.SECONDS)
                .writeTimeout(6,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://39.108.65.230:8080/")
                /*.enableJwt(() -> {
                    String jwt = JwtUtil.getJwt();
                    return jwt == null?"error":jwt;
                })*/
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        iNetService = retrofit.create(INetService.class);
    }



    private static INetService iNetService;

    public static INetService getInstance(){
        return iNetService;
    }


    public static MultipartBody.Part wrapFileForUpload(File file){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploadFile", file.getName(), requestFile);

        return part;
    }
}
