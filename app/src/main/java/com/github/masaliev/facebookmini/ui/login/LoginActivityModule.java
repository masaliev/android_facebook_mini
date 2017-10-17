package com.github.masaliev.facebookmini.ui.login;

import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mbt on 10/16/17.
 */

@Module
public class LoginActivityModule {
    @Provides
    LoginViewModel provideLoginViewModel(SchedulerProvider schedulerProvider,
                                         PreferencesHelper preferencesHelper,
                                         SessionRepository sessionRepository){
        return new LoginViewModel(schedulerProvider, preferencesHelper, sessionRepository);
    }
}
