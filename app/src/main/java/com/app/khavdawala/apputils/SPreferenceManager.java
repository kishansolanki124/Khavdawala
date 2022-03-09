package com.app.khavdawala.apputils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.khavdawala.pojo.response.ProductListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.app.patidarsaurabh.apputils.AppConstants;

public class SPreferenceManager {

    private static final String PREF_NAME = "khavda";
    private static SPreferenceManager mInstance;
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;
    private final Gson mGson;

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

    public void saveSession(String mobileNumber) {
        setBoolean(AppConstants.IS_LOGIN, true);
        setString(AppConstants.MOBILE, mobileNumber);
    }

    public String getSession() {
        return mPreferences.getString(AppConstants.MOBILE, "");
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value).apply();
    }

    public String getString(String key, String value) {
        return mPreferences.getString(key, value);
    }
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
    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).apply();
    }
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

    public <T> void putList(String key, List<T> list) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, mGson.toJson(list));
        editor.apply();
    }

    public <T> ArrayList<T> getList(String key, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return mGson.fromJson(mPreferences.getString(key, null), typeOfT);
    }
}