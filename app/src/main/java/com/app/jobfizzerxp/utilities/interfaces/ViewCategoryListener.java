package com.app.jobfizzerxp.utilities.interfaces;

import android.content.Context;

import com.app.jobfizzerxp.model.viewCategoryApi.Category;

public interface ViewCategoryListener {
    void onEditDialog(Context context, Category category);
}