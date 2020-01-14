package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.messageListApi.ChatMessageApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChatActRepository {
    public LiveData<ChatMessageApiModel> chatMessageList(InputForAPI inputForAPI) {

        final MutableLiveData<ChatMessageApiModel> mutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ChatMessageApiModel chatMessageApiModel = gson.fromJson(response.toString(), ChatMessageApiModel.class);
                mutableLiveData.setValue(chatMessageApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ChatMessageApiModel chatMessageApiModel = new ChatMessageApiModel();
                chatMessageApiModel.setError(true);
                chatMessageApiModel.setMessage(error);
                mutableLiveData.setValue(chatMessageApiModel);
            }
        });
        return mutableLiveData;
    }
}