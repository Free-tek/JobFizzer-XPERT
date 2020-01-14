package com.app.jobfizzerxp.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.PaymentConfirmationApi;
import com.app.jobfizzerxp.model.ReviewModel;
import com.app.jobfizzerxp.model.beforeAfterImage.BeforeAfterImageModel;
import com.app.jobfizzerxp.model.beforeAfterImage.Data;
import com.app.jobfizzerxp.model.googleMaps.GoogleMapApiModel;
import com.app.jobfizzerxp.model.googleMaps.Result;
import com.app.jobfizzerxp.model.homeDashboardApi.Accepted;
import com.app.jobfizzerxp.model.homeDashboardApi.HomeDashBoardResponseModel;
import com.app.jobfizzerxp.model.homeDashboardApi.Pending;
import com.app.jobfizzerxp.model.homeDashboardApi.RandomRequestPending;
import com.app.jobfizzerxp.model.jobApi.JobAcceptApi;
import com.app.jobfizzerxp.model.jobApi.JobRejectApi;
import com.app.jobfizzerxp.model.jobApi.RandomRequestAcceptApi;
import com.app.jobfizzerxp.model.jobApi.RandomRequestRejectApi;
import com.app.jobfizzerxp.model.jobStatusApi.CompletedJobApiModel;
import com.app.jobfizzerxp.model.jobStatusApi.StartJobApiModel;
import com.app.jobfizzerxp.utilities.events.Status;
import com.app.jobfizzerxp.utilities.helpers.AnimationHelper;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.interfaces.ImageUpload;
import com.app.jobfizzerxp.utilities.interfaces.StartImageJob;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.activities.MainActivity;
import com.app.jobfizzerxp.view.adapters.BookingsAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.viewModel.HomeFragmentViewmodel;
import com.app.jobfizzerxp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.app.jobfizzerxp.utilities.helpers.Constants.BOOKINGS.NORMAL_BOOKING;
import static com.app.jobfizzerxp.utilities.helpers.Constants.BOOKINGS.RANDOM_BOOKING;

public class HomeFragment extends Fragment {
    private RecyclerView bookingsList;
    private String TAG = HomeFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private View empty_layout;
    private MainActivity activity;
    private Dialog dialog;
    private TextView after_tv;
    private TextView before_tv;
    private LinearLayout image_ln;
    private LinearLayout before_image_ln;
    private ImageView before_image;
    private LinearLayout after_image_ln;
    private ImageView after_image;

    private HomeFragmentViewmodel homeFragmentViewmodel;
    private CommonViewModel commonViewModel;
    private AppSettings appSettings;
    private FrameLayout rootView;
    private AppCompatButton yesButton, noButton, okButton;
    private ImageView imageView;
    private TextView dialogHeading;
    private File setUploadFile;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    public void changeAlertDialog() {
        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);
        okButton.setVisibility(View.VISIBLE);
        loadImage(appSettings.getImageUploadPath(), imageView,
                ContextCompat.getDrawable(imageView.getContext(), R.drawable.service_ph));

