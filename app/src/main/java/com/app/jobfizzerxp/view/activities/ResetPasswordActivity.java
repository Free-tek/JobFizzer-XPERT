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

import com.app.jobfizzerxp.model.ResetPasswordApiModel;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.uiUtils.AsteriskPasswordTransformation;
import com.app.jobfizzerxp.viewModel.ChangePasswordViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText confirmPassword, newPassword;
    private ImageView backButton;
    private String email;
    private Button saveButton;
    private ChangePasswordViewModel changePasswordViewModel;
    private RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initViews();
        initListners();
        getIntentValues();
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        email = intent.getStringExtra("emailValue");
    }

    private void initListners() {
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        confirmPassword.setTransformationMethod(new AsteriskPasswordTransformation());
        newPassword.setTransformationMethod(new AsteriskPasswordTransformation());

        newPassword.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
        confirmPassword.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
    }

    private void initViews() {
        changePasswordViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        rootView = findViewById(R.id.rootView);
        confirmPassword = findViewById(R.id.confirmPassword);
        newPassword = findViewById(R.id.newPassword);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.reset_password);
    }

    @Override
    public void onClick(View view) {

        if (view == backButton) {
            onBackPressed();
        } else if (view == saveButton) {
            UiUtils.closeKeyboard(view);
            if (validateInputs().equalsIgnoreCase("true")) {
                try {
                    getInputs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                UiUtils.snackBar(rootView, validateInputs());
            }
        }
    }


    private void getInputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("password", newPassword.getText().toString());
        jsonObject.put("email", email);
        jsonObject.put("confirmpassword", confirmPassword.getText().toString());

        InputForAPI inputForAPI = new InputForAPI(ResetPasswordActivity.this);
        inputForAPI.setUrl(UrlHelper.RESET_PASSWORD);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(false);
        resetPassword(inputForAPI);
    }

    private void resetPassword(InputForAPI inputForAPI) {
        changePasswordViewModel.resetPassword(inputForAPI).observe(this, new Observer<ResetPasswordApiModel>() {
            @Override
            public void onChanged(@Nullable ResetPasswordApiModel flagResponseModel) {
                if (flagResponseModel != null) {
                    if (!flagResponseModel.getError()) {
                        moveSignActivity();
                    } else {
                        UiUtils.snackBar(rootView, flagResponseModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private String validateInputs() {
        String message = "true";

        if (ValidationsUtils.invalidText(newPassword.getText().toString())) {
            return getResources().getString(R.string.enter_password);

        } else if (ValidationsUtils.invalidText(confirmPassword.getText().toString())) {
            return getResources().getString(R.string.enter_confirmpassword);

        } else if (ValidationsUtils.passwordUnmatch(newPassword.getText().toString(), confirmPassword.getText().toString())) {
            return getResources().getString(R.string.password_not_match);

        } else if (ValidationsUtils.invalidPassword(newPassword.getText().toString())) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (ValidationsUtils.invalidPassword(confirmPassword.getText().toString())) {
            return getResources().getString(R.string.password_must_be_six);
        } else {
            return message;
        }
    }


    private void moveSignActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}