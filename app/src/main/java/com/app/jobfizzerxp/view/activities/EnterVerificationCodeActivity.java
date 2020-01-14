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

import com.app.jobfizzerxp.model.FlagResponseModel;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.ConnectionUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.ChangePasswordViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class EnterVerificationCodeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView close;
    private Button verifyButton;
    private String email;
    private EditText otpEditText;
    private RelativeLayout rootView;
    private ChangePasswordViewModel changePasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_verification_code);
        changePasswordViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        initViews();
        initListners();
        getIntentValues();
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        email = intent.getStringExtra("emailValue");
    }

    private void initListners() {
        close.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
    }

    private void initViews() {
        close = findViewById(R.id.close);
        rootView = findViewById(R.id.rootView);
        verifyButton = findViewById(R.id.verifyButton);
        otpEditText = findViewById(R.id.otpEditText);

        otpEditText.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter, new InputFilter.LengthFilter(6)});

    }

    @Override
    public void onClick(View view) {
        if (view == close) {
            onBackPressed();
        } else if (view == verifyButton) {
            UiUtils.closeKeyboard(view);

            if (ConnectionUtils.isNetworkConnected(EnterVerificationCodeActivity.this)) {

                if (checkOtp().equalsIgnoreCase("true")) {
                    requestOtp();
                } else {
                    UiUtils.snackBar(rootView, checkOtp());
                }
            } else {
                UiUtils.snackBar(rootView, getString(R.string.no_internet_connection));
            }
        }
    }

    private void moveChangePassword() {
        Intent intent = new Intent(EnterVerificationCodeActivity.this, ResetPasswordActivity.class);
        intent.putExtra("emailValue", email);
        startActivity(intent);
        finish();
    }

    private String checkOtp() {
        String val;
        if (ValidationsUtils.invalidText(otpEditText.getText().toString().trim())) {
            val = getResources().getString(R.string.please_enter_valid_otp);

        } else if (ValidationsUtils.invalidPassword(otpEditText.getText().toString().trim())) {
            val = getResources().getString(R.string.please_enter_valid_otp);

        } else {
            val = "true";
        }
        return val;
    }

    private void requestOtp() {

        JSONObject jsonObject = getInputs();

        InputForAPI inputForAPI = new InputForAPI(EnterVerificationCodeActivity.this);
        inputForAPI.setUrl(UrlHelper.VERIFY_OTP);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);
        CheckOtp(inputForAPI);

//        moveChangePassword();

    }

    private void CheckOtp(InputForAPI inputForAPI) {
        changePasswordViewModel.flagUpdate(inputForAPI).observe(this,
                new Observer<FlagResponseModel>() {
                    @Override
                    public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                        if (flagResponseModel != null)
                            if (flagResponseModel.getError().equalsIgnoreCase("false")) {
                                moveChangePassword();
                            } else {
                                UiUtils.snackBar(rootView, "Please enter valid otp");
//                            Toast.makeText(EnterVerificationCodeActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();;
                            }
                    }
                });
    }

    private JSONObject getInputs() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("otp", otpEditText.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}