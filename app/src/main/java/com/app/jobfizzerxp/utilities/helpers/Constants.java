package com.app.jobfizzerxp.utilities.helpers;

/*
 * Created by Poyya on 05-09-2018.
 */

import android.Manifest;

import com.app.jobfizzerxp.BuildConfig;

public class Constants {
    public static final boolean DEBUG = true;

    public static class URL {
        public static final String BASE = BuildConfig.BASE;
        public static final String LOCATION_BASE = BuildConfig.LOCATION_BASE;
        public static final String CHAT_BASE = BuildConfig.CHAT_BASE;
    }

    public static class BOOKING_STATUS {
        public static final String Blocked = "Blocked";
        public static final String Dispute = "Dispute";
        public static final String Pending = "Pending";
        public static final String Accepted = "Accepted";
        public static final String Rejected = "Rejected";
        public static final String CancelledByUser = "CancelledbyUser";
        public static final String CancelledByProvider = "CancelledbyProvider";
        public static final String StartToCustomerPlace = "StarttoCustomerPlace";
        public static final String StartedJob = "Startedjob";
        public static final String CompletedJob = "Completedjob";
        public static final String ReviewPending = "Reviewpending";
        public static final String WaitingForPaymentConfirmation = "Waitingforpaymentconfirmation";
        public static final String Finished = "Finished";
    }

    public static class LOGIN {
        public static final String HOME_TAB = "home_tab";
        public static final String CHAT_TAB = "chat_tab";
        public static final String BOOKINGS_TAB = "bookings_tab";
        public static final String ACCOUNTS_TAB = "accounts_tab";
        public static final String LOGIN_TYPE = "login_type";
    }

    public static class CHATS {
        public static final String TYPE_RECEIVER = "receiver";
        public static final String CONTENT_TEXT = "text";
        public static final String TYPE_SENDER = "sender";
    }

    public static class PERMISSIONS {
        public static final int STORAGE_PERMISSIONS = 190;
        public static final int CAMERA_PERMISSIONS = 191;
        public static final int COARSE_LOCATION_PERMISSIONS = 192;
        public static final int FINE_LOCATION_PERMISSIONS = 196;
        public static final int LOCATION_PERMISSIONS = 197;
        public static final int CALL_PERMISSIONS = 193;
        public static final int READ_STORAGE_PERMISSIONS = 194;
        public static final int CAMERA_STORAGE_PERMISSIONS = 195;
        public static final String[] cameraPermList = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        public static final String[] readStoragePermList = {Manifest.permission.READ_EXTERNAL_STORAGE};
        public static final String[] galleryPermList = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public static class REQUEST_CODE {
        public static final int CAMERA_INTENT = 201;
        public static final int GALLERY_INTENT = 202;
    }

    public static class INTENT_KEYS {
        public static String VALUES = "appointments";
        public static String DATE = "date";
        public static String USER_IMAGE = "userImage";
        public static String USER_NAME = "userName";
        public static String USER_ID = "userID";
        public static String BOOKING_ID = "booking_id";
        public static String BOOKING_STATUS = "bookingstatus";
        public static String PROVIDER_DETAILS = "provider_details";

    }

    public static class RANDOM_BOOLEAN {
        public static final boolean FALSE = false;
        public static final boolean TRUE = true;
    }

    public static class CAMERA {
        public static final String FILE_NAME_PROFILE = "PROFILE_IMG_";
        public static final String FILE_NAME_JOB = "JOB_IMG_";
    }

    public static class BOOKINGS {
        public static final String RANDOM_BOOKING = "random";
        public static final String NORMAL_BOOKING = "normal";
        public static final String USER_TYPE = "Provider";
        public static final String STATUS_START = "startJob";
        public static final String STATUS_COMPLETE = "completeJob";
    }
}