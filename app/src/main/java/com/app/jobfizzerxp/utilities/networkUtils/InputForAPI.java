package com.app.jobfizzerxp.utilities.networkUtils;

import android.content.Context;

import com.app.jobfizzerxp.utilities.helpers.AppSettings;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class InputForAPI {
    private JSONObject jsonObject;
    private String url;
    private HashMap<String, String> headers;
    private File file;
    private Context context;
    private boolean showLoader;

    public InputForAPI(Context context) {
        this.context = context;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setHeaders() {
        AppSettings appSettings = new AppSettings(getContext());
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + appSettings.getToken());
        this.headers = headers;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isShowLoader() {
        return showLoader;
    }

    public void setShowLoader(boolean showLoader) {
        this.showLoader = showLoader;
    }

    public void setHeaderStatus(boolean headerStatus) {
        if (headerStatus) {
            setHeaders();
        } else {
            this.headers = new HashMap<>();
        }
    }
}