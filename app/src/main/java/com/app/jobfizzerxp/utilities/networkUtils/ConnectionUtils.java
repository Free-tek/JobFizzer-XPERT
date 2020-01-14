package com.app.jobfizzerxp.utilities.networkUtils;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionUtils {


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        } else {
            return false;
        }
    }
}