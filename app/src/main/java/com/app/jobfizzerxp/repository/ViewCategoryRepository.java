package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.viewCategoryApi.AddCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.DeleteCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.EditCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.ViewCategoryApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ViewCategoryRepository {
    public LiveData<AddCategoryApiModel> addCategory(InputForAPI inputForAPI) {
        final MutableLiveData<AddCategoryApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                AddCategoryApiModel aboutUsModel = gson.fromJson(response.toString(), AddCategoryApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                AddCategoryApiModel aboutUsModel = new AddCategoryApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<EditCategoryApiModel> editCategory(InputForAPI inputForAPI) {
        final MutableLiveData<EditCategoryApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                EditCategoryApiModel aboutUsModel = gson.fromJson(response.toString(), EditCategoryApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                EditCategoryApiModel aboutUsModel = new EditCategoryApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<DeleteCategoryApiModel> deleteCategory(InputForAPI inputForAPI) {
        final MutableLiveData<DeleteCategoryApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                DeleteCategoryApiModel aboutUsModel = gson.fromJson(response.toString(), DeleteCategoryApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                DeleteCategoryApiModel aboutUsModel = new DeleteCategoryApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<ViewCategoryApiModel> viewCategory(InputForAPI inputForAPI) {
        final MutableLiveData<ViewCategoryApiModel> liveData = new MutableLiveData<>();

        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ViewCategoryApiModel aboutUsModel = gson.fromJson(response.toString(), ViewCategoryApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                ViewCategoryApiModel aboutUsModel = new ViewCategoryApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }


}