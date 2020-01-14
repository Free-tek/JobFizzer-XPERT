package com.app.jobfizzerxp.model.appSettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Schedules implements Serializable {
    private final static long serialVersionUID = -3515589073376307128L;

    @SerializedName("selected")
    @Expose
    private String selected;
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("time_Slots_id")
    @Expose
    private String timeSlotsId;
    @SerializedName("days")
    @Expose
    private String days;

    @SerializedName("selected_text")
    @Expose
    private String selected_text;
    @SerializedName("TimeSlotApi")
    @Expose
    private List<TimeSlotApi> mTimeSlotApi = null;

    public String getTimeSlotsId() {
        return timeSlotsId;
    }

    public void setTimeSlotsId(String timeSlotsId) {
        this.timeSlotsId = timeSlotsId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }


    public String getSelected_text() {
        return selected_text;
    }

    public void setSelected_text(String selected_text) {
        this.selected_text = selected_text;
    }


    public List<TimeSlotApi> getmTimeSlotApi() {
        return mTimeSlotApi;
    }

    public void setmTimeSlotApi(List<TimeSlotApi> mTimeSlotApi) {
        this.mTimeSlotApi = mTimeSlotApi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}