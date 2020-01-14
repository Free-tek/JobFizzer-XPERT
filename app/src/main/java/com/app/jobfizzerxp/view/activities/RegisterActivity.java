package com.app.jobfizzerxp.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.SignUpApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.AppsettingsApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.TimeSlotApi;
import com.app.jobfizzerxp.model.categoryApi.CategoryApiModel;
import com.app.jobfizzerxp.model.categoryApi.ListCategory;
import com.app.jobfizzerxp.model.categoryApi.ListSubcategory;
import com.app.jobfizzerxp.model.categoryApi.SubCategoryApiModel;
import com.app.jobfizzerxp.model.googleMaps.AddressComponent;
import com.app.jobfizzerxp.model.googleMaps.GoogleMapApiModel;
import com.app.jobfizzerxp.model.googleMaps.Result;
import com.app.jobfizzerxp.utilities.customLibraries.CircleImageView;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.networkUtils.ConnectionUtils;
import com.app.jobfizzerxp.utilities.uiUtils.AsteriskPasswordTransformation;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.SharedHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.interfaces.CategoryListener;
import com.app.jobfizzerxp.utilities.interfaces.ImageUpload;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.utilities.uiUtils.MapDialog;
import com.app.jobfizzerxp.view.adapters.RegisterCategoryAdapter;
import com.app.jobfizzerxp.view.adapters.TimeSlotAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.viewModel.RegisterViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.app.jobfizzerxp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.FINE_LOCATION_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.READ_STORAGE_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.CAMERA_INTENT;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.GALLERY_INTENT;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private TextView mondayView, tuesdayView, wednesdayView, thursdayView,
            fridayView, saturdayView, sundayView;
    private int selectedTime;
    private JSONArray registerDetails = new JSONArray();
    private FrameLayout address_arrow, general_arrow, register_arrow, profile_arrow, category_arrow, available_arrow;
    private String[] genders;
    private Spinner genderSpinner;
    private LinearLayout generalLayout, addressLayout, registerLayout, profileLayout, categoryLayout, availableLayout;
    private LinearLayout general, address, register, profile, category, available_layout;
    private EditText firstName, lastName, userName, dateOfBirth, addressOne, addressTwo, city_et, zipCode, state_et, emailAddress, phoneNumber, passWord, confirmPassword, workExperience, aboutYou;

    private LinearLayout addCategory;
    private RecyclerView categoryItems;
    private JSONArray fullCategories = new JSONArray();
    private Button bottomButton;
    private TextView bottomText;
    private ImageView add_photos;
    private int currentDetails = 0;
    private int clickedDetails = 0;
    private Boolean isGeneralValid = false, isAddressValid = false, isRegisterValid = false, isProfileValid = false, isCategoryValid = false, isAvailableValid = false;
    private AppSettings appSettings = new AppSettings(RegisterActivity.this);
    private Boolean isFirstTime = true;
    private Calendar myCalendar = Calendar.getInstance();
    private CircleImageView profilePicture;

    private DatePickerDialog datePickerDialog;
    private float startX;
    private float startY;
    private static String TAG = RegisterActivity.class.getSimpleName();
    private String[] categoryName;
    private String[] subcategoryName;
    private File uploadFile;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private MapDialog customDialog;
    private ImageView map_cancel;
    private Button savelocation;
    private EditText cityAddress;
    private Boolean value = false;
    private String address_lat = "";
    private String address_lngg = "";
    private TextView googleMap;
    private ImageView googlMap;
    private String straddd;
    private GoogleMapApiModel mGoogleMapApiModel = null;
    private RegisterCategoryAdapter registerCategoryAdapter;
    private JSONArray categoryDetails = new JSONArray();
    private CommonViewModel commonViewModel;
    private RegisterViewModel mRegisterViewModel;
    private List<ListSubcategory> listSubcategories = new ArrayList<>();
    private List<ListCategory> listCategories = new ArrayList<>();
    private LinearLayout rootView;
    private List<TimeSlotApi> timeSlotApiList;
    private List<TimeSlotApi> savedTimeSlotList;

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String theme_value = SharedHelper.getKey(RegisterActivity.this, "theme_value");
        UiUtils.setTheme(RegisterActivity.this, theme_value);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);
        genders = new String[]{getResources().getString(R.string.male), getResources().getString(R.string.female), getResources().getString(R.string.other)};
        initViews();
        initAdapters();
        initListners();
        initMap();
        getTimeslotApi();

        if (appSettings.getPageNumber().equalsIgnoreCase("")) {
            setGeneral();
        } else {
            try {
                setValues();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getCategoryDataApi();
        isFirstTime = false;

    }


    public void setGoogleAddress() {
        if (mGoogleMapApiModel != null) {
            String address_line_1 = "";
            String address_line_2 = null;
            String city = null;
            String state = null;
            String country = null;
            String postal_code = null;
            List<AddressComponent> addressComponentList;
            addressComponentList = mGoogleMapApiModel.getResults().get(0).getAddressComponents();

            for (int i = 0; i < addressComponentList.size(); i++) {
                List<String> typesList;
                typesList = addressComponentList.get(i).getTypes();

                for (int j = 0; j < typesList.size(); j++) {
                    if (typesList.get(j).equalsIgnoreCase("postal_code")) {
                        postal_code = addressComponentList.get(i).getLongName();
                    }
                    if (typesList.get(j).equalsIgnoreCase("locality")) {
                        city = addressComponentList.get(i).getLongName();
                    }
                    if (typesList.get(j).equalsIgnoreCase("administrative_area_level_1")) {
                        state = addressComponentList.get(i).getLongName();
                    }
                    if (typesList.get(j).equalsIgnoreCase("sublocality_level_2")) {
                        address_line_2 = addressComponentList.get(i).getLongName();
                    }
                    if (typesList.get(j).equalsIgnoreCase("premise")
                            || typesList.get(j).equalsIgnoreCase("street_number")) {
                        address_line_1 = addressComponentList.get(i).getLongName();
                    }
                    if (typesList.get(j).equalsIgnoreCase("route")) {
                        address_line_1 = address_line_1 + " " + addressComponentList.get(i).getLongName();
                    }
                    if (typesList.get(j).equalsIgnoreCase("country")) {
                        country = addressComponentList.get(i).getLongName();
                    }

                }
            }
            BaseUtils.log("Address:", "address_line_1: " + address_line_1 + " address_line_2: " + address_line_2 +
                    " city: " + city + " state: " + state + " country: " + country + " postal_code: " + postal_code);

            addressOne.setText(address_line_1);
            addressTwo.setText(address_line_2);

            city_et.setText(city);
            state_et.setText(state);
            zipCode.setText(postal_code);
        }
    }


    private void initMap() {
        customDialog = new MapDialog(RegisterActivity.this);
        customDialog.setContentView(R.layout.dialog_setting_map);
        savelocation = customDialog.findViewById(R.id.savelocation);
        cityAddress = customDialog.findViewById(R.id.location_address);
        map_cancel = customDialog.findViewById(R.id.map_cancel);
        ImageView mapp = customDialog.findViewById(R.id.mapp);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.onCreate(customDialog.onSaveInstanceState());
        MapsInitializer.initialize(RegisterActivity.this);
        mapFragment.onResume();
        mapFragment.getMapAsync(this);

        setGoogleAddress();
    }

    private void moveToGoogleMap() {
        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        savelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.closeKeyboard(view);

                straddd = cityAddress.getText().toString();
                if (cityAddress.getText().toString().length() > 0) {
                    SharedHelper.putKey(RegisterActivity.this, "Slocation", straddd);
                    SharedHelper.putKey(RegisterActivity.this, "LAT", address_lat);
                    SharedHelper.putKey(RegisterActivity.this, "LNG", address_lngg);
                    setGoogleAddress();
                }
                if (customDialog.isShowing()) {
                    customDialog.dismiss();
                }
            }
        });
        map_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customDialog.isShowing()) {
                    customDialog.dismiss();
                }
            }
        });
    }

    private String buildUrl(double latitude, double longitude) {
        return commonViewModel.getGeoCodeMapUrl(latitude, longitude);
    }

    private void getAddressFromLocation(double latt, double lngg) {
        String url = buildUrl(latt, lngg);
        InputForAPI inputForAPI = new InputForAPI(this);
        inputForAPI.setUrl(url);
        inputForAPI.setShowLoader(false);
        inputForAPI.setHeaderStatus(false);

        commonViewModel.googleGeoCodeApi(inputForAPI).observe(this, new Observer<GoogleMapApiModel>() {
            @Override
            public void onChanged(@Nullable GoogleMapApiModel bookingadapterAddressModel) {
                if (bookingadapterAddressModel != null) {
                    handleAddressResponse(bookingadapterAddressModel);
                }
            }
        });
    }

    public void handleAddressResponse(GoogleMapApiModel response) {
        Result resultAddress;

        if (response.getStatus().equalsIgnoreCase("OK")) {
            mGoogleMapApiModel = response;
            List<Result> listDetailBooking;
            listDetailBooking = response.getResults();
            if (listDetailBooking.size() != 0) {
                resultAddress = listDetailBooking.get(0);
                String strAdd = resultAddress.getFormattedAddress();
                BaseUtils.log(TAG, "strAdd.comp: " + strAdd);
                cityAddress.setText(strAdd);
            }
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSIONS);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void getTimeslotApi() {
        InputForAPI inputForAPI = new InputForAPI(RegisterActivity.this);
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

    private void handleAppSettingResponse(@NonNull AppsettingsApiModel appsettingsApiModel) {
        timeSlotApiList = new ArrayList<>();
        savedTimeSlotList = new ArrayList<>();

        timeSlotApiList = appsettingsApiModel.getTimeSlotApiList();

        for (int i = 0; i < timeSlotApiList.size(); i++) {
            TimeSlotApi timeSlotApi = new TimeSlotApi();
            timeSlotApi = timeSlotApiList.get(i);
            timeSlotApiList.get(i).setSelected("false");

            for (int j = 0; j < 7; j++) {
                TimeSlotApi savedTimeSlot = new TimeSlotApi();
                savedTimeSlot.setTime_Slots_id("" + timeSlotApi.getId());
                if (j == 0) {
                    savedTimeSlot.setDays("Mon");
                }
                if (j == 1) {
                    savedTimeSlot.setDays("Tue");
                }
                if (j == 2) {
                    savedTimeSlot.setDays("Wed");
                }
                if (j == 3) {
                    savedTimeSlot.setDays("Thu");
                }
                if (j == 4) {
                    savedTimeSlot.setDays("Fri");
                }
                if (j == 5) {
                    savedTimeSlot.setDays("Sat");
                }
                if (j == 6) {
                    savedTimeSlot.setDays("Sun");
                }

                savedTimeSlot.setStatus("1");
                savedTimeSlotList.add(savedTimeSlot);
            }
        }
    }


    public void setGeneralValues() throws JSONException {
        JSONObject generalObject = new JSONObject();
        appSettings.setPageNumber("1");
        generalObject.put("first_name", firstName.getText().toString().trim());
        generalObject.put("last_name", lastName.getText().toString().trim());
        generalObject.put("user_name", userName.getText().toString());
        generalObject.put("dob", dateOfBirth.getText().toString());
        BaseUtils.log(TAG, "gender: " + genderSpinner.getSelectedItem().toString());
        generalObject.put("gender", genderSpinner.getSelectedItem().toString());

        registerDetails.put(0, generalObject);
    }

    public void setAddressValues() throws JSONException {
        JSONObject addressObject = new JSONObject();
        appSettings.setPageNumber("2");
        addressObject.put("address_line_one", addressOne.getText().toString());
        addressObject.put("address_line_two", addressTwo.getText().toString());
        addressObject.put("city", city_et.getText().toString());
        addressObject.put("state", state_et.getText().toString());
        addressObject.put("zipcode", zipCode.getText().toString());

        registerDetails.put(1, addressObject);
    }

    public void setRegisterValue() throws JSONException {
        JSONObject registerObject = new JSONObject();
        appSettings.setPageNumber("3");
        registerObject.put("email_address", emailAddress.getText().toString().trim());
        registerObject.put("phone", phoneNumber.getText().toString());
        registerObject.put("password", passWord.getText().toString());
        registerObject.put("confirm_password", confirmPassword.getText().toString());

        registerDetails.put(2, registerObject);
    }

    public void setProfileValue() throws JSONException {
        JSONObject profileObject = new JSONObject();
        appSettings.setPageNumber("4");
        profileObject.put("work_experience", workExperience.getText().toString());
        profileObject.put("about_you", aboutYou.getText().toString());


        registerDetails.put(3, profileObject);
    }

    public void setCategoryValue(Context context) throws JSONException {
        JSONObject categoryObject = new JSONObject();
        AppSettings appSettings = new AppSettings(context);
        appSettings.setPageNumber("5");
        categoryObject.put("category_details", categoryDetails);
        BaseUtils.log(TAG, "setCategoryValue: " + categoryObject);

        registerDetails.put(4, categoryObject);
    }

    private void setValues() {
        try {
            registerDetails = appSettings.getRegisterArray();
            JSONObject generalObject = registerDetails.optJSONObject(0);
            JSONObject addressObject = registerDetails.optJSONObject(1);
            JSONObject registerObject = registerDetails.optJSONObject(2);
            JSONObject profileObject = registerDetails.optJSONObject(3);
            JSONObject categoryObject = registerDetails.optJSONObject(4);
            JSONObject availableObject = registerDetails.optJSONObject(5);

            firstName.setText(generalObject.optString("first_name"));
            lastName.setText(generalObject.optString("last_name"));
            userName.setText(generalObject.optString("user_name"));
            dateOfBirth.setText(generalObject.optString("dob"));

//            if (appSettings.getProfilePic() != null) {
//                add_photos.setVisibility(View.GONE);
//                uploadFile = new File(appSettings.getProfilePic());
//                loadImage(appSettings.getProfilePic(), profilePicture,
//                        UiUtils.getProfilePicture(RegisterActivity.this));
//            }

            if (generalObject.optString("gender").equalsIgnoreCase(getString(R.string.male))) {
                genderSpinner.setSelection(0);
            } else if (generalObject.optString("gender").equalsIgnoreCase(getString(R.string.female))) {
                genderSpinner.setSelection(1);
            } else {
                genderSpinner.setSelection(2);
            }

            addressOne.setText(addressObject.optString("address_line_one"));
            addressTwo.setText(addressObject.optString("address_line_two"));
            city_et.setText(addressObject.optString("city"));
            state_et.setText(addressObject.optString("state"));
            zipCode.setText(addressObject.optString("zipcode"));

            emailAddress.setText(registerObject.optString("email_address"));
            phoneNumber.setText(registerObject.optString("phone"));
            passWord.setText(registerObject.optString("password"));
            confirmPassword.setText(registerObject.optString("confirm_password"));

            workExperience.setText(profileObject.optString("work_experience"));
            aboutYou.setText(profileObject.optString("about_you"));

        } catch (Exception e) {
            e.printStackTrace();

        }

        categoryDetails = new JSONArray();
        registerCategoryAdapter = new RegisterCategoryAdapter(RegisterActivity.this, categoryDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RegisterActivity.this, LinearLayoutManager.HORIZONTAL, false);
        categoryItems.setLayoutManager(linearLayoutManager);
        categoryItems.setAdapter(registerCategoryAdapter);
        removeObject();

        if (appSettings.getPageNumber().equalsIgnoreCase("1")) {
            setGeneral();
            validGeneral();
            setAlpha();
        } else if (appSettings.getPageNumber().equalsIgnoreCase("2")) {
            isGeneralValid = true;
            setAddress();
            validAddress();
            setAlpha();
        } else if (appSettings.getPageNumber().equalsIgnoreCase("3")) {
            isGeneralValid = true;
            isAddressValid = true;
            setRegister();
            validRegister();
            setAlpha();

        } else if (appSettings.getPageNumber().equalsIgnoreCase("4")) {
            isGeneralValid = true;
            isAddressValid = true;
            isRegisterValid = true;
            setProfile();
            validProfile();
            setAlpha();
        } else if (appSettings.getPageNumber().equalsIgnoreCase("5")) {
            isGeneralValid = true;
            isAddressValid = true;
            isRegisterValid = true;
            isProfileValid = true;
            setCategory();
            validCategory();
            setAlpha();

        } else if (appSettings.getPageNumber().equalsIgnoreCase("6")) {
            isGeneralValid = true;
            isAddressValid = true;
            isRegisterValid = true;
            isProfileValid = true;
            isCategoryValid = true;
            setAvailable();
            setAlpha();
        }
    }


    private void initAdapters() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.items_spinner, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        registerCategoryAdapter = new RegisterCategoryAdapter(RegisterActivity.this, categoryDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RegisterActivity.this, LinearLayoutManager.HORIZONTAL, false);
        categoryItems.setLayoutManager(linearLayoutManager);
        categoryItems.setAdapter(registerCategoryAdapter);
        removeObject();

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    setGeneralValues();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void slideFromLeft(View view) {
        view.setVisibility(View.VISIBLE);
        view.startAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.slidefromleft));
    }

    private void initListners() {
        general.setOnClickListener(this);
        address.setOnClickListener(this);
        register.setOnClickListener(this);
        profile.setOnClickListener(this);
        category.setOnClickListener(this);
        addCategory.setOnClickListener(this);
        mondayView.setOnClickListener(this);
        tuesdayView.setOnClickListener(this);
        wednesdayView.setOnClickListener(this);
        thursdayView.setOnClickListener(this);
        fridayView.setOnClickListener(this);
        saturdayView.setOnClickListener(this);
        sundayView.setOnClickListener(this);
        bottomButton.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
        bottomText.setOnClickListener(this);
        googlMap.setOnClickListener(this);
        googleMap.setOnClickListener(this);

        dateOfBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();
                        if (UiUtils.onTouch(startX, endX, startY, endY)) {
                            UiUtils.closeKeyboard(view);
                            datePickerDialog.show();
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setAlpha() {
        if (!isAddressValid) {
            address.setAlpha((float) 0.5);
        } else {

            address.setAlpha(1);
        }
        if (!isRegisterValid) {
            register.setAlpha((float) 0.5);
        } else {

            register.setAlpha(1);
        }
        if (!isProfileValid) {
            profile.setAlpha((float) 0.5);
        } else {

            profile.setAlpha(1);
        }
        if (!isCategoryValid) {
            category.setAlpha((float) 0.5);
        } else {

            category.setAlpha(1);
        }
        if (!isGeneralValid) {
            general.setAlpha((float) 0.5);
        } else {

            general.setAlpha(1);
        }
    }

    private void setGeneral() {
        bottomButton.setText(getResources().getString(R.string.next));
        currentDetails = 0;
        bottomText.setVisibility(View.VISIBLE);
        addressLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
        availableLayout.setVisibility(View.GONE);

        setAlpha();
        slideFromLeft(generalLayout);

        address_arrow.setVisibility(View.INVISIBLE);
        general_arrow.setVisibility(View.VISIBLE);
        register_arrow.setVisibility(View.INVISIBLE);
        profile_arrow.setVisibility(View.INVISIBLE);
        category_arrow.setVisibility(View.INVISIBLE);
        available_arrow.setVisibility(View.GONE);
    }

    private void setAddress() {
        currentDetails = 1;
        bottomButton.setText(getResources().getString(R.string.next));

        bottomText.setVisibility(View.VISIBLE);

        generalLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
        availableLayout.setVisibility(View.GONE);

        setAlpha();

        slideFromLeft(addressLayout);
        address_arrow.setVisibility(View.VISIBLE);
        general_arrow.setVisibility(View.INVISIBLE);
        register_arrow.setVisibility(View.INVISIBLE);
        profile_arrow.setVisibility(View.INVISIBLE);
        category_arrow.setVisibility(View.INVISIBLE);
        available_arrow.setVisibility(View.GONE);
    }

    private void setRegister() {
        currentDetails = 2;
        bottomButton.setText(getResources().getString(R.string.next));

        bottomText.setVisibility(View.VISIBLE);

        generalLayout.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
        availableLayout.setVisibility(View.GONE);

        setAlpha();


        slideFromLeft(registerLayout);

        address_arrow.setVisibility(View.INVISIBLE);
        general_arrow.setVisibility(View.INVISIBLE);
        register_arrow.setVisibility(View.VISIBLE);
        profile_arrow.setVisibility(View.INVISIBLE);
        category_arrow.setVisibility(View.INVISIBLE);
        available_arrow.setVisibility(View.GONE);
    }

    private void setProfile() {
        currentDetails = 3;
        bottomButton.setText(getResources().getString(R.string.next));

        bottomText.setVisibility(View.VISIBLE);

        setAlpha();

        generalLayout.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
        availableLayout.setVisibility(View.GONE);

        slideFromLeft(profileLayout);
        address_arrow.setVisibility(View.INVISIBLE);
        general_arrow.setVisibility(View.INVISIBLE);
        register_arrow.setVisibility(View.INVISIBLE);
        profile_arrow.setVisibility(View.VISIBLE);
        category_arrow.setVisibility(View.INVISIBLE);
        available_arrow.setVisibility(View.GONE);
    }

    private void setCategory() {
        currentDetails = 4;
        bottomButton.setText(getResources().getString(R.string.finish));

        bottomText.setVisibility(View.VISIBLE);

        setAlpha();

        generalLayout.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
        availableLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        slideFromLeft(categoryLayout);
        address_arrow.setVisibility(View.INVISIBLE);
        general_arrow.setVisibility(View.INVISIBLE);
        register_arrow.setVisibility(View.INVISIBLE);
        profile_arrow.setVisibility(View.INVISIBLE);
        category_arrow.setVisibility(View.VISIBLE);
        available_arrow.setVisibility(View.GONE);
    }

    private void setAvailable() {
        currentDetails = 5;

        bottomText.setVisibility(View.VISIBLE);
        setAlpha();
        generalLayout.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.GONE);

        address_arrow.setVisibility(View.INVISIBLE);
        general_arrow.setVisibility(View.INVISIBLE);
        register_arrow.setVisibility(View.INVISIBLE);
        profile_arrow.setVisibility(View.INVISIBLE);
        category_arrow.setVisibility(View.VISIBLE);
        available_arrow.setVisibility(View.GONE);
    }

    private void initViews() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        rootView = findViewById(R.id.rootView);
        address_arrow = findViewById(R.id.address_arrow);
        general_arrow = findViewById(R.id.general_arrow);
        register_arrow = findViewById(R.id.register_arrow);
        profile_arrow = findViewById(R.id.profile_arrow);
        category_arrow = findViewById(R.id.category_arrow);
        available_arrow = findViewById(R.id.available_arrow);

        generalLayout = findViewById(R.id.generalLayout);
        addressLayout = findViewById(R.id.addressLayout);
        registerLayout = findViewById(R.id.registerLayout);
        profileLayout = findViewById(R.id.profileLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        availableLayout = findViewById(R.id.availableLayout);

        general = findViewById(R.id.general);
        address = findViewById(R.id.address);
        register = findViewById(R.id.register);
        profile = findViewById(R.id.profile);
        category = findViewById(R.id.category);
        available_layout = findViewById(R.id.available);

        addCategory = findViewById(R.id.addCategory);
        categoryItems = findViewById(R.id.categoryItems);
        mondayView = findViewById(R.id.mondayView);
        tuesdayView = findViewById(R.id.tuesdayView);
        wednesdayView = findViewById(R.id.wednesdayView);
        thursdayView = findViewById(R.id.thursdayView);
        fridayView = findViewById(R.id.fridayView);
        saturdayView = findViewById(R.id.saturdayView);
        sundayView = findViewById(R.id.sundayView);

        bottomButton = findViewById(R.id.bottomButton);
        bottomText = findViewById(R.id.bottomText);
        googlMap = findViewById(R.id.googlMap);
        googleMap = findViewById(R.id.googleMap);
        add_photos = findViewById(R.id.add_photos);

        profilePicture = findViewById(R.id.profilePicture);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userName = findViewById(R.id.userName);
        dateOfBirth = findViewById(R.id.dateOfBirth);

        genderSpinner = findViewById(R.id.genderSpinner);
        addressOne = findViewById(R.id.addressOne);
        addressTwo = findViewById(R.id.addressTwo);
        city_et = findViewById(R.id.city);
        state_et = findViewById(R.id.state);
        zipCode = findViewById(R.id.zipCode);

        emailAddress = findViewById(R.id.emailAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        passWord = findViewById(R.id.passWord);
        confirmPassword = findViewById(R.id.confirmPassword);
        workExperience = findViewById(R.id.workExperience);
        aboutYou = findViewById(R.id.aboutYou);

        setFilters();
        setPasswordTransformation();
        setTextWatchers();
        setCurrentDate();
    }

    private void setCurrentDate() {
        datePickerDialog = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.updateDate(2000, 6, 12);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void setTextWatchers() {
        firstName.addTextChangedListener(new GenericTextWatcher("1"));
        lastName.addTextChangedListener(new GenericTextWatcher("1"));
        userName.addTextChangedListener(new GenericTextWatcher("1"));
        dateOfBirth.addTextChangedListener(new GenericTextWatcher("1"));
        addressOne.addTextChangedListener(new GenericTextWatcher("2"));
        addressTwo.addTextChangedListener(new GenericTextWatcher("2"));

        city_et.addTextChangedListener(new GenericTextWatcher("2"));
        state_et.addTextChangedListener(new GenericTextWatcher("2"));

        zipCode.addTextChangedListener(new GenericTextWatcher("2"));
        emailAddress.addTextChangedListener(new GenericTextWatcher("3"));
        phoneNumber.addTextChangedListener(new GenericTextWatcher("3"));
        passWord.addTextChangedListener(new GenericTextWatcher("3"));
        confirmPassword.addTextChangedListener(new GenericTextWatcher("3"));
        workExperience.addTextChangedListener(new GenericTextWatcher("4"));
        aboutYou.addTextChangedListener(new GenericTextWatcher("4"));
    }

    private void setPasswordTransformation() {
        passWord.setTransformationMethod(new AsteriskPasswordTransformation());
        confirmPassword.setTransformationMethod(new AsteriskPasswordTransformation());
    }

    private void setFilters() {
        firstName.setFilters(new InputFilter[]{ValidationsUtils.emojiSpecialFilter, new InputFilter.LengthFilter(15)});
        lastName.setFilters(new InputFilter[]{ValidationsUtils.emojiSpecialFilter, new InputFilter.LengthFilter(15)});

        addressOne.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        addressTwo.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        city_et.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        state_et.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        workExperience.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        aboutYou.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});

        passWord.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
        confirmPassword.setFilters(new InputFilter[]{ValidationsUtils.whiteSpaceFilter});
    }

    public String validGeneral() {
        String value = "true";

        if (firstName.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_firstname);
        } else if (lastName.getText().toString().trim().length() == 0) {

            return getResources().getString(R.string.enter_lastname);
        } else if (dateOfBirth.getText().length() == 0) {
            return getResources().getString(R.string.enter_dob);

        } else {
            isGeneralValid = true;
            return value;
        }
    }

    public String validAddress() {
        String value = "true";

        if (addressOne.getText().toString().trim().length() == 0 || addressTwo.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_address);

        } else if (city_et.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_city);

        } else if (state_et.getText().toString().trim().length() == 0) {

            return getResources().getString(R.string.enter_state);

        } else if (zipCode.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_zipcode);

        } else {
            isAddressValid = true;
            return value;
        }
    }

    public String validRegister() {
        String value = "true";

        if (emailAddress.getText().toString().trim().length() == 0 ||
                ValidationsUtils.isInValidEmail(emailAddress.getText().toString().trim())) {
            return getResources().getString(R.string.please_enter_valid_email);
        } else if (phoneNumber.getText().length() == 0) {
            return getResources().getString(R.string.enter_phonenumber);

        } else if (phoneNumber.getText().toString().trim().length() < 5) {
            return getResources().getString(R.string.enter_phonelength);
        } else if (passWord.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_password);

        } else if (confirmPassword.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_confirmpassword);

        } else if (!passWord.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {
            return getResources().getString(R.string.password_not_match);

        } else if (passWord.getText().toString().trim().length() < 6) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (confirmPassword.getText().toString().trim().length() < 6) {
            return getResources().getString(R.string.password_must_be_six);
        } else {
            isRegisterValid = true;
            return value;
        }
    }


    public String validProfile() {
        String value = "true";

        if (workExperience.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_work_experience);

        } else if (aboutYou.getText().toString().trim().length() == 0) {
            return getResources().getString(R.string.enter_about_you);

        } else if (uploadFile == null) {
            return getResources().getString(R.string.please_upload_an_image);
        } else {
            isProfileValid = true;
            return value;
        }
    }

    public String validCategory() {
        String value = "true";

        if (categoryDetails.length() == 0) {
            return getResources().getString(R.string.please_select_categroy);
        } else {
            isCategoryValid = true;
            return value;
        }
    }

    @Override
    public void onClick(View view) {

        if (view == general) {
            UiUtils.closeKeyboard(view);
            clickedDetails = 0;
            if (generalValidation().equalsIgnoreCase("true")) {
                setGeneral();
            } else {
                UiUtils.snackBar(rootView, generalValidation());
            }

        } else if (view == address) {
            UiUtils.closeKeyboard(view);
            clickedDetails = 1;

            if (generalValidation().equalsIgnoreCase("true")) {
                setAddress();

            } else {
                UiUtils.snackBar(rootView, generalValidation());
            }

        } else if (view == bottomText) {
            moveToSignIn();

        } else if (view == googlMap || view == googleMap) {
            if (BaseUtils.checkGpsIsEnabled(this)) {
                UiUtils.snackBar(rootView,getResources().getString(R.string.enable_gps));
            } else {
                moveToGoogleMap();
            }

        } else if (view == profilePicture) {
            UiUtils.showPictureDialog(RegisterActivity.this, Constants.CAMERA.FILE_NAME_PROFILE);
        } else if (view == register) {
            UiUtils.closeKeyboard(view);
            clickedDetails = 2;

            if (generalValidation().equalsIgnoreCase("true")) {
                setRegister();

            } else {
                UiUtils.snackBar(rootView, generalValidation());
            }

        } else if (view == profile) {
            UiUtils.closeKeyboard(view);
            clickedDetails = 3;

            if (generalValidation().equalsIgnoreCase("true")) {
                setProfile();

            } else {
                UiUtils.snackBar(rootView, generalValidation());
            }


        } else if (view == category) {
            UiUtils.closeKeyboard(view);
            clickedDetails = 4;

            if (generalValidation().equalsIgnoreCase("true")) {
                setCategory();

            } else {
                UiUtils.snackBar(rootView, generalValidation());
            }

        } else if (view == addCategory) {
            if (ConnectionUtils.isNetworkConnected(RegisterActivity.this)) {
                showAddDialog();
            } else {
                UiUtils.snackBar(rootView, getResources().getString(R.string.no_internet_connection));
            }

        } else if (view == mondayView) {
            selectedTime = 0;
            showTimeSlotDialog();
        } else if (view == tuesdayView) {
            selectedTime = 1;
            showTimeSlotDialog();
        } else if (view == wednesdayView) {
            selectedTime = 2;
            showTimeSlotDialog();
        } else if (view == thursdayView) {
            selectedTime = 3;
            showTimeSlotDialog();
        } else if (view == fridayView) {
            selectedTime = 4;
            showTimeSlotDialog();
        } else if (view == saturdayView) {
            selectedTime = 5;
            showTimeSlotDialog();
        } else if (view == sundayView) {
            selectedTime = 6;
            showTimeSlotDialog();
        } else if (view == bottomButton) {
            if (ConnectionUtils.isNetworkConnected(RegisterActivity.this)) {
                UiUtils.closeKeyboard(view);
                if (currentDetails == 0) {
                    if (validGeneral().equalsIgnoreCase("true")) {
                        setAddress();
                    } else {
                        UiUtils.snackBar(rootView, validGeneral());
                    }
                } else if (currentDetails == 1) {
                    if (validAddress().equalsIgnoreCase("true")) {
                        setRegister();
                    } else {
                        UiUtils.snackBar(rootView, validAddress());
                    }

                } else if (currentDetails == 2) {
                    if (validRegister().equalsIgnoreCase("true")) {
                        setProfile();
                    } else {
                        UiUtils.snackBar(rootView, validRegister());
                    }

                } else if (currentDetails == 3) {
                    if (validProfile().equalsIgnoreCase("true")) {
                        setCategory();
                    } else {
                        UiUtils.snackBar(rootView, validProfile());
                    }
                } else if (currentDetails == 4) {
                    UiUtils.show(RegisterActivity.this);
                    if (validCategory().equalsIgnoreCase("true")) {
                        setAvailable();
                        if (validProfile().equalsIgnoreCase("true")) {
                            imageUpload(RegisterActivity.this, uploadFile);

                        } else {
                            UiUtils.dismiss();
                            UiUtils.snackBar(rootView, validProfile());
                        }

                    } else {
                        UiUtils.dismiss();
                        UiUtils.snackBar(rootView, validCategory());

                    }
                }
            } else {
                UiUtils.snackBar(rootView, getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    public void promptEnableGpsDialog() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                getLastKnownLocation(true);
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(RegisterActivity.this, 203);
                    } catch (IntentSender.SendIntentException sendEx) {
                        sendEx.printStackTrace();
                    }
                }
            }
        });
    }

    private void imageUpload(Activity activity, File imageFile) {
        new HitImageUpload(activity, imageFile, new ImageUpload() {
            @Override
            public void onFinish(String imageUrl) {
                if (imageUrl != null) {
                    try {
                        registerValues(imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    UiUtils.dismiss();
                    UiUtils.snackBar(rootView, activity.getString(R.string.please_upload_an_image));
                }

            }
        }).execute();
    }

    private class HitImageUpload extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        private File imageFile;
        private ImageUpload imageUpload;

        public HitImageUpload(Activity activity, File uploadFile, ImageUpload imageUpload) {
            this.activity = activity;
            this.imageFile = uploadFile;
            this.imageUpload = imageUpload;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imageUpload(activity, imageFile, imageUpload);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void imageUpload(Activity activity, File imageFile, ImageUpload imageUpload) {
        File imageFiles = UiUtils.getCompressedFile(activity, imageFile);
        final InputForAPI inputForAPI = new InputForAPI(activity);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setFile(imageFiles);

        commonViewModel.uploadImageApi(inputForAPI).observe((LifecycleOwner) activity,
                new Observer<ImageUploadApiModel>() {
                    @Override
                    public void onChanged(@Nullable ImageUploadApiModel imageUploadApiModel) {
                        if (imageUploadApiModel != null) {
                            if (!imageUploadApiModel.getError()) {
                                BaseUtils.log(TAG, "image: " + imageUploadApiModel.getImage());
                                imageUpload.onFinish(imageUploadApiModel.getImage());

                            } else {
                                imageUpload.onFinish(null);
                            }
                        } else {
                            imageUpload.onFinish(null);
                        }
                    }
                });
    }

    private void registerValues(String imageUrl) throws JSONException {
        registerDetails = appSettings.getRegisterArray();
        JSONObject jsonObject = new JSONObject();

        JSONObject generalObject = registerDetails.optJSONObject(0);
        JSONObject addressObject = registerDetails.optJSONObject(1);
        JSONObject registerObject = registerDetails.optJSONObject(2);
        JSONObject profileObject = registerDetails.optJSONObject(3);
        JSONObject categoryObject = registerDetails.optJSONObject(4);
        JSONObject availableObject = registerDetails.optJSONObject(5);

        categoryDetails = categoryObject.optJSONArray("category_details");
        jsonObject.put("first_name", generalObject.optString("first_name"));
        jsonObject.put("last_name", generalObject.optString("last_name"));
        jsonObject.put("dob", generalObject.optString("dob"));

        jsonObject.put("address1", addressObject.optString("address_line_one"));
        jsonObject.put("address2", addressObject.optString("address_line_two"));
        jsonObject.put("city", addressObject.optString("city"));
        jsonObject.put("state", addressObject.optString("state"));
        jsonObject.put("zipcode", addressObject.optString("zipcode"));

        jsonObject.put("about", profileObject.optString("about_you"));
        jsonObject.put("workexperience", profileObject.optString("work_experience"));

        jsonObject.put("email", registerObject.optString("email_address"));
        jsonObject.put("password", registerObject.optString("password"));
        jsonObject.put("mobile", registerObject.optString("phone"));

        jsonObject.put("gender", generalObject.optString("gender"));
        jsonObject.put("image", imageUrl);

        appSettings.setCategoryArray(getCategoyArray());
        BaseUtils.log(TAG, "setCategoryArray: " + appSettings.getCategoryArray().toString());

        jsonObject.put("category", appSettings.getCategoryArray().toString());
        jsonObject.put("schedules", getSchedules());

        BaseUtils.log(TAG, "registerValues:" + registerDetails);
        InputForAPI inputForAPI = new InputForAPI(RegisterActivity.this);
        inputForAPI.setUrl(UrlHelper.SIGN_UP);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(false);
        inputForAPI.setHeaderStatus(false);

        mRegisterViewModel.signUp(inputForAPI).observe(this, new Observer<SignUpApiModel>() {
            @Override
            public void onChanged(@Nullable SignUpApiModel signUpApiModel) {
                UiUtils.dismiss();
                if (signUpApiModel != null) {
                    if (!signUpApiModel.getError()) {
                        timeSlotApiList = new ArrayList<>();
                        savedTimeSlotList = new ArrayList<>();

                        appSettings.setRegisterArray(new JSONArray());
                        appSettings.setSchedules(new ArrayList<>());
                        appSettings.setCategoryArray(new JSONArray());

                        isGeneralValid = false;
                        isAddressValid = false;
                        isRegisterValid = false;
                        isProfileValid = false;
                        isCategoryValid = false;

                        appSettings.setPageNumber("");
                        setGeneral();
                        setAlpha();

                        moveToSignIn();
                    } else {
                        UiUtils.snackBar(rootView, signUpApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private String getSchedules() {
        for (int i = 0; i < savedTimeSlotList.size(); i++) {
            savedTimeSlotList.get(i).setSelected(null);
        }
        BaseUtils.log(TAG, "savedTimeSlotList: " + savedTimeSlotList);

        return new Gson().toJson(savedTimeSlotList);
    }

    private JSONArray getCategoyArray() {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < categoryDetails.length(); i++) {
            JSONObject jsonObject = new JSONObject();
            categoryDetails.optJSONObject(i).remove("categoryMainName");
            categoryDetails.optJSONObject(i).remove("sub_category_name");
            jsonObject = categoryDetails.optJSONObject(i);
            jsonArray.put(jsonObject);
        }
        BaseUtils.log(TAG, "getCategoyArray: " + jsonArray);
        return jsonArray;
    }


    private void moveToSignIn() {
        Intent signin = new Intent(RegisterActivity.this, SignInActivity.class);
        startActivity(signin);
        finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseUtils.log(TAG, "onRequestPermissionsResult:" + requestCode);
        switch (requestCode) {

            case CAMERA_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    UiUtils.openCamera(RegisterActivity.this, Constants.CAMERA.FILE_NAME_PROFILE);
                } else {
                    UiUtils.snackBar(rootView, getResources().getString(R.string.camera_permission_error));
                }
                break;


            case READ_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UiUtils.openGallery(RegisterActivity.this);
                } else {
                    UiUtils.snackBar(rootView, getString(R.string.storage_permission_error));

                }
                break;

            case FINE_LOCATION_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    UiUtils.snackBar(rootView, getString(R.string.permission_denied));
                }
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseUtils.log(TAG, "onActivityResult: " + requestCode);
        switch (requestCode) {

            case GALLERY_INTENT://gallery
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        handleimage(uri);
                    } else {
                        UiUtils.snackBar(rootView, getResources().getString(R.string.unable_to_select));
                    }
                }
                break;

            case CAMERA_INTENT://camera
                uploadFile = new File(String.valueOf(appSettings.getImageUploadPath()));
                appSettings.setProfilePic(appSettings.getImageUploadPath());
                BaseUtils.log(TAG, "proPic: " + appSettings.getProfilePic());
                if (uploadFile.exists()) {
                    add_photos.setVisibility(View.GONE);
                    BaseUtils.log(TAG, "onActivityResult: " + appSettings.getImageUploadPath());
                    loadImage(appSettings.getImageUploadPath(), profilePicture, UiUtils.getProfilePicture(RegisterActivity.this));
                }
                break;

            case 203:
                switch (resultCode) {
                    case RESULT_OK:
//                    startLocationUpdate();
//                        getLastKnownLocation(true);
                        break;
                    case RESULT_CANCELED:
//                        statusCheck();
                        break;
                    default:
                        break;
                }

                break;
        }
    }

//    private void getLastKnownLocation(final boolean shouldReload) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                fusedLocationClient.requestLocationUpdates(mLocationRequest, null);
//                Task locationResult = fusedLocationClient.getLastLocation();
//                locationResult.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//
//                            Location location = (Location) task.getResult();
//                            if (location != null) {
//                                printLog("Last location retrieved not null ");
//                                currentLatitude = location.getLatitude();
//                                currentLongitude = location.getLongitude();
//                                JSONObject jsonObject = new JSONObject();
//
//
//                                if (shouldReload) {
//                                    getMyCelldID(currentLatitude, currentLongitude);
//                                    getOngoingBooking();
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//
//        }
//    }

    private void handleimage(Uri uri) {
        add_photos.setVisibility(View.GONE);
        appSettings.setProfilePic(BaseUtils.getRealPathFromUriNew(RegisterActivity.this, uri));
        BaseUtils.log(TAG, "proPic: " + appSettings.getProfilePic());
        loadImage(BaseUtils.getRealPathFromUriNew(RegisterActivity.this, uri), profilePicture, UiUtils.getProfilePicture(RegisterActivity.this));
        uploadFile = new File(BaseUtils.getRealPathFromURI(RegisterActivity.this, uri));

    }

    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        GlideHelper.setImage(url, imageView, drawable);
    }


    private String generalValidation() {
        String value = "true";


        if (currentDetails <= clickedDetails) {

            if (currentDetails == 0) {
                if (validGeneral().equalsIgnoreCase("true")) {
                    return value;
                } else {
                    return validGeneral();
                }
            } else if (currentDetails == 1) {
                if (validAddress().equalsIgnoreCase("true")) {

                    return value;
                } else {
                    return validAddress();
                }

            } else if (currentDetails == 2) {
                if (validRegister().equalsIgnoreCase("true")) {

                    return value;
                } else {
                    return validRegister();

                }

            } else if (currentDetails == 3) {
                if (validProfile().equalsIgnoreCase("true")) {

                    return value;
                } else {
                    return validProfile();
                }
            } else if (currentDetails == 4) {
                if (validCategory().equalsIgnoreCase("true")) {

                    return value;
                } else {
                    return validCategory();
                }
            } else return value;
        } else {

            return value;
        }


    }

    private void removeObject() {
        registerCategoryAdapter.setCategoryListener(new CategoryListener() {
            @Override
            public void removeCategory(int position, Context context) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    categoryDetails.remove(position);
                }
                AppSettings appSettings = new AppSettings(context);
                registerCategoryAdapter = new RegisterCategoryAdapter(context, categoryDetails);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false);
                categoryItems.setLayoutManager(linearLayoutManager);
                categoryItems.setAdapter(registerCategoryAdapter);
                registerCategoryAdapter.notifyDataSetChanged();
                removeObject();
                try {
                    setCategoryValue(context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appSettings.setRegisterArray(registerDetails);

            }
        });
    }

    private void showAddDialog() {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_category_add);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        dialog.show();


        final Spinner categorySpinner, subCategorySpinner;
        final EditText pricePerhour, quickPitch, experience;
        Button addButton;
        CardView cardView;

        categorySpinner = dialog.findViewById(R.id.categorySpinner);
        subCategorySpinner = dialog.findViewById(R.id.subCategorySpinner);
        pricePerhour = dialog.findViewById(R.id.pricePerhour);
        quickPitch = dialog.findViewById(R.id.quickPitch);
        experience = dialog.findViewById(R.id.experience);
        addButton = dialog.findViewById(R.id.addButton);
        cardView = dialog.findViewById(R.id.cardView);


        quickPitch.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});

        BaseUtils.log(TAG, "categoryDetails: " + fullCategories);


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.items_spinner, categoryName);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);


        categorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    BaseUtils.log(TAG, "onTouch: ");
                    UiUtils.closeKeyboard(view);
                }
                return false;
            }
        });
        subCategorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    UiUtils.closeKeyboard(view);
                }
                return false;
            }
        });


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                requestListSubApi(i, subCategorySpinner, RegisterActivity.this);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.closeKeyboard(view);

