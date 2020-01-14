package com.app.jobfizzerxp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.interfaces.ConnectivityListener;

/**
 * no internet toast
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    private ConnectivityListener connectivityListener;
    private static String TAG = ConnectivityReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        onReceive(context);
    }

    public void setConnectivityListener(ConnectivityListener connectivityListener) {
        this.connectivityListener = connectivityListener;
    }

    public void onReceive(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cn != null) {
            networkInfo = cn.getActiveNetworkInfo();
        }

        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                BaseUtils.log(TAG, "Connected");
                connectivityListener.onNetworkConnectionChanged(true);

            } else {
                connectivityListener.onNetworkConnectionChanged(false);
            }
        } else {
            connectivityListener.onNetworkConnectionChanged(false);
        }
    }
}