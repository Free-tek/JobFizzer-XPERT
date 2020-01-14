
package com.app.jobfizzerxp.model.categoryApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String error_message;
    @SerializedName("list_category")
    @Expose
    private List<ListCategory> list_category = null;
    private final static long serialVersionUID = -5731288976390516402L;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return error_message;
    }

    public void setErrorMessage(String errorMessage) {
        this.error_message = errorMessage;
    }

    public List<ListCategory> getListCategory() {
        return list_category;
    }

    public void setListCategory(List<ListCategory> listCategory) {
        this.list_category = listCategory;
    }

}
