package com.app.jobfizzerxp.utilities.helpers;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by KrishnaDev on 1/10/17.
 */

public class SharedHelper {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.apply();

    }

    public static void putKey(Context context, String Key, boolean Value) {
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(Key, Value);
        editor.apply();

    }

    public static boolean getKey(Context contextGetKey, String Key, boolean defValue) {
        sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        boolean value = defValue;
        value = sharedPreferences.getBoolean(Key, defValue);
        return value;

    }

    public static void putKey(Context context, String Key, int Value) {
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(Key, Value);
        editor.apply();

    }

    public static String getKey(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");

    }

    public static boolean putKey(Context mContext, String arrayName, boolean[] array) {

        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);

        return editor.commit();
    }

    public static boolean[] getKey(String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        boolean array[] = new boolean[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);

        return array;
    }

    public static void putHeader(Context context, String Key, String Value) {
        if (Value != null) {
            sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString(Key, Value);
            editor.apply();
        } else {
            sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString(Key, "0");
            editor.commit();
        }
    }

    public static String getHeader(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "0");

    }

    public static void putInt(Context context, String Key, int Value) {
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, String.valueOf(Value));
        editor.apply();

    }

    public static String getInt(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "0");

    }


}
