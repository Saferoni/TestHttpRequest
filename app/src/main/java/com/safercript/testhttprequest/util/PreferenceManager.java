package com.safercript.testhttprequest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferenceManager {
    private static final String LOG_TAG = PreferenceManager.class.getSimpleName();

    private static final String PREF_NAME = "com.safercript.testhttprequest";

    private static final String PREF_USER_NICKNAME = "pref-nickname";
    private static final String PREF_USER_EMAIL = "pref-user-email";
    private static final String PREF_USER_PASSWORD = "pref-user-pass";
    private static final String PREF_USER_TOKEN = "pref-user-token";

    private static SharedPreferences prefs;

    private static SharedPreferences init(Context activity) {
        if (prefs == null) {
            prefs = activity.getSharedPreferences(
                    PREF_NAME, Context.MODE_PRIVATE);
        }
        return prefs;
    }


    public static void saveUserNickname(Context context, String userNickname) {
        putStringValue(context, PreferenceManager.PREF_USER_NICKNAME, userNickname);
    }
    public static String getUserNickname(Context context) {
        return getStringValue(context, PreferenceManager.PREF_USER_NICKNAME, null);
    }



    public static void saveUserEmail(Context context, String userNickname) {
        putStringValue(context, PreferenceManager.PREF_USER_EMAIL, userNickname);
    }
    public static String getUserEmail(Context context) {
        return getStringValue(context, PreferenceManager.PREF_USER_EMAIL, null);
    }



    public static void saveUserPassword(Context context, String userPassword) {
        putStringValue(context, PreferenceManager.PREF_USER_PASSWORD, userPassword);
    }
    public static String getUserPassword(Context context) {
        return getStringValue(context, PreferenceManager.PREF_USER_PASSWORD, null);
    }
    public static void deleteUserPassword(Context context) {
        deleteValue(context, PreferenceManager.PREF_USER_PASSWORD);
    }



    public static void saveToken(Context context, String userNickname) {
        putStringValue(context, PreferenceManager.PREF_USER_TOKEN, userNickname);
    }
    public static String getToken(Context context) {
        return getStringValue(context, PreferenceManager.PREF_USER_TOKEN, null);
    }
    public static void deleteToken(Context context) {
        deleteValue(context, PreferenceManager.PREF_USER_TOKEN);
    }




    private static void putStringValue(Context context, String key, String value) {
        init(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStringValue(Context context, String key, String defaultValue) {
        init(context);
        return prefs.getString(key, defaultValue);
    }

    private static void putBooleanValue(Context context, String key, boolean value) {
        init(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static boolean getBooleanValue(Context context, String key) {
        init(context);
        return prefs.getBoolean(key, true);
    }

    private static void putIntValue(Context context, String key , int value){
        init(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static int getIntValue(Context context, String key){
        init(context);
        return prefs.getInt(key, 0);
    }

    private static void deleteValue(Context context, String key) {
        init(context);
        prefs.edit().remove(key).apply();
    }

    public static void clearPreferences(Context activity) {
        init(activity);

        prefs.edit().clear().apply();

        Log.d(LOG_TAG, "Pref cleared");
    }
}