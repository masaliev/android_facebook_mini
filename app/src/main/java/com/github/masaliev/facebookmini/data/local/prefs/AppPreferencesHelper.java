package com.github.masaliev.facebookmini.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.github.masaliev.facebookmini.di.PreferenceInfo;

import javax.inject.Inject;

/**
 * Created by mbt on 10/16/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    public static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        this.mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public @Nullable String getCurrentUserToken() {
        return mPrefs.getString(PREF_KEY_TOKEN, null);
    }

    @Override
    public void setCurrentUserToken(String token) {
        mPrefs.edit().putString(PREF_KEY_TOKEN, token).apply();
    }


}
