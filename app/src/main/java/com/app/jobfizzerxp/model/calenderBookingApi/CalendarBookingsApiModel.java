package com.app.jobfizzerxp.model.calenderBookingApi;

import com.app.jobfizzerxp.model.homeDashboardApi.Accepted;
import com.app.jobfizzerxp.model.homeDashboardApi.Pending;
import com.app.jobfizzerxp.model.homeDashboardApi.RandomRequestPending;
import com.app.jobfizzerxp.model.homeDashboardApi.RecurralPending;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CalendarBookingsApiModel implements Serializable {

    @SerializedName("Pending")
    @Expose
    private List<Pending> pending = null;
    @SerializedName("Accepted")
    @Expose
    private List<Accepted> accepted = null;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("random_request_pending")
    @Expose
    private List<RandomRequestPending> randomRequestPending = null;
    @SerializedName("RecurralPending")
    @Expose
    private List<RecurralPending> recurralPending = null;
    private final static long serialVersionUID = -6281773006508263923L;

    public List<Pending> getPending() {
        return pending;
    }

    public void setPending(List<Pending> pending) {
        this.pending = pending;
    }

    public List<Accepted> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<Accepted> accepted) {
        this.accepted = accepted;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<RandomRequestPending> getRandomRequestPending() {
        return randomRequestPending;
    }

    public void setRandomRequestPending(List<RandomRequestPending> randomRequestPending) {
        this.randomRequestPending = randomRequestPending;
    }

    public List<RecurralPending> getRecurralPending() {
        return recurralPending;
    }

    public void setRecurralPending(List<RecurralPending> recurralPending) {
        this.recurralPending = recurralPending;
    }


}