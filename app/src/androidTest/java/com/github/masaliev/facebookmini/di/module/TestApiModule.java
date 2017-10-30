package com.github.masaliev.facebookmini.di.module;

import com.github.masaliev.facebookmini.MockHelper;
import com.github.masaliev.facebookmini.data.remote.api.SessionApi;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mbt on 10/25/17.
 */

@Module
public class TestApiModule {
    @Provides
    @Singleton
    SessionApi provideSessionApi(){
        return mock(SessionApi.class);
    }

    @Provides
    @Singleton
    SessionRepository provideSessionRepository(){
        SessionRepository repository = mock(SessionRepository.class);
        when(repository.login(anyString(), anyString())).thenReturn(Observable.just(MockHelper.getUser()));
        return repository;
    }
}
