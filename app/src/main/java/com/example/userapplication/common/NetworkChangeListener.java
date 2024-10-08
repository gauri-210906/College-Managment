package com.example.userapplication.common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkDetails;

public class NetworkChangeListener extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        if (!NetworkDetails.isConnectedToInternet(context)){

            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_connection_dialog,null);
            ad.setView(layout_dialog);

            // used to store xml file in java class - View
            // LayoutInflater = layout call

            AppCompatButton btnRetry = layout_dialog.findViewById(R.id.btnCheckInternetConnectionRetry);

            AlertDialog alertDialog = ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });


        } else {

        }

    }
}
