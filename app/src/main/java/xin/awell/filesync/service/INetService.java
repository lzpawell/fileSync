package xin.awell.filesync.service;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Jwt;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import xin.awell.filesync.model.LoginResult;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.model.User;
import xin.awell.filesync.model.WorkGroup;

public interface INetService {

    @Headers("ContentType:application/json")
    @POST("/api/user/login")
    Call<LoginResult> login(@Body User user);

    @POST("/api/user/registry")
    Call<LoginResult> registry(@Body User user);

    @Jwt
    @GET("/api/user/{userId}/info")
    Call<User> getUserInfo(@Path("userId") String userId);


    @Jwt
    @POST("/api/share/create")
    Call<Integer> createWorkGroup(WorkGroup workGroup);

    @Jwt
    @GET("/api/share/{groupId}/members")
    Call<String> getGroupMembers(String groupId);

    @Jwt
    @POST("/api/share/{groupId}/members")
    Call<String> joinGroupMembers(WorkGroup workGroup, String userId);

    @Jwt
    @DELETE("/api/share/{groupId}/members")
    Call<String> quitGroup();


    @Multipart
    @POST("/api/file/upload/{owner}/{parentFolder}")
    Call<List<MyFile>> uploadFile(@Path("owner") String owner, @Path("parentFolder") String parentFolder, @Part MultipartBody.Part uploadFilePart);


    @GET("/api/file/check-upload-progress")
    Call<JSONObject> checkUploadProgress(@Query("owner") String owner, @Query("filePath") String filePath);

    @GET()
    Call<List<MyFile>> listFile(@Url String url);

    @FormUrlEncoded
    @POST("/api/file/create-folder/")
    Call<JSONObject> createDir(@Field("userId") String userId, @Field("folderPath") String folderPath);


    @DELETE
    Call<JSONObject> deleteFile(@Url String url);


    @GET("/api/share/list")
    Call<List<WorkGroup>> listGroup(@Query("userId")String userId);
}
