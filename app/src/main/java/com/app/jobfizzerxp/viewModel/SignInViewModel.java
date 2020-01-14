package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.SignInResponseModel;
import com.app.jobfizzerxp.repository.SignInRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class SignInViewModel extends AndroidViewModel {

    private SignInRepository signInRepository;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        signInRepository = new SignInRepository();
    }


    public LiveData<SignInResponseModel> signIn(InputForAPI inputs) {
        return signInRepository.signIn(inputs);
    }

}