package com.companysf.filmbilet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private SharedPreferences pref;

    private static final String PREF_NAME = "customerLogin";
    private static final String IS_LOGGEDIN_KEY = "isLoggedIn";

    public SessionManager (Context context){
        pref = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void setLogin(boolean isLoggedIn){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_LOGGEDIN_KEY, isLoggedIn);
        editor.apply();
        Log.d(SessionManager.class.getSimpleName(), "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGEDIN_KEY, false);
    }
}
