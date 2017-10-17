package com.github.masaliev.facebookmini.ui.signup;

import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mbt on 10/17/17.
 */

@Module
public class SignupActivityModule  {
    @Provides
    SignupViewModel provideSignupViewModel(SchedulerProvider schedulerProvider,
                                           PreferencesHelper preferencesHelper,
                                           SessionRepository sessionRepository){
        return new SignupViewModel(schedulerProvider, preferencesHelper, sessionRepository);
    }
}
