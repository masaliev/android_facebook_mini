package com.github.masaliev.facebookmini.ui.login;

import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.ui.base.BaseViewModel;
import com.github.masaliev.facebookmini.utils.Constants;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

/**
 * Created by mbt on 10/16/17.
 */

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private SessionRepository mSessionRepository;
    private PreferencesHelper mPreferencesHelper;

    public LoginViewModel(SchedulerProvider mSchedulerProvider, PreferencesHelper preferencesHelper, SessionRepository sessionRepository) {
        super(mSchedulerProvider);
        this.mPreferencesHelper = preferencesHelper;
        this.mSessionRepository = sessionRepository;
    }

    public void onClickLogin(){
        getNavigator().login();
    }

    public void onClickSignUp(){
        getNavigator().openSignUpActivity();
    }

    public void login(String phone, String password){
        getNavigator().showLoading();
        getCompositeDisposable().add(mSessionRepository.login(phone, password)
        .subscribeOn(getSchedulerProvider().io())
        .observeOn(getSchedulerProvider().ui())
        .subscribe(user -> {
            getNavigator().hideLoading();
            mPreferencesHelper.setCurrentUserToken(user.token);
            getNavigator().openMainActivity();
        }, throwable -> {
            getNavigator().hideLoading();
            getNavigator().handleError(throwable);
        }));
    }

    public boolean isPhoneAndPasswordValid(String phone, String password){
        if(phone == null || phone.length() != 12 ){
            return false;
        }

        if(password == null || password.length() < Constants.MIN_PASSWORD_LENGTH){
            return false;
        }
        return true;
    }

}
