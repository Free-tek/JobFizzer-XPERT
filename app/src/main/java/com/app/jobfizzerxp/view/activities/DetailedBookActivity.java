package com.app.jobfizzerxp.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.CancelJobApiModel;
import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.elapsedTime.Data;
import com.app.jobfizzerxp.model.elapsedTime.ElapsedTimeApiModel;
import com.app.jobfizzerxp.model.googleMaps.GoogleMapApiModel;
import com.app.jobfizzerxp.model.googleMaps.Result;
import com.app.jobfizzerxp.model.homeDashboardApi.Accepted;
import com.app.jobfizzerxp.model.homeDashboardApi.Alltax;
import com.app.jobfizzerxp.model.homeDashboardApi.Invoicedetail;
import com.app.jobfizzerxp.model.homeDashboardApi.MaterialDetails;
import com.app.jobfizzerxp.model.jobStatusApi.CompletedJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartToPlaceApiModel;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.interfaces.ImageUpload;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.adapters.PaymentMiscellaneousAdapter;
import com.app.jobfizzerxp.view.adapters.PaymentTaxAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.viewModel.DetailedBookingActViewModel;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.LOCATION_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.CAMERA_INTENT;


public class DetailedBookActivity extends BaseActivity {
    private Accepted bookingValues;
    private ImageView backButton;
    private LinearLayout billLayout;
    private TextView billingName, bookingId, bokkingDate,
            bookingAddress, pricePerhour, serviceName;
    private ImageView mapData, clock;
    private TextView statusText, workProgress;
    private RelativeLayout topBillingLayout;

    private View viewOne, viewTwo, viewThree, viewFour, viewFive, viewSix,
            viewSeven, viewEight, viewNine, viewTen;
    private LinearLayout layOne, layTwo, layThree, layFour, layFive, laySix;
    private TextView textOne, textTwo, textThree, textFour, textFive, textSix;
    private String statusOne = "", statusTwo = "", statusThree = "", statusFour = "",
            statusFive = "", statusSix = "";

    private TextView totalCostandTime, hoursValue;
    private TextView bookingGst, taxName, taxPercentage;
    private String TAG = DetailedBookActivity.class.getSimpleName();

    private Button startPlace;
    private AppSettings appSettings;
    private File uploadFile;
    private String bookingId_st = "";
    private String bookingStatus_st = "";

    private ImageView imageView;
    private Dialog dialog;
    private AppCompatButton yesButton, noButton, okButton;
    private TextView dialogHeading;
    private CommonViewModel commonViewModel;
    private DetailedBookingActViewModel detailedBookingActViewModel;
    private RelativeLayout rootView;
    private ImageView receipt_btn;
    private SpeedDialView speedDialView;
    private long cal;
    private ImageView firstCircle, secondCircle, thirdCircle, fourthCircle, fivthCircle, sixCircle;

