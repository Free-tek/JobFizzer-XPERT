package com.app.jobfizzerxp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CancelJobApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    private final static long serialVersionUID = 41303188396911970L;

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

}