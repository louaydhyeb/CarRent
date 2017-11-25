package com.esprit.carrent.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;


/**
 * Created by Lou_g on 19/11/2017.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private RequestQueue mRequestQueue;

    private static Context mCtx;

    private static String SHARED_PREF_NAME = "mysharedpref";
    private static String KEY_USERNAME = "username";
    private static String KEY_USER_EMAIL = "email";
    private static String KEY_USER_ID = "id";


    private SharedPrefManager(Context context) {
        mCtx = context;


    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String email) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);

        editor.apply();

        return true;

    }

    public boolean isLoggedin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME, null) != null) {
            return true;

        }
        return false;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ;
        return true;

    }


}
