package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.providerCalendarApi.ProviderCalendarApiModel;
import com.app.jobfizzerxp.repository.AppointmentActRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class AppointmentsActViewModel extends AndroidViewModel {

    private AppointmentActRepository appointmentActRepository;

    public AppointmentsActViewModel(@NonNull Application application) {
        super(application);
        appointmentActRepository = new AppointmentActRepository();
    }

    public LiveData<ProviderCalendarApiModel> calendarApi(InputForAPI inputs) {
        return appointmentActRepository.calendarApi(inputs);
    }
}