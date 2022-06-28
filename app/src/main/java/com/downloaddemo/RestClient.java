package com.downloaddemo;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient implements IRestClient {

    private Retrofit retrofit;
    private static final int READ_TIMEOUT_MILLISECONDS = 5* 60 * 1000;     // 5 mins
    private static final int CONNECTION_TIMEOUT_MILLISECONDS = 1 * 60 * 1000;      // 60 sec
    private DownloadProgressListener listener;

    public RestClient(String baseUrl) {
        setUpClient(baseUrl);
    }

    public RestClient(String baseUrl, Activity activity) {
        listener = (DownloadProgressListener)activity;
        setUpClient(baseUrl);
    }

    @Override
    public void setUpClient(String baseUrl) {

        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);

         Gson gson = new GsonBuilder()
                .setLenient()
                .create();

         OkHttpClient okHttpClient =new OkHttpClient().newBuilder().readTimeout(READ_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                 .connectTimeout(CONNECTION_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                 .addInterceptor(interceptor)
                 .addInterceptor(chain -> {
                  Request newRequest  = chain.request().newBuilder()
                             .addHeader("x-functions-key", "CQMN4rq2pa08wpDETqMAHaJzyzAzgxxEKdq1yzYJSHPpO9feDhC5FA==")
                             .addHeader("Ocp-Apim-Subscription-Key", Constants.KEY_UAT_SUBSCRIPTION_KEY)
                             .build();
                     return chain.proceed(newRequest);
                 })
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public Retrofit getClient() {
        return retrofit;
    }
}
