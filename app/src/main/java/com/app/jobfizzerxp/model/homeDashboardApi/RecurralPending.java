package com.app.jobfizzerxp.model.homeDashboardApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecurralPending implements Serializable {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("user_mobile")
    @Expose
    private int userMobile;
    @SerializedName("userimage")
    @Expose
    private String userimage;
    @SerializedName("ProviderName")
    @Expose
    private String providerName;
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
    @SerializedName("startjob_timestamp")
    @Expose
    private String startjobTimestamp;
    @SerializedName("endjob_timestamp")
    @Expose
    private String endjobTimestamp;
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
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("job_start_time")
    @Expose
    private String jobStartTime;
    @SerializedName("job_end_time")
    @Expose
    private String jobEndTime;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("reduced_cost")
    @Expose
    private String reducedCost;
    @SerializedName("coupon_applied")
    @Expose
    private String couponApplied;
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
    private Integer totalCost;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("doorno")
    @Expose
    private String doorno;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("user_latitude")
    @Expose
    private Double userLatitude;
    @SerializedName("user_longitude")
    @Expose
    private Double userLongitude;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isProviderReviewed")
    @Expose
    private Integer isProviderReviewed;
    @SerializedName("admin_share")
    @Expose
    private String adminShare;
    @SerializedName("provider_share")
    @Expose
    private String providerShare;
    @SerializedName("show_bill_flag")
    @Expose
    private String showBillFlag;
    @SerializedName("invoicedetails")
    @Expose
    private List<Invoicedetail> invoicedetails = null;
    private final static long serialVersionUID = 2157966328908380421L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(int userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getPendingTime() {
        return pendingTime;
    }

    public void setPendingTime(String pendingTime) {
        this.pendingTime = pendingTime;
    }

    public String getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(String acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public String getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(String rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getCancelledbyUserTime() {
        return cancelledbyUserTime;
    }

    public void setCancelledbyUserTime(String cancelledbyUserTime) {
        this.cancelledbyUserTime = cancelledbyUserTime;
    }

    public String getCancelledbyProviderTime() {
        return cancelledbyProviderTime;
    }

    public void setCancelledbyProviderTime(String cancelledbyProviderTime) {
        this.cancelledbyProviderTime = cancelledbyProviderTime;
    }

    public String getStarttoCustomerPlaceTime() {
        return starttoCustomerPlaceTime;
    }

    public void setStarttoCustomerPlaceTime(String starttoCustomerPlaceTime) {
        this.starttoCustomerPlaceTime = starttoCustomerPlaceTime;
    }

    public String getStartjobTimestamp() {
        return startjobTimestamp;
    }

    public void setStartjobTimestamp(String startjobTimestamp) {
        this.startjobTimestamp = startjobTimestamp;
    }

    public String getEndjobTimestamp() {
        return endjobTimestamp;
    }

    public void setEndjobTimestamp(String endjobTimestamp) {
        this.endjobTimestamp = endjobTimestamp;
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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getReducedCost() {
        return reducedCost;
    }

    public void setReducedCost(String reducedCost) {
        this.reducedCost = reducedCost;
    }

    public String getCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(String couponApplied) {
        this.couponApplied = couponApplied;
    }

    public String getWorkedMins() {
        return workedMins;
    }

    public void setWorkedMins(String workedMins) {
        this.workedMins = workedMins;
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

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsProviderReviewed() {
        return isProviderReviewed;
    }

    public void setIsProviderReviewed(Integer isProviderReviewed) {
        this.isProviderReviewed = isProviderReviewed;
    }

    public String getAdminShare() {
        return adminShare;
    }

    public void setAdminShare(String adminShare) {
        this.adminShare = adminShare;
    }

    public String getProviderShare() {
        return providerShare;
    }

    public void setProviderShare(String providerShare) {
        this.providerShare = providerShare;
    }

    public String getShowBillFlag() {
        return showBillFlag;
    }

    public void setShowBillFlag(String showBillFlag) {
        this.showBillFlag = showBillFlag;
    }

    public List<Invoicedetail> getInvoicedetails() {
        return invoicedetails;
    }

    public void setInvoicedetails(List<Invoicedetail> invoicedetails) {
        this.invoicedetails = invoicedetails;
    }

}