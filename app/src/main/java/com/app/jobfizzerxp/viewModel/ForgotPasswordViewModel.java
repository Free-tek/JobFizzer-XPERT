package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.ForgotPasswordResponseModel;
import com.app.jobfizzerxp.repository.ForgotPasswordRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class ForgotPasswordViewModel extends AndroidViewModel {

    private ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
        forgotPasswordRepository = new ForgotPasswordRepository();
    }

    public LiveData<ForgotPasswordResponseModel> requestOtp(InputForAPI inputs) {
        return forgotPasswordRepository.requestOtp(inputs);
    }
}