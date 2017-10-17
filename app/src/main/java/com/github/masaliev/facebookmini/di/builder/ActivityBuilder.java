package com.github.masaliev.facebookmini.di.builder;

import com.github.masaliev.facebookmini.ui.login.LoginActivity;
import com.github.masaliev.facebookmini.ui.login.LoginActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mbt on 10/16/17.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity bindLoginActivity();

}
