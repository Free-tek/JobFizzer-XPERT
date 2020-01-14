package com.app.jobfizzerxp.model.messageListApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("senderID")
    @Expose
    private Integer senderID;
    @SerializedName("receiverID")
    @Expose
    private Integer receiverID;
    @SerializedName("senderType")
    @Expose
    private String senderType;
    @SerializedName("receiverType")
    @Expose
    private String receiverType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("matchID")
    @Expose
    private String matchID;
    private final static long serialVersionUID = 1283918781249596081L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public Integer getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Integer receiverID) {
        this.receiverID = receiverID;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}