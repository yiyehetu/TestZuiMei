package com.yph.beauty.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yph
 * Time is 2016/11/30 17:51
 * Good Good Study, Day Day Up
 */

public class ApiManager {
    private Retrofit mRetrofit;

    private ApiManager(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static class InnerApiManager{
        private static ApiManager instance = new ApiManager();
    }

    public static ApiManager getInstance(){
        return InnerApiManager.instance;
    }

    public ApiService getApiService(){
        return mRetrofit.create(ApiService.class);
    }
}
