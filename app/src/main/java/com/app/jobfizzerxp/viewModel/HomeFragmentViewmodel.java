package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.beforeAfterImage.BeforeAfterImageModel;
import com.app.jobfizzerxp.model.homeDashboardApi.HomeDashBoardResponseModel;
import com.app.jobfizzerxp.repository.HomeFragmentRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class HomeFragmentViewmodel extends AndroidViewModel {

    private HomeFragmentRepository homeFragmentRepository;

    public HomeFragmentViewmodel(@NonNull Application application) {
        super(application);
        homeFragmentRepository = new HomeFragmentRepository();
    }

    public LiveData<HomeDashBoardResponseModel> getDashBoard(InputForAPI inputs) {
        return homeFragmentRepository.dashBoard(inputs);
    }

    public LiveData<BeforeAfterImageModel> getAfterBeforeImage(InputForAPI inputs) {
        return homeFragmentRepository.getAfterBeforeImage(inputs);
    }


}