package com.app.jobfizzerxp.services;

import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by user on 25-10-2017.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
       BaseUtils.log(TAG, "sendRegistrationToServer: " + token);
        AppSettings appSettings = new AppSettings(FirebaseIdService.this);
        appSettings.setFireBaseToken(token);

    }
}