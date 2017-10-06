package com.safercript.testhttprequest.dao;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.safercript.testhttprequest.MyHttpRequestApp;
import com.safercript.testhttprequest.api.RetrofitService;
import com.safercript.testhttprequest.entity.ImageToSend;
import com.safercript.testhttprequest.entity.User;
import com.safercript.testhttprequest.entity.response.ResponseAllArray;
import com.safercript.testhttprequest.entity.response.ResponseError;
import com.safercript.testhttprequest.entity.response.ResponseImage;
import com.safercript.testhttprequest.entity.response.ResponseUserLogin;
import com.safercript.testhttprequest.util.AppConstants;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerDao extends ObjectDao {
    private static final String LOG_TAG = ManagerDao.class.getSimpleName();

    private RetrofitService retrofitService;


    public ManagerDao() {
        retrofitService = MyHttpRequestApp.getRetrofitService();
    }

    public void init(Handler handler){
        super.init(handler);

    }

    public void signUP(User user){
        if (user == null){
            error(AppConstants.HANDLER_RESULT_ERROR);
            return;
        }

        RequestBody userName = RequestBody.create(
                MediaType.parse("multipart/form-data"), user.getUserName());

        RequestBody userEmail = RequestBody.create(
                MediaType.parse("multipart/form-data"), user.getUserEmail());

        RequestBody password = RequestBody.create(
                MediaType.parse("multipart/form-data"), user.getUserPassword());

        File imageAvatar = new File(user.getAvatar());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageAvatar);
        // MultipartBody.Part используется, чтобы передать имя файла
        MultipartBody.Part bodyAvatar = MultipartBody.Part.createFormData("avatar", imageAvatar.getName(), requestFile);


        Call<ResponseUserLogin> call = retrofitService.requestSignUp(userName, userEmail, password, bodyAvatar);
        call.enqueue(new Callback<ResponseUserLogin>() {
            @Override
            public void onResponse(Call<ResponseUserLogin> call,
                                   Response<ResponseUserLogin> response) {
                if (response.isSuccessful()){
                    Log.v("Upload", "success" + response.body());
                    success(AppConstants.HANDLER_RESULT_OK, (ResponseUserLogin) response.body());
                }else {
                    Log.d("Upload", "error " + response.errorBody() + "/ " + response.body() + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseUserLogin> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void signIn(User user) {
        if (user == null) {
            error(AppConstants.HANDLER_RESULT_ERROR);
            return;
        }

        UserSignIn body = new UserSignIn(user.getUserEmail(), user.getUserPassword());
        Call<ResponseUserLogin> call = retrofitService.requestSignIn(body);
        call.enqueue(new Callback<ResponseUserLogin>() {
            @Override
            public void onResponse(Call<ResponseUserLogin> call,
                                   Response<ResponseUserLogin> response) {
                if (response.isSuccessful()){
                    Log.v("Upload", "success" + response.body());
                    success(AppConstants.HANDLER_RESULT_OK, (ResponseUserLogin) response.body());
                }else {
                    if (response.code() == 400) {
                        Log.v("Error code 400", response.errorBody().toString());
                    }
                    Log.d("Upload", "error " + response.errorBody() + "/ " + response.body() + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseUserLogin> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void getAllImages(String token){

        Call<ResponseAllArray> call = retrofitService.getAll(token);
        call.enqueue(new Callback<ResponseAllArray>() {
            @Override
            public void onResponse(Call<ResponseAllArray> call,
                                   Response<ResponseAllArray> response) {
                if (response.isSuccessful()){
                    Log.v("Upload", "success" + response.body());
                    success(AppConstants.HANDLER_RESULT_OK, (ResponseAllArray) response.body());
                }else {
                    if (response.code() == 400) {
                        Log.v("Error code 400", response.errorBody().toString());
                    }
                    Log.d("Upload", "error " + response.errorBody() + "/ " + response.body() + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseAllArray> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void getGif(String token, String weather){

        Call<GifResponse> call = retrofitService.getGif(token, weather);
        call.enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call,
                                   Response<GifResponse> response) {
                if (response.isSuccessful()){
                    Log.v("Upload", "success" + response.body());
                    success(AppConstants.HANDLER_RESULT_GIF, (GifResponse) response.body());
                }else {
                    if (response.code() == 400) {
                        Log.v("Error code 400", response.errorBody().toString());
                    }
                    Log.d("Upload", "error " + response.errorBody() + "/ " + response.body() + response.message());
                }
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void addImage(ImageToSend imageToSend, String token){
        if (imageToSend == null || imageToSend.getImagePath() == null){
            error(AppConstants.HANDLER_RESULT_ERROR);
            return;
        }

        File imageFile = new File(imageToSend.getImagePath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        // MultipartBody.Part используется, чтобы передать имя файла
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        RequestBody description = RequestBody.create(
                MediaType.parse("form-data"), imageToSend.getDescription());

        RequestBody hashTag = RequestBody.create(
                MediaType.parse("form-data"), imageToSend.getHashTag());

        RequestBody latitude = RequestBody.create(
                MediaType.parse("form-data"), imageToSend.getLatitude());

        RequestBody longitude = RequestBody.create(
                MediaType.parse("form-data"), imageToSend.getLongitude());


        Call<ResponseImage> call = retrofitService.uploadImage(token, bodyImage, description, hashTag, latitude, longitude);
        call.enqueue(new Callback<ResponseImage>() {
            @Override
            public void onResponse(Call<ResponseImage> call,
                                   Response<ResponseImage> response) {
                if (response.isSuccessful()){
                    Log.v("Upload", "success" + response.body());
                    success(AppConstants.HANDLER_RESULT_UPLOAD_IMAGE, (ResponseImage) response.body());
                }else {
                    if (response.code() == 400) {
                        ResponseError responseError;
                        try {
                            Gson gson = new Gson();
                            responseError = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                            success(AppConstants.HANDLER_RESULT_ERROR, responseError);
                            Log.e(LOG_TAG, responseError.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            success(AppConstants.HANDLER_RESULT_ERROR);
                        }
                    }
                        if (response.code() == 403) {
                        success(AppConstants.HANDLER_RESULT_ERROR_TOKEN, response.body());
                        Log.v("Error code 403", response.errorBody().toString());
                    }
                    Log.d("Upload", "error " + response.errorBody() + "/ " + response.body() + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseImage> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                success(AppConstants.HANDLER_RESULT_ERROR);
            }
        });
    }

    public class UserSignIn{
        private String email;
        private String password;

        private UserSignIn(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public class GifResponse{
        private String gif;

        public String getGif() {
            return gif;
        }

        public void setGif(String gif) {
            this.gif = gif;
        }
    }

}
