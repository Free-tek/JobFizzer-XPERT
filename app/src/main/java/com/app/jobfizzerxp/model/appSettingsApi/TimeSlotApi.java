package com.app.jobfizzerxp.model.appSettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TimeSlotApi implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("provider_id")
    @Expose
    private String provider_id;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("time_Slots_id")
    @Expose
    private String time_Slots_id;
    @SerializedName("selected")
    @Expose
    private String selected;
    @SerializedName("selected_text")
    @Expose
    private String selected_text;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("fromTime")
    @Expose
    private String fromTime;
    @SerializedName("toTime")
    @Expose
    private String toTime;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = 467895346219913000L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime_Slots_id() {
        return time_Slots_id;
    }

    public void setTime_Slots_id(String time_Slots_id) {
        this.time_Slots_id = time_Slots_id;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }
}