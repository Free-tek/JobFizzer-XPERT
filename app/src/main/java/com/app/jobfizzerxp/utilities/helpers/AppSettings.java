package com.app.jobfizzerxp.utilities.helpers;

import android.content.Context;

import com.app.jobfizzerxp.model.appSettingsApi.Schedules;
import com.app.jobfizzerxp.model.appSettingsApi.TimeSlotApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/*
 * Created by user on 26-10-2017.
 */

public class AppSettings {
    private Context context;
    private boolean isLogged;
    private String token;
    private String fireBaseToken;
    private String imageUploadPath;
    private String profilePic;
    private String pageNumber;
    private String providerId;
    private JSONArray registerArray = new JSONArray();
    private JSONArray categoryArray = new JSONArray();
    private List<Schedules> schedules = new ArrayList<>();
    private JSONArray availableSlots = new JSONArray();
    private String latitude;
    private String longitude;
    private String accessToken;
    private String userName;
    private String userType;


    private JSONArray addressArray = new JSONArray();


    public AppSettings(Context context) {
        this.context = context;
    }


    public JSONArray getAddressArray() {
        try {
            addressArray = new JSONArray(SharedHelper.getKey(context, "addressArray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addressArray;
    }


    public void setAddressArray(JSONArray addressArray) {
        SharedHelper.putKey(context, "addressArray", addressArray.toString());

        this.addressArray = addressArray;
    }


    public JSONArray getCategoryArray() {
        try {
            categoryArray = new JSONArray(SharedHelper.getKey(context, "categoryArray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categoryArray;
    }

    public void setCategoryArray(JSONArray categoryArray) {
        SharedHelper.putKey(context, "categoryArray", categoryArray.toString());
        this.categoryArray = categoryArray;
    }


    public String getUserName() {
        userName = SharedHelper.getKey(context, "userName");

        return userName;
    }

    public void setUserName(String userName) {
        SharedHelper.putKey(context, "userName", userName);

        this.userName = userName;
    }

    public String getAccessToken() {
        accessToken = SharedHelper.getKey(context, "accessToken");
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        SharedHelper.putKey(context, "accessToken", accessToken);
        this.accessToken = accessToken;
    }


    public String getUserType() {
        userType = SharedHelper.getKey(context, "userType");

        return userType;
    }

    public void setUserType(String userType) {
        SharedHelper.putKey(context, "userType", userType);

        this.userType = userType;
    }


    public String getImageUploadPath() {
        imageUploadPath = SharedHelper.getKey(context, "imageUploadPath");

        return imageUploadPath;
    }

    public void setImageUploadPath(String imageUploadPath) {
        SharedHelper.putKey(context, "imageUploadPath", imageUploadPath);

        this.imageUploadPath = imageUploadPath;
    }

    public String getProviderId() {
        providerId = SharedHelper.getKey(context, "providerId");

        return providerId;
    }

    public void setProviderId(String providerId) {
        SharedHelper.putKey(context, "providerId", providerId);

        this.providerId = providerId;
    }


    public JSONArray getRegisterArray() {
        try {
            registerArray = new JSONArray(SharedHelper.getKey(context, "registerArray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return registerArray;
    }

    public void setRegisterArray(JSONArray registerArray) {
        SharedHelper.putKey(context, "registerArray", registerArray.toString());

        this.registerArray = registerArray;
    }


    public JSONArray getAvailableSlots() throws JSONException {
        availableSlots = new JSONArray(SharedHelper.getKey(context, "registerArray"));

        return availableSlots;
    }

    public void setAvailableSlots(JSONArray availableSlots) {
        SharedHelper.putKey(context, "availableSlots", availableSlots.toString());

        this.availableSlots = availableSlots;
    }

    public List<Schedules> getSchedules() {
        Type type = new TypeToken<List<TimeSlotApi>>() {
        }.getType();
        schedules = new Gson().fromJson(SharedHelper.getKey(context, "schedules"), type);

        return schedules;
    }

    public void setSchedules(List<Schedules> imagesList) {
        SharedHelper.putKey(context, "schedules", new Gson().toJson(imagesList));
        this.schedules = imagesList;

    }


    public String getPageNumber() {
        pageNumber = SharedHelper.getKey(context, "pageNumber");

        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        SharedHelper.putKey(context, "pageNumber", pageNumber);

        this.pageNumber = pageNumber;
    }

    public boolean getIsLogged() {
        isLogged = SharedHelper.getKey(context, "isLogged", false);
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        SharedHelper.putKey(context, "isLogged", isLogged);
        this.isLogged = isLogged;
    }


    public String getLatitude() {
        latitude = SharedHelper.getKey(context, "latitude");
        return latitude;
    }

    public void setLatitude(String latitude) {
        SharedHelper.putKey(context, "latitude", latitude);
        this.latitude = latitude;
    }

    public String getLongitude() {
        longitude = SharedHelper.getKey(context, "longitude");
        return longitude;
    }

    public void setLongitude(String longitude) {
        SharedHelper.putKey(context, "longitude", longitude);
        this.longitude = longitude;
    }

    public String getToken() {
        token = SharedHelper.getKey(context, "token");
        return token;
    }

    public void setToken(String token) {
        SharedHelper.putKey(context, "token", token);
        this.token = token;
    }

    public String getFireBaseToken() {
        fireBaseToken = SharedHelper.getKey(context, "fireBaseToken");
        return fireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        SharedHelper.putKey(context, "fireBaseToken", fireBaseToken);
        this.fireBaseToken = fireBaseToken;
    }

    public String getProfilePic() {
        profilePic = SharedHelper.getKey(context, "profilePic");
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        SharedHelper.putKey(context, "profilePic", profilePic);
        this.profilePic = profilePic;
    }
}