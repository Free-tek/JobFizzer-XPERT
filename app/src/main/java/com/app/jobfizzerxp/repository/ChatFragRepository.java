package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.chatListApi.ChatListApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChatFragRepository {
    public LiveData<ChatListApiModel> chatList(InputForAPI inputForAPI) {

        final MutableLiveData<ChatListApiModel> mutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ChatListApiModel chatListApiModel = gson.fromJson(response.toString(), ChatListApiModel.class);
                mutableLiveData.setValue(chatListApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ChatListApiModel chatListApiModel = new ChatListApiModel();
                chatListApiModel.setError(true);
                chatListApiModel.setErrorMessage(error);
                mutableLiveData.setValue(chatListApiModel);
            }
        });
        return mutableLiveData;
    }
}