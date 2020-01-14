package com.app.jobfizzerxp.view.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.appSettingsApi.AppsettingsApiModel;
import com.app.jobfizzerxp.model.appSettingsApi.TimeSlotApi;
import com.app.jobfizzerxp.model.viewScheduleApi.UpdateScheduleApiModel;
import com.app.jobfizzerxp.model.viewScheduleApi.ViewScheduleApiModel;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.adapters.TimeSlotAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.viewModel.ViewScheduleViewModel;
import com.google.gson.Gson;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSchedule extends BaseActivity implements View.OnClickListener {
    private TextView mondayView, tuesdayView, wednesdayView, thursdayView, fridayView,
            saturdayView, sundayView;
    private int selectedTime;
    private String mon = "", tue = "", wed = "", thu = "", fri = "", sat = "", sun = "";
    private Button submitButton;
    private String provider_id;
    private String id;
    private AppSettings appSettings;
    private String TAG = ViewSchedule.class.getSimpleName();
    private ImageView backButton;

    private LinearLayout rootView;
    private ViewScheduleViewModel scheduleViewModel;
    private CommonViewModel commonViewModel;
    private List<TimeSlotApi> scheduleList = new ArrayList<>();
    private List<TimeSlotApi> timeSlotApiList = new ArrayList<>();
    private List<TimeSlotApi> savedTimeSlotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        initViews();
        initListners();
        getMyTimes();
    }


    private void getMyTimes() {
        InputForAPI inputForAPI = new InputForAPI(ViewSchedule.this);
        inputForAPI.setUrl(UrlHelper.VIEW_SCHEDULES);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        scheduleViewModel.viewSchedule(inputForAPI).observe(this, new Observer<ViewScheduleApiModel>() {
            @Override
            public void onChanged(@Nullable ViewScheduleApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        onViewScheduleSuccess(addCategoryApiModel);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void onViewScheduleSuccess(ViewScheduleApiModel viewScheduleApiModel) {
        scheduleList = viewScheduleApiModel.getSchedules();
        if (scheduleList.size() != 0) {
            provider_id = String.valueOf(scheduleList.get(0).getProvider_id());
        }

        for (int i = 0; i < scheduleList.size(); i++) {
            TimeSlotApi schedule;
            schedule = scheduleList.get(i);
            if (schedule.getStatus().equalsIgnoreCase("1")) {
                if (schedule.getDays().equalsIgnoreCase("Mon")) {
                    if (mon.length() == 0) {
                        mon = schedule.getTiming();
                    } else {
                        mon = mon + ", " + schedule.getTiming();
                    }
                }
                if (schedule.getDays().equalsIgnoreCase("Tue")) {
                    if (tue.length() == 0) {
                        tue = schedule.getTiming();
                    } else {
                        tue = tue + ", " + schedule.getTiming();
                    }
                }
                if (schedule.getDays().equalsIgnoreCase("Wed")) {
                    if (wed.length() == 0) {
                        wed = schedule.getTiming();
                    } else {
                        wed = wed + ", " + schedule.getTiming();
                    }
                }
                if (schedule.getDays().equalsIgnoreCase("Thu")) {
                    if (thu.length() == 0) {
                        thu = schedule.getTiming();
                    } else {
                        thu = thu + ", " + schedule.getTiming();
                    }
                }
                if (schedule.getDays().equalsIgnoreCase("Fri")) {
                    if (fri.length() == 0) {
                        fri = schedule.getTiming();
                    } else {
                        fri = fri + ", " + schedule.getTiming();
                    }
                }
                if (schedule.getDays().equalsIgnoreCase("Sat")) {
                    if (sat.length() == 0) {
                        sat = schedule.getTiming();
                    } else {
                        sat = sat + ", " + schedule.getTiming();
                    }
                }
                if (schedule.getDays().equalsIgnoreCase("Sun")) {
                    if (sun.length() == 0) {
                        sun = schedule.getTiming();
                    } else {
                        sun = sun + ", " + schedule.getTiming();
                    }
                }
            }
        }
        setDateTexts(mondayView, mon);
        setDateTexts(tuesdayView, tue);
        setDateTexts(wednesdayView, wed);
        setDateTexts(thursdayView, thu);
        setDateTexts(fridayView, fri);
        setDateTexts(saturdayView, sat);
        setDateTexts(sundayView, sun);
        getTimeslotApi();

    }


    private void setDateTexts(TextView textView, String text) {
        if (text.length() == 0) {
            setDefaultTimeslots(textView);
        } else {
            setSelectedTimeslots(textView, text);
        }
    }

    private void initViews() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        appSettings = new AppSettings(ViewSchedule.this);
        scheduleViewModel = ViewModelProviders.of(this).get(ViewScheduleViewModel.class);
        rootView = findViewById(R.id.rootView);
        mondayView = findViewById(R.id.mondayView);
        tuesdayView = findViewById(R.id.tuesdayView);
        wednesdayView = findViewById(R.id.wednesdayView);
        thursdayView = findViewById(R.id.thursdayView);
        fridayView = findViewById(R.id.fridayView);
        saturdayView = findViewById(R.id.saturdayView);
        sundayView = findViewById(R.id.sundayView);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.schedules);

    }

    private void initListners() {
        mondayView.setOnClickListener(this);
        tuesdayView.setOnClickListener(this);
        wednesdayView.setOnClickListener(this);
        thursdayView.setOnClickListener(this);
        fridayView.setOnClickListener(this);
        saturdayView.setOnClickListener(this);
        sundayView.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    public void setDefaultTimeslots(TextView textView) {
        textView.setText(getResources().getString(R.string.add_time));
        textView.setTextSize(14);
        textView.setTextColor(UiUtils.getPrimaryColor(ViewSchedule.this));
    }

    public void setSelectedTimeslots(TextView textView, String text) {
        textView.setText(text);
        textView.setTextSize(11);
        textView.setTextColor(ContextCompat.getColor(ViewSchedule.this, R.color.grey));
    }


    private void getTimeslotApi() {
        InputForAPI inputForAPI = new InputForAPI(ViewSchedule.this);
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

                savedTimeSlot.setStatus("0");
                savedTimeSlot.setSelected_text("");
                savedTimeSlot.setProvider_id(appSettings.getProviderId());
                savedTimeSlot.setId("");

                for (int k = 0; k < scheduleList.size(); k++) {
                    TimeSlotApi timeSlotApi1 = new TimeSlotApi();
                    timeSlotApi1 = scheduleList.get(k);

                    if (timeSlotApi1.getTime_Slots_id().equalsIgnoreCase(savedTimeSlot.getTime_Slots_id())) {
                        if (timeSlotApi1.getDays().equalsIgnoreCase(savedTimeSlot.getDays())) {
                            savedTimeSlot.setStatus(timeSlotApi1.getStatus());
                            savedTimeSlot.setId(timeSlotApi1.getId());

                        }
                    }
                }
                savedTimeSlotList.add(savedTimeSlot);
            }
        }
    }

    private void showTimeSlotDialog() {
        final Dialog dialog = new Dialog(ViewSchedule.this);
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

                String text = commonViewModel.getSelectedTimeSlots(timeSlotApiList);
                if (selectedTime == 0) {
                    setDateTexts(mondayView, text);

                } else if (selectedTime == 1) {
                    setDateTexts(tuesdayView, text);
                } else if (selectedTime == 2) {
                    setDateTexts(wednesdayView, text);
                } else if (selectedTime == 3) {
                    setDateTexts(thursdayView, text);
                } else if (selectedTime == 4) {
                    setDateTexts(fridayView, text);
                } else if (selectedTime == 5) {
                    setDateTexts(saturdayView, text);
                } else if (selectedTime == 6) {
                    setDateTexts(sundayView, text);
                }
                dialog.dismiss();
            }
        });
        int totalLength = timeSlotApiList.size();
        try {
            for (int i = 0; i < totalLength; i++) {
                TimeSlotApi timeSlotApi = new TimeSlotApi();
                timeSlotApi = timeSlotApiList.get(i);
                timeSlotApi.setSelected("false");

                for (int j = 0; j < savedTimeSlotList.size(); j++) {
                    String check_tex;
                    if (selectedTime == 0) {
                        check_tex = "Mon";
                    } else if (selectedTime == 1) {
                        check_tex = "Tue";
                    } else if (selectedTime == 2) {
                        check_tex = "Wed";
                    } else if (selectedTime == 3) {
                        check_tex = "Thu";
                    } else if (selectedTime == 4) {
                        check_tex = "Fri";
                    } else if (selectedTime == 5) {
                        check_tex = "Sat";
                    } else {
                        check_tex = "Sun";
                    }
                    TimeSlotApi timeSlotApi1 = new TimeSlotApi();
                    timeSlotApi1 = savedTimeSlotList.get(j);


                    if (timeSlotApi1.getTime_Slots_id().equalsIgnoreCase(timeSlotApi.getId())) {
                        if (timeSlotApi1.getDays().equalsIgnoreCase(check_tex)) {
                            if (timeSlotApi1.getStatus().equalsIgnoreCase("0")) {
                                timeSlotApi.setSelected("false");
                            } else {
                                timeSlotApi.setSelected("true");

                            }
                            break;
                        }
                    }
                }
                timeSlotApiList.set(i, timeSlotApi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(ViewSchedule.this, timeSlotApiList, savedTimeSlotList, selectedTime);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(ViewSchedule.this, 4, GridLayoutManager.VERTICAL, false);
        timeSlot.setLayoutManager(linearLayoutManager);
        timeSlot.setAdapter(timeSlotAdapter);

    }


    @Override
    public void onClick(View view) {

        if (view == mondayView) {
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
        } else if (view == submitButton) {
            UiUtils.closeKeyboard(view);
            try {
                postUpdatedValues();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (view == backButton) {
            finish();
        }

    }

    private void postUpdatedValues() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schedules", getSchedules());

        InputForAPI inputForAPI = new InputForAPI(ViewSchedule.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_SCHEDULES);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);

        scheduleViewModel.updateSchedule(inputForAPI).observe(this, new Observer<UpdateScheduleApiModel>() {
            @Override
            public void onChanged(@Nullable UpdateScheduleApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        moveMainActivity();
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void moveMainActivity() {
        finish();
    }


    private String getSchedules() {
        for (int i = 0; i < savedTimeSlotList.size(); i++) {
            savedTimeSlotList.get(i).setSelected(null);
        }
        BaseUtils.log(TAG, "savedTimeSlotList: " + savedTimeSlotList);

        return new Gson().toJson(savedTimeSlotList);
    }
}