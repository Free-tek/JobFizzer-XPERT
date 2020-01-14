package com.app.jobfizzerxp.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.jobfizzerxp.model.UpdateDeviceApiModel;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.HOME_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;

public class SplashActivity extends AppCompatActivity {
    private AppSettings appSettings = new AppSettings(SplashActivity.this);
    private CommonViewModel commonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        commonViewModel.generateKeyHash();


        processHandler();
    }

    private void processHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appSettings.getIsLogged()) {
                    try {
                        deviceTokenIputs();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    moveOnBoardActivity();
                }
            }
        }, 3000);
    }

    public void deviceTokenIputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fcm_token", appSettings.getFireBaseToken());
        jsonObject.put("os", "android");

        InputForAPI inputForAPI = new InputForAPI(SplashActivity.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_DEVICE_TOKEN);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setShowLoader(true);

        upDateToken(inputForAPI);

    }

    private void upDateToken(InputForAPI inputForAPI) {
        commonViewModel.updateDeviceToken(inputForAPI).observe(this, new Observer<UpdateDeviceApiModel>() {
            @Override
            public void onChanged(@Nullable UpdateDeviceApiModel flagResponseModel) {
                if (flagResponseModel != null) {
                    if (flagResponseModel.getError()) {
                        UiUtils.snackBar(findViewById(android.R.id.content), flagResponseModel.getErrorMessage());
                        moveToSignIn();
                    } else {
                        moveMainActivity();
                    }
                }
            }
        });
    }
    private void moveToSignIn() {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void moveOnBoardActivity() {
        Intent onboard = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(onboard);
        finish();
    }

    private void moveMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(LOGIN_TYPE, HOME_TAB);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}