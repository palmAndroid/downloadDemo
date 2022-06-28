package com.downloaddemo;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class MainActivity extends AppCompatActivity implements DownloadProgressListener, ConnectivityReceiverInternet.ConnectivityReceiverListener {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TrafficSpeedMeasurer mTrafficSpeedMeasurer;
    private static final boolean SHOW_SPEED_IN_BITS = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();
        MyApplication.getInstance().setConnectivityListener(this);

        mTrafficSpeedMeasurer = new TrafficSpeedMeasurer(TrafficSpeedMeasurer.TrafficType.ALL);
        mTrafficSpeedMeasurer.startMeasuring();

        String newToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiIyY2FjNGNjNy0wYzIwLTRkOGMtOTg5NC1lZDdmNWJiZGQ5MWMiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vY2Q5OWZlZjgtMWNkMy00YTJhLTliZGYtMTU1MzExODFkNjVlL3YyLjAiLCJpYXQiOjE2MzY5NjgwNDIsIm5iZiI6MTYzNjk2ODA0MiwiZXhwIjoxNjM2OTcxOTQyLCJhaW8iOiJBVlFBcS84VEFBQUF3c0lhYTVEMWJzTXdWaThqZytCa1ZTSC9JaXoxNm9zcDFaMGYvVW1VUm9mdU1UK0doR2hxNXBXeHovVVNlanhsVVhkKzJ4ZGw0eGJod2I2d1psTGZJbWdUQXduRXEyOVBTOFczanc1RUx0WT0iLCJjdHJ5IjoiU0ciLCJuYW1lIjoiQW5raXRhIFNodWtsYSAoREhMIFNHKSwgZXh0ZXJuYWwiLCJvaWQiOiI4NzMyNGIwNy02MmMwLTQ3NDUtODZjOC1lMGVlNjdlZWM1MGEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbmtpdGEuc2h1a2xhQGRobC5jb20iLCJyaCI6IjAuQVJFQS1QNlp6ZE1jS2txYjN4VlRFWUhXWHNkTXJDd2dESXhObUpUdGYxdTkyUndSQU84LiIsInJvbGVzIjpbIlNTQ0lNX0lUX0FETUlOIiwiU1NDSU1fQ09HX0FETUlOIiwiU1NDSU1fQ09HX1VTRVIiLCJTU0NJTV9PUFNfVVNFUiJdLCJzdWIiOiJ5N0ZBRmVTbnpzcXJBamtIanhDeTlORzloQ3FabjZfbHdQcTc2STZhSUJnIiwidGlkIjoiY2Q5OWZlZjgtMWNkMy00YTJhLTliZGYtMTU1MzExODFkNjVlIiwidXRpIjoiTjVzcm84b1NxMGFDUWJxYkd1ZC1BQSIsInZlciI6IjIuMCJ9.BcF6ZHzxFcSV3zjJvr3gF3-kzpGKQF6FfMKGtLNXwp2eDuW8gaG_wetY74TPlJDPNiIt9UlY3ULZ94Nw9zv65JUvMX3vKJUVcvXXOHcdlts9fpaV01QdDC2hmGdDYVg10Mxrv3vMq9ICoKtLC5R5fFgBZQ1KJ4lADTHMGXbi5xfg8FHJ6-woK-pZTDjqe8fLVBNYMfrTpJlXJA8s2yBY4KJgXiWEOp8E7gG3_XqSnOX0PO43gKYeF_ql9y97SaHnN_O5OUE2lnbFpbPWroUq7sX90I2I2WBgyVRj_CfoE2LZzgYKhkkR4obLuW8Q63r73000Ln61NJ0ve8YPRY6oDQ";
        LiveData<DownloadResponse> getDataObservable =
                WebRepo.getInstance().doDownload(this , newToken);
    }

     private void checkConnection() {
         boolean isConnected = ConnectivityReceiverInternet.isConnected();
         Log.d("Tag","MainActivity internet connect 33 :"+isConnected);

     }

     @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        int progress = (int) ((bytesRead * 100) / contentLength);
        Log.d("Tag","MainActivity Progress:"+progress);

    }
    String PERMISSION_STRING_PRIVATE_RECEIVER = "YOU_NEED_THIS_TO_RECEIVE_THIS_BROADCAST";

    @Override
    protected void onStart() {
        super.onStart();
       IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
       registerReceiver(networkChangeListener,intentFilter,PERMISSION_STRING_PRIVATE_RECEIVER,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTrafficSpeedMeasurer.registerListener(mStreamSpeedListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
      unregisterReceiver(networkChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTrafficSpeedMeasurer.stopMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTrafficSpeedMeasurer.removeListener();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d("Tag","MainActivity internet connect :"+isConnected);
    }
    private ITrafficSpeedListener mStreamSpeedListener = (upStream, downStream) -> runOnUiThread(new Runnable() {
        @Override
        public void run() {
            String upStreamSpeed = Utils.parseSpeed(upStream, SHOW_SPEED_IN_BITS);
            String downStreamSpeed = Utils.parseSpeed(downStream, SHOW_SPEED_IN_BITS);
            Log.d("Tag","Up Stream Speed: " + upStreamSpeed + "\n" + "Down Stream Speed: " + downStreamSpeed);
        }
    });
}