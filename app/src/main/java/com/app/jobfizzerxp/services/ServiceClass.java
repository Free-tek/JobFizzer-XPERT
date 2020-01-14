package com.app.jobfizzerxp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.app.jobfizzerxp.utilities.events.MessageEvent;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.GET_ONLINE;
import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.IS_OFFLINE;
import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.IS_ONLINE;
import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.RECEVICE_MESSAGE;
import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.SEND_DELIVERED;
import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.SEND_MESSAGE;

/*
 * Created by yuvaraj on 02/12/17.
 */

public class ServiceClass extends Service {
    public static Socket socket;
    private static String TAG = ServiceClass.class.getSimpleName();
    private AppSettings appSettings = new AppSettings(ServiceClass.this);
    private Boolean UpdatedOff = false;
    private Handler handler = new Handler();
    private static String my_id;
    private Handler handleroffline = new Handler();
    private boolean Updatedon = false;
    private Runnable statusCheck = new Runnable() {
        @Override
        public void run() {
            Boolean isBacgkround = BaseUtils.isAppIsInBackground(ServiceClass.this);
            if (isBacgkround) {
                if (!UpdatedOff) {
                    Emitters emitters = new Emitters(ServiceClass.this);
                    // emitters.emitoffline();
                    UpdatedOff = true;
                    Updatedon = false;
                }
            } else {
                if (!Updatedon) {
                    Emitters emitters = new Emitters(ServiceClass.this);
                    // emitters.emitnewonline();
                    UpdatedOff = false;
                    Updatedon = true;
                }
            }
            handleroffline.postDelayed(this, 1000);
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.post(statusCheck);
            Emitters emitters = new Emitters(ServiceClass.this);
            emitters.emitonline();
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        my_id = appSettings.getProviderId();

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;

        BaseUtils.log("service: ", "MainActivity");
        try {
            socket = IO.socket(UrlHelper.CHAT_SOCKET, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            BaseUtils.log("SOCKET.IO ", e.getMessage());
        }
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnect);
        BaseUtils.log("service: ", "connected");

        socket.on(RECEVICE_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) args[0];
                BaseUtils.log(TAG, "recivedMessage: " + jsonObject);
                try {
                    jsonObject.put("type", "receiver");
                    JSONObject send = new JSONObject();
                    send.put("user_id", my_id);
                    send.put("message_id", jsonObject.optString("message_id"));
                    Emitters emitters = new Emitters(ServiceClass.this);
                    // emitters.sendDelievered(send);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                EventBus.getDefault().post(new MessageEvent(jsonObject));
            }
        });
        return START_STICKY;
    }


    public static class Emitters {
        private AppSettings appSettings;

        public Emitters(Context context) {
            appSettings = new AppSettings(context);
        }


        public void emitonline() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("providerid", appSettings.getProviderId());
                socket.emit(GET_ONLINE, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public void emitoffline() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid", appSettings.getProviderId());
                socket.emit(IS_OFFLINE, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public void emitnewonline() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid", appSettings.getProviderId());
                socket.emit(IS_ONLINE, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public void sendMessage(JSONObject jsonObject) {
            BaseUtils.log(TAG, "sendMessage: " + jsonObject);
            socket.emit(SEND_MESSAGE, jsonObject);

        }

        public void sendDelievered(JSONObject jsonObject) {
            socket.emit(SEND_DELIVERED, jsonObject);
        }
    }
}