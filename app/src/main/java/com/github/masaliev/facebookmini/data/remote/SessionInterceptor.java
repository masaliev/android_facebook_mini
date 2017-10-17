package com.github.masaliev.facebookmini.data.remote;

import android.support.annotation.NonNull;

import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mbt on 10/16/17.
 */

public class SessionInterceptor implements Interceptor {

    private PreferencesHelper mPreferencesHelper;

    @Inject
    public SessionInterceptor(PreferencesHelper preferencesHelper){
        this.mPreferencesHelper = preferencesHelper;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String token = mPreferencesHelper.getCurrentUserToken();
        if(token != null && token.length() > 0){
            return chain.proceed(request.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build());
        }
        return chain.proceed(request);
    }
}
