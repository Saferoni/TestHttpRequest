package com.safercript.testhttprequest.api;


import com.safercript.testhttprequest.dao.ManagerDao;
import com.safercript.testhttprequest.entity.response.ResponseAllArray;
import com.safercript.testhttprequest.entity.response.ResponseImage;
import com.safercript.testhttprequest.entity.response.ResponseUserLogin;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService {

    @POST("/create")
    @Multipart
    Call<ResponseUserLogin> requestSignUp(@Part("username") RequestBody userName,
                                          @Part("email") RequestBody userEmail,
                                          @Part("password") RequestBody userPassword,
                                          @Part MultipartBody.Part file);

    @POST("/login")
    Call<ResponseUserLogin> requestSignIn(@Body ManagerDao.UserSignIn user);

    @GET("/all")
    Call<ResponseAllArray> getAll(@Header("token") String token);

    @GET("/gif")
    Call<ManagerDao.GifResponse> getGif(@Header("token") String token, @Query("weather") String weather);

    @POST("/image")
    @Multipart
    Call<ResponseImage> uploadImage(@Header("token") String token,
                                    @Part MultipartBody.Part file,
                                    @Part("description") RequestBody description,
                                    @Part("hashtag") RequestBody hashtag,
                                    @Part("latitude") RequestBody latitude,
                                    @Part("longitude") RequestBody longitude);

}
