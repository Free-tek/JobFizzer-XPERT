package com.app.jobfizzerxp.utilities.uiUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.app.jobfizzerxp.R;

/*
 * Created by Poyya on 11-02-2017.
 */
public class MapDialog extends Dialog {
    @SuppressWarnings("ConstantConditions")
    public MapDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }
}