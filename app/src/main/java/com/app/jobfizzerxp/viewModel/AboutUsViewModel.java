package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.AboutUsResponseModel;
import com.app.jobfizzerxp.repository.AboutUsRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class AboutUsViewModel extends AndroidViewModel {

    private AboutUsRepository aboutUsRepository;

    public AboutUsViewModel(@NonNull Application application) {
        super(application);
        aboutUsRepository = new AboutUsRepository();
    }

    public LiveData<AboutUsResponseModel> getAboutUs(InputForAPI inputs) {
        return aboutUsRepository.aboutUs(inputs);
    }
}