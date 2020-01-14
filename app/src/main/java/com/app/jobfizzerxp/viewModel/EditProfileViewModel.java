package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.UpdateProfileApiModel;
import com.app.jobfizzerxp.repository.EditProfileRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class EditProfileViewModel extends AndroidViewModel {

    private EditProfileRepository editProfileRepository;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);
        editProfileRepository = new EditProfileRepository();
    }

    public LiveData<UpdateProfileApiModel> updateProfile(InputForAPI inputs) {
        return editProfileRepository.updateProfile(inputs);
    }
}