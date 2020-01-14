package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.providerCalendarApi.ProviderCalendarApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AppointmentActRepository {
    public LiveData<ProviderCalendarApiModel> calendarApi(InputForAPI inputForAPI) {
        final MutableLiveData<ProviderCalendarApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ProviderCalendarApiModel calendarApiModel = gson.fromJson(response.toString(), ProviderCalendarApiModel.class);
                liveData.setValue(calendarApiModel);
            }

            @Override
            public void setResponseError(String error) {
                ProviderCalendarApiModel calendarApiModel = new ProviderCalendarApiModel();
                calendarApiModel.setError(true);
                calendarApiModel.setErrorMessage(error);
                liveData.setValue(calendarApiModel);
            }
        });
        return liveData;
    }
}