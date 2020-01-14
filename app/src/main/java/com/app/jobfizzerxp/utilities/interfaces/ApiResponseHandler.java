package com.app.jobfizzerxp.utilities.interfaces;

import org.json.JSONObject;

public interface ApiResponseHandler {

    void setResponseSuccess(JSONObject response);

    void setResponseError(String error);

}