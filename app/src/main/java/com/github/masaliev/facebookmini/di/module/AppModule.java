package com.github.masaliev.facebookmini.di.module;

import android.app.Application;
import android.content.Context;

import com.github.masaliev.facebookmini.BuildConfig;
import com.github.masaliev.facebookmini.R;
import com.github.masaliev.facebookmini.data.local.prefs.AppPreferencesHelper;
import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.data.remote.SessionInterceptor;
import com.github.masaliev.facebookmini.data.remote.api.SessionApi;
import com.github.masaliev.facebookmini.data.remote.repository.AppSessionRepository;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.di.ApiInfo;
import com.github.masaliev.facebookmini.di.PreferenceInfo;
import com.github.masaliev.facebookmini.utils.Constants;
import com.github.masaliev.facebookmini.utils.rx.AppSchedulerProvider;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mbt on 10/16/17.
 */

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application){
        return application;
    }

    @Provides
    @ApiInfo
    String provideApiUrl(Context context){
        return context.getString(R.string.api_url);
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
    HttpLoggingInterceptor provideLoggingInterceptor(){
        return new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    }

    @Provides
    Gson provideGson(){
        return new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor, SessionInterceptor sessionInterceptor){
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(sessionInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson, @ApiInfo String apiUrl){
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(apiUrl)
                .build();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider(){
        return new AppSchedulerProvider();
    }
}
