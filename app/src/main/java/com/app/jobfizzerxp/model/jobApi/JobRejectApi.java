package com.app.jobfizzerxp.model.jobApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobRejectApi implements Serializable {

    @Expose
    @SerializedName("error_message")
    private String error_message;
    @Expose
    @SerializedName("error")
    private boolean error;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
