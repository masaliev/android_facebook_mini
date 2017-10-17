package com.github.masaliev.facebookmini.ui.login;

/**
 * Created by mbt on 10/16/17.
 */

public interface LoginNavigator {

    void openMainActivity();
    void openSignUpActivity();
    void login();
    void showLoading();
    void hideLoading();
    void handleError(Throwable throwable);
}
