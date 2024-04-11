package com.mevy.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginData {
    public static final String APP_NAME = "Moviplex Sukabumi";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_LOGIN = "login";

    SharedPreferences sp;
    SharedPreferences.Editor spEd;

    public LoginData(Context context){
        sp = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        spEd = sp.edit();
    }

    public void saveString(String key, String value) {
        spEd.putString(key, value);
        spEd.apply();
    }

    public void saveBoolean(String key, Boolean value) {
        spEd.putBoolean(key, value);
        spEd.apply();
    }

    public String getName(){
        return sp.getString(KEY_USERNAME, "");
    }

    public String getUserID(){
        return sp.getString(KEY_USERID, "");
    }

    public String getEmail(){
        return sp.getString(KEY_EMAIL, "");
    }

    public Boolean isLogin(){
        return sp.getBoolean(KEY_LOGIN, false);
    }

    public void logout(){
        saveString(KEY_USERNAME, "");
        saveString(KEY_EMAIL, "");
        saveString(KEY_USERID, "");
        saveBoolean(KEY_LOGIN, false);
    }
}
