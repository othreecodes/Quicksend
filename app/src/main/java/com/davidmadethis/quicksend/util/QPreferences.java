package com.davidmadethis.quicksend.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 5/7/17.
 */

public class QPreferences {

    // Shared Preferences reference
    private SharedPreferences pref;
    // Editor reference for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREFER_NAME = "quicksend";
    //sharedpref Show Tip key

    private String CV_LOCATION = "cv_location";
    private String NAME = "name";
    private String EMAIL = "email";

    public QPreferences(Context _context){
        this._context=_context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    public String getCV_LOCATION() {
        return pref.getString(CV_LOCATION,null);
    }

    public void setCV_LOCATION(String string) {
        editor.putString(CV_LOCATION, string);
        editor.commit();
        editor.apply();
    }

    public String getNAME() {
        return pref.getString(NAME,null);
    }

    public void setNAME(String string) {
        editor.putString(NAME, string);
        editor.commit();
        editor.apply();
    }

    public String getEMAIL() {
        return pref.getString(EMAIL,null);
    }

    public void setEMAIL(String string) {
        editor.putString(EMAIL, string);
        editor.commit();
        editor.apply();
    }
}
