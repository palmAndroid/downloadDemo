//package com.downloaddemo;
//
//import static com.dhl.sscim.utils.Utilities.outputObservable;
//
//import android.app.Activity;
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.dhl.sscim.TabDemoApplication;
//import com.dhl.sscim.db.BillRepo;
//import com.dhl.sscim.entity.request.BoxSyncListRequest;
//import com.dhl.sscim.entity.response.BBXPieceInfoEntity;
//import com.dhl.sscim.entity.response.SyncApiResponse;
//import com.dhl.sscim.network.CountingRequestBody;
//import com.dhl.sscim.preference.Preference;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class WebServicesRepository {
//
//    private ApiInterface apiService;
//
//
//    private static class SingletonHelper
//    {
//        private static final WebServicesRepository INSTANCE = new WebServicesRepository();
//    }
//
//    public static WebServicesRepository getInstance()
//    {
//        return SingletonHelper.INSTANCE;
//    }
//
//    private WebServicesRepository()
//    {
//        apiService = new RestClient(Constants.BASE_URL).getClient()
//                .create(ApiInterface.class);
//    }
//
//    /**
//     * @param
//     * @param newToken
//     * @return
//     */
//    public LiveData<DownloadResponse> doDownload(Activity activity, String newToken) {
//        Preference.getInstance(TabDemoApplication.getInstance()).setNetworkCalling(true);
//       MutableLiveData<DownloadResponse> data = new MutableLiveData<>();
//        Call<DownloadResponse> call = apiService.downloadData("Bearer "+newToken);
//        RequestBody requestBody =  call.request().body();
//
//
//         new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
//             double progress = (1.0 * bytesWritten) / contentLength;
//             Log.d(Constants.TAG, "Request Progress:"+progress);
//             outputObservable.postValue(progress);
//         });
//
//        call.enqueue(new Callback<DownloadResponse>() {
//            @Override
//            public void onResponse(Call<DownloadResponse> call, Response<DownloadResponse> response) {
//                Log.d(Constants.TAG,"Download Response Code "+ response.code());
//                if(response.code()==200 && response.body()!=null){
//                    Log.e(Constants.TAG, "Response null body code"+response.code());
//                    List<Download> boxes = response.body().getResults();
//                    if (boxes!=null && !boxes.isEmpty()) {
//                       /* for (int i = 0 ; i<boxes.size(); i++){
//                            outputObservable.postValue(i);
//                        }*/
//                        long tx = response.raw().sentRequestAtMillis();
//                        long rx = response.raw().receivedResponseAtMillis();
//                        System.out.println("response time : "+(rx - tx)+" ms");
//                        data.setValue(response.body());
//                        new BillRepo(activity).insertAll(response.body().getResults());
//                    }else{
//                        Log.e(Constants.TAG, "Response null box data code "+response.code());
//                        data.setValue(setBoxData("No Box data is available to download!"));
//                        Preference.getInstance(TabDemoApplication.getInstance()).setNetworkCalling(false);
//                    }
//                }else if(response.code()==200 && response.body()==null){
//                    Log.e(Constants.TAG, "Response null body code"+response.code());
//                    data.setValue(setBoxData("No Box data is available to download!"));
//                    Preference.getInstance(TabDemoApplication.getInstance()).setNetworkCalling(false);
//                }else if(response.code()==404 || response.code()==401){
//                    Log.e(Constants.TAG, "Results helper");
//                    data.setValue(setBoxData("User Validation failed, Please provide valid access token"));
//                  //  new RefreshTokenHelper(activity, Constants.DOWNLOAD_API);
//                    Preference.getInstance(TabDemoApplication.getInstance()).setNetworkCalling(false);
//                }else{
//                    data.setValue(setBoxData("No Box data is available to download!"));
//                    Preference.getInstance(TabDemoApplication.getInstance()).setNetworkCalling(false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DownloadResponse> call, Throwable t) {
//                Log.e(Constants.TAG, t.getMessage());
//                data.setValue(setBoxData(t.getMessage()));
//            }
//        });
//        return data;
//    }
//
//    /**
//     *
//     * @param
//     * @param
//     * @return
//     */
//    public LiveData<SyncApiResponse> doSync(BoxSyncListRequest boxData, String accessToken, Activity activity) {
//        final MutableLiveData<SyncApiResponse> data = new MutableLiveData<>();
//        Call<SyncApiResponse> call = apiService.doSync(boxData, "Bearer "+accessToken);
//        call.enqueue(new Callback<SyncApiResponse>() {
//            @Override
//            public void onResponse(Call<SyncApiResponse> call, Response<SyncApiResponse> response) {
//                Log.d(Constants.TAG,"Sync Response Code "+ response.code());
//                if(response.code() == 200){
//                    data.setValue(response.body());
//                    updateBillLastUpdated(boxData, activity);
//                }else if(response.code()==404 || response.code()==401){
//                    Log.d(Constants.TAG,"API response");
//                    SyncApiResponse download = new SyncApiResponse();
//                    download.setErrorMessage("User Validation failed, Please provide valid access token");
//                    download.setError(true);
//                    data.setValue(download);
//               }else{
//                    SyncApiResponse download = new SyncApiResponse();
//                    download.setErrorMessage("api failed");
//                    download.setError(true);
//                    data.setValue(download);
//                    Log.e(Constants.TAG,"API Failed");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SyncApiResponse> call, Throwable t) {
//                Log.d(Constants.TAG, t.getMessage());
//                SyncApiResponse download = new SyncApiResponse();
//                download.setErrorMessage(t.getMessage());
//                download.setError(true);
//                data.setValue(download);
//            }
//        });
//        return data;
//    }
//
//    public MutableLiveData<BBXPieceInfoEntity> getBBXInfo(String pieceId, String accessToken, Activity activity) {
//        final MutableLiveData<BBXPieceInfoEntity> data = new MutableLiveData<>();
//        Call<BBXPieceInfoEntity> call = apiService.getBBXInfo(pieceId, "Bearer "+accessToken);
//        call.enqueue(new Callback<BBXPieceInfoEntity>() {
//            @Override
//            public void onResponse(Call<BBXPieceInfoEntity> call, Response<BBXPieceInfoEntity> response) {
//                Log.d(Constants.TAG,"BBX response code"+response.code());
//                if (response.isSuccessful()) {
//                    data.setValue(response.body());
//                } else if (response.errorBody() != null) {
//                    BBXPieceInfoEntity bbxPieceInfoEntity = new BBXPieceInfoEntity();
//                    bbxPieceInfoEntity.setError(true);
//                    if(response.code()==404 || response.code()==401){
//                        Log.d(Constants.TAG,"API response");
//                        bbxPieceInfoEntity.setErrorMessage("User Validation failed, Please provide valid access token");
//                    }else{
//                        try {
//                            bbxPieceInfoEntity.setErrorMessage(response.errorBody().string());
//                        } catch (IOException e) {
//                            bbxPieceInfoEntity.setErrorMessage("Api Failed");
//                        }
//                    }
//                    data.setValue(bbxPieceInfoEntity);
//                }
//            }
//            @Override
//            public void onFailure(Call<BBXPieceInfoEntity> call, Throwable t) {
//                BBXPieceInfoEntity bbxPieceInfoEntity = new BBXPieceInfoEntity();
//                bbxPieceInfoEntity.setError(true);
//                bbxPieceInfoEntity.setErrorMessage(t.getLocalizedMessage());
//                data.setValue(bbxPieceInfoEntity);
//            }
//        });
//        return data;
//    }
//
//
//    private void updateBillLastUpdated(BoxSyncListRequest boxData, Activity activity) {
//        List<String> boxIds = new ArrayList<>();
//        for(int i = 0; i< boxData.getBoxData().size(); i++){
//            boxIds.add(boxData.getBoxData().get(i).getBox_id());
//        }
//        new BillRepo(activity).updateBillInfoTiming(boxIds);
//    }
//
//    private DownloadResponse setBoxData(String errorMessage){
//        DownloadResponse download = new DownloadResponse();
//        download.setErrorMessage(errorMessage);
//        download.setError(true);
//        return download;
//    }
//}