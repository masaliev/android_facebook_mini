package com.github.masaliev.facebookmini.di.component;

import android.app.Application;

import com.github.masaliev.facebookmini.di.builder.ActivityBuilder;
import com.github.masaliev.facebookmini.di.module.AppModule;
import com.github.masaliev.facebookmini.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by mbt on 10/16/17.
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class})
public interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);

}
