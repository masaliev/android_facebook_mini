package com.github.masaliev.facebookmini.data.remote.repository;

import android.support.annotation.Nullable;

import com.github.masaliev.facebookmini.data.model.User;
import com.github.masaliev.facebookmini.data.remote.api.SessionApi;
import com.github.masaliev.facebookmini.utils.Helper;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    @Override
    public Observable<User> uploadPicture(File file, @Nullable String mediaType) {

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        mediaType == null ? null : MediaType.parse(mediaType),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        return Observable.defer(() -> mSessionApi.uploadPicture(body))
                .retryWhen(Helper::getRetryWhenObservable);
    }
}
