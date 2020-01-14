package com.app.jobfizzerxp.model.appSettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status implements Serializable {

    @SerializedName("Blocked")
    @Expose
    private Blocked blocked;
    @SerializedName("Dispute")
    @Expose
    private Dispute dispute;
    @SerializedName("Reviewpending")
    @Expose
    private Reviewpending reviewpending;
    @SerializedName("InvoicePending")
    @Expose
    private InvoicePending invoicePending;
    @SerializedName("Waitingforpaymentconfirmation")
    @Expose
    private Waitingforpaymentconfirmation waitingforpaymentconfirmation;
    @SerializedName("Finished")
    @Expose
    private Finished finished;
    @SerializedName("Completedjob")
    @Expose
    private Completedjob completedjob;
    private final static long serialVersionUID = 3380054576397783998L;

    public Blocked getBlocked() {
        return blocked;
    }

    public void setBlocked(Blocked blocked) {
        this.blocked = blocked;
    }

    public Dispute getDispute() {
        return dispute;
    }

    public void setDispute(Dispute dispute) {
        this.dispute = dispute;
    }

    public Reviewpending getReviewpending() {
        return reviewpending;
    }

    public void setReviewpending(Reviewpending reviewpending) {
        this.reviewpending = reviewpending;
    }

    public InvoicePending getInvoicePending() {
        return invoicePending;
    }

    public void setInvoicePending(InvoicePending invoicePending) {
        this.invoicePending = invoicePending;
    }

    public Waitingforpaymentconfirmation getWaitingforpaymentconfirmation() {
        return waitingforpaymentconfirmation;
    }

    public void setWaitingforpaymentconfirmation(Waitingforpaymentconfirmation waitingforpaymentconfirmation) {
        this.waitingforpaymentconfirmation = waitingforpaymentconfirmation;
    }

    public Finished getFinished() {
        return finished;
    }

    public void setFinished(Finished finished) {
        this.finished = finished;
    }

    public Completedjob getCompletedjob() {
        return completedjob;
    }

    public void setCompletedjob(Completedjob completedjob) {
        this.completedjob = completedjob;
    }

}
