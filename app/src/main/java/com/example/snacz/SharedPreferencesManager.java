package com.example.snacz;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_AUTH_TOKEN = "authToken";
    private static final String KEY_USER_PHONE_NUMBER = "phoneNumber";
    private static SharedPreferencesManager instance;
    private final SharedPreferences preferences;

    SharedPreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUsername(String username) {
        preferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public String getPhoneNumber() {
        return preferences.getString(KEY_USER_PHONE_NUMBER, null);
    }

    public void clearAllFields() {
        preferences.edit().clear().apply();
    }


    public void saveAuthToken(String authToken) {
        preferences.edit().putString(KEY_AUTH_TOKEN, authToken).apply();
    }
    public void saveUserName(String userName) {
        preferences.edit().putString(KEY_USERNAME, userName).apply();
    }
    public void savePhoneNumber(String phoneNumber){
        preferences.edit().putString(KEY_USER_PHONE_NUMBER, phoneNumber).apply();
    }
    public String getAuthToken() {
        return preferences.getString(KEY_AUTH_TOKEN, null);
    }
}
