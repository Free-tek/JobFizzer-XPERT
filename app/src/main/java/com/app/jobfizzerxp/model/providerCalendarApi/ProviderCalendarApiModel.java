package com.app.jobfizzerxp.model.providerCalendarApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProviderCalendarApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("providerworkingdetails")
    @Expose
    private List<Providerworkingdetail> providerworkingdetails = null;
    private final static long serialVersionUID = 581186938843549458L;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Providerworkingdetail> getProviderworkingdetails() {
        return providerworkingdetails;
    }

    public void setProviderworkingdetails(List<Providerworkingdetail> providerworkingdetails) {
        this.providerworkingdetails = providerworkingdetails;
    }

}