package com.github.masaliev.facebookmini.di.module;

import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static org.mockito.Mockito.mock;

/**
 * Created by mbt on 10/25/17.
 */

@Module
public class TestAppModule {

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper() {
        return mock(PreferencesHelper.class);
    }

    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider() {
            @Override
            public Scheduler ui() {
                return AndroidSchedulers.mainThread();
            }

            @Override
            public Scheduler computation() {
                return AndroidSchedulers.mainThread();
            }

            @Override
            public Scheduler io() {
                return AndroidSchedulers.mainThread();
            }
        };
    }

}
