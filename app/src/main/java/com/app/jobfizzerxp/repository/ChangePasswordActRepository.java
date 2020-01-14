package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.ChangePasswordApiModel;
import com.app.jobfizzerxp.model.FlagResponseModel;
import com.app.jobfizzerxp.model.ResetPasswordApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChangePasswordActRepository {
    public LiveData<ChangePasswordApiModel> changePassword(InputForAPI inputForAPI) {

        final MutableLiveData<ChangePasswordApiModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ChangePasswordApiModel changePasswordApiModel = gson.fromJson(response.toString(), ChangePasswordApiModel.class);
                flagResponseModelMutableLiveData.setValue(changePasswordApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ChangePasswordApiModel changePasswordApiModel = new ChangePasswordApiModel();
                changePasswordApiModel.setError(true);
                changePasswordApiModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(changePasswordApiModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }

    public LiveData<ResetPasswordApiModel> resetPassword(InputForAPI inputForAPI) {

        final MutableLiveData<ResetPasswordApiModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ResetPasswordApiModel resetPasswordApiModel = gson.fromJson(response.toString(), ResetPasswordApiModel.class);
                flagResponseModelMutableLiveData.setValue(resetPasswordApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ResetPasswordApiModel resetPasswordApiModel = new ResetPasswordApiModel();
                resetPasswordApiModel.setError(true);
                resetPasswordApiModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(resetPasswordApiModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }

    public LiveData<FlagResponseModel> flagUpdate(InputForAPI inputForAPI) {
        final MutableLiveData<FlagResponseModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                FlagResponseModel resetPasswordApiModel = gson.fromJson(response.toString(), FlagResponseModel.class);
                flagResponseModelMutableLiveData.setValue(resetPasswordApiModel);
            }

            @Override
            public void setResponseError(String error) {
                flagResponseModelMutableLiveData.setValue(new FlagResponseModel());
            }
        });
        return flagResponseModelMutableLiveData;
    }
}