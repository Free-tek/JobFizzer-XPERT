package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.viewScheduleApi.UpdateScheduleApiModel;
import com.app.jobfizzerxp.model.viewScheduleApi.ViewScheduleApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ViewScheduleRepository {
    public LiveData<ViewScheduleApiModel> viewSchedule(InputForAPI inputForAPI) {
        final MutableLiveData<ViewScheduleApiModel> liveData = new MutableLiveData<>();

        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ViewScheduleApiModel aboutUsModel = gson.fromJson(response.toString(), ViewScheduleApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                ViewScheduleApiModel aboutUsModel = new ViewScheduleApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<UpdateScheduleApiModel> updateSchedule(InputForAPI inputForAPI) {
        final MutableLiveData<UpdateScheduleApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                UpdateScheduleApiModel aboutUsModel = gson.fromJson(response.toString(), UpdateScheduleApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                UpdateScheduleApiModel aboutUsModel = new UpdateScheduleApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }
}