package com.github.masaliev.facebookmini;


import com.github.masaliev.facebookmini.di.component.AppComponent;
import com.github.masaliev.facebookmini.di.component.DaggerTestAppComponent;

/**
 * Created by mbt on 10/24/17.
 */

public class TestApp extends App {
    @Override
    protected AppComponent prepareAppComponent() {
        return DaggerTestAppComponent.builder().build();
    }
}
