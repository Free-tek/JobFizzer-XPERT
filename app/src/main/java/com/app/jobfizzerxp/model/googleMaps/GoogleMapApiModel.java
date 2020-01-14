
package com.app.jobfizzerxp.model.googleMaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoogleMapApiModel implements Serializable {

    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    private final static long serialVersionUID = 8150551162575003972L;

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