//                if (pricePerhour.getText().toString().trim().length() != 0 )
                checkAddValues(cardView, quickPitch, pricePerhour, experience, categorySpinner, subCategorySpinner, dialog);
//                else
//                    UiUtils.toast(rootView, "Price should not be zero");
            }
        });
    }


    private void checkAddValues(CardView cardView, EditText quickPitch, EditText pricePerhour, EditText experience, Spinner categorySpinner, Spinner subCategorySpinner, Dialog dialog) {
        if (categoryName[categorySpinner.getSelectedItemPosition()].length() > 0) {
            if (subcategoryName.length > 0) {
                if (subcategoryName[subCategorySpinner.getSelectedItemPosition()].length() > 0) {
                    if (experience.getText().toString().trim().length() > 0) {
                        if (pricePerhour.getText().toString().trim().length() != 0 && Integer.parseInt(pricePerhour.getText().toString()) != 0) {
                            if (quickPitch.getText().toString().trim().length() > 0) {
                                setAdapter(quickPitch, pricePerhour, experience, categorySpinner, subCategorySpinner, dialog);
                            } else {
                                UiUtils.snackBar(cardView, getString(R.string.please_enter_quick_pitch));
                            }
                        } else {
                            UiUtils.snackBar(cardView, getString(R.string.please_enter_price_per_ho));
                        }
                    } else {
                        UiUtils.snackBar(cardView, getString(R.string.please_enter_experience));
                    }
                } else {
                    UiUtils.snackBar(cardView, getString(R.string.please_select_subcategroy));
                }
            } else {
                UiUtils.snackBar(cardView, getString(R.string.no_sub_categroy));
            }
        } else {
            UiUtils.snackBar(cardView, getString(R.string.please_select_categroy));
        }
    }

    private void setAdapter(EditText quickPitch, EditText pricePerhour, EditText experience,
                            Spinner categorySpinner, Spinner subCategorySpinner, Dialog dialog) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("categoryMainName", categoryName[categorySpinner.getSelectedItemPosition()]);
            jsonObject.put("sub_category_name", subcategoryName[subCategorySpinner.getSelectedItemPosition()]);
            jsonObject.put("sub_category_id", listSubcategories.get(subCategorySpinner.getSelectedItemPosition()).getId());
            jsonObject.put("quickpitch", quickPitch.getText().toString().trim());
            jsonObject.put("priceperhour", pricePerhour.getText().toString().trim());
            jsonObject.put("experience", experience.getText().toString().trim());
            jsonObject.put("category_id", listCategories.get(categorySpinner.getSelectedItemPosition()).getId());
            jsonObject.put("status", "1");
            categoryDetails.put(jsonObject);

            registerCategoryAdapter = new RegisterCategoryAdapter(RegisterActivity.this, categoryDetails);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RegisterActivity.this, LinearLayoutManager.HORIZONTAL, false);
            categoryItems.setLayoutManager(linearLayoutManager);
            categoryItems.setAdapter(registerCategoryAdapter);
            removeObject();
            setCategoryValue(RegisterActivity.this);
            appSettings.setRegisterArray(registerDetails);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        UiUtils.closeKeyboard(quickPitch);
        dialog.dismiss();
    }

    private void getCategoryDataApi() {
        InputForAPI inputForAPI = new InputForAPI(RegisterActivity.this);
        inputForAPI.setUrl(UrlHelper.LIST_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(false);

        commonViewModel.listCategory(inputForAPI).observe(this, new Observer<CategoryApiModel>() {
            @Override
            public void onChanged(@Nullable CategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        handleListCategorySuccess(addCategoryApiModel);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
        registerCategoryAdapter.notifyDataSetChanged();
    }

    private void handleListCategorySuccess(CategoryApiModel addCategoryApiModel) {
        listCategories = new ArrayList<>();
        listCategories = addCategoryApiModel.getListCategory();
        if (listCategories != null) {
            categoryName = new String[listCategories.size()];
            for (int i = 0; i < listCategories.size(); i++) {
                categoryName[i] = listCategories.get(i).getCategoryName();
            }
        }
    }

    @NonNull
    private JSONObject getJsonObjectListSub(int categoryPosition) {
        JSONObject input = new JSONObject();
        try {
            input.put("id", listCategories.get(categoryPosition).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return input;
    }

    @NonNull
    private InputForAPI getInputForListSubAPI(Context context, int categoryPosition) {
        JSONObject input = getJsonObjectListSub(categoryPosition);

        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.LIST_SUB_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(input);
        inputForAPI.setHeaderStatus(true);
        return inputForAPI;
    }

    private void requestListSubApi(int i, Spinner subCategorySpinner, Context context) {
        InputForAPI inputForAPI = getInputForListSubAPI(context, i);
        commonViewModel.listSubCategory(inputForAPI).observe(this,
                new Observer<SubCategoryApiModel>() {
                    @Override
                    public void onChanged(@Nullable SubCategoryApiModel subCategoryApiModel) {
                        if (subCategoryApiModel != null) {
                            if (!subCategoryApiModel.getError()) {
                                responseListSub(subCategoryApiModel, subCategorySpinner);
                            } else {
                                UiUtils.snackBar(rootView, subCategoryApiModel.getErrorMessage());
                            }
                        }
                    }
                });
    }

    private void responseListSub(SubCategoryApiModel subCategoryApiModel, Spinner subCategorySpinner) {
        listSubcategories = subCategoryApiModel.getListSubcategory();
        subcategoryName = new String[listSubcategories.size()];
        for (int in = 0; in < listSubcategories.size(); in++) {
            subcategoryName[in] = listSubcategories.get(in).getSubCategoryName();
        }
        ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, subcategoryName);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategorySpinner.setAdapter(subCategoryAdapter);

    }

    public void setDefaultTimeslots(TextView textView) {
        textView.setText(getResources().getString(R.string.add_time));
        textView.setTextSize(14);
        textView.setTextColor(UiUtils.getPrimaryColor(RegisterActivity.this));
    }

    public void setSelectedTimeslots(TextView textView, String text) {
        textView.setText(text);
        textView.setTextSize(11);
        textView.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.grey));
    }

    private void showTimeSlotDialog() {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_time_slot);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();

        final RecyclerView timeSlot = dialog.findViewById(R.id.timeSlot);
        TextView okayText = dialog.findViewById(R.id.okayText);
        okayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text;
                text = commonViewModel.getSelectedTimeSlots(timeSlotApiList);
                if (selectedTime == 0) {
                    setDateText(text, mondayView);
                } else if (selectedTime == 1) {
                    setDateText(text, tuesdayView);
                } else if (selectedTime == 2) {
                    setDateText(text, wednesdayView);
                } else if (selectedTime == 3) {
                    setDateText(text, thursdayView);
                } else if (selectedTime == 4) {
                    setDateText(text, fridayView);
                } else if (selectedTime == 5) {
                    setDateText(text, saturdayView);
                } else if (selectedTime == 6) {
                    setDateText(text, sundayView);
                }

                dialog.dismiss();
            }
        });

        for (int i = 0; i < timeSlotApiList.size(); i++) {
            timeSlotApiList.get(i).setSelected("false");
        }

        appSettings.setPageNumber("6");

        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(RegisterActivity.this, timeSlotApiList,
                savedTimeSlotList, selectedTime);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(RegisterActivity.this, 4, GridLayoutManager.VERTICAL, false);
        timeSlot.setLayoutManager(linearLayoutManager);
        timeSlot.setAdapter(timeSlotAdapter);

    }

    private void setDateText(String text, TextView textView) {
        if (text.length() == 0) {
            setDefaultTimeslots(textView);
        } else {
            setSelectedTimeslots(textView, text);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private class GenericTextWatcher implements TextWatcher {
        private String editText;

        private GenericTextWatcher(String view) {
            this.editText = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            try {
                if (!isFirstTime) {
                    if (editText.equalsIgnoreCase("1")) {
                        try {
                            setGeneralValues();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (editText.equalsIgnoreCase("2")) {
                        setAddressValues();
                    } else if (editText.equalsIgnoreCase("3")) {
                        setRegisterValue();
                    } else if (editText.equalsIgnoreCase("4")) {
                        setProfileValue();
                    }
                    appSettings.setRegisterArray(registerDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        BaseUtils.log(TAG, "onConnected: ");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        BaseUtils.log(TAG, "onConnectionSuspended: ");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        BaseUtils.log(TAG, "onConnectionFailed: ");

    }

    @Override
    public void onLocationChanged(Location location) {
        BaseUtils.log(TAG, "onLocationChanged: ");
        if (!value) {
            BaseUtils.log(TAG, "onLocationChanged: LATLNG");
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
            value = true;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        BaseUtils.log(TAG, "onMapReady: ");
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                View locationButton = ((View) customDialog.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 10);
            } else {
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            View locationButton = ((View) customDialog.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 10);
        }


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // LatLng latLng=mMap.getCameraPosition().target;
                double latitude = mMap.getCameraPosition().target.latitude;
                double longititude = mMap.getCameraPosition().target.longitude;
                String lat = String.valueOf(latitude);
                String lngg = String.valueOf(longititude);

                //getAddressFromLocation(lat, lngg);
                getAddressFromLocation(latitude, longititude);

            }
        });

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
        Log.d("sdsddsvsdv", myCalendar.getTime().toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        BaseUtils.log(TAG, "onStop: ");
        appSettings.setSchedules(new ArrayList<>());

        if (customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }
}