package com.app.jobfizzerxp.model.homeDashboardApi;

import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Invoicedetail implements Serializable {

    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("booking_order_id")
    @Expose
    private String bookingOrderId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("providername")
    @Expose
    private String providername;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("job_start_time")
    @Expose
    private String jobStartTime;
    @SerializedName("job_end_time")
    @Expose
    private String jobEndTime;
    @SerializedName("coupon_applied")
    @Expose
    private String couponApplied;
    @SerializedName("off")
    @Expose
    private String off;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("tax_name")
    @Expose
    private String taxName;
    @SerializedName("gst_percent")
    @Expose
    private String gstPercent;
    @SerializedName("gst_cost")
    @Expose
    private String gstCost;
    @SerializedName("total_cost")
    @Expose
    private String totalCost;
    @SerializedName("doorno")
    @Expose
    private String doorno;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("worked_mins")
    @Expose
    private String workedMins;
    @SerializedName("alltax")
    @Expose
    private List<Alltax> alltax = null;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @SerializedName("material_details")
    @Expose
    private List<MaterialDetails> material_details = null;
    private final static long serialVersionUID = -8267604436220772512L;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingOrderId() {
        return bookingOrderId;
    }

    public void setBookingOrderId(String bookingOrderId) {
        this.bookingOrderId = bookingOrderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(String jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public String getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(String jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public String getCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(String couponApplied) {
        this.couponApplied = couponApplied;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(String gstPercent) {
        this.gstPercent = gstPercent;
    }

    public String getGstCost() {
        return gstCost;
    }

    public void setGstCost(String gstCost) {
        this.gstCost = gstCost;
    }

    public String getTotalCost() {
        return ConversionUtils.getRoundUpValue(totalCost);

    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(String doorno) {
        this.doorno = doorno;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getWorkedMins() {
        return workedMins;
    }

    public void setWorkedMins(String workedMins) {
        this.workedMins = workedMins;
    }

    public List<Alltax> getAlltax() {
        return alltax;
    }

    public List<MaterialDetails> getMaterial_details() {
        return material_details;
    }

    public void setMaterial_details(List<MaterialDetails> material_details) {
        this.material_details = material_details;
    }

    public void setAlltax(List<Alltax> alltax) {
        this.alltax = alltax;
    }

}