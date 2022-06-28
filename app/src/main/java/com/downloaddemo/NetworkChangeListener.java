package com.downloaddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Tag","NetworkChangeListener onReceive:");

        if(!Common.isConnectedToInternet(context)){
            Log.d("Tag","NetworkChangeListener onReceive connect:");
            AlertDialog.Builder builder =  new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.no_internet_connection,null);
            builder.setView(view);

            Button btnRetry = view.findViewById(R.id.btn_retry);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(view1 -> {
                dialog.dismiss();
                onReceive(context,intent);
            });
        }
    }
}