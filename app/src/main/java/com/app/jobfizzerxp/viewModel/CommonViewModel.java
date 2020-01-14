package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.PaymentConfirmationApi;
import com.app.jobfizzerxp.model.ReviewModel;
import com.app.jobfizzerxp.model.UpdateDeviceApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.AppsettingsApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.TimeSlotApi;
import com.app.jobfizzerxp.model.calenderBookingApi.CalendarBookingsApiModel;
import com.app.jobfizzerxp.model.categoryApi.CategoryApiModel;
import com.app.jobfizzerxp.model.categoryApi.SubCategoryApiModel;
import com.app.jobfizzerxp.model.googleMaps.GoogleMapApiModel;
import com.app.jobfizzerxp.model.jobApi.JobAcceptApi;
import com.app.jobfizzerxp.model.jobApi.JobRejectApi;
import com.app.jobfizzerxp.model.jobApi.RandomRequestAcceptApi;
import com.app.jobfizzerxp.model.jobApi.RandomRequestRejectApi;
import com.app.jobfizzerxp.model.jobStatusApi.CompletedJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartToPlaceApiModel;
import com.app.jobfizzerxp.repository.CommonRepository;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.R;

import java.security.MessageDigest;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommonViewModel extends AndroidViewModel {
    private CommonRepository commonRepository;
    private Application application;
    private String TAG = CommonViewModel.class.getSimpleName();

    public CommonViewModel(@NonNull Application application) {
        super(application);
        commonRepository = new CommonRepository();
        this.application = application;
    }

    public LiveData<AppsettingsApiModel> appSettingsApi(InputForAPI inputs) {
        return commonRepository.appSettingsApi(inputs);
    }

    public LiveData<CategoryApiModel> listCategory(InputForAPI inputs) {
        return commonRepository.listCategory(inputs);
    }

    public LiveData<SubCategoryApiModel> listSubCategory(InputForAPI inputs) {
        return commonRepository.listSubCategory(inputs);
    }

    public LiveData<UpdateDeviceApiModel> updateDeviceToken(InputForAPI inputs) {
        return commonRepository.updateDeviceToken(inputs);
    }


    public LiveData<ReviewModel> review(InputForAPI inputs) {
        return commonRepository.review(inputs);
    }

    public LiveData<CalendarBookingsApiModel> calendarBookingDetails(InputForAPI inputs) {
        return commonRepository.calendarBookingDetails(inputs);
    }

    public LiveData<PaymentConfirmationApi> paymentConfirmation(InputForAPI inputs) {
        return commonRepository.paymentConfirmation(inputs);
    }

    public LiveData<RandomRequestRejectApi> rejectRandomRequest(InputForAPI inputs) {
        return commonRepository.rejectRandomRequest(inputs);
    }

    public LiveData<RandomRequestAcceptApi> acceptRandomRequest(InputForAPI inputs) {
        return commonRepository.acceptRandomRequest(inputs);
    }

    public LiveData<JobRejectApi> rejectJob(InputForAPI inputs) {
        return commonRepository.rejectJob(inputs);
    }

    public LiveData<JobAcceptApi> acceptJob(InputForAPI inputs) {
        return commonRepository.acceptJob(inputs);
    }

    public LiveData<StartJobApiModel> startJob(InputForAPI inputs) {
        return commonRepository.startJob(inputs);
    }

    public LiveData<CompletedJobApiModel> completeJob(InputForAPI inputs) {
        return commonRepository.completeJob(inputs);
    }

    public LiveData<StartToPlaceApiModel> startToPlace(InputForAPI inputs) {
        return commonRepository.startToPlace(inputs);
    }

    public LiveData<ImageUploadApiModel> uploadImageApi(InputForAPI inputs) {
        return commonRepository.uploadImageApi(inputs);
    }


    public LiveData<GoogleMapApiModel> googleGeoCodeApi(InputForAPI inputs) {
        return commonRepository.googleGeoCodeApi(inputs);
    }

    public String getGeoCodeMapUrl(double lat, double longg) {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                + longg + "&sensor=true&key=" + application.getString(R.string.google_map_key);
    }

    public String getStaticMapUrl(String lat, String longg) {
        return "https://maps.googleapis.com/maps/api/staticmap?key=" +
                application.getString(R.string.google_map_key) + "&center=" + lat + ","
                + longg + "&zoom=15&format=jpeg&maptype=roadmap&size=512x512&sensor=false";
    }


    private CharSequence getElapsedTimeText(String first, String second, String third,
                                            String four, String five, String six) {
        return ConversionUtils.getCombinedStrings(application.getString(R.string.elapsed_time),
                first, second, ": ", third, four, ": ", five, six);

    }


    public CharSequence getElapsedTime(long different) {
        String first = "";
        String second = "";
        String third = "";
        String four = "";
        String five = "";
        String six = "";
        different = TimeUnit.SECONDS.toMillis(different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli *24;
        different = different % daysInMilli;

        BaseUtils.log("elapsedDays", String.valueOf(+elapsedDays));
        long elapsedHours = different / hoursInMilli + elapsedDays;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        String hourFull = String.valueOf(elapsedHours);
        String minutesFull = String.valueOf(elapsedMinutes);
        String SecondsFull = String.valueOf(elapsedSeconds);
        BaseUtils.log(TAG, "getDifference: " + hourFull + "/" + minutesFull + "/" + SecondsFull);

        if (hourFull.equalsIgnoreCase("0")) {
            first = "0";
            second = "0";
        } else if (hourFull.length() == 1) {
            first = "0";
            second = String.valueOf(hourFull.charAt(0));
        } else {
            first = hourFull;

        }

        if (elapsedMinutes == 0) {
            third = "0";
            four = "0";
        } else if (minutesFull.length() == 1) {
            third = "0";
            four = String.valueOf(minutesFull.charAt(0));
        } else {
            third = String.valueOf(minutesFull.charAt(0));
            four = String.valueOf(minutesFull.charAt(1));
        }


        if (elapsedSeconds == 0) {
            five = "0";
            six = "0";
        } else if (SecondsFull.length() == 1) {
            five = "0";
            six = String.valueOf(SecondsFull.charAt(0));
        } else {
            five = String.valueOf(SecondsFull.charAt(0));
            six = String.valueOf(SecondsFull.charAt(1));

        }
        return getElapsedTimeText(first, second, third, four, five, six);

    }

    public String getSelectedTimeSlots(List<TimeSlotApi> timeSlotDetails) {
        String retuntext = "";
        for (int i = 0; i < timeSlotDetails.size(); i++) {
            TimeSlotApi timeslot;
            timeslot = timeSlotDetails.get(i);
            if (timeslot.getSelected().equalsIgnoreCase("true")) {
                if (retuntext.length() == 0) {
                    retuntext = timeslot.getTiming();

                } else {
                    retuntext = retuntext + ", " + timeslot.getTiming();
                }
            }
        }
        return retuntext;
    }

    public void generateKeyHash() {
        try {
            PackageInfo info = getApplication().getBaseContext().getPackageManager().getPackageInfo(
                    getApplication().getBaseContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                BaseUtils.log("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}