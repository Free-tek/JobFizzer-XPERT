package com.app.jobfizzerxp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzerxp.model.googleMaps.GoogleMapApiModel;
import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.PaymentConfirmationApi;
import com.app.jobfizzerxp.model.ReviewModel;
import com.app.jobfizzerxp.model.UpdateDeviceApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.AppsettingsApiModel;
import com.app.jobfizzerxp.model.calenderBookingApi.CalendarBookingsApiModel;
import com.app.jobfizzerxp.model.categoryApi.CategoryApiModel;
import com.app.jobfizzerxp.model.categoryApi.SubCategoryApiModel;
import com.app.jobfizzerxp.model.jobApi.JobAcceptApi;
import com.app.jobfizzerxp.model.jobApi.JobRejectApi;
import com.app.jobfizzerxp.model.jobApi.RandomRequestAcceptApi;
import com.app.jobfizzerxp.model.jobApi.RandomRequestRejectApi;
import com.app.jobfizzerxp.model.jobStatusApi.CompletedJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartToPlaceApiModel;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.networkUtils.VolleyCall;
import com.google.gson.Gson;

import org.json.JSONObject;

public class CommonRepository {


    public LiveData<AppsettingsApiModel> appSettingsApi(InputForAPI inputForAPI) {
        final MutableLiveData<AppsettingsApiModel> liveData = new MutableLiveData<>();

        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                AppsettingsApiModel appsettingsApiModel = gson.fromJson(response.toString(), AppsettingsApiModel.class);
                liveData.setValue(appsettingsApiModel);
            }

