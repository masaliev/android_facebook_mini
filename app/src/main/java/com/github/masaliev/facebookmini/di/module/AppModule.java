package com.github.masaliev.facebookmini.di.module;

import android.app.Application;
import android.content.Context;

import com.github.masaliev.facebookmini.data.local.prefs.AppPreferencesHelper;
import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.di.PreferenceInfo;
import com.github.masaliev.facebookmini.utils.Constants;
import com.github.masaliev.facebookmini.utils.rx.AppSchedulerProvider;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mbt on 10/16/17.
 */

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return mApplication;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return Constants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper){
        return appPreferencesHelper;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider(){
        return new AppSchedulerProvider();
    }
}
