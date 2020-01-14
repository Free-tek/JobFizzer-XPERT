package com.app.jobfizzerxp.model.viewScheduleApi;

import com.app.jobfizzerxp.model.appSettingsApi.TimeSlotApi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ViewScheduleApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("schedules")
    @Expose
    private List<TimeSlotApi> schedules = null;
    private final static long serialVersionUID = 7867479039447070743L;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<TimeSlotApi> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<TimeSlotApi> schedules) {
        this.schedules = schedules;
    }

}