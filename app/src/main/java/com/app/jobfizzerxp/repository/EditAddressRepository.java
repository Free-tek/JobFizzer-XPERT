package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.UpdateAddressApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class EditAddressRepository {
    public LiveData<UpdateAddressApiModel> updateAddress(InputForAPI inputForAPI) {
        final MutableLiveData<UpdateAddressApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                UpdateAddressApiModel updateAddressApiModel = gson.fromJson(response.toString(), UpdateAddressApiModel.class);
                liveData.setValue(updateAddressApiModel);
            }

            @Override
            public void setResponseError(String error) {

                UpdateAddressApiModel updateAddressApiModel = new UpdateAddressApiModel();
                updateAddressApiModel.setError(true);
                updateAddressApiModel.setErrorMessage(error);
                liveData.setValue(updateAddressApiModel);

            }
        });
        return liveData;
    }
}