package com.peacecorps.malaria.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencesHelper implements PreferencesHelper{

    private static final String PREF_KEY_USER_PREFERENCES = "HAS_USER_SET_PREFERENCES";

    private final SharedPreferences mPrefs;

    public AppPreferencesHelper(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
    @Override
    public boolean hasUserSetPreferences() {
        return mPrefs.getBoolean(PREF_KEY_USER_PREFERENCES,false);
    }

    @Override
    public void setUserPreferences(boolean value) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_PREFERENCES,value).apply();
    }
}
