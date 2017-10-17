package com.github.masaliev.facebookmini.ui.signup;

import android.databinding.ObservableBoolean;

import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.ui.base.BaseViewModel;
import com.github.masaliev.facebookmini.utils.Constants;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

/**
 * Created by mbt on 10/17/17.
 */

public class SignupViewModel extends BaseViewModel<SignupNavigator> {

    private SessionRepository mSessionRepository;
    private PreferencesHelper mPreferencesHelper;

    private final ObservableBoolean isSignUpButtonEnabled = new ObservableBoolean(false);

    public SignupViewModel(SchedulerProvider mSchedulerProvider, PreferencesHelper preferencesHelper,
                           SessionRepository sessionRepository) {
        super(mSchedulerProvider);
        this.mPreferencesHelper = preferencesHelper;
        this.mSessionRepository = sessionRepository;
    }

    public void onClickSignUp(){
        getNavigator().signup();
    }

    public void signup(String fullName, String phone, String password){
        getNavigator().showProgress();
        getCompositeDisposable().add(mSessionRepository.signUp(fullName, phone, password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(user -> {
                    getNavigator().hideProgress();
                    mPreferencesHelper.setCurrentUserToken(user.token);
                    getNavigator().openUploadPhotoActivity();
                }, throwable -> {
                    getNavigator().hideProgress();
                    getNavigator().handleError(throwable);
                })
        );
    }

    public void setSignUpButtonEnabled(boolean isEnabled){
        isSignUpButtonEnabled.set(isEnabled);
    }

    public ObservableBoolean isSignUpButtonEnabled(){
        return isSignUpButtonEnabled;
    }

    public boolean isValid(String fullName, String phone, String password, String repeatPassword){
        if(fullName == null || fullName.isEmpty() ){
            return false;
        }

        if(phone == null || phone.length() != 12 ){
            return false;
        }

        if(password == null || password.length() < Constants.MIN_PASSWORD_LENGTH
                || repeatPassword == null || !password.equals(repeatPassword)){
            return false;
        }
        return true;
    }
}
