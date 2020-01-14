package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.SignUpApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class RegisterActRepository {
    public LiveData<SignUpApiModel> signUp(InputForAPI inputForAPI) {
        final MutableLiveData<SignUpApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SignUpApiModel signUpApiModel = gson.fromJson(response.toString(), SignUpApiModel.class);
                liveData.setValue(signUpApiModel);
            }

            @Override
            public void setResponseError(String error) {
                SignUpApiModel signUpApiModel = new SignUpApiModel();
                signUpApiModel.setError(true);
                signUpApiModel.setErrorMessage(error);
                liveData.setValue(signUpApiModel);
            }
        });
        return liveData;
    }
}