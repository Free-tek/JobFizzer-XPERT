package com.app.jobfizzerxp.model.appSettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Waitingforpaymentconfirmation implements Serializable {

    @SerializedName("booking_id")
    @Expose
    private Object bookingId;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = -6952874133326515296L;

    public Object getBookingId() {
        return bookingId;
    }

    public void setBookingId(Object bookingId) {
        this.bookingId = bookingId;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}