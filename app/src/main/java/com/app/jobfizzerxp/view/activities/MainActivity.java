package com.app.jobfizzerxp.view.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.app.jobfizzerxp.model.UpdateDeviceApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.AppsettingsApiModel;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.adapters.MainPagerAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.ACCOUNTS_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;
import static java.lang.Boolean.FALSE;

public class MainActivity extends BaseActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private AppSettings appSettings = new AppSettings(MainActivity.this);
    private CommonViewModel commonViewModel;
    private RelativeLayout rootView;
    private ViewPager mainPager;
    private com.app.jobfizzerxp.utilities.customLibraries.NavigationTabStrip tabStrip;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setAdapter();
        postToken();
        BaseUtils.initLocationService(MainActivity.this);
        BaseUtils.initChatService(MainActivity.this);
    }

    private void setAdapter() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPager.setAdapter(mainPagerAdapter);
        if (getIntent().getStringExtra(LOGIN_TYPE).equalsIgnoreCase(ACCOUNTS_TAB)) {
            mainPager.setCurrentItem(2);
        } else {
            mainPager.setCurrentItem(1);
        }
        tabStrip.setViewPager(mainPager);
    }


    private void initViews() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        rootView = findViewById(R.id.activity_main);
        mainPager = findViewById(R.id.mainPager);
        tabStrip = findViewById(R.id.tabStrip);

        String[] titles = new String[]{getResources().getString(R.string.chat), getResources().getString(R.string.home), getResources().getString(R.string.account)};
        tabStrip.setTitles(titles);
    }

    public void postToken() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fcm_token", appSettings.getFireBaseToken());
            jsonObject.put("os", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_DEVICE_TOKEN);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);
        updateDeviceRequest(inputForAPI);

    }

    private void updateDeviceRequest(InputForAPI inputForAPI) {
        commonViewModel.updateDeviceToken(inputForAPI).observe(this, new Observer<UpdateDeviceApiModel>() {
            @Override
            public void onChanged(@Nullable UpdateDeviceApiModel mainScreenResponseModel) {
                if (mainScreenResponseModel != null) {
                    handelResponse(mainScreenResponseModel);
                }
            }
        });
    }

    private void handelResponse(UpdateDeviceApiModel mainScreenResponseModel) {
        if (mainScreenResponseModel.getError()) {
            UiUtils.snackBar(rootView, mainScreenResponseModel.getErrorMessage());
        }
    }

    private void getAppSettings() {
        InputForAPI inputForAPI = new InputForAPI(MainActivity.this);
        inputForAPI.setUrl(UrlHelper.APP_SETTINGS);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(false);

        commonViewModel.appSettingsApi(inputForAPI).observe(this, new Observer<AppsettingsApiModel>() {
            @Override
            public void onChanged(@Nullable AppsettingsApiModel appsettingsApiModel) {
                if (appsettingsApiModel != null) {
                    if (!appsettingsApiModel.getError()) {
                        handleAppSettingResponse(appsettingsApiModel);
                    } else {
                        UiUtils.snackBar(rootView, appsettingsApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void handleAppSettingResponse(AppsettingsApiModel appsettingsApiModel) {
        if (appsettingsApiModel.getDeleteStatus().equalsIgnoreCase("active")) {

        }
    }

    private void showDialogDeleteStatus() {

        final Dialog dialog = new Dialog(MainActivity.this);
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
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //bookings adapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}