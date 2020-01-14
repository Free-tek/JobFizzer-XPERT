package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.ChangePasswordApiModel;
import com.app.jobfizzerxp.model.FlagResponseModel;
import com.app.jobfizzerxp.model.ResetPasswordApiModel;
import com.app.jobfizzerxp.repository.ChangePasswordActRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class ChangePasswordViewModel extends AndroidViewModel {

    private ChangePasswordActRepository changePasswordActRepository;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        changePasswordActRepository = new ChangePasswordActRepository();
    }

    public LiveData<ChangePasswordApiModel> changePassword(InputForAPI inputs) {
        return changePasswordActRepository.changePassword(inputs);
    }

    public LiveData<ResetPasswordApiModel> resetPassword(InputForAPI inputs) {
        return changePasswordActRepository.resetPassword(inputs);
    }


    public  LiveData<FlagResponseModel> flagUpdate(InputForAPI inputForAPI) {
        return changePasswordActRepository.flagUpdate(inputForAPI);
    }
}