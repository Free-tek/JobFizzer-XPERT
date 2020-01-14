package com.app.jobfizzerxp.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.calenderBookingApi.CalendarBookingsApiModel;
import com.app.jobfizzerxp.model.providerCalendarApi.ProviderCalendarApiModel;
import com.app.jobfizzerxp.model.providerCalendarApi.Providerworkingdetail;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.AppointmentsActViewModel;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppointmentsActivity extends BaseActivity {
    private String TAG = AppointmentsActivity.class.getSimpleName();
    private final String params_1 = "date";
    private CalendarView calendarView;
    private List<EventDay> bookings;
    private int currentYear, currentMonth, currentDate;
    private CommonViewModel commonViewModel;
    private AppointmentsActViewModel appointmentsActViewModel;
    private LinearLayout rootView;
    private ImageView backButton;
    private TextView toolBarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        initViews();
        setValue();
        initListeners();
        providerCalendarRequest(ConversionUtils.getCurrentDate());

    }


    private void initViews() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        appointmentsActViewModel = ViewModelProviders.of(this).get(AppointmentsActViewModel.class);
        calendarView = findViewById(R.id.calendarView);
        rootView = findViewById(R.id.rootView);
        backButton = findViewById(R.id.backButton);
        toolBarTitle = findViewById(R.id.toolBarTitle);
    }

    private void setValue() {
        bookings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDate = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(currentYear, currentMonth, currentDate);
        toolBarTitle.setText(getString(R.string.appointments));

        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
    }


    private void initListeners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                if (currentMonth <= 0) {
                    currentYear = currentYear - 1;
                    currentMonth = 11;

                } else {
                    currentMonth = currentMonth - 1;
                }

                providerCalendarRequest(setSelectedDate());
            }
        });
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                if (currentMonth >= 11) {
                    currentYear = currentYear + 1;
                    currentMonth = 0;

                } else {
                    currentMonth = currentMonth + 1;
                }
                providerCalendarRequest(setSelectedDate());

            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                calendarBookingRequest(ConversionUtils.dateToString(clickedDayCalendar));
            }
        });
    }


    private void providerCalendarRequest(String value) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(params_1, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(AppointmentsActivity.this);
        inputForAPI.setUrl(UrlHelper.PROVIDER_CALENDAR);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(false);
        inputForAPI.setHeaderStatus(true);

        appointmentsActViewModel.calendarApi(inputForAPI).observe(this, new Observer<ProviderCalendarApiModel>() {
            @Override
            public void onChanged(@Nullable ProviderCalendarApiModel providerCalendarApiModel) {
                handleCalendarResponse(providerCalendarApiModel);
            }
        });
    }

    private String setSelectedDate() {

        return String.valueOf(ConversionUtils.getCombinedStrings(currentYear + "-" + (currentMonth + 1) + "-" + currentDate));

    }

    private void handleCalendarResponse(ProviderCalendarApiModel providerCalendarApiModel) {
        if (providerCalendarApiModel != null) {
            if (!providerCalendarApiModel.getError()) {
                List<Providerworkingdetail> providerWorkingDetails = providerCalendarApiModel.getProviderworkingdetails();
                if (providerWorkingDetails != null) {
                    for (int i = 0; i < providerWorkingDetails.size(); i++) {
                        Providerworkingdetail providerworkingdetail = providerWorkingDetails.get(i);
                        setBookingValue(providerworkingdetail.getBookingDate());
                    }
                    calendarView.setEvents(bookings);
                }
            } else {

                UiUtils.snackBar(rootView, providerCalendarApiModel.getErrorMessage());
            }
        }
    }

    private void setBookingValue(String date) {
        EventDay eventDay = new EventDay(ConversionUtils.stringToDate(date), R.drawable.appoint_circle);
        bookings.add(eventDay);
    }

    private void calendarBookingRequest(final String value) {
        InputForAPI inputForAPI = getInputForCalendarAPI(value);

        commonViewModel.calendarBookingDetails(inputForAPI).observe(this, new Observer<CalendarBookingsApiModel>() {
            @Override
            public void onChanged(@Nullable CalendarBookingsApiModel reviewModel) {

                if (reviewModel != null) {
                    if (!reviewModel.getError()) {
                        onCalendarBookingSuccess(inputForAPI.getJsonObject());
                    } else {
                        UiUtils.snackBar(rootView, reviewModel.getMessage());
                    }
                }
            }
        });
    }

    @NonNull
    private InputForAPI getInputForCalendarAPI(String value) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(params_1, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(AppointmentsActivity.this);
        inputForAPI.setUrl(UrlHelper.CALENDAR_BOOKING_DETAILS);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setShowLoader(true);
        return inputForAPI;
    }

    private void onCalendarBookingSuccess(JSONObject jsonObject) {
        Intent intent = new Intent(AppointmentsActivity.this, DetailedAppointmentsActivity.class);
        intent.putExtra(Constants.INTENT_KEYS.DATE, jsonObject.toString());
        startActivity(intent);
    }

}