package com.app.jobfizzerxp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LogOutApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    private final static long serialVersionUID = 7055010241070447712L;

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