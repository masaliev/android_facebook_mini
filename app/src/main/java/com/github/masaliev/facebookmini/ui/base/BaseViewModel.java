package com.github.masaliev.facebookmini.ui.base;

import android.arch.lifecycle.ViewModel;

import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by mbt on 10/16/17.
 */

public class BaseViewModel<N> extends ViewModel {

    private N mNavigator;

    private final SchedulerProvider mSchedulerProvider;

    public BaseViewModel(SchedulerProvider mSchedulerProvider) {
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private CompositeDisposable mCompositeDisposable;


    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }

    public N getNavigator() {
        return mNavigator;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public void onViewCreated(){
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public void onDestroyView(){
        mCompositeDisposable.dispose();
    }
}