            @Override
            public void setResponseError(String error) {
                AppsettingsApiModel appsettingsApiModel = new AppsettingsApiModel();
                appsettingsApiModel.setError(true);
                appsettingsApiModel.setErrorMessage(error);
                liveData.setValue(appsettingsApiModel);

            }
        });
        return liveData;
    }

    public LiveData<CategoryApiModel> listCategory(InputForAPI inputForAPI) {

        final MutableLiveData<CategoryApiModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                CategoryApiModel flagResponseModel = gson.fromJson(response.toString(), CategoryApiModel.class);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                CategoryApiModel flagResponseModel = new CategoryApiModel();
                flagResponseModel.setError(true);
                flagResponseModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }

    public LiveData<SubCategoryApiModel> listSubCategory(InputForAPI inputForAPI) {

        final MutableLiveData<SubCategoryApiModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SubCategoryApiModel flagResponseModel = gson.fromJson(response.toString(), SubCategoryApiModel.class);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                SubCategoryApiModel flagResponseModel = new SubCategoryApiModel();
                flagResponseModel.setError(true);
                flagResponseModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }

    public LiveData<UpdateDeviceApiModel> updateDeviceToken(InputForAPI inputForAPI) {

        final MutableLiveData<UpdateDeviceApiModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                UpdateDeviceApiModel flagResponseModel = gson.fromJson(response.toString(), UpdateDeviceApiModel.class);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                UpdateDeviceApiModel flagResponseModel = new UpdateDeviceApiModel();
                flagResponseModel.setError(true);
                flagResponseModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }


    public LiveData<GoogleMapApiModel> googleGeoCodeApi(InputForAPI inputForAPI) {
        final MutableLiveData<GoogleMapApiModel> liveData = new MutableLiveData<>();
        VolleyCall.getMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                GoogleMapApiModel flagResponseModel = gson.fromJson(response.toString(),
                        GoogleMapApiModel.class);
                liveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                GoogleMapApiModel flagResponseModel = new GoogleMapApiModel();
                flagResponseModel.setStatus("false");
                liveData.setValue(flagResponseModel);

            }
        });
        return liveData;
    }


    public LiveData<RandomRequestRejectApi> rejectRandomRequest(InputForAPI inputForAPI) {
        final MutableLiveData<RandomRequestRejectApi> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                RandomRequestRejectApi randomRequestRejectApi = gson.fromJson(response.toString(), RandomRequestRejectApi.class);
                liveData.setValue(randomRequestRejectApi);
            }

            @Override
            public void setResponseError(String error) {

                RandomRequestRejectApi randomRequestRejectApi = new RandomRequestRejectApi();
                randomRequestRejectApi.setError(true);
                randomRequestRejectApi.setError_message(error);
                liveData.setValue(randomRequestRejectApi);

            }
        });
        return liveData;
    }

    public LiveData<RandomRequestAcceptApi> acceptRandomRequest(InputForAPI inputForAPI) {
        final MutableLiveData<RandomRequestAcceptApi> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                RandomRequestAcceptApi randomRequestAcceptApi = gson.fromJson(response.toString(), RandomRequestAcceptApi.class);
                liveData.setValue(randomRequestAcceptApi);
            }

            @Override
            public void setResponseError(String error) {
                RandomRequestAcceptApi randomRequestAcceptApi = new RandomRequestAcceptApi();
                randomRequestAcceptApi.setError(true);
                randomRequestAcceptApi.setError_message(error);
                liveData.setValue(randomRequestAcceptApi);

            }
        });
        return liveData;
    }

    public LiveData<JobRejectApi> rejectJob(InputForAPI inputForAPI) {
        final MutableLiveData<JobRejectApi> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                JobRejectApi jobRejectApi = gson.fromJson(response.toString(), JobRejectApi.class);
                liveData.setValue(jobRejectApi);
            }

            @Override
            public void setResponseError(String error) {

                JobRejectApi jobRejectApi = new JobRejectApi();
                jobRejectApi.setError(true);
                jobRejectApi.setError_message(error);
                liveData.setValue(jobRejectApi);

            }
        });
        return liveData;
    }

    public LiveData<StartJobApiModel> startJob(InputForAPI inputForAPI) {
        final MutableLiveData<StartJobApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                StartJobApiModel jobRejectApi = gson.fromJson(response.toString(), StartJobApiModel.class);
                liveData.setValue(jobRejectApi);
            }

            @Override
            public void setResponseError(String error) {

                StartJobApiModel jobRejectApi = new StartJobApiModel();
                jobRejectApi.setError(true);
                jobRejectApi.setErrorMessage(error);
                liveData.setValue(jobRejectApi);

            }
        });
        return liveData;
    }

    public LiveData<CompletedJobApiModel> completeJob(InputForAPI inputForAPI) {
        final MutableLiveData<CompletedJobApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                CompletedJobApiModel jobRejectApi = gson.fromJson(response.toString(), CompletedJobApiModel.class);
                liveData.setValue(jobRejectApi);
            }

            @Override
            public void setResponseError(String error) {

                CompletedJobApiModel jobRejectApi = new CompletedJobApiModel();
                jobRejectApi.setError(true);
                jobRejectApi.setErrorMessage(error);
                liveData.setValue(jobRejectApi);

            }
        });
        return liveData;
    }

    public LiveData<StartToPlaceApiModel> startToPlace(InputForAPI inputForAPI) {
        final MutableLiveData<StartToPlaceApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                StartToPlaceApiModel jobRejectApi = gson.fromJson(response.toString(), StartToPlaceApiModel.class);
                liveData.setValue(jobRejectApi);
            }

            @Override
            public void setResponseError(String error) {

                StartToPlaceApiModel jobRejectApi = new StartToPlaceApiModel();
                jobRejectApi.setError(true);
                jobRejectApi.setErrorMessage(error);
                liveData.setValue(jobRejectApi);

            }
        });
        return liveData;
    }

    public LiveData<JobAcceptApi> acceptJob(InputForAPI inputForAPI) {
        final MutableLiveData<JobAcceptApi> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                JobAcceptApi jobAcceptApi = gson.fromJson(response.toString(), JobAcceptApi.class);
                liveData.setValue(jobAcceptApi);
            }

            @Override
            public void setResponseError(String error) {
                JobAcceptApi jobAcceptApi = new JobAcceptApi();
                jobAcceptApi.setError(true);
                jobAcceptApi.setError_message(error);
                liveData.setValue(jobAcceptApi);

            }
        });
        return liveData;
    }

    public LiveData<ReviewModel> review(InputForAPI inputForAPI) {
        final MutableLiveData<ReviewModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ReviewModel aboutUsModel = gson.fromJson(response.toString(), ReviewModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {
                ReviewModel aboutUsModel = new ReviewModel();
                aboutUsModel.setError(true);
                aboutUsModel.setErrorMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<CalendarBookingsApiModel> calendarBookingDetails(InputForAPI inputForAPI) {
        final MutableLiveData<CalendarBookingsApiModel> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                CalendarBookingsApiModel aboutUsModel = gson.fromJson(response.toString(), CalendarBookingsApiModel.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {
                CalendarBookingsApiModel aboutUsModel = new CalendarBookingsApiModel();
                aboutUsModel.setError(true);
                aboutUsModel.setMessage(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<PaymentConfirmationApi> paymentConfirmation(InputForAPI inputForAPI) {
        final MutableLiveData<PaymentConfirmationApi> liveData = new MutableLiveData<>();

        VolleyCall.postMethod(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                PaymentConfirmationApi aboutUsModel = gson.fromJson(response.toString(), PaymentConfirmationApi.class);
                liveData.setValue(aboutUsModel);
            }

            @Override
            public void setResponseError(String error) {

                PaymentConfirmationApi aboutUsModel = new PaymentConfirmationApi();
                aboutUsModel.setError(true);
                aboutUsModel.setError_message(error);
                liveData.setValue(aboutUsModel);

            }
        });
        return liveData;
    }

    public LiveData<ImageUploadApiModel> uploadImageApi(InputForAPI inputForAPI) {
        final MutableLiveData<ImageUploadApiModel> liveData = new MutableLiveData<>();

        VolleyCall.uploadImage(inputForAPI, new ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ImageUploadApiModel imageUploadApiModel = gson.fromJson(response.toString(), ImageUploadApiModel.class);
                liveData.postValue(imageUploadApiModel);
            }

            @Override
            public void setResponseError(String error) {

                ImageUploadApiModel imageUploadApiModel = new ImageUploadApiModel();
                imageUploadApiModel.setError(true);
                imageUploadApiModel.setErrorMessage(error);
                liveData.postValue(imageUploadApiModel);

            }
        });
        return liveData;
    }


}