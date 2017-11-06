package com.github.masaliev.facebookmini.di.component;

import com.github.masaliev.facebookmini.di.module.TestApiModule;
import com.github.masaliev.facebookmini.di.module.TestAppModule;
import com.github.masaliev.facebookmini.ui.login.LoginActivityModule;
import com.github.masaliev.facebookmini.ui.login.LoginActivityTest;
import com.github.masaliev.facebookmini.ui.signup.SignupActivityTestModule;
import com.github.masaliev.facebookmini.ui.upload_photo.UploadPhotoActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mbt on 10/24/17.
 */
@Singleton
@Component(modules = {
        TestAppModule.class,
        TestApiModule.class,
        LoginActivityModule.class,
        SignupActivityTestModule.class,
        UploadPhotoActivityModule.class,
})
public interface TestAppComponent extends AppComponent {
    void inject(LoginActivityTest activityTest);
}