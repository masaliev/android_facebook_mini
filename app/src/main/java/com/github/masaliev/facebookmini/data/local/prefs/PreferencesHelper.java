package com.github.masaliev.facebookmini.data.local.prefs;

/**
 * Created by mbt on 10/16/17.
 */

public interface PreferencesHelper {
    String getCurrentUserToken();
    void setCurrentUserToken(String token);
}
