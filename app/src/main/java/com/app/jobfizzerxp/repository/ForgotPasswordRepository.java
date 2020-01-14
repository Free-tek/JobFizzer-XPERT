package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.ForgotPasswordResponseModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ForgotPasswordRepository {
    public LiveData<ForgotPasswordResponseModel> requestOtp(InputForAPI inputForAPI) {

        final MutableLiveData<ForgotPasswordResponseModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ForgotPasswordResponseModel forgotPasswordResponseModel = gson.fromJson(response.toString(), ForgotPasswordResponseModel.class);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                ForgotPasswordResponseModel forgotPasswordResponseModel = new ForgotPasswordResponseModel();
                forgotPasswordResponseModel.setError(true);
                forgotPasswordResponseModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }
}