package com.github.masaliev.facebookmini;

import android.app.Application;
import android.content.Context;

import com.github.masaliev.facebookmini.di.component.AppComponent;
import com.github.masaliev.facebookmini.di.component.DaggerAppComponent;
import com.github.masaliev.facebookmini.di.module.AppModule;
import com.github.masaliev.facebookmini.di.module.NetworkModule;

/**
 * Created by mbt on 10/13/17.
 */

public class App extends Application{

    private AppComponent mComponent;

    public static App get(Context context){
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = prepareAppComponent();
    }

    protected AppComponent prepareAppComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(getString(R.string.api_url)))
                .build();
    }

    public AppComponent getComponent(){
        return mComponent;
    }
}
