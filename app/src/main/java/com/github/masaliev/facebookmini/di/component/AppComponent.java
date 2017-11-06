package com.github.masaliev.facebookmini.di.component;

import com.github.masaliev.facebookmini.App;
import com.github.masaliev.facebookmini.di.module.ApiModule;
import com.github.masaliev.facebookmini.di.module.AppModule;
import com.github.masaliev.facebookmini.di.module.NetworkModule;
import com.github.masaliev.facebookmini.ui.login.LoginActivity;
import com.github.masaliev.facebookmini.ui.login.LoginActivityModule;
import com.github.masaliev.facebookmini.ui.signup.SignupActivity;
import com.github.masaliev.facebookmini.ui.signup.SignupActivityModule;
import com.github.masaliev.facebookmini.ui.upload_photo.UploadPhotoActivity;
import com.github.masaliev.facebookmini.ui.upload_photo.UploadPhotoActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mbt on 10/16/17.
 */
@Singleton
//@Component(modules = {AndroidInjectionModule.class, AppModule.class, ApiModule.class, ActivityBuilder.class})
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ApiModule.class,
        LoginActivityModule.class,
        SignupActivityModule.class,
        UploadPhotoActivityModule.class,
})
public interface AppComponent {

    void inject(LoginActivity activity);
    void inject(SignupActivity activity);
    void inject(UploadPhotoActivity activity);

}
