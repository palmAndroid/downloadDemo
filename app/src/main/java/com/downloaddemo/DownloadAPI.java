//package com.downloaddemo;
//
//import android.os.FileUtils;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import org.reactivestreams.Subscriber;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.Flow;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.functions.Action;
//import io.reactivex.rxjava3.functions.Function;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.ResponseBody;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public class DownloadAPI {
//    private static final String TAG = "DownloadAPI";
//    private static final int DEFAULT_TIMEOUT = 15;
//    public Retrofit retrofit;
//
//    public DownloadAPI(String url, DownloadProgressListener listener) {
//
//        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        OkHttpClient okHttpClient =new OkHttpClient().newBuilder()
//                .addInterceptor(interceptor)
//                .retryOnConnectionFailure(true)
//                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .addInterceptor(chain -> {
//                    Request newRequest  = chain.request().newBuilder()
//                            .addHeader("x-functions-key", "CQMN4rq2pa08wpDETqMAHaJzyzAzgxxEKdq1yzYJSHPpO9feDhC5FA==")
//                            .addHeader("Ocp-Apim-Subscription-Key", Constants.KEY_UAT_SUBSCRIPTION_KEY)
//                            .build();
//                    return chain.proceed(newRequest);
//                })
//                .build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(okHttpClient)
//                .build();
//    }
//
//    public void downloadAPK(@NonNull String url, final File file, Subscriber subscriber) {
//        Log.d(TAG, "downloadAPK: " + url);
//
//        ApiInterface  apiService = new RestClient(Constants.BASE_URL).getClient()
//                .create(ApiInterface.class);
//
//     /*   apiService.downloadData(url)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .map();
//*/
//        retrofit.create(ApiInterface.class)
//                .downloadData(url)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .map(new Function<ResponseBody, InputStream>() {
//                    @Override
//                    public InputStream apply(ResponseBody responseBody) throws Throwable {
//                        return responseBody.byteStream();
//                    }
//                })
//                .observeOn(Schedulers.computation())
//                .doOnNext(new Action<InputStream>() {
//                    @Override
//                    public void run() throws Throwable {
//                        try {
//                            FileUtils.writeFile(inputStream, file);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            throw new Exception(e.getMessage(), e);
//                        }
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
//
//
//}
