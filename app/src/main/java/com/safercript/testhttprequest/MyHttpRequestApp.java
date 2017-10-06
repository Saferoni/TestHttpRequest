package com.safercript.testhttprequest;

import android.app.Application;

import com.safercript.testhttprequest.api.RetrofitService;
import com.safercript.testhttprequest.dao.ManagerDao;
import com.safercript.testhttprequest.util.AppConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyHttpRequestApp extends Application {

    private static RetrofitService retrofitService;
    private ManagerDao managerDao;

    @Override
    public void onCreate() {
        super.onCreate();

        createRetrofitService();

        SharedInfo.get();

        managerDao = new ManagerDao();
    }

    private void createRetrofitService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitService = retrofit.create(RetrofitService.class);
    }

    public static RetrofitService getRetrofitService() {
        return retrofitService;
    }

    public ManagerDao getManagerDao(){
        return managerDao;
    }

}
