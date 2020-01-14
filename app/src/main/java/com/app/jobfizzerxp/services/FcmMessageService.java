package com.app.jobfizzerxp.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.app.jobfizzerxp.utilities.events.Status;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.view.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.app.jobfizzerxp.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.HOME_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;


/*

 * Created by user on 25-10-2017.
 */

public class FcmMessageService extends FirebaseMessagingService {
    private static final String TAG = FcmMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        BaseUtils.log(TAG, "onMessageReceived: " + remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            BaseUtils.log(TAG, "getData: " + remoteMessage.getData());
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            sendNotification(jsonObject);
        }
        if (remoteMessage.getNotification() != null) {
            BaseUtils.log(TAG, "getNotification: " + remoteMessage.getNotification());
        }
        EventBus.getDefault().post(new Status());
    }

    private void sendNotification(JSONObject jsonObject) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LOGIN_TYPE, HOME_TAB);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(jsonObject.optString("title"))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        if (jsonObject.has("body")) {
            notificationBuilder.setContentText(jsonObject.optString("body"));
        } else {
            notificationBuilder.setContentText(jsonObject.optString("message"));

        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String NOTIFICATION_CHANNEL_ID = "111";
                String NOTIFICATION_NAME = "jobfizzerxp";
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_NAME, importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.WHITE);
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}