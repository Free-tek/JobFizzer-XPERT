package com.app.jobfizzerxp.view.activities;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.app.jobfizzerxp.model.SignInResponseModel;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.uiUtils.AsteriskPasswordTransformation;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.SignInViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.jobfizzerxp.utilities.helpers.Constants.BOOKINGS.USER_TYPE;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.HOME_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;
import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.FINE_LOCATION_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.RANDOM_BOOLEAN.TRUE;
import static java.lang.Boolean.FALSE;

/**
 * Created by karthik on 01/10/17.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener {
    private EditText usernameEditText, passwordEditText;
    private Button dontHaveAnAccount, loginButton, forgotPassword;
    private AppSettings appSettings = new AppSettings(SignInActivity.this);

    private SignInViewModel signInViewModel;
    private String TAG = SignInActivity.class.getSimpleName();
    private ScrollView rootView;

    private Button ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
        initListners();
    }

    private void initViews() {
        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        dontHaveAnAccount = findViewById(R.id.dontHaveAnAccount);
        rootView = findViewById(R.id.rootView);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);


        passwordEditText.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
        passwordEditText.setTransformationMethod(new AsteriskPasswordTransformation());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSIONS);
            }
        }
    }

    private void initListners() {
        dontHaveAnAccount.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == dontHaveAnAccount) {
            moveSignUp();
        } else if (view == loginButton) {
            UiUtils.closeKeyboard(view);
            if (isValidInputs().equalsIgnoreCase("true")) {
                try {
                    getSignInInputs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                UiUtils.snackBar(rootView, isValidInputs());
            }

        } else if (view == forgotPassword) {
            moveToForgotPassword();
        }
    }

    private void moveToForgotPassword() {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private String isValidInputs() {
        String value;
        if (ValidationsUtils.invalidText(usernameEditText.getText().toString().trim()) || ValidationsUtils.isInValidEmail(usernameEditText.getText().toString().trim())) {
            value = getResources().getString(R.string.please_enter_valid_email);
        } else if (ValidationsUtils.invalidText(passwordEditText.getText().toString())) {
            value = getResources().getString(R.string.please_enter_valid_password);
        } else {
            value = "true";
        }
        return value;
    }

    private void getSignInInputs() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email", usernameEditText.getText().toString().trim());
        jsonObject.put("password", passwordEditText.getText().toString());
        jsonObject.put("user_type", "Provider");
        InputForAPI inputForAPI = new InputForAPI(SignInActivity.this);
        inputForAPI.setUrl(UrlHelper.SIGN_IN);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(false);

        processLogin(inputForAPI);

    }

    private void processLogin(InputForAPI inputForAPI) {
        signInViewModel.signIn(inputForAPI).observe(this, new Observer<SignInResponseModel>() {
            @Override
            public void onChanged(@Nullable SignInResponseModel signInResponseModel) {
                if (signInResponseModel != null) {
                    if (signInResponseModel.getError()) {
                        UiUtils.snackBar(rootView, signInResponseModel.getErrorMessage());
                    } else {
                        handleSuccessResponeLogin(signInResponseModel);

                    }
                }
            }
        });
    }

    private void handleSuccessResponeLogin(SignInResponseModel signInResponseModel) {
        BaseUtils.log(TAG, "handleSuccessResponeLogin: " + signInResponseModel.getAccessToken());
        appSettings.setToken(signInResponseModel.getAccessToken());
        appSettings.setProviderId(String.valueOf(signInResponseModel.getProviderId()));
        appSettings.setAccessToken(signInResponseModel.getAccessToken());
        appSettings.setUserName("" + ConversionUtils.getCombinedStrings(signInResponseModel.getFirstName() + " " + signInResponseModel.getLastName()));
        appSettings.setIsLogged(TRUE);


        if (!signInResponseModel.isDelete_status().equalsIgnoreCase("active")) {
            moveMainActivity();
        } else {
            showDialogDeleteStatus();
        }
    }

    private void showDialogDeleteStatus() {

        final Dialog dialog = new Dialog(SignInActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.show_delete_status);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();

        ok = dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSettings.setIsLogged(FALSE);
                Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });

    }

    private void moveMainActivity() {
        appSettings.setUserType(USER_TYPE);
        Intent main = new Intent(SignInActivity.this, MainActivity.class);
        main.putExtra(LOGIN_TYPE, HOME_TAB);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        finish();
    }

    public void moveSignUp() {
        Intent signup = new Intent(SignInActivity.this, RegisterActivity.class);
        startActivity(signup);
        finish();
    }
}