package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.viewCategoryApi.AddCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.DeleteCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.EditCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.ViewCategoryApiModel;
import com.app.jobfizzerxp.repository.ViewCategoryRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class ViewCategoryViewModel extends AndroidViewModel {

    private ViewCategoryRepository viewCategoryRepository;

    public ViewCategoryViewModel(@NonNull Application application) {
        super(application);
        viewCategoryRepository = new ViewCategoryRepository();
    }

    public LiveData<ViewCategoryApiModel> viewCategory(InputForAPI inputs) {
        return viewCategoryRepository.viewCategory(inputs);
    }

    public LiveData<AddCategoryApiModel> addCategory(InputForAPI inputs) {
        return viewCategoryRepository.addCategory(inputs);
    }

    public LiveData<EditCategoryApiModel> editCategory(InputForAPI inputs) {
        return viewCategoryRepository.editCategory(inputs);
    }

    public LiveData<DeleteCategoryApiModel> deleteCategory(InputForAPI inputs) {
        return viewCategoryRepository.deleteCategory(inputs);
    }

}