package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.beforeAfterImage.BeforeAfterImageModel;
import com.app.jobfizzerxp.model.homeDashboardApi.HomeDashBoardResponseModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class HomeFragmentRepository {

    public LiveData<HomeDashBoardResponseModel> dashBoard(InputForAPI inputForAPI) {
        final MutableLiveData<HomeDashBoardResponseModel> liveData = new MutableLiveData<>();

        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                HomeDashBoardResponseModel dashBoardResponseModel = gson.fromJson(response.toString(),
                        HomeDashBoardResponseModel.class);
                liveData.setValue(dashBoardResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                HomeDashBoardResponseModel dashBoardResponseModel
                        = new HomeDashBoardResponseModel();
                dashBoardResponseModel.setError(true);
                dashBoardResponseModel.setMessage(error);
                liveData.setValue(dashBoardResponseModel);

            }
        });
        return liveData;
    }

    public LiveData<BeforeAfterImageModel> getAfterBeforeImage(InputForAPI inputForAPI) {
        final MutableLiveData<BeforeAfterImageModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                BeforeAfterImageModel dashBoardResponseModel = gson.fromJson(response.toString(),
                        BeforeAfterImageModel.class);
                liveData.setValue(dashBoardResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                BeforeAfterImageModel beforeAfterImageModelResponseModel
                        = new BeforeAfterImageModel();
                beforeAfterImageModelResponseModel.setError(true);
                beforeAfterImageModelResponseModel.setErrorMessage(error);
                liveData.setValue(beforeAfterImageModelResponseModel);

            }
        });
        return liveData;
    }


}