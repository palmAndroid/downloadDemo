package com.downloaddemo;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebRepo{

    private ApiInterface apiService;


    private static class SingletonHelper {
        private static final WebRepo INSTANCE = new WebRepo();
    }

    public static WebRepo getInstance() {
        return WebRepo.SingletonHelper.INSTANCE;
    }

    private WebRepo() {
        apiService = new RestClient(Constants.BASE_URL).getClient()
                .create(ApiInterface.class);
    }


    private WebRepo(Activity activity) {
        apiService = new RestClient(Constants.BASE_URL,activity).getClient()
                .create(ApiInterface.class);
    }

    /**
     * @param
     * @param newToken
     * @return
     */
    public LiveData<DownloadResponse> doDownload(Activity activity, String newToken) {
        MutableLiveData<DownloadResponse> data = new MutableLiveData<>();
        Call<DownloadResponse> call = apiService.downloadData("Bearer " + newToken);
        RequestBody requestBody = call.request().body();

     /*   DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);

                sendNotification(download);
            }
        };
        */
       // apiService.downloadData("Bearer "+newToken).





        call.enqueue(new Callback<DownloadResponse>() {
            @Override
            public void onResponse(Call<DownloadResponse> call, Response<DownloadResponse> response) {
                Log.d(Constants.TAG, "Download Response Code " + response.code());
                if (response.code() == 200 && response.body() != null) {
                    Log.e(Constants.TAG, "Response null body code" + response.code());
                    List<Download> boxes = response.body().getResults();
                    if (boxes != null && !boxes.isEmpty()) {
                        long tx = response.raw().sentRequestAtMillis();
                        long rx = response.raw().receivedResponseAtMillis();
                        System.out.println("response time : " + (rx - tx) + " ms");
                        data.setValue(response.body());
                    } else {
                        Log.e(Constants.TAG, "Response null box data code " + response.code());
                        data.setValue(setBoxData("No Box data is available to download!"));
                    }
                } else if (response.code() == 200 && response.body() == null) {
                    Log.e(Constants.TAG, "Response null body code" + response.code());
                    data.setValue(setBoxData("No Box data is available to download!"));
                } else if (response.code() == 404 || response.code() == 401) {
                    Log.e(Constants.TAG, "Results helper");
                    data.setValue(setBoxData("User Validation failed, Please provide valid access token"));
                    //  new RefreshTokenHelper(activity, Constants.DOWNLOAD_API);
                } else {
                    data.setValue(setBoxData("No Box data is available to download!"));
                }
            }

            @Override
            public void onFailure(Call<DownloadResponse> call, Throwable t) {
                Log.e(Constants.TAG, t.getMessage());
                data.setValue(setBoxData(t.getMessage()));
            }
        });
        return data;
    }
    private DownloadResponse setBoxData(String errorMessage){
        DownloadResponse download = new DownloadResponse();
        download.setErrorMessage(errorMessage);
        download.setError(true);
        return download;
    }
}
