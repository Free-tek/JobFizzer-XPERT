package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.AboutUsResponseModel;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AboutUsRepository {
    public LiveData<AboutUsResponseModel> aboutUs(InputForAPI inputForAPI) {
        final MutableLiveData<AboutUsResponseModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                AboutUsResponseModel aboutUsModel = gson.fromJson(response.toString(), AboutUsResponseModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                AboutUsResponseModel aboutUsModel = new AboutUsResponseModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }
}