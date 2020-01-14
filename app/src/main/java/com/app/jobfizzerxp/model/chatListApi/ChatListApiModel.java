package com.app.jobfizzerxp.model.chatListApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatListApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("msglist")
    @Expose
    private List<MsgList> msglist = null;
    private final static long serialVersionUID = -4602281749660234808L;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<MsgList> getMsglist() {
        return msglist;
    }

    public void setMsglist(List<MsgList> msglist) {
        this.msglist = msglist;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}