package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.SignUpApiModel;
import com.app.jobfizzerxp.repository.RegisterActRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class RegisterViewModel extends AndroidViewModel {
    private RegisterActRepository registerActRepository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        registerActRepository = new RegisterActRepository();
    }

    public LiveData<SignUpApiModel> signUp(InputForAPI inputs) {
        return registerActRepository.signUp(inputs);
    }
}