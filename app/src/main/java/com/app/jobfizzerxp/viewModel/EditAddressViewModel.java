package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.UpdateAddressApiModel;
import com.app.jobfizzerxp.repository.EditAddressRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class EditAddressViewModel extends AndroidViewModel {

    private EditAddressRepository editAddressRepository;

    public EditAddressViewModel(@NonNull Application application) {
        super(application);
        editAddressRepository = new EditAddressRepository();
    }

    public LiveData<UpdateAddressApiModel> updateAddress(InputForAPI inputs) {
        return editAddressRepository.updateAddress(inputs);
    }
}