    private MiscellaneousActivity miscellaneousDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_book_activitys);
        initViews();
        initListeners();
        try {
            setValues();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getDifferenceFromAPI() {
        JSONObject beforeObject = new JSONObject();
        try {
            beforeObject.put("booking_id", bookingValues.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(DetailedBookActivity.this);
        inputForAPI.setJsonObject(beforeObject);
        inputForAPI.setUrl(UrlHelper.ELAPSED_TIME);
        inputForAPI.setHeaderStatus(false);
        inputForAPI.setShowLoader(true);

        detailedBookingActViewModel.elapsedTime(inputForAPI).observe(this, new Observer<ElapsedTimeApiModel>() {
            @Override
            public void onChanged(@Nullable ElapsedTimeApiModel elapsedTimeApiModel) {
                if (elapsedTimeApiModel != null) {
                    if (!elapsedTimeApiModel.getError()) {
                        if (elapsedTimeApiModel.getData() != null) {
                            handleElapsedResponse(elapsedTimeApiModel);
                        }
                    } else {
                        UiUtils.snackBar(rootView, elapsedTimeApiModel.getError_message());
                    }
                }
            }
        });
    }

    private void handleElapsedResponse(@NonNull ElapsedTimeApiModel cancelJobApiModel) {
        Data data = cancelJobApiModel.getData();
        if (data != null) {

            long str_OverallDays = data.getDays();
            long str_Day = data.getD();
            long str_Hour = data.getH();
            long str_Minutes = data.getI();
            long str_Seconds = data.getS();

            long calculateTotalSeconds = str_Seconds
                    + TimeUnit.MINUTES.toSeconds(str_Minutes)
                    + TimeUnit.HOURS.toSeconds(str_Hour)
                    + TimeUnit.DAYS.toSeconds(str_OverallDays);

            setElapsedTime(calculateTotalSeconds);
//          + TimeUnit.DAYS.toSeconds(str_Day)
        }
    }

    private void setElapsedTime(long calculateTotalSeconds) {
        cal = calculateTotalSeconds;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                workProgress.setText(commonViewModel.getElapsedTime(cal));
                clock.setVisibility(View.VISIBLE);
                cal++;
                Log.d("caculateSecond", String.valueOf(cal));
                handler.postDelayed(this, 1000);
            }
        });
    }


    private void addFabCancel() {
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_cancel, R.drawable.ic_error)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setFabImageTintColor(UiUtils.getPrimaryColor(DetailedBookActivity.this))
                        .setLabel(getString(R.string.cancel))
                        .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.text_colour, getTheme()))
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabelClickable(true)
                        .create());
    }

    private void addFabTrack() {
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_track, R.drawable.track_marker)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setFabImageTintColor(UiUtils.getPrimaryColor(DetailedBookActivity.this))
                        .setLabel(getString(R.string.track))
                        .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.text_colour, getTheme()))
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabelClickable(true)
                        .create());
    }

    private void addFabChat() {
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_chat, R.drawable.chat)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setFabImageTintColor(UiUtils.getPrimaryColor(DetailedBookActivity.this))
                        .setLabel(getString(R.string.chat))
                        .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.text_colour, getTheme()))
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabelClickable(true)
                        .create());
    }

    private void removeFabChat() {
        speedDialView.removeActionItemById(R.id.fab_chat);
    }

    private void removeFabCancel() {
        speedDialView.removeActionItemById(R.id.fab_cancel);
    }

    private void removeFabTrack() {
        speedDialView.removeActionItemById(R.id.fab_track);
    }

    private void removeFabIcon() {
        speedDialView.setVisibility(View.GONE);
    }


    private void initListeners() {

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_cancel:
                        showCancelDialog("" + bookingValues.getId().toString());
                        speedDialView.close();
                        return true;
                    case R.id.fab_track:
                        startNavigate();
                        speedDialView.close();
                        return true;
                    case R.id.fab_chat:
                        moveToChat();
                        speedDialView.close();
                        return true;
                }
                return false;
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        receipt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewReceipt();
            }
        });


        startPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startPlace.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_to_place))) {
                    updateStatus(bookingValues.getId().toString());

                } else if (startPlace.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_job))) {
                    bookingId_st = bookingValues.getId().toString();
                    bookingStatus_st = Constants.BOOKINGS.STATUS_START;
                    showAlertDialog(DetailedBookActivity.this, bookingId_st, bookingStatus_st);

                } else if (startPlace.getText().toString().equalsIgnoreCase(getResources().getString(R.string.completed))) {
                    bookingId_st = bookingValues.getId().toString();
                    bookingStatus_st = Constants.BOOKINGS.STATUS_COMPLETE;

                    Intent intent = new Intent(DetailedBookActivity.this, MiscellaneousActivity.class);
                    intent.putExtra(Constants.INTENT_KEYS.BOOKING_ID, bookingId_st);
                    intent.putExtra(Constants.INTENT_KEYS.BOOKING_STATUS, bookingStatus_st);
                    startActivity(intent);
                }
            }
        });
    }

    private void startNavigate() {
        String src = bookingValues.getUserLatitude().toString();
        String des = bookingValues.getUserLongitude().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + src + "," + des));
        startActivity(intent);
    }

    private void moveToChat() {
        Intent intent = new Intent(DetailedBookActivity.this, ChatActivity.class);
        intent.putExtra(Constants.INTENT_KEYS.USER_IMAGE, bookingValues.getUserimage());
        intent.putExtra(Constants.INTENT_KEYS.USER_NAME, bookingValues.getName());
        intent.putExtra(Constants.INTENT_KEYS.USER_ID, "" + bookingValues.getUserId());
        intent.putExtra(Constants.INTENT_KEYS.BOOKING_ID, "" + bookingValues.getId());
        BaseUtils.log(TAG, "onClick: " + bookingValues.getId());
        startActivity(intent);
    }

    private void setRecyclerViewTax(final RecyclerView recyclerView, List<Alltax> alltaxList) {
        PaymentTaxAdapter paymentAdapter = new PaymentTaxAdapter(DetailedBookActivity.this,
                alltaxList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailedBookActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewMiscellanous(final RecyclerView recyclerView, List<MaterialDetails> materialDetails) {
        PaymentMiscellaneousAdapter paymentMiscellaneousAdapter = new PaymentMiscellaneousAdapter(DetailedBookActivity.this,
                materialDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailedBookActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paymentMiscellaneousAdapter);
        paymentMiscellaneousAdapter.notifyDataSetChanged();
    }

    private void showViewReceipt() {
        final Dialog dialog = UiUtils.getCustomDialog(DetailedBookActivity.this, R.layout.dialog_receipt);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView txt_close, providerName, billingName, bookingId, bookingDate,
                bookingTime, workedHours, bookingPrice, bookingTotal;
        TextView discount;
        RecyclerView recy_Tax, recy_miscellanous;
        CardView miscellaneous_card, tax;

        recy_Tax = dialog.findViewById(R.id.recy_Tax);
        recy_miscellanous = dialog.findViewById(R.id.recy_miscellanous);
        tax = dialog.findViewById(R.id.tax);
        miscellaneous_card = dialog.findViewById(R.id.miscellaneous_card);
        discount = dialog.findViewById(R.id.discount);
        txt_close = dialog.findViewById(R.id.id_close);
        providerName = dialog.findViewById(R.id.providerName);
        billingName = dialog.findViewById(R.id.billingName);
        bookingId = dialog.findViewById(R.id.bookingId);
        bookingDate = dialog.findViewById(R.id.bookingDate);
        workedHours = dialog.findViewById(R.id.workedHours);
        bookingTime = dialog.findViewById(R.id.bookingTime);
        bookingPrice = dialog.findViewById(R.id.bookingPrice);
        bookingTotal = dialog.findViewById(R.id.bookingTotal);

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        List<Invoicedetail> invoiceDetailList = bookingValues.getInvoicedetails();
        if (invoiceDetailList != null) {
            Invoicedetail invoicedetail = invoiceDetailList.get(0);

            if (invoicedetail != null) {

                providerName.setText(invoicedetail.getProvidername());
                billingName.setText(invoicedetail.getUsername());
                bookingId.setText(invoicedetail.getBookingOrderId());
                bookingDate.setText(invoicedetail.getBookingDate());
                bookingTime.setText(invoicedetail.getTiming());


                if (invoicedetail.getOff().equalsIgnoreCase("")) {
                    discount.setText(ConversionUtils.getCombinedStrings(getString(R.string.minus_symbol), " ", getString(R.string.currency_symbol), getString(R.string.no_offers)));
                } else {
                    discount.setText(ConversionUtils.getCombinedStrings(getString(R.string.minus_symbol), " ", getString(R.string.currency_symbol), invoicedetail.getOff()));
                }


                bookingPrice.setText(ConversionUtils.appendCurrencySymbol(DetailedBookActivity.this,
                        invoicedetail.getCost()));
                bookingTotal.setText(ConversionUtils.appendCurrencySymbol(DetailedBookActivity.this,
                        invoicedetail.getTotalCost()));
                bookingDate.setText(invoicedetail.getBookingDate());

                if (invoicedetail.getWorkedMins() != null) {
                    String hours = ConversionUtils
                            .formatHoursAndMinutes(Integer.parseInt(invoicedetail.getWorkedMins()));
                    workedHours.setText(hours);
                }

                setRecyclerViewTax(recy_Tax, invoicedetail.getAlltax());
                setRecyclerViewMiscellanous(recy_miscellanous, invoicedetail.getMaterial_details());
                if (invoicedetail.getMaterial_details().size() == 0) {
                    miscellaneous_card.setVisibility(View.GONE);
                } else {
                    miscellaneous_card.setVisibility(View.VISIBLE);
                }
                if (invoicedetail.getAlltax().size() == 0) {
                    tax.setVisibility(View.GONE);
                } else {
                    tax.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void showAlertDialog(Activity context, String bookingId, String bookingStatus) {
        dialog = UiUtils.getPopupDialog(context, R.layout.dialog_image);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialogHeading = dialog.findViewById(R.id.dialogHeading);
        yesButton = dialog.findViewById(R.id.yesButton);
        okButton = dialog.findViewById(R.id.okButton);
        noButton = dialog.findViewById(R.id.noButton);
        imageView = dialog.findViewById(R.id.imageView);

        dialogHeading.setText(R.string.take_picture);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkApi(DetailedBookActivity.this);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkApi(DetailedBookActivity.this);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                updateStatus(context, bookingId, bookingStatus);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                updateStatus(context, bookingId, bookingStatus);
            }
        });
    }


    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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
                    UiUtils.openCamera(DetailedBookActivity.this, Constants.CAMERA.FILE_NAME_JOB);
                } else {
                    UiUtils.snackBar(rootView, getString(R.string.camera_permission_error));

                }
                break;

            case LOCATION_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    updateStatus(DetailedBookActivity.this, bookingId_st, bookingStatus_st);
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

    private void checkApi(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Constants.PERMISSIONS.cameraPermList,
                    Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS);
        } else {
            UiUtils.openCamera(activity, Constants.CAMERA.FILE_NAME_JOB);
        }
    }

    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        GlideHelper.setImage(url, imageView, drawable);
    }


    private void changeAlertDialog() {
        loadImage(appSettings.getImageUploadPath(), imageView, UiUtils.getProfilePicture(imageView.getContext()));
        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);
        okButton.setVisibility(View.VISIBLE);

        dialogHeading.setText(R.string.send_photo);
    }

    private void showCancelDialog(final String id) {
        dialog = UiUtils.getPopupDialog(DetailedBookActivity.this, R.layout.dialog_job_cancel);
        dialog.show();

        TextView yesButton, noButton;
        yesButton = dialog.findViewById(R.id.yesButton);
        noButton = dialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                cancelRequest(id);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
    }

    private void cancelRequest(String id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(this);
        inputForAPI.setUrl(UrlHelper.CANCEL_BOOKING);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setShowLoader(true);
        getValue(inputForAPI);


    }

    private void getValue(InputForAPI inputForAPI) {

        detailedBookingActViewModel.cancelJobApi(inputForAPI).observe(this, new Observer<CancelJobApiModel>() {
            @Override
            public void onChanged(@Nullable CancelJobApiModel response) {

                if (response != null) {
                    if (!response.getError()) {
                        finish();
                    } else {
                        UiUtils.snackBar(rootView, response.getErrorMessage());
                    }
                }
            }
        });
    }


    private void updateStatus(String id) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(this);
        inputForAPI.setUrl(UrlHelper.START_TO_PLACE);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        getStartToPlace(inputForAPI);

    }

    private void getStartToPlace(InputForAPI inputForAPI) {
        commonViewModel.startToPlace(inputForAPI).observe((LifecycleOwner) inputForAPI.getContext(),
                new Observer<StartToPlaceApiModel>() {
                    @Override
                    public void onChanged(@Nullable StartToPlaceApiModel response) {
                        if (response != null) {
                            if (!response.getError()) {
                                finish();
                            } else {
                                UiUtils.snackBar(rootView, response.getErrorMessage());
                            }
                        }
                    }
                });
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

    private String buildUrl(double latitude, double longitude) {
        return commonViewModel.getGeoCodeMapUrl(latitude, longitude);
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
                if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_START)) {
                    try {
                        mainJsonObject.put("start_time", System.currentTimeMillis());
                        mainJsonObject.put("start_lat", appSettings.getLatitude());
                        mainJsonObject.put("start_long", appSettings.getLongitude());
                        mainJsonObject.put("start_address", address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_COMPLETE)) {
                    try {
                        mainJsonObject.put("end_time", System.currentTimeMillis());
                        mainJsonObject.put("end_lat", appSettings.getLatitude());
                        mainJsonObject.put("end_long", appSettings.getLongitude());
                        mainJsonObject.put("end_address", address);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                imageRequest(context, uploadFile, mainJsonObject, status);
            }
        }
    }

    private void initViews() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        detailedBookingActViewModel = ViewModelProviders.of(this).get(DetailedBookingActViewModel.class);
        appSettings = new AppSettings(DetailedBookActivity.this);
        backButton = findViewById(R.id.backButton);
        rootView = findViewById(R.id.activity_detailed_booking);
        billingName = findViewById(R.id.billingName);
        bookingId = findViewById(R.id.bookingId);
        bokkingDate = findViewById(R.id.bokkingDate);
        hoursValue = findViewById(R.id.hoursValue);
        bookingAddress = findViewById(R.id.bookingAddress);
        pricePerhour = findViewById(R.id.pricePerhour);
        serviceName = findViewById(R.id.serviceName);
        totalCostandTime = findViewById(R.id.totalCostandTime);
        billLayout = findViewById(R.id.billLayout);
        bookingGst = findViewById(R.id.bookingGst);
        taxName = findViewById(R.id.taxName);
        taxPercentage = findViewById(R.id.taxPercentage);
        mapData = findViewById(R.id.mapData);
        statusText = findViewById(R.id.statusText);
        workProgress = findViewById(R.id.working_progress);
        clock = findViewById(R.id.clock);

        startPlace = findViewById(R.id.startPlace);
        textOne = findViewById(R.id.textOne);
        textTwo = findViewById(R.id.textTwo);
        textThree = findViewById(R.id.textThree);
        textFour = findViewById(R.id.textFour);
        textFive = findViewById(R.id.textFive);
        textSix = findViewById(R.id.textSix);

        layOne = findViewById(R.id.layOne);
        layTwo = findViewById(R.id.layTwo);
        layThree = findViewById(R.id.layThree);
        layFour = findViewById(R.id.layFour);
        layFive = findViewById(R.id.layFive);
        laySix = findViewById(R.id.laySix);

        viewOne = findViewById(R.id.viewOne);
        viewTwo = findViewById(R.id.viewTwo);
        viewThree = findViewById(R.id.viewThree);
        viewFour = findViewById(R.id.viewFour);
        viewFive = findViewById(R.id.viewFive);
        viewSix = findViewById(R.id.viewSix);
        viewSeven = findViewById(R.id.viewSeven);
        viewEight = findViewById(R.id.viewEight);
        viewNine = findViewById(R.id.viewNine);
        viewTen = findViewById(R.id.viewTen);

        firstCircle = findViewById(R.id.firstCircle);
        secondCircle = findViewById(R.id.secondCircle);
        thirdCircle = findViewById(R.id.thirdCircle);
        fourthCircle = findViewById(R.id.fourthCircle);
        fivthCircle = findViewById(R.id.fivthCircle);
        sixCircle = findViewById(R.id.sixCircle);

        topBillingLayout = findViewById(R.id.topBillingLayout);
        receipt_btn = findViewById(R.id.id_imgbtn_receipt);
        speedDialView = findViewById(R.id.speedDial);

        addFabChat();
        addFabTrack();
        addFabCancel();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setValues() throws ParseException {
        Intent intent = getIntent();
        if (intent != null) {
            bookingValues = (Accepted) intent.getSerializableExtra("bookingValues");

            BaseUtils.log(TAG, ":bookingValues " + bookingValues);
            String status = bookingValues.getStatus();
            BaseUtils.log("statusDetailed", status);
            String showBill = bookingValues.getShowBillFlag();
            if (showBill.equalsIgnoreCase("0")) {
                billLayout.setVisibility(View.GONE);
                totalCostandTime.setVisibility(View.GONE);
                topBillingLayout.setVisibility(View.GONE);
                billLayout.setVisibility(View.GONE);
                bookingGst.setVisibility(View.GONE);
                taxName.setVisibility(View.GONE);
                taxPercentage.setVisibility(View.GONE);
            } else {
                topBillingLayout.setVisibility(View.VISIBLE);

                billLayout.setVisibility(View.VISIBLE);
                totalCostandTime.setVisibility(View.VISIBLE);

                int total = 0;
                if (bookingValues.getWorkedMins() != null) {
                    total = Integer.parseInt(bookingValues.getWorkedMins());
                }

                String hours = ConversionUtils.formatHoursAndMinutes(total);
                totalCostandTime.setText(ConversionUtils.appendCurrencySymbol(DetailedBookActivity.this,
                        bookingValues.getTotalCost()));
                hoursValue.setText(hours);

                bookingGst.setText(ConversionUtils.appendCurrencySymbol(DetailedBookActivity.this, bookingValues.getGstCost()));
                taxName.setText(ConversionUtils.capWords(bookingValues.getTaxName()));
                taxPercentage.setText(ConversionUtils.appendPercentage(DetailedBookActivity.this, bookingValues.getGstPercent()));
                pricePerhour.setText(ConversionUtils.appendCurrencySymbol(DetailedBookActivity.this, bookingValues.getCost()));

            }

            billingName.setText(bookingValues.getName());
            bookingId.setText(bookingValues.getBookingOrderId());
            bokkingDate.setText(ConversionUtils.getCombinedStrings(bookingValues.getBookingDate(), " ", bookingValues.getTiming()));
            bookingAddress.setText(bookingValues.getAddressLine1());
            serviceName.setText(bookingValues.getSubCategoryName());

            statusText.setText(status.toUpperCase());
            workProgress.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);

            if (bookingValues.getPendingTime() == null) {
                statusOne = getResources().getString(R.string.service_booked);

            } else {
                statusOne = getConvertedStatus(R.string.service_booked, bookingValues.getPendingTime());
            }
            if (bookingValues.getAcceptedTime() == null) {
                statusTwo = getResources().getString(R.string.service_accepted);


            } else {
                statusTwo = getConvertedStatus(R.string.service_accepted, bookingValues.getAcceptedTime());
            }
            if (bookingValues.getStarttoCustomerPlaceTime() == null) {
                statusThree = getResources().getString(R.string.started_to_customer_place);

            } else {
                statusThree = getConvertedStatus(R.string.started_to_customer_place, bookingValues.getStarttoCustomerPlaceTime());
            }

            if (bookingValues.getStartjobTimestamp() == null) {
                statusFour = getResources().getString(R.string.provider_arrived);
                statusFive = getResources().getString(R.string.job_started);


            } else {
                statusFour = getConvertedStatus(R.string.provider_arrived, bookingValues.getStartjobTimestamp());

                statusFive = getConvertedStatus(R.string.job_started, bookingValues.getStartjobTimestamp());
            }

            if (bookingValues.getEndjobTimestamp() == null) {
                statusSix = getResources().getString(R.string.job_completed);

            } else {
                statusSix = getConvertedStatus(R.string.job_completed, bookingValues.getEndjobTimestamp());
            }
            int colorvalue = 0;


            if (status.equalsIgnoreCase("Pending")) {
                colorvalue = getResources().getColor(R.color.light_violet);
                statusText.setText(getResources().getString(R.string.cancel));
                workProgress.setVisibility(View.GONE);
                clock.setVisibility(View.GONE);
                startPlace.setVisibility(View.VISIBLE);
                removeFabIcon();
                setBooked();

            } else if (status.equalsIgnoreCase("Accepted")) {
                colorvalue = getResources().getColor(R.color.light_violet);
                statusText.setText(getResources().getString(R.string.accepted));
                workProgress.setVisibility(View.GONE);
                clock.setVisibility(View.GONE);
                startPlace.setVisibility(View.VISIBLE);
                startPlace.setText(getResources().getString(R.string.start_to_place));

                addFabChat();
                addFabCancel();
                addFabTrack();
                setAccepted();

            } else if (status.equalsIgnoreCase("Rejected")) {
                statusTwo = getConvertedStatus(R.string.service_rejected, bookingValues.getRejectedTime());
                colorvalue = getResources().getColor(R.color.red);
                statusText.setText(getResources().getString(R.string.rejected));
                workProgress.setVisibility(View.GONE);
                clock.setVisibility(View.GONE);
                startPlace.setVisibility(View.GONE);
                removeFabIcon();
                setAccepted();

            } else if (status.equalsIgnoreCase("CancelledbyUser")) {
                statusThree = getConvertedStatus(R.string.cancelled_by_user, bookingValues.getCancelledbyUserTime());
                colorvalue = getResources().getColor(R.color.red);
                statusText.setText(getResources().getString(R.string.cancelled));
                workProgress.setVisibility(View.GONE);
                clock.setVisibility(View.GONE);
                startPlace.setVisibility(View.GONE);
                removeFabIcon();
                setStartToPlace();

            } else if (status.equalsIgnoreCase("CancelledbyProvider")) {
                statusThree = getConvertedStatus(R.string.cancelled_by_provider, bookingValues.getCancelledbyProviderTime());
                colorvalue = getResources().getColor(R.color.red);
                statusText.setText(getResources().getString(R.string.cancelled));
                workProgress.setVisibility(View.GONE);
                clock.setVisibility(View.GONE);
                startPlace.setVisibility(View.GONE);
                removeFabIcon();
                setStartToPlace();

            } else if (status.equalsIgnoreCase("StarttoCustomerPlace")) {
                colorvalue = getResources().getColor(R.color.light_violet);
                statusText.setText(getResources().getString(R.string.on_the_way));

                startPlace.setVisibility(View.VISIBLE);
                startPlace.setText(getResources().getString(R.string.start_job));

                addFabChat();
                addFabTrack();
                removeFabCancel();
                setStartToPlace();

            } else if (status.equalsIgnoreCase("Startedjob")) {
                colorvalue = getResources().getColor(R.color.light_violet);

                getDifferenceFromAPI();
                statusText.setText(getResources().getString(R.string.work_in_progress));
                clock.setVisibility(View.VISIBLE);
                workProgress.setVisibility(View.VISIBLE);
                startPlace.setVisibility(View.VISIBLE);
                startPlace.setText(getResources().getString(R.string.completed));

                addFabChat();
                removeFabCancel();
                removeFabTrack();
                setJobStarted();

            } else if (status.equalsIgnoreCase("Completedjob")) {
                colorvalue = getResources().getColor(R.color.green);
                statusText.setText(getResources().getString(R.string.completed));

                startPlace.setVisibility(View.GONE);

                removeFabIcon();
                setJobCompleted();

            } else if (status.equalsIgnoreCase("Waitingforpaymentconfirmation")) {
                colorvalue = getResources().getColor(R.color.status_orange);

                startPlace.setVisibility(View.GONE);
                removeFabIcon();
                setJobCompleted();
            } else if (status.equalsIgnoreCase("Reviewpending")) {
                colorvalue = getResources().getColor(R.color.ratingColor);

                startPlace.setVisibility(View.GONE);
                removeFabIcon();
                setJobCompleted();
            } else if (status.equalsIgnoreCase("Finished")) {
                colorvalue = getResources().getColor(R.color.green);
                statusText.setText(getResources().getString(R.string.completed));

                startPlace.setVisibility(View.GONE);
                removeFabIcon();
                setJobCompleted();

            }

            statusText.setTextColor(colorvalue);

            String src = bookingValues.getUserLatitude().toString();
            String des = bookingValues.getUserLongitude().toString();
            getStaticMap(src, des);
            BaseUtils.log(TAG, "setValues: " + statusOne);
        }
    }

    @NonNull
    private String getConvertedStatus(int service_booked, String pendingTime) throws ParseException {
        return String.valueOf(ConversionUtils.getCombinedStrings(getString(service_booked), "\n", ConversionUtils.convertedTime(pendingTime)));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setBooked() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.GONE);
        layThree.setVisibility(View.GONE);
        layFour.setVisibility(View.GONE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);


        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.INVISIBLE);
        viewTwo.setVisibility(View.INVISIBLE);
        viewThree.setVisibility(View.INVISIBLE);
        viewFour.setVisibility(View.INVISIBLE);
        viewFive.setVisibility(View.INVISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);

        firstCircle.setImageDrawable(getDrawable(R.drawable.circle_violeet_filled));


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setAccepted() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.GONE);
        layFour.setVisibility(View.GONE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);

        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.INVISIBLE);
        viewFour.setVisibility(View.INVISIBLE);
        viewFive.setVisibility(View.INVISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);

        secondCircle.setImageDrawable(getDrawable(R.drawable.circle_violeet_filled));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStartToPlace() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.GONE);
        layFive.setVisibility(View.GONE);
        laySix.setVisibility(View.GONE);

        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.INVISIBLE);
        viewSix.setVisibility(View.INVISIBLE);
        viewSeven.setVisibility(View.INVISIBLE);
        viewEight.setVisibility(View.INVISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);

        thirdCircle.setImageDrawable(getDrawable(R.drawable.circle_violeet_filled));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setJobStarted() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.VISIBLE);
        layFive.setVisibility(View.VISIBLE);
        laySix.setVisibility(View.GONE);

        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.VISIBLE);
        viewSix.setVisibility(View.VISIBLE);
        viewSeven.setVisibility(View.VISIBLE);
        viewEight.setVisibility(View.VISIBLE);
        viewNine.setVisibility(View.INVISIBLE);
        viewTen.setVisibility(View.INVISIBLE);

        fivthCircle.setImageDrawable(getDrawable(R.drawable.circle_violeet_filled));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setJobCompleted() {
        layOne.setVisibility(View.VISIBLE);
        layTwo.setVisibility(View.VISIBLE);
        layThree.setVisibility(View.VISIBLE);
        layFour.setVisibility(View.VISIBLE);
        layFive.setVisibility(View.VISIBLE);
        laySix.setVisibility(View.VISIBLE);

        textOne.setText(statusOne);
        textTwo.setText(statusTwo);
        textThree.setText(statusThree);
        textFour.setText(statusFour);
        textFive.setText(statusFive);
        textSix.setText(statusSix);

        viewOne.setVisibility(View.VISIBLE);
        viewTwo.setVisibility(View.VISIBLE);
        viewThree.setVisibility(View.VISIBLE);
        viewFour.setVisibility(View.VISIBLE);
        viewFive.setVisibility(View.VISIBLE);
        viewSix.setVisibility(View.VISIBLE);
        viewSeven.setVisibility(View.VISIBLE);
        viewEight.setVisibility(View.VISIBLE);
        viewNine.setVisibility(View.VISIBLE);
        viewTen.setVisibility(View.VISIBLE);

        sixCircle.setImageDrawable(getDrawable(R.drawable.circle_violeet_filled));
    }

    private void getStaticMap(String lat, String longg) {
        String urls = commonViewModel.getStaticMapUrl(lat, longg);
        BaseUtils.log(TAG, "url: " + urls);
        loadImage(urls, mapData, UiUtils.getMapRequest(DetailedBookActivity.this));

    }


    private void startJobApi(Context context, InputForAPI inputForAPI) {
        commonViewModel.startJob(inputForAPI).observe((LifecycleOwner) context, new Observer<StartJobApiModel>() {
            @Override
            public void onChanged(@Nullable StartJobApiModel response) {
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

    private void bookingStatusRequest(Activity activity, JSONObject mainJsonObject, String status, String imageUrl) throws JSONException {

        InputForAPI inputForAPI = new InputForAPI(activity);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_START)) {
            mainJsonObject.put("start_image", imageUrl);
            inputForAPI.setJsonObject(mainJsonObject);
            inputForAPI.setUrl(UrlHelper.START_JOB);
            BaseUtils.log(TAG, "imageUploadJson: " + mainJsonObject);
            startJobApi(activity, inputForAPI);
        }
//        else if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_COMPLETE)) {
//
////            Intent intent = new Intent(DetailedBookActivity.this, MiscellaneousActivity.class);
////            startActivity(intent);
//
//            mainJsonObject.put("end_image", imageUrl);
//            inputForAPI.setJsonObject(mainJsonObject);
//            inputForAPI.setUrl(UrlHelper.COMPLETED_JOB);
//            BaseUtils.log(TAG, "imageUploadJson: " + mainJsonObject);
//            completeJobApi(activity, inputForAPI);
//
//        }
    }

    private void imageRequest(Activity activity, File imageFile, JSONObject jsonObject, String status) {
        if (imageFile != null && imageFile.exists()) {
            new HitImageUpload(activity, imageFile, new ImageUpload() {
                @Override
                public void onFinish(String imageUrl) {
                    if (imageUrl == null) {
                        UiUtils.snackBar(rootView, activity.getString(R.string.image_failed));

                    } else {
                        BaseUtils.log(TAG, "imageUploadJson: " + jsonObject);
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
}