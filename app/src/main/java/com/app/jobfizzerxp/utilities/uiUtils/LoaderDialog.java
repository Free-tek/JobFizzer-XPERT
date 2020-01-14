package com.app.jobfizzerxp.utilities.uiUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

/**
 * Created by User on 11-02-2017.
 */
public class LoaderDialog extends Dialog {
    @SuppressWarnings("ConstantConditions")
    public LoaderDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }
}
