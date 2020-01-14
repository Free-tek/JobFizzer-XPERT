package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.LogOutApiModel;
import com.app.jobfizzerxp.model.viewProfileApi.ViewProfileApiModel;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AccountsFragmentRepository {

    public LiveData<LogOutApiModel> logOut(InputForAPI inputForAPI) {
        final MutableLiveData<LogOutApiModel> liveData = new MutableLiveData<>();

        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                LogOutApiModel mainActivityResponseModel = gson.fromJson(response.toString(),
                        LogOutApiModel.class);
                liveData.setValue(mainActivityResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                LogOutApiModel logOutApiModel = new LogOutApiModel();
                logOutApiModel.setError(true);
                logOutApiModel.setErrorMessage(error);
                liveData.setValue(logOutApiModel);

            }
        });
        return liveData;
    }

    public LiveData<ViewProfileApiModel> viewProfileApi(InputForAPI inputForAPI) {
        final MutableLiveData<ViewProfileApiModel> liveData = new MutableLiveData<>();


        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ViewProfileApiModel viewProfileApiModel = gson.fromJson(response.toString(),
                        ViewProfileApiModel.class);
                liveData.setValue(viewProfileApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ViewProfileApiModel viewProfileApiModel = new ViewProfileApiModel();
                viewProfileApiModel.setError(true);
                viewProfileApiModel.setErrorMessage(error);
                liveData.setValue(viewProfileApiModel);

            }
        });
        return liveData;
    }
}