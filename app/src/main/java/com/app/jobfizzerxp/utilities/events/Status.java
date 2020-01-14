package com.app.jobfizzerxp.utilities.events;

import org.json.JSONObject;

/**
 * Created by yuvaraj on 31/10/17.
 */

public class Status {
    private JSONObject jsonObject;

    public Status(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Status() {
    }

    public JSONObject getJsonObject() {

        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
