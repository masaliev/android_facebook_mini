package com.github.masaliev.facebookmini.data.remote.repository;

import com.github.masaliev.facebookmini.data.model.User;
import com.github.masaliev.facebookmini.data.remote.api.SessionApi;
import com.github.masaliev.facebookmini.utils.Helper;

import io.reactivex.Observable;

/**
 * Created by mbt on 10/16/17.
 */

public class AppSessionRepository implements SessionRepository {

    private final SessionApi mSessionApi;

    public AppSessionRepository(SessionApi sessionApi) {
        this.mSessionApi = sessionApi;
    }

    @Override
    public Observable<User> login(final String phone, final String password) {

        return Observable.defer(() -> mSessionApi.login(phone, password))
                .retryWhen(Helper::getRetryWhenObservable);
    }

    @Override
    public Observable<User> signUp(String fullName, String phone, String password) {
        return Observable.defer(() -> mSessionApi.signUp(fullName, phone, password))
                .retryWhen(Helper::getRetryWhenObservable);
    }
}
