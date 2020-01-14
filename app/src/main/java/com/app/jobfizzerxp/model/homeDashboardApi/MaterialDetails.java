package com.app.jobfizzerxp.model.homeDashboardApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MaterialDetails implements Serializable {

    @SerializedName("material_name")
    @Expose
    private String material_name;

    @SerializedName("material_cost")
    @Expose
    private String material_cost;

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMaterial_cost() {
        return material_cost;
    }

    public void setMaterial_cost(String material_cost) {
        this.material_cost = material_cost;
    }


}
