package com.krushna.veginew.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.krushna.veginew.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public static RetrofitService create() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.networkInterceptors().add(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                if (SessionManager.USER_ID != null && !SessionManager.USER_ID.isEmpty()) {
//                    requestBuilder.header("token", SessionManager.userToken);
                    requestBuilder.header("userId", SessionManager.USER_ID);
                }
                return chain.proceed(requestBuilder.build());
            }
        });
//                .addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(client.addInterceptor(interceptor).build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);


    }
}
