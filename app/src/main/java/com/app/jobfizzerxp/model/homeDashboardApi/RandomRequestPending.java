package com.app.jobfizzerxp.model.homeDashboardApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RandomRequestPending implements Serializable {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("user_mobile")
    @Expose
    private String userMobile;
    @SerializedName("userimage")
    @Expose
    private String userimage;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("long_description")
    @Expose
    private String longDescription;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("fromTime")
    @Expose
    private String fromTime;
    @SerializedName("toTime")
    @Expose
    private String toTime;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("booking_order_id")
    @Expose
    private String bookingOrderId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("show_bill_flag")
    @Expose
    private String showBillFlag;
    @SerializedName("Pending_time")
    @Expose
    private String pendingTime;
    @SerializedName("Accepted_time")
    @Expose
    private String acceptedTime;
    @SerializedName("Rejected_time")
    @Expose
    private String rejectedTime;
    @SerializedName("Finished_time")
    @Expose
    private String finishedTime;
    @SerializedName("CancelledbyUser_time")
    @Expose
    private String cancelledbyUserTime;
    @SerializedName("CancelledbyProvider_time")
    @Expose
    private String cancelledbyProviderTime;
    @SerializedName("StarttoCustomerPlace_time")
    @Expose
    private String starttoCustomerPlaceTime;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("worked_mins")
    @Expose
    private String workedMins;
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
    @SerializedName("booking_type")
    @Expose
    private String bookingType;
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("user_latitude")
    @Expose
    private Double userLatitude;
    @SerializedName("user_longitude")
    @Expose
    private Double userLongitude;
    @SerializedName("admin_share")
    @Expose
    private String adminShare;
    @SerializedName("provider_share")
    @Expose
    private String providerShare;
    private final static long serialVersionUID = -5293084400711129337L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookingOrderId() {
        return bookingOrderId;
    }

    public void setBookingOrderId(String bookingOrderId) {
        this.bookingOrderId = bookingOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShowBillFlag() {
        return showBillFlag;
    }

    public void setShowBillFlag(String showBillFlag) {
        this.showBillFlag = showBillFlag;
    }

    public Object getPendingTime() {
        return pendingTime;
    }

    public void setPendingTime(String pendingTime) {
        this.pendingTime = pendingTime;
    }

    public Object getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(String acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public Object getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(String rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public Object getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Object getCancelledbyUserTime() {
        return cancelledbyUserTime;
    }

    public void setCancelledbyUserTime(String cancelledbyUserTime) {
        this.cancelledbyUserTime = cancelledbyUserTime;
    }

    public Object getCancelledbyProviderTime() {
        return cancelledbyProviderTime;
    }

    public void setCancelledbyProviderTime(String cancelledbyProviderTime) {
        this.cancelledbyProviderTime = cancelledbyProviderTime;
    }

    public Object getStarttoCustomerPlaceTime() {
        return starttoCustomerPlaceTime;
    }

    public void setStarttoCustomerPlaceTime(String starttoCustomerPlaceTime) {
        this.starttoCustomerPlaceTime = starttoCustomerPlaceTime;
    }

    public Object getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Object getWorkedMins() {
        return workedMins;
    }

    public void setWorkedMins(String workedMins) {
        this.workedMins = workedMins;
    }

    public Object getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Object getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(String gstPercent) {
        this.gstPercent = gstPercent;
    }

    public Object getGstCost() {
        return gstCost;
    }

    public void setGstCost(String gstCost) {
        this.gstCost = gstCost;
    }

    public Object getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(Double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public Object getAdminShare() {
        return adminShare;
    }

    public void setAdminShare(String adminShare) {
        this.adminShare = adminShare;
    }

    public Object getProviderShare() {
        return providerShare;
    }

    public void setProviderShare(String providerShare) {
        this.providerShare = providerShare;
    }

}
