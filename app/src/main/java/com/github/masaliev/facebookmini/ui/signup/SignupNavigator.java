package com.github.masaliev.facebookmini.ui.signup;

/**
 * Created by mbt on 10/17/17.
 */

public interface SignupNavigator {
    void signup();
    void showProgress();
    void hideProgress();
    void handleError(Throwable throwable);
    void openUploadPhotoActivity();
}
