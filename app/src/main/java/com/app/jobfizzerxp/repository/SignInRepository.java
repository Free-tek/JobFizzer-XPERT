package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.SignInResponseModel;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SignInRepository {

    public LiveData<SignInResponseModel> signIn(InputForAPI inputForAPI) {
        final MutableLiveData<SignInResponseModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SignInResponseModel signInResponseModel = gson.fromJson(response.toString(), SignInResponseModel.class);
                liveData.setValue(signInResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                SignInResponseModel signInResponseModel = new SignInResponseModel();
                signInResponseModel.setError(true);
                signInResponseModel.setErrorMessage(error);
                liveData.setValue(signInResponseModel);

            }
        });
        return liveData;
    }


}