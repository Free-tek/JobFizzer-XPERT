package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.LogOutApiModel;
import com.app.jobfizzerxp.model.viewProfileApi.ViewProfileApiModel;
import com.app.jobfizzerxp.repository.AccountsFragmentRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class AccountsFragViewModel extends AndroidViewModel {

    private AccountsFragmentRepository accountsFragmentRepository;

    public AccountsFragViewModel(@NonNull Application application) {
        super(application);
        accountsFragmentRepository = new AccountsFragmentRepository();
    }

    public LiveData<LogOutApiModel> logOut(InputForAPI inputs) {
        return accountsFragmentRepository.logOut(inputs);
    }
    public LiveData<ViewProfileApiModel> viewProfile(InputForAPI inputForAPI){
        return accountsFragmentRepository.viewProfileApi(inputForAPI);
    }
}