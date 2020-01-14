package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.viewScheduleApi.UpdateScheduleApiModel;
import com.app.jobfizzerxp.model.viewScheduleApi.ViewScheduleApiModel;
import com.app.jobfizzerxp.repository.ViewScheduleRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class ViewScheduleViewModel extends AndroidViewModel {

    private ViewScheduleRepository viewScheduleRepository;

    public ViewScheduleViewModel(@NonNull Application application) {
        super(application);
        viewScheduleRepository = new ViewScheduleRepository();
    }

    public LiveData<ViewScheduleApiModel> viewSchedule(InputForAPI inputs) {
        return viewScheduleRepository.viewSchedule(inputs);
    }

    public LiveData<UpdateScheduleApiModel> updateSchedule(InputForAPI inputs) {
        return viewScheduleRepository.updateSchedule(inputs);
    }
}