        dialogHeading.setText(R.string.send_photo);

    }


    public void getPendRequest(final Context context, boolean loader) {
        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.HOME_DASHBOARD);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setShowLoader(loader);
        getValues(inputForAPI, context);
    }


    private void getValues(InputForAPI inputForAPI, final Context context) {
        homeFragmentViewmodel.getDashBoard(inputForAPI).observe(this, new Observer<HomeDashBoardResponseModel>() {
            @Override
            public void onChanged(@Nullable HomeDashBoardResponseModel
                                          dashBoardResponse) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (dashBoardResponse != null) {
                    if (!dashBoardResponse.getError()) {
                        getDashBoardResponse(dashBoardResponse, context);
                    } else {
                        UiUtils.snackBar(rootView, dashBoardResponse.getMessage());
                    }
                }
            }
        });
    }


    public void getDashBoardResponse(HomeDashBoardResponseModel
                                             response, Context context) {

        final List<RandomRequestPending> getRandomRequest;
        final List<Pending> getPending = response.getPending();

        Accepted mAccepted;
        Pending mPending;
        RandomRequestPending requestPending;

        if (response.getRandomRequestPending() != null) {
            getRandomRequest = response.getRandomRequestPending();

            if (getRandomRequest.size() == 0) {
                if (getPending.size() > 0) {
                    mPending = getPending.get(0);
                    showRequestDialog(mPending.getName(), mPending.getUserimage(),
                            mPending.getSubCategoryName(),
                            mPending.getTiming(), mPending.getBookingDate(),
                            String.valueOf(mPending.getId()),
                            NORMAL_BOOKING, context);
                }

            } else {
                requestPending = getRandomRequest.get(0);

                showRequestDialog(requestPending.getName(), requestPending.getUserimage(), requestPending.getSubCategoryName(),
                        requestPending.getTiming(), requestPending.getBookingDate(), String.valueOf(requestPending.getId()),
                        RANDOM_BOOKING, context);
            }
        }


        final List<Accepted> getAccepted = response.getAccepted();
        if (getAccepted.size() > 0) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
            bookingsList.setVisibility(View.VISIBLE);
            bookingsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            BookingsAdapter adapter = new BookingsAdapter(activity, getAccepted);

            adapter.setStartImageJob(new StartImageJob() {
                @Override
                public void getBookingDetails(Activity activity, String bookingId,
                                              String bookingStatus) {
                    showImageDialog(activity, bookingId, bookingStatus);
                }
            });

            bookingsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);

            empty_layout.setVisibility(View.VISIBLE);
            bookingsList.setVisibility(View.GONE);
        }

        for (int i = 0; i < getAccepted.size(); i++) {
            mAccepted = getAccepted.get(i);

            if (mAccepted.getStatus().equalsIgnoreCase("Reviewpending")) {
                if (mAccepted.getIsProviderReviewed().toString().equals("0")) {
                    showReviewPending(mAccepted, context);
                }
            } else if (mAccepted.getStatus().equalsIgnoreCase("Waitingforpaymentconfirmation")) {
                showStatusDialog(context, mAccepted.getStatus(), "" + mAccepted.getId());

            } else if (mAccepted.getStatus().equalsIgnoreCase("Finished")) {
                if (mAccepted.getIsProviderReviewed().toString().equals("0")) {
                    showReviewPending(mAccepted, context);
                }
            } else if (mAccepted.getStatus().equalsIgnoreCase("Blocked")) {
                showStatusDialog(context, mAccepted.getStatus(), "");
            } else if (mAccepted.getStatus().equalsIgnoreCase("Dispute")) {
                showStatusDialog(context, mAccepted.getStatus(), "");
            }
        }

    }

    public void showReviewPending(final Accepted mAccepted,
                                  final Context context) {
        dismissDialog();

        BaseUtils.log(TAG, ":reviewCheck " + mAccepted);
        dialog = UiUtils.getCustomDialog(context, R.layout.dialog_rating);
        dialog.show();

        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button submit = dialog.findViewById(R.id.submit);
        final LinearLayout topLayout = dialog.findViewById(R.id.topLayout);
        final EditText feedBackText = dialog.findViewById(R.id.feedBackText);
        final LinearLayout commentSection = dialog.findViewById(R.id.commentSection);

        before_image_ln = dialog.findViewById(R.id.before_image_ln);
        after_image_ln = dialog.findViewById(R.id.after_image_ln);
        image_ln = dialog.findViewById(R.id.image_ln);

        final TextView booking_id = dialog.findViewById(R.id.booking_id);
        before_tv = dialog.findViewById(R.id.before_tv);
        after_tv = dialog.findViewById(R.id.after_tv);
        after_image = dialog.findViewById(R.id.after_image);
        before_image = dialog.findViewById(R.id.before_image);

        booking_id.setText(mAccepted.getBookingOrderId());
        feedBackText.setFilters(new InputFilter[]{ValidationsUtils.specialFilter});


        feedBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        JSONObject beforeObject = new JSONObject();
        try {
            beforeObject.put("booking_id", mAccepted.getId());
            beforeAfterImage(context, beforeObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (commentSection.getVisibility() != View.VISIBLE) {
                    AnimationHelper.animateUp(commentSection, context, topLayout);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.closeKeyboard(view);
                reviewRequest(context, ratingBar.getRating(), mAccepted.getId().toString(),
                        feedBackText.getText().toString(), appSettings.getProviderId());

            }
        });

    }

    private void showStatusDialog(final Context context, String status, String id) {
        BaseUtils.log(TAG, "CustomDialogs_status: " + status);
        dismissDialog();
        dialog = UiUtils.getCustomDialog(context, R.layout.dialog_status);
        dialog.show();

        FrameLayout dialogPayFl = dialog.findViewById(R.id.dialogPayFl);
        FrameLayout dialogIconFl = dialog.findViewById(R.id.dialogIconFl);
        TextView dialogHeading = dialog.findViewById(R.id.dialogHeading);
        TextView dialogContent = dialog.findViewById(R.id.dialogContent);
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        Button dialogButton = dialog.findViewById(R.id.dialogButton);


        if (status.equalsIgnoreCase(Constants.BOOKING_STATUS.Blocked)) {
            dialogIconFl.setVisibility(View.VISIBLE);

            loadImage("", dialogIcon, ContextCompat.getDrawable(context, R.drawable.block));
            dialogContent.setText(context.getString(R.string.blocked_text));

        } else if (status.equalsIgnoreCase(Constants.BOOKING_STATUS.Dispute)) {
            dialogIconFl.setVisibility(View.VISIBLE);

            loadImage("", dialogIcon, ContextCompat.getDrawable(context, R.drawable.dispute));
            dialogContent.setText(context.getString(R.string.dispute_text));


        } else if (status.equalsIgnoreCase(Constants.BOOKING_STATUS.WaitingForPaymentConfirmation)) {
            dialogHeading.setVisibility(View.VISIBLE);
            dialogPayFl.setVisibility(View.VISIBLE);
            dialogButton.setVisibility(View.VISIBLE);

            dialogButton.setText(context.getString(R.string.confirm));
            dialogContent.setText(context.getString(R.string.confirm_when_user_paid));

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentRequest(id, context);
                }
            });

        } else if (status.equalsIgnoreCase(Constants.BOOKING_STATUS.CompletedJob)) {
            dialogButton.setVisibility(View.VISIBLE);
            dialogIconFl.setVisibility(View.VISIBLE);

            loadImage("", dialogIcon, ContextCompat.getDrawable(context, R.drawable.ok));
            dialogIcon.setColorFilter(new PorterDuffColorFilter(UiUtils.getPrimaryColor(context), PorterDuff.Mode.SRC_IN));
            dialogContent.setText(context.getString(R.string.thanks_for));
            dialogButton.setText(context.getString(R.string.okay));

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pendingRequest(context);
                }
            });
        }
    }

    private void showRequestDialog(String mName, String mUserImages, String mSubcategoryName,
                                   String mTiming, String mBookingDate, final String mId,
                                   final String mBookingType, final Context context) {
        dismissDialog();
        dialog = UiUtils.getPopupDialog(activity, R.layout.dialog_request);
        dialog.show();

        TextView userName, descriptionText;
        ImageView userImage;
        LinearLayout acceptButton, rejectButton;

        acceptButton = dialog.findViewById(R.id.acceptButton);
        rejectButton = dialog.findViewById(R.id.rejectButton);
        userImage = dialog.findViewById(R.id.userImage);
        userName = dialog.findViewById(R.id.userName);
        descriptionText = dialog.findViewById(R.id.descriptionText);

        UiUtils.setProfilePicture(context, userImage);
        userName.setText(mName);
        loadImage(mUserImages, userImage, UiUtils.getProfilePicture(context));
        String[] split_name = mName.split(" ");
        String f_name = split_name[0];

        final SpannableString userNameSp = new SpannableString(f_name);
        final SpannableString sentRequestSp = new SpannableString(context.getResources().getString(R.string.sent_you_a_request));
        final SpannableString subCategorySp = new SpannableString(mSubcategoryName);
        final SpannableString timingSp = new SpannableString(mTiming);
        final SpannableString bookingDateSp = new SpannableString(mBookingDate);

        setSpanColor(context, userNameSp);
        setSpanColor(context, subCategorySp);
        setSpanColor(context, timingSp);
        setSpanColor(context, bookingDateSp);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(userNameSp).append(" ").append(sentRequestSp).append(" ")
                .append(subCategorySp).append(" ").append(getString(R.string.at)).append(" ")
                .append(timingSp).append(" ").append(getString(R.string.on)).append(" ")
                .append(bookingDateSp);
        descriptionText.setText(stringBuilder);


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptJob(mId, context, mBookingType);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectJob(mId, context, mBookingType);
            }
        });
    }

    private void setSpanColor(Context context, SpannableString userNameSp) {
        userNameSp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black)),
                0, userNameSp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void showImageDialog(Activity activity, String bookingId, String bookingStatus) {
        dialog = UiUtils.getPopupDialog(activity, R.layout.dialog_image);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        BaseUtils.log(TAG, "bookingId: " + bookingId + ", bookingStatus:" + bookingStatus);

        dialogHeading = dialog.findViewById(R.id.dialogHeading);
        yesButton = dialog.findViewById(R.id.yesButton);
        okButton = dialog.findViewById(R.id.okButton);
        noButton = dialog.findViewById(R.id.noButton);
        imageView = dialog.findViewById(R.id.imageView);

        dialogHeading.setText(R.string.take_picture);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkApi(activity);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkApi(activity);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                updateStatus(activity, bookingId, bookingStatus);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                updateStatus(activity, bookingId, bookingStatus);
            }
        });
    }

    private void checkApi(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Constants.PERMISSIONS.cameraPermList,
                    Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS);
        } else {
            UiUtils.openCamera(activity, Constants.CAMERA.FILE_NAME_JOB);
        }
    }


    public void beforeAfterImage(Context context, JSONObject beforeObject) {
        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.BEFORE_AFTER_IMAGE);
        inputForAPI.setJsonObject(beforeObject);
        inputForAPI.setHeaderStatus(false);
        inputForAPI.setShowLoader(true);
        getBeforeAfter(inputForAPI, context);
    }

    public void getBeforeAfter(InputForAPI inputForAPI, final Context context) {

        homeFragmentViewmodel.getAfterBeforeImage(inputForAPI).observe(this,
                new Observer<BeforeAfterImageModel>() {
                    @Override
                    public void onChanged(@Nullable BeforeAfterImageModel
                                                  response) {

                        handleBeforeAfterResponse(response, context);
                    }
                });
    }

    private void handleBeforeAfterResponse(@Nullable BeforeAfterImageModel response, Context context) {
        if (response != null) {
            if (!response.getError()) {
                Data data = response.getData();
                if (data != null) {

                    String start_image = null;
                    String end_image = null;

                    start_image = data.getStartImage();
                    end_image = data.getEndImage();
                    BaseUtils.log(TAG, "start_image: " + start_image
                            + ", end_image: " + end_image);

                    try {


                        if (start_image != null && !start_image.equalsIgnoreCase("null")) {
                            image_ln.setVisibility(View.VISIBLE);
                            before_image_ln.setVisibility(View.VISIBLE);
                            before_tv.setVisibility(View.VISIBLE);
                            loadImage(start_image, before_image,
                                    ContextCompat.getDrawable(context, R.drawable.service_ph));
                        } else {
                            before_image_ln.setVisibility(View.VISIBLE);
                            before_tv.setVisibility(View.VISIBLE);
                            image_ln.setVisibility(View.VISIBLE);
                            loadImage(start_image, before_image,
                                    ContextCompat.getDrawable(context, R.drawable.service_ph));
                        }

                        if (end_image != null && !end_image.equalsIgnoreCase("null")) {
                            image_ln.setVisibility(View.VISIBLE);
                            after_image_ln.setVisibility(View.VISIBLE);
                            after_tv.setVisibility(View.VISIBLE);
                            loadImage(end_image, after_image,
                                    ContextCompat.getDrawable(context, R.drawable.service_ph));
                        } else {
                            image_ln.setVisibility(View.VISIBLE);
                            after_image_ln.setVisibility(View.VISIBLE);
                            after_tv.setVisibility(View.VISIBLE);
                            loadImage(end_image, after_image,
                                    ContextCompat.getDrawable(context, R.drawable.service_ph));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                UiUtils.snackBar(rootView, response.getErrorMessage());
            }
        }
    }


    private void rejectJob(String id, final Context context,
                           String booking_type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);

        if (booking_type.equalsIgnoreCase(RANDOM_BOOKING)) {
            inputForAPI.setUrl(UrlHelper.REJECT_RANDOM_REQUEST);
            randomRequestRejectApi(inputForAPI);

        } else {
            inputForAPI.setUrl(UrlHelper.REJECT_JOB);
            rejectJobApi(inputForAPI);
        }

    }


    private void acceptJob(String id, final Context context,
                           String booking_type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);

        if (booking_type.equalsIgnoreCase(RANDOM_BOOKING)) {
            inputForAPI.setUrl(UrlHelper.ACCEPT_RANDOM_REQUEST);
            randomRequestAcceptApi(inputForAPI);
        } else {
            inputForAPI.setUrl(UrlHelper.ACCEPT_JOB);
            acceptJobApi(inputForAPI);
        }
    }


    private void paymentRequest(String id, final Context context) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.PAYMENT_CONFIRMATION);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        paymentConfigurationApi(inputForAPI);

    }

    public void reviewRequest(final Context context, float rating,
                              String booking_id, String s, String provider_id) {
        int f_rating = Math.round(rating);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("id", provider_id);
            jsonObject.put("rating", "" + f_rating);
            jsonObject.put("feedback", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.REVIEW);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        reviewApiCall(inputForAPI);
    }

    private void reviewApiCall(InputForAPI inputForAPI) {
        commonViewModel.review(inputForAPI).observe(this, new Observer<ReviewModel>() {
            @Override
            public void onChanged(@Nullable ReviewModel reviewModel) {
                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        onReviewSuccess(inputForAPI.getContext());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void acceptJobApi(InputForAPI inputForAPI) {

        commonViewModel.acceptJob(inputForAPI).observe(this, new Observer<JobAcceptApi>() {
            @Override
            public void onChanged(@Nullable JobAcceptApi reviewModel) {

                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        pendingRequest(inputForAPI.getContext());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getError_message());
                    }
                }
            }
        });
    }


    private void randomRequestAcceptApi(InputForAPI inputForAPI) {

        commonViewModel.acceptRandomRequest(inputForAPI).observe(this, new Observer<RandomRequestAcceptApi>() {
            @Override
            public void onChanged(@Nullable RandomRequestAcceptApi reviewModel) {

                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        pendingRequest(inputForAPI.getContext());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getError_message());
                    }
                }
            }
        });
    }

    private void rejectJobApi(InputForAPI inputForAPI) {

        commonViewModel.rejectJob(inputForAPI).observe(this, new Observer<JobRejectApi>() {
            @Override
            public void onChanged(@Nullable JobRejectApi reviewModel) {

                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        pendingRequest(inputForAPI.getContext());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getError_message());
                    }
                }
            }
        });
    }


    private void randomRequestRejectApi(InputForAPI inputForAPI) {

        commonViewModel.rejectRandomRequest(inputForAPI).observe(this, new Observer<RandomRequestRejectApi>() {
            @Override
            public void onChanged(@Nullable RandomRequestRejectApi reviewModel) {

                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        pendingRequest(inputForAPI.getContext());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getError_message());
                    }
                }
            }
        });
    }

    private void paymentConfigurationApi(InputForAPI inputForAPI) {
        commonViewModel.paymentConfirmation(inputForAPI).observe(this, new Observer<PaymentConfirmationApi>() {
            @Override
            public void onChanged(@Nullable PaymentConfirmationApi reviewModel) {
                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        pendingRequest(inputForAPI.getContext());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getError_message());
                    }
                }
            }
        });
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void pendingRequest(Context context) {
        dismissDialog();
        getPendRequest(context, true);
    }

    private void onReviewSuccess(Context context) {
        dismissDialog();
        showStatusDialog(context, Constants.BOOKING_STATUS.CompletedJob, "");
    }

    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        GlideHelper.setImage(url, imageView, drawable);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Status event) {
        getPendRequest(activity, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPendRequest(activity, true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = (MainActivity) view.getContext();
        initViews(view);
        initListener();
        return view;
    }


    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPendRequest(activity, false);
            }
        });
    }

    private void initViews(View view) {
        appSettings = new AppSettings(activity);
        bookingsList = view.findViewById(R.id.bookingsList);
        rootView = view.findViewById(R.id.rootView);
        empty_layout = view.findViewById(R.id.empty_layout);
        TextView empty_text = view.findViewById(R.id.empty_text);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        homeFragmentViewmodel = ViewModelProviders.of(this).get(HomeFragmentViewmodel.class);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);

        empty_text.setText(getString(R.string.empty_bookings));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BaseUtils.log(TAG, "onRequestPermissionsResult:" + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int unMaskedRequestCode = requestCode & 0x0000ffff;
        switch (unMaskedRequestCode) {
            case Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    UiUtils.openCamera(activity, Constants.CAMERA.FILE_NAME_JOB);
                } else {
                    UiUtils.snackBar(rootView, activity.getString(R.string.camera_permission_error));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseUtils.log(TAG, "onActivityResult: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        int unMaskedRequestCode = requestCode & 0x0000ffff;
        switch (unMaskedRequestCode) {
            case Constants.REQUEST_CODE.CAMERA_INTENT://camera
                if (resultCode == RESULT_OK) {
                    setUploadFile = new File(String.valueOf(appSettings.getImageUploadPath()));
                    if (setUploadFile.exists()) {
                        changeAlertDialog();
                    }
                }
                break;
        }
    }


    private void updateStatus(Activity context, String id, final String status) {
        if (id != null && status != null) {

            final JSONObject mainJsonObject = new JSONObject();
            try {
                mainJsonObject.put("id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getCompleteAddress(context, mainJsonObject, status);

        }
    }

    private void getCompleteAddress(Activity context, JSONObject mainJsonObject, String status) {
        String url = buildUrl(Double.parseDouble(appSettings.getLatitude()), Double.parseDouble(appSettings.getLongitude()));

        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(url);
        inputForAPI.setJsonObject(mainJsonObject);
        inputForAPI.setHeaderStatus(false);
        inputForAPI.setShowLoader(true);
        getAddressDetails(context, inputForAPI, mainJsonObject, status);
    }

    private String buildUrl(double latitude, double longitude) {
        return commonViewModel.getGeoCodeMapUrl(latitude, longitude);
    }

    private void getAddressDetails(Activity context, InputForAPI inputForAPI, final JSONObject mainJsonObject,
                                   final String status) {

        commonViewModel.googleGeoCodeApi(inputForAPI).observe(this,
                new Observer<GoogleMapApiModel>() {
                    @Override
                    public void onChanged(@Nullable GoogleMapApiModel bookingadapterAddressModel) {
                        if (bookingadapterAddressModel != null) {
                            handleAddressResponse(context, bookingadapterAddressModel, mainJsonObject, status);
                        }
                    }
                });
    }

    private void handleAddressResponse(Activity context, GoogleMapApiModel response,
                                       JSONObject mainJsonObject, String status) {
        Result resultAddress;

        if (response.getStatus().equalsIgnoreCase("OK")) {
            List<Result> listDetailBooking;
            listDetailBooking = response.getResults();
            resultAddress = listDetailBooking.get(0);
            String address = resultAddress.getFormattedAddress();

            if (address != null) {
                callApi(context, address, mainJsonObject, status);
            } else {
                callApi(context, " ", mainJsonObject, status);
            }

        } else {
            callApi(context, " ", mainJsonObject, status);
        }
    }

    private void callApi(Activity context, String address, JSONObject mainJsonObject, String status) {
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
        imageRequest(context, setUploadFile, mainJsonObject, status);
    }

    private void startJobApi(Context context, InputForAPI inputForAPI) {
        commonViewModel.startJob(inputForAPI).observe((LifecycleOwner) context, new Observer<StartJobApiModel>() {
            @Override
            public void onChanged(@Nullable StartJobApiModel response) {
                UiUtils.dismiss();
                if (response != null) {
                    if (!response.getError()) {
                        startEndJobResponse(context);
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
                        startEndJobResponse(context);
                    } else {
                        UiUtils.snackBar(rootView, response.getErrorMessage());
                    }
                }
            }
        });
    }

    private void startEndJobResponse(Context context) {
        setUploadFile = new File("");
        getPendRequest(context, true);
        EventBus.getDefault().post(new Status());
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
        } else if (status.equalsIgnoreCase(Constants.BOOKINGS.STATUS_COMPLETE)) {
            mainJsonObject.put("end_image", imageUrl);
            inputForAPI.setJsonObject(mainJsonObject);
            inputForAPI.setUrl(UrlHelper.COMPLETED_JOB);
            BaseUtils.log(TAG, "imageUploadJson: " + mainJsonObject);
            completeJobApi(activity, inputForAPI);
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