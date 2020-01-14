package com.app.jobfizzerxp.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.ChangePasswordApiModel;
import com.app.jobfizzerxp.utilities.uiUtils.AsteriskPasswordTransformation;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.ChangePasswordViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.HOME_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;


public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText confirmPassword, newPassword, oldPassword;
    private ImageView backButton;
    private Button saveButton;
    private ChangePasswordViewModel changePasswordViewModel;
    private RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
        initListners();
    }


    private String validateInputs() {
        String message = "true";

        if (ValidationsUtils.invalidText(oldPassword.getText().toString())) {
            return getResources().getString(R.string.enter_password);

        } else if (ValidationsUtils.invalidText(newPassword.getText().toString())) {
            return getResources().getString(R.string.enter_newpassword);

        } else if (ValidationsUtils.invalidText(confirmPassword.getText().toString())) {
            return getResources().getString(R.string.enter_confirmpassword);

        } else if (ValidationsUtils.invalidPassword(oldPassword.getText().toString().trim())) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (ValidationsUtils.invalidPassword(newPassword.getText().toString().trim())) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (ValidationsUtils.invalidPassword(confirmPassword.getText().toString().trim())) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (ValidationsUtils.passwordUnmatch(newPassword.getText().toString().trim(),
                confirmPassword.getText().toString().trim())) {
            return getResources().getString(R.string.password_not_match);

        } else {
            return message;
        }
    }


    private void initListners() {
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void initViews() {
        changePasswordViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        confirmPassword = findViewById(R.id.confirmPassword);
        rootView = findViewById(R.id.rootView);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.change_password);

        confirmPassword.setTransformationMethod(new AsteriskPasswordTransformation());
        oldPassword.setTransformationMethod(new AsteriskPasswordTransformation());
        newPassword.setTransformationMethod(new AsteriskPasswordTransformation());


        newPassword.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
        confirmPassword.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
        oldPassword.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});


    }

    @Override
    public void onClick(View view) {

        if (view == backButton) {
            onBackPressed();
        } else if (view == saveButton) {
            UiUtils.closeKeyboard(view);
            if (validateInputs().equalsIgnoreCase("true")) {
                resetPassword();
            } else {
                UiUtils.snackBar(rootView, validateInputs());
            }

        }
    }

    private void resetPassword() {
        InputForAPI inputForAPI = new InputForAPI(this);
        inputForAPI.setUrl(UrlHelper.CHANGE_PASSWORD);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setJsonObject(getInputs());
        inputForAPI.setShowLoader(true);
        getValue(inputForAPI);

    }

    private JSONObject getInputs() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldpassword", oldPassword.getText().toString());
            jsonObject.put("newpassword", newPassword.getText().toString());
            jsonObject.put("cnfpassword", confirmPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void getValue(InputForAPI inputForAPI) {
        changePasswordViewModel.changePassword(inputForAPI).observe(this, new Observer<ChangePasswordApiModel>() {
            @Override
            public void onChanged(@Nullable ChangePasswordApiModel response) {
                if (response != null) {
                    if (!response.getError()) {
                        moveMainActivity();
                    } else {
                        UiUtils.snackBar(rootView, response.getErrorMessage());
                    }
                }
            }
        });
    }

    private void moveMainActivity() {
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        intent.putExtra(LOGIN_TYPE, HOME_TAB);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

}