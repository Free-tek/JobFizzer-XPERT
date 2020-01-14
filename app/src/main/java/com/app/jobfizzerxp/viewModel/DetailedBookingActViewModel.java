package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.CancelJobApiModel;
import com.app.jobfizzerxp.model.elapsedTime.ElapsedTimeApiModel;
import com.app.jobfizzerxp.repository.DetailedBookingActRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class DetailedBookingActViewModel extends AndroidViewModel {

    private DetailedBookingActRepository detailedBookingActRepository;

    public DetailedBookingActViewModel(@NonNull Application application) {
        super(application);
        detailedBookingActRepository = new DetailedBookingActRepository();
    }


    public LiveData<CancelJobApiModel> cancelJobApi(InputForAPI inputs) {
        return detailedBookingActRepository.cancelJobApi(inputs);
    }

    public LiveData<ElapsedTimeApiModel> elapsedTime(InputForAPI inputs) {
        return detailedBookingActRepository.elapsedTime(inputs);
    }
}