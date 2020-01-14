package com.app.jobfizzerxp.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.jobfizzerxp.model.LogOutApiModel;
import com.app.jobfizzerxp.model.viewProfileApi.ProviderDetails;
import com.app.jobfizzerxp.model.viewProfileApi.ViewProfileApiModel;
import com.app.jobfizzerxp.utilities.helpers.AnimationHelper;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.activities.AboutUsActivity;
import com.app.jobfizzerxp.view.activities.AppointmentsActivity;
import com.app.jobfizzerxp.view.activities.ChangePasswordActivity;
import com.app.jobfizzerxp.view.activities.EditAddressActivity;
import com.app.jobfizzerxp.view.activities.EditProfileActivity;
import com.app.jobfizzerxp.view.activities.MainActivity;
import com.app.jobfizzerxp.view.activities.SignInActivity;
import com.app.jobfizzerxp.view.activities.ViewCategory;
import com.app.jobfizzerxp.view.activities.ViewSchedule;
import com.app.jobfizzerxp.view.adapters.ChangeThemeAdapter;
import com.app.jobfizzerxp.viewModel.AccountsFragViewModel;
import com.app.jobfizzerxp.R;

import java.util.List;

import static com.app.jobfizzerxp.utilities.helpers.Constants.INTENT_KEYS.PROVIDER_DETAILS;
import static java.lang.Boolean.FALSE;

public class AccountsFragment extends Fragment {
    private LinearLayout logOut, viewCategory, viewSchedule, viewEditAddress;
    private TextView providerName;
    private TextView providerMobile;
    private ImageView profilePicture;
    private LinearLayout changeProfile;
    private ScrollView rootView;

    private LinearLayout changePassword;
    private String TAG = AccountsFragment.class.getSimpleName();
    private ImageView changeThemIcon;
    private RecyclerView changeThemeAdapter;
    private boolean themeOpen;
    private MainActivity activity;
    private LinearLayout changeTheme, appointments;
    private LinearLayout aboutUs;

    private AccountsFragViewModel accountsFragViewModel;
    private ProviderDetails providerDetails;

    public AccountsFragment() {
        // Required empty public constructor
    }

    public static AccountsFragment newInstance() {
        return new AccountsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        activity = (MainActivity) view.getContext();
        initViews(view);
        setThemeColor();
        initListeners();
        viewProfileApiCall();
        setThemeAdapter();
        return view;
    }

    private void setThemeAdapter() {
        themeOpen = false;
        List<Integer> allColors = UiUtils.getAllMaterialColors(activity);

        int numberOfColumns = 6;
        changeThemeAdapter.setLayoutManager(new GridLayoutManager(activity, numberOfColumns));
        ChangeThemeAdapter adapter = new ChangeThemeAdapter(activity, allColors);
        changeThemeAdapter.setAdapter(adapter);
    }

    private void initListeners() {
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext(AboutUsActivity.class);
            }
        });

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!themeOpen) {
                    AnimationHelper.expand(changeThemeAdapter, changeThemIcon);
                    themeOpen = true;
                } else {
                    AnimationHelper.collapse(changeThemeAdapter, changeThemIcon);
                    themeOpen = false;
                }
            }
        });

        viewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext(ViewCategory.class);
            }
        });
        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext(ViewSchedule.class);
            }
        });
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext(AppointmentsActivity.class);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext(ChangePasswordActivity.class);
            }
        });
        logOut.setOnClickListener(view -> logOutApiCall());


        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveWithProvider(EditProfileActivity.class);
            }
        });

        viewEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveWithProvider(EditAddressActivity.class);
            }
        });
    }

    private void moveToNext(Class<?> activityName) {
        startActivity(new Intent(activity, activityName));
    }

    private void moveWithProvider(Class<?> activityName) {
        Intent intent = new Intent(activity, activityName);
        intent.putExtra(PROVIDER_DETAILS, providerDetails);
        startActivity(intent);
    }

    public void logOutApiCall() {
        InputForAPI inputForAPI = new InputForAPI(activity);
        inputForAPI.setUrl(UrlHelper.LOG_OUT);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);
        logOut(inputForAPI);

    }

    private void logOut(InputForAPI inputForAPI) {

        accountsFragViewModel.logOut(inputForAPI).observe(this, new Observer<LogOutApiModel>() {
            @Override
            public void onChanged(@Nullable LogOutApiModel logOutApiModel) {
                if (logOutApiModel != null) {
                    handleLogOutResponse(logOutApiModel);
                }
            }
        });
    }

    private void handleLogOutResponse(LogOutApiModel logOutApiModel) {
        if (!logOutApiModel.getError()) {

            AppSettings appSettings = new AppSettings(activity);
            appSettings.setIsLogged(FALSE);
            try {
                BaseUtils.stopLocationService(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            moveToSignIn();
        } else {
            UiUtils.snackBar(rootView, logOutApiModel.getErrorMessage());
        }

    }

    private void moveToSignIn() {
        Intent intent = new Intent(activity, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void viewProfileApiCall() {
        InputForAPI inputForAPI = new InputForAPI(activity);
        inputForAPI.setUrl(UrlHelper.VIEW_PROFILE);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setShowLoader(true);
        viewProfile(inputForAPI);
    }

    public void viewProfile(InputForAPI inputForAPI) {
        accountsFragViewModel.viewProfile(inputForAPI).observe(this, new Observer<ViewProfileApiModel>() {
            @Override
            public void onChanged(@Nullable ViewProfileApiModel viewProfileApiModel) {
                if (viewProfileApiModel != null) {
                    handelViewProfileResponse(viewProfileApiModel);
                }
            }
        });
    }

    public void handelViewProfileResponse(ViewProfileApiModel viewProfileApiModel) {

        if (viewProfileApiModel.getError()) {
            UiUtils.snackBar(rootView, viewProfileApiModel.getErrorMessage());

        } else {
            providerDetails = viewProfileApiModel.getProviderDetails();

            providerName.setText(providerDetails.getFirstName() + " " + providerDetails.getLastName());

            String userMobile = providerDetails.getMobile();
            if (userMobile.length() != 0 && !userMobile.equalsIgnoreCase("null")) {
                providerMobile.setText(userMobile);
            } else {
                providerMobile.setText("");
            }
            GlideHelper.setImage(providerDetails.getImage(),
                    profilePicture, UiUtils.getProfilePicture(activity));
        }
    }


    private void setThemeColor() {
        UiUtils.setProfilePicture(activity, profilePicture);
    }

    private void initViews(View view) {
        accountsFragViewModel = ViewModelProviders.of(this).get(AccountsFragViewModel.class);
        logOut = view.findViewById(R.id.logOut);
        rootView = view.findViewById(R.id.rootView);
        providerName = view.findViewById(R.id.providerName);
        providerMobile = view.findViewById(R.id.providerMobile);
        viewCategory = view.findViewById(R.id.viewCategory);
        viewSchedule = view.findViewById(R.id.viewSchedule);
        viewEditAddress = view.findViewById(R.id.viewEditAddress);
        profilePicture = view.findViewById(R.id.profilePic);
        changeProfile = view.findViewById(R.id.changeProfile);
        changePassword = view.findViewById(R.id.changePassword);
        changeThemIcon = view.findViewById(R.id.changeThemeIcon);
        changeThemeAdapter = view.findViewById(R.id.changeThemeAdapter);

        aboutUs = view.findViewById(R.id.aboutUs);
        appointments = view.findViewById(R.id.appointments);
        changeTheme = view.findViewById(R.id.changeTheme);
    }
}