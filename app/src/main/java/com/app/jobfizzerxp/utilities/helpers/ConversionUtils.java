package com.app.jobfizzerxp.utilities.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import com.app.jobfizzerxp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConversionUtils {
    private static String TAG = ConversionUtils.class.getSimpleName();


    @NonNull
    public static String getRoundUpValue(String totalCost) {
        return new BigDecimal(totalCost).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static CharSequence appendCurrencySymbol(Context context, String value) {
        return new StringBuilder().append(context.getString(R.string.currency_symbol)).append(value);
    }

    public static CharSequence appendPercentage(Context context, String value) {
        return new StringBuilder().append(value).append(context.getString(R.string.percentage_symbol));
    }

    public static CharSequence getCombinedStrings(String... strings) {
        StringBuilder finalString = new StringBuilder();
        for (String string : strings) {
            finalString.append(string);
        }
        return finalString;
    }



    public static String dateToString(Calendar strDate) {
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return mdformat.format(strDate.getTime());
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return mdformat.format(calendar.getTime());
    }

    public static String capWords(String str) {
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }

    public static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static String convertDate(String dateInMilliseconds) {
        return DateFormat.format("h:mm a", Long.parseLong(dateInMilliseconds)).toString();
    }

    public static String getConvertedString(String str_getBookingDate) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date outDate = dateFormatter.parse(str_getBookingDate);

        java.text.DateFormat formatter = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
        return formatter.format(outDate);
    }

    public static Calendar stringToDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String convertedTime(String time) throws ParseException {
        String ftime = "";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date outDate = dateFormatter.parse(time);
        java.text.DateFormat formatter = new SimpleDateFormat("d MMM, yyyy HH:mm a", Locale.getDefault());
        ftime = formatter.format(outDate);

        return ftime;
    }

    public static String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        if (minutes.equalsIgnoreCase("00")) {
            return (totalMinutes / 60) + " hrs";
        }
        return (totalMinutes / 60) + ":" + minutes + " hrs";
    }
}