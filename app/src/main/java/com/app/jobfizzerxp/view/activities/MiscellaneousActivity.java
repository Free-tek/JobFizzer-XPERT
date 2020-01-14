package com.app.jobfizzerxp.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.MiscellaneousModel;
import com.app.jobfizzerxp.model.googleMaps.GoogleMapApiModel;
import com.app.jobfizzerxp.model.googleMaps.Result;
import com.app.jobfizzerxp.model.jobStatusApi.CompletedJobApiModel;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.interfaces.ImageUpload;
import com.app.jobfizzerxp.utilities.interfaces.MiscellaneousAdapterHandler;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.adapters.MiscellaneousAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.LOCATION_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.CAMERA_INTENT;


public class MiscellaneousActivity extends BaseActivity {

    private ImageView completeImage;
    private RecyclerView miscellaneousRecylerView;
    private TextView miscellaneousTextview, skip, name, price, takephoto;
    private Button submit;
    private MiscellaneousAdapter miscellaneousAdapter;

    private String bookingId_st, bookingStatus_st;

    List<MiscellaneousModel> itemIncrease = new ArrayList();

    private CommonViewModel commonViewModel;

    private File uploadFile;

    private String TAG = MiscellaneousActivity.class.getSimpleName();

    private ConstraintLayout rootView;
    private AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miscellaneous_activity);
        initView();
        getIntentValue();
        setClickListner();
        setAdapter();
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        try {
            if (intent != null) {
                bookingId_st = intent.getStringExtra(Constants.INTENT_KEYS.BOOKING_ID);
                bookingStatus_st = intent.getStringExtra(Constants.INTENT_KEYS.BOOKING_STATUS);
                BaseUtils.log(TAG, "booking_id: " + bookingId_st);
                BaseUtils.log(TAG, "booking_status: " + bookingStatus_st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setClickListner() {
        miscellaneousTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToggle(miscellaneousTextview.getText().toString());
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(MiscellaneousActivity.this, bookingId_st, bookingStatus_st);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(MiscellaneousActivity.this, bookingId_st, bookingStatus_st);
            }
        });

        completeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkApi(MiscellaneousActivity.this);
            }
        });
    }

    private void setAdapter() {
        miscellaneousAdapter = new
                MiscellaneousAdapter(MiscellaneousActivity.this, new MiscellaneousAdapterHandler() {
            @Override
            public void Count(int count) {
                if (count == 0) {
                    name.setVisibility(View.GONE);
                    price.setVisibility(View.GONE);
                    miscellaneousTextview.setText(R.string.miscellaneous);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(MiscellaneousActivity.this, LinearLayoutManager.VERTICAL, false);
        miscellaneousRecylerView.setLayoutManager(linearLayoutManager);
        miscellaneousRecylerView.setNestedScrollingEnabled(false);
        miscellaneousRecylerView.setAdapter(miscellaneousAdapter);
    }

    private void initView() {

        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);

        completeImage = findViewById(R.id.complete_job_img);
        miscellaneousRecylerView = findViewById(R.id.miscellaneous_recyclerview);

        appSettings = new AppSettings(MiscellaneousActivity.this);

        submit = findViewById(R.id.submit);

        skip = findViewById(R.id.skip);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        takephoto = findViewById(R.id.takephoto);
        miscellaneousTextview = findViewById(R.id.miscellaneous);
        rootView = findViewById(R.id.rootView);
    }

    private void textToggle(String textToggle) {
        if (textToggle.equalsIgnoreCase(MiscellaneousActivity.this.getResources()
                .getString(R.string.miscellaneous))) {
            name.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            miscellaneousTextview.setText(R.string.add_more);
        }

        miscellaneousAdapter.addValue();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BaseUtils.log(TAG, "onRequestPermissionsResult:" + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    UiUtils.openCamera(MiscellaneousActivity.this, Constants.CAMERA.FILE_NAME_JOB);
                } else {
                    UiUtils.snackBar(rootView, MiscellaneousActivity.this.getResources().getString(R.string.camera_permission_error));

                }
                break;
            case LOCATION_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    updateStatus(MiscellaneousActivity.this, bookingId_st, bookingStatus_st);
                } else {
                    UiUtils.snackBar(rootView, getResources().getString(R.string.location_permission_error));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseUtils.log(TAG, "onActivityResult: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_INTENT://camera
                if (resultCode == RESULT_OK) {
                    uploadFile = new File(String.valueOf(appSettings.getImageUploadPath()));
                    BaseUtils.log(TAG, "onActivityResult: " + appSettings.getImageUploadPath());

                    if (uploadFile.exists()) {
                        changeAlertDialog();
                    }

                }
                break;
        }
    }

    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        GlideHelper.setImage(url, imageView, drawable);
    }

    private void changeAlertDialog() {
        loadImage(appSettings.getImageUploadPath(), completeImage, UiUtils.getProfilePicture(completeImage.getContext()));
        takephoto.setText(R.string.send_photo);
    }

    private void checkApi(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Constants.PERMISSIONS.cameraPermList,
                    Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS);
        } else {
            UiUtils.openCamera(activity, Constants.CAMERA.FILE_NAME_JOB);
        }
    }

    private void updateStatus(Activity context, String id, final String status) {
        BaseUtils.log(TAG, "bookingId: " + id + ", bookingStatus:" + status);
        if (id != null && status != null) {
            final JSONObject mainJsonObject = new JSONObject();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS);

            } else {
                try {
                    mainJsonObject.put("id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getCompleteAddress(context, mainJsonObject, status);
            }
        }
    }

    private String buildUrl(double latitude, double longitude) {
        return commonViewModel.getGeoCodeMapUrl(latitude, longitude);
    }

    private void getCompleteAddress(Activity context, JSONObject mainJsonObject, String status) {
        BaseUtils.log(TAG, "lat: " + appSettings.getLatitude() + ", loc: " + appSettings.getLongitude());
        String url = buildUrl(Double.parseDouble(appSettings.getLatitude()), Double.parseDouble(appSettings.getLongitude()));

        InputForAPI inputForAPI = new InputForAPI(this);
        inputForAPI.setUrl(url);
        inputForAPI.setJsonObject(mainJsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(false);
        getAddressDetails(context, inputForAPI, mainJsonObject, status);
    }

    public void getAddressDetails(Activity context, InputForAPI inputForAPI, final JSONObject mainJsonObject,
                                  final String status) {

        commonViewModel.googleGeoCodeApi(inputForAPI).observe(this, new Observer<GoogleMapApiModel>() {
            @Override
            public void onChanged(@Nullable GoogleMapApiModel bookingadapterAddressModel) {
                if (bookingadapterAddressModel != null) {
                    handleAddressResponse(context, bookingadapterAddressModel, mainJsonObject, status);
                }
            }
        });
    }

    public void handleAddressResponse(Activity context, GoogleMapApiModel response,
                                      JSONObject mainJsonObject, String status) {
        Result resultAddress;

        if (response.getStatus().equalsIgnoreCase("OK")) {
            List<Result> listDetailBooking;
            listDetailBooking = response.getResults();
            resultAddress = listDetailBooking.get(0);
            String address = resultAddress.getFormattedAddress();

            if (address != null) {
                if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_COMPLETE)) {
                    try {
                        mainJsonObject.put("end_time", System.currentTimeMillis());
                        mainJsonObject.put("end_lat", appSettings.getLatitude());
                        mainJsonObject.put("end_long", appSettings.getLongitude());
                        mainJsonObject.put("end_address", address);

                        itemIncrease = miscellaneousAdapter.getItemList();

                        if (itemIncrease.size() != 0) {
                            try {

                                JSONArray fileUploadValue = new JSONArray();

                                for (int i = 0; i < itemIncrease.size(); i++) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("material_name", itemIncrease.get(i).getName());
                                    jsonObject.put("material_cost", itemIncrease.get(i).getPrice());
                                    fileUploadValue.put(jsonObject);
                                }
                                mainJsonObject.put("material_cost", fileUploadValue.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                imageRequest(context, uploadFile, mainJsonObject, status);

            }
        }
    }

    private void imageRequest(Activity activity, File imageFile, JSONObject jsonObject, String status) {
        if (imageFile != null && imageFile.exists()) {
            new HitImageUpload(activity, imageFile, new ImageUpload() {
                @Override
                public void onFinish(String imageUrl) {
                    if (imageUrl == null) {
                        UiUtils.snackBar(rootView, activity.getString(R.string.image_failed));

                    } else {
                        BaseUtils.log(TAG, "imageUploadJson: " + jsonObject.toString());
                        try {
                            bookingStatusRequest(activity, jsonObject, status, imageUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).execute();
        } else {
            BaseUtils.log(TAG, "imageUploadJson: " + jsonObject);
            try {
                bookingStatusRequest(activity, jsonObject, status, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class HitImageUpload extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        private File imageFile;
        private ImageUpload imageUpload;

        HitImageUpload(Activity activity, File uploadFile, ImageUpload imageUpload) {
            this.activity = activity;
            this.imageFile = uploadFile;
            this.imageUpload = imageUpload;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UiUtils.show(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imageUpload(activity, imageFile, imageUpload);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UiUtils.dismiss();
        }
    }

    private void bookingStatusRequest(Activity activity, JSONObject mainJsonObject, String status, String imageUrl) throws JSONException {

        InputForAPI inputForAPI = new InputForAPI(activity);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_COMPLETE)) {
            mainJsonObject.put("end_image", imageUrl);
            inputForAPI.setJsonObject(mainJsonObject);
            inputForAPI.setUrl(UrlHelper.COMPLETED_JOB);
            BaseUtils.log(TAG, "imageUploadJson: " + mainJsonObject);
            completeJobApi(activity, inputForAPI);
        }
    }

    private void completeJobApi(Context context, InputForAPI inputForAPI) {
        commonViewModel.completeJob(inputForAPI).observe((LifecycleOwner) context, new Observer<CompletedJobApiModel>() {
            @Override
            public void onChanged(@Nullable CompletedJobApiModel response) {
                UiUtils.dismiss();
                if (response != null) {
                    if (!response.getError()) {
                        startEndJobResponse();
                    } else {
                        UiUtils.snackBar(rootView, response.getErrorMessage());
                    }
                }
            }
        });
    }

    private void startEndJobResponse() {
        uploadFile = new File("");
        finish();
    }

    private void imageUpload(Context context, File imageFile, ImageUpload imageUpload) {

        File imageFiles = UiUtils.getCompressedFile(context, imageFile);
        final InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setFile(imageFiles);

        commonViewModel.uploadImageApi(inputForAPI).observe((LifecycleOwner) context,
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}