package com.app.jobfizzerxp.model.categoryApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubCategoryApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("list_subcategory")
    @Expose
    private List<ListSubcategory> listSubcategory = null;
    private final static long serialVersionUID = -11416124303664426L;

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

    public List<ListSubcategory> getListSubcategory() {
        return listSubcategory;
    }

    public void setListSubcategory(List<ListSubcategory> listSubcategory) {
        this.listSubcategory = listSubcategory;
    }

}