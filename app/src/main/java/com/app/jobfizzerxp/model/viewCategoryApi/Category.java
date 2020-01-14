package com.app.jobfizzerxp.model.viewCategoryApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("service_sub_category_id")
    @Expose
    private Integer serviceSubCategoryId;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("priceperhour")
    @Expose
    private String priceperhour;
    @SerializedName("quickpitch")
    @Expose
    private String quickpitch;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("long_description")
    @Expose
    private String longDescription;
    private final static long serialVersionUID = 7648746287291837592L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getServiceSubCategoryId() {
        return serviceSubCategoryId;
    }

    public void setServiceSubCategoryId(Integer serviceSubCategoryId) {
        this.serviceSubCategoryId = serviceSubCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPriceperhour() {
        return priceperhour;
    }

    public void setPriceperhour(String priceperhour) {
        this.priceperhour = priceperhour;
    }

    public String getQuickpitch() {
        return quickpitch;
    }

    public void setQuickpitch(String quickpitch) {
        this.quickpitch = quickpitch;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}