package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.UpdateProfileApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class EditProfileRepository {
    public LiveData<UpdateProfileApiModel> updateProfile(InputForAPI inputForAPI) {
        final MutableLiveData<UpdateProfileApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                UpdateProfileApiModel updateProfileApiModel = gson.fromJson(response.toString(), UpdateProfileApiModel.class);
                liveData.setValue(updateProfileApiModel);
            }

            @Override
            public void setResponseError(String error) {

                UpdateProfileApiModel updateProfileApiModel = new UpdateProfileApiModel();
                updateProfileApiModel.setError(true);
                updateProfileApiModel.setErrorMessage(error);
                liveData.setValue(updateProfileApiModel);

            }
        });
        return liveData;
    }
}