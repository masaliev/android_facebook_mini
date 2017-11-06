package com.github.masaliev.facebookmini.di.module;

import android.content.Context;

import com.github.masaliev.facebookmini.BuildConfig;
import com.github.masaliev.facebookmini.data.remote.SessionInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mbt on 10/24/17.
 */

@Module
public class NetworkModule {

    private final String mApiUrl;

    public NetworkModule(String apiUrl) {
        this.mApiUrl = apiUrl;
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
    Retrofit provideRetrofit(OkHttpClient client, Gson gson){
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mApiUrl)
                .build();
    }

}
