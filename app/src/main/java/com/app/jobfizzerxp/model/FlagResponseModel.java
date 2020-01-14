package com.app.jobfizzerxp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FlagResponseModel implements Serializable {


    @Expose
    @SerializedName("error_message")
    private String error_message;
    @Expose
    @SerializedName("error")
    private String error;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
