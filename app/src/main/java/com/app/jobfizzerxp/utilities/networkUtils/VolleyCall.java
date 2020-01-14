package com.app.jobfizzerxp.utilities.networkUtils;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.jobfizzerxp.application.AppController;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.interfaces.ApiResponseHandler;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by user on 23-10-2017.
 */

public class VolleyCall {

    private static final int MY_SOCKET_TIMEOUT_MS = 50000;
    private static final String TAG = VolleyCall.class.getSimpleName();

    public static void postMethod(InputForAPI inputForAPI, final ApiResponseHandler volleyCallback) {
        final Context context = inputForAPI.getContext();
        final String url = inputForAPI.getUrl();
        JSONObject params = inputForAPI.getJsonObject();
        final HashMap<String, String> headers = inputForAPI.getHeaders();
        boolean showLoader = inputForAPI.isShowLoader();

        if (ConnectionUtils.isNetworkConnected(context)) {
            if (showLoader) {
                UiUtils.show(context);
            }
            BaseUtils.log(TAG, "url:" + url + ",input: " + params);
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            BaseUtils.log(TAG, "url: " + url + ", response: " + response);
                            if (showLoader) {
                                UiUtils.dismiss();
                            }
                            volleyCallback.setResponseSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    BaseUtils.log(TAG, "url: " + url + ", onErrorResponse: " + error);
                    if (showLoader) {
                        UiUtils.dismiss();
                    }
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        volleyCallback.setResponseError(context.getString(R.string.no_internet_connection));
                    } else if (error instanceof AuthFailureError) {
                        volleyCallback.setResponseError(context.getString(R.string.session_expired));
                    } else if (error instanceof ServerError) {
                        volleyCallback.setResponseError(context.getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {
                        volleyCallback.setResponseError(context.getString(R.string.network_error));
                    } else if (error instanceof ParseError) {
                        volleyCallback.setResponseError(context.getString(R.string.parsing_error));
                    } else {
                        volleyCallback.setResponseError(context.getString(R.string.network_error));
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    BaseUtils.log(TAG, "getHeaders: " + headers.toString());
                    return headers;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq);

        } else {
            volleyCallback.setResponseError(context.getString(R.string.no_internet_connection));
        }
    }


    public static void getMethod(final InputForAPI inputForAPI, final ApiResponseHandler volleyCallback) {
        final Context context = inputForAPI.getContext();
        final String url = inputForAPI.getUrl();
        final HashMap<String, String> headers = inputForAPI.getHeaders();
        boolean showLoader = inputForAPI.isShowLoader();

        if (ConnectionUtils.isNetworkConnected(context)) {
            if (showLoader) {
                UiUtils.show(context);
            }
            BaseUtils.log(TAG, "url: " + url);
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            BaseUtils.log(TAG, "url: " + url + ", response: " + response);
                            if (showLoader) {
                                UiUtils.dismiss();
                            }
                            volleyCallback.setResponseSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    BaseUtils.log(TAG, "url: " + url + ", onErrorResponse: " + error);
                    if (showLoader) {
                        UiUtils.dismiss();
                    }
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.no_internet_connection));
                    } else if (error instanceof AuthFailureError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.session_expired));
                    } else if (error instanceof ServerError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.network_error));
                    } else if (error instanceof ParseError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.parsing_error));
                    } else {
                        volleyCallback.setResponseError(context.getString(R.string.network_error));
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    BaseUtils.log(TAG, "getHeaders: " + headers.toString());
                    return headers;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq);

        } else {
            volleyCallback.setResponseError(context.getString(R.string.no_internet_connection));
        }
    }

    public static void uploadImage(final InputForAPI inputForAPI, final ApiResponseHandler apiResponseHandler) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        File file = inputForAPI.getFile();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        MediaType mediaType = MediaType.parse("image/jpeg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(mediaType, new File(file.getPath())))
                .build();
        AppSettings appSettings = new AppSettings(inputForAPI.getContext());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(UrlHelper.UPLOAD_IMAGE)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + appSettings.getToken())
                .build();
        BaseUtils.log(TAG, "url: " + UrlHelper.UPLOAD_IMAGE + " ," + "file: " + file.getPath());
        try {
            okhttp3.Response response = client.newCall(request).execute();
            JSONObject jsonObject = null;
            if (response.body() != null) {
//                BaseUtils.log(TAG, "response: "+response.body().string());
//                BaseUtils.log(TAG, "response: "+response.message());
                jsonObject = new JSONObject(response.body().string());
                BaseUtils.log(TAG, "url: " + UrlHelper.UPLOAD_IMAGE + " ," + "setResponseSuccess: " + jsonObject);
                apiResponseHandler.setResponseSuccess(jsonObject);
            } else {
                BaseUtils.log(TAG, "url: " + UrlHelper.UPLOAD_IMAGE + " ," + "setResponseError: ");
                UiUtils.dismiss();
                apiResponseHandler.setResponseError(inputForAPI.getContext().getString(R.string.network_error));
            }
        } catch (IOException e) {
            BaseUtils.log(TAG, "url: " + UrlHelper.UPLOAD_IMAGE + " ," + "IOException: " + e.getMessage());
            UiUtils.dismiss();
            apiResponseHandler.setResponseError(e.getMessage());
        } catch (JSONException e) {
            BaseUtils.log(TAG, "url: " + UrlHelper.UPLOAD_IMAGE + " ," + "JSONException: " + e.getMessage());
            UiUtils.dismiss();
            apiResponseHandler.setResponseError(e.getMessage());
        }
    }
}