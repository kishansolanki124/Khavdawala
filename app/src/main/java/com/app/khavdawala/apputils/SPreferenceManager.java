package com.app.khavdawala.apputils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import app.app.patidarsaurabh.apputils.AppConstants;

public class SPreferenceManager {

    private static final String PREF_NAME = "khavda";
    private static SPreferenceManager mInstance;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson mGson;

    private SPreferenceManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mGson = new Gson();
    }

    public static SPreferenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SPreferenceManager(context);
        }
        return mInstance;
    }

    public Boolean isLogin() {
        return mPreferences.getBoolean(AppConstants.IS_LOGIN, false);
    }
//
//    public void saveSession(String mobileNumber) {
//        setBoolean(AppConstants.IS_LOGIN, true);
//        setString(AppConstants.MOBILE, mobileNumber);
//    }
//
//    public String getSession() {
//        return mPreferences.getString(AppConstants.MOBILE, "");
//    }
//
//    public void setString(String key, String value) {
//        mEditor.putString(key, value).apply();
//    }
//
//    public String getString(String key, String value) {
//        return mPreferences.getString(key, value);
//    }
//
//    public void setInteger(String key, int value) {
//        mEditor.putInt(key, value).apply();
//    }
//
//    public int getInteger(String key, int defaultValue) {
//        return mPreferences.getInt(key, defaultValue);
//    }
//
//    public void setLong(String key, long value) {
//        mEditor.putLong(key, value).apply();
//    }
//
//    public long getLong(String key, long defaultValue) {
//        return mPreferences.getLong(key, defaultValue);
//    }
//
//    public void setBoolean(String key, boolean value) {
//        mEditor.putBoolean(key, value).apply();
//    }
//
//    public boolean getBoolean(String key) {
//        return mPreferences.getBoolean(key, false);
//    }
//
//    public boolean getBoolean(String key, Boolean defaultVal) {
//        return mPreferences.getBoolean(key, defaultVal);
//    }
//
//    public void clearSession() {
//        mPreferences.edit().clear().apply();
//    }
}