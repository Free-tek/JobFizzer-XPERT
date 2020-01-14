package com.app.jobfizzerxp.model.appSettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppsettingsApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    @SerializedName("delete_status")
    @Expose
    private String deleteStatus;

    @SerializedName("location")
    @Expose
    private List<Location> location = null;
    @SerializedName("timeslots")
    @Expose
    private List<TimeSlotApi> timeSlotApiList = null;
    @SerializedName("status")
    @Expose
    private List<Status> status = null;
    private final static long serialVersionUID = 6626283414282228445L;

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

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public List<TimeSlotApi> getTimeSlotApiList() {
        return timeSlotApiList;
    }

    public void setTimeSlotApiList(List<TimeSlotApi> timeSlotApiList) {
        this.timeSlotApiList = timeSlotApiList;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}