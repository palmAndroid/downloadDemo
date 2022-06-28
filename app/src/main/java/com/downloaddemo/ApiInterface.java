package com.downloaddemo;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiInterface {

    /**
     * @param
     * @return
     */
    @GET(Constants.DOWNLOAD_API)
    Call<DownloadResponse> doDownload(@Query("timestamp") String timestamp);

    @Streaming
    @GET(Constants.DOWNLOAD_API)
    Call<DownloadResponse> downloadData(@Header("Authorization")String newToken);

    @GET(Constants.DOWNLOAD_API)
    Observable<DownloadResponse> downloadDataRX(@Header("Authorization")String newToken);

 /*   @POST(Constants.SYNC_API)
    Call<SyncApiResponse> doSync(@Body BoxSyncListRequest boxData,
                                 @Header("Authorization")String newToken);

    @GET(Constants.BBX_BUILD_API)
    Call<BBXPieceInfoEntity> getBBXInfo(@Query("piece_id") String pieceID,
                                        @Header("Authorization")String newToken);
*/
}