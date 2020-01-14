package com.app.jobfizzerxp.utilities.helpers;
/*
 * Created by user on 23-10-2017.
 */

public class UrlHelper {
    private static String BASE = Constants.URL.BASE;
    public static String LOCATION_SOCKET = Constants.URL.LOCATION_BASE;
    public static String CHAT_SOCKET = Constants.URL.CHAT_BASE;
    private static String BASE_URL = BASE + "uberdoo/public/provider/";
    public static String UPLOAD_IMAGE = BASE_URL + "imageupload";
    public static String ABOUT_US = BASE + "uberdoo/public/admin/showPag";
    public static String SIGN_UP = BASE_URL + "provider_signup";
    public static String SIGN_IN = BASE_URL + "providerlogin";
    public static String APP_SETTINGS = BASE_URL + "appsettingsprovider";
    public static String UPDATE_DEVICE_TOKEN = BASE_URL + "updatedevicetoken";

    public static String HOME_DASHBOARD = BASE_URL + "homedashboard";
    public static String VIEW_PROFILE = BASE_URL + "viewprofile";
    public static String VIEW_CATEGORY = BASE_URL + "view_provider_category";
    public static String VIEW_SCHEDULES = BASE_URL + "view_schedules";
    public static String UPDATE_SCHEDULES = BASE_URL + "updateschedules";
    public static String UPDATE_ADDRESS = BASE_URL + "update_address";
    public static String UPDATE_PROFILE = BASE_URL + "updateprofile";

    public static String FORGOT_PASSWORD = BASE_URL + "forgotpassword";
    public static String RESET_PASSWORD = BASE_URL + "resetpassword";
    public static String CHANGE_PASSWORD = BASE_URL + "changepassword";

    public static String ACCEPT_JOB = BASE_URL + "acceptbooking";
    public static String REJECT_JOB = BASE_URL + "rejectbooking";
    public static String START_JOB = BASE_URL + "startedjob";
    public static String START_TO_PLACE = BASE_URL + "starttocustomerplace";
    public static String COMPLETED_JOB = BASE_URL + "completedjob";
    public static String PAYMENT_CONFIRMATION = BASE_URL + "paymentaccept";
    public static String REVIEW = BASE_URL + "userreviews";
    public static String CANCEL_BOOKING = BASE_URL + "cancelbyprovider";

    public static String LIST_CATEGORY = BASE_URL + "listcategory";
    public static String LIST_SUB_CATEGORY = BASE_URL + "listsubcategory";
    public static String ADD_CATEGORY = BASE_URL + "add_category";
    public static String EDIT_CATEGORY = BASE_URL + "edit_category";
    public static String DELETE_CATEGORY = BASE_URL + "delete_category";
    public static String VERIFY_OTP = BASE_URL + "otpcheck";


    public static String ACCEPT_RANDOM_REQUEST = BASE_URL + "accept_random_request";
    public static String REJECT_RANDOM_REQUEST = BASE_URL + "reject_random_request";
    public static String PROVIDER_CALENDAR = BASE_URL + "providercalender";
    public static String CALENDAR_BOOKING_DETAILS = BASE_URL + "calenderbookingdetails";
    public static String BEFORE_AFTER_IMAGE = BASE_URL + "startjobendjobdetails";
    public static String LOG_OUT = BASE_URL + "logout";
    public static String ELAPSED_TIME = BASE_URL + "elapsetime";

    //CHAT APi
    public static String CHAT_MESSAGES = CHAT_SOCKET + "userchatlist";
    public static String CHAT_LISTS = CHAT_SOCKET + "usermsglist";

    //SOCKET EMITTERS
    public static String GET_ONLINE = "get_provider_online";
    public static String SEND_MESSAGE = "sendmessagefromprovider";
    public static String IS_OFFLINE = "isOffline";
    public static String IS_ONLINE = "isOnline";
    public static String SEND_DELIVERED = "send_delivered";
    public static String UPDATE_LOCATION = "UpdateLocation";

    //SOCKET LISTENER
    public static String RECEVICE_MESSAGE = "recievemessage";
}