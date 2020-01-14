package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.CancelJobApiModel;
import com.app.jobfizzerxp.model.elapsedTime.ElapsedTimeApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class DetailedBookingActRepository {
    public LiveData<CancelJobApiModel> cancelJobApi(InputForAPI inputForAPI) {
        final MutableLiveData<CancelJobApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                CancelJobApiModel aboutUsModel = gson.fromJson(response.toString(), CancelJobApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {
                CancelJobApiModel aboutUsModel = new CancelJobApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);
            }
        });
        return liveData;
    }

    public LiveData<ElapsedTimeApiModel> elapsedTime(InputForAPI inputForAPI) {
        final MutableLiveData<ElapsedTimeApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ElapsedTimeApiModel elapsedTimeApiModel = gson.fromJson(response.toString(), ElapsedTimeApiModel.class);
                liveData.setValue(elapsedTimeApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ElapsedTimeApiModel elapsedTimeApiModel = new ElapsedTimeApiModel();
                elapsedTimeApiModel.setError(true);
                elapsedTimeApiModel.setError_message(error);
                liveData.setValue(elapsedTimeApiModel);
            }
        });
        return liveData;
    }
}