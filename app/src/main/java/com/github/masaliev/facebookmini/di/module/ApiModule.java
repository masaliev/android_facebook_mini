package com.github.masaliev.facebookmini.di.module;

import com.github.masaliev.facebookmini.data.remote.api.SessionApi;
import com.github.masaliev.facebookmini.data.remote.repository.AppSessionRepository;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by mbt on 10/17/17.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    SessionApi provideSessionApi(Retrofit retrofit){
        return retrofit.create(SessionApi.class);
    }

    @Provides
    @Singleton
    SessionRepository provideSessionRepository(SessionApi sessionApi){
        return new AppSessionRepository(sessionApi);
    }
}
