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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.ForgotPasswordResponseModel;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.ForgotPasswordViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private Button sendOtp;
    private EditText emailEditText;
    private ImageView backButton;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();
        initListners();
    }

    private void initListners() {
        backButton.setOnClickListener(this);
        sendOtp.setOnClickListener(this);
    }

    private void initViews() {
        forgotPasswordViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);

        rootView = findViewById(R.id.linearLayout);
        backButton = findViewById(R.id.backButton);
        sendOtp = findViewById(R.id.sendOtp);
        emailEditText = findViewById(R.id.emailEditText);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.forgot_password);
        emailEditText.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});

    }

    @Override
    public void onClick(View view) {

        if (view == backButton) {
            finish();
        } else if (view == sendOtp) {
            UiUtils.closeKeyboard(view);
            if (isValidInputs().equalsIgnoreCase("true")) {
                try {
                    buildOtpinputs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                UiUtils.snackBar(rootView, isValidInputs());
            }
        }
    }

    private void buildOtpinputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", emailEditText.getText().toString().trim());
        InputForAPI inputForAPI = new InputForAPI(ForgotPasswordActivity.this);
        inputForAPI.setUrl(UrlHelper.FORGOT_PASSWORD);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);
        requestOtp(inputForAPI);

    }

    private void requestOtp(InputForAPI inputForAPI) {
        forgotPasswordViewModel.requestOtp(inputForAPI).observe(this, new Observer<ForgotPasswordResponseModel>() {

            @Override
            public void onChanged(@Nullable ForgotPasswordResponseModel forgotPasswordResponseModel) {
                if (forgotPasswordResponseModel != null) {
                    handleForgotPasswordResponse(forgotPasswordResponseModel);
                }
            }
        });
    }

    private void handleForgotPasswordResponse(ForgotPasswordResponseModel response) {
        if (!response.getError()) {
            moveVerfication();
        } else {
            UiUtils.snackBar(rootView, response.getErrorMessage());
        }
    }

    private String isValidInputs() {

        String val;
        if (ValidationsUtils.invalidText(emailEditText.getText().toString().trim()) || ValidationsUtils.isInValidEmail(emailEditText.getText().toString().trim())) {
            val = getResources().getString(R.string.please_enter_valid_email);
        } else {
            val = "true";
        }
        return val;

    }

    private void moveVerfication() {
        Intent intent = new Intent(ForgotPasswordActivity.this, EnterVerificationCodeActivity.class);
        intent.putExtra("emailValue", emailEditText.getText().toString().trim());
        startActivity(intent);
    }